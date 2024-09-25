package com.olympus.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class CustomPageableTest {
    @Test
    void testCustomPageableConstructorAndAccessors() {
        // Arrange - create instance of CustomPageable with sample data
        int pageNumber = 1;
        int pageSize = 10;
        int totalPages = 5;
        long totalElements = 50;
        boolean isFirst = true;
        boolean isLast = false;

        CustomPageable pageable = new CustomPageable(pageNumber, pageSize, totalPages, totalElements, isFirst, isLast);

        // Act & Assert - verify that the getters return the correct data
        assertEquals(pageNumber, pageable.getPageNumber());
        assertEquals(pageSize, pageable.getPageSize());
        assertEquals(totalPages, pageable.getTotalPages());
        assertEquals(totalElements, pageable.getTotalElements());
        assertEquals(isFirst, pageable.isFirst());
        assertEquals(isLast, pageable.isLast());
    }
}