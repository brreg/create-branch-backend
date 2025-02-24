package no.brreg.createbranchbackend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import no.brreg.createbranchbackend.model.Credential;
import no.brreg.createbranchbackend.repository.CredentialRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;

// This class parses the incoming credentials from Mattr and stores them in the database

@Service
public class CredentialService {


    private final CredentialRepository credentialRepository;

    private final SimpMessagingTemplate messagingTemplate;

    public CredentialService(CredentialRepository credentialRepository, SimpMessagingTemplate messagingTemplate) {
        this.credentialRepository = credentialRepository;
        this.messagingTemplate = messagingTemplate;
    }

    public Credential getCredentialBySessionId(String sessionId) {
        return credentialRepository.findByUserSessionId(sessionId);
    }

    public void parseResponseAndCreateCredentials(String jsonResponse) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode npidCredentialNode = null;
        JsonNode euccCredentialNode = null;

        JsonNode rootNode = objectMapper.readTree(jsonResponse);

        // Hent challengeId
        String challengeId = rootNode.path("challengeId").asText();

        // Hent holderWalletAddress
        String holderWalletAddress = rootNode.path("holder").asText();

        // Naviger til presentation.verifiableCredential[]
        JsonNode verifiableCredentialsNode = rootNode
                .path("presentation")
                .path("verifiableCredential");

        for (JsonNode credentialNode : verifiableCredentialsNode) {
            JsonNode typeNode = credentialNode.path("type");

            for (JsonNode typeValue : typeNode) {
                // Sjekk om type[] inneholder "npid eller eucc"
                if ("npid".equalsIgnoreCase(typeValue.asText())) {
                    npidCredentialNode = credentialNode;
                }
                if ("eucc".equalsIgnoreCase(typeValue.asText())) {
                    euccCredentialNode = credentialNode;
                }
            }
        }

        assert euccCredentialNode != null;
        assert npidCredentialNode != null;
        parseAndStoreCredential(euccCredentialNode, npidCredentialNode, challengeId, holderWalletAddress);
    }

    private void parseAndStoreCredential(JsonNode euccCredentialNode, JsonNode npidCredentialNode, String challengeId, String holderWalletAddress) {
        Credential c = new Credential();

        // Hent credentialSubject for NPID
        JsonNode n = npidCredentialNode.path("credentialSubject");
        c.setPersonNavn(n.path("navn").asText());
        c.setPersonFnr(n.path("fnr").asText());


        // Hent credentialSubject for EUCC
        JsonNode e = euccCredentialNode.path("credentialSubject");

        c.setIssuingAuthority(e.path("issuing_authority").asText());

        c.setIssuingAuthorityId(e.path("issuing_authority_id").asText());

        c.setIssuingCountry(e.path("issuing_country").asText());

        c.setAuthenticSourceName(e.path("authentic_source_name").asText());

        c.setAuthenticSourceID(e.path("authentic_source_id").asText());

        c.setForetakNavn(e.path("legal_person_name").asText());

        c.setForetakOrgnr(e.path("legal_person_id").asText());

        c.setForetakOrgform(e.path("legal_form_type").asText());

        c.setForetakStiftet(e.path("registration_date").asText());

        c.setForetakAdresse(e.path("full_address").asText());

        c.setForetakNaceKode(e.path("legal_entity_activity_code").asText());

        c.setForetakNaceBeskrivelse(e.path("legal_entity_activity_description").asText());

        c.setForetakAktive(e.path("legal_entity_status").asText());

        c.setRepresentantNavn(e.path("legal_representative_full_name").asText());

        c.setRepresentantFodselsdato(e.path("legal_representative_date_of_birth").asText());

        c.setRepresentantSignaturRegel(e.path("legal_representative_signatory_rule").asText());

        // Sett userSessionId
        c.setUserSessionId(challengeId);

        // Hent issuer
        JsonNode issuerNode = euccCredentialNode.path("issuer");
        c.setIssuerWalletName(issuerNode.path("name").asText());
        c.setIssuerWalletAddress(issuerNode.path("id").asText());

        // Sett holderWalletAddress
        c.setHolderWalletAddress(holderWalletAddress);

        credentialRepository.save(c);

        // inform frontend about data is received from Mattr
        String destination = "/topic/sessions/" + challengeId;
        messagingTemplate.convertAndSend(destination, "Data available for session " + c);
    }
}

