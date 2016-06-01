package plumbeer.app.development.repository;

import plumbeer.app.development.domain.Trabajo;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Trabajo entity.
 */
public interface TrabajoRepository extends JpaRepository<Trabajo,Long> {

    @Query("select trabajo from Trabajo trabajo where trabajo.tecnico.login = ?#{principal.username}")
    List<Trabajo> findByTecnicoIsCurrentUser();

}
