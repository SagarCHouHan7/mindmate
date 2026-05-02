package com.MindMate.agents.lifestyle;

public record UserRoutineInput(
        String wakeUpTime,
        String sleepTime,

        String workStartTime,
        String workEndTime,

        String studyStartTime,
        String studyEndTime,

        String gymTime,        // optional
        String freeTimeNotes   // user says: "evening free", "weekends busy", etc.
) {
}
