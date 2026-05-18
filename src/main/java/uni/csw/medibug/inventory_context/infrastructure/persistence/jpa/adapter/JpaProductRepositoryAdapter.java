package uni.csw.medibug.inventory_context.infrastructure.persistence.jpa.adapter;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import uni.csw.medibug.inventory_context.application.port.ProductRepository;
import uni.csw.medibug.inventory_context.domain.Product;
import uni.csw.medibug.inventory_context.domain.vo.ProductId;
import uni.csw.medibug.inventory_context.infrastructure.mapper.ProductJpaMapper;
import uni.csw.medibug.inventory_context.infrastructure.persistence.jpa.entity.ProductEntity;
import uni.csw.medibug.inventory_context.infrastructure.persistence.jpa.spring.SpringJpaProductRepositoryJpa;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class JpaProductRepositoryAdapter implements ProductRepository {

    private final SpringJpaProductRepositoryJpa springJpaProductRepositoryJpa;
    private final ProductJpaMapper productJpaMapper;

    public JpaProductRepositoryAdapter(
            SpringJpaProductRepositoryJpa springJpaProductRepositoryJpa,
            ProductJpaMapper productJpaMapper
    ) {
        this.springJpaProductRepositoryJpa = springJpaProductRepositoryJpa;
        this.productJpaMapper = productJpaMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Product> findById(ProductId id) {
        if (id == null || id.value() == null || id.value().isBlank()) {
            return Optional.empty();
        }

        UUID uuid = UUID.fromString(id.value());

        return springJpaProductRepositoryJpa.findById(uuid)
                .map(productJpaMapper::toDomain);
    }

    @Override
    @Transactional
    public Product save(Product product) {
        if (product == null) {
            return null;
        }

        ProductEntity entity = productJpaMapper.toEntity(product);
        ProductEntity saved = springJpaProductRepositoryJpa.save(entity);

        return productJpaMapper.toDomain(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return springJpaProductRepositoryJpa.findAll()
                .stream()
                .map(productJpaMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(ProductId id) {
        if (id == null || id.value() == null || id.value().isBlank()) {
            return;
        }

        UUID uuid = UUID.fromString(id.value());
        springJpaProductRepositoryJpa.deleteById(uuid);
    }
}

