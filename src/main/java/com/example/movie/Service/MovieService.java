package com.example.movie.Service;

import com.example.movie.Entity.Cinema;
import com.example.movie.Entity.Movie;
import com.example.movie.Repository.MovieRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {
    @Autowired
private MovieRepo movieRepository;

    public List<Movie> getAllMovie() {
        return movieRepository.findAll();
    }

    public List<Movie> getAllMovie(Integer pageNo,
                                  Integer pageSize,
                                  String sortBy){
        return movieRepository.getAllMovie(pageNo, pageSize, sortBy);
    }

    public Movie getMovieById(Long id) {
        return movieRepository.findById(id).orElse(null);
    }
    Sort sortByStartDate = Sort.by(Sort.Direction.ASC, "starDate");
    public List<Movie> getAllLatestMovie(){
        return movieRepository.findAll(sortByStartDate);
    }

    public Movie addMovie(Movie movie){
        //goi repository ==> save dua vao id cua category
        return movieRepository.save(movie);
    }

    public void deleteMovie(Long id) {
        //goi repository ==> deleteById
        movieRepository.deleteById(id);
    }


//    Sort sortByStartDate_v2 = JpaSort.unsafe(Sort.Direction.ASC, "starDate > CURRENT_DATE()");
//    public List<Movie> getAllCommingMovie(){
//        return movieRepository.findAll(sortByStartDate_v2);
//    }

    public List<Movie> searchMovie(String keyword){
        return movieRepository.searchMovie(keyword);
    }
}
