package no.brreg.createbranchbackend.repository;

import no.brreg.createbranchbackend.model.Signering;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SigneringRepository extends JpaRepository<Signering, Long> {

    Signering findByUserSessionId(String userSessionId);
}
