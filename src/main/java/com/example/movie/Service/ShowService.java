package com.example.movie.Service;

import com.example.movie.Entity.Movie;
import com.example.movie.Entity.Shows;
import com.example.movie.Repository.ReservationRepo;
import com.example.movie.Repository.ShowsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShowService {
    @Autowired
    private ShowsRepo showsRepo;
    public Shows getShowById (Long id) {
        return showsRepo.findById(id).orElse(null);
    }
    public List<Shows> getAllShows() {
        return showsRepo.findAll();
    }
    public Shows getShowsById(Long id) {
        return showsRepo.findById(id).orElse(null);
    }
    public Shows addShows(Shows shows) {
        return showsRepo.save(shows);
    }
    public void deleteShows(Long id) {
        //goi repository ==> deleteById
        showsRepo.deleteById(id);
    }

    public List<Shows> getAllShows(Integer pageNo,
                                   Integer pageSize,
                                   String sortBy){
        return showsRepo.getAllShows(pageNo, pageSize, sortBy);
    }

    public List<Shows> getAllShows(Integer pageNo,
                                   Integer pageSize
                                   ){
        return showsRepo.getAllShows(pageNo, pageSize);
    }

    public List<Shows> searchShows(String keyword){
        return showsRepo.searchShows(keyword);
    }
}
