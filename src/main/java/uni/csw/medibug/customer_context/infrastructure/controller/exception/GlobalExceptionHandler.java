package uni.csw.medibug.customer_context.infrastructure.controller.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import uni.csw.medibug.customer_context.domain.error.BirthDayValidateError;
import uni.csw.medibug.customer_context.domain.error.DocumentValidateError;
import uni.csw.medibug.customer_context.domain.error.EmailValidateError;
import uni.csw.medibug.customer_context.infrastructure.persistence.jpa.error.DocumentTypeNotFoundException;
import uni.csw.medibug.shared.infrastructure.config.ApiError;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            DocumentValidateError.class,
            BirthDayValidateError.class,
            EmailValidateError.class
    })
    public ResponseEntity<ApiError> handleDomainValidation(RuntimeException ex, HttpServletRequest request) {
        return ResponseEntity
                .badRequest()
                .body(ApiError.of(
                        "DOMAIN_VALIDATION_ERROR",
                        ex.getMessage(),
                        List.of(ex.getMessage()),
                        request.getRequestURI()
                ));
    }

    @ExceptionHandler(DocumentTypeNotFoundException.class)
    public ResponseEntity<ApiError> handleDocumentTypeNotFound(DocumentTypeNotFoundException ex, HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiError.of(
                        "DOCUMENT_TYPE_NOT_FOUND",
                        ex.getMessage(),
                        List.of(ex.getMessage()),
                        request.getRequestURI()
                ));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleInvalidBody(HttpMessageNotReadableException ex, HttpServletRequest request) {
        ex.getMostSpecificCause();
        String detail = ex.getMostSpecificCause().getMessage();

        return ResponseEntity
                .badRequest()
                .body(ApiError.of(
                        "INVALID_REQUEST_BODY",
                        "El cuerpo de la petición no es válido",
                        List.of(detail),
                        request.getRequestURI()
                ));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
        return ResponseEntity
                .badRequest()
                .body(ApiError.of(
                        "BAD_REQUEST",
                        ex.getMessage(),
                        List.of(ex.getMessage()),
                        request.getRequestURI()
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(Exception ex, HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiError.of(
                        "INTERNAL_SERVER_ERROR",
                        "Ocurrió un error inesperado",
                        List.of(ex.getMessage()),
                        request.getRequestURI()
                ));
    }
}