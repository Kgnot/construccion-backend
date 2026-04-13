package uni.csw.medibug.telemetry_context.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import uni.csw.medibug.telemetry_context.application.event.TelemetryAcceptedEvent;
import uni.csw.medibug.telemetry_context.application.event.TelemetryReceivedEvent;
import uni.csw.medibug.telemetry_context.domain.payload.type.TelemetryType;

import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

// Este Transaltor es por tema de consistencia de eventos
@Component
@RequiredArgsConstructor
@Slf4j
public class TelemetryStateTranslator {

    private final ApplicationEventPublisher eventPublisher;
    private final ConcurrentHashMap<StateKey, Instant> lastTimestampByDeviceAndType = new ConcurrentHashMap<>();

    @EventListener
    public void onReceived(TelemetryReceivedEvent event) {
        Instant incomingTs = event.eventTimestamp() != null ? event.eventTimestamp() : Instant.now();
        // la llave para juntar el id del dispositivo y el tipo de telemetría
        StateKey key = new StateKey(event.deviceId(), event.telemetryType());
        AtomicBoolean accepted = new AtomicBoolean(false);
        // compute nos sirve para actualizar atómicamente el valor asociado a una clave.
        lastTimestampByDeviceAndType.compute(key, (k, previousTs) -> {
            if (previousTs == null || incomingTs.isAfter(previousTs)) {
                accepted.set(true);
                return incomingTs;
            }
            return previousTs;
        });

        if (!accepted.get()) {
            log.debug("Descartado por duplicado/out-of-order: type={}, device={}, ts={}",
                    event.telemetryType(), event.deviceId(), incomingTs);
            return;
        }
        eventPublisher.publishEvent(new TelemetryAcceptedEvent(
                event.telemetryType(),
                event.userId(),
                event.deviceId(),
                incomingTs,
                event.payload()
        ));
    }


    // Esto es para generar una llave (ya que no generamos en los eventos)
    private record StateKey(String deviceId, TelemetryType telemetryType) {
    }
}
