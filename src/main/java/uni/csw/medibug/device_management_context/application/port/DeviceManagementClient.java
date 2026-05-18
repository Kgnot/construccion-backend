package uni.csw.medibug.device_management_context.application.port;

import uni.csw.medibug.device_management_context.application.dto.DeviceRequestActivateDTO;
import uni.csw.medibug.device_management_context.application.dto.DeviceRequestDTO;
import uni.csw.medibug.device_management_context.application.dto.DeviceRespondDTO;

public interface DeviceManagementClient {
    DeviceRespondDTO registerDevice(DeviceRequestDTO deviceRequestDTO);
    void activateDevice(DeviceRequestActivateDTO deviceRequestActivateDTO);
}

