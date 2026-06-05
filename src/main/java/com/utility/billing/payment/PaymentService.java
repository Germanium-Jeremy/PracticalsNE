package com.utility.billing.payment;

import com.utility.billing.billing.Bill;
import com.utility.billing.billing.BillRepository;
import com.utility.billing.billing.BillStatus;
import com.utility.billing.notification.NotificationService;
import com.utility.billing.user.User;
import com.utility.billing.user.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BillRepository billRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public PaymentService(
            PaymentRepository paymentRepository, 
            BillRepository billRepository, 
            UserRepository userRepository,
            NotificationService notificationService
    ) {
        this.paymentRepository = paymentRepository;
        this.billRepository = billRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    @Transactional
    public PaymentResponse processPayment(PaymentRequest request) {
        Bill bill = billRepository.findById(request.getBillId())
                .orElseThrow(() -> new RuntimeException("Bill not found"));

        if (bill.getStatus() == BillStatus.PENDING) {
            throw new RuntimeException("Cannot pay a PENDING bill. It must be APPROVED first.");
        }

        if (bill.getStatus() == BillStatus.PAID) {
            throw new RuntimeException("Bill is already fully PAID.");
        }

        if (request.getAmount() > bill.getBalance()) {
            throw new RuntimeException("Overpayment not allowed. Balance is " + bill.getBalance());
        }

        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User recorder = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));

        Payment payment = Payment.builder()
                .bill(bill)
                .amountPaid(request.getAmount())
                .paymentMethod(request.getMethod())
                .paymentDate(LocalDateTime.now())
                .recordedBy(recorder)
                .build();

        // Update bill
        bill.setPaidAmount(bill.getPaidAmount() + request.getAmount());
        bill.setBalance(bill.getTotalAmount() - bill.getPaidAmount());

        if (bill.getBalance() == 0) {
            bill.setStatus(BillStatus.PAID);
        } else {
            bill.setStatus(BillStatus.PARTIALLY_PAID);
        }

        billRepository.save(bill);
        Payment savedPayment = paymentRepository.save(payment);

        notificationService.sendPaymentNotification(bill.getCustomer(), request.getAmount(), bill.getBillNumber());

        return mapToResponse(savedPayment);
    }

    public List<PaymentResponse> getAllPayments() {
        return paymentRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<PaymentResponse> getCustomerPayments(Long customerId) {
        return paymentRepository.findAll().stream()
                .filter(p -> p.getBill().getCustomer().getId().equals(customerId))
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private PaymentResponse mapToResponse(Payment payment) {
        PaymentResponse response = new PaymentResponse();
        response.setId(payment.getId());
        response.setBillNumber(payment.getBill().getBillNumber());
        response.setAmountPaid(payment.getAmountPaid());
        response.setPaymentMethod(payment.getPaymentMethod());
        response.setPaymentDate(payment.getPaymentDate());
        response.setRecordedBy(payment.getRecordedBy().getFullName());
        return response;
    }
}
