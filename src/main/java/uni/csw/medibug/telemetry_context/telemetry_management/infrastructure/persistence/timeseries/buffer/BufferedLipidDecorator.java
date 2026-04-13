package uni.csw.medibug.telemetry_context.telemetry_management.infrastructure.persistence.timeseries.buffer;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import uni.csw.medibug.telemetry_context.telemetry_management.application.port.out.TimeSeriesLipidRepository;
import uni.csw.medibug.telemetry_context.telemetry_management.domain.payload.LipidPayload;

import java.util.List;

@Component
@Primary
public class BufferedLipidDecorator implements TimeSeriesLipidRepository {

    private final TelemetryBuffer<LipidPayload> buffer;

    public BufferedLipidDecorator(
            @Qualifier("influxDirectLipidRepository") TimeSeriesLipidRepository client) {
        this.buffer = new TelemetryBuffer<>(200, client::saveBatch);
    }

    @Override
    public void save(String userId, LipidPayload payload) {
        buffer.add(payload);
    }

    @Override
    public void saveBatch(List<LipidPayload> payloads) {
        payloads.forEach(buffer::add);
    }

    @Scheduled(fixedDelay = 5_000)
    public void scheduledFlush() {
        buffer.flush();
    }
}