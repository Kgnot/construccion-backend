package uni.csw.medibug.customer_context.domain.vo;

import lombok.Getter;
import uni.csw.medibug.customer_context.domain.error.BirthDayValidateError;

import java.time.LocalDate;

@Getter
public class BirthDay {

    private final LocalDate value;

    private static final LocalDate MIN_DATE = LocalDate.of(1850, 1, 1);

    private BirthDay(LocalDate value) {
        this.value = value;
    }

    public static BirthDay create(LocalDate value) {
        if (value == null) {
            throw new BirthDayValidateError("Birthday cannot be null");
        }

        if (value.isBefore(MIN_DATE)) {
            throw new BirthDayValidateError("Date cannot be before 1850");
        }

        if (value.isAfter(LocalDate.now())) {
            throw new BirthDayValidateError("Date cannot be in the future");
        }

        return new BirthDay(value);
    }

    public static BirthDay create(String value) {
        if (value == null) {
            throw new BirthDayValidateError("Birthday cannot be null");
        }

        if (value.isBlank()) {
            throw new BirthDayValidateError("Birthday cannot be empty");
        }

        try {
            LocalDate date = LocalDate.parse(value);
            return create(date);
        } catch (Exception e) {
            throw new BirthDayValidateError("Invalid birthday format, expected yyyy-MM-dd");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BirthDay birthDay = (BirthDay) o;
        return value.equals(birthDay.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}