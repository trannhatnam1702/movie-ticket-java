package com.example.movie.Repository;


import com.example.movie.Entity.Movie;
import com.example.movie.Entity.Reservation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepo extends JpaRepository<Reservation, Long>{
    @Query(value = "SELECT seat_id FROM `reservation` WHERE show_id = ?1", nativeQuery = true)
    List<Integer> findSeatByShowId(Long show_id);
    @Query(value = "SELECT c.cinema_name from shows s, cinema c where c.cinema_id = s.cinema_id and show_id = ?1", nativeQuery = true)
    String[] getCinemaByShowId(Long show_id);

    @Query(value = "SELECT c.time from shows s, show_time c where c.show_time_id = s.show_time_id and show_id = ?1", nativeQuery = true)
    String[] getTimeByShowId(Long show_id);

    @Query(value = "SELECT c.day from shows s, show_day c where c.show_day_id = s.show_day_id and show_id = ?1", nativeQuery = true)
    String[] getDayByShowId(Long show_id);

    @Query(value = "SELECT i.invoice_id, i.created_at, m.movie_name AS movie, st.time, sd.day, GROUP_CONCAT(DISTINCT s.seat_no ORDER BY s.seat_no ASC SEPARATOR '/') AS seat, i.total FROM invoice i JOIN user u ON i.user_id = u.user_id JOIN reservation r ON i.invoice_id = r.invoice_id JOIN shows sh ON r.show_id = sh.show_id JOIN movie m ON sh.movie_id = m.movie_id JOIN seat s ON r.seat_id = s.seat_id JOIN show_time st ON st.show_time_id = sh.show_time_id JOIN show_day sd ON sd.show_day_id = sh.show_day_id WHERE u.user_id = ?1 GROUP BY i.invoice_id, i.created_at, m.movie_name, st.time, sd.day, i.total;", nativeQuery = true)
    String[] getReservationByUserId(Long user_id);

    default List<Reservation> getAllReservations(Integer pageNo,
                                                 Integer pageSize,
                                                 String sortBy){
        return findAll(PageRequest.of(pageNo,
                pageSize,
                Sort.by(sortBy)))
                .getContent();
    }

    @Query("""
            SELECT r
            FROM Reservation r
            WHERE r.shows.movie.movieName LIKE %?1%
                OR r.invoice.user.username LIKE %?1%
                OR r.shows.cinema.cinemaName LIKE %?1%
            """)
    List<Reservation> searchReservation(String keyword);



//    @Query(value = "select sum(c.price) from reservation a, shows b, seat c where a.show_id = b.show_id and a.seat_id = c.seat_id", nativeQuery = true)
//    String revenue();
}
