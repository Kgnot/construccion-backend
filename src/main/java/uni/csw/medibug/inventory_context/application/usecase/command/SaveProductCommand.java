package uni.csw.medibug.inventory_context.application.usecase.command;

public record SaveProductCommand(
        String name,
        String description,
        String serialNumber,
        String model,
        String manufacturer,
        String status
) {
}

