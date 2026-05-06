package unpsjb.labprog.backend.business;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import unpsjb.labprog.backend.model.Cargo;
import unpsjb.labprog.backend.model.Designacion;

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
        return repository.findAll(PageRequest.of(page, size));
    }

    public List<Designacion> search(String term) {
        return repository.search("%" + term.toUpperCase() + "%");
    }

    public long countByCargo(Cargo cargo) {
        return repository.countByCargo(cargo);
    }
}
