package pt.iade.planit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantDTO {
    private Integer id;            // ID do participante
    private Integer userId;        // ID do utilizador
    private String userName;       // Nome do utilizador
    private Integer eventId;       // ID do evento
    private String eventTitle;     // Título do evento
    private String status;         // Estado do participante (invited, confirmed, declined)
}
