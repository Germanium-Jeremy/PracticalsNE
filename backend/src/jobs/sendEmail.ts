import nodemailer from 'nodemailer';

// Configure transporter once

/**
 * Send an email with the given subject and HTML content
 * @param email recipient email
 * @param subject email subject
 * @param html email HTML content
*/
export async function sendEmail(email: string, subject: string, html: string) {
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
        subject,
        html
    });
    console.log(`Email sent to ${email} with subject: ${subject}`);
}