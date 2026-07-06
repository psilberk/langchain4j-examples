package dev.langchain4j;
import dev.langchain4j.data.message.CustomMessage;
import java.sql.SQLException;
import java.util.Map;
import java.util.Scanner;

import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.memory.chat.oracle.OracleChatMemoryStore;
import dev.langchain4j.store.memory.chat.oracle.OracleChatMemoryStore.ContentColumnType;

public class Main {
    public static void main(String[] args) throws SQLException {

        ChatModel model = OllamaChatModel.builder()
                .baseUrl(System.getenv("OLLAMA_BASE_URL"))
                .modelName("qwen3:8b")
                .build();

        // Create a memory store backed by Oracle DB using wallet-based datasource/connection

        OracleChatMemoryStore memoryStore = OracleChatMemoryStore.builder()
                .dataSource(OracleWalletDataSourceFactory.createconnection())
                .tableName("CHAT_MEMORY_JSON")
                .contentColumnType(ContentColumnType.JSON)
                .createTable()
                .build();


        // A stable identifier for the conversation session/user.

        String memoryId = "user123-session0";

        // In-memory window on top of the persistent store

        ChatMemory chatMemory = MessageWindowChatMemory.builder()
                .id(memoryId)
                .maxMessages(20)
                .chatMemoryStore(memoryStore)
                .build();


        // Build an AI service that maps assistant interface methods to LLM calls
        Assistant assistant = AiServices.builder(Assistant.class)
                .chatModel(model)
                .chatMemory(chatMemory)
                .tools(new Demotools())
                .build();

        CustomMessage customMessage = CustomMessage.from(Map.of(
                "event", "TOOL_EXECUTION_RESULT",
                "toolCallId", "tool_call_001",
                "isError", true,
                "error", "Tool timeout",
                "fallbackQuestion", "I could not run the tool. Do you want me to continue without it?"
        ));
        System.out.println("Sample CustomMessage: " + customMessage);
        chatMemory.add(customMessage);

        System.out.println("Chat started. Type your message and press Enter.");
        System.out.println("Type 'exit' or 'quit' to stop.");

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.print("You: ");
                if (!scanner.hasNextLine()) {
                    break;
                }
                String userInput = scanner.nextLine().trim();
                if (userInput.isEmpty()) {
                    continue;
                }
                if ("exit".equalsIgnoreCase(userInput) || "quit".equalsIgnoreCase(userInput)) {
                    System.out.println("Bye.");
                    break;
                }

                String answer = assistant.chat(userInput);
                System.out.println("Bot: " + answer);
            }
        }
    }
}
