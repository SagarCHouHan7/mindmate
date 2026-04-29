package com.MindMate.service.AIservices;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class MemoryExtractionService {

    private final ChatClient chatClient;
    private final MemoryVectorService memoryVectorService;

    public MemoryExtractionService(ChatClient chatClient, MemoryVectorService memoryVectorService){
        this.chatClient=chatClient;
        this.memoryVectorService= memoryVectorService;
    }



    public void extractAndStoreMemory(
            Long userId,
            String userMessage,
            String aiReply
    ){

        String extractionPrompt = """
Convert conversation into ONE structured memory fact.

Rules:
- Store as third-person memory about the user.
- Compress into a durable reusable fact.
- Do NOT quote the user.
- Do NOT copy conversation.
- Max 15 words.
- If nothing worth remembering return NONE.

Examples

User:
Watching good movies helps me feel less lonely.

Output:
User feels less lonely after watching uplifting movies


User:
I had pizza today.

Output:
NONE


Conversation:
User: %s

Assistant: %s
""".formatted(
                userMessage,
                aiReply
        );


        String memory=
                chatClient.prompt()
                        .user(extractionPrompt)
                        .call()
                        .content();


        if(memory==null ||
                memory.isBlank() ||
                memory.equalsIgnoreCase("NONE")){
            return;
        }


        memoryVectorService.saveMemory(
                userId,
                memory,
                "personal"
        );

    }

}