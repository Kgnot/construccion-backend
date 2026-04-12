package uni.csw.medibug.telemetry_context.telemetry_management.application.payload_handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import uni.csw.medibug.telemetry_context.telemetry_management.application.port.out.TimeSeriesLipidRepository;
import uni.csw.medibug.telemetry_context.telemetry_management.application.service.UserNotificationService;
import uni.csw.medibug.telemetry_context.telemetry_management.domain.payload.LipidPayload;
import uni.csw.medibug.telemetry_context.telemetry_management.infrastructure.configure.shared.MqttTopic;

@Component
@RequiredArgsConstructor
@Slf4j
@MqttTopic("sensor/lipid")
public class LipidPayloadHandler implements MqttPayloadHandler<LipidPayload> {

    private final TimeSeriesLipidRepository repository;
    private final UserNotificationService webSocketService;

    @Override
    public Class<LipidPayload> getPayloadType() {
        return LipidPayload.class;
    }

    @Override
    @Async
    public void handle(LipidPayload payload) {
        var userId = payload.userId();
        try {
            repository.save(userId, payload);
        } catch (Exception e) {
            log.error("Error saving lipid payload to userId - {}", userId, e);
            return;
        }

        try {
            webSocketService.notify(userId, "/queue/lipid", payload);
        } catch (Exception e) {
            log.error("Error communication with user - {} \n error: {}", userId, e.getMessage());
        }
    }
}