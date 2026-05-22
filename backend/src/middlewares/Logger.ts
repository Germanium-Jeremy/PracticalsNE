import fs from 'fs';
import path from 'path';
import type { Request, Response, NextFunction } from 'express';
import { fileURLToPath } from 'url';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const logFilePath = path.join(__dirname, '..', '..', 'logs', 'logs.json');

interface LogEntry {
    timestamp: string;
    srcIp: string;
    route: string;
    method: string;
    status: number;
    error: string | null;
}

function saveLog(log: LogEntry) {
    let logs: LogEntry[] = [];

    // Read existing logs
    if (fs.existsSync(logFilePath)) {
        try {
            const fileData = fs.readFileSync(logFilePath, 'utf-8');
            logs = JSON.parse(fileData || '[]');
        } catch {
            logs = [];
        }
    }

    // Add new log
    logs.push(log);

    // Save back
    fs.writeFileSync(logFilePath, JSON.stringify(logs, null, 4));
}

export const Logger = (req: Request, res: Response, next: NextFunction) => {

    const startTime = new Date().toISOString();

    // Run after response is finished
    res.on('finish', () => {

        const log: LogEntry = {
            timestamp: startTime,
            srcIp:
                req.ip ||
                req.socket.remoteAddress ||
                'Unknown',
            route: req.originalUrl,
            method: req.method,
            status: res.statusCode,
            error: res.statusCode >= 400
                ? `HTTP Error ${res.statusCode}`
                : null
        };

        saveLog(log);
    });

    next();
};