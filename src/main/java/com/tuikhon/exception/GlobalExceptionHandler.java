package com.tuikhon.exception;

import com.tuikhon.constant.MessageConstant;
import com.tuikhon.dto.response.ApiResponse;
import com.tuikhon.util.ResponseUtils;
import com.tuikhon.validation.EnumValue;
import com.tuikhon.validation.RequireField;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler providing centralized error responses across all REST controllers.
 * Processes custom {@link HttpException}, validation annotations ({@link RequireField}, {@link EnumValue}),
 * and unhandled system exceptions into standardized {@link ApiResponse} envelopes.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles custom application {@link HttpException}.
     *
     * @param ex the HttpException instance
     * @return ResponseEntity with standardized ApiResponse
     */
    @ExceptionHandler(HttpException.class)
    public ResponseEntity<ApiResponse<Object>> handleHttpException(HttpException ex) {
        logger.error("HttpException occurred: status={}, message={}", ex.getStatusCode(), ex.getMessage());
        HttpStatus status = HttpStatus.resolve(ex.getStatusCode());
        if (Objects.isNull(status)) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return ResponseUtils.error(status, ex.getMessage());
    }

    /**
     * Handles validation errors thrown during @Valid DTO request body processing.
     * Extracts {@link RequireField} and {@link EnumValue} annotations to build validation messages.
     *
     * @param ex the MethodArgumentNotValidException instance
     * @return ResponseEntity with standardized ApiResponse
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<List<String>>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex) {
        logger.error("MethodArgumentNotValidException occurred: {}", ex.getMessage());

        List<String> errors = new ArrayList<>();

        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            try {
                ConstraintViolation<?> violation = error.unwrap(ConstraintViolation.class);
                if (Objects.nonNull(violation) && Objects.nonNull(violation.getConstraintDescriptor())) {
                    var annotation = violation.getConstraintDescriptor().getAnnotation();
                    if (annotation instanceof RequireField requireField) {
                        String fieldName = !requireField.field().isBlank()
                                ? requireField.field()
                                : violation.getPropertyPath().toString();
                        errors.add(String.format(MessageConstant.FIELD_REQUIRED, fieldName));
                        continue;
                    } else if (annotation instanceof EnumValue enumValue) {
                        String fieldName = !enumValue.field().isBlank()
                                ? enumValue.field()
                                : violation.getPropertyPath().toString();
                        errors.add(String.format(MessageConstant.FIELD_INVALID, fieldName));
                        continue;
                    }
                }
            } catch (Exception ignored) {
                // Fallback for non-unwrap violations
            }

            String defaultMsg = error.getDefaultMessage();
            if (Objects.nonNull(defaultMsg) && !defaultMsg.isBlank()) {
                errors.add(defaultMsg);
            }
        }

        return ResponseUtils.error(HttpStatus.BAD_REQUEST, MessageConstant.VALIDATION_FAILED, errors);
    }

    /**
     * Handles JSR-380 {@link ConstraintViolationException} for path variables and request params.
     *
     * @param ex the ConstraintViolationException instance
     * @return ResponseEntity with standardized ApiResponse
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<List<String>>> handleConstraintViolationException(
            ConstraintViolationException ex) {
        logger.error("ConstraintViolationException occurred: {}", ex.getMessage());

        List<String> errors = new ArrayList<>();

        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            if (Objects.nonNull(violation.getConstraintDescriptor())) {
                var annotation = violation.getConstraintDescriptor().getAnnotation();
                if (annotation instanceof RequireField requireField) {
                    String fieldName = !requireField.field().isBlank()
                            ? requireField.field()
                            : violation.getPropertyPath().toString();
                    errors.add(String.format(MessageConstant.FIELD_REQUIRED, fieldName));
                } else if (annotation instanceof EnumValue enumValue) {
                    String fieldName = !enumValue.field().isBlank()
                            ? enumValue.field()
                            : violation.getPropertyPath().toString();
                    errors.add(String.format(MessageConstant.FIELD_INVALID, fieldName));
                } else {
                    errors.add(violation.getMessage());
                }
            }
        }

        return ResponseUtils.error(HttpStatus.BAD_REQUEST, MessageConstant.VALIDATION_FAILED, errors);
    }

    /**
     * Fallback handler for unhandled exceptions.
     *
     * @param ex the Exception instance
     * @return ResponseEntity with standardized ApiResponse
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGenericException(Exception ex) {
        logger.error("Unhandled exception: ", ex);
        return ResponseUtils.error(HttpStatus.INTERNAL_SERVER_ERROR, MessageConstant.INTERNAL_SERVER_ERROR);
    }
}
