import type { User } from "../../models/user.model.js";
import { hashPassword } from "../../utils/password.js";
import db from "../database.js";

export const createUser = async (username: string, email: string, password: string, role: string = 'user'): Promise<number> => {
    const hashedPassword = await hashPassword(password);
    return new Promise((resolve, reject) => {
        db.run(`INSERT INTO users (username, email, password, role) VALUES (?, ?, ?, ?)`, [username, email, hashedPassword, role], function(err) {
            if (err) {
                reject(err);
            } else {
                resolve(this.lastID);
            }
        })
    })
};

export const getAllUsers = async (): Promise<User[] | unknown> => {
    return new Promise((resolve, reject) => {
        db.all(`SELECT * FROM users`, function(err, rows) {
            if (err) {
                reject(err);
            } else {
                resolve(rows);
            }
        })
    })
};

export const getUserById = async (id: number): Promise<User | undefined> => {
    return new Promise((resolve, reject) => {
        db.get(`SELECT * FROM users WHERE id = ?`, [id], function(err, row) {
            if (err) {
                reject(err);
            } else {
                resolve(row as User | undefined);
            }
        })
    })
};

export const getUserByEmail = async (email: string): Promise<User | undefined> => {
    return new Promise((resolve, reject) => {
        db.get(`SELECT * FROM users WHERE email = ?`, [email], function(err, row) {
            if (err) {
                reject(err);
            } else {
                resolve(row as User | undefined);
            }
        })
    })
}

export const updateUser = async (id: number, username?: string, email?: string, password?: string): Promise<number> => {
    if (password) {
        password = await hashPassword(password);
    }
    
    return new Promise((resolve, reject) => {
        const fields = [];
        const values = [];
        
        if (username) {
            fields.push("username = ?");
            values.push(username);
        }
        
        if (email) {
            fields.push("email = ?");
            values.push(email);
        }
        
        if (password) {
            fields.push("password = ?");
            values.push(password);
        }

        db.run(`UPDATE users SET ${fields.join(", ")} WHERE id = ?`, [...values, id], function(err) {
            if (err) {
                reject(err);
            } else {
                resolve(this.changes);
            }
        })
    })
};

export const getUserByRole = async (role: string): Promise<User | undefined> => {
    return new Promise((resolve, reject) => {
        db.get(`SELECT * FROM users WHERE role = ?`, [role], function(err, row) {
            if (err) {
                reject(err);
            } else {
                resolve(row as User | undefined);
            }
        })
    })
}