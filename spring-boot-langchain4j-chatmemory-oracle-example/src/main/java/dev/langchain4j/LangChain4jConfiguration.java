package dev.langchain4j;

import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class LangChain4jConfiguration {

    @Bean
    ChatModel ollamaChatModel(
            @Value("${langchain4j.ollama.chat-model.base-url}") String baseUrl,
            @Value("${langchain4j.ollama.chat-model.model-name}") String modelName) {

        return OllamaChatModel.builder()
                .baseUrl(baseUrl)
                .modelName(modelName)
                .build();
    }

    @Bean
    ChatMemory chatMemory(
            ChatMemoryStore chatMemoryStore,
            @Value("${langchain4j.chat-memory.max-messages:20}") int maxMessages) {

        return MessageWindowChatMemory.builder()
                .id("cli")
                .maxMessages(maxMessages)
                .chatMemoryStore(chatMemoryStore)
                .build();
    }
}
