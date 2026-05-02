package com.MindMate.agents.wellness.Service;

import com.MindMate.agents.RedisChatService;
import com.MindMate.agents.escalation.RiskDetectionService;
import com.MindMate.agents.untils.ChatHistoryService;
import com.MindMate.agents.untils.MemoryService;
import com.MindMate.agents.wellness.ChatMessage;
import com.MindMate.dto.PageResponseDto;
import com.MindMate.model.account.User;
import com.MindMate.agents.wellness.Role;
import com.MindMate.agents.wellness.ChatRepo;
import com.MindMate.agents.escalation.RiskStatusRepo;
import com.MindMate.service.Utils.CurrentRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.*;

@Slf4j
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
    @Autowired
    private RedisChatService redisChatService;

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

        redisChatService.saveMessage(
                user.getId(),
                Role.USER.toString(),
                userMessage
        );


        // Load memory
        log.info("Loading chat history for user {} from DB", user.getUsername());
        StringBuilder prompt = chatHistoryService.getLast10Message(user);
        log.info("Loaded chat history from DB:\n{}", prompt.toString());
        //load memory from redis
        StringBuilder redisPrompt = chatHistoryService.getLast10MessageFromRedis(user);
        redisPrompt.append("USER: ")
                .append(userMessage)
                .append("\n");

        System.out.println(redisPrompt.toString());
        log.info("Loaded chat history from Redis:\n{}", redisPrompt.toString());

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
                        //.param("history", prompt.toString() )
                        .param("history", redisPrompt.toString())
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

                    redisChatService.saveMessage(
                            user.getId(),
                            Role.ASSISTANT.toString(),
                            fullResponse.toString()
                    );

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


    public PageResponseDto<ChatMessage> loadChatHistory(int page) {

        User user = currentRoleService.getCurrentUser();

        int size = 10;

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("createdAt").descending()
        );

        Page<ChatMessage> messagePage =
                chatRepo.findByUserOrderByCreatedAtDesc(user, pageable);


        List<ChatMessage> messages = new ArrayList<>(messagePage.getContent());

        // Debug
        messages.forEach(x -> System.out.println(x.getContent()));


        Collections.reverse(messages);

        PageResponseDto<ChatMessage> response = new PageResponseDto<>();
        response.setContent(messages);
        response.setPage(messagePage.getNumber());
        response.setSize(messagePage.getSize());
        response.setTotalPages(messagePage.getTotalPages());
        response.setTotalElements(messagePage.getTotalElements());
        response.setLast(messagePage.isLast());

        return response;
    }
}
