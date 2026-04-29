package com.MindMate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.MindMate.model.account.Account;
import com.MindMate.model.enums.NotificationStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String note;

    private LocalDateTime time;

    @ManyToOne
    @JoinColumn(name = "account_id" , nullable = false)
    @JsonIgnore
    private Account account;

    private NotificationStatus status;
}
