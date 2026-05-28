package com.app.javaapp.Services;

import com.app.javaapp.Models.EmailMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailConsumerService {


    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @RabbitListener(queues = "${rabbitmq.queue.email.name}")
    public void receiveEmail(EmailMessage email) {
        try {
            System.out.println("Received email job for: " + email.getTo());

            // Prepare Thymeleaf context and render template
            Context context = new Context();
            // You can add more variables as needed
            context.setVariable("username", email.getTo().split("@")[0]);

            String htmlContent = templateEngine.process("welcome-email.html", context);

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(email.getTo());
            helper.setSubject(email.getSubject());
            helper.setText(htmlContent, true); // true = isHtml

            mailSender.send(mimeMessage);
            System.out.println("HTML Email sent successfully to: " + email.getTo());
        } catch (Exception e) {
            System.err.println("Failed to send HTML email to: " + email.getTo());
            e.printStackTrace();
        }
    }
}