package unpsjb.labprog.backend.business;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import unpsjb.labprog.backend.model.Cargo;
import unpsjb.labprog.backend.model.Designacion;
import unpsjb.labprog.backend.model.Persona;

@Repository
public interface DesignacionRepository
                extends CrudRepository<Designacion, Long>, PagingAndSortingRepository<Designacion, Long> {

        @Query("SELECT d FROM Designacion d WHERE UPPER(d.situacionRevista) LIKE ?1")
        List<Designacion> search(String term);

        @Query("SELECT d FROM Designacion d WHERE d.persona.dni = ?1")
        List<Designacion> findByPersona(Long dni);

        @Query("SELECT COUNT(d) FROM Designacion d WHERE d.cargo = ?1")
        long countByCargo(Cargo cargo);

        @Query(value = "SELECT * FROM designacion d WHERE d.cargo_id = :cargoId " +
                        "AND d.situacion_revista != 'ERROR' " +
                        "AND CAST(d.fecha_inicio AS date) < CAST(:fechaFin AS date) " +
                        "AND (d.fecha_fin IS NULL OR CAST(d.fecha_fin AS date) > CAST(:fechaInicio AS date))", nativeQuery = true)
        List<Designacion> buscarSolapamientoDesignaciones(@Param("cargoId") Long cargoId,
                        @Param("fechaInicio") LocalDate fechaInicio,
                        @Param("fechaFin") LocalDate fechaFin);

        @Query("SELECT d.persona FROM Designacion d WHERE d.cargo.id = :cargoId " +
                        "AND d.situacionRevista = unpsjb.labprog.backend.model.SituacionRevista.TITULAR " +
                        "AND (d.fechaFin IS NULL OR d.fechaFin >= :inicio) " +
                        "AND (d.fechaInicio <= :fin)")
        Persona buscarPersonaTitular(@Param("cargoId") Long cargoId,
                        @Param("inicio") LocalDate inicio,
                        @Param("fin") LocalDate fin);

}