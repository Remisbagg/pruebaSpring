package com.olympus.config;

import com.olympus.config.security.AuthDetailsServiceImpl;
import com.olympus.entity.Authentication;
import com.olympus.repository.IAuthenticationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthDetailsServiceImplTest {
    @InjectMocks
    private AuthDetailsServiceImpl authDetailsService;

    @Mock
    private IAuthenticationRepository authenticationRepository;

    @Test
    void whenValidEmail_thenUserDetailsShouldBeFound() {
        String email = "test@example.com";
        Authentication mockAuthentication = new Authentication(); // Replace with actual instantiation
        when(authenticationRepository.findAuthenticationByUser_Email(email)).thenReturn(Optional.of(mockAuthentication));

        UserDetails userDetails = authDetailsService.loadUserByUsername(email);

        assertNotNull(userDetails);
        // Add more assertions to validate the actual attributes of the returned UserDetails
    }

    @Test
    void whenInvalidEmail_thenThrowUsernameNotFoundException() {
        String email = "nonexisting@example.com";
        when(authenticationRepository.findAuthenticationByUser_Email(email)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            authDetailsService.loadUserByUsername(email);
        });
    }
}