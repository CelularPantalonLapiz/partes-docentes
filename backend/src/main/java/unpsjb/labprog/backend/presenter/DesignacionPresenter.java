package unpsjb.labprog.backend.presenter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unpsjb.labprog.backend.Response;
import unpsjb.labprog.backend.business.CargoService;
import unpsjb.labprog.backend.business.DesignacionService;
import unpsjb.labprog.backend.business.PersonaService;
import unpsjb.labprog.backend.model.Cargo;
import unpsjb.labprog.backend.model.Designacion;
import unpsjb.labprog.backend.model.Persona;
import unpsjb.labprog.backend.model.TipoDesignacion;

@RestController
@RequestMapping("designaciones")
public class DesignacionPresenter {

    @Autowired
    private DesignacionService service;

    @Autowired
    private PersonaService personaService;

    @Autowired
    private CargoService cargoService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Object> findAll() {
        return Response.ok(service.findAll());
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Object> create(@RequestBody Designacion aDesignacion) {
        if (aDesignacion.getId() != null && aDesignacion.getId() != 0) {
            return Response.notFound("Error: No se puede crear una designación con un ID definido.");
        }

        Persona p = personaService.findByDni(aDesignacion.getPersona().getDni());
        Cargo c = cargoService.findById(aDesignacion.getCargo().getId());
        if (p == null || c == null) {
            return ResponseEntity.ok(new Response(400, "Error: Persona o Cargo no encontrados.", null));
        }
        aDesignacion.setPersona(p);
        long asignacionesPrevias = service.countByCargo(c);
        service.save(aDesignacion);
        String msg = "";
        String nombreCompleto = p.getNombre() + " " + p.getApellido();

        if (TipoDesignacion.ESPACIO_CURRICULAR.equals(c.getTipo())) {

            String turnoFormateado = c.getDivision().getTurno().toString().toLowerCase();
            turnoFormateado = turnoFormateado.substring(0, 1).toUpperCase() + turnoFormateado.substring(1);
            if (turnoFormateado.equals("Manana"))
                turnoFormateado = "Mañana";

            msg = String.format("%s ha sido designado/a a la asignatura %s a la división %dº %dº turno %s exitosamente",
                    nombreCompleto, c.getNombre(),
                    c.getDivision().getAnio(), c.getDivision().getNumDivision(), turnoFormateado);
        } else {
            String tituloParaMensaje = (asignacionesPrevias > 0) ? "Auxiliar" : c.getNombre();

            msg = String.format("%s ha sido designado/a como %s exitosamente",
                    nombreCompleto, tituloParaMensaje);
        }
        return ResponseEntity.ok(new Response(200, msg, aDesignacion));
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Object> update(@RequestBody Designacion aDesignacion) {
        if (aDesignacion.getId() == null || aDesignacion.getId() <= 0) {
            return Response.notFound("Error: Debe ingresar un ID válido para actualizar.");
        }
        return Response.ok(service.save(aDesignacion));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return Response.ok("Designación " + id + " eliminada.");
    }

    @RequestMapping(value = "/id/{id}", method = RequestMethod.GET)
    public ResponseEntity<Object> findById(@PathVariable("id") Long id) {
        Designacion d = service.findById(id);
        return (d != null) ? Response.ok(d) : Response.notFound("Designación no encontrada.");
    }

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public ResponseEntity<Object> findByPage(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Response.ok(service.findByPage(page, size));
    }
}
