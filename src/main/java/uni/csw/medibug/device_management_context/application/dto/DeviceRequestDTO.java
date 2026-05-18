package uni.csw.medibug.device_management_context.application.dto;

public record DeviceRequestDTO(
        String deviceId,
        String userId,
        String deviceType,
        Integer interval
) {
}

