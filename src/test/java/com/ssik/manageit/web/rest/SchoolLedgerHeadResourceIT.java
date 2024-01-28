package com.ssik.manageit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ssik.manageit.IntegrationTest;
import com.ssik.manageit.domain.ClassFee;
import com.ssik.manageit.domain.School;
import com.ssik.manageit.domain.SchoolLedgerHead;
import com.ssik.manageit.domain.StudentAdditionalCharges;
import com.ssik.manageit.domain.StudentChargesSummary;
import com.ssik.manageit.domain.StudentDiscount;
import com.ssik.manageit.domain.enumeration.SchoolLedgerHeadType;
import com.ssik.manageit.repository.SchoolLedgerHeadRepository;
import com.ssik.manageit.service.criteria.SchoolLedgerHeadCriteria;
import com.ssik.manageit.service.dto.SchoolLedgerHeadDTO;
import com.ssik.manageit.service.mapper.SchoolLedgerHeadMapper;
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
 * Integration tests for the {@link SchoolLedgerHeadResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SchoolLedgerHeadResourceIT {

    private static final SchoolLedgerHeadType DEFAULT_STUDENT_LEDGER_HEAD_TYPE = SchoolLedgerHeadType.FEE;
    private static final SchoolLedgerHeadType UPDATED_STUDENT_LEDGER_HEAD_TYPE = SchoolLedgerHeadType.ADMITION;

    private static final String DEFAULT_LEDGER_HEAD_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LEDGER_HEAD_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LEDGER_HEAD_LONG_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LEDGER_HEAD_LONG_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATE_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CREATE_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_LAST_MODIFIED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_MODIFIED = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_LAST_MODIFIED = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_CANCEL_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CANCEL_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CANCEL_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/school-ledger-heads";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SchoolLedgerHeadRepository schoolLedgerHeadRepository;

    @Autowired
    private SchoolLedgerHeadMapper schoolLedgerHeadMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSchoolLedgerHeadMockMvc;

    private SchoolLedgerHead schoolLedgerHead;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SchoolLedgerHead createEntity(EntityManager em) {
        SchoolLedgerHead schoolLedgerHead = new SchoolLedgerHead()
            .studentLedgerHeadType(DEFAULT_STUDENT_LEDGER_HEAD_TYPE)
            .ledgerHeadName(DEFAULT_LEDGER_HEAD_NAME)
            .ledgerHeadLongName(DEFAULT_LEDGER_HEAD_LONG_NAME)
            .createDate(DEFAULT_CREATE_DATE)
            .lastModified(DEFAULT_LAST_MODIFIED)
            .cancelDate(DEFAULT_CANCEL_DATE);
        return schoolLedgerHead;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SchoolLedgerHead createUpdatedEntity(EntityManager em) {
        SchoolLedgerHead schoolLedgerHead = new SchoolLedgerHead()
            .studentLedgerHeadType(UPDATED_STUDENT_LEDGER_HEAD_TYPE)
            .ledgerHeadName(UPDATED_LEDGER_HEAD_NAME)
            .ledgerHeadLongName(UPDATED_LEDGER_HEAD_LONG_NAME)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);
        return schoolLedgerHead;
    }

    @BeforeEach
    public void initTest() {
        schoolLedgerHead = createEntity(em);
    }

    @Test
    @Transactional
    void createSchoolLedgerHead() throws Exception {
        int databaseSizeBeforeCreate = schoolLedgerHeadRepository.findAll().size();
        // Create the SchoolLedgerHead
        SchoolLedgerHeadDTO schoolLedgerHeadDTO = schoolLedgerHeadMapper.toDto(schoolLedgerHead);
        restSchoolLedgerHeadMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(schoolLedgerHeadDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SchoolLedgerHead in the database
        List<SchoolLedgerHead> schoolLedgerHeadList = schoolLedgerHeadRepository.findAll();
        assertThat(schoolLedgerHeadList).hasSize(databaseSizeBeforeCreate + 1);
        SchoolLedgerHead testSchoolLedgerHead = schoolLedgerHeadList.get(schoolLedgerHeadList.size() - 1);
        assertThat(testSchoolLedgerHead.getStudentLedgerHeadType()).isEqualTo(DEFAULT_STUDENT_LEDGER_HEAD_TYPE);
        assertThat(testSchoolLedgerHead.getLedgerHeadName()).isEqualTo(DEFAULT_LEDGER_HEAD_NAME);
        assertThat(testSchoolLedgerHead.getLedgerHeadLongName()).isEqualTo(DEFAULT_LEDGER_HEAD_LONG_NAME);
        assertThat(testSchoolLedgerHead.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testSchoolLedgerHead.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
        assertThat(testSchoolLedgerHead.getCancelDate()).isEqualTo(DEFAULT_CANCEL_DATE);
    }

    @Test
    @Transactional
    void createSchoolLedgerHeadWithExistingId() throws Exception {
        // Create the SchoolLedgerHead with an existing ID
        schoolLedgerHead.setId(1L);
        SchoolLedgerHeadDTO schoolLedgerHeadDTO = schoolLedgerHeadMapper.toDto(schoolLedgerHead);

        int databaseSizeBeforeCreate = schoolLedgerHeadRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSchoolLedgerHeadMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(schoolLedgerHeadDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchoolLedgerHead in the database
        List<SchoolLedgerHead> schoolLedgerHeadList = schoolLedgerHeadRepository.findAll();
        assertThat(schoolLedgerHeadList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStudentLedgerHeadTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = schoolLedgerHeadRepository.findAll().size();
        // set the field null
        schoolLedgerHead.setStudentLedgerHeadType(null);

        // Create the SchoolLedgerHead, which fails.
        SchoolLedgerHeadDTO schoolLedgerHeadDTO = schoolLedgerHeadMapper.toDto(schoolLedgerHead);

        restSchoolLedgerHeadMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(schoolLedgerHeadDTO))
            )
            .andExpect(status().isBadRequest());

        List<SchoolLedgerHead> schoolLedgerHeadList = schoolLedgerHeadRepository.findAll();
        assertThat(schoolLedgerHeadList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLedgerHeadNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = schoolLedgerHeadRepository.findAll().size();
        // set the field null
        schoolLedgerHead.setLedgerHeadName(null);

        // Create the SchoolLedgerHead, which fails.
        SchoolLedgerHeadDTO schoolLedgerHeadDTO = schoolLedgerHeadMapper.toDto(schoolLedgerHead);

        restSchoolLedgerHeadMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(schoolLedgerHeadDTO))
            )
            .andExpect(status().isBadRequest());

        List<SchoolLedgerHead> schoolLedgerHeadList = schoolLedgerHeadRepository.findAll();
        assertThat(schoolLedgerHeadList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSchoolLedgerHeads() throws Exception {
        // Initialize the database
        schoolLedgerHeadRepository.saveAndFlush(schoolLedgerHead);

        // Get all the schoolLedgerHeadList
        restSchoolLedgerHeadMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(schoolLedgerHead.getId().intValue())))
            .andExpect(jsonPath("$.[*].studentLedgerHeadType").value(hasItem(DEFAULT_STUDENT_LEDGER_HEAD_TYPE.toString())))
            .andExpect(jsonPath("$.[*].ledgerHeadName").value(hasItem(DEFAULT_LEDGER_HEAD_NAME)))
            .andExpect(jsonPath("$.[*].ledgerHeadLongName").value(hasItem(DEFAULT_LEDGER_HEAD_LONG_NAME)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].cancelDate").value(hasItem(DEFAULT_CANCEL_DATE.toString())));
    }

    @Test
    @Transactional
    void getSchoolLedgerHead() throws Exception {
        // Initialize the database
        schoolLedgerHeadRepository.saveAndFlush(schoolLedgerHead);

        // Get the schoolLedgerHead
        restSchoolLedgerHeadMockMvc
            .perform(get(ENTITY_API_URL_ID, schoolLedgerHead.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(schoolLedgerHead.getId().intValue()))
            .andExpect(jsonPath("$.studentLedgerHeadType").value(DEFAULT_STUDENT_LEDGER_HEAD_TYPE.toString()))
            .andExpect(jsonPath("$.ledgerHeadName").value(DEFAULT_LEDGER_HEAD_NAME))
            .andExpect(jsonPath("$.ledgerHeadLongName").value(DEFAULT_LEDGER_HEAD_LONG_NAME))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.lastModified").value(DEFAULT_LAST_MODIFIED.toString()))
            .andExpect(jsonPath("$.cancelDate").value(DEFAULT_CANCEL_DATE.toString()));
    }

    @Test
    @Transactional
    void getSchoolLedgerHeadsByIdFiltering() throws Exception {
        // Initialize the database
        schoolLedgerHeadRepository.saveAndFlush(schoolLedgerHead);

        Long id = schoolLedgerHead.getId();

        defaultSchoolLedgerHeadShouldBeFound("id.equals=" + id);
        defaultSchoolLedgerHeadShouldNotBeFound("id.notEquals=" + id);

        defaultSchoolLedgerHeadShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSchoolLedgerHeadShouldNotBeFound("id.greaterThan=" + id);

        defaultSchoolLedgerHeadShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSchoolLedgerHeadShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSchoolLedgerHeadsByStudentLedgerHeadTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolLedgerHeadRepository.saveAndFlush(schoolLedgerHead);

        // Get all the schoolLedgerHeadList where studentLedgerHeadType equals to DEFAULT_STUDENT_LEDGER_HEAD_TYPE
        defaultSchoolLedgerHeadShouldBeFound("studentLedgerHeadType.equals=" + DEFAULT_STUDENT_LEDGER_HEAD_TYPE);

        // Get all the schoolLedgerHeadList where studentLedgerHeadType equals to UPDATED_STUDENT_LEDGER_HEAD_TYPE
        defaultSchoolLedgerHeadShouldNotBeFound("studentLedgerHeadType.equals=" + UPDATED_STUDENT_LEDGER_HEAD_TYPE);
    }

    @Test
    @Transactional
    void getAllSchoolLedgerHeadsByStudentLedgerHeadTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        schoolLedgerHeadRepository.saveAndFlush(schoolLedgerHead);

        // Get all the schoolLedgerHeadList where studentLedgerHeadType not equals to DEFAULT_STUDENT_LEDGER_HEAD_TYPE
        defaultSchoolLedgerHeadShouldNotBeFound("studentLedgerHeadType.notEquals=" + DEFAULT_STUDENT_LEDGER_HEAD_TYPE);

        // Get all the schoolLedgerHeadList where studentLedgerHeadType not equals to UPDATED_STUDENT_LEDGER_HEAD_TYPE
        defaultSchoolLedgerHeadShouldBeFound("studentLedgerHeadType.notEquals=" + UPDATED_STUDENT_LEDGER_HEAD_TYPE);
    }

    @Test
    @Transactional
    void getAllSchoolLedgerHeadsByStudentLedgerHeadTypeIsInShouldWork() throws Exception {
        // Initialize the database
        schoolLedgerHeadRepository.saveAndFlush(schoolLedgerHead);

        // Get all the schoolLedgerHeadList where studentLedgerHeadType in DEFAULT_STUDENT_LEDGER_HEAD_TYPE or UPDATED_STUDENT_LEDGER_HEAD_TYPE
        defaultSchoolLedgerHeadShouldBeFound(
            "studentLedgerHeadType.in=" + DEFAULT_STUDENT_LEDGER_HEAD_TYPE + "," + UPDATED_STUDENT_LEDGER_HEAD_TYPE
        );

        // Get all the schoolLedgerHeadList where studentLedgerHeadType equals to UPDATED_STUDENT_LEDGER_HEAD_TYPE
        defaultSchoolLedgerHeadShouldNotBeFound("studentLedgerHeadType.in=" + UPDATED_STUDENT_LEDGER_HEAD_TYPE);
    }

    @Test
    @Transactional
    void getAllSchoolLedgerHeadsByStudentLedgerHeadTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolLedgerHeadRepository.saveAndFlush(schoolLedgerHead);

        // Get all the schoolLedgerHeadList where studentLedgerHeadType is not null
        defaultSchoolLedgerHeadShouldBeFound("studentLedgerHeadType.specified=true");

        // Get all the schoolLedgerHeadList where studentLedgerHeadType is null
        defaultSchoolLedgerHeadShouldNotBeFound("studentLedgerHeadType.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolLedgerHeadsByLedgerHeadNameIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolLedgerHeadRepository.saveAndFlush(schoolLedgerHead);

        // Get all the schoolLedgerHeadList where ledgerHeadName equals to DEFAULT_LEDGER_HEAD_NAME
        defaultSchoolLedgerHeadShouldBeFound("ledgerHeadName.equals=" + DEFAULT_LEDGER_HEAD_NAME);

        // Get all the schoolLedgerHeadList where ledgerHeadName equals to UPDATED_LEDGER_HEAD_NAME
        defaultSchoolLedgerHeadShouldNotBeFound("ledgerHeadName.equals=" + UPDATED_LEDGER_HEAD_NAME);
    }

    @Test
    @Transactional
    void getAllSchoolLedgerHeadsByLedgerHeadNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        schoolLedgerHeadRepository.saveAndFlush(schoolLedgerHead);

        // Get all the schoolLedgerHeadList where ledgerHeadName not equals to DEFAULT_LEDGER_HEAD_NAME
        defaultSchoolLedgerHeadShouldNotBeFound("ledgerHeadName.notEquals=" + DEFAULT_LEDGER_HEAD_NAME);

        // Get all the schoolLedgerHeadList where ledgerHeadName not equals to UPDATED_LEDGER_HEAD_NAME
        defaultSchoolLedgerHeadShouldBeFound("ledgerHeadName.notEquals=" + UPDATED_LEDGER_HEAD_NAME);
    }

    @Test
    @Transactional
    void getAllSchoolLedgerHeadsByLedgerHeadNameIsInShouldWork() throws Exception {
        // Initialize the database
        schoolLedgerHeadRepository.saveAndFlush(schoolLedgerHead);

        // Get all the schoolLedgerHeadList where ledgerHeadName in DEFAULT_LEDGER_HEAD_NAME or UPDATED_LEDGER_HEAD_NAME
        defaultSchoolLedgerHeadShouldBeFound("ledgerHeadName.in=" + DEFAULT_LEDGER_HEAD_NAME + "," + UPDATED_LEDGER_HEAD_NAME);

        // Get all the schoolLedgerHeadList where ledgerHeadName equals to UPDATED_LEDGER_HEAD_NAME
        defaultSchoolLedgerHeadShouldNotBeFound("ledgerHeadName.in=" + UPDATED_LEDGER_HEAD_NAME);
    }

    @Test
    @Transactional
    void getAllSchoolLedgerHeadsByLedgerHeadNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolLedgerHeadRepository.saveAndFlush(schoolLedgerHead);

        // Get all the schoolLedgerHeadList where ledgerHeadName is not null
        defaultSchoolLedgerHeadShouldBeFound("ledgerHeadName.specified=true");

        // Get all the schoolLedgerHeadList where ledgerHeadName is null
        defaultSchoolLedgerHeadShouldNotBeFound("ledgerHeadName.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolLedgerHeadsByLedgerHeadNameContainsSomething() throws Exception {
        // Initialize the database
        schoolLedgerHeadRepository.saveAndFlush(schoolLedgerHead);

        // Get all the schoolLedgerHeadList where ledgerHeadName contains DEFAULT_LEDGER_HEAD_NAME
        defaultSchoolLedgerHeadShouldBeFound("ledgerHeadName.contains=" + DEFAULT_LEDGER_HEAD_NAME);

        // Get all the schoolLedgerHeadList where ledgerHeadName contains UPDATED_LEDGER_HEAD_NAME
        defaultSchoolLedgerHeadShouldNotBeFound("ledgerHeadName.contains=" + UPDATED_LEDGER_HEAD_NAME);
    }

    @Test
    @Transactional
    void getAllSchoolLedgerHeadsByLedgerHeadNameNotContainsSomething() throws Exception {
        // Initialize the database
        schoolLedgerHeadRepository.saveAndFlush(schoolLedgerHead);

        // Get all the schoolLedgerHeadList where ledgerHeadName does not contain DEFAULT_LEDGER_HEAD_NAME
        defaultSchoolLedgerHeadShouldNotBeFound("ledgerHeadName.doesNotContain=" + DEFAULT_LEDGER_HEAD_NAME);

        // Get all the schoolLedgerHeadList where ledgerHeadName does not contain UPDATED_LEDGER_HEAD_NAME
        defaultSchoolLedgerHeadShouldBeFound("ledgerHeadName.doesNotContain=" + UPDATED_LEDGER_HEAD_NAME);
    }

    @Test
    @Transactional
    void getAllSchoolLedgerHeadsByLedgerHeadLongNameIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolLedgerHeadRepository.saveAndFlush(schoolLedgerHead);

        // Get all the schoolLedgerHeadList where ledgerHeadLongName equals to DEFAULT_LEDGER_HEAD_LONG_NAME
        defaultSchoolLedgerHeadShouldBeFound("ledgerHeadLongName.equals=" + DEFAULT_LEDGER_HEAD_LONG_NAME);

        // Get all the schoolLedgerHeadList where ledgerHeadLongName equals to UPDATED_LEDGER_HEAD_LONG_NAME
        defaultSchoolLedgerHeadShouldNotBeFound("ledgerHeadLongName.equals=" + UPDATED_LEDGER_HEAD_LONG_NAME);
    }

    @Test
    @Transactional
    void getAllSchoolLedgerHeadsByLedgerHeadLongNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        schoolLedgerHeadRepository.saveAndFlush(schoolLedgerHead);

        // Get all the schoolLedgerHeadList where ledgerHeadLongName not equals to DEFAULT_LEDGER_HEAD_LONG_NAME
        defaultSchoolLedgerHeadShouldNotBeFound("ledgerHeadLongName.notEquals=" + DEFAULT_LEDGER_HEAD_LONG_NAME);

        // Get all the schoolLedgerHeadList where ledgerHeadLongName not equals to UPDATED_LEDGER_HEAD_LONG_NAME
        defaultSchoolLedgerHeadShouldBeFound("ledgerHeadLongName.notEquals=" + UPDATED_LEDGER_HEAD_LONG_NAME);
    }

    @Test
    @Transactional
    void getAllSchoolLedgerHeadsByLedgerHeadLongNameIsInShouldWork() throws Exception {
        // Initialize the database
        schoolLedgerHeadRepository.saveAndFlush(schoolLedgerHead);

        // Get all the schoolLedgerHeadList where ledgerHeadLongName in DEFAULT_LEDGER_HEAD_LONG_NAME or UPDATED_LEDGER_HEAD_LONG_NAME
        defaultSchoolLedgerHeadShouldBeFound(
            "ledgerHeadLongName.in=" + DEFAULT_LEDGER_HEAD_LONG_NAME + "," + UPDATED_LEDGER_HEAD_LONG_NAME
        );

        // Get all the schoolLedgerHeadList where ledgerHeadLongName equals to UPDATED_LEDGER_HEAD_LONG_NAME
        defaultSchoolLedgerHeadShouldNotBeFound("ledgerHeadLongName.in=" + UPDATED_LEDGER_HEAD_LONG_NAME);
    }

    @Test
    @Transactional
    void getAllSchoolLedgerHeadsByLedgerHeadLongNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolLedgerHeadRepository.saveAndFlush(schoolLedgerHead);

        // Get all the schoolLedgerHeadList where ledgerHeadLongName is not null
        defaultSchoolLedgerHeadShouldBeFound("ledgerHeadLongName.specified=true");

        // Get all the schoolLedgerHeadList where ledgerHeadLongName is null
        defaultSchoolLedgerHeadShouldNotBeFound("ledgerHeadLongName.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolLedgerHeadsByLedgerHeadLongNameContainsSomething() throws Exception {
        // Initialize the database
        schoolLedgerHeadRepository.saveAndFlush(schoolLedgerHead);

        // Get all the schoolLedgerHeadList where ledgerHeadLongName contains DEFAULT_LEDGER_HEAD_LONG_NAME
        defaultSchoolLedgerHeadShouldBeFound("ledgerHeadLongName.contains=" + DEFAULT_LEDGER_HEAD_LONG_NAME);

        // Get all the schoolLedgerHeadList where ledgerHeadLongName contains UPDATED_LEDGER_HEAD_LONG_NAME
        defaultSchoolLedgerHeadShouldNotBeFound("ledgerHeadLongName.contains=" + UPDATED_LEDGER_HEAD_LONG_NAME);
    }

    @Test
    @Transactional
    void getAllSchoolLedgerHeadsByLedgerHeadLongNameNotContainsSomething() throws Exception {
        // Initialize the database
        schoolLedgerHeadRepository.saveAndFlush(schoolLedgerHead);

        // Get all the schoolLedgerHeadList where ledgerHeadLongName does not contain DEFAULT_LEDGER_HEAD_LONG_NAME
        defaultSchoolLedgerHeadShouldNotBeFound("ledgerHeadLongName.doesNotContain=" + DEFAULT_LEDGER_HEAD_LONG_NAME);

        // Get all the schoolLedgerHeadList where ledgerHeadLongName does not contain UPDATED_LEDGER_HEAD_LONG_NAME
        defaultSchoolLedgerHeadShouldBeFound("ledgerHeadLongName.doesNotContain=" + UPDATED_LEDGER_HEAD_LONG_NAME);
    }

    @Test
    @Transactional
    void getAllSchoolLedgerHeadsByCreateDateIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolLedgerHeadRepository.saveAndFlush(schoolLedgerHead);

        // Get all the schoolLedgerHeadList where createDate equals to DEFAULT_CREATE_DATE
        defaultSchoolLedgerHeadShouldBeFound("createDate.equals=" + DEFAULT_CREATE_DATE);

        // Get all the schoolLedgerHeadList where createDate equals to UPDATED_CREATE_DATE
        defaultSchoolLedgerHeadShouldNotBeFound("createDate.equals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolLedgerHeadsByCreateDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        schoolLedgerHeadRepository.saveAndFlush(schoolLedgerHead);

        // Get all the schoolLedgerHeadList where createDate not equals to DEFAULT_CREATE_DATE
        defaultSchoolLedgerHeadShouldNotBeFound("createDate.notEquals=" + DEFAULT_CREATE_DATE);

        // Get all the schoolLedgerHeadList where createDate not equals to UPDATED_CREATE_DATE
        defaultSchoolLedgerHeadShouldBeFound("createDate.notEquals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolLedgerHeadsByCreateDateIsInShouldWork() throws Exception {
        // Initialize the database
        schoolLedgerHeadRepository.saveAndFlush(schoolLedgerHead);

        // Get all the schoolLedgerHeadList where createDate in DEFAULT_CREATE_DATE or UPDATED_CREATE_DATE
        defaultSchoolLedgerHeadShouldBeFound("createDate.in=" + DEFAULT_CREATE_DATE + "," + UPDATED_CREATE_DATE);

        // Get all the schoolLedgerHeadList where createDate equals to UPDATED_CREATE_DATE
        defaultSchoolLedgerHeadShouldNotBeFound("createDate.in=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolLedgerHeadsByCreateDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolLedgerHeadRepository.saveAndFlush(schoolLedgerHead);

        // Get all the schoolLedgerHeadList where createDate is not null
        defaultSchoolLedgerHeadShouldBeFound("createDate.specified=true");

        // Get all the schoolLedgerHeadList where createDate is null
        defaultSchoolLedgerHeadShouldNotBeFound("createDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolLedgerHeadsByCreateDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        schoolLedgerHeadRepository.saveAndFlush(schoolLedgerHead);

        // Get all the schoolLedgerHeadList where createDate is greater than or equal to DEFAULT_CREATE_DATE
        defaultSchoolLedgerHeadShouldBeFound("createDate.greaterThanOrEqual=" + DEFAULT_CREATE_DATE);

        // Get all the schoolLedgerHeadList where createDate is greater than or equal to UPDATED_CREATE_DATE
        defaultSchoolLedgerHeadShouldNotBeFound("createDate.greaterThanOrEqual=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolLedgerHeadsByCreateDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        schoolLedgerHeadRepository.saveAndFlush(schoolLedgerHead);

        // Get all the schoolLedgerHeadList where createDate is less than or equal to DEFAULT_CREATE_DATE
        defaultSchoolLedgerHeadShouldBeFound("createDate.lessThanOrEqual=" + DEFAULT_CREATE_DATE);

        // Get all the schoolLedgerHeadList where createDate is less than or equal to SMALLER_CREATE_DATE
        defaultSchoolLedgerHeadShouldNotBeFound("createDate.lessThanOrEqual=" + SMALLER_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolLedgerHeadsByCreateDateIsLessThanSomething() throws Exception {
        // Initialize the database
        schoolLedgerHeadRepository.saveAndFlush(schoolLedgerHead);

        // Get all the schoolLedgerHeadList where createDate is less than DEFAULT_CREATE_DATE
        defaultSchoolLedgerHeadShouldNotBeFound("createDate.lessThan=" + DEFAULT_CREATE_DATE);

        // Get all the schoolLedgerHeadList where createDate is less than UPDATED_CREATE_DATE
        defaultSchoolLedgerHeadShouldBeFound("createDate.lessThan=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolLedgerHeadsByCreateDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        schoolLedgerHeadRepository.saveAndFlush(schoolLedgerHead);

        // Get all the schoolLedgerHeadList where createDate is greater than DEFAULT_CREATE_DATE
        defaultSchoolLedgerHeadShouldNotBeFound("createDate.greaterThan=" + DEFAULT_CREATE_DATE);

        // Get all the schoolLedgerHeadList where createDate is greater than SMALLER_CREATE_DATE
        defaultSchoolLedgerHeadShouldBeFound("createDate.greaterThan=" + SMALLER_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolLedgerHeadsByLastModifiedIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolLedgerHeadRepository.saveAndFlush(schoolLedgerHead);

        // Get all the schoolLedgerHeadList where lastModified equals to DEFAULT_LAST_MODIFIED
        defaultSchoolLedgerHeadShouldBeFound("lastModified.equals=" + DEFAULT_LAST_MODIFIED);

        // Get all the schoolLedgerHeadList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultSchoolLedgerHeadShouldNotBeFound("lastModified.equals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSchoolLedgerHeadsByLastModifiedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        schoolLedgerHeadRepository.saveAndFlush(schoolLedgerHead);

        // Get all the schoolLedgerHeadList where lastModified not equals to DEFAULT_LAST_MODIFIED
        defaultSchoolLedgerHeadShouldNotBeFound("lastModified.notEquals=" + DEFAULT_LAST_MODIFIED);

        // Get all the schoolLedgerHeadList where lastModified not equals to UPDATED_LAST_MODIFIED
        defaultSchoolLedgerHeadShouldBeFound("lastModified.notEquals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSchoolLedgerHeadsByLastModifiedIsInShouldWork() throws Exception {
        // Initialize the database
        schoolLedgerHeadRepository.saveAndFlush(schoolLedgerHead);

        // Get all the schoolLedgerHeadList where lastModified in DEFAULT_LAST_MODIFIED or UPDATED_LAST_MODIFIED
        defaultSchoolLedgerHeadShouldBeFound("lastModified.in=" + DEFAULT_LAST_MODIFIED + "," + UPDATED_LAST_MODIFIED);

        // Get all the schoolLedgerHeadList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultSchoolLedgerHeadShouldNotBeFound("lastModified.in=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSchoolLedgerHeadsByLastModifiedIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolLedgerHeadRepository.saveAndFlush(schoolLedgerHead);

        // Get all the schoolLedgerHeadList where lastModified is not null
        defaultSchoolLedgerHeadShouldBeFound("lastModified.specified=true");

        // Get all the schoolLedgerHeadList where lastModified is null
        defaultSchoolLedgerHeadShouldNotBeFound("lastModified.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolLedgerHeadsByLastModifiedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        schoolLedgerHeadRepository.saveAndFlush(schoolLedgerHead);

        // Get all the schoolLedgerHeadList where lastModified is greater than or equal to DEFAULT_LAST_MODIFIED
        defaultSchoolLedgerHeadShouldBeFound("lastModified.greaterThanOrEqual=" + DEFAULT_LAST_MODIFIED);

        // Get all the schoolLedgerHeadList where lastModified is greater than or equal to UPDATED_LAST_MODIFIED
        defaultSchoolLedgerHeadShouldNotBeFound("lastModified.greaterThanOrEqual=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSchoolLedgerHeadsByLastModifiedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        schoolLedgerHeadRepository.saveAndFlush(schoolLedgerHead);

        // Get all the schoolLedgerHeadList where lastModified is less than or equal to DEFAULT_LAST_MODIFIED
        defaultSchoolLedgerHeadShouldBeFound("lastModified.lessThanOrEqual=" + DEFAULT_LAST_MODIFIED);

        // Get all the schoolLedgerHeadList where lastModified is less than or equal to SMALLER_LAST_MODIFIED
        defaultSchoolLedgerHeadShouldNotBeFound("lastModified.lessThanOrEqual=" + SMALLER_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSchoolLedgerHeadsByLastModifiedIsLessThanSomething() throws Exception {
        // Initialize the database
        schoolLedgerHeadRepository.saveAndFlush(schoolLedgerHead);

        // Get all the schoolLedgerHeadList where lastModified is less than DEFAULT_LAST_MODIFIED
        defaultSchoolLedgerHeadShouldNotBeFound("lastModified.lessThan=" + DEFAULT_LAST_MODIFIED);

        // Get all the schoolLedgerHeadList where lastModified is less than UPDATED_LAST_MODIFIED
        defaultSchoolLedgerHeadShouldBeFound("lastModified.lessThan=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSchoolLedgerHeadsByLastModifiedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        schoolLedgerHeadRepository.saveAndFlush(schoolLedgerHead);

        // Get all the schoolLedgerHeadList where lastModified is greater than DEFAULT_LAST_MODIFIED
        defaultSchoolLedgerHeadShouldNotBeFound("lastModified.greaterThan=" + DEFAULT_LAST_MODIFIED);

        // Get all the schoolLedgerHeadList where lastModified is greater than SMALLER_LAST_MODIFIED
        defaultSchoolLedgerHeadShouldBeFound("lastModified.greaterThan=" + SMALLER_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSchoolLedgerHeadsByCancelDateIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolLedgerHeadRepository.saveAndFlush(schoolLedgerHead);

        // Get all the schoolLedgerHeadList where cancelDate equals to DEFAULT_CANCEL_DATE
        defaultSchoolLedgerHeadShouldBeFound("cancelDate.equals=" + DEFAULT_CANCEL_DATE);

        // Get all the schoolLedgerHeadList where cancelDate equals to UPDATED_CANCEL_DATE
        defaultSchoolLedgerHeadShouldNotBeFound("cancelDate.equals=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolLedgerHeadsByCancelDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        schoolLedgerHeadRepository.saveAndFlush(schoolLedgerHead);

        // Get all the schoolLedgerHeadList where cancelDate not equals to DEFAULT_CANCEL_DATE
        defaultSchoolLedgerHeadShouldNotBeFound("cancelDate.notEquals=" + DEFAULT_CANCEL_DATE);

        // Get all the schoolLedgerHeadList where cancelDate not equals to UPDATED_CANCEL_DATE
        defaultSchoolLedgerHeadShouldBeFound("cancelDate.notEquals=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolLedgerHeadsByCancelDateIsInShouldWork() throws Exception {
        // Initialize the database
        schoolLedgerHeadRepository.saveAndFlush(schoolLedgerHead);

        // Get all the schoolLedgerHeadList where cancelDate in DEFAULT_CANCEL_DATE or UPDATED_CANCEL_DATE
        defaultSchoolLedgerHeadShouldBeFound("cancelDate.in=" + DEFAULT_CANCEL_DATE + "," + UPDATED_CANCEL_DATE);

        // Get all the schoolLedgerHeadList where cancelDate equals to UPDATED_CANCEL_DATE
        defaultSchoolLedgerHeadShouldNotBeFound("cancelDate.in=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolLedgerHeadsByCancelDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolLedgerHeadRepository.saveAndFlush(schoolLedgerHead);

        // Get all the schoolLedgerHeadList where cancelDate is not null
        defaultSchoolLedgerHeadShouldBeFound("cancelDate.specified=true");

        // Get all the schoolLedgerHeadList where cancelDate is null
        defaultSchoolLedgerHeadShouldNotBeFound("cancelDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolLedgerHeadsByCancelDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        schoolLedgerHeadRepository.saveAndFlush(schoolLedgerHead);

        // Get all the schoolLedgerHeadList where cancelDate is greater than or equal to DEFAULT_CANCEL_DATE
        defaultSchoolLedgerHeadShouldBeFound("cancelDate.greaterThanOrEqual=" + DEFAULT_CANCEL_DATE);

        // Get all the schoolLedgerHeadList where cancelDate is greater than or equal to UPDATED_CANCEL_DATE
        defaultSchoolLedgerHeadShouldNotBeFound("cancelDate.greaterThanOrEqual=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolLedgerHeadsByCancelDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        schoolLedgerHeadRepository.saveAndFlush(schoolLedgerHead);

        // Get all the schoolLedgerHeadList where cancelDate is less than or equal to DEFAULT_CANCEL_DATE
        defaultSchoolLedgerHeadShouldBeFound("cancelDate.lessThanOrEqual=" + DEFAULT_CANCEL_DATE);

        // Get all the schoolLedgerHeadList where cancelDate is less than or equal to SMALLER_CANCEL_DATE
        defaultSchoolLedgerHeadShouldNotBeFound("cancelDate.lessThanOrEqual=" + SMALLER_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolLedgerHeadsByCancelDateIsLessThanSomething() throws Exception {
        // Initialize the database
        schoolLedgerHeadRepository.saveAndFlush(schoolLedgerHead);

        // Get all the schoolLedgerHeadList where cancelDate is less than DEFAULT_CANCEL_DATE
        defaultSchoolLedgerHeadShouldNotBeFound("cancelDate.lessThan=" + DEFAULT_CANCEL_DATE);

        // Get all the schoolLedgerHeadList where cancelDate is less than UPDATED_CANCEL_DATE
        defaultSchoolLedgerHeadShouldBeFound("cancelDate.lessThan=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolLedgerHeadsByCancelDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        schoolLedgerHeadRepository.saveAndFlush(schoolLedgerHead);

        // Get all the schoolLedgerHeadList where cancelDate is greater than DEFAULT_CANCEL_DATE
        defaultSchoolLedgerHeadShouldNotBeFound("cancelDate.greaterThan=" + DEFAULT_CANCEL_DATE);

        // Get all the schoolLedgerHeadList where cancelDate is greater than SMALLER_CANCEL_DATE
        defaultSchoolLedgerHeadShouldBeFound("cancelDate.greaterThan=" + SMALLER_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolLedgerHeadsByClassFeeIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolLedgerHeadRepository.saveAndFlush(schoolLedgerHead);
        ClassFee classFee = ClassFeeResourceIT.createEntity(em);
        em.persist(classFee);
        em.flush();
        schoolLedgerHead.addClassFee(classFee);
        schoolLedgerHeadRepository.saveAndFlush(schoolLedgerHead);
        Long classFeeId = classFee.getId();

        // Get all the schoolLedgerHeadList where classFee equals to classFeeId
        defaultSchoolLedgerHeadShouldBeFound("classFeeId.equals=" + classFeeId);

        // Get all the schoolLedgerHeadList where classFee equals to (classFeeId + 1)
        defaultSchoolLedgerHeadShouldNotBeFound("classFeeId.equals=" + (classFeeId + 1));
    }

    @Test
    @Transactional
    void getAllSchoolLedgerHeadsByStudentDiscountIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolLedgerHeadRepository.saveAndFlush(schoolLedgerHead);
        StudentDiscount studentDiscount = StudentDiscountResourceIT.createEntity(em);
        em.persist(studentDiscount);
        em.flush();
        schoolLedgerHead.addStudentDiscount(studentDiscount);
        schoolLedgerHeadRepository.saveAndFlush(schoolLedgerHead);
        Long studentDiscountId = studentDiscount.getId();

        // Get all the schoolLedgerHeadList where studentDiscount equals to studentDiscountId
        defaultSchoolLedgerHeadShouldBeFound("studentDiscountId.equals=" + studentDiscountId);

        // Get all the schoolLedgerHeadList where studentDiscount equals to (studentDiscountId + 1)
        defaultSchoolLedgerHeadShouldNotBeFound("studentDiscountId.equals=" + (studentDiscountId + 1));
    }

    @Test
    @Transactional
    void getAllSchoolLedgerHeadsByStudentAdditionalChargesIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolLedgerHeadRepository.saveAndFlush(schoolLedgerHead);
        StudentAdditionalCharges studentAdditionalCharges = StudentAdditionalChargesResourceIT.createEntity(em);
        em.persist(studentAdditionalCharges);
        em.flush();
        schoolLedgerHead.addStudentAdditionalCharges(studentAdditionalCharges);
        schoolLedgerHeadRepository.saveAndFlush(schoolLedgerHead);
        Long studentAdditionalChargesId = studentAdditionalCharges.getId();

        // Get all the schoolLedgerHeadList where studentAdditionalCharges equals to studentAdditionalChargesId
        defaultSchoolLedgerHeadShouldBeFound("studentAdditionalChargesId.equals=" + studentAdditionalChargesId);

        // Get all the schoolLedgerHeadList where studentAdditionalCharges equals to (studentAdditionalChargesId + 1)
        defaultSchoolLedgerHeadShouldNotBeFound("studentAdditionalChargesId.equals=" + (studentAdditionalChargesId + 1));
    }

    @Test
    @Transactional
    void getAllSchoolLedgerHeadsByStudentChargesSummaryIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolLedgerHeadRepository.saveAndFlush(schoolLedgerHead);
        StudentChargesSummary studentChargesSummary = StudentChargesSummaryResourceIT.createEntity(em);
        em.persist(studentChargesSummary);
        em.flush();
        schoolLedgerHead.addStudentChargesSummary(studentChargesSummary);
        schoolLedgerHeadRepository.saveAndFlush(schoolLedgerHead);
        Long studentChargesSummaryId = studentChargesSummary.getId();

        // Get all the schoolLedgerHeadList where studentChargesSummary equals to studentChargesSummaryId
        defaultSchoolLedgerHeadShouldBeFound("studentChargesSummaryId.equals=" + studentChargesSummaryId);

        // Get all the schoolLedgerHeadList where studentChargesSummary equals to (studentChargesSummaryId + 1)
        defaultSchoolLedgerHeadShouldNotBeFound("studentChargesSummaryId.equals=" + (studentChargesSummaryId + 1));
    }

    @Test
    @Transactional
    void getAllSchoolLedgerHeadsBySchoolIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolLedgerHeadRepository.saveAndFlush(schoolLedgerHead);
        School school = SchoolResourceIT.createEntity(em);
        em.persist(school);
        em.flush();
        schoolLedgerHead.setSchool(school);
        schoolLedgerHeadRepository.saveAndFlush(schoolLedgerHead);
        Long schoolId = school.getId();

        // Get all the schoolLedgerHeadList where school equals to schoolId
        defaultSchoolLedgerHeadShouldBeFound("schoolId.equals=" + schoolId);

        // Get all the schoolLedgerHeadList where school equals to (schoolId + 1)
        defaultSchoolLedgerHeadShouldNotBeFound("schoolId.equals=" + (schoolId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSchoolLedgerHeadShouldBeFound(String filter) throws Exception {
        restSchoolLedgerHeadMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(schoolLedgerHead.getId().intValue())))
            .andExpect(jsonPath("$.[*].studentLedgerHeadType").value(hasItem(DEFAULT_STUDENT_LEDGER_HEAD_TYPE.toString())))
            .andExpect(jsonPath("$.[*].ledgerHeadName").value(hasItem(DEFAULT_LEDGER_HEAD_NAME)))
            .andExpect(jsonPath("$.[*].ledgerHeadLongName").value(hasItem(DEFAULT_LEDGER_HEAD_LONG_NAME)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].cancelDate").value(hasItem(DEFAULT_CANCEL_DATE.toString())));

        // Check, that the count call also returns 1
        restSchoolLedgerHeadMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSchoolLedgerHeadShouldNotBeFound(String filter) throws Exception {
        restSchoolLedgerHeadMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSchoolLedgerHeadMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSchoolLedgerHead() throws Exception {
        // Get the schoolLedgerHead
        restSchoolLedgerHeadMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSchoolLedgerHead() throws Exception {
        // Initialize the database
        schoolLedgerHeadRepository.saveAndFlush(schoolLedgerHead);

        int databaseSizeBeforeUpdate = schoolLedgerHeadRepository.findAll().size();

        // Update the schoolLedgerHead
        SchoolLedgerHead updatedSchoolLedgerHead = schoolLedgerHeadRepository.findById(schoolLedgerHead.getId()).get();
        // Disconnect from session so that the updates on updatedSchoolLedgerHead are not directly saved in db
        em.detach(updatedSchoolLedgerHead);
        updatedSchoolLedgerHead
            .studentLedgerHeadType(UPDATED_STUDENT_LEDGER_HEAD_TYPE)
            .ledgerHeadName(UPDATED_LEDGER_HEAD_NAME)
            .ledgerHeadLongName(UPDATED_LEDGER_HEAD_LONG_NAME)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);
        SchoolLedgerHeadDTO schoolLedgerHeadDTO = schoolLedgerHeadMapper.toDto(updatedSchoolLedgerHead);

        restSchoolLedgerHeadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, schoolLedgerHeadDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schoolLedgerHeadDTO))
            )
            .andExpect(status().isOk());

        // Validate the SchoolLedgerHead in the database
        List<SchoolLedgerHead> schoolLedgerHeadList = schoolLedgerHeadRepository.findAll();
        assertThat(schoolLedgerHeadList).hasSize(databaseSizeBeforeUpdate);
        SchoolLedgerHead testSchoolLedgerHead = schoolLedgerHeadList.get(schoolLedgerHeadList.size() - 1);
        assertThat(testSchoolLedgerHead.getStudentLedgerHeadType()).isEqualTo(UPDATED_STUDENT_LEDGER_HEAD_TYPE);
        assertThat(testSchoolLedgerHead.getLedgerHeadName()).isEqualTo(UPDATED_LEDGER_HEAD_NAME);
        assertThat(testSchoolLedgerHead.getLedgerHeadLongName()).isEqualTo(UPDATED_LEDGER_HEAD_LONG_NAME);
        assertThat(testSchoolLedgerHead.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testSchoolLedgerHead.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testSchoolLedgerHead.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void putNonExistingSchoolLedgerHead() throws Exception {
        int databaseSizeBeforeUpdate = schoolLedgerHeadRepository.findAll().size();
        schoolLedgerHead.setId(count.incrementAndGet());

        // Create the SchoolLedgerHead
        SchoolLedgerHeadDTO schoolLedgerHeadDTO = schoolLedgerHeadMapper.toDto(schoolLedgerHead);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSchoolLedgerHeadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, schoolLedgerHeadDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schoolLedgerHeadDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchoolLedgerHead in the database
        List<SchoolLedgerHead> schoolLedgerHeadList = schoolLedgerHeadRepository.findAll();
        assertThat(schoolLedgerHeadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSchoolLedgerHead() throws Exception {
        int databaseSizeBeforeUpdate = schoolLedgerHeadRepository.findAll().size();
        schoolLedgerHead.setId(count.incrementAndGet());

        // Create the SchoolLedgerHead
        SchoolLedgerHeadDTO schoolLedgerHeadDTO = schoolLedgerHeadMapper.toDto(schoolLedgerHead);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchoolLedgerHeadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schoolLedgerHeadDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchoolLedgerHead in the database
        List<SchoolLedgerHead> schoolLedgerHeadList = schoolLedgerHeadRepository.findAll();
        assertThat(schoolLedgerHeadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSchoolLedgerHead() throws Exception {
        int databaseSizeBeforeUpdate = schoolLedgerHeadRepository.findAll().size();
        schoolLedgerHead.setId(count.incrementAndGet());

        // Create the SchoolLedgerHead
        SchoolLedgerHeadDTO schoolLedgerHeadDTO = schoolLedgerHeadMapper.toDto(schoolLedgerHead);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchoolLedgerHeadMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(schoolLedgerHeadDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SchoolLedgerHead in the database
        List<SchoolLedgerHead> schoolLedgerHeadList = schoolLedgerHeadRepository.findAll();
        assertThat(schoolLedgerHeadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSchoolLedgerHeadWithPatch() throws Exception {
        // Initialize the database
        schoolLedgerHeadRepository.saveAndFlush(schoolLedgerHead);

        int databaseSizeBeforeUpdate = schoolLedgerHeadRepository.findAll().size();

        // Update the schoolLedgerHead using partial update
        SchoolLedgerHead partialUpdatedSchoolLedgerHead = new SchoolLedgerHead();
        partialUpdatedSchoolLedgerHead.setId(schoolLedgerHead.getId());

        partialUpdatedSchoolLedgerHead
            .studentLedgerHeadType(UPDATED_STUDENT_LEDGER_HEAD_TYPE)
            .ledgerHeadName(UPDATED_LEDGER_HEAD_NAME)
            .ledgerHeadLongName(UPDATED_LEDGER_HEAD_LONG_NAME)
            .cancelDate(UPDATED_CANCEL_DATE);

        restSchoolLedgerHeadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSchoolLedgerHead.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSchoolLedgerHead))
            )
            .andExpect(status().isOk());

        // Validate the SchoolLedgerHead in the database
        List<SchoolLedgerHead> schoolLedgerHeadList = schoolLedgerHeadRepository.findAll();
        assertThat(schoolLedgerHeadList).hasSize(databaseSizeBeforeUpdate);
        SchoolLedgerHead testSchoolLedgerHead = schoolLedgerHeadList.get(schoolLedgerHeadList.size() - 1);
        assertThat(testSchoolLedgerHead.getStudentLedgerHeadType()).isEqualTo(UPDATED_STUDENT_LEDGER_HEAD_TYPE);
        assertThat(testSchoolLedgerHead.getLedgerHeadName()).isEqualTo(UPDATED_LEDGER_HEAD_NAME);
        assertThat(testSchoolLedgerHead.getLedgerHeadLongName()).isEqualTo(UPDATED_LEDGER_HEAD_LONG_NAME);
        assertThat(testSchoolLedgerHead.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testSchoolLedgerHead.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
        assertThat(testSchoolLedgerHead.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void fullUpdateSchoolLedgerHeadWithPatch() throws Exception {
        // Initialize the database
        schoolLedgerHeadRepository.saveAndFlush(schoolLedgerHead);

        int databaseSizeBeforeUpdate = schoolLedgerHeadRepository.findAll().size();

        // Update the schoolLedgerHead using partial update
        SchoolLedgerHead partialUpdatedSchoolLedgerHead = new SchoolLedgerHead();
        partialUpdatedSchoolLedgerHead.setId(schoolLedgerHead.getId());

        partialUpdatedSchoolLedgerHead
            .studentLedgerHeadType(UPDATED_STUDENT_LEDGER_HEAD_TYPE)
            .ledgerHeadName(UPDATED_LEDGER_HEAD_NAME)
            .ledgerHeadLongName(UPDATED_LEDGER_HEAD_LONG_NAME)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);

        restSchoolLedgerHeadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSchoolLedgerHead.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSchoolLedgerHead))
            )
            .andExpect(status().isOk());

        // Validate the SchoolLedgerHead in the database
        List<SchoolLedgerHead> schoolLedgerHeadList = schoolLedgerHeadRepository.findAll();
        assertThat(schoolLedgerHeadList).hasSize(databaseSizeBeforeUpdate);
        SchoolLedgerHead testSchoolLedgerHead = schoolLedgerHeadList.get(schoolLedgerHeadList.size() - 1);
        assertThat(testSchoolLedgerHead.getStudentLedgerHeadType()).isEqualTo(UPDATED_STUDENT_LEDGER_HEAD_TYPE);
        assertThat(testSchoolLedgerHead.getLedgerHeadName()).isEqualTo(UPDATED_LEDGER_HEAD_NAME);
        assertThat(testSchoolLedgerHead.getLedgerHeadLongName()).isEqualTo(UPDATED_LEDGER_HEAD_LONG_NAME);
        assertThat(testSchoolLedgerHead.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testSchoolLedgerHead.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testSchoolLedgerHead.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingSchoolLedgerHead() throws Exception {
        int databaseSizeBeforeUpdate = schoolLedgerHeadRepository.findAll().size();
        schoolLedgerHead.setId(count.incrementAndGet());

        // Create the SchoolLedgerHead
        SchoolLedgerHeadDTO schoolLedgerHeadDTO = schoolLedgerHeadMapper.toDto(schoolLedgerHead);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSchoolLedgerHeadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, schoolLedgerHeadDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(schoolLedgerHeadDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchoolLedgerHead in the database
        List<SchoolLedgerHead> schoolLedgerHeadList = schoolLedgerHeadRepository.findAll();
        assertThat(schoolLedgerHeadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSchoolLedgerHead() throws Exception {
        int databaseSizeBeforeUpdate = schoolLedgerHeadRepository.findAll().size();
        schoolLedgerHead.setId(count.incrementAndGet());

        // Create the SchoolLedgerHead
        SchoolLedgerHeadDTO schoolLedgerHeadDTO = schoolLedgerHeadMapper.toDto(schoolLedgerHead);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchoolLedgerHeadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(schoolLedgerHeadDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchoolLedgerHead in the database
        List<SchoolLedgerHead> schoolLedgerHeadList = schoolLedgerHeadRepository.findAll();
        assertThat(schoolLedgerHeadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSchoolLedgerHead() throws Exception {
        int databaseSizeBeforeUpdate = schoolLedgerHeadRepository.findAll().size();
        schoolLedgerHead.setId(count.incrementAndGet());

        // Create the SchoolLedgerHead
        SchoolLedgerHeadDTO schoolLedgerHeadDTO = schoolLedgerHeadMapper.toDto(schoolLedgerHead);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchoolLedgerHeadMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(schoolLedgerHeadDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SchoolLedgerHead in the database
        List<SchoolLedgerHead> schoolLedgerHeadList = schoolLedgerHeadRepository.findAll();
        assertThat(schoolLedgerHeadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSchoolLedgerHead() throws Exception {
        // Initialize the database
        schoolLedgerHeadRepository.saveAndFlush(schoolLedgerHead);

        int databaseSizeBeforeDelete = schoolLedgerHeadRepository.findAll().size();

        // Delete the schoolLedgerHead
        restSchoolLedgerHeadMockMvc
            .perform(delete(ENTITY_API_URL_ID, schoolLedgerHead.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SchoolLedgerHead> schoolLedgerHeadList = schoolLedgerHeadRepository.findAll();
        assertThat(schoolLedgerHeadList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
