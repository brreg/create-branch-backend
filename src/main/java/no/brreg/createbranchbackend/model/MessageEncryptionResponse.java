package no.brreg.createbranchbackend.model;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

@Data
public class MessageEncryptionResponse {
    private JsonNode jwe;
}
