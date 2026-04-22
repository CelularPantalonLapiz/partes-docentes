package unpsjb.labprog.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Persona {

    @Id
    private Long dni;

    private String cuil;
    private String nombre;
    private String apellido;
    private String titulo;
    private String sexo;
    private String domicilio;
    private String telefono;
}
