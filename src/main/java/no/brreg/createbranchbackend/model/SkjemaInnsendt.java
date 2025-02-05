package no.brreg.createbranchbackend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "skjema")
public class SkjemaInnsendt {
    @Id
    private String userSessionId;

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
