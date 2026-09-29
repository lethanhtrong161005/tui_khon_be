package com.tuikhon.dto.response.auth;

import com.tuikhon.enums.RoleName;
import com.tuikhon.enums.UserStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response payload representing profile information of the user.
 */
@Schema(description = "Response payload containing profile details of user")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponse {

    /**
     * Unique user account identifier.
     */
    @Schema(description = "User unique UUID", example = "b1c2d3e4-f5a6-7b8c-9d0e-1f2a3b4c5d6e")
    private UUID userId;

    /**
     * User's primary email address.
     */
    @Schema(description = "User email address", example = "user@tuikhon.vn")
    private String email;

    /**
     * User's display name.
     */
    @Schema(description = "User display name", example = "Nguyễn Văn A")
    private String displayName;

    /**
     * User's avatar URL.
     */
    @Schema(description = "User avatar URL", example = "https://example.com/avatar.jpg")
    private String avatarUrl;

    /**
     * User's registered phone number.
     */
    @Schema(description = "User phone number", example = "0987654321")
    private String phoneNumber;

    /**
     * User account status.
     */
    @Schema(description = "User account active status", example = "ACTIVE")
    private UserStatus status;

    /**
     * Assigned system role name.
     */
    @Schema(description = "User system role name", example = "USER")
    private RoleName role;
}
