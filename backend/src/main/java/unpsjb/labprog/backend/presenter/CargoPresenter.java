package unpsjb.labprog.backend.presenter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import unpsjb.labprog.backend.Response;
import unpsjb.labprog.backend.business.CargoService;
import unpsjb.labprog.backend.model.Cargo;
import unpsjb.labprog.backend.model.TipoDesignacion;

@RestController
@RequestMapping("cargos")
public class CargoPresenter {
    @Autowired
    private CargoService service;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Object> findAll() {
        return Response.ok(service.findAll());
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Object> create(@RequestBody Cargo aCargo) {
        if (aCargo.getId() != null && aCargo.getId() != 0) {
            return Response.notFound("Error: No se puede crear un cargo con un ID ya definido.");
        }

        if (aCargo.getTipo() == TipoDesignacion.CARGO && aCargo.getDivision() != null) {
            return new ResponseEntity<>(
                    new Response(501, "Cargo de " + aCargo.getNombre() + " es CARGO y no corresponde asignar división",
                            null),
                    HttpStatus.valueOf(501));
        }

        if (!fechasCheck(aCargo)) {
            return ResponseEntity
                    .ok(new Response(400, "Error: La fecha de inicio no puede ser posterior a la de fin.", null));
        }

        if (aCargo.getTipo() == TipoDesignacion.ESPACIO_CURRICULAR && aCargo.getDivision() == null) {
            return new ResponseEntity<>(
                    new Response(501, "Espacio Curricular " + aCargo.getNombre() + " falta asignar división", null),
                    HttpStatus.valueOf(501));
        }
        aCargo.setDivision(aCargo.getDivision());
        Cargo guardado = service.save(aCargo);

        String msg;
        if (guardado.getTipo() == TipoDesignacion.CARGO) {
            String nombreCorto = guardado.getNombre().split(" ")[0];
            msg = "Cargo de " + nombreCorto + " ingresado correctamente";
        } else {
            msg = String.format("Espacio Curricular %s para la división %dº %dº Turno %s ingresado correctamente",
                    guardado.getNombre(),
                    guardado.getDivision().getAnio(),
                    guardado.getDivision().getNumDivision(),
                    capitalizar(guardado.getDivision().getTurno().toString()));
        }

        return ResponseEntity.ok(new Response(200, msg, null));
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Object> update(@RequestBody Cargo aCargo) {
        if (aCargo.getId() == null || aCargo.getId() <= 0) {
            return Response.notFound("Error: Debe ingresar un ID válido para actualizar el cargo.");
        }
        if (!fechasCheck(aCargo)) {
            return ResponseEntity
                    .ok(new Response(400, "Error: La fecha de inicio no puede ser posterior a la de fin.", null));
        }

        return Response.ok(service.save(aCargo));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return Response.ok("Cargo con ID " + id + " eliminado correctamente.");
    }

    @RequestMapping(value = "/id/{id}", method = RequestMethod.GET)
    public ResponseEntity<Object> findById(@PathVariable("id") Long id) {
        Cargo c = service.findById(id);
        return (c != null) ? Response.ok(c) : Response.notFound("No se encontró el cargo con ID: " + id);
    }

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public ResponseEntity<Object> findByPage(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Response.ok(service.findByPage(page, size));
    }

    @RequestMapping(value = "/search/{term}", method = RequestMethod.GET)
    public ResponseEntity<Object> search(@PathVariable("term") String term) {
        return Response.ok(service.search(term));
    }

    @RequestMapping(value = "/division/{divisionId}", method = RequestMethod.GET)
    public ResponseEntity<Object> findByDivision(@PathVariable("divisionId") Long divisionId) {
        return Response.ok(service.findByDivision(divisionId));
    }

    private String capitalizar(String s) {
        if (s == null)
            return "";
        String lower = s.toLowerCase().replace("manana", "Mañana");
        if (lower.equals("mañana"))
            return "Mañana";
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }

    private boolean fechasCheck(Cargo cargo) {
        if (cargo.getFechaInicio() == null)
            return false;
        if (cargo.getFechaFin() == null)
            return true;

        return !cargo.getFechaInicio().isAfter(cargo.getFechaFin());
    }
}
