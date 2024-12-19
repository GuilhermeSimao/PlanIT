package pt.iade.planit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pt.iade.planit.entity.Participant;
import java.util.List;
import java.util.Optional;

public interface ParticipantRepository extends JpaRepository<Participant, Integer> {
    List<Participant> findByEventId(Integer eventId);
    List<Participant> findByUserId(Integer userId);
    @Query("SELECT p FROM Participant p WHERE p.event.id = :eventId AND p.user.email = :email")
    Optional<Participant> findByEventIdAndUserEmail(@Param("eventId") Integer eventId, @Param("email") String email);
    List<Participant> findByUserIdAndStatus(Integer userId, Participant.Status status);


}
