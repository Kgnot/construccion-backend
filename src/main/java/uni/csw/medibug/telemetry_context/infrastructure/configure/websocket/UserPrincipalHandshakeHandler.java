package uni.csw.medibug.telemetry_context.infrastructure.configure.websocket;

import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

@Slf4j
public class UserPrincipalHandshakeHandler extends DefaultHandshakeHandler {

    /**
     * Determina el Principal basado en el userId del query param
     * Spring STOMP lo usa para asociar sesiones con usuarios
     */
    @Override
    protected Principal determineUser(
            @NonNull ServerHttpRequest request,
            @NonNull WebSocketHandler wsHandler,
            Map<String, Object> attributes) {

        // El interceptor ya extrajo el userId y lo puso en attributes
        String userId = (String) attributes.get("userId");

        if (userId == null || userId.isEmpty()) {
            log.warn("No userId found in handshake attributes, using anonymous");
            userId = "anonymous-" + System.currentTimeMillis();
        }

        log.debug("Handshake successful for userId: {}", userId);

        // Retorna un Principal con el userId
        return new UserPrincipal(userId);
    }

    /**
     * Clase interna simple que implementa Principal
     */
    public static class UserPrincipal implements Principal {
        private final String userId;

        public UserPrincipal(String userId) {
            this.userId = userId;
        }

        @Override
        public String getName() {
            return userId;
        }

        @Override
        public String toString() {
            return "UserPrincipal{" + userId + "}";
        }
    }
}