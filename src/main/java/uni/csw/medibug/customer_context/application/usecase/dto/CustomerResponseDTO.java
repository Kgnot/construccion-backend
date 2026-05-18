package uni.csw.medibug.customer_context.application.usecase.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CustomerResponseDTO(
        String id,
        String firstName,
        String lastName,
        String email,
        String documentType,
        String documentNumber,
        LocalDate birthDate
) {
}