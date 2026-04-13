package uni.csw.medibug.telemetry_context.telemetry_management.infrastructure.persistence.timeseries.buffer;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import uni.csw.medibug.telemetry_context.telemetry_management.application.port.out.TimeSeriesMetabolicRepository;
import uni.csw.medibug.telemetry_context.telemetry_management.domain.payload.MetabolicPayload;

import java.util.List;

@Component
@Primary
public class BufferedMetabolicDecorator implements TimeSeriesMetabolicRepository {

    private final TelemetryBuffer<MetabolicPayload> buffer;

    public BufferedMetabolicDecorator(
            @Qualifier("influxDirectMetabolicRepository") TimeSeriesMetabolicRepository client) {
        this.buffer = new TelemetryBuffer<>(200, client::saveBatch);
    }

    @Override
    public void save(String userId, MetabolicPayload payload) {
        buffer.add(payload);
    }

    @Override
    public void saveBatch(List<MetabolicPayload> payloads) {
        payloads.forEach(buffer::add);
    }

    @Scheduled(fixedDelay = 5_000)
    public void scheduledFlush() {
        buffer.flush();
    }
}