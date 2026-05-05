package org.acme;

import io.quarkus.picocli.runtime.annotations.TopCommand;

import jakarta.inject.Inject;

import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@TopCommand
@Command(name = "mcp", subcommands = {
        McpCommand.ListTools.class,
        McpCommand.ToLowerCase.class,
        McpCommand.Answer.class,
        McpCommand.CodeAssist.class
})
public class McpCommand {

    @Command(name = "list-tools", description = "List available tools on the MCP server")
    static class ListTools implements Runnable {

        @Inject
        McpServerClient client;

        @Override
        public void run() {
            client.listTools().forEach(tool ->
                    System.out.println(tool.name() + " - " + tool.description()));
        }
    }

    @Command(name = "toLowerCase", description = "Convert a string to lower case")
    static class ToLowerCase implements Runnable {

        @Inject
        McpServerClient client;

        @Parameters(index = "0", description = "The value to convert")
        String value;

        @Override
        public void run() {
            System.out.println(client.toLowerCase(value));
        }
    }

    @Command(name = "answer", description = "Get an answer to a question")
    static class Answer implements Runnable {

        @Inject
        McpServerClient client;

        @Parameters(index = "0", description = "The question to answer")
        String question;

        @Override
        public void run() {
            System.out.println(client.answer(question));
        }
    }

    @Command(name = "code-assist", description = "Get code assistance for a language")
    static class CodeAssist implements Runnable {

        @Inject
        McpServerClient client;

        @Parameters(index = "0", description = "The programming language")
        String language;

        @Override
        public void run() {
            System.out.println(client.codeAssist(language));
        }
    }
}
