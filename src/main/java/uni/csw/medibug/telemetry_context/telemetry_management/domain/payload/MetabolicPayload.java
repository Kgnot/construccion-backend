package uni.csw.medibug.telemetry_context.telemetry_management.domain.payload;

import java.time.Instant;

public record MetabolicPayload(
        String deviceId,
        String userId,
        Instant timestamp,
        double glucose,
        String glucoseUnit,
        double creatinine,
        double bloodUreaNitrogen,
        double uricAcid,
        double ph,
        double calcium
) {}