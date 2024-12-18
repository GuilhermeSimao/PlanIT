package pt.iade.planit.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Location")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String address;

    @Column(precision = 9)
    private Double latitude;

    @Column(precision = 9)
    private Double longitude;
}
