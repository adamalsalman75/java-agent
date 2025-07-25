package dev.alsalman.javaagent.agents;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.stereotype.Component;

@Component
public class ReviewAgent {

    private final ChatClient chatClient;

    public ReviewAgent(ChatClient.Builder builder) {
        this.chatClient = builder
                .defaultSystem("Respond with a JSON object containing two fields: 'happy' (a boolean indicating if you are happy with the story) and 'feedback' (a string containing your review)")
                .defaultOptions(ChatOptions.builder()
                        .temperature(0.2)
                        .build())
                .build();
    }

    public Review reviewStory(String story) {
        return chatClient.prompt()
                .user("Review the following story and provide feedback: " + story)
                .call().entity(Review.class);
    }
}
