package com.ssik.manageit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ssik.manageit.IntegrationTest;
import com.ssik.manageit.domain.ClassFee;
import com.ssik.manageit.domain.ClassLessionPlan;
import com.ssik.manageit.domain.ClassStudent;
import com.ssik.manageit.domain.ClassSubject;
import com.ssik.manageit.domain.School;
import com.ssik.manageit.domain.SchoolClass;
import com.ssik.manageit.domain.SchoolDaysOff;
import com.ssik.manageit.domain.SchoolEvent;
import com.ssik.manageit.domain.SchoolNotifications;
import com.ssik.manageit.domain.SchoolPictureGallery;
import com.ssik.manageit.domain.SchoolReport;
import com.ssik.manageit.domain.SchoolUser;
import com.ssik.manageit.domain.SchoolVideoGallery;
import com.ssik.manageit.repository.SchoolClassRepository;
import com.ssik.manageit.service.criteria.SchoolClassCriteria;
import com.ssik.manageit.service.dto.SchoolClassDTO;
import com.ssik.manageit.service.mapper.SchoolClassMapper;
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
 * Integration tests for the {@link SchoolClassResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SchoolClassResourceIT {

    private static final String DEFAULT_CLASS_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CLASS_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CLASS_LONG_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CLASS_LONG_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATE_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CREATE_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_LAST_MODIFIED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_MODIFIED = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_LAST_MODIFIED = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_CANCEL_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CANCEL_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CANCEL_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/school-classes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SchoolClassRepository schoolClassRepository;

    @Autowired
    private SchoolClassMapper schoolClassMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSchoolClassMockMvc;

    private SchoolClass schoolClass;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SchoolClass createEntity(EntityManager em) {
        SchoolClass schoolClass = new SchoolClass()
            .className(DEFAULT_CLASS_NAME)
            .classLongName(DEFAULT_CLASS_LONG_NAME)
            .createDate(DEFAULT_CREATE_DATE)
            .lastModified(DEFAULT_LAST_MODIFIED)
            .cancelDate(DEFAULT_CANCEL_DATE);
        return schoolClass;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SchoolClass createUpdatedEntity(EntityManager em) {
        SchoolClass schoolClass = new SchoolClass()
            .className(UPDATED_CLASS_NAME)
            .classLongName(UPDATED_CLASS_LONG_NAME)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);
        return schoolClass;
    }

    @BeforeEach
    public void initTest() {
        schoolClass = createEntity(em);
    }

    @Test
    @Transactional
    void createSchoolClass() throws Exception {
        int databaseSizeBeforeCreate = schoolClassRepository.findAll().size();
        // Create the SchoolClass
        SchoolClassDTO schoolClassDTO = schoolClassMapper.toDto(schoolClass);
        restSchoolClassMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(schoolClassDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SchoolClass in the database
        List<SchoolClass> schoolClassList = schoolClassRepository.findAll();
        assertThat(schoolClassList).hasSize(databaseSizeBeforeCreate + 1);
        SchoolClass testSchoolClass = schoolClassList.get(schoolClassList.size() - 1);
        assertThat(testSchoolClass.getClassName()).isEqualTo(DEFAULT_CLASS_NAME);
        assertThat(testSchoolClass.getClassLongName()).isEqualTo(DEFAULT_CLASS_LONG_NAME);
        assertThat(testSchoolClass.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testSchoolClass.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
        assertThat(testSchoolClass.getCancelDate()).isEqualTo(DEFAULT_CANCEL_DATE);
    }

    @Test
    @Transactional
    void createSchoolClassWithExistingId() throws Exception {
        // Create the SchoolClass with an existing ID
        schoolClass.setId(1L);
        SchoolClassDTO schoolClassDTO = schoolClassMapper.toDto(schoolClass);

        int databaseSizeBeforeCreate = schoolClassRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSchoolClassMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(schoolClassDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchoolClass in the database
        List<SchoolClass> schoolClassList = schoolClassRepository.findAll();
        assertThat(schoolClassList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkClassNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = schoolClassRepository.findAll().size();
        // set the field null
        schoolClass.setClassName(null);

        // Create the SchoolClass, which fails.
        SchoolClassDTO schoolClassDTO = schoolClassMapper.toDto(schoolClass);

        restSchoolClassMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(schoolClassDTO))
            )
            .andExpect(status().isBadRequest());

        List<SchoolClass> schoolClassList = schoolClassRepository.findAll();
        assertThat(schoolClassList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSchoolClasses() throws Exception {
        // Initialize the database
        schoolClassRepository.saveAndFlush(schoolClass);

        // Get all the schoolClassList
        restSchoolClassMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(schoolClass.getId().intValue())))
            .andExpect(jsonPath("$.[*].className").value(hasItem(DEFAULT_CLASS_NAME)))
            .andExpect(jsonPath("$.[*].classLongName").value(hasItem(DEFAULT_CLASS_LONG_NAME)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].cancelDate").value(hasItem(DEFAULT_CANCEL_DATE.toString())));
    }

    @Test
    @Transactional
    void getSchoolClass() throws Exception {
        // Initialize the database
        schoolClassRepository.saveAndFlush(schoolClass);

        // Get the schoolClass
        restSchoolClassMockMvc
            .perform(get(ENTITY_API_URL_ID, schoolClass.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(schoolClass.getId().intValue()))
            .andExpect(jsonPath("$.className").value(DEFAULT_CLASS_NAME))
            .andExpect(jsonPath("$.classLongName").value(DEFAULT_CLASS_LONG_NAME))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.lastModified").value(DEFAULT_LAST_MODIFIED.toString()))
            .andExpect(jsonPath("$.cancelDate").value(DEFAULT_CANCEL_DATE.toString()));
    }

    @Test
    @Transactional
    void getSchoolClassesByIdFiltering() throws Exception {
        // Initialize the database
        schoolClassRepository.saveAndFlush(schoolClass);

        Long id = schoolClass.getId();

        defaultSchoolClassShouldBeFound("id.equals=" + id);
        defaultSchoolClassShouldNotBeFound("id.notEquals=" + id);

        defaultSchoolClassShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSchoolClassShouldNotBeFound("id.greaterThan=" + id);

        defaultSchoolClassShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSchoolClassShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSchoolClassesByClassNameIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolClassRepository.saveAndFlush(schoolClass);

        // Get all the schoolClassList where className equals to DEFAULT_CLASS_NAME
        defaultSchoolClassShouldBeFound("className.equals=" + DEFAULT_CLASS_NAME);

        // Get all the schoolClassList where className equals to UPDATED_CLASS_NAME
        defaultSchoolClassShouldNotBeFound("className.equals=" + UPDATED_CLASS_NAME);
    }

    @Test
    @Transactional
    void getAllSchoolClassesByClassNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        schoolClassRepository.saveAndFlush(schoolClass);

        // Get all the schoolClassList where className not equals to DEFAULT_CLASS_NAME
        defaultSchoolClassShouldNotBeFound("className.notEquals=" + DEFAULT_CLASS_NAME);

        // Get all the schoolClassList where className not equals to UPDATED_CLASS_NAME
        defaultSchoolClassShouldBeFound("className.notEquals=" + UPDATED_CLASS_NAME);
    }

    @Test
    @Transactional
    void getAllSchoolClassesByClassNameIsInShouldWork() throws Exception {
        // Initialize the database
        schoolClassRepository.saveAndFlush(schoolClass);

        // Get all the schoolClassList where className in DEFAULT_CLASS_NAME or UPDATED_CLASS_NAME
        defaultSchoolClassShouldBeFound("className.in=" + DEFAULT_CLASS_NAME + "," + UPDATED_CLASS_NAME);

        // Get all the schoolClassList where className equals to UPDATED_CLASS_NAME
        defaultSchoolClassShouldNotBeFound("className.in=" + UPDATED_CLASS_NAME);
    }

    @Test
    @Transactional
    void getAllSchoolClassesByClassNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolClassRepository.saveAndFlush(schoolClass);

        // Get all the schoolClassList where className is not null
        defaultSchoolClassShouldBeFound("className.specified=true");

        // Get all the schoolClassList where className is null
        defaultSchoolClassShouldNotBeFound("className.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolClassesByClassNameContainsSomething() throws Exception {
        // Initialize the database
        schoolClassRepository.saveAndFlush(schoolClass);

        // Get all the schoolClassList where className contains DEFAULT_CLASS_NAME
        defaultSchoolClassShouldBeFound("className.contains=" + DEFAULT_CLASS_NAME);

        // Get all the schoolClassList where className contains UPDATED_CLASS_NAME
        defaultSchoolClassShouldNotBeFound("className.contains=" + UPDATED_CLASS_NAME);
    }

    @Test
    @Transactional
    void getAllSchoolClassesByClassNameNotContainsSomething() throws Exception {
        // Initialize the database
        schoolClassRepository.saveAndFlush(schoolClass);

        // Get all the schoolClassList where className does not contain DEFAULT_CLASS_NAME
        defaultSchoolClassShouldNotBeFound("className.doesNotContain=" + DEFAULT_CLASS_NAME);

        // Get all the schoolClassList where className does not contain UPDATED_CLASS_NAME
        defaultSchoolClassShouldBeFound("className.doesNotContain=" + UPDATED_CLASS_NAME);
    }

    @Test
    @Transactional
    void getAllSchoolClassesByClassLongNameIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolClassRepository.saveAndFlush(schoolClass);

        // Get all the schoolClassList where classLongName equals to DEFAULT_CLASS_LONG_NAME
        defaultSchoolClassShouldBeFound("classLongName.equals=" + DEFAULT_CLASS_LONG_NAME);

        // Get all the schoolClassList where classLongName equals to UPDATED_CLASS_LONG_NAME
        defaultSchoolClassShouldNotBeFound("classLongName.equals=" + UPDATED_CLASS_LONG_NAME);
    }

    @Test
    @Transactional
    void getAllSchoolClassesByClassLongNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        schoolClassRepository.saveAndFlush(schoolClass);

        // Get all the schoolClassList where classLongName not equals to DEFAULT_CLASS_LONG_NAME
        defaultSchoolClassShouldNotBeFound("classLongName.notEquals=" + DEFAULT_CLASS_LONG_NAME);

        // Get all the schoolClassList where classLongName not equals to UPDATED_CLASS_LONG_NAME
        defaultSchoolClassShouldBeFound("classLongName.notEquals=" + UPDATED_CLASS_LONG_NAME);
    }

    @Test
    @Transactional
    void getAllSchoolClassesByClassLongNameIsInShouldWork() throws Exception {
        // Initialize the database
        schoolClassRepository.saveAndFlush(schoolClass);

        // Get all the schoolClassList where classLongName in DEFAULT_CLASS_LONG_NAME or UPDATED_CLASS_LONG_NAME
        defaultSchoolClassShouldBeFound("classLongName.in=" + DEFAULT_CLASS_LONG_NAME + "," + UPDATED_CLASS_LONG_NAME);

        // Get all the schoolClassList where classLongName equals to UPDATED_CLASS_LONG_NAME
        defaultSchoolClassShouldNotBeFound("classLongName.in=" + UPDATED_CLASS_LONG_NAME);
    }

    @Test
    @Transactional
    void getAllSchoolClassesByClassLongNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolClassRepository.saveAndFlush(schoolClass);

        // Get all the schoolClassList where classLongName is not null
        defaultSchoolClassShouldBeFound("classLongName.specified=true");

        // Get all the schoolClassList where classLongName is null
        defaultSchoolClassShouldNotBeFound("classLongName.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolClassesByClassLongNameContainsSomething() throws Exception {
        // Initialize the database
        schoolClassRepository.saveAndFlush(schoolClass);

        // Get all the schoolClassList where classLongName contains DEFAULT_CLASS_LONG_NAME
        defaultSchoolClassShouldBeFound("classLongName.contains=" + DEFAULT_CLASS_LONG_NAME);

        // Get all the schoolClassList where classLongName contains UPDATED_CLASS_LONG_NAME
        defaultSchoolClassShouldNotBeFound("classLongName.contains=" + UPDATED_CLASS_LONG_NAME);
    }

    @Test
    @Transactional
    void getAllSchoolClassesByClassLongNameNotContainsSomething() throws Exception {
        // Initialize the database
        schoolClassRepository.saveAndFlush(schoolClass);

        // Get all the schoolClassList where classLongName does not contain DEFAULT_CLASS_LONG_NAME
        defaultSchoolClassShouldNotBeFound("classLongName.doesNotContain=" + DEFAULT_CLASS_LONG_NAME);

        // Get all the schoolClassList where classLongName does not contain UPDATED_CLASS_LONG_NAME
        defaultSchoolClassShouldBeFound("classLongName.doesNotContain=" + UPDATED_CLASS_LONG_NAME);
    }

    @Test
    @Transactional
    void getAllSchoolClassesByCreateDateIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolClassRepository.saveAndFlush(schoolClass);

        // Get all the schoolClassList where createDate equals to DEFAULT_CREATE_DATE
        defaultSchoolClassShouldBeFound("createDate.equals=" + DEFAULT_CREATE_DATE);

        // Get all the schoolClassList where createDate equals to UPDATED_CREATE_DATE
        defaultSchoolClassShouldNotBeFound("createDate.equals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolClassesByCreateDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        schoolClassRepository.saveAndFlush(schoolClass);

        // Get all the schoolClassList where createDate not equals to DEFAULT_CREATE_DATE
        defaultSchoolClassShouldNotBeFound("createDate.notEquals=" + DEFAULT_CREATE_DATE);

        // Get all the schoolClassList where createDate not equals to UPDATED_CREATE_DATE
        defaultSchoolClassShouldBeFound("createDate.notEquals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolClassesByCreateDateIsInShouldWork() throws Exception {
        // Initialize the database
        schoolClassRepository.saveAndFlush(schoolClass);

        // Get all the schoolClassList where createDate in DEFAULT_CREATE_DATE or UPDATED_CREATE_DATE
        defaultSchoolClassShouldBeFound("createDate.in=" + DEFAULT_CREATE_DATE + "," + UPDATED_CREATE_DATE);

        // Get all the schoolClassList where createDate equals to UPDATED_CREATE_DATE
        defaultSchoolClassShouldNotBeFound("createDate.in=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolClassesByCreateDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolClassRepository.saveAndFlush(schoolClass);

        // Get all the schoolClassList where createDate is not null
        defaultSchoolClassShouldBeFound("createDate.specified=true");

        // Get all the schoolClassList where createDate is null
        defaultSchoolClassShouldNotBeFound("createDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolClassesByCreateDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        schoolClassRepository.saveAndFlush(schoolClass);

        // Get all the schoolClassList where createDate is greater than or equal to DEFAULT_CREATE_DATE
        defaultSchoolClassShouldBeFound("createDate.greaterThanOrEqual=" + DEFAULT_CREATE_DATE);

        // Get all the schoolClassList where createDate is greater than or equal to UPDATED_CREATE_DATE
        defaultSchoolClassShouldNotBeFound("createDate.greaterThanOrEqual=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolClassesByCreateDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        schoolClassRepository.saveAndFlush(schoolClass);

        // Get all the schoolClassList where createDate is less than or equal to DEFAULT_CREATE_DATE
        defaultSchoolClassShouldBeFound("createDate.lessThanOrEqual=" + DEFAULT_CREATE_DATE);

        // Get all the schoolClassList where createDate is less than or equal to SMALLER_CREATE_DATE
        defaultSchoolClassShouldNotBeFound("createDate.lessThanOrEqual=" + SMALLER_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolClassesByCreateDateIsLessThanSomething() throws Exception {
        // Initialize the database
        schoolClassRepository.saveAndFlush(schoolClass);

        // Get all the schoolClassList where createDate is less than DEFAULT_CREATE_DATE
        defaultSchoolClassShouldNotBeFound("createDate.lessThan=" + DEFAULT_CREATE_DATE);

        // Get all the schoolClassList where createDate is less than UPDATED_CREATE_DATE
        defaultSchoolClassShouldBeFound("createDate.lessThan=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolClassesByCreateDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        schoolClassRepository.saveAndFlush(schoolClass);

        // Get all the schoolClassList where createDate is greater than DEFAULT_CREATE_DATE
        defaultSchoolClassShouldNotBeFound("createDate.greaterThan=" + DEFAULT_CREATE_DATE);

        // Get all the schoolClassList where createDate is greater than SMALLER_CREATE_DATE
        defaultSchoolClassShouldBeFound("createDate.greaterThan=" + SMALLER_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolClassesByLastModifiedIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolClassRepository.saveAndFlush(schoolClass);

        // Get all the schoolClassList where lastModified equals to DEFAULT_LAST_MODIFIED
        defaultSchoolClassShouldBeFound("lastModified.equals=" + DEFAULT_LAST_MODIFIED);

        // Get all the schoolClassList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultSchoolClassShouldNotBeFound("lastModified.equals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSchoolClassesByLastModifiedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        schoolClassRepository.saveAndFlush(schoolClass);

        // Get all the schoolClassList where lastModified not equals to DEFAULT_LAST_MODIFIED
        defaultSchoolClassShouldNotBeFound("lastModified.notEquals=" + DEFAULT_LAST_MODIFIED);

        // Get all the schoolClassList where lastModified not equals to UPDATED_LAST_MODIFIED
        defaultSchoolClassShouldBeFound("lastModified.notEquals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSchoolClassesByLastModifiedIsInShouldWork() throws Exception {
        // Initialize the database
        schoolClassRepository.saveAndFlush(schoolClass);

        // Get all the schoolClassList where lastModified in DEFAULT_LAST_MODIFIED or UPDATED_LAST_MODIFIED
        defaultSchoolClassShouldBeFound("lastModified.in=" + DEFAULT_LAST_MODIFIED + "," + UPDATED_LAST_MODIFIED);

        // Get all the schoolClassList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultSchoolClassShouldNotBeFound("lastModified.in=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSchoolClassesByLastModifiedIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolClassRepository.saveAndFlush(schoolClass);

        // Get all the schoolClassList where lastModified is not null
        defaultSchoolClassShouldBeFound("lastModified.specified=true");

        // Get all the schoolClassList where lastModified is null
        defaultSchoolClassShouldNotBeFound("lastModified.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolClassesByLastModifiedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        schoolClassRepository.saveAndFlush(schoolClass);

        // Get all the schoolClassList where lastModified is greater than or equal to DEFAULT_LAST_MODIFIED
        defaultSchoolClassShouldBeFound("lastModified.greaterThanOrEqual=" + DEFAULT_LAST_MODIFIED);

        // Get all the schoolClassList where lastModified is greater than or equal to UPDATED_LAST_MODIFIED
        defaultSchoolClassShouldNotBeFound("lastModified.greaterThanOrEqual=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSchoolClassesByLastModifiedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        schoolClassRepository.saveAndFlush(schoolClass);

        // Get all the schoolClassList where lastModified is less than or equal to DEFAULT_LAST_MODIFIED
        defaultSchoolClassShouldBeFound("lastModified.lessThanOrEqual=" + DEFAULT_LAST_MODIFIED);

        // Get all the schoolClassList where lastModified is less than or equal to SMALLER_LAST_MODIFIED
        defaultSchoolClassShouldNotBeFound("lastModified.lessThanOrEqual=" + SMALLER_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSchoolClassesByLastModifiedIsLessThanSomething() throws Exception {
        // Initialize the database
        schoolClassRepository.saveAndFlush(schoolClass);

        // Get all the schoolClassList where lastModified is less than DEFAULT_LAST_MODIFIED
        defaultSchoolClassShouldNotBeFound("lastModified.lessThan=" + DEFAULT_LAST_MODIFIED);

        // Get all the schoolClassList where lastModified is less than UPDATED_LAST_MODIFIED
        defaultSchoolClassShouldBeFound("lastModified.lessThan=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSchoolClassesByLastModifiedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        schoolClassRepository.saveAndFlush(schoolClass);

        // Get all the schoolClassList where lastModified is greater than DEFAULT_LAST_MODIFIED
        defaultSchoolClassShouldNotBeFound("lastModified.greaterThan=" + DEFAULT_LAST_MODIFIED);

        // Get all the schoolClassList where lastModified is greater than SMALLER_LAST_MODIFIED
        defaultSchoolClassShouldBeFound("lastModified.greaterThan=" + SMALLER_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSchoolClassesByCancelDateIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolClassRepository.saveAndFlush(schoolClass);

        // Get all the schoolClassList where cancelDate equals to DEFAULT_CANCEL_DATE
        defaultSchoolClassShouldBeFound("cancelDate.equals=" + DEFAULT_CANCEL_DATE);

        // Get all the schoolClassList where cancelDate equals to UPDATED_CANCEL_DATE
        defaultSchoolClassShouldNotBeFound("cancelDate.equals=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolClassesByCancelDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        schoolClassRepository.saveAndFlush(schoolClass);

        // Get all the schoolClassList where cancelDate not equals to DEFAULT_CANCEL_DATE
        defaultSchoolClassShouldNotBeFound("cancelDate.notEquals=" + DEFAULT_CANCEL_DATE);

        // Get all the schoolClassList where cancelDate not equals to UPDATED_CANCEL_DATE
        defaultSchoolClassShouldBeFound("cancelDate.notEquals=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolClassesByCancelDateIsInShouldWork() throws Exception {
        // Initialize the database
        schoolClassRepository.saveAndFlush(schoolClass);

        // Get all the schoolClassList where cancelDate in DEFAULT_CANCEL_DATE or UPDATED_CANCEL_DATE
        defaultSchoolClassShouldBeFound("cancelDate.in=" + DEFAULT_CANCEL_DATE + "," + UPDATED_CANCEL_DATE);

        // Get all the schoolClassList where cancelDate equals to UPDATED_CANCEL_DATE
        defaultSchoolClassShouldNotBeFound("cancelDate.in=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolClassesByCancelDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolClassRepository.saveAndFlush(schoolClass);

        // Get all the schoolClassList where cancelDate is not null
        defaultSchoolClassShouldBeFound("cancelDate.specified=true");

        // Get all the schoolClassList where cancelDate is null
        defaultSchoolClassShouldNotBeFound("cancelDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolClassesByCancelDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        schoolClassRepository.saveAndFlush(schoolClass);

        // Get all the schoolClassList where cancelDate is greater than or equal to DEFAULT_CANCEL_DATE
        defaultSchoolClassShouldBeFound("cancelDate.greaterThanOrEqual=" + DEFAULT_CANCEL_DATE);

        // Get all the schoolClassList where cancelDate is greater than or equal to UPDATED_CANCEL_DATE
        defaultSchoolClassShouldNotBeFound("cancelDate.greaterThanOrEqual=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolClassesByCancelDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        schoolClassRepository.saveAndFlush(schoolClass);

        // Get all the schoolClassList where cancelDate is less than or equal to DEFAULT_CANCEL_DATE
        defaultSchoolClassShouldBeFound("cancelDate.lessThanOrEqual=" + DEFAULT_CANCEL_DATE);

        // Get all the schoolClassList where cancelDate is less than or equal to SMALLER_CANCEL_DATE
        defaultSchoolClassShouldNotBeFound("cancelDate.lessThanOrEqual=" + SMALLER_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolClassesByCancelDateIsLessThanSomething() throws Exception {
        // Initialize the database
        schoolClassRepository.saveAndFlush(schoolClass);

        // Get all the schoolClassList where cancelDate is less than DEFAULT_CANCEL_DATE
        defaultSchoolClassShouldNotBeFound("cancelDate.lessThan=" + DEFAULT_CANCEL_DATE);

        // Get all the schoolClassList where cancelDate is less than UPDATED_CANCEL_DATE
        defaultSchoolClassShouldBeFound("cancelDate.lessThan=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolClassesByCancelDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        schoolClassRepository.saveAndFlush(schoolClass);

        // Get all the schoolClassList where cancelDate is greater than DEFAULT_CANCEL_DATE
        defaultSchoolClassShouldNotBeFound("cancelDate.greaterThan=" + DEFAULT_CANCEL_DATE);

        // Get all the schoolClassList where cancelDate is greater than SMALLER_CANCEL_DATE
        defaultSchoolClassShouldBeFound("cancelDate.greaterThan=" + SMALLER_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolClassesByClassStudentIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolClassRepository.saveAndFlush(schoolClass);
        ClassStudent classStudent = ClassStudentResourceIT.createEntity(em);
        em.persist(classStudent);
        em.flush();
        schoolClass.addClassStudent(classStudent);
        schoolClassRepository.saveAndFlush(schoolClass);
        Long classStudentId = classStudent.getId();

        // Get all the schoolClassList where classStudent equals to classStudentId
        defaultSchoolClassShouldBeFound("classStudentId.equals=" + classStudentId);

        // Get all the schoolClassList where classStudent equals to (classStudentId + 1)
        defaultSchoolClassShouldNotBeFound("classStudentId.equals=" + (classStudentId + 1));
    }

    @Test
    @Transactional
    void getAllSchoolClassesByClassLessionPlanIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolClassRepository.saveAndFlush(schoolClass);
        ClassLessionPlan classLessionPlan = ClassLessionPlanResourceIT.createEntity(em);
        em.persist(classLessionPlan);
        em.flush();
        schoolClass.addClassLessionPlan(classLessionPlan);
        schoolClassRepository.saveAndFlush(schoolClass);
        Long classLessionPlanId = classLessionPlan.getId();

        // Get all the schoolClassList where classLessionPlan equals to classLessionPlanId
        defaultSchoolClassShouldBeFound("classLessionPlanId.equals=" + classLessionPlanId);

        // Get all the schoolClassList where classLessionPlan equals to (classLessionPlanId + 1)
        defaultSchoolClassShouldNotBeFound("classLessionPlanId.equals=" + (classLessionPlanId + 1));
    }

    @Test
    @Transactional
    void getAllSchoolClassesBySchoolIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolClassRepository.saveAndFlush(schoolClass);
        School school = SchoolResourceIT.createEntity(em);
        em.persist(school);
        em.flush();
        schoolClass.setSchool(school);
        schoolClassRepository.saveAndFlush(schoolClass);
        Long schoolId = school.getId();

        // Get all the schoolClassList where school equals to schoolId
        defaultSchoolClassShouldBeFound("schoolId.equals=" + schoolId);

        // Get all the schoolClassList where school equals to (schoolId + 1)
        defaultSchoolClassShouldNotBeFound("schoolId.equals=" + (schoolId + 1));
    }

    @Test
    @Transactional
    void getAllSchoolClassesBySchoolNotificationsIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolClassRepository.saveAndFlush(schoolClass);
        SchoolNotifications schoolNotifications = SchoolNotificationsResourceIT.createEntity(em);
        em.persist(schoolNotifications);
        em.flush();
        schoolClass.addSchoolNotifications(schoolNotifications);
        schoolClassRepository.saveAndFlush(schoolClass);
        Long schoolNotificationsId = schoolNotifications.getId();

        // Get all the schoolClassList where schoolNotifications equals to schoolNotificationsId
        defaultSchoolClassShouldBeFound("schoolNotificationsId.equals=" + schoolNotificationsId);

        // Get all the schoolClassList where schoolNotifications equals to (schoolNotificationsId + 1)
        defaultSchoolClassShouldNotBeFound("schoolNotificationsId.equals=" + (schoolNotificationsId + 1));
    }

    @Test
    @Transactional
    void getAllSchoolClassesByClassFeeIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolClassRepository.saveAndFlush(schoolClass);
        ClassFee classFee = ClassFeeResourceIT.createEntity(em);
        em.persist(classFee);
        em.flush();
        schoolClass.addClassFee(classFee);
        schoolClassRepository.saveAndFlush(schoolClass);
        Long classFeeId = classFee.getId();

        // Get all the schoolClassList where classFee equals to classFeeId
        defaultSchoolClassShouldBeFound("classFeeId.equals=" + classFeeId);

        // Get all the schoolClassList where classFee equals to (classFeeId + 1)
        defaultSchoolClassShouldNotBeFound("classFeeId.equals=" + (classFeeId + 1));
    }

    @Test
    @Transactional
    void getAllSchoolClassesByClassSubjectIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolClassRepository.saveAndFlush(schoolClass);
        ClassSubject classSubject = ClassSubjectResourceIT.createEntity(em);
        em.persist(classSubject);
        em.flush();
        schoolClass.addClassSubject(classSubject);
        schoolClassRepository.saveAndFlush(schoolClass);
        Long classSubjectId = classSubject.getId();

        // Get all the schoolClassList where classSubject equals to classSubjectId
        defaultSchoolClassShouldBeFound("classSubjectId.equals=" + classSubjectId);

        // Get all the schoolClassList where classSubject equals to (classSubjectId + 1)
        defaultSchoolClassShouldNotBeFound("classSubjectId.equals=" + (classSubjectId + 1));
    }

    @Test
    @Transactional
    void getAllSchoolClassesBySchoolUserIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolClassRepository.saveAndFlush(schoolClass);
        SchoolUser schoolUser = SchoolUserResourceIT.createEntity(em);
        em.persist(schoolUser);
        em.flush();
        schoolClass.addSchoolUser(schoolUser);
        schoolClassRepository.saveAndFlush(schoolClass);
        Long schoolUserId = schoolUser.getId();

        // Get all the schoolClassList where schoolUser equals to schoolUserId
        defaultSchoolClassShouldBeFound("schoolUserId.equals=" + schoolUserId);

        // Get all the schoolClassList where schoolUser equals to (schoolUserId + 1)
        defaultSchoolClassShouldNotBeFound("schoolUserId.equals=" + (schoolUserId + 1));
    }

    @Test
    @Transactional
    void getAllSchoolClassesBySchoolDaysOffIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolClassRepository.saveAndFlush(schoolClass);
        SchoolDaysOff schoolDaysOff = SchoolDaysOffResourceIT.createEntity(em);
        em.persist(schoolDaysOff);
        em.flush();
        schoolClass.addSchoolDaysOff(schoolDaysOff);
        schoolClassRepository.saveAndFlush(schoolClass);
        Long schoolDaysOffId = schoolDaysOff.getId();

        // Get all the schoolClassList where schoolDaysOff equals to schoolDaysOffId
        defaultSchoolClassShouldBeFound("schoolDaysOffId.equals=" + schoolDaysOffId);

        // Get all the schoolClassList where schoolDaysOff equals to (schoolDaysOffId + 1)
        defaultSchoolClassShouldNotBeFound("schoolDaysOffId.equals=" + (schoolDaysOffId + 1));
    }

    @Test
    @Transactional
    void getAllSchoolClassesBySchoolEventIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolClassRepository.saveAndFlush(schoolClass);
        SchoolEvent schoolEvent = SchoolEventResourceIT.createEntity(em);
        em.persist(schoolEvent);
        em.flush();
        schoolClass.addSchoolEvent(schoolEvent);
        schoolClassRepository.saveAndFlush(schoolClass);
        Long schoolEventId = schoolEvent.getId();

        // Get all the schoolClassList where schoolEvent equals to schoolEventId
        defaultSchoolClassShouldBeFound("schoolEventId.equals=" + schoolEventId);

        // Get all the schoolClassList where schoolEvent equals to (schoolEventId + 1)
        defaultSchoolClassShouldNotBeFound("schoolEventId.equals=" + (schoolEventId + 1));
    }

    @Test
    @Transactional
    void getAllSchoolClassesBySchoolPictureGalleryIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolClassRepository.saveAndFlush(schoolClass);
        SchoolPictureGallery schoolPictureGallery = SchoolPictureGalleryResourceIT.createEntity(em);
        em.persist(schoolPictureGallery);
        em.flush();
        schoolClass.addSchoolPictureGallery(schoolPictureGallery);
        schoolClassRepository.saveAndFlush(schoolClass);
        Long schoolPictureGalleryId = schoolPictureGallery.getId();

        // Get all the schoolClassList where schoolPictureGallery equals to schoolPictureGalleryId
        defaultSchoolClassShouldBeFound("schoolPictureGalleryId.equals=" + schoolPictureGalleryId);

        // Get all the schoolClassList where schoolPictureGallery equals to (schoolPictureGalleryId + 1)
        defaultSchoolClassShouldNotBeFound("schoolPictureGalleryId.equals=" + (schoolPictureGalleryId + 1));
    }

    @Test
    @Transactional
    void getAllSchoolClassesByVchoolVideoGalleryIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolClassRepository.saveAndFlush(schoolClass);
        SchoolVideoGallery vchoolVideoGallery = SchoolVideoGalleryResourceIT.createEntity(em);
        em.persist(vchoolVideoGallery);
        em.flush();
        schoolClass.addVchoolVideoGallery(vchoolVideoGallery);
        schoolClassRepository.saveAndFlush(schoolClass);
        Long vchoolVideoGalleryId = vchoolVideoGallery.getId();

        // Get all the schoolClassList where vchoolVideoGallery equals to vchoolVideoGalleryId
        defaultSchoolClassShouldBeFound("vchoolVideoGalleryId.equals=" + vchoolVideoGalleryId);

        // Get all the schoolClassList where vchoolVideoGallery equals to (vchoolVideoGalleryId + 1)
        defaultSchoolClassShouldNotBeFound("vchoolVideoGalleryId.equals=" + (vchoolVideoGalleryId + 1));
    }

    @Test
    @Transactional
    void getAllSchoolClassesBySchoolReportIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolClassRepository.saveAndFlush(schoolClass);
        SchoolReport schoolReport = SchoolReportResourceIT.createEntity(em);
        em.persist(schoolReport);
        em.flush();
        schoolClass.addSchoolReport(schoolReport);
        schoolClassRepository.saveAndFlush(schoolClass);
        Long schoolReportId = schoolReport.getId();

        // Get all the schoolClassList where schoolReport equals to schoolReportId
        defaultSchoolClassShouldBeFound("schoolReportId.equals=" + schoolReportId);

        // Get all the schoolClassList where schoolReport equals to (schoolReportId + 1)
        defaultSchoolClassShouldNotBeFound("schoolReportId.equals=" + (schoolReportId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSchoolClassShouldBeFound(String filter) throws Exception {
        restSchoolClassMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(schoolClass.getId().intValue())))
            .andExpect(jsonPath("$.[*].className").value(hasItem(DEFAULT_CLASS_NAME)))
            .andExpect(jsonPath("$.[*].classLongName").value(hasItem(DEFAULT_CLASS_LONG_NAME)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].cancelDate").value(hasItem(DEFAULT_CANCEL_DATE.toString())));

        // Check, that the count call also returns 1
        restSchoolClassMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSchoolClassShouldNotBeFound(String filter) throws Exception {
        restSchoolClassMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSchoolClassMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSchoolClass() throws Exception {
        // Get the schoolClass
        restSchoolClassMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSchoolClass() throws Exception {
        // Initialize the database
        schoolClassRepository.saveAndFlush(schoolClass);

        int databaseSizeBeforeUpdate = schoolClassRepository.findAll().size();

        // Update the schoolClass
        SchoolClass updatedSchoolClass = schoolClassRepository.findById(schoolClass.getId()).get();
        // Disconnect from session so that the updates on updatedSchoolClass are not directly saved in db
        em.detach(updatedSchoolClass);
        updatedSchoolClass
            .className(UPDATED_CLASS_NAME)
            .classLongName(UPDATED_CLASS_LONG_NAME)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);
        SchoolClassDTO schoolClassDTO = schoolClassMapper.toDto(updatedSchoolClass);

        restSchoolClassMockMvc
            .perform(
                put(ENTITY_API_URL_ID, schoolClassDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schoolClassDTO))
            )
            .andExpect(status().isOk());

        // Validate the SchoolClass in the database
        List<SchoolClass> schoolClassList = schoolClassRepository.findAll();
        assertThat(schoolClassList).hasSize(databaseSizeBeforeUpdate);
        SchoolClass testSchoolClass = schoolClassList.get(schoolClassList.size() - 1);
        assertThat(testSchoolClass.getClassName()).isEqualTo(UPDATED_CLASS_NAME);
        assertThat(testSchoolClass.getClassLongName()).isEqualTo(UPDATED_CLASS_LONG_NAME);
        assertThat(testSchoolClass.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testSchoolClass.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testSchoolClass.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void putNonExistingSchoolClass() throws Exception {
        int databaseSizeBeforeUpdate = schoolClassRepository.findAll().size();
        schoolClass.setId(count.incrementAndGet());

        // Create the SchoolClass
        SchoolClassDTO schoolClassDTO = schoolClassMapper.toDto(schoolClass);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSchoolClassMockMvc
            .perform(
                put(ENTITY_API_URL_ID, schoolClassDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schoolClassDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchoolClass in the database
        List<SchoolClass> schoolClassList = schoolClassRepository.findAll();
        assertThat(schoolClassList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSchoolClass() throws Exception {
        int databaseSizeBeforeUpdate = schoolClassRepository.findAll().size();
        schoolClass.setId(count.incrementAndGet());

        // Create the SchoolClass
        SchoolClassDTO schoolClassDTO = schoolClassMapper.toDto(schoolClass);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchoolClassMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schoolClassDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchoolClass in the database
        List<SchoolClass> schoolClassList = schoolClassRepository.findAll();
        assertThat(schoolClassList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSchoolClass() throws Exception {
        int databaseSizeBeforeUpdate = schoolClassRepository.findAll().size();
        schoolClass.setId(count.incrementAndGet());

        // Create the SchoolClass
        SchoolClassDTO schoolClassDTO = schoolClassMapper.toDto(schoolClass);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchoolClassMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(schoolClassDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SchoolClass in the database
        List<SchoolClass> schoolClassList = schoolClassRepository.findAll();
        assertThat(schoolClassList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSchoolClassWithPatch() throws Exception {
        // Initialize the database
        schoolClassRepository.saveAndFlush(schoolClass);

        int databaseSizeBeforeUpdate = schoolClassRepository.findAll().size();

        // Update the schoolClass using partial update
        SchoolClass partialUpdatedSchoolClass = new SchoolClass();
        partialUpdatedSchoolClass.setId(schoolClass.getId());

        partialUpdatedSchoolClass
            .className(UPDATED_CLASS_NAME)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);

        restSchoolClassMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSchoolClass.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSchoolClass))
            )
            .andExpect(status().isOk());

        // Validate the SchoolClass in the database
        List<SchoolClass> schoolClassList = schoolClassRepository.findAll();
        assertThat(schoolClassList).hasSize(databaseSizeBeforeUpdate);
        SchoolClass testSchoolClass = schoolClassList.get(schoolClassList.size() - 1);
        assertThat(testSchoolClass.getClassName()).isEqualTo(UPDATED_CLASS_NAME);
        assertThat(testSchoolClass.getClassLongName()).isEqualTo(DEFAULT_CLASS_LONG_NAME);
        assertThat(testSchoolClass.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testSchoolClass.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testSchoolClass.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void fullUpdateSchoolClassWithPatch() throws Exception {
        // Initialize the database
        schoolClassRepository.saveAndFlush(schoolClass);

        int databaseSizeBeforeUpdate = schoolClassRepository.findAll().size();

        // Update the schoolClass using partial update
        SchoolClass partialUpdatedSchoolClass = new SchoolClass();
        partialUpdatedSchoolClass.setId(schoolClass.getId());

        partialUpdatedSchoolClass
            .className(UPDATED_CLASS_NAME)
            .classLongName(UPDATED_CLASS_LONG_NAME)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);

        restSchoolClassMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSchoolClass.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSchoolClass))
            )
            .andExpect(status().isOk());

        // Validate the SchoolClass in the database
        List<SchoolClass> schoolClassList = schoolClassRepository.findAll();
        assertThat(schoolClassList).hasSize(databaseSizeBeforeUpdate);
        SchoolClass testSchoolClass = schoolClassList.get(schoolClassList.size() - 1);
        assertThat(testSchoolClass.getClassName()).isEqualTo(UPDATED_CLASS_NAME);
        assertThat(testSchoolClass.getClassLongName()).isEqualTo(UPDATED_CLASS_LONG_NAME);
        assertThat(testSchoolClass.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testSchoolClass.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testSchoolClass.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingSchoolClass() throws Exception {
        int databaseSizeBeforeUpdate = schoolClassRepository.findAll().size();
        schoolClass.setId(count.incrementAndGet());

        // Create the SchoolClass
        SchoolClassDTO schoolClassDTO = schoolClassMapper.toDto(schoolClass);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSchoolClassMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, schoolClassDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(schoolClassDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchoolClass in the database
        List<SchoolClass> schoolClassList = schoolClassRepository.findAll();
        assertThat(schoolClassList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSchoolClass() throws Exception {
        int databaseSizeBeforeUpdate = schoolClassRepository.findAll().size();
        schoolClass.setId(count.incrementAndGet());

        // Create the SchoolClass
        SchoolClassDTO schoolClassDTO = schoolClassMapper.toDto(schoolClass);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchoolClassMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(schoolClassDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchoolClass in the database
        List<SchoolClass> schoolClassList = schoolClassRepository.findAll();
        assertThat(schoolClassList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSchoolClass() throws Exception {
        int databaseSizeBeforeUpdate = schoolClassRepository.findAll().size();
        schoolClass.setId(count.incrementAndGet());

        // Create the SchoolClass
        SchoolClassDTO schoolClassDTO = schoolClassMapper.toDto(schoolClass);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchoolClassMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(schoolClassDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SchoolClass in the database
        List<SchoolClass> schoolClassList = schoolClassRepository.findAll();
        assertThat(schoolClassList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSchoolClass() throws Exception {
        // Initialize the database
        schoolClassRepository.saveAndFlush(schoolClass);

        int databaseSizeBeforeDelete = schoolClassRepository.findAll().size();

        // Delete the schoolClass
        restSchoolClassMockMvc
            .perform(delete(ENTITY_API_URL_ID, schoolClass.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SchoolClass> schoolClassList = schoolClassRepository.findAll();
        assertThat(schoolClassList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
