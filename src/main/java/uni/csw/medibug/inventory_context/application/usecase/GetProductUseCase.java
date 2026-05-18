package uni.csw.medibug.inventory_context.application.usecase;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uni.csw.medibug.inventory_context.application.port.ProductRepository;
import uni.csw.medibug.inventory_context.application.usecase.dto.ProductResponseDTO;
import uni.csw.medibug.inventory_context.domain.Product;
import uni.csw.medibug.inventory_context.domain.error.ProductNotFoundException;
import uni.csw.medibug.inventory_context.domain.vo.ProductId;

@Component
@Slf4j
public class GetProductUseCase {

    private final ProductRepository productRepository;

    public GetProductUseCase(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponseDTO execute(String productId) {
        if (productId == null || productId.isBlank()) {
            throw new IllegalArgumentException("Product ID cannot be null or empty");
        }

        Product product = productRepository.findById(new ProductId(productId))
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + productId));

        return mapToDTO(product);
    }

    private ProductResponseDTO mapToDTO(Product product) {
        return new ProductResponseDTO(
                product.getId().value(),
                product.getName(),
                product.getDescription(),
                product.getSerialNumber(),
                product.getModel(),
                product.getManufacturer(),
                product.getStatus()
        );
    }
}

