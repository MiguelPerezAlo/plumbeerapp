package plumbeer.app.development.web.rest;

import plumbeer.app.development.Application;
import plumbeer.app.development.domain.Trabajo;
import plumbeer.app.development.repository.TrabajoRepository;

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
import org.springframework.util.Base64Utils;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the TrabajoResource REST controller.
 *
 * @see TrabajoResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class TrabajoResourceIntTest {

    private static final String DEFAULT_ASUNTO = "AAAAA";
    private static final String UPDATED_ASUNTO = "BBBBB";
    
    private static final String DEFAULT_DESCRIPCION = "AAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBB";

    private static final LocalDate DEFAULT_FECHA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_VISIBLE = false;
    private static final Boolean UPDATED_VISIBLE = true;

    private static final Boolean DEFAULT_ABIERTO = false;
    private static final Boolean UPDATED_ABIERTO = true;

    @Inject
    private TrabajoRepository trabajoRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTrabajoMockMvc;

    private Trabajo trabajo;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TrabajoResource trabajoResource = new TrabajoResource();
        ReflectionTestUtils.setField(trabajoResource, "trabajoRepository", trabajoRepository);
        this.restTrabajoMockMvc = MockMvcBuilders.standaloneSetup(trabajoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        trabajo = new Trabajo();
        trabajo.setAsunto(DEFAULT_ASUNTO);
        trabajo.setDescripcion(DEFAULT_DESCRIPCION);
        trabajo.setFecha(DEFAULT_FECHA);
        trabajo.setVisible(DEFAULT_VISIBLE);
        trabajo.setAbierto(DEFAULT_ABIERTO);
    }

    @Test
    @Transactional
    public void createTrabajo() throws Exception {
        int databaseSizeBeforeCreate = trabajoRepository.findAll().size();

        // Create the Trabajo

        restTrabajoMockMvc.perform(post("/api/trabajos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(trabajo)))
                .andExpect(status().isCreated());

        // Validate the Trabajo in the database
        List<Trabajo> trabajos = trabajoRepository.findAll();
        assertThat(trabajos).hasSize(databaseSizeBeforeCreate + 1);
        Trabajo testTrabajo = trabajos.get(trabajos.size() - 1);
        assertThat(testTrabajo.getAsunto()).isEqualTo(DEFAULT_ASUNTO);
        assertThat(testTrabajo.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testTrabajo.getFecha()).isEqualTo(DEFAULT_FECHA);
        assertThat(testTrabajo.getVisible()).isEqualTo(DEFAULT_VISIBLE);
        assertThat(testTrabajo.getAbierto()).isEqualTo(DEFAULT_ABIERTO);
    }

    @Test
    @Transactional
    public void getAllTrabajos() throws Exception {
        // Initialize the database
        trabajoRepository.saveAndFlush(trabajo);

        // Get all the trabajos
        restTrabajoMockMvc.perform(get("/api/trabajos?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(trabajo.getId().intValue())))
                .andExpect(jsonPath("$.[*].asunto").value(hasItem(DEFAULT_ASUNTO.toString())))
                .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION.toString())))
                .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
                .andExpect(jsonPath("$.[*].visible").value(hasItem(DEFAULT_VISIBLE.booleanValue())))
                .andExpect(jsonPath("$.[*].abierto").value(hasItem(DEFAULT_ABIERTO.booleanValue())));
    }

    @Test
    @Transactional
    public void getTrabajo() throws Exception {
        // Initialize the database
        trabajoRepository.saveAndFlush(trabajo);

        // Get the trabajo
        restTrabajoMockMvc.perform(get("/api/trabajos/{id}", trabajo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(trabajo.getId().intValue()))
            .andExpect(jsonPath("$.asunto").value(DEFAULT_ASUNTO.toString()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION.toString()))
            .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA.toString()))
            .andExpect(jsonPath("$.visible").value(DEFAULT_VISIBLE.booleanValue()))
            .andExpect(jsonPath("$.abierto").value(DEFAULT_ABIERTO.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingTrabajo() throws Exception {
        // Get the trabajo
        restTrabajoMockMvc.perform(get("/api/trabajos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTrabajo() throws Exception {
        // Initialize the database
        trabajoRepository.saveAndFlush(trabajo);

		int databaseSizeBeforeUpdate = trabajoRepository.findAll().size();

        // Update the trabajo
        trabajo.setAsunto(UPDATED_ASUNTO);
        trabajo.setDescripcion(UPDATED_DESCRIPCION);
        trabajo.setFecha(UPDATED_FECHA);
        trabajo.setVisible(UPDATED_VISIBLE);
        trabajo.setAbierto(UPDATED_ABIERTO);

        restTrabajoMockMvc.perform(put("/api/trabajos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(trabajo)))
                .andExpect(status().isOk());

        // Validate the Trabajo in the database
        List<Trabajo> trabajos = trabajoRepository.findAll();
        assertThat(trabajos).hasSize(databaseSizeBeforeUpdate);
        Trabajo testTrabajo = trabajos.get(trabajos.size() - 1);
        assertThat(testTrabajo.getAsunto()).isEqualTo(UPDATED_ASUNTO);
        assertThat(testTrabajo.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testTrabajo.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testTrabajo.getVisible()).isEqualTo(UPDATED_VISIBLE);
        assertThat(testTrabajo.getAbierto()).isEqualTo(UPDATED_ABIERTO);
    }

    @Test
    @Transactional
    public void deleteTrabajo() throws Exception {
        // Initialize the database
        trabajoRepository.saveAndFlush(trabajo);

		int databaseSizeBeforeDelete = trabajoRepository.findAll().size();

        // Get the trabajo
        restTrabajoMockMvc.perform(delete("/api/trabajos/{id}", trabajo.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Trabajo> trabajos = trabajoRepository.findAll();
        assertThat(trabajos).hasSize(databaseSizeBeforeDelete - 1);
    }
}
