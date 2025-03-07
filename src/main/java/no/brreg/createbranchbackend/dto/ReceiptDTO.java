package no.brreg.createbranchbackend.dto;

import lombok.Data;

@Data
public class ReceiptDTO {
    private String issuanceMode = "InTime";
    private String credentialDefinitionId = "b8eb2a49-ef8e-4344-b1d9-d66690e684c0";
    private Credential credential = new Credential();

    @Data
    public static class Credential {
        private Claims claims = new Claims();

        @Data
        public static class Claims {
            private String EUID = "NOFOR:311032348";
        }
    }
}
