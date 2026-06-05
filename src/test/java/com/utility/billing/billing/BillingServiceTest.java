package com.utility.billing.billing;

import com.utility.billing.customer.Customer;
import com.utility.billing.customer.CustomerStatus;
import com.utility.billing.meter.Meter;
import com.utility.billing.meter.MeterRepository;
import com.utility.billing.meter.MeterType;
import com.utility.billing.reading.MeterReading;
import com.utility.billing.reading.MeterReadingRepository;
import com.utility.billing.tariff.Tariff;
import com.utility.billing.tariff.TariffService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BillingServiceTest {

    @Mock
    private BillRepository billRepository;
    @Mock
    private MeterRepository meterRepository;
    @Mock
    private MeterReadingRepository readingRepository;
    @Mock
    private TariffService tariffService;

    @InjectMocks
    private BillingService billingService;

    private Meter meter;
    private MeterReading reading;
    private Tariff tariff;

    @BeforeEach
    void setUp() {
        Customer customer = Customer.builder().id(1L).status(CustomerStatus.ACTIVE).build();
        meter = Meter.builder().id(1L).meterType(MeterType.WATER).customer(customer).build();
        reading = MeterReading.builder().meter(meter).previousReading(0.0).currentReading(10.0).month(5).year(2026).build();
        tariff = Tariff.builder().rate(100.0).build();
    }

    @Test
    void generateBill_ShouldReturnBill_WhenSuccessful() {
        when(meterRepository.findById(any())).thenReturn(Optional.of(meter));
        when(billRepository.existsByMeterIdAndBillingMonthAndBillingYear(any(), any(), any())).thenReturn(false);
        when(readingRepository.findAll()).thenReturn(Collections.singletonList(reading));
        when(tariffService.getActiveTariff(any())).thenReturn(tariff);
        when(billRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        Bill bill = billingService.generateBill(1L, 5, 2026);

        assertNotNull(bill);
        assertEquals(1000.0, bill.getTariffAmount()); // 10 * 100
        verify(billRepository).save(any());
    }
}
