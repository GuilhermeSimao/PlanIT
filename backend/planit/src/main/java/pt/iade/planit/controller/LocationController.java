package pt.iade.planit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.iade.planit.dto.LocationDTO;
import pt.iade.planit.entity.Location;
import pt.iade.planit.service.LocationService;

import java.util.List;

@RestController
@RequestMapping("/location")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @PostMapping("/add")
    public ResponseEntity<LocationDTO> addLocation(@RequestBody LocationDTO locationDTO) {
        return ResponseEntity.ok(locationService.addLocation(locationDTO));
    }

    @GetMapping("/all")
    public ResponseEntity<List<LocationDTO>> getAllLocations() {
        return ResponseEntity.ok(locationService.getAllLocations());
    }

    @GetMapping("/{locationId}")
    public ResponseEntity<LocationDTO> getLocationById(@PathVariable Integer locationId) {
        return ResponseEntity.ok(locationService.getLocationById(locationId));
    }

    @DeleteMapping("/delete/{locationId}")
    public ResponseEntity<Void> deleteLocation(@PathVariable Integer locationId) {
        locationService.deleteLocation(locationId);
        return ResponseEntity.noContent().build();
    }
}
