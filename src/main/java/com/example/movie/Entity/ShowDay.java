package com.example.movie.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "ShowDay")
public class ShowDay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long showDayId;
    @Column(name = "Day")
    private String Day;

    @OneToMany(mappedBy = "showDay", cascade = CascadeType.ALL)
    Set<Shows> shows;

}
