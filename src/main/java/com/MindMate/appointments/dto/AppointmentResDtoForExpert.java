package com.MindMate.appointments.dto;

import com.MindMate.dto.userDto.UserProfileDto;
import com.MindMate.appointments.enums.AppointmentStatus;
import com.MindMate.appointments.enums.PaymentStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AppointmentResDtoForExpert {
    private Long id;
    private String reason;
    private LocalDateTime createdTime;
    private LocalDateTime appointmentTime;
    private int durationInMinutes;
    private AppointmentStatus appointmentStatus;
    private String orderId;
    private PaymentStatus paymentStatus;
    private int amountInPaise;
    private String currency;
    private UserProfileDto user;
}
