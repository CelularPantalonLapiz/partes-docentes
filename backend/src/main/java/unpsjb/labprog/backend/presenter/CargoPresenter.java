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
import unpsjb.labprog.backend.business.CargoService;
import unpsjb.labprog.backend.model.Cargo;

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
        return Response.ok(service.save(aCargo));
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Object> update(@RequestBody Cargo aCargo) {
        if (aCargo.getId() == null || aCargo.getId() <= 0) {
            return Response.notFound("Error: Debe ingresar un ID válido para actualizar el cargo.");
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
}
