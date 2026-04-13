package uni.csw.medibug.telemetry_context.application.port.out;


import uni.csw.medibug.telemetry_context.domain.payload.ElectrolytePayload;

import java.util.List;

public interface TimeSeriesElectrolyteRepository {

    /*
     * Guardamos el payload en el noSQL
     * */
    void save(String userId, ElectrolytePayload payload);

    void saveBatch(List<ElectrolytePayload> payloads);
}
