package com.example.movie.dto;

import lombok.Data;

@Data
public class UserDto {
    private Long Id;
    private String Email;
    private String fullName;
    private String Password;
    private String username;
    private String roleName;
    private String rewardPoints;
    private String rankName;
}
