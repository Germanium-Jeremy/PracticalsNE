package com.utility.billing.billing;

import com.utility.billing.customer.CustomerStatus;
import com.utility.billing.meter.Meter;
import com.utility.billing.meter.MeterRepository;
import com.utility.billing.reading.MeterReading;
import com.utility.billing.reading.MeterReadingRepository;
import com.utility.billing.tariff.Tariff;
import com.utility.billing.tariff.TariffService;
import com.utility.billing.notification.NotificationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BillingService {

    private final BillRepository billRepository;
    private final MeterRepository meterRepository;
    private final MeterReadingRepository readingRepository;
    private final TariffService tariffService;
    private final NotificationService notificationService;

    public BillingService(
            BillRepository billRepository, 
            MeterRepository meterRepository, 
            MeterReadingRepository readingRepository, 
            TariffService tariffService,
            NotificationService notificationService
    ) {
        this.billRepository = billRepository;
        this.meterRepository = meterRepository;
        this.readingRepository = readingRepository;
        this.tariffService = tariffService;
        this.notificationService = notificationService;
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

        Bill savedBill = billRepository.save(bill);
        notificationService.sendBillNotification(savedBill);
        return savedBill;
    }

    @Transactional
    public BillResponse approveBill(Long billId) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new RuntimeException("Bill not found"));

        if (bill.getStatus() != BillStatus.PENDING) {
            throw new RuntimeException("Only PENDING bills can be approved");
        }

        bill.setStatus(BillStatus.APPROVED);
        bill.setApprovedDate(LocalDateTime.now());
        Bill approvedBill = billRepository.save(bill);
        return mapToResponse(approvedBill);
    }

    public List<BillResponse> getCustomerBills(Long customerId) {
        return billRepository.findByCustomerId(customerId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<BillResponse> getAllBills() {
        return billRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public BillResponse getBillById(Long id) {
        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bill not found"));
        return mapToResponse(bill);
    }

    private BillResponse mapToResponse(Bill bill) {
        BillResponse response = new BillResponse();
        response.setId(bill.getId());
        response.setBillNumber(bill.getBillNumber());
        response.setCustomerId(bill.getCustomer().getId());
        response.setCustomerName(bill.getCustomer().getFullNames());
        response.setMeterId(bill.getMeter().getId());
        response.setMeterNumber(bill.getMeter().getMeterNumber());
        response.setBillingMonth(bill.getBillingMonth());
        response.setBillingYear(bill.getBillingYear());
        response.setConsumption(bill.getConsumption());
        response.setTariffAmount(bill.getTariffAmount());
        response.setTaxAmount(bill.getTaxAmount());
        response.setPenaltyAmount(bill.getPenaltyAmount());
        response.setTotalAmount(bill.getTotalAmount());
        response.setPaidAmount(bill.getPaidAmount());
        response.setBalance(bill.getBalance());
        response.setStatus(bill.getStatus());
        response.setGeneratedDate(bill.getGeneratedDate());
        response.setApprovedDate(bill.getApprovedDate());
        return response;
    }
}
