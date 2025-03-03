package no.brreg.createbranchbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class NpidDTO {
    @JsonProperty("issuanceMode")
    private String issuanceMode = "InTime";

    @JsonProperty("credentialDefinitionId")
    private String credentialDefinitionId = "8c9c84aa-b5fa-45da-8457-e5da6057a291";

    @JsonProperty("credential")
    public Credential credential = new Credential();

    @Data
    public static class Credential {
        @JsonProperty("claims")
        private Claims claims = new Claims();

        @Data
        public static class Claims {
            @JsonProperty("birth_date")
            private String birthDate = "1943-01-15";

            @JsonProperty("birth_place")
            private String birthPlace = "NORDBERG";

            @JsonProperty("email_address")
            private String emailAddress = "hopefullynotexisting@brreg.no";

            @JsonProperty("family_name")
            private String familyName = "JEGER";

            @JsonProperty("given_name")
            private String givenName = "USELVISK";

            @JsonProperty("mobile_phone_number")
            private String mobilePhoneNumber = "81549300";

            @JsonProperty("nationality")
            private String nationality = "NO";

            @JsonProperty("personal_administrative_number")
            private String personalAdministrativeNumber = "15814399394";

            @JsonProperty("resident_address")
            private String residentAddress = "Bråtåvegen 70; 2693; NORDBERG";

            @JsonProperty("resident_city")
            private String residentCity = "NORDBERG";

            @JsonProperty("resident_country")
            private String residentCountry = "NO";

            @JsonProperty("resident_house_number")
            private String residentHouseNumber = "70";

            @JsonProperty("resident_postal_code")
            private String residentPostalCode = "2693";

            @JsonProperty("resident_street")
            private String residentStreet = "Bråtåvegen";

            @JsonProperty("sex")
            private int sex = 1;
        }
    }
}