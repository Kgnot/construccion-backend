package uni.csw.medibug.customer_context.infrastructure.persistence.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "document_type", schema = "customer_c")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DocumentTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "document_type_id")
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;

    @Column(name = "description", length = 100)
    private String description;
}