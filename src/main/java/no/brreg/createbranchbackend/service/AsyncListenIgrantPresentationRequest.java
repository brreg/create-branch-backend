package no.brreg.createbranchbackend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsProperties;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Locale;
import java.util.Objects;

@Service
public class AsyncListenIgrantPresentationRequest {

    @Value("${igrant.api-key}")
    private String api_key;

    @Value("${igrant.endpoint}")
    private String endpoint;

    private final WebClient webClient = WebClient.create();

    private final CredentialService credentialService;

    public AsyncListenIgrantPresentationRequest(CredentialService credentialService) {
        this.credentialService = credentialService;
    }


    @Async
    public void asyncListenIgrantPresentation(String presentationExchangeId,String userSessionId) throws InterruptedException {
        // Din asynkrone logikk her
        System.out.println("Asynkron metode kjører...PresentationExchangeId: " + presentationExchangeId);

        for (int i = 0;i<60;i++)
        {
            Thread.sleep(3000); // Simulerer en langvarig oppgave
            String igrantURL = endpoint + "/v3/config/digital-wallet/openid/sdjwt/verification/history/" + presentationExchangeId;
            String response = webClient.get().uri(igrantURL).header("Authorization",api_key).retrieve().bodyToMono(String.class).block();
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(response);
                if (Objects.equals(jsonNode.path("verificationHistory").path("status").asText(), "presentation_acked"))
                {
                    System.out.println("Credential exchange complete. Continuing..");
                    credentialService.parseResponseAndCreateCredentials(jsonNode.path("verificationHistory").path("presentation").get(0).path("vp").path("verifiableCredential"),userSessionId);

                    break;
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

