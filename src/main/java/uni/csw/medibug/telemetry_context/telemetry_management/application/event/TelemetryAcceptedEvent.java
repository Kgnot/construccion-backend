package uni.csw.medibug.telemetry_context.telemetry_management.application.event;

import uni.csw.medibug.telemetry_context.telemetry_management.domain.payload.TelemetryPayload;
import uni.csw.medibug.telemetry_context.telemetry_management.domain.payload.type.TelemetryType;

import java.time.Instant;

/*
 * Este evento no es que se recibio, es que se acepto para ser guardado
 * */
public record TelemetryAcceptedEvent(
        TelemetryType telemetryType,
        String userId,
        String deviceId,
        Instant eventTimestamp,
        TelemetryPayload payload
) {
}
