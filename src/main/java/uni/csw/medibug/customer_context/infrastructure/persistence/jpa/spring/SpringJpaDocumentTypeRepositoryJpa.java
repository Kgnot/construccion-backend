package uni.csw.medibug.customer_context.infrastructure.persistence.jpa.spring;

import org.springframework.data.jpa.repository.JpaRepository;
import uni.csw.medibug.customer_context.infrastructure.persistence.jpa.entity.DocumentTypeEntity;

import java.util.Optional;

public interface SpringJpaDocumentTypeRepositoryJpa extends JpaRepository<DocumentTypeEntity, Long> {

    Optional<DocumentTypeEntity> findByName(String name);
}