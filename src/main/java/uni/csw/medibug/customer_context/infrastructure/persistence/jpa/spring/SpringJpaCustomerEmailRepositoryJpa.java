package uni.csw.medibug.customer_context.infrastructure.persistence.jpa.spring;

import org.springframework.data.jpa.repository.JpaRepository;
import uni.csw.medibug.customer_context.infrastructure.persistence.jpa.entity.CustomerEmailEntity;
import uni.csw.medibug.customer_context.infrastructure.persistence.jpa.entity.CustomerEmailId;

import java.util.List;
import java.util.UUID;

public interface SpringJpaCustomerEmailRepositoryJpa extends JpaRepository<CustomerEmailEntity, CustomerEmailId> {
    List<CustomerEmailEntity> findByIdCustomerId(UUID customerId);
}