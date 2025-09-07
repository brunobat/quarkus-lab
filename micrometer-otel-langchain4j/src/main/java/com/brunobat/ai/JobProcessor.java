package com.brunobat.ai;

import io.micrometer.core.instrument.FunctionCounter;
import io.micrometer.core.instrument.FunctionTimer;
import io.micrometer.core.instrument.MeterRegistry;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@ApplicationScoped
public class JobProcessor {

    private final AtomicLong completedJobs = new AtomicLong(1);
    private final AtomicLong totalProcessingTime = new AtomicLong(0);
    private final ExecutorService executor;

    public JobProcessor(MeterRegistry registry, @Named("customBusinessExecutor") ExecutorService executor) {
        this.executor = executor;

        FunctionCounter.builder("demo.jobs.completed", this,
                        jp -> jp.completedJobs.get())
                .description("Completed jobs (throughput KPI)")
                .register(registry);

        FunctionTimer.builder("demo.jobs.function.timer", this,
                        jp -> jp.completedJobs.get(),
                        jp -> jp.totalProcessingTime.get(), TimeUnit.NANOSECONDS)
                .description("Job processing time (efficiency KPI)")
                .register(registry);
    }

    public void processAsync(long millis) {
        executor.submit(() -> {
            long start = System.nanoTime();
            try {
                Thread.sleep(millis);
            } catch (InterruptedException ignored) {
                // nothing
            }
            long took = totalProcessingTime.addAndGet(System.nanoTime() - start);
            long jobId = completedJobs.incrementAndGet();
            Log.infov("Job {0} expected to take {1}ms, took {2}ns", jobId, millis, took);
        });
    }
}