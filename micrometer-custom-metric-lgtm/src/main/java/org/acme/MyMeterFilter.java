package org.acme;

import io.micrometer.core.instrument.config.MeterFilter;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Singleton;

@Singleton
public class MyMeterFilter {

    @Produces
    @Singleton
    public MeterFilter denySpecificMetric() {
        return MeterFilter.ignoreTags("jvm.buffer.total.capacity"); // filters out all JVM GC metrics
//        return MeterFilter.denyNameStartsWith("jvm.buffer.total.capacity"); // filters out all JVM GC metrics
    }
}