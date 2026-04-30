package com.MindMate.agents.assessment.controller;

import com.MindMate.agents.assessment.dto.AssessmentSubmissionRequest;
import com.MindMate.agents.assessment.enums.AssessmentType;
import com.MindMate.agents.assessment.dto.AssessmentQuestionDto;
import com.MindMate.agents.assessment.model.Assessment;
import com.MindMate.agents.assessment.model.AssessmentResult;
import com.MindMate.agents.assessment.service.AssessmentEligibilityService;
import com.MindMate.agents.assessment.service.AssessmentResultService;
import com.MindMate.agents.assessment.service.AssessmentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/assessment")
public class AssessmentController {

    private final AssessmentService assessmentService;
    private final AssessmentResultService assessmentResultService;
    private final AssessmentEligibilityService assessmentEligibilityService;

    //getting assessment questions based on the type of assessment requested by user, user can only give assessment if they are eligible for it, eligibility is decided based on the user's past assessments and their results
    @GetMapping("/{type}")
    public ResponseEntity<List<AssessmentQuestionDto>>
    getAssessmentQuestions(@PathVariable AssessmentType type) {

        return ResponseEntity.ok(assessmentService.getAssessmentQuestions(type));
    }

//submitting assessment answers, user can only submit assessment if they are eligible for it, eligibility is decided based on the user's past assessments and their results
    @PostMapping("")
    public ResponseEntity<Boolean> submitAssessment(@RequestBody AssessmentSubmissionRequest submission){
        assessmentService.submitAssessment(submission);
        return ResponseEntity.ok(true);
    }

    //getting all the past assessment results of the user, user can only access their assessment results
    @GetMapping("/results")
    public ResponseEntity<List<AssessmentResult>> getMyAssessmentResults(){
        return ResponseEntity.ok(assessmentResultService.getMyAssessmentResults());
    }

    //checking if user is eligible for giving assessment, eligibility is decided based on the user's past assessments and their results
    @GetMapping("/isEligible")
    public ResponseEntity<Boolean> isEligibleForAssessment() {
        return ResponseEntity.ok(assessmentService.isEligibleForAssessment());
    }

    //getting all the available assessments in the system, this is not based on user's eligibility, it's just to show user what are the assessments available in the system
    @GetMapping("/getAvailableAssessments")
    public ResponseEntity<List<Assessment>> getAssessmentOptions() {
        return ResponseEntity.ok(assessmentService.getAssessments());
    }

    //getting recommended assessment for user based on their past assessments and their results, this is to guide user on which assessment they should take next for better understanding of their mental state and better recommendations from the system
    @GetMapping("/getRecommendedAssessment")
    public ResponseEntity<List<Assessment>> getRecommendedAssessment() {
        return ResponseEntity.ok(assessmentService.getRecommendedAssessment());
    }

}
