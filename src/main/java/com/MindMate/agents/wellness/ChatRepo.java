package com.MindMate.agents.wellness;

import com.MindMate.model.account.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepo extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findTop10ByUserIdOrderByCreatedAtDesc(Long userId);

    List<ChatMessage> findTop5ByUserIdOrderByCreatedAtDesc(Long userId);

    Long countByUserIdAndRole(Long userId, Role role);

    Page<ChatMessage> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);
}
