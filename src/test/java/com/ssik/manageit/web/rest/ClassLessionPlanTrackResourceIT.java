package com.ssik.manageit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ssik.manageit.IntegrationTest;
import com.ssik.manageit.domain.ClassLessionPlan;
import com.ssik.manageit.domain.ClassLessionPlanTrack;
import com.ssik.manageit.domain.enumeration.TaskStatus;
import com.ssik.manageit.repository.ClassLessionPlanTrackRepository;
import com.ssik.manageit.service.criteria.ClassLessionPlanTrackCriteria;
import com.ssik.manageit.service.dto.ClassLessionPlanTrackDTO;
import com.ssik.manageit.service.mapper.ClassLessionPlanTrackMapper;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ClassLessionPlanTrackResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ClassLessionPlanTrackResourceIT {

    private static final TaskStatus DEFAULT_WORK_STATUS = TaskStatus.NotStarted;
    private static final TaskStatus UPDATED_WORK_STATUS = TaskStatus.InProgress;

    private static final String DEFAULT_REMARKS = "AAAAAAAAAA";
    private static final String UPDATED_REMARKS = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATE_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CREATE_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_LAST_MODIFIED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_MODIFIED = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_LAST_MODIFIED = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_CANCEL_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CANCEL_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CANCEL_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/class-lession-plan-tracks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ClassLessionPlanTrackRepository classLessionPlanTrackRepository;

    @Autowired
    private ClassLessionPlanTrackMapper classLessionPlanTrackMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restClassLessionPlanTrackMockMvc;

    private ClassLessionPlanTrack classLessionPlanTrack;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClassLessionPlanTrack createEntity(EntityManager em) {
        ClassLessionPlanTrack classLessionPlanTrack = new ClassLessionPlanTrack()
            .workStatus(DEFAULT_WORK_STATUS)
            .remarks(DEFAULT_REMARKS)
            .createDate(DEFAULT_CREATE_DATE)
            .lastModified(DEFAULT_LAST_MODIFIED)
            .cancelDate(DEFAULT_CANCEL_DATE);
        return classLessionPlanTrack;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClassLessionPlanTrack createUpdatedEntity(EntityManager em) {
        ClassLessionPlanTrack classLessionPlanTrack = new ClassLessionPlanTrack()
            .workStatus(UPDATED_WORK_STATUS)
            .remarks(UPDATED_REMARKS)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);
        return classLessionPlanTrack;
    }

    @BeforeEach
    public void initTest() {
        classLessionPlanTrack = createEntity(em);
    }

    @Test
    @Transactional
    void createClassLessionPlanTrack() throws Exception {
        int databaseSizeBeforeCreate = classLessionPlanTrackRepository.findAll().size();
        // Create the ClassLessionPlanTrack
        ClassLessionPlanTrackDTO classLessionPlanTrackDTO = classLessionPlanTrackMapper.toDto(classLessionPlanTrack);
        restClassLessionPlanTrackMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(classLessionPlanTrackDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ClassLessionPlanTrack in the database
        List<ClassLessionPlanTrack> classLessionPlanTrackList = classLessionPlanTrackRepository.findAll();
        assertThat(classLessionPlanTrackList).hasSize(databaseSizeBeforeCreate + 1);
        ClassLessionPlanTrack testClassLessionPlanTrack = classLessionPlanTrackList.get(classLessionPlanTrackList.size() - 1);
        assertThat(testClassLessionPlanTrack.getWorkStatus()).isEqualTo(DEFAULT_WORK_STATUS);
        assertThat(testClassLessionPlanTrack.getRemarks()).isEqualTo(DEFAULT_REMARKS);
        assertThat(testClassLessionPlanTrack.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testClassLessionPlanTrack.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
        assertThat(testClassLessionPlanTrack.getCancelDate()).isEqualTo(DEFAULT_CANCEL_DATE);
    }

    @Test
    @Transactional
    void createClassLessionPlanTrackWithExistingId() throws Exception {
        // Create the ClassLessionPlanTrack with an existing ID
        classLessionPlanTrack.setId(1L);
        ClassLessionPlanTrackDTO classLessionPlanTrackDTO = classLessionPlanTrackMapper.toDto(classLessionPlanTrack);

        int databaseSizeBeforeCreate = classLessionPlanTrackRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restClassLessionPlanTrackMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(classLessionPlanTrackDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClassLessionPlanTrack in the database
        List<ClassLessionPlanTrack> classLessionPlanTrackList = classLessionPlanTrackRepository.findAll();
        assertThat(classLessionPlanTrackList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkWorkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = classLessionPlanTrackRepository.findAll().size();
        // set the field null
        classLessionPlanTrack.setWorkStatus(null);

        // Create the ClassLessionPlanTrack, which fails.
        ClassLessionPlanTrackDTO classLessionPlanTrackDTO = classLessionPlanTrackMapper.toDto(classLessionPlanTrack);

        restClassLessionPlanTrackMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(classLessionPlanTrackDTO))
            )
            .andExpect(status().isBadRequest());

        List<ClassLessionPlanTrack> classLessionPlanTrackList = classLessionPlanTrackRepository.findAll();
        assertThat(classLessionPlanTrackList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllClassLessionPlanTracks() throws Exception {
        // Initialize the database
        classLessionPlanTrackRepository.saveAndFlush(classLessionPlanTrack);

        // Get all the classLessionPlanTrackList
        restClassLessionPlanTrackMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(classLessionPlanTrack.getId().intValue())))
            .andExpect(jsonPath("$.[*].workStatus").value(hasItem(DEFAULT_WORK_STATUS.toString())))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].cancelDate").value(hasItem(DEFAULT_CANCEL_DATE.toString())));
    }

    @Test
    @Transactional
    void getClassLessionPlanTrack() throws Exception {
        // Initialize the database
        classLessionPlanTrackRepository.saveAndFlush(classLessionPlanTrack);

        // Get the classLessionPlanTrack
        restClassLessionPlanTrackMockMvc
            .perform(get(ENTITY_API_URL_ID, classLessionPlanTrack.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(classLessionPlanTrack.getId().intValue()))
            .andExpect(jsonPath("$.workStatus").value(DEFAULT_WORK_STATUS.toString()))
            .andExpect(jsonPath("$.remarks").value(DEFAULT_REMARKS))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.lastModified").value(DEFAULT_LAST_MODIFIED.toString()))
            .andExpect(jsonPath("$.cancelDate").value(DEFAULT_CANCEL_DATE.toString()));
    }

    @Test
    @Transactional
    void getClassLessionPlanTracksByIdFiltering() throws Exception {
        // Initialize the database
        classLessionPlanTrackRepository.saveAndFlush(classLessionPlanTrack);

        Long id = classLessionPlanTrack.getId();

        defaultClassLessionPlanTrackShouldBeFound("id.equals=" + id);
        defaultClassLessionPlanTrackShouldNotBeFound("id.notEquals=" + id);

        defaultClassLessionPlanTrackShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultClassLessionPlanTrackShouldNotBeFound("id.greaterThan=" + id);

        defaultClassLessionPlanTrackShouldBeFound("id.lessThanOrEqual=" + id);
        defaultClassLessionPlanTrackShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllClassLessionPlanTracksByWorkStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        classLessionPlanTrackRepository.saveAndFlush(classLessionPlanTrack);

        // Get all the classLessionPlanTrackList where workStatus equals to DEFAULT_WORK_STATUS
        defaultClassLessionPlanTrackShouldBeFound("workStatus.equals=" + DEFAULT_WORK_STATUS);

        // Get all the classLessionPlanTrackList where workStatus equals to UPDATED_WORK_STATUS
        defaultClassLessionPlanTrackShouldNotBeFound("workStatus.equals=" + UPDATED_WORK_STATUS);
    }

    @Test
    @Transactional
    void getAllClassLessionPlanTracksByWorkStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classLessionPlanTrackRepository.saveAndFlush(classLessionPlanTrack);

        // Get all the classLessionPlanTrackList where workStatus not equals to DEFAULT_WORK_STATUS
        defaultClassLessionPlanTrackShouldNotBeFound("workStatus.notEquals=" + DEFAULT_WORK_STATUS);

        // Get all the classLessionPlanTrackList where workStatus not equals to UPDATED_WORK_STATUS
        defaultClassLessionPlanTrackShouldBeFound("workStatus.notEquals=" + UPDATED_WORK_STATUS);
    }

    @Test
    @Transactional
    void getAllClassLessionPlanTracksByWorkStatusIsInShouldWork() throws Exception {
        // Initialize the database
        classLessionPlanTrackRepository.saveAndFlush(classLessionPlanTrack);

        // Get all the classLessionPlanTrackList where workStatus in DEFAULT_WORK_STATUS or UPDATED_WORK_STATUS
        defaultClassLessionPlanTrackShouldBeFound("workStatus.in=" + DEFAULT_WORK_STATUS + "," + UPDATED_WORK_STATUS);

        // Get all the classLessionPlanTrackList where workStatus equals to UPDATED_WORK_STATUS
        defaultClassLessionPlanTrackShouldNotBeFound("workStatus.in=" + UPDATED_WORK_STATUS);
    }

    @Test
    @Transactional
    void getAllClassLessionPlanTracksByWorkStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        classLessionPlanTrackRepository.saveAndFlush(classLessionPlanTrack);

        // Get all the classLessionPlanTrackList where workStatus is not null
        defaultClassLessionPlanTrackShouldBeFound("workStatus.specified=true");

        // Get all the classLessionPlanTrackList where workStatus is null
        defaultClassLessionPlanTrackShouldNotBeFound("workStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllClassLessionPlanTracksByRemarksIsEqualToSomething() throws Exception {
        // Initialize the database
        classLessionPlanTrackRepository.saveAndFlush(classLessionPlanTrack);

        // Get all the classLessionPlanTrackList where remarks equals to DEFAULT_REMARKS
        defaultClassLessionPlanTrackShouldBeFound("remarks.equals=" + DEFAULT_REMARKS);

        // Get all the classLessionPlanTrackList where remarks equals to UPDATED_REMARKS
        defaultClassLessionPlanTrackShouldNotBeFound("remarks.equals=" + UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void getAllClassLessionPlanTracksByRemarksIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classLessionPlanTrackRepository.saveAndFlush(classLessionPlanTrack);

        // Get all the classLessionPlanTrackList where remarks not equals to DEFAULT_REMARKS
        defaultClassLessionPlanTrackShouldNotBeFound("remarks.notEquals=" + DEFAULT_REMARKS);

        // Get all the classLessionPlanTrackList where remarks not equals to UPDATED_REMARKS
        defaultClassLessionPlanTrackShouldBeFound("remarks.notEquals=" + UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void getAllClassLessionPlanTracksByRemarksIsInShouldWork() throws Exception {
        // Initialize the database
        classLessionPlanTrackRepository.saveAndFlush(classLessionPlanTrack);

        // Get all the classLessionPlanTrackList where remarks in DEFAULT_REMARKS or UPDATED_REMARKS
        defaultClassLessionPlanTrackShouldBeFound("remarks.in=" + DEFAULT_REMARKS + "," + UPDATED_REMARKS);

        // Get all the classLessionPlanTrackList where remarks equals to UPDATED_REMARKS
        defaultClassLessionPlanTrackShouldNotBeFound("remarks.in=" + UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void getAllClassLessionPlanTracksByRemarksIsNullOrNotNull() throws Exception {
        // Initialize the database
        classLessionPlanTrackRepository.saveAndFlush(classLessionPlanTrack);

        // Get all the classLessionPlanTrackList where remarks is not null
        defaultClassLessionPlanTrackShouldBeFound("remarks.specified=true");

        // Get all the classLessionPlanTrackList where remarks is null
        defaultClassLessionPlanTrackShouldNotBeFound("remarks.specified=false");
    }

    @Test
    @Transactional
    void getAllClassLessionPlanTracksByRemarksContainsSomething() throws Exception {
        // Initialize the database
        classLessionPlanTrackRepository.saveAndFlush(classLessionPlanTrack);

        // Get all the classLessionPlanTrackList where remarks contains DEFAULT_REMARKS
        defaultClassLessionPlanTrackShouldBeFound("remarks.contains=" + DEFAULT_REMARKS);

        // Get all the classLessionPlanTrackList where remarks contains UPDATED_REMARKS
        defaultClassLessionPlanTrackShouldNotBeFound("remarks.contains=" + UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void getAllClassLessionPlanTracksByRemarksNotContainsSomething() throws Exception {
        // Initialize the database
        classLessionPlanTrackRepository.saveAndFlush(classLessionPlanTrack);

        // Get all the classLessionPlanTrackList where remarks does not contain DEFAULT_REMARKS
        defaultClassLessionPlanTrackShouldNotBeFound("remarks.doesNotContain=" + DEFAULT_REMARKS);

        // Get all the classLessionPlanTrackList where remarks does not contain UPDATED_REMARKS
        defaultClassLessionPlanTrackShouldBeFound("remarks.doesNotContain=" + UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void getAllClassLessionPlanTracksByCreateDateIsEqualToSomething() throws Exception {
        // Initialize the database
        classLessionPlanTrackRepository.saveAndFlush(classLessionPlanTrack);

        // Get all the classLessionPlanTrackList where createDate equals to DEFAULT_CREATE_DATE
        defaultClassLessionPlanTrackShouldBeFound("createDate.equals=" + DEFAULT_CREATE_DATE);

        // Get all the classLessionPlanTrackList where createDate equals to UPDATED_CREATE_DATE
        defaultClassLessionPlanTrackShouldNotBeFound("createDate.equals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllClassLessionPlanTracksByCreateDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classLessionPlanTrackRepository.saveAndFlush(classLessionPlanTrack);

        // Get all the classLessionPlanTrackList where createDate not equals to DEFAULT_CREATE_DATE
        defaultClassLessionPlanTrackShouldNotBeFound("createDate.notEquals=" + DEFAULT_CREATE_DATE);

        // Get all the classLessionPlanTrackList where createDate not equals to UPDATED_CREATE_DATE
        defaultClassLessionPlanTrackShouldBeFound("createDate.notEquals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllClassLessionPlanTracksByCreateDateIsInShouldWork() throws Exception {
        // Initialize the database
        classLessionPlanTrackRepository.saveAndFlush(classLessionPlanTrack);

        // Get all the classLessionPlanTrackList where createDate in DEFAULT_CREATE_DATE or UPDATED_CREATE_DATE
        defaultClassLessionPlanTrackShouldBeFound("createDate.in=" + DEFAULT_CREATE_DATE + "," + UPDATED_CREATE_DATE);

        // Get all the classLessionPlanTrackList where createDate equals to UPDATED_CREATE_DATE
        defaultClassLessionPlanTrackShouldNotBeFound("createDate.in=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllClassLessionPlanTracksByCreateDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        classLessionPlanTrackRepository.saveAndFlush(classLessionPlanTrack);

        // Get all the classLessionPlanTrackList where createDate is not null
        defaultClassLessionPlanTrackShouldBeFound("createDate.specified=true");

        // Get all the classLessionPlanTrackList where createDate is null
        defaultClassLessionPlanTrackShouldNotBeFound("createDate.specified=false");
    }

    @Test
    @Transactional
    void getAllClassLessionPlanTracksByCreateDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        classLessionPlanTrackRepository.saveAndFlush(classLessionPlanTrack);

        // Get all the classLessionPlanTrackList where createDate is greater than or equal to DEFAULT_CREATE_DATE
        defaultClassLessionPlanTrackShouldBeFound("createDate.greaterThanOrEqual=" + DEFAULT_CREATE_DATE);

        // Get all the classLessionPlanTrackList where createDate is greater than or equal to UPDATED_CREATE_DATE
        defaultClassLessionPlanTrackShouldNotBeFound("createDate.greaterThanOrEqual=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllClassLessionPlanTracksByCreateDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        classLessionPlanTrackRepository.saveAndFlush(classLessionPlanTrack);

        // Get all the classLessionPlanTrackList where createDate is less than or equal to DEFAULT_CREATE_DATE
        defaultClassLessionPlanTrackShouldBeFound("createDate.lessThanOrEqual=" + DEFAULT_CREATE_DATE);

        // Get all the classLessionPlanTrackList where createDate is less than or equal to SMALLER_CREATE_DATE
        defaultClassLessionPlanTrackShouldNotBeFound("createDate.lessThanOrEqual=" + SMALLER_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllClassLessionPlanTracksByCreateDateIsLessThanSomething() throws Exception {
        // Initialize the database
        classLessionPlanTrackRepository.saveAndFlush(classLessionPlanTrack);

        // Get all the classLessionPlanTrackList where createDate is less than DEFAULT_CREATE_DATE
        defaultClassLessionPlanTrackShouldNotBeFound("createDate.lessThan=" + DEFAULT_CREATE_DATE);

        // Get all the classLessionPlanTrackList where createDate is less than UPDATED_CREATE_DATE
        defaultClassLessionPlanTrackShouldBeFound("createDate.lessThan=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllClassLessionPlanTracksByCreateDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        classLessionPlanTrackRepository.saveAndFlush(classLessionPlanTrack);

        // Get all the classLessionPlanTrackList where createDate is greater than DEFAULT_CREATE_DATE
        defaultClassLessionPlanTrackShouldNotBeFound("createDate.greaterThan=" + DEFAULT_CREATE_DATE);

        // Get all the classLessionPlanTrackList where createDate is greater than SMALLER_CREATE_DATE
        defaultClassLessionPlanTrackShouldBeFound("createDate.greaterThan=" + SMALLER_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllClassLessionPlanTracksByLastModifiedIsEqualToSomething() throws Exception {
        // Initialize the database
        classLessionPlanTrackRepository.saveAndFlush(classLessionPlanTrack);

        // Get all the classLessionPlanTrackList where lastModified equals to DEFAULT_LAST_MODIFIED
        defaultClassLessionPlanTrackShouldBeFound("lastModified.equals=" + DEFAULT_LAST_MODIFIED);

        // Get all the classLessionPlanTrackList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultClassLessionPlanTrackShouldNotBeFound("lastModified.equals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllClassLessionPlanTracksByLastModifiedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classLessionPlanTrackRepository.saveAndFlush(classLessionPlanTrack);

        // Get all the classLessionPlanTrackList where lastModified not equals to DEFAULT_LAST_MODIFIED
        defaultClassLessionPlanTrackShouldNotBeFound("lastModified.notEquals=" + DEFAULT_LAST_MODIFIED);

        // Get all the classLessionPlanTrackList where lastModified not equals to UPDATED_LAST_MODIFIED
        defaultClassLessionPlanTrackShouldBeFound("lastModified.notEquals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllClassLessionPlanTracksByLastModifiedIsInShouldWork() throws Exception {
        // Initialize the database
        classLessionPlanTrackRepository.saveAndFlush(classLessionPlanTrack);

        // Get all the classLessionPlanTrackList where lastModified in DEFAULT_LAST_MODIFIED or UPDATED_LAST_MODIFIED
        defaultClassLessionPlanTrackShouldBeFound("lastModified.in=" + DEFAULT_LAST_MODIFIED + "," + UPDATED_LAST_MODIFIED);

        // Get all the classLessionPlanTrackList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultClassLessionPlanTrackShouldNotBeFound("lastModified.in=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllClassLessionPlanTracksByLastModifiedIsNullOrNotNull() throws Exception {
        // Initialize the database
        classLessionPlanTrackRepository.saveAndFlush(classLessionPlanTrack);

        // Get all the classLessionPlanTrackList where lastModified is not null
        defaultClassLessionPlanTrackShouldBeFound("lastModified.specified=true");

        // Get all the classLessionPlanTrackList where lastModified is null
        defaultClassLessionPlanTrackShouldNotBeFound("lastModified.specified=false");
    }

    @Test
    @Transactional
    void getAllClassLessionPlanTracksByLastModifiedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        classLessionPlanTrackRepository.saveAndFlush(classLessionPlanTrack);

        // Get all the classLessionPlanTrackList where lastModified is greater than or equal to DEFAULT_LAST_MODIFIED
        defaultClassLessionPlanTrackShouldBeFound("lastModified.greaterThanOrEqual=" + DEFAULT_LAST_MODIFIED);

        // Get all the classLessionPlanTrackList where lastModified is greater than or equal to UPDATED_LAST_MODIFIED
        defaultClassLessionPlanTrackShouldNotBeFound("lastModified.greaterThanOrEqual=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllClassLessionPlanTracksByLastModifiedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        classLessionPlanTrackRepository.saveAndFlush(classLessionPlanTrack);

        // Get all the classLessionPlanTrackList where lastModified is less than or equal to DEFAULT_LAST_MODIFIED
        defaultClassLessionPlanTrackShouldBeFound("lastModified.lessThanOrEqual=" + DEFAULT_LAST_MODIFIED);

        // Get all the classLessionPlanTrackList where lastModified is less than or equal to SMALLER_LAST_MODIFIED
        defaultClassLessionPlanTrackShouldNotBeFound("lastModified.lessThanOrEqual=" + SMALLER_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllClassLessionPlanTracksByLastModifiedIsLessThanSomething() throws Exception {
        // Initialize the database
        classLessionPlanTrackRepository.saveAndFlush(classLessionPlanTrack);

        // Get all the classLessionPlanTrackList where lastModified is less than DEFAULT_LAST_MODIFIED
        defaultClassLessionPlanTrackShouldNotBeFound("lastModified.lessThan=" + DEFAULT_LAST_MODIFIED);

        // Get all the classLessionPlanTrackList where lastModified is less than UPDATED_LAST_MODIFIED
        defaultClassLessionPlanTrackShouldBeFound("lastModified.lessThan=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllClassLessionPlanTracksByLastModifiedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        classLessionPlanTrackRepository.saveAndFlush(classLessionPlanTrack);

        // Get all the classLessionPlanTrackList where lastModified is greater than DEFAULT_LAST_MODIFIED
        defaultClassLessionPlanTrackShouldNotBeFound("lastModified.greaterThan=" + DEFAULT_LAST_MODIFIED);

        // Get all the classLessionPlanTrackList where lastModified is greater than SMALLER_LAST_MODIFIED
        defaultClassLessionPlanTrackShouldBeFound("lastModified.greaterThan=" + SMALLER_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllClassLessionPlanTracksByCancelDateIsEqualToSomething() throws Exception {
        // Initialize the database
        classLessionPlanTrackRepository.saveAndFlush(classLessionPlanTrack);

        // Get all the classLessionPlanTrackList where cancelDate equals to DEFAULT_CANCEL_DATE
        defaultClassLessionPlanTrackShouldBeFound("cancelDate.equals=" + DEFAULT_CANCEL_DATE);

        // Get all the classLessionPlanTrackList where cancelDate equals to UPDATED_CANCEL_DATE
        defaultClassLessionPlanTrackShouldNotBeFound("cancelDate.equals=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllClassLessionPlanTracksByCancelDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classLessionPlanTrackRepository.saveAndFlush(classLessionPlanTrack);

        // Get all the classLessionPlanTrackList where cancelDate not equals to DEFAULT_CANCEL_DATE
        defaultClassLessionPlanTrackShouldNotBeFound("cancelDate.notEquals=" + DEFAULT_CANCEL_DATE);

        // Get all the classLessionPlanTrackList where cancelDate not equals to UPDATED_CANCEL_DATE
        defaultClassLessionPlanTrackShouldBeFound("cancelDate.notEquals=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllClassLessionPlanTracksByCancelDateIsInShouldWork() throws Exception {
        // Initialize the database
        classLessionPlanTrackRepository.saveAndFlush(classLessionPlanTrack);

        // Get all the classLessionPlanTrackList where cancelDate in DEFAULT_CANCEL_DATE or UPDATED_CANCEL_DATE
        defaultClassLessionPlanTrackShouldBeFound("cancelDate.in=" + DEFAULT_CANCEL_DATE + "," + UPDATED_CANCEL_DATE);

        // Get all the classLessionPlanTrackList where cancelDate equals to UPDATED_CANCEL_DATE
        defaultClassLessionPlanTrackShouldNotBeFound("cancelDate.in=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllClassLessionPlanTracksByCancelDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        classLessionPlanTrackRepository.saveAndFlush(classLessionPlanTrack);

        // Get all the classLessionPlanTrackList where cancelDate is not null
        defaultClassLessionPlanTrackShouldBeFound("cancelDate.specified=true");

        // Get all the classLessionPlanTrackList where cancelDate is null
        defaultClassLessionPlanTrackShouldNotBeFound("cancelDate.specified=false");
    }

    @Test
    @Transactional
    void getAllClassLessionPlanTracksByCancelDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        classLessionPlanTrackRepository.saveAndFlush(classLessionPlanTrack);

        // Get all the classLessionPlanTrackList where cancelDate is greater than or equal to DEFAULT_CANCEL_DATE
        defaultClassLessionPlanTrackShouldBeFound("cancelDate.greaterThanOrEqual=" + DEFAULT_CANCEL_DATE);

        // Get all the classLessionPlanTrackList where cancelDate is greater than or equal to UPDATED_CANCEL_DATE
        defaultClassLessionPlanTrackShouldNotBeFound("cancelDate.greaterThanOrEqual=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllClassLessionPlanTracksByCancelDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        classLessionPlanTrackRepository.saveAndFlush(classLessionPlanTrack);

        // Get all the classLessionPlanTrackList where cancelDate is less than or equal to DEFAULT_CANCEL_DATE
        defaultClassLessionPlanTrackShouldBeFound("cancelDate.lessThanOrEqual=" + DEFAULT_CANCEL_DATE);

        // Get all the classLessionPlanTrackList where cancelDate is less than or equal to SMALLER_CANCEL_DATE
        defaultClassLessionPlanTrackShouldNotBeFound("cancelDate.lessThanOrEqual=" + SMALLER_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllClassLessionPlanTracksByCancelDateIsLessThanSomething() throws Exception {
        // Initialize the database
        classLessionPlanTrackRepository.saveAndFlush(classLessionPlanTrack);

        // Get all the classLessionPlanTrackList where cancelDate is less than DEFAULT_CANCEL_DATE
        defaultClassLessionPlanTrackShouldNotBeFound("cancelDate.lessThan=" + DEFAULT_CANCEL_DATE);

        // Get all the classLessionPlanTrackList where cancelDate is less than UPDATED_CANCEL_DATE
        defaultClassLessionPlanTrackShouldBeFound("cancelDate.lessThan=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllClassLessionPlanTracksByCancelDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        classLessionPlanTrackRepository.saveAndFlush(classLessionPlanTrack);

        // Get all the classLessionPlanTrackList where cancelDate is greater than DEFAULT_CANCEL_DATE
        defaultClassLessionPlanTrackShouldNotBeFound("cancelDate.greaterThan=" + DEFAULT_CANCEL_DATE);

        // Get all the classLessionPlanTrackList where cancelDate is greater than SMALLER_CANCEL_DATE
        defaultClassLessionPlanTrackShouldBeFound("cancelDate.greaterThan=" + SMALLER_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllClassLessionPlanTracksByClassLessionPlanIsEqualToSomething() throws Exception {
        // Initialize the database
        classLessionPlanTrackRepository.saveAndFlush(classLessionPlanTrack);
        ClassLessionPlan classLessionPlan = ClassLessionPlanResourceIT.createEntity(em);
        em.persist(classLessionPlan);
        em.flush();
        classLessionPlanTrack.setClassLessionPlan(classLessionPlan);
        classLessionPlanTrackRepository.saveAndFlush(classLessionPlanTrack);
        Long classLessionPlanId = classLessionPlan.getId();

        // Get all the classLessionPlanTrackList where classLessionPlan equals to classLessionPlanId
        defaultClassLessionPlanTrackShouldBeFound("classLessionPlanId.equals=" + classLessionPlanId);

        // Get all the classLessionPlanTrackList where classLessionPlan equals to (classLessionPlanId + 1)
        defaultClassLessionPlanTrackShouldNotBeFound("classLessionPlanId.equals=" + (classLessionPlanId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultClassLessionPlanTrackShouldBeFound(String filter) throws Exception {
        restClassLessionPlanTrackMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(classLessionPlanTrack.getId().intValue())))
            .andExpect(jsonPath("$.[*].workStatus").value(hasItem(DEFAULT_WORK_STATUS.toString())))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].cancelDate").value(hasItem(DEFAULT_CANCEL_DATE.toString())));

        // Check, that the count call also returns 1
        restClassLessionPlanTrackMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultClassLessionPlanTrackShouldNotBeFound(String filter) throws Exception {
        restClassLessionPlanTrackMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restClassLessionPlanTrackMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingClassLessionPlanTrack() throws Exception {
        // Get the classLessionPlanTrack
        restClassLessionPlanTrackMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewClassLessionPlanTrack() throws Exception {
        // Initialize the database
        classLessionPlanTrackRepository.saveAndFlush(classLessionPlanTrack);

        int databaseSizeBeforeUpdate = classLessionPlanTrackRepository.findAll().size();

        // Update the classLessionPlanTrack
        ClassLessionPlanTrack updatedClassLessionPlanTrack = classLessionPlanTrackRepository.findById(classLessionPlanTrack.getId()).get();
        // Disconnect from session so that the updates on updatedClassLessionPlanTrack are not directly saved in db
        em.detach(updatedClassLessionPlanTrack);
        updatedClassLessionPlanTrack
            .workStatus(UPDATED_WORK_STATUS)
            .remarks(UPDATED_REMARKS)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);
        ClassLessionPlanTrackDTO classLessionPlanTrackDTO = classLessionPlanTrackMapper.toDto(updatedClassLessionPlanTrack);

        restClassLessionPlanTrackMockMvc
            .perform(
                put(ENTITY_API_URL_ID, classLessionPlanTrackDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(classLessionPlanTrackDTO))
            )
            .andExpect(status().isOk());

        // Validate the ClassLessionPlanTrack in the database
        List<ClassLessionPlanTrack> classLessionPlanTrackList = classLessionPlanTrackRepository.findAll();
        assertThat(classLessionPlanTrackList).hasSize(databaseSizeBeforeUpdate);
        ClassLessionPlanTrack testClassLessionPlanTrack = classLessionPlanTrackList.get(classLessionPlanTrackList.size() - 1);
        assertThat(testClassLessionPlanTrack.getWorkStatus()).isEqualTo(UPDATED_WORK_STATUS);
        assertThat(testClassLessionPlanTrack.getRemarks()).isEqualTo(UPDATED_REMARKS);
        assertThat(testClassLessionPlanTrack.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testClassLessionPlanTrack.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testClassLessionPlanTrack.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void putNonExistingClassLessionPlanTrack() throws Exception {
        int databaseSizeBeforeUpdate = classLessionPlanTrackRepository.findAll().size();
        classLessionPlanTrack.setId(count.incrementAndGet());

        // Create the ClassLessionPlanTrack
        ClassLessionPlanTrackDTO classLessionPlanTrackDTO = classLessionPlanTrackMapper.toDto(classLessionPlanTrack);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClassLessionPlanTrackMockMvc
            .perform(
                put(ENTITY_API_URL_ID, classLessionPlanTrackDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(classLessionPlanTrackDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClassLessionPlanTrack in the database
        List<ClassLessionPlanTrack> classLessionPlanTrackList = classLessionPlanTrackRepository.findAll();
        assertThat(classLessionPlanTrackList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchClassLessionPlanTrack() throws Exception {
        int databaseSizeBeforeUpdate = classLessionPlanTrackRepository.findAll().size();
        classLessionPlanTrack.setId(count.incrementAndGet());

        // Create the ClassLessionPlanTrack
        ClassLessionPlanTrackDTO classLessionPlanTrackDTO = classLessionPlanTrackMapper.toDto(classLessionPlanTrack);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClassLessionPlanTrackMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(classLessionPlanTrackDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClassLessionPlanTrack in the database
        List<ClassLessionPlanTrack> classLessionPlanTrackList = classLessionPlanTrackRepository.findAll();
        assertThat(classLessionPlanTrackList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamClassLessionPlanTrack() throws Exception {
        int databaseSizeBeforeUpdate = classLessionPlanTrackRepository.findAll().size();
        classLessionPlanTrack.setId(count.incrementAndGet());

        // Create the ClassLessionPlanTrack
        ClassLessionPlanTrackDTO classLessionPlanTrackDTO = classLessionPlanTrackMapper.toDto(classLessionPlanTrack);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClassLessionPlanTrackMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(classLessionPlanTrackDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ClassLessionPlanTrack in the database
        List<ClassLessionPlanTrack> classLessionPlanTrackList = classLessionPlanTrackRepository.findAll();
        assertThat(classLessionPlanTrackList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateClassLessionPlanTrackWithPatch() throws Exception {
        // Initialize the database
        classLessionPlanTrackRepository.saveAndFlush(classLessionPlanTrack);

        int databaseSizeBeforeUpdate = classLessionPlanTrackRepository.findAll().size();

        // Update the classLessionPlanTrack using partial update
        ClassLessionPlanTrack partialUpdatedClassLessionPlanTrack = new ClassLessionPlanTrack();
        partialUpdatedClassLessionPlanTrack.setId(classLessionPlanTrack.getId());

        partialUpdatedClassLessionPlanTrack.cancelDate(UPDATED_CANCEL_DATE);

        restClassLessionPlanTrackMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClassLessionPlanTrack.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedClassLessionPlanTrack))
            )
            .andExpect(status().isOk());

        // Validate the ClassLessionPlanTrack in the database
        List<ClassLessionPlanTrack> classLessionPlanTrackList = classLessionPlanTrackRepository.findAll();
        assertThat(classLessionPlanTrackList).hasSize(databaseSizeBeforeUpdate);
        ClassLessionPlanTrack testClassLessionPlanTrack = classLessionPlanTrackList.get(classLessionPlanTrackList.size() - 1);
        assertThat(testClassLessionPlanTrack.getWorkStatus()).isEqualTo(DEFAULT_WORK_STATUS);
        assertThat(testClassLessionPlanTrack.getRemarks()).isEqualTo(DEFAULT_REMARKS);
        assertThat(testClassLessionPlanTrack.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testClassLessionPlanTrack.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
        assertThat(testClassLessionPlanTrack.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void fullUpdateClassLessionPlanTrackWithPatch() throws Exception {
        // Initialize the database
        classLessionPlanTrackRepository.saveAndFlush(classLessionPlanTrack);

        int databaseSizeBeforeUpdate = classLessionPlanTrackRepository.findAll().size();

        // Update the classLessionPlanTrack using partial update
        ClassLessionPlanTrack partialUpdatedClassLessionPlanTrack = new ClassLessionPlanTrack();
        partialUpdatedClassLessionPlanTrack.setId(classLessionPlanTrack.getId());

        partialUpdatedClassLessionPlanTrack
            .workStatus(UPDATED_WORK_STATUS)
            .remarks(UPDATED_REMARKS)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);

        restClassLessionPlanTrackMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClassLessionPlanTrack.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedClassLessionPlanTrack))
            )
            .andExpect(status().isOk());

        // Validate the ClassLessionPlanTrack in the database
        List<ClassLessionPlanTrack> classLessionPlanTrackList = classLessionPlanTrackRepository.findAll();
        assertThat(classLessionPlanTrackList).hasSize(databaseSizeBeforeUpdate);
        ClassLessionPlanTrack testClassLessionPlanTrack = classLessionPlanTrackList.get(classLessionPlanTrackList.size() - 1);
        assertThat(testClassLessionPlanTrack.getWorkStatus()).isEqualTo(UPDATED_WORK_STATUS);
        assertThat(testClassLessionPlanTrack.getRemarks()).isEqualTo(UPDATED_REMARKS);
        assertThat(testClassLessionPlanTrack.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testClassLessionPlanTrack.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testClassLessionPlanTrack.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingClassLessionPlanTrack() throws Exception {
        int databaseSizeBeforeUpdate = classLessionPlanTrackRepository.findAll().size();
        classLessionPlanTrack.setId(count.incrementAndGet());

        // Create the ClassLessionPlanTrack
        ClassLessionPlanTrackDTO classLessionPlanTrackDTO = classLessionPlanTrackMapper.toDto(classLessionPlanTrack);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClassLessionPlanTrackMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, classLessionPlanTrackDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(classLessionPlanTrackDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClassLessionPlanTrack in the database
        List<ClassLessionPlanTrack> classLessionPlanTrackList = classLessionPlanTrackRepository.findAll();
        assertThat(classLessionPlanTrackList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchClassLessionPlanTrack() throws Exception {
        int databaseSizeBeforeUpdate = classLessionPlanTrackRepository.findAll().size();
        classLessionPlanTrack.setId(count.incrementAndGet());

        // Create the ClassLessionPlanTrack
        ClassLessionPlanTrackDTO classLessionPlanTrackDTO = classLessionPlanTrackMapper.toDto(classLessionPlanTrack);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClassLessionPlanTrackMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(classLessionPlanTrackDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClassLessionPlanTrack in the database
        List<ClassLessionPlanTrack> classLessionPlanTrackList = classLessionPlanTrackRepository.findAll();
        assertThat(classLessionPlanTrackList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamClassLessionPlanTrack() throws Exception {
        int databaseSizeBeforeUpdate = classLessionPlanTrackRepository.findAll().size();
        classLessionPlanTrack.setId(count.incrementAndGet());

        // Create the ClassLessionPlanTrack
        ClassLessionPlanTrackDTO classLessionPlanTrackDTO = classLessionPlanTrackMapper.toDto(classLessionPlanTrack);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClassLessionPlanTrackMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(classLessionPlanTrackDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ClassLessionPlanTrack in the database
        List<ClassLessionPlanTrack> classLessionPlanTrackList = classLessionPlanTrackRepository.findAll();
        assertThat(classLessionPlanTrackList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteClassLessionPlanTrack() throws Exception {
        // Initialize the database
        classLessionPlanTrackRepository.saveAndFlush(classLessionPlanTrack);

        int databaseSizeBeforeDelete = classLessionPlanTrackRepository.findAll().size();

        // Delete the classLessionPlanTrack
        restClassLessionPlanTrackMockMvc
            .perform(delete(ENTITY_API_URL_ID, classLessionPlanTrack.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ClassLessionPlanTrack> classLessionPlanTrackList = classLessionPlanTrackRepository.findAll();
        assertThat(classLessionPlanTrackList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
