package uni.csw.medibug.telemetry_context.telemetry_management.infrastructure.persistence.pyhole;

import com.influxdb.v3.client.InfluxDBClient;
import com.influxdb.v3.client.Point;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import uni.csw.medibug.telemetry_context.telemetry_management.application.port.out.TimeSeriesLipidRepository;
import uni.csw.medibug.telemetry_context.telemetry_management.domain.payload.LipidPayload;

@Repository
@Slf4j
public class InfluxDBTimeSeriesLipidRepository implements TimeSeriesLipidRepository {

    private final InfluxDBClient influxDbClient;

    public InfluxDBTimeSeriesLipidRepository(InfluxDBClient influxDbClient) {
        this.influxDbClient = influxDbClient;
    }

    @Override
    public void save(String userId, LipidPayload payload) {
        Point point = Point.measurement("lipid_panel")
                .setTag("userId", userId)
                .setField("totalCholesterol", payload.totalCholesterol())
                .setField("triglycerides", payload.triglycerides())
                .setTimestamp(payload.timestamp());
        log.info("insert lipid_panel into InfluxDBTimeSeriesLipidRepository");
        influxDbClient.writePoint(point);
    }
}