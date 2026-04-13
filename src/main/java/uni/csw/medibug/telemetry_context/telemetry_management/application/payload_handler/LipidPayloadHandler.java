package uni.csw.medibug.telemetry_context.telemetry_management.application.payload_handler;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import uni.csw.medibug.telemetry_context.telemetry_management.application.event.TelemetryReceivedEvent;
import uni.csw.medibug.telemetry_context.telemetry_management.domain.payload.LipidPayload;
import uni.csw.medibug.telemetry_context.telemetry_management.domain.payload.type.TelemetryType;
import uni.csw.medibug.telemetry_context.telemetry_management.infrastructure.configure.shared.MqttTopic;

@Component
@RequiredArgsConstructor
@MqttTopic("sensor/lipid")
public class LipidPayloadHandler implements MqttPayloadHandler<LipidPayload> {

    private final ApplicationEventPublisher eventPublisher;

    @Override
    public Class<LipidPayload> getPayloadType() {
        return LipidPayload.class;
    }

    @Override
    public void handle(LipidPayload payload) {
        eventPublisher.publishEvent(new TelemetryReceivedEvent(
                TelemetryType.LIPID,
                payload.userId(),
                payload.deviceId(),
                payload.timestamp(),
                payload
        ));
    }
}