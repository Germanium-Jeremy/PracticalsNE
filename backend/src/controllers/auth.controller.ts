import { service_loginUser, service_registerUser } from "../services/auth.service.js";
import type { Request, Response } from "express";
import { welcomeEmailTemplate } from "../templates/WelcomeEmail.js";
import { sendEmail } from '../jobs/sendEmail.js';

export async function controller_registerUser(req: Request, res: Response) {
    try {
        const { username, email, password } = req.body;

        const data = await service_registerUser(username, email, password);

        res.status(201).json(data);

        setImmediate(async () => {
            try {
                await sendEmail(data.email, 'Welcome to Our App', welcomeEmailTemplate(data.username));
            } catch (err) {
                console.error(err);
            }
        });
    } catch (err: any) {
        res.status(400).json({ error: err.message });
    }
}

export async function controller_loginUser(req: Request, res: Response) {
    try {
        const { email, password } = req.body;

        const data = await service_loginUser(email, password);

        res.status(200).json(data);
    } catch (err: any) {
        res.status(400).json({ error: err.message });
    }
}