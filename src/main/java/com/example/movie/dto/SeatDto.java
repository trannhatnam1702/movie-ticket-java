package com.example.movie.dto;

import lombok.Data;

@Data
public class SeatDto {
    private Long Id;
    private Integer seatNo;
    private Boolean Status;
    private Double Price;
}
