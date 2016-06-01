package plumbeer.app.development.web.rest;

import com.codahale.metrics.annotation.Timed;
import plumbeer.app.development.domain.Planificacion;
import plumbeer.app.development.repository.PlanificacionRepository;
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
 * REST controller for managing Planificacion.
 */
@RestController
@RequestMapping("/api")
public class PlanificacionResource {

    private final Logger log = LoggerFactory.getLogger(PlanificacionResource.class);
        
    @Inject
    private PlanificacionRepository planificacionRepository;
    
    /**
     * POST  /planificacions -> Create a new planificacion.
     */
    @RequestMapping(value = "/planificacions",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Planificacion> createPlanificacion(@RequestBody Planificacion planificacion) throws URISyntaxException {
        log.debug("REST request to save Planificacion : {}", planificacion);
        if (planificacion.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("planificacion", "idexists", "A new planificacion cannot already have an ID")).body(null);
        }
        Planificacion result = planificacionRepository.save(planificacion);
        return ResponseEntity.created(new URI("/api/planificacions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("planificacion", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /planificacions -> Updates an existing planificacion.
     */
    @RequestMapping(value = "/planificacions",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Planificacion> updatePlanificacion(@RequestBody Planificacion planificacion) throws URISyntaxException {
        log.debug("REST request to update Planificacion : {}", planificacion);
        if (planificacion.getId() == null) {
            return createPlanificacion(planificacion);
        }
        Planificacion result = planificacionRepository.save(planificacion);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("planificacion", planificacion.getId().toString()))
            .body(result);
    }

    /**
     * GET  /planificacions -> get all the planificacions.
     */
    @RequestMapping(value = "/planificacions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Planificacion> getAllPlanificacions() {
        log.debug("REST request to get all Planificacions");
        return planificacionRepository.findAll();
            }

    /**
     * GET  /planificacions/:id -> get the "id" planificacion.
     */
    @RequestMapping(value = "/planificacions/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Planificacion> getPlanificacion(@PathVariable Long id) {
        log.debug("REST request to get Planificacion : {}", id);
        Planificacion planificacion = planificacionRepository.findOne(id);
        return Optional.ofNullable(planificacion)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /planificacions/:id -> delete the "id" planificacion.
     */
    @RequestMapping(value = "/planificacions/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePlanificacion(@PathVariable Long id) {
        log.debug("REST request to delete Planificacion : {}", id);
        planificacionRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("planificacion", id.toString())).build();
    }
}
