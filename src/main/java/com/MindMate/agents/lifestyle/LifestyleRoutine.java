package com.MindMate.agents.lifestyle;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class LifestyleRoutine {

    @Id
    private Long userId;

    @ElementCollection
    private List<String> morningBoost;

    @ElementCollection
    private List<String> daytimeBalance;

    @ElementCollection
    private List<String> eveningReset;

    @ElementCollection
    private List<String> nightWindDown;

    @ElementCollection
    private List<String> weekendIdeas;

    @ElementCollection
    private List<String> personalizedTips;
}
