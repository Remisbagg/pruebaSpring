package com.olympus.dto.request;

import com.olympus.validator.annotation.user.UniqueEmail;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema
public class AccountRegister {
    @NotBlank(message = "The email is required")
    @Email(message = "The email is an invalid email")
    @UniqueEmail
    @Schema(example = "user@email")
    private String email;
    @NotBlank(message = "The password is required")
    @Size(min = 6, message = "The password must be at least 6 characters")
    @Schema(example = "123456")
    private String password;
}
