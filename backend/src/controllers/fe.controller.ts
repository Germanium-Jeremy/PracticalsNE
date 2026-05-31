import type { Request, Response } from "express";
import { service_getAllExtinguishers, service_addExtinguisher, service_buyExtinguisher, service_returnAndAutoAssignExtinguisher } from "../services/fe.service.js";

export async function controller_getAllExtinguishers(req: Request, res: Response) {
    try {
        const data = await service_getAllExtinguishers();
        res.status(200).json(data);
    } catch (err: any) {
        res.status(400).json({ error: err.message });
    }
}

export async function controller_addExtinguisher(req: Request, res: Response) {
    try {
        await service_addExtinguisher();
        res.status(201).json({ message: "Extinguisher added" });
    } catch (err: any) {
        res.status(400).json({ error: err.message });
    }
}

export async function controller_buyExtinguisher(req: Request, res: Response) {
    try {
        const { extinguisherId, userId } = req.body;
        await service_buyExtinguisher(extinguisherId, userId);
        res.status(200).json({ message: "Extinguisher bought" });
    } catch (err: any) {
        res.status(400).json({ error: err.message });
    }
}

export async function controller_returnExtinguisher(req: Request, res: Response) {
    try {
        const { extinguisherId, userId } = req.body;
        const result = await service_returnAndAutoAssignExtinguisher(extinguisherId, userId);
        res.status(200).json(result);
    } catch (err: any) {
        res.status(400).json({ error: err.message });
    }
}