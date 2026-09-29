package com.tuikhon.dto.request.auth;

import com.tuikhon.validation.RequireField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request payload for logout containing the Refresh Token to revoke.
 */
@Schema(description = "Request payload for account logout and refresh token revocation")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogoutRequest {

    /**
     * Refresh Token associated with active user session.
     */
    @Schema(description = "Refresh token to be revoked", example = "eyJhbGciOiJIUzI1NiJ9...")
    @RequireField(field = "Refresh Token")
    private String refreshToken;
}
