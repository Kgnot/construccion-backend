package uni.csw.medibug.inventory_context.domain;

import lombok.Getter;
import uni.csw.medibug.inventory_context.domain.vo.ProductId;
import uni.csw.medibug.shared.domain.events.BaseDomainEvent;
import uni.csw.medibug.shared.domain.events.ProductCreatedEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class Product {

    private final ProductId id;
    private final String name;
    private final String description;
    private final String serialNumber;
    private final String model;
    private final String manufacturer;
    private final String status;
    // eventos
    private final List<BaseDomainEvent> domainEvents;

    private Product(
            ProductId id,
            String name,
            String description,
            String serialNumber,
            String model,
            String manufacturer,
            String status
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.serialNumber = serialNumber;
        this.model = model;
        this.manufacturer = manufacturer;
        this.status = status;
        this.domainEvents = new ArrayList<>();
    }

    private Product(
            String name,
            String description,
            String serialNumber,
            String model,
            String manufacturer,
            String status
    ) {
        this(
                null,
                name,
                description,
                serialNumber,
                model,
                manufacturer,
                status
        );
    }

    public static Product create(
            String name,
            String description,
            String serialNumber,
            String model,
            String manufacturer,
            String status
    ) {
        Product product = new Product(name, description, serialNumber, model, manufacturer, status);

        product.record(new ProductCreatedEvent(
                product.getId() != null ? product.getId().value() : null,
                product.getName(),
                product.getSerialNumber()
        ));

        return product;
    }

    public static Product rehydrate(
            ProductId id,
            String name,
            String description,
            String serialNumber,
            String model,
            String manufacturer,
            String status
    ) {
        return new Product(id, name, description, serialNumber, model, manufacturer, status);
    }

    public List<BaseDomainEvent> pullDomainEvents() {
        var events = Collections.unmodifiableList(this.domainEvents);
        this.domainEvents.clear();
        return events;
    }

    private void record(BaseDomainEvent event) {
        this.domainEvents.add(event);
    }
}
