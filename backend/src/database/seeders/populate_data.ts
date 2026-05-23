import db from '../database.js';
import { createUser, getUserByRole } from '../repositories/user.repo.js';

export function seedAdminUser() {
    const admin = {
        username: 'admin',
        email: 'admin@example.com',
        password: 'admin123' // later hash this
    };

    getUserByRole('admin').then((existingAdmin) => {
        if (existingAdmin) {
            console.log('Admin user already exists');
        } else {
            createUser(admin.username, admin.email, admin.password, 'admin')
                .then(() => console.log('Admin user created'))
                .catch(err => console.error('Error creating admin user:', err.message));
        }
    }).catch(err => console.error('Error checking for existing admin user:', err.message));
}

export function addDefaultUsers() {
    const users = [
        { username: 'user1', email: 'user1@email.com', password: 'password1' },
        { username: 'user2', email: 'user2@email.com', password: 'password2' }
    ]

    users.forEach(user => {
        createUser(user.username, user.email, user.password, 'user')
            .then(() => console.log(`User ${user.username} created`))
            .catch(err => console.error(`Error creating user ${user.username}:`, err.message));
    });
}