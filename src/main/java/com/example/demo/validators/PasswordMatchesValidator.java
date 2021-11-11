package com.example.demo.validators;

import com.example.demo.annotations.PasswordMatches;
import com.example.demo.payload.requests.SignupRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {
    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext) {
        SignupRequest signupRequest = (SignupRequest) object;

        return signupRequest.getPassword()
                .equals(signupRequest.getConfirmPassword());
    }
}
