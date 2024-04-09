package com.example.movie.Repository;

import com.example.movie.Entity.Cinema;
import com.example.movie.Entity.ShowDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
@Repository
public interface ShowDayRepo extends JpaRepository<ShowDay, Long> {
    @Query(value = "SELECT c.day FROM Shows s, Movie m, show_day c WHERE m.movie_id = s.movie_id AND s.show_day_id = c.show_day_id AND s.movie_id = ?1",
            nativeQuery = true)
    String[] findDayByMovieId(Long movie_id);
}


