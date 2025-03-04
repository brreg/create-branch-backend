package no.brreg.createbranchbackend.service;

import com.fasterxml.jackson.databind.JsonNode;
import no.brreg.createbranchbackend.model.Credential;
import no.brreg.createbranchbackend.repository.CredentialRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;


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
 // TODO: DO i realy need to do this dance with multiple handoffs of userSessionId ?
    public void parseResponseAndCreateCredentials(JsonNode jsonResponse,String userSessionId) {

        String holderWalletAddress  = jsonResponse.get(0).get("sub").asText();


        // TODO: Check for validity of credentials
        JsonNode npidCredentialNode = null;
        JsonNode euccCredentialNode = null;

        for (int i=0;i<jsonResponse.size();i++){
            if (jsonResponse.get(i).path("vct").asText().equals("EWCEuCompanyCertificate")) {
                euccCredentialNode = jsonResponse.get(i);
            }
            if (jsonResponse.get(i).path("vct").asText().equals("NaturalPersonalIdentificationData")) {
                npidCredentialNode = jsonResponse.get(i);
            }
        }
        // TODO: These asserts dont work!
        assert euccCredentialNode != null;
        assert npidCredentialNode != null;
        parseAndStoreCredential(euccCredentialNode, npidCredentialNode, userSessionId, holderWalletAddress);
    }

    private void parseAndStoreCredential(JsonNode euccCredentialNode, JsonNode npidCredentialNode, String sessionId, String holderWalletAddress) {
        Credential c = new Credential();

        // Hent credentialSubject for NPID
        JsonNode n = npidCredentialNode;
        c.setPersonNavn(n.path("given_name").asText() + " " + n.path("family_name").asText());
        c.setPersonFnr(n.path("personal_administrative_number").asText());
        c.setPersonBy(n.path("resident_city").asText());
        c.setPersonLand(n.path("resident_country").asText());
        c.setPersonPostcode(n.path("resident_postal_code").asText());
        c.setPersonVei(n.path("resident_street").asText());
        c.setPersonHusnummer(n.path("resident_house_number").asText());
        c.setPersonVeiAddresse( c.getPersonVei() + " " + c.getPersonHusnummer());


        // Hent credentialSubject for EUCC
        JsonNode e = euccCredentialNode;

        c.setIssuingAuthority(e.path("issuing_authority").asText());

        c.setIssuingAuthorityId(e.path("issuing_authority_id").asText());

        c.setIssuingCountry(e.path("issuing_country").asText());

        c.setAuthenticSourceName(e.path("issuing_authority").asText());

        c.setAuthenticSourceID(e.path("issuing_authority_id").asText());


        c.setForetakNavn(e.path("credentialSubject").path("legal_person").path("legal_person_name").asText());

        c.setForetakOrgnr(e.path("credentialSubject").path("legal_person").path("legal_person_id").asText());
        c.setForetakOrgform((e.path("credentialSubject").path("legal_person").path("legal_form_type").asText()));

        c.setForetakStiftet(e.path("credentialSubject").path("legal_person").path("registration_date").asText());

        c.setForetakAdresse(e.path("credentialSubject").path("legal_person").path("registered_address").path("full_address").asText());

        c.setForetakNaceKode(e.path("credentialSubject").path("legal_person").path("legal_entity_activity").path("code").asText());

        c.setForetakNaceBeskrivelse(e.path("credentialSubject").path("legal_person").path("legal_entity_activity").path("code").asText());

        c.setForetakAktive(e.path("credentialSubject").path("legal_person").path("legal_entity_status").asText());
        
// TODO: Support multiple people here !
        c.setRepresentantNavn(e.path("credentialSubject").path("legal_representative").get(0).path("full_name").asText());

        c.setRepresentantFodselsdato(e.path("credentialSubject").path("legal_representative").get(0).path("date_of_birth").asText());

        c.setRepresentantSignaturRegel(e.path("credentialSubject").path("legal_representative").get(0).path("signatory_rule").asText());

        // Sett userSessionId
        c.setUserSessionId(sessionId);

        // TODO: Why do we need these, and what for ?
        // Hent issuer
        c.setIssuerWalletName(euccCredentialNode.path("issuing_authority").asText());
        c.setIssuerWalletAddress(euccCredentialNode.path("id").asText());

        // Sett holderWalletAddress
        c.setHolderWalletAddress(holderWalletAddress);

        credentialRepository.save(c);

        // inform frontend about data is received from Mattr
        String destination = "/topic/sessions/" + sessionId;
        messagingTemplate.convertAndSend(destination, "Data available for session " + c);
    }
}

