package pt.iade.planit.service;

import pt.iade.planit.dto.ParticipantDTO;
import pt.iade.planit.entity.Participant;

import java.util.List;

public interface ParticipantService {
    ParticipantDTO addParticipant(Integer eventId, Integer userId, Participant.Status status);
    List<ParticipantDTO> getParticipantsByEventId(Integer eventId);
    List<ParticipantDTO> getParticipantsByUserId(Integer userId);
    void removeParticipant(Integer participantId);
    ParticipantDTO inviteParticipant(Integer eventId, String userEmail);
    ParticipantDTO updateParticipantStatus(Integer participantId, String status);
    List<ParticipantDTO> getPendingInvites(Integer userId);


}
