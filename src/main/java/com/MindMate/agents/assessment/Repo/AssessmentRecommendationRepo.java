package com.MindMate.agents.assessment.Repo;

import com.MindMate.agents.assessment.model.AssessmentRecommendation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssessmentRecommendationRepo extends JpaRepository<AssessmentRecommendation, Long> {
}
