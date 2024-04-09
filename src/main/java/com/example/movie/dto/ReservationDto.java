package com.example.movie.dto;

import lombok.Data;

@Data
public class ReservationDto {
    private Long Id;
    private  String user;
    private String movie;
    private String seat;
    private String day;
    private String time;
    private String cinema;
    private String shows;
    private String invoice;
}
