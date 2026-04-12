package uni.csw.medibug.telemetry_context.telemetry_management.application.payload_handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import uni.csw.medibug.telemetry_context.telemetry_management.application.service.UserNotificationService;
import uni.csw.medibug.telemetry_context.telemetry_management.application.port.out.TimeSeriesBloodCountRepository;
import uni.csw.medibug.telemetry_context.telemetry_management.domain.payload.BloodCountPayload;
import uni.csw.medibug.telemetry_context.telemetry_management.infrastructure.configure.shared.MqttTopic;

@Component
@RequiredArgsConstructor
@Slf4j
@MqttTopic("sensor/blood")
public class BloodCountPayloadHandler implements MqttPayloadHandler<BloodCountPayload> {

    private final TimeSeriesBloodCountRepository repository;
    private final UserNotificationService webSocketService;


    @Override
    public Class<BloodCountPayload> getPayloadType() {
        return BloodCountPayload.class;
    }

    @Override
    @Async
    public void handle(BloodCountPayload payload) {
        var userId = payload.userId();
        try {
            repository.save(userId, payload);
        } catch (Exception e) {
            log.error("Error saving blood count payload to userId - {}", userId);
            return;
        }
        try {
            webSocketService.notify(userId, "/queue/blood", payload);
        } catch (Exception e) {
            log.error("Error communication with user - {} \n error: {}", userId, e.getMessage());
        }
    }
}
