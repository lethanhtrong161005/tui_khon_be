package com.tuikhon.controller;

import com.tuikhon.constant.MessageConstant;
import com.tuikhon.dto.response.ApiResponse;
import com.tuikhon.dto.response.HealthCheckResponse;
import com.tuikhon.util.ResponseUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller exposing system health check endpoints.
 */
@Tag(name = "Health", description = "System Health and Monitoring API")
@RestController
@RequestMapping("/api/v1/health")
public class HealthController {

    @Value("${spring.application.name:tui_khon_be}")
    private String applicationName;

    /**
     * Returns current application health status and timestamp.
     *
     * @return ResponseEntity envelope containing HealthCheckResponse.
     */
    @Operation(summary = "Get application health status")
    @GetMapping
    public ResponseEntity<ApiResponse<HealthCheckResponse>> checkHealth() {
        HealthCheckResponse response = HealthCheckResponse.builder()
                .status("UP")
                .timestamp(LocalDateTime.now())
                .applicationName(applicationName)
                .build();

        return ResponseUtils.successWithData(response, MessageConstant.OPERATION_SUCCESSFUL);
    }
}
