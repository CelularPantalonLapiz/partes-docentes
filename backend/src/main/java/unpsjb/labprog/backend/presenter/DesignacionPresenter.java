package unpsjb.labprog.backend.presenter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unpsjb.labprog.backend.Response;
import unpsjb.labprog.backend.business.DesignacionService;
import unpsjb.labprog.backend.model.Designacion;

@RestController
@RequestMapping("designaciones")
public class DesignacionPresenter {

    @Autowired
    private DesignacionService service;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Object> findAll() {
        return Response.ok(service.findAll());
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Object> create(@RequestBody Designacion aDesignacion) {
        if (aDesignacion.getId() != null && aDesignacion.getId() != 0) {
            return Response.notFound("Error: No se puede crear una designación con un ID definido.");
        }
        return Response.ok(service.save(aDesignacion));
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
