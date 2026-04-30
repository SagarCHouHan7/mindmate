package com.MindMate.agents.assessment.service;

import com.MindMate.agents.assessment.Repo.AssessmentQuestionRepo;
import com.MindMate.agents.assessment.Repo.AssessmentResultRepo;
import com.MindMate.agents.assessment.dto.AssessmentAnswerDto;
import com.MindMate.agents.assessment.dto.AssessmentResultJson;
import com.MindMate.agents.assessment.dto.AssessmentSubmissionRequest;
import com.MindMate.agents.assessment.model.AssessmentQuestion;
import com.MindMate.agents.assessment.model.AssessmentResult;
import com.MindMate.agents.untils.MemoryService;
import com.MindMate.model.account.User;
import com.MindMate.service.Utils.CurrentRoleService;
import org.jspecify.annotations.Nullable;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class AssessmentResultService {

    @Autowired
    private MemoryService memoryService;
    @Autowired
    private ChatClient chatClient;
    @Autowired
    private AssessmentQuestionRepo assessmentQuestionRepo;
    @Autowired
    private AssessmentResultRepo assessmentResultRepo;
    @Autowired
    private CurrentRoleService currentRoleService;

    @Value("classpath:/prompts/wellness-assessment-interpretation-template.st")
    private Resource interpretationTemplate;

    @Async("taskExecutor")
    public void getAssessmentResultAndStore(AssessmentSubmissionRequest submission, User user) {

        System.out.println("result evolution begins");
        try {
            if (submission == null || submission.getAnswers() == null || submission.getAnswers().isEmpty()) {
                return;
            }

            int totalScore = submission.getAnswers()
                    .stream()
                    .mapToInt(AssessmentAnswerDto::getScore)
                    .sum();

            String deterministicSeverity = calculateSeverity(totalScore);

            String summary = memoryService.getSummary(user.getId());

            StringBuilder assessmentResponses = new StringBuilder();

            // Step 1: Collect all questionIds
            List<Long> questionIds = submission.getAnswers()
                    .stream()
                    .map(AssessmentAnswerDto::getQuestionId)
                    .filter(Objects::nonNull)
                    .toList();

            // Step 2: Fetch all questions in one query
            Map<Long, AssessmentQuestion> questionMap = assessmentQuestionRepo
                    .findAllById(questionIds)
                    .stream()
                    .collect(Collectors.toMap(AssessmentQuestion::getId, Function.identity()));

            // Step 3: Build response
            for (AssessmentAnswerDto answerDto : submission.getAnswers()) {
                Long questionId = answerDto.getQuestionId();
                // get assessmentOption for the answerDto.getScore() if needed, assuming you have a method to do that

                AssessmentQuestion question = questionMap.get(questionId);
                if (question != null) {
                    assessmentResponses.append("Question: ")
                            .append(question.getQuestion())
                            .append("\n");

                    assessmentResponses.append("Answer: ")
                            .append(mapScoreToText(answerDto.getScore()))
                            .append(" (")
                            .append(answerDto.getScore())
                            .append(")")
                            .append("\n\n");

                }
            }
            System.out.println(assessmentResponses.toString());

            String response = chatClient.prompt()
                    .user(u -> u.text(interpretationTemplate)
                            .param("summary", summary)
                            .param("assessment_responses", assessmentResponses.toString())
                            .param("assessment_type", submission.getAssessmentType().name()))
                    .call()
                    .content();

            System.out.println("raw AI response : " + response);
            String json = extractJson(response);
            ObjectMapper mapper = new ObjectMapper();
            AssessmentResultJson result = mapper.readValue(json, AssessmentResultJson.class);
            System.out.println("final object : " +result.toString());
            result.setSeverity(deterministicSeverity); // Override severity with deterministic value based on total score

            saveResultInDB(result, submission, user, totalScore);




        } catch (Exception e) {
//            saveResultInDB(
//                    new AssessmentResultJson("unKnown", "Unknown", true, "Unknown"),
//                    submission,
//                    user,
//                    0);
            throw new RuntimeException(e);

        }
    }

    private String extractJson(String raw) {
        if (raw == null || raw.isBlank()) {
            throw new IllegalArgumentException("AI response is empty");
        }

        int start = raw.indexOf('{');
        int end = raw.lastIndexOf('}');

        if (start == -1 || end == -1 || end < start) {
            throw new IllegalArgumentException("No valid JSON object found in AI response: " + raw);
        }

        return raw.substring(start, end + 1);
    }

    private void saveResultInDB(AssessmentResultJson result, AssessmentSubmissionRequest submission, User user, int totalScore){
        AssessmentResult assessmentResult = new AssessmentResult();
        assessmentResult.setUser(user);
        assessmentResult.setAssessmentType(submission.getAssessmentType());
        assessmentResult.setTotalScore(totalScore);
        assessmentResult.setSeverity(result.getSeverity());
        assessmentResult.setAiInterpretation(result.getInterpretation());
        assessmentResult.setRecommendationSummary(result.getRecommendationSummary());
        assessmentResult.setExpertConsultationRequired(result.getExpertConsultationRequired());

        assessmentResultRepo.save(assessmentResult);

    }

    private String mapScoreToText(int score) {
        return switch (score) {
            case 0 -> "NEVER";
            case 1 -> "RARELY";
            case 2 -> "SOMETIMES";
            case 3 -> "OFTEN";
            case 4 -> "VERY_OFTEN";
            default -> "UNKNOWN";
        };
    }

    private String calculateSeverity(int totalScore) {
        if (totalScore <= 5) return "LOW";
        else if (totalScore <= 10) return "MILD";
        else if (totalScore <= 15) return "MODERATE";
        else return "HIGH";
    }


    public @Nullable List<AssessmentResult> getMyAssessmentResults() {

        User user = currentRoleService.getCurrentUser();

        return assessmentResultRepo.findAllByUser(user);
    }
}
