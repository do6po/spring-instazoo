package com.example.demo.validators;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.HashMap;

@Service
public class ResponseErrorValidation {
    public ResponseEntity<Object> mapValidationService(BindingResult result) {
        if (!result.hasErrors()) {
            return null;
        }

        var errorMap = new HashMap<String, String>();

        if (!result.getAllErrors().isEmpty()) {
            for (ObjectError error : result.getAllErrors()) {
                errorMap.put(error.getCode(), error.getDefaultMessage());
            }
        }

        for (FieldError error : result.getFieldErrors()) {
            errorMap.put(error.getField(), error.getDefaultMessage());
        }

        return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
    }
}
