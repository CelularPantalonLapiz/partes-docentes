package unpsjb.labprog.backend.presenter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import unpsjb.labprog.backend.Response;
import unpsjb.labprog.backend.business.DivisionService;
import unpsjb.labprog.backend.model.Division;
import unpsjb.labprog.backend.model.Turno;

@RestController
@RequestMapping("divisiones")
public class DivisionPresenter {

    @Autowired
    private DivisionService service;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Object> findAll() {
        return Response.ok(service.findAll());
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Object> create(@RequestBody Division aDivision) {
        if (aDivision.getId() != null && aDivision.getId() != 0) {
            return Response.notFound("Para crear una división el ID debe ser nulo o cero.");
        }
        service.save(aDivision);
        String turnoFormateado = aDivision.getTurno().toString().toLowerCase();
        turnoFormateado = turnoFormateado.substring(0, 1).toUpperCase() + turnoFormateado.substring(1);
        if (turnoFormateado.equals("Manana"))
            turnoFormateado = "Mañana";

        String msg = String.format("División %dº %dº turno %s ingresada correctamente",
                aDivision.getAnio(),
                aDivision.getNumDivision(),
                turnoFormateado);

        return ResponseEntity.ok(new Response(200, msg, aDivision));
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Object> update(@RequestBody Division aDivision) {
        if (aDivision.getId() == null || aDivision.getId() <= 0) {
            return Response.notFound("ID inválido para actualizar la división.");
        }
        service.save(aDivision);
        String turnoFormateado = aDivision.getTurno().toString().toLowerCase();
        turnoFormateado = turnoFormateado.substring(0, 1).toUpperCase() + turnoFormateado.substring(1);
        if (turnoFormateado.equals("Manana"))
            turnoFormateado = "Mañana";

        String msg = String.format("División %dº %dº turno %s actualizada correctamente",
                aDivision.getAnio(),
                aDivision.getNumDivision(),
                turnoFormateado);

        return ResponseEntity.ok(new Response(200, msg, aDivision));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> delete(@PathVariable("id") Long id) {
        service.delete(id);
        String msg = String.format("División eliminada correctamente.");
        return ResponseEntity.ok(new Response(200, msg, null));
    }

    @RequestMapping(value = "/id/{id}", method = RequestMethod.GET)
    public ResponseEntity<Object> findById(@PathVariable("id") Long id) {
        Division d = service.findById(id);
        return (d != null) ? Response.ok(d) : Response.notFound("No se encontró la división.");
    }

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public ResponseEntity<Object> findByPage(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Response.ok(service.findByPage(page, size));
    }

}