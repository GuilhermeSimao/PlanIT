package pt.iade.planit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationDTO {
    private Integer id;            // ID da localização
    private String address;        // Endereço (opcional)
    private Double latitude;       // Latitude
    private Double longitude;      // Longitude
}
