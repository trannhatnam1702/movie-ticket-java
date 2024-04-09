package com.example.movie.Entity;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Comment")
public class Comment {
            @Id
            @GeneratedValue(strategy = GenerationType.IDENTITY)
            private Long commentId;

            @Column(name = "content")
            private String content;

            @ManyToOne
            @JoinColumn(name = "user_id")
            private User user;
            @ManyToOne
            @JoinColumn(name = "movie_id")
            private Movie movie;


}
