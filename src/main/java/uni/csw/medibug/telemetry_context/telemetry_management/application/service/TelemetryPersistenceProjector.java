package uni.csw.medibug.telemetry_context.telemetry_management.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import uni.csw.medibug.telemetry_context.telemetry_management.application.event.TelemetryAcceptedEvent;
import uni.csw.medibug.telemetry_context.telemetry_management.application.port.out.TimeSeriesBloodCountRepository;
import uni.csw.medibug.telemetry_context.telemetry_management.application.port.out.TimeSeriesElectrolyteRepository;
import uni.csw.medibug.telemetry_context.telemetry_management.application.port.out.TimeSeriesLipidRepository;
import uni.csw.medibug.telemetry_context.telemetry_management.application.port.out.TimeSeriesMetabolicRepository;
import uni.csw.medibug.telemetry_context.telemetry_management.domain.payload.BloodCountPayload;
import uni.csw.medibug.telemetry_context.telemetry_management.domain.payload.ElectrolytePayload;
import uni.csw.medibug.telemetry_context.telemetry_management.domain.payload.LipidPayload;
import uni.csw.medibug.telemetry_context.telemetry_management.domain.payload.MetabolicPayload;

@Component
@RequiredArgsConstructor
@Slf4j
public class TelemetryPersistenceProjector {
    // proyector destinado para escuchar eventos y generar el guardado
    private final TimeSeriesBloodCountRepository bloodRepo;
    private final TimeSeriesElectrolyteRepository electrolyteRepo;
    private final TimeSeriesLipidRepository lipidRepo;
    private final TimeSeriesMetabolicRepository metabolicRepo;

    @Async("taskExecutor")
    @EventListener
    public void onAccepted(TelemetryAcceptedEvent event) {
        try {
            switch (event.telemetryType()) {
                case "blood" -> bloodRepo.save(event.userId(), (BloodCountPayload) event.payload());
                case "electrolyte" -> electrolyteRepo.save(event.userId(), (ElectrolytePayload) event.payload());
                case "lipid" -> lipidRepo.save(event.userId(), (LipidPayload) event.payload());
                case "metabolic" -> metabolicRepo.save(event.userId(), (MetabolicPayload) event.payload());
                default -> log.warn("Telemetry type no soportado para persistencia: {}", event.telemetryType());
            }
        } catch (Exception e) {
            log.error("Error persistiendo telemetría type={}, userId={}", event.telemetryType(), event.userId(), e);
        }
    }
}
