package unpsjb.labprog.backend.model;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Cargo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private int cargaHoraria;

    @JsonProperty("fechaDesde")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaInicio;

    @JsonProperty("fechaHasta")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaFin;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Horario> horarios;

    @ManyToOne
    private Division division;

    @JsonProperty("tipoDesignacion")
    @Enumerated(EnumType.STRING)
    private TipoDesignacion tipo;
}
