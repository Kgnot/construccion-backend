package uni.csw.medibug.telemetry_context.telemetry_management.application.payload_handler;

public interface MqttPayloadHandler<T> {
    // devuelve la clase de objeto para la serialización
    Class<T> getPayloadType();

    // Lógica de lo que va a hacer con el payload
    void handle(T Payload);
}
