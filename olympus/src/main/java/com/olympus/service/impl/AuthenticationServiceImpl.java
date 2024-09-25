package com.olympus.service.impl;

import com.olympus.entity.Authentication;
import com.olympus.entity.User;
import com.olympus.exception.UserNotFoundException;
import com.olympus.repository.IAuthenticationRepository;
import com.olympus.service.IAuthenticationService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class AuthenticationServiceImpl implements IAuthenticationService {
    private final IAuthenticationRepository authenticationRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthenticationServiceImpl(IAuthenticationRepository authenticationRepository,
                                     PasswordEncoder passwordEncoder) {
        this.authenticationRepository = authenticationRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void createAuthentication(User user, String code) {
        String hashedCode = passwordEncoder.encode(code);
        Optional<Authentication> storedAuth = authenticationRepository.findByUser(user);
        if (storedAuth.isPresent()) {
            storedAuth.get().setCode(hashedCode);
            storedAuth.get().setCreatedTime(LocalDateTime.now());
            authenticationRepository.save(storedAuth.get());
        } else {
            Authentication authentication = new Authentication(user, hashedCode);
            authenticationRepository.save(authentication);
        }
    }

    @Override
    public void reset(String email) {
        Authentication authentication = authenticationRepository.
                findAuthenticationByUser_Email(email).orElseThrow(() -> new UserNotFoundException(email));
        authentication.setCode("");
        authenticationRepository.save(authentication);
    }
}
