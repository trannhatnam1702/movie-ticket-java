package com.example.movie.Service;

import com.example.movie.Entity.Movie;
import com.example.movie.Entity.Reservation;
import com.example.movie.Repository.MovieRepo;
import com.example.movie.Repository.ReservationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {
    @Autowired
    private ReservationRepo reservationRepo;


    public List<Reservation> getAllReservations() {
        return reservationRepo.findAll();
    }

//    public List<Reservation> getSeatIdsByShowId(Long id) {
//        return reservationRepo.findSeatIdsByShowId(id);
//    }

    public Reservation addReservation(Reservation reservation){
        //goi repository ==> save dua vao id cua category
        return reservationRepo.save(reservation);
    }

    public Reservation getReservationById(Long id) {
        return reservationRepo.findById(id).orElse(null);
    }

    public Reservation addReservationV2(Reservation reservation) {
        return reservationRepo.save(reservation);
    }

    public void deleteReservation(Long id) {
        //goi repository ==> deleteById
        reservationRepo.deleteById(id);
    }

    public List<Reservation> getAllReservations(Integer pageNo,
                                                Integer pageSize,
                                                String sortBy){
        return reservationRepo.getAllReservations(pageNo, pageSize, sortBy);
    }

    public List<Reservation> searchReservation(String keyword){
        return reservationRepo.searchReservation(keyword);
    }
}
