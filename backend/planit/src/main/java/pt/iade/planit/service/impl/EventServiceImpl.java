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
        if (eventDto.getUserId() == null) {
            throw new IllegalArgumentException("User ID must not be null");
        }

        // Buscar o usuário associado ao evento
        User user = userRepository.findById(eventDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + eventDto.getUserId()));

        // Criar o objeto do evento
        Event event = new Event();
        event.setTitle(eventDto.getTitle());
        event.setDescription(eventDto.getDescription());
        event.setDate(eventDto.getDate());

        // Configurar a URL da foto (caso não seja fornecida, usar um padrão)
        String defaultImageUrl = "https://example.com/images/default.jpg";
        event.setPhotoUrl(eventDto.getPhotoUrl() != null && !eventDto.getPhotoUrl().isEmpty()
                ? eventDto.getPhotoUrl()
                : defaultImageUrl);

        event.setUser(user);

        // Logs para verificar os dados recebidos
        System.out.println("Latitude: " + eventDto.getLatitude());
        System.out.println("Longitude: " + eventDto.getLongitude());
        System.out.println("Address: " + eventDto.getAddress());

        // Processar a localização
        if (eventDto.getLatitude() != null && eventDto.getLongitude() != null && eventDto.getAddress() != null) {
            Location location = new Location();
            location.setLatitude(eventDto.getLatitude());
            location.setLongitude(eventDto.getLongitude());
            location.setAddress(eventDto.getAddress());

            // Salvar a localização e associá-la ao evento
            location = locationRepository.save(location);
            System.out.println("Saving Location: " + location);
            event.setLocation(location);
        } else {
            System.out.println("Localização não fornecida para o evento.");
        }

        // Salvar o evento no banco de dados
        eventRepository.save(event);
        System.out.println("Event Saved: " + event);
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
                dto.setAddress(event.getLocation().getAddress());
                dto.setLatitude(event.getLocation().getLatitude());
                dto.setLongitude(event.getLocation().getLongitude());
            } else {
                dto.setAddress(null); // Explicitamente defina null se não houver localização
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

        // Configurar localização se estiver presente
        if (event.getLocation() != null) {
            Location location = event.getLocation();
            dto.setAddress(location.getAddress());
            dto.setLatitude(location.getLatitude());
            dto.setLongitude(location.getLongitude());
            System.out.println("Localização encontrada: " + location.getAddress());
        } else {
            System.out.println("Evento sem localização associada.");
        }

        // Mapear participantes
        dto.setParticipants(event.getParticipants().stream().map(participant -> {
            ParticipantDTO participantDTO = new ParticipantDTO();
            participantDTO.setUserId(participant.getUser().getId());
            participantDTO.setUserName(participant.getUser().getName());
            participantDTO.setStatus(participant.getStatus().toString());
            return participantDTO;
        }).toList());

        System.out.println("Evento processado: " + dto);
        return dto;
    }



    @Override
    public void deleteEventById(Integer eventId) {
        // Validate if the event exists
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with ID: " + eventId));
        eventRepository.delete(event);
    }

    @Override
    public List<EventDTO> searchEvents(String title, String description, String date, String address) {
        List<Event> filteredEvents = eventRepository.findAll(); // Começa com todos os eventos

        if (title != null && !title.isEmpty()) {
            filteredEvents = filteredEvents.stream()
                    .filter(event -> event.getTitle().toLowerCase().contains(title.toLowerCase()))
                    .toList();
        }

        if (description != null && !description.isEmpty()) {
            filteredEvents = filteredEvents.stream()
                    .filter(event -> event.getDescription() != null &&
                            event.getDescription().toLowerCase().contains(description.toLowerCase()))
                    .toList();
        }

        if (date != null && !date.isEmpty()) {
            filteredEvents = filteredEvents.stream()
                    .filter(event -> event.getDate().toLocalDate().toString().equals(date))
                    .toList();
        }

        if (address != null && !address.isEmpty()) {
            filteredEvents = filteredEvents.stream()
                    .filter(event -> event.getLocation() != null &&
                            event.getLocation().getAddress().toLowerCase().contains(address.toLowerCase()))
                    .toList();
        }

        // Converte para DTO
        return filteredEvents.stream().map(event -> {
            EventDTO dto = new EventDTO();
            dto.setId(event.getId());
            dto.setTitle(event.getTitle());
            dto.setDescription(event.getDescription());
            dto.setDate(event.getDate());
            dto.setPhotoUrl(event.getPhotoUrl());
            dto.setUserId(event.getUser().getId());
            dto.setUserName(event.getUser().getName());

            if (event.getLocation() != null) {
                dto.setAddress(event.getLocation().getAddress());
                dto.setLatitude(event.getLocation().getLatitude());
                dto.setLongitude(event.getLocation().getLongitude());
            } else {
                dto.setAddress(null); // Explicitamente defina null se não houver localização
            }

            return dto;
        }).toList();
    }

    @Override
    public List<EventDTO> findConfirmedParticipatingEvents(Integer userId) {
        List<Event> events = eventRepository.findConfirmedParticipatingEvents(userId);

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
                dto.setAddress(event.getLocation().getAddress());
                dto.setLatitude(event.getLocation().getLatitude());
                dto.setLongitude(event.getLocation().getLongitude());
            } else {
                dto.setAddress(null); // Explicitamente defina null se não houver localização
            }

            return dto;
        }).toList();
    }

    @Override
    public void updateEvent(Integer eventId, EventDTO eventDTO) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with ID: " + eventId));

        event.setTitle(eventDTO.getTitle());
        event.setDescription(eventDTO.getDescription());
        event.setDate(eventDTO.getDate());
        event.setPhotoUrl(eventDTO.getPhotoUrl());

        // Atualizar localização
        if (eventDTO.getLatitude() != null && eventDTO.getLongitude() != null && eventDTO.getAddress() != null) {
            Location location = event.getLocation();
            if (location == null) {
                location = new Location();
            }
            location.setLatitude(eventDTO.getLatitude());
            location.setLongitude(eventDTO.getLongitude());
            location.setAddress(eventDTO.getAddress());
            location = locationRepository.save(location);
            event.setLocation(location);
        }

        eventRepository.save(event);
    }



}
