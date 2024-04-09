package com.example.movie.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity
@Table(name = "Seat")
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seatId;
    @Column(name = "seatNo")
    private Integer seatNo;
    @Column(name = "Status")
    private Boolean Status;
    @Column(name = "Price")
    private Double Price;

    @OneToMany(mappedBy = "seat", cascade = CascadeType.ALL)
    Set<Reservation> reservations;
}
