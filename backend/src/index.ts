import express from 'express';
import type { Request, Response } from 'express';
import cors from 'cors';
import dotenv from 'dotenv';
import { Logger } from './middlewares/Logger.js';
import { initDatabase } from './database/init.js';
import authRoutes from './routes/auth.route.js';
import userRoutes from './routes/user.route.js';
import feRoutes from './routes/fe.route.js'
import './jobs/extinguisherNotifications.js';

dotenv.config();

const app = express();
const { PORT } = process.env;


// Middleware configurations
app.use(Logger);
app.use(cors({
    origin: 'http://localhost:5173',
    methods: ['GET', 'POST', 'PUT', 'DELETE'],
    allowedHeaders: ['Content-Type', 'Authorization']
}));
app.use(express.json());


// Routes
app.get('/', (req: Request, res: Response) => {
    res.status(200).send('Hello, TypeScript Express!');
});

app.get('/error', (req, res) => {
    res.status(500).send('Internal Server Error');
});

app.use('/api/auth', authRoutes);
app.use('/api/profile', userRoutes);
app.use('/api/extenguisher', feRoutes);

app.use((req, res) => {
    res.status(404).json({ error: 'Not Found' });
});


// Database
initDatabase();

// Server
app.listen(PORT, () => {
    console.log(`Server running at ${process.env.backend_url}:${PORT}`);
});
