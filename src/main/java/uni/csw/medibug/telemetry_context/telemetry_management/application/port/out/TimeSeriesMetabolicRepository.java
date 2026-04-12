package uni.csw.medibug.telemetry_context.telemetry_management.application.port.out;


import uni.csw.medibug.telemetry_context.telemetry_management.domain.payload.MetabolicPayload;

public interface TimeSeriesMetabolicRepository {

    void save(String userId, MetabolicPayload payload);

}
