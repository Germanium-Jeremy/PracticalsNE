package com.utility.billing.payment;

import com.utility.billing.billing.Bill;
import com.utility.billing.billing.BillRepository;
import com.utility.billing.billing.BillStatus;
import com.utility.billing.user.User;
import com.utility.billing.user.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BillRepository billRepository;
    private final UserRepository userRepository;

    public PaymentService(PaymentRepository paymentRepository, BillRepository billRepository, UserRepository userRepository) {
        this.paymentRepository = paymentRepository;
        this.billRepository = billRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Payment processPayment(Long billId, Double amount, PaymentMethod method) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new RuntimeException("Bill not found"));

        if (bill.getStatus() == BillStatus.PENDING) {
            throw new RuntimeException("Cannot pay a PENDING bill. It must be APPROVED first.");
        }

        if (bill.getStatus() == BillStatus.PAID) {
            throw new RuntimeException("Bill is already fully PAID.");
        }

        if (amount > bill.getBalance()) {
            throw new RuntimeException("Overpayment not allowed. Balance is " + bill.getBalance());
        }

        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User recorder = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));

        Payment payment = Payment.builder()
                .bill(bill)
                .amountPaid(amount)
                .paymentMethod(method)
                .paymentDate(LocalDateTime.now())
                .recordedBy(recorder)
                .build();

        // Update bill
        bill.setPaidAmount(bill.getPaidAmount() + amount);
        bill.setBalance(bill.getTotalAmount() - bill.getPaidAmount());

        if (bill.getBalance() == 0) {
            bill.setStatus(BillStatus.PAID);
        } else {
            bill.setStatus(BillStatus.PARTIALLY_PAID);
        }

        billRepository.save(bill);
        return paymentRepository.save(payment);
    }
}
