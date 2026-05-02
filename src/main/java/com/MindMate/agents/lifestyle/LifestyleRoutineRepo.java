package com.MindMate.agents.lifestyle;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LifestyleRoutineRepo extends JpaRepository<LifestyleRoutine, Long> {

    Optional<LifestyleRoutine> findByUserId(Long userId);

}
