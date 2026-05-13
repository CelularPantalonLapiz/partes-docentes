package unpsjb.labprog.backend.presenter;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unpsjb.labprog.backend.Response;
import unpsjb.labprog.backend.business.CargoService;
import unpsjb.labprog.backend.business.DesignacionService;
import unpsjb.labprog.backend.business.PersonaService;
import unpsjb.labprog.backend.model.Cargo;
import unpsjb.labprog.backend.model.Designacion;
import unpsjb.labprog.backend.model.Persona;
import unpsjb.labprog.backend.model.SituacionRevista;
import unpsjb.labprog.backend.model.TipoDesignacion;
import unpsjb.labprog.backend.model.Turno;

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

        if (!fechaCheck(aDesignacion)) {
            return ResponseEntity.ok(new Response(400, "Error: Asignar bien las fechas.", null));
        }

        Persona p = personaService.findByDni(aDesignacion.getPersona().getDni());
        Cargo c = cargoService.findById(aDesignacion.getCargo().getId());
        if (p == null || c == null) {
            return ResponseEntity.ok(new Response(400, "Error: Persona o Cargo no encontrados.", null));
        }

        aDesignacion.setPersona(p);
        aDesignacion.setCargo(c);

        if (!vigenciaCheck(aDesignacion, c)) {
            return ResponseEntity.ok(new Response(400, "Error: Asignar las fechas en un lapso posible.", null));
        }

        SituacionRevista situacion = service.buscarSolapamientoDesignaciones(c, aDesignacion);
        aDesignacion.setSituacionRevista(situacion);

        service.save(aDesignacion);

        String nombreCompleto = p.getNombre() + " " + p.getApellido();

        if (SituacionRevista.ERROR.equals(aDesignacion.getSituacionRevista())) {
            Persona personaOcupante = service.quienOcupaDesignacion(c, aDesignacion);
            String nombreOcupante = (personaOcupante != null)
                    ? personaOcupante.getNombre() + " " + personaOcupante.getApellido()
                    : "alguien";

            String msgError = "";

            if (TipoDesignacion.ESPACIO_CURRICULAR.equals(c.getTipo())) {
                msgError = String.format(
                        "%s NO ha sido designado/a debido a que la asignatura %s de la división %dº %dº turno %s lo ocupa %s para el período",
                        nombreCompleto,
                        c.getNombre(),
                        c.getDivision().getAnio(),
                        c.getDivision().getNumDivision(),
                        capitalizarTurno(c.getDivision().getTurno()),
                        nombreOcupante);
            } else {
                msgError = String.format(
                        "%s NO ha sido designado/a como %s. pues el cargo solicitado lo ocupa %s para el período",
                        nombreCompleto,
                        c.getNombre().toLowerCase(),
                        nombreOcupante);
            }

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Response(500, msgError, aDesignacion));
        }
        String msg = armarMensajeExito(aDesignacion, c, nombreCompleto);
        return ResponseEntity.ok(new Response(200, msg, aDesignacion));
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Object> update(@RequestBody Designacion aDesignacion) {
        if (aDesignacion.getId() == null || aDesignacion.getId() <= 0) {
            return Response.notFound("Error: Debe ingresar un ID válido para actualizar.");
        }
        if (fechaCheck(aDesignacion) == false) {
            return ResponseEntity.ok(new Response(400, "Error: Asignar bien las fechas.", null));
        }
        if (vigenciaCheck(aDesignacion, aDesignacion.getCargo()) == false) {
            return ResponseEntity.ok(new Response(400, "Error: Asignar las fechas en un lapso posible.", null));
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

    @GetMapping("/{id}/ocupantes")
    public ResponseEntity<Object> getOcupantes(@PathVariable Long id) {
        Designacion d = service.findById(id);
        if (d == null)
            return Response.notFound("Designación no encontrada");
        List<Designacion> solapados = service.getDesignacionesSolapa(d.getCargo(), d);
        solapados.removeIf(s -> s.getId().equals(id));

        return Response.ok(solapados);
    }

    private boolean fechaCheck(Designacion designacion) {
        if (designacion.getFechaInicio() == null)
            return false;
        if (designacion.getFechaFin() == null)
            return true;
        if (designacion.getFechaInicio().isAfter(designacion.getFechaFin()))
            return false;
        return true;
    }

    private boolean vigenciaCheck(Designacion designacion, Cargo cargo) {
        if (cargo.getFechaInicio().isAfter(designacion.getFechaInicio()))
            return false;
        if (cargo.getFechaFin() != null && designacion.getFechaFin() != null) {
            if (cargo.getFechaFin().isBefore(designacion.getFechaFin()))
                return false;
        }
        return true;
    }

    private String capitalizarTurno(Turno turno) {
        String t = turno.toString().toLowerCase();
        if (t.equals("manana"))
            return "Mañana";
        return t.substring(0, 1).toUpperCase() + t.substring(1);
    }

    private String armarMensajeExito(Designacion d, Cargo c, String nombre) {
        if (TipoDesignacion.ESPACIO_CURRICULAR.equals(c.getTipo())) {
            return String.format(
                    "%s ha sido designado/a a la asignatura %s a la división %dº %dº turno %s exitosamente",
                    nombre, c.getNombre(), c.getDivision().getAnio(), c.getDivision().getNumDivision(),
                    capitalizarTurno(c.getDivision().getTurno()));
        } else {
            long previos = service.countByCargo(c);
            String titulo = (previos > 1) ? "Auxiliar" : c.getNombre();
            return String.format("%s ha sido designado/a como %s exitosamente", nombre, titulo);
        }
    }

}
