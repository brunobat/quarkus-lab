package com.brunobat.ai.common;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.jvm.ExecutorServiceMetrics;
import io.quarkus.runtime.Shutdown;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Named;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@ApplicationScoped
public class ExecutorProducer {

    private final MeterRegistry meterRegistry;
    private ExecutorService executor;

    public ExecutorProducer(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @Produces
    @ApplicationScoped // there will be only one
    @Named("customBusinessExecutor")
    public ExecutorService executorService() {
        ExecutorService exec = Executors.newFixedThreadPool(4);

        // wrapper will keep an eye on the executor metrics
        ExecutorServiceMetrics
                .monitor(meterRegistry, exec,
                        "business_executor",
                        "demo.business");

        this.executor = exec;
        return exec;
    }

    @Shutdown
    public void shutdown() {
        if (executor != null) {
            executor.shutdown();
        }
    }
}