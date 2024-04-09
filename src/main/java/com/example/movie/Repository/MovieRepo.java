package com.example.movie.Repository;

import com.example.movie.Entity.Cinema;
import com.example.movie.Entity.Movie;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepo extends JpaRepository<Movie, Long> {
//    @Query("SELECT c.cinema_name FROM Shows s, Movie m, Cinema c WHERE m.movie_id = s.movie_id AND s.cinema_id = c.cinema_id AND s.movie_id = ?1")
//    List<String> findCinemaNameByMovieId(Long movieId);
    @Query(value = "SELECT * FROM movie WHERE star_date > CURRENT_DATE()", nativeQuery = true)
    List<Movie> findAllUpcomingMovies();
    default List<Movie> getAllMovie(Integer pageNo,
                                    Integer pageSize,
                                    String sortBy){
        return findAll(PageRequest.of(pageNo,
                pageSize,
                Sort.by(sortBy)))
                .getContent();
    }

    @Query(value = "SELECT * FROM movie WHERE movie_name LIKE %?1%", nativeQuery = true)
    List<Movie> searchMovie(String keyword);
}

