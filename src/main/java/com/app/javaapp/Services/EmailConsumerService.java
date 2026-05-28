package com.app.javaapp.Services;

import com.app.javaapp.Models.EmailMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailConsumerService {

    @Autowired
    private JavaMailSender mailSender;

    @RabbitListener(queues = "${rabbitmq.queue.email.name}")
    public void receiveEmail(EmailMessage email) {
        try {
            System.out.println("Received email job for: " + email.getTo());

            // Simple email message for this example
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email.getTo());
            message.setSubject(email.getSubject());
            message.setText(email.getBody());

            mailSender.send(message);
            System.out.println("Email sent successfully to: " + email.getTo());
        } catch (Exception e) {
            System.err.println("Failed to send email to: " + email.getTo());
            e.printStackTrace();
        }
    }
}