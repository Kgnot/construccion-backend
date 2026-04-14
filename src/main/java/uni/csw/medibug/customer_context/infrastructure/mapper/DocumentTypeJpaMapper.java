package uni.csw.medibug.customer_context.infrastructure.mapper;

import org.springframework.stereotype.Component;
import uni.csw.medibug.customer_context.domain.vo.Document;
import uni.csw.medibug.customer_context.infrastructure.persistence.jpa.entity.DocumentTypeEntity;

@Component
public class DocumentTypeJpaMapper {

    public Document.DocumentType toDomainType(DocumentTypeEntity entity) {
        if (entity == null || entity.getName() == null) {
            return null;
        }
        return Document.DocumentType.valueOf(entity.getName().trim().toUpperCase());
    }

    public String toEntityName(Document.DocumentType documentType) {
        return documentType == null ? null : documentType.name();
    }
}