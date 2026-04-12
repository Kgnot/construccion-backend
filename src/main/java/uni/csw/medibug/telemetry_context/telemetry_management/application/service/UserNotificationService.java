package uni.csw.medibug.telemetry_context.telemetry_management.application.service;

public interface UserNotificationService {
    /*
     * Notificamos al usuario con el id, mensaje y un payload
     * */
    void notify(String userId, String destination, Object payload);

}
