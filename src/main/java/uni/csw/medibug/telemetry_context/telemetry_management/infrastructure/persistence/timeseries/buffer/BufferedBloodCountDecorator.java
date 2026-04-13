package uni.csw.medibug.telemetry_context.telemetry_management.infrastructure.persistence.timeseries.buffer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import uni.csw.medibug.telemetry_context.telemetry_management.application.port.out.TimeSeriesBloodCountRepository;
import uni.csw.medibug.telemetry_context.telemetry_management.domain.payload.BloodCountPayload;

import java.util.List;

@Component
@Slf4j
@Primary
public class BufferedBloodCountDecorator implements TimeSeriesBloodCountRepository {

    private final TelemetryBuffer<BloodCountPayload> buffer;

    public BufferedBloodCountDecorator(
            @Qualifier("influxDirectBloodCountRepository") TimeSeriesBloodCountRepository influxRepo) {
        this.buffer = new TelemetryBuffer<>(200, influxRepo::saveBatch); // delega el flush
    }

    @Override
    public void save(String userId, BloodCountPayload payload) {
        buffer.add(payload);
    }

    @Override
    public void saveBatch(List<BloodCountPayload> payloads) {
        payloads.forEach(buffer::add);
    }

    @Scheduled(fixedDelay = 5_000)
    public void scheduledFlush() {
        buffer.flush();
    }
}