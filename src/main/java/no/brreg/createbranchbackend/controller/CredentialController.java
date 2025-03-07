package no.brreg.createbranchbackend.controller;

import lombok.extern.slf4j.Slf4j;
import no.brreg.createbranchbackend.model.Credential;
import no.brreg.createbranchbackend.service.CredentialService;
import no.brreg.createbranchbackend.dto.CredentialValidationResultDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000, https://polite-bush-0c26cb003.5.azurestaticapps.net")
public class CredentialController {

    private final CredentialService credentialService;

    public CredentialController(CredentialService credentialService) { this.credentialService = credentialService; }

    @GetMapping("/session")
    public ResponseEntity<?> getSession(@RequestHeader(value = "x-session-id") String userSessionId) {
        if (userSessionId == null || userSessionId.isEmpty()) {
            log.error("Missing x-session-id in header");
            return ResponseEntity.badRequest().body("Missing x-session-id in header");
        }

        try {
            Credential credential = credentialService.getCredentialBySessionId(userSessionId);
            if (credential == null) {
                log.error("Credential not found");
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.ok(credential);
            }
        } catch (Exception e) {
            log.error("Error getting credential", e);
            return ResponseEntity.badRequest().body("Error getting credential");
        }
    }

    @GetMapping("/validateCredential")
    public ResponseEntity<?> validateCredential(@RequestHeader(value = "x-session-id") String userSessionId) {
        if (userSessionId == null || userSessionId.isEmpty()) {
            log.error("Missing x-session-id in header");
            return ResponseEntity.badRequest().body(new CredentialValidationResultDTO(false, "Missing x-session-id in header"));
        }

        try {
            CredentialValidationResultDTO result = credentialService.validateCredential(userSessionId);
            if (result.isValid()) {
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.badRequest().body(result.getReason());
            }
        } catch (Exception e) {
            log.error("Error validating credential", e);
            return ResponseEntity.badRequest().body("Error validating credential: " + e.getMessage());
        }
    }

}
