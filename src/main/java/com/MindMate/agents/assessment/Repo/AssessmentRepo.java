package com.MindMate.agents.assessment.Repo;

import com.MindMate.agents.assessment.enums.AssessmentType;
import com.MindMate.agents.assessment.model.Assessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssessmentRepo extends JpaRepository<Assessment, Long> {

    Optional<Assessment> findByType(AssessmentType type);

    List<Assessment> findByTypeIn(List<AssessmentType> stress);
}
