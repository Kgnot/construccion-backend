package uni.csw.medibug.inventory_context.infrastructure.persistence.jpa.spring;

import org.springframework.data.jpa.repository.JpaRepository;
import uni.csw.medibug.inventory_context.infrastructure.persistence.jpa.entity.ProductEntity;

import java.util.UUID;

public interface SpringJpaProductRepositoryJpa extends JpaRepository<ProductEntity, UUID> {
}

