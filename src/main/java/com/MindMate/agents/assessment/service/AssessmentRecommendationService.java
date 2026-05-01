package com.MindMate.agents.assessment.service;

import com.MindMate.agents.assessment.Repo.AssessmentRecommendationRepo;
import com.MindMate.agents.assessment.Repo.AssessmentRepo;
import com.MindMate.agents.assessment.enums.AssessmentRecommendationResponse;
import com.MindMate.agents.assessment.enums.AssessmentType;
import com.MindMate.agents.assessment.model.Assessment;
import com.MindMate.agents.assessment.model.AssessmentRecommendation;
import com.MindMate.agents.escalation.RiskDetectionService;
import com.MindMate.agents.escalation.RiskStatus;
import com.MindMate.agents.untils.ChatHistoryService;
import com.MindMate.agents.untils.MemoryService;
import com.MindMate.model.account.User;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AssessmentRecommendationService {

    private final AssessmentRecommendationRepo assessmentRecommendationRepo;
    private final AssessmentRepo assessmentRepo;
    private final MemoryService memoryService;
    private final ChatHistoryService chatHistoryService;
    private final RiskDetectionService riskDetectionService;
    private final ChatClient chatClient;
    private final ObjectMapper objectMapper;

    @Value("classpath:/prompts/assessment-recommendation-template.st")
    private Resource recommendationTemplate;

    @Async("taskExecutor")
    public void generateRecommendedAssessment(User user){

        String longTermHistory = memoryService.getSummary(user.getId());
        StringBuilder shortTermHistory = chatHistoryService.getLast10Message(user);
        RiskStatus riskStatus = riskDetectionService.getCurrentRiskStatus(user);

        //use the above information to determine the most suitable assessment for the user at this point of time

        try {
            String response = chatClient.prompt()
                    .user(s -> s.text(recommendationTemplate)
                            .param("longTermHistory", longTermHistory)
                            .param("shortTermHistory", shortTermHistory)
                            .param("riskStatus", riskStatus.getRiskLevel().name())
                    )
                    .call()
                    .content();

            assert response != null;
            String json = extractJson(response);

            AssessmentRecommendationResponse aiResponse =
                    objectMapper.readValue(json, AssessmentRecommendationResponse.class);

            List<AssessmentType> types = aiResponse.recommendedAssessments();

            List<Assessment> assessmentList = assessmentRepo.findByTypeIn(types);

            saveRecommendationInDB(assessmentList, user);

        } catch (Exception e) {
            // fallback (important)
            List<Assessment> assessmentList =  assessmentRepo.findByTypeIn(List.of(AssessmentType.STRESS));
            saveRecommendationInDB(assessmentList, user);
            e.printStackTrace();
        }

    }

    private String extractJson(String raw) {
        if (raw == null || raw.isBlank()) {
            throw new IllegalArgumentException("Empty AI response");
        }

        int start = raw.indexOf('{');
        int end = raw.lastIndexOf('}');

        if (start == -1 || end == -1 || end < start) {
            throw new IllegalArgumentException("Invalid JSON: " + raw);
        }

        return raw.substring(start, end + 1);
    }

    private void saveRecommendationInDB(List<Assessment> assessmentList, User user){
        AssessmentRecommendation recommendation = new AssessmentRecommendation();
        recommendation.setUserId(user.getId());
        recommendation.setAssessmentList(assessmentList);
        recommendation.setRecommendedOn(LocalDateTime.now());
        assessmentRecommendationRepo.save(recommendation);
    }
}
