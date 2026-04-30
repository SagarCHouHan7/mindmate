package com.MindMate.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.api.OllamaChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AIConfig {

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder){
        return builder
                .defaultOptions(
                        OllamaChatOptions
                                .builder()
                                .temperature(0.7)
                                .build()
                        )
                .build();
    }

//OllamaOptions
//        .builder()
//        .temperature(0.4)
//                                .build()
}
