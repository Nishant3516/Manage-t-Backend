package com.ssik.manageit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ssik.manageit.IntegrationTest;
import com.ssik.manageit.domain.SchoolClass;
import com.ssik.manageit.domain.SchoolDaysOff;
import com.ssik.manageit.domain.enumeration.DayOffType;
import com.ssik.manageit.repository.SchoolDaysOffRepository;
import com.ssik.manageit.service.SchoolDaysOffService;
import com.ssik.manageit.service.criteria.SchoolDaysOffCriteria;
import com.ssik.manageit.service.dto.SchoolDaysOffDTO;
import com.ssik.manageit.service.mapper.SchoolDaysOffMapper;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SchoolDaysOffResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SchoolDaysOffResourceIT {

    private static final DayOffType DEFAULT_DAY_OFF_TYPE = DayOffType.WEEKEND;
    private static final DayOffType UPDATED_DAY_OFF_TYPE = DayOffType.VACATION;

    private static final String DEFAULT_DAY_OFF_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DAY_OFF_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DAY_OFF_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_DAY_OFF_DETAILS = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_START_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_END_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_CREATE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATE_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CREATE_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_LAST_MODIFIED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_MODIFIED = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_LAST_MODIFIED = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_CANCEL_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CANCEL_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CANCEL_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/school-days-offs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SchoolDaysOffRepository schoolDaysOffRepository;

    @Mock
    private SchoolDaysOffRepository schoolDaysOffRepositoryMock;

    @Autowired
    private SchoolDaysOffMapper schoolDaysOffMapper;

    @Mock
    private SchoolDaysOffService schoolDaysOffServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSchoolDaysOffMockMvc;

    private SchoolDaysOff schoolDaysOff;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SchoolDaysOff createEntity(EntityManager em) {
        SchoolDaysOff schoolDaysOff = new SchoolDaysOff()
            .dayOffType(DEFAULT_DAY_OFF_TYPE)
            .dayOffName(DEFAULT_DAY_OFF_NAME)
            .dayOffDetails(DEFAULT_DAY_OFF_DETAILS)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .createDate(DEFAULT_CREATE_DATE)
            .lastModified(DEFAULT_LAST_MODIFIED)
            .cancelDate(DEFAULT_CANCEL_DATE);
        return schoolDaysOff;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SchoolDaysOff createUpdatedEntity(EntityManager em) {
        SchoolDaysOff schoolDaysOff = new SchoolDaysOff()
            .dayOffType(UPDATED_DAY_OFF_TYPE)
            .dayOffName(UPDATED_DAY_OFF_NAME)
            .dayOffDetails(UPDATED_DAY_OFF_DETAILS)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);
        return schoolDaysOff;
    }

    @BeforeEach
    public void initTest() {
        schoolDaysOff = createEntity(em);
    }

    @Test
    @Transactional
    void createSchoolDaysOff() throws Exception {
        int databaseSizeBeforeCreate = schoolDaysOffRepository.findAll().size();
        // Create the SchoolDaysOff
        SchoolDaysOffDTO schoolDaysOffDTO = schoolDaysOffMapper.toDto(schoolDaysOff);
        restSchoolDaysOffMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(schoolDaysOffDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SchoolDaysOff in the database
        List<SchoolDaysOff> schoolDaysOffList = schoolDaysOffRepository.findAll();
        assertThat(schoolDaysOffList).hasSize(databaseSizeBeforeCreate + 1);
        SchoolDaysOff testSchoolDaysOff = schoolDaysOffList.get(schoolDaysOffList.size() - 1);
        assertThat(testSchoolDaysOff.getDayOffType()).isEqualTo(DEFAULT_DAY_OFF_TYPE);
        assertThat(testSchoolDaysOff.getDayOffName()).isEqualTo(DEFAULT_DAY_OFF_NAME);
        assertThat(testSchoolDaysOff.getDayOffDetails()).isEqualTo(DEFAULT_DAY_OFF_DETAILS);
        assertThat(testSchoolDaysOff.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testSchoolDaysOff.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testSchoolDaysOff.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testSchoolDaysOff.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
        assertThat(testSchoolDaysOff.getCancelDate()).isEqualTo(DEFAULT_CANCEL_DATE);
    }

    @Test
    @Transactional
    void createSchoolDaysOffWithExistingId() throws Exception {
        // Create the SchoolDaysOff with an existing ID
        schoolDaysOff.setId(1L);
        SchoolDaysOffDTO schoolDaysOffDTO = schoolDaysOffMapper.toDto(schoolDaysOff);

        int databaseSizeBeforeCreate = schoolDaysOffRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSchoolDaysOffMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(schoolDaysOffDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchoolDaysOff in the database
        List<SchoolDaysOff> schoolDaysOffList = schoolDaysOffRepository.findAll();
        assertThat(schoolDaysOffList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDayOffTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = schoolDaysOffRepository.findAll().size();
        // set the field null
        schoolDaysOff.setDayOffType(null);

        // Create the SchoolDaysOff, which fails.
        SchoolDaysOffDTO schoolDaysOffDTO = schoolDaysOffMapper.toDto(schoolDaysOff);

        restSchoolDaysOffMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(schoolDaysOffDTO))
            )
            .andExpect(status().isBadRequest());

        List<SchoolDaysOff> schoolDaysOffList = schoolDaysOffRepository.findAll();
        assertThat(schoolDaysOffList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDayOffNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = schoolDaysOffRepository.findAll().size();
        // set the field null
        schoolDaysOff.setDayOffName(null);

        // Create the SchoolDaysOff, which fails.
        SchoolDaysOffDTO schoolDaysOffDTO = schoolDaysOffMapper.toDto(schoolDaysOff);

        restSchoolDaysOffMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(schoolDaysOffDTO))
            )
            .andExpect(status().isBadRequest());

        List<SchoolDaysOff> schoolDaysOffList = schoolDaysOffRepository.findAll();
        assertThat(schoolDaysOffList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = schoolDaysOffRepository.findAll().size();
        // set the field null
        schoolDaysOff.setStartDate(null);

        // Create the SchoolDaysOff, which fails.
        SchoolDaysOffDTO schoolDaysOffDTO = schoolDaysOffMapper.toDto(schoolDaysOff);

        restSchoolDaysOffMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(schoolDaysOffDTO))
            )
            .andExpect(status().isBadRequest());

        List<SchoolDaysOff> schoolDaysOffList = schoolDaysOffRepository.findAll();
        assertThat(schoolDaysOffList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = schoolDaysOffRepository.findAll().size();
        // set the field null
        schoolDaysOff.setEndDate(null);

        // Create the SchoolDaysOff, which fails.
        SchoolDaysOffDTO schoolDaysOffDTO = schoolDaysOffMapper.toDto(schoolDaysOff);

        restSchoolDaysOffMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(schoolDaysOffDTO))
            )
            .andExpect(status().isBadRequest());

        List<SchoolDaysOff> schoolDaysOffList = schoolDaysOffRepository.findAll();
        assertThat(schoolDaysOffList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSchoolDaysOffs() throws Exception {
        // Initialize the database
        schoolDaysOffRepository.saveAndFlush(schoolDaysOff);

        // Get all the schoolDaysOffList
        restSchoolDaysOffMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(schoolDaysOff.getId().intValue())))
            .andExpect(jsonPath("$.[*].dayOffType").value(hasItem(DEFAULT_DAY_OFF_TYPE.toString())))
            .andExpect(jsonPath("$.[*].dayOffName").value(hasItem(DEFAULT_DAY_OFF_NAME)))
            .andExpect(jsonPath("$.[*].dayOffDetails").value(hasItem(DEFAULT_DAY_OFF_DETAILS)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].cancelDate").value(hasItem(DEFAULT_CANCEL_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSchoolDaysOffsWithEagerRelationshipsIsEnabled() throws Exception {
        when(schoolDaysOffServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSchoolDaysOffMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(schoolDaysOffServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSchoolDaysOffsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(schoolDaysOffServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSchoolDaysOffMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(schoolDaysOffServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getSchoolDaysOff() throws Exception {
        // Initialize the database
        schoolDaysOffRepository.saveAndFlush(schoolDaysOff);

        // Get the schoolDaysOff
        restSchoolDaysOffMockMvc
            .perform(get(ENTITY_API_URL_ID, schoolDaysOff.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(schoolDaysOff.getId().intValue()))
            .andExpect(jsonPath("$.dayOffType").value(DEFAULT_DAY_OFF_TYPE.toString()))
            .andExpect(jsonPath("$.dayOffName").value(DEFAULT_DAY_OFF_NAME))
            .andExpect(jsonPath("$.dayOffDetails").value(DEFAULT_DAY_OFF_DETAILS))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.lastModified").value(DEFAULT_LAST_MODIFIED.toString()))
            .andExpect(jsonPath("$.cancelDate").value(DEFAULT_CANCEL_DATE.toString()));
    }

    @Test
    @Transactional
    void getSchoolDaysOffsByIdFiltering() throws Exception {
        // Initialize the database
        schoolDaysOffRepository.saveAndFlush(schoolDaysOff);

        Long id = schoolDaysOff.getId();

        defaultSchoolDaysOffShouldBeFound("id.equals=" + id);
        defaultSchoolDaysOffShouldNotBeFound("id.notEquals=" + id);

        defaultSchoolDaysOffShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSchoolDaysOffShouldNotBeFound("id.greaterThan=" + id);

        defaultSchoolDaysOffShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSchoolDaysOffShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSchoolDaysOffsByDayOffTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolDaysOffRepository.saveAndFlush(schoolDaysOff);

        // Get all the schoolDaysOffList where dayOffType equals to DEFAULT_DAY_OFF_TYPE
        defaultSchoolDaysOffShouldBeFound("dayOffType.equals=" + DEFAULT_DAY_OFF_TYPE);

        // Get all the schoolDaysOffList where dayOffType equals to UPDATED_DAY_OFF_TYPE
        defaultSchoolDaysOffShouldNotBeFound("dayOffType.equals=" + UPDATED_DAY_OFF_TYPE);
    }

    @Test
    @Transactional
    void getAllSchoolDaysOffsByDayOffTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        schoolDaysOffRepository.saveAndFlush(schoolDaysOff);

        // Get all the schoolDaysOffList where dayOffType not equals to DEFAULT_DAY_OFF_TYPE
        defaultSchoolDaysOffShouldNotBeFound("dayOffType.notEquals=" + DEFAULT_DAY_OFF_TYPE);

        // Get all the schoolDaysOffList where dayOffType not equals to UPDATED_DAY_OFF_TYPE
        defaultSchoolDaysOffShouldBeFound("dayOffType.notEquals=" + UPDATED_DAY_OFF_TYPE);
    }

    @Test
    @Transactional
    void getAllSchoolDaysOffsByDayOffTypeIsInShouldWork() throws Exception {
        // Initialize the database
        schoolDaysOffRepository.saveAndFlush(schoolDaysOff);

        // Get all the schoolDaysOffList where dayOffType in DEFAULT_DAY_OFF_TYPE or UPDATED_DAY_OFF_TYPE
        defaultSchoolDaysOffShouldBeFound("dayOffType.in=" + DEFAULT_DAY_OFF_TYPE + "," + UPDATED_DAY_OFF_TYPE);

        // Get all the schoolDaysOffList where dayOffType equals to UPDATED_DAY_OFF_TYPE
        defaultSchoolDaysOffShouldNotBeFound("dayOffType.in=" + UPDATED_DAY_OFF_TYPE);
    }

    @Test
    @Transactional
    void getAllSchoolDaysOffsByDayOffTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolDaysOffRepository.saveAndFlush(schoolDaysOff);

        // Get all the schoolDaysOffList where dayOffType is not null
        defaultSchoolDaysOffShouldBeFound("dayOffType.specified=true");

        // Get all the schoolDaysOffList where dayOffType is null
        defaultSchoolDaysOffShouldNotBeFound("dayOffType.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolDaysOffsByDayOffNameIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolDaysOffRepository.saveAndFlush(schoolDaysOff);

        // Get all the schoolDaysOffList where dayOffName equals to DEFAULT_DAY_OFF_NAME
        defaultSchoolDaysOffShouldBeFound("dayOffName.equals=" + DEFAULT_DAY_OFF_NAME);

        // Get all the schoolDaysOffList where dayOffName equals to UPDATED_DAY_OFF_NAME
        defaultSchoolDaysOffShouldNotBeFound("dayOffName.equals=" + UPDATED_DAY_OFF_NAME);
    }

    @Test
    @Transactional
    void getAllSchoolDaysOffsByDayOffNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        schoolDaysOffRepository.saveAndFlush(schoolDaysOff);

        // Get all the schoolDaysOffList where dayOffName not equals to DEFAULT_DAY_OFF_NAME
        defaultSchoolDaysOffShouldNotBeFound("dayOffName.notEquals=" + DEFAULT_DAY_OFF_NAME);

        // Get all the schoolDaysOffList where dayOffName not equals to UPDATED_DAY_OFF_NAME
        defaultSchoolDaysOffShouldBeFound("dayOffName.notEquals=" + UPDATED_DAY_OFF_NAME);
    }

    @Test
    @Transactional
    void getAllSchoolDaysOffsByDayOffNameIsInShouldWork() throws Exception {
        // Initialize the database
        schoolDaysOffRepository.saveAndFlush(schoolDaysOff);

        // Get all the schoolDaysOffList where dayOffName in DEFAULT_DAY_OFF_NAME or UPDATED_DAY_OFF_NAME
        defaultSchoolDaysOffShouldBeFound("dayOffName.in=" + DEFAULT_DAY_OFF_NAME + "," + UPDATED_DAY_OFF_NAME);

        // Get all the schoolDaysOffList where dayOffName equals to UPDATED_DAY_OFF_NAME
        defaultSchoolDaysOffShouldNotBeFound("dayOffName.in=" + UPDATED_DAY_OFF_NAME);
    }

    @Test
    @Transactional
    void getAllSchoolDaysOffsByDayOffNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolDaysOffRepository.saveAndFlush(schoolDaysOff);

        // Get all the schoolDaysOffList where dayOffName is not null
        defaultSchoolDaysOffShouldBeFound("dayOffName.specified=true");

        // Get all the schoolDaysOffList where dayOffName is null
        defaultSchoolDaysOffShouldNotBeFound("dayOffName.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolDaysOffsByDayOffNameContainsSomething() throws Exception {
        // Initialize the database
        schoolDaysOffRepository.saveAndFlush(schoolDaysOff);

        // Get all the schoolDaysOffList where dayOffName contains DEFAULT_DAY_OFF_NAME
        defaultSchoolDaysOffShouldBeFound("dayOffName.contains=" + DEFAULT_DAY_OFF_NAME);

        // Get all the schoolDaysOffList where dayOffName contains UPDATED_DAY_OFF_NAME
        defaultSchoolDaysOffShouldNotBeFound("dayOffName.contains=" + UPDATED_DAY_OFF_NAME);
    }

    @Test
    @Transactional
    void getAllSchoolDaysOffsByDayOffNameNotContainsSomething() throws Exception {
        // Initialize the database
        schoolDaysOffRepository.saveAndFlush(schoolDaysOff);

        // Get all the schoolDaysOffList where dayOffName does not contain DEFAULT_DAY_OFF_NAME
        defaultSchoolDaysOffShouldNotBeFound("dayOffName.doesNotContain=" + DEFAULT_DAY_OFF_NAME);

        // Get all the schoolDaysOffList where dayOffName does not contain UPDATED_DAY_OFF_NAME
        defaultSchoolDaysOffShouldBeFound("dayOffName.doesNotContain=" + UPDATED_DAY_OFF_NAME);
    }

    @Test
    @Transactional
    void getAllSchoolDaysOffsByDayOffDetailsIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolDaysOffRepository.saveAndFlush(schoolDaysOff);

        // Get all the schoolDaysOffList where dayOffDetails equals to DEFAULT_DAY_OFF_DETAILS
        defaultSchoolDaysOffShouldBeFound("dayOffDetails.equals=" + DEFAULT_DAY_OFF_DETAILS);

        // Get all the schoolDaysOffList where dayOffDetails equals to UPDATED_DAY_OFF_DETAILS
        defaultSchoolDaysOffShouldNotBeFound("dayOffDetails.equals=" + UPDATED_DAY_OFF_DETAILS);
    }

    @Test
    @Transactional
    void getAllSchoolDaysOffsByDayOffDetailsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        schoolDaysOffRepository.saveAndFlush(schoolDaysOff);

        // Get all the schoolDaysOffList where dayOffDetails not equals to DEFAULT_DAY_OFF_DETAILS
        defaultSchoolDaysOffShouldNotBeFound("dayOffDetails.notEquals=" + DEFAULT_DAY_OFF_DETAILS);

        // Get all the schoolDaysOffList where dayOffDetails not equals to UPDATED_DAY_OFF_DETAILS
        defaultSchoolDaysOffShouldBeFound("dayOffDetails.notEquals=" + UPDATED_DAY_OFF_DETAILS);
    }

    @Test
    @Transactional
    void getAllSchoolDaysOffsByDayOffDetailsIsInShouldWork() throws Exception {
        // Initialize the database
        schoolDaysOffRepository.saveAndFlush(schoolDaysOff);

        // Get all the schoolDaysOffList where dayOffDetails in DEFAULT_DAY_OFF_DETAILS or UPDATED_DAY_OFF_DETAILS
        defaultSchoolDaysOffShouldBeFound("dayOffDetails.in=" + DEFAULT_DAY_OFF_DETAILS + "," + UPDATED_DAY_OFF_DETAILS);

        // Get all the schoolDaysOffList where dayOffDetails equals to UPDATED_DAY_OFF_DETAILS
        defaultSchoolDaysOffShouldNotBeFound("dayOffDetails.in=" + UPDATED_DAY_OFF_DETAILS);
    }

    @Test
    @Transactional
    void getAllSchoolDaysOffsByDayOffDetailsIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolDaysOffRepository.saveAndFlush(schoolDaysOff);

        // Get all the schoolDaysOffList where dayOffDetails is not null
        defaultSchoolDaysOffShouldBeFound("dayOffDetails.specified=true");

        // Get all the schoolDaysOffList where dayOffDetails is null
        defaultSchoolDaysOffShouldNotBeFound("dayOffDetails.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolDaysOffsByDayOffDetailsContainsSomething() throws Exception {
        // Initialize the database
        schoolDaysOffRepository.saveAndFlush(schoolDaysOff);

        // Get all the schoolDaysOffList where dayOffDetails contains DEFAULT_DAY_OFF_DETAILS
        defaultSchoolDaysOffShouldBeFound("dayOffDetails.contains=" + DEFAULT_DAY_OFF_DETAILS);

        // Get all the schoolDaysOffList where dayOffDetails contains UPDATED_DAY_OFF_DETAILS
        defaultSchoolDaysOffShouldNotBeFound("dayOffDetails.contains=" + UPDATED_DAY_OFF_DETAILS);
    }

    @Test
    @Transactional
    void getAllSchoolDaysOffsByDayOffDetailsNotContainsSomething() throws Exception {
        // Initialize the database
        schoolDaysOffRepository.saveAndFlush(schoolDaysOff);

        // Get all the schoolDaysOffList where dayOffDetails does not contain DEFAULT_DAY_OFF_DETAILS
        defaultSchoolDaysOffShouldNotBeFound("dayOffDetails.doesNotContain=" + DEFAULT_DAY_OFF_DETAILS);

        // Get all the schoolDaysOffList where dayOffDetails does not contain UPDATED_DAY_OFF_DETAILS
        defaultSchoolDaysOffShouldBeFound("dayOffDetails.doesNotContain=" + UPDATED_DAY_OFF_DETAILS);
    }

    @Test
    @Transactional
    void getAllSchoolDaysOffsByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolDaysOffRepository.saveAndFlush(schoolDaysOff);

        // Get all the schoolDaysOffList where startDate equals to DEFAULT_START_DATE
        defaultSchoolDaysOffShouldBeFound("startDate.equals=" + DEFAULT_START_DATE);

        // Get all the schoolDaysOffList where startDate equals to UPDATED_START_DATE
        defaultSchoolDaysOffShouldNotBeFound("startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolDaysOffsByStartDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        schoolDaysOffRepository.saveAndFlush(schoolDaysOff);

        // Get all the schoolDaysOffList where startDate not equals to DEFAULT_START_DATE
        defaultSchoolDaysOffShouldNotBeFound("startDate.notEquals=" + DEFAULT_START_DATE);

        // Get all the schoolDaysOffList where startDate not equals to UPDATED_START_DATE
        defaultSchoolDaysOffShouldBeFound("startDate.notEquals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolDaysOffsByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        schoolDaysOffRepository.saveAndFlush(schoolDaysOff);

        // Get all the schoolDaysOffList where startDate in DEFAULT_START_DATE or UPDATED_START_DATE
        defaultSchoolDaysOffShouldBeFound("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE);

        // Get all the schoolDaysOffList where startDate equals to UPDATED_START_DATE
        defaultSchoolDaysOffShouldNotBeFound("startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolDaysOffsByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolDaysOffRepository.saveAndFlush(schoolDaysOff);

        // Get all the schoolDaysOffList where startDate is not null
        defaultSchoolDaysOffShouldBeFound("startDate.specified=true");

        // Get all the schoolDaysOffList where startDate is null
        defaultSchoolDaysOffShouldNotBeFound("startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolDaysOffsByStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        schoolDaysOffRepository.saveAndFlush(schoolDaysOff);

        // Get all the schoolDaysOffList where startDate is greater than or equal to DEFAULT_START_DATE
        defaultSchoolDaysOffShouldBeFound("startDate.greaterThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the schoolDaysOffList where startDate is greater than or equal to UPDATED_START_DATE
        defaultSchoolDaysOffShouldNotBeFound("startDate.greaterThanOrEqual=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolDaysOffsByStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        schoolDaysOffRepository.saveAndFlush(schoolDaysOff);

        // Get all the schoolDaysOffList where startDate is less than or equal to DEFAULT_START_DATE
        defaultSchoolDaysOffShouldBeFound("startDate.lessThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the schoolDaysOffList where startDate is less than or equal to SMALLER_START_DATE
        defaultSchoolDaysOffShouldNotBeFound("startDate.lessThanOrEqual=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolDaysOffsByStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        schoolDaysOffRepository.saveAndFlush(schoolDaysOff);

        // Get all the schoolDaysOffList where startDate is less than DEFAULT_START_DATE
        defaultSchoolDaysOffShouldNotBeFound("startDate.lessThan=" + DEFAULT_START_DATE);

        // Get all the schoolDaysOffList where startDate is less than UPDATED_START_DATE
        defaultSchoolDaysOffShouldBeFound("startDate.lessThan=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolDaysOffsByStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        schoolDaysOffRepository.saveAndFlush(schoolDaysOff);

        // Get all the schoolDaysOffList where startDate is greater than DEFAULT_START_DATE
        defaultSchoolDaysOffShouldNotBeFound("startDate.greaterThan=" + DEFAULT_START_DATE);

        // Get all the schoolDaysOffList where startDate is greater than SMALLER_START_DATE
        defaultSchoolDaysOffShouldBeFound("startDate.greaterThan=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolDaysOffsByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolDaysOffRepository.saveAndFlush(schoolDaysOff);

        // Get all the schoolDaysOffList where endDate equals to DEFAULT_END_DATE
        defaultSchoolDaysOffShouldBeFound("endDate.equals=" + DEFAULT_END_DATE);

        // Get all the schoolDaysOffList where endDate equals to UPDATED_END_DATE
        defaultSchoolDaysOffShouldNotBeFound("endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolDaysOffsByEndDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        schoolDaysOffRepository.saveAndFlush(schoolDaysOff);

        // Get all the schoolDaysOffList where endDate not equals to DEFAULT_END_DATE
        defaultSchoolDaysOffShouldNotBeFound("endDate.notEquals=" + DEFAULT_END_DATE);

        // Get all the schoolDaysOffList where endDate not equals to UPDATED_END_DATE
        defaultSchoolDaysOffShouldBeFound("endDate.notEquals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolDaysOffsByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        schoolDaysOffRepository.saveAndFlush(schoolDaysOff);

        // Get all the schoolDaysOffList where endDate in DEFAULT_END_DATE or UPDATED_END_DATE
        defaultSchoolDaysOffShouldBeFound("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE);

        // Get all the schoolDaysOffList where endDate equals to UPDATED_END_DATE
        defaultSchoolDaysOffShouldNotBeFound("endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolDaysOffsByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolDaysOffRepository.saveAndFlush(schoolDaysOff);

        // Get all the schoolDaysOffList where endDate is not null
        defaultSchoolDaysOffShouldBeFound("endDate.specified=true");

        // Get all the schoolDaysOffList where endDate is null
        defaultSchoolDaysOffShouldNotBeFound("endDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolDaysOffsByEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        schoolDaysOffRepository.saveAndFlush(schoolDaysOff);

        // Get all the schoolDaysOffList where endDate is greater than or equal to DEFAULT_END_DATE
        defaultSchoolDaysOffShouldBeFound("endDate.greaterThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the schoolDaysOffList where endDate is greater than or equal to UPDATED_END_DATE
        defaultSchoolDaysOffShouldNotBeFound("endDate.greaterThanOrEqual=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolDaysOffsByEndDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        schoolDaysOffRepository.saveAndFlush(schoolDaysOff);

        // Get all the schoolDaysOffList where endDate is less than or equal to DEFAULT_END_DATE
        defaultSchoolDaysOffShouldBeFound("endDate.lessThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the schoolDaysOffList where endDate is less than or equal to SMALLER_END_DATE
        defaultSchoolDaysOffShouldNotBeFound("endDate.lessThanOrEqual=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolDaysOffsByEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        schoolDaysOffRepository.saveAndFlush(schoolDaysOff);

        // Get all the schoolDaysOffList where endDate is less than DEFAULT_END_DATE
        defaultSchoolDaysOffShouldNotBeFound("endDate.lessThan=" + DEFAULT_END_DATE);

        // Get all the schoolDaysOffList where endDate is less than UPDATED_END_DATE
        defaultSchoolDaysOffShouldBeFound("endDate.lessThan=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolDaysOffsByEndDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        schoolDaysOffRepository.saveAndFlush(schoolDaysOff);

        // Get all the schoolDaysOffList where endDate is greater than DEFAULT_END_DATE
        defaultSchoolDaysOffShouldNotBeFound("endDate.greaterThan=" + DEFAULT_END_DATE);

        // Get all the schoolDaysOffList where endDate is greater than SMALLER_END_DATE
        defaultSchoolDaysOffShouldBeFound("endDate.greaterThan=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolDaysOffsByCreateDateIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolDaysOffRepository.saveAndFlush(schoolDaysOff);

        // Get all the schoolDaysOffList where createDate equals to DEFAULT_CREATE_DATE
        defaultSchoolDaysOffShouldBeFound("createDate.equals=" + DEFAULT_CREATE_DATE);

        // Get all the schoolDaysOffList where createDate equals to UPDATED_CREATE_DATE
        defaultSchoolDaysOffShouldNotBeFound("createDate.equals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolDaysOffsByCreateDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        schoolDaysOffRepository.saveAndFlush(schoolDaysOff);

        // Get all the schoolDaysOffList where createDate not equals to DEFAULT_CREATE_DATE
        defaultSchoolDaysOffShouldNotBeFound("createDate.notEquals=" + DEFAULT_CREATE_DATE);

        // Get all the schoolDaysOffList where createDate not equals to UPDATED_CREATE_DATE
        defaultSchoolDaysOffShouldBeFound("createDate.notEquals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolDaysOffsByCreateDateIsInShouldWork() throws Exception {
        // Initialize the database
        schoolDaysOffRepository.saveAndFlush(schoolDaysOff);

        // Get all the schoolDaysOffList where createDate in DEFAULT_CREATE_DATE or UPDATED_CREATE_DATE
        defaultSchoolDaysOffShouldBeFound("createDate.in=" + DEFAULT_CREATE_DATE + "," + UPDATED_CREATE_DATE);

        // Get all the schoolDaysOffList where createDate equals to UPDATED_CREATE_DATE
        defaultSchoolDaysOffShouldNotBeFound("createDate.in=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolDaysOffsByCreateDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolDaysOffRepository.saveAndFlush(schoolDaysOff);

        // Get all the schoolDaysOffList where createDate is not null
        defaultSchoolDaysOffShouldBeFound("createDate.specified=true");

        // Get all the schoolDaysOffList where createDate is null
        defaultSchoolDaysOffShouldNotBeFound("createDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolDaysOffsByCreateDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        schoolDaysOffRepository.saveAndFlush(schoolDaysOff);

        // Get all the schoolDaysOffList where createDate is greater than or equal to DEFAULT_CREATE_DATE
        defaultSchoolDaysOffShouldBeFound("createDate.greaterThanOrEqual=" + DEFAULT_CREATE_DATE);

        // Get all the schoolDaysOffList where createDate is greater than or equal to UPDATED_CREATE_DATE
        defaultSchoolDaysOffShouldNotBeFound("createDate.greaterThanOrEqual=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolDaysOffsByCreateDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        schoolDaysOffRepository.saveAndFlush(schoolDaysOff);

        // Get all the schoolDaysOffList where createDate is less than or equal to DEFAULT_CREATE_DATE
        defaultSchoolDaysOffShouldBeFound("createDate.lessThanOrEqual=" + DEFAULT_CREATE_DATE);

        // Get all the schoolDaysOffList where createDate is less than or equal to SMALLER_CREATE_DATE
        defaultSchoolDaysOffShouldNotBeFound("createDate.lessThanOrEqual=" + SMALLER_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolDaysOffsByCreateDateIsLessThanSomething() throws Exception {
        // Initialize the database
        schoolDaysOffRepository.saveAndFlush(schoolDaysOff);

        // Get all the schoolDaysOffList where createDate is less than DEFAULT_CREATE_DATE
        defaultSchoolDaysOffShouldNotBeFound("createDate.lessThan=" + DEFAULT_CREATE_DATE);

        // Get all the schoolDaysOffList where createDate is less than UPDATED_CREATE_DATE
        defaultSchoolDaysOffShouldBeFound("createDate.lessThan=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolDaysOffsByCreateDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        schoolDaysOffRepository.saveAndFlush(schoolDaysOff);

        // Get all the schoolDaysOffList where createDate is greater than DEFAULT_CREATE_DATE
        defaultSchoolDaysOffShouldNotBeFound("createDate.greaterThan=" + DEFAULT_CREATE_DATE);

        // Get all the schoolDaysOffList where createDate is greater than SMALLER_CREATE_DATE
        defaultSchoolDaysOffShouldBeFound("createDate.greaterThan=" + SMALLER_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolDaysOffsByLastModifiedIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolDaysOffRepository.saveAndFlush(schoolDaysOff);

        // Get all the schoolDaysOffList where lastModified equals to DEFAULT_LAST_MODIFIED
        defaultSchoolDaysOffShouldBeFound("lastModified.equals=" + DEFAULT_LAST_MODIFIED);

        // Get all the schoolDaysOffList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultSchoolDaysOffShouldNotBeFound("lastModified.equals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSchoolDaysOffsByLastModifiedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        schoolDaysOffRepository.saveAndFlush(schoolDaysOff);

        // Get all the schoolDaysOffList where lastModified not equals to DEFAULT_LAST_MODIFIED
        defaultSchoolDaysOffShouldNotBeFound("lastModified.notEquals=" + DEFAULT_LAST_MODIFIED);

        // Get all the schoolDaysOffList where lastModified not equals to UPDATED_LAST_MODIFIED
        defaultSchoolDaysOffShouldBeFound("lastModified.notEquals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSchoolDaysOffsByLastModifiedIsInShouldWork() throws Exception {
        // Initialize the database
        schoolDaysOffRepository.saveAndFlush(schoolDaysOff);

        // Get all the schoolDaysOffList where lastModified in DEFAULT_LAST_MODIFIED or UPDATED_LAST_MODIFIED
        defaultSchoolDaysOffShouldBeFound("lastModified.in=" + DEFAULT_LAST_MODIFIED + "," + UPDATED_LAST_MODIFIED);

        // Get all the schoolDaysOffList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultSchoolDaysOffShouldNotBeFound("lastModified.in=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSchoolDaysOffsByLastModifiedIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolDaysOffRepository.saveAndFlush(schoolDaysOff);

        // Get all the schoolDaysOffList where lastModified is not null
        defaultSchoolDaysOffShouldBeFound("lastModified.specified=true");

        // Get all the schoolDaysOffList where lastModified is null
        defaultSchoolDaysOffShouldNotBeFound("lastModified.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolDaysOffsByLastModifiedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        schoolDaysOffRepository.saveAndFlush(schoolDaysOff);

        // Get all the schoolDaysOffList where lastModified is greater than or equal to DEFAULT_LAST_MODIFIED
        defaultSchoolDaysOffShouldBeFound("lastModified.greaterThanOrEqual=" + DEFAULT_LAST_MODIFIED);

        // Get all the schoolDaysOffList where lastModified is greater than or equal to UPDATED_LAST_MODIFIED
        defaultSchoolDaysOffShouldNotBeFound("lastModified.greaterThanOrEqual=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSchoolDaysOffsByLastModifiedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        schoolDaysOffRepository.saveAndFlush(schoolDaysOff);

        // Get all the schoolDaysOffList where lastModified is less than or equal to DEFAULT_LAST_MODIFIED
        defaultSchoolDaysOffShouldBeFound("lastModified.lessThanOrEqual=" + DEFAULT_LAST_MODIFIED);

        // Get all the schoolDaysOffList where lastModified is less than or equal to SMALLER_LAST_MODIFIED
        defaultSchoolDaysOffShouldNotBeFound("lastModified.lessThanOrEqual=" + SMALLER_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSchoolDaysOffsByLastModifiedIsLessThanSomething() throws Exception {
        // Initialize the database
        schoolDaysOffRepository.saveAndFlush(schoolDaysOff);

        // Get all the schoolDaysOffList where lastModified is less than DEFAULT_LAST_MODIFIED
        defaultSchoolDaysOffShouldNotBeFound("lastModified.lessThan=" + DEFAULT_LAST_MODIFIED);

        // Get all the schoolDaysOffList where lastModified is less than UPDATED_LAST_MODIFIED
        defaultSchoolDaysOffShouldBeFound("lastModified.lessThan=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSchoolDaysOffsByLastModifiedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        schoolDaysOffRepository.saveAndFlush(schoolDaysOff);

        // Get all the schoolDaysOffList where lastModified is greater than DEFAULT_LAST_MODIFIED
        defaultSchoolDaysOffShouldNotBeFound("lastModified.greaterThan=" + DEFAULT_LAST_MODIFIED);

        // Get all the schoolDaysOffList where lastModified is greater than SMALLER_LAST_MODIFIED
        defaultSchoolDaysOffShouldBeFound("lastModified.greaterThan=" + SMALLER_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSchoolDaysOffsByCancelDateIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolDaysOffRepository.saveAndFlush(schoolDaysOff);

        // Get all the schoolDaysOffList where cancelDate equals to DEFAULT_CANCEL_DATE
        defaultSchoolDaysOffShouldBeFound("cancelDate.equals=" + DEFAULT_CANCEL_DATE);

        // Get all the schoolDaysOffList where cancelDate equals to UPDATED_CANCEL_DATE
        defaultSchoolDaysOffShouldNotBeFound("cancelDate.equals=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolDaysOffsByCancelDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        schoolDaysOffRepository.saveAndFlush(schoolDaysOff);

        // Get all the schoolDaysOffList where cancelDate not equals to DEFAULT_CANCEL_DATE
        defaultSchoolDaysOffShouldNotBeFound("cancelDate.notEquals=" + DEFAULT_CANCEL_DATE);

        // Get all the schoolDaysOffList where cancelDate not equals to UPDATED_CANCEL_DATE
        defaultSchoolDaysOffShouldBeFound("cancelDate.notEquals=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolDaysOffsByCancelDateIsInShouldWork() throws Exception {
        // Initialize the database
        schoolDaysOffRepository.saveAndFlush(schoolDaysOff);

        // Get all the schoolDaysOffList where cancelDate in DEFAULT_CANCEL_DATE or UPDATED_CANCEL_DATE
        defaultSchoolDaysOffShouldBeFound("cancelDate.in=" + DEFAULT_CANCEL_DATE + "," + UPDATED_CANCEL_DATE);

        // Get all the schoolDaysOffList where cancelDate equals to UPDATED_CANCEL_DATE
        defaultSchoolDaysOffShouldNotBeFound("cancelDate.in=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolDaysOffsByCancelDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolDaysOffRepository.saveAndFlush(schoolDaysOff);

        // Get all the schoolDaysOffList where cancelDate is not null
        defaultSchoolDaysOffShouldBeFound("cancelDate.specified=true");

        // Get all the schoolDaysOffList where cancelDate is null
        defaultSchoolDaysOffShouldNotBeFound("cancelDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolDaysOffsByCancelDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        schoolDaysOffRepository.saveAndFlush(schoolDaysOff);

        // Get all the schoolDaysOffList where cancelDate is greater than or equal to DEFAULT_CANCEL_DATE
        defaultSchoolDaysOffShouldBeFound("cancelDate.greaterThanOrEqual=" + DEFAULT_CANCEL_DATE);

        // Get all the schoolDaysOffList where cancelDate is greater than or equal to UPDATED_CANCEL_DATE
        defaultSchoolDaysOffShouldNotBeFound("cancelDate.greaterThanOrEqual=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolDaysOffsByCancelDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        schoolDaysOffRepository.saveAndFlush(schoolDaysOff);

        // Get all the schoolDaysOffList where cancelDate is less than or equal to DEFAULT_CANCEL_DATE
        defaultSchoolDaysOffShouldBeFound("cancelDate.lessThanOrEqual=" + DEFAULT_CANCEL_DATE);

        // Get all the schoolDaysOffList where cancelDate is less than or equal to SMALLER_CANCEL_DATE
        defaultSchoolDaysOffShouldNotBeFound("cancelDate.lessThanOrEqual=" + SMALLER_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolDaysOffsByCancelDateIsLessThanSomething() throws Exception {
        // Initialize the database
        schoolDaysOffRepository.saveAndFlush(schoolDaysOff);

        // Get all the schoolDaysOffList where cancelDate is less than DEFAULT_CANCEL_DATE
        defaultSchoolDaysOffShouldNotBeFound("cancelDate.lessThan=" + DEFAULT_CANCEL_DATE);

        // Get all the schoolDaysOffList where cancelDate is less than UPDATED_CANCEL_DATE
        defaultSchoolDaysOffShouldBeFound("cancelDate.lessThan=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolDaysOffsByCancelDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        schoolDaysOffRepository.saveAndFlush(schoolDaysOff);

        // Get all the schoolDaysOffList where cancelDate is greater than DEFAULT_CANCEL_DATE
        defaultSchoolDaysOffShouldNotBeFound("cancelDate.greaterThan=" + DEFAULT_CANCEL_DATE);

        // Get all the schoolDaysOffList where cancelDate is greater than SMALLER_CANCEL_DATE
        defaultSchoolDaysOffShouldBeFound("cancelDate.greaterThan=" + SMALLER_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolDaysOffsBySchoolClassIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolDaysOffRepository.saveAndFlush(schoolDaysOff);
        SchoolClass schoolClass = SchoolClassResourceIT.createEntity(em);
        em.persist(schoolClass);
        em.flush();
        schoolDaysOff.addSchoolClass(schoolClass);
        schoolDaysOffRepository.saveAndFlush(schoolDaysOff);
        Long schoolClassId = schoolClass.getId();

        // Get all the schoolDaysOffList where schoolClass equals to schoolClassId
        defaultSchoolDaysOffShouldBeFound("schoolClassId.equals=" + schoolClassId);

        // Get all the schoolDaysOffList where schoolClass equals to (schoolClassId + 1)
        defaultSchoolDaysOffShouldNotBeFound("schoolClassId.equals=" + (schoolClassId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSchoolDaysOffShouldBeFound(String filter) throws Exception {
        restSchoolDaysOffMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(schoolDaysOff.getId().intValue())))
            .andExpect(jsonPath("$.[*].dayOffType").value(hasItem(DEFAULT_DAY_OFF_TYPE.toString())))
            .andExpect(jsonPath("$.[*].dayOffName").value(hasItem(DEFAULT_DAY_OFF_NAME)))
            .andExpect(jsonPath("$.[*].dayOffDetails").value(hasItem(DEFAULT_DAY_OFF_DETAILS)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].cancelDate").value(hasItem(DEFAULT_CANCEL_DATE.toString())));

        // Check, that the count call also returns 1
        restSchoolDaysOffMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSchoolDaysOffShouldNotBeFound(String filter) throws Exception {
        restSchoolDaysOffMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSchoolDaysOffMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSchoolDaysOff() throws Exception {
        // Get the schoolDaysOff
        restSchoolDaysOffMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSchoolDaysOff() throws Exception {
        // Initialize the database
        schoolDaysOffRepository.saveAndFlush(schoolDaysOff);

        int databaseSizeBeforeUpdate = schoolDaysOffRepository.findAll().size();

        // Update the schoolDaysOff
        SchoolDaysOff updatedSchoolDaysOff = schoolDaysOffRepository.findById(schoolDaysOff.getId()).get();
        // Disconnect from session so that the updates on updatedSchoolDaysOff are not directly saved in db
        em.detach(updatedSchoolDaysOff);
        updatedSchoolDaysOff
            .dayOffType(UPDATED_DAY_OFF_TYPE)
            .dayOffName(UPDATED_DAY_OFF_NAME)
            .dayOffDetails(UPDATED_DAY_OFF_DETAILS)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);
        SchoolDaysOffDTO schoolDaysOffDTO = schoolDaysOffMapper.toDto(updatedSchoolDaysOff);

        restSchoolDaysOffMockMvc
            .perform(
                put(ENTITY_API_URL_ID, schoolDaysOffDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schoolDaysOffDTO))
            )
            .andExpect(status().isOk());

        // Validate the SchoolDaysOff in the database
        List<SchoolDaysOff> schoolDaysOffList = schoolDaysOffRepository.findAll();
        assertThat(schoolDaysOffList).hasSize(databaseSizeBeforeUpdate);
        SchoolDaysOff testSchoolDaysOff = schoolDaysOffList.get(schoolDaysOffList.size() - 1);
        assertThat(testSchoolDaysOff.getDayOffType()).isEqualTo(UPDATED_DAY_OFF_TYPE);
        assertThat(testSchoolDaysOff.getDayOffName()).isEqualTo(UPDATED_DAY_OFF_NAME);
        assertThat(testSchoolDaysOff.getDayOffDetails()).isEqualTo(UPDATED_DAY_OFF_DETAILS);
        assertThat(testSchoolDaysOff.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testSchoolDaysOff.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testSchoolDaysOff.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testSchoolDaysOff.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testSchoolDaysOff.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void putNonExistingSchoolDaysOff() throws Exception {
        int databaseSizeBeforeUpdate = schoolDaysOffRepository.findAll().size();
        schoolDaysOff.setId(count.incrementAndGet());

        // Create the SchoolDaysOff
        SchoolDaysOffDTO schoolDaysOffDTO = schoolDaysOffMapper.toDto(schoolDaysOff);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSchoolDaysOffMockMvc
            .perform(
                put(ENTITY_API_URL_ID, schoolDaysOffDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schoolDaysOffDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchoolDaysOff in the database
        List<SchoolDaysOff> schoolDaysOffList = schoolDaysOffRepository.findAll();
        assertThat(schoolDaysOffList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSchoolDaysOff() throws Exception {
        int databaseSizeBeforeUpdate = schoolDaysOffRepository.findAll().size();
        schoolDaysOff.setId(count.incrementAndGet());

        // Create the SchoolDaysOff
        SchoolDaysOffDTO schoolDaysOffDTO = schoolDaysOffMapper.toDto(schoolDaysOff);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchoolDaysOffMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schoolDaysOffDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchoolDaysOff in the database
        List<SchoolDaysOff> schoolDaysOffList = schoolDaysOffRepository.findAll();
        assertThat(schoolDaysOffList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSchoolDaysOff() throws Exception {
        int databaseSizeBeforeUpdate = schoolDaysOffRepository.findAll().size();
        schoolDaysOff.setId(count.incrementAndGet());

        // Create the SchoolDaysOff
        SchoolDaysOffDTO schoolDaysOffDTO = schoolDaysOffMapper.toDto(schoolDaysOff);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchoolDaysOffMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(schoolDaysOffDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SchoolDaysOff in the database
        List<SchoolDaysOff> schoolDaysOffList = schoolDaysOffRepository.findAll();
        assertThat(schoolDaysOffList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSchoolDaysOffWithPatch() throws Exception {
        // Initialize the database
        schoolDaysOffRepository.saveAndFlush(schoolDaysOff);

        int databaseSizeBeforeUpdate = schoolDaysOffRepository.findAll().size();

        // Update the schoolDaysOff using partial update
        SchoolDaysOff partialUpdatedSchoolDaysOff = new SchoolDaysOff();
        partialUpdatedSchoolDaysOff.setId(schoolDaysOff.getId());

        partialUpdatedSchoolDaysOff
            .dayOffName(UPDATED_DAY_OFF_NAME)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);

        restSchoolDaysOffMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSchoolDaysOff.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSchoolDaysOff))
            )
            .andExpect(status().isOk());

        // Validate the SchoolDaysOff in the database
        List<SchoolDaysOff> schoolDaysOffList = schoolDaysOffRepository.findAll();
        assertThat(schoolDaysOffList).hasSize(databaseSizeBeforeUpdate);
        SchoolDaysOff testSchoolDaysOff = schoolDaysOffList.get(schoolDaysOffList.size() - 1);
        assertThat(testSchoolDaysOff.getDayOffType()).isEqualTo(DEFAULT_DAY_OFF_TYPE);
        assertThat(testSchoolDaysOff.getDayOffName()).isEqualTo(UPDATED_DAY_OFF_NAME);
        assertThat(testSchoolDaysOff.getDayOffDetails()).isEqualTo(DEFAULT_DAY_OFF_DETAILS);
        assertThat(testSchoolDaysOff.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testSchoolDaysOff.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testSchoolDaysOff.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testSchoolDaysOff.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testSchoolDaysOff.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void fullUpdateSchoolDaysOffWithPatch() throws Exception {
        // Initialize the database
        schoolDaysOffRepository.saveAndFlush(schoolDaysOff);

        int databaseSizeBeforeUpdate = schoolDaysOffRepository.findAll().size();

        // Update the schoolDaysOff using partial update
        SchoolDaysOff partialUpdatedSchoolDaysOff = new SchoolDaysOff();
        partialUpdatedSchoolDaysOff.setId(schoolDaysOff.getId());

        partialUpdatedSchoolDaysOff
            .dayOffType(UPDATED_DAY_OFF_TYPE)
            .dayOffName(UPDATED_DAY_OFF_NAME)
            .dayOffDetails(UPDATED_DAY_OFF_DETAILS)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);

        restSchoolDaysOffMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSchoolDaysOff.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSchoolDaysOff))
            )
            .andExpect(status().isOk());

        // Validate the SchoolDaysOff in the database
        List<SchoolDaysOff> schoolDaysOffList = schoolDaysOffRepository.findAll();
        assertThat(schoolDaysOffList).hasSize(databaseSizeBeforeUpdate);
        SchoolDaysOff testSchoolDaysOff = schoolDaysOffList.get(schoolDaysOffList.size() - 1);
        assertThat(testSchoolDaysOff.getDayOffType()).isEqualTo(UPDATED_DAY_OFF_TYPE);
        assertThat(testSchoolDaysOff.getDayOffName()).isEqualTo(UPDATED_DAY_OFF_NAME);
        assertThat(testSchoolDaysOff.getDayOffDetails()).isEqualTo(UPDATED_DAY_OFF_DETAILS);
        assertThat(testSchoolDaysOff.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testSchoolDaysOff.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testSchoolDaysOff.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testSchoolDaysOff.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testSchoolDaysOff.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingSchoolDaysOff() throws Exception {
        int databaseSizeBeforeUpdate = schoolDaysOffRepository.findAll().size();
        schoolDaysOff.setId(count.incrementAndGet());

        // Create the SchoolDaysOff
        SchoolDaysOffDTO schoolDaysOffDTO = schoolDaysOffMapper.toDto(schoolDaysOff);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSchoolDaysOffMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, schoolDaysOffDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(schoolDaysOffDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchoolDaysOff in the database
        List<SchoolDaysOff> schoolDaysOffList = schoolDaysOffRepository.findAll();
        assertThat(schoolDaysOffList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSchoolDaysOff() throws Exception {
        int databaseSizeBeforeUpdate = schoolDaysOffRepository.findAll().size();
        schoolDaysOff.setId(count.incrementAndGet());

        // Create the SchoolDaysOff
        SchoolDaysOffDTO schoolDaysOffDTO = schoolDaysOffMapper.toDto(schoolDaysOff);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchoolDaysOffMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(schoolDaysOffDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchoolDaysOff in the database
        List<SchoolDaysOff> schoolDaysOffList = schoolDaysOffRepository.findAll();
        assertThat(schoolDaysOffList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSchoolDaysOff() throws Exception {
        int databaseSizeBeforeUpdate = schoolDaysOffRepository.findAll().size();
        schoolDaysOff.setId(count.incrementAndGet());

        // Create the SchoolDaysOff
        SchoolDaysOffDTO schoolDaysOffDTO = schoolDaysOffMapper.toDto(schoolDaysOff);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchoolDaysOffMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(schoolDaysOffDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SchoolDaysOff in the database
        List<SchoolDaysOff> schoolDaysOffList = schoolDaysOffRepository.findAll();
        assertThat(schoolDaysOffList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSchoolDaysOff() throws Exception {
        // Initialize the database
        schoolDaysOffRepository.saveAndFlush(schoolDaysOff);

        int databaseSizeBeforeDelete = schoolDaysOffRepository.findAll().size();

        // Delete the schoolDaysOff
        restSchoolDaysOffMockMvc
            .perform(delete(ENTITY_API_URL_ID, schoolDaysOff.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SchoolDaysOff> schoolDaysOffList = schoolDaysOffRepository.findAll();
        assertThat(schoolDaysOffList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
