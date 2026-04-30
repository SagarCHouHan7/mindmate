package com.MindMate.community.model;

import com.MindMate.model.account.Account;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "answer" , length = 40000)
    private String answer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id" , nullable = false)
    private Question question;

    @ManyToOne
    @JoinColumn(name = "answered_by_account_id" , nullable = true)
    private Account answeredBy;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private long likes;

    private Integer safetyRating = 5;
    private Boolean moderationSafe;
    private String moderationReason;
    private String moderationCategories;
    private String suggestedAnswer;
    private boolean isAnswerChanged = false;
    private int suggestedAnswerSafetyRating = 8;
}
