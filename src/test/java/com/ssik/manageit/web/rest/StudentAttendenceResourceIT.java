package com.ssik.manageit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ssik.manageit.IntegrationTest;
import com.ssik.manageit.domain.ClassStudent;
import com.ssik.manageit.domain.StudentAttendence;
import com.ssik.manageit.repository.StudentAttendenceRepository;
import com.ssik.manageit.service.criteria.StudentAttendenceCriteria;
import com.ssik.manageit.service.dto.StudentAttendenceDTO;
import com.ssik.manageit.service.mapper.StudentAttendenceMapper;
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
 * Integration tests for the {@link StudentAttendenceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StudentAttendenceResourceIT {

    private static final LocalDate DEFAULT_SCHOOL_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_SCHOOL_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_SCHOOL_DATE = LocalDate.ofEpochDay(-1L);

    private static final Boolean DEFAULT_ATTENDENCE = false;
    private static final Boolean UPDATED_ATTENDENCE = true;

    private static final LocalDate DEFAULT_CREATE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATE_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CREATE_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_LAST_MODIFIED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_MODIFIED = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_LAST_MODIFIED = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_CANCEL_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CANCEL_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CANCEL_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/student-attendences";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private StudentAttendenceRepository studentAttendenceRepository;

    @Autowired
    private StudentAttendenceMapper studentAttendenceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStudentAttendenceMockMvc;

    private StudentAttendence studentAttendence;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StudentAttendence createEntity(EntityManager em) {
        StudentAttendence studentAttendence = new StudentAttendence()
            .schoolDate(DEFAULT_SCHOOL_DATE)
            .attendence(DEFAULT_ATTENDENCE)
            .createDate(DEFAULT_CREATE_DATE)
            .lastModified(DEFAULT_LAST_MODIFIED)
            .cancelDate(DEFAULT_CANCEL_DATE);
        return studentAttendence;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StudentAttendence createUpdatedEntity(EntityManager em) {
        StudentAttendence studentAttendence = new StudentAttendence()
            .schoolDate(UPDATED_SCHOOL_DATE)
            .attendence(UPDATED_ATTENDENCE)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);
        return studentAttendence;
    }

    @BeforeEach
    public void initTest() {
        studentAttendence = createEntity(em);
    }

    @Test
    @Transactional
    void createStudentAttendence() throws Exception {
        int databaseSizeBeforeCreate = studentAttendenceRepository.findAll().size();
        // Create the StudentAttendence
        StudentAttendenceDTO studentAttendenceDTO = studentAttendenceMapper.toDto(studentAttendence);
        restStudentAttendenceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(studentAttendenceDTO))
            )
            .andExpect(status().isCreated());

        // Validate the StudentAttendence in the database
        List<StudentAttendence> studentAttendenceList = studentAttendenceRepository.findAll();
        assertThat(studentAttendenceList).hasSize(databaseSizeBeforeCreate + 1);
        StudentAttendence testStudentAttendence = studentAttendenceList.get(studentAttendenceList.size() - 1);
        assertThat(testStudentAttendence.getSchoolDate()).isEqualTo(DEFAULT_SCHOOL_DATE);
        assertThat(testStudentAttendence.getAttendence()).isEqualTo(DEFAULT_ATTENDENCE);
        assertThat(testStudentAttendence.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testStudentAttendence.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
        assertThat(testStudentAttendence.getCancelDate()).isEqualTo(DEFAULT_CANCEL_DATE);
    }

    @Test
    @Transactional
    void createStudentAttendenceWithExistingId() throws Exception {
        // Create the StudentAttendence with an existing ID
        studentAttendence.setId(1L);
        StudentAttendenceDTO studentAttendenceDTO = studentAttendenceMapper.toDto(studentAttendence);

        int databaseSizeBeforeCreate = studentAttendenceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStudentAttendenceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(studentAttendenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentAttendence in the database
        List<StudentAttendence> studentAttendenceList = studentAttendenceRepository.findAll();
        assertThat(studentAttendenceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSchoolDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = studentAttendenceRepository.findAll().size();
        // set the field null
        studentAttendence.setSchoolDate(null);

        // Create the StudentAttendence, which fails.
        StudentAttendenceDTO studentAttendenceDTO = studentAttendenceMapper.toDto(studentAttendence);

        restStudentAttendenceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(studentAttendenceDTO))
            )
            .andExpect(status().isBadRequest());

        List<StudentAttendence> studentAttendenceList = studentAttendenceRepository.findAll();
        assertThat(studentAttendenceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAttendenceIsRequired() throws Exception {
        int databaseSizeBeforeTest = studentAttendenceRepository.findAll().size();
        // set the field null
        studentAttendence.setAttendence(null);

        // Create the StudentAttendence, which fails.
        StudentAttendenceDTO studentAttendenceDTO = studentAttendenceMapper.toDto(studentAttendence);

        restStudentAttendenceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(studentAttendenceDTO))
            )
            .andExpect(status().isBadRequest());

        List<StudentAttendence> studentAttendenceList = studentAttendenceRepository.findAll();
        assertThat(studentAttendenceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllStudentAttendences() throws Exception {
        // Initialize the database
        studentAttendenceRepository.saveAndFlush(studentAttendence);

        // Get all the studentAttendenceList
        restStudentAttendenceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(studentAttendence.getId().intValue())))
            .andExpect(jsonPath("$.[*].schoolDate").value(hasItem(DEFAULT_SCHOOL_DATE.toString())))
            .andExpect(jsonPath("$.[*].attendence").value(hasItem(DEFAULT_ATTENDENCE.booleanValue())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].cancelDate").value(hasItem(DEFAULT_CANCEL_DATE.toString())));
    }

    @Test
    @Transactional
    void getStudentAttendence() throws Exception {
        // Initialize the database
        studentAttendenceRepository.saveAndFlush(studentAttendence);

        // Get the studentAttendence
        restStudentAttendenceMockMvc
            .perform(get(ENTITY_API_URL_ID, studentAttendence.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(studentAttendence.getId().intValue()))
            .andExpect(jsonPath("$.schoolDate").value(DEFAULT_SCHOOL_DATE.toString()))
            .andExpect(jsonPath("$.attendence").value(DEFAULT_ATTENDENCE.booleanValue()))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.lastModified").value(DEFAULT_LAST_MODIFIED.toString()))
            .andExpect(jsonPath("$.cancelDate").value(DEFAULT_CANCEL_DATE.toString()));
    }

    @Test
    @Transactional
    void getStudentAttendencesByIdFiltering() throws Exception {
        // Initialize the database
        studentAttendenceRepository.saveAndFlush(studentAttendence);

        Long id = studentAttendence.getId();

        defaultStudentAttendenceShouldBeFound("id.equals=" + id);
        defaultStudentAttendenceShouldNotBeFound("id.notEquals=" + id);

        defaultStudentAttendenceShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultStudentAttendenceShouldNotBeFound("id.greaterThan=" + id);

        defaultStudentAttendenceShouldBeFound("id.lessThanOrEqual=" + id);
        defaultStudentAttendenceShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllStudentAttendencesBySchoolDateIsEqualToSomething() throws Exception {
        // Initialize the database
        studentAttendenceRepository.saveAndFlush(studentAttendence);

        // Get all the studentAttendenceList where schoolDate equals to DEFAULT_SCHOOL_DATE
        defaultStudentAttendenceShouldBeFound("schoolDate.equals=" + DEFAULT_SCHOOL_DATE);

        // Get all the studentAttendenceList where schoolDate equals to UPDATED_SCHOOL_DATE
        defaultStudentAttendenceShouldNotBeFound("schoolDate.equals=" + UPDATED_SCHOOL_DATE);
    }

    @Test
    @Transactional
    void getAllStudentAttendencesBySchoolDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentAttendenceRepository.saveAndFlush(studentAttendence);

        // Get all the studentAttendenceList where schoolDate not equals to DEFAULT_SCHOOL_DATE
        defaultStudentAttendenceShouldNotBeFound("schoolDate.notEquals=" + DEFAULT_SCHOOL_DATE);

        // Get all the studentAttendenceList where schoolDate not equals to UPDATED_SCHOOL_DATE
        defaultStudentAttendenceShouldBeFound("schoolDate.notEquals=" + UPDATED_SCHOOL_DATE);
    }

    @Test
    @Transactional
    void getAllStudentAttendencesBySchoolDateIsInShouldWork() throws Exception {
        // Initialize the database
        studentAttendenceRepository.saveAndFlush(studentAttendence);

        // Get all the studentAttendenceList where schoolDate in DEFAULT_SCHOOL_DATE or UPDATED_SCHOOL_DATE
        defaultStudentAttendenceShouldBeFound("schoolDate.in=" + DEFAULT_SCHOOL_DATE + "," + UPDATED_SCHOOL_DATE);

        // Get all the studentAttendenceList where schoolDate equals to UPDATED_SCHOOL_DATE
        defaultStudentAttendenceShouldNotBeFound("schoolDate.in=" + UPDATED_SCHOOL_DATE);
    }

    @Test
    @Transactional
    void getAllStudentAttendencesBySchoolDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentAttendenceRepository.saveAndFlush(studentAttendence);

        // Get all the studentAttendenceList where schoolDate is not null
        defaultStudentAttendenceShouldBeFound("schoolDate.specified=true");

        // Get all the studentAttendenceList where schoolDate is null
        defaultStudentAttendenceShouldNotBeFound("schoolDate.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentAttendencesBySchoolDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        studentAttendenceRepository.saveAndFlush(studentAttendence);

        // Get all the studentAttendenceList where schoolDate is greater than or equal to DEFAULT_SCHOOL_DATE
        defaultStudentAttendenceShouldBeFound("schoolDate.greaterThanOrEqual=" + DEFAULT_SCHOOL_DATE);

        // Get all the studentAttendenceList where schoolDate is greater than or equal to UPDATED_SCHOOL_DATE
        defaultStudentAttendenceShouldNotBeFound("schoolDate.greaterThanOrEqual=" + UPDATED_SCHOOL_DATE);
    }

    @Test
    @Transactional
    void getAllStudentAttendencesBySchoolDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        studentAttendenceRepository.saveAndFlush(studentAttendence);

        // Get all the studentAttendenceList where schoolDate is less than or equal to DEFAULT_SCHOOL_DATE
        defaultStudentAttendenceShouldBeFound("schoolDate.lessThanOrEqual=" + DEFAULT_SCHOOL_DATE);

        // Get all the studentAttendenceList where schoolDate is less than or equal to SMALLER_SCHOOL_DATE
        defaultStudentAttendenceShouldNotBeFound("schoolDate.lessThanOrEqual=" + SMALLER_SCHOOL_DATE);
    }

    @Test
    @Transactional
    void getAllStudentAttendencesBySchoolDateIsLessThanSomething() throws Exception {
        // Initialize the database
        studentAttendenceRepository.saveAndFlush(studentAttendence);

        // Get all the studentAttendenceList where schoolDate is less than DEFAULT_SCHOOL_DATE
        defaultStudentAttendenceShouldNotBeFound("schoolDate.lessThan=" + DEFAULT_SCHOOL_DATE);

        // Get all the studentAttendenceList where schoolDate is less than UPDATED_SCHOOL_DATE
        defaultStudentAttendenceShouldBeFound("schoolDate.lessThan=" + UPDATED_SCHOOL_DATE);
    }

    @Test
    @Transactional
    void getAllStudentAttendencesBySchoolDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        studentAttendenceRepository.saveAndFlush(studentAttendence);

        // Get all the studentAttendenceList where schoolDate is greater than DEFAULT_SCHOOL_DATE
        defaultStudentAttendenceShouldNotBeFound("schoolDate.greaterThan=" + DEFAULT_SCHOOL_DATE);

        // Get all the studentAttendenceList where schoolDate is greater than SMALLER_SCHOOL_DATE
        defaultStudentAttendenceShouldBeFound("schoolDate.greaterThan=" + SMALLER_SCHOOL_DATE);
    }

    @Test
    @Transactional
    void getAllStudentAttendencesByAttendenceIsEqualToSomething() throws Exception {
        // Initialize the database
        studentAttendenceRepository.saveAndFlush(studentAttendence);

        // Get all the studentAttendenceList where attendence equals to DEFAULT_ATTENDENCE
        defaultStudentAttendenceShouldBeFound("attendence.equals=" + DEFAULT_ATTENDENCE);

        // Get all the studentAttendenceList where attendence equals to UPDATED_ATTENDENCE
        defaultStudentAttendenceShouldNotBeFound("attendence.equals=" + UPDATED_ATTENDENCE);
    }

    @Test
    @Transactional
    void getAllStudentAttendencesByAttendenceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentAttendenceRepository.saveAndFlush(studentAttendence);

        // Get all the studentAttendenceList where attendence not equals to DEFAULT_ATTENDENCE
        defaultStudentAttendenceShouldNotBeFound("attendence.notEquals=" + DEFAULT_ATTENDENCE);

        // Get all the studentAttendenceList where attendence not equals to UPDATED_ATTENDENCE
        defaultStudentAttendenceShouldBeFound("attendence.notEquals=" + UPDATED_ATTENDENCE);
    }

    @Test
    @Transactional
    void getAllStudentAttendencesByAttendenceIsInShouldWork() throws Exception {
        // Initialize the database
        studentAttendenceRepository.saveAndFlush(studentAttendence);

        // Get all the studentAttendenceList where attendence in DEFAULT_ATTENDENCE or UPDATED_ATTENDENCE
        defaultStudentAttendenceShouldBeFound("attendence.in=" + DEFAULT_ATTENDENCE + "," + UPDATED_ATTENDENCE);

        // Get all the studentAttendenceList where attendence equals to UPDATED_ATTENDENCE
        defaultStudentAttendenceShouldNotBeFound("attendence.in=" + UPDATED_ATTENDENCE);
    }

    @Test
    @Transactional
    void getAllStudentAttendencesByAttendenceIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentAttendenceRepository.saveAndFlush(studentAttendence);

        // Get all the studentAttendenceList where attendence is not null
        defaultStudentAttendenceShouldBeFound("attendence.specified=true");

        // Get all the studentAttendenceList where attendence is null
        defaultStudentAttendenceShouldNotBeFound("attendence.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentAttendencesByCreateDateIsEqualToSomething() throws Exception {
        // Initialize the database
        studentAttendenceRepository.saveAndFlush(studentAttendence);

        // Get all the studentAttendenceList where createDate equals to DEFAULT_CREATE_DATE
        defaultStudentAttendenceShouldBeFound("createDate.equals=" + DEFAULT_CREATE_DATE);

        // Get all the studentAttendenceList where createDate equals to UPDATED_CREATE_DATE
        defaultStudentAttendenceShouldNotBeFound("createDate.equals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllStudentAttendencesByCreateDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentAttendenceRepository.saveAndFlush(studentAttendence);

        // Get all the studentAttendenceList where createDate not equals to DEFAULT_CREATE_DATE
        defaultStudentAttendenceShouldNotBeFound("createDate.notEquals=" + DEFAULT_CREATE_DATE);

        // Get all the studentAttendenceList where createDate not equals to UPDATED_CREATE_DATE
        defaultStudentAttendenceShouldBeFound("createDate.notEquals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllStudentAttendencesByCreateDateIsInShouldWork() throws Exception {
        // Initialize the database
        studentAttendenceRepository.saveAndFlush(studentAttendence);

        // Get all the studentAttendenceList where createDate in DEFAULT_CREATE_DATE or UPDATED_CREATE_DATE
        defaultStudentAttendenceShouldBeFound("createDate.in=" + DEFAULT_CREATE_DATE + "," + UPDATED_CREATE_DATE);

        // Get all the studentAttendenceList where createDate equals to UPDATED_CREATE_DATE
        defaultStudentAttendenceShouldNotBeFound("createDate.in=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllStudentAttendencesByCreateDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentAttendenceRepository.saveAndFlush(studentAttendence);

        // Get all the studentAttendenceList where createDate is not null
        defaultStudentAttendenceShouldBeFound("createDate.specified=true");

        // Get all the studentAttendenceList where createDate is null
        defaultStudentAttendenceShouldNotBeFound("createDate.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentAttendencesByCreateDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        studentAttendenceRepository.saveAndFlush(studentAttendence);

        // Get all the studentAttendenceList where createDate is greater than or equal to DEFAULT_CREATE_DATE
        defaultStudentAttendenceShouldBeFound("createDate.greaterThanOrEqual=" + DEFAULT_CREATE_DATE);

        // Get all the studentAttendenceList where createDate is greater than or equal to UPDATED_CREATE_DATE
        defaultStudentAttendenceShouldNotBeFound("createDate.greaterThanOrEqual=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllStudentAttendencesByCreateDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        studentAttendenceRepository.saveAndFlush(studentAttendence);

        // Get all the studentAttendenceList where createDate is less than or equal to DEFAULT_CREATE_DATE
        defaultStudentAttendenceShouldBeFound("createDate.lessThanOrEqual=" + DEFAULT_CREATE_DATE);

        // Get all the studentAttendenceList where createDate is less than or equal to SMALLER_CREATE_DATE
        defaultStudentAttendenceShouldNotBeFound("createDate.lessThanOrEqual=" + SMALLER_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllStudentAttendencesByCreateDateIsLessThanSomething() throws Exception {
        // Initialize the database
        studentAttendenceRepository.saveAndFlush(studentAttendence);

        // Get all the studentAttendenceList where createDate is less than DEFAULT_CREATE_DATE
        defaultStudentAttendenceShouldNotBeFound("createDate.lessThan=" + DEFAULT_CREATE_DATE);

        // Get all the studentAttendenceList where createDate is less than UPDATED_CREATE_DATE
        defaultStudentAttendenceShouldBeFound("createDate.lessThan=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllStudentAttendencesByCreateDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        studentAttendenceRepository.saveAndFlush(studentAttendence);

        // Get all the studentAttendenceList where createDate is greater than DEFAULT_CREATE_DATE
        defaultStudentAttendenceShouldNotBeFound("createDate.greaterThan=" + DEFAULT_CREATE_DATE);

        // Get all the studentAttendenceList where createDate is greater than SMALLER_CREATE_DATE
        defaultStudentAttendenceShouldBeFound("createDate.greaterThan=" + SMALLER_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllStudentAttendencesByLastModifiedIsEqualToSomething() throws Exception {
        // Initialize the database
        studentAttendenceRepository.saveAndFlush(studentAttendence);

        // Get all the studentAttendenceList where lastModified equals to DEFAULT_LAST_MODIFIED
        defaultStudentAttendenceShouldBeFound("lastModified.equals=" + DEFAULT_LAST_MODIFIED);

        // Get all the studentAttendenceList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultStudentAttendenceShouldNotBeFound("lastModified.equals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllStudentAttendencesByLastModifiedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentAttendenceRepository.saveAndFlush(studentAttendence);

        // Get all the studentAttendenceList where lastModified not equals to DEFAULT_LAST_MODIFIED
        defaultStudentAttendenceShouldNotBeFound("lastModified.notEquals=" + DEFAULT_LAST_MODIFIED);

        // Get all the studentAttendenceList where lastModified not equals to UPDATED_LAST_MODIFIED
        defaultStudentAttendenceShouldBeFound("lastModified.notEquals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllStudentAttendencesByLastModifiedIsInShouldWork() throws Exception {
        // Initialize the database
        studentAttendenceRepository.saveAndFlush(studentAttendence);

        // Get all the studentAttendenceList where lastModified in DEFAULT_LAST_MODIFIED or UPDATED_LAST_MODIFIED
        defaultStudentAttendenceShouldBeFound("lastModified.in=" + DEFAULT_LAST_MODIFIED + "," + UPDATED_LAST_MODIFIED);

        // Get all the studentAttendenceList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultStudentAttendenceShouldNotBeFound("lastModified.in=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllStudentAttendencesByLastModifiedIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentAttendenceRepository.saveAndFlush(studentAttendence);

        // Get all the studentAttendenceList where lastModified is not null
        defaultStudentAttendenceShouldBeFound("lastModified.specified=true");

        // Get all the studentAttendenceList where lastModified is null
        defaultStudentAttendenceShouldNotBeFound("lastModified.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentAttendencesByLastModifiedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        studentAttendenceRepository.saveAndFlush(studentAttendence);

        // Get all the studentAttendenceList where lastModified is greater than or equal to DEFAULT_LAST_MODIFIED
        defaultStudentAttendenceShouldBeFound("lastModified.greaterThanOrEqual=" + DEFAULT_LAST_MODIFIED);

        // Get all the studentAttendenceList where lastModified is greater than or equal to UPDATED_LAST_MODIFIED
        defaultStudentAttendenceShouldNotBeFound("lastModified.greaterThanOrEqual=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllStudentAttendencesByLastModifiedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        studentAttendenceRepository.saveAndFlush(studentAttendence);

        // Get all the studentAttendenceList where lastModified is less than or equal to DEFAULT_LAST_MODIFIED
        defaultStudentAttendenceShouldBeFound("lastModified.lessThanOrEqual=" + DEFAULT_LAST_MODIFIED);

        // Get all the studentAttendenceList where lastModified is less than or equal to SMALLER_LAST_MODIFIED
        defaultStudentAttendenceShouldNotBeFound("lastModified.lessThanOrEqual=" + SMALLER_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllStudentAttendencesByLastModifiedIsLessThanSomething() throws Exception {
        // Initialize the database
        studentAttendenceRepository.saveAndFlush(studentAttendence);

        // Get all the studentAttendenceList where lastModified is less than DEFAULT_LAST_MODIFIED
        defaultStudentAttendenceShouldNotBeFound("lastModified.lessThan=" + DEFAULT_LAST_MODIFIED);

        // Get all the studentAttendenceList where lastModified is less than UPDATED_LAST_MODIFIED
        defaultStudentAttendenceShouldBeFound("lastModified.lessThan=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllStudentAttendencesByLastModifiedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        studentAttendenceRepository.saveAndFlush(studentAttendence);

        // Get all the studentAttendenceList where lastModified is greater than DEFAULT_LAST_MODIFIED
        defaultStudentAttendenceShouldNotBeFound("lastModified.greaterThan=" + DEFAULT_LAST_MODIFIED);

        // Get all the studentAttendenceList where lastModified is greater than SMALLER_LAST_MODIFIED
        defaultStudentAttendenceShouldBeFound("lastModified.greaterThan=" + SMALLER_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllStudentAttendencesByCancelDateIsEqualToSomething() throws Exception {
        // Initialize the database
        studentAttendenceRepository.saveAndFlush(studentAttendence);

        // Get all the studentAttendenceList where cancelDate equals to DEFAULT_CANCEL_DATE
        defaultStudentAttendenceShouldBeFound("cancelDate.equals=" + DEFAULT_CANCEL_DATE);

        // Get all the studentAttendenceList where cancelDate equals to UPDATED_CANCEL_DATE
        defaultStudentAttendenceShouldNotBeFound("cancelDate.equals=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllStudentAttendencesByCancelDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentAttendenceRepository.saveAndFlush(studentAttendence);

        // Get all the studentAttendenceList where cancelDate not equals to DEFAULT_CANCEL_DATE
        defaultStudentAttendenceShouldNotBeFound("cancelDate.notEquals=" + DEFAULT_CANCEL_DATE);

        // Get all the studentAttendenceList where cancelDate not equals to UPDATED_CANCEL_DATE
        defaultStudentAttendenceShouldBeFound("cancelDate.notEquals=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllStudentAttendencesByCancelDateIsInShouldWork() throws Exception {
        // Initialize the database
        studentAttendenceRepository.saveAndFlush(studentAttendence);

        // Get all the studentAttendenceList where cancelDate in DEFAULT_CANCEL_DATE or UPDATED_CANCEL_DATE
        defaultStudentAttendenceShouldBeFound("cancelDate.in=" + DEFAULT_CANCEL_DATE + "," + UPDATED_CANCEL_DATE);

        // Get all the studentAttendenceList where cancelDate equals to UPDATED_CANCEL_DATE
        defaultStudentAttendenceShouldNotBeFound("cancelDate.in=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllStudentAttendencesByCancelDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentAttendenceRepository.saveAndFlush(studentAttendence);

        // Get all the studentAttendenceList where cancelDate is not null
        defaultStudentAttendenceShouldBeFound("cancelDate.specified=true");

        // Get all the studentAttendenceList where cancelDate is null
        defaultStudentAttendenceShouldNotBeFound("cancelDate.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentAttendencesByCancelDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        studentAttendenceRepository.saveAndFlush(studentAttendence);

        // Get all the studentAttendenceList where cancelDate is greater than or equal to DEFAULT_CANCEL_DATE
        defaultStudentAttendenceShouldBeFound("cancelDate.greaterThanOrEqual=" + DEFAULT_CANCEL_DATE);

        // Get all the studentAttendenceList where cancelDate is greater than or equal to UPDATED_CANCEL_DATE
        defaultStudentAttendenceShouldNotBeFound("cancelDate.greaterThanOrEqual=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllStudentAttendencesByCancelDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        studentAttendenceRepository.saveAndFlush(studentAttendence);

        // Get all the studentAttendenceList where cancelDate is less than or equal to DEFAULT_CANCEL_DATE
        defaultStudentAttendenceShouldBeFound("cancelDate.lessThanOrEqual=" + DEFAULT_CANCEL_DATE);

        // Get all the studentAttendenceList where cancelDate is less than or equal to SMALLER_CANCEL_DATE
        defaultStudentAttendenceShouldNotBeFound("cancelDate.lessThanOrEqual=" + SMALLER_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllStudentAttendencesByCancelDateIsLessThanSomething() throws Exception {
        // Initialize the database
        studentAttendenceRepository.saveAndFlush(studentAttendence);

        // Get all the studentAttendenceList where cancelDate is less than DEFAULT_CANCEL_DATE
        defaultStudentAttendenceShouldNotBeFound("cancelDate.lessThan=" + DEFAULT_CANCEL_DATE);

        // Get all the studentAttendenceList where cancelDate is less than UPDATED_CANCEL_DATE
        defaultStudentAttendenceShouldBeFound("cancelDate.lessThan=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllStudentAttendencesByCancelDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        studentAttendenceRepository.saveAndFlush(studentAttendence);

        // Get all the studentAttendenceList where cancelDate is greater than DEFAULT_CANCEL_DATE
        defaultStudentAttendenceShouldNotBeFound("cancelDate.greaterThan=" + DEFAULT_CANCEL_DATE);

        // Get all the studentAttendenceList where cancelDate is greater than SMALLER_CANCEL_DATE
        defaultStudentAttendenceShouldBeFound("cancelDate.greaterThan=" + SMALLER_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllStudentAttendencesByClassStudentIsEqualToSomething() throws Exception {
        // Initialize the database
        studentAttendenceRepository.saveAndFlush(studentAttendence);
        ClassStudent classStudent = ClassStudentResourceIT.createEntity(em);
        em.persist(classStudent);
        em.flush();
        studentAttendence.setClassStudent(classStudent);
        studentAttendenceRepository.saveAndFlush(studentAttendence);
        Long classStudentId = classStudent.getId();

        // Get all the studentAttendenceList where classStudent equals to classStudentId
        defaultStudentAttendenceShouldBeFound("classStudentId.equals=" + classStudentId);

        // Get all the studentAttendenceList where classStudent equals to (classStudentId + 1)
        defaultStudentAttendenceShouldNotBeFound("classStudentId.equals=" + (classStudentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultStudentAttendenceShouldBeFound(String filter) throws Exception {
        restStudentAttendenceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(studentAttendence.getId().intValue())))
            .andExpect(jsonPath("$.[*].schoolDate").value(hasItem(DEFAULT_SCHOOL_DATE.toString())))
            .andExpect(jsonPath("$.[*].attendence").value(hasItem(DEFAULT_ATTENDENCE.booleanValue())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].cancelDate").value(hasItem(DEFAULT_CANCEL_DATE.toString())));

        // Check, that the count call also returns 1
        restStudentAttendenceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultStudentAttendenceShouldNotBeFound(String filter) throws Exception {
        restStudentAttendenceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restStudentAttendenceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingStudentAttendence() throws Exception {
        // Get the studentAttendence
        restStudentAttendenceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewStudentAttendence() throws Exception {
        // Initialize the database
        studentAttendenceRepository.saveAndFlush(studentAttendence);

        int databaseSizeBeforeUpdate = studentAttendenceRepository.findAll().size();

        // Update the studentAttendence
        StudentAttendence updatedStudentAttendence = studentAttendenceRepository.findById(studentAttendence.getId()).get();
        // Disconnect from session so that the updates on updatedStudentAttendence are not directly saved in db
        em.detach(updatedStudentAttendence);
        updatedStudentAttendence
            .schoolDate(UPDATED_SCHOOL_DATE)
            .attendence(UPDATED_ATTENDENCE)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);
        StudentAttendenceDTO studentAttendenceDTO = studentAttendenceMapper.toDto(updatedStudentAttendence);

        restStudentAttendenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, studentAttendenceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(studentAttendenceDTO))
            )
            .andExpect(status().isOk());

        // Validate the StudentAttendence in the database
        List<StudentAttendence> studentAttendenceList = studentAttendenceRepository.findAll();
        assertThat(studentAttendenceList).hasSize(databaseSizeBeforeUpdate);
        StudentAttendence testStudentAttendence = studentAttendenceList.get(studentAttendenceList.size() - 1);
        assertThat(testStudentAttendence.getSchoolDate()).isEqualTo(UPDATED_SCHOOL_DATE);
        assertThat(testStudentAttendence.getAttendence()).isEqualTo(UPDATED_ATTENDENCE);
        assertThat(testStudentAttendence.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testStudentAttendence.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testStudentAttendence.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void putNonExistingStudentAttendence() throws Exception {
        int databaseSizeBeforeUpdate = studentAttendenceRepository.findAll().size();
        studentAttendence.setId(count.incrementAndGet());

        // Create the StudentAttendence
        StudentAttendenceDTO studentAttendenceDTO = studentAttendenceMapper.toDto(studentAttendence);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudentAttendenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, studentAttendenceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(studentAttendenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentAttendence in the database
        List<StudentAttendence> studentAttendenceList = studentAttendenceRepository.findAll();
        assertThat(studentAttendenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStudentAttendence() throws Exception {
        int databaseSizeBeforeUpdate = studentAttendenceRepository.findAll().size();
        studentAttendence.setId(count.incrementAndGet());

        // Create the StudentAttendence
        StudentAttendenceDTO studentAttendenceDTO = studentAttendenceMapper.toDto(studentAttendence);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentAttendenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(studentAttendenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentAttendence in the database
        List<StudentAttendence> studentAttendenceList = studentAttendenceRepository.findAll();
        assertThat(studentAttendenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStudentAttendence() throws Exception {
        int databaseSizeBeforeUpdate = studentAttendenceRepository.findAll().size();
        studentAttendence.setId(count.incrementAndGet());

        // Create the StudentAttendence
        StudentAttendenceDTO studentAttendenceDTO = studentAttendenceMapper.toDto(studentAttendence);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentAttendenceMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(studentAttendenceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StudentAttendence in the database
        List<StudentAttendence> studentAttendenceList = studentAttendenceRepository.findAll();
        assertThat(studentAttendenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStudentAttendenceWithPatch() throws Exception {
        // Initialize the database
        studentAttendenceRepository.saveAndFlush(studentAttendence);

        int databaseSizeBeforeUpdate = studentAttendenceRepository.findAll().size();

        // Update the studentAttendence using partial update
        StudentAttendence partialUpdatedStudentAttendence = new StudentAttendence();
        partialUpdatedStudentAttendence.setId(studentAttendence.getId());

        partialUpdatedStudentAttendence.createDate(UPDATED_CREATE_DATE).lastModified(UPDATED_LAST_MODIFIED).cancelDate(UPDATED_CANCEL_DATE);

        restStudentAttendenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStudentAttendence.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStudentAttendence))
            )
            .andExpect(status().isOk());

        // Validate the StudentAttendence in the database
        List<StudentAttendence> studentAttendenceList = studentAttendenceRepository.findAll();
        assertThat(studentAttendenceList).hasSize(databaseSizeBeforeUpdate);
        StudentAttendence testStudentAttendence = studentAttendenceList.get(studentAttendenceList.size() - 1);
        assertThat(testStudentAttendence.getSchoolDate()).isEqualTo(DEFAULT_SCHOOL_DATE);
        assertThat(testStudentAttendence.getAttendence()).isEqualTo(DEFAULT_ATTENDENCE);
        assertThat(testStudentAttendence.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testStudentAttendence.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testStudentAttendence.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void fullUpdateStudentAttendenceWithPatch() throws Exception {
        // Initialize the database
        studentAttendenceRepository.saveAndFlush(studentAttendence);

        int databaseSizeBeforeUpdate = studentAttendenceRepository.findAll().size();

        // Update the studentAttendence using partial update
        StudentAttendence partialUpdatedStudentAttendence = new StudentAttendence();
        partialUpdatedStudentAttendence.setId(studentAttendence.getId());

        partialUpdatedStudentAttendence
            .schoolDate(UPDATED_SCHOOL_DATE)
            .attendence(UPDATED_ATTENDENCE)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);

        restStudentAttendenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStudentAttendence.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStudentAttendence))
            )
            .andExpect(status().isOk());

        // Validate the StudentAttendence in the database
        List<StudentAttendence> studentAttendenceList = studentAttendenceRepository.findAll();
        assertThat(studentAttendenceList).hasSize(databaseSizeBeforeUpdate);
        StudentAttendence testStudentAttendence = studentAttendenceList.get(studentAttendenceList.size() - 1);
        assertThat(testStudentAttendence.getSchoolDate()).isEqualTo(UPDATED_SCHOOL_DATE);
        assertThat(testStudentAttendence.getAttendence()).isEqualTo(UPDATED_ATTENDENCE);
        assertThat(testStudentAttendence.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testStudentAttendence.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testStudentAttendence.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingStudentAttendence() throws Exception {
        int databaseSizeBeforeUpdate = studentAttendenceRepository.findAll().size();
        studentAttendence.setId(count.incrementAndGet());

        // Create the StudentAttendence
        StudentAttendenceDTO studentAttendenceDTO = studentAttendenceMapper.toDto(studentAttendence);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudentAttendenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, studentAttendenceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(studentAttendenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentAttendence in the database
        List<StudentAttendence> studentAttendenceList = studentAttendenceRepository.findAll();
        assertThat(studentAttendenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStudentAttendence() throws Exception {
        int databaseSizeBeforeUpdate = studentAttendenceRepository.findAll().size();
        studentAttendence.setId(count.incrementAndGet());

        // Create the StudentAttendence
        StudentAttendenceDTO studentAttendenceDTO = studentAttendenceMapper.toDto(studentAttendence);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentAttendenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(studentAttendenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentAttendence in the database
        List<StudentAttendence> studentAttendenceList = studentAttendenceRepository.findAll();
        assertThat(studentAttendenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStudentAttendence() throws Exception {
        int databaseSizeBeforeUpdate = studentAttendenceRepository.findAll().size();
        studentAttendence.setId(count.incrementAndGet());

        // Create the StudentAttendence
        StudentAttendenceDTO studentAttendenceDTO = studentAttendenceMapper.toDto(studentAttendence);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentAttendenceMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(studentAttendenceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StudentAttendence in the database
        List<StudentAttendence> studentAttendenceList = studentAttendenceRepository.findAll();
        assertThat(studentAttendenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStudentAttendence() throws Exception {
        // Initialize the database
        studentAttendenceRepository.saveAndFlush(studentAttendence);

        int databaseSizeBeforeDelete = studentAttendenceRepository.findAll().size();

        // Delete the studentAttendence
        restStudentAttendenceMockMvc
            .perform(delete(ENTITY_API_URL_ID, studentAttendence.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<StudentAttendence> studentAttendenceList = studentAttendenceRepository.findAll();
        assertThat(studentAttendenceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
