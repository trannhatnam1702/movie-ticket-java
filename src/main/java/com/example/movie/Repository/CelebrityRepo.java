package com.example.movie.Repository;

import com.example.movie.Entity.Celebrity;
import com.example.movie.Entity.Movie;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CelebrityRepo extends JpaRepository<Celebrity, Long> {
    @Query(value = "SELECT c.celebrity_id as celebrity_id, c.name as name, c.height as height, c.weight as weight,c.url_avatar as url_avatar, c.description as description, c.language as language FROM movie_details m, celebrity c WHERE m.celebrity_id = c.celebrity_id AND m.movie_id = ?1", nativeQuery = true)
    List<Celebrity> findCelebrityByMovieId(Long movie_id);
    default List<Celebrity> getAllCelebrity(Integer pageNo,
                                    Integer pageSize,
                                    String sortBy){
        return findAll(PageRequest.of(pageNo,
                pageSize,
                Sort.by(sortBy)))
                .getContent();
    }

    @Query(value = "SELECT * FROM celebrity WHERE name LIKE %?1%", nativeQuery = true)
    List<Celebrity> searchCelebrity(String keyword);

}
