package com.example.movie.dto;

import lombok.Data;

@Data
public class RankingDto {
    private long id;
    private String rankName;
    private String discountPercentage;
    private String points;
}
