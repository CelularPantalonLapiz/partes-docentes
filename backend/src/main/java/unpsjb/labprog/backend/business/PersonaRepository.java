package unpsjb.labprog.backend.business;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import unpsjb.labprog.backend.model.Persona;

@Repository
public interface PersonaRepository extends CrudRepository<Persona, Long>, PagingAndSortingRepository<Persona, Long> {
    @Query("SELECT p FROM Persona p WHERE p.dni = :dni")
    public Persona findByDni(Long dni);

}
