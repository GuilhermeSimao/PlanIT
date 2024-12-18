package pt.iade.planit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.iade.planit.entity.Participant;
import java.util.List;

public interface ParticipantRepository extends JpaRepository<Participant, Integer> {
    List<Participant> findByEventId(Integer eventId);
    List<Participant> findByUserId(Integer userId);
}
