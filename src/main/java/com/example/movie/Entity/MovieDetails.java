package com.example.movie.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "MovieDetails")
public class MovieDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long movieDetailsId;

    @ManyToOne
    @JoinColumn(name = "celebrityId")
    private Celebrity celebrity;

    @ManyToOne
    @JoinColumn(name = "movieId")
    private Movie movie;
}
