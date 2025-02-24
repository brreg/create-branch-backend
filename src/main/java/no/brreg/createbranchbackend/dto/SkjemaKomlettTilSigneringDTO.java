package no.brreg.createbranchbackend.dto;

import lombok.Data;
import no.brreg.createbranchbackend.model.Credential;
import no.brreg.createbranchbackend.model.Signering;
import no.brreg.createbranchbackend.model.SkjemaInnsendt;
import no.brreg.createbranchbackend.model.TenorPerson;

@Data
public class SkjemaKomlettTilSigneringDTO {

    public SkjemaKomlettTilSigneringDTO(Credential credential, TenorPerson tenorPerson, SkjemaInnsendt skjemaInnsendt, Signering signering) {
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

        foretakTelefonnummer = skjemaInnsendt.getForetakTelefonnummer();
        foretakWebPageUrl = skjemaInnsendt.getForetakWebPageUrl();
        norskForetaksNavn = skjemaInnsendt.getNorskForetaksNavn();
        harFilialINorge = skjemaInnsendt.isHarFilialINorge();
        filialAdresse = skjemaInnsendt.getFilialAdresse();
        filialPostnummer = skjemaInnsendt.getFilialPostnummer();
        filialPoststed = skjemaInnsendt.getFilialPoststed();
        filialTelefonnummer = skjemaInnsendt.getFilialTelefonnummer();
        filialWebPageUrl = skjemaInnsendt.getFilialWebPageUrl();
        filialNaceKode1 = skjemaInnsendt.getFilialNaceKode1();
        filialNaceKode2 = skjemaInnsendt.getFilialNaceKode2();
        filialNaceKode3 = skjemaInnsendt.getFilialNaceKode3();

        signert = signering.isSignert();
    }

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

    // ---

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

    // ---

    private boolean signert;
}
