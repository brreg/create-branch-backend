package no.brreg.createbranchbackend.model;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

@Data
public class PresentationResponse {
    private String id;
    private JsonNode request; // Lagres som JSON-streng
    private String callbackUrl;
    private Long expiresTime;
    private String didcommUri;
    private JsonNode verificationHistory;
    private JsonNode credentialHistory;
}
