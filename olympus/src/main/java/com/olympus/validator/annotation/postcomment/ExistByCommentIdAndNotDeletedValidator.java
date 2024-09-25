package com.olympus.validator.annotation.postcomment;

import com.olympus.service.IPostCommentService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class ExistByCommentIdAndNotDeletedValidator implements ConstraintValidator<ExistByCommentIdAndNotDeleted, Long> {
    private final IPostCommentService postCommentService;

    @Autowired
    public ExistByCommentIdAndNotDeletedValidator(IPostCommentService postCommentService) {
        this.postCommentService = postCommentService;
    }

    @Override
    public boolean isValid(Long id, ConstraintValidatorContext constraintValidatorContext) {
        try {
            return id != null && postCommentService.existByIdAndNotDeleted(id);
        } catch (Exception e) {
            return false;
        }
    }
}
