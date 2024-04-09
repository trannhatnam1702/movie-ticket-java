package com.example.movie.Service;

        import com.example.movie.Entity.Movie;
        import com.example.movie.Entity.News;
        import com.example.movie.Repository.NewsRepo;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Service;


        import java.util.List;
        import java.util.Optional;

@Service
public class NewService {
    @Autowired
    private NewsRepo newsRepository;

    public List<News> getAllNews() {
        return newsRepository.findAll();
    }
    public List<News> getAllNews(Integer pageNo,
                                   Integer pageSize,
                                   String sortBy){
        return newsRepository.getAllNews(pageNo, pageSize, sortBy);
    }

    public News getNewsById(Long id) {
        return newsRepository.findById(id).orElse(null);
    }

    public News saveNews(News news) {
        return newsRepository.save(news);
    }

    public void deleteNews(Long id) {
        //goi repository ==> deleteById
        newsRepository.deleteById(id);
    }

    public List<News> searchNews(String keyword){
        return newsRepository.searchNews(keyword);
    }

}
