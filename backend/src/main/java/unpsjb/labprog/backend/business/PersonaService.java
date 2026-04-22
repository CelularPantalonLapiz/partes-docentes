package unpsjb.labprog.backend.business;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import unpsjb.labprog.backend.model.Persona;

@Service
public class PersonaService {

    @Autowired
    private PersonaRepository personaRepo;

    public Iterable<Persona> findAll() {
        List<Persona> result = new ArrayList<>();
        personaRepo.findAll().forEach(e -> result.add(e));
        return result;
    }

    public Persona findByDni(Long dni) {
        return personaRepo.findByDni(dni);
    }

    @Transactional
    public Persona save(Persona persona) {
        return personaRepo.save(persona);
    }

    @Transactional
    public void delete(Long id) {
        personaRepo.deleteById(id);
    }

    public Page<Persona> findByPage(int page, int size) {
        return personaRepo.findAll(PageRequest.of(page, size));
    }

}
