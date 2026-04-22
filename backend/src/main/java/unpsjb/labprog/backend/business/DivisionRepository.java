package unpsjb.labprog.backend.business;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import unpsjb.labprog.backend.model.Division;

@Repository
public interface DivisionRepository extends CrudRepository<Division, Long>, PagingAndSortingRepository<Division, Long> {

    @Query("SELECT d FROM Division d WHERE CAST(d.anio AS string) LIKE ?1 OR CAST(d.numDivision AS string) LIKE ?1")
    List<Division> search(String term);
}
