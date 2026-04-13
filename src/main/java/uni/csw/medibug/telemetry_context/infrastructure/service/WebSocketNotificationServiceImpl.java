package uni.csw.medibug.telemetry_context.infrastructure.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import uni.csw.medibug.telemetry_context.application.service.UserNotificationService;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebSocketNotificationServiceImpl implements UserNotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void notify(String userId, String destination, Object payload) {
        try {
            // convertAndSendToUser() busca sesiones con ese usuario (Principal.getName() == userId)
            // y envía el payload a esa sesión con la ruta especificada.
            //
            // Ejemplo: userId="patient-123", destination="/queue/blood"
            //         → se envía a: /user/patient-123/queue/blood
            messagingTemplate.convertAndSendToUser(
                    userId,
                    destination,
                    payload
            );
            log.debug("WebSocket notificación enviada: userId={}, destination={}", userId, destination);
        } catch (Exception e) {
            log.error("Error enviando notificación WebSocket: userId={}, destination={}", userId, destination, e);
        }
    }
}


/*
*
* Aqui voy a copiar el proceso porque se me hace interesante tenerlo para aprender:
*
* 1. Cliente conecta: GET /ws?userId=patient-123

2. UserHandshakeInterceptor
   ↓
   attributes.put("userId", "patient-123")

3. UserPrincipalHandshakeHandler.determineUser()
   ↓
   return new UserPrincipal("patient-123")

4. Sesión STOMP abierta con Principal.getName() = "patient-123"

5. TelemetryWebSocketProjector publica evento
   ↓
   notificationService.notify("patient-123", "/queue/blood", payload)

6. convertAndSendToUser("patient-123", "/queue/blood", payload)
   ↓
   busca sesiones donde Principal.getName() == "patient-123"
   ↓
   envía a: /user/patient-123/queue/blood
*
* */