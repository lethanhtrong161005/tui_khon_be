package com.tuikhon.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Objects;

/**
 * Constraint validator implementation verifying that a given value matches one of the declared constants of an Enum class.
 */
public class EnumValueValidator implements ConstraintValidator<EnumValue, Object> {

    private Class<? extends Enum<?>> enumClass;

    @Override
    public void initialize(EnumValue constraintAnnotation) {
        this.enumClass = constraintAnnotation.enumClass();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (Objects.isNull(value)) {
            return true; // Allow null; use @RequireField if mandatory
        }

        if (value instanceof Enum<?>) {
            return Arrays.stream(enumClass.getEnumConstants())
                    .anyMatch(e -> e.equals(value));
        }

        if (value instanceof String strVal) {
            if (strVal.isBlank()) {
                return true;
            }
            return Arrays.stream(enumClass.getEnumConstants())
                    .anyMatch(e -> e.name().equalsIgnoreCase(strVal));
        }

        return false;
    }
}
