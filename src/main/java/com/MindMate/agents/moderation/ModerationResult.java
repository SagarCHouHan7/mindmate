package com.MindMate.agents.moderation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ModerationResult {
    private boolean safe = false;
    private int rating = 1;
    private List<String> categories;
    private String reason;
    private String suggestedAnswer;
    private int suggestedAnswerSafetyRating;

}
