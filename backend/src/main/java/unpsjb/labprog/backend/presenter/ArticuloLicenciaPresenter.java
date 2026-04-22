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
import unpsjb.labprog.backend.business.ArticuloLicenciaService;
import unpsjb.labprog.backend.model.ArticuloLicencia;

@RestController
@RequestMapping("artlic")
public class ArticuloLicenciaPresenter {

    @Autowired
    private ArticuloLicenciaService service;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Object> findAll() {
        return Response.ok(service.findAll());
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Object> create(@RequestBody ArticuloLicencia aArticulo) {
        if (aArticulo.getArticulo() == null || aArticulo.getArticulo().isEmpty()) {
            return Response.notFound("Error: Debe ingresar un código de artículo.");
        }
        return Response.ok(service.save(aArticulo));
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Object> update(@RequestBody ArticuloLicencia aArticulo) {
        if (aArticulo.getArticulo() == null || aArticulo.getArticulo().isEmpty()) {
            return Response.notFound("Error: Artículo inválido para actualizar.");
        }
        return Response.ok(service.save(aArticulo));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> delete(@PathVariable("id") String id) {
        service.delete(id);
        return Response.ok("Artículo " + id + " eliminado.");
    }

    @RequestMapping(value = "/id/{id}", method = RequestMethod.GET)
    public ResponseEntity<Object> findById(@PathVariable("id") String id) {
        ArticuloLicencia a = service.findById(id);
        return (a != null) ? Response.ok(a) : Response.notFound("No se encontró el artículo: " + id);
    }

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public ResponseEntity<Object> findByPage(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Response.ok(service.findByPage(page, size));
    }
}
