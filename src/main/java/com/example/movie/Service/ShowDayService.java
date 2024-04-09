package com.example.movie.Service;

import com.example.movie.Entity.Celebrity;
import com.example.movie.Entity.ShowDay;
import com.example.movie.Repository.CelebrityRepo;
import com.example.movie.Repository.ShowDayRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShowDayService {
    @Autowired
    private ShowDayRepo showDayRepo;

    public List<ShowDay> getAllShowDay() {
        return showDayRepo.findAll();
    }

    public ShowDay getShowDayById(Long id) {
        return showDayRepo.findById(id).orElse(null);
    }

    public ShowDay addShowDay(ShowDay showDay){
        return showDayRepo.save(showDay);
    }

    public void deleteShowDay(Long id) {
        //goi repository ==> deleteById
        showDayRepo.deleteById(id);
    }
}