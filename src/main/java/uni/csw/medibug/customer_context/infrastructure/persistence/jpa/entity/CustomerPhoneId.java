package uni.csw.medibug.customer_context.infrastructure.persistence.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Getter
@Embeddable
public class CustomerPhoneId implements Serializable {

    @Column(name = "customer_id")
    private UUID customerId;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    protected CustomerPhoneId() {
    }

    public CustomerPhoneId(UUID customerId, String phoneNumber) {
        this.customerId = customerId;
        this.phoneNumber = phoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CustomerPhoneId that)) return false;
        return Objects.equals(customerId, that.customerId)
                && Objects.equals(phoneNumber, that.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId, phoneNumber);
    }
}