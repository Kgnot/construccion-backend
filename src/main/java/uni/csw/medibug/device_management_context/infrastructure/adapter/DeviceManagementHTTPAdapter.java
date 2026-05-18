package uni.csw.medibug.device_management_context.infrastructure.adapter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import uni.csw.medibug.device_management_context.application.dto.DeviceRequestActivateDTO;
import uni.csw.medibug.device_management_context.application.dto.DeviceRequestDTO;
import uni.csw.medibug.device_management_context.application.dto.DeviceRespondDTO;
import uni.csw.medibug.device_management_context.application.port.DeviceManagementClient;

@Component
@Slf4j
public class DeviceManagementHTTPAdapter implements DeviceManagementClient {

    private final RestTemplate restTemplate;

    @Value("${device-management.base-url:http://localhost:8081}")
    private String baseUrl;

    public DeviceManagementHTTPAdapter(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public DeviceRespondDTO registerDevice(DeviceRequestDTO deviceRequestDTO) {
        try {
            String url = baseUrl + "/api/v1/device_management/register";
            log.info("Registering device with ID: {} to URL: {}", deviceRequestDTO.deviceId(), url);

            DeviceRespondDTO response = restTemplate.postForObject(
                    url,
                    deviceRequestDTO,
                    DeviceRespondDTO.class
            );

            log.info("Device registered successfully: {}", response.deviceId());
            return response;
        } catch (RestClientException e) {
            log.error("Error registering device: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to register device with device management service", e);
        }
    }

    @Override
    public void activateDevice(DeviceRequestActivateDTO deviceRequestActivateDTO) {
        try {
            String url = baseUrl + "/api/v1/device_management/activate";
            log.info("Activating device with ID: {} to URL: {}", deviceRequestActivateDTO.deviceId(), url);

            restTemplate.postForObject(
                    url,
                    deviceRequestActivateDTO,
                    Boolean.class
            );

            log.info("Device activated successfully: {}", deviceRequestActivateDTO.deviceId());
        } catch (RestClientException e) {
            log.error("Error activating device: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to activate device with device management service", e);
        }
    }
}

