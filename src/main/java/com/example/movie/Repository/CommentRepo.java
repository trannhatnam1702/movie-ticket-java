package com.example.movie.Repository;

import com.example.movie.Entity.Celebrity;
import com.example.movie.Entity.Comment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepo extends JpaRepository<Comment, Long> {
    @Query(value = "SELECT * from comment where movie_id = ?1", nativeQuery = true)
    List<Comment> getCommentByMovieId(Long movie_id);

    default List<Comment> getAllComments(Integer pageNo,
                                         Integer pageSize,
                                         String sortBy){
        return findAll(PageRequest.of(pageNo,
                pageSize,
                Sort.by(sortBy)))
                .getContent();
    }

    @Query("""
            SELECT c
            FROM Comment c
            WHERE c.content LIKE %?1%
                OR c.user.username LIKE %?1%
                OR c.movie.movieName LIKE %?1%
            """)
    List<Comment> searchComment(String keyword);
}