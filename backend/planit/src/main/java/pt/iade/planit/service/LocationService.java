package pt.iade.planit.service;

import pt.iade.planit.dto.LocationDTO;

import java.util.List;

public interface LocationService {
    LocationDTO addLocation(LocationDTO location);
    List<LocationDTO> getAllLocations();
    LocationDTO getLocationById(Integer locationId);
    void deleteLocation(Integer locationId);
}
