package com.example.movie.dto;

import lombok.Data;

@Data
public class CelebrityDto {
    private Long Id;
    private String Name;
    private String Height;
    private String Weight;
    private String urlAvatar;
    private String Description;
    private String Language;
}
