package com.MindMate.agents.assessment.enums;

import java.util.List;

public record AssessmentRecommendationResponse(
        List<AssessmentType> recommendedAssessments
) {}
