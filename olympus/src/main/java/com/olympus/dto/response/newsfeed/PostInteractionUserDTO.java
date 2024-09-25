package com.olympus.dto.response.newsfeed;

import lombok.Data;

@Data
public class PostInteractionUserDTO {
    private Long userId;
    private String email;
    private String firstName;
    private String lastName;
    private String avatarUrl;
}
