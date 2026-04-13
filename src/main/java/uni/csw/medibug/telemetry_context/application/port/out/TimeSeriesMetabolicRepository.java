package uni.csw.medibug.telemetry_context.application.port.out;


import uni.csw.medibug.telemetry_context.domain.payload.MetabolicPayload;

import java.util.List;

public interface TimeSeriesMetabolicRepository {

    void save(String userId, MetabolicPayload payload);

    void saveBatch(List<MetabolicPayload> payloads);
}
