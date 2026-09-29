package com.tuikhon.dto.response.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response payload containing issued JWT Access Token and Refresh Token.
 */
@Schema(description = "Response payload containing JWT authentication tokens")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponse {

    /**
     * JWT Access Token for authenticating protected API endpoints.
     */
    @Schema(description = "JWT Access Token string", example = "eyJhbGciOiJIUzI1NiJ9...")
    private String accessToken;

    /**
     * JWT Refresh Token for renewing Access Tokens.
     */
    @Schema(description = "JWT Refresh Token string", example = "eyJhbGciOiJIUzI1NiJ9...")
    private String refreshToken;
}
