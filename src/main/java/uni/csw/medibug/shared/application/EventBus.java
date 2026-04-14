package uni.csw.medibug.shared.application;

import uni.csw.medibug.shared.domain.events.BaseDomainEvent;

public interface EventBus {

    void publish(BaseDomainEvent event);

}
