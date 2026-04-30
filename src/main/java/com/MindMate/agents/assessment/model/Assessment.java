package com.MindMate.agents.assessment.model;

import com.MindMate.agents.assessment.enums.AssessmentType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Assessment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private AssessmentType type;

    private String title;

    private String description;

    private boolean active=true;
}
