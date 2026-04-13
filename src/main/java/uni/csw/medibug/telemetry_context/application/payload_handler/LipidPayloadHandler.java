package uni.csw.medibug.telemetry_context.application.payload_handler;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import uni.csw.medibug.telemetry_context.application.event.TelemetryReceivedEvent;
import uni.csw.medibug.telemetry_context.domain.payload.LipidPayload;
import uni.csw.medibug.telemetry_context.domain.payload.type.TelemetryType;
import uni.csw.medibug.telemetry_context.infrastructure.configure.shared.MqttTopic;

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