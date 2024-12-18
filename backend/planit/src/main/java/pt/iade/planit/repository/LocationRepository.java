package pt.iade.planit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.iade.planit.entity.Location;

public interface LocationRepository extends JpaRepository<Location, Integer> {
}
