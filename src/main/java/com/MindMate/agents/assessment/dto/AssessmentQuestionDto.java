package com.MindMate.agents.assessment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AssessmentQuestionDto {
    private Long id;
    private Integer order;
    private String question;
}
