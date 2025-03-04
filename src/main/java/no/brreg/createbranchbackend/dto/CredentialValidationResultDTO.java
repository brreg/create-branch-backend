package no.brreg.createbranchbackend.dto;

import lombok.Data;

@Data
public class CredentialValidationResultDTO {
    private boolean isValid;
    private String reason;

    public CredentialValidationResultDTO(boolean isValid, String reason) {
        this.isValid = isValid;
        this.reason = reason;
    }
} 