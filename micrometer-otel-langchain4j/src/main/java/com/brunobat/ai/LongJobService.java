package com.brunobat.ai;

import io.micrometer.core.instrument.MeterRegistry;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

@ApplicationScoped
public class LongJobService {

    private final Random rnd = new Random();
    private final ExecutorService executor;
    private final AtomicInteger jobIndex = new AtomicInteger(1);

    public LongJobService(MeterRegistry registry, @Named("customBusinessExecutor") ExecutorService executor) {
        this.executor = executor;
    }

    public long startLongTask() {
        int id = jobIndex.getAndIncrement();

        try {
            Thread.sleep(3000 + rnd.nextInt(2000));
        } catch (InterruptedException ignored) {
            Log.info("terminated on executor");
        }

        return id;
    }
}