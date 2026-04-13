package uni.csw.medibug.telemetry_context.telemetry_management.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import uni.csw.medibug.telemetry_context.telemetry_management.application.event.TelemetryAcceptedEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class TelemetryWebSocketProjector {
    // projeccion destinada a websocket
    private final UserNotificationService notificationService;

    @Async("taskExecutor")
    @EventListener
    public void onAccepted(TelemetryAcceptedEvent event) {
        // generamos un topic destinatario tipo websocket
        String destination = switch (event.telemetryType()) {
            case "blood" -> "/queue/blood";
            case "electrolyte" -> "/queue/electrolyte";
            case "metabolic" -> "/queue/metabolic";
            case "lipid" -> "/queue/lipid";
            default -> null;
        };
        if (destination == null) {
            log.warn("Telemetry type no soportado para WS : {}", event.telemetryType());
            return;
        }

        try {
            notificationService.notify(event.userId(), destination, event.payload());
        } catch (Exception e) {
            log.error("Error notificando WS type={}, userId={}", event.telemetryType(), event.userId(), e);
        }
    }


}
