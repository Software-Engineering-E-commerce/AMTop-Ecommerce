package com.example.BackEnd.Services;

import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import jakarta.mail.internet.MimeMessage;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class EmailServiceTest {
    @Value("${spring.mail.username}")
    private String fromEmail;

    @InjectMocks
    private EmailService emailService;

    @Mock
    private JavaMailSender emailSender;

    @Test
    public void testSendEmail() throws Exception {
        // Prepare test data
        String to = "test@example.com";
        String subject = "Test Subject";
        String body = "Test Body";

        // Mock MimeMessage
        MimeMessage mimeMessage = Mockito.mock(MimeMessage.class);

        // Mock behavior of emailSender
        Mockito.when(emailSender.createMimeMessage()).thenReturn(mimeMessage);
        Mockito.doNothing().when(emailSender).send(mimeMessage);
        emailService.setFromEmail(fromEmail);
        // Call the method under test
        emailService.sendEmail(to, subject, body);

        // Verify that emailSender.createMimeMessage was called once
        verify(emailSender, times(1)).createMimeMessage();
        // Verify that emailSender.send was called once
        verify(emailSender, times(1)).send(mimeMessage);
    }

    @Test
    public void testSendEmailWithMailSendException() throws MailSendException {
        String to = "test@example.com";
        String subject = "Test Subject";
        String body = "Test Body";

        // Mock MimeMessage
        MimeMessage mimeMessage = Mockito.mock(MimeMessage.class);

        // Mock behavior of emailSender
        Mockito.when(emailSender.createMimeMessage()).thenReturn(mimeMessage);
        Mockito.doThrow(new MailSendException("Test")).when(emailSender).send(mimeMessage);
        emailService.setFromEmail(fromEmail);

        // Call the method under test
        MailSendException exception = assertThrows(MailSendException.class, () -> {
            emailService.sendEmail(to, subject, body);
        });

        // Verify that emailSender.createMimeMessage was called once
        verify(emailSender, times(1)).createMimeMessage();
        // Verify that emailSender.send was called once
        verify(emailSender, times(1)).send(mimeMessage);
    }

    @Test
    public void testSendEmailWithMessagingException() throws MessagingException {
        String to = "test@example.com";
        String subject = "Test Subject";
        String body = "Test Body";

        MimeMessage mimeMessage = Mockito.mock(MimeMessage.class);
        Mockito.when(emailSender.createMimeMessage()).thenReturn(mimeMessage);

        // Make MimeMessageHelper throw MessagingException
        Mockito.doThrow(new MessagingException("Test")).when(mimeMessage).setSubject(subject);

        emailService.setFromEmail(fromEmail);

        // Act and Assert
        MessagingException exception = assertThrows(MessagingException.class, () -> {
            emailService.sendEmail(to, subject, body);
        });

        // Verify that emailSender.createMimeMessage was called once
        verify(emailSender, times(1)).createMimeMessage();
        // Verify that emailSender.send was not called
        verify(emailSender, never()).send(mimeMessage);
    }
}
