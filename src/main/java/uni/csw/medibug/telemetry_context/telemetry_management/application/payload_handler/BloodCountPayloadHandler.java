package uni.csw.medibug.telemetry_context.telemetry_management.application.payload_handler;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import uni.csw.medibug.telemetry_context.telemetry_management.application.event.TelemetryReceivedEvent;
import uni.csw.medibug.telemetry_context.telemetry_management.domain.payload.BloodCountPayload;
import uni.csw.medibug.telemetry_context.telemetry_management.domain.payload.type.TelemetryType;
import uni.csw.medibug.telemetry_context.telemetry_management.infrastructure.configure.shared.MqttTopic;

@Component
@RequiredArgsConstructor
@MqttTopic("sensor/blood")
public class BloodCountPayloadHandler implements MqttPayloadHandler<BloodCountPayload> {

    private final ApplicationEventPublisher eventPublisher;

    @Override
    public Class<BloodCountPayload> getPayloadType() {
        return BloodCountPayload.class;
    }

    @Override
    public void handle(BloodCountPayload payload) {
        eventPublisher.publishEvent(new TelemetryReceivedEvent(
                TelemetryType.BLOOD,
                payload.userId(),
                payload.deviceId(),
                payload.timestamp(),
                payload
        ));
    }
}