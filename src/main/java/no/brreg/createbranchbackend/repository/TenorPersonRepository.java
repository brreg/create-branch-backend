package no.brreg.createbranchbackend.repository;

import no.brreg.createbranchbackend.model.TenorPerson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TenorPersonRepository extends JpaRepository<TenorPerson, Long> {

    TenorPerson findByFnr(String fnr);
}
