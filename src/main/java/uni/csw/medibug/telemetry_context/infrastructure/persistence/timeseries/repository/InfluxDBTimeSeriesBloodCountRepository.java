package uni.csw.medibug.telemetry_context.infrastructure.persistence.timeseries.repository;

import com.influxdb.v3.client.InfluxDBClient;
import com.influxdb.v3.client.Point;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import uni.csw.medibug.telemetry_context.application.port.out.TimeSeriesBloodCountRepository;
import uni.csw.medibug.telemetry_context.domain.payload.BloodCountPayload;

import java.util.List;

@Repository
@Slf4j
@Qualifier("influxDirectBloodCountRepository")
public class InfluxDBTimeSeriesBloodCountRepository implements TimeSeriesBloodCountRepository {

    private final InfluxDBClient influxDbClient;

    public InfluxDBTimeSeriesBloodCountRepository(InfluxDBClient influxDbClient) {
        this.influxDbClient = influxDbClient;
    }

    @Override
    public void save(String userId, BloodCountPayload payload) {
        Point point = createPoint(userId, payload);
        influxDbClient.writePoint(point);
        log.info("insert blood_count into InfluxDB");
    }

    @Override
    public void saveBatch(List<BloodCountPayload> payloads) {
        if (payloads == null || payloads.isEmpty()) {
            log.warn("payloads is empty or null, cannot save batch");
            return;
        }
        List<Point> points = payloads.stream()
                .map(payload -> createPoint(payload.userId(), payload))
                .toList();
        influxDbClient.writePoints(points);
        log.info("insert batch of blood_count into InfluxDB, size: {}", points.size());
    }

    // privado:
    private Point createPoint(String userId, BloodCountPayload payload) {
        return Point.measurement("blood_count")
                .setTag("userId", userId)
                .setField("redBloodCells", payload.redBloodCells())
                .setField("whiteBloodCells", payload.whiteBloodCells())
                .setField("platelets", payload.platelets())
                .setField("hemoglobin", payload.hemoglobin())
                .setField("iron", payload.iron())
                .setTimestamp(payload.timestamp());
    }
}