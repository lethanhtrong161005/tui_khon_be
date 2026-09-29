package com.tuikhon.helper;

import com.tuikhon.dto.response.auth.UserProfileResponse;
import com.tuikhon.entity.UserEntity;
import java.util.Objects;
import org.springframework.stereotype.Component;

/**
 * Helper component for mapping {@link UserEntity} to DTO responses.
 */
@Component
public class UserHelper {

    /**
     * Maps {@link UserEntity} to {@link UserProfileResponse}.
     *
     * @param user UserEntity instance.
     * @return UserProfileResponse instance or null if input user is null.
     */
    public UserProfileResponse mapToUserProfileResponse(UserEntity user) {
        if (Objects.isNull(user)) {
            return null;
        }

        return UserProfileResponse.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .displayName(user.getDisplayName())
                .avatarUrl(user.getAvatarUrl())
                .phoneNumber(user.getPhoneNumber())
                .status(user.getStatus())
                .role(user.getRole())
                .build();
    }
}
