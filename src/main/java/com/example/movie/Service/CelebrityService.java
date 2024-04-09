package com.example.movie.Service;

import com.example.movie.Entity.Celebrity;
import com.example.movie.Entity.Movie;
import com.example.movie.Repository.CelebrityRepo;
import com.example.movie.Repository.MovieRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CelebrityService {
    @Autowired
    private CelebrityRepo celebrityRepo;

    public Celebrity getCelebrityById(Long id) {
        return celebrityRepo.findById(id).orElse(null);
    }

    public List<Celebrity> getAllCelebrity(Integer pageNo,
                                   Integer pageSize,
                                   String sortBy){
        return celebrityRepo.getAllCelebrity(pageNo, pageSize, sortBy);
    }


    public List<Celebrity> getAllCelebrity() {
        return celebrityRepo.findAll();
    }
    public Celebrity addcelebrity(Celebrity celebrity){
        return celebrityRepo.save(celebrity);
    }

    public void deleteCelebrity(Long id) {
        //goi repository ==> deleteById
        celebrityRepo.deleteById(id);
    }

    public List<Celebrity> searchCelebrity(String keyword){
        return celebrityRepo.searchCelebrity(keyword);
    }
}
