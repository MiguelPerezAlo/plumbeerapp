package plumbeer.app.development.web.rest;

import com.codahale.metrics.annotation.Timed;
import plumbeer.app.development.domain.Mensaje;
import plumbeer.app.development.repository.MensajeRepository;
import plumbeer.app.development.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Mensaje.
 */
@RestController
@RequestMapping("/api")
public class MensajeResource {

    private final Logger log = LoggerFactory.getLogger(MensajeResource.class);
        
    @Inject
    private MensajeRepository mensajeRepository;
    
    /**
     * POST  /mensajes -> Create a new mensaje.
     */
    @RequestMapping(value = "/mensajes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Mensaje> createMensaje(@RequestBody Mensaje mensaje) throws URISyntaxException {
        log.debug("REST request to save Mensaje : {}", mensaje);
        if (mensaje.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("mensaje", "idexists", "A new mensaje cannot already have an ID")).body(null);
        }
        Mensaje result = mensajeRepository.save(mensaje);
        return ResponseEntity.created(new URI("/api/mensajes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("mensaje", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /mensajes -> Updates an existing mensaje.
     */
    @RequestMapping(value = "/mensajes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Mensaje> updateMensaje(@RequestBody Mensaje mensaje) throws URISyntaxException {
        log.debug("REST request to update Mensaje : {}", mensaje);
        if (mensaje.getId() == null) {
            return createMensaje(mensaje);
        }
        Mensaje result = mensajeRepository.save(mensaje);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("mensaje", mensaje.getId().toString()))
            .body(result);
    }

    /**
     * GET  /mensajes -> get all the mensajes.
     */
    @RequestMapping(value = "/mensajes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Mensaje> getAllMensajes() {
        log.debug("REST request to get all Mensajes");
        return mensajeRepository.findAll();
            }

    /**
     * GET  /mensajes/:id -> get the "id" mensaje.
     */
    @RequestMapping(value = "/mensajes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Mensaje> getMensaje(@PathVariable Long id) {
        log.debug("REST request to get Mensaje : {}", id);
        Mensaje mensaje = mensajeRepository.findOne(id);
        return Optional.ofNullable(mensaje)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /mensajes/:id -> delete the "id" mensaje.
     */
    @RequestMapping(value = "/mensajes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteMensaje(@PathVariable Long id) {
        log.debug("REST request to delete Mensaje : {}", id);
        mensajeRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("mensaje", id.toString())).build();
    }
}
