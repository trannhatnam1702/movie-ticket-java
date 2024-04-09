package com.example.movie.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "Roles")
public class Roles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;

    @Column(name = "Name")
    private String Name;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();
}