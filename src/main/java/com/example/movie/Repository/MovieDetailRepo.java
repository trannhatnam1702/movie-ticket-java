package com.example.movie.Repository;

import com.example.movie.Entity.Cinema;
import com.example.movie.Entity.Movie;
import com.example.movie.Entity.MovieDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieDetailRepo extends JpaRepository<MovieDetails, Long> {
    @Query(value = "select c.Name from MovieDetails m, Celebrities c where m.CelebrityId = c.CelebrityId and m.movie_id = c.movie_id and = m.movie_id?1", nativeQuery = true)
    String[] findTimeByMovieId(Long movie_id);
}