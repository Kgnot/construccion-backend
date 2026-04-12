package uni.csw.medibug.telemetry_context.telemetry_management.application.port.out;

public interface MQTTClient {

    void subscribe(String topic);

    void unsubscribe(String topic);

    void publish(String topic, String payload);
}
