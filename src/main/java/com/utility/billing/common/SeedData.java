package com.utility.billing.common;

import com.utility.billing.billing.Bill;
import com.utility.billing.billing.BillRepository;
import com.utility.billing.billing.BillStatus;
import com.utility.billing.customer.Customer;
import com.utility.billing.customer.CustomerRepository;
import com.utility.billing.customer.CustomerStatus;
import com.utility.billing.meter.Meter;
import com.utility.billing.meter.MeterRepository;
import com.utility.billing.meter.MeterStatus;
import com.utility.billing.meter.MeterType;
import com.utility.billing.reading.MeterReading;
import com.utility.billing.reading.MeterReadingRepository;
import com.utility.billing.tariff.TariffService;
import com.utility.billing.tariff.TariffType;
import com.utility.billing.user.Role;
import com.utility.billing.user.User;
import com.utility.billing.user.UserRepository;
import com.utility.billing.user.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class SeedData implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final MeterRepository meterRepository;
    private final MeterReadingRepository readingRepository;
    private final BillRepository billRepository;
    private final TariffService tariffService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) return;

        // 1. Users
        User admin = User.builder()
                .fullName("Admin User")
                .email("admin@wasac.gov.rw")
                .phoneNumber("0780000001")
                .password(passwordEncoder.encode("admin123"))
                .role(Role.ROLE_ADMIN)
                .status(UserStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        User operator = User.builder()
                .fullName("Operator User")
                .email("operator@wasac.gov.rw")
                .phoneNumber("0780000002")
                .password(passwordEncoder.encode("operator123"))
                .role(Role.ROLE_OPERATOR)
                .status(UserStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        userRepository.save(admin);
        userRepository.save(operator);

        // 2. Tariffs
        tariffService.createTariff(MeterType.WATER, TariffType.FLAT_RATE, 350.0);
        tariffService.createTariff(MeterType.ELECTRICITY, TariffType.FLAT_RATE, 220.0);

        // 3. Customers
        Customer customer1 = Customer.builder()
                .fullNames("John Doe")
                .nationalId("1199080012345678")
                .email("john.doe@example.com")
                .phone("0781234567")
                .address("Kigali, Rwanda")
                .status(CustomerStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .build();
        customerRepository.save(customer1);

        // 4. Meters
        Meter waterMeter = Meter.builder()
                .meterNumber("WTR-001")
                .meterType(MeterType.WATER)
                .customer(customer1)
                .status(MeterStatus.ACTIVE)
                .installationDate(LocalDateTime.now())
                .build();
        meterRepository.save(waterMeter);

        // 5. Readings
        MeterReading reading1 = MeterReading.builder()
                .meter(waterMeter)
                .previousReading(0.0)
                .currentReading(15.0)
                .month(5)
                .year(2026)
                .readingDate(LocalDateTime.now())
                .capturedBy(operator)
                .build();
        readingRepository.save(reading1);

        // 6. Bills
        Bill bill1 = Bill.builder()
                .billNumber("BILL-TEST-001")
                .customer(customer1)
                .meter(waterMeter)
                .billingMonth(5)
                .billingYear(2026)
                .consumption(15.0)
                .tariffAmount(15.0 * 350.0)
                .taxAmount(15.0 * 350.0 * 0.18)
                .penaltyAmount(0.0)
                .totalAmount(15.0 * 350.0 * 1.18)
                .paidAmount(0.0)
                .balance(15.0 * 350.0 * 1.18)
                .status(BillStatus.PENDING)
                .generatedDate(LocalDateTime.now())
                .build();
        billRepository.save(bill1);
    }
}
