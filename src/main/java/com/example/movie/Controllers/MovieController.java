package com.example.movie.Controllers;

import com.example.movie.Entity.*;
import com.example.movie.Repository.*;
import com.example.movie.Service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URLEncoder;
import java.util.List;

@Controller
@SpringBootApplication
@RequestMapping("/movie")
public class MovieController {
    @Autowired
    private MovieService movieService;
    @Autowired
    private CinemaRepo cinemaRepo;
    @Autowired
    private ShowTimeRepo showTimeRepo;

    @Autowired
    private MovieRepo movieRepo;
    @Autowired
    private ShowDayRepo showDayRepo;
    @Autowired
    private CommentService commentService;
    @Autowired
    private CommentRepo commentRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RatingRepo ratingRepo;
    @Autowired
    private RatingService ratingService;

    @GetMapping
    public String movies(Model model,
                         @RequestParam(defaultValue = "0") Integer pageNo,
                         @RequestParam(defaultValue = "6") Integer pageSize,
                         @RequestParam(defaultValue = "movieId") String sortBy){
        List<Movie> allMovies = movieService.getAllMovie(pageNo, pageSize, sortBy);
        model.addAttribute("allMovies", allMovies);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", movieService.getAllMovie().size() / pageSize);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        model.addAttribute("username", username);

        return "movies/index";
    }

    @GetMapping("/search")
    public String searchMovie(Model model,
                             @RequestParam String keyword,
                             @RequestParam(defaultValue = "0") Integer pageNo,
                             @RequestParam(defaultValue = "6") Integer pageSize,
                             @RequestParam(defaultValue = "id") String sortBy) {
        model.addAttribute("allMovies", movieService.searchMovie(keyword));
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", movieService.searchMovie(keyword).size() / pageSize);

        return "movies/index";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Long id,
                         HttpServletRequest request,
                         Long movieId,
                         Model model){
        Movie movie = movieService.getMovieById(id);
        model.addAttribute("movie", movie);

        String[] allCinema = cinemaRepo.findCinemaNameByMovieId(id);
        model.addAttribute("allCinema", allCinema);

        String[] allTime = showTimeRepo.findTimeByMovieId(id);
        model.addAttribute("allTime", allTime);

        String[] allDay = showDayRepo.findDayByMovieId(id);
        model.addAttribute("allDay", allDay);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Long user_id = userRepo.getUserIdByUsername(username);
        model.addAttribute("username", username);

        List<Comment> comments = commentRepo.getCommentByMovieId(id);
        model.addAttribute("comments", comments);

        int rateValue = ratingService.getRatingOrDefault(user_id, id);
        model.addAttribute("rateValue", rateValue);

        List<Rating> ratings = ratingRepo.getRatingByMovieId(id);
        double averageRating = ratingService.calculateAverageRating(id);
        int totalReviewCount = ratingService.getTotalReviewCount(id);
        model.addAttribute("totalReviewCount", totalReviewCount);
        model.addAttribute("averageRating", averageRating);


        //String url = "https://21a5-42-119-149-32.ngrok-free.app/";
        String url = request.getRequestURL().toString();
        String encoded = URLEncoder.encode(url);
        String shareHref = "https://www.facebook.com/sharer/sharer.php?u=" + encoded + "&amp;src=sdkpreparse";
        model.addAttribute("shareDataHref", url);
        model.addAttribute("shareHref", shareHref);
        return "movies/details";
    }

    @GetMapping("/latest")
    public String latest(Model model){
        List<Movie> allMovies = movieService.getAllLatestMovie();
        model.addAttribute("allMovies", allMovies);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        model.addAttribute("username", username);

        return "movies/latest";
    }

    @GetMapping("/comming")
    public String comming(Model model){
        List<Movie> allMovies = movieRepo.findAllUpcomingMovies();
        model.addAttribute("allMovies", allMovies);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        model.addAttribute("username", username);

        return "movies/comming";
    }
}
