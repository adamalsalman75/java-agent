package dev.alsalman.javaagent;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class AIConfig {

    @Bean
    @Primary
    @ConfigurationProperties("spring.ai.openai.chat")
    public OpenAiChatOptions writerChatOptions() {
        return new OpenAiChatOptions();
    }

    @Bean
    @ConfigurationProperties("spring.ai.openai.chat-review")
    public OpenAiChatOptions reviewerChatOptions() {
        return new OpenAiChatOptions();
    }

    @Bean
    public JobStore jobStore() {
        return new JobStore();
    }

    @Bean
    public ChatClient writerChatClient(ChatClient.Builder builder, @Qualifier("writerChatOptions") OpenAiChatOptions options) {
        return builder.defaultOptions(options).build();
    }

    @Bean
    public ChatClient reviewerChatClient(ChatClient.Builder builder, @Qualifier("reviewerChatOptions") OpenAiChatOptions options) {
        return builder.defaultOptions(options).build();
    }
}
