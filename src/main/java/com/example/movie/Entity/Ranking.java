package com.example.movie.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity
@Table(name = "Ranking")
public class Ranking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rankingId;

    @Column(name = "rank_name")
    private String rankName;

    @Column(name = "points")
    private int points;

    @Column(name = "discount_percentage")
    private int discountPercentage;

    @OneToMany(mappedBy = "ranking", cascade =  CascadeType.ALL)
    private Set<User> users;
}
