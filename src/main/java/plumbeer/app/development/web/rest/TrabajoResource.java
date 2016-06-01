package plumbeer.app.development.web.rest;

import com.codahale.metrics.annotation.Timed;
import plumbeer.app.development.domain.Trabajo;
import plumbeer.app.development.repository.TrabajoRepository;
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
 * REST controller for managing Trabajo.
 */
@RestController
@RequestMapping("/api")
public class TrabajoResource {

    private final Logger log = LoggerFactory.getLogger(TrabajoResource.class);
        
    @Inject
    private TrabajoRepository trabajoRepository;
    
    /**
     * POST  /trabajos -> Create a new trabajo.
     */
    @RequestMapping(value = "/trabajos",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Trabajo> createTrabajo(@RequestBody Trabajo trabajo) throws URISyntaxException {
        log.debug("REST request to save Trabajo : {}", trabajo);
        if (trabajo.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("trabajo", "idexists", "A new trabajo cannot already have an ID")).body(null);
        }
        Trabajo result = trabajoRepository.save(trabajo);
        return ResponseEntity.created(new URI("/api/trabajos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("trabajo", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /trabajos -> Updates an existing trabajo.
     */
    @RequestMapping(value = "/trabajos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Trabajo> updateTrabajo(@RequestBody Trabajo trabajo) throws URISyntaxException {
        log.debug("REST request to update Trabajo : {}", trabajo);
        if (trabajo.getId() == null) {
            return createTrabajo(trabajo);
        }
        Trabajo result = trabajoRepository.save(trabajo);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("trabajo", trabajo.getId().toString()))
            .body(result);
    }

    /**
     * GET  /trabajos -> get all the trabajos.
     */
    @RequestMapping(value = "/trabajos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Trabajo> getAllTrabajos() {
        log.debug("REST request to get all Trabajos");
        return trabajoRepository.findAll();
            }

    /**
     * GET  /trabajos/:id -> get the "id" trabajo.
     */
    @RequestMapping(value = "/trabajos/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Trabajo> getTrabajo(@PathVariable Long id) {
        log.debug("REST request to get Trabajo : {}", id);
        Trabajo trabajo = trabajoRepository.findOne(id);
        return Optional.ofNullable(trabajo)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /trabajos/:id -> delete the "id" trabajo.
     */
    @RequestMapping(value = "/trabajos/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTrabajo(@PathVariable Long id) {
        log.debug("REST request to delete Trabajo : {}", id);
        trabajoRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("trabajo", id.toString())).build();
    }
}
