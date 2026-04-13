package uni.csw.medibug.telemetry_context.application.port.out;

public interface MQTTClient {

    void subscribe(String topic);

    void unsubscribe(String topic);

    void publish(String topic, String payload);
}
