package uni.csw.medibug.inventory_context.application.usecase;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uni.csw.medibug.inventory_context.application.port.ProductRepository;
import uni.csw.medibug.inventory_context.application.usecase.command.UpdateProductCommand;
import uni.csw.medibug.inventory_context.domain.Product;
import uni.csw.medibug.inventory_context.domain.error.ProductNotFoundException;
import uni.csw.medibug.inventory_context.domain.vo.ProductId;
import uni.csw.medibug.shared.application.EventBus;

@Component
@Slf4j
public class UpdateProductUseCase {

    private final ProductRepository productRepository;
    private final EventBus eventBus;

    public UpdateProductUseCase(
            ProductRepository productRepository,
            EventBus eventBus) {
        this.productRepository = productRepository;
        this.eventBus = eventBus;
    }

    public void execute(UpdateProductCommand cmd) {
        if (cmd.productId() == null || cmd.productId().isBlank()) {
            throw new IllegalArgumentException("Product ID cannot be null or empty");
        }

        Product product = productRepository.findById(new ProductId(cmd.productId()))
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + cmd.productId()));

        // Rehidratamos el producto con los nuevos datos
        Product updatedProduct = Product.rehydrate(
                new ProductId(cmd.productId()),
                cmd.name(),
                cmd.description(),
                cmd.serialNumber(),
                cmd.model(),
                cmd.manufacturer(),
                cmd.status()
        );

        var productSaved = productRepository.save(updatedProduct);
        if (productSaved != null) {
            log.info("Product updated successfully with id: {}", cmd.productId());
            return;
        }
        log.error("Product not updated successfully");
    }
}
