package com.MindMate.repository.AIRepo;

import com.MindMate.model.UserMemory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMemoryRepo extends JpaRepository<UserMemory, Long> {
}
