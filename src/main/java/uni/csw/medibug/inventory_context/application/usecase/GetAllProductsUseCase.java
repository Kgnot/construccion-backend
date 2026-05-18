package uni.csw.medibug.inventory_context.application.usecase;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uni.csw.medibug.inventory_context.application.port.ProductRepository;
import uni.csw.medibug.inventory_context.application.usecase.dto.ProductResponseDTO;
import uni.csw.medibug.inventory_context.domain.Product;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class GetAllProductsUseCase {

    private final ProductRepository productRepository;

    public GetAllProductsUseCase(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductResponseDTO> execute() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
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

