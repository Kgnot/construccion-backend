package uni.csw.medibug.telemetry_context.application.payload_handler;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import uni.csw.medibug.telemetry_context.application.event.TelemetryReceivedEvent;
import uni.csw.medibug.telemetry_context.domain.payload.ElectrolytePayload;
import uni.csw.medibug.telemetry_context.domain.payload.type.TelemetryType;
import uni.csw.medibug.telemetry_context.infrastructure.configure.shared.MqttTopic;

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
                TelemetryType.ELECTROLYTE,
                payload.userId(),
                payload.deviceId(),
                payload.timestamp(),
                payload
        ));
    }
}