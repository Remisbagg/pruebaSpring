package com.olympus.dto.response;

import lombok.Data;

@Data
public class PostCommentUser {
    private Long userId;
    private String email;
    private String firstName;
    private String lastName;
    private String avatarUrl;
}
