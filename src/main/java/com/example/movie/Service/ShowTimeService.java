package com.example.movie.Service;

import com.example.movie.Entity.ShowTime;
import com.example.movie.Repository.ShowTimeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShowTimeService {
    @Autowired
    private ShowTimeRepo showTimeRepo;

    public List<ShowTime> getAllShowTime() {
        return showTimeRepo.findAll();
    }

    public ShowTime getShowTimeById(Long id) {
        return showTimeRepo.findById(id).orElse(null);
    }

    public ShowTime addShowTime(ShowTime showTime){
        return showTimeRepo.save(showTime);
    }

    public void deleteShowTime(Long id) {
        //goi repository ==> deleteById
        showTimeRepo.deleteById(id);
    }
}