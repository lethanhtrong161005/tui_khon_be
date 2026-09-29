package com.tuikhon.dto.request.common;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Advanced search request payload containing filter criteria and pagination settings.
 */
@Schema(description = "Payload container for dynamic search and filter queries")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PayloadSearchRequest extends PageRequest {

    /**
     * Global search keyword.
     */
    @Schema(description = "Global search string", example = "test")
    private String keyword;

    /**
     * Advanced filter conditions list.
     */
    @Schema(description = "List of dynamic field filter conditions")
    private List<Object> filters;
}
