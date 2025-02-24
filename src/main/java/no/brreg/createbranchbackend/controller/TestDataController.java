package no.brreg.createbranchbackend.controller;

import lombok.extern.slf4j.Slf4j;
import no.brreg.createbranchbackend.dto.PresentationUrlDTO;
import no.brreg.createbranchbackend.service.TestDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/testdata")
@Slf4j
public class TestDataController {
    private final TestDataService testDataService;

    @Autowired
    public TestDataController(TestDataService testDataService) {
        this.testDataService = testDataService;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/qrcodeNpid")
    public ResponseEntity<?> createQrCodeNpid(@RequestParam(value = "button", required = false, defaultValue = "no_data") String buttonContent) {
        try {
            PresentationUrlDTO presentationUrlDTO = testDataService.createTestNpid(buttonContent);
            return ResponseEntity.ok(presentationUrlDTO);
        } catch (Exception e) {
            log.error("Error creating QR code: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/qrcodeEucc")
    public ResponseEntity<?> createQrCodeEucc(@RequestParam(value = "button", required = false, defaultValue = "no_data") String buttonContent) {
        try {
            PresentationUrlDTO presentationUrlDTO = testDataService.createTestEucc(buttonContent);
            return ResponseEntity.ok(presentationUrlDTO);
        } catch (Exception e) {
            log.error("Error creating QR code: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}