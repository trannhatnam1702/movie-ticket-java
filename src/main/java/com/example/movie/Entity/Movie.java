package com.example.movie.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "Movie")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long movieId;

    @Column(name = "movie_name")
    private String movieName;

    @Column(name = "description")
    private String Description;

    @Column(name = "imageurl")
    private String imageURL;

    @Column(name = "genres")
    private String genres;

    @Column(name = "time")
    private String time;

    @Column(name = "trailer")
    private String trailer;

    @Column(name = "director")
    private String director;

    @Column(name = "writer")
    private String writer;

    @Column(name = "language")
    private String language;

    @Column(name = "star_date")
    private String starDate;

    @Column(name = "end_date")
    private String endDate;

    @Column(name = "vote")
    private Long Vote;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    Set<News> news;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    Set<Shows> shows;

    @OneToMany(mappedBy = "movie")
    Set<MovieDetails> movieDetails;
}
