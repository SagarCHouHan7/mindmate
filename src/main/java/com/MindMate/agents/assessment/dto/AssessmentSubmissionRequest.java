package com.MindMate.agents.assessment.dto;

import com.MindMate.agents.assessment.enums.AssessmentType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AssessmentSubmissionRequest {
    private AssessmentType assessmentType;
    private List<AssessmentAnswerDto> answers;
}
