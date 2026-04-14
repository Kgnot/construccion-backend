package uni.csw.medibug.customer_context.infrastructure.persistence.jpa.error;

public class DocumentTypeNotFoundException extends RuntimeException {
    public DocumentTypeNotFoundException(String message) {
        super(message);
    }
}
