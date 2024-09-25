package com.back.service;

import com.back.model.User;

public interface IAuthenticationService {

    void createAuthentication(User user, String code);

    void reset(String email);
}
