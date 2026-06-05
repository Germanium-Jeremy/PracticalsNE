package com.utility.billing.payment;

import com.utility.billing.billing.Bill;
import com.utility.billing.billing.BillRepository;
import com.utility.billing.billing.BillStatus;
import com.utility.billing.user.User;
import com.utility.billing.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private BillRepository billRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PaymentService paymentService;

    private Bill bill;
    private User user;

    @BeforeEach
    void setUp() {
        bill = Bill.builder()
                .id(1L)
                .totalAmount(1000.0)
                .paidAmount(0.0)
                .balance(1000.0)
                .status(BillStatus.APPROVED)
                .build();
        user = User.builder().email("fin@test.com").build();

        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("fin@test.com");
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void processPayment_ShouldReturnPayment_WhenSuccessful() {
        when(billRepository.findById(any())).thenReturn(Optional.of(bill));
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        when(paymentRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        Payment payment = paymentService.processPayment(1L, 500.0, PaymentMethod.CASH);

        assertNotNull(payment);
        assertEquals(500.0, bill.getPaidAmount());
        assertEquals(500.0, bill.getBalance());
        assertEquals(BillStatus.PARTIALLY_PAID, bill.getStatus());
    }

    @Test
    void processPayment_ShouldMarkPaid_WhenBalanceZero() {
        when(billRepository.findById(any())).thenReturn(Optional.of(bill));
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        when(paymentRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        paymentService.processPayment(1L, 1000.0, PaymentMethod.CASH);

        assertEquals(0.0, bill.getBalance());
        assertEquals(BillStatus.PAID, bill.getStatus());
    }
}
