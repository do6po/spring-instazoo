package com.example.demo.validators;

import com.example.demo.annotations.ValidEmail;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class EmailValidator implements ConstraintValidator<ValidEmail, String> {
    @Override
    public void initialize(ValidEmail constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        return Pattern
                .compile("^[\\w\\d]+@[\\w\\d]+\\.[\\w\\d]+$")
                .matcher(email)
                .matches();
    }
}
