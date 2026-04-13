package uni.csw.medibug.telemetry_context.telemetry_management.application.event;

import java.time.Instant;

/*
 * Evento de que se recibió un elemento de telemetría
 * */
public record TelemetryReceivedEvent(
        String telemetryType, // Blood, Electrolyte, lipid, metabolic
        String userId,
        String deviceId,
        Instant eventTimestamp,
        Object payload
) {
}
