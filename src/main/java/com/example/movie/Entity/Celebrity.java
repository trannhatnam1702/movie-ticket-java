package com.example.movie.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity
@Table(name = "Celebrity")
public class Celebrity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long celebrityId;
    @Column(name = "Name")
    private String Name;
    @Column(name = "Height")
    private String Height;
    @Column(name = "Weight")
    private String Weight;
    @Column(name = "urlAvatar")
    private String urlAvatar;
    @Column(name = "Description")
    private String Description;
    @Column(name = "Language")
    private String Language;

    @OneToMany(mappedBy = "celebrity", cascade = CascadeType.ALL)
    Set<MovieDetails> movieDetails;
}
