package unpsjb.labprog.backend.presenter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unpsjb.labprog.backend.Response;
import unpsjb.labprog.backend.business.LicenciaService;
import unpsjb.labprog.backend.model.Licencia;

@RestController
@RequestMapping("licencias")
public class LicenciaPresenter {

    @Autowired
    private LicenciaService service;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Object> findAll() {
        return Response.ok(service.findAll());
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Object> create(@RequestBody Licencia aLicencia) {
        if (aLicencia.getId() != null && aLicencia.getId() != 0) {
            return Response.notFound("Error: No se puede crear una licencia con ID manual.");
        }
        return Response.ok(service.save(aLicencia));
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Object> update(@RequestBody Licencia aLicencia) {
        if (aLicencia.getId() == null || aLicencia.getId() <= 0) {
            return Response.notFound("Error: ID inválido para actualizar.");
        }
        return Response.ok(service.save(aLicencia));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return Response.ok("Licencia " + id + " eliminada.");
    }

    @RequestMapping(value = "/id/{id}", method = RequestMethod.GET)
    public ResponseEntity<Object> findById(@PathVariable("id") Long id) {
        Licencia l = service.findById(id);
        return (l != null) ? Response.ok(l) : Response.notFound("Licencia no encontrada.");
    }

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public ResponseEntity<Object> findByPage(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Response.ok(service.findByPage(page, size));
    }
}
