package com.MindMate.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class UserMemory {

        @Id
        private Long userId;

        @Column(columnDefinition = "TEXT")
        private String summary;

        private LocalDateTime updatedAt;

        @PreUpdate
        @PrePersist
        public void touch(){
            updatedAt = LocalDateTime.now();
        }

}
