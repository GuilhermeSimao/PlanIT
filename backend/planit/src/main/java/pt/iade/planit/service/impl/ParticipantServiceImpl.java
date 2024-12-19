package pt.iade.planit.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.iade.planit.dto.ParticipantDTO;
import pt.iade.planit.entity.Event;
import pt.iade.planit.entity.Participant;
import pt.iade.planit.entity.User;
import pt.iade.planit.repository.EventRepository;
import pt.iade.planit.repository.ParticipantRepository;
import pt.iade.planit.repository.UserRepository;
import pt.iade.planit.service.ParticipantService;

import java.util.List;
import java.util.Optional;

@Service
public class ParticipantServiceImpl implements ParticipantService {

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ParticipantDTO addParticipant(Integer eventId, Integer userId, Participant.Status status) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with ID: " + eventId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        Participant participant = new Participant();
        participant.setEvent(event);
        participant.setUser(user);
        participant.setStatus(status);

        Participant savedParticipant = participantRepository.save(participant);

        return new ParticipantDTO(
                savedParticipant.getId(),
                userId,
                user.getName(),
                eventId,
                event.getTitle(),
                status.name()
        );
    }

    @Override
    public List<ParticipantDTO> getParticipantsByEventId(Integer eventId) {
        List<Participant> participants = participantRepository.findByEventId(eventId);

        return participants.stream().map(participant -> new ParticipantDTO(
                participant.getId(),
                participant.getUser().getId(),
                participant.getUser().getName(),
                participant.getEvent().getId(),
                participant.getEvent().getTitle(),
                participant.getStatus().name()
        )).toList();
    }

    @Override
    public List<ParticipantDTO> getParticipantsByUserId(Integer userId) {
        List<Participant> participants = participantRepository.findByUserId(userId);

        return participants.stream().map(participant -> new ParticipantDTO(
                participant.getId(),
                participant.getUser().getId(),
                participant.getUser().getName(),
                participant.getEvent().getId(),
                participant.getEvent().getTitle(),
                participant.getStatus().name()
        )).toList();
    }

    @Override
    public void removeParticipant(Integer participantId) {
        participantRepository.deleteById(participantId);
    }

    @Override
    public ParticipantDTO inviteParticipant(Integer eventId, String userEmail) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with ID: " + eventId));
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + userEmail));

        Optional<Participant> existingParticipant = participantRepository.findByEventIdAndUserEmail(eventId, userEmail);
        if (existingParticipant.isPresent()) {
            throw new RuntimeException("User is already invited to this event.");
        }

        Participant participant = new Participant();
        participant.setEvent(event);
        participant.setUser(user);
        participant.setStatus(Participant.Status.INVITED);

        Participant savedParticipant = participantRepository.save(participant);

        return new ParticipantDTO(
                savedParticipant.getId(),
                user.getId(),
                user.getName(),
                event.getId(),
                event.getTitle(),
                savedParticipant.getStatus().name()
        );
    }

    @Override
    public ParticipantDTO updateParticipantStatus(Integer participantId, String status) {
        Participant participant = participantRepository.findById(participantId)
                .orElseThrow(() -> new RuntimeException("Participant not found with ID: " + participantId));

        participant.setStatus(Participant.Status.fromString(status));
        Participant updatedParticipant = participantRepository.save(participant);

        return new ParticipantDTO(
                updatedParticipant.getId(),
                updatedParticipant.getUser().getId(),
                updatedParticipant.getUser().getName(),
                updatedParticipant.getEvent().getId(),
                updatedParticipant.getEvent().getTitle(),
                updatedParticipant.getStatus().name()
        );
    }

    @Override
    public List<ParticipantDTO> getPendingInvites(Integer userId) {
        List<Participant> participants = participantRepository.findByUserIdAndStatus(userId, Participant.Status.INVITED);

        return participants.stream().map(participant -> new ParticipantDTO(
                participant.getId(),
                participant.getUser().getId(),
                participant.getUser().getName(),
                participant.getEvent().getId(),
                participant.getEvent().getTitle(),
                participant.getStatus().name()
        )).toList();
    }

}
