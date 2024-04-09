package com.example.movie.Validator.annotation;

import com.example.demo.Validator.annotation.ValidUserId;
import com.example.movie.Entity.User;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidUserIdValidator implements ConstraintValidator<com.example.demo.Validator.annotation.ValidUserId, User> {
    @Override
    public boolean isValid(User user, ConstraintValidatorContext context){
        if (user == null)
            return true;
        return user.getUserId() != null;
    }
}
