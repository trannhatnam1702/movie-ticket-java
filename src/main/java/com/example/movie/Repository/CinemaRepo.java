package com.example.movie.Repository;

import com.example.movie.Entity.Cinema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.*;


import java.util.List;

@Repository
public interface CinemaRepo extends JpaRepository<Cinema, Long> {

@Query(value = "SELECT c.cinema_name FROM Shows s, Movie m, Cinema c WHERE m.movie_id = s.movie_id AND s.cinema_id = c.cinema_id AND s.movie_id = ?1",
        nativeQuery = true)
String[] findCinemaNameByMovieId(Long movie_id);


}


