package dev.langchain4j;

import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class Demotools {

    @Tool("Returns the current time in UTC as ISO-8601")
    public String currentTimeUtc() {
        return Instant.now().toString();
    }
}
