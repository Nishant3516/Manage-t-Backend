package com.ssik.manageit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ssik.manageit.IntegrationTest;
import com.ssik.manageit.domain.AuditLog;
import com.ssik.manageit.domain.IdStore;
import com.ssik.manageit.domain.School;
import com.ssik.manageit.domain.SchoolClass;
import com.ssik.manageit.domain.SchoolLedgerHead;
import com.ssik.manageit.repository.SchoolRepository;
import com.ssik.manageit.service.criteria.SchoolCriteria;
import com.ssik.manageit.service.dto.SchoolDTO;
import com.ssik.manageit.service.mapper.SchoolMapper;
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
 * Integration tests for the {@link SchoolResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SchoolResourceIT {

    private static final String DEFAULT_GROUP_NAME = "AAAAAAAAAA";
    private static final String UPDATED_GROUP_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SCHOOL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SCHOOL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_AFFL_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_AFFL_NUMBER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATE_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CREATE_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_LAST_MODIFIED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_MODIFIED = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_LAST_MODIFIED = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_CANCEL_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CANCEL_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CANCEL_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/schools";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private SchoolMapper schoolMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSchoolMockMvc;

    private School school;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static School createEntity(EntityManager em) {
        School school = new School()
            .groupName(DEFAULT_GROUP_NAME)
            .schoolName(DEFAULT_SCHOOL_NAME)
            .address(DEFAULT_ADDRESS)
            .afflNumber(DEFAULT_AFFL_NUMBER)
            .createDate(DEFAULT_CREATE_DATE)
            .lastModified(DEFAULT_LAST_MODIFIED)
            .cancelDate(DEFAULT_CANCEL_DATE);
        return school;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static School createUpdatedEntity(EntityManager em) {
        School school = new School()
            .groupName(UPDATED_GROUP_NAME)
            .schoolName(UPDATED_SCHOOL_NAME)
            .address(UPDATED_ADDRESS)
            .afflNumber(UPDATED_AFFL_NUMBER)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);
        return school;
    }

    @BeforeEach
    public void initTest() {
        school = createEntity(em);
    }

    @Test
    @Transactional
    void createSchool() throws Exception {
        int databaseSizeBeforeCreate = schoolRepository.findAll().size();
        // Create the School
        SchoolDTO schoolDTO = schoolMapper.toDto(school);
        restSchoolMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(schoolDTO)))
            .andExpect(status().isCreated());

        // Validate the School in the database
        List<School> schoolList = schoolRepository.findAll();
        assertThat(schoolList).hasSize(databaseSizeBeforeCreate + 1);
        School testSchool = schoolList.get(schoolList.size() - 1);
        assertThat(testSchool.getGroupName()).isEqualTo(DEFAULT_GROUP_NAME);
        assertThat(testSchool.getSchoolName()).isEqualTo(DEFAULT_SCHOOL_NAME);
        assertThat(testSchool.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testSchool.getAfflNumber()).isEqualTo(DEFAULT_AFFL_NUMBER);
        assertThat(testSchool.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testSchool.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
        assertThat(testSchool.getCancelDate()).isEqualTo(DEFAULT_CANCEL_DATE);
    }

    @Test
    @Transactional
    void createSchoolWithExistingId() throws Exception {
        // Create the School with an existing ID
        school.setId(1L);
        SchoolDTO schoolDTO = schoolMapper.toDto(school);

        int databaseSizeBeforeCreate = schoolRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSchoolMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(schoolDTO)))
            .andExpect(status().isBadRequest());

        // Validate the School in the database
        List<School> schoolList = schoolRepository.findAll();
        assertThat(schoolList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkGroupNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = schoolRepository.findAll().size();
        // set the field null
        school.setGroupName(null);

        // Create the School, which fails.
        SchoolDTO schoolDTO = schoolMapper.toDto(school);

        restSchoolMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(schoolDTO)))
            .andExpect(status().isBadRequest());

        List<School> schoolList = schoolRepository.findAll();
        assertThat(schoolList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSchoolNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = schoolRepository.findAll().size();
        // set the field null
        school.setSchoolName(null);

        // Create the School, which fails.
        SchoolDTO schoolDTO = schoolMapper.toDto(school);

        restSchoolMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(schoolDTO)))
            .andExpect(status().isBadRequest());

        List<School> schoolList = schoolRepository.findAll();
        assertThat(schoolList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSchools() throws Exception {
        // Initialize the database
        schoolRepository.saveAndFlush(school);

        // Get all the schoolList
        restSchoolMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(school.getId().intValue())))
            .andExpect(jsonPath("$.[*].groupName").value(hasItem(DEFAULT_GROUP_NAME)))
            .andExpect(jsonPath("$.[*].schoolName").value(hasItem(DEFAULT_SCHOOL_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].afflNumber").value(hasItem(DEFAULT_AFFL_NUMBER)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].cancelDate").value(hasItem(DEFAULT_CANCEL_DATE.toString())));
    }

    @Test
    @Transactional
    void getSchool() throws Exception {
        // Initialize the database
        schoolRepository.saveAndFlush(school);

        // Get the school
        restSchoolMockMvc
            .perform(get(ENTITY_API_URL_ID, school.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(school.getId().intValue()))
            .andExpect(jsonPath("$.groupName").value(DEFAULT_GROUP_NAME))
            .andExpect(jsonPath("$.schoolName").value(DEFAULT_SCHOOL_NAME))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.afflNumber").value(DEFAULT_AFFL_NUMBER))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.lastModified").value(DEFAULT_LAST_MODIFIED.toString()))
            .andExpect(jsonPath("$.cancelDate").value(DEFAULT_CANCEL_DATE.toString()));
    }

    @Test
    @Transactional
    void getSchoolsByIdFiltering() throws Exception {
        // Initialize the database
        schoolRepository.saveAndFlush(school);

        Long id = school.getId();

        defaultSchoolShouldBeFound("id.equals=" + id);
        defaultSchoolShouldNotBeFound("id.notEquals=" + id);

        defaultSchoolShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSchoolShouldNotBeFound("id.greaterThan=" + id);

        defaultSchoolShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSchoolShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSchoolsByGroupNameIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolRepository.saveAndFlush(school);

        // Get all the schoolList where groupName equals to DEFAULT_GROUP_NAME
        defaultSchoolShouldBeFound("groupName.equals=" + DEFAULT_GROUP_NAME);

        // Get all the schoolList where groupName equals to UPDATED_GROUP_NAME
        defaultSchoolShouldNotBeFound("groupName.equals=" + UPDATED_GROUP_NAME);
    }

    @Test
    @Transactional
    void getAllSchoolsByGroupNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        schoolRepository.saveAndFlush(school);

        // Get all the schoolList where groupName not equals to DEFAULT_GROUP_NAME
        defaultSchoolShouldNotBeFound("groupName.notEquals=" + DEFAULT_GROUP_NAME);

        // Get all the schoolList where groupName not equals to UPDATED_GROUP_NAME
        defaultSchoolShouldBeFound("groupName.notEquals=" + UPDATED_GROUP_NAME);
    }

    @Test
    @Transactional
    void getAllSchoolsByGroupNameIsInShouldWork() throws Exception {
        // Initialize the database
        schoolRepository.saveAndFlush(school);

        // Get all the schoolList where groupName in DEFAULT_GROUP_NAME or UPDATED_GROUP_NAME
        defaultSchoolShouldBeFound("groupName.in=" + DEFAULT_GROUP_NAME + "," + UPDATED_GROUP_NAME);

        // Get all the schoolList where groupName equals to UPDATED_GROUP_NAME
        defaultSchoolShouldNotBeFound("groupName.in=" + UPDATED_GROUP_NAME);
    }

    @Test
    @Transactional
    void getAllSchoolsByGroupNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolRepository.saveAndFlush(school);

        // Get all the schoolList where groupName is not null
        defaultSchoolShouldBeFound("groupName.specified=true");

        // Get all the schoolList where groupName is null
        defaultSchoolShouldNotBeFound("groupName.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolsByGroupNameContainsSomething() throws Exception {
        // Initialize the database
        schoolRepository.saveAndFlush(school);

        // Get all the schoolList where groupName contains DEFAULT_GROUP_NAME
        defaultSchoolShouldBeFound("groupName.contains=" + DEFAULT_GROUP_NAME);

        // Get all the schoolList where groupName contains UPDATED_GROUP_NAME
        defaultSchoolShouldNotBeFound("groupName.contains=" + UPDATED_GROUP_NAME);
    }

    @Test
    @Transactional
    void getAllSchoolsByGroupNameNotContainsSomething() throws Exception {
        // Initialize the database
        schoolRepository.saveAndFlush(school);

        // Get all the schoolList where groupName does not contain DEFAULT_GROUP_NAME
        defaultSchoolShouldNotBeFound("groupName.doesNotContain=" + DEFAULT_GROUP_NAME);

        // Get all the schoolList where groupName does not contain UPDATED_GROUP_NAME
        defaultSchoolShouldBeFound("groupName.doesNotContain=" + UPDATED_GROUP_NAME);
    }

    @Test
    @Transactional
    void getAllSchoolsBySchoolNameIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolRepository.saveAndFlush(school);

        // Get all the schoolList where schoolName equals to DEFAULT_SCHOOL_NAME
        defaultSchoolShouldBeFound("schoolName.equals=" + DEFAULT_SCHOOL_NAME);

        // Get all the schoolList where schoolName equals to UPDATED_SCHOOL_NAME
        defaultSchoolShouldNotBeFound("schoolName.equals=" + UPDATED_SCHOOL_NAME);
    }

    @Test
    @Transactional
    void getAllSchoolsBySchoolNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        schoolRepository.saveAndFlush(school);

        // Get all the schoolList where schoolName not equals to DEFAULT_SCHOOL_NAME
        defaultSchoolShouldNotBeFound("schoolName.notEquals=" + DEFAULT_SCHOOL_NAME);

        // Get all the schoolList where schoolName not equals to UPDATED_SCHOOL_NAME
        defaultSchoolShouldBeFound("schoolName.notEquals=" + UPDATED_SCHOOL_NAME);
    }

    @Test
    @Transactional
    void getAllSchoolsBySchoolNameIsInShouldWork() throws Exception {
        // Initialize the database
        schoolRepository.saveAndFlush(school);

        // Get all the schoolList where schoolName in DEFAULT_SCHOOL_NAME or UPDATED_SCHOOL_NAME
        defaultSchoolShouldBeFound("schoolName.in=" + DEFAULT_SCHOOL_NAME + "," + UPDATED_SCHOOL_NAME);

        // Get all the schoolList where schoolName equals to UPDATED_SCHOOL_NAME
        defaultSchoolShouldNotBeFound("schoolName.in=" + UPDATED_SCHOOL_NAME);
    }

    @Test
    @Transactional
    void getAllSchoolsBySchoolNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolRepository.saveAndFlush(school);

        // Get all the schoolList where schoolName is not null
        defaultSchoolShouldBeFound("schoolName.specified=true");

        // Get all the schoolList where schoolName is null
        defaultSchoolShouldNotBeFound("schoolName.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolsBySchoolNameContainsSomething() throws Exception {
        // Initialize the database
        schoolRepository.saveAndFlush(school);

        // Get all the schoolList where schoolName contains DEFAULT_SCHOOL_NAME
        defaultSchoolShouldBeFound("schoolName.contains=" + DEFAULT_SCHOOL_NAME);

        // Get all the schoolList where schoolName contains UPDATED_SCHOOL_NAME
        defaultSchoolShouldNotBeFound("schoolName.contains=" + UPDATED_SCHOOL_NAME);
    }

    @Test
    @Transactional
    void getAllSchoolsBySchoolNameNotContainsSomething() throws Exception {
        // Initialize the database
        schoolRepository.saveAndFlush(school);

        // Get all the schoolList where schoolName does not contain DEFAULT_SCHOOL_NAME
        defaultSchoolShouldNotBeFound("schoolName.doesNotContain=" + DEFAULT_SCHOOL_NAME);

        // Get all the schoolList where schoolName does not contain UPDATED_SCHOOL_NAME
        defaultSchoolShouldBeFound("schoolName.doesNotContain=" + UPDATED_SCHOOL_NAME);
    }

    @Test
    @Transactional
    void getAllSchoolsByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolRepository.saveAndFlush(school);

        // Get all the schoolList where address equals to DEFAULT_ADDRESS
        defaultSchoolShouldBeFound("address.equals=" + DEFAULT_ADDRESS);

        // Get all the schoolList where address equals to UPDATED_ADDRESS
        defaultSchoolShouldNotBeFound("address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllSchoolsByAddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        schoolRepository.saveAndFlush(school);

        // Get all the schoolList where address not equals to DEFAULT_ADDRESS
        defaultSchoolShouldNotBeFound("address.notEquals=" + DEFAULT_ADDRESS);

        // Get all the schoolList where address not equals to UPDATED_ADDRESS
        defaultSchoolShouldBeFound("address.notEquals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllSchoolsByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        schoolRepository.saveAndFlush(school);

        // Get all the schoolList where address in DEFAULT_ADDRESS or UPDATED_ADDRESS
        defaultSchoolShouldBeFound("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS);

        // Get all the schoolList where address equals to UPDATED_ADDRESS
        defaultSchoolShouldNotBeFound("address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllSchoolsByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolRepository.saveAndFlush(school);

        // Get all the schoolList where address is not null
        defaultSchoolShouldBeFound("address.specified=true");

        // Get all the schoolList where address is null
        defaultSchoolShouldNotBeFound("address.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolsByAddressContainsSomething() throws Exception {
        // Initialize the database
        schoolRepository.saveAndFlush(school);

        // Get all the schoolList where address contains DEFAULT_ADDRESS
        defaultSchoolShouldBeFound("address.contains=" + DEFAULT_ADDRESS);

        // Get all the schoolList where address contains UPDATED_ADDRESS
        defaultSchoolShouldNotBeFound("address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllSchoolsByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        schoolRepository.saveAndFlush(school);

        // Get all the schoolList where address does not contain DEFAULT_ADDRESS
        defaultSchoolShouldNotBeFound("address.doesNotContain=" + DEFAULT_ADDRESS);

        // Get all the schoolList where address does not contain UPDATED_ADDRESS
        defaultSchoolShouldBeFound("address.doesNotContain=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllSchoolsByAfflNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolRepository.saveAndFlush(school);

        // Get all the schoolList where afflNumber equals to DEFAULT_AFFL_NUMBER
        defaultSchoolShouldBeFound("afflNumber.equals=" + DEFAULT_AFFL_NUMBER);

        // Get all the schoolList where afflNumber equals to UPDATED_AFFL_NUMBER
        defaultSchoolShouldNotBeFound("afflNumber.equals=" + UPDATED_AFFL_NUMBER);
    }

    @Test
    @Transactional
    void getAllSchoolsByAfflNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        schoolRepository.saveAndFlush(school);

        // Get all the schoolList where afflNumber not equals to DEFAULT_AFFL_NUMBER
        defaultSchoolShouldNotBeFound("afflNumber.notEquals=" + DEFAULT_AFFL_NUMBER);

        // Get all the schoolList where afflNumber not equals to UPDATED_AFFL_NUMBER
        defaultSchoolShouldBeFound("afflNumber.notEquals=" + UPDATED_AFFL_NUMBER);
    }

    @Test
    @Transactional
    void getAllSchoolsByAfflNumberIsInShouldWork() throws Exception {
        // Initialize the database
        schoolRepository.saveAndFlush(school);

        // Get all the schoolList where afflNumber in DEFAULT_AFFL_NUMBER or UPDATED_AFFL_NUMBER
        defaultSchoolShouldBeFound("afflNumber.in=" + DEFAULT_AFFL_NUMBER + "," + UPDATED_AFFL_NUMBER);

        // Get all the schoolList where afflNumber equals to UPDATED_AFFL_NUMBER
        defaultSchoolShouldNotBeFound("afflNumber.in=" + UPDATED_AFFL_NUMBER);
    }

    @Test
    @Transactional
    void getAllSchoolsByAfflNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolRepository.saveAndFlush(school);

        // Get all the schoolList where afflNumber is not null
        defaultSchoolShouldBeFound("afflNumber.specified=true");

        // Get all the schoolList where afflNumber is null
        defaultSchoolShouldNotBeFound("afflNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolsByAfflNumberContainsSomething() throws Exception {
        // Initialize the database
        schoolRepository.saveAndFlush(school);

        // Get all the schoolList where afflNumber contains DEFAULT_AFFL_NUMBER
        defaultSchoolShouldBeFound("afflNumber.contains=" + DEFAULT_AFFL_NUMBER);

        // Get all the schoolList where afflNumber contains UPDATED_AFFL_NUMBER
        defaultSchoolShouldNotBeFound("afflNumber.contains=" + UPDATED_AFFL_NUMBER);
    }

    @Test
    @Transactional
    void getAllSchoolsByAfflNumberNotContainsSomething() throws Exception {
        // Initialize the database
        schoolRepository.saveAndFlush(school);

        // Get all the schoolList where afflNumber does not contain DEFAULT_AFFL_NUMBER
        defaultSchoolShouldNotBeFound("afflNumber.doesNotContain=" + DEFAULT_AFFL_NUMBER);

        // Get all the schoolList where afflNumber does not contain UPDATED_AFFL_NUMBER
        defaultSchoolShouldBeFound("afflNumber.doesNotContain=" + UPDATED_AFFL_NUMBER);
    }

    @Test
    @Transactional
    void getAllSchoolsByCreateDateIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolRepository.saveAndFlush(school);

        // Get all the schoolList where createDate equals to DEFAULT_CREATE_DATE
        defaultSchoolShouldBeFound("createDate.equals=" + DEFAULT_CREATE_DATE);

        // Get all the schoolList where createDate equals to UPDATED_CREATE_DATE
        defaultSchoolShouldNotBeFound("createDate.equals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolsByCreateDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        schoolRepository.saveAndFlush(school);

        // Get all the schoolList where createDate not equals to DEFAULT_CREATE_DATE
        defaultSchoolShouldNotBeFound("createDate.notEquals=" + DEFAULT_CREATE_DATE);

        // Get all the schoolList where createDate not equals to UPDATED_CREATE_DATE
        defaultSchoolShouldBeFound("createDate.notEquals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolsByCreateDateIsInShouldWork() throws Exception {
        // Initialize the database
        schoolRepository.saveAndFlush(school);

        // Get all the schoolList where createDate in DEFAULT_CREATE_DATE or UPDATED_CREATE_DATE
        defaultSchoolShouldBeFound("createDate.in=" + DEFAULT_CREATE_DATE + "," + UPDATED_CREATE_DATE);

        // Get all the schoolList where createDate equals to UPDATED_CREATE_DATE
        defaultSchoolShouldNotBeFound("createDate.in=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolsByCreateDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolRepository.saveAndFlush(school);

        // Get all the schoolList where createDate is not null
        defaultSchoolShouldBeFound("createDate.specified=true");

        // Get all the schoolList where createDate is null
        defaultSchoolShouldNotBeFound("createDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolsByCreateDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        schoolRepository.saveAndFlush(school);

        // Get all the schoolList where createDate is greater than or equal to DEFAULT_CREATE_DATE
        defaultSchoolShouldBeFound("createDate.greaterThanOrEqual=" + DEFAULT_CREATE_DATE);

        // Get all the schoolList where createDate is greater than or equal to UPDATED_CREATE_DATE
        defaultSchoolShouldNotBeFound("createDate.greaterThanOrEqual=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolsByCreateDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        schoolRepository.saveAndFlush(school);

        // Get all the schoolList where createDate is less than or equal to DEFAULT_CREATE_DATE
        defaultSchoolShouldBeFound("createDate.lessThanOrEqual=" + DEFAULT_CREATE_DATE);

        // Get all the schoolList where createDate is less than or equal to SMALLER_CREATE_DATE
        defaultSchoolShouldNotBeFound("createDate.lessThanOrEqual=" + SMALLER_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolsByCreateDateIsLessThanSomething() throws Exception {
        // Initialize the database
        schoolRepository.saveAndFlush(school);

        // Get all the schoolList where createDate is less than DEFAULT_CREATE_DATE
        defaultSchoolShouldNotBeFound("createDate.lessThan=" + DEFAULT_CREATE_DATE);

        // Get all the schoolList where createDate is less than UPDATED_CREATE_DATE
        defaultSchoolShouldBeFound("createDate.lessThan=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolsByCreateDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        schoolRepository.saveAndFlush(school);

        // Get all the schoolList where createDate is greater than DEFAULT_CREATE_DATE
        defaultSchoolShouldNotBeFound("createDate.greaterThan=" + DEFAULT_CREATE_DATE);

        // Get all the schoolList where createDate is greater than SMALLER_CREATE_DATE
        defaultSchoolShouldBeFound("createDate.greaterThan=" + SMALLER_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolsByLastModifiedIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolRepository.saveAndFlush(school);

        // Get all the schoolList where lastModified equals to DEFAULT_LAST_MODIFIED
        defaultSchoolShouldBeFound("lastModified.equals=" + DEFAULT_LAST_MODIFIED);

        // Get all the schoolList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultSchoolShouldNotBeFound("lastModified.equals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSchoolsByLastModifiedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        schoolRepository.saveAndFlush(school);

        // Get all the schoolList where lastModified not equals to DEFAULT_LAST_MODIFIED
        defaultSchoolShouldNotBeFound("lastModified.notEquals=" + DEFAULT_LAST_MODIFIED);

        // Get all the schoolList where lastModified not equals to UPDATED_LAST_MODIFIED
        defaultSchoolShouldBeFound("lastModified.notEquals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSchoolsByLastModifiedIsInShouldWork() throws Exception {
        // Initialize the database
        schoolRepository.saveAndFlush(school);

        // Get all the schoolList where lastModified in DEFAULT_LAST_MODIFIED or UPDATED_LAST_MODIFIED
        defaultSchoolShouldBeFound("lastModified.in=" + DEFAULT_LAST_MODIFIED + "," + UPDATED_LAST_MODIFIED);

        // Get all the schoolList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultSchoolShouldNotBeFound("lastModified.in=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSchoolsByLastModifiedIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolRepository.saveAndFlush(school);

        // Get all the schoolList where lastModified is not null
        defaultSchoolShouldBeFound("lastModified.specified=true");

        // Get all the schoolList where lastModified is null
        defaultSchoolShouldNotBeFound("lastModified.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolsByLastModifiedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        schoolRepository.saveAndFlush(school);

        // Get all the schoolList where lastModified is greater than or equal to DEFAULT_LAST_MODIFIED
        defaultSchoolShouldBeFound("lastModified.greaterThanOrEqual=" + DEFAULT_LAST_MODIFIED);

        // Get all the schoolList where lastModified is greater than or equal to UPDATED_LAST_MODIFIED
        defaultSchoolShouldNotBeFound("lastModified.greaterThanOrEqual=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSchoolsByLastModifiedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        schoolRepository.saveAndFlush(school);

        // Get all the schoolList where lastModified is less than or equal to DEFAULT_LAST_MODIFIED
        defaultSchoolShouldBeFound("lastModified.lessThanOrEqual=" + DEFAULT_LAST_MODIFIED);

        // Get all the schoolList where lastModified is less than or equal to SMALLER_LAST_MODIFIED
        defaultSchoolShouldNotBeFound("lastModified.lessThanOrEqual=" + SMALLER_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSchoolsByLastModifiedIsLessThanSomething() throws Exception {
        // Initialize the database
        schoolRepository.saveAndFlush(school);

        // Get all the schoolList where lastModified is less than DEFAULT_LAST_MODIFIED
        defaultSchoolShouldNotBeFound("lastModified.lessThan=" + DEFAULT_LAST_MODIFIED);

        // Get all the schoolList where lastModified is less than UPDATED_LAST_MODIFIED
        defaultSchoolShouldBeFound("lastModified.lessThan=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSchoolsByLastModifiedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        schoolRepository.saveAndFlush(school);

        // Get all the schoolList where lastModified is greater than DEFAULT_LAST_MODIFIED
        defaultSchoolShouldNotBeFound("lastModified.greaterThan=" + DEFAULT_LAST_MODIFIED);

        // Get all the schoolList where lastModified is greater than SMALLER_LAST_MODIFIED
        defaultSchoolShouldBeFound("lastModified.greaterThan=" + SMALLER_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSchoolsByCancelDateIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolRepository.saveAndFlush(school);

        // Get all the schoolList where cancelDate equals to DEFAULT_CANCEL_DATE
        defaultSchoolShouldBeFound("cancelDate.equals=" + DEFAULT_CANCEL_DATE);

        // Get all the schoolList where cancelDate equals to UPDATED_CANCEL_DATE
        defaultSchoolShouldNotBeFound("cancelDate.equals=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolsByCancelDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        schoolRepository.saveAndFlush(school);

        // Get all the schoolList where cancelDate not equals to DEFAULT_CANCEL_DATE
        defaultSchoolShouldNotBeFound("cancelDate.notEquals=" + DEFAULT_CANCEL_DATE);

        // Get all the schoolList where cancelDate not equals to UPDATED_CANCEL_DATE
        defaultSchoolShouldBeFound("cancelDate.notEquals=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolsByCancelDateIsInShouldWork() throws Exception {
        // Initialize the database
        schoolRepository.saveAndFlush(school);

        // Get all the schoolList where cancelDate in DEFAULT_CANCEL_DATE or UPDATED_CANCEL_DATE
        defaultSchoolShouldBeFound("cancelDate.in=" + DEFAULT_CANCEL_DATE + "," + UPDATED_CANCEL_DATE);

        // Get all the schoolList where cancelDate equals to UPDATED_CANCEL_DATE
        defaultSchoolShouldNotBeFound("cancelDate.in=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolsByCancelDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolRepository.saveAndFlush(school);

        // Get all the schoolList where cancelDate is not null
        defaultSchoolShouldBeFound("cancelDate.specified=true");

        // Get all the schoolList where cancelDate is null
        defaultSchoolShouldNotBeFound("cancelDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolsByCancelDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        schoolRepository.saveAndFlush(school);

        // Get all the schoolList where cancelDate is greater than or equal to DEFAULT_CANCEL_DATE
        defaultSchoolShouldBeFound("cancelDate.greaterThanOrEqual=" + DEFAULT_CANCEL_DATE);

        // Get all the schoolList where cancelDate is greater than or equal to UPDATED_CANCEL_DATE
        defaultSchoolShouldNotBeFound("cancelDate.greaterThanOrEqual=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolsByCancelDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        schoolRepository.saveAndFlush(school);

        // Get all the schoolList where cancelDate is less than or equal to DEFAULT_CANCEL_DATE
        defaultSchoolShouldBeFound("cancelDate.lessThanOrEqual=" + DEFAULT_CANCEL_DATE);

        // Get all the schoolList where cancelDate is less than or equal to SMALLER_CANCEL_DATE
        defaultSchoolShouldNotBeFound("cancelDate.lessThanOrEqual=" + SMALLER_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolsByCancelDateIsLessThanSomething() throws Exception {
        // Initialize the database
        schoolRepository.saveAndFlush(school);

        // Get all the schoolList where cancelDate is less than DEFAULT_CANCEL_DATE
        defaultSchoolShouldNotBeFound("cancelDate.lessThan=" + DEFAULT_CANCEL_DATE);

        // Get all the schoolList where cancelDate is less than UPDATED_CANCEL_DATE
        defaultSchoolShouldBeFound("cancelDate.lessThan=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolsByCancelDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        schoolRepository.saveAndFlush(school);

        // Get all the schoolList where cancelDate is greater than DEFAULT_CANCEL_DATE
        defaultSchoolShouldNotBeFound("cancelDate.greaterThan=" + DEFAULT_CANCEL_DATE);

        // Get all the schoolList where cancelDate is greater than SMALLER_CANCEL_DATE
        defaultSchoolShouldBeFound("cancelDate.greaterThan=" + SMALLER_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolsBySchoolClassIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolRepository.saveAndFlush(school);
        SchoolClass schoolClass = SchoolClassResourceIT.createEntity(em);
        em.persist(schoolClass);
        em.flush();
        school.addSchoolClass(schoolClass);
        schoolRepository.saveAndFlush(school);
        Long schoolClassId = schoolClass.getId();

        // Get all the schoolList where schoolClass equals to schoolClassId
        defaultSchoolShouldBeFound("schoolClassId.equals=" + schoolClassId);

        // Get all the schoolList where schoolClass equals to (schoolClassId + 1)
        defaultSchoolShouldNotBeFound("schoolClassId.equals=" + (schoolClassId + 1));
    }

    @Test
    @Transactional
    void getAllSchoolsBySchoolLedgerHeadIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolRepository.saveAndFlush(school);
        SchoolLedgerHead schoolLedgerHead = SchoolLedgerHeadResourceIT.createEntity(em);
        em.persist(schoolLedgerHead);
        em.flush();
        school.addSchoolLedgerHead(schoolLedgerHead);
        schoolRepository.saveAndFlush(school);
        Long schoolLedgerHeadId = schoolLedgerHead.getId();

        // Get all the schoolList where schoolLedgerHead equals to schoolLedgerHeadId
        defaultSchoolShouldBeFound("schoolLedgerHeadId.equals=" + schoolLedgerHeadId);

        // Get all the schoolList where schoolLedgerHead equals to (schoolLedgerHeadId + 1)
        defaultSchoolShouldNotBeFound("schoolLedgerHeadId.equals=" + (schoolLedgerHeadId + 1));
    }

    @Test
    @Transactional
    void getAllSchoolsByIdStoreIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolRepository.saveAndFlush(school);
        IdStore idStore = IdStoreResourceIT.createEntity(em);
        em.persist(idStore);
        em.flush();
        school.addIdStore(idStore);
        schoolRepository.saveAndFlush(school);
        Long idStoreId = idStore.getId();

        // Get all the schoolList where idStore equals to idStoreId
        defaultSchoolShouldBeFound("idStoreId.equals=" + idStoreId);

        // Get all the schoolList where idStore equals to (idStoreId + 1)
        defaultSchoolShouldNotBeFound("idStoreId.equals=" + (idStoreId + 1));
    }

    @Test
    @Transactional
    void getAllSchoolsByAuditLogIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolRepository.saveAndFlush(school);
        AuditLog auditLog = AuditLogResourceIT.createEntity(em);
        em.persist(auditLog);
        em.flush();
        school.addAuditLog(auditLog);
        schoolRepository.saveAndFlush(school);
        Long auditLogId = auditLog.getId();

        // Get all the schoolList where auditLog equals to auditLogId
        defaultSchoolShouldBeFound("auditLogId.equals=" + auditLogId);

        // Get all the schoolList where auditLog equals to (auditLogId + 1)
        defaultSchoolShouldNotBeFound("auditLogId.equals=" + (auditLogId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSchoolShouldBeFound(String filter) throws Exception {
        restSchoolMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(school.getId().intValue())))
            .andExpect(jsonPath("$.[*].groupName").value(hasItem(DEFAULT_GROUP_NAME)))
            .andExpect(jsonPath("$.[*].schoolName").value(hasItem(DEFAULT_SCHOOL_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].afflNumber").value(hasItem(DEFAULT_AFFL_NUMBER)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].cancelDate").value(hasItem(DEFAULT_CANCEL_DATE.toString())));

        // Check, that the count call also returns 1
        restSchoolMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSchoolShouldNotBeFound(String filter) throws Exception {
        restSchoolMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSchoolMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSchool() throws Exception {
        // Get the school
        restSchoolMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSchool() throws Exception {
        // Initialize the database
        schoolRepository.saveAndFlush(school);

        int databaseSizeBeforeUpdate = schoolRepository.findAll().size();

        // Update the school
        School updatedSchool = schoolRepository.findById(school.getId()).get();
        // Disconnect from session so that the updates on updatedSchool are not directly saved in db
        em.detach(updatedSchool);
        updatedSchool
            .groupName(UPDATED_GROUP_NAME)
            .schoolName(UPDATED_SCHOOL_NAME)
            .address(UPDATED_ADDRESS)
            .afflNumber(UPDATED_AFFL_NUMBER)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);
        SchoolDTO schoolDTO = schoolMapper.toDto(updatedSchool);

        restSchoolMockMvc
            .perform(
                put(ENTITY_API_URL_ID, schoolDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schoolDTO))
            )
            .andExpect(status().isOk());

        // Validate the School in the database
        List<School> schoolList = schoolRepository.findAll();
        assertThat(schoolList).hasSize(databaseSizeBeforeUpdate);
        School testSchool = schoolList.get(schoolList.size() - 1);
        assertThat(testSchool.getGroupName()).isEqualTo(UPDATED_GROUP_NAME);
        assertThat(testSchool.getSchoolName()).isEqualTo(UPDATED_SCHOOL_NAME);
        assertThat(testSchool.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testSchool.getAfflNumber()).isEqualTo(UPDATED_AFFL_NUMBER);
        assertThat(testSchool.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testSchool.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testSchool.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void putNonExistingSchool() throws Exception {
        int databaseSizeBeforeUpdate = schoolRepository.findAll().size();
        school.setId(count.incrementAndGet());

        // Create the School
        SchoolDTO schoolDTO = schoolMapper.toDto(school);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSchoolMockMvc
            .perform(
                put(ENTITY_API_URL_ID, schoolDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schoolDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the School in the database
        List<School> schoolList = schoolRepository.findAll();
        assertThat(schoolList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSchool() throws Exception {
        int databaseSizeBeforeUpdate = schoolRepository.findAll().size();
        school.setId(count.incrementAndGet());

        // Create the School
        SchoolDTO schoolDTO = schoolMapper.toDto(school);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchoolMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schoolDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the School in the database
        List<School> schoolList = schoolRepository.findAll();
        assertThat(schoolList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSchool() throws Exception {
        int databaseSizeBeforeUpdate = schoolRepository.findAll().size();
        school.setId(count.incrementAndGet());

        // Create the School
        SchoolDTO schoolDTO = schoolMapper.toDto(school);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchoolMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(schoolDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the School in the database
        List<School> schoolList = schoolRepository.findAll();
        assertThat(schoolList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSchoolWithPatch() throws Exception {
        // Initialize the database
        schoolRepository.saveAndFlush(school);

        int databaseSizeBeforeUpdate = schoolRepository.findAll().size();

        // Update the school using partial update
        School partialUpdatedSchool = new School();
        partialUpdatedSchool.setId(school.getId());

        partialUpdatedSchool.groupName(UPDATED_GROUP_NAME).createDate(UPDATED_CREATE_DATE);

        restSchoolMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSchool.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSchool))
            )
            .andExpect(status().isOk());

        // Validate the School in the database
        List<School> schoolList = schoolRepository.findAll();
        assertThat(schoolList).hasSize(databaseSizeBeforeUpdate);
        School testSchool = schoolList.get(schoolList.size() - 1);
        assertThat(testSchool.getGroupName()).isEqualTo(UPDATED_GROUP_NAME);
        assertThat(testSchool.getSchoolName()).isEqualTo(DEFAULT_SCHOOL_NAME);
        assertThat(testSchool.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testSchool.getAfflNumber()).isEqualTo(DEFAULT_AFFL_NUMBER);
        assertThat(testSchool.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testSchool.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
        assertThat(testSchool.getCancelDate()).isEqualTo(DEFAULT_CANCEL_DATE);
    }

    @Test
    @Transactional
    void fullUpdateSchoolWithPatch() throws Exception {
        // Initialize the database
        schoolRepository.saveAndFlush(school);

        int databaseSizeBeforeUpdate = schoolRepository.findAll().size();

        // Update the school using partial update
        School partialUpdatedSchool = new School();
        partialUpdatedSchool.setId(school.getId());

        partialUpdatedSchool
            .groupName(UPDATED_GROUP_NAME)
            .schoolName(UPDATED_SCHOOL_NAME)
            .address(UPDATED_ADDRESS)
            .afflNumber(UPDATED_AFFL_NUMBER)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);

        restSchoolMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSchool.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSchool))
            )
            .andExpect(status().isOk());

        // Validate the School in the database
        List<School> schoolList = schoolRepository.findAll();
        assertThat(schoolList).hasSize(databaseSizeBeforeUpdate);
        School testSchool = schoolList.get(schoolList.size() - 1);
        assertThat(testSchool.getGroupName()).isEqualTo(UPDATED_GROUP_NAME);
        assertThat(testSchool.getSchoolName()).isEqualTo(UPDATED_SCHOOL_NAME);
        assertThat(testSchool.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testSchool.getAfflNumber()).isEqualTo(UPDATED_AFFL_NUMBER);
        assertThat(testSchool.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testSchool.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testSchool.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingSchool() throws Exception {
        int databaseSizeBeforeUpdate = schoolRepository.findAll().size();
        school.setId(count.incrementAndGet());

        // Create the School
        SchoolDTO schoolDTO = schoolMapper.toDto(school);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSchoolMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, schoolDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(schoolDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the School in the database
        List<School> schoolList = schoolRepository.findAll();
        assertThat(schoolList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSchool() throws Exception {
        int databaseSizeBeforeUpdate = schoolRepository.findAll().size();
        school.setId(count.incrementAndGet());

        // Create the School
        SchoolDTO schoolDTO = schoolMapper.toDto(school);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchoolMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(schoolDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the School in the database
        List<School> schoolList = schoolRepository.findAll();
        assertThat(schoolList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSchool() throws Exception {
        int databaseSizeBeforeUpdate = schoolRepository.findAll().size();
        school.setId(count.incrementAndGet());

        // Create the School
        SchoolDTO schoolDTO = schoolMapper.toDto(school);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchoolMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(schoolDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the School in the database
        List<School> schoolList = schoolRepository.findAll();
        assertThat(schoolList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSchool() throws Exception {
        // Initialize the database
        schoolRepository.saveAndFlush(school);

        int databaseSizeBeforeDelete = schoolRepository.findAll().size();

        // Delete the school
        restSchoolMockMvc
            .perform(delete(ENTITY_API_URL_ID, school.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<School> schoolList = schoolRepository.findAll();
        assertThat(schoolList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
