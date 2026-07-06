package dev.langchain4j;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Scanner;

@SpringBootApplication
public class ChatMemoryOracleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatMemoryOracleApplication.class, args);
    }

    @Bean
    CommandLineRunner chatRunner(Assistant assistant) {
        return args -> {
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
        };
    }
}
