package com.digitallending.userservice.utils;

import com.digitallending.userservice.exception.MailNotSentException;
import com.digitallending.userservice.service.def.EmailOTPService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class MailServiceUtil {
    private final Random randomIndex = new Random();

    @Autowired
    private EmailOTPService emailOTPService;
    @Autowired
    private JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String fromMail;

    public void sendOTPEmail(String to) throws MailNotSentException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper;
        String otp = generateOTP();

        String subject = "OTP Verification";
        String body = String.format("""
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>OTP Email</title>
                </head>
                <body>
                    <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;">
                        <h2>One Time Password (OTP) Email</h2>
                        <p>Your One Time Password (OTP) is: <strong>%s</strong></p>
                        <p>This OTP is valid for a single use and should not be shared with anyone.</p>
                        <p>If you did not request this OTP, please ignore this email.</p>
                    </div>
                </body>
                </html>
                """, otp);

        try {
            helper = new MimeMessageHelper(message, true); // true indicates multipart message
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true); // true indicates HTML content
            mailSender.send(message);
        } catch (MailException | MessagingException e) {
            throw new MailNotSentException("Mail Could not be sent"); // Handle any exceptions properly
        }
        emailOTPService.saveOTP(to, otp);

    }

    public void sendStatusMail(String to, String status, String reason) throws MailNotSentException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper;

        String subject = "Status of verification";
        String body = String.format("""
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>OTP Email</title>
                </head>
                <body>
                    <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;">
                        <h2>Status: %s</h2>
                        <p>%s</p>
                    </div>
                </body>
                </html>
                """, status, reason);
        try {
            helper = new MimeMessageHelper(message, true); // true indicates multipart message
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true); // true indicates HTML content
            mailSender.send(message);
        } catch (MailException | MessagingException e) {
            throw new MailNotSentException("Mail Could not be sent"); // Handle any exceptions properly
        }
    }

    public void sendMail(String to, String subject, String bodyMessage) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper;

        String body = String.format("""
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>OTP Email</title>
                </head>
                <body>
                    <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;">
                        <p>%s</p>
                    </div>
                </body>
                </html>
                """, bodyMessage);
        try {
            helper = new MimeMessageHelper(message, true); // true indicates multipart message
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true); // true indicates HTML content
            mailSender.send(message);
        } catch (MailException | MessagingException e) {
            throw new MailNotSentException("Mail Could not be sent"); // Handle any exceptions properly
        }
    }

    private String generateOTP() {
        int length = 6;
        String num = "0123456789";

        StringBuilder otp = new StringBuilder();

        for (int i = 0; i < length; i++) {
            otp.append(num.charAt(randomIndex.nextInt(num.length())));

        }
        return otp.toString();

    }
}
