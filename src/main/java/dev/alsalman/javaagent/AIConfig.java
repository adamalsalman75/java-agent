package dev.alsalman.javaagent;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AIConfig {

    @Bean
    public JobStore jobStore() {
        return new JobStore();
    }
}
