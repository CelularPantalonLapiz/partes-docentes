package unpsjb.labprog.backend.business;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import unpsjb.labprog.backend.model.Division;

@Service
public class DivisionService {

    @Autowired
    private DivisionRepository divisionRepository;

    public List<Division> findAll() {
        List<Division> result = new ArrayList<>();
        divisionRepository.findAll().forEach(result::add);
        return result;
    }

    @Transactional
    public Division save(Division division) {
        return divisionRepository.save(division);
    }

    @Transactional
    public void delete(Long id) {
        divisionRepository.deleteById(id);
    }

    public Division findById(Long id) {
        return divisionRepository.findById(id).orElse(null);
    }

    public Page<Division> findByPage(int page, int size) {
        return divisionRepository.findAll(PageRequest.of(page, size));
    }

    public List<Division> search(String term) {
        return divisionRepository.search("%" + term + "%");
    }
}
