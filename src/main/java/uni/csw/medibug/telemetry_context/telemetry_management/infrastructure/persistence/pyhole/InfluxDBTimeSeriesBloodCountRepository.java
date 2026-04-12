package uni.csw.medibug.telemetry_context.telemetry_management.infrastructure.persistence.pyhole;

import com.influxdb.v3.client.InfluxDBClient;
import com.influxdb.v3.client.Point;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import uni.csw.medibug.telemetry_context.telemetry_management.application.port.out.TimeSeriesBloodCountRepository;
import uni.csw.medibug.telemetry_context.telemetry_management.domain.payload.BloodCountPayload;

@Repository
@Slf4j
public class InfluxDBTimeSeriesBloodCountRepository implements TimeSeriesBloodCountRepository {

    private final InfluxDBClient influxDbClient;

    public InfluxDBTimeSeriesBloodCountRepository(InfluxDBClient influxDbClient) {
        this.influxDbClient = influxDbClient;
    }

    @Override
    public void save(String userId, BloodCountPayload payload) {
        Point point = Point.measurement("blood_count")
                .setTag("userId", userId) // Los Tags son indexados, ideales para filtrar por usuario
                .setField("redBloodCells", payload.redBloodCells())
                .setField("whiteBloodCells", payload.whiteBloodCells())
                .setField("platelets", payload.platelets())
                .setField("hemoglobin", payload.hemoglobin())
                .setField("iron", payload.iron())
                .setTimestamp(payload.timestamp());
        log.info("insert blood_count into InfluxDBTimeSeriesBloodCountRepository");
        influxDbClient.writePoint(point);
    }
}