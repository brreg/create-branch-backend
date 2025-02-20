package no.brreg.createbranchbackend.service;


import lombok.extern.slf4j.Slf4j;
import no.brreg.createbranchbackend.dto.PresentationRequestDTO;
import no.brreg.createbranchbackend.dto.PresentationUrlDTO;
import no.brreg.createbranchbackend.model.PresentationResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class IgrantService
{

    @Value("${igrant.api-key}")
    private String api_key;

    @Value("${igrant.endpoint}")
    private String endpoint;

    @Value("${igrant.presentation-request-template-id}")
    private String presentationRequestTemplateId;

    private final WebClient webClient;

    public IgrantService(WebClient.Builder webClientBuilder, AsyncListenIgrantPresentationRequest asyncService) {
        this.webClient = webClientBuilder.build();
        this.asyncService = asyncService;
    }

    private final AsyncListenIgrantPresentationRequest asyncService;

    public PresentationUrlDTO createPresentationUrl(String userSessionId) throws Exception {
        try {
            String presentationResponse = fetchPresentation(userSessionId, api_key);
            var response = new PresentationUrlDTO();
            response.setDidcommUri(presentationResponse);
            return response;
        } catch (Exception e) {
            log.error("Exception: {}", e.getMessage(), e);
            throw e;
        }
    }

    private String fetchPresentation(String userSessionId, String api_key) throws Exception {
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
            asyncService.asyncListenIgrantPresentation(presentationResponse.getVerificationHistory().path("presentationExchangeId").asText(),userSessionId);

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

}
