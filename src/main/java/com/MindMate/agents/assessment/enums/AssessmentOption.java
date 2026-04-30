package com.MindMate.agents.assessment.enums;

public enum AssessmentOption {
    NEVER(0),
    RARELY(1),
    SOMETIMES(2),
    OFTEN(3),
    VERY_OFTEN(4);

    private final int score;

    // Constructor
    AssessmentOption(int score) {
        this.score = score;
    }

    // Getter
    public int getScore() {
        return score;
    }

}
