package com.olympus.config.jwt;

import com.olympus.config.security.AuthDetailsImpl;
import com.olympus.config.Constant;
import com.olympus.entity.Authentication;
import com.olympus.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtProviderTest {
    @InjectMocks
    private JwtProvider jwtProvider;

    @Mock
    private AuthDetailsImpl mockAuthDetails;

    @Test
    void whenGenerateToken_thenReturnJwt() {
        Authentication mockAuth = mock(Authentication.class);
        User mockUser = mock(User.class);
        when(mockAuthDetails.getAuthentication()).thenReturn(mockAuth);
        when(mockAuthDetails.getAuthentication().getUser()).thenReturn(mockUser);
        when(mockAuthDetails.getUsername()).thenReturn("testUser");
        String token = jwtProvider.generateToken(mockAuthDetails);
        assertNotNull(token);
    }

    @Test
    void whenGetUserEmailFromJwt_thenReturnEmail() {
        String email = "testUser";
        String token = createToken(email);
        assertEquals(email, jwtProvider.getUserEmailFromJwt(token));
    }

    @Test
    void whenGetUserIdFromJwt_thenReturnUserId() {
        String userId = "12345";
        String token = createTokenWithId(userId);
        assertEquals(userId, jwtProvider.getUserIdFromJwt(token));
    }

    @Test
    void whenTokenIsValidated_thenReturnTrue() {
        String token = createToken("testUser");
        assertTrue(jwtProvider.isTokenValidated(token));
    }

    @Test
    void whenTokenIsInvalid_thenReturnFalse() {
        String token = "invalidToken";
        assertFalse(jwtProvider.isTokenValidated(token));
    }

    // Utility method to create a token for testing
    private String createToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .signWith(SignatureAlgorithm.HS512, Constant.JWT_SECRET)
                .compact();
    }

    // Utility method to create a token with a user ID claim for testing
    private String createTokenWithId(String userId) {
        return Jwts.builder()
                .claim("id", userId)
                .signWith(SignatureAlgorithm.HS512, Constant.JWT_SECRET)
                .compact();
    }

}