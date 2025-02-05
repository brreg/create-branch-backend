package no.brreg.createbranchbackend.dto.mattr;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

@Data
public class MessageTransmissionDTO {
    private String to;
    private JsonNode message;
}
