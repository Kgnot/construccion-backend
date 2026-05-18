package uni.csw.medibug.shared.domain.events;

import lombok.Getter;

@Getter
public final class ProductCreatedEvent extends BaseDomainEvent {
    private final String productId;
    private final String productName;
    private final String serialNumber;

    public ProductCreatedEvent(String productId, String productName, String serialNumber) {
        super();
        this.productId = productId;
        this.productName = productName;
        this.serialNumber = serialNumber;
    }
}

