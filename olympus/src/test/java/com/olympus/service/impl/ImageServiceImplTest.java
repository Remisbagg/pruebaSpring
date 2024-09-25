package com.olympus.service.impl;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.olympus.config.FirebaseConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.context.event.ApplicationReadyEvent;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ImageServiceImplTest {
    @Mock
    private FirebaseConfig firebaseConfig;
    @Mock
    private Storage storage;
    @Mock
    private Bucket bucket;
    @Mock
    private Blob blob;
    @InjectMocks
    private ImageServiceImpl imageService;

    @BeforeEach
    void setUp() {
        when(firebaseConfig.getBucketName()).thenReturn("bucket-name");
        when(storage.get(anyString())).thenReturn(bucket);
        when(bucket.get(anyString())).thenReturn(blob);
    }

    @Test
    void testGetImageUrl() {
        String name = "test-image.png";
        when(firebaseConfig.getImageUrl()).thenReturn("http://localhost/%s");
        String url = imageService.getImageUrl(name);
        assertEquals("http://localhost/test-image.png", url);
    }

    @Test
    void testInit() {
        ApplicationReadyEvent event = mock(ApplicationReadyEvent.class);
        imageService.init(event);
        assertDoesNotThrow(() -> Exception.class);
    }
}