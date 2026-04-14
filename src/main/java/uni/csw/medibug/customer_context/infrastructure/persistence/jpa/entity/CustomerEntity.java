package uni.csw.medibug.customer_context.infrastructure.persistence.jpa.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Entity
@Table(name = "customer", schema = "customer_c")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "customer_id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "document_type_id", nullable = false)
    private DocumentTypeEntity documentType;

    @Column(name = "document_number", nullable = false, unique = true, length = 50)
    private String documentNumber;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustomerPhoneEntity> phones = new ArrayList<>();

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustomerEmailEntity> emails = new ArrayList<>();

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustomerAddressEntity> addresses = new ArrayList<>();
}