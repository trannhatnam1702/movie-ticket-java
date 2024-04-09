package com.example.movie.dto;

import lombok.Data;

@Data
public class PurchasedDto {
    private String invoiceId;
    private String createdAt;
    private String movie;
    private String time;
    private String day;
    private String seat;
    private String total;
}
