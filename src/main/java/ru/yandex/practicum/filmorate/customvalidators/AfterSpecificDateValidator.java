package ru.yandex.practicum.filmorate.customvalidators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class AfterSpecificDateValidator implements ConstraintValidator<AfterSpecificDateValidation, LocalDate> {

    private String message;

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {

        boolean isValid;
        LocalDate birthOfCinemaDate = LocalDate.of(1895, 12, 28);

        isValid = localDate.isAfter(birthOfCinemaDate);
        if (!isValid) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext
                    .buildConstraintViolationWithTemplate(message)
                    .addConstraintViolation();
        }

        return isValid;
    }

    @Override
    public void initialize(AfterSpecificDateValidation constraintAnnotation) {
        //ConstraintValidator.super.initialize(constraintAnnotation);
        message = constraintAnnotation.message();
    }
}
