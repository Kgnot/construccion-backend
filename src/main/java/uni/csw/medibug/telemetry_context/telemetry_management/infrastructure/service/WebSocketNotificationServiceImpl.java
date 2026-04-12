package uni.csw.medibug.telemetry_context.telemetry_management.infrastructure.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import uni.csw.medibug.telemetry_context.telemetry_management.application.service.UserNotificationService;

@Service
public class WebSocketNotificationServiceImpl implements UserNotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketNotificationServiceImpl(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void notify(String userId, String destination, Object payload) {
        // Internamente, Spring busca la conexión que tiene el "userId" en sus atributos
        // y le manda el payload.
        messagingTemplate.convertAndSendToUser(
                userId,
                destination, // segun destino, o sea: /../../.. del ws
                payload
        );
    }
}
