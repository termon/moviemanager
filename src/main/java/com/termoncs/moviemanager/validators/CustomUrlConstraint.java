package com.termoncs.moviemanager.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CustomUrlValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomUrlConstraint {
    String message() default "Invalid or non-existant url";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
