package com.MindMate.agents.wellness;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepo extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findTop10ByUserIdOrderByCreatedAtDesc(Long userId);

    List<ChatMessage> findTop5ByUserIdOrderByCreatedAtDesc(Long userId);

    Long countByUserIdAndRole(Long userId, Role role);
}
