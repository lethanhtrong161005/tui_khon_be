package com.tuikhon.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Generic paginated response wrapper envelope.
 *
 * @param <T> Page item data model type.
 */
@Schema(description = "Paginated list response wrapper")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> {

    /**
     * List of items on current page.
     */
    @Schema(description = "List of record items for current page")
    private List<T> content;

    /**
     * Current zero-indexed page number.
     */
    @Schema(description = "Current page number", example = "0")
    private int pageNumber;

    /**
     * Page size (items per page).
     */
    @Schema(description = "Number of items per page", example = "10")
    private int pageSize;

    /**
     * Total number of records across all pages.
     */
    @Schema(description = "Total element count", example = "42")
    private long totalElements;

    /**
     * Total number of pages.
     */
    @Schema(description = "Total page count", example = "5")
    private int totalPages;

    /**
     * Indicates if current page is the last page.
     */
    @Schema(description = "Whether current page is last page", example = "false")
    private boolean last;
}
