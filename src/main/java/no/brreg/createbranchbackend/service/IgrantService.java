package no.brreg.createbranchbackend.service;


import lombok.extern.slf4j.Slf4j;
import no.brreg.createbranchbackend.dto.PresentationRequestDTO;
import no.brreg.createbranchbackend.dto.PresentationUrlDTO;
import no.brreg.createbranchbackend.dto.ReceiptDTO;
import no.brreg.createbranchbackend.model.Credential;
import no.brreg.createbranchbackend.model.PresentationResponse;
import no.brreg.createbranchbackend.repository.CredentialRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
@Slf4j
public class IgrantService
{

    private final CredentialRepository credentialRepository;


    @Value("${igrant.api-key}")
    private String api_key;

    @Value("${igrant.endpoint}")
    private String endpoint;


    @Value("${igrant.presentation-request-id-bulk}")
    private String presentationRequestIdBulk;
    @Value("${igrant.presentation-request-id-npid}")
    private String presentationRequestIdNpid;

    private final WebClient webClient;

    public IgrantService(CredentialRepository credentialRepository, WebClient.Builder webClientBuilder, AsyncListenIgrantPresentationRequest asyncService) {
        this.credentialRepository = credentialRepository;
        this.webClient = webClientBuilder.build();
        this.asyncService = asyncService;
    }

    private final AsyncListenIgrantPresentationRequest asyncService;

    public PresentationUrlDTO createPresentationUrlBulk(String userSessionId) throws Exception {
        try {
            String presentationResponse = fetchPresentationRequest(userSessionId, api_key,presentationRequestIdBulk);
            var response = new PresentationUrlDTO();
            response.setDidcommUri(presentationResponse);
            return response;
        } catch (Exception e) {
            log.error("Exception: {}", e.getMessage(), e);
            throw e;
        }
    }

    public PresentationUrlDTO createPresentationUrlNpid(String userSessionId) throws Exception {
        try {
            String presentationResponse = fetchPresentationRequest(userSessionId, api_key,presentationRequestIdNpid);
            var response = new PresentationUrlDTO();
            response.setDidcommUri(presentationResponse);
            return response;
        } catch (Exception e) {
            log.error("Exception: {}", e.getMessage(), e);
            throw e;
        }
    }

    private String fetchPresentationRequest(String userSessionId, String api_key, String presentationRequestTemplateId) throws Exception {
        String igrantURL = endpoint + "v3/config/digital-wallet/openid/sdjwt/verification/send";

        PresentationRequestDTO presentationRequestDTO = new PresentationRequestDTO();
        presentationRequestDTO.setPresentationDefinitionId(presentationRequestTemplateId);

        try {
            PresentationResponse presentationResponse = webClient.post()
                    .uri(igrantURL)
                    .header("Authorization",api_key)
                    .bodyValue(presentationRequestDTO)
                    .retrieve()
                    .onStatus(
                            status -> status != HttpStatus.OK,
                            clientResponse -> clientResponse.bodyToMono(String.class)
                                    .flatMap(errorBody -> {
                                        log.error("Error creating presentation request: {}", errorBody);
                                        return Mono.error(new Exception("Error creating presentation request: " + errorBody));
                                    })
                    )
                    .bodyToMono(PresentationResponse.class)
                    .block();

            if (presentationResponse == null) {
                throw new Exception("Failed to create presentation request from Igrant");
            }
            String type = "";
            if (Objects.equals(presentationRequestTemplateId, presentationRequestIdBulk)){
                type = "bulk";
            }
            if (Objects.equals(presentationRequestTemplateId, presentationRequestIdNpid)){
                type = "signatur";
            }

            asyncService.asyncListenIgrantPresentation(presentationResponse.getVerificationHistory().path("presentationExchangeId").asText(),userSessionId,type);

            return "openid4vp://?request_uri=https%3A%2F%2Foid4vc.igrant.io%2Forganisation%2F"
                    + presentationResponse.getVerificationHistory().path("openIdOrganisationId").asText()
                    + "%2Fservice%2Fverification%2F"
                    +  presentationResponse.getVerificationHistory().path("presentationExchangeId").asText();

        } catch (WebClientResponseException e) {
            log.error("error creating presentation request: {}", e.getResponseBodyAsString());
            throw new Exception("Error creating presentation request: " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            log.error("Exception: {}", e.getMessage(), e);
            throw e;
        }
    }

    public PresentationUrlDTO issueReceipt(String userSessionId) throws Exception {

        try {
            Credential c = credentialRepository.findByUserSessionId(userSessionId);
            if (c == null) {
                log.error("Credential not found");
                return null;
            }
            else {
                ReceiptDTO receiptDTO = new ReceiptDTO();

                receiptDTO.getCredential().getClaims().setLegal_person_id(c.getForetakOrgnr());

                String igrantURL = endpoint + "v2/config/digital-wallet/openid/sdjwt/credential/issue";


                PresentationResponse presentationResponse = webClient.post()
                        .uri(igrantURL)
                        .header("Authorization",api_key)
                        .bodyValue(receiptDTO)
                        .retrieve()
                        .onStatus(
                                status -> status != HttpStatus.OK,
                                clientResponse -> clientResponse.bodyToMono(String.class)
                                        .flatMap(errorBody -> {
                                            log.error("Error creating receipt request: {}", errorBody);
                                            return Mono.error(new Exception("Error creating presentation request: " + errorBody));
                                        })
                        )
                        .bodyToMono(PresentationResponse.class)
                        .block();

                assert presentationResponse != null;
                var response = new PresentationUrlDTO();
                response.setDidcommUri(presentationResponse.getCredentialHistory().path("credentialOffer").asText());
                return response;

            }
        } catch (Exception e) {
            log.error("Error getting credential", e);
            return null;
        }
    }

}
