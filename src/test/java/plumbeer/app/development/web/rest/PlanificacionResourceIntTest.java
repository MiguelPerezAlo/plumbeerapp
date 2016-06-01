package plumbeer.app.development.web.rest;

import plumbeer.app.development.Application;
import plumbeer.app.development.domain.Planificacion;
import plumbeer.app.development.repository.PlanificacionRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the PlanificacionResource REST controller.
 *
 * @see PlanificacionResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PlanificacionResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));


    private static final ZonedDateTime DEFAULT_FECHA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_FECHA = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_FECHA_STR = dateTimeFormatter.format(DEFAULT_FECHA);

    @Inject
    private PlanificacionRepository planificacionRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPlanificacionMockMvc;

    private Planificacion planificacion;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PlanificacionResource planificacionResource = new PlanificacionResource();
        ReflectionTestUtils.setField(planificacionResource, "planificacionRepository", planificacionRepository);
        this.restPlanificacionMockMvc = MockMvcBuilders.standaloneSetup(planificacionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        planificacion = new Planificacion();
        planificacion.setFecha(DEFAULT_FECHA);
    }

    @Test
    @Transactional
    public void createPlanificacion() throws Exception {
        int databaseSizeBeforeCreate = planificacionRepository.findAll().size();

        // Create the Planificacion

        restPlanificacionMockMvc.perform(post("/api/planificacions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(planificacion)))
                .andExpect(status().isCreated());

        // Validate the Planificacion in the database
        List<Planificacion> planificacions = planificacionRepository.findAll();
        assertThat(planificacions).hasSize(databaseSizeBeforeCreate + 1);
        Planificacion testPlanificacion = planificacions.get(planificacions.size() - 1);
        assertThat(testPlanificacion.getFecha()).isEqualTo(DEFAULT_FECHA);
    }

    @Test
    @Transactional
    public void getAllPlanificacions() throws Exception {
        // Initialize the database
        planificacionRepository.saveAndFlush(planificacion);

        // Get all the planificacions
        restPlanificacionMockMvc.perform(get("/api/planificacions?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(planificacion.getId().intValue())))
                .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA_STR)));
    }

    @Test
    @Transactional
    public void getPlanificacion() throws Exception {
        // Initialize the database
        planificacionRepository.saveAndFlush(planificacion);

        // Get the planificacion
        restPlanificacionMockMvc.perform(get("/api/planificacions/{id}", planificacion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(planificacion.getId().intValue()))
            .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA_STR));
    }

    @Test
    @Transactional
    public void getNonExistingPlanificacion() throws Exception {
        // Get the planificacion
        restPlanificacionMockMvc.perform(get("/api/planificacions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePlanificacion() throws Exception {
        // Initialize the database
        planificacionRepository.saveAndFlush(planificacion);

		int databaseSizeBeforeUpdate = planificacionRepository.findAll().size();

        // Update the planificacion
        planificacion.setFecha(UPDATED_FECHA);

        restPlanificacionMockMvc.perform(put("/api/planificacions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(planificacion)))
                .andExpect(status().isOk());

        // Validate the Planificacion in the database
        List<Planificacion> planificacions = planificacionRepository.findAll();
        assertThat(planificacions).hasSize(databaseSizeBeforeUpdate);
        Planificacion testPlanificacion = planificacions.get(planificacions.size() - 1);
        assertThat(testPlanificacion.getFecha()).isEqualTo(UPDATED_FECHA);
    }

    @Test
    @Transactional
    public void deletePlanificacion() throws Exception {
        // Initialize the database
        planificacionRepository.saveAndFlush(planificacion);

		int databaseSizeBeforeDelete = planificacionRepository.findAll().size();

        // Get the planificacion
        restPlanificacionMockMvc.perform(delete("/api/planificacions/{id}", planificacion.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Planificacion> planificacions = planificacionRepository.findAll();
        assertThat(planificacions).hasSize(databaseSizeBeforeDelete - 1);
    }
}
