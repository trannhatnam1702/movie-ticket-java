package com.example.movie.Service;

import com.example.movie.Entity.Comment;
import com.example.movie.Entity.Reservation;
import com.example.movie.Repository.CommentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepo commentRepository;

    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    public void createComment(Comment comment) {
        commentRepository.save(comment);
    }

    public Comment getCommentById(Long id) {
        return commentRepository.findById(id).orElse(null);
    }

    public void deleteComment(Long id) {
        //goi repository ==> deleteById
        commentRepository.deleteById(id);
    }

    public List<Comment> getAllComments(Integer pageNo,
                                        Integer pageSize,
                                        String sortBy){
        return commentRepository.getAllComments(pageNo, pageSize, sortBy);
    }

    public List<Comment> searchComment(String keyword){
        return commentRepository.searchComment(keyword);
    }

}

