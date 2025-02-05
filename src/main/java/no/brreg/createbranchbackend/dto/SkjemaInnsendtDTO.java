package no.brreg.createbranchbackend.dto;

import lombok.Data;

@Data
public class SkjemaInnsendtDTO {
    private String foretakTelefonnummer;

    private String foretakWebPageUrl;

    private String norskForetaksNavn;

    private boolean harFilialINorge;

    private String filialAdresse;

    private String filialPostnummer;

    private String filialPoststed;

    private String filialTelefonnummer;

    private String filialWebPageUrl;

    private String filialNaceKode1;

    private String filialNaceKode2;

    private String filialNaceKode3;
}
