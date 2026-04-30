package com.MindMate.agents.assessment.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AssessmentResultJson {

    private String severity;

    private String interpretation;

    private Boolean expertConsultationRequired;

    private String recommendationSummary;

}
