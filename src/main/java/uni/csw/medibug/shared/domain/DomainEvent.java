package uni.csw.medibug.shared.domain;

import java.time.Instant;
import java.util.Optional;

public interface DomainEvent<TPayload> {

    String getEventId();

    Instant getOccurredOn();

    Optional<TPayload> getPayload();

    String getEventType();

    Boolean isExternal();

}
