package org.acme;

import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.binder.BaseUnits;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Singleton;

import java.time.Duration;

@Singleton
public class PercentilHistogram {

    final DistributionSummary summary;

    public PercentilHistogram(MeterRegistry registry) {
        summary = DistributionSummary.builder("my.histogram")
                .description("Total time taken by network/kafka/Franklin")
                .baseUnit(BaseUnits.MILLISECONDS)
                .publishPercentiles(0.5, 0.9, 0.95, 0.99)
                .serviceLevelObjectives(
                        0.100d,// it's in seconds
                        0.200d,
                        0.300d,
                        0.400d,
                        0.500d,
                        0.600d,
                        0.700d,
                        0.800d,
                        0.900d,
                        1.000d,
                        1.100d,
                        2.000d,
                        3.000d,
                        4.00d,
                        5.00d)
                .tags(Tags.of("logical_position", "my_tag"))
                .register(registry);
    }

    public void record(double value) {
        summary.record(value);
    }
}
