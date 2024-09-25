package com.olympus.validator.annotation.resetPasswordToken;

import com.olympus.service.IResetPwdTokenService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class ValidResetPasswordTokenValidator implements ConstraintValidator<ValidResetPasswordToken, String> {
    private final IResetPwdTokenService resetPwdTokenService;

    @Autowired
    public ValidResetPasswordTokenValidator(IResetPwdTokenService resetPwdTokenService) {
        this.resetPwdTokenService = resetPwdTokenService;
    }

    @Override
    public boolean isValid(String token, ConstraintValidatorContext constraintValidatorContext) {
        try {
            return token != null && !token.isBlank() && resetPwdTokenService.existByToken(token);
        } catch (Exception e) {
            return false;
        }
    }
}
