package com.olympus.validator.annotation.post;

import com.olympus.service.IPostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
class ExistByPostIdAndNotDeletedValidatorTest {
    private ExistByPostIdAndNotDeletedValidator validator;
    @Mock
    private IPostService postService;

    @BeforeEach
    void setUp() {
        validator = new ExistByPostIdAndNotDeletedValidator(postService);
    }

    @Test
    void whenIdIsNull_thenShouldReturnFalse() {
        // Act
        boolean result = validator.isValid(null, null);

        // Assert
        assertFalse(result);
    }

    @Test
    void whenExceptionOccur_ThenShouldReturnFalse() {
        //Arrange
        Mockito.lenient().when(postService.existByPostIdAndNotDeleted(anyLong())).thenThrow(NullPointerException.class);

        //Act
        boolean result = validator.isValid(null, null);

        //Assert
        assertFalse(result);
    }

}