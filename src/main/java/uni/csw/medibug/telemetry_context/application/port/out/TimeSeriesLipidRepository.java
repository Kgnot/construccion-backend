package uni.csw.medibug.telemetry_context.application.port.out;

import uni.csw.medibug.telemetry_context.domain.payload.LipidPayload;

import java.util.List;

public interface TimeSeriesLipidRepository {
    /*
     * Guardamos el payload en el noSQL
     * */
    void save(String userId, LipidPayload payload);

    void saveBatch(List<LipidPayload> payloads);
}
