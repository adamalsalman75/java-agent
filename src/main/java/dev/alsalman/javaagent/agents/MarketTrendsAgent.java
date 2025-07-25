package dev.alsalman.javaagent.agents;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.stereotype.Component;

@Component
public class MarketTrendsAgent {

    private final ChatClient chatClient;

    public MarketTrendsAgent(ChatClient.Builder builder) {
        this.chatClient =  builder.
            defaultOptions(ChatOptions.builder()
                    .temperature(0.2)
                    .build())
                    .build();
    }

    public String getMarketTrends(String topic) {
        return chatClient.prompt()
                .user("What are current market trends for stories about: " + topic)
                .call().content();
    }
}
