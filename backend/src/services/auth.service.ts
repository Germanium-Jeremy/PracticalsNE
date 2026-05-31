import { createUser, getUserByEmail } from "../database/repositories/user.repo.js";
import jwt from 'jsonwebtoken';
import { comparePassword } from "../utils/password.js";

export async function service_registerUser(username: string, email: string, password: string) {

    const existing = await getUserByEmail(email);

    if (existing) {
        throw new Error("User already exists");
    }

    const userId = await createUser(username, email, password);

    const token = jwt.sign(
        { id: userId, email: email, role: 'user' },
        process.env.JWT_SECRET!,
        { expiresIn: "1h" }
    );

    return { token, userId, username, email, role: 'user' };
}

export async function service_loginUser(email: string, password: string) {
    const user = await getUserByEmail(email);

    if (!user) {
        throw new Error("Invalid credentials");
    }

    const match = await comparePassword(password, user.password);

    if (!match) {
        throw new Error("Invalid credentials");
    }

    const token = jwt.sign(
        { id: user.id, email: user.email, role: user.role },
        process.env.JWT_SECRET!,
        { expiresIn: "1h" }
    );

    return { token, userId: user.id, username: user.username, email: user.email, role: user.role };
}