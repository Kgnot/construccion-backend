package uni.csw.medibug.telemetry_context.telemetry_management.application.payload_handler;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import uni.csw.medibug.telemetry_context.telemetry_management.application.event.TelemetryReceivedEvent;
import uni.csw.medibug.telemetry_context.telemetry_management.domain.payload.ElectrolytePayload;
import uni.csw.medibug.telemetry_context.telemetry_management.infrastructure.configure.shared.MqttTopic;

@Component
@RequiredArgsConstructor
@MqttTopic("sensor/electrolyte")
public class ElectrolytePayloadHandler implements MqttPayloadHandler<ElectrolytePayload> {

    private final ApplicationEventPublisher eventPublisher;

    @Override
    public Class<ElectrolytePayload> getPayloadType() {
        return ElectrolytePayload.class;
    }

    @Override
    public void handle(ElectrolytePayload payload) {
        eventPublisher.publishEvent(new TelemetryReceivedEvent(
                "electrolyte",
                payload.userId(),
                payload.deviceId(),
                payload.timestamp(),
                payload
        ));
    }
}