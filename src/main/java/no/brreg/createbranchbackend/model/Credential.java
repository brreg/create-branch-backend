package no.brreg.createbranchbackend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "credentials")
public class Credential {

    // --------------
    // NPID part

    // Henviser til .presentation.verifiableCredential[].credentialSubject.navn
    private String personNavn;

    // Henviser til .presentation.verifiableCredential[].credentialSubject.fnr
    private String personFnr;


    // --------------
    // EUCC part

    //    "issuing_authority": "Bronnoysund Register Center",
    private String issuingAuthority;

    //    "issuing_authority_id": "nofor:974760673",
    private String issuingAuthorityId;

    //    "issuing_country": "no",
    private String issuingCountry;

    //    "authentic_source_name": "Bronnoysund Register Center",
    private String AuthenticSourceName;

    //    "authentic_source_id": "nofor:974760673",
    private String AuthenticSourceID;

    //    "legal_person_name": "USELVSTENDIG HYGGELIG TIGER AS",
    private String foretakNavn;

    //    "legal_person_id": "nofor:214254182",
    private String foretakOrgnr;

    //    "registration_date": "2012-07-26",
    private String foretakStiftet;

    //    "full_address": "Husanvegen 121;7655 VERDAL;Norge",
    private String foretakAdresse;

    //    "legal_entity_activity_code": "69.201",
    private String foretakNaceKode;

    //    "legal_entity_activity_description": "Regnskap og bokforing",
    private String foretakNaceBeskrivelse;

    //    "legal_entity_status": "active",
    private String foretakAktive;

    //    "legal_representative_full_name": "TOPP AMBASSADE",
    private String representantNavn;

    //    "legal_representative_date_of_birth": "1947-87-10",
    private String representantFodselsdato;

    //    "legal_representative_signatory_rule": "alone",
    private String representantSignaturRegel;


    // -------------
    // Metadata

    @Id
    @GeneratedValue
    private Long id;

    // Henviser til .challengeId
    private String userSessionId;

    // Henviser til .presentation.verifiableCredential[].issuer.name
    private String issuerWalletName;

    // Henviser til .presentation.verifiableCredential[].issuer.id
    private String issuerWalletAddress;

    // Henviser til .holder
    private String holderWalletAddress;

}

