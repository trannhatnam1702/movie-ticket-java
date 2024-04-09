package com.example.movie.dto;

import com.paypal.api.payments.Payment;
import lombok.Data;

@Data
public class PaymentSession {
    private Payment payment;
    private String showId;
    private String seat;
}