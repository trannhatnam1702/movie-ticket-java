package com.example.movie.Controllers;

import com.example.movie.Entity.Comment;
import com.example.movie.Entity.Movie;
import com.example.movie.Entity.User;
import com.example.movie.Repository.UserRepo;
import com.example.movie.Service.CommentService;
import com.example.movie.Service.MovieService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Controller
@SpringBootApplication
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private MovieService movieService;

    @GetMapping
    public String getAllComments(Model model) {
        List<Comment> comments = commentService.getAllComments();
        model.addAttribute("comments", comments);
        return "comments";
    }

    @GetMapping("/create")
    public String showCreateCommentForm(Model model) {
        Comment comment = new Comment();
        model.addAttribute("comment", comment);
        return "create-comment";
    }

    @PostMapping("/create")
    public String createComment(HttpServletRequest request, @ModelAttribute("comment") Comment comment) {
        String previousPageUrl = request.getHeader("Referer");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepo.findByUsername(authentication.getName());
        try {
            URI uri = new URI(previousPageUrl);
            String path = uri.getPath();
            String[] pathSegments = path.split("/");
            String movieIdStr = pathSegments[pathSegments.length - 1];
            long movieId = Long.parseLong(movieIdStr);
            Movie movie = movieService.getMovieById(movieId);
            comment.setUser(user);
            comment.setMovie(movie);
            commentService.createComment(comment);
            return "redirect:" + previousPageUrl;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        // Chuyển đổi movieId từ kiểu String sang kiểu số nguyên



        return "redirect:" + previousPageUrl;

    }
}