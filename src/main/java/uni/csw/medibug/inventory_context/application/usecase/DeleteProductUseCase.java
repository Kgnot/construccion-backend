package uni.csw.medibug.inventory_context.application.usecase;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uni.csw.medibug.inventory_context.application.port.ProductRepository;
import uni.csw.medibug.inventory_context.application.usecase.command.DeleteProductCommand;
import uni.csw.medibug.inventory_context.domain.error.ProductNotFoundException;
import uni.csw.medibug.inventory_context.domain.vo.ProductId;

@Component
@Slf4j
public class DeleteProductUseCase {

    private final ProductRepository productRepository;

    public DeleteProductUseCase(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void execute(DeleteProductCommand cmd) {
        if (cmd.productId() == null || cmd.productId().isBlank()) {
            throw new IllegalArgumentException("Product ID cannot be null or empty");
        }

        ProductId productId = new ProductId(cmd.productId());

        productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + cmd.productId()));

        productRepository.delete(productId);
        log.info("Product deleted successfully with id: {}", cmd.productId());
    }
}
