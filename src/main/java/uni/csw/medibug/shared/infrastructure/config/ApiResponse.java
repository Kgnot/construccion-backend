package uni.csw.medibug.shared.infrastructure.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.time.Instant;

/**
 * Standard API success response.
 */
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private final boolean success;
    private final String message;
    private final T data;
    private final Instant timestamp;
    private final PaginationMeta pagination;

    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .timestamp(Instant.now())
                .build();
    }

    public static ApiResponse<Void> success(String message) {
        return ApiResponse.<Void>builder()
                .success(true)
                .message(message)
                .timestamp(Instant.now())
                .build();
    }

    public static <T> ApiResponse<T> success(Page<T> page, String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data((T) page.getContent())
                .timestamp(Instant.now())
                .pagination(PaginationMeta.from(page))
                .build();
    }
}