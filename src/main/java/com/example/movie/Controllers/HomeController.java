package com.example.movie.Controllers;

import com.example.movie.Entity.Celebrity;
import com.example.movie.Entity.Movie;
import com.example.movie.Entity.MovieDetails;
import com.example.movie.Entity.News;
import com.example.movie.Repository.CelebrityRepo;
import com.example.movie.Repository.MovieRepo;
import com.example.movie.Service.HomeService;
import com.example.movie.Service.MovieDetailsService;
import com.example.movie.Service.MovieService;
import com.example.movie.Service.NewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.AttributedString;
import java.util.List;


@Controller
@SpringBootApplication
@RequestMapping("/")

public class HomeController {
    @Autowired
    private HomeService HomeService;
    @Autowired
    private NewService NewService;
    @Autowired
    private MovieDetailsService movieDetailsService;
    @Autowired
    private MovieRepo movieRepo;
    @Autowired
    private MovieService movieService;
    @Autowired
    private CelebrityRepo celebrityRepo;
    @GetMapping
    public String home(Model model){
        List<Celebrity> celebritiesa = celebrityRepo.findCelebrityByMovieId(4L);
        model.addAttribute("celebritiesa", celebritiesa);
        List<Celebrity> celebritiesb = celebrityRepo.findCelebrityByMovieId(2L);
        model.addAttribute("celebritiesb", celebritiesb);

        Movie moviea = movieService.getMovieById(4L);
        model.addAttribute("moviea", moviea);
        Movie movieb = movieService.getMovieById(2L);
        model.addAttribute("movieb", movieb);

        List<Movie> allMovies = HomeService.getAllMovie();
        model.addAttribute("allMovies", allMovies);

        List<News> allNews = NewService.getAllNews();
        model.addAttribute("allNews", allNews);

        List<MovieDetails> allMovieDetails = movieDetailsService.getAllMovieDetail();
        model.addAttribute("allMovieDetails", allMovieDetails);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        model.addAttribute("username", username);

        return "home/index";
    }

}
