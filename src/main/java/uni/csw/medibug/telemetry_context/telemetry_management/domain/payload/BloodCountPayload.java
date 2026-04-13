package uni.csw.medibug.telemetry_context.telemetry_management.domain.payload;

import java.time.Instant;

public record BloodCountPayload(
        String deviceId,
        String userId,
        Instant timestamp,
        double hemoglobin,
        String hemoglobinUnit,
        long redBloodCells,        // Usamos long porque en tu log son valores enteros grandes (5.08 millones)
        long whiteBloodCells,       // Usamos long porque en tu log son valores enteros grandes (7508)
        String whiteBloodCellsUnit,
        long platelets,             // Igual aquí (245460)
        String plateletsUnit,
        double iron,
        String ironUnit
) implements TelemetryPayload {
}
