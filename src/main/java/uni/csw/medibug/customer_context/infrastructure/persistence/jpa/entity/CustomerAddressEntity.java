package uni.csw.medibug.customer_context.infrastructure.persistence.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "customer_address", schema = "customer_c")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerAddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "address_id", nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private CustomerEntity customer;

    @Column(name = "address_line", nullable = false, length = 255)
    private String addressLine;

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "state", length = 100)
    private String state;

    @Column(name = "country", length = 100)
    private String country;

    @Column(name = "postal_code", length = 20)
    private String postalCode;

    @Column(name = "is_primary")
    private Boolean isPrimary = Boolean.FALSE;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

}