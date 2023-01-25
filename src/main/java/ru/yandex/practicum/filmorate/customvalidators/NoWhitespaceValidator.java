package ru.yandex.practicum.filmorate.customvalidators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NoWhitespaceValidator implements ConstraintValidator<NoWhitespaceValidation, String> {

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        String[] array = s.split(" ");
        return array.length == 1;
    }

    @Override
    public void initialize(NoWhitespaceValidation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
}
