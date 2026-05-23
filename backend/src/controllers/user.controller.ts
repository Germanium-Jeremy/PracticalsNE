import type { Request, Response } from "express";
import { getAUser, getUsers } from "../services/user.service.js";

export async function allUsers(req: Request, res: Response) {
    try {
        const data = await getUsers();
        res.status(200).json(data);
    } catch (err: any) {
        res.status(400).json({ error: err.message });
    }
}

export async function returnASingleUser(req: Request, res: Response) {
    try {
        const { field, value } = req.query;

        if (!field || !value) {
            return res.status(400).json({ error: "Field and value are required" });
        }

        const data = await getAUser(field as string, value as string);
        if (!data) {
            return res.status(400).json({ error: "User not found" });
        }

        res.status(200).json(data);
    } catch (err: any) {
        res.status(400).json({ error: err.message });
    }
}