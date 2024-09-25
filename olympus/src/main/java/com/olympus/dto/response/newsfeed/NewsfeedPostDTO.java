package com.olympus.dto.response.newsfeed;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class NewsfeedPostDTO {
    private Long postId;
    private Long userId;
    private String userFirstname;
    private String userLastname;
    private String userEmail;
    private String userAvatar;
    private String content;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedTime;
    private List<PostImageDTO> images;
    private List<PostInteractionUserDTO> likes;
    private List<PostCommentDTO> comments;
}
