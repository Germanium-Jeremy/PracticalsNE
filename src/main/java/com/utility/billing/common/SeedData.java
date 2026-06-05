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
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class SeedData implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final MeterRepository meterRepository;
    private final MeterReadingRepository readingRepository;
    private final BillRepository billRepository;
    private final TariffService tariffService;
    private final com.utility.billing.tariff.PenaltyConfigurationRepository penaltyConfigurationRepository;
    private final PasswordEncoder passwordEncoder;

    public SeedData(UserRepository userRepository, CustomerRepository customerRepository, MeterRepository meterRepository, MeterReadingRepository readingRepository, BillRepository billRepository, TariffService tariffService, com.utility.billing.tariff.PenaltyConfigurationRepository penaltyConfigurationRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
        this.meterRepository = meterRepository;
        this.readingRepository = readingRepository;
        this.billRepository = billRepository;
        this.tariffService = tariffService;
        this.penaltyConfigurationRepository = penaltyConfigurationRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) return;

        // 1. Users
        User admin = User.builder()
                .fullName("Admin User")
                .email("admin@wasac.gov.rw")
                .phoneNumber("0780000001")
                .password(passwordEncoder.encode("Admin123!"))
                .role(Role.ROLE_ADMIN)
                .status(UserStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        User operator = User.builder()
                .fullName("Operator User")
                .email("operator@wasac.gov.rw")
                .phoneNumber("0780000002")
                .password(passwordEncoder.encode("Operator123!"))
                .role(Role.ROLE_OPERATOR)
                .status(UserStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        User finance = User.builder()
                .fullName("Finance User")
                .email("finance@wasac.gov.rw")
                .phoneNumber("0780000003")
                .password(passwordEncoder.encode("Finance123!"))
                .role(Role.ROLE_FINANCE)
                .status(UserStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        userRepository.save(admin);
        userRepository.save(operator);
        userRepository.save(finance);

        // 2. Tariffs
        tariffService.createTariff(MeterType.WATER, TariffType.FLAT_RATE, 350.0);
        tariffService.createTariff(MeterType.ELECTRICITY, TariffType.FLAT_RATE, 220.0);

        // 3. Penalty Configuration
        com.utility.billing.tariff.PenaltyConfiguration penaltyConfig = com.utility.billing.tariff.PenaltyConfiguration.builder()
                .name("Standard Overdue Penalty")
                .fixedAmount(500.0)
                .percentagePerMonth(2.0)
                .active(true)
                .build();
        penaltyConfigurationRepository.save(penaltyConfig);

        // 4. Customers
        User customerUser = User.builder()
                .fullName("John Doe")
                .email("john.doe@example.com")
                .phoneNumber("0781234567")
                .password(passwordEncoder.encode("Customer123!"))
                .role(Role.ROLE_CUSTOMER)
                .status(UserStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        userRepository.save(customerUser);

        Customer customer1 = Customer.builder()
                .fullNames("John Doe")
                .nationalId("1199080012345678")
                .email("john.doe@example.com")
                .phone("0781234567")
                .address("Kigali, Rwanda")
                .status(CustomerStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .user(customerUser)
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
