package no.brreg.createbranchbackend.repository;

import no.brreg.createbranchbackend.model.SkjemaInnsendt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkjemaInnsendtRepository extends JpaRepository<SkjemaInnsendt, Long> {

    SkjemaInnsendt findByUserSessionId(String userSessionId);
}
