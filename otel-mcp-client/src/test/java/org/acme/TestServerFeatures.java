package org.acme;

import jakarta.inject.Inject;

import io.quarkiverse.mcp.server.Prompt;
import io.quarkiverse.mcp.server.PromptArg;
import io.quarkiverse.mcp.server.PromptMessage;
import io.quarkiverse.mcp.server.TextContent;
import io.quarkiverse.mcp.server.Tool;

public class TestServerFeatures {

    @Inject
    TestCodeService codeService;

    @Tool
    TextContent toLowerCase(String value) {
        return new TextContent(value.toLowerCase());
    }

    @Prompt(name = "code_assist")
    PromptMessage codeAssist(@PromptArg(name = "lang") String language) {
        return PromptMessage.withUserRole(new TextContent(codeService.assist(language)));
    }

    @Tool
    Answer answer(String question) {
        return new Answer(question.toLowerCase());
    }

    public record Answer(String value) {
    }
}
