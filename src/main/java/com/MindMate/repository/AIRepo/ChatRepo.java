package com.MindMate.repository.AIRepo;

import com.MindMate.model.ChatMessage;
import com.MindMate.model.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepo extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findTop10ByUserIdOrderByCreatedAtDesc(Long userId);

    List<ChatMessage> findTop5ByUserIdOrderByCreatedAtDesc(Long userId);

    Long countByUserIdAndRole(Long userId, Role role);
}
