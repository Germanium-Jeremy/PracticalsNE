package com.utility.billing.reading;

import com.utility.billing.meter.Meter;
import com.utility.billing.meter.MeterRepository;
import com.utility.billing.meter.MeterStatus;
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
class MeterReadingServiceTest {

    @Mock
    private MeterReadingRepository readingRepository;
    @Mock
    private MeterRepository meterRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private MeterReadingService readingService;

    private MeterReadingRequest readingRequest;
    private Meter meter;
    private User user;

    @BeforeEach
    void setUp() {
        readingRequest = new MeterReadingRequest();
        readingRequest.setMeterId(1L);
        readingRequest.setCurrentReading(100.0);
        readingRequest.setMonth(5);
        readingRequest.setYear(2026);

        meter = Meter.builder().id(1L).status(MeterStatus.ACTIVE).meterNumber("M1").build();
        user = User.builder().id(1L).email("op@test.com").fullName("Op").build();

        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("op@test.com");
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void captureReading_ShouldReturnResponse_WhenSuccessful() {
        when(meterRepository.findById(any())).thenReturn(Optional.of(meter));
        when(readingRepository.existsByMeterIdAndMonthAndYear(any(), any(), any())).thenReturn(false);
        when(readingRepository.findLatestReadingByMeterId(any())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        when(readingRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        MeterReadingResponse response = readingService.captureReading(readingRequest);

        assertNotNull(response);
        assertEquals(100.0, response.getCurrentReading());
    }

    @Test
    void captureReading_ShouldThrowException_WhenMeterInactive() {
        meter.setStatus(MeterStatus.INACTIVE);
        when(meterRepository.findById(any())).thenReturn(Optional.of(meter));

        assertThrows(RuntimeException.class, () -> readingService.captureReading(readingRequest));
    }
}
