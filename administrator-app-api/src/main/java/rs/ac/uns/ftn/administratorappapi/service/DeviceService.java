package rs.ac.uns.ftn.administratorappapi.service;

import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.administratorappapi.dto.DeviceDTO;
import rs.ac.uns.ftn.administratorappapi.model.Device;

@Service
public interface DeviceService {

    Device addDevice(DeviceDTO deviceDTO);
}
