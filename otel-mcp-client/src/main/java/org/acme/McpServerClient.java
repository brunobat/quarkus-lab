package org.acme;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.mcp.client.McpClient;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.service.tool.ToolExecutionResult;

import io.quarkiverse.langchain4j.mcp.runtime.McpClientName;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class McpServerClient {

    @Inject
    @McpClientName("server")
    McpClient mcpClient;

    @Inject
    ObjectMapper objectMapper;

    public List<ToolSpecification> listTools() {
        return mcpClient.listTools();
    }

    public String toLowerCase(String value) {
        return callTool("toLowerCase", Map.of("value", value));
    }

    public String answer(String question) {
        return callTool("answer", Map.of("question", question));
    }

    public String codeAssist(String language) {
        return mcpClient.getPrompt("code_assist", Map.of("lang", language))
                .messages().stream()
                .map(m -> (UserMessage) m.toChatMessage())
                .map(UserMessage::singleText)
                .reduce("", (a, b) -> a + b);
    }

    private String callTool(String toolName, Map<String, String> arguments) {
        try {
            String argsJson = objectMapper.writeValueAsString(arguments);
            ToolExecutionRequest request = ToolExecutionRequest.builder()
                    .name(toolName)
                    .arguments(argsJson)
                    .build();
            ToolExecutionResult result = mcpClient.executeTool(request);
            return result.resultText();
        } catch (Exception e) {
            throw new RuntimeException("MCP call failed: " + toolName, e);
        }
    }
}
