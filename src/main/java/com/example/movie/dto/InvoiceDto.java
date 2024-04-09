package com.example.movie.dto;

import lombok.Data;

import java.util.Date;

@Data
public class InvoiceDto {
    private Long Id;
    private int quantity;
    private Double total;
    private Date createdAt;
    private String user;
    private String movieName;
    private String seats;
}
