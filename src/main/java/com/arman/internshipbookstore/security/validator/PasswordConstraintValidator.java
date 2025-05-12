package com.arman.internshipbookstore.security.validator;

import com.arman.internshipbookstore.security.annotation.ValidPassword;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {
    private static final String PASSWORD_PATTERN =
            "(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}";


    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if(password==null){
            return false;
        }
        return password.matches(PASSWORD_PATTERN);
    }
}
