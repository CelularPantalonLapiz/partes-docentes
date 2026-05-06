package unpsjb.labprog.backend.business;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import unpsjb.labprog.backend.model.Cargo;
import unpsjb.labprog.backend.model.Designacion;

@Repository
public interface DesignacionRepository
        extends CrudRepository<Designacion, Long>, PagingAndSortingRepository<Designacion, Long> {

    @Query("SELECT d FROM Designacion d WHERE UPPER(d.situacionRevista) LIKE ?1")
    List<Designacion> search(String term);

    @Query("SELECT d FROM Designacion d WHERE d.persona.dni = ?1")
    List<Designacion> findByPersona(Long dni);

    @Query("SELECT COUNT(d) FROM Designacion d WHERE d.cargo = ?1")
    long countByCargo(Cargo cargo);
}