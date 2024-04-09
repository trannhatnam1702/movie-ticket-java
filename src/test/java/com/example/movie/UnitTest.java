package com.example.movie;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.example.movie.Controllers.ApiController;
import com.example.movie.Entity.Cinema;
import com.example.movie.Service.CinemaService;
import com.example.movie.dto.CinemaDto;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class UnitTest {

    private static ExtentReports extent;
    private static ExtentTest report;

    @BeforeAll
    public void setUp() {
        // Khởi tạo ExtentReports và ExtentHtmlReporter
        extent = new ExtentReports();
        ExtentSparkReporter htmlReporter = new ExtentSparkReporter("test-report.html");
        extent.attachReporter(htmlReporter);
    }

    @AfterAll
    public void tearDown() {
        // Đóng báo cáo sau khi hoàn thành tất cả các kiểm thử
        extent.flush();
    }

    @Test
    public void testAddCinema_Success() {
        report = extent.createTest("Test Add Cinema - Success");
        // Arrange
        Cinema cinemaToAdd = new Cinema();
        cinemaToAdd.setCinemaName("Rạp A");
        cinemaToAdd.setDescription("Rạp chiếu phim hiện đại");

        Cinema savedCinema = new Cinema();
        savedCinema.setCinemaId(1L);
        savedCinema.setCinemaName("Rạp A");
        savedCinema.setDescription("Rạp chiếu phim hiện đại");

        CinemaService cinemaService = mock(CinemaService.class);
        when(cinemaService.addCinema(cinemaToAdd)).thenReturn(savedCinema);

        // Act
        ApiController cinemaController = new ApiController(cinemaService);

        // Gọi phương thức addCinema() của CinemaController
        CinemaDto cinemaDto = cinemaController.addCinema(cinemaToAdd);

        // Assert
        assertNotNull(cinemaDto);
        assertEquals(savedCinema.getCinemaId(), cinemaDto.getId());
        assertEquals(savedCinema.getCinemaName(), cinemaDto.getCinemaName());
        assertEquals(savedCinema.getDescription(), cinemaDto.getDescription());

        // Log result to report
        if (cinemaDto != null) {
            report.log(Status.PASS, "Test passed successfully");
        } else {
            report.log(Status.FAIL, "Test failed");
        }
    }

    // Hoàn thiện phần còn lại cho hai trường hợp kiểm thử khác

    @Test
    public void testAddCinema_InternalServerError() {
        report = extent.createTest("Test Add Cinema - Internal Server Error");

        CinemaService cinemaService = mock(CinemaService.class);
        when(cinemaService.addCinema(any())).thenThrow(new RuntimeException());

        ApiController cinemaController = new ApiController(cinemaService);

        Cinema cinemaToAdd = new Cinema();
        cinemaToAdd.setCinemaName("Rạp A");
        cinemaToAdd.setDescription("Rạp chiếu phim hiện đại");

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            cinemaController.addCinema(cinemaToAdd);
        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
        assertEquals("Error occurred while adding cinema", exception.getReason());

        // Log result to report
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
        // Arrange
        CinemaService cinemaService = mock(CinemaService.class);
        ApiController cinemaController = new ApiController(cinemaService);

        // Act
        CinemaDto cinemaDto = cinemaController.addCinema(null);

        // Assert
        // Kiểm tra xem phương thức addCinema() của CinemaService có được gọi không
        verify(cinemaService, never()).addCinema(any(Cinema.class));

        // Kiểm tra xem kết quả trả về có phải là null không
        assertNull(cinemaDto);
        if (cinemaDto != null) {
            report.log(Status.FAIL, "Test failed");
        } else {
            report.log(Status.PASS, "Test passed successfully");
        }
    }



}
