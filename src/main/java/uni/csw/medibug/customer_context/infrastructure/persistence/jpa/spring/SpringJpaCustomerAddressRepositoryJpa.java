package uni.csw.medibug.customer_context.infrastructure.persistence.jpa.spring;

import org.springframework.data.jpa.repository.JpaRepository;
import uni.csw.medibug.customer_context.infrastructure.persistence.jpa.entity.CustomerAddressEntity;

import java.util.List;
import java.util.UUID;

public interface SpringJpaCustomerAddressRepositoryJpa extends JpaRepository<CustomerAddressEntity, UUID> {
    List<CustomerAddressEntity> findByCustomerId(UUID customerId);
}