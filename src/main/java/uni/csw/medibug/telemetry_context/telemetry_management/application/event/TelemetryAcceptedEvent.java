package uni.csw.medibug.telemetry_context.telemetry_management.application.event;

import java.time.Instant;

/*
 * Este evento no es que se recibio, es que se acepto para ser guardado
 * */
public record TelemetryAcceptedEvent(
        String telemetryType,
        String userId,
        String deviceId,
        Instant eventTimestamp,
        Object payload
) {
}
