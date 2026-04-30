package com.MindMate.agents.assessment.dto;

public record AssessmentInterpretationDto(
        String interpretation,
        Boolean expertConsultationRequired,
        String recommendationSummary
) {
}
