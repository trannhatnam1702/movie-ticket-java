package com.example.movie.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "Cinema")
public class Cinema {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cinemaId;
    @Column(name = "cinema_name")
    private String cinemaName;
    @Column(name = "description")
    private String Description;

    @OneToMany(mappedBy = "cinema", cascade = CascadeType.ALL)
    Set<Shows> shows;

    public Cinema(Long cinemaId, String cinemaName, String description) {
        this.cinemaId = cinemaId;
        this.cinemaName = cinemaName;
        this.Description = description;
    }

    public Cinema() {
    }
}
