package uni.csw.medibug.telemetry_context.telemetry_management.domain.payload;

import java.time.Instant;

public record ElectrolytePayload(
        String deviceId,
        String userId,
        Instant timestamp,
        double sodium,
        String sodiumUnit,
        double potassium,
        String potassiumUnit
) implements TelemetryPayload {}