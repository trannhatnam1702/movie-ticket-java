package com.example.movie.Repository;

import com.example.movie.Entity.ShowDay;
import com.example.movie.Entity.ShowTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
@Repository
public interface ShowTimeRepo extends JpaRepository<ShowTime, Long> {
    @Query(value = "SELECT c.time FROM Shows s, Movie m, show_time c WHERE m.movie_id = s.movie_id AND s.show_time_id = c.show_time_id AND s.movie_id = ?1",
            nativeQuery = true)
    String[] findTimeByMovieId(Long movie_id);
}


