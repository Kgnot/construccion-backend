package uni.csw.medibug.telemetry_context.application.router;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;
import uni.csw.medibug.telemetry_context.application.payload_handler.MqttPayloadHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * Esta clase es para enrutar los mensajes a sus respectivos handlers
 * Esta acoplada a MQTT, pero se puede adaptar a otros protocolos si es necesario.
 * */
@Component
@Slf4j
public class MqttMessageRouter {

    private final Map<String, MqttPayloadHandler<?>> registry = new HashMap<>();
    private final ObjectMapper mapper;

    public MqttMessageRouter(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public <T> void registerHandler(String topic, MqttPayloadHandler<T> handler) {
        registry.put(topic, handler);
    }

    public List<String> getRegisteredTopics() {
        return registry.keySet().stream().toList();
    }

    public void routeMessage(String topic, Object payload) {
        MqttPayloadHandler<?> handler = registry.get(topic);

        if (handler == null) {
            log.error("No handler for topic {}", topic);
            return;
        }

        try {
            // Obtendremos el tipo que esperamos
            Class<?> typeHandler = handler.getPayloadType();
            // ahora deserializers:
            Object deserializedPayload = mapper.readValue(payload.toString(), typeHandler);
            // invocamos el handler
            invokeHandler(handler, deserializedPayload);
        } catch (Exception e) {
            log.error("Error while routing message for topic {}", topic, e);
        }

    }

    // uncheked porque ya verificamos anteriormente que era.
    @SuppressWarnings("unchecked")
    private <T> void invokeHandler(MqttPayloadHandler<T> handler, Object payload) {
        T typePayload = (T) payload;
        handler.handle(typePayload);
    }
}
