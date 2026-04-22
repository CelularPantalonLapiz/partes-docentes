package unpsjb.labprog.backend.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Licencia {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private LocalDateTime pedidoDesde;
    private LocalDateTime pedidoHasta;
    private String domicilio;
    private boolean certificadoMedico;

    @ManyToOne
    private Persona persona;

    @ManyToOne
    private ArticuloLicencia articulo;

    @ManyToMany
    private List<Designacion> designaciones;
}
