package uni.csw.medibug.customer_context.infrastructure.persistence.jpa.spring;

import org.springframework.data.jpa.repository.JpaRepository;
import uni.csw.medibug.customer_context.infrastructure.persistence.jpa.entity.CustomerPhoneEntity;
import uni.csw.medibug.customer_context.infrastructure.persistence.jpa.entity.CustomerPhoneId;

import java.util.List;
import java.util.UUID;

public interface SpringJpaCustomerPhoneRepositoryJpa extends JpaRepository<CustomerPhoneEntity, CustomerPhoneId> {
    List<CustomerPhoneEntity> findByIdCustomerId(UUID customerId);
}