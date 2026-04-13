package uni.csw.medibug.telemetry_context.application.event;

import uni.csw.medibug.telemetry_context.domain.payload.TelemetryPayload;
import uni.csw.medibug.telemetry_context.domain.payload.type.TelemetryType;

import java.time.Instant;

/*
 * Evento de que se recibió un elemento de telemetría
 * */
public record TelemetryReceivedEvent(
        TelemetryType telemetryType, // Blood, Electrolyte, lipid, metabolic
        String userId,
        String deviceId,
        Instant eventTimestamp,
        TelemetryPayload payload
) {
}
