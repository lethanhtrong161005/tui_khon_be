package com.tuikhon.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom constraint annotation checking that a string field is non-null and non-blank.
 */
@Documented
@Constraint(validatedBy = RequireFieldValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireField {

    /**
     * Field display name for message formatting.
     *
     * @return field name string
     */
    String field() default "";

    /**
     * Validation message template.
     *
     * @return message string
     */
    String message() default "";

    /**
     * Validation groups payload.
     *
     * @return group classes
     */
    Class<?>[] groups() default {};

    /**
     * Payload associated with the constraint.
     *
     * @return payload classes
     */
    Class<? extends Payload>[] payload() default {};
}
