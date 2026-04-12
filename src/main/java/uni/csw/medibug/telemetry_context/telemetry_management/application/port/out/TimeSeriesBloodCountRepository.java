package uni.csw.medibug.telemetry_context.telemetry_management.application.port.out;

import uni.csw.medibug.telemetry_context.telemetry_management.domain.payload.BloodCountPayload;

public interface TimeSeriesBloodCountRepository {

    /*
     * Guardamos el payload en el noSQL
     * */
    void save(String userId, BloodCountPayload payload);

}
