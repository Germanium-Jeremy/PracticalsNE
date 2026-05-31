import { createUsersTable } from './migrations/001_create_user_table.js';
import { createFireExtinguisherTable } from './migrations/002_create_fe_table.js';
import { seedAdminUser, addDefaultUsers } from './seeders/populate_data.js';

export function initDatabase() {
    createUsersTable();
    createFireExtinguisherTable()
    seedAdminUser();
    addDefaultUsers();
}