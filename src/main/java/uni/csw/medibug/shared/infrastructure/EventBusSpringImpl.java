package uni.csw.medibug.shared.infrastructure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import uni.csw.medibug.shared.application.EventBus;
import uni.csw.medibug.shared.domain.events.BaseDomainEvent;

@Component
@Slf4j
public class EventBusSpringImpl implements EventBus {

    private final ApplicationEventPublisher applicationEventPublisher;

    public EventBusSpringImpl(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void publish(BaseDomainEvent event) {
        // publicamos directamente por spring
        applicationEventPublisher.publishEvent(event);
        log.info("Published event: {}", event);
    }
}

