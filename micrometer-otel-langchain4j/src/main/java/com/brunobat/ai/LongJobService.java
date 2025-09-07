package com.brunobat.ai;

import io.micrometer.core.instrument.LongTaskTimer;
import io.micrometer.core.instrument.MeterRegistry;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@ApplicationScoped
public class LongJobService {

    private final LongTaskTimer longTaskTimer;

    private final Random rnd = new Random();
    private final ExecutorService executor;
    private final AtomicInteger jobIndex = new AtomicInteger(1);

    public LongJobService(MeterRegistry registry, @Named("customBusinessExecutor") ExecutorService executor) {
        this.longTaskTimer = LongTaskTimer.builder("demo.long.job")
                .description("Long running operations (active + duration)")
                .tags("job", "indexer")
                .register(registry);

        this.executor = executor;
    }

    public long startLongTask() {
        int id = jobIndex.getAndIncrement();
        LongTaskTimer.Sample sample = longTaskTimer.start();

        Future<?> task = executor.submit(() -> {
            try {
                Thread.sleep(3000 + rnd.nextInt(2000));
                Log.infov("JobIndex with id {0} has stopped and current step duration is {1}. {2} jobs running.",
                        id,
                        sample.duration(TimeUnit.MILLISECONDS),
                        longTaskTimer.activeTasks());
            } catch (InterruptedException ignored) {
                Log.info("terminated on executor");
            }
            sample.stop();
        });
        return id;
    }
}