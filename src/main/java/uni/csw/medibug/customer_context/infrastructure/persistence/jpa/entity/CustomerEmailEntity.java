package uni.csw.medibug.customer_context.infrastructure.persistence.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "customer_email", schema = "customer_c")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerEmailEntity {

    @EmbeddedId
    private CustomerEmailId id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("customerId")
    @JoinColumn(name = "customer_id", nullable = false)
    private CustomerEntity customer;

    @Column(name = "is_primary")
    private Boolean isPrimary = Boolean.FALSE;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    public CustomerEmailId getId() {
        return id;
    }

    public CustomerEntity getCustomer() {
        return customer;
    }

    public Boolean getIsPrimary() {
        return isPrimary;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}