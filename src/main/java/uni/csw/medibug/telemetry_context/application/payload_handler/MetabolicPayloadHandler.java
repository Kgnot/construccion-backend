package uni.csw.medibug.telemetry_context.application.payload_handler;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import uni.csw.medibug.telemetry_context.application.event.TelemetryReceivedEvent;
import uni.csw.medibug.telemetry_context.domain.payload.MetabolicPayload;
import uni.csw.medibug.telemetry_context.domain.payload.type.TelemetryType;
import uni.csw.medibug.telemetry_context.infrastructure.configure.shared.MqttTopic;

@Component
@RequiredArgsConstructor
@MqttTopic("sensor/metabolic")
public class MetabolicPayloadHandler implements MqttPayloadHandler<MetabolicPayload> {

    private final ApplicationEventPublisher eventPublisher;

    @Override
    public Class<MetabolicPayload> getPayloadType() {
        return MetabolicPayload.class;
    }

    @Override
    public void handle(MetabolicPayload payload) {
        eventPublisher.publishEvent(new TelemetryReceivedEvent(
                TelemetryType.METABOLIC,
                payload.userId(),
                payload.deviceId(),
                payload.timestamp(),
                payload
        ));
    }
}