package unpsjb.labprog.backend.presenter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import unpsjb.labprog.backend.Response;
import unpsjb.labprog.backend.business.PersonaService;
import unpsjb.labprog.backend.model.Persona;

@RestController
@RequestMapping("personas")
public class PersonaPresenter {

    @Autowired
    private PersonaService personaService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Object> findAll() {
        return Response.ok(personaService.findAll());
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Object> create(@RequestBody Persona aPersona) {
        if (aPersona.getDni() <= 0 || aPersona.getDni() == null) {
            return Response.notFound("Para crear un docente debe ingresar un DNI válido.");
        }

        personaService.save(aPersona);
        String mensaje = String.format("%s %s con DNI %d ingresado/a correctamente",
                aPersona.getNombre(),
                aPersona.getApellido(),
                aPersona.getDni());
        return Response.ok(mensaje);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Object> update(@RequestBody Persona aPersona) {
        if (aPersona.getDni() == null || aPersona.getDni() <= 0) {
            return Response.notFound("Está intentando actualizar un docente sin un DNI válido.");
        }
        personaService.save(aPersona);
        String mensaje = String.format("%s %s con DNI %d actualizado/a correctamente",
                aPersona.getNombre(),
                aPersona.getApellido(),
                aPersona.getDni());
        return Response.ok(mensaje);
    }

    @RequestMapping(value = "/{dni}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> delete(@PathVariable("dni") Long dni) {
        personaService.delete(dni);
        String mensaje = String.format("Docente con DNI " + dni + " eliminado correctamente");
        return Response.ok(mensaje);
    }

    @RequestMapping(value = "/dni/{dni}", method = RequestMethod.GET)
    public ResponseEntity<Object> findByDni(@PathVariable("dni") Long dni) {
        Persona p = personaService.findByDni(dni);
        return (p != null) ? Response.ok(p) : Response.notFound("No se encontró el docente con DNI: " + dni);
    }

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public ResponseEntity<Object> findByPage(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Response.ok(personaService.findByPage(page, size));
    }

}
