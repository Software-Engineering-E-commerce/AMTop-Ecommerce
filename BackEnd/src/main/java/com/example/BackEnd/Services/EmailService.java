package com.example.BackEnd.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Autowired
    private JavaMailSender emailSender;

    @Async
    public void sendEmail(String to, String subject, String body) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            System.out.println();
            helper.setFrom(fromEmail);
            System.out.println(fromEmail);
            helper.setTo(to);
            System.out.println(to);
            helper.setSubject(subject);
            helper.setText(body, true);
            System.out.println(subject);
            emailSender.send(message);
            System.out.println("Mail Send...");
        } catch (MessagingException e) {
            throw new MessagingException("Messaging Exception");
        } catch (MailException e) {
            throw new MailSendException("Mail Send Exception");
        }
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }
}