package uni.csw.medibug.customer_context.infrastructure.persistence.jpa.spring;

import org.springframework.data.jpa.repository.JpaRepository;
import uni.csw.medibug.customer_context.infrastructure.persistence.jpa.entity.CustomerEntity;

import java.util.Optional;
import java.util.UUID;

public interface SpringJpaCustomerRepositoryJpa extends JpaRepository<CustomerEntity, UUID> {
    Optional<CustomerEntity> findByDocumentNumber(String documentNumber);
}