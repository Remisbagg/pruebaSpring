package com.olympus.validator.annotation.user;

import com.olympus.dto.request.AccountLogin;
import com.olympus.service.IUserService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class ExistByEmailAndPassValidator implements ConstraintValidator<ExistByEmailAndPass, AccountLogin> {
    private final IUserService userService;

    @Autowired
    public ExistByEmailAndPassValidator(IUserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean isValid(AccountLogin accountLogin, ConstraintValidatorContext constraintValidatorContext) {
        try {
            return userService.existsUserByEmailAndPassword(accountLogin);
        } catch (Exception e) {
            return false;
        }
    }
}
