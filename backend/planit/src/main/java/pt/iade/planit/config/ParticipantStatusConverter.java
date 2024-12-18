package pt.iade.planit.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import pt.iade.planit.entity.Participant;

@Component
public class ParticipantStatusConverter implements Converter<String, Participant.Status> {

    @Override
    public Participant.Status convert(String source) {
        try {
            return Participant.Status.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid status: " + source);
        }
    }
}
