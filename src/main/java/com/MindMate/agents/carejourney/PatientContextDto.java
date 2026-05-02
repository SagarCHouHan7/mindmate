package com.MindMate.agents.carejourney;

import com.MindMate.agents.assessment.model.AssessmentResult;

import java.util.List;

public record PatientContextDto (
        Long userId,
        String name,
        Integer age,
        String longTermSummary,
        String shortTermSummary,
        String riskStatus,
        List<AssessmentResult> assessmentResults
){}
