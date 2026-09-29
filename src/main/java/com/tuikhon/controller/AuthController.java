package com.tuikhon.controller;

import com.tuikhon.constant.MessageConstant;
import com.tuikhon.dto.request.auth.ChangePasswordRequest;
import com.tuikhon.dto.request.auth.LoginRequest;
import com.tuikhon.dto.request.auth.LogoutRequest;
import com.tuikhon.dto.request.auth.RefreshTokenRequest;
import com.tuikhon.dto.request.auth.RegisterRequest;
import com.tuikhon.dto.request.auth.UpdateProfileRequest;
import com.tuikhon.dto.response.ApiResponse;
import com.tuikhon.dto.response.auth.TokenResponse;
import com.tuikhon.dto.response.auth.UserProfileResponse;
import com.tuikhon.service.AuthService;
import com.tuikhon.util.ResponseUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller exposing REST endpoints for Authentication, Token Rotation, Registration, Logout, and User Profile.
 */
@Tag(name = "Authentication", description = "Authentication, Token Refresh, and Session Management API")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * User login via email and password, returning accessToken and refreshToken.
     *
     * @param request LoginRequest payload containing email and password.
     * @return ResponseEntity envelope containing TokenResponse.
     */
    @Operation(summary = "Login via Email and Password, returns Access Token and Refresh Token")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> login(
            @Valid @RequestBody LoginRequest request) {
        TokenResponse response = authService.login(request);
        return ResponseUtils.successWithData(response, MessageConstant.LOGIN_SUCCESSFUL);
    }

    /**
     * User registration with email, password, and display name.
     *
     * @param request RegisterRequest payload.
     * @return ResponseEntity envelope containing UserProfileResponse.
     */
    @Operation(summary = "Register a new user account")
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserProfileResponse>> register(
            @Valid @RequestBody RegisterRequest request) {
        UserProfileResponse response = authService.register(request);
        return ResponseUtils.created(response, MessageConstant.REGISTER_SUCCESSFUL);
    }

    /**
     * Refreshes JWT Access Token using active Refresh Token with Token Family Rotation.
     *
     * @param request RefreshTokenRequest payload containing Refresh Token.
     * @return ResponseEntity envelope containing new TokenResponse.
     */
    @Operation(summary = "Refresh JWT Access Token using Refresh Token")
    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<TokenResponse>> refreshToken(
            @Valid @RequestBody RefreshTokenRequest request) {
        TokenResponse response = authService.refreshToken(request);
        return ResponseUtils.successWithData(response, MessageConstant.REFRESH_TOKEN_SUCCESSFUL);
    }

    /**
     * Logs out user account, revoking Refresh Token family and blacklisting Access Token JTI.
     *
     * @param httpRequest HttpServletRequest to extract Authorization header.
     * @param request      LogoutRequest payload containing Refresh Token.
     * @return ResponseEntity envelope containing success response.
     */
    @Operation(summary = "Logout user account and revoke Refresh Token")
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            HttpServletRequest httpRequest,
            @Valid @RequestBody LogoutRequest request) {
        String authHeader = httpRequest.getHeader("Authorization");
        authService.logout(authHeader, request);
        return ResponseUtils.success(MessageConstant.LOGOUT_SUCCESSFUL);
    }

    /**
     * Retrieves profile information of currently authenticated user.
     *
     * @return ResponseEntity envelope containing UserProfileResponse.
     */
    @Operation(summary = "Get profile information of currently authenticated user")
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getCurrentUserProfile() {
        UserProfileResponse response = authService.getCurrentUserProfile();
        return ResponseUtils.successWithData(response, MessageConstant.OPERATION_SUCCESSFUL);
    }

    /**
     * Updates profile information of currently authenticated user.
     *
     * @param request UpdateProfileRequest payload.
     * @return ResponseEntity envelope containing updated UserProfileResponse.
     */
    @Operation(summary = "Update profile of currently authenticated user")
    @PutMapping("/me")
    public ResponseEntity<ApiResponse<UserProfileResponse>> updateProfile(
            @Valid @RequestBody UpdateProfileRequest request) {
        UserProfileResponse response = authService.updateProfile(request);
        return ResponseUtils.successWithData(response, MessageConstant.UPDATE_PROFILE_SUCCESSFUL);
    }

    /**
     * Changes password for currently authenticated user.
     *
     * @param request ChangePasswordRequest payload.
     * @return ResponseEntity envelope containing success response.
     */
    @Operation(summary = "Change password of currently authenticated user")
    @PutMapping("/change-password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @Valid @RequestBody ChangePasswordRequest request) {
        authService.changePassword(request);
        return ResponseUtils.success(MessageConstant.CHANGE_PASSWORD_SUCCESSFUL);
    }
}
