package com.olympus.service.impl;

import com.olympus.config.Constant;
import com.olympus.entity.User;
import com.olympus.exception.UserNotFoundException;
import com.olympus.service.IAuthenticationService;
import com.olympus.service.IMailService;
import com.olympus.service.IResetPwdTokenService;
import com.olympus.service.IUserService;
import com.olympus.utils.AppUtils;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional
public class MailServiceImpl implements IMailService {
    private final JavaMailSender mailSender;
    private final IUserService userService;
    private final IAuthenticationService authenticationService;
    private final IResetPwdTokenService resetPwdTokenService;

    @Autowired
    public MailServiceImpl(JavaMailSender mailSender,
                           IUserService userService,
                           IAuthenticationService authenticationService,
                           IResetPwdTokenService resetPwdTokenService) {
        this.mailSender = mailSender;
        this.userService = userService;
        this.authenticationService = authenticationService;
        this.resetPwdTokenService = resetPwdTokenService;
    }

    @Override
    public void sendLoginOTP(String email) throws MessagingException {
        String code = AppUtils.generateRandomOTP();
        User user = userService.findUserByEmail(email).orElseThrow(() -> new UserNotFoundException(email + " not found"));

        authenticationService.createAuthentication(user, code);

        MimeMessage message = mailSender.createMimeMessage();
        message.setFrom(new InternetAddress(Constant.MailSenderAddress));
        message.setRecipients(MimeMessage.RecipientType.TO, email);
        message.setSubject("Your OTP");
        String htmlContent = "<p>Your verification code is <b>" + code + "</b></p>" +
                "<br>" +
                "<p>Your code will be expired in 5 minutes</p>";
        message.setContent(htmlContent, "text/html; charset=utf-8");
        mailSender.send(message);

    }

    @Override
    public void sendPasswordResetToken(String email) throws MessagingException {
        String token = UUID.randomUUID().toString();
        User user = userService.findUserByEmail(email).orElseThrow(() -> new UserNotFoundException(email + "not found"));
        resetPwdTokenService.createToken(user, token);

        MimeMessage message = mailSender.createMimeMessage();
        message.setFrom(new InternetAddress(Constant.MailSenderAddress));
        message.setRecipients(MimeMessage.RecipientType.TO, email);
        message.setSubject("Reset Password");
        String link = "http://localhost:4200/reset-password?token=" + token;
        String htmlContent = "<p> Click link below to reset password</p>" +
                "<br>" +
                "<a href=\"" + link + "\">Reset</a>" +
                "<br>" +
                "<p>Your code will be expired in 5 minutes</p>";
        message.setContent(htmlContent, "text/html; charset=utf-8");
        mailSender.send(message);
    }
}
