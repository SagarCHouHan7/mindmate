package com.MindMate.community.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAnswerDto {
    private long questionId;
    private String answer;
}
