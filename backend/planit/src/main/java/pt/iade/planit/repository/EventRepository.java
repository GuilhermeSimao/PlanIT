package pt.iade.planit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pt.iade.planit.entity.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Integer> {
    List<Event> findByUserId(Integer userId);
    @Query("SELECT e FROM Event e " +
            "WHERE (:title IS NULL OR LOWER(e.title) LIKE LOWER(CONCAT('%', :title, '%'))) " +
            "AND (:description IS NULL OR LOWER(e.description) LIKE LOWER(CONCAT('%', :description, '%'))) " +
            "AND (:date IS NULL OR e.date = :date) " +
            "AND (:address IS NULL OR LOWER(e.location.address) LIKE LOWER(CONCAT('%', :address, '%')))")
    List<Event> searchEvents(
            @Param("title") String title,
            @Param("description") String description,
            @Param("date") LocalDateTime date,
            @Param("address") String address
    );
    @Query("SELECT e FROM Event e JOIN e.participants p WHERE p.user.id = :userId AND p.status = 'CONFIRMED'")
    List<Event> findConfirmedParticipatingEvents(Integer userId);


}
