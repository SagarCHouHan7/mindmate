package com.MindMate.service.AIservices;

import com.MindMate.model.ChatMessage;
import com.MindMate.model.RiskStatus;
import com.MindMate.model.account.User;
import com.MindMate.model.enums.RiskStatusLevel;
import com.MindMate.model.enums.Role;
import com.MindMate.repository.AIRepo.ChatRepo;
import com.MindMate.repository.AIRepo.RiskStatusRepo;
import com.MindMate.service.Utils.CurrentRoleService;
import org.jspecify.annotations.Nullable;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class ChatService {

    private ChatClient chatClient;
    @Autowired
    private ChatRepo chatRepo;
    @Autowired
    private CurrentRoleService currentRoleService;
    @Autowired
    private MemoryService memoryService;
    @Autowired
    private RagService ragService;
    @Autowired
    private MemoryExtractionService memoryExtractionService;
    @Autowired
    private RiskStatusRepo riskStatusRepo;
    @Autowired
    private RiskDetectionService riskDetectionService;

    @Value("classpath:/prompts/AIWellnessExpert/chat-guide.st")
    private Resource systemGuide;

    public ChatService( ChatClient chatClient){
        this.chatClient = chatClient;
    }

    public Flux<String> chat(String userMessage){

        User user = currentRoleService.getCurrentUser();

        // Save user message
        ChatMessage userMsg = new ChatMessage();
        userMsg.setUser(user);
        userMsg.setRole(Role.USER);
        userMsg.setContent(userMessage);

        chatRepo.save(userMsg);


        // Load memory
        List<ChatMessage> history =
                chatRepo.findTop10ByUserIdOrderByCreatedAtDesc(user.getId());
        Collections.reverse(history);

        StringBuilder prompt =
                new StringBuilder();

        for(ChatMessage msg : history){
            prompt.append(msg.getRole()).append(": ").append(msg.getContent()).append("\n");
        }

        prompt.append("USER: ")
                .append(userMessage);

        StringBuilder fullResponse = new StringBuilder();

        Date age = user.getDob();
        Date dob = (age == null) ? java.sql.Date.valueOf("2005-01-01") : age;

        String summary = memoryService.getSummary(user.getId());
        String ragMemory =
                ragService.retrieveMemories(
                        user.getId(),
                        userMessage
                );
        String retrievedMemories = ragMemory.isBlank() ? "No relevant memories found" : ragMemory;

//        System.out.println(retrievedMemories);
//        System.out.println(summary);
//        System.out.println(prompt.toString());

        return chatClient.prompt()
                .user(s->s.text(systemGuide)
                        .param("username", user.getUsername())
                        .param("dob", dob)
                        .param("summary", summary)
                        .param("history", prompt.toString() )
                        .param("retrievedMemories", retrievedMemories))
                .stream()
                .content()
                .doOnNext(fullResponse::append)
                .doOnComplete( ()->{
                    ChatMessage message = new ChatMessage();
                    message.setRole(Role.ASSISTANT);
                    message.setUser(user);
                    message.setContent(String.valueOf(fullResponse));
                    chatRepo.save(message);

                    memoryService.updateSummary(
                            user.getId(),
                            userMessage,
                            fullResponse.toString()
                    );

                    memoryExtractionService.extractAndStoreMemory(user.getId(), userMessage, fullResponse.toString());

                    riskDetectionService.detectRisk(userMessage, user);

                });

    }

    public @Nullable RiskStatus getRiskStatus() {
        User user = currentRoleService.getCurrentUser();
        return riskStatusRepo.findTopByUserOrderByCreatedAtDesc(user).orElseThrow(RuntimeException::new);
    }
}
