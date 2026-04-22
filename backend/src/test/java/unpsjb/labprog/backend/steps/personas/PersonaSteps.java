package unpsjb.labprog.backend.steps.personas;

import static org.junit.Assert.assertEquals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.boot.test.context.SpringBootTest;
import io.cucumber.spring.CucumberContextConfiguration;

import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dada;
import io.cucumber.java.es.Entonces;
import unpsjb.labprog.backend.Response;
import unpsjb.labprog.backend.model.Persona;
import unpsjb.labprog.backend.presenter.PersonaPresenter;

public class PersonaSteps {

    @Autowired
    private PersonaPresenter personaPresenter;

    private Persona persona;
    private ResponseEntity<Object> response;

    @Dada("^la persona con \"([^\"]*)\" \"([^\"]*)\" (\\d+) \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\"$")
    public void la_persona_con(String nombre, String apellido, Long dni, String cuil, String sexo, String titulo,
            String domicilio, String telefono) {
        persona = new Persona();
        persona.setDni(dni);
        persona.setNombre(nombre);
        persona.setApellido(apellido);
        persona.setCuil(cuil);
        persona.setSexo(sexo);
        persona.setTitulo(titulo);
        persona.setDomicilio(domicilio);
        persona.setTelefono(telefono);
    }

    @Cuando("^se presiona el botón de guardar$")
    public void se_presiona_el_boton_de_guardar() {
        response = personaPresenter.create(persona);
    }

    @Entonces("^se espera el siguiente (\\d+) con la \"([^\"]*)\"$")
    public void se_espera_el_siguiente_status_con_la_respuesta(int status, String respuestaEsperada) {
        assertEquals(status, response.getStatusCode().value());

        Object body = response.getBody();

        String mensajeRecibido = ((Response) body).getData().toString();

        assertEquals(respuestaEsperada, mensajeRecibido);
    }
}