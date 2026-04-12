package uni.csw.medibug.telemetry_context.telemetry_management.infrastructure.persistence.pyhole;

import com.influxdb.v3.client.InfluxDBClient;
import com.influxdb.v3.client.Point;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import uni.csw.medibug.telemetry_context.telemetry_management.application.port.out.TimeSeriesMetabolicRepository;
import uni.csw.medibug.telemetry_context.telemetry_management.domain.payload.MetabolicPayload;

@Repository
@Slf4j
public class InfluxDBTimeSeriesMetabolicRepository implements TimeSeriesMetabolicRepository {

    private final InfluxDBClient influxDbClient;

    public InfluxDBTimeSeriesMetabolicRepository(InfluxDBClient influxDbClient) {
        this.influxDbClient = influxDbClient;
    }

    @Override
    public void save(String userId, MetabolicPayload payload) {
        Point point = Point.measurement("metabolic_panel")
                .setTag("userId", userId)
                .setField("glucose", payload.glucose())
                .setField("creatinine", payload.creatinine())
                .setField("bloodUreaNitrogen", payload.bloodUreaNitrogen())
                .setField("uricAcid", payload.uricAcid())
                .setField("ph", payload.ph())
                .setField("calcium", payload.calcium())
                .setTimestamp(payload.timestamp());
        log.info("insert metabolic_panel into InfluxDBTimeSeriesMetabolicRepository");
        influxDbClient.writePoint(point);
    }
}