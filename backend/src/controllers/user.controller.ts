import type { Request, Response } from "express";
import { service_getAUser, service_getUsers } from "../services/user.service.js";

export async function controller_getUsers(req: Request, res: Response) {
    try {
        const data = await service_getUsers();
        res.status(200).json(data);
    } catch (err: any) {
        res.status(400).json({ error: err.message });
    }
}

export async function controller_getAUser(req: Request, res: Response) {
    try {
        const { field, value } = req.query;

        if (!field || !value) {
            return res.status(400).json({ error: "Field and value are required" });
        }

        const data = await service_getAUser(field as string, value as string);
        if (!data) {
            return res.status(400).json({ error: "User not found" });
        }

        res.status(200).json(data);
    } catch (err: any) {
        res.status(400).json({ error: err.message });
    }
}