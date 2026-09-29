package com.tuikhon.util;

import com.tuikhon.constant.AppConstant;
import com.tuikhon.constant.MessageConstant;
import com.tuikhon.dto.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.Objects;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Utility class for constructing standardized {@link ApiResponse} envelopes.
 * Supports automatic trace ID and request path injection.
 */
public final class ResponseUtils {

    private ResponseUtils() {
        // Utility class
    }

    /**
     * Builds standard ApiResponse envelope with status, message, traceId, path, and data.
     *
     * @param <T>        data payload type
     * @param status     HTTP status integer
     * @param message    human-readable message
     * @param data       data payload
     * @param customPath optional custom request path
     * @return constructed ApiResponse
     */
    private static <T> ApiResponse<T> buildEnvelope(
            int status, String message, T data, String customPath) {
        String path = (Objects.nonNull(customPath) && !customPath.isBlank())
                ? customPath
                : getCurrentPath();

        return ApiResponse.<T>builder()
                .status(status)
                .message(message)
                .traceId(getTraceId())
                .path(path)
                .data(data)
                .timestamp(Instant.now())
                .build();
    }

    /**
     * Constructs a successful ResponseEntity with payload and a custom message.
     *
     * @param <T>     data payload type
     * @param data    payload object
     * @param message custom success message
     * @return ResponseEntity containing ApiResponse envelope
     */
    public static <T> ResponseEntity<ApiResponse<T>> successWithData(T data, String message) {
        return ResponseEntity.ok(buildEnvelope(HttpStatus.OK.value(), message, data, null));
    }

    /**
     * Constructs a successful ResponseEntity with payload and default success message.
     *
     * @param <T>  data payload type
     * @param data payload object
     * @return ResponseEntity containing ApiResponse envelope
     */
    public static <T> ResponseEntity<ApiResponse<T>> successWithData(T data) {
        return successWithData(data, MessageConstant.OPERATION_SUCCESSFUL);
    }

    /**
     * Constructs a successful ResponseEntity without payload using a custom message.
     *
     * @param <T>     data payload type
     * @param message custom success message
     * @return ResponseEntity containing ApiResponse envelope
     */
    public static <T> ResponseEntity<ApiResponse<T>> success(String message) {
        return successWithData(null, message);
    }

    /**
     * Constructs a successful ResponseEntity without payload and default success message.
     *
     * @param <T> data payload type
     * @return ResponseEntity containing ApiResponse envelope
     */
    public static <T> ResponseEntity<ApiResponse<T>> success() {
        return success(MessageConstant.OPERATION_SUCCESSFUL);
    }

    /**
     * Constructs a 201 Created ResponseEntity with payload and custom message.
     *
     * @param <T>     data payload type
     * @param data    payload object
     * @param message custom success message
     * @return ResponseEntity containing ApiResponse envelope
     */
    public static <T> ResponseEntity<ApiResponse<T>> created(T data, String message) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(buildEnvelope(HttpStatus.CREATED.value(), message, data, null));
    }

    /**
     * Constructs an error ResponseEntity with HTTP status and message.
     *
     * @param <T>     data payload type
     * @param status  HTTP status code
     * @param message error message
     * @return ResponseEntity containing ApiResponse envelope
     */
    public static <T> ResponseEntity<ApiResponse<T>> error(HttpStatus status, String message) {
        return error(status, message, (T) null, null);
    }

    /**
     * Constructs an error ResponseEntity with HTTP status, message, and error details data.
     *
     * @param <T>     data payload type
     * @param status  HTTP status code
     * @param message error message
     * @param data    error details data
     * @return ResponseEntity containing ApiResponse envelope
     */
    public static <T> ResponseEntity<ApiResponse<T>> error(HttpStatus status, String message, T data) {
        return error(status, message, data, null);
    }

    /**
     * Constructs an error ResponseEntity with HTTP status, message, and explicit path.
     *
     * @param <T>        data payload type
     * @param status     HTTP status code
     * @param message    error message
     * @param customPath request path string
     * @return ResponseEntity containing ApiResponse envelope
     */
    public static <T> ResponseEntity<ApiResponse<T>> error(
            HttpStatus status, String message, String customPath) {
        return error(status, message, (T) null, customPath);
    }

    /**
     * Constructs an error ResponseEntity with HTTP status, message, error details data, and explicit path.
     *
     * @param <T>        data payload type
     * @param status     HTTP status code
     * @param message    error message
     * @param data       error details data
     * @param customPath request path string
     * @return ResponseEntity containing ApiResponse envelope
     */
    public static <T> ResponseEntity<ApiResponse<T>> error(
            HttpStatus status, String message, T data, String customPath) {
        return ResponseEntity.status(status)
                .body(buildEnvelope(status.value(), message, data, customPath));
    }

    /**
     * Returns current MDC trace ID.
     *
     * @return trace ID string
     */
    public static String getTraceId() {
        String traceId = MDC.get(AppConstant.TRACE_ID_KEY);
        return Objects.nonNull(traceId) ? traceId : "";
    }

    /**
     * Returns current HttpServletRequest URI path from RequestContextHolder.
     *
     * @return request URI path string or empty string if not in web context
     */
    public static String getCurrentPath() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes instanceof ServletRequestAttributes servletRequestAttributes) {
            HttpServletRequest request = servletRequestAttributes.getRequest();
            return request.getRequestURI();
        }
        return "";
    }
}
