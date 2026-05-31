import cron from 'node-cron';
import { getExpiringExtinguishers, getExpiredExtinguishers } from '../database/repositories/fe.repo.js';
import { getUserById } from '../database/repositories/user.repo.js';

import { sendEmail } from './sendEmail.js';
import { notifyExpiry } from '../templates/NotifyExpirely.js';
import { notifyAdminExpiry } from '../templates/AdminExpirely.js';
const ADMIN_EMAIL = process.env.ADMIN_EMAIL || 'golan72331@4nly.com';

// Notify users 1 minute before expiry
cron.schedule('*/30 * * * * *', async () => {
    try {
        const expiring = await getExpiringExtinguishers(60); // 60 seconds
        console.log('Got expiring extinguishers:', expiring.length);
        for (const extinguisher of expiring) {
            if (extinguisher.user_id) {
                const user = await getUserById(extinguisher.user_id);
                if (user && user.email) {
                    const html = notifyExpiry(user.username, extinguisher.id, extinguisher.expires_at);
                    await sendEmail(user.email, 'Your Fire Extinguisher is About to Expire', html);
                    console.log(`Notified user ${user.email} about expiring extinguisher ${extinguisher.id}`);
                }
            }
        }
    } catch (err) {
        console.error('Error in user expiry notification:', err);
    }
});

// Notify admin after expiry
cron.schedule('*/5 * * * * *', async () => {
    console.log('Starting extinguisher notification admin jobs...');
    try {
        const expired = await getExpiredExtinguishers();
        for (const extinguisher of expired) {
            const html = notifyAdminExpiry(extinguisher.user_id, extinguisher.id, extinguisher.expires_at);
            await sendEmail(ADMIN_EMAIL, 'Fire Extinguisher Expired', html);
            console.log(`Notified admin about expired extinguisher ${extinguisher.id}`);
        }
    } catch (err) {
        console.error('Error in admin expiry notification:', err);
    }
});