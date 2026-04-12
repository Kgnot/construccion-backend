package uni.csw.medibug.telemetry_context.telemetry_management.application.payload_handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import uni.csw.medibug.telemetry_context.telemetry_management.application.port.out.TimeSeriesMetabolicRepository;
import uni.csw.medibug.telemetry_context.telemetry_management.application.service.UserNotificationService;
import uni.csw.medibug.telemetry_context.telemetry_management.domain.payload.MetabolicPayload;
import uni.csw.medibug.telemetry_context.telemetry_management.infrastructure.configure.shared.MqttTopic;

@Component
@RequiredArgsConstructor
@Slf4j
@MqttTopic("sensor/metabolic")
public class MetabolicPayloadHandler implements MqttPayloadHandler<MetabolicPayload> {

    private final TimeSeriesMetabolicRepository repository;
    private final UserNotificationService webSocketService;

    @Override
    public Class<MetabolicPayload> getPayloadType() {
        return MetabolicPayload.class;
    }

    @Override
    @Async
    public void handle(MetabolicPayload payload) {
        var userId = payload.userId();
        try {
            repository.save(userId, payload);
        } catch (Exception e) {
            log.error("Error saving metabolic payload to userId - {}", userId, e);
            return;
        }

        try {
            webSocketService.notify(userId, "/queue/metabolic", payload);
        } catch (Exception e) {
            log.error("Error communication with user - {} \n error: {}", userId, e.getMessage());
        }
    }
}