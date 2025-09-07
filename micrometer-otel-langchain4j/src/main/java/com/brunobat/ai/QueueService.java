package com.brunobat.ai;

import com.brunobat.ai.common.Assistant;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@ApplicationScoped
public class QueueService {

    private final Queue<Message> promptQueue = new ConcurrentLinkedQueue<>();

    private final MeterRegistry registry;
    private final Assistant assistant;

    public QueueService(MeterRegistry registry, Assistant assistant) {
        this.registry = registry;
        this.assistant = assistant;
    }

    @PostConstruct
    void init() {

        List.of("My name is Bruno",
                        "Do you remember my name?",
                        "What's your name?")
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

            String response = assistant.chat(1234, msg.prompt());

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
