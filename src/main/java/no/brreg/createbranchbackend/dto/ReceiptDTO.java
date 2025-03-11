package no.brreg.createbranchbackend.dto;

import lombok.Data;

@Data
public class ReceiptDTO {
    private String issuanceMode = "InTime";
    private String credentialDefinitionId = "3b1c56f2-be61-43c4-9bc5-c72a483fcfd8";
    private Credential credential = new Credential();

    @Data
    public static class Credential {
        private Claims claims = new Claims();

        @Data
        public static class Claims {
            private String euid = "NOFOR:311032348";
        }
    }
}
