package me.r1411.askapi.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueUsernameValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueUsernameConstraint {
    String message() default "This username is already taken";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
