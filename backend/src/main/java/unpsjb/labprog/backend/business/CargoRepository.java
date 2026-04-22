package unpsjb.labprog.backend.business;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import unpsjb.labprog.backend.model.Cargo;

@Repository
public interface CargoRepository extends CrudRepository<Cargo, Long>, PagingAndSortingRepository<Cargo, Long> {

    @Query("SELECT c FROM Cargo c WHERE UPPER(c.nombre) LIKE ?1")
    List<Cargo> search(String term);
}
