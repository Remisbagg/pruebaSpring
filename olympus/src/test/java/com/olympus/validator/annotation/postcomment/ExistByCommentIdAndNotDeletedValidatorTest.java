package com.olympus.validator.annotation.postcomment;

import com.olympus.service.IPostCommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(MockitoExtension.class)
class ExistByCommentIdAndNotDeletedValidatorTest {
    @Mock
    IPostCommentService postCommentService;
    private ExistByCommentIdAndNotDeletedValidator validator;

    @BeforeEach
    void setup() {
        validator = new ExistByCommentIdAndNotDeletedValidator(postCommentService);
    }

    @Test
    void whenIdIsNull_thenShouldReturnFalse() {
        // Act
        boolean result = validator.isValid(null, null);

        // Assert
        assertFalse(result);
    }
}