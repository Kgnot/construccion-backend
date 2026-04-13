package uni.csw.medibug.telemetry_context.infrastructure.persistence.timeseries.buffer;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import uni.csw.medibug.telemetry_context.application.port.out.TimeSeriesElectrolyteRepository;
import uni.csw.medibug.telemetry_context.domain.payload.ElectrolytePayload;

import java.util.List;

@Component
@Primary
public class BufferedElectrolyteDecorator implements TimeSeriesElectrolyteRepository {

    private final TelemetryBuffer<ElectrolytePayload> buffer;

    public BufferedElectrolyteDecorator(
            @Qualifier("influxDirectElectrolyteRepository") TimeSeriesElectrolyteRepository client) {
        this.buffer = new TelemetryBuffer<>(200, client::saveBatch);
    }


    @Override
    public void save(String userId, ElectrolytePayload payload) {
        buffer.add(payload);
    }

    @Override
    public void saveBatch(List<ElectrolytePayload> payloads) {
        payloads.forEach(buffer::add);
    }

    @Scheduled(fixedDelay = 5_000)
    public void scheduledFlush() {
        buffer.flush();
    }
}
