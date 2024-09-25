package com.olympus.config.security;

import com.olympus.entity.Authentication;
import com.olympus.repository.IAuthenticationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthDetailsServiceImpl implements UserDetailsService {
    private final IAuthenticationRepository authenticationRepository;

    @Autowired
    AuthDetailsServiceImpl(IAuthenticationRepository authenticationRepository) {
        this.authenticationRepository = authenticationRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Authentication> authentication = authenticationRepository.findAuthenticationByUser_Email(email);
        if (authentication.isEmpty()) {
            throw new UsernameNotFoundException(email);
        }
        return new AuthDetailsImpl(authentication.get());
    }
}
