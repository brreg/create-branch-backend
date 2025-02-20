package no.brreg.createbranchbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class EuccDTO {
    @JsonProperty("issuanceMode")
    private String issuanceMode = "InTime";

    @JsonProperty("credentialDefinitionId")
    private String credentialDefinitionId = "35333c94-da82-42e3-8c1d-49ed80197374";

    @JsonProperty("credential")
    private Credential credential = new Credential();


    public static class Credential {
        @JsonProperty("authentic_source_id")
        private String authenticSourceId = "1234";

        @JsonProperty("authentic_source_name")
        private String authenticSourceName = "testing";

        @JsonProperty("credentialSchema")
        private CredentialSchema credentialSchema = new CredentialSchema();

        @JsonProperty("credentialStatus")
        private CredentialStatus credentialStatus = new CredentialStatus();

        @JsonProperty("credentialSubject")
        private CredentialSubject credentialSubject = new CredentialSubject();

        @JsonProperty("expiry_date")
        private String expiryDate = "2025-03-16T11:08:54Z";

        @JsonProperty("issuance_date")
        private String issuanceDate = "2025-02-14T11:08:54Z";

        @JsonProperty("issuing_authority")
        private String issuingAuthority = "Brønnøysundregistrene";

        @JsonProperty("issuing_authority_id")
        private String issuingAuthorityId = "NOFOR:974760673";

        @JsonProperty("issuing_country")
        private String issuingCountry = "NO";

        @JsonProperty("issuing_jurisdiction")
        private String issuingJurisdiction = "NO";

    }

    public static class CredentialSchema {
        @JsonProperty("id")
        private String id = "http://json-schema.org/draft-07/schema#";

        @JsonProperty("type")
        private String type = "FullJsonSchemaValidator2021";

    }

    public static class CredentialStatus {
        @JsonProperty("id")
        private String id = "http://testing.1234";

        @JsonProperty("type")
        private String type = "testing";

    }

    public static class CredentialSubject {
        @JsonProperty("legal_entity")
        private String legalEntity = "AKADEMISK AKADEMISK KATT ALMISSE";

        @JsonProperty("legal_person")
        private LegalPerson legalPerson = new LegalPerson();

        @JsonProperty("legal_representative")
        private List<LegalRepresentative> legalRepresentative = List.of(
                new LegalRepresentative("USELVISK JEGER", "1943-01-15", "alone")
        );
    }

    public static class LegalPerson {
        @JsonProperty("legal_entity_activity")
        private LegalEntityActivity legalEntityActivity = new LegalEntityActivity();

        @JsonProperty("legal_entity_status")
        private String legalEntityStatus = "active";

        @JsonProperty("legal_form_type")
        private String legalFormType = "ENK";

        @JsonProperty("legal_person_id")
        private String legalPersonId = "NOFOR:311032348";

        @JsonProperty("legal_person_name")
        private String legalPersonName = "AKADEMISK AKADEMISK KATT ALMISSE";

        @JsonProperty("registered_address")
        private RegisteredAddress registeredAddress = new RegisteredAddress();

        @JsonProperty("registration_date")
        private String registrationDate = "2021-03-12";

        @JsonProperty("registration_member_state")
        private String registrationMemberState = "NO";

    }

    public static class LegalEntityActivity {
        @JsonProperty("code")
        private String code = "90.031";

        @JsonProperty("description")
        private String description = "Selvstendig kunstnerisk virksomhet innen visuell kunst";

    }

    public static class RegisteredAddress {
        @JsonProperty("full_address")
        private String fullAddress = "Vestre Røysenleirstedet;8742;SELVÆR";

    }

    public static class LegalRepresentative {
        @JsonProperty("full_name")
        private String fullName;

        @JsonProperty("date_of_birth")
        private String dateOfBirth;

        @JsonProperty("signatory_rule")
        private String signatoryRule;

        public LegalRepresentative(String fullName, String dateOfBirth, String signatoryRule) {
            this.fullName = fullName;
            this.dateOfBirth = dateOfBirth;
            this.signatoryRule = signatoryRule;
        }

    }

}