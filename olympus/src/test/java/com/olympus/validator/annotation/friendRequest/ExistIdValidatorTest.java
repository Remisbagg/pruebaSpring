package com.olympus.validator.annotation.friendRequest;

import com.olympus.service.IFriendRequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExistIdValidatorTest {
    private ExistIdValidator existIdValidator;

    @Mock
    private IFriendRequestService friendRequestService;

    @BeforeEach
    void setUp() {
        existIdValidator = new ExistIdValidator(friendRequestService);
    }

    @Test
    void whenIdIsValid_thenShouldReturnTrue() {
        // Arrange
        Long validId = 1L;
        when(friendRequestService.existByRequestId(validId)).thenReturn(true);

        // Act
        boolean result = existIdValidator.isValid(validId, null);

        // Assert
        assertTrue(result);
    }

    @Test
    void whenIdIsNull_thenShouldReturnFalse() {
        // Act
        boolean result = existIdValidator.isValid(null, null);

        // Assert
        assertFalse(result);
    }

    @Test
    void whenServiceThrowsException_thenShouldReturnFalse() {
        // Arrange
        Long invalidId = 1L;
        when(friendRequestService.existByRequestId(invalidId)).thenThrow(RuntimeException.class);

        // Act
        boolean result = existIdValidator.isValid(invalidId, null);

        // Assert
        assertFalse(result);
    }
}