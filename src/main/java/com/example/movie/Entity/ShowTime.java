package com.example.movie.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "ShowTime")
public class ShowTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long showTimeId;
    @Column(name = "Time")
    private String Time;

    @OneToMany(mappedBy = "showTime", cascade = CascadeType.ALL)
    Set<Shows> shows;
}
