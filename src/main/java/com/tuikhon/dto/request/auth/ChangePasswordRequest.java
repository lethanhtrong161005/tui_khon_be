package com.tuikhon.dto.request.auth;

import com.tuikhon.validation.RequireField;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request payload for changing user password.
 */
@Schema(description = "Request payload for changing user password")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordRequest {

    /**
     * Current user password.
     */
    @Schema(description = "Current password", example = "OldPass123!")
    @RequireField(field = "Mật khẩu cũ")
    private String oldPassword;

    /**
     * New user password.
     */
    @Schema(description = "New password", example = "NewPass123!")
    @RequireField(field = "Mật khẩu mới")
    @Size(min = 6, message = "Mật khẩu mới phải có ít nhất 6 ký tự")
    private String newPassword;

    /**
     * Confirmation of new user password.
     */
    @Schema(description = "Confirmation password", example = "NewPass123!")
    @RequireField(field = "Xác nhận mật khẩu mới")
    private String confirmPassword;
}
