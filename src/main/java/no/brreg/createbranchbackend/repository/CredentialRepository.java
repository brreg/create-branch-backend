package no.brreg.createbranchbackend.repository;

import no.brreg.createbranchbackend.model.Credential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CredentialRepository extends JpaRepository<Credential, Long> {

    Credential findByUserSessionId(String userSessionId);
}
