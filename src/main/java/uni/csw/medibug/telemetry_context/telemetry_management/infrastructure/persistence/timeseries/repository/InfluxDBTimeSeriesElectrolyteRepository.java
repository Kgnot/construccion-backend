package uni.csw.medibug.telemetry_context.telemetry_management.infrastructure.persistence.timeseries.repository;

import com.influxdb.v3.client.InfluxDBClient;
import com.influxdb.v3.client.Point;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import uni.csw.medibug.telemetry_context.telemetry_management.application.port.out.TimeSeriesElectrolyteRepository;
import uni.csw.medibug.telemetry_context.telemetry_management.domain.payload.ElectrolytePayload;

import java.util.List;

@Repository
@Slf4j
@Qualifier("influxDirectElectrolyteRepository")
public class InfluxDBTimeSeriesElectrolyteRepository implements TimeSeriesElectrolyteRepository {

    private final InfluxDBClient influxDbClient;

    public InfluxDBTimeSeriesElectrolyteRepository(InfluxDBClient influxDbClient) {
        this.influxDbClient = influxDbClient;
    }

    @Override
    public void save(String userId, ElectrolytePayload payload) {
        Point point = createPoint(userId, payload);
        log.info("insert electrolyte_panel into InfluxDBTimeSeriesElectrolyteRepository");
        influxDbClient.writePoint(point);
    }

    @Override
    public void saveBatch(List<ElectrolytePayload> payloads) {
        if (payloads == null || payloads.isEmpty()) {
            log.warn("payloads is empty or null, cannot save batch");
            return;
        }
        List<Point> points = payloads.stream()
                .map(payload -> createPoint(payload.userId(), payload))
                .toList();
        influxDbClient.writePoints(points);
        log.info("insert batch of electrolyte_panel into InfluxDB, size: {}", points.size());
    }

    // private

    private Point createPoint(String userId, ElectrolytePayload payload) {
        return Point.measurement("electrolyte_panel")
                .setTag("userId", userId)
                .setField("sodium", payload.sodium())
                .setField("potassium", payload.potassium())
                .setTimestamp(payload.timestamp());

    }

}