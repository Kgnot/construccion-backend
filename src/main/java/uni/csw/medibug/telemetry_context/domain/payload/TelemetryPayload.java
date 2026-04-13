package uni.csw.medibug.telemetry_context.domain.payload;

public sealed interface TelemetryPayload
        permits BloodCountPayload, ElectrolytePayload, LipidPayload, MetabolicPayload {
}
