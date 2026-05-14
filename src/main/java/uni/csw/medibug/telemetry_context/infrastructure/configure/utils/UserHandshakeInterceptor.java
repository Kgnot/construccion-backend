package uni.csw.medibug.telemetry_context.infrastructure.configure.utils;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Component
public class UserHandshakeInterceptor implements HandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   @NonNull ServerHttpResponse response,
                                   @NonNull WebSocketHandler wsHandler,
                                   @NonNull Map<String, Object> attributes) {

        // Cuando el frontend conecte, esperamos que mande el ID así: /ws?userId=123
        String userId = UriComponentsBuilder.fromUri(request.getURI())
                .build()
                .getQueryParams()
                .getFirst("userId");

    // estamos recibiendo algo como: userId: ws%3A%2F%2Flocalhost%3A8080%2Fws%3FuserId%3Ddoctor-juan asi que debemos cambiarlo y solo obtener doctor-juan
        if (userId != null && userId.contains("%")) {
            try {
                userId = java.net.URLDecoder.decode(userId, "UTF-8");
                // Si después de decodificar sigue siendo una URL, extraer el param real
                if (userId.startsWith("ws://") || userId.startsWith("http://")) {
                    userId = UriComponentsBuilder.fromUriString(userId)
                            .build()
                            .getQueryParams()
                            .getFirst("userId");
                }
            } catch (Exception e) {
                return false;
            }
        }
        // solo si es null
        if (userId != null && !userId.isEmpty()) {
            attributes.put("userId", userId);
            return true;
        }

        return false; // no permitimos la conexión si no hay usuario de conexión
    }

    @Override
    public void afterHandshake(@NonNull ServerHttpRequest request,
                               @NonNull ServerHttpResponse response,
                               @NonNull WebSocketHandler wsHandler,
                               @Nullable Exception exception) {

    }
}
