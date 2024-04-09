package com.example.movie;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.example.movie.Controllers.AdminController;
import com.example.movie.Controllers.ApiController;
import com.example.movie.Entity.Cinema;
import com.example.movie.Service.CinemaService;
import com.example.movie.Service.MovieService;
import com.example.movie.dto.CinemaDto;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.Optional;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class UnitTest {

    private static ExtentReports extent;
    private static ExtentTest report;
    @Mock
    private MovieService movieService;
    @InjectMocks
    private AdminController adminController;



    @BeforeAll
    public void setUp() {
        extent = new ExtentReports();
        ExtentSparkReporter htmlReporter = new ExtentSparkReporter("test-report.html");
        extent.attachReporter(htmlReporter);
    }

    @AfterAll
    public void tearDown() {
        extent.flush();
    }

    @Test
    public void testAddCinema_Success() {
        report = extent.createTest("Test Add Cinema - Success");

        Cinema cinemaToAdd = new Cinema();
        cinemaToAdd.setCinemaName("Rạp A");
        cinemaToAdd.setDescription("Rạp chiếu phim hiện đại");

        Cinema savedCinema = new Cinema();
        savedCinema.setCinemaId(1L);
        savedCinema.setCinemaName("Rạp A");
        savedCinema.setDescription("Rạp chiếu phim hiện đại");

        CinemaService cinemaService = mock(CinemaService.class);
        when(cinemaService.addCinema(cinemaToAdd)).thenReturn(savedCinema);

        ApiController cinemaController = new ApiController(cinemaService);
        CinemaDto cinemaDto = cinemaController.addCinema(cinemaToAdd);

        assertNotNull(cinemaDto);
        assertEquals(savedCinema.getCinemaId(), cinemaDto.getId());
        assertEquals(savedCinema.getCinemaName(), cinemaDto.getCinemaName());
        assertEquals(savedCinema.getDescription(), cinemaDto.getDescription());

        if (cinemaDto != null) {
            report.log(Status.PASS, "Test passed successfully");
        } else {
            report.log(Status.FAIL, "Test failed");
        }
    }

    @Test
    public void testAddCinema_InternalServerError() {
        report = extent.createTest("Test Add Cinema - Internal Server Error");

        CinemaService cinemaService = mock(CinemaService.class);
        when(cinemaService.addCinema(any())).thenThrow(new RuntimeException());

        ApiController cinemaController = new ApiController(cinemaService);

        Cinema cinemaToAdd = new Cinema();
        cinemaToAdd.setCinemaName("Rạp A");
        cinemaToAdd.setDescription("Rạp chiếu phim hiện đại");
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            cinemaController.addCinema(cinemaToAdd);
        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
        assertEquals("Error occurred while adding cinema", exception.getReason());

        report.log(Status.INFO, "Test Add Cinema - Internal Server Error");
        if (exception.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
            report.log(Status.FAIL, "Test failed");
        } else {
            report.log(Status.PASS, "Test passed successfully");
        }
        extent.flush();
    }

    @Test
    public void testAddCinema_NullInput() {
        report = extent.createTest("Test Add Cinema - Null Input");
        CinemaService cinemaService = mock(CinemaService.class);
        ApiController cinemaController = new ApiController(cinemaService);

        CinemaDto cinemaDto = cinemaController.addCinema(null);

        verify(cinemaService, never()).addCinema(any(Cinema.class));

        assertNull(cinemaDto);
        if (cinemaDto != null) {
            report.log(Status.FAIL, "Test failed");
        } else {
            report.log(Status.PASS, "Test passed successfully");
        }
    }

    @Test
    public void testSearchMovie_Success() {
        report = extent.createTest("Test Search Movie - Success");
        String keyword = "action";
        Model model = mock(Model.class);
        when(movieService.searchMovie(keyword)).thenReturn(Collections.emptyList());

        String viewName = adminController.searchMovie(model, keyword, 0, 10, "id");

        verify(model).addAttribute("movies", Collections.emptyList());
        verify(model).addAttribute("currentPage", 0);
        verify(model).addAttribute("totalPages", 0);
        assertEquals("admin/movie/index", viewName);

        report.log(Status.PASS, "Test passed successfully");
    }

    @Test
    public void testSearchMovie_Pagination() {
        report = extent.createTest("Test Search Movie - Pagination");
        String keyword = "comedy";
        Model model = mock(Model.class);
        when(movieService.searchMovie(keyword)).thenReturn(Collections.emptyList());

        String viewName = adminController.searchMovie(model, keyword, 1, 10, "name");

        verify(model).addAttribute("movies", Collections.emptyList());
        verify(model).addAttribute("currentPage", 1);
        verify(model).addAttribute("totalPages", 0);
        assertEquals("admin/movie/index", viewName);

        report.log(Status.PASS, "Test passed successfully");
    }

    @Test
    public void testSearchMovie_NoResults() {
        report = extent.createTest("Test Search Movie - No Results");
        String keyword = "sci-fi";
        Model model = mock(Model.class);
        when(movieService.searchMovie(keyword)).thenReturn(Collections.emptyList());

        String viewName = adminController.searchMovie(model, keyword, 0, 10, "rating");

        verify(model).addAttribute("movies", Collections.emptyList());
        verify(model).addAttribute("currentPage", 0);
        verify(model).addAttribute("totalPages", 0);
        assertEquals("admin/movie/index", viewName);

        report.log(Status.PASS, "Test passed successfully");
    }

}
