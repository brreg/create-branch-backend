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
        EuccDTO euccDTO = new EuccDTO();

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
        NpidDTO.CredentialSubject credentialSubject = n.credential.credentialSubject;



        switch (buttonContent) {
            case "USELVISK JEGER":
                credentialSubject.setBirthDate("1943-01-15");
                credentialSubject.setBirthPlace("NORDBERG");
                credentialSubject.setEmailAddress("hopefullynotexisting@brreg.no");
                credentialSubject.setFamilyName("JEGER");
                credentialSubject.setGivenName("USELVISK");
                credentialSubject.setMobilePhoneNumber("81549300");
                credentialSubject.setNationality("NO");
                credentialSubject.setPersonalAdministrativeNumber("15814399394");
                credentialSubject.setResidentAddress("Bråtåvegen 70; 2693; NORDBERG");
                credentialSubject.setResidentCity("NORDBERG");
                credentialSubject.setResidentCountry("NO");
                credentialSubject.setResidentHouseNumber("70");
                credentialSubject.setResidentPostalCode("2693");
                credentialSubject.setResidentStreet("Bråtåvegen");
                credentialSubject.setSex(1);

                return n;

            case "FORMBAR OPPORTUNIST":
                credentialSubject.setBirthDate("1993-05-10");
                credentialSubject.setBirthPlace("SANDER");
                credentialSubject.setEmailAddress("hopefullynotexisting@brreg.no");
                credentialSubject.setFamilyName("OPPORTUNIST");
                credentialSubject.setGivenName("FORMBAR");
                credentialSubject.setMobilePhoneNumber("81549300");
                credentialSubject.setNationality("NO");
                credentialSubject.setPersonalAdministrativeNumber("10859399067");
                credentialSubject.setResidentAddress("Sanddalen 20;2116; SANDER");
                credentialSubject.setResidentCity("SANDER");
                credentialSubject.setResidentCountry("NO");
                credentialSubject.setResidentHouseNumber("20");
                credentialSubject.setResidentPostalCode("2116");
                credentialSubject.setResidentStreet("Sanddalen");
                credentialSubject.setSex(2);

                return n;
            case "UTNYTTENDE ÆRFUGL":
                credentialSubject.setBirthDate("1994-07-19");
                credentialSubject.setBirthPlace("PORSGRUNN");
                credentialSubject.setEmailAddress("hopefullynotexisting@brreg.no");
                credentialSubject.setFamilyName("ÆRFUGL");
                credentialSubject.setGivenName("UTNYTTENDE");
                credentialSubject.setMobilePhoneNumber("81549300");
                credentialSubject.setNationality("NO");
                credentialSubject.setPersonalAdministrativeNumber("19879496295");
                credentialSubject.setResidentAddress("Viervegen 26;3929;PORSGRUNN");
                credentialSubject.setResidentCity("PORSGRUNN");
                credentialSubject.setResidentCountry("NO");
                credentialSubject.setResidentHouseNumber("26");
                credentialSubject.setResidentPostalCode("3929");
                credentialSubject.setResidentStreet("Viervegen");
                credentialSubject.setSex(2);

                return n;
            case "UTØRST KLINKEKULE":
                credentialSubject.setBirthDate("1971-12-18");
                credentialSubject.setBirthPlace("RØST");
                credentialSubject.setEmailAddress("hopefullynotexisting@brreg.no");
                credentialSubject.setFamilyName("KLINKEKULE");
                credentialSubject.setGivenName("UTØRST");
                credentialSubject.setMobilePhoneNumber("81549300");
                credentialSubject.setNationality("NO");
                credentialSubject.setPersonalAdministrativeNumber("18927199897");
                credentialSubject.setResidentAddress("Markveien 4;8064;RØST");
                credentialSubject.setResidentCity("RØST");
                credentialSubject.setResidentCountry("NO");
                credentialSubject.setResidentHouseNumber("4");
                credentialSubject.setResidentPostalCode("8064");
                credentialSubject.setResidentStreet("Markveien");
                credentialSubject.setSex(2);

                return n;
            case "VIRTUELL PERSEPSJON":
                credentialSubject.setBirthDate("1992-12-08");
                credentialSubject.setBirthPlace("AVERØY");
                credentialSubject.setEmailAddress("hopefullynotexisting@brreg.no");
                credentialSubject.setFamilyName("PERSEPSJON");
                credentialSubject.setGivenName("VIRTUELL");
                credentialSubject.setMobilePhoneNumber("81549300");
                credentialSubject.setNationality("NO");
                credentialSubject.setPersonalAdministrativeNumber("08929299389");
                credentialSubject.setResidentAddress("Tyttebærstien 15 A;6530;AVERØY");
                credentialSubject.setResidentCity("AVERØY");
                credentialSubject.setResidentCountry("NO");
                credentialSubject.setResidentHouseNumber("15 A");
                credentialSubject.setResidentPostalCode("6530");
                credentialSubject.setResidentStreet("Tyttebærstien");
                credentialSubject.setSex(1);

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

                e.getCredential().getCredentialSubject().setLegalRepresentative(List.of(
                        new EuccDTO.LegalRepresentative("USELVISK JEGER", "1943-01-15", "alone")
                ));

                yield e;
            }
            case "FORMBAR OPPORTUNIST" -> {
                e.getCredential().getCredentialSubject().setLegalRepresentative(List.of(
                        new EuccDTO.LegalRepresentative("FORMBAR OPPORTUNIST", "1993-05-10", "alone")
                ));

                yield e;
            }
            case "UTNYTTENDE ÆRFUGL" -> {
                e.getCredential().getCredentialSubject().setLegalRepresentative(List.of(
                        new EuccDTO.LegalRepresentative("UTNYTTENDE ÆRFUGL", "1994-07-19", "alone")
                ));

                yield e;
            }
            case "UTØRST KLINKEKULE" -> {
                e.getCredential().getCredentialSubject().setLegalRepresentative(List.of(
                        new EuccDTO.LegalRepresentative("UTØRST KLINKEKULE", "1971-12-18", "alone")
                ));

                yield e;
            }
            case "VIRTUELL PERSEPSJON" -> {
                e.getCredential().getCredentialSubject().setLegalRepresentative(List.of(
                        new EuccDTO.LegalRepresentative("VIRTUELL PERSEPSJON", "1992-12-08", "alone")
                ));

                yield e;
            }
            default -> e;
        };
    }
}
