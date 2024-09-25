package com.olympus.validator.annotation.user;

import com.olympus.service.IUserService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class ExistUserIdValidator implements ConstraintValidator<ExistUserById, Long> {
    private final IUserService userService;

    @Autowired
    public ExistUserIdValidator(IUserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean isValid(Long id, ConstraintValidatorContext constraintValidatorContext) {
        try {
            return  id!= null && userService.existByUserId(id);
        } catch (Exception e) {
            return false;
        }
    }
}
