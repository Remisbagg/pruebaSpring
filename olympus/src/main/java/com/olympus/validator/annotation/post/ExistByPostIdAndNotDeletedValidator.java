package com.olympus.validator.annotation.post;

import com.olympus.service.IPostService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class ExistByPostIdAndNotDeletedValidator implements ConstraintValidator<ExistByPostIdAndNotDeleted, Long> {
    private final IPostService postService;

    @Autowired
    public ExistByPostIdAndNotDeletedValidator(IPostService postService) {
        this.postService = postService;
    }

    @Override
    public boolean isValid(Long id, ConstraintValidatorContext constraintValidatorContext) {
        try {
            return id!= null && postService.existByPostIdAndNotDeleted(id);
        } catch (Exception e) {
            return false;
        }
    }
}
