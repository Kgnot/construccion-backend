package uni.csw.medibug.shared.infrastructure.config;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

/**
 * Pagination metadata.
 */
@Getter
@Builder
public class PaginationMeta {

    private final int page;
    private final int size;
    private final long totalElements;
    private final int totalPages;
    private final boolean hasNext;
    private final boolean hasPrevious;

    public static PaginationMeta from(Page<?> page) {
        return PaginationMeta.builder()
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .hasNext(page.hasNext())
                .hasPrevious(page.hasPrevious())
                .build();
    }
}