package com.MindMate.repository.AIRepo;

import com.MindMate.model.RiskStatus;
import com.MindMate.model.account.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RiskStatusRepo extends JpaRepository<RiskStatus, Long> {

    Optional<RiskStatus> findTopByUserOrderByCreatedAtDesc(User user);

}
