package no.brreg.createbranchbackend.service;

import lombok.extern.slf4j.Slf4j;
import no.brreg.createbranchbackend.dto.EuccDTO;
import no.brreg.createbranchbackend.dto.NpidDTO;
import no.brreg.createbranchbackend.dto.PresentationUrlDTO;
import no.brreg.createbranchbackend.model.IssuingResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@Slf4j
public class TestDataService {

    @Value("${igrant.api-key}")
    private String api_key;

    @Value("${igrant.endpoint}")
    private String endpoint;

    private final WebClient webClient;

    public TestDataService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }
    public PresentationUrlDTO createTestNpid(String buttonContent) throws Exception {
        try {
            String presentationResponse = issueNpid(buttonContent,api_key);
            var response = new PresentationUrlDTO();
            response.setDidcommUri(presentationResponse);
            return response;
        } catch (Exception e) {
            log.error("Exception: {}", e.getMessage(), e);
            throw e;
        }
    }
    private String issueNpid(String buttonContent,String api_key) throws Exception {
        String igrantURL = endpoint + "v2/config/digital-wallet/openid/sdjwt/credential/issue";
        NpidDTO npidDTO = createSpecificNpid(buttonContent);

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
    public PresentationUrlDTO createTestEucc(String buttonContent) throws Exception {
        try {
            String presentationResponse = issueEucc(buttonContent,api_key);
            var response = new PresentationUrlDTO();
            response.setDidcommUri(presentationResponse);
            return response;
        } catch (Exception e) {
            log.error("Exception: {}", e.getMessage(), e);
            throw e;
        }
    }
    private String issueEucc(String buttonContent,String api_key) throws Exception {
        String igrantURL = endpoint + "v2/config/digital-wallet/openid/sdjwt/credential/issue";
        EuccDTO euccDTO = createSpecificEucc(buttonContent);

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

    private NpidDTO createSpecificNpid(String buttonContent) {
        NpidDTO n = new NpidDTO();
        NpidDTO.Credential.Claims claim = n.getCredential().getClaims();



        switch (buttonContent) {
            case "USELVISK JEGER":
                claim.setBirthDate("1943-01-15");
                claim.setBirthPlace("NORDBERG");
                claim.setEmailAddress("hopefullynotexisting@brreg.no");
                claim.setFamilyName("JEGER");
                claim.setGivenName("USELVISK");
                claim.setMobilePhoneNumber("81549300");
                claim.setNationality("NO");
                claim.setPersonalAdministrativeNumber("15814399394");
                claim.setResidentAddress("Bråtåvegen 70; 2693; NORDBERG");
                claim.setResidentCity("NORDBERG");
                claim.setResidentCountry("NO");
                claim.setResidentHouseNumber("70");
                claim.setResidentPostalCode("2693");
                claim.setResidentStreet("Bråtåvegen");
                claim.setSex(1);

                return n;

            case "FORMBAR OPPORTUNIST":
                claim.setBirthDate("1993-05-10");
                claim.setBirthPlace("SANDER");
                claim.setEmailAddress("hopefullynotexisting@brreg.no");
                claim.setFamilyName("OPPORTUNIST");
                claim.setGivenName("FORMBAR");
                claim.setMobilePhoneNumber("81549300");
                claim.setNationality("NO");
                claim.setPersonalAdministrativeNumber("10859399067");
                claim.setResidentAddress("Sanddalen 20;2116; SANDER");
                claim.setResidentCity("SANDER");
                claim.setResidentCountry("NO");
                claim.setResidentHouseNumber("20");
                claim.setResidentPostalCode("2116");
                claim.setResidentStreet("Sanddalen");
                claim.setSex(2);

                return n;
            case "UTNYTTENDE ÆRFUGL":
                claim.setBirthDate("1994-07-19");
                claim.setBirthPlace("PORSGRUNN");
                claim.setEmailAddress("hopefullynotexisting@brreg.no");
                claim.setFamilyName("ÆRFUGL");
                claim.setGivenName("UTNYTTENDE");
                claim.setMobilePhoneNumber("81549300");
                claim.setNationality("NO");
                claim.setPersonalAdministrativeNumber("19879496295");
                claim.setResidentAddress("Viervegen 26;3929;PORSGRUNN");
                claim.setResidentCity("PORSGRUNN");
                claim.setResidentCountry("NO");
                claim.setResidentHouseNumber("26");
                claim.setResidentPostalCode("3929");
                claim.setResidentStreet("Viervegen");
                claim.setSex(2);

                return n;
            case "UTØRST KLINKEKULE":
                claim.setBirthDate("1971-12-18");
                claim.setBirthPlace("RØST");
                claim.setEmailAddress("hopefullynotexisting@brreg.no");
                claim.setFamilyName("KLINKEKULE");
                claim.setGivenName("UTØRST");
                claim.setMobilePhoneNumber("81549300");
                claim.setNationality("NO");
                claim.setPersonalAdministrativeNumber("18927199897");
                claim.setResidentAddress("Markveien 4;8064;RØST");
                claim.setResidentCity("RØST");
                claim.setResidentCountry("NO");
                claim.setResidentHouseNumber("4");
                claim.setResidentPostalCode("8064");
                claim.setResidentStreet("Markveien");
                claim.setSex(2);

                return n;
            case "VIRTUELL PERSEPSJON":
                claim.setBirthDate("1992-12-08");
                claim.setBirthPlace("AVERØY");
                claim.setEmailAddress("hopefullynotexisting@brreg.no");
                claim.setFamilyName("PERSEPSJON");
                claim.setGivenName("VIRTUELL");
                claim.setMobilePhoneNumber("81549300");
                claim.setNationality("NO");
                claim.setPersonalAdministrativeNumber("08929299389");
                claim.setResidentAddress("Tyttebærstien 15 A;6530;AVERØY");
                claim.setResidentCity("AVERØY");
                claim.setResidentCountry("NO");
                claim.setResidentHouseNumber("15 A");
                claim.setResidentPostalCode("6530");
                claim.setResidentStreet("Tyttebærstien");
                claim.setSex(1);

                return n;
            default:
                // Handle default case
                return n;

        }
    }
    private EuccDTO createSpecificEucc(String buttonContent) {
        EuccDTO e = new EuccDTO();


        return switch (buttonContent) {
            case "USELVISK JEGER" -> {

                e.getCredential().getClaims().getCredentialSubject().setLegalRepresentative(List.of(
                        new EuccDTO.LegalRepresentative("USELVISK JEGER", "1943-01-15", "alone")
                ));

                yield e;
            }
            case "FORMBAR OPPORTUNIST" -> {
                e.getCredential().getClaims().getCredentialSubject().setLegalRepresentative(List.of(
                        new EuccDTO.LegalRepresentative("FORMBAR OPPORTUNIST", "1993-05-10", "alone")
                ));

                yield e;
            }
            case "UTNYTTENDE ÆRFUGL" -> {
                e.getCredential().getClaims().getCredentialSubject().setLegalRepresentative(List.of(
                        new EuccDTO.LegalRepresentative("UTNYTTENDE ÆRFUGL", "1994-07-19", "alone")
                ));

                yield e;
            }
            case "UTØRST KLINKEKULE" -> {
                e.getCredential().getClaims().getCredentialSubject().setLegalRepresentative(List.of(
                        new EuccDTO.LegalRepresentative("UTØRST KLINKEKULE", "1971-12-18", "alone")
                ));

                yield e;
            }
            case "VIRTUELL PERSEPSJON" -> {
                e.getCredential().getClaims().getCredentialSubject().setLegalRepresentative(List.of(
                        new EuccDTO.LegalRepresentative("VIRTUELL PERSEPSJON", "1992-12-08", "alone")
                ));

                yield e;
            }
            default -> e;
        };
    }
}
