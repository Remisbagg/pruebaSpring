package com.back.dto.request;

import com.back.entity.Privacy;
import com.back.validator.annotation.ValidEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Schema
public class PostCreate {
    @Schema(example = "Hello World. This is a new post")
    @Pattern(regexp = ".*\\S.*", message = "The content cannot be blank")
    private String content;
    @Schema(example = "public")
    @NotBlank(message = "The privacy is not valid")
    @ValidEnum(enumClass = Privacy.class, message = "The privacy is not valid")
    private String privacy;
}
