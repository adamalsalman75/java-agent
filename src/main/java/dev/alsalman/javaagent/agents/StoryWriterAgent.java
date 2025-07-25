package dev.alsalman.javaagent.agents;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.stereotype.Component;

@Component
public class StoryWriterAgent {

    private final ChatClient chatClient;

    public StoryWriterAgent(ChatClient.Builder builder) {
        this.chatClient = builder
                .defaultOptions(ChatOptions.builder()
                        .temperature(0.9)
                        .build())
                .build();
    }

    public String writeStory(String topic, String targetAudience, String marketTrends) {
        return chatClient.prompt()
                .user(String.format("Write an engaging story about: %s, targeting %s, considering the trend %s", topic, targetAudience, marketTrends))
                .call().content();
    }

    public String rewriteStory(String story, String feedback) {
        return chatClient.prompt()
                .user(String.format("Rewrite the following story: %s, based on the following feedback: %s", story, feedback))
                .call().content();
    }
}
