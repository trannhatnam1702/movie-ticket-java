package com.example.movie.Controllers;

import com.example.movie.Service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seats")
public class SeatsController {

    @Autowired
    private SeatService seatService;

    @PostMapping("/select")
    public ResponseEntity<String> selectSeat(@RequestParam String seatId) {
        try{
            String[] numbersArray = seatId.split("\\s+");

            Long[] longArray = new Long[numbersArray.length];

            for (int i = 0; i < numbersArray.length; i++) {
                try {
                    longArray[i] = Long.parseLong(numbersArray[i]);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }

            // Thực hiện logic giải phóng ghế
            for(int i = 0; i < longArray.length; i++){
                System.out.println(longArray[i]);
                seatService.updateWaitingSeat(longArray[i]);
            }

            return ResponseEntity.ok("Seat selected successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Seat is not available");
        }
    }

    @PostMapping("/release")
    public ResponseEntity<String> releaseSeat(@RequestParam String seatId) {
        try {
            String[] numbersArray = seatId.split("\\s+");

            Long[] longArray = new Long[numbersArray.length];

            for (int i = 0; i < numbersArray.length; i++) {
                try {
                    longArray[i] = Long.parseLong(numbersArray[i]);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }

            // Thực hiện logic giải phóng ghế
            for(int i = 0; i < longArray.length; i++){
                System.out.println(longArray[i]);
                seatService.updateEmptySeat(longArray[i]);
            }

            return ResponseEntity.ok("Seat released successfully");
        } catch (Exception e) {
            e.printStackTrace(); // Log thông báo lỗi
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error: " + e.getMessage());
        }
    }

    @GetMapping("/datajax-seat")
    public ResponseEntity<List<Long>> atajaxSeat() {
        List<Long> waitingSeats = seatService.listWaitingSeat();
        return ResponseEntity.ok(waitingSeats);
    }
}
