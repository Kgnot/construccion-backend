package uni.csw.medibug.inventory_context.application.usecase;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uni.csw.medibug.inventory_context.application.port.ProductRepository;
import uni.csw.medibug.inventory_context.application.usecase.command.SaveProductCommand;
import uni.csw.medibug.inventory_context.domain.Product;
import uni.csw.medibug.shared.application.EventBus;

@Component
@Slf4j
public class SaveProductUseCase {

    private final ProductRepository productRepository;
    private final EventBus eventBus;

    public SaveProductUseCase(
            ProductRepository productRepository,
            EventBus eventBus) {
        this.productRepository = productRepository;
        this.eventBus = eventBus;
    }

    public void execute(SaveProductCommand cmd) {
        // Creamos el producto (dispositivo único)
        Product product = Product.create(
                cmd.name(),
                cmd.description(),
                cmd.serialNumber(),
                cmd.model(),
                cmd.manufacturer(),
                cmd.status()
        );

        // Guardamos el producto en el repository
        var productSaved = productRepository.save(product);
        if (productSaved != null) {
            var domainEvents = product.pullDomainEvents();
            domainEvents.forEach(eventBus::publish);
            log.info("Product saved successfully and evented");
            return;
        }
        log.error("Product not saved successfully");
    }
}
