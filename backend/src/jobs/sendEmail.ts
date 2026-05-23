import nodemailer from 'nodemailer';

import { welcomeEmailTemplate } from '../templates/WelcomeEmail.js';

export async function sendWelcomeEmail(email: string, username: string) {

    const html = welcomeEmailTemplate(username);
    const transporter = nodemailer.createTransport({
        service: 'gmail',

        auth: {
            user: process.env.EMAIL_USER,
            pass: process.env.EMAIL_PASS
        }
    });

    await transporter.sendMail({
        from: process.env.EMAIL_USER,
        to: email,
        subject: 'Welcome to our platform',
        html
    });

    console.log(`Welcome email sent to ${email}`);
}