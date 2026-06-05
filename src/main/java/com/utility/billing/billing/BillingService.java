package com.utility.billing.billing;

import com.utility.billing.customer.CustomerStatus;
import com.utility.billing.meter.Meter;
import com.utility.billing.meter.MeterRepository;
import com.utility.billing.reading.MeterReading;
import com.utility.billing.reading.MeterReadingRepository;
import com.utility.billing.tariff.Tariff;
import com.utility.billing.tariff.TariffService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class BillingService {

    private final BillRepository billRepository;
    private final MeterRepository meterRepository;
    private final MeterReadingRepository readingRepository;
    private final TariffService tariffService;

    public BillingService(BillRepository billRepository, MeterRepository meterRepository, MeterReadingRepository readingRepository, TariffService tariffService) {
        this.billRepository = billRepository;
        this.meterRepository = meterRepository;
        this.readingRepository = readingRepository;
        this.tariffService = tariffService;
    }

    @Transactional
    public Bill generateBill(Long meterId, Integer month, Integer year) {
        Meter meter = meterRepository.findById(meterId)
                .orElseThrow(() -> new RuntimeException("Meter not found"));

        if (meter.getCustomer().getStatus() != CustomerStatus.ACTIVE) {
            throw new RuntimeException("Cannot generate bill for inactive customer");
        }

        if (billRepository.existsByMeterIdAndBillingMonthAndBillingYear(meterId, month, year)) {
            throw new RuntimeException("Bill already exists for this meter and period");
        }

        MeterReading reading = readingRepository.findAll().stream()
                .filter(r -> r.getMeter().getId().equals(meterId) && r.getMonth().equals(month) && r.getYear().equals(year))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No reading found for the given period"));

        double consumption = reading.getCurrentReading() - reading.getPreviousReading();
        Tariff tariff = tariffService.getActiveTariff(meter.getMeterType());

        double tariffAmount = consumption * tariff.getRate();
        double taxAmount = tariffAmount * 0.18; // Default 18% VAT
        double totalAmount = tariffAmount + taxAmount;

        Bill bill = Bill.builder()
                .billNumber("BILL-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .customer(meter.getCustomer())
                .meter(meter)
                .billingMonth(month)
                .billingYear(year)
                .consumption(consumption)
                .tariffAmount(tariffAmount)
                .taxAmount(taxAmount)
                .penaltyAmount(0.0)
                .totalAmount(totalAmount)
                .paidAmount(0.0)
                .balance(totalAmount)
                .status(BillStatus.PENDING)
                .generatedDate(LocalDateTime.now())
                .build();

        return billRepository.save(bill);
    }

    @Transactional
    public Bill approveBill(Long billId) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new RuntimeException("Bill not found"));

        if (bill.getStatus() != BillStatus.PENDING) {
            throw new RuntimeException("Only PENDING bills can be approved");
        }

        bill.setStatus(BillStatus.APPROVED);
        bill.setApprovedDate(LocalDateTime.now());
        return billRepository.save(bill);
    }

    public List<Bill> getCustomerBills(Long customerId) {
        return billRepository.findByCustomerId(customerId);
    }
}
