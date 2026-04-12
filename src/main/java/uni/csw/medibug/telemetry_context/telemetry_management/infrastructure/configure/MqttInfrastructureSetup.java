package uni.csw.medibug.telemetry_context.telemetry_management.infrastructure.configure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.annotation.Configuration;
import uni.csw.medibug.telemetry_context.telemetry_management.application.payload_handler.MqttPayloadHandler;
import uni.csw.medibug.telemetry_context.telemetry_management.application.router.MqttMessageRouter;
import uni.csw.medibug.telemetry_context.telemetry_management.infrastructure.configure.shared.MqttTopic;

import java.util.List;

@Configuration
@Slf4j
public class MqttInfrastructureSetup {

    private final MqttMessageRouter router;
    private final List<MqttPayloadHandler<?>> handlers; // Spring inyecta todos automágicamente

    public MqttInfrastructureSetup(MqttMessageRouter router, List<MqttPayloadHandler<?>> handlers) {
        this.router = router;
        this.handlers = handlers;
        configureRouter();
    }

    private void configureRouter() {
        for (MqttPayloadHandler<?> handler : handlers) {
            Class<?> targetClass = AopUtils.getTargetClass(handler);
            MqttTopic annotation = targetClass.getAnnotation(MqttTopic.class);
            if (annotation != null) {
                router.registerHandler(annotation.value(), handler);
                log.info("Registrando handler {} para topic {}", targetClass.getSimpleName(), annotation.value());
            }
        }
    }
}