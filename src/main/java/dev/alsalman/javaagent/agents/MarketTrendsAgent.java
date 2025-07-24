package dev.alsalman.javaagent.agents;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

@Component
public class MarketTrendsAgent {

    private final ChatClient chatClient;

    public MarketTrendsAgent(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public String getMarketTrends(String topic) {
        return chatClient.prompt()
                .user("What are current market trends for stories about: " + topic)
                .call().content();
    }
}
