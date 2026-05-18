package uni.csw.medibug.inventory_context.application.usecase.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProductResponseDTO(
        String id,
        String name,
        String description,
        String serialNumber,
        String model,
        String manufacturer,
        String status
) {
}

