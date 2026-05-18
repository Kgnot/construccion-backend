package uni.csw.medibug.device_management_context.infrastructure.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uni.csw.medibug.device_management_context.application.dto.DeviceRequestActivateDTO;
import uni.csw.medibug.device_management_context.application.dto.DeviceRequestDTO;
import uni.csw.medibug.device_management_context.application.dto.DeviceRespondDTO;
import uni.csw.medibug.device_management_context.application.port.DeviceManagementClient;
import uni.csw.medibug.shared.infrastructure.config.ApiError;
import uni.csw.medibug.shared.infrastructure.config.ApiResponse;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/v1/device_management")
@Slf4j
public class DeviceManagementController {

    private final DeviceManagementClient deviceManagementClient;

    public DeviceManagementController(DeviceManagementClient deviceManagementClient) {
        this.deviceManagementClient = deviceManagementClient;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerDevice(
            @RequestBody DeviceRequestDTO deviceRequestDTO,
            HttpServletRequest request) {
        log.info("Registering device: {}", deviceRequestDTO.deviceId());
        try {
            DeviceRespondDTO response = deviceManagementClient.registerDevice(deviceRequestDTO);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ApiResponse.success(response, "Device registered successfully"));
        } catch (Exception e) {
            log.error("Error registering device: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiError.of(
                            "DEVICE_REGISTRATION_ERROR",
                            "Error registering device: " + e.getMessage(),
                            List.of(e.getMessage()),
                            request.getRequestURI()
                    ));
        }
    }

    @PostMapping("/activate")
    public ResponseEntity<?> activateDevice(
            @RequestBody DeviceRequestActivateDTO deviceRequestActivateDTO,
            HttpServletRequest request) {
        log.info("Activating device: {}", deviceRequestActivateDTO.deviceId());
        try {
            deviceManagementClient.activateDevice(deviceRequestActivateDTO);
            return ResponseEntity
                    .ok(ApiResponse.success("Device activated successfully"));
        } catch (Exception e) {
            log.error("Error activating device: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiError.of(
                            "DEVICE_ACTIVATION_ERROR",
                            "Error activating device: " + e.getMessage(),
                            List.of(e.getMessage()),
                            request.getRequestURI()
                    ));
        }
    }
}

