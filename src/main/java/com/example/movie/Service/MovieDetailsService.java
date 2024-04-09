package com.example.movie.Service;

import com.example.movie.Entity.Movie;
import com.example.movie.Entity.MovieDetails;
import com.example.movie.Repository.MovieDetailRepo;
import com.example.movie.Repository.MovieRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;




        import com.example.movie.Entity.Movie;
        import com.example.movie.Repository.MovieRepo;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Service;

        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.data.domain.Page;
        import org.springframework.data.domain.PageRequest;
        import org.springframework.data.domain.Pageable;
        import org.springframework.data.domain.Sort;
        import org.springframework.stereotype.Service;

        import java.util.List;
        import java.util.Optional;

@Service
public class MovieDetailsService {
    @Autowired
    private MovieDetailRepo movieDetailRepository;

    public List<MovieDetails> getAllMovieDetail() {
        return movieDetailRepository.findAll();
    }

}
