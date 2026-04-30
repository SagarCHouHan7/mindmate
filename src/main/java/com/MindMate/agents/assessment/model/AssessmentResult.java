package com.MindMate.agents.assessment.model;

import com.MindMate.agents.assessment.enums.AssessmentType;
import com.MindMate.model.account.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class AssessmentResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @Enumerated(EnumType.STRING)
    private AssessmentType assessmentType;

    private Integer totalScore;

    private String severity;

    private String aiInterpretation;

    private Boolean expertConsultationRequired;

    private String recommendationSummary;

    private LocalDateTime createAt;

    @PrePersist
    public void prePersist(){
        this.createAt = LocalDateTime.now();
    }


}
