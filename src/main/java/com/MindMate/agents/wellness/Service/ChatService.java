package com.MindMate.agents.wellness.Service;

import com.MindMate.agents.escalation.RiskDetectionService;
import com.MindMate.agents.untils.ChatHistoryService;
import com.MindMate.agents.untils.MemoryService;
import com.MindMate.agents.wellness.ChatMessage;
import com.MindMate.model.account.User;
import com.MindMate.agents.wellness.Role;
import com.MindMate.agents.wellness.ChatRepo;
import com.MindMate.agents.escalation.RiskStatusRepo;
import com.MindMate.service.Utils.CurrentRoleService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ChatService {

    @Autowired
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
    @Autowired
    private ChatHistoryService chatHistoryService;

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
        StringBuilder prompt = chatHistoryService.getLast10Message(user);

        prompt.append("USER: ")
                .append(userMessage);

        StringBuilder fullResponse = new StringBuilder();

        Date age = user.getDob();

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
                        .param("age", calculateAge(user.getDob()))
                        .param("summary", summary)
                        .param("history", prompt.toString() )
                        .param("retrievedMemories", retrievedMemories)
                        .param("riskStatus", Objects.requireNonNull(riskDetectionService.getCurrentRiskStatus(user))))
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

    private int calculateAge(Date dob) {
        if (dob == null) return 25;

        LocalDate birthDate = dob.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        LocalDate currentDate = LocalDate.now();

        return Period.between(birthDate, currentDate).getYears();
    }


    public @Nullable Object loadChatHistory() {

        return null;
    }
}
