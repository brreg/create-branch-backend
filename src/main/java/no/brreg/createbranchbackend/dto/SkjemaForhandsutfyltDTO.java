package no.brreg.createbranchbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import no.brreg.createbranchbackend.model.Credential;
import no.brreg.createbranchbackend.model.TenorPerson;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SkjemaForhandsutfyltDTO {

    public SkjemaForhandsutfyltDTO(Credential credential, TenorPerson tenorPerson) {
        personNavn = credential.getPersonNavn();
        personFnr = credential.getPersonFnr();
        personAdresse = tenorPerson.getAdresse();
        personPostnummer = tenorPerson.getPostnummer();
        personPoststed = tenorPerson.getPoststed();
        personLand = tenorPerson.getLand();

        issuingAuthority = credential.getIssuingAuthority();
        issuingAuthorityId = credential.getIssuingAuthorityId();
        issuingCountry = credential.getIssuingCountry();
        authenticSourceName = credential.getAuthenticSourceName();
        authenticSourceID = credential.getAuthenticSourceID();
        foretakNavn = credential.getForetakNavn();
        foretakOrgnr = credential.getForetakOrgnr();
        foretakOrgform = credential.getForetakOrgform();
        foretakStiftet = credential.getForetakStiftet();
        foretakAdresse = credential.getForetakAdresse();
        foretakNaceKode = credential.getForetakNaceKode();
        foretakNaceBeskrivelse = credential.getForetakNaceBeskrivelse();
        foretakAktive = credential.getForetakAktive();
        representantNavn = credential.getRepresentantNavn();
        representantFodselsdato = credential.getRepresentantFodselsdato();
        representantSignaturRegel = credential.getRepresentantSignaturRegel();
    }

    // --------------
    // NPID ++ part

    private String personNavn;

    private String personFnr;

    private String personAdresse;

    private String personPostnummer;

    private String personPoststed;

    private String personLand;

    // --------------
    // EUCC part

    private String issuingAuthority;

    private String issuingAuthorityId;

    private String issuingCountry;

    private String authenticSourceName;

    private String authenticSourceID;

    private String foretakNavn;

    private String foretakOrgnr;

    private String foretakOrgform;

    private String foretakStiftet;

    private String foretakAdresse;

    private String foretakNaceKode;

    private String foretakNaceBeskrivelse;

    private String foretakAktive;

    private String representantNavn;

    private String representantFodselsdato;

    private String representantSignaturRegel;
}
