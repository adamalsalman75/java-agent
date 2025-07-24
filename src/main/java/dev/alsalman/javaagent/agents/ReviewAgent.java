package dev.alsalman.javaagent.agents;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

@Component
public class ReviewAgent {

    private final ChatClient chatClient;

    public ReviewAgent(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public String reviewStory(String story) {
        return chatClient.prompt()
                .user("Review the following story and provide feedback. If the story is good, please include the word \"happy\" in your response: " + story)
                .call().content();
    }
}
