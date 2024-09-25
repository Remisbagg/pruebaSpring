package com.olympus.dto.request;

import com.olympus.validator.annotation.user.ExistEmail;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Schema
@Data
public class AccountPasswordReset {
    @NotBlank(message = "The email is required")
    @Email(message = "The email is an invalid email")
    @ExistEmail
    @Schema(example = "user@email")
    private String email;
    @NotBlank(message = "The password is required")
    @Size(min = 6, message = "The password must be at least 6 characters")
    @Schema(example = "12345")
    private String password;
}
