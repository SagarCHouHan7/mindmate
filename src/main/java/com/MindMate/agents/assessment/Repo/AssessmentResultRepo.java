package com.MindMate.agents.assessment.Repo;

import com.MindMate.agents.assessment.model.AssessmentResult;
import com.MindMate.model.account.User;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssessmentResultRepo extends JpaRepository<AssessmentResult, Long> {

    @Nullable List<AssessmentResult> findAllByUser(User user);


    @Query(value = """
    SELECT * FROM (
        SELECT ar.*,
               ROW_NUMBER() OVER (
                   PARTITION BY ar.assessment_type
                   ORDER BY ar.create_at DESC, ar.id DESC
               ) rn
        FROM assessment_result ar
        WHERE ar.user_id = :userId
    ) t
    WHERE t.rn = 1
    """, nativeQuery = true)
    Optional<List<AssessmentResult>> findLatestPerAssessmentType(@Param("userId") Long userId);
}
