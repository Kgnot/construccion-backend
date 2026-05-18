package uni.csw.medibug.inventory_context.application.usecase;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uni.csw.medibug.device_management_context.application.dto.DeviceRequestActivateDTO;
import uni.csw.medibug.device_management_context.application.dto.DeviceRequestDTO;
import uni.csw.medibug.device_management_context.application.port.DeviceManagementClient;
import uni.csw.medibug.inventory_context.application.port.ProductRepository;
import uni.csw.medibug.inventory_context.application.usecase.command.SaveProductCommand;
import uni.csw.medibug.inventory_context.domain.Product;
import uni.csw.medibug.shared.application.EventBus;

@Component
@Slf4j
public class SaveProductUseCase {

    private final ProductRepository productRepository;
    private final EventBus eventBus;
    private final DeviceManagementClient deviceManagementClient;

    public SaveProductUseCase(
            ProductRepository productRepository,
            EventBus eventBus,
            DeviceManagementClient deviceManagementClient) {
        this.productRepository = productRepository;
        this.eventBus = eventBus;
        this.deviceManagementClient = deviceManagementClient;
    }

    public void execute(SaveProductCommand cmd) {
        Product product = Product.create(
                cmd.name(),
                cmd.description(),
                cmd.serialNumber(),
                cmd.model(),
                cmd.manufacturer(),
                cmd.status()
        );

        var productSaved = productRepository.save(product);
        if (productSaved != null) {
            try {
                String deviceId = productSaved.getId().value();
                log.info("Registering device with ID: {}", deviceId);

                DeviceRequestDTO registerRequest = new DeviceRequestDTO(
                        deviceId,
                        cmd.userId(),
                        cmd.deviceType() != null ? cmd.deviceType() : "MEDIBUG_DEVICE",
                        cmd.interval() != null ? cmd.interval() : 5000
                );

                var deviceResponse = deviceManagementClient.registerDevice(registerRequest);
                log.info("Device registered successfully: {}", deviceResponse.deviceId());

                log.info("Activating device with ID: {}", deviceId);
                DeviceRequestActivateDTO activateRequest = new DeviceRequestActivateDTO(deviceId);
                deviceManagementClient.activateDevice(activateRequest);
                log.info("Device activated successfully: {}", deviceId);

            } catch (Exception e) {
                log.error("Error registering or activating device: {}", e.getMessage(), e);
                throw new RuntimeException("Failed to register/activate device with device management service", e);
            }

            var domainEvents = product.pullDomainEvents();
            domainEvents.forEach(eventBus::publish);
            log.info("Product saved successfully and evented");
            return;
        }
        log.error("Product not saved successfully");
    }
}
