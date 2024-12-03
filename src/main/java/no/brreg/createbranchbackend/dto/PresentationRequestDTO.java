package no.brreg.createbranchbackend.dto;

import lombok.Data;

@Data
public class PresentationRequestDTO {

    private String challenge;

    private String templateId;

    private String did;

    private String callbackUrl;
}