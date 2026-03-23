package com.brunobat;

import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.mcp.client.McpClient;
import dev.langchain4j.service.tool.ToolExecutionResult;

import io.quarkiverse.langchain4j.mcp.runtime.McpClientName;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * MCP client that connects to Tempo's MCP server using the Quarkus LangChain4j
 * MCP client extension.
 */
@ApplicationScoped
public class TempoMcpClient {

    @Inject
    @McpClientName("tempo")
    McpClient mcpClient;

    @Inject
    ObjectMapper objectMapper;

    /** Search for traces matching a TraceQL query. */
    public JsonNode searchTraces(String traceqlQuery) {
        return callTool("traceql-search", Map.of("query", traceqlQuery));
    }

    /** Get available attribute names, optionally filtered by scope. */
    public JsonNode getAttributeNames(String scope) {
        if (scope != null) {
            return callTool("get-attribute-names", Map.of("scope", scope));
        }
        return callTool("get-attribute-names", Map.of());
    }

    /** Get values for a specific attribute. */
    public JsonNode getAttributeValues(String attributeName) {
        return callTool("get-attribute-values", Map.of("name", attributeName));
    }

    /** Get a specific trace by ID. */
    public JsonNode getTrace(String traceId) {
        return callTool("get-trace", Map.of("trace_id", traceId));
    }

    /** Execute a TraceQL metrics instant query. */
    public JsonNode metricsInstant(String query) {
        return callTool("traceql-metrics-instant", Map.of("query", query));
    }

    private JsonNode callTool(String toolName, Map<String, String> arguments) {
        try {
            String argsJson = objectMapper.writeValueAsString(arguments);
            ToolExecutionRequest request = ToolExecutionRequest.builder()
                    .name(toolName)
                    .arguments(argsJson)
                    .build();
            ToolExecutionResult result = mcpClient.executeTool(request);
            String text = result.resultText();
            if (text != null && !text.isBlank()) {
                return objectMapper.readTree(text);
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("MCP tool call failed: " + toolName, e);
        }
    }
}
