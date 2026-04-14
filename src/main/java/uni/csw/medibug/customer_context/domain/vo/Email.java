package uni.csw.medibug.customer_context.domain.vo;

import lombok.Getter;
import uni.csw.medibug.customer_context.domain.error.EmailValidateError;

import java.util.regex.Pattern;

@Getter
public class Email {

    private final String value;
    private static final String EMAIL_REGEX =
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
                    "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    private static final Pattern PATTERN = Pattern.compile(EMAIL_REGEX);

    private Email(String value) {
        this.value = value;
    }

    public static Email create(String value) {
        validate(value);
        return new Email(value);
    }

    private static void validate(String value) {
        if (value == null) {
            throw new EmailValidateError("El email no puede ser null");
        }

        if (value.isBlank()) {
            throw new EmailValidateError("El email no puede estar vacío");
        }

        if (value.length() > 254) {
            throw new EmailValidateError("El email excede los 254 caracteres permitidos");
        }

        if (!PATTERN.matcher(value).matches()) {
            throw new EmailValidateError("Formato de email inválido: " + value);
        }

        String[] parts = value.split("@");
        if (parts.length != 2) {
            throw new EmailValidateError("Formato de email inválido");
        }

        String localPart = parts[0];
        String domain = parts[1];

        if (localPart.isEmpty() || localPart.length() > 64) {
            throw new EmailValidateError("La parte local del email es inválida");
        }

        if (!domain.contains(".")) {
            throw new EmailValidateError("El dominio del email es inválido");
        }
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email email = (Email) o;
        return value.equals(email.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}