package uni.csw.medibug.shared.domain.events;

import lombok.Getter;

@Getter
public final class CustomerCreatedEvent extends BaseDomainEvent {
    private final String customerId;
    private final String fullName;
    private final String email;

    public CustomerCreatedEvent(String customerId, String fullName, String email) {
        super();
        this.customerId = customerId;
        this.fullName = fullName;
        this.email = email;
    }
}