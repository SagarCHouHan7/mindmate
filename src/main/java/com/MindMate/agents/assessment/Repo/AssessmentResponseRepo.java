package com.MindMate.agents.assessment.Repo;

import com.MindMate.agents.assessment.model.AssessmentResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssessmentResponseRepo extends JpaRepository<AssessmentResponse, Long> {
}
