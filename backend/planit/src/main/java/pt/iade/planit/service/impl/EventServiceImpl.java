package pt.iade.planit.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.iade.planit.dto.EventDTO;
import pt.iade.planit.dto.ParticipantDTO;
import pt.iade.planit.entity.Event;
import pt.iade.planit.entity.User;
import pt.iade.planit.entity.Location;
import pt.iade.planit.repository.EventRepository;
import pt.iade.planit.repository.UserRepository;
import pt.iade.planit.repository.LocationRepository;
import pt.iade.planit.service.EventService;

import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Override
    public void createEvent(EventDTO eventDto) {
        // Validate User ID
        if (eventDto.getUserId() == null) {
            throw new IllegalArgumentException("User ID must not be null");
        }

        // Fetch the User entity
        User user = userRepository.findById(eventDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + eventDto.getUserId()));

        // Initialize the Event entity
        Event event = new Event();
        event.setTitle(eventDto.getTitle());
        event.setDescription(eventDto.getDescription());
        event.setDate(eventDto.getDate());
        event.setPhotoUrl(eventDto.getPhotoUrl());
        event.setUser(user);

        // Fetch Location (optional)
        if (eventDto.getLocationId() != null) {
            Location location = locationRepository.findById(eventDto.getLocationId())
                    .orElseThrow(() -> new RuntimeException("Location not found with ID: " + eventDto.getLocationId()));
            event.setLocation(location);
        }

        // Save and return the event
        eventRepository.save(event);
    }

    @Override
    public List<EventDTO> findEventsByUserId(Integer userId) {
        List<Event> events = eventRepository.findByUserId(userId);

        // Map Events to EventDTO
        return events.stream().map(event -> {
            EventDTO dto = new EventDTO();
            dto.setId(event.getId());
            dto.setTitle(event.getTitle());
            dto.setDescription(event.getDescription());
            dto.setDate(event.getDate());
            dto.setPhotoUrl(event.getPhotoUrl() != null ? event.getPhotoUrl() : "https://example.com/images/default.jpg");

            dto.setUserId(event.getUser().getId());       // Set user ID
            dto.setUserName(event.getUser().getName().trim());   // Set username
            dto.setUserEmail(event.getUser().getEmail()); // Set user email

            if (event.getLocation() != null) {
                dto.setLocationId(event.getLocation().getId());
            }

            return dto;
        }).toList();
    }

    @Override
    public List<EventDTO> getAllEvents() {
        List<Event> events = eventRepository.findAll();

        // Map Events to EventDTO
        return events.stream().map(event -> {
            EventDTO dto = new EventDTO();
            dto.setId(event.getId());
            dto.setTitle(event.getTitle());
            dto.setDescription(event.getDescription());
            dto.setDate(event.getDate());
            dto.setPhotoUrl(event.getPhotoUrl() != null ? event.getPhotoUrl() : "https://example.com/images/default.jpg");

            dto.setUserId(event.getUser().getId());
            dto.setUserName(event.getUser().getName().trim());
            dto.setUserEmail(event.getUser().getEmail());

            if (event.getLocation() != null) {
                dto.setLocationId(event.getLocation().getId());
            }

            return dto;
        }).toList();
    }

    @Override
    public EventDTO getEventDetails(Integer eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with ID: " + eventId));

        EventDTO dto = new EventDTO();
        dto.setId(event.getId());
        dto.setTitle(event.getTitle());
        dto.setDescription(event.getDescription());
        dto.setDate(event.getDate());
        dto.setPhotoUrl(event.getPhotoUrl());
        dto.setUserId(event.getUser().getId());
        dto.setUserName(event.getUser().getName());

        if (event.getLocation() != null) {
            dto.setLocationId(event.getLocation().getId());
        }

        dto.setParticipants(event.getParticipants().stream().map(participant -> {
            ParticipantDTO participantDTO = new ParticipantDTO();
            participantDTO.setUserId(participant.getUser().getId());
            participantDTO.setUserName(participant.getUser().getName());
            participantDTO.setStatus(participant.getStatus().toString());
            return participantDTO;
        }).toList());

        return dto;
    }


    @Override
    public void deleteEventById(Integer eventId) {
        // Validate if the event exists
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with ID: " + eventId));
        eventRepository.delete(event);
    }
}
