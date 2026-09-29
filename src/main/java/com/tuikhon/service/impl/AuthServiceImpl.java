package com.tuikhon.service.impl;

import com.tuikhon.constant.AppConstant;
import com.tuikhon.constant.MessageConstant;
import com.tuikhon.dto.request.auth.ChangePasswordRequest;
import com.tuikhon.dto.request.auth.LoginRequest;
import com.tuikhon.dto.request.auth.LogoutRequest;
import com.tuikhon.dto.request.auth.RefreshTokenRequest;
import com.tuikhon.dto.request.auth.RegisterRequest;
import com.tuikhon.dto.request.auth.UpdateProfileRequest;
import com.tuikhon.dto.response.auth.TokenResponse;
import com.tuikhon.dto.response.auth.UserProfileResponse;
import com.tuikhon.entity.UserEntity;
import com.tuikhon.enums.RoleName;
import com.tuikhon.enums.UserStatus;
import com.tuikhon.exception.HttpException;
import com.tuikhon.helper.UserHelper;
import com.tuikhon.repository.UserRepository;
import com.tuikhon.security.JwtProvider;
import com.tuikhon.service.AuthService;
import com.tuikhon.service.RedisService;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Enterprise Implementation of {@link AuthService} featuring direct JWT authentication,
 * Token Family Rotation, and JTI Blacklist Logout.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final RedisService redisService;
    private final UserHelper userHelper;

    @Override
    @Transactional(readOnly = true)
    public TokenResponse login(LoginRequest request) {
        UserEntity user = userRepository.findByEmailAndIsDeletedFalse(request.getEmail())
                .orElseThrow(() -> new HttpException(
                        HttpStatus.UNAUTHORIZED,
                        MessageConstant.INVALID_CREDENTIALS
                ));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new HttpException(
                    HttpStatus.UNAUTHORIZED,
                    MessageConstant.INVALID_CREDENTIALS
            );
        }

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new HttpException(
                    HttpStatus.FORBIDDEN,
                    MessageConstant.ACCESS_DENIED
            );
        }

        // Generate new Token Family ID
        String familyId = UUID.randomUUID().toString();

        String accessToken = jwtProvider.generateAccessToken(user, familyId);
        String refreshToken = jwtProvider.generateRefreshToken(user, familyId);

        // Register active refresh token in Redis under the token family
        String jtiRt = jwtProvider.getJtiFromToken(refreshToken);
        redisService.set(
                AppConstant.RT_FAMILY_PREFIX + familyId + ":" + jtiRt,
                "ACTIVE",
                AppConstant.REFRESH_FAMILY_TTL_DAYS,
                TimeUnit.DAYS
        );

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    @Transactional
    public UserProfileResponse register(RegisterRequest request) {
        if (userRepository.existsByEmailAndIsDeletedFalse(request.getEmail())) {
            throw new HttpException(
                    HttpStatus.BAD_REQUEST,
                    MessageConstant.USER_ALREADY_EXISTS
            );
        }

        UserEntity user = UserEntity.builder()
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .displayName(request.getDisplayName())
                .phoneNumber(request.getPhoneNumber())
                .role(RoleName.USER)
                .status(UserStatus.ACTIVE)
                .build();

        user = userRepository.save(user);
        return userHelper.mapToUserProfileResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public TokenResponse refreshToken(RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();
        if (!jwtProvider.validateToken(refreshToken)) {
            throw new HttpException(
                    HttpStatus.UNAUTHORIZED,
                    MessageConstant.TOKEN_INVALID
            );
        }

        UUID userId = jwtProvider.getUserIdFromToken(refreshToken);
        String familyId = jwtProvider.getFamilyIdFromToken(refreshToken);
        String jtiRt = jwtProvider.getJtiFromToken(refreshToken);

        // 1. Check if token family was revoked
        if (redisService.hasKey(AppConstant.RT_REVOKED_FAMILY_PREFIX + familyId)) {
            log.warn("[Token Theft] Attempt to use token from revoked family: {}", familyId);
            throw new HttpException(
                    HttpStatus.UNAUTHORIZED,
                    MessageConstant.TOKEN_REVOKED
            );
        }

        // 2. Check if this specific refresh token JTI is active
        String familyKey = AppConstant.RT_FAMILY_PREFIX + familyId + ":" + jtiRt;
        if (!redisService.hasKey(familyKey)) {
            // Replay attack / Reuse of already rotated refresh token! Revoke entire family!
            log.error("[Token Theft Detected] Refresh token JTI {} already spent. Revoking family {}", jtiRt, familyId);
            redisService.set(AppConstant.RT_REVOKED_FAMILY_PREFIX + familyId, "REVOKED", AppConstant.REFRESH_FAMILY_TTL_DAYS, TimeUnit.DAYS);

            throw new HttpException(
                    HttpStatus.UNAUTHORIZED,
                    MessageConstant.TOKEN_REVOKED
            );
        }

        // 3. Token is valid — invalidate spent JTI
        redisService.delete(familyKey);

        UserEntity user = userRepository.findByUserIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new HttpException(
                        HttpStatus.NOT_FOUND,
                        MessageConstant.USER_NOT_FOUND
                ));

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new HttpException(
                    HttpStatus.FORBIDDEN,
                    MessageConstant.ACCESS_DENIED
            );
        }

        // 4. Rotate tokens: Keep same familyId, generate new JTI for AT and RT
        String newAccessToken = jwtProvider.generateAccessToken(user, familyId);
        String newRefreshToken = jwtProvider.generateRefreshToken(user, familyId);

        String newJtiRt = jwtProvider.getJtiFromToken(newRefreshToken);
        redisService.set(
                AppConstant.RT_FAMILY_PREFIX + familyId + ":" + newJtiRt,
                "ACTIVE",
                AppConstant.REFRESH_FAMILY_TTL_DAYS,
                TimeUnit.DAYS
        );

        return TokenResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    @Override
    public void logout(String authHeader, LogoutRequest logoutRequest) {
        // 1. Blacklist Access Token via JTI
        if (Objects.nonNull(authHeader) && authHeader.startsWith("Bearer ")) {
            String accessToken = authHeader.substring(7);
            if (jwtProvider.validateToken(accessToken)) {
                String jtiAt = jwtProvider.getJtiFromToken(accessToken);
                long remainingMs = jwtProvider.getRemainingExpirationMs(accessToken);
                if (remainingMs > 0 && Objects.nonNull(jtiAt)) {
                    redisService.set(AppConstant.BLACKLIST_JTI_PREFIX + jtiAt, "REVOKED", remainingMs, TimeUnit.MILLISECONDS);
                    log.info("[Logout] Blacklisted Access Token JTI [{}] for {} ms", jtiAt, remainingMs);
                }
            }
        }

        // 2. Revoke Refresh Token & Token Family
        if (Objects.nonNull(logoutRequest) && Objects.nonNull(logoutRequest.getRefreshToken())) {
            String refreshToken = logoutRequest.getRefreshToken();
            if (jwtProvider.validateToken(refreshToken)) {
                String familyId = jwtProvider.getFamilyIdFromToken(refreshToken);
                String jtiRt = jwtProvider.getJtiFromToken(refreshToken);

                if (Objects.nonNull(familyId)) {
                    redisService.set(AppConstant.RT_REVOKED_FAMILY_PREFIX + familyId, "REVOKED", AppConstant.REFRESH_FAMILY_TTL_DAYS, TimeUnit.DAYS);
                    if (Objects.nonNull(jtiRt)) {
                        redisService.delete(AppConstant.RT_FAMILY_PREFIX + familyId + ":" + jtiRt);
                    }
                    log.info("[Logout] Revoked Token Family [{}] and Refresh Token JTI [{}]", familyId, jtiRt);
                }
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public UserProfileResponse getCurrentUserProfile() {
        UserEntity user = getAuthenticatedUser();
        return userHelper.mapToUserProfileResponse(user);
    }

    @Override
    @Transactional
    public UserProfileResponse updateProfile(UpdateProfileRequest request) {
        UserEntity user = getAuthenticatedUser();

        user.setDisplayName(request.getDisplayName());
        if (Objects.nonNull(request.getAvatarUrl())) {
            user.setAvatarUrl(request.getAvatarUrl());
        }
        if (Objects.nonNull(request.getPhoneNumber())) {
            user.setPhoneNumber(request.getPhoneNumber());
        }

        user = userRepository.save(user);
        return userHelper.mapToUserProfileResponse(user);
    }

    @Override
    @Transactional
    public void changePassword(ChangePasswordRequest request) {
        UserEntity user = getAuthenticatedUser();

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPasswordHash())) {
            throw new HttpException(HttpStatus.BAD_REQUEST, MessageConstant.OLD_PASSWORD_INCORRECT);
        }

        if (!Objects.equals(request.getNewPassword(), request.getConfirmPassword())) {
            throw new HttpException(HttpStatus.BAD_REQUEST, MessageConstant.PASSWORD_CONFIRMATION_MISMATCH);
        }

        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    private UserEntity getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.isNull(authentication) || !(authentication.getPrincipal() instanceof UserEntity principal)) {
            throw new HttpException(
                    HttpStatus.UNAUTHORIZED,
                    MessageConstant.UNAUTHORIZED_ACCESS
            );
        }

        return userRepository.findById(principal.getUserId())
                .orElseThrow(() -> new HttpException(
                        HttpStatus.NOT_FOUND,
                        MessageConstant.USER_NOT_FOUND
                ));
    }
}
