package com.olympus.service;

import com.olympus.entity.User;

public interface IAuthenticationService {

    void createAuthentication(User user, String code);

    void reset(String email);
}
