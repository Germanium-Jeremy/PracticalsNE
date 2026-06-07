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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
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
    
    private org.springframework.mail.javamail.JavaMailSender mailSender;
    
    // Using a manual implementation instead of Mockito.mock for NotificationService to avoid Java 25 issues
    private com.utility.billing.notification.NotificationService notificationService;
    private com.utility.billing.notification.NotificationRepository notificationRepository;

    @InjectMocks
    private PaymentService paymentService;

    private Bill bill;
    private User user;

    @BeforeEach
    void setUp() {
        notificationRepository = mock(com.utility.billing.notification.NotificationRepository.class);
        mailSender = mock(org.springframework.mail.javamail.JavaMailSender.class);
        notificationService = new com.utility.billing.notification.NotificationService(notificationRepository, mailSender);
        paymentService = new PaymentService(paymentRepository, billRepository, userRepository, notificationService);

        com.utility.billing.customer.Customer customer = com.utility.billing.customer.Customer.builder()
                .id(1L)
                .fullNames("John Doe")
                .email("test@example.com")
                .build();

        bill = Bill.builder()
                .id(1L)
                .billNumber("BILL-1")
                .customer(customer)
                .totalAmount(1000.0)
                .paidAmount(0.0)
                .balance(1000.0)
                .status(BillStatus.APPROVED)
                .build();
        user = User.builder().email("fin@test.com").build();

        // Mock SecurityContextHolder without using mock() on interfaces (Java 25 fix)
        final Authentication authentication = new UsernamePasswordAuthenticationToken(
                "fin@test.com", 
                null, 
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_FINANCE"))
        );
        
        SecurityContext securityContext = new SecurityContext() {
            @Override
            public Authentication getAuthentication() { return authentication; }
            @Override
            public void setAuthentication(Authentication authentication) {}
        };
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void processPayment_ShouldReturnPayment_WhenSuccessful() {
        PaymentRequest request = new PaymentRequest();
        request.setBillId(1L);
        request.setAmount(500.0);
        request.setMethod(PaymentMethod.CASH);

        when(billRepository.findById(any())).thenReturn(Optional.of(bill));
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        when(paymentRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        PaymentResponse response = paymentService.processPayment(request);

        assertNotNull(response);
        assertEquals(500.0, bill.getPaidAmount());
        assertEquals(500.0, bill.getBalance());
        assertEquals(BillStatus.PARTIALLY_PAID, bill.getStatus());
        verify(notificationRepository, atLeastOnce()).save(any());
    }

    @Test
    void processPayment_ShouldMarkPaid_WhenBalanceZero() {
        PaymentRequest request = new PaymentRequest();
        request.setBillId(1L);
        request.setAmount(1000.0);
        request.setMethod(PaymentMethod.CASH);

        when(billRepository.findById(any())).thenReturn(Optional.of(bill));
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        when(paymentRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        paymentService.processPayment(request);

        assertEquals(0.0, bill.getBalance());
        assertEquals(BillStatus.PAID, bill.getStatus());
        verify(notificationRepository, atLeastOnce()).save(any());
    }
}
