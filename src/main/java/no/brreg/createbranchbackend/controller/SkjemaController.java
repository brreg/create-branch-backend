package no.brreg.createbranchbackend.controller;

import lombok.extern.slf4j.Slf4j;
import no.brreg.createbranchbackend.dto.SkjemaInnsendtDTO;
import no.brreg.createbranchbackend.dto.SkjemaKomlettTilSigneringDTO;
import no.brreg.createbranchbackend.model.Signering;
import no.brreg.createbranchbackend.service.SkjemaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// TODO: implementer skjema innsending og signering i frontend

@Slf4j
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000, https://polite-bush-0c26cb003.5.azurestaticapps.net")
public class SkjemaController {

    private final SkjemaService skjemaService;

    public SkjemaController(SkjemaService skjemaService) {
        this.skjemaService = skjemaService;
    }

    // send inn utfylt skjema
    @PostMapping("/sendinn")
    public String storeSkjema(@RequestHeader(value = "x-session-id") String userSessionId, @RequestBody SkjemaInnsendtDTO skjema) {
        try {
            skjemaService.storeSkjemaInnsendt(userSessionId, skjema);
            return "Skjema received and saved successfully.";
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return "Failed to process credentials.";
        }
    }

    // hent signeringsoppgave
    @GetMapping("/signeringsoppgave")
    public ResponseEntity<?> getSigneringsOppgave(@RequestHeader(value = "x-session-id") String userSessionId) {
        if (userSessionId == null || userSessionId.isEmpty()) {
            log.error("Missing x-session-id in header");
        }

        try {
            SkjemaKomlettTilSigneringDTO skjema = skjemaService.getSigneringsOppgave(userSessionId);
            if (skjema == null) {
                log.info("signeringsoppgave skjema for sessionId {} not found", userSessionId);
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(skjema);
        } catch (Exception e) {
            log.error("Error getting credential", e);
            return ResponseEntity.badRequest().body("Error getting signeringsOppgave");
        }
    }

    // signer signeringsoppgave
    @PostMapping("/signeringsoppgave")
    public ResponseEntity<?> signerOppgave(@RequestHeader(value = "x-session-id") String userSessionId) {
        if (userSessionId == null || userSessionId.isEmpty()) {
            log.error("Missing x-session-id in header");
        }

        try {
            Signering signering = skjemaService.signer(userSessionId);

            if (signering == null) {
                log.info("signeringsoppgave skjema for sessionId {} not found", userSessionId);
                return ResponseEntity.noContent().build();
            }
            log.info("oppgave med userSessionId {} ble signert", userSessionId);
            return ResponseEntity.ok(signering);

        } catch (Exception e) {
            log.error("Error getting credential", e);
            return ResponseEntity.badRequest().body("Error getting signeringsOppgave");
        }
    }

}
