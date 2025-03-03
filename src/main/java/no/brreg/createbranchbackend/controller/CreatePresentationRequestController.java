package no.brreg.createbranchbackend.controller;

import lombok.extern.slf4j.Slf4j;
import no.brreg.createbranchbackend.dto.PresentationUrlDTO;
import no.brreg.createbranchbackend.service.IgrantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Slf4j
@CrossOrigin(origins = "http://localhost:3000, https://polite-bush-0c26cb003.5.azurestaticapps.net")
public class CreatePresentationRequestController {

    private final IgrantService igrantService;

    @Autowired
    public CreatePresentationRequestController(IgrantService igrantService) {
        this.igrantService = igrantService;
    }

    @PostMapping("/qrcode")
    public ResponseEntity<?> createQrCode(@RequestHeader(value = "x-session-id") String userSessionId) {
        if (userSessionId == null || userSessionId.isEmpty()) {
            log.error("Missing x-session-id in header");
            return ResponseEntity.badRequest().body("Missing x-session-id in header");
        }

        try {
            PresentationUrlDTO presentationResponseDTO = igrantService.createPresentationUrlBulk(userSessionId);
            return ResponseEntity.ok(presentationResponseDTO);
        } catch (Exception e) {
            log.error("Error creating QR code: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
    @PostMapping("/qrcodeNpid")
    public ResponseEntity<?> createQrCodeNpid(@RequestHeader(value = "x-session-id") String userSessionId) {
        if (userSessionId == null || userSessionId.isEmpty()) {
            log.error("Missing x-session-id in header");
            return ResponseEntity.badRequest().body("Missing x-session-id in header");
        }

        try {
            PresentationUrlDTO presentationResponseDTO = igrantService.createPresentationUrlNpid(userSessionId);
            return ResponseEntity.ok(presentationResponseDTO);
        } catch (Exception e) {
            log.error("Error creating QR code: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PostMapping("/qrcodeReceipt")
    public ResponseEntity<?> createQrCodeReceipt(@RequestHeader(value = "x-session-id") String userSessionId) {
        if (userSessionId == null || userSessionId.isEmpty()) {
            log.error("Missing x-session-id in header");
            return ResponseEntity.badRequest().body("Missing x-session-id in header");
        }

        try {
            PresentationUrlDTO presentationResponseDTO = igrantService.issueReceipt(userSessionId);
            return ResponseEntity.ok(presentationResponseDTO);
        } catch (Exception e) {
            log.error("Error creating QR code: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }




    /*
    //This block is allowed to live rent free here, until the DID functionality is supported in igrant.
    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/message")
    public ResponseEntity<?> createMessage(@RequestHeader(value = "x-session-id") String userSessionId, @RequestHeader(value = "recipient-did-url") String recipient) {
        if (userSessionId == null || userSessionId.isEmpty()) {
            log.error("Missing x-session-id in header");
            return ResponseEntity.badRequest().body("Missing x-session-id in header");
        }
        if (recipient == null || recipient.isEmpty()) {
            log.error("Missing recipient in header");
            return ResponseEntity.badRequest().body("Missing recipient in header");
        }

        try {
            igrantService.createPresentationMessage(userSessionId, recipient);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error sending message {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
 */
}
