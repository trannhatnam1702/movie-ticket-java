package com.example.movie.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "News")
public class News {
    public Long getNewId() {
        return newId;
    }

    public void setNewId(Long newId) {
        this.newId = newId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long newId;
    @Column(name = "Title")
    private String Title;

    @Column(name = "Image")
    private String Image;

    @Column(name = "Date")
    private String Date;

    @Column(name = "Description")
    private String Description;

    @ManyToOne
    @JoinColumn(name = "movieId")
    private Movie movie;

}
