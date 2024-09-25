package com.olympus.validator.annotation.resetPasswordToken;

import com.olympus.dto.request.AccountPasswordResetToken;
import com.olympus.service.IResetPwdTokenService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class ExistResetPasswordTokenValidator implements ConstraintValidator<ExistResetPasswordToken, AccountPasswordResetToken> {
    private final IResetPwdTokenService resetPwdTokenService;

    @Autowired
    public ExistResetPasswordTokenValidator(IResetPwdTokenService resetPwdTokenService) {
        this.resetPwdTokenService = resetPwdTokenService;
    }

    @Override
    public boolean isValid(AccountPasswordResetToken resetPwdToken, ConstraintValidatorContext constraintValidatorContext) {
        try {
            return resetPwdTokenService.existByTokenAndEmail(resetPwdToken.getToken(), resetPwdToken.getEmail());
        } catch (Exception e) {
            return false;
        }
    }
}
