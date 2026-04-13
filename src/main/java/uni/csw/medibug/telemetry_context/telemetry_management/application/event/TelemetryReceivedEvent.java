package uni.csw.medibug.telemetry_context.telemetry_management.application.event;

import uni.csw.medibug.telemetry_context.telemetry_management.domain.payload.TelemetryPayload;
import uni.csw.medibug.telemetry_context.telemetry_management.domain.payload.type.TelemetryType;

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
