package no.brreg.createbranchbackend.controller;

import lombok.extern.slf4j.Slf4j;
import no.brreg.createbranchbackend.service.CredentialService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/credentials")
@Slf4j
@CrossOrigin(origins = "*")
public class MattrCallbackController {

    private final CredentialService credentialService;

    public MattrCallbackController(CredentialService credentialService) {
        this.credentialService = credentialService;
    }

    @PostMapping("/receive")
    public String receiveCredentials(@RequestBody String jsonResponse) {
        try {
            credentialService.parseResponseAndCreateCredentials(jsonResponse);
            return "Credentials received and saved successfully.";
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return "Failed to process credentials.";
        }
    }
}
