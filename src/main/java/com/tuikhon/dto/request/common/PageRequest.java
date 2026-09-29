package com.tuikhon.dto.request.common;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Standard page request DTO containing page number, size, and sorting conditions.
 */
@Schema(description = "Standard page request parameter container")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PageRequest {

    /**
     * Zero-indexed page number (default 0).
     */
    @Schema(description = "Zero-indexed page number", example = "0")
    @Builder.Default
    private Integer page = 0;

    /**
     * Page size (default 10).
     */
    @Schema(description = "Number of items per page", example = "10")
    @Builder.Default
    private Integer size = 10;

    /**
     * List of sort conditions.
     */
    @Schema(description = "List of sort field conditions")
    private List<BaseSortCondition> sort;
}
