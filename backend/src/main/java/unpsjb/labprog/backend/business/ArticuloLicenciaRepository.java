package unpsjb.labprog.backend.business;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import unpsjb.labprog.backend.model.ArticuloLicencia;

@Repository
public interface ArticuloLicenciaRepository
        extends CrudRepository<ArticuloLicencia, String>, PagingAndSortingRepository<ArticuloLicencia, String> {

    @Query("SELECT a FROM ArticuloLicencia a WHERE UPPER(a.articulo) LIKE ?1 OR UPPER(a.descripcion) LIKE ?1")
    List<ArticuloLicencia> search(String term);
}
