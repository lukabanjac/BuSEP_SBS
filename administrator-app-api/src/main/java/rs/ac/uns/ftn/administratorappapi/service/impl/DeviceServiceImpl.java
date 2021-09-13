package rs.ac.uns.ftn.administratorappapi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.administratorappapi.dto.DeviceDTO;
import rs.ac.uns.ftn.administratorappapi.model.Device;
import rs.ac.uns.ftn.administratorappapi.model.TrustedOrganization;
import rs.ac.uns.ftn.administratorappapi.repository.DeviceRepository;
import rs.ac.uns.ftn.administratorappapi.repository.TrustedOrganizationRepository;
import rs.ac.uns.ftn.administratorappapi.service.DeviceService;

@Service
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private TrustedOrganizationRepository trustedOrganizationRepository;

    @Override
    public Device addDevice(DeviceDTO deviceDTO) {
        TrustedOrganization trustedOrganization = trustedOrganizationRepository.getOne(Long.parseLong(deviceDTO.getTrustedOrganisationId()));
        Device device = new Device();
        device.setName(deviceDTO.getName());
        device.setTrusted_organization(trustedOrganization);
        deviceRepository.save(device);
        return device;
    }
}
