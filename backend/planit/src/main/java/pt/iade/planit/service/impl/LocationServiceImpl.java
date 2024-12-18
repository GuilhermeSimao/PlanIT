package pt.iade.planit.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.iade.planit.dto.LocationDTO;
import pt.iade.planit.entity.Location;
import pt.iade.planit.repository.LocationRepository;
import pt.iade.planit.service.LocationService;

import java.util.List;

@Service
public class LocationServiceImpl implements LocationService {

    @Autowired
    private LocationRepository locationRepository;

    @Override
    public LocationDTO addLocation(LocationDTO locationDTO) {
        Location location = new Location();
        location.setAddress(locationDTO.getAddress());
        location.setLatitude(locationDTO.getLatitude());
        location.setLongitude(locationDTO.getLongitude());

        Location savedLocation = locationRepository.save(location);

        return new LocationDTO(
                savedLocation.getId(),
                savedLocation.getAddress(),
                savedLocation.getLatitude(),
                savedLocation.getLongitude()
        );
    }

    @Override
    public List<LocationDTO> getAllLocations() {
        List<Location> locations = locationRepository.findAll();

        return locations.stream().map(location -> new LocationDTO(
                location.getId(),
                location.getAddress(),
                location.getLatitude(),
                location.getLongitude()
        )).toList();
    }

    @Override
    public LocationDTO getLocationById(Integer locationId) {
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new RuntimeException("Location not found with ID: " + locationId));

        return new LocationDTO(
                location.getId(),
                location.getAddress(),
                location.getLatitude(),
                location.getLongitude()
        );
    }

    @Override
    public void deleteLocation(Integer locationId) {
        locationRepository.deleteById(locationId);
    }
}
