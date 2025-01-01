package pt.iade.planit.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDTO {
    private Integer id;
    private String title;
    private String description;
    private LocalDateTime date;

    @JsonProperty("photoUrl")
    private String photoUrl;

    @JsonProperty("userId")
    private Integer userId; // Link to User ID

    private String userName;

    private String userEmail;

    @JsonProperty("locationId")
    private Integer locationId; // Optional Location ID

    // List of participants
    @JsonProperty("participants")
    private List<ParticipantDTO> participants;

    private String address;
    private Double latitude;
    private Double longitude;
}
