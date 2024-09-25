package com.olympus.dto.response;

import lombok.Data;

@Data
public class GetPostImage {
    private String imageId;
    private String url;
    private String postId;
}
