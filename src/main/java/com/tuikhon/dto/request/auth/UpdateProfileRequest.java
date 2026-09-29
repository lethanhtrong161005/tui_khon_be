package com.tuikhon.dto.request.auth;

import com.tuikhon.validation.RequireField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request payload for updating user profile.
 */
@Schema(description = "Request payload for updating user profile")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileRequest {

    /**
     * User's updated display name.
     */
    @Schema(description = "User display name", example = "Nguyễn Văn B")
    @RequireField(field = "Tên hiển thị")
    private String displayName;

    /**
     * User's avatar URL.
     */
    @Schema(description = "Avatar image URL", example = "https://example.com/new-avatar.jpg")
    private String avatarUrl;

    /**
     * User's phone number.
     */
    @Schema(description = "Phone number", example = "0987654321")
    private String phoneNumber;
}
