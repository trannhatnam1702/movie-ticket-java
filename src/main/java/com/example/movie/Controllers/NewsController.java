package com.example.movie.Controllers;

import com.example.movie.Entity.Movie;
import com.example.movie.Entity.News;
import com.example.movie.Service.MovieService;
import com.example.movie.Service.NewService;
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

import java.util.List;

@Controller
@SpringBootApplication
@RequestMapping("/news")
public class NewsController {
    @Autowired
    private NewService newService;
    @GetMapping
    public String news( Model model,
                        @RequestParam(defaultValue = "0") Integer pageNo,
                        @RequestParam(defaultValue = "4") Integer pageSize,
                        @RequestParam(defaultValue = "newId") String sortBy){
        List<News> allNews = newService.getAllNews(pageNo, pageSize, sortBy);
        model.addAttribute("allNews", allNews);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", newService.getAllNews().size() / pageSize);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        model.addAttribute("username", username);
        return "news/index";
    }
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        model.addAttribute("username", username);

        List<News> allNews = newService.getAllNews();
        model.addAttribute("allNews", allNews);

        model.addAttribute("news", newService.getNewsById(id));
        return "news/detail"; //trả về view của templates/book/edit
    }
}
