package com.brunobat.ai;

import jakarta.enterprise.context.ApplicationScoped;

import java.time.Instant;
import java.util.Collections;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@ApplicationScoped
public class QueueService {

    private final Queue<Message> promptQueue = new ConcurrentLinkedQueue<>();

    public QueueService() {
    }

    public boolean enqueue(String prompt) {
        return promptQueue.add(Message.of(prompt));
    }

    public int depth() {
        return promptQueue.size();
    }

    public Map<String, Object> drainOne() {
        return Collections.emptyMap();
    }

    public record Message(String prompt, Instant createdAt) {
        public static Message of(String prompt) {
            return new Message(prompt, Instant.now());
        }
    }
}
