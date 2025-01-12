package pt.iade.planit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.iade.planit.dto.EventDTO;
import pt.iade.planit.service.EventService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/event")
public class EventController {

    @Autowired
    private EventService eventService;

    // Add Event
    @PostMapping("/add")
    public ResponseEntity<Map<String, String>> addEvent(@RequestBody EventDTO event) {
        System.out.println("Payload recebido: " + event);
        eventService.createEvent(event);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Event created successfully");
        return ResponseEntity.ok(response);
    }


    // Get Events by User ID
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<EventDTO>> getEventsByUserId(@PathVariable Integer userId) {
        List<EventDTO> events = eventService.findEventsByUserId(userId);
        return ResponseEntity.ok(events);
    }

    // Fetch all events
    @GetMapping("/all")
    public ResponseEntity<List<EventDTO>> getAllEvents() {
        List<EventDTO> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDTO> getEventDetails(@PathVariable Integer id) {
        EventDTO event = eventService.getEventDetails(id);
        return ResponseEntity.ok(event);
    }

    // Delete event by ID
    @DeleteMapping("/delete/{eventId}")
    public ResponseEntity<String> deleteEvent(@PathVariable Integer eventId) {
        eventService.deleteEventById(eventId);
        return ResponseEntity.ok("Event successfully deleted");
    }

    @GetMapping("/search")
    public ResponseEntity<List<EventDTO>> searchEvents(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String address
    ) {
        List<EventDTO> events = eventService.searchEvents(title, description, date, address);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/participating/confirmed/{userId}")
    public ResponseEntity<List<EventDTO>> getConfirmedParticipatingEvents(@PathVariable Integer userId) {
        List<EventDTO> events = eventService.findConfirmedParticipatingEvents(userId);
        return ResponseEntity.ok(events);
    }


}
