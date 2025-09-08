package com.brunobat.ai;

import com.brunobat.ai.common.Assistant;
import io.micrometer.core.instrument.*;
import io.micrometer.core.instrument.Meter.MeterProvider;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@ApplicationScoped
public class QueueService {

    private final Queue<Message> promptQueue = new ConcurrentLinkedQueue<>();

    private final MeterRegistry registry;
    private final ChatService chatService;

    private Counter processedCounter;
    MeterProvider<Counter> charsCount;
    private Timer processingTimer;
    private DistributionSummary waitSummary;

    @Inject
    public QueueService(MeterRegistry registry, ChatService chatService) {
        this.registry = registry;
        this.chatService = chatService;
    }

    @PostConstruct
    void init() {
        Gauge.builder("demo.queue.depth", promptQueue, Queue::size)
                .description("How many AI prompts are waiting to be processed")
                .baseUnit("prompts")
                .register(registry);

        processedCounter = Counter.builder("demo.queue.processed")
                .description("Total messages processed from the AI queue")
                .register(registry);

        charsCount = Counter.builder("demo.queue.chars")
                .description("Size of processed AI request/responses (chars)")
                .baseUnit("chars")
                .withRegistry(registry);

        processingTimer = Timer.builder("demo.queue.processing") // includes count, max, sum and percentiles
                .description("Time to process a message with Assistant.chat")
                .publishPercentiles(0.5, 0.9, 0.99, 0.999, 0.9999)
                .register(registry);

        waitSummary = DistributionSummary.builder("demo.queue.wait")
                .description("Time a message waited in queue before processing")
                .baseUnit("ms")
                .serviceLevelObjectives(30, 100, 500, 1000, 2000, 3000, 4500, 5000, 6500, 7000, 8000, 9000)
                .register(registry);

        List.of("My name is Bruno", "Do you remember my name?", "What's your name?")
                .forEach(phrase -> enqueue(phrase));
    }


    public boolean enqueue(String prompt) {
        return promptQueue.add(Message.of(prompt));
    }


    public int depth() {
        return promptQueue.size();
    }

    public Map<String, Object> drainOne() {
        try {
            Message msg = promptQueue.poll();
            if (msg == null) {
                return Map.of("status", "empty queue");
            }

            long waitedMs = Duration.between(msg.createdAt(), Instant.now()).toMillis();
            waitSummary.record(waitedMs);

            charsCount.withTag("direction", "outbound").increment(msg.prompt.length());
            String response = processingTimer.record(() -> chatService.answer(msg.prompt()));
            if (response != null) {
                charsCount.withTag("direction", "inbound").increment(response.length());
            }

            processedCounter.increment();
            return Map.of("Question", msg, "Response", response);
        } catch (Exception e) {
            return Map.of("popped", "failure" + e.getMessage());
        }
    }

    public record Message(String prompt, Instant createdAt) {
        public static Message of(String prompt) {
            return new Message(prompt, Instant.now());
        }
    }
}
