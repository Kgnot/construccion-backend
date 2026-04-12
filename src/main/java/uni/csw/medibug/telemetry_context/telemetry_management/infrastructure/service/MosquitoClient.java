package uni.csw.medibug.telemetry_context.telemetry_management.infrastructure.service;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uni.csw.medibug.telemetry_context.telemetry_management.application.port.out.MQTTClient;
import uni.csw.medibug.telemetry_context.telemetry_management.application.router.MqttMessageRouter;

@Service
@Slf4j
public class MosquitoClient implements MQTTClient {

    private final MqttMessageRouter router;
    private IMqttAsyncClient pahoClient;

    public MosquitoClient(MqttMessageRouter router,
                          @Value("${mqtt.broker.url}") String brokerUrl,
                          @Value("${mqtt.client.id}") String clientId) {
        this.router = router;
        try {
            connect(brokerUrl, clientId);
        } catch (MqttException e) {
            log.error("Error crítico al inicializar el cliente MQTT: {}", e.getMessage());
            // Si no podemos inicializar, es mejor que la aplicación no arranque.
            throw new RuntimeException("No se pudo crear el cliente MQTT", e);
        }
    }


    @Override
    public void subscribe(String topic) {
        try {
            this.pahoClient.subscribe(topic, 1);
            log.info("Suscrito exitosamente al topic: {}", topic);
        } catch (MqttException e) {
            log.error("Error subscribing to topic {}: {}", topic, e.getMessage());
        }
    }

    @Override
    public void unsubscribe(String topic) {

    }

    @Override
    public void publish(String topic, String payload) {
        try {
            MqttMessage message = new MqttMessage(payload.getBytes());
            message.setQos(1);
            pahoClient.publish(topic, message);
        } catch (MqttException e) {
            log.error("Error publicando en {}: {}", topic, e.getMessage());
        }
    }

    // conffigaciones private:
    private void connect(
            String brokerUrl,
            String clientId
    ) throws MqttException {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true); // reconecte solo
        options.setCleanSession(true); //
        options.setConnectionTimeout(10); // timeout
        options.setKeepAliveInterval(60); // interval

        // creamos el cliente paho y el memoryPersistence para que no guarde estados en disco
        this.pahoClient = new MqttAsyncClient(brokerUrl, clientId, new MemoryPersistence());
        // asignamos el callback
        this.pahoClient.setCallback(new MqttCallbackExtended() {

            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                log.info("connect complete");
                subscribeAllTopics(); // llamad a la clase MosquitClient
            }

            @Override
            public void connectionLost(Throwable cause) {
                log.error("Conexión con MQTT perdida permanentemente: {}", cause.getMessage());
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                try {
                    String payload = new String(message.getPayload());
                    log.debug("Mensaje recibido en [{}]: {}", topic, payload);
                    // Delegamos al Router que creamos en el paso anterior
                    router.routeMessage(topic, payload);

                } catch (Exception e) {
                    log.error("Error procesando el mensaje del topic {}: {}", topic, e.getMessage(), e);
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                // Se usa si publicas mensajes con QoS > 0. Ignorar por ahora./
            }
        });
        log.info("Intentando conectar a Mosquitto en {}...", brokerUrl);
        this.pahoClient.connect(options);
    }

    // private para subs:
    private void subscribeAllTopics() {

        for (String topic : router.getRegisteredTopics()) {
            this.subscribe(topic);
        }
    }
}
