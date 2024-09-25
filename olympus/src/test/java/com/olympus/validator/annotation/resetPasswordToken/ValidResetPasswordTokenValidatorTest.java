package com.olympus.validator.annotation.resetPasswordToken;

import com.olympus.service.IResetPwdTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(MockitoExtension.class)
class ValidResetPasswordTokenValidatorTest {
    private ValidResetPasswordTokenValidator validator;
    @Mock
    private IResetPwdTokenService resetPwdTokenService;

    @BeforeEach
    void setup() {
        validator = new ValidResetPasswordTokenValidator(resetPwdTokenService);
    }

    @Test
    void whenIdIsNull_thenShouldReturnFalse() {
        // Act
        boolean result = validator.isValid(null, null);

        // Assert
        assertFalse(result);
    }
}