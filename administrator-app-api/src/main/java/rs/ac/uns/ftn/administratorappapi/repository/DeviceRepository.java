package rs.ac.uns.ftn.administratorappapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.uns.ftn.administratorappapi.model.Device;

public interface DeviceRepository extends JpaRepository<Device, Long> {
}
