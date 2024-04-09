package com.example.movie.Controllers;

import com.example.movie.Entity.Celebrity;
import com.example.movie.Entity.Movie;
import com.example.movie.Entity.MovieDetails;
import com.example.movie.Entity.News;
import com.example.movie.Service.CelebrityService;
import com.example.movie.Service.HomeService;
import com.example.movie.Service.MovieDetailsService;
import com.example.movie.Service.NewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/celebrity/detail/{id}")
public class CelebritiesController {
    @Autowired
   private CelebrityService celebrityService;

//    @GetMapping()
//    public String home(Model model){
//        long id = 1L;
//        Celebrity celebrity = celebrityService.getCelebrityById(id);
//
//        model.addAttribute("celebrity", celebrity);
//        return "celebrities/index";
//    }

    @GetMapping()
    public String detail(@PathVariable("id") Long id, Model model){
        if (id == null) {
            id = 1L; // Gán giá trị mặc định là 1
        }
        Celebrity celebrity = celebrityService.getCelebrityById(id);
        model.addAttribute("celebrity", celebrity);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        model.addAttribute("username", username);

        return "celebrities/details";
    }
}
