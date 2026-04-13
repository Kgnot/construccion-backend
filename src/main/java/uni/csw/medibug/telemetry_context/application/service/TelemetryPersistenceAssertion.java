package uni.csw.medibug.telemetry_context.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import uni.csw.medibug.telemetry_context.application.event.TelemetryAcceptedEvent;
import uni.csw.medibug.telemetry_context.application.port.out.TimeSeriesBloodCountRepository;
import uni.csw.medibug.telemetry_context.application.port.out.TimeSeriesElectrolyteRepository;
import uni.csw.medibug.telemetry_context.application.port.out.TimeSeriesLipidRepository;
import uni.csw.medibug.telemetry_context.application.port.out.TimeSeriesMetabolicRepository;
import uni.csw.medibug.telemetry_context.domain.payload.BloodCountPayload;
import uni.csw.medibug.telemetry_context.domain.payload.ElectrolytePayload;
import uni.csw.medibug.telemetry_context.domain.payload.LipidPayload;
import uni.csw.medibug.telemetry_context.domain.payload.MetabolicPayload;

@Component
@RequiredArgsConstructor
@Slf4j
public class TelemetryPersistenceAssertion {
    // proyector destinado para escuchar eventos y generar el guardado
    private final TimeSeriesBloodCountRepository bloodRepo;
    private final TimeSeriesElectrolyteRepository electrolyteRepo;
    private final TimeSeriesLipidRepository lipidRepo;
    private final TimeSeriesMetabolicRepository metabolicRepo;

    @Async("taskExecutor")
    @EventListener
    public void onAccepted(TelemetryAcceptedEvent event) {
        try {
            switch (event.payload()) {
                case BloodCountPayload p -> bloodRepo.save(p.userId(), p);
                case ElectrolytePayload p -> electrolyteRepo.save(p.userId(), p);
                case LipidPayload p -> lipidRepo.save(p.userId(), p);
                case MetabolicPayload p -> metabolicRepo.save(p.userId(), p);
            }
        } catch (Exception e) {
            log.error("Error persistiendo telemetría type={}, userId={}",
                    event.telemetryType(), event.userId(), e);
        }
    }
}
