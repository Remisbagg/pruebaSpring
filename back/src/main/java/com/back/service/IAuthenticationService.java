package com.back.service;

import com.back.entity.User;

public interface IAuthenticationService {

    void createAuthentication(User user, String code);

    void reset(String email);
}
