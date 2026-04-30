package com.MindMate.agents.untils;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMemoryRepo extends JpaRepository<UserMemory, Long> {
}
