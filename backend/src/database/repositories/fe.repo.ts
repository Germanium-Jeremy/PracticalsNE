import type { FireExtinguisher } from "../../models/f-extenguisher.model.js";
import db from "../database.js";
// All date strings must be in 'YYYY-MM-DD HH:MM:SS' UTC format (see utils/date.ts)

export const addFireExtinguisher = async (): Promise<number> => {
    return new Promise((resolve, reject) => {
        db.run(`INSERT INTO fire_extinguisher (bought_at, returned_at, user_id) VALUES (?, ?, ?)`, [null, null, null], function(err) {
            if (err) {
                reject(err);
            } else {
                resolve(this.lastID);
            }
        })
    })
};

// Assign extinguisher to user (buy)
export const buyExtinguisher = async (extinguisherId: number, userId: number, boughtAt: string, expiresAt: string): Promise<number> => {
    return new Promise((resolve, reject) => {
        // boughtAt and expiresAt must be UTC 'YYYY-MM-DD HH:MM:SS'
        db.run(`UPDATE fire_extinguisher SET user_id = ?, bought_at = ?, status = 'sold', expires_at = ? WHERE id = ? AND status = 'in_stock'`, [userId, boughtAt, expiresAt, extinguisherId], function(err) {
            if (err) {
                reject(err);
            } else {
                resolve(this.changes);
            }
        })
    })
};

// Return extinguisher
export const returnExtinguisher = async (extinguisherId: number, returnedAt: string): Promise<number> => {
    return new Promise((resolve, reject) => {
        // returnedAt must be UTC 'YYYY-MM-DD HH:MM:SS'
        db.run(`UPDATE fire_extinguisher SET returned_at = ?, status = 'returned' WHERE id = ?`, [returnedAt, extinguisherId], function(err) {
            if (err) {
                reject(err);
            } else {
                resolve(this.changes);
            }
        })
    })
};

// Get extinguishers about to expire in X seconds
export const getExpiringExtinguishers = async (seconds: number): Promise<FireExtinguisher[]> => {
    return new Promise((resolve, reject) => {
        // All date comparisons are in UTC
        db.all(`SELECT * FROM fire_extinguisher WHERE status = 'sold' AND expires_at <= datetime('now', 'utc', '+' || ? || ' seconds') AND expires_at > datetime('now', 'utc')`, [seconds], function(err, rows) {
            if (err) {
                console.log('Query failed:', err);
                reject(err);
            } else {
                console.log('Queried expiring extinguishers:', rows);
                resolve(rows as FireExtinguisher[]);
            }
        })
    })
};

// Get extinguishers already expired
export const getExpiredExtinguishers = async (): Promise<FireExtinguisher[]> => {
    return new Promise((resolve, reject) => {
        db.all(`SELECT * FROM fire_extinguisher WHERE status = 'sold' AND expires_at <= datetime('now', 'utc')`, [], function(err, rows) {
            if (err) {
                reject(err);
            } else {
                resolve(rows as FireExtinguisher[]);
            }
        })
    })
};

export const getAllFireExtinguishers = async (): Promise<FireExtinguisher[] | unknown> => {
    return new Promise((resolve, reject) => {
        db.all(`SELECT * FROM fire_extinguisher`, function(err, rows) {
            if (err) {
                reject(err);
            } else {
                resolve(rows);
            }
        })
    })
};

export const getNumberOfExtenguishers = async (field?: string, value?: string): Promise<number> => {
    let query = `SELECT COUNT(*) FROM fire_extinguisher`
    if (field) query =  `SELECT COUNT(*) FROM fire_extinguisher WHERE ${field} = ?`
    return new Promise((resolve, reject) => {
        db.get(query, [value], function(err, row) {
            if (err) {
                reject(err);
            } else {
                resolve(row as number);
            }
        })
    })
};

export const getExtenguisherByStatus = async (status: string): Promise<FireExtinguisher[] | unknown> => {
    return new Promise((resolve, reject) => {
        db.all(`SELECT * FROM fire_extinguisher WHERE status = ?`, [status], function(err, rows) {
            if (err) {
                reject(err);
            } else {
                resolve(rows);
            }
        })
    })
}

export const getExtenguisherByCreatedAt = async (created_at: string): Promise<FireExtinguisher[] | unknown> => {
    return new Promise((resolve, reject) => {
        // created_at must be UTC 'YYYY-MM-DD HH:MM:SS'
        db.all(`SELECT * FROM fire_extinguisher WHERE created_at = ?`, [created_at], function(err, rows) {
            if (err) {
                reject(err);
            } else {
                resolve(rows);
            }
        })
    })
}

export const getExtenguisherByBoughtAt = async (bought_at: string): Promise<FireExtinguisher[] | unknown> => {
    return new Promise((resolve, reject) => {
        // bought_at must be UTC 'YYYY-MM-DD HH:MM:SS'
        db.all(`SELECT * FROM fire_extinguisher WHERE bought_at = ?`, [bought_at], function(err, rows) {
            if (err) {
                reject(err);
            } else {
                resolve(rows);
            }
        })
    })
}

export const getExtenguisherByReturnedAt = async (returned_at: string): Promise<FireExtinguisher[] | unknown> => {
    return new Promise((resolve, reject) => {
        // returned_at must be UTC 'YYYY-MM-DD HH:MM:SS'
        db.all(`SELECT * FROM fire_extinguisher WHERE returned_at = ?`, [returned_at], function(err, rows) {
            if (err) {
                reject(err);
            } else {
                resolve(rows);
            }
        })
    })
}

export const updateExtinguisher = async (id: number, bought_at?: string, returned_at?: string): Promise<number> => { 
    return new Promise((resolve, reject) => {
        const fields = [];
        const values = [];
        
        if (bought_at) {
            fields.push("bought_at = ?");
            values.push(bought_at);
            fields.push("status = ?")
            values.push('sold');
        }
        
        if (returned_at) {
            fields.push("returned_at = ?");
            values.push(returned_at);
            fields.push("status = ?");
            values.push('returned');
        }
        
        db.run(`UPDATE fire_extinguisher SET ${fields.join(", ")} WHERE id = ?`, [...values, id], function(err) {
            if (err) {
                reject(err);
            } else {
                resolve(this.changes);
            }
        })
    })
};