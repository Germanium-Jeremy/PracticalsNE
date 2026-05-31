import db from '../database.js';

export function createFireExtinguisherTable() {
    db.run(`
        CREATE TABLE IF NOT EXISTS fire_extinguisher (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            user_id INTEGER,
            created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
            bought_at DATETIME,
            returned_at DATETIME,
            status TEXT NOT NULL DEFAULT 'in_stock',
            expires_at DATETIME NOT NULL DEFAULT (datetime('now', '+2 minutes')),
            user_notified INTEGER NOT NULL DEFAULT 0,
            admin_notified INTEGER NOT NULL DEFAULT 0,
            FOREIGN KEY (user_id) REFERENCES users(id)
        )
    `, (err) => {
        if (err) {
            console.error('Error creating fire_extinguisher table:', err.message);
        } else {
            console.log('Fire_extinguisher table created or already exists');
        }
    });
}