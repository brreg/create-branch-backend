package no.brreg.createbranchbackend.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

@Data
public class MessageEncryptionDTO {

    private String senderDidUrl;

    private String[] recipientDidUrls;

    private JsonNode payload; // request from PresentationResponse
}
