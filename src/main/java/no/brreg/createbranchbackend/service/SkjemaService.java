package no.brreg.createbranchbackend.service;

import no.brreg.createbranchbackend.dto.SkjemaForhandsutfyltDTO;
import no.brreg.createbranchbackend.dto.SkjemaInnsendtDTO;
import no.brreg.createbranchbackend.dto.SkjemaKomlettTilSigneringDTO;
import no.brreg.createbranchbackend.model.Credential;
import no.brreg.createbranchbackend.model.Signering;
import no.brreg.createbranchbackend.model.SkjemaInnsendt;
import no.brreg.createbranchbackend.model.TenorPerson;
import no.brreg.createbranchbackend.repository.*;
import org.springframework.stereotype.Service;

import java.util.Optional;

// This class is responsible for handle the response when the user is done filling out the NUF registration form

@Service
public class SkjemaService {

    private final CredentialRepository credentialRepository;
    private final SkjemaInnsendtRepository skjemaInnsendtRepository;
    private final TenorPersonRepository tenorPersonRepository;
    private final SigneringRepository signeringRepository;

    public SkjemaService(
            CredentialRepository credentialRepository,
            TenorPersonRepository tenorPersonRepository,
            SkjemaInnsendtRepository skjemaInnsendtRepository,
            SigneringRepository signeringRepository
    ) {
        this.credentialRepository = credentialRepository;
        this.tenorPersonRepository = tenorPersonRepository;
        this.skjemaInnsendtRepository = skjemaInnsendtRepository;
        this.signeringRepository = signeringRepository;
    }

    public SkjemaForhandsutfyltDTO getForhandsutfyltSkjema(String userSessionId) {
        Credential c = credentialRepository.findByUserSessionId(userSessionId);
        if (c == null) {
            return null;
        }
        TenorPerson p = tenorPersonRepository.findByFnr(c.getPersonFnr());

        return new SkjemaForhandsutfyltDTO(c, p);
    }

    public void storeSkjemaInnsendt(String userSessionId, SkjemaInnsendtDTO skjemaInnsendt) {
        SkjemaInnsendt skjema = new SkjemaInnsendt();
        skjema.setUserSessionId(userSessionId);
        skjema.setForetakTelefonnummer(skjemaInnsendt.getForetakTelefonnummer());
        skjema.setForetakWebPageUrl(skjemaInnsendt.getForetakWebPageUrl());
        skjema.setNorskForetaksNavn(skjemaInnsendt.getNorskForetaksNavn());
        skjema.setHarFilialINorge(skjemaInnsendt.isHarFilialINorge());
        skjema.setFilialAdresse(skjemaInnsendt.getFilialAdresse());
        skjema.setFilialPostnummer(skjemaInnsendt.getFilialPostnummer());
        skjema.setFilialPoststed(skjemaInnsendt.getFilialPoststed());
        skjema.setFilialTelefonnummer(skjemaInnsendt.getFilialTelefonnummer());
        skjema.setFilialWebPageUrl(skjemaInnsendt.getFilialWebPageUrl());
        skjema.setFilialNaceKode1(skjemaInnsendt.getFilialNaceKode1());
        skjema.setFilialNaceKode2(skjemaInnsendt.getFilialNaceKode2());
        skjema.setFilialNaceKode3(skjemaInnsendt.getFilialNaceKode3());

        skjemaInnsendtRepository.save(skjema);

        Signering signering = new Signering();
        signering.setUserSessionId(userSessionId);
        signering.setSignert(false);

        signeringRepository.save(signering);
    }

    public SkjemaKomlettTilSigneringDTO getSigneringsOppgave(String userSessionId) {
        Credential c = credentialRepository.findByUserSessionId(userSessionId);
        TenorPerson p = tenorPersonRepository.findByFnr(c.getPersonFnr());
        SkjemaInnsendt skjema = skjemaInnsendtRepository.findByUserSessionId(userSessionId);
        Signering signering = signeringRepository.findByUserSessionId(userSessionId);

        return new SkjemaKomlettTilSigneringDTO(c, p, skjema, signering);
    }

    public Signering signer(String userSessionId) {
        Signering signering = signeringRepository.findByUserSessionId(userSessionId);
        if (signering == null) {
            return null;
        }

        signering.setSignert(true);
        signeringRepository.save(signering);
        return signering;
    }
}
