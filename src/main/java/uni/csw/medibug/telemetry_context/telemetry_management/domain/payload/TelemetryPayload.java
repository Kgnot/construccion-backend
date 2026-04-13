package uni.csw.medibug.telemetry_context.telemetry_management.domain.payload;

public sealed interface TelemetryPayload
        permits BloodCountPayload, ElectrolytePayload, LipidPayload, MetabolicPayload {
}
