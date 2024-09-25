package com.olympus.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomPageTest {
    @Test
    void testCustomPageConstructor() {
        // Arrange
        List<String> contentList = Arrays.asList("Item1", "Item2", "Item3");
        Page<String> mockPage = Mockito.mock(Page.class);
        Pageable mockPageable = Mockito.mock(Pageable.class);

        when(mockPage.getContent()).thenReturn(contentList);
        when(mockPage.getPageable()).thenReturn(mockPageable);
        when(mockPageable.getPageNumber()).thenReturn(1);
        when(mockPageable.getPageSize()).thenReturn(3);
        when(mockPage.getTotalPages()).thenReturn(2);
        when(mockPage.getTotalElements()).thenReturn(6L);
        when(mockPage.isFirst()).thenReturn(true);
        when(mockPage.isLast()).thenReturn(false);

        // Act
        CustomPage<String> customPage = new CustomPage<>(mockPage);

        // Assert
        assertEquals(contentList, customPage.getContent(), "Content should match provided page content.");
        assertEquals(1, customPage.getPageable().getPageNumber(), "Page number should match.");
        assertEquals(3, customPage.getPageable().getPageSize(), "Page size should match.");
        assertEquals(2, customPage.getPageable().getTotalPages(), "Total pages should match.");
        assertEquals(6L, customPage.getPageable().getTotalElements(), "Total elements should match.");
        assertTrue(customPage.getPageable().isFirst(), "Is first should match.");
        assertFalse(customPage.getPageable().isLast(), "Is last should match.");
    }
}