package com.MindMate.appointments.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentVerificationRequestDto {

    private String razorpay_order_id;
    private String razorpay_payment_id;
    private String razorpay_signature;

}
