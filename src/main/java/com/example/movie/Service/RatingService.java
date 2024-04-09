package com.example.movie.Service;

import com.example.movie.Entity.Rating;
import com.example.movie.Repository.RatingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.List;

@Service
public class RatingService {

    @Autowired
    private RatingRepo ratingRepo;

    public List<Rating> getAllRatings() {
        return ratingRepo.findAll();
    }

    public double calculateAverageRating(Long movieId) {
        Double averageRating = ratingRepo.getAverageRatingByMovieId(movieId);
        if (averageRating != null) {
            // Format the averageRating to display only one decimal place
            DecimalFormat decimalFormat = new DecimalFormat("#.#");
            return Double.parseDouble(decimalFormat.format(averageRating));
        } else {
            return 0.0;
        }
    }
    public int getTotalReviewCount(Long movieId) {
        return ratingRepo.getTotalReviewCountByMovieId(movieId);
    }


    public int getRatingOrDefault(Long userId, Long movieId) {
        return ratingRepo.getRatingMovieByUser(userId, movieId)
                .orElse(0);
    }
}
