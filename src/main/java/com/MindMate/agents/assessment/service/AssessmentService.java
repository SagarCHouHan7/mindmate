package com.MindMate.agents.assessment.service;

import com.MindMate.agents.assessment.Repo.AssessmentRecommendationRepo;
import com.MindMate.agents.assessment.Repo.AssessmentRepo;
import com.MindMate.agents.assessment.dto.AssessmentSubmissionRequest;
import com.MindMate.agents.assessment.model.Assessment;
import com.MindMate.agents.assessment.model.AssessmentRecommendation;
import com.MindMate.model.account.User;

import com.MindMate.agents.assessment.enums.AssessmentType;
import com.MindMate.agents.assessment.Repo.AssessmentQuestionRepo;
import com.MindMate.agents.assessment.dto.AssessmentQuestionDto;
import com.MindMate.service.Utils.CurrentRoleService;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AssessmentService {

    private final AssessmentQuestionRepo assessmentQuestionRepo;
    private final CurrentRoleService currentRoleService;
    private final AssessmentEligibilityService assessmentEligibilityService;
    private final AssessmentResultService assessmentResultService;
    private final AssessmentRepo assessmentRepo;
    private final AssessmentRecommendationService assessmentRecommendationService;
    private final AssessmentRecommendationRepo assessmentRecommendationRepo;
    public List<AssessmentQuestionDto> getAssessmentQuestions(AssessmentType type){

        User user = currentRoleService.getCurrentUser();
        if(!assessmentEligibilityService.isEligible(user))
            throw new RuntimeException("you're not eligible for giving assessment at this point of time");

        return assessmentQuestionRepo.findByAssessmentTypeOrderByQuestionOrder(type)
                .stream()
                .map(q->new AssessmentQuestionDto(
                        q.getId(),
                        q.getQuestionOrder(),
                        q.getQuestion()
                ))
                .toList();
    }

   public void submitAssessment(AssessmentSubmissionRequest submission){

        User user = currentRoleService.getCurrentUser();
        if(!assessmentEligibilityService.isEligible(user))
            throw new RuntimeException("you're not eligible for giving assessment at this point of time");

       //async processing of assessment result to avoid making user wait for the response
        assessmentResultService.getAssessmentResultAndStore(submission, user);
    }


    public @Nullable Boolean isEligibleForAssessment() {
        User user = currentRoleService.getCurrentUser();
        return assessmentEligibilityService.isEligible(user);
    }

    public @Nullable List<Assessment> getAssessments() {
        return assessmentRepo.findAll();
    }

    public @Nullable List<Assessment> getRecommendedAssessment() {
        User user = currentRoleService.getCurrentUser();

        Optional<AssessmentRecommendation> recommendedAssessments = assessmentRecommendationRepo.findById(user.getId());

        if(recommendedAssessments.isEmpty()){

            if(!assessmentEligibilityService.isEligible(user))
                return null;
            //async call to generate assessment recommendation for the user based on their risk status and other factors
            assessmentRecommendationService.generateRecommendedAssessment(user);
            return null;
        }
        assessmentRecommendationService.generateRecommendedAssessment(user);
        return recommendedAssessments.get().getAssessmentList();
    }
}
