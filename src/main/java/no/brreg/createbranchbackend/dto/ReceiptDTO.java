package no.brreg.createbranchbackend.dto;

import lombok.Data;

@Data
public class ReceiptDTO {
    private String issuanceMode = "InTime";
    private String credentialDefinitionId = "9ff6c695-28db-4c74-b165-56b58cbd6ddb";
    private Credential credential = new Credential();

    @Data
    public static class Credential {
        private Claims claims = new Claims();

        @Data
        public static class Claims {
            private String legal_person_id = "NOFOR:311032348";
        }
    }
}
