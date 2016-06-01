package plumbeer.app.development.repository;

import plumbeer.app.development.domain.Mensaje;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Mensaje entity.
 */
public interface MensajeRepository extends JpaRepository<Mensaje,Long> {

    @Query("select mensaje from Mensaje mensaje where mensaje.receptor.login = ?#{principal.username}")
    List<Mensaje> findByReceptorIsCurrentUser();

    @Query("select mensaje from Mensaje mensaje where mensaje.emisor.login = ?#{principal.username}")
    List<Mensaje> findByEmisorIsCurrentUser();

}
