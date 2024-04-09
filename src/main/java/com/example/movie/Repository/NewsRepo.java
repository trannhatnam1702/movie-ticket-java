package com.example.movie.Repository;

import com.example.movie.Entity.Movie;
import com.example.movie.Entity.News;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsRepo extends JpaRepository<News, Long> {


    default List<News> getAllNews(Integer pageNo,
                                    Integer pageSize,
                                    String sortBy){
        return findAll(PageRequest.of(pageNo,
                pageSize,
                Sort.by(sortBy)))
                .getContent();
    }
    @Query("""
            SELECT n
            FROM News n
            WHERE n.movie.movieName LIKE %?1%
                OR n.Title LIKE %?1%
                OR n.Date LIKE %?1%
            """)
    List<News> searchNews(String keyword);
}
