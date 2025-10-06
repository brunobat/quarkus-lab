package com.brunobat.ai.common;

import io.quarkus.runtime.Shutdown;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Named;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@ApplicationScoped
public class ExecutorProducer {

    private ExecutorService executor;

    public ExecutorProducer() {
        executor = Executors.newFixedThreadPool(4);
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