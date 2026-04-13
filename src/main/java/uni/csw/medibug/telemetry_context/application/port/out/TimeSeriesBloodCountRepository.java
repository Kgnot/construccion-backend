package uni.csw.medibug.telemetry_context.application.port.out;

import uni.csw.medibug.telemetry_context.domain.payload.BloodCountPayload;

import java.util.List;

public interface TimeSeriesBloodCountRepository {

    /*
     * Guardamos el payload en el noSQL
     * */
    void save(String userId, BloodCountPayload payload);

    void saveBatch(List<BloodCountPayload> payloads);
}
