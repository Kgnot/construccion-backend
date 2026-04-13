package uni.csw.medibug.telemetry_context.telemetry_management.infrastructure.service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
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
    private final String brokerUrl;
    private final String clientId;

    private IMqttAsyncClient pahoClient;

    public MosquitoClient(
            MqttMessageRouter router,
            @Value("${mqtt.broker.url}") String brokerUrl,
            @Value("${mqtt.client.id}") String clientId
    ) {
        this.router = router;
        this.brokerUrl = brokerUrl;
        this.clientId = clientId;
    }

    /**
     * Inicialización controlada después de construir el bean.
     */
    @PostConstruct
    public void init() {
        try {
            connect(brokerUrl, clientId);
        } catch (MqttException e) {
            log.error("Error crítico al inicializar cliente MQTT: {}", e.getMessage(), e);
            throw new RuntimeException("No se pudo crear cliente MQTT", e);
        }
    }

    @Override
    public void subscribe(String topic) {
        try {
            if (pahoClient != null && pahoClient.isConnected()) {
                pahoClient.subscribe(topic, 1).waitForCompletion();
                log.info("Suscrito exitosamente al topic: {}", topic);
            } else {
                log.warn("No se pudo suscribir a {} porque MQTT no está conectado.", topic);
            }
        } catch (MqttException e) {
            log.error("Error subscribing to topic {}: {}", topic, e.getMessage(), e);
        }
    }

    @Override
    public void unsubscribe(String topic) {
        try {
            if (pahoClient != null && pahoClient.isConnected()) {
                pahoClient.unsubscribe(topic).waitForCompletion();
                log.info("Desuscrito exitosamente del topic: {}", topic);
            }
        } catch (MqttException e) {
            log.error("Error unsubscribing from topic {}: {}", topic, e.getMessage(), e);
        }
    }

    @Override
    public void publish(String topic, String payload) {
        try {
            if (pahoClient != null && pahoClient.isConnected()) {
                MqttMessage message = new MqttMessage(payload.getBytes());
                message.setQos(1);
                pahoClient.publish(topic, message).waitForCompletion();
            } else {
                log.warn("No se pudo publicar en {} porque MQTT no está conectado.", topic);
            }
        } catch (MqttException e) {
            log.error("Error publicando en {}: {}", topic, e.getMessage(), e);
        }
    }

    /**
     * Configuración y conexión MQTT.
     */
    private void connect(String brokerUrl, String clientId) throws MqttException {

        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);
        options.setConnectionTimeout(10);
        options.setKeepAliveInterval(60);

        this.pahoClient = new MqttAsyncClient(
                brokerUrl,
                clientId,
                new MemoryPersistence()
        );

        this.pahoClient.setCallback(new MqttCallbackExtended() {

            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                log.info("MQTT connect complete. reconnect={}, server={}", reconnect, serverURI);
                subscribeAllTopics();
            }

            @Override
            public void connectionLost(Throwable cause) {
                log.error("Conexión MQTT perdida: {}", cause.getMessage(), cause);
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) {
                try {
                    String payload = new String(message.getPayload());
                    log.debug("Mensaje recibido en [{}]: {}", topic, payload);

                    router.routeMessage(topic, payload);

                } catch (Exception e) {
                    log.error(
                            "Error procesando mensaje del topic {}: {}",
                            topic,
                            e.getMessage(),
                            e
                    );
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                // QoS > 0 acknowledgment callback
            }
        });

        log.info("Intentando conectar a Mosquitto en {}...", brokerUrl);

        this.pahoClient.connect(options).waitForCompletion();

        log.info("Cliente MQTT conectado correctamente.");
    }

    /**
     * Suscribe automáticamente todos los topics registrados.
     */
    private void subscribeAllTopics() {
        for (String topic : router.getRegisteredTopics()) {
            subscribe(topic);
        }
    }

    /**
     * Cleanup seguro al apagar aplicación.
     */
    @PreDestroy
    public void cleanup() {
        try {
            if (pahoClient != null) {

                if (pahoClient.isConnected()) {
                    log.info("Desconectando cliente MQTT...");
                    pahoClient.disconnect().waitForCompletion();
                }

                pahoClient.close();
                log.info("Cliente MQTT cerrado correctamente.");
            }

        } catch (MqttException e) {
            log.error("Error cerrando cliente MQTT: {}", e.getMessage(), e);
        }
    }
}