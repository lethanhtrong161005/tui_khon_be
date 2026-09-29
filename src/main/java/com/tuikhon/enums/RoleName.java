package com.tuikhon.enums;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Enumeration representing system authorization role names.
 */
@Schema(description = "System role name (USER, ADMIN)")
public enum RoleName {
    USER,
    ADMIN
}
