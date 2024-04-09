package com.example.movie.Controllers;

import com.example.movie.Entity.Movie;
import com.example.movie.Entity.Rating;
import com.example.movie.Entity.User;
import com.example.movie.Repository.MovieRepo;
import com.example.movie.Repository.RatingRepo;
import com.example.movie.Repository.UserRepo;
import com.example.movie.Service.MovieService;
import com.example.movie.Service.RatingService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;


@Controller
public class RatingController {
    @Autowired
    private RatingRepo ratingRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private MovieService movieService;

    @Autowired
    private MovieRepo movieRepo;

    @Autowired
    private RatingService ratingService;

    @GetMapping("/ratings")
    public String getAllRatings(Model model, @PathVariable Long movieId) {
        List<Rating> ratings = ratingRepo.getRatingByMovieId(movieId);
        double averageRating = ratingService.calculateAverageRating(movieId);
        int totalReviewCount = ratingService.getTotalReviewCount(movieId);
        model.addAttribute("ratings", ratings);
        model.addAttribute("totalReviewCount", totalReviewCount);
        model.addAttribute("averageRating", averageRating);

        return "/movies/ratings";
    }

    @PostMapping("/ratings")
    public ResponseEntity<?> addRating(HttpServletRequest request, @RequestParam("value") int value) {
        String previousPageUrl = request.getHeader("Referer");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepo.findByUsername(authentication.getName());

        try {
            URI uri = new URI(previousPageUrl);
            String path = uri.getPath();
            String[] pathSegments = path.split("/");
            String movieIdStr = pathSegments[pathSegments.length - 1];
            long movieId = Long.parseLong(movieIdStr);

            Rating existingRating = ratingRepo.findByUserAndMovieId(user.getUserId(), movieId);
            if (existingRating == null) {
                Movie movie = movieService.getMovieById(movieId);
                Rating rating = new Rating();
                rating.setUser(user);
                rating.setValue(value);
                rating.setMovie(movie);
                ratingRepo.save(rating);
// After saving the rating, calculate the updated totalReviewCount and averageRating
                double updatedAverageRating = ratingService.calculateAverageRating(movieId);
                int updatedTotalReviewCount = ratingService.getTotalReviewCount(movieId);

                // Return a JSON response with the updated values
                return ResponseEntity.ok(Map.of("totalReviewCount", updatedTotalReviewCount, "averageRating", updatedAverageRating));
            } else {
                // User has already rated, return a bad request status
                return ResponseEntity.badRequest().body("You have already rated this movie.");
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error processing the request.");
        }
    }

}