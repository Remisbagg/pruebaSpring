package com.olympus.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema
public class AuthRequest {
    @Schema(example = "user@email")
    private String email;
    @Schema(example = "123456")
    private String code;
}
