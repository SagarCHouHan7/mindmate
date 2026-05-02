package com.MindMate.agents.lifestyle;

import com.MindMate.agents.escalation.RiskDetectionService;
import com.MindMate.agents.escalation.RiskStatusLevel;
import com.MindMate.agents.untils.ChatHistoryService;
import com.MindMate.agents.untils.MemoryService;
import com.MindMate.model.account.User;
import com.MindMate.service.Utils.CurrentRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class RoutineService {

    private final CurrentRoleService currentRoleService;
    private final ChatClient chatClient;
    private final MemoryService memoryService;
    private final ChatHistoryService chatHistoryService;
    private final RiskDetectionService riskDetectionService;
    private final ObjectMapper objectMapper;
    private final LifestyleRoutineRepo lifestyleRoutineRepo;


    @Value("classpath:/prompts/routine-generation-template.st")
    private Resource routineTemplate;

    @Async("taskExecutor")
    public void generateRoutine(UserRoutineInput input) {

        User user = currentRoleService.getCurrentUser();

        String longTermSummary = memoryService.getSummary(user.getId());
        String shorTermSummary = chatHistoryService.getLast10Message(user).toString();
        RiskStatusLevel riskStatus = riskDetectionService.getCurrentRiskStatus(user).getRiskLevel();


        String response = chatClient.prompt()
                .user(u -> u.text(routineTemplate)
                        .param("name", user.getUsername())
                        .param("age", calculateAge(user.getDob()))
                        .param("longTerm", longTermSummary)   // from memory
                        .param("shortTerm", shorTermSummary)  // from chat history
                        .param("risk", riskStatus.name())       // from risk service

                        .param("wakeUp", input.wakeUpTime())
                        .param("sleep", input.sleepTime())
                        .param("routine", input.dailyRoutineDescription())
                )
                .call()
                .content();

        String json = extractJson(response);

        LifestyleRoutine routine;

        try {
            routine = objectMapper.readValue(json, LifestyleRoutine.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse AI response", e);
        }
        routine.setUserId(user.getId());
        System.out.println(routine);
        lifestyleRoutineRepo.save(routine);
    }

    public LifestyleRoutine getRoutine(){
        User user = currentRoleService.getCurrentUser();
        return lifestyleRoutineRepo.findByUserId(user.getId()).orElse(null);
    }

    private int calculateAge(Date dob) {
        if (dob == null) return 25;

        LocalDate birthDate = dob.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        LocalDate currentDate = LocalDate.now();

        return Period.between(birthDate, currentDate).getYears();
    }

    private String extractJson(String raw) {

        int start = raw.indexOf('{');
        int end = raw.lastIndexOf('}');

        if (start == -1 || end == -1) {
            throw new RuntimeException("Invalid JSON from AI");
        }

        return raw.substring(start, end + 1);
    }
}
