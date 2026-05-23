import jwt from 'jsonwebtoken';

import type { Request, Response, NextFunction } from 'express';

export interface AuthRequest extends Request {
    user?: any;
}

export function authenticateToken(req: AuthRequest, res: Response, next: NextFunction) {
    const JWT_SECRET = process.env.JWT_SECRET as string;
    
    const authHeader = req.headers.authorization;
    
    if (!authHeader) {
        return res.status(401).json({
            error: 'Access token missing'
        });
    }
    
    const token = authHeader.split(' ')[1];
    
    if (!token) {
        return res.status(401).json({
            error: 'Invalid token format'
        });
    }

    try {

        const decoded = jwt.verify(token, JWT_SECRET);

        req.user = decoded;

        next();

    } catch (err) {

        return res.status(403).json({
            error: 'Invalid or expired token'
        });
    }
}