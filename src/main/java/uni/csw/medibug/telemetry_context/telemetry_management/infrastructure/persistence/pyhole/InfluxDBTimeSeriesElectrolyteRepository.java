package uni.csw.medibug.telemetry_context.telemetry_management.infrastructure.persistence.pyhole;

import com.influxdb.v3.client.InfluxDBClient;
import com.influxdb.v3.client.Point;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import uni.csw.medibug.telemetry_context.telemetry_management.application.port.out.TimeSeriesElectrolyteRepository;
import uni.csw.medibug.telemetry_context.telemetry_management.domain.payload.ElectrolytePayload;

@Repository
@Slf4j
public class InfluxDBTimeSeriesElectrolyteRepository implements TimeSeriesElectrolyteRepository {

    private final InfluxDBClient influxDbClient;

    public InfluxDBTimeSeriesElectrolyteRepository(InfluxDBClient influxDbClient) {
        this.influxDbClient = influxDbClient;
    }

    @Override
    public void save(String userId, ElectrolytePayload payload) {
        Point point = Point.measurement("electrolyte_panel")
                .setTag("userId", userId)
                .setField("sodium", payload.sodium())
                .setField("potassium", payload.potassium())
                .setTimestamp(payload.timestamp());
        log.info("insert electrolyte_panel into InfluxDBTimeSeriesElectrolyteRepository");
        influxDbClient.writePoint(point);
    }
}