package com.olympus.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Schema
@Data
public class PostCommentUpdate {
    @Schema(example = "This comment is edited!")
    @NotBlank(message = "Comment something, dont leave it blank!")
    private String content;
}
