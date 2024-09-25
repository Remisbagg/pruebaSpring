package com.olympus.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class MailConfigTest {
    @Test
    void testSender() {
        // Given
        MailConfig config = new MailConfig();

        // When
        JavaMailSenderImpl mailSender = (JavaMailSenderImpl) config.sender();

        // Then
        assertEquals("smtp.gmail.com", mailSender.getHost());
        assertEquals(587, mailSender.getPort());
        assertEquals(Constant.MailSenderAddress, mailSender.getUsername());
        assertEquals(Constant.MailSenderPassword, mailSender.getPassword());

        // Verify properties
        Properties props = mailSender.getJavaMailProperties();
        assertEquals("smtp", props.get("mail.transport.protocol"));
        assertEquals("true", props.get("mail.smtp.auth"));
        assertEquals("true", props.get("mail.smtp.starttls.enable"));
        assertEquals("true", props.get("mail.debug"));
    }
}