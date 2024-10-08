package com.back.dto.response.curentUserPost;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.back.dto.response.newsfeed.PostCommentDTO;
import com.back.dto.response.newsfeed.PostImageDTO;
import com.back.dto.response.newsfeed.PostInteractionUserDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CurrentUserPost {
    private String postId;
    private String userId;
    private String userFirstname;
    private String userLastname;
    private String userEmail;
    private String userAvatar;
    private String content;
    private String privacy;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedTime;
    private List<PostImageDTO> images;
    private List<PostInteractionUserDTO> likes;
    private List<PostCommentDTO> comments;
}
