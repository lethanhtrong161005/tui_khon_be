package com.tuikhon.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Standardized API Response envelope wrapping response data, metadata, status, message, traceId, and path.
 *
 * @param <T> Response payload data type.
 */
@Schema(description = "Standardized envelope response wrapper for all REST API endpoints")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    /**
     * HTTP status code (e.g., 200, 400, 401, 500).
     */
    @Schema(description = "HTTP status code", example = "200")
    private int status;

    /**
     * Human-readable result message.
     */
    @Schema(description = "Human-readable result message", example = "Thao tác thành công")
    private String message;

    /**
     * Unique request tracing identifier.
     */
    @Schema(description = "Unique trace ID for request tracking", example = "c8f94e96-6e47-4f68-9844-8da8fb6bf9c4")
    private String traceId;

    /**
     * Request URI path.
     */
    @Schema(description = "Request URI path", example = "/api/v1/auth/login")
    private String path;

    /**
     * Response payload data object.
     */
    @Schema(description = "Main response payload data")
    private T data;

    /**
     * ISO-8601 response generation timestamp.
     */
    @Schema(description = "ISO-8601 timestamp when response was generated", example = "2026-09-29T12:00:00Z")
    @Builder.Default
    private Instant timestamp = Instant.now();
}
