package com.example.movie.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "shows")
public class Shows {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long showId;

    @ManyToOne
    @JoinColumn(name = "cinema_id")
    private Cinema cinema;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "show_day_id")
    private ShowDay showDay;

    @ManyToOne
    @JoinColumn(name = "show_time_id")
    private ShowTime showTime;

    @OneToMany(mappedBy = "shows", cascade = CascadeType.ALL)
    Set<Reservation> reservations;

}
