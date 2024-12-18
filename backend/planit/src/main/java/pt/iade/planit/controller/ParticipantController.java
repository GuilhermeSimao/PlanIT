package pt.iade.planit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.iade.planit.dto.ParticipantDTO;
import pt.iade.planit.entity.Participant;
import pt.iade.planit.service.ParticipantService;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RestController
@RequestMapping("/participant")
public class ParticipantController {

    @Autowired
    private ParticipantService participantService;

    @PostMapping("/add")
    public ResponseEntity<ParticipantDTO> addParticipant(
            @RequestParam Integer eventId,
            @RequestParam Integer userId,
            @RequestParam("status") Participant.Status status) {
        return ResponseEntity.ok(participantService.addParticipant(eventId, userId, status));
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<ParticipantDTO>> getParticipantsByEventId(@PathVariable Integer eventId) {
        return ResponseEntity.ok(participantService.getParticipantsByEventId(eventId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ParticipantDTO>> getParticipantsByUserId(@PathVariable Integer userId) {
        return ResponseEntity.ok(participantService.getParticipantsByUserId(userId));
    }

    @DeleteMapping("/delete/{participantId}")
    public ResponseEntity<Void> removeParticipant(@PathVariable Integer participantId) {
        participantService.removeParticipant(participantId);
        return ResponseEntity.noContent().build();
    }
}
