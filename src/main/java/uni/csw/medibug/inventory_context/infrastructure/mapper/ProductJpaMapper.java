package uni.csw.medibug.inventory_context.infrastructure.mapper;

import org.springframework.stereotype.Component;
import uni.csw.medibug.inventory_context.domain.Product;
import uni.csw.medibug.inventory_context.domain.vo.ProductId;
import uni.csw.medibug.inventory_context.infrastructure.persistence.jpa.entity.ProductEntity;

import java.util.UUID;

@Component
public class ProductJpaMapper {

    public Product toDomain(ProductEntity entity) {
        if (entity == null) {
            return null;
        }

        return Product.rehydrate(
                new ProductId(entity.getId().toString()),
                entity.getName(),
                entity.getDescription(),
                entity.getSerialNumber(),
                entity.getModel(),
                entity.getManufacturer(),
                entity.getStatus()
        );
    }

    public ProductEntity toEntity(Product product) {
        if (product == null) {
            return null;
        }

        return ProductEntity.builder()
                .id(product.getId() != null && product.getId().value() != null
                    ? UUID.fromString(product.getId().value())
                    : UUID.randomUUID())
                .name(product.getName())
                .description(product.getDescription())
                .serialNumber(product.getSerialNumber())
                .model(product.getModel())
                .manufacturer(product.getManufacturer())
                .status(product.getStatus())
                .build();
    }
}

