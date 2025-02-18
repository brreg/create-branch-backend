package no.brreg.createbranchbackend.service;

import jakarta.annotation.PostConstruct;
import no.brreg.createbranchbackend.model.TenorPerson;
import no.brreg.createbranchbackend.repository.TenorPersonRepository;
import org.springframework.stereotype.Service;

// This class fills the database with testdata, to avoid connecting our service to ann Tenor testdata server

@Service
public class TenorDataLoaderService {

    private final TenorPersonRepository tenorRepository;

    public TenorDataLoaderService(TenorPersonRepository tenorRepository) {
        this.tenorRepository = tenorRepository;
    }

    @PostConstruct
    public void initData() {
        TenorPerson tenor1 = new TenorPerson();
        tenor1.setFnr("10874797230");
        tenor1.setNavn("TOPP AMBASSADE");
        tenor1.setAdresse("Torvgaten 7 A");
        tenor1.setPostnummer("3110");
        tenor1.setPoststed("TØNSBERG");
        tenor1.setLand("Norway");

        tenorRepository.save(tenor1);
    }
}