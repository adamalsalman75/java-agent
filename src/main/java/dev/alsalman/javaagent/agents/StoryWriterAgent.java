package dev.alsalman.javaagent.agents;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

@Component
public class StoryWriterAgent {

    private final ChatClient chatClient;

    public StoryWriterAgent(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public String writeStory(String topic, String targetAudience, String marketTrends) {
        return chatClient.prompt()
                .user(String.format("Write an engaging story about: %s, targeting %s, considering the trend %s", topic, targetAudience, marketTrends))
                .call().content();
    }
}
