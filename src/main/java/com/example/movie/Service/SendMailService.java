package com.example.movie.Service;

import com.example.movie.Entity.User;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.activation.DataHandler;
import javax.mail.internet.InternetAddress;
import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.io.ByteArrayOutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

@Service
public class SendMailService {
    public void sendEmail(User user, String cinema, String time, String day, String seat, String quantity, String total, String movie, String invoice_id) {
        // Cấu hình thông tin email
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        // Thông tin đăng nhập email
        String senderEmail = "sendcodetw@gmail.com";
        String senderPassword = "adgv dekj hpnm cwde";

        // Tạo một phiên làm việc (session)
        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            // Tạo mã QR code
            String qrCodeData = "Invoice Id: "+ invoice_id + ", Movie: " + movie + ", Cinema: " + cinema + ", Day: " + day + ", Time: " + time + ", Seat: " + seat + ", Quantity: " + quantity + ", Total: " + total;
            byte[] qrCodeImageData = generateQRCodeImageInMemory(qrCodeData, 350, 350); // 350x350 là kích thước hình ảnh QR code

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
            message.setSubject("New Order");

            // Nội dung HTML với hình ảnh QR code
            String htmlContent = "<!DOCTYPE html>"
                    + "<html>"
                    + "<head>"
                    + "<title>Thông tin đơn hàng mới từ khách hàng: " + user.getFullName() +"</title>"
                    + "<meta charset=\"utf-8\" />"
                    + "</head>"
                    + "<body>"
                    + "New order information from customers: " + user.getFullName() +"<br />"
                    + "Invoice Id.: " + invoice_id + "<br />"
                    + "Email: " + user.getEmail() + "<br />"
                    + "MovieName: "  + movie + "<br />"
                    + "Cinema: "  + cinema + "<br />"
                    + "Day: "  + day + ", Time: " + time + "<br />"
                    + "Quantity: "  + quantity + "  Seats: "  + seat + "<br />"
                    + "Total: "  + total +  "<br />"
                    + "<i style=\"font-weight: bold\">\"I hope you have a great time at the movie theater!\"</i>"
                    + "<img src='data:image/png;base64," + Base64.getEncoder().encodeToString(qrCodeImageData) + "'/>"
                    + "</body>"
                    + "</html>";

            // Gửi email
            MimeMultipart multipart = new MimeMultipart();
            // Phần văn bản
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(htmlContent, "utf-8", "html");
            multipart.addBodyPart(textPart);

            // Phần hình ảnh QR code
            MimeBodyPart imagePart = new MimeBodyPart();
            imagePart.setContentID("<qrcode>");
            imagePart.setDisposition(MimeBodyPart.INLINE);
            imagePart.setDataHandler(new DataHandler(new ByteArrayDataSource(qrCodeImageData, "image/png")));
            multipart.addBodyPart(imagePart);

            // Gán phần nội dung cho thư
            message.setContent(multipart);

            // Gửi email
            Transport.send(message);

            System.out.println("Email sent successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private byte[] generateQRCodeImageInMemory(String text, int width, int height) throws Exception {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height, hints);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);

        return outputStream.toByteArray();
    }

    public void resetPassword(User user, String newPassword){
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        // Thông tin đăng nhập email
        String senderEmail = "sendcodetw@gmail.com";
        String senderPassword = "adgv dekj hpnm cwde";

        // Tạo một phiên làm việc (session)
        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            // Tạo đối tượng MimeMessage
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
            message.setSubject("New Order");
            String htmlContent = "<!DOCTYPE html>"
                    + "<html>"
                    + "<head>"
                    + "<title>Reset password</title>"
                    + "<meta charset=\"utf-8\" />"
                    + "</head>"
                    + "<body>"
                    + "Customers: " + user.getFullName() + "<br />"
                    + "Name: " + user.getUsername() + "<br />"
                    + "New password: " + newPassword + "<br />"
                    + "<i style=\"font-weight: bold\">\"Don't share your account with anyone!\"</i>"
                    + "</body>"
                    + "</html>";
            message.setContent(htmlContent, "text/html");
            // Gửi email
            Transport.send(message);

            System.out.println("Email sent successfully!");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}

