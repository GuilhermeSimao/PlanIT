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
}
