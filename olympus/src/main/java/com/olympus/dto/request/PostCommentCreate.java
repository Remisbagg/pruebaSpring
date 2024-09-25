package com.olympus.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Schema
@Data
public class PostCommentCreate {
    @Schema(example = "This post is great!")
    @NotBlank(message = "Comment something, dont leave it blank!")
    private String content;
}
