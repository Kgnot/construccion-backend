package uni.csw.medibug.customer_context.infrastructure.persistence.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Getter
@Embeddable
public class CustomerEmailId implements Serializable {

    @Column(name = "customer_id")
    private UUID customerId;

    @Column(name = "email_address", length = 255)
    private String emailAddress;

    protected CustomerEmailId() {
    }

    public CustomerEmailId(UUID customerId, String emailAddress) {
        this.customerId = customerId;
        this.emailAddress = emailAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CustomerEmailId that)) return false;
        return Objects.equals(customerId, that.customerId)
                && Objects.equals(emailAddress, that.emailAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId, emailAddress);
    }
}