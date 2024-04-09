package com.example.movie.Service;

import com.example.movie.Entity.Ranking;
import com.example.movie.Repository.RankingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RankingService {
    @Autowired
    private RankingRepo rankingRepo;

    public List<Ranking> getAllRanking() {
        return rankingRepo.findAll();
    }

    public Ranking getRankingById(Long id) {
        return rankingRepo.findById(id).orElse(null);
    }


    public Ranking addRanking(Ranking ranking){
        return rankingRepo.save(ranking);
    }

    public void deleteRanking(Long id) {
        rankingRepo.deleteById(id);
    }
}
