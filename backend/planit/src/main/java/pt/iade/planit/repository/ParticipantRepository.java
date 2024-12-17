package pt.iade.planit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.iade.planit.entity.Participant;

public interface ParticipantRepository extends JpaRepository<Participant, Integer> {

}
