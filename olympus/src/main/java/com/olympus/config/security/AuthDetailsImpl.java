package com.olympus.config.security;

import com.olympus.entity.Authentication;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

@Data
@AllArgsConstructor
public class AuthDetailsImpl implements UserDetails {
    private Authentication authentication;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(authentication.getUser().getRole().toString()));
    }

    @Override
    public String getPassword() {
        return authentication.getCode();
    }

    @Override
    public String getUsername() {
        return authentication.getUser().getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime exprTime = getAuthentication().getCreatedTime();
        return now.isBefore(exprTime.plusMinutes(5));
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
