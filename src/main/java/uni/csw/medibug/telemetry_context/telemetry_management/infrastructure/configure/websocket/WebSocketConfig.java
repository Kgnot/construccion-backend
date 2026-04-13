package uni.csw.medibug.telemetry_context.telemetry_management.infrastructure.configure.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import uni.csw.medibug.telemetry_context.telemetry_management.infrastructure.configure.utils.UserHandshakeInterceptor;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final UserHandshakeInterceptor userInterceptor;

    public WebSocketConfig(UserHandshakeInterceptor userInterceptor) {
        this.userInterceptor = userInterceptor;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Broker para /topic (broadcast) y /queue (punto a punto)
        config.enableSimpleBroker("/topic", "/queue");
        // Prefijo para mensajes desde cliente a servidor
        config.setApplicationDestinationPrefixes("/app");
        // /user es el prefijo para mensajes usuario-específicos
        // Cuando usas convertAndSendToUser(), Spring automáticamente lo envía a /user/{userId}/...
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .addInterceptors(userInterceptor) // Interceptor para extraer userId del token
                .setHandshakeHandler(new UserPrincipalHandshakeHandler()) // Asocia sesiones con Principal basado en userId
                .setAllowedOrigins("*"); // todo, toca cambiar url frontend
        // para viejos fallback:
        registry.addEndpoint("/ws")
                .addInterceptors(userInterceptor)
                .setHandshakeHandler(new UserPrincipalHandshakeHandler())
                .withSockJS();
    }
}