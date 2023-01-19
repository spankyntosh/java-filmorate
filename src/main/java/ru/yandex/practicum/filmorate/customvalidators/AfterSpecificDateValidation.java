package ru.yandex.practicum.filmorate.customvalidators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AfterSpecificDateValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface AfterSpecificDateValidation {
    String message() default "Введена очень старая дата";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
