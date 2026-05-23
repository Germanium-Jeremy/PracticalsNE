import { sendWelcomeEmail } from "../jobs/sendEmail.js";
import { registerUser, loginUser } from "../services/auth.service.js";
import type { Request, Response } from "express";

export async function register(req: Request, res: Response) {
    try {
        const { username, email, password } = req.body;

        const data = await registerUser(username, email, password);

        res.status(201).json(data);

        setImmediate(async () => {
            try {
                await sendWelcomeEmail(data.email, data.username);
            } catch (err) {
                console.error(err);
            }
        });
    } catch (err: any) {
        res.status(400).json({ error: err.message });
    }
}

export async function login(req: Request, res: Response) {
    try {
        const { email, password } = req.body;

        const data = await loginUser(email, password);

        res.status(200).json(data);
    } catch (err: any) {
        res.status(400).json({ error: err.message });
    }
}