package com.example.movie.Repository;

import com.example.movie.Entity.Comment;
import com.example.movie.Entity.Seat;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepo extends JpaRepository<Seat, Long> {
    default List<Seat> getAllSeat(Integer pageNo,
                                         Integer pageSize,
                                         String sortBy){
        return findAll(PageRequest.of(pageNo,
                pageSize,
                Sort.by(sortBy)))
                .getContent();
    }
    default List<Seat> getAllSeat(Integer pageNo,
                                  Integer pageSize){
        return findAll(PageRequest.of(pageNo,
                pageSize))
                .getContent();
    }

    @Modifying
    @Query("UPDATE Seat s SET s.Status = true WHERE s.seatId = :seatId")
    void updateWaitingSeat(@Param("seatId") Long seatId);

    @Modifying
    @Query("UPDATE Seat s SET s.Status = false WHERE s.seatId = :seatId")
    void updateEmptySeat(@Param("seatId") Long seatId);

    @Query(value = "SELECT seat_id FROM seat where status = 1", nativeQuery = true)
    List<Long> listWaitingSeat();
}
