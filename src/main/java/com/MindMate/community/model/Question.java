package com.MindMate.community.model;

import com.MindMate.model.account.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@ToString
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "question" , length = 4000)
    private String question;
    private LocalDateTime createdTime;
    private LocalDateTime modifiedTime;
    private int likes;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id" , nullable = false)
    private User user;

    @OneToMany(
            mappedBy = "question",
            cascade = CascadeType.ALL
    )
    List<Answer> answerList;

    private String username;
    private int answerCount;
}
