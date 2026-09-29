package com.tuikhon.dto.request.auth;

import com.tuikhon.validation.RequireField;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request payload for login via email and password.
 */
@Schema(description = "Request payload for email and password login")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    /**
     * User's registered email address.
     */
    @Schema(description = "User registered email", example = "user@tuikhon.vn")
    @RequireField(field = "Email")
    @Email(message = "Email không hợp lệ")
    private String email;

    /**
     * User's plain text password.
     */
    @Schema(description = "User account password", example = "SecurePassword123!")
    @RequireField(field = "Password")
    private String password;
}
