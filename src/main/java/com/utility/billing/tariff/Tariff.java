package com.utility.billing.tariff;

import com.utility.billing.meter.MeterType;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tariffs")
public class Tariff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MeterType meterType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TariffType tariffType;

    @Column(nullable = false)
    private Double rate;

    @Column(nullable = false)
    private LocalDateTime effectiveFrom;

    @Column(nullable = false)
    private Integer version;

    @Column(nullable = false)
    private boolean active;

    public Tariff() {}

    public Tariff(Long id, MeterType meterType, TariffType tariffType, Double rate, LocalDateTime effectiveFrom, Integer version, boolean active) {
        this.id = id;
        this.meterType = meterType;
        this.tariffType = tariffType;
        this.rate = rate;
        this.effectiveFrom = effectiveFrom;
        this.version = version;
        this.active = active;
    }

    public static TariffBuilder builder() {
        return new TariffBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public MeterType getMeterType() { return meterType; }
    public void setMeterType(MeterType meterType) { this.meterType = meterType; }
    public TariffType getTariffType() { return tariffType; }
    public void setTariffType(TariffType tariffType) { this.tariffType = tariffType; }
    public Double getRate() { return rate; }
    public void setRate(Double rate) { this.rate = rate; }
    public LocalDateTime getEffectiveFrom() { return effectiveFrom; }
    public void setEffectiveFrom(LocalDateTime effectiveFrom) { this.effectiveFrom = effectiveFrom; }
    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public static class TariffBuilder {
        private Long id;
        private MeterType meterType;
        private TariffType tariffType;
        private Double rate;
        private LocalDateTime effectiveFrom;
        private Integer version;
        private boolean active;

        public TariffBuilder id(Long id) { this.id = id; return this; }
        public TariffBuilder meterType(MeterType meterType) { this.meterType = meterType; return this; }
        public TariffBuilder tariffType(TariffType tariffType) { this.tariffType = tariffType; return this; }
        public TariffBuilder rate(Double rate) { this.rate = rate; return this; }
        public TariffBuilder effectiveFrom(LocalDateTime effectiveFrom) { this.effectiveFrom = effectiveFrom; return this; }
        public TariffBuilder version(Integer version) { this.version = version; return this; }
        public TariffBuilder active(boolean active) { this.active = active; return this; }

        public Tariff build() {
            return new Tariff(id, meterType, tariffType, rate, effectiveFrom, version, active);
        }
    }
}
