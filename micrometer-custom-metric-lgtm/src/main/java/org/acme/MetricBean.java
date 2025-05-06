package org.acme;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.concurrent.atomic.LongAdder;

@ApplicationScoped
public class MetricBean {

    final LongAdder variableMagnitude = new LongAdder();

    public MetricBean(MeterRegistry registry) {
        Gauge.builder("your.custom.metric.name", () -> variableMagnitude.longValue())
                .description("Gauge sampling the value of variableMagnitude attibute")
                // add tags here
                .register(registry);
    }

    public LongAdder getVariableMagnitude() {
        return variableMagnitude;
    }

    public void incrementVariableMagnitude() {
        variableMagnitude.increment();
    }

    public void decrementVariableMagnitude() {
        variableMagnitude.decrement();
    }
}