package pt.iade.planit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.iade.planit.entity.Event;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Integer> {
    List<Event> findByUserId(Integer userId);
}
