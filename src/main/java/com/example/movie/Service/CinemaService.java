package com.example.movie.Service;

import com.example.movie.Entity.Cinema;
import com.example.movie.Repository.CinemaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CinemaService {

    @Autowired
    private CinemaRepo cinemaRepo;

    public List<Cinema> getAllCinema() {
        return cinemaRepo.findAll();
    }

    public Cinema getCinemaById(Long id) {
        return cinemaRepo.findById(id).orElse(null);
    }


    public Cinema addCinema(Cinema cinema){
        return cinemaRepo.save(cinema);
    }

    public void deleteCinema(Long id) {
        //goi repository ==> deleteById
        cinemaRepo.deleteById(id);
    }
}