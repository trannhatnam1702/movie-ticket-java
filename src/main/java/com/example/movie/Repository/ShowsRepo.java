package com.example.movie.Repository;

import com.example.movie.Entity.Cinema;
import com.example.movie.Entity.Reservation;
import com.example.movie.Entity.Shows;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShowsRepo extends JpaRepository<Shows, Long> {
    @Query(value = "SELECT show_id FROM shows WHERE movie_id = ?1", nativeQuery = true)
    List<Integer> findShowByMovieId(Long movie_id);

    @Query(value = "SELECT c.cinema_name from shows s, cinema c WHERE s.cinema_id = c.cinema_id and s.show_id = ?1", nativeQuery = true)
    String findCinemaNameByShowId(Long show_id);

    @Query(value = "SELECT c.day from shows s, show_day c WHERE s.show_day_id = c.show_day_id and s.show_id = ?1", nativeQuery = true)
    String findDayByShowId(Long show_id);

    @Query(value = "SELECT c.time from shows s, show_time c WHERE s.show_time_id = c.show_time_id and s.show_id = ?1", nativeQuery = true)
    String findTimeByShowId(Long show_id);

    @Query(value = "SELECT c.movie_name from shows s, movie c WHERE s.movie_id = c.movie_id and s.show_id = ?1", nativeQuery = true)
    String findMovieByShowId(Long show_id);

    default List<Shows> getAllShows(Integer pageNo,
                                    Integer pageSize,
                                    String sortBy){
        return findAll(PageRequest.of(pageNo,
                pageSize,
                Sort.by(sortBy)))
                .getContent();
    }

    default List<Shows> getAllShows(Integer pageNo,
                                    Integer pageSize
                                   ){
        return findAll(PageRequest.of(pageNo,
                pageSize))
                .getContent();
    }


    @Query("""
            SELECT s
            FROM Shows s
            WHERE s.movie.movieName LIKE %?1%
                OR s.cinema.cinemaName LIKE %?1%
            """)
    List<Shows> searchShows(String keyword);


}

