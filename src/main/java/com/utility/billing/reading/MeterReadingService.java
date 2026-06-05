package com.utility.billing.reading;

import com.utility.billing.meter.Meter;
import com.utility.billing.meter.MeterRepository;
import com.utility.billing.meter.MeterStatus;
import com.utility.billing.user.User;
import com.utility.billing.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MeterReadingService {

    private final MeterReadingRepository readingRepository;
    private final MeterRepository meterRepository;
    private final UserRepository userRepository;

    @Transactional
    public MeterReadingResponse captureReading(MeterReadingRequest request) {
        Meter meter = meterRepository.findById(request.getMeterId())
                .orElseThrow(() -> new RuntimeException("Meter not found"));

        if (meter.getStatus() != MeterStatus.ACTIVE) {
            throw new RuntimeException("Meter is not ACTIVE. Cannot capture reading.");
        }

        if (readingRepository.existsByMeterIdAndMonthAndYear(request.getMeterId(), request.getMonth(), request.getYear())) {
            throw new RuntimeException("Reading already exists for this meter in the given month/year");
        }

        Double previousReading = readingRepository.findLatestReadingByMeterId(request.getMeterId())
                .map(MeterReading::getCurrentReading)
                .orElse(0.0);

        if (request.getCurrentReading() < previousReading) {
            throw new RuntimeException("Current reading cannot be less than previous reading");
        }

        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User capturedBy = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));

        MeterReading reading = MeterReading.builder()
                .meter(meter)
                .previousReading(previousReading)
                .currentReading(request.getCurrentReading())
                .readingDate(LocalDateTime.now())
                .month(request.getMonth())
                .year(request.getYear())
                .capturedBy(capturedBy)
                .build();

        return mapToResponse(readingRepository.save(reading));
    }

    public List<MeterReadingResponse> getAllReadings() {
        return readingRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private MeterReadingResponse mapToResponse(MeterReading reading) {
        MeterReadingResponse response = new MeterReadingResponse();
        response.setId(reading.getId());
        response.setMeterId(reading.getMeter().getId());
        response.setMeterNumber(reading.getMeter().getMeterNumber());
        response.setPreviousReading(reading.getPreviousReading());
        response.setCurrentReading(reading.getCurrentReading());
        response.setReadingDate(reading.getReadingDate());
        response.setMonth(reading.getMonth());
        response.setYear(reading.getYear());
        response.setCapturedBy(reading.getCapturedBy().getFullName());
        return response;
    }
}
