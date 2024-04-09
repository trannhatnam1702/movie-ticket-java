package com.example.movie.Controllers;


import com.example.movie.Entity.*;
import com.example.movie.Repository.ReservationRepo;
import com.example.movie.Repository.RoleRepo;
import com.example.movie.Repository.UserRepo;
import com.example.movie.Service.*;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Controller
@SpringBootApplication
@RequestMapping("/admin")
public class AdminController {

    //-----------------MOVIE--------------------
    @Autowired
    private MovieService movieService;

    //MOVIE
    @GetMapping("/movie")
    public String adminMovies(Model model,
                              @RequestParam(defaultValue = "0") Integer pageNo,
                              @RequestParam(defaultValue = "6") Integer pageSize,
                              @RequestParam(defaultValue = "movieId") String sortBy){
        List<Movie> allMovies = movieService.getAllMovie(pageNo, pageSize, sortBy);
        model.addAttribute("movies", allMovies);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", movieService.getAllMovie().size() / pageSize);
        return "admin/movie/index";
    }

    @GetMapping("/movie/search")
    public String searchMovie(@NotNull Model model, @RequestParam String keyword,
                              @RequestParam(defaultValue = "0") Integer pageNo,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "id") String sortBy) {
        model.addAttribute("movies", movieService.searchMovie(keyword));
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", movieService.searchMovie(keyword).size()/pageSize);
        return "admin/movie/index";
    }

    @GetMapping("/movie/add")
    public String addMovieForm(Model model){
        //truyen 2 tham so sang view de hien thi
        model.addAttribute("movie", new Movie());
        //truyen danh sach category de hien thi cho user chon
        //model.addAttribute("categories",categoryService.getAllCategories());
        //goi layout
        return "admin/movie/add";
    }

    public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/src/main/resources/static/assets/img";

    @PostMapping("/movie/add")
    public String addMovie(@Valid @ModelAttribute("movie") Movie movie,
                           @RequestParam("image") MultipartFile file,
                           BindingResult bindingResult,
                           Model model,
                           RedirectAttributes redirectAsttributes) throws Exception, ParseException {
        StringBuilder fileNames = new StringBuilder();
        Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, file.getOriginalFilename());
        fileNames.append(file.getOriginalFilename());
        Files.write(fileNameAndPath, file.getBytes());
        movie.setImageURL(file.getOriginalFilename());
        model.addAttribute("msg", "Uploaded images: " + fileNames.toString());

        String viewName = "admin/movie/add";
        if (bindingResult.hasErrors()){
            model.addAttribute("movie", movie);
            return "admin/movie/add";
        }

        movieService.addMovie(movie);
        return "redirect:/admin/movie";
    }

    @GetMapping("/movie/edit/{id}")
    public  String editMovieForm(@PathVariable("id") Long movieId, Model model){
        model.addAttribute("movie",movieService.getMovieById(movieId));
        return "admin/movie/edit";
    }


    @PostMapping("/movie/edit")
    public String editMovie(@Valid @ModelAttribute("movie") Movie updateMovie,
                            @RequestParam("file") MultipartFile file,
                            @RequestParam("movieName") String movieName,
                            @RequestParam("genres") String genres,
                            @RequestParam("time") String time,
                            @RequestParam("endDate") String endDate,
                            @RequestParam("starDate") String starDate,
                            @RequestParam("language") String language,
                            @RequestParam("director") String director,
                            @RequestParam("writer") String writer,
                            @RequestParam("trailer") String trailer,
                            @RequestParam("Description") String description) throws Exception {

        updateMovie = movieService.getMovieById(updateMovie.getMovieId());
        if(file.isEmpty()) {
            updateMovie.setImageURL(updateMovie.getImageURL());


        } else {
            StringBuilder fileNames = new StringBuilder();

            Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, file.getOriginalFilename());
            fileNames.append(file.getOriginalFilename());
            byte[] fileBytes = file.getBytes();
            Files.write(fileNameAndPath, fileBytes);
            updateMovie.setImageURL(file.getOriginalFilename());

        }
        updateMovie.setMovieName(movieName);
        updateMovie.setGenres(genres);
        updateMovie.setTime(time);
        updateMovie.setEndDate(endDate);
        updateMovie.setStarDate(starDate);
        updateMovie.setLanguage(language);
        updateMovie.setDirector(director);
        updateMovie.setWriter(writer);
        updateMovie.setTrailer(trailer);
        updateMovie.setDescription(description);

        movieService.addMovie(updateMovie);
        return "redirect:/admin/movie";
    }

    @GetMapping("/movie/delete/{id}")
    public String deleteMovieForm(@PathVariable("id") Long id){
        movieService.deleteMovie(id);
        return "redirect:/admin/movie";
    }

    @GetMapping("/movie/export")
    public void exportMovie(HttpServletResponse response) throws IOException {
        List<Movie> movies = movieService.getAllMovie();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Movies");
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Movie's Id");
        headerRow.createCell(1).setCellValue("Name");
        headerRow.createCell(2).setCellValue("Time");
        headerRow.createCell(3).setCellValue("Start date");
        headerRow.createCell(4).setCellValue("End date");
        headerRow.createCell(5).setCellValue("Image URL");
        headerRow.createCell(6).setCellValue("Genres");
        headerRow.createCell(7).setCellValue("Director");
        headerRow.createCell(8).setCellValue("Writer");
        headerRow.createCell(9).setCellValue("Language");
        headerRow.createCell(10).setCellValue("Trailer");
        headerRow.createCell(11).setCellValue("Description");

        for (int i = 0; i < movies.size(); i++) {
            Row row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(movies.get(i).getMovieId());
            row.createCell(1).setCellValue(movies.get(i).getMovieName());
            row.createCell(2).setCellValue(movies.get(i).getTime());
            row.createCell(3).setCellValue(String.valueOf(movies.get(i).getStarDate()));
            row.createCell(4).setCellValue(String.valueOf(movies.get(i).getEndDate()));
            row.createCell(5).setCellValue(movies.get(i).getImageURL());
            row.createCell(6).setCellValue(movies.get(i).getGenres());
            row.createCell(7).setCellValue(movies.get(i).getDirector());
            row.createCell(8).setCellValue(movies.get(i).getWriter());
            row.createCell(9).setCellValue(movies.get(i).getLanguage());
            row.createCell(10).setCellValue(movies.get(i).getTrailer());
            row.createCell(11).setCellValue(movies.get(i).getDescription());
        }
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=movies.xlsx");
        workbook.write(response.getOutputStream());
    }

    //--------------CINEMA--------------//

    @Autowired
    private CinemaService cinemaService;

    @GetMapping("/cinema")
    public String adminCinema(Model model){
        List<Cinema> allCinema = cinemaService.getAllCinema();
        model.addAttribute("cinemas", allCinema);
        return "admin/cinema/index";
    }

    @GetMapping("/cinema/add")
    public String addCinemaForm(Model model){
        model.addAttribute("cinema", new Cinema());
        return "admin/cinema/add";
    }

    @PostMapping("/cinema/add")
    public String addCinema(@Valid @ModelAttribute("cinema") Cinema cinema,
                            BindingResult bindingResult,
                            Model model,
                            RedirectAttributes redirectAsttributes){
        String viewName = "admin/cinema/add";
        if (bindingResult.hasErrors()){
            model.addAttribute("cinema",cinema);
            return "admin/cinema/add";
        }
        cinemaService.addCinema(cinema);
        return "redirect:/admin/cinema";
    }

    @GetMapping("/cinema/edit/{id}")
    public String editCinemaForm(@PathVariable("id") Long id, Model model){
        model.addAttribute("cinema",cinemaService.getCinemaById(id));
        return "admin/cinema/edit";
    }

    @PostMapping("/cinema/edit")
    public String editCine(@Valid @ModelAttribute("cinema") Cinema updatecinema){
        cinemaService.addCinema(updatecinema);
        //Sau khi add xong thi chuyen ve màn hình danh sách list
        return "redirect:/admin/cinema";
    }

    @GetMapping("/cinema/delete/{id}")
    public String deleteCinemaForm(@PathVariable("id") Long id){
        cinemaService.deleteCinema(id);
        return "redirect:/admin/cinema";
    }

    //-------------CELEBRITY---------------//

    @Autowired
    private CelebrityService celebrityService;

    @GetMapping("/celebrity")
    public String adminCelebrities(Model model,
                                   @RequestParam(defaultValue = "0") Integer pageNo,
                                   @RequestParam(defaultValue = "6") Integer pageSize,
                                   @RequestParam(defaultValue = "celebrityId") String sortBy){

        List<Celebrity> allCelebrities = celebrityService.getAllCelebrity(pageNo, pageSize, sortBy);
        model.addAttribute("celebrities", allCelebrities);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", movieService.getAllMovie().size() / pageSize);


        return "admin/celebrity/index";
    }


    @GetMapping("/celebrity/search")
    public String searchCelebrity(Model model,
                                  @RequestParam String keyword,
                                  @RequestParam(defaultValue = "0") Integer pageNo,
                                  @RequestParam(defaultValue = "6") Integer pageSize,
                                  @RequestParam(defaultValue = "celebrityId") String sortBy) {

        model.addAttribute("celebrities",celebrityService.searchCelebrity(keyword));
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", celebrityService.searchCelebrity(keyword).size() / pageSize);
        return "admin/celebrity/index";
    }


    @GetMapping("/celebrity/add")
    public String addCelebrityForm(Model model){
        model.addAttribute("celebrity", new Celebrity());
        return "admin/celebrity/add";
    }


    @PostMapping("/celebrity/add")
    public String addCelebrity(@Valid @ModelAttribute("celebrity") Celebrity celebrity,
                               @RequestParam("image") MultipartFile file,
                               BindingResult bindingResult,
                               Model model,
                               RedirectAttributes redirectAsttributes) throws Exception {
        StringBuilder fileNames = new StringBuilder();
        Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, file.getOriginalFilename());
        fileNames.append(file.getOriginalFilename());
        Files.write(fileNameAndPath, file.getBytes());
        celebrity.setUrlAvatar(file.getOriginalFilename());
        model.addAttribute("msg", "Uploaded images: " + fileNames.toString());

        String viewName = "admin/celebrity/add";
        if (bindingResult.hasErrors()){
            model.addAttribute("celebrity", celebrity);
            return "admin/celebrity/add";
        }

        celebrityService.addcelebrity(celebrity);
        return "redirect:/admin/celebrity";
    }

    @GetMapping("/celebrity/edit/{id}")
    public  String editCelebrityForm(@PathVariable("id") Long celebrityId, Model model){
        model.addAttribute("celebrity",celebrityService.getCelebrityById(celebrityId));
        return "admin/celebrity/edit";
    }

    @PostMapping("/celebrity/edit")
    public String editCeleb(@Valid @ModelAttribute("celebrity") Celebrity updateCelebrity,
                            @RequestParam("image") MultipartFile file,
                            @RequestParam("Name") String Name,
                            @RequestParam("Height") String Height,
                            @RequestParam("Weight") String Weight,
                            @RequestParam("Description") String Description,
                            @RequestParam("Language") String Language) throws Exception {
        updateCelebrity = celebrityService.getCelebrityById(updateCelebrity.getCelebrityId());
        if(file.isEmpty()) {
            updateCelebrity.setUrlAvatar(updateCelebrity.getUrlAvatar());
        } else {
            StringBuilder fileNames = new StringBuilder();
            Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, file.getOriginalFilename());
            fileNames.append(file.getOriginalFilename());
            byte[] fileBytes = file.getBytes();
            Files.write(fileNameAndPath, fileBytes);
            updateCelebrity.setUrlAvatar(file.getOriginalFilename());
        }
        updateCelebrity.setName(Name);
        updateCelebrity.setHeight(Height);
        updateCelebrity.setWeight(Weight);
        updateCelebrity.setDescription(Description);
        updateCelebrity.setLanguage(Language);
        celebrityService.addcelebrity(updateCelebrity);
        return "redirect:/admin/celebrity";
    }

    @GetMapping("/celebrity/delete/{id}")
    public String deleteCelebrityForm(@PathVariable("id") Long id){
        celebrityService.deleteCelebrity(id);
        return "redirect:/admin/celebrity";
    }

    @GetMapping("/celebrity/export")
    public void exportCelebrity(HttpServletResponse response) throws IOException {
        List<Celebrity> celebrities = celebrityService.getAllCelebrity();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Celebrities");
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Celebrity's Id");
        headerRow.createCell(1).setCellValue("Name");
        headerRow.createCell(2).setCellValue("Weight");
        headerRow.createCell(3).setCellValue("Height");
        headerRow.createCell(4).setCellValue("Avatar");
        headerRow.createCell(5).setCellValue("Language");
        headerRow.createCell(6).setCellValue("Description");

        for (int i = 0; i < celebrities.size(); i++) {
            Row row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(celebrities.get(i).getCelebrityId());
            row.createCell(1).setCellValue(celebrities.get(i).getName());
            row.createCell(2).setCellValue(celebrities.get(i).getWeight());
            row.createCell(3).setCellValue(celebrities.get(i).getHeight());
            row.createCell(4).setCellValue(celebrities.get(i).getUrlAvatar());
            row.createCell(5).setCellValue(celebrities.get(i).getLanguage());
            row.createCell(6).setCellValue(celebrities.get(i).getDescription());
        }
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=celebrities.xlsx");
        workbook.write(response.getOutputStream());
    }


    //----------------SHOW DAY------------------//

    @Autowired
    private ShowDayService showDayService;

    @GetMapping("/showday")
    public String adminShowDay(Model model){
        List<ShowDay> allShowDay = showDayService.getAllShowDay();
        model.addAttribute("showDays", allShowDay);
        return "admin/showday/index";
    }

    @GetMapping("/showday/add")
    public String addShowDayForm(Model model){
        model.addAttribute("showDay", new ShowDay());
        return "admin/showday/add";
    }

    @PostMapping("/showday/add")
    public String addShowDay(@Valid @ModelAttribute("showDay") ShowDay showDay,
                             BindingResult bindingResult,
                             Model model,
                             RedirectAttributes redirectAsttributes){
        String viewName = "admin/showday/add";
        if (bindingResult.hasErrors()){
            model.addAttribute("showDay",showDay);
            return "admin/showday/add";
        }
        showDayService.addShowDay(showDay);
        return "redirect:/admin/showday";
    }

    @GetMapping("/showday/edit/{id}")
    public String editShowDayForm(@PathVariable("id") Long id, Model model){
        model.addAttribute("showDay",showDayService.getShowDayById(id));
        return "admin/showday/edit";
    }

    @PostMapping("/showday/edit")
    public String editShowDay(@Valid @ModelAttribute("showDay") ShowDay updateShowDay){
        showDayService.addShowDay(updateShowDay);
        //Sau khi add xong thi chuyen ve màn hình danh sách list
        return "redirect:/admin/showday";
    }

    @GetMapping("/showday/delete/{id}")
    public String deleteShowDayForm(@PathVariable("id") Long id){
        showDayService.deleteShowDay(id);
        return "redirect:/admin/showday";
    }

    //------------------SHOW TIME-------------------//

    @Autowired
    private ShowTimeService showTimeService;

    @GetMapping("/showtime")
    public String adminShowTime(Model model){
        List<ShowTime> allShowTime = showTimeService.getAllShowTime();
        model.addAttribute("showTimes", allShowTime);
        return "admin/showtime/index";
    }

    @GetMapping("/showtime/add")
    public String addShowTimeForm(Model model){
        model.addAttribute("showTime", new ShowTime());
        return "admin/showtime/add";
    }

    @PostMapping("/showtime/add")
    public String addShowTime(@Valid @ModelAttribute("showTime") ShowTime showTime,
                              BindingResult bindingResult,
                              Model model,
                              RedirectAttributes redirectAsttributes){
        String viewName = "admin/showtime/add";
        if (bindingResult.hasErrors()){
            model.addAttribute("shoTime",showTime);
            return "admin/showtime/add";
        }
        showTimeService.addShowTime(showTime);
        return "redirect:/admin/showtime";
    }

    @GetMapping("/showtime/edit/{id}")
    public String editShowTimeForm(@PathVariable("id") Long id, Model model){
        model.addAttribute("showTime",showTimeService.getShowTimeById(id));
        return "admin/showtime/edit";
    }

    @PostMapping("/showtime/edit")
    public String editShowTime(@Valid @ModelAttribute("showTime") ShowTime updateShowTime){
        showTimeService.addShowTime(updateShowTime);
        //Sau khi add xong thi chuyen ve màn hình danh sách list
        return "redirect:/admin/showtime";
    }

    @GetMapping("/showtime/delete/{id}")
    public String deleteShowTimeForm(@PathVariable("id") Long id){
        showTimeService.deleteShowTime(id);
        return "redirect:/admin/showtime";
    }

    //---------------------SEAT-------------------//

    @Autowired
    private SeatService seatService;

    @GetMapping("/seat")
    public String adminSeat(Model model,
                            @RequestParam(defaultValue = "0") Integer pageNo,
                            @RequestParam(defaultValue = "10") Integer pageSize,
                            @RequestParam(defaultValue = "seatId") String sortBy){
        List<Seat> allSeat = seatService.getAllSeat(pageNo,pageSize,sortBy);
        model.addAttribute("seats", allSeat);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", seatService.getAllSeat().size() / pageSize);
        return "admin/seat/index";
    }

    @GetMapping("/seat/add")
    public String addSeatForm(Model model){
        model.addAttribute("seat", new Seat());
        return "admin/seat/add";
    }

    @PostMapping("/seat/add")
    public String addShowTime(@Valid @ModelAttribute("seat") Seat seat,
                              BindingResult bindingResult,
                              Model model,
                              RedirectAttributes redirectAsttributes){
        String viewName = "admin/seat/add";
        if (bindingResult.hasErrors()){
            model.addAttribute("seat",seat);
            return "admin/seat/add";
        }
        seatService.addSeat(seat);
        return "redirect:/admin/seat";
    }

    @GetMapping("/seat/edit/{id}")
    public String editSeatForm(@PathVariable("id") Long id, Model model){
        model.addAttribute("seat",seatService.getSeatById(id));
        return "admin/seat/edit";
    }

    @PostMapping("/seat/edit")
    public String editSeat(@Valid @ModelAttribute("seat") Seat updateSeat){
        seatService.addSeat(updateSeat);
        //Sau khi add xong thi chuyen ve màn hình danh sách list
        return "redirect:/admin/seat";
    }

    @GetMapping("/seat/delete/{id}")
    public String deleteSeatForm(@PathVariable("id") Long id){
        seatService.deleteSeat(id);
        return "redirect:/admin/seat";
    }

    //-------------------NEWS---------------------//

    @Autowired
    private NewService newService;

    @GetMapping("/news")
    public String adminNews(Model model,
                            @RequestParam(defaultValue = "0") Integer pageNo,
                            @RequestParam(defaultValue = "10") Integer pageSize,
                            @RequestParam(defaultValue = "newId") String sortBy){
        List<News> allNews = newService.getAllNews(pageNo,pageSize,sortBy);
        model.addAttribute("news", allNews);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", newService.getAllNews().size()/pageSize);
        return "admin/news/index";
    }

    @GetMapping("news/search")
    public String searchNews(@NotNull Model model, @RequestParam String keyword,
                             @RequestParam(defaultValue = "0") Integer pageNo,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             @RequestParam(defaultValue = "newId") String sortBy) {
        model.addAttribute("news", newService.searchNews(keyword));
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", newService.searchNews(keyword).size()/pageSize);
        return "admin/news/index";
    }

    @GetMapping("/news/add")
    public String addNewsForm(Model model){
        model.addAttribute("news", new News());
        model.addAttribute("movies", movieService.getAllMovie());
        return "admin/news/add";
    }

    @PostMapping("/news/add")
    public String addNew(@Valid @ModelAttribute("news") News news,
                               @RequestParam("file") MultipartFile file,
                               BindingResult bindingResult,
                               Model model,
                               RedirectAttributes redirectAsttributes) throws Exception {

        StringBuilder fileNames = new StringBuilder();
        Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, file.getOriginalFilename());
        fileNames.append(file.getOriginalFilename());
        Files.write(fileNameAndPath, file.getBytes());
        news.setImage(file.getOriginalFilename());
        model.addAttribute("msg", "Uploaded images: " + fileNames.toString());

        String viewName = "admin/news/add";
        if (bindingResult.hasErrors()){
            model.addAttribute("news", news);
            model.addAttribute("movies", movieService.getAllMovie());
            return "admin/news/add";
        }

        newService.saveNews(news);
        return "redirect:/admin/news";
    }

    @GetMapping("/news/edit/{id}")
    public String editNewsForm(@PathVariable("id") Long id, Model model){
        model.addAttribute("news",newService.getNewsById(id));
        model.addAttribute("movies", movieService.getAllMovie());
        return "admin/news/edit";
    }

    @PostMapping("/news/edit")
    public String editNews(@Valid @ModelAttribute("news") News updateNews,
                           @RequestParam("file") MultipartFile file,
                           @RequestParam("movieId") String movieId,
                           @RequestParam("Title") String Title,
                           @RequestParam("Date") String Date,
                           @RequestParam("Description") String Description) throws Exception{
        updateNews = newService.getNewsById(updateNews.getNewId());
        if(file.isEmpty()) {
            updateNews.setImage(updateNews.getImage());
        } else {
            StringBuilder fileNames = new StringBuilder();

            Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, file.getOriginalFilename());
            fileNames.append(file.getOriginalFilename());
            byte[] fileBytes = file.getBytes();
            Files.write(fileNameAndPath, fileBytes);
            updateNews.setImage(file.getOriginalFilename());
        }
        Movie movie = movieService.getMovieById(Long.parseLong(movieId));
        updateNews.setMovie(movie);
        updateNews.setTitle(Title);
        updateNews.setDate(Date);
        updateNews.setDescription(Description);
        newService.saveNews(updateNews);
        //Sau khi add xong thi chuyen ve màn hình danh sách list
        return "redirect:/admin/news";
    }

    @GetMapping("/news/delete/{id}")
    public String deleteNewsForm(@PathVariable("id") Long id){
        newService.deleteNews(id);
        return "redirect:/admin/news";
    }

    //------------------SHOWS-------------------//

    @Autowired
    private ShowService showService;

    @GetMapping("/shows")
    public String adminShows(Model model,
                             @RequestParam(defaultValue = "0") Integer pageNo,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             @RequestParam(defaultValue = "showId") String sortBy){
        List<Shows> allShows = showService.getAllShows(pageNo, pageSize, sortBy);
        model.addAttribute("shows", allShows);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", showService.getAllShows().size()/pageSize);
        return "admin/shows/index";
    }

    @GetMapping("shows/search")
    public String searchShow(@NotNull Model model, @RequestParam String keyword,
                             @RequestParam(defaultValue = "0") Integer pageNo,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             @RequestParam(defaultValue = "showId") String sortBy) {
        model.addAttribute("shows", showService.searchShows(keyword));
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", showService.searchShows(keyword).size()/pageSize);
        return "admin/shows/index";
    }

    @GetMapping("/shows/add")
    public String addShowsForm(Model model){
        model.addAttribute("shows", new Shows());
        model.addAttribute("movies", movieService.getAllMovie());
        model.addAttribute("cinemas", cinemaService.getAllCinema());
        model.addAttribute("showDays", showDayService.getAllShowDay());
        model.addAttribute("showTimes", showTimeService.getAllShowTime());
        return "admin/shows/add";
    }



    @PostMapping("/shows/add")
    public String addShowTime(@Valid @ModelAttribute("shows") Shows shows,
                              BindingResult bindingResult,
                              Model model,
                              RedirectAttributes redirectAsttributes){
        String viewName = "admin/shows/add";
        if (bindingResult.hasErrors()){
            model.addAttribute("shows",shows);
            model.addAttribute("movies", showService.getAllShows());
            model.addAttribute("cinemas", cinemaService.getAllCinema());
            model.addAttribute("showDays", showDayService.getAllShowDay());
            model.addAttribute("showTimes", showTimeService.getAllShowTime());
            return "admin/shows/add";
        }
        showService.addShows(shows);
        return "redirect:/admin/shows";
    }

    @GetMapping("/shows/edit/{id}")
    public String editShowsForm(@PathVariable("id") Long id, Model model){
        model.addAttribute("shows",showService.getShowsById(id));
        model.addAttribute("movies", movieService.getAllMovie());
        model.addAttribute("cinemas", cinemaService.getAllCinema());
        model.addAttribute("showDays", showDayService.getAllShowDay());
        model.addAttribute("showTimes", showTimeService.getAllShowTime());
        return "admin/shows/edit";
    }

    @PostMapping("/shows/edit")
    public String editShow(@Valid @ModelAttribute("shows") Shows updateShows){
        showService.addShows(updateShows);
        //Sau khi add xong thi chuyen ve màn hình danh sách list
        return "redirect:/admin/shows";
    }

    @GetMapping("/shows/delete/{id}")
    public String deleteShowsForm(@PathVariable("id") Long id){
        showService.deleteShows(id);
        return "redirect:/admin/shows";
    }

    //------------------RESERVATION------------------//

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private UserService userService;


    @GetMapping("/reservation")
    public String adminReservation(Model model,
                                   @RequestParam(defaultValue = "0") Integer pageNo,
                                   @RequestParam(defaultValue = "10") Integer pageSize,
                                   @RequestParam(defaultValue = "reservationId") String sortBy){

        List<Reservation> allReservation = reservationService.getAllReservations(pageNo, pageSize, sortBy);
        model.addAttribute("reservations", allReservation);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", reservationService.getAllReservations().size()/pageSize);

        return "admin/reservation/index";
    }

    @GetMapping("/reservation/search")
    public String searchReservation(@NotNull Model model, @RequestParam String keyword,
                                    @RequestParam(defaultValue = "0") Integer pageNo,
                                    @RequestParam(defaultValue = "10") Integer pageSize,
                                    @RequestParam(defaultValue = "reservationId") String sortBy) {
        model.addAttribute("reservations", reservationService.searchReservation(keyword));
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", reservationService.searchReservation(keyword).size()/pageSize);
        return "admin/reservation/index";
    }

    @GetMapping("/reservation/add")
    public String addReservationForm(Model model){
        model.addAttribute("reservation", new Reservation());
        model.addAttribute("users", userService.getAllUser());
        model.addAttribute("shows", showService.getAllShows());
        model.addAttribute("seats", seatService.getAllSeat());
        model.addAttribute("invoices", invoiceService.getAllInvoices());
        return "admin/reservation/add";
    }

    @PostMapping("/reservation/add")
    public String addReservation(@Valid @ModelAttribute("reservation") Reservation reservation,
                                 BindingResult bindingResult,
                                 Model model,
                                 RedirectAttributes redirectAsttributes){
        String viewName = "admin/reservation/add";
        if (bindingResult.hasErrors()){
            model.addAttribute("reservation",reservation);
            model.addAttribute("users", userService.getAllUser());
            model.addAttribute("shows", showService.getAllShows());
            model.addAttribute("seats", seatService.getAllSeat());
            return "admin/reservation/add";
        }
        reservationService.addReservationV2(reservation);
        return "redirect:/admin/reservation";
    }

    @GetMapping("/reservation/edit/{id}")
    public String editReservationForm(@PathVariable("id") Long id, Model model){
        model.addAttribute("reservation",reservationService.getReservationById(id));
        model.addAttribute("users", userService.getAllUser());
        model.addAttribute("shows", showService.getAllShows());
        model.addAttribute("seats", seatService.getAllSeat());
        model.addAttribute("invoices", invoiceService.getAllInvoices());
        return "admin/reservation/edit";
    }

    @PostMapping("/reservation/edit")
    public String editReservation(@Valid @ModelAttribute("reservation") Reservation updateReservation){
        reservationService.addReservationV2(updateReservation);
        //Sau khi add xong thi chuyen ve màn hình danh sách list
        return "redirect:/admin/reservation";
    }

    @GetMapping("/reservation/delete/{id}")
    public String deleteReservation(@PathVariable("id") Long id){
        reservationService.deleteReservation(id);
        return "redirect:/admin/reservation";
    }

    @GetMapping("/reservation/export")
    public void exportReservation(HttpServletResponse response) throws IOException {
        List<Reservation> reservations = reservationService.getAllReservations();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Reservations");
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Reservation's Id");
        headerRow.createCell(1).setCellValue("Customer Name");
        headerRow.createCell(2).setCellValue("Seat No.");
        headerRow.createCell(3).setCellValue("Movie's Name");
        headerRow.createCell(4).setCellValue("Day");
        headerRow.createCell(5).setCellValue("Time");
        headerRow.createCell(6).setCellValue("Cinema");

        for (int i = 0; i < reservations.size(); i++) {
            Row row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(reservations.get(i).getReservationId());
            //User user = reservations.get(i).getUser();
            //row.createCell(1).setCellValue(user.getUsername());
            Seat seat = reservations.get(i).getSeat();
            row.createCell(2).setCellValue(seat.getSeatNo());
            Shows shows = reservations.get(i).getShows();
            row.createCell(3).setCellValue(shows.getMovie().getMovieName());
            row.createCell(4).setCellValue(shows.getShowDay().getDay());
            row.createCell(5).setCellValue(shows.getShowTime().getTime());
            row.createCell(6).setCellValue(shows.getCinema().getCinemaName());
        }
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=reservations.xlsx");
        workbook.write(response.getOutputStream());
    }

    //--------------PROFILE----------------//

    @Autowired
    private UserRepo userRepo;

    @GetMapping("/profile")
    public String getProfile(Model model, Authentication authentication) {
        model.addAttribute("user", userRepo.findByUsername(authentication.getName()));
        return "admin/account/profile";
    }

    //--------------ACCOUNT--------------//

    @Autowired
    private RoleRepo roleRepo;


    @GetMapping("/account")
    public String getAllUser(Model model,
                             @RequestParam(defaultValue = "0") Integer pageNo,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             @RequestParam(defaultValue = "userId") String sortBy)
    {
        List<User> users = userService.getAllUser(pageNo, pageSize, sortBy);

        String[] userrole = userRepo.getAllUserRole();
        List<String> userrolename = new ArrayList<>();

        String name;
        for(int i = 0 ; i < userrole.length; i++)
        {
            name = roleRepo.getNameByRoleId(Long.parseLong(userrole[i]));
            userrolename.add(name);
        }
        model.addAttribute("users", users);
        model.addAttribute("userrolename", userrolename);

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", userService.getAllUser().size()/pageSize);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        model.addAttribute("username", username);

        return "admin/account/index";
    }

    @GetMapping("/account/search")
    public String searchUser(@NotNull Model model, @RequestParam String keyword,
                             @RequestParam(defaultValue = "0") Integer pageNo,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             @RequestParam(defaultValue = "userId") String sortBy) {
        model.addAttribute("users", userService.searchUser(keyword));
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", userService.searchUser(keyword).size()/pageSize);
        return "admin/account/index";
    }

    @GetMapping("/account/add")
    public String addAccountForm(Model model){
        model.addAttribute("user", new User());
        return "admin/account/add";
    }

    @PostMapping("/account/add")
    public String addAccount(@Valid @ModelAttribute("user") User user,
                             BindingResult bindingResult,
                             Model model,
                             RedirectAttributes redirectAsttributes){

        String viewName = "admin/account/add";
        if (bindingResult.hasErrors()){
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                model.addAttribute(error.getField() + "_error",
                        error.getDefaultMessage());
            }
            return "admin/account/add";
        }
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userService.save(user);
        return "redirect:/admin/account";
    }


//    @PostMapping("/register")
//    public String register(@Valid @ModelAttribute("user") User user,
//                           BindingResult bindingResult, Model model) {
//        if (bindingResult.hasErrors()) {
//            List<FieldError> errors = bindingResult.getFieldErrors();
//            for (FieldError error : errors) {
//                model.addAttribute(error.getField() + "_error",
//                        error.getDefaultMessage());
//            }
//            return "account/register";
//        }
//        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
//        userService.save(user);
//        return "redirect:/account/login";
//    }

    @GetMapping("/account/edit/{id}")
    public String editAccountForm(@PathVariable("id") Long id, Model model){
        model.addAttribute("user",userService.getUserById(id));

        String[] AllRole = userRepo.getAllRole();
        model.addAttribute("AllRole", AllRole);

        return "admin/account/edit";
    }

    @PostMapping("/account/edit")
    public String editAccount(@Valid @ModelAttribute("user") User updateUser, @ModelAttribute(("name")) String name){
        updateUser.setPassword(new BCryptPasswordEncoder().encode(updateUser.getPassword()));
        userService.addSUser(updateUser);

        Long role_id = roleRepo.getRoleIdByName(name);
        Long user_id = updateUser.getUserId();

        userRepo.saveUserRole(user_id, role_id);

        return "redirect:/admin/account";
    }

    @GetMapping("/account/delete/{id}")
    public String deleteAccountForm(@PathVariable("id") Long id){
        userService.deleteUser(id);
        return "redirect:/admin/account";
    }

    //---------------------DASHBOARD------------------------//

    @Autowired
    private ReservationRepo reservationRepo;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        //model.addAttribute("revenue", reservationRepo.revenue());
        model.addAttribute("reservation", reservationService.getAllReservations().stream().count());
        model.addAttribute("movies", movieService.getAllMovie().stream().count());
        model.addAttribute("users", userService.getAllUser().stream().count());
        model.addAttribute("revenue", invoiceService.getTotalRevenue());


        return "admin/dashboard/index";
    }

    //----------------------COMMENT-----------------//

    @Autowired
    private CommentService commentService;

    @GetMapping("/comment")
    public String adminComment(Model model,
                               @RequestParam(defaultValue = "0") Integer pageNo,
                               @RequestParam(defaultValue = "10") Integer pageSize,
                               @RequestParam(defaultValue = "commentId") String sortBy){
        List<Comment> allComment = commentService.getAllComments(pageNo, pageSize, sortBy);
        model.addAttribute("comments", allComment);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", commentService.getAllComments().size()/pageSize);
        return "admin/comment/index";
    }

    @GetMapping("comment/search")
    public String searchComment(@NotNull Model model, @RequestParam String keyword,
                                @RequestParam(defaultValue = "0") Integer pageNo,
                                @RequestParam(defaultValue = "10") Integer pageSize,
                                @RequestParam(defaultValue = "commentId") String sortBy) {
        model.addAttribute("comments", commentService.searchComment(keyword));
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", commentService.searchComment(keyword).size()/pageSize);
        return "admin/comment/index";
    }

    @GetMapping("/comment/delete/{id}")
    public String deleteCommentForm(@PathVariable("id") Long id){
        commentService.deleteComment(id);
        return "redirect:/admin/comment";
    }

    //------------------INVOICE------------------//
    @Autowired
    private InvoiceService invoiceService;

    @GetMapping("/invoice")
    public String adminInvoice(Model model,
                                   @RequestParam(defaultValue = "0") Integer pageNo,
                                   @RequestParam(defaultValue = "10") Integer pageSize,
                                   @RequestParam(defaultValue = "invoiceId") String sortBy){

        List<Invoice> allInvoice = invoiceService.getAllInvoices(pageNo, pageSize, sortBy);
        model.addAttribute("invoices", allInvoice);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", invoiceService.getAllInvoices().size()/pageSize);

        return "admin/invoice/index";
    }

    @GetMapping("/invoice/search")
    public String searchInvoice(@NotNull Model model, @RequestParam String keyword,
                                    @RequestParam(defaultValue = "0") Integer pageNo,
                                    @RequestParam(defaultValue = "10") Integer pageSize,
                                    @RequestParam(defaultValue = "invoiceId") String sortBy) {
        model.addAttribute("invoices", invoiceService.searchInvoice(keyword));
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", invoiceService.searchInvoice(keyword).size()/pageSize);
        return "admin/invoice/index";
    }

    @GetMapping("/invoice/add")
    public String addInvoiceForm(Model model){
        model.addAttribute("invoice", new Invoice());
        model.addAttribute("users", userService.getAllUser());
        return "admin/invoice/add";
    }

    @PostMapping("/invoice/add")
    public String addInvoice(@Valid @ModelAttribute("invoice") Invoice invoice,
                                 BindingResult bindingResult,
                                 Model model,
                                 RedirectAttributes redirectAsttributes){
        String viewName = "admin/invoice/add";
        if (bindingResult.hasErrors()){
            model.addAttribute("invoice",invoice);
            model.addAttribute("users", userService.getAllUser());
            return "admin/invoice/add";
        }
        invoiceService.createInvoice(invoice);
        return "redirect:/admin/invoice";
    }

    @GetMapping("/invoice/edit/{id}")
    public String editInvoiceForm(@PathVariable("id") Long id, Model model){
        model.addAttribute("invoice",invoiceService.getInvoiceById(id));
        model.addAttribute("users", userService.getAllUser());
        return "admin/invoice/edit";
    }

    @PostMapping("/invoice/edit")
    public String editInvoice(@Valid @ModelAttribute("invoice") Invoice updateInvoice){
        invoiceService.createInvoice(updateInvoice);
        //Sau khi add xong thi chuyen ve màn hình danh sách list
        return "redirect:/admin/invoice";
    }

    @GetMapping("/invoice/delete/{id}")
    public String deleteInvoice(@PathVariable("id") Long id){
        invoiceService.deleteInvoice(id);
        return "redirect:/admin/invoice";
    }

    //--------------ranking--------------//

    @Autowired
    private RankingService rankingService;

    @GetMapping("/ranking")
    public String adminRanking(){
        return "admin/ranking/index";
    }
    @GetMapping("/ranking/add")
    public String addRanking(){
        return "admin/ranking/add";
    }

    @GetMapping("/ranking/edit/{id}")
    public String editRankForm(@PathVariable("id") Long id, Model model){
        model.addAttribute("ranking",rankingService.getRankingById(id));
        return "admin/ranking/edit";
    }

}