package unpsjb.labprog.backend.steps.divisiones;

import static org.junit.Assert.assertEquals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import unpsjb.labprog.backend.Response;
import unpsjb.labprog.backend.model.Division;
import unpsjb.labprog.backend.model.Turno;
import unpsjb.labprog.backend.presenter.DivisionPresenter;

public class DivisionSteps {
    private Division division;
    private ResponseEntity<Object> response;

    @Autowired
    private DivisionPresenter divisionPresenter;

    @Dado("el espacio físico división con {int} {int} {string} {string}")
    public void crearDivision(int anio, int numero, String orientacion, String turno) {
        division = new Division();
        division.setAnio(anio);
        division.setNumDivision(numero);
        division.setOrientacion(orientacion);
        String turnoParaEnum = turno.toUpperCase().replace("MANANA", "MAÑANA");

        division.setTurno(Turno.valueOf(turnoParaEnum));
    }

    @Cuando("se presiona el botón de guardar")
    public void presionarGuardar() {
        response = divisionPresenter.create(division);
    }

    @Entonces("se espera el siguiente {int} con la {}")
    public void verificarRespuesta(int status, String respuestaEsperada) {
        assertEquals(status, response.getStatusCode().value());
        Response resBody = (Response) response.getBody();

        assertEquals(respuestaEsperada.trim(), resBody.getMessage().trim());
    }
}