package com.MindMate.dto.AnswerDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAnswerDto {
    private long questionId;
    private String answer;
}
