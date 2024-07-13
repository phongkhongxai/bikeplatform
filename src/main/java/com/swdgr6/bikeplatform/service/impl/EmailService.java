package com.swdgr6.bikeplatform.service.impl;

import com.swdgr6.bikeplatform.model.exception.BikeApiException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    @Async
    public void sendSuccessWithdrawEmail(String recipientEmail, String recipientName, double amount, String date ) {

        try {
            String subject = "Yêu cầu rút tiền thành công";
            String content = getSuccessEmailContent(recipientName, amount, date );
            sendEmail(recipientEmail, subject, content);
        } catch (MessagingException e) {
            throw new BikeApiException(HttpStatus.BAD_REQUEST, "Send mail error!");
        }
    }

    @Async
    public void sendSuccessTransactionEmail(String recipientEmail, String recipientName,String bp, double amount, String date ) {

        try {
            String subject = "Giao dịch đơn hàng thành công.";
            String content = getSuccessEmailContentTransaction(recipientName, amount,bp, date );
            sendEmail(recipientEmail, subject, content);
        } catch (MessagingException e) {
            throw new BikeApiException(HttpStatus.BAD_REQUEST, "Send mail error!");
        }
    }

    @Async
    public void sendFailureTransactionEmail(String recipientEmail, String recipientName,String bp, double amount,String reason, String date ) {

        try {
            String subject = "Giao dịch đơn hàng thất bại!";
            String content = getFailureEmailContentTransaction(recipientName, amount,bp,reason, date );
            sendEmail(recipientEmail, subject, content);
        } catch (MessagingException e) {
            throw new BikeApiException(HttpStatus.BAD_REQUEST, "Send mail error!");
        }
    }

    @Async
    public void sendFailureWithdrawEmail(String recipientEmail, String recipientName, double amount, String date, String failureReason) {

        try {
            String subject = "Yêu cầu rút tiền thất bại";
            String content = getFailureEmailContent(recipientName, amount, date, failureReason );
            sendEmail(recipientEmail, subject, content);
        } catch (MessagingException e) {
            throw new BikeApiException(HttpStatus.BAD_REQUEST, "Send mail error!");
        }
    }

    private void sendEmail(String recipientEmail, String subject, String content) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(recipientEmail);
        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(message);
    }

    private String getSuccessEmailContent(String recipientName, double amount, String date ) {
        return "<!DOCTYPE html>" +
                "<html lang=\"vi\">" +
                "<head>" +
                "<meta charset=\"UTF-8\">" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "<style>" +
                "body {font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f4f4f4;}" +
                ".container {width: 100%; max-width: 600px; margin: 0 auto; background-color: #ffffff; padding: 20px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);}" +
                ".header {text-align: center; padding: 10px 0;}" +
                ".header img {max-width: 150px;}" +
                ".content {padding: 20px; text-align: left;}" +
                ".footer {text-align: center; padding: 10px 0; font-size: 12px; color: #777;}" +
                ".button {display: inline-block; padding: 10px 20px; margin-top: 20px; background-color: #28a745; color: #ffffff; text-decoration: none; border-radius: 5px;}" +
                "@media (max-width: 600px) {.container {padding: 10px;} .content {padding: 10px;}}" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class=\"container\">" +
                "<div class=\"header\">" +
                "<img src=\"your-logo-url.png\" alt=\"Company Logo\">" +
                "</div>" +
                "<div class=\"content\">" +
                "<h1>Chào " + recipientName + ",</h1>" +
                "<p>Yêu cầu rút tiền của bạn đã được xử lý thành công.</p>" +
                "<p>Số tiền: " + amount + "</p>" +
                "<p>Ngày thực hiện: " + date + "</p>" +
                "<p>Cảm ơn bạn đã sử dụng dịch vụ của chúng tôi.</p>" +
                "</div>" +
                "<div class=\"footer\">" +
                "<p>&copy; " + LocalDate.now().getYear() + " Công ty của bạn. Tất cả các quyền được bảo lưu.</p>" +
                "<p>Địa chỉ công ty bạn, Thành phố, Quốc gia</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }

    private String getSuccessEmailContentTransaction(String recipientName, double amount,String bikepoint, String date ) {
        return "<!DOCTYPE html>" +
                "<html lang=\"vi\">" +
                "<head>" +
                "<meta charset=\"UTF-8\">" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "<style>" +
                "body {font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f4f4f4;}" +
                ".container {width: 100%; max-width: 600px; margin: 0 auto; background-color: #ffffff; padding: 20px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);}" +
                ".header {text-align: center; padding: 10px 0;}" +
                ".header img {max-width: 150px;}" +
                ".content {padding: 20px; text-align: left;}" +
                ".footer {text-align: center; padding: 10px 0; font-size: 12px; color: #777;}" +
                ".button {display: inline-block; padding: 10px 20px; margin-top: 20px; background-color: #28a745; color: #ffffff; text-decoration: none; border-radius: 5px;}" +
                "@media (max-width: 600px) {.container {padding: 10px;} .content {padding: 10px;}}" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class=\"container\">" +
                "<div class=\"header\">" +
                "<img src=\"your-logo-url.png\" alt=\"Company Logo\">" +
                "</div>" +
                "<div class=\"content\">" +
                "<h1>Chào " + recipientName + ",</h1>" +
                "<p>Giao dịch chuyển tiền cho " + bikepoint + " đã hoàn thành.</p>" +
                "<p>Vui lòng kiểm tra lại số dư trong ví của " + bikepoint +".</p>" +
                "<p>Số tiền: " + amount + "</p>" +
                "<p>Ngày thực hiện: " + date + "</p>" +
                "<p>Cảm ơn bạn đã sử dụng dịch vụ của chúng tôi.</p>" +
                "</div>" +
                "<div class=\"footer\">" +
                "<p>&copy; " + LocalDate.now().getYear() + " Công ty của bạn. Tất cả các quyền được bảo lưu.</p>" +
                "<p>Địa chỉ công ty bạn, Thành phố, Quốc gia</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }

    private String getFailureEmailContentTransaction(String recipientName, double amount,String bikepoint,String reason, String date ) {
        return "<!DOCTYPE html>" +
                "<html lang=\"vi\">" +
                "<head>" +
                "<meta charset=\"UTF-8\">" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "<style>" +
                "body {font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f4f4f4;}" +
                ".container {width: 100%; max-width: 600px; margin: 0 auto; background-color: #ffffff; padding: 20px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);}" +
                ".header {text-align: center; padding: 10px 0;}" +
                ".header img {max-width: 150px;}" +
                ".content {padding: 20px; text-align: left;}" +
                ".footer {text-align: center; padding: 10px 0; font-size: 12px; color: #777;}" +
                ".button {display: inline-block; padding: 10px 20px; margin-top: 20px; background-color: #28a745; color: #ffffff; text-decoration: none; border-radius: 5px;}" +
                "@media (max-width: 600px) {.container {padding: 10px;} .content {padding: 10px;}}" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class=\"container\">" +
                "<div class=\"header\">" +
                "<img src=\"your-logo-url.png\" alt=\"Company Logo\">" +
                "</div>" +
                "<div class=\"content\">" +
                "<h1>Chào " + recipientName + ",</h1>" +
                "<p>Giao dịch chuyển tiền cho " + bikepoint + " không thành công.</p>" +
                "<p>Lý do: "+ reason +".</p>" +
                "<p>Số tiền: " + amount + "</p>" +
                "<p>Ngày thực hiện: " + date + "</p>" +
                "<p>Hãy liên hệ lại với chúng tôi nếu có gì đó không đúng, bằng cách reply mail này.</p>" +
                "</div>" +
                "<div class=\"footer\">" +
                "<p>&copy; " + LocalDate.now().getYear() + " Công ty của bạn. Tất cả các quyền được bảo lưu.</p>" +
                "<p>Địa chỉ công ty bạn, Thành phố, Quốc gia</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }

    private String getFailureEmailContent(String recipientName, double amount, String date, String failureReason ) {
        return "<!DOCTYPE html>" +
                "<html lang=\"vi\">" +
                "<head>" +
                "<meta charset=\"UTF-8\">" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "<style>" +
                "body {font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f4f4f4;}" +
                ".container {width: 100%; max-width: 600px; margin: 0 auto; background-color: #ffffff; padding: 20px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);}" +
                ".header {text-align: center; padding: 10px 0;}" +
                ".header img {max-width: 150px;}" +
                ".content {padding: 20px; text-align: left;}" +
                ".footer {text-align: center; padding: 10px 0; font-size: 12px; color: #777;}" +
                ".button {display: inline-block; padding: 10px 20px; margin-top: 20px; background-color: #dc3545; color: #ffffff; text-decoration: none; border-radius: 5px;}" +
                "@media (max-width: 600px) {.container {padding: 10px;} .content {padding: 10px;}}" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class=\"container\">" +
                "<div class=\"header\">" +
                "<img src=\"your-logo-url.png\" alt=\"Company Logo\">" +
                "</div>" +
                "<div class=\"content\">" +
                "<h1>Chào " + recipientName + ",</h1>" +
                "<p>Rất tiếc, yêu cầu rút tiền của bạn đã thất bại.</p>" +
                "<p>Số tiền: " + amount + "</p>" +
                "<p>Ngày thực hiện: " + date + "</p>" +
                "<p>Lý do thất bại: " + failureReason + "</p>" +
                "<p>Vui lòng kiểm tra lại thông tin và thử lại.</p>" +
                "</div>" +
                "<div class=\"footer\">" +
                "<p>&copy; " + LocalDate.now().getYear() + " Công ty của bạn. Tất cả các quyền được bảo lưu.</p>" +
                "<p>Địa chỉ công ty bạn, Thành phố, Quốc gia</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }
}
