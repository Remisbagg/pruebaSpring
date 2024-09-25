package com.olympus.config;

import com.olympus.config.security.AuthDetailsImpl;
import com.olympus.entity.Authentication;
import com.olympus.entity.Role;
import com.olympus.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthDetailsImplTest {
    @InjectMocks
    private AuthDetailsImpl authDetails;

    @Mock
    private Authentication authentication;

    @Mock
    private User user;

    @Mock
    private Role role;

    @Test
    void testGetAuthorities() {
        when(authentication.getUser()).thenReturn(user);
        when(user.getRole()).thenReturn(role);
        when(role.toString()).thenReturn("USER_ROLE");
        Collection<? extends GrantedAuthority> authorities = authDetails.getAuthorities();
        assertNotNull(authorities);
        assertEquals("USER_ROLE", authorities.iterator().next().getAuthority());
    }

    @Test
    void testGetPassword() {
        when(authentication.getCode()).thenReturn("password");
        assertEquals("password", authDetails.getPassword());
    }

    @Test
    void testGetUsername() {
        when(authentication.getUser()).thenReturn(user);
        when(user.getEmail()).thenReturn("user@example.com");
        assertEquals("user@example.com", authDetails.getUsername());
    }

    @Test
    void testIsAccountNonExpired() {
        assertTrue(authDetails.isAccountNonExpired());
    }

    @Test
    void testIsAccountNonLocked() {
        assertTrue(authDetails.isAccountNonLocked());
    }

    @Test
    void testIsCredentialsNonExpired() {
        when(authentication.getCreatedTime()).thenReturn(LocalDateTime.now().minusMinutes(1));
        assertTrue(authDetails.isCredentialsNonExpired());
    }

    @Test
    void testIsEnabled() {
        assertTrue(authDetails.isEnabled());
    }
}