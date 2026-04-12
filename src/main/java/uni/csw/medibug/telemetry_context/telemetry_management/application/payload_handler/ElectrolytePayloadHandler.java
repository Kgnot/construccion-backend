package uni.csw.medibug.telemetry_context.telemetry_management.application.payload_handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import uni.csw.medibug.telemetry_context.telemetry_management.application.port.out.TimeSeriesElectrolyteRepository;
import uni.csw.medibug.telemetry_context.telemetry_management.application.service.UserNotificationService;
import uni.csw.medibug.telemetry_context.telemetry_management.domain.payload.ElectrolytePayload;
import uni.csw.medibug.telemetry_context.telemetry_management.infrastructure.configure.shared.MqttTopic;

@Component
@RequiredArgsConstructor
@Slf4j
@MqttTopic("sensor/electrolyte")
public class ElectrolytePayloadHandler implements MqttPayloadHandler<ElectrolytePayload> {

    private final TimeSeriesElectrolyteRepository repository;
    private final UserNotificationService webSocketService;

    @Override
    public Class<ElectrolytePayload> getPayloadType() {
        return ElectrolytePayload.class;
    }

    @Override
    @Async
    public void handle(ElectrolytePayload payload) {
        var userId = payload.userId();
        try {
            repository.save(userId, payload);
        } catch (Exception e) {
            log.error("Error saving electrolyte payload to userId - {}", userId, e);
            return;
        }

        try {
            webSocketService.notify(userId, "/queue/electrolyte", payload);
        } catch (Exception e) {
            log.error("Error communication with user - {} \n error: {}", userId, e.getMessage());
        }
    }
}