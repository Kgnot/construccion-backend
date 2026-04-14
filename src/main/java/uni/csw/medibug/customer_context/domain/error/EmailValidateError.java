package uni.csw.medibug.customer_context.domain.error;

public class EmailValidateError extends RuntimeException {
    public EmailValidateError(String message) {
        super(message);
    }
}
