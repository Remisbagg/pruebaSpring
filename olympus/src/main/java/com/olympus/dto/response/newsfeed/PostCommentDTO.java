package com.olympus.dto.response.newsfeed;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.olympus.dto.response.PostCommentUser;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostCommentDTO {
    private Long commentId;
    private PostCommentUser user;
    private String content;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedTime;
}
