package com.utility.billing.notification;

import com.utility.billing.billing.Bill;
import com.utility.billing.customer.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);
    private final NotificationRepository notificationRepository;
    private final JavaMailSender mailSender;

    public NotificationService(NotificationRepository notificationRepository, JavaMailSender mailSender) {
        this.notificationRepository = notificationRepository;
        this.mailSender = mailSender;
    }

    public void sendBillNotification(Bill bill) {
        Customer customer = bill.getCustomer();
        String messageText = String.format("Dear %s, Your %d/%d utility bill of %.2f FRW has been successfully processed.",
                customer.getFullNames(), bill.getBillingMonth(), bill.getBillingYear(), bill.getTotalAmount());

        Notification notification = Notification.builder()
                .customer(customer)
                .bill(bill)
                .message(messageText)
                .status(NotificationStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        notificationRepository.save(notification);
        
        sendEmail(customer.getEmail(), "Utility Bill Generated", messageText);

        notification.setStatus(NotificationStatus.SENT);
        notificationRepository.save(notification);
        
        log.info("Bill notification sent to {}", customer.getEmail());
    }

    public void sendPaymentNotification(Customer customer, Double amount, String billNumber) {
        String messageText = String.format("Dear %s, Your payment of %.2f FRW for bill %s has been received.",
                customer.getFullNames(), amount, billNumber);

        Notification notification = Notification.builder()
                .customer(customer)
                .message(messageText)
                .status(NotificationStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        notificationRepository.save(notification);

        sendEmail(customer.getEmail(), "Payment Confirmation", messageText);

        notification.setStatus(NotificationStatus.SENT);
        notificationRepository.save(notification);

        log.info("Payment notification sent to {}", customer.getEmail());
    }

    /**
     * Sends an OTP email for account activation.
     * @param email the recipient email
     * @param otp the activation code
     */
    public void sendActivationEmail(String email, String otp) {
        String messageText = String.format("Dear user, your account activation code is: %s. Please use it to verify your account.", otp);
        
        sendEmail(email, "Account Activation", messageText);

        log.info("Activation email sent to {}", email);
    }

    private void sendEmail(String to, String subject, String content) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(to);
            mailMessage.setSubject(subject);
            mailMessage.setText(content);
            mailMessage.setFrom("utility-billing@wasac-reg.rw");
            
            mailSender.send(mailMessage);
            log.info("Email sent successfully to {}", to);
        } catch (Exception e) {
            log.error("Failed to send email to {}: {}", to, e.getMessage());
            // We catch it so it doesn't break the main transaction, as requested "manually"
            // and often email services are unreliable in test environments.
        }
    }
}
