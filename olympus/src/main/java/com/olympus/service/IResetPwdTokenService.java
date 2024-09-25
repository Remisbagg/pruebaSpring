package com.olympus.service;

import com.olympus.dto.request.AccountPasswordResetToken;
import com.olympus.entity.User;

public interface IResetPwdTokenService {
    boolean existByTokenAndEmail(String token, String email);

    void createToken(User user, String token);

    void reset(AccountPasswordResetToken token);
    void reset(String token);

    boolean existByToken(String token);
    String findEmailByToken(String token);
}