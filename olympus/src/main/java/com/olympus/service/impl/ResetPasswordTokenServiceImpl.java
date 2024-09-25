package com.olympus.service.impl;

import com.google.common.hash.Hashing;
import com.olympus.dto.request.AccountPasswordResetToken;
import com.olympus.entity.ResetPwdToken;
import com.olympus.entity.User;
import com.olympus.exception.UserNotFoundException;
import com.olympus.repository.IResetPwdTokenRepository;
import com.olympus.service.IResetPwdTokenService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class ResetPasswordTokenServiceImpl implements IResetPwdTokenService {
    private final IResetPwdTokenRepository resetPwdTokenRepository;

    @Autowired
    public ResetPasswordTokenServiceImpl(IResetPwdTokenRepository resetPwdTokenRepository) {
        this.resetPwdTokenRepository = resetPwdTokenRepository;
    }

    @Override
    public boolean existByTokenAndEmail(String token, String email) {
        String hashedToken = Hashing.sha256()
                .hashString(token, StandardCharsets.UTF_8)
                .toString();
        Optional<ResetPwdToken> resetPwdToken = resetPwdTokenRepository.findByTokenAndUser_Email(hashedToken, email);
        if (resetPwdToken.isPresent()) {
            String storedToken = resetPwdToken.get().getToken();
            if (storedToken.equals(hashedToken)) {
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime exprTime = resetPwdToken.get().getCreatedTime();
                return now.isBefore(exprTime.plusMinutes(5));
            }
        }
        return false;
    }

    @Override
    public void createToken(User user, String token) {
        String hashedToken = Hashing.sha256()
                .hashString(token, StandardCharsets.UTF_8)
                .toString();
        Optional<ResetPwdToken> storedToken = resetPwdTokenRepository.findByUser(user);
        if (storedToken.isPresent()) {
            storedToken.get().setToken(hashedToken);
            storedToken.get().setCreatedTime(LocalDateTime.now());
            resetPwdTokenRepository.save(storedToken.get());
        } else {
            ResetPwdToken newToken = new ResetPwdToken(user, hashedToken);
            resetPwdTokenRepository.save(newToken);
        }
    }

    @Override
    public void reset(AccountPasswordResetToken token) {
        String hashedToken = Hashing.sha256()
                .hashString(token.getToken(), StandardCharsets.UTF_8)
                .toString();
        ResetPwdToken resetPwdToken = resetPwdTokenRepository.findByTokenAndUser_Email(hashedToken, token.getEmail())
                .orElseThrow(() -> new UserNotFoundException(token.getEmail()));
        String newToken = UUID.randomUUID().toString();
        String newHashedToken = Hashing.sha256()
                .hashString(newToken, StandardCharsets.UTF_8)
                .toString();
        resetPwdToken.setToken(newHashedToken);
        resetPwdToken.setCreatedTime(LocalDateTime.now());
        resetPwdTokenRepository.save(resetPwdToken);
    }

    @Override
    public boolean existByToken(String token) {
        String hashedToken = Hashing.sha256()
                .hashString(token, StandardCharsets.UTF_8)
                .toString();
        Optional<ResetPwdToken> resetPwdToken = resetPwdTokenRepository.findByToken(hashedToken);
        if (resetPwdToken.isPresent()) {
            String storedToken = resetPwdToken.get().getToken();
            if (storedToken.equals(hashedToken)) {
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime exprTime = resetPwdToken.get().getCreatedTime();
                return now.isBefore(exprTime.plusMinutes(5));
            }
        }
        return false;
    }

    @Override
    public void reset(String token) {
        String hashedToken = Hashing.sha256()
                .hashString(token, StandardCharsets.UTF_8)
                .toString();
        ResetPwdToken resetPwdToken = resetPwdTokenRepository.findByToken(hashedToken)
                .orElseThrow(() -> new UserNotFoundException(token));
        String newToken = UUID.randomUUID().toString();
        String newHashedToken = Hashing.sha256()
                .hashString(newToken, StandardCharsets.UTF_8)
                .toString();
        resetPwdToken.setToken(newHashedToken);
        resetPwdTokenRepository.save(resetPwdToken);
    }

    @Override
    public String findEmailByToken(String token) {
        String hashedToken = Hashing.sha256()
                .hashString(token, StandardCharsets.UTF_8)
                .toString();
        ResetPwdToken resetPwdToken = resetPwdTokenRepository.findByToken(hashedToken)
                .orElseThrow(() -> new UserNotFoundException(token));
        return resetPwdToken.getUser().getEmail();
    }
}
