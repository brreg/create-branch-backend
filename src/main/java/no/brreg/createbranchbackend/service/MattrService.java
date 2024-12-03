package no.brreg.createbranchbackend.service;

import lombok.extern.slf4j.Slf4j;
import no.brreg.createbranchbackend.dto.*;
import no.brreg.createbranchbackend.model.PresentationResponse;
import no.brreg.createbranchbackend.model.MessageEncryptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class MattrService {

    @Value("${mattr.client-id}")
    private String clientId;

    @Value("${mattr.client-secret}")
    private String clientSecret;

    @Value("${mattr.tenant-url}")
    private String mattrTenantUrl;

    @Value("${mattr.presentation-request-template-id}")
    private String presentationRequestTemplateId;

    @Value("${mattr.brreg-wallet-did}")
    private String brregWalletDid;

    @Value("${mattr.brreg-wallet-did-long}")
    private String brregWalletDidLong;

    @Value("${mattr.callback-url}")
    private String callbackUrl;

    private final WebClient webClient;

    public MattrService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    private String fetchAccessToken() throws Exception {
        String tokenUrl = "https://auth.au01.mattr.global/oauth/token";

        TokenRequestDTO tokenRequest = new TokenRequestDTO();
        tokenRequest.setClientId(clientId);
        tokenRequest.setClientSecret(clientSecret);
        tokenRequest.setAudience(mattrTenantUrl);
        tokenRequest.setGrantType("client_credentials");

        try {
            TokenResponseDTO tokenResponse = webClient.post()
                    .uri(tokenUrl)
                    .bodyValue(tokenRequest)
                    .retrieve()
                    .onStatus(
                            status -> !status.is2xxSuccessful(),
                            clientResponse -> clientResponse.bodyToMono(String.class)
                                    .flatMap(errorBody -> {
                                        log.error("Error fetching access token: {}", errorBody);
                                        return Mono.error(new Exception("Error fetching access token: " + errorBody));
                                    })
                    )
                    .bodyToMono(TokenResponseDTO.class)
                    .block();

            if (tokenResponse != null) {
                return tokenResponse.getAccessToken();
            } else {
                throw new Exception("Failed to fetch access token from Mattr");
            }
        } catch (WebClientResponseException e) {
            log.error("error fetching access token: {}", e.getResponseBodyAsString());
            throw new Exception("Error fetching access token: " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            log.error("Exception: {}", e.getMessage(), e);
            throw e;
        }
    }

    private PresentationResponse fetchPresentation(String userSessionId, String accessToken) throws Exception {
        String mattrURL = mattrTenantUrl + "/v2/credentials/web-semantic/presentations/requests";

        PresentationRequestDTO presentationRequestDTO = new PresentationRequestDTO();
        presentationRequestDTO.setChallenge(userSessionId);
        presentationRequestDTO.setCallbackUrl(mattrURL);
        presentationRequestDTO.setTemplateId(presentationRequestTemplateId);
        presentationRequestDTO.setDid(brregWalletDid);
        presentationRequestDTO.setCallbackUrl(callbackUrl);

        try {
            PresentationResponse presentationResponse = webClient.post()
                    .uri(mattrURL)
                    .header("Authorization", "Bearer " + accessToken)
                    .bodyValue(presentationRequestDTO)
                    .retrieve()
                    .onStatus(
                            status -> status != HttpStatus.CREATED,
                            clientResponse -> clientResponse.bodyToMono(String.class)
                                    .flatMap(errorBody -> {
                                        log.error("Error creating presentation request: {}", errorBody);
                                        return Mono.error(new Exception("Error creating presentation request: " + errorBody));
                                    })
                    )
                    .bodyToMono(PresentationResponse.class)
                    .block();

            if (presentationResponse == null) {
                throw new Exception("Failed to create presentation request from Mattr");
            }
            return presentationResponse;

        } catch (WebClientResponseException e) {
            log.error("error creating presentation request: {}", e.getResponseBodyAsString());
            throw new Exception("Error creating presentation request: " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            log.error("Exception: {}", e.getMessage(), e);
            throw e;
        }
    }

    private MessageEncryptionResponse encryptMessage(PresentationResponse presentationResponse, String recipientDidUrl, String accessToken) throws Exception {
        String mattrURL = mattrTenantUrl + "/core/v1/messaging/encrypt";

        String[] recipientDidUrls = new String[] {recipientDidUrl};

        MessageEncryptionDTO messageEncryptionDTO = new MessageEncryptionDTO();
        messageEncryptionDTO.setSenderDidUrl(brregWalletDidLong);
        messageEncryptionDTO.setRecipientDidUrls(recipientDidUrls);
        messageEncryptionDTO.setPayload(presentationResponse.getRequest());

        log.debug("message to encrypt: {}", messageEncryptionDTO);

        try {
            MessageEncryptionResponse messageEncryptionResponse = webClient.post()
                    .uri(mattrURL)
                    .header("Authorization", "Bearer " + accessToken)
                    .bodyValue(messageEncryptionDTO)
                    .retrieve()
                    .onStatus(
                            status -> status != HttpStatus.OK,
                            clientResponse -> clientResponse.bodyToMono(String.class)
                                    .flatMap(errorBody -> {
                                        log.error("Error encrypting message: {}", errorBody);
                                        return Mono.error(new Exception("Error encrypting message: " + errorBody));
                                    })
                    )
                    .bodyToMono(MessageEncryptionResponse.class)
                    .block();

            if (messageEncryptionResponse == null) {
                throw new Exception("Error encrypting message");
            }
            return messageEncryptionResponse;
        } catch (WebClientResponseException e) {
            log.error("Error encrypting message: {}", e.getResponseBodyAsString());
            throw new Exception("Error encrypting message: " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            log.error("Error encrypting message: {}", e.getMessage(), e);
            throw e;
        }
    }

    private void sendMessage(MessageEncryptionResponse message, String recipient, String accessToken) throws Exception {
        log.debug("send presentation request to wallet {}", recipient);
        String mattrURL = mattrTenantUrl + "/core/v1/messaging/send";

        MessageTransmissionDTO messageTransmissionDTO = new MessageTransmissionDTO();
        messageTransmissionDTO.setTo(recipient);
        messageTransmissionDTO.setMessage(message.getJwe());

        log.debug("message to send: {}", messageTransmissionDTO);

        try {
            webClient.post()
                    .uri(mattrURL)
                    .header("Authorization", "Bearer " + accessToken)
                    .bodyValue(messageTransmissionDTO)
                    .retrieve()
                    .onStatus(
                            status -> status != HttpStatus.OK,
                            clientResponse -> clientResponse.bodyToMono(String.class)
                                    .flatMap(errorBody -> {
                                        log.error("Error sending message: {}", errorBody);
                                        return Mono.error(new Exception("Error sending message: " + errorBody));
                                    })
                    )
                    .bodyToMono(String.class)
                    .block();
        } catch (WebClientResponseException e) {
            log.error("Error sending message: {}", e.getResponseBodyAsString());
            throw new Exception("Error sending message: " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            log.error("Error sending message: {}", e.getMessage(), e);
        }
    }

    public PresentationUrlDTO createPresentationUrl(String userSessionId) throws Exception {
        try {
            String accessToken = fetchAccessToken();
            PresentationResponse presentationResponse = fetchPresentation(userSessionId, accessToken);
            var response = new PresentationUrlDTO();
            response.setDidcommUri(presentationResponse.getDidcommUri());
            return response;
        } catch (Exception e) {
            log.error("Exception: {}", e.getMessage(), e);
            throw e;
        }
    }

    public void createPresentationMessage(String userSessionId, String recipient) throws Exception {
        try {
            String accessToken = fetchAccessToken();
            PresentationResponse presentationResponse = fetchPresentation(userSessionId, accessToken);
            MessageEncryptionResponse messageEncryptionResponse = encryptMessage(presentationResponse, recipient, accessToken);
            sendMessage(messageEncryptionResponse, recipient, accessToken);
            log.info("Message sendt to wallet {}", recipient);
        } catch (Exception e) {
            log.error("Exception: {}", e.getMessage(), e);
            throw e;
        }
    }
}
