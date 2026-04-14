package uni.csw.medibug.shared.domain.events;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public abstract class BaseDomainEvent {

    private final String eventId;
    private final Instant occurredOn;

    protected BaseDomainEvent() {
        this.eventId = UUID.randomUUID().toString();
        this.occurredOn = Instant.now();
    }

}
