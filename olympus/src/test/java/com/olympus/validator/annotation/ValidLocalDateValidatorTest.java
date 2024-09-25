package com.olympus.validator.annotation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class ValidLocalDateValidatorTest {
    private ValidLocalDateValidator validator;

    @BeforeEach
    void setUp() {
        validator = new ValidLocalDateValidator();
    }

    @Test
    void whenValueIsValidDate_thenShouldReturnTrue() {
        // Arrange
        String validDate = "2023-01-01"; // Use a known good date

        // Act
        boolean result = validator.isValid(validDate, null);

        // Assert
        assertTrue(result);
    }

    @Test
    void whenValueIsInvalidDate_thenShouldReturnFalse() {
        // Arrange
        String invalidDate = "2023-02-30"; // February 30th is not a real date

        // Act
        boolean result = validator.isValid(invalidDate, null);

        // Assert
        assertFalse(result);
    }

    @Test
    void whenValueIsNull_thenShouldReturnTrue() {
        // Act
        boolean result = validator.isValid(null, null);

        // Assert
        assertTrue(result);
    }
}