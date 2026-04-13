package uni.csw.medibug.telemetry_context.infrastructure.persistence.timeseries.repository;

import com.influxdb.v3.client.InfluxDBClient;
import com.influxdb.v3.client.Point;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import uni.csw.medibug.telemetry_context.application.port.out.TimeSeriesMetabolicRepository;
import uni.csw.medibug.telemetry_context.domain.payload.MetabolicPayload;

import java.util.List;

@Repository
@Slf4j
@Qualifier("influxDirectMetabolicRepository")
public class InfluxDBTimeSeriesMetabolicRepository implements TimeSeriesMetabolicRepository {

    private final InfluxDBClient influxDbClient;

    public InfluxDBTimeSeriesMetabolicRepository(InfluxDBClient influxDbClient) {
        this.influxDbClient = influxDbClient;
    }

    @Override
    public void save(String userId, MetabolicPayload payload) {
        Point point = createPoint(userId, payload);
        log.info("insert metabolic_panel into InfluxDBTimeSeriesMetabolicRepository");
        influxDbClient.writePoint(point);
    }

    @Override
    public void saveBatch(List<MetabolicPayload> payloads) {
        if (payloads == null || payloads.isEmpty()) {
            log.warn("payloads is empty or null, cannot save batch");
            return;
        }
        List<Point> points = payloads.stream()
                .map(payload -> createPoint(payload.userId(), payload))
                .toList();
        influxDbClient.writePoints(points);
        log.info("insert batch of metabolic_panel into InfluxDB, size: {}", points.size());
    }

    // private
    private Point createPoint(String userId, MetabolicPayload payload) {
        return Point.measurement("metabolic_panel")
                .setTag("userId", userId)
                .setField("glucose", payload.glucose())
                .setField("creatinine", payload.creatinine())
                .setField("bloodUreaNitrogen", payload.bloodUreaNitrogen())
                .setField("uricAcid", payload.uricAcid())
                .setField("ph", payload.ph())
                .setField("calcium", payload.calcium())
                .setTimestamp(payload.timestamp());

    }
}