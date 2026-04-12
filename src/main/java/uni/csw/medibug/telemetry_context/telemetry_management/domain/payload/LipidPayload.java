package uni.csw.medibug.telemetry_context.telemetry_management.domain.payload;

import java.time.Instant;

public record LipidPayload(
        String deviceId,
        String userId,
        Instant timestamp,
        double totalCholesterol,
        String totalCholesterolUnit,
        double triglycerides,
        String triglyceridesUnit
) {}