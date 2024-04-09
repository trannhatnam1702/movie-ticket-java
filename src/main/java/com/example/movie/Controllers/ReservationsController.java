package com.example.movie.Controllers;

import com.example.movie.Entity.Movie;
import com.example.movie.Entity.Reservation;
import com.example.movie.Entity.Shows;
import com.example.movie.Repository.ReservationRepo;
import com.example.movie.Repository.ShowsRepo;
import com.example.movie.Service.MovieService;
import com.example.movie.Service.ReservationService;
import com.example.movie.Service.ShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@SpringBootApplication
@RequestMapping("/reservations/{id}/{movie_id}")
public class ReservationsController {
    @Autowired
    private ReservationService reservationService;

    @Autowired
    private MovieService movieService;

    @Autowired
    private ShowService showService;
    @Autowired
    private ShowsRepo showsRepo;

    @Autowired
    private ReservationRepo reservationRepo;
    @GetMapping
    public String reservations(@PathVariable Long id, @PathVariable Long movie_id, Model model){
        model.addAttribute("movie_id", movie_id);

        Movie movie = movieService.getMovieById(movie_id);
        model.addAttribute("movie", movie);

        List<Reservation> reservations = reservationService.getAllReservations();
        model.addAttribute("reservations", reservations);

        List<Integer> seatIds = reservationRepo.findSeatByShowId(id);
        model.addAttribute("seatIds", seatIds);

        List<Integer> showIds = showsRepo.findShowByMovieId(movie_id);
        model.addAttribute("re", showIds);

        String[] cinema_name = reservationRepo.getCinemaByShowId(id);
        model.addAttribute("cinema_name", cinema_name);

        String[] day = reservationRepo.getDayByShowId(id);
        model.addAttribute("day", day);

        String[] time = reservationRepo.getTimeByShowId(id);
        model.addAttribute("time", time);

        Shows show = showService.getShowById(id);
        model.addAttribute("showId", show.getShowId());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        model.addAttribute("username", username);

        return "reservations/chooseshow";
    }
}
