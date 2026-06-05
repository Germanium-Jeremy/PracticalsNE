package com.utility.billing.meter;

import com.utility.billing.customer.Customer;
import com.utility.billing.customer.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MeterService {

    private final MeterRepository meterRepository;
    private final CustomerRepository customerRepository;

    public MeterService(MeterRepository meterRepository, CustomerRepository customerRepository) {
        this.meterRepository = meterRepository;
        this.customerRepository = customerRepository;
    }

    public MeterResponse createMeter(MeterRequest request) {
        if (meterRepository.existsByMeterNumber(request.getMeterNumber())) {
            throw new RuntimeException("Meter number already exists");
        }

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Meter meter = Meter.builder()
                .meterNumber(request.getMeterNumber())
                .meterType(request.getMeterType())
                .customer(customer)
                .status(MeterStatus.ACTIVE)
                .installationDate(LocalDateTime.now())
                .build();

        return mapToResponse(meterRepository.save(meter));
    }

    public List<MeterResponse> getAllMeters() {
        return meterRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public MeterResponse getMeterById(Long id) {
        return meterRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new RuntimeException("Meter not found"));
    }

    public List<MeterResponse> getMetersByCustomer(Long customerId) {
        return meterRepository.findByCustomerId(customerId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public MeterResponse updateMeter(Long id, MeterRequest request) {
        Meter meter = meterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Meter not found"));

        if (request.getStatus() != null) {
            meter.setStatus(request.getStatus());
        }
        
        // Meter type and number usually don't change, but can be updated if needed
        if (request.getMeterType() != null) {
            meter.setMeterType(request.getMeterType());
        }

        return mapToResponse(meterRepository.save(meter));
    }

    public void deleteMeter(Long id) {
        meterRepository.deleteById(id);
    }

    private MeterResponse mapToResponse(Meter meter) {
        MeterResponse response = new MeterResponse();
        response.setId(meter.getId());
        response.setMeterNumber(meter.getMeterNumber());
        response.setMeterType(meter.getMeterType());
        response.setStatus(meter.getStatus());
        response.setInstallationDate(meter.getInstallationDate());
        response.setCustomerId(meter.getCustomer().getId());
        return response;
    }
}
