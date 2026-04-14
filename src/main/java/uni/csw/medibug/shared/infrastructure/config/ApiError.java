package uni.csw.medibug.shared.infrastructure.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.List;

/**
 * Standard API error response.
 */
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {

    private final boolean success;
    private final String errorCode;
    private final String message;
    private final List<String> details;
    private final Instant timestamp;
    private final String path;

    public static ApiError of(
            String errorCode,
            String message,
            List<String> details,
            String path
    ) {
        return ApiError.builder()
                .success(false)
                .errorCode(errorCode)
                .message(message)
                .details(details)
                .timestamp(Instant.now())
                .path(path)
                .build();
    }

    public static ApiError of(
            String errorCode,
            String message,
            String path
    ) {
        return ApiError.builder()
                .success(false)
                .errorCode(errorCode)
                .message(message)
                .details(List.of())
                .timestamp(Instant.now())
                .path(path)
                .build();
    }
}