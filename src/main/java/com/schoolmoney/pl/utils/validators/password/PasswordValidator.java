package com.schoolmoney.pl.utils.validators.password;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordValidator implements ConstraintValidator<PasswordConstraint, String> {
    private static final int MIN_LENGTH = 8;
    private static final String DIGIT_PATTERN = ".*\\d.*";
    private static final String UPPERCASE_PATTERN = ".*[A-Z].*";
    private static final String SYMBOL_PATTERN = ".*[@#$%^!.*&+=~-].*";

    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {

        if (password == null || password.length() < MIN_LENGTH)
            return false;

        Pattern digitPattern = Pattern.compile(DIGIT_PATTERN);
        Pattern uppercasePattern = Pattern.compile(UPPERCASE_PATTERN);
        Pattern symbolPattern = Pattern.compile(SYMBOL_PATTERN);

        Matcher digitMatcher = digitPattern.matcher(password);
        Matcher uppercaseMatcher = uppercasePattern.matcher(password);
        Matcher symbolMatcher = symbolPattern.matcher(password);

        return digitMatcher.matches() && uppercaseMatcher.matches() && symbolMatcher.matches();
    }
}
