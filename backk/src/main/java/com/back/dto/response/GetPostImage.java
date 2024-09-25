package com.back.dto.response;

import lombok.Data;

@Data
public class GetPostImage {
    private String imageId;
    private String url;
    private String postId;
}
