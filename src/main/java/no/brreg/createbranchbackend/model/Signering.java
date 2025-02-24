package no.brreg.createbranchbackend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "signering")
public class Signering {
    @Id
    private String userSessionId;

    private boolean signert;
}
