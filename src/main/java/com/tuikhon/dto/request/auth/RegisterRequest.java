package com.tuikhon.dto.request.auth;

import com.tuikhon.validation.RequireField;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request payload for user registration.
 */
@Schema(description = "Request payload for user registration")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    /**
     * User's email address.
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
    @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự")
    private String password;

    /**
     * User's display name.
     */
    @Schema(description = "User display name", example = "Nguyễn Văn A")
    @RequireField(field = "Display name")
    private String displayName;

    /**
     * User's phone number.
     */
    @Schema(description = "User phone number", example = "0987654321")
    private String phoneNumber;
}
