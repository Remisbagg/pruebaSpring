package com.olympus.service;

import jakarta.mail.MessagingException;

public interface IMailService {
    void sendLoginOTP(String email) throws MessagingException;

    void sendPasswordResetToken(String email) throws MessagingException;
}
