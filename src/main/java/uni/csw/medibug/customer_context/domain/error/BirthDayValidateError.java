package uni.csw.medibug.customer_context.domain.error;

public class BirthDayValidateError extends RuntimeException {
    public BirthDayValidateError(String message) {
        super(message);
    }
}
