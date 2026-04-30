package com.MindMate.agents.escalation;

import com.MindMate.model.account.User;
import com.MindMate.model.enums.RiskStatusLevel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "risk_status")
@AllArgsConstructor
@NoArgsConstructor
public class RiskStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private RiskStatusLevel riskLevel = RiskStatusLevel.LOW;


    @PrePersist
    public void prePersist(){
        createdAt = LocalDateTime.now();
    }
}
