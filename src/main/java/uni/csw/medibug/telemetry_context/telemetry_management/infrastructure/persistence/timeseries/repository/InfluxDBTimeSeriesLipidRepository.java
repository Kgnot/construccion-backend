package uni.csw.medibug.telemetry_context.telemetry_management.infrastructure.persistence.timeseries.repository;

import com.influxdb.v3.client.InfluxDBClient;
import com.influxdb.v3.client.Point;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import uni.csw.medibug.telemetry_context.telemetry_management.application.port.out.TimeSeriesLipidRepository;
import uni.csw.medibug.telemetry_context.telemetry_management.domain.payload.LipidPayload;

import java.util.List;

@Repository
@Slf4j
@Qualifier("influxDirectLipidRepository")
public class InfluxDBTimeSeriesLipidRepository implements TimeSeriesLipidRepository {

    private final InfluxDBClient influxDbClient;

    public InfluxDBTimeSeriesLipidRepository(InfluxDBClient influxDbClient) {
        this.influxDbClient = influxDbClient;
    }

    @Override
    public void save(String userId, LipidPayload payload) {
        Point point = createPoint(userId, payload);
        log.info("insert lipid_panel into InfluxDBTimeSeriesLipidRepository");
        influxDbClient.writePoint(point);
    }

    @Override
    public void saveBatch(List<LipidPayload> payloads) {
        if (payloads == null || payloads.isEmpty()) {
            log.warn("payloads is empty or null, cannot save batch");
            return;
        }
        List<Point> points = payloads.stream()
                .map(payload -> createPoint(payload.userId(), payload))
                .toList();
        influxDbClient.writePoints(points);
        log.info("insert batch of lipid_panel into InfluxDB, size: {}", points.size());
    }

    // private

    private Point createPoint(String userId, LipidPayload payload) {
        return Point.measurement("lipid_panel")
                .setTag("userId", userId)
                .setField("totalCholesterol", payload.totalCholesterol())
                .setField("triglycerides", payload.triglycerides())
                .setTimestamp(payload.timestamp());
    }
}