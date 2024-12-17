package pt.iade.planit.service;

import pt.iade.planit.dto.EventDTO;

import java.util.List;

public interface EventService {
    void createEvent(EventDTO eventDto);
    List<EventDTO> findEventsByUserId(Integer userId);
    List<EventDTO> getAllEvents();  // New method for getting all events
    void deleteEventById(Integer eventId);  // New method for deleting an event by ID
}
