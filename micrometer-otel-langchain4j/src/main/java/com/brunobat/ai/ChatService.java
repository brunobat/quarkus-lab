package com.brunobat.ai;

import com.brunobat.ai.common.Assistant;
import io.micrometer.core.instrument.*;
import io.micrometer.core.instrument.Meter.MeterProvider;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.Duration;

@ApplicationScoped
public class ChatService {

    private final Assistant assistant;
    private final Counter chatRequests;
    private final Timer chatLatency;
    private final MeterProvider<DistributionSummary> payloadChars;

    public ChatService(Assistant assistant, MeterRegistry registry) {
        this.assistant = assistant;

        this.chatRequests = Counter.builder("demo.chat.requests") // don't use total, or the units
                .description("Total chat requests (business demand)")
                .tag("endpoint", "chat")
                .register(registry);

        this.chatLatency = Timer.builder("demo.chat.latency")
                .description("End-to-end chat latency (SLA)")
                .sla(Duration.ofMillis(100),
                        Duration.ofSeconds(1),
                        Duration.ofSeconds(10)) // sla or percentiles, not both at the same time!
                .register(registry);

        this.payloadChars = DistributionSummary.builder("demo.chat.payload")
                .description("Payload size distribution (cost driver)")
                .baseUnit("chars")
                .publishPercentiles(0.5, 0.9, 0.99, 0.999, 0.9999) // force nr of percentiles. There will be 1 bucket.
                .withRegistry(registry);
    }

    //    @WithSpan("chat.service")
    public String answer(String question) {
        chatRequests.increment();

        return chatLatency.record(() -> {
            payloadChars.withTag("direction", "outbound").record(question.length());
            String ans = assistant.chat(1234, question); // use agent with memory
            payloadChars.withTag("direction", "inbound").record(ans.length());
            return ans;
        });
    }
}
