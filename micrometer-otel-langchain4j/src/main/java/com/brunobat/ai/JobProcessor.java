package com.brunobat.ai;

import io.micrometer.core.instrument.MeterRegistry;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

import java.util.concurrent.ExecutorService;

@ApplicationScoped
public class JobProcessor {

    private final ExecutorService executor;

    public JobProcessor(MeterRegistry registry, @Named("customBusinessExecutor") ExecutorService executor) {
        this.executor = executor;
    }

    public void processAsync(long millis) {

    }
}