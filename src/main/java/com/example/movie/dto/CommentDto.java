package com.example.movie.dto;

import com.example.movie.Entity.Movie;
import com.example.movie.Entity.User;
import lombok.Data;

@Data
public class CommentDto {
    private Long Id;
    private String content;
    private String user;
    private String movie;
}
