package uni.csw.medibug.device_management_context.application.dto;

public record DeviceRespondDTO(
        String deviceId,
        String userId,
        String deviceType,
        Integer interval,
        String status
) {
}

