package uni.csw.medibug.customer_context.infrastructure.persistence.jpa.adapter;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import uni.csw.medibug.customer_context.application.port.CustomerRepository;
import uni.csw.medibug.customer_context.domain.Customer;
import uni.csw.medibug.customer_context.domain.vo.CustomerId;
import uni.csw.medibug.customer_context.infrastructure.mapper.CustomerJpaMapper;
import uni.csw.medibug.customer_context.infrastructure.persistence.jpa.entity.CustomerEntity;
import uni.csw.medibug.customer_context.infrastructure.persistence.jpa.entity.DocumentTypeEntity;
import uni.csw.medibug.customer_context.infrastructure.persistence.jpa.error.DocumentTypeNotFoundException;
import uni.csw.medibug.customer_context.infrastructure.persistence.jpa.spring.SpringJpaCustomerRepositoryJpa;
import uni.csw.medibug.customer_context.infrastructure.persistence.jpa.spring.SpringJpaDocumentTypeRepositoryJpa;

import java.util.UUID;

@Repository
public class JpaCustomerRepositoryAdapter implements CustomerRepository {

    private final SpringJpaCustomerRepositoryJpa springJpaCustomerRepositoryJpa;
    private final SpringJpaDocumentTypeRepositoryJpa springJpaDocumentTypeRepositoryJpa;
    private final CustomerJpaMapper customerJpaMapper;

    public JpaCustomerRepositoryAdapter(
            SpringJpaCustomerRepositoryJpa springJpaCustomerRepositoryJpa,
            SpringJpaDocumentTypeRepositoryJpa springJpaDocumentTypeRepositoryJpa,
            CustomerJpaMapper customerJpaMapper
    ) {
        this.springJpaCustomerRepositoryJpa = springJpaCustomerRepositoryJpa;
        this.springJpaDocumentTypeRepositoryJpa = springJpaDocumentTypeRepositoryJpa;
        this.customerJpaMapper = customerJpaMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Customer findById(CustomerId id) {
        if (id == null || id.value() == null || id.value().isBlank()) {
            return null;
        }

        UUID uuid = UUID.fromString(id.value());

        return springJpaCustomerRepositoryJpa.findById(uuid)
                .map(customerJpaMapper::toDomain)
                .orElse(null);
    }

    @Override
    @Transactional
    public Customer save(Customer customer) {
        if (customer == null) {
            return null;
        }

        DocumentTypeEntity documentTypeEntity = null;
        if (customer.getDocument() != null && customer.getDocument().getDocumentType() != null) {
            String documentTypeName = customer.getDocument().getDocumentType().name();
            documentTypeEntity = springJpaDocumentTypeRepositoryJpa.findByName(documentTypeName)
                    .orElseThrow(() -> new DocumentTypeNotFoundException(
                            "No existe el tipo de documento: " + documentTypeName
                    ));
        }

        CustomerEntity entity = customerJpaMapper.toEntity(customer, documentTypeEntity);
        CustomerEntity saved = springJpaCustomerRepositoryJpa.save(entity);

        return customerJpaMapper.toDomain(saved);
    }
}