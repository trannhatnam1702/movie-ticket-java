package com.example.movie.Controllers;

import com.example.movie.Entity.*;
import com.example.movie.Repository.RoleRepo;
import com.example.movie.Service.*;
import com.example.movie.dto.*;
import com.example.movie.utils.UploadImage;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1")
public class ApiController {

    //------------------------DASHBOARD--------------------------
    @Autowired
    private InvoiceService invoiceService;
    @GetMapping("/revenue/{year}")
    public List<Object[]> getRevenueByYear(@PathVariable String year) {
        return invoiceService.getRevenueByYear( Integer.parseInt(year));
    }


    //------------------------CINEMA--------------------------
    @Autowired
    private CinemaService cinemaService;

    private CinemaDto convertToCinemaDto(Cinema cinema) {
        if (cinema == null) {
            return null;
        }
        CinemaDto cinemaDto = new CinemaDto();
        cinemaDto.setId(cinema.getCinemaId());
        cinemaDto.setCinemaName(cinema.getCinemaName());
        cinemaDto.setDescription(cinema.getDescription());
        return cinemaDto;
    }

    @GetMapping("/cinemas")
    @ResponseBody
    public List<CinemaDto> getAllCinemas() {
        List<Cinema> cinemas = cinemaService.getAllCinema();
        List<CinemaDto> cinemaDtos = new ArrayList<>();
        for (Cinema cinema : cinemas) {
            cinemaDtos.add(convertToCinemaDto(cinema));
        }

        return cinemaDtos;
    }

    @GetMapping("/cinemas/{id}")
    @ResponseBody
    public CinemaDto getCinemaById(@PathVariable Long id) {
        Cinema cinema = cinemaService.getCinemaById(id);
        return convertToCinemaDto(cinema);
    }

    @DeleteMapping("/cinemas/{id}")
    @Transactional
    public ResponseEntity<String> deleteCinema(@PathVariable Long id) {
        Cinema cinema = cinemaService.getCinemaById(id);
        if (cinema == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid cinema ID");
        }
        try {
            cinemaService.deleteCinema(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Cinema deleted successfully");
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred while deleting cinema");
        }
    }

    @PostMapping("/cinemas")
    public CinemaDto addCinema(@RequestBody Cinema cinema) {
        try {
            cinema = cinemaService.addCinema(cinema);
            return convertToCinemaDto(cinema);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred while adding cinema", e);
        }
    }

    @PutMapping("/cinemas/{id}")
    public CinemaDto updateCinema(@RequestBody Cinema cinema, @PathVariable Long id) {
        Cinema updateCinema = cinemaService.getCinemaById(id);
        if (updateCinema == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid cinema ID");
        }
        updateCinema.setCinemaName(cinema.getCinemaName());
        updateCinema.setDescription(cinema.getDescription());
        updateCinema = cinemaService.addCinema(updateCinema);
        return convertToCinemaDto(updateCinema);
    }

    //------------------------SHOW DAY--------------------------

    @Autowired
    private ShowDayService showDayService;

    private ShowDayDto convertToShowDayDto(ShowDay showDay) {
        ShowDayDto showDayDto = new ShowDayDto();
        showDayDto.setId(showDay.getShowDayId());
        showDayDto.setDay(showDay.getDay());
        return showDayDto;
    }

    @GetMapping("/showday")
    @ResponseBody
    public List<ShowDayDto> getAllShowDay() {
        List<ShowDay> showDays = showDayService.getAllShowDay();
        List<ShowDayDto> showDayDtos = new ArrayList<>();
        for (ShowDay showDay : showDays) {
            showDayDtos.add(convertToShowDayDto(showDay));
        }
        return showDayDtos;
    }

    @GetMapping("/showday/{id}")
    @ResponseBody
    public ShowDayDto getShowDayById(@PathVariable Long id) {
        ShowDay showDay = showDayService.getShowDayById(id);
        return convertToShowDayDto(showDay);
    }

    @DeleteMapping("/showday/{id}")
    @Transactional
    public void deleteShowDay(@PathVariable Long id) {
        if(showDayService.getShowDayById(id) != null);
        showDayService.deleteShowDay(id);
    }

    @PostMapping("/showday")
    public ShowDayDto addShowDay(@RequestBody ShowDay showDay) {
        showDay = showDayService.addShowDay(showDay);
        return convertToShowDayDto(showDay);
    }

    @PutMapping("/showday/{id}")
    public ShowDayDto updateShowDay(@RequestBody ShowDay showDay, @PathVariable Long id) {
        ShowDay updateShowDay = showDayService.getShowDayById(id);
        updateShowDay.setDay(showDay.getDay());
        updateShowDay = showDayService.addShowDay(updateShowDay);
        return convertToShowDayDto(updateShowDay);
    }

    //------------------------SHOW TIME--------------------------

    @Autowired
    private ShowTimeService showTimeService;

    private ShowTimeDto convertToShowTimeDto(ShowTime showTime) {
        ShowTimeDto showTimeDto = new ShowTimeDto();
        showTimeDto.setId(showTime.getShowTimeId());
        showTimeDto.setTime(showTime.getTime());
        return showTimeDto;
    }

    @GetMapping("/showtime")
    @ResponseBody
    public List<ShowTimeDto> getAllShowTime() {
        List<ShowTime> showTimes = showTimeService.getAllShowTime();
        List<ShowTimeDto> showTimeDtos = new ArrayList<>();
        for (ShowTime showTime : showTimes) {
            showTimeDtos.add(convertToShowTimeDto(showTime));
        }
        return showTimeDtos;
    }

    @GetMapping("/showtime/{id}")
    @ResponseBody
    public ShowTimeDto getShowTimeById(@PathVariable Long id) {
        ShowTime showTime = showTimeService.getShowTimeById(id);
        return convertToShowTimeDto(showTime);
    }

    @DeleteMapping("/showtime/{id}")
    @Transactional
    public void deleteShowTime(@PathVariable Long id) {
        if(showTimeService.getShowTimeById(id) != null);
        showTimeService.deleteShowTime(id);
    }

    @PostMapping("/showtime")
    public ShowTimeDto addTimeDay(@RequestBody ShowTime showTime) {
        showTime = showTimeService.addShowTime(showTime);
        return convertToShowTimeDto(showTime);
    }

    @PutMapping("/showtime/{id}")
    public ShowTimeDto updateShowTime(@RequestBody ShowTime showTime, @PathVariable Long id) {
        ShowTime updateShowTime = showTimeService.getShowTimeById(id);
        updateShowTime.setTime(showTime.getTime());
        updateShowTime = showTimeService.addShowTime(updateShowTime);
        return convertToShowTimeDto(updateShowTime);
    }

    //------------------------SEAT--------------------------

    @Autowired
    private SeatService seatService;

    private SeatDto convertToSeatDto(Seat seat) {
        SeatDto seatDto = new SeatDto();
        seatDto.setId(seat.getSeatId());
        seatDto.setSeatNo(seat.getSeatNo());
        seatDto.setStatus(seat.getStatus());
        seatDto.setPrice(seat.getPrice());
        return seatDto;
    }

    @GetMapping("/seat")
    @ResponseBody
    public List<SeatDto> getAllSeat() {
        List<Seat> seats = seatService.getAllSeat();
        List<SeatDto> seatDtos = new ArrayList<>();
        for (Seat seat : seats) {
            seatDtos.add(convertToSeatDto(seat));
        }
        return seatDtos;
    }

//    @GetMapping("/ajax-seat")
//    @ResponseBody
//    public List<SeatDto> getAllSeat(int page, int pageSize) {
//        pageSize = 10;
//        List<Seat> seats = seatService.getAllSeat(page, pageSize);
//        List<SeatDto> seatDtos = new ArrayList<>();
//        for (Seat seat : seats) {
//            seatDtos.add(convertToSeatDto(seat));
//        }
//        int totalPages = seatService.getAllSeat().size() / pageSize;
//
//        Object[] data = {
//                "totalPages": totalPages,
//                "seatDtos": seatDtos
//        };
//
//        System.out.println(data);
//
//        return seatDtos;
//    }

    @GetMapping("/seat/{id}")
    @ResponseBody
    public SeatDto getSeatById(@PathVariable Long id) {
        Seat seat = seatService.getSeatById(id);
        return convertToSeatDto(seat);
    }

    @DeleteMapping("/seat/{id}")
    @Transactional
    public void deleteSeat(@PathVariable Long id) {
        if(seatService.getSeatBySeatId(id) != null);
        seatService.deleteSeat(id);
    }

    @PostMapping("/seat")
    public SeatDto addSeat(@RequestBody Seat seat) {
        seat = seatService.addSeat(seat);
        return convertToSeatDto(seat);
    }

    @PutMapping("/seat/{id}")
    public SeatDto updateSeat(@RequestBody Seat seat, @PathVariable Long id) {
        Seat updateSeat = seatService.getSeatById(id);
        updateSeat.setSeatNo(seat.getSeatNo());
        updateSeat.setStatus(seat.getStatus());
        updateSeat.setPrice(seat.getPrice());
        updateSeat = seatService.addSeat(updateSeat);
        return convertToSeatDto(updateSeat);
    }

    //------------------------USER--------------------------

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepo roleRepo;

    private UserDto convertToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getUserId());
        userDto.setEmail(user.getEmail());
        userDto.setFullName(user.getFullName());
        userDto.setPassword(user.getPassword());
        userDto.setUsername(user.getUsername());
        userDto.setRoleName(user.getRoleNames());
        userDto.setRankName(rankingService.getRankingById(user.getRanking().getRankingId()).getRankName());
        userDto.setRewardPoints(String.valueOf(user.getRewardPoints()));
        return userDto;
    }

    @GetMapping("/account")
    @ResponseBody
    public List<UserDto> getAllUser() {
        List<User> users = userService.getAllUser();
        List<UserDto> userDtos = new ArrayList<>();
        for (User user : users) {
            userDtos.add(convertToUserDto(user));
        }
        return userDtos;
    }

    @GetMapping("/account/{id}")
    @ResponseBody
    public UserDto getAccountById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return convertToUserDto(user);
    }

    @DeleteMapping("/account/{id}")
    @Transactional
    public void deleteAccount(@PathVariable Long id) {
        if(userService.getUserById(id) != null);
        userService.deleteUser(id);
    }

    @PostMapping("/account")
    public UserDto addAccount(@RequestBody User user) {
        user = userService.addSUser(user);
        return convertToUserDto(user);
    }

    @PutMapping("/account/{id}")
    public UserDto updateAccount(@RequestBody User user, @PathVariable Long id) {
        User updateAccount = userService.getUserById(id);
        updateAccount.setEmail(user.getEmail());
        updateAccount.setPassword(user.getPassword());
        updateAccount.setFullName(user.getFullName());
        updateAccount.setUsername(user.getUsername());
        updateAccount.setRoles(user.getRoles());
        updateAccount = userService.addSUser(updateAccount);
        return convertToUserDto(updateAccount);
    }
//------------------------SHOWS--------------------------

    @Autowired
    private ShowService showService;

    private ShowDto convertToShowDto(Shows shows) {
        ShowDto showDto = new ShowDto();
        showDto.setId(shows.getShowId());
        showDto.setCinema(cinemaService.getCinemaById(shows.getCinema().getCinemaId()).getCinemaName());
        showDto.setMovie(movieService.getMovieById(shows.getMovie().getMovieId()).getMovieName());
        showDto.setShowDay(showDayService.getShowDayById(shows.getShowDay().getShowDayId()).getDay());
        showDto.setShowTime(showTimeService.getShowTimeById(shows.getShowTime().getShowTimeId()).getTime());
        return showDto;
    }

    @GetMapping("/shows")
    @ResponseBody
    public List<ShowDto> getAllShows() {
        List<Shows> shows = showService.getAllShows();
        List<ShowDto> showDtos = new ArrayList<>();
        for (Shows showss : shows) {
            showDtos.add(convertToShowDto(showss));
        }
        return showDtos;
    }

    @GetMapping("/shows/{id}")
    @ResponseBody
    public ShowDto getShowsById(@PathVariable Long id) {
        Shows shows = showService.getShowsById(id);
        return convertToShowDto(shows);
    }

    @DeleteMapping("/shows/{id}")
    @Transactional
    public void deleteShows(@PathVariable Long id) {
        if(showService.getShowById(id) != null);
        showService.deleteShows(id);
    }

    @PostMapping("/shows")
    public boolean addShows(@RequestBody ShowDto shows) {

        Shows sh = new Shows();
        Movie m = movieService.getMovieById(Long.parseLong(shows.getMovie()));
        sh.setMovie(m);

        Cinema c = cinemaService.getCinemaById(Long.parseLong(shows.getCinema()));
        sh.setCinema(c);

        ShowTime st = showTimeService.getShowTimeById(Long.parseLong(shows.getShowTime()));
        sh.setShowTime(st);

        ShowDay sd = showDayService.getShowDayById(Long.parseLong(shows.getShowDay()));
        sh.setShowDay(sd);

        sh = showService.addShows(sh);
        return true;
    }

    @PutMapping("/shows/{id}")
    public Boolean updateShows(@RequestBody ShowDto shows, @PathVariable Long id) {
        Shows updateShow = showService.getShowById(id);

        Movie m = movieService.getMovieById(Long.parseLong(shows.getMovie()));
        updateShow.setMovie(m);

        Cinema c = cinemaService.getCinemaById(Long.parseLong(shows.getCinema()));
        updateShow.setCinema(c);

        ShowTime st = showTimeService.getShowTimeById(Long.parseLong(shows.getShowTime()));
        updateShow.setShowTime(st);

        ShowDay sd = showDayService.getShowDayById(Long.parseLong(shows.getShowDay()));
        updateShow.setShowDay(sd);

        updateShow = showService.addShows(updateShow);
        return true;
    }

    //------------------------COMMENT--------------------------

    @Autowired
    private CommentService commentService;

    private CommentDto convertToCommentDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getCommentId());
        commentDto.setContent(comment.getContent());
        commentDto.setMovie(movieService.getMovieById(comment.getMovie().getMovieId()).getMovieName());
        commentDto.setUser(userService.getUserById(comment.getUser().getUserId()).getUsername());
        return commentDto;
    }

    @GetMapping("/comment")
    @ResponseBody
    public List<CommentDto> getAllComment() {
        List<Comment> comments = commentService.getAllComments();
        List<CommentDto> commentDtos = new ArrayList<>();
        for (Comment comment : comments) {
            commentDtos.add(convertToCommentDto(comment));
        }
        return commentDtos;
    }

    @DeleteMapping("/comment/{id}")
    @Transactional
    public void deleteComment(@PathVariable Long id) {
        if(commentService.getCommentById(id) != null);
        commentService.deleteComment(id);
    }

    //------------------------CELEBRITY--------------------------

    //public static String UPLOAD_DIRECTORY = "F:/study/web java/movie(4)/movie/src/main/resources/static/assets/img";
    public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/src/main/resources/static/assets/img";
    @Autowired
    private CelebrityService celebrityService;

    private CelebrityDto convertToCelebrityDto(Celebrity celebrity) {
        CelebrityDto celebrityDto = new CelebrityDto();
        celebrityDto.setId(celebrity.getCelebrityId());
        celebrityDto.setName(celebrity.getName());
        celebrityDto.setHeight(celebrity.getHeight());
        celebrityDto.setWeight(celebrity.getWeight());
        celebrityDto.setUrlAvatar(celebrity.getUrlAvatar());
        celebrityDto.setDescription(celebrity.getDescription());
        celebrityDto.setLanguage(celebrity.getLanguage());
        return celebrityDto;
    }

    @GetMapping("/celebrity")
    @ResponseBody
    public List<CelebrityDto> getAllCelebrity() {
        List<Celebrity> celebrities = celebrityService.getAllCelebrity();
        List<CelebrityDto> celebrityDtos = new ArrayList<>();
        for (Celebrity celebrity : celebrities) {
            celebrityDtos.add(convertToCelebrityDto(celebrity));
        }

        return celebrityDtos;
    }

    @GetMapping("/celebrity/{id}")
    @ResponseBody
    public CelebrityDto getCelebrityById(@PathVariable Long id) {
        Celebrity celebrity = celebrityService.getCelebrityById(id);
        return convertToCelebrityDto(celebrity);
    }

    @DeleteMapping("/celebrity/{id}")
    @Transactional
    public void deleteCelebrity(@PathVariable Long id) {
        if(celebrityService.getCelebrityById(id) != null);
        celebrityService.deleteCelebrity(id);
    }

    @PostMapping("/celebrity")
    public CelebrityDto addCelebrity(@ModelAttribute Celebrity celebrity,
                                     @RequestParam("image") MultipartFile file,
                                     RedirectAttributes redirectAttributes) throws IOException {
        StringBuilder fileNames = new StringBuilder();
        byte[] bytes = file.getBytes();
        Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY , file.getOriginalFilename());
        System.out.println(fileNameAndPath.toAbsolutePath());
        fileNames.append(file.getOriginalFilename());
        Files.write(fileNameAndPath, bytes);
        celebrity.setUrlAvatar(file.getOriginalFilename());
        celebrity = celebrityService.addcelebrity(celebrity);
        return convertToCelebrityDto(celebrity);
    }

    @PutMapping("/celebrity/{id}")
    public CelebrityDto updateCelebrity(@ModelAttribute Celebrity celebrity,
                                        @RequestParam(name = "image", required = false) MultipartFile file,
                                        @PathVariable Long id) throws IOException {
        Celebrity updateCelebrity = celebrityService.getCelebrityById(id);

        if (file != null && !file.isEmpty()) {
            StringBuilder fileNames = new StringBuilder();
            Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, file.getOriginalFilename());
            fileNames.append(file.getOriginalFilename());
            byte[] fileBytes = file.getBytes();
            Files.write(fileNameAndPath, fileBytes);
            updateCelebrity.setUrlAvatar(file.getOriginalFilename());
        }

        updateCelebrity.setName(celebrity.getName());
        updateCelebrity.setHeight(celebrity.getHeight());
        updateCelebrity.setWeight(celebrity.getWeight());
        updateCelebrity.setDescription(celebrity.getDescription());
        updateCelebrity.setLanguage(celebrity.getLanguage());

        updateCelebrity = celebrityService.addcelebrity(updateCelebrity);
        return convertToCelebrityDto(updateCelebrity);
    }

        //------------------------MOVIE--------------------------
    @Autowired
    private MovieService movieService;

    //------------------------RESERVATION--------------------------
    @Autowired
    private ReservationService reservationService;

    private ReservationDto convertToReservationDto (Reservation reservation) {
        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setId(reservation.getReservationId());
        reservationDto.setUser(userService.getUserById(reservation.getInvoice().getUser().getUserId()).getUsername());
        reservationDto.setMovie(movieService.getMovieById(reservation.getShows().getMovie().getMovieId()).getMovieName());
        reservationDto.setSeat(String.valueOf(seatService.getSeatById(reservation.getSeat().getSeatId()).getSeatNo()));
        reservationDto.setDay(showDayService.getShowDayById(reservation.getShows().getShowDay().getShowDayId()).getDay());
        reservationDto.setTime(showTimeService.getShowTimeById(reservation.getShows().getShowTime().getShowTimeId()).getTime());
        reservationDto.setCinema(cinemaService.getCinemaById(reservation.getShows().getCinema().getCinemaId()).getCinemaName());
        reservationDto.setInvoice(String.valueOf(invoiceService.getInvoiceById(reservation.getInvoice().getInvoiceId()).getInvoiceId()));
        return reservationDto;
    }

    private ReservationDto convertToReservationDtoV2 (Reservation reservation) {
        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setId(reservation.getReservationId());
        reservationDto.setShows(String.valueOf(showService.getShowsById(reservation.getShows().getShowId()).getShowId()));
        reservationDto.setSeat(String.valueOf(seatService.getSeatById(reservation.getSeat().getSeatId()).getSeatNo()));
        reservationDto.setInvoice(String.valueOf(invoiceService.getInvoiceById(reservation.getInvoice().getInvoiceId()).getInvoiceId()));
        return reservationDto;
    }

    @GetMapping("/reservation")
    @ResponseBody
    public List<ReservationDto> getAllReservation() {
        List<Reservation> reservations = reservationService.getAllReservations();
        List<ReservationDto> reservationDtos = new ArrayList<>();
        for (Reservation reservation : reservations) {
            reservationDtos.add(convertToReservationDto(reservation));
        }
        return reservationDtos;
    }

    @GetMapping("/reservation/{id}")
    @ResponseBody
    public ReservationDto getReservationById(@PathVariable Long id) {
        Reservation reservation = reservationService.getReservationById(id);
        return convertToReservationDto(reservation);
    }

    @DeleteMapping("/reservation/{id}")
    @Transactional
    public void deleteReservation(@PathVariable Long id) {
        if(reservationService.getReservationById(id) != null);
        reservationService.deleteReservation(id);
    }

    @PostMapping("/reservation")
    public boolean addReservation(@RequestBody ReservationDto reservationDto) {
        Reservation reservation = new Reservation();
        reservation.setShows(showService.getShowsById(Long.valueOf(reservationDto.getShows())));
        reservation.setSeat(seatService.getSeatById(Long.valueOf(reservationDto.getSeat())));
        reservation.setInvoice(invoiceService.getInvoiceById(Long.valueOf(reservationDto.getInvoice())));
        reservation = reservationService.addReservation(reservation);
        return true;
    }

    @PutMapping("/reservation/{id}")
    public Boolean updateReservation(@RequestBody ReservationDto reservationDto, @PathVariable Long id) {
        Reservation updateReservation = reservationService.getReservationById(id);
        updateReservation.setSeat(seatService.getSeatById(Long.valueOf(reservationDto.getSeat())));
        updateReservation.setShows(showService.getShowsById(Long.valueOf(reservationDto.getShows())));
        updateReservation.setInvoice(invoiceService.getInvoiceById(Long.valueOf(reservationDto.getInvoice())));
        updateReservation = reservationService.addReservation(updateReservation);
        return true;
    }

    //------------------------INVOICE--------------------------


    private InvoiceDto convertToInvoiceDto(Invoice invoice) {
        InvoiceDto invoiceDto = new InvoiceDto();
        invoiceDto.setId(invoice.getInvoiceId());
        invoiceDto.setUser(userService.getUserById(invoice.getUser().getUserId()).getUsername());
        invoiceDto.setCreatedAt(invoice.getCreatedAt());
        invoiceDto.setQuantity(invoice.getQuantity());
        invoiceDto.setTotal(invoice.getTotal());
//        invoiceDto.getMovieName();
//        invoiceDto.setTotal(invoice.getTotal());
        return invoiceDto;
    }

    @GetMapping("/invoice")
    @ResponseBody
    public List<InvoiceDto> getAllInvoice() {
        List<Invoice> invoices = invoiceService.getAllInvoices();
        List<InvoiceDto> invoiceDtos = new ArrayList<>();
        for (Invoice invoice : invoices) {
            invoiceDtos.add(convertToInvoiceDto(invoice));
        }
        return invoiceDtos;
    }

    @GetMapping("/invoice/{id}")
    @ResponseBody
    public InvoiceDto getInvoiceById(@PathVariable Long id) {
        Invoice invoice = invoiceService.getInvoiceById(id);
        return convertToInvoiceDto(invoice);
    }

    @DeleteMapping("/invoice/{id}")
    @Transactional
    public void deleteInvoice(@PathVariable Long id) {
        if(invoiceService.getInvoiceById(id) != null);
        invoiceService.deleteInvoice(id);
    }

    @PostMapping("/invoice")
    public boolean addinvoice(@RequestBody InvoiceDto invoiceDto) {
        Invoice invoice = new Invoice();
        invoice.setUser(userService.getUserById(Long.valueOf(invoiceDto.getUser())));
        invoice.setCreatedAt(invoiceDto.getCreatedAt());
        invoice.setQuantity(invoiceDto.getQuantity());
        invoice.setTotal(invoiceDto.getTotal());
        invoiceService.createInvoice(invoice);
        return true;
    }

    @PutMapping("/invoice/{id}")
    public InvoiceDto updateInvoice(@RequestBody InvoiceDto invoiceDto, @PathVariable Long id) {
        Invoice updateInvoice = invoiceService.getInvoiceById(id);
        //System.out.println(invoiceDto);
        updateInvoice.setUser(userService.getUserById(Long.valueOf(invoiceDto.getUser())));
        updateInvoice.setCreatedAt(invoiceDto.getCreatedAt());
        updateInvoice.setQuantity(invoiceDto.getQuantity());
        updateInvoice.setTotal(invoiceDto.getTotal());
        invoiceService.createInvoice(updateInvoice);
        return convertToInvoiceDto(updateInvoice);
    }


    //---------------------RANKING-----------------

    @Autowired
    private RankingService rankingService;

    private RankingDto convertToRankingDto(Ranking ranking) {
        RankingDto rankingDto = new RankingDto();
        rankingDto.setId(ranking.getRankingId());
        rankingDto.setRankName(ranking.getRankName());
        rankingDto.setDiscountPercentage(String.valueOf(ranking.getDiscountPercentage()));
        rankingDto.setPoints(String.valueOf(ranking.getPoints()));
        return rankingDto;
    }

    @GetMapping("/ranking")
    @ResponseBody
    public List<RankingDto> getAllRanking() {
        List<Ranking> rankings = rankingService.getAllRanking();
        List<RankingDto> rankingDtos = new ArrayList<>();
        for (Ranking ranking : rankings) {
            rankingDtos.add(convertToRankingDto(ranking));
        }

        return rankingDtos;
    }

    @GetMapping("/ranking/{id}")
    @ResponseBody
    public RankingDto getRankingById(@PathVariable Long id) {
        Ranking ranking = rankingService.getRankingById(id);
        return convertToRankingDto(ranking);
    }

    @DeleteMapping("/ranking/{id}")
    @Transactional
    public void deleteRanking(@PathVariable Long id) {
        if(rankingService.getRankingById(id) != null);
        rankingService.deleteRanking(id);
    }

    @PostMapping("/ranking")
    public RankingDto addRanking(@RequestBody Ranking ranking) {
        ranking = rankingService.addRanking(ranking);
        return convertToRankingDto(ranking);
    }

    @PutMapping("/ranking/{id}")
    public RankingDto updateRanking(@RequestBody Ranking ranking, @PathVariable Long id) {
        Ranking updateRanking = rankingService.getRankingById(id);
        updateRanking.setRankName(ranking.getRankName());
        updateRanking.setDiscountPercentage(ranking.getDiscountPercentage());
        updateRanking.setPoints(ranking.getPoints());
        updateRanking = rankingService.addRanking(updateRanking);
        return convertToRankingDto(updateRanking);
    }


    @GetMapping("/datajax-buyticket")
    public ResponseEntity<String> getDataForBuyTicket(@RequestParam String showId,
                                      @RequestParam String price,
                                      @RequestParam String seat) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            String discountPercentage = userService.getDiscountPercentageByUsername(username);
            String responseData = "ShowId: " + showId + ", Price: " + price + ", Seat: " + seat + discountPercentage;

            return ResponseEntity.ok(discountPercentage);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error processing request: " + e.getMessage());
        }
    }

    public ApiController(CinemaService cinemaService) {
        this.cinemaService = cinemaService;
    }

    public ApiController() {
    }
}
