package com.brunobat.ai;

import io.micrometer.core.instrument.LongTaskTimer;
import io.micrometer.core.instrument.MeterRegistry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@ApplicationScoped
public class LongJobService {

    private final Random rnd = new Random();
    private final ExecutorService executor;
    private final AtomicInteger jobIndex = new AtomicInteger(1);

    private final LongTaskTimer longTaskTimer;
    private final Tracer tracer;

    public LongJobService(MeterRegistry registry,
                          @Named("customBusinessExecutor") ExecutorService executor,
                          Tracer tracer) {
        this.executor = executor;
        this.tracer = tracer;

        this.longTaskTimer = LongTaskTimer.builder("demo.long.job")
                .description("Long running operations (active+duration")
                .tag("job", "indexer")
                .register(registry);
    }

    public long startLongTask() {
        int id = jobIndex.getAndIncrement();
        Span span = tracer.spanBuilder("LongJobService.startLongTask").startSpan();


        executor.submit(() -> {
            LongTaskTimer.Sample sample = longTaskTimer.start();
            try(Scope scope = span.makeCurrent()) {
                Thread.sleep(3000 + rnd.nextInt(2000));

                double duration = sample.duration(TimeUnit.MILLISECONDS);
                int activeTasks = longTaskTimer.activeTasks();

                Log.infov("JobIndex with id {0} has stopped and current" +
                                " duration is {1}. {2} jobs are running", id,
                        duration,
                        activeTasks);

                span.setAttribute("jobIndex", id);
                span.setAttribute("activeTasks", activeTasks);
                span.setAttribute("duration", duration);

            } catch (InterruptedException ignored) {
                span.recordException(ignored);
                Log.info("terminated on executor");
            } finally {
                sample.stop();
                span.end();
            }
        });

        return id;
    }
}