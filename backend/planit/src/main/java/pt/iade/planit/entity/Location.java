package pt.iade.planit.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Location {
    @Id
    @GeneratedValue
    private Integer id;
    private String address;
    private String latitude;
    private String longitude;

}