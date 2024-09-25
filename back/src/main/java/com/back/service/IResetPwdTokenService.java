package com.back.service;

import com.back.dto.request.AccountPasswordResetToken;
import com.back.entity.User;

public interface IResetPwdTokenService {
    boolean existByTokenAndEmail(String token, String email);

    void createToken(User user, String token);

    void reset(AccountPasswordResetToken token);
    void reset(String token);

    boolean existByToken(String token);
    String findEmailByToken(String token);
}