package com.tuikhon.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Objects;

/**
 * Constraint validator implementation enforcing non-null and non-blank rules for {@link RequireField}.
 */
public class RequireFieldValidator implements ConstraintValidator<RequireField, String> {

    /**
     * Validates whether string is non-null and non-blank using java.util.Objects.
     *
     * @param value   string value to validate
     * @param context validator context
     * @return true if valid, false otherwise
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (Objects.isNull(value)) {
            return false;
        }
        return !value.isBlank();
    }
}
