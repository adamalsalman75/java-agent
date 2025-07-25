package dev.alsalman.javaagent.agents;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.stereotype.Component;

@Component
public class TargetAudienceAgent {

    private final ChatClient chatClient;

    public TargetAudienceAgent(ChatClient.Builder builder) {
        this.chatClient =  builder.
                defaultOptions(ChatOptions.builder()
                        .temperature(0.2)
                        .build())
                .build();
    }

    public String getTargetAudience(String topic) {
        return chatClient.prompt()
                .user("Analyze the target audience for stories about: " + topic)
                .call().content();
    }
}
