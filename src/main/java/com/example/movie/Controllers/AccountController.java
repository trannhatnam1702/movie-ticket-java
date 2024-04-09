package com.example.movie.Controllers;

import com.example.movie.Entity.Reservation;
import com.example.movie.Entity.User;
import com.example.movie.Repository.ReservationRepo;
import com.example.movie.Repository.UserRepo;
import com.example.movie.Service.SendMailService;
import com.example.movie.Service.UserService;
import com.example.movie.dto.PurchasedDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import jakarta.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Controller
@RequestMapping("/account")
public class AccountController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private SendMailService sendMailService;

    @Autowired
    private ReservationRepo reservationRepo;

    public static String generateRandomString(String characters, int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);
            sb.append(randomChar);
        }

        return sb.toString();
    }



    @GetMapping
    public String profile(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        model.addAttribute("username", username);

        User user = userRepo.findByUsername(username);
        model.addAttribute("user", user);

        model.addAttribute("UserID",user.getUserId());


        String[] reservation = reservationRepo.getReservationByUserId(user.getUserId());
        List<PurchasedDto> purchasedDtoList = new ArrayList<>();

        for(int i = 0; i < reservation.length; i++) {
            PurchasedDto purchasedDto = new PurchasedDto();
            String[] resultArray = reservation[i].split(",");
            purchasedDto.setInvoiceId(resultArray[0]);
            purchasedDto.setCreatedAt(resultArray[1]);
            purchasedDto.setMovie(resultArray[2]);
            purchasedDto.setTime(resultArray[3]);
            purchasedDto.setDay(resultArray[4]);
            purchasedDto.setSeat(resultArray[5]);
            purchasedDto.setTotal(resultArray[6]);
            purchasedDtoList.add(purchasedDto);
        }
        System.out.println(purchasedDtoList);
        model.addAttribute("reservations", purchasedDtoList);
        return "account/profile";
    }


    @GetMapping("/resetpassword")
    public String resetpassword(){
        return "account/resetpassword";
    }

    @PostMapping("/resetpassword")
    public String resetpassword(@ModelAttribute("username") String username){

        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        int length = 7;
        String newPassword = generateRandomString(characters, length);
        User user = userRepo.findByUsername(username);
        sendMailService.resetPassword(user, newPassword);

        user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
        userService.addSUser(user);

        return "account/login";
    }

    @PostMapping
    public String UpdateProfile(@Valid @ModelAttribute("user") User updateUser){

        userService.updateUser(updateUser);

        return "account/profile";
    }

    @PostMapping("/changepassword")
    public String ChangePassword(@Valid @ModelAttribute("user") User updateUser){
        updateUser.setPassword(new BCryptPasswordEncoder().encode(updateUser.getPassword()));
        userService.updateUser(updateUser);

        return "account/profile";
    }




    @GetMapping("/login")
    public String login(){
        return "account/login";
    }

    @GetMapping("/register")
    public String register(Model model){
        //Tạo đối tượng user gắn vào biến có tên user
        model.addAttribute("user", new User());
        //Trả về màn hình gia diện register
        return "account/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") User user,
                           BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                model.addAttribute(error.getField() + "_error",
                        error.getDefaultMessage());
            }
            return "account/register";
        }
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userService.save(user);
        return "redirect:/account/login";
    }
}
