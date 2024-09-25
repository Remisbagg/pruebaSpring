package com.olympus.validator.annotation.friendRequest;

import com.olympus.service.IFriendRequestService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class ExistIdValidator implements ConstraintValidator<ExistById, Long> {
    private final IFriendRequestService friendRequestService;

    @Autowired
    public ExistIdValidator(IFriendRequestService friendRequestService) {
        this.friendRequestService = friendRequestService;
    }

    @Override
    public boolean isValid(Long id, ConstraintValidatorContext constraintValidatorContext) {
        try{
            return id!= null && friendRequestService.existByRequestId(id);
        } catch (Exception ex) {
            return false;
        }
    }
}
