package com.example.movie.Service;

import com.example.movie.Entity.Comment;
import com.example.movie.Entity.Seat;
import com.example.movie.Entity.Shows;
import com.example.movie.Repository.SeatRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SeatService {
    @Autowired
    private SeatRepo seatRepo;
    public Seat getSeatBySeatId (Long id) {
        return seatRepo.findById(id).orElse(null);
    }
    public List<Seat> getAllSeat() {
        return seatRepo.findAll();
    }
    public List<Seat> getAllSeat(Integer pageNo,
                                        Integer pageSize,
                                        String sortBy){
        return seatRepo.getAllSeat(pageNo, pageSize, sortBy);
    }
    public List<Seat> getAllSeat(Integer pageNo,
                                 Integer pageSize){
        return seatRepo.getAllSeat(pageNo, pageSize);
    }
    public Seat getSeatById(Long id) {
        return seatRepo.findById(id).orElse(null);
    }
    public Seat addSeat(Seat seat){
        return seatRepo.save(seat);
    }
    public void deleteSeat(Long id) {
        //goi repository ==> deleteById
        seatRepo.deleteById(id);
    }
    @Transactional
    public boolean updateWaitingSeat(Long seatId) {
        seatRepo.updateWaitingSeat(seatId);
        return true;
    }
    // Method to release a seat
    @Transactional
    public boolean updateEmptySeat(Long seatId) {
        seatRepo.updateEmptySeat(seatId);
        return true;
    }

    public List<Long> listWaitingSeat(){
        return seatRepo.listWaitingSeat();
    }


}
