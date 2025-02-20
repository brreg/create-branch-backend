package no.brreg.createbranchbackend.service;

import lombok.extern.slf4j.Slf4j;
import no.brreg.createbranchbackend.dto.EuccDTO;
import no.brreg.createbranchbackend.dto.NpidDTO;
import no.brreg.createbranchbackend.dto.PresentationRequestDTO;
import no.brreg.createbranchbackend.dto.PresentationUrlDTO;
import no.brreg.createbranchbackend.model.IssuingResponse;
import no.brreg.createbranchbackend.model.PresentationResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class TestDataService {

    @Value("${igrant.api-key}")
    private String api_key;

    @Value("${igrant.endpoint}")
    private String endpoint;

    @Value("${igrant.presentation-request-template-id}")
    private String presentationRequestTemplateId;

    private final WebClient webClient;

    public TestDataService(WebClient.Builder webClientBuilder, AsyncListenIgrantPresentationRequest asyncService) {
        this.webClient = webClientBuilder.build();
    }
    public PresentationUrlDTO createTestNpid() throws Exception {
        try {
            String presentationResponse = issueNpid(api_key);
            var response = new PresentationUrlDTO();
            response.setDidcommUri(presentationResponse);
            return response;
        } catch (Exception e) {
            log.error("Exception: {}", e.getMessage(), e);
            throw e;
        }
    }
    private String issueNpid(String api_key) throws Exception {
        String igrantURL = endpoint + "v2/config/digital-wallet/openid/sdjwt/credential/issue";
        NpidDTO npidDTO = new NpidDTO();

        try {
            IssuingResponse issuingResponse = webClient.post()
                    .uri(igrantURL)
                    .header("Authorization", api_key)
                    .bodyValue(npidDTO)
                    .retrieve()
                    .onStatus(
                            status -> status != HttpStatus.OK,
                            clientResponse -> clientResponse.bodyToMono(String.class)
                                    .flatMap(errorBody -> {
                                        log.error("Error creating presentation request: {}", errorBody);
                                        return Mono.error(new Exception("Error creating presentation request: " + errorBody));
                                    })
                    )
                    .bodyToMono(IssuingResponse.class)
                    .block();

            if (issuingResponse == null) {
                throw new Exception("Failed to create presentation request from Igrant");
            }
            return issuingResponse.getCredentialHistory().path("credentialOffer").asText();

        } catch (WebClientResponseException e) {
            log.error("error creating presentation request: {}", e.getResponseBodyAsString());
            throw new Exception("Error creating presentation request: " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            log.error("Exception: {}", e.getMessage(), e);
            throw e;
        }
    }
    public PresentationUrlDTO createTestEucc() throws Exception {
        try {
            String presentationResponse = issueEucc(api_key);
            var response = new PresentationUrlDTO();
            response.setDidcommUri(presentationResponse);
            return response;
        } catch (Exception e) {
            log.error("Exception: {}", e.getMessage(), e);
            throw e;
        }
    }
    private String issueEucc(String api_key) throws Exception {
        String igrantURL = endpoint + "v2/config/digital-wallet/openid/sdjwt/credential/issue";
        EuccDTO euccDTO = new EuccDTO();

        try {
            IssuingResponse issuingResponse = webClient.post()
                    .uri(igrantURL)
                    .header("Authorization", api_key)
                    .bodyValue(euccDTO)
                    .retrieve()
                    .onStatus(
                            status -> status != HttpStatus.OK,
                            clientResponse -> clientResponse.bodyToMono(String.class)
                                    .flatMap(errorBody -> {
                                        log.error("Error creating presentation request: {}", errorBody);
                                        return Mono.error(new Exception("Error creating presentation request: " + errorBody));
                                    })
                    )
                    .bodyToMono(IssuingResponse.class)
                    .block();

            if (issuingResponse == null) {
                throw new Exception("Failed to create presentation request from Igrant");
            }
            return issuingResponse.getCredentialHistory().path("credentialOffer").asText();

        } catch (WebClientResponseException e) {
            log.error("error creating presentation request: {}", e.getResponseBodyAsString());
            throw new Exception("Error creating presentation request: " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            log.error("Exception: {}", e.getMessage(), e);
            throw e;
        }
    }
}
