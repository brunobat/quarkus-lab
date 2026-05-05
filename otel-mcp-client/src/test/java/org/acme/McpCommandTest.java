package org.acme;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.quarkus.test.junit.main.Launch;
import io.quarkus.test.junit.main.LaunchResult;
import io.quarkus.test.junit.main.QuarkusMainTest;

import org.junit.jupiter.api.Test;

@QuarkusMainTest
class McpCommandTest {

    @Test
    @Launch({"toLowerCase", "HELLO"})
    void testToLowerCase(LaunchResult result) {
        assertTrue(result.getOutput().contains("hello"));
    }

    @Test
    @Launch({"toLowerCase", "Quarkus MCP"})
    void testToLowerCaseWithSpaces(LaunchResult result) {
        assertTrue(result.getOutput().contains("quarkus mcp"));
    }

    @Test
    @Launch({"answer", "What is Quarkus?"})
    void testAnswer(LaunchResult result) {
        assertTrue(result.getOutput().contains("what is quarkus?"));
    }

    @Test
    @Launch({"code-assist", "java"})
    void testCodeAssist(LaunchResult result) {
        assertTrue(result.getOutput().contains("Hello world!"));
    }

    @Test
    @Launch(value = {"code-assist", "python"}, exitCode = 1)
    void testCodeAssistUnsupportedLanguage(LaunchResult result) {
        assertEquals(1, result.exitCode());
    }
}
