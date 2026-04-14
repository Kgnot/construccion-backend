package uni.csw.medibug.customer_context.domain.error;

public class DocumentValidateError extends RuntimeException {
    public DocumentValidateError(String message) {
        super(message);
    }
}
