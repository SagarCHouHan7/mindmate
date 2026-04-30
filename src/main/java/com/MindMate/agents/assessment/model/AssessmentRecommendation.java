package com.MindMate.agents.assessment.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class AssessmentRecommendation {

    @Id
    private Long userId;

    @ManyToMany
    @JoinTable(
            name = "assessment_recommendation_mapping",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "assessment_id")
    )
    private List<Assessment> assessmentList;

    private LocalDateTime recommendedOn;
}
