package uni.csw.medibug.customer_context.application.usecase.command;

import uni.csw.medibug.customer_context.domain.vo.Document;

public record UpdateCustomerCommand(
        String customerId,
        String firstName,
        String lastName,
        String email,
        Document.DocumentType documentType,
        String document,
        String birthDay
) {
}

