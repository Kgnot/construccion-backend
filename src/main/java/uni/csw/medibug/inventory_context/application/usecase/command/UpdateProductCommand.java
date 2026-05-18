package uni.csw.medibug.inventory_context.application.usecase.command;

public record UpdateProductCommand(
        String productId,
        String name,
        String description,
        String serialNumber,
        String model,
        String manufacturer,
        String status
) {
}

