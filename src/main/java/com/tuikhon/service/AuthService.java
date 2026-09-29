package com.tuikhon.service;

import com.tuikhon.dto.request.auth.ChangePasswordRequest;
import com.tuikhon.dto.request.auth.LoginRequest;
import com.tuikhon.dto.request.auth.LogoutRequest;
import com.tuikhon.dto.request.auth.RefreshTokenRequest;
import com.tuikhon.dto.request.auth.RegisterRequest;
import com.tuikhon.dto.request.auth.UpdateProfileRequest;
import com.tuikhon.dto.response.auth.TokenResponse;
import com.tuikhon.dto.response.auth.UserProfileResponse;

/**
 * Service interface defining authentication, registration, token refresh, and session management operations.
 */
public interface AuthService {

    /**
     * Handles authentication via email and password, directly issuing JWT Access and Refresh Tokens.
     *
     * @param request LoginRequest containing email and password.
     * @return TokenResponse containing issued JWT tokens.
     */
    TokenResponse login(LoginRequest request);

    /**
     * Registers a new user account.
     *
     * @param request RegisterRequest containing user credentials.
     * @return UserProfileResponse of newly created user.
     */
    UserProfileResponse register(RegisterRequest request);

    /**
     * Issues new Access Token using active Refresh Token with Token Family Rotation.
     *
     * @param request RefreshTokenRequest containing Refresh Token.
     * @return TokenResponse containing newly issued tokens.
     */
    TokenResponse refreshToken(RefreshTokenRequest request);

    /**
     * Revokes Refresh Token family and adds Access Token JTI to Redis blacklist.
     *
     * @param authHeader    Authorization header string containing Bearer Access Token.
     * @param logoutRequest LogoutRequest containing Refresh Token.
     */
    void logout(String authHeader, LogoutRequest logoutRequest);

    /**
     * Retrieves profile details of the currently authenticated user.
     *
     * @return UserProfileResponse containing user profile information.
     */
    UserProfileResponse getCurrentUserProfile();

    /**
     * Updates profile details of the currently authenticated user.
     *
     * @param request UpdateProfileRequest containing updated user details.
     * @return UserProfileResponse containing updated user information.
     */
    UserProfileResponse updateProfile(UpdateProfileRequest request);

    /**
     * Changes password for the currently authenticated user.
     *
     * @param request ChangePasswordRequest containing old, new, and confirm password.
     */
    void changePassword(ChangePasswordRequest request);
}
