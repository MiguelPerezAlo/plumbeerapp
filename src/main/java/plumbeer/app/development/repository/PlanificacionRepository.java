package plumbeer.app.development.repository;

import plumbeer.app.development.domain.Planificacion;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Planificacion entity.
 */
public interface PlanificacionRepository extends JpaRepository<Planificacion,Long> {

    @Query("select planificacion from Planificacion planificacion where planificacion.user.login = ?#{principal.username}")
    List<Planificacion> findByUserIsCurrentUser();

}
