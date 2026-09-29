package com.tuikhon.enums;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Enumeration representing sort direction order (ASC or DESC).
 */
@Schema(description = "Sort direction order (ASC or DESC)")
public enum SortOrder {
    ASC,
    DESC
}
