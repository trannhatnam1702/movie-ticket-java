package com.example.movie.Repository;

import com.example.movie.Entity.Ranking;
import com.example.movie.Entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RankingRepo extends JpaRepository<Ranking, Long> {

}
