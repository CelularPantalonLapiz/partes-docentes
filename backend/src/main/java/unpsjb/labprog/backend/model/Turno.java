package unpsjb.labprog.backend.model;

import com.fasterxml.jackson.annotation.JsonAlias;

public enum Turno {
    @JsonAlias("MANANA")
    MAÑANA,
    TARDE,
    VESPERTINO,
    NOCHE
}
