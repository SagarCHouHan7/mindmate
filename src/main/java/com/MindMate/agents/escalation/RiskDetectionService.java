package com.MindMate.agents.escalation;

import com.MindMate.agents.untils.ChatHistoryService;
import com.MindMate.agents.untils.MemoryService;
import com.MindMate.model.account.User;
import com.MindMate.agents.wellness.Role;
import com.MindMate.agents.wellness.ChatRepo;
import com.MindMate.service.Utils.CurrentRoleService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RiskDetectionService {

    private final ChatClient chatClient;
    private final MemoryService memoryService;
    private final ChatRepo chatRepo;
    private final RiskStatusRepo riskStatusRepo;
    private final CurrentRoleService currentRoleService;
    private final ChatHistoryService chatHistoryService;


    @Value("classpath:/prompts/AIWellnessExpert/risk-detection-template.st")
    private Resource template;

    private static final List<String> SEVERE_RISK = List.of(
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


//    @Async("taskExecutor")
//    public void detectRisk(String userMessage, User user){
//
//        System.out.println("Running risk detection for user: " + user.getId() + " with message: " + userMessage);
//        try {
//
//            if (detectHighRiskWords(userMessage)) {
//                markRiskStatusAndSave(RiskStatusLevel.SEVERE, user);
//                return;
//            }
//
//            if (!shouldRunRiskCheck(userMessage, user.getId())) return;
//
//            String summary = memoryService.getSummary(user.getId());
//
//            StringBuilder pastMessages = chatHistoryService.getLast10Message(user);
//
//
//            String riskLevel = Objects.requireNonNull(chatClient.prompt()
//                            .user(s -> s.text(template)
//                                    .param("summary", summary)
//                                    .param("history", pastMessages.toString())
//                                    .param("risk_status", Objects.requireNonNull(getCurrentRiskStatus(user)).getRiskLevel().name()))
//                            .call()
//                            .content())
//                    .trim()
//                    .toUpperCase();
//
//            RiskStatusLevel status = getRiskLevelStatus(riskLevel);
//
//            markRiskStatusAndSave(status, user);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }

    @Async("taskExecutor")
    public void detectRisk(String userMessage, User user){

        System.out.println("Running risk detection for user: " + user.getId());

        try {

            String summary = memoryService.getSummary(user.getId());
            String history = chatHistoryService.getLast10Message(user).toString();

            RiskStatus currentStatus = getCurrentRiskStatus(user);

            String response = chatClient.prompt()
                    .user(s -> s.text(template)
                            .param("summary", summary)
                            .param("history", history)
                            .param("risk_status", currentStatus.getRiskLevel().name()))
                    .call()
                    .content();

            if (response == null || response.isBlank()) return;

            RiskStatusLevel newStatus = parseRiskLevel(response);

            markRiskStatusAndSave(newStatus, user);

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private RiskStatusLevel getRiskLevelStatus(String riskLevel){
        return switch (riskLevel) {
            case "SEVERE" -> RiskStatusLevel.SEVERE;
            case "LOW" -> RiskStatusLevel.LOW;
            case "MEDIUM" -> RiskStatusLevel.MODERATE;
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
        return SEVERE_RISK.stream()
                .anyMatch(lower::contains);
    }

    public void markRiskStatusAndSave(RiskStatusLevel currStatus, User user){

        Optional<RiskStatus> optional =
                riskStatusRepo.findTopByUserOrderByCreatedAtDesc(user);

        if(optional.isPresent() &&
                optional.get().getRiskLevel() == currStatus){
            return;
        }

        RiskStatus obj = new RiskStatus();
        obj.setRiskLevel(currStatus);
        obj.setUser(user);
        riskStatusRepo.save(obj);
        System.out.println("Risk status updated to " + currStatus + " for user: " + user.getId());
    }

    public @Nullable RiskStatus getCurrentRiskStatus() {
        User user = currentRoleService.getCurrentUser();
        return getCurrentRiskStatus(user);
    }

    public @Nullable RiskStatus getCurrentRiskStatus(User user) {
        Optional<RiskStatus> currentRiskStatus = riskStatusRepo.findTopByUserOrderByCreatedAtDesc(user);
        if(currentRiskStatus.isPresent()) return currentRiskStatus.get();

        RiskStatus obj = new RiskStatus();
        obj.setRiskLevel(RiskStatusLevel.UNKNOWN);
        obj.setUser(user);
        return riskStatusRepo.save(obj);
    }

    private RiskStatusLevel parseRiskLevel(String response){

        String r = response.trim().toUpperCase();

        if(r.contains("SEVERE")) return RiskStatusLevel.SEVERE;
        if(r.contains("HIGH")) return RiskStatusLevel.HIGH;
        if(r.contains("MEDIUM")) return RiskStatusLevel.MODERATE;
        if(r.contains("LOW")) return RiskStatusLevel.LOW;

        return RiskStatusLevel.UNKNOWN;
    }
}
