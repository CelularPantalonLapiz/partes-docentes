package unpsjb.labprog.backend.business;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import unpsjb.labprog.backend.model.Cargo;
import unpsjb.labprog.backend.model.Designacion;
import unpsjb.labprog.backend.model.Persona;
import unpsjb.labprog.backend.model.SituacionRevista;
import unpsjb.labprog.backend.model.TipoDesignacion;

@Service
public class DesignacionService {

    @Autowired
    private DesignacionRepository repository;

    public List<Designacion> findAll() {
        List<Designacion> result = new ArrayList<>();
        repository.findAll().forEach(result::add);
        return result;
    }

    @Transactional
    public Designacion save(Designacion designacion) {
        Cargo cargo = designacion.getCargo();
        SituacionRevista situacion = SituacionRevista.TITULAR;
        if (cargo != null) {
            situacion = buscarSolapamientoDesignacionesConFiltro(cargo, designacion);
        }
        if (situacion == SituacionRevista.ERROR && designacion.getId() != null) {
            Designacion designacionError = new Designacion();
            designacionError.setPersona(designacion.getPersona());
            designacionError.setCargo(designacion.getCargo());
            designacionError.setFechaInicio(designacion.getFechaInicio());
            designacionError.setFechaFin(designacion.getFechaFin());
            designacionError.setSituacionRevista(SituacionRevista.ERROR);
            repository.save(designacionError);
            designacion.setSituacionRevista(SituacionRevista.ERROR);
            return designacion;
        }
        designacion.setSituacionRevista(situacion);
        return repository.save(designacion);
    }

    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Designacion findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Page<Designacion> findByPage(int page, int size) {
        return repository.findBySituacionRevistaNot(SituacionRevista.ERROR, PageRequest.of(page, size));
    }

    public List<Designacion> search(String term) {
        return repository.search("%" + term.toUpperCase() + "%");
    }

    public long countByCargo(Cargo cargo) {
        return repository.countByCargo(cargo);
    }

    public SituacionRevista buscarSolapamientoDesignaciones(Cargo c, Designacion designacion) {
        LocalDate fin = designacion.getFechaFin() != null ? designacion.getFechaFin() : LocalDate.of(9999, 12, 31);
        List<Designacion> solapados = repository.buscarSolapamientoDesignaciones(c.getId(),
                designacion.getFechaInicio(), fin);

        if (solapados.size() == 0)
            return SituacionRevista.TITULAR;
        if (TipoDesignacion.ESPACIO_CURRICULAR.equals(c.getTipo()))
            return SituacionRevista.ERROR;
        if (solapados.size() == 1)
            return SituacionRevista.SUPLENTE;
        return SituacionRevista.ERROR;
    }

    public Persona quienOcupaDesignacion(Cargo c, Designacion designacion) {
        LocalDate fin = designacion.getFechaFin() != null ? designacion.getFechaFin() : LocalDate.of(9999, 12, 31);
        return repository.buscarPersonaTitular(c.getId(), designacion.getFechaInicio(), fin);
    }

    public List<Designacion> getDesignacionesSolapa(Cargo c, Designacion designacion) {
        LocalDate fin = designacion.getFechaFin() != null ? designacion.getFechaFin() : LocalDate.of(9999, 12, 31);
        return repository.buscarSolapamientoDesignaciones(c.getId(), designacion.getFechaInicio(), fin);
    }

    public List<Designacion> getErroresRecientes() {
        return repository.buscarErroresRecientes();
    }

    public List<Designacion> buscarGeneral(String term) {
        return repository.buscarPorTermino("%" + term.toUpperCase() + "%");
    }

    public List<Designacion> getAceptadas() {
        return repository.buscarAceptadas();
    }

    private SituacionRevista buscarSolapamientoDesignacionesConFiltro(Cargo c, Designacion designacion) {
        LocalDate fin = designacion.getFechaFin() != null ? designacion.getFechaFin() : LocalDate.of(9999, 12, 31);

        List<Designacion> solapados = repository.buscarSolapamientoDesignaciones(c.getId(),
                designacion.getFechaInicio(), fin);

        // Filtro fundamental: Si estamos editando un registro, evitamos que se choque
        // consigo mismo
        if (designacion.getId() != null) {
            solapados.removeIf(d -> d.getId().equals(designacion.getId()));
        }

        if (solapados.size() == 0)
            return SituacionRevista.TITULAR;
        if (TipoDesignacion.ESPACIO_CURRICULAR.equals(c.getTipo())) {
            return SituacionRevista.ERROR;
        }
        if (solapados.size() == 1)
            return SituacionRevista.SUPLENTE;

        return SituacionRevista.ERROR;
    }
}
