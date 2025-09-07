package com.brunobat.ai.common;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;
import jakarta.enterprise.context.ApplicationScoped;

//@Timed
@RegisterAiService
@ApplicationScoped
public interface Assistant {
    @SystemMessage("You are concise and friendly. Keep answers short.")
    String chat(@MemoryId int id, @UserMessage String question);
}
