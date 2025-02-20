package no.brreg.createbranchbackend.dto;

import lombok.Data;

@Data
public class PresentationRequestDTO {

    private Boolean requestByReference=Boolean.TRUE;

    private String presentationDefinitionId;
}