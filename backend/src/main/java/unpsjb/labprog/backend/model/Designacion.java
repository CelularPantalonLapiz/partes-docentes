package unpsjb.labprog.backend.model;

import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Designacion {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String situacionRevista;
    private LocalTime fechaInicio;
    private LocalTime fechaFin;

    @ManyToOne
    private Persona persona;

    @ManyToOne
    private Cargo cargo;

}
