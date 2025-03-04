package no.brreg.createbranchbackend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Objects;

@Service
public class AsyncListenIgrantPresentationRequest {

    private final SimpMessagingTemplate messagingTemplate;

    @Value("${igrant.api-key}")
    private String api_key;

    @Value("${igrant.endpoint}")
    private String endpoint;

    private final WebClient webClient = WebClient.create();

    private final CredentialService credentialService;

    public AsyncListenIgrantPresentationRequest(CredentialService credentialService, SimpMessagingTemplate messagingTemplate) {
        this.credentialService = credentialService;
        this.messagingTemplate = messagingTemplate;

    }


    @Async
    public void asyncListenIgrantPresentation(String presentationExchangeId,String userSessionId, String type) throws InterruptedException {
        // Din asynkrone logikk her
        System.out.println("Asynkron metode kjører...PresentationExchangeId: " + presentationExchangeId + " Type: " + type );

        for (int i = 0;i<60;i++)
        {
            Thread.sleep(3000);
            String igrantURL = endpoint + "/v3/config/digital-wallet/openid/sdjwt/verification/history/" + presentationExchangeId;
            String response = webClient.get().uri(igrantURL).header("Authorization",api_key).retrieve().bodyToMono(String.class).block();
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(response);
                if (Objects.equals(jsonNode.path("verificationHistory").path("status").asText(), "presentation_acked"))
                {
                    System.out.println("Credential exchange complete. Continuing..");
                    if (Objects.equals(type, "bulk")) {
                        credentialService.parseResponseAndCreateCredentials(jsonNode.path("verificationHistory").path("presentation"), userSessionId);
                        return;
                    }
                    if (Objects.equals(type, "signatur")) {
                        // inform frontend about data is received from Mattr
                        String destination = "/topic/sessions/" + userSessionId + "_signed";
                        messagingTemplate.convertAndSend(destination, "Request Signed");
                        return;
                    }
                }
                if (i == 39){
                    // TODO: Give error message/timeout to frontend that the process needs to be restarted.
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        }
    }

