package com.MindMate.agents.assessment.Repo;

import com.MindMate.agents.assessment.enums.AssessmentType;
import com.MindMate.agents.assessment.model.AssessmentQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssessmentQuestionRepo extends JpaRepository<AssessmentQuestion, Long> {

    List<AssessmentQuestion> findByAssessmentTypeOrderByQuestionOrder(AssessmentType type);
}
