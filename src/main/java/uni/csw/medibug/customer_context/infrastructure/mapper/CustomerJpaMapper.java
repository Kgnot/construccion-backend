package uni.csw.medibug.customer_context.infrastructure.mapper;

import org.springframework.stereotype.Component;
import uni.csw.medibug.customer_context.domain.Customer;
import uni.csw.medibug.customer_context.domain.vo.BirthDay;
import uni.csw.medibug.customer_context.domain.vo.CustomerId;
import uni.csw.medibug.customer_context.domain.vo.Document;
import uni.csw.medibug.customer_context.domain.vo.Email;
import uni.csw.medibug.customer_context.infrastructure.persistence.jpa.entity.CustomerEntity;
import uni.csw.medibug.customer_context.infrastructure.persistence.jpa.entity.DocumentTypeEntity;

import java.util.UUID;

@Component
public class CustomerJpaMapper {

    private final DocumentTypeJpaMapper documentTypeJpaMapper;

    public CustomerJpaMapper(DocumentTypeJpaMapper documentTypeJpaMapper) {
        this.documentTypeJpaMapper = documentTypeJpaMapper;
    }

    public Customer toDomain(CustomerEntity entity) {
        if (entity == null) {
            return null;
        }

        CustomerId customerId = entity.getId() == null
                ? null
                : new CustomerId(entity.getId().toString());

        Document document = null;
        if (entity.getDocumentType() != null && entity.getDocumentNumber() != null) {
            Document.DocumentType documentType = documentTypeJpaMapper.toDomainType(entity.getDocumentType());
            document = Document.create(documentType, entity.getDocumentNumber());
        }

        Email email = null;
        if (entity.getEmails() != null && !entity.getEmails().isEmpty()) {
            var primaryEmail = entity.getEmails().stream()
                    .filter(e -> Boolean.TRUE.equals(e.getIsPrimary()))
                    .findFirst()
                    .orElse(entity.getEmails().get(0));

            if (primaryEmail.getId() != null && primaryEmail.getId().getEmailAddress() != null) {
                email = Email.create(primaryEmail.getId().getEmailAddress());
            }
        }

        BirthDay birthDay = entity.getBirthDate() == null
                ? null
                : BirthDay.create(entity.getBirthDate());

        return Customer.rehydrate(
                customerId,
                entity.getFirstName(),
                entity.getLastName(),
                email,
                document,
                birthDay
        );
    }

    public CustomerEntity toEntity(Customer customer, DocumentTypeEntity documentTypeEntity) {
        if (customer == null) {
            return null;
        }

        CustomerEntity entity = new CustomerEntity();

        if (customer.getId() != null && customer.getId().value() != null && !customer.getId().value().isBlank()) {
            entity.setId(UUID.fromString(customer.getId().value()));
        }

        entity.setFirstName(customer.getFirstName());
        entity.setLastName(customer.getLastName());
        entity.setBirthDate(customer.getBirthDay() == null ? null : customer.getBirthDay().getValue());
        entity.setDocumentType(documentTypeEntity);
        entity.setDocumentNumber(customer.getDocument() == null ? null : customer.getDocument().getValue());

        return entity;
    }
}