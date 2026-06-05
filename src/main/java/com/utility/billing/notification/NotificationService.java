package com.utility.billing.notification;

import com.utility.billing.billing.Bill;
import com.utility.billing.customer.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);
    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public void sendBillNotification(Bill bill) {
        Customer customer = bill.getCustomer();
        String message = String.format("Dear %s, Your %d/%d utility bill of %.2f FRW has been successfully processed.",
                customer.getFullNames(), bill.getBillingMonth(), bill.getBillingYear(), bill.getTotalAmount());

        Notification notification = Notification.builder()
                .customer(customer)
                .bill(bill)
                .message(message)
                .status(NotificationStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        notificationRepository.save(notification);

        // Simulate sending
        log.info("[NOTIFICATION SENT to {}]: {}", customer.getEmail(), message);
        notification.setStatus(NotificationStatus.SENT);
        notificationRepository.save(notification);
    }

    public void sendPaymentNotification(Customer customer, Double amount, String billNumber) {
        String message = String.format("Dear %s, Your payment of %.2f FRW for bill %s has been received.",
                customer.getFullNames(), amount, billNumber);

        Notification notification = Notification.builder()
                .customer(customer)
                .message(message)
                .status(NotificationStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        notificationRepository.save(notification);

        // Simulate sending
        log.info("[NOTIFICATION SENT to {}]: {}", customer.getEmail(), message);
        notification.setStatus(NotificationStatus.SENT);
        notificationRepository.save(notification);
    }
}
