package no.brreg.createbranchbackend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "tenor_person")
public class TenorPerson {
    @Id
    private String fnr;

    private String navn;

    private String adresse;

    private String postnummer;

    private String poststed;

    private String land;
}
