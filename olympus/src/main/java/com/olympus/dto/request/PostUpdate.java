package com.olympus.dto.request;

import com.olympus.entity.Privacy;
import com.olympus.validator.annotation.ValidEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Schema
public class PostUpdate {
    @Schema(example = "This is an update post")
    @Pattern(regexp = ".*\\S.*", message = "The content cannot be blank")
    private String content;
    @Schema(example = "public")
    @NotBlank(message = "The privacy is not valid")
    @ValidEnum(enumClass = Privacy.class, message = "The privacy is not valid")
    private String privacy;
}
