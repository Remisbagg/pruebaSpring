package com.olympus.dto.request;

import com.olympus.validator.annotation.resetPasswordToken.ExistResetPasswordToken;
import com.olympus.validator.annotation.user.ExistEmail;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema
@ExistResetPasswordToken
public class AccountPasswordResetToken {
    @Schema(example = "user@email")
    @NotBlank(message = "The email is required")
    @Email(message = "The email is an invalid email")
    @ExistEmail
    private String email;
    @Schema(example = "@2134ds%&*?J")
    @NotBlank(message = "Token is invalid")
    private String token;
}
