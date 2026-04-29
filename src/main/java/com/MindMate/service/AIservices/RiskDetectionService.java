package com.MindMate.service.AIservices;

import com.MindMate.model.ChatMessage;
import com.MindMate.model.RiskStatus;
import com.MindMate.model.account.User;
import com.MindMate.model.enums.RiskStatusLevel;
import com.MindMate.model.enums.Role;
import com.MindMate.repository.AIRepo.ChatRepo;
import com.MindMate.repository.AIRepo.RiskStatusRepo;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class RiskDetectionService {

    @Autowired
    private ChatClient chatClient;
    @Autowired
    private MemoryService memoryService;
    @Autowired
    private ChatRepo chatRepo;
    @Autowired
    private RiskStatusRepo riskStatusRepo;

    @Value("classpath:/prompts/AIWellnessExpert/risk-detection-template.st")
    private Resource template;

    private static final List<String> HIGH_RISK = List.of(
            "suicide",
            "suicidal",
            "kill myself",
            "want to die",
            "end my life",
            "better off dead",
            "don't want to live",
            "self harm"
    );

    private static final List<String> DISTRESS_FLAGS = List.of(
            "hopeless",
            "worthless",
            "panic attack",
            "can't go on",
            "completely alone"
    );


    @Async("taskExecutor")
    public void detectRisk(String userMessage, User user){

        try {

            if (detectHighRiskWords(userMessage)) {
                markRiskStatusAndSave(RiskStatusLevel.HIGH, user);
                return;
            }

            if (!shouldRunRiskCheck(userMessage, user.getId())) return;

            String summary = memoryService.getSummary(user.getId());

            List<ChatMessage> history = chatRepo.findTop10ByUserIdOrderByCreatedAtDesc(user.getId());
            Collections.reverse(history);
            StringBuilder pastMessages =
                    new StringBuilder();

            for (ChatMessage msg : history) {
                pastMessages.append(msg.getRole()).append(": ").append(msg.getContent()).append("\n");
            }

            String riskLevel = Objects.requireNonNull(chatClient.prompt()
                            .user(s -> s.text(template)
                                    .param("summary", summary)
                                    .param("history", pastMessages))
                            .call()
                            .content())
                    .trim()
                    .toUpperCase();

            RiskStatusLevel status = getRiskLevelStatus(riskLevel);

            markRiskStatusAndSave(status, user);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private RiskStatusLevel getRiskLevelStatus(String riskLevel){
        return switch (riskLevel) {
            case "LOW" -> RiskStatusLevel.LOW;
            case "MEDIUM" -> RiskStatusLevel.MEDIUM;
            case "HIGH" -> RiskStatusLevel.HIGH;
            default -> RiskStatusLevel.UNKNOWN;
        };
    }



    public boolean shouldRunRiskCheck(String userMessage, Long userId){

        // // CONDITION 1
        String lower= userMessage.toLowerCase();
        boolean b = DISTRESS_FLAGS.stream()
                .anyMatch(lower::contains);
        if(b) return true;

        // CONDITION 2
        long userMessageCount= chatRepo.countByUserIdAndRole(userId, Role.USER);
        return userMessageCount % 5 == 0;

    }


    public boolean detectHighRiskWords(String userMessage){
        String lower= userMessage.toLowerCase();
        return HIGH_RISK.stream()
                .anyMatch(lower::contains);
    }

    public void markRiskStatusAndSave(RiskStatusLevel currStatus, User user){
        RiskStatus existingRiskStatus = riskStatusRepo.findTopByUserOrderByCreatedAtDesc(user).orElseThrow(RuntimeException::new);

        if(existingRiskStatus.getRiskLevel() == currStatus) return;
        RiskStatus obj = new RiskStatus();
        obj.setRiskLevel(currStatus);
        obj.setUser(user);
        riskStatusRepo.save(obj);
    }
}
