package com.MindMate.dto.publicDto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SnapshotsDto {

    long totalMembers;
    long totalQuestions;
    long totalExperts;
    long totalAppointments;
}
