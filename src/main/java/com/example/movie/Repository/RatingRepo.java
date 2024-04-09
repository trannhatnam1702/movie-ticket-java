package com.example.movie.Repository;

import com.example.movie.Entity.Rating;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RatingRepo extends JpaRepository<Rating, Long> {

    default List<Rating> getRatingByMovieId(Integer pageNo,
                                            Integer pageSize,
                                            String sortBy){
        return findAll(PageRequest.of(pageNo,
                pageSize,
                Sort.by(sortBy)))
                .getContent();
    }
    @Query("""
            SELECT c
            FROM Rating c
            WHERE c.value LIKE %?1%
                OR c.user.username LIKE %?1%
                OR c.movie.movieName LIKE %?1%
            """)
    List<Rating> searchRating(String keyword);

    @Query(value = "SELECT * FROM rating WHERE movie_id = ?1", nativeQuery = true)
    List<Rating> getRatingByMovieId(Long movie_id);

    @Query("SELECT r FROM Rating r WHERE r.user.userId = :userId AND r.movie.movieId = :movieId")
    Rating findByUserAndMovieId(@Param("userId") Long userId, @Param("movieId") Long movieId);

    // Updated method to get the total review count for a specific movie
    @Query(value = "SELECT COUNT(*) FROM rating WHERE movie_id = ?1", nativeQuery = true)
    int getTotalReviewCountByMovieId(Long movie_id);

    // Updated method to calculate the average rating for a specific movie
    @Query(value = "SELECT AVG(value) FROM rating WHERE movie_id = ?1", nativeQuery = true)
    Double getAverageRatingByMovieId(Long movie_id);

    @Query(value = "SELECT value FROM rating where user_id = ?1 and movie_id = ?2", nativeQuery = true)
    Optional<Integer> getRatingMovieByUser(Long user_id, Long id);
}