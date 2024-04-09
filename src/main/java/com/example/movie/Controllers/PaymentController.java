package com.example.movie.Controllers;

import com.example.movie.Entity.*;
import com.example.movie.Repository.ShowsRepo;
import com.example.movie.Repository.UserRepo;
import com.example.movie.Service.*;
import com.example.movie.config.PaypalPaymentIntent;
import com.example.movie.config.PaypalPaymentMethod;
import com.example.movie.dto.PaymentSession;
import com.example.movie.utils.Utils;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@SpringBootApplication
@RequestMapping("/payment")

public class PaymentController {


    public static final String URL_PAYPAL_SUCCESS = "pay/success";
    public static final String URL_PAYPAL_CANCEL = "pay/cancel";
    private Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private PaypalService paypalService;

    @Autowired
    private SeatService seatService;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private ReservationService reservationService;
    @Autowired
    private ShowService showService;

    @Autowired
    private ShowsRepo showsRepo;
    @Autowired
    private UserRepo UserRepo;
    @Autowired
    private SendMailService sendMailService;
//    @Autowired
//    private UserSer UserRepo;

    @GetMapping
    public String index() {
        return "payment/index";
    }

    @PostMapping("/pay")
    public String pay(HttpServletRequest request, @RequestParam("showId") String showId, @RequestParam("price") String price, @RequestParam("seat") String seat) {
    // ... (phần code hiện tại của bạn)

    double priceB = Double.parseDouble(price);
    String cancelUrl = Utils.getBaseURL(request) + "/" + "payment" + "/" + URL_PAYPAL_CANCEL;
    String successUrl = Utils.getBaseURL(request) + "/" + "payment" + "/" + URL_PAYPAL_SUCCESS;

    try {
        Payment payment = paypalService.createPayment(
                priceB,
                "USD",
                PaypalPaymentMethod.paypal,
                PaypalPaymentIntent.sale,
                "payment description",
                cancelUrl,
                successUrl);

        PaymentSession paymentSession = new PaymentSession();
        paymentSession.setPayment(payment);
        paymentSession.setShowId(showId);
        paymentSession.setSeat(seat);
        request.getSession().setAttribute("paymentSession", paymentSession);

        for (Links links : payment.getLinks()) {
            if (links.getRel().equals("approval_url")) {
                return "redirect:" + links.getHref();
            }
        }
    } catch (PayPalRESTException e) {
        log.error(e.getMessage());
    }
    return "redirect:/";
}


    @GetMapping(URL_PAYPAL_SUCCESS)
    public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId, HttpServletRequest request) {
        try {
            // Lấy thông tin thanh toán từ session
            PaymentSession paymentSession = (PaymentSession) request.getSession().getAttribute("paymentSession");
            if (paymentSession != null) {
                Payment executedPayment = paypalService.executePayment(paymentId, payerId);
                if (executedPayment.getState().equals("approved")) {
                    // Lưu thông tin đặt vé vào cơ sở dữ liệu khi thanh toán thành công
                    saveReservationInfo(paymentSession.getShowId(), paymentSession.getSeat(), executedPayment);
                    // Xóa thông tin thanh toán khỏi session sau khi sử dụng
                    request.getSession().removeAttribute("paymentSession");
                    return "payment/success";
                }
            }
        } catch (PayPalRESTException e) {
            log.error(e.getMessage());
        }
        return "redirect:/";
    }

    private void saveReservationInfo(String showId, String seat, Payment payment) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        User user = UserRepo.findByUsername(name);
        // Lấy thông tin đặt vé từ session
        String input = seat;
        String[] array = input.trim().split("\\s+");

        String quantity = String.valueOf(array.length);

        String discountper = UserRepo.getDiscountPercentageByUsername(name);
        int amount = (array.length * 9) - ((array.length * 9)* 20 /100);
        String total = String.valueOf(amount);

        Reservation reservation;
        //  save invoice

        Invoice invoice = new Invoice();
        invoice.setUser(user);
        invoice.setTotal(Double.parseDouble(total));
        invoice.setQuantity(Integer.parseInt(quantity));
        invoice.setCreatedAt();
        invoiceService.createInvoice(invoice);

        for (int i = 0; i < array.length; i++) {
            reservation = new Reservation();
            Seat s = seatService.getSeatBySeatId(Long.parseLong(array[i]));
            reservation.setSeat(s);
            Shows sh = showService.getShowById(Long.parseLong(showId));
            reservation.setShows(sh);
            reservation.setInvoice(invoice);
            reservationService.addReservation(reservation);
        }

        // update rewardPoints user
        int pointMark = Integer.parseInt(quantity) * 10;
        int rewardPoints = UserRepo.getRewardPoints(name) + pointMark;

        System.out.println(rewardPoints);
        UserRepo.updateRewardPoints(rewardPoints, user.getUserId());


        //send mail
        Shows show = showService.getShowById(Long.parseLong(showId));
        String cinema = showsRepo.findCinemaNameByShowId(Long.parseLong(showId));
        String time = showsRepo.findTimeByShowId(Long.parseLong(showId));
        String day = showsRepo.findDayByShowId(Long.parseLong(showId));
        String movie = showsRepo.findMovieByShowId(Long.parseLong(showId));
        String invoice_id = Long.toString(invoice.getInvoiceId());

        sendMailService.sendEmail(user, cinema, time, day, seat, quantity, total, movie, invoice_id);
    }


//    @GetMapping(URL_PAYPAL_CANCEL)
//    public String cancelPay() {
//        return "payment/cancel";
//    }
//
//    @GetMapping(URL_PAYPAL_SUCCESS)
//    public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
//        try {
//            Payment payment = paypalService.executePayment(paymentId, payerId);
//            if (payment.getState().equals("approved")) {
//                return "payment/success";
//            }
//        } catch (PayPalRESTException e) {
//            log.error(e.getMessage());
//        }
//        return "redirect:/";
//    }
}
