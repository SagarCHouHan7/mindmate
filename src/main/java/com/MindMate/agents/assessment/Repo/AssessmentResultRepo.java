package com.MindMate.agents.assessment.Repo;

import com.MindMate.agents.assessment.model.AssessmentResult;
import com.MindMate.model.account.User;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssessmentResultRepo extends JpaRepository<AssessmentResult, Long> {

    @Nullable List<AssessmentResult> findAllByUser(User user);
}
