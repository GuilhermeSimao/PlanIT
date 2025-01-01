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
        // Valida o ID do usuário
        if (eventDto.getUserId() == null) {
            throw new IllegalArgumentException("User ID must not be null");
        }

        // Busca o usuário
        User user = userRepository.findById(eventDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + eventDto.getUserId()));

        // Cria o evento
        Event event = new Event();
        event.setTitle(eventDto.getTitle());
        event.setDescription(eventDto.getDescription());
        event.setDate(eventDto.getDate());
        event.setPhotoUrl(eventDto.getPhotoUrl());
        event.setUser(user);

        // Verifica e salva a localização
        if (eventDto.getLatitude() != null && eventDto.getLongitude() != null) {
            Location location = new Location();
            location.setLatitude(eventDto.getLatitude());
            location.setLongitude(eventDto.getLongitude());
            location.setAddress(eventDto.getAddress());

            Location savedLocation = locationRepository.save(location);
            event.setLocation(savedLocation); // Vincula a localização ao evento
        }

        // Salva o evento
        eventRepository.save(event);
    }


    @Override
    public List<EventDTO> findEventsByUserId(Integer userId) {
        List<Event> events = eventRepository.findByUserId(userId);

        return events.stream().map(event -> {
            EventDTO dto = new EventDTO();
            dto.setId(event.getId());
            dto.setTitle(event.getTitle());
            dto.setDescription(event.getDescription());
            dto.setDate(event.getDate());
            dto.setPhotoUrl(event.getPhotoUrl());
            dto.setUserId(event.getUser().getId());
            dto.setUserName(event.getUser().getName());
            dto.setUserEmail(event.getUser().getEmail());

            if (event.getLocation() != null) {
                dto.setLocationId(event.getLocation().getId());
                dto.setLatitude(event.getLocation().getLatitude());
                dto.setLongitude(event.getLocation().getLongitude());
                dto.setAddress(event.getLocation().getAddress());
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
            dto.setAddress(event.getLocation().getAddress());
            dto.setLatitude(event.getLocation().getLatitude());
            dto.setLongitude(event.getLocation().getLongitude());
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
