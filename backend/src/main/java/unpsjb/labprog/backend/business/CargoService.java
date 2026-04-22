package unpsjb.labprog.backend.business;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import unpsjb.labprog.backend.model.Cargo;
import unpsjb.labprog.backend.model.Division;

@Service
public class CargoService {
    @Autowired
    private CargoRepository repository;
    @Autowired
    private DivisionRepository divisionRepository;

    public List<Cargo> findAll() {
        List<Cargo> result = new ArrayList<>();
        repository.findAll().forEach(result::add);
        return result;
    }

    @Transactional
    public Cargo save(Cargo cargo) {
        if (cargo.getDivision() != null && cargo.getDivision().getId() != null) {
            Division division = divisionRepository.findById(cargo.getDivision().getId())
                    .orElseThrow(() -> new RuntimeException("División no encontrada"));
            cargo.setDivision(division);
        }
        return repository.save(cargo);
    }

    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Cargo findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Page<Cargo> findByPage(int page, int size) {
        return repository.findAll(PageRequest.of(page, size));
    }

    public List<Cargo> search(String term) {
        return repository.search("%" + term.toUpperCase() + "%");
    }
}
