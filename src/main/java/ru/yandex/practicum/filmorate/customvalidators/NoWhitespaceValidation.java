package ru.yandex.practicum.filmorate.customvalidators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NoWhitespaceValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface NoWhitespaceValidation {

    String message() default "В логине есть пробелы";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
