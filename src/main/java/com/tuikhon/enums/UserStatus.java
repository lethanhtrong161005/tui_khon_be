package com.tuikhon.enums;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Enumeration representing user account status.
 */
@Schema(description = "User account status (ACTIVE, INACTIVE, BLOCKED)")
public enum UserStatus {
    ACTIVE,
    INACTIVE,
    BLOCKED
}
