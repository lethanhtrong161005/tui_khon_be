package com.tuikhon.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response payload for system health check endpoint.
 */
@Schema(description = "System health check status response")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HealthCheckResponse {

    /**
     * Overall health status string (e.g., UP, DOWN).
     */
    @Schema(description = "Application status string", example = "UP")
    private String status;

    /**
     * System current timestamp.
     */
    @Schema(description = "System timestamp", example = "2026-09-29T19:30:00")
    private LocalDateTime timestamp;

    /**
     * Application name.
     */
    @Schema(description = "Application service name", example = "Tui Khon Backend")
    private String applicationName;
}
