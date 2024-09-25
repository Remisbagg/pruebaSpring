package com.olympus.service.impl;

import com.olympus.entity.Post;
import com.olympus.entity.PostImage;
import com.olympus.repository.IPostImageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PostImageServiceImplTest {
    @InjectMocks
    private PostImageServiceImpl postImageService;
    @Mock
    private IPostImageRepository postImageRepository;

    @Test
    public void testSaveSingleImage() {
        // Arrange
        String imageUrl = "https://example.com/image.jpg";
        Post post = new Post();
        PostImage postImage = new PostImage(imageUrl, post); // Your PostImage constructor might differ

        // Act
        postImageService.save(imageUrl, post);

        // Assert
        // Verify that save was called on the repository
        verify(postImageRepository).save(postImage);
    }

    @Test
    public void testSaveMultipleImages() {
        // Arrange
        List<String> imageUrls = List.of("https://example.com/image1.jpg", "https://example.com/image2.jpg");
        Post post = new Post();

        // Act
        postImageService.save(imageUrls, post);

        // Assert
        verify(postImageRepository, times(imageUrls.size())).save(any(PostImage.class)); // Verify that save was called the correct number of times
    }

    @Test
    public void testDeleteByPost() {
        // Arrange
        Post post = new Post();

        // Act
        postImageService.deleteByPost(post);

        // Assert
        // Verify that deleteByPost was called on the repository
        verify(postImageRepository).deleteByPost(post);
    }
}