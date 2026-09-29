package com.tuikhon.dto.request.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO representing sorting field and direction conditions.
 */
@Schema(description = "DTO representing sorting field and direction conditions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseSortCondition {

    /**
     * Field name to sort by.
     */
    @Schema(description = "Entity field name to apply sorting", example = "createdAt")
    private String field;

    /**
     * Sort direction: ASC or DESC.
     */
    @Schema(description = "Sorting order direction", example = "DESC")
    private String direction;
}
