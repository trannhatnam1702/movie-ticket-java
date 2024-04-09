package com.example.movie.dto;

import com.example.movie.Entity.Cinema;
import com.example.movie.Entity.Movie;
import com.example.movie.Entity.ShowDay;
import lombok.Data;

@Data
public class ShowDto {
    private Long Id;
    private String cinema;
    private String movie;
    private String showDay;
    private String showTime;
}
