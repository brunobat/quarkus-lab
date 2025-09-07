package com.brunobat.ai;

import com.brunobat.ai.common.Assistant;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ChatService {

    private final Assistant assistant;

    public ChatService(Assistant assistant, MeterRegistry registry) {
        this.assistant = assistant;
    }

    //    @WithSpan("chat.service")
    public String answer(String question) {
        String ans = assistant.chat(1234, question); // use agent with memory
        return ans;
    }
}
