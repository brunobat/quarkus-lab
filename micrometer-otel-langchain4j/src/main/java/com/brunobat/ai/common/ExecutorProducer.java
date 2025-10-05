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

    private ExecutorService executor;

    public ExecutorProducer(MeterRegistry registry) {
        executor = Executors.newFixedThreadPool(4);

        ExecutorServiceMetrics.monitor(registry, executor,
                "business_executor",
                "demo.business") ;
    }

    @Produces
    @ApplicationScoped // there will be only one
    @Named("customBusinessExecutor")
    public ExecutorService executorService() {
        return executor;
    }

    @Shutdown
    public void shutdown() {
        if (executor != null) {
            executor.shutdown();
        }
    }
}