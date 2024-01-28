package com.ssik.manageit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ssik.manageit.IntegrationTest;
import com.ssik.manageit.domain.ChapterSection;
import com.ssik.manageit.domain.ClassLessionPlan;
import com.ssik.manageit.domain.ClassLessionPlanTrack;
import com.ssik.manageit.domain.ClassSubject;
import com.ssik.manageit.domain.SchoolClass;
import com.ssik.manageit.domain.SubjectChapter;
import com.ssik.manageit.domain.enumeration.TaskStatus;
import com.ssik.manageit.repository.ClassLessionPlanRepository;
import com.ssik.manageit.service.criteria.ClassLessionPlanCriteria;
import com.ssik.manageit.service.dto.ClassLessionPlanDTO;
import com.ssik.manageit.service.mapper.ClassLessionPlanMapper;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link ClassLessionPlanResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ClassLessionPlanResourceIT {

    private static final LocalDate DEFAULT_SCHOOL_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_SCHOOL_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_SCHOOL_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_CLASS_WORK_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_CLASS_WORK_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_HOME_WORK_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_HOME_WORK_TEXT = "BBBBBBBBBB";

    private static final TaskStatus DEFAULT_WORK_STATUS = TaskStatus.NotStarted;
    private static final TaskStatus UPDATED_WORK_STATUS = TaskStatus.InProgress;

    private static final byte[] DEFAULT_LESION_PLAN_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_LESION_PLAN_FILE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_LESION_PLAN_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_LESION_PLAN_FILE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_LESSION_PLAN_FILE_LINK = "AAAAAAAAAA";
    private static final String UPDATED_LESSION_PLAN_FILE_LINK = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATE_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CREATE_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_LAST_MODIFIED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_MODIFIED = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_LAST_MODIFIED = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_CANCEL_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CANCEL_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CANCEL_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/class-lession-plans";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ClassLessionPlanRepository classLessionPlanRepository;

    @Autowired
    private ClassLessionPlanMapper classLessionPlanMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restClassLessionPlanMockMvc;

    private ClassLessionPlan classLessionPlan;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClassLessionPlan createEntity(EntityManager em) {
        ClassLessionPlan classLessionPlan = new ClassLessionPlan()
            .schoolDate(DEFAULT_SCHOOL_DATE)
            .classWorkText(DEFAULT_CLASS_WORK_TEXT)
            .homeWorkText(DEFAULT_HOME_WORK_TEXT)
            .workStatus(DEFAULT_WORK_STATUS)
            .lesionPlanFile(DEFAULT_LESION_PLAN_FILE)
            .lesionPlanFileContentType(DEFAULT_LESION_PLAN_FILE_CONTENT_TYPE)
            .lessionPlanFileLink(DEFAULT_LESSION_PLAN_FILE_LINK)
            .createDate(DEFAULT_CREATE_DATE)
            .lastModified(DEFAULT_LAST_MODIFIED)
            .cancelDate(DEFAULT_CANCEL_DATE);
        return classLessionPlan;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClassLessionPlan createUpdatedEntity(EntityManager em) {
        ClassLessionPlan classLessionPlan = new ClassLessionPlan()
            .schoolDate(UPDATED_SCHOOL_DATE)
            .classWorkText(UPDATED_CLASS_WORK_TEXT)
            .homeWorkText(UPDATED_HOME_WORK_TEXT)
            .workStatus(UPDATED_WORK_STATUS)
            .lesionPlanFile(UPDATED_LESION_PLAN_FILE)
            .lesionPlanFileContentType(UPDATED_LESION_PLAN_FILE_CONTENT_TYPE)
            .lessionPlanFileLink(UPDATED_LESSION_PLAN_FILE_LINK)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);
        return classLessionPlan;
    }

    @BeforeEach
    public void initTest() {
        classLessionPlan = createEntity(em);
    }

    @Test
    @Transactional
    void createClassLessionPlan() throws Exception {
        int databaseSizeBeforeCreate = classLessionPlanRepository.findAll().size();
        // Create the ClassLessionPlan
        ClassLessionPlanDTO classLessionPlanDTO = classLessionPlanMapper.toDto(classLessionPlan);
        restClassLessionPlanMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(classLessionPlanDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ClassLessionPlan in the database
        List<ClassLessionPlan> classLessionPlanList = classLessionPlanRepository.findAll();
        assertThat(classLessionPlanList).hasSize(databaseSizeBeforeCreate + 1);
        ClassLessionPlan testClassLessionPlan = classLessionPlanList.get(classLessionPlanList.size() - 1);
        assertThat(testClassLessionPlan.getSchoolDate()).isEqualTo(DEFAULT_SCHOOL_DATE);
        assertThat(testClassLessionPlan.getClassWorkText()).isEqualTo(DEFAULT_CLASS_WORK_TEXT);
        assertThat(testClassLessionPlan.getHomeWorkText()).isEqualTo(DEFAULT_HOME_WORK_TEXT);
        assertThat(testClassLessionPlan.getWorkStatus()).isEqualTo(DEFAULT_WORK_STATUS);
        assertThat(testClassLessionPlan.getLesionPlanFile()).isEqualTo(DEFAULT_LESION_PLAN_FILE);
        assertThat(testClassLessionPlan.getLesionPlanFileContentType()).isEqualTo(DEFAULT_LESION_PLAN_FILE_CONTENT_TYPE);
        assertThat(testClassLessionPlan.getLessionPlanFileLink()).isEqualTo(DEFAULT_LESSION_PLAN_FILE_LINK);
        assertThat(testClassLessionPlan.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testClassLessionPlan.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
        assertThat(testClassLessionPlan.getCancelDate()).isEqualTo(DEFAULT_CANCEL_DATE);
    }

    @Test
    @Transactional
    void createClassLessionPlanWithExistingId() throws Exception {
        // Create the ClassLessionPlan with an existing ID
        classLessionPlan.setId(1L);
        ClassLessionPlanDTO classLessionPlanDTO = classLessionPlanMapper.toDto(classLessionPlan);

        int databaseSizeBeforeCreate = classLessionPlanRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restClassLessionPlanMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(classLessionPlanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClassLessionPlan in the database
        List<ClassLessionPlan> classLessionPlanList = classLessionPlanRepository.findAll();
        assertThat(classLessionPlanList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSchoolDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = classLessionPlanRepository.findAll().size();
        // set the field null
        classLessionPlan.setSchoolDate(null);

        // Create the ClassLessionPlan, which fails.
        ClassLessionPlanDTO classLessionPlanDTO = classLessionPlanMapper.toDto(classLessionPlan);

        restClassLessionPlanMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(classLessionPlanDTO))
            )
            .andExpect(status().isBadRequest());

        List<ClassLessionPlan> classLessionPlanList = classLessionPlanRepository.findAll();
        assertThat(classLessionPlanList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkClassWorkTextIsRequired() throws Exception {
        int databaseSizeBeforeTest = classLessionPlanRepository.findAll().size();
        // set the field null
        classLessionPlan.setClassWorkText(null);

        // Create the ClassLessionPlan, which fails.
        ClassLessionPlanDTO classLessionPlanDTO = classLessionPlanMapper.toDto(classLessionPlan);

        restClassLessionPlanMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(classLessionPlanDTO))
            )
            .andExpect(status().isBadRequest());

        List<ClassLessionPlan> classLessionPlanList = classLessionPlanRepository.findAll();
        assertThat(classLessionPlanList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkHomeWorkTextIsRequired() throws Exception {
        int databaseSizeBeforeTest = classLessionPlanRepository.findAll().size();
        // set the field null
        classLessionPlan.setHomeWorkText(null);

        // Create the ClassLessionPlan, which fails.
        ClassLessionPlanDTO classLessionPlanDTO = classLessionPlanMapper.toDto(classLessionPlan);

        restClassLessionPlanMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(classLessionPlanDTO))
            )
            .andExpect(status().isBadRequest());

        List<ClassLessionPlan> classLessionPlanList = classLessionPlanRepository.findAll();
        assertThat(classLessionPlanList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkWorkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = classLessionPlanRepository.findAll().size();
        // set the field null
        classLessionPlan.setWorkStatus(null);

        // Create the ClassLessionPlan, which fails.
        ClassLessionPlanDTO classLessionPlanDTO = classLessionPlanMapper.toDto(classLessionPlan);

        restClassLessionPlanMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(classLessionPlanDTO))
            )
            .andExpect(status().isBadRequest());

        List<ClassLessionPlan> classLessionPlanList = classLessionPlanRepository.findAll();
        assertThat(classLessionPlanList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllClassLessionPlans() throws Exception {
        // Initialize the database
        classLessionPlanRepository.saveAndFlush(classLessionPlan);

        // Get all the classLessionPlanList
        restClassLessionPlanMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(classLessionPlan.getId().intValue())))
            .andExpect(jsonPath("$.[*].schoolDate").value(hasItem(DEFAULT_SCHOOL_DATE.toString())))
            .andExpect(jsonPath("$.[*].classWorkText").value(hasItem(DEFAULT_CLASS_WORK_TEXT)))
            .andExpect(jsonPath("$.[*].homeWorkText").value(hasItem(DEFAULT_HOME_WORK_TEXT)))
            .andExpect(jsonPath("$.[*].workStatus").value(hasItem(DEFAULT_WORK_STATUS.toString())))
            .andExpect(jsonPath("$.[*].lesionPlanFileContentType").value(hasItem(DEFAULT_LESION_PLAN_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].lesionPlanFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_LESION_PLAN_FILE))))
            .andExpect(jsonPath("$.[*].lessionPlanFileLink").value(hasItem(DEFAULT_LESSION_PLAN_FILE_LINK)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].cancelDate").value(hasItem(DEFAULT_CANCEL_DATE.toString())));
    }

    @Test
    @Transactional
    void getClassLessionPlan() throws Exception {
        // Initialize the database
        classLessionPlanRepository.saveAndFlush(classLessionPlan);

        // Get the classLessionPlan
        restClassLessionPlanMockMvc
            .perform(get(ENTITY_API_URL_ID, classLessionPlan.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(classLessionPlan.getId().intValue()))
            .andExpect(jsonPath("$.schoolDate").value(DEFAULT_SCHOOL_DATE.toString()))
            .andExpect(jsonPath("$.classWorkText").value(DEFAULT_CLASS_WORK_TEXT))
            .andExpect(jsonPath("$.homeWorkText").value(DEFAULT_HOME_WORK_TEXT))
            .andExpect(jsonPath("$.workStatus").value(DEFAULT_WORK_STATUS.toString()))
            .andExpect(jsonPath("$.lesionPlanFileContentType").value(DEFAULT_LESION_PLAN_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.lesionPlanFile").value(Base64Utils.encodeToString(DEFAULT_LESION_PLAN_FILE)))
            .andExpect(jsonPath("$.lessionPlanFileLink").value(DEFAULT_LESSION_PLAN_FILE_LINK))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.lastModified").value(DEFAULT_LAST_MODIFIED.toString()))
            .andExpect(jsonPath("$.cancelDate").value(DEFAULT_CANCEL_DATE.toString()));
    }

    @Test
    @Transactional
    void getClassLessionPlansByIdFiltering() throws Exception {
        // Initialize the database
        classLessionPlanRepository.saveAndFlush(classLessionPlan);

        Long id = classLessionPlan.getId();

        defaultClassLessionPlanShouldBeFound("id.equals=" + id);
        defaultClassLessionPlanShouldNotBeFound("id.notEquals=" + id);

        defaultClassLessionPlanShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultClassLessionPlanShouldNotBeFound("id.greaterThan=" + id);

        defaultClassLessionPlanShouldBeFound("id.lessThanOrEqual=" + id);
        defaultClassLessionPlanShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllClassLessionPlansBySchoolDateIsEqualToSomething() throws Exception {
        // Initialize the database
        classLessionPlanRepository.saveAndFlush(classLessionPlan);

        // Get all the classLessionPlanList where schoolDate equals to DEFAULT_SCHOOL_DATE
        defaultClassLessionPlanShouldBeFound("schoolDate.equals=" + DEFAULT_SCHOOL_DATE);

        // Get all the classLessionPlanList where schoolDate equals to UPDATED_SCHOOL_DATE
        defaultClassLessionPlanShouldNotBeFound("schoolDate.equals=" + UPDATED_SCHOOL_DATE);
    }

    @Test
    @Transactional
    void getAllClassLessionPlansBySchoolDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classLessionPlanRepository.saveAndFlush(classLessionPlan);

        // Get all the classLessionPlanList where schoolDate not equals to DEFAULT_SCHOOL_DATE
        defaultClassLessionPlanShouldNotBeFound("schoolDate.notEquals=" + DEFAULT_SCHOOL_DATE);

        // Get all the classLessionPlanList where schoolDate not equals to UPDATED_SCHOOL_DATE
        defaultClassLessionPlanShouldBeFound("schoolDate.notEquals=" + UPDATED_SCHOOL_DATE);
    }

    @Test
    @Transactional
    void getAllClassLessionPlansBySchoolDateIsInShouldWork() throws Exception {
        // Initialize the database
        classLessionPlanRepository.saveAndFlush(classLessionPlan);

        // Get all the classLessionPlanList where schoolDate in DEFAULT_SCHOOL_DATE or UPDATED_SCHOOL_DATE
        defaultClassLessionPlanShouldBeFound("schoolDate.in=" + DEFAULT_SCHOOL_DATE + "," + UPDATED_SCHOOL_DATE);

        // Get all the classLessionPlanList where schoolDate equals to UPDATED_SCHOOL_DATE
        defaultClassLessionPlanShouldNotBeFound("schoolDate.in=" + UPDATED_SCHOOL_DATE);
    }

    @Test
    @Transactional
    void getAllClassLessionPlansBySchoolDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        classLessionPlanRepository.saveAndFlush(classLessionPlan);

        // Get all the classLessionPlanList where schoolDate is not null
        defaultClassLessionPlanShouldBeFound("schoolDate.specified=true");

        // Get all the classLessionPlanList where schoolDate is null
        defaultClassLessionPlanShouldNotBeFound("schoolDate.specified=false");
    }

    @Test
    @Transactional
    void getAllClassLessionPlansBySchoolDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        classLessionPlanRepository.saveAndFlush(classLessionPlan);

        // Get all the classLessionPlanList where schoolDate is greater than or equal to DEFAULT_SCHOOL_DATE
        defaultClassLessionPlanShouldBeFound("schoolDate.greaterThanOrEqual=" + DEFAULT_SCHOOL_DATE);

        // Get all the classLessionPlanList where schoolDate is greater than or equal to UPDATED_SCHOOL_DATE
        defaultClassLessionPlanShouldNotBeFound("schoolDate.greaterThanOrEqual=" + UPDATED_SCHOOL_DATE);
    }

    @Test
    @Transactional
    void getAllClassLessionPlansBySchoolDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        classLessionPlanRepository.saveAndFlush(classLessionPlan);

        // Get all the classLessionPlanList where schoolDate is less than or equal to DEFAULT_SCHOOL_DATE
        defaultClassLessionPlanShouldBeFound("schoolDate.lessThanOrEqual=" + DEFAULT_SCHOOL_DATE);

        // Get all the classLessionPlanList where schoolDate is less than or equal to SMALLER_SCHOOL_DATE
        defaultClassLessionPlanShouldNotBeFound("schoolDate.lessThanOrEqual=" + SMALLER_SCHOOL_DATE);
    }

    @Test
    @Transactional
    void getAllClassLessionPlansBySchoolDateIsLessThanSomething() throws Exception {
        // Initialize the database
        classLessionPlanRepository.saveAndFlush(classLessionPlan);

        // Get all the classLessionPlanList where schoolDate is less than DEFAULT_SCHOOL_DATE
        defaultClassLessionPlanShouldNotBeFound("schoolDate.lessThan=" + DEFAULT_SCHOOL_DATE);

        // Get all the classLessionPlanList where schoolDate is less than UPDATED_SCHOOL_DATE
        defaultClassLessionPlanShouldBeFound("schoolDate.lessThan=" + UPDATED_SCHOOL_DATE);
    }

    @Test
    @Transactional
    void getAllClassLessionPlansBySchoolDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        classLessionPlanRepository.saveAndFlush(classLessionPlan);

        // Get all the classLessionPlanList where schoolDate is greater than DEFAULT_SCHOOL_DATE
        defaultClassLessionPlanShouldNotBeFound("schoolDate.greaterThan=" + DEFAULT_SCHOOL_DATE);

        // Get all the classLessionPlanList where schoolDate is greater than SMALLER_SCHOOL_DATE
        defaultClassLessionPlanShouldBeFound("schoolDate.greaterThan=" + SMALLER_SCHOOL_DATE);
    }

    @Test
    @Transactional
    void getAllClassLessionPlansByClassWorkTextIsEqualToSomething() throws Exception {
        // Initialize the database
        classLessionPlanRepository.saveAndFlush(classLessionPlan);

        // Get all the classLessionPlanList where classWorkText equals to DEFAULT_CLASS_WORK_TEXT
        defaultClassLessionPlanShouldBeFound("classWorkText.equals=" + DEFAULT_CLASS_WORK_TEXT);

        // Get all the classLessionPlanList where classWorkText equals to UPDATED_CLASS_WORK_TEXT
        defaultClassLessionPlanShouldNotBeFound("classWorkText.equals=" + UPDATED_CLASS_WORK_TEXT);
    }

    @Test
    @Transactional
    void getAllClassLessionPlansByClassWorkTextIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classLessionPlanRepository.saveAndFlush(classLessionPlan);

        // Get all the classLessionPlanList where classWorkText not equals to DEFAULT_CLASS_WORK_TEXT
        defaultClassLessionPlanShouldNotBeFound("classWorkText.notEquals=" + DEFAULT_CLASS_WORK_TEXT);

        // Get all the classLessionPlanList where classWorkText not equals to UPDATED_CLASS_WORK_TEXT
        defaultClassLessionPlanShouldBeFound("classWorkText.notEquals=" + UPDATED_CLASS_WORK_TEXT);
    }

    @Test
    @Transactional
    void getAllClassLessionPlansByClassWorkTextIsInShouldWork() throws Exception {
        // Initialize the database
        classLessionPlanRepository.saveAndFlush(classLessionPlan);

        // Get all the classLessionPlanList where classWorkText in DEFAULT_CLASS_WORK_TEXT or UPDATED_CLASS_WORK_TEXT
        defaultClassLessionPlanShouldBeFound("classWorkText.in=" + DEFAULT_CLASS_WORK_TEXT + "," + UPDATED_CLASS_WORK_TEXT);

        // Get all the classLessionPlanList where classWorkText equals to UPDATED_CLASS_WORK_TEXT
        defaultClassLessionPlanShouldNotBeFound("classWorkText.in=" + UPDATED_CLASS_WORK_TEXT);
    }

    @Test
    @Transactional
    void getAllClassLessionPlansByClassWorkTextIsNullOrNotNull() throws Exception {
        // Initialize the database
        classLessionPlanRepository.saveAndFlush(classLessionPlan);

        // Get all the classLessionPlanList where classWorkText is not null
        defaultClassLessionPlanShouldBeFound("classWorkText.specified=true");

        // Get all the classLessionPlanList where classWorkText is null
        defaultClassLessionPlanShouldNotBeFound("classWorkText.specified=false");
    }

    @Test
    @Transactional
    void getAllClassLessionPlansByClassWorkTextContainsSomething() throws Exception {
        // Initialize the database
        classLessionPlanRepository.saveAndFlush(classLessionPlan);

        // Get all the classLessionPlanList where classWorkText contains DEFAULT_CLASS_WORK_TEXT
        defaultClassLessionPlanShouldBeFound("classWorkText.contains=" + DEFAULT_CLASS_WORK_TEXT);

        // Get all the classLessionPlanList where classWorkText contains UPDATED_CLASS_WORK_TEXT
        defaultClassLessionPlanShouldNotBeFound("classWorkText.contains=" + UPDATED_CLASS_WORK_TEXT);
    }

    @Test
    @Transactional
    void getAllClassLessionPlansByClassWorkTextNotContainsSomething() throws Exception {
        // Initialize the database
        classLessionPlanRepository.saveAndFlush(classLessionPlan);

        // Get all the classLessionPlanList where classWorkText does not contain DEFAULT_CLASS_WORK_TEXT
        defaultClassLessionPlanShouldNotBeFound("classWorkText.doesNotContain=" + DEFAULT_CLASS_WORK_TEXT);

        // Get all the classLessionPlanList where classWorkText does not contain UPDATED_CLASS_WORK_TEXT
        defaultClassLessionPlanShouldBeFound("classWorkText.doesNotContain=" + UPDATED_CLASS_WORK_TEXT);
    }

    @Test
    @Transactional
    void getAllClassLessionPlansByHomeWorkTextIsEqualToSomething() throws Exception {
        // Initialize the database
        classLessionPlanRepository.saveAndFlush(classLessionPlan);

        // Get all the classLessionPlanList where homeWorkText equals to DEFAULT_HOME_WORK_TEXT
        defaultClassLessionPlanShouldBeFound("homeWorkText.equals=" + DEFAULT_HOME_WORK_TEXT);

        // Get all the classLessionPlanList where homeWorkText equals to UPDATED_HOME_WORK_TEXT
        defaultClassLessionPlanShouldNotBeFound("homeWorkText.equals=" + UPDATED_HOME_WORK_TEXT);
    }

    @Test
    @Transactional
    void getAllClassLessionPlansByHomeWorkTextIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classLessionPlanRepository.saveAndFlush(classLessionPlan);

        // Get all the classLessionPlanList where homeWorkText not equals to DEFAULT_HOME_WORK_TEXT
        defaultClassLessionPlanShouldNotBeFound("homeWorkText.notEquals=" + DEFAULT_HOME_WORK_TEXT);

        // Get all the classLessionPlanList where homeWorkText not equals to UPDATED_HOME_WORK_TEXT
        defaultClassLessionPlanShouldBeFound("homeWorkText.notEquals=" + UPDATED_HOME_WORK_TEXT);
    }

    @Test
    @Transactional
    void getAllClassLessionPlansByHomeWorkTextIsInShouldWork() throws Exception {
        // Initialize the database
        classLessionPlanRepository.saveAndFlush(classLessionPlan);

        // Get all the classLessionPlanList where homeWorkText in DEFAULT_HOME_WORK_TEXT or UPDATED_HOME_WORK_TEXT
        defaultClassLessionPlanShouldBeFound("homeWorkText.in=" + DEFAULT_HOME_WORK_TEXT + "," + UPDATED_HOME_WORK_TEXT);

        // Get all the classLessionPlanList where homeWorkText equals to UPDATED_HOME_WORK_TEXT
        defaultClassLessionPlanShouldNotBeFound("homeWorkText.in=" + UPDATED_HOME_WORK_TEXT);
    }

    @Test
    @Transactional
    void getAllClassLessionPlansByHomeWorkTextIsNullOrNotNull() throws Exception {
        // Initialize the database
        classLessionPlanRepository.saveAndFlush(classLessionPlan);

        // Get all the classLessionPlanList where homeWorkText is not null
        defaultClassLessionPlanShouldBeFound("homeWorkText.specified=true");

        // Get all the classLessionPlanList where homeWorkText is null
        defaultClassLessionPlanShouldNotBeFound("homeWorkText.specified=false");
    }

    @Test
    @Transactional
    void getAllClassLessionPlansByHomeWorkTextContainsSomething() throws Exception {
        // Initialize the database
        classLessionPlanRepository.saveAndFlush(classLessionPlan);

        // Get all the classLessionPlanList where homeWorkText contains DEFAULT_HOME_WORK_TEXT
        defaultClassLessionPlanShouldBeFound("homeWorkText.contains=" + DEFAULT_HOME_WORK_TEXT);

        // Get all the classLessionPlanList where homeWorkText contains UPDATED_HOME_WORK_TEXT
        defaultClassLessionPlanShouldNotBeFound("homeWorkText.contains=" + UPDATED_HOME_WORK_TEXT);
    }

    @Test
    @Transactional
    void getAllClassLessionPlansByHomeWorkTextNotContainsSomething() throws Exception {
        // Initialize the database
        classLessionPlanRepository.saveAndFlush(classLessionPlan);

        // Get all the classLessionPlanList where homeWorkText does not contain DEFAULT_HOME_WORK_TEXT
        defaultClassLessionPlanShouldNotBeFound("homeWorkText.doesNotContain=" + DEFAULT_HOME_WORK_TEXT);

        // Get all the classLessionPlanList where homeWorkText does not contain UPDATED_HOME_WORK_TEXT
        defaultClassLessionPlanShouldBeFound("homeWorkText.doesNotContain=" + UPDATED_HOME_WORK_TEXT);
    }

    @Test
    @Transactional
    void getAllClassLessionPlansByWorkStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        classLessionPlanRepository.saveAndFlush(classLessionPlan);

        // Get all the classLessionPlanList where workStatus equals to DEFAULT_WORK_STATUS
        defaultClassLessionPlanShouldBeFound("workStatus.equals=" + DEFAULT_WORK_STATUS);

        // Get all the classLessionPlanList where workStatus equals to UPDATED_WORK_STATUS
        defaultClassLessionPlanShouldNotBeFound("workStatus.equals=" + UPDATED_WORK_STATUS);
    }

    @Test
    @Transactional
    void getAllClassLessionPlansByWorkStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classLessionPlanRepository.saveAndFlush(classLessionPlan);

        // Get all the classLessionPlanList where workStatus not equals to DEFAULT_WORK_STATUS
        defaultClassLessionPlanShouldNotBeFound("workStatus.notEquals=" + DEFAULT_WORK_STATUS);

        // Get all the classLessionPlanList where workStatus not equals to UPDATED_WORK_STATUS
        defaultClassLessionPlanShouldBeFound("workStatus.notEquals=" + UPDATED_WORK_STATUS);
    }

    @Test
    @Transactional
    void getAllClassLessionPlansByWorkStatusIsInShouldWork() throws Exception {
        // Initialize the database
        classLessionPlanRepository.saveAndFlush(classLessionPlan);

        // Get all the classLessionPlanList where workStatus in DEFAULT_WORK_STATUS or UPDATED_WORK_STATUS
        defaultClassLessionPlanShouldBeFound("workStatus.in=" + DEFAULT_WORK_STATUS + "," + UPDATED_WORK_STATUS);

        // Get all the classLessionPlanList where workStatus equals to UPDATED_WORK_STATUS
        defaultClassLessionPlanShouldNotBeFound("workStatus.in=" + UPDATED_WORK_STATUS);
    }

    @Test
    @Transactional
    void getAllClassLessionPlansByWorkStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        classLessionPlanRepository.saveAndFlush(classLessionPlan);

        // Get all the classLessionPlanList where workStatus is not null
        defaultClassLessionPlanShouldBeFound("workStatus.specified=true");

        // Get all the classLessionPlanList where workStatus is null
        defaultClassLessionPlanShouldNotBeFound("workStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllClassLessionPlansByLessionPlanFileLinkIsEqualToSomething() throws Exception {
        // Initialize the database
        classLessionPlanRepository.saveAndFlush(classLessionPlan);

        // Get all the classLessionPlanList where lessionPlanFileLink equals to DEFAULT_LESSION_PLAN_FILE_LINK
        defaultClassLessionPlanShouldBeFound("lessionPlanFileLink.equals=" + DEFAULT_LESSION_PLAN_FILE_LINK);

        // Get all the classLessionPlanList where lessionPlanFileLink equals to UPDATED_LESSION_PLAN_FILE_LINK
        defaultClassLessionPlanShouldNotBeFound("lessionPlanFileLink.equals=" + UPDATED_LESSION_PLAN_FILE_LINK);
    }

    @Test
    @Transactional
    void getAllClassLessionPlansByLessionPlanFileLinkIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classLessionPlanRepository.saveAndFlush(classLessionPlan);

        // Get all the classLessionPlanList where lessionPlanFileLink not equals to DEFAULT_LESSION_PLAN_FILE_LINK
        defaultClassLessionPlanShouldNotBeFound("lessionPlanFileLink.notEquals=" + DEFAULT_LESSION_PLAN_FILE_LINK);

        // Get all the classLessionPlanList where lessionPlanFileLink not equals to UPDATED_LESSION_PLAN_FILE_LINK
        defaultClassLessionPlanShouldBeFound("lessionPlanFileLink.notEquals=" + UPDATED_LESSION_PLAN_FILE_LINK);
    }

    @Test
    @Transactional
    void getAllClassLessionPlansByLessionPlanFileLinkIsInShouldWork() throws Exception {
        // Initialize the database
        classLessionPlanRepository.saveAndFlush(classLessionPlan);

        // Get all the classLessionPlanList where lessionPlanFileLink in DEFAULT_LESSION_PLAN_FILE_LINK or UPDATED_LESSION_PLAN_FILE_LINK
        defaultClassLessionPlanShouldBeFound(
            "lessionPlanFileLink.in=" + DEFAULT_LESSION_PLAN_FILE_LINK + "," + UPDATED_LESSION_PLAN_FILE_LINK
        );

        // Get all the classLessionPlanList where lessionPlanFileLink equals to UPDATED_LESSION_PLAN_FILE_LINK
        defaultClassLessionPlanShouldNotBeFound("lessionPlanFileLink.in=" + UPDATED_LESSION_PLAN_FILE_LINK);
    }

    @Test
    @Transactional
    void getAllClassLessionPlansByLessionPlanFileLinkIsNullOrNotNull() throws Exception {
        // Initialize the database
        classLessionPlanRepository.saveAndFlush(classLessionPlan);

        // Get all the classLessionPlanList where lessionPlanFileLink is not null
        defaultClassLessionPlanShouldBeFound("lessionPlanFileLink.specified=true");

        // Get all the classLessionPlanList where lessionPlanFileLink is null
        defaultClassLessionPlanShouldNotBeFound("lessionPlanFileLink.specified=false");
    }

    @Test
    @Transactional
    void getAllClassLessionPlansByLessionPlanFileLinkContainsSomething() throws Exception {
        // Initialize the database
        classLessionPlanRepository.saveAndFlush(classLessionPlan);

        // Get all the classLessionPlanList where lessionPlanFileLink contains DEFAULT_LESSION_PLAN_FILE_LINK
        defaultClassLessionPlanShouldBeFound("lessionPlanFileLink.contains=" + DEFAULT_LESSION_PLAN_FILE_LINK);

        // Get all the classLessionPlanList where lessionPlanFileLink contains UPDATED_LESSION_PLAN_FILE_LINK
        defaultClassLessionPlanShouldNotBeFound("lessionPlanFileLink.contains=" + UPDATED_LESSION_PLAN_FILE_LINK);
    }

    @Test
    @Transactional
    void getAllClassLessionPlansByLessionPlanFileLinkNotContainsSomething() throws Exception {
        // Initialize the database
        classLessionPlanRepository.saveAndFlush(classLessionPlan);

        // Get all the classLessionPlanList where lessionPlanFileLink does not contain DEFAULT_LESSION_PLAN_FILE_LINK
        defaultClassLessionPlanShouldNotBeFound("lessionPlanFileLink.doesNotContain=" + DEFAULT_LESSION_PLAN_FILE_LINK);

        // Get all the classLessionPlanList where lessionPlanFileLink does not contain UPDATED_LESSION_PLAN_FILE_LINK
        defaultClassLessionPlanShouldBeFound("lessionPlanFileLink.doesNotContain=" + UPDATED_LESSION_PLAN_FILE_LINK);
    }

    @Test
    @Transactional
    void getAllClassLessionPlansByCreateDateIsEqualToSomething() throws Exception {
        // Initialize the database
        classLessionPlanRepository.saveAndFlush(classLessionPlan);

        // Get all the classLessionPlanList where createDate equals to DEFAULT_CREATE_DATE
        defaultClassLessionPlanShouldBeFound("createDate.equals=" + DEFAULT_CREATE_DATE);

        // Get all the classLessionPlanList where createDate equals to UPDATED_CREATE_DATE
        defaultClassLessionPlanShouldNotBeFound("createDate.equals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllClassLessionPlansByCreateDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classLessionPlanRepository.saveAndFlush(classLessionPlan);

        // Get all the classLessionPlanList where createDate not equals to DEFAULT_CREATE_DATE
        defaultClassLessionPlanShouldNotBeFound("createDate.notEquals=" + DEFAULT_CREATE_DATE);

        // Get all the classLessionPlanList where createDate not equals to UPDATED_CREATE_DATE
        defaultClassLessionPlanShouldBeFound("createDate.notEquals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllClassLessionPlansByCreateDateIsInShouldWork() throws Exception {
        // Initialize the database
        classLessionPlanRepository.saveAndFlush(classLessionPlan);

        // Get all the classLessionPlanList where createDate in DEFAULT_CREATE_DATE or UPDATED_CREATE_DATE
        defaultClassLessionPlanShouldBeFound("createDate.in=" + DEFAULT_CREATE_DATE + "," + UPDATED_CREATE_DATE);

        // Get all the classLessionPlanList where createDate equals to UPDATED_CREATE_DATE
        defaultClassLessionPlanShouldNotBeFound("createDate.in=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllClassLessionPlansByCreateDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        classLessionPlanRepository.saveAndFlush(classLessionPlan);

        // Get all the classLessionPlanList where createDate is not null
        defaultClassLessionPlanShouldBeFound("createDate.specified=true");

        // Get all the classLessionPlanList where createDate is null
        defaultClassLessionPlanShouldNotBeFound("createDate.specified=false");
    }

    @Test
    @Transactional
    void getAllClassLessionPlansByCreateDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        classLessionPlanRepository.saveAndFlush(classLessionPlan);

        // Get all the classLessionPlanList where createDate is greater than or equal to DEFAULT_CREATE_DATE
        defaultClassLessionPlanShouldBeFound("createDate.greaterThanOrEqual=" + DEFAULT_CREATE_DATE);

        // Get all the classLessionPlanList where createDate is greater than or equal to UPDATED_CREATE_DATE
        defaultClassLessionPlanShouldNotBeFound("createDate.greaterThanOrEqual=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllClassLessionPlansByCreateDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        classLessionPlanRepository.saveAndFlush(classLessionPlan);

        // Get all the classLessionPlanList where createDate is less than or equal to DEFAULT_CREATE_DATE
        defaultClassLessionPlanShouldBeFound("createDate.lessThanOrEqual=" + DEFAULT_CREATE_DATE);

        // Get all the classLessionPlanList where createDate is less than or equal to SMALLER_CREATE_DATE
        defaultClassLessionPlanShouldNotBeFound("createDate.lessThanOrEqual=" + SMALLER_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllClassLessionPlansByCreateDateIsLessThanSomething() throws Exception {
        // Initialize the database
        classLessionPlanRepository.saveAndFlush(classLessionPlan);

        // Get all the classLessionPlanList where createDate is less than DEFAULT_CREATE_DATE
        defaultClassLessionPlanShouldNotBeFound("createDate.lessThan=" + DEFAULT_CREATE_DATE);

        // Get all the classLessionPlanList where createDate is less than UPDATED_CREATE_DATE
        defaultClassLessionPlanShouldBeFound("createDate.lessThan=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllClassLessionPlansByCreateDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        classLessionPlanRepository.saveAndFlush(classLessionPlan);

        // Get all the classLessionPlanList where createDate is greater than DEFAULT_CREATE_DATE
        defaultClassLessionPlanShouldNotBeFound("createDate.greaterThan=" + DEFAULT_CREATE_DATE);

        // Get all the classLessionPlanList where createDate is greater than SMALLER_CREATE_DATE
        defaultClassLessionPlanShouldBeFound("createDate.greaterThan=" + SMALLER_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllClassLessionPlansByLastModifiedIsEqualToSomething() throws Exception {
        // Initialize the database
        classLessionPlanRepository.saveAndFlush(classLessionPlan);

        // Get all the classLessionPlanList where lastModified equals to DEFAULT_LAST_MODIFIED
        defaultClassLessionPlanShouldBeFound("lastModified.equals=" + DEFAULT_LAST_MODIFIED);

        // Get all the classLessionPlanList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultClassLessionPlanShouldNotBeFound("lastModified.equals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllClassLessionPlansByLastModifiedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classLessionPlanRepository.saveAndFlush(classLessionPlan);

        // Get all the classLessionPlanList where lastModified not equals to DEFAULT_LAST_MODIFIED
        defaultClassLessionPlanShouldNotBeFound("lastModified.notEquals=" + DEFAULT_LAST_MODIFIED);

        // Get all the classLessionPlanList where lastModified not equals to UPDATED_LAST_MODIFIED
        defaultClassLessionPlanShouldBeFound("lastModified.notEquals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllClassLessionPlansByLastModifiedIsInShouldWork() throws Exception {
        // Initialize the database
        classLessionPlanRepository.saveAndFlush(classLessionPlan);

        // Get all the classLessionPlanList where lastModified in DEFAULT_LAST_MODIFIED or UPDATED_LAST_MODIFIED
        defaultClassLessionPlanShouldBeFound("lastModified.in=" + DEFAULT_LAST_MODIFIED + "," + UPDATED_LAST_MODIFIED);

        // Get all the classLessionPlanList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultClassLessionPlanShouldNotBeFound("lastModified.in=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllClassLessionPlansByLastModifiedIsNullOrNotNull() throws Exception {
        // Initialize the database
        classLessionPlanRepository.saveAndFlush(classLessionPlan);

        // Get all the classLessionPlanList where lastModified is not null
        defaultClassLessionPlanShouldBeFound("lastModified.specified=true");

        // Get all the classLessionPlanList where lastModified is null
        defaultClassLessionPlanShouldNotBeFound("lastModified.specified=false");
    }

    @Test
    @Transactional
    void getAllClassLessionPlansByLastModifiedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        classLessionPlanRepository.saveAndFlush(classLessionPlan);

        // Get all the classLessionPlanList where lastModified is greater than or equal to DEFAULT_LAST_MODIFIED
        defaultClassLessionPlanShouldBeFound("lastModified.greaterThanOrEqual=" + DEFAULT_LAST_MODIFIED);

        // Get all the classLessionPlanList where lastModified is greater than or equal to UPDATED_LAST_MODIFIED
        defaultClassLessionPlanShouldNotBeFound("lastModified.greaterThanOrEqual=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllClassLessionPlansByLastModifiedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        classLessionPlanRepository.saveAndFlush(classLessionPlan);

        // Get all the classLessionPlanList where lastModified is less than or equal to DEFAULT_LAST_MODIFIED
        defaultClassLessionPlanShouldBeFound("lastModified.lessThanOrEqual=" + DEFAULT_LAST_MODIFIED);

        // Get all the classLessionPlanList where lastModified is less than or equal to SMALLER_LAST_MODIFIED
        defaultClassLessionPlanShouldNotBeFound("lastModified.lessThanOrEqual=" + SMALLER_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllClassLessionPlansByLastModifiedIsLessThanSomething() throws Exception {
        // Initialize the database
        classLessionPlanRepository.saveAndFlush(classLessionPlan);

        // Get all the classLessionPlanList where lastModified is less than DEFAULT_LAST_MODIFIED
        defaultClassLessionPlanShouldNotBeFound("lastModified.lessThan=" + DEFAULT_LAST_MODIFIED);

        // Get all the classLessionPlanList where lastModified is less than UPDATED_LAST_MODIFIED
        defaultClassLessionPlanShouldBeFound("lastModified.lessThan=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllClassLessionPlansByLastModifiedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        classLessionPlanRepository.saveAndFlush(classLessionPlan);

        // Get all the classLessionPlanList where lastModified is greater than DEFAULT_LAST_MODIFIED
        defaultClassLessionPlanShouldNotBeFound("lastModified.greaterThan=" + DEFAULT_LAST_MODIFIED);

        // Get all the classLessionPlanList where lastModified is greater than SMALLER_LAST_MODIFIED
        defaultClassLessionPlanShouldBeFound("lastModified.greaterThan=" + SMALLER_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllClassLessionPlansByCancelDateIsEqualToSomething() throws Exception {
        // Initialize the database
        classLessionPlanRepository.saveAndFlush(classLessionPlan);

        // Get all the classLessionPlanList where cancelDate equals to DEFAULT_CANCEL_DATE
        defaultClassLessionPlanShouldBeFound("cancelDate.equals=" + DEFAULT_CANCEL_DATE);

        // Get all the classLessionPlanList where cancelDate equals to UPDATED_CANCEL_DATE
        defaultClassLessionPlanShouldNotBeFound("cancelDate.equals=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllClassLessionPlansByCancelDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classLessionPlanRepository.saveAndFlush(classLessionPlan);

        // Get all the classLessionPlanList where cancelDate not equals to DEFAULT_CANCEL_DATE
        defaultClassLessionPlanShouldNotBeFound("cancelDate.notEquals=" + DEFAULT_CANCEL_DATE);

        // Get all the classLessionPlanList where cancelDate not equals to UPDATED_CANCEL_DATE
        defaultClassLessionPlanShouldBeFound("cancelDate.notEquals=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllClassLessionPlansByCancelDateIsInShouldWork() throws Exception {
        // Initialize the database
        classLessionPlanRepository.saveAndFlush(classLessionPlan);

        // Get all the classLessionPlanList where cancelDate in DEFAULT_CANCEL_DATE or UPDATED_CANCEL_DATE
        defaultClassLessionPlanShouldBeFound("cancelDate.in=" + DEFAULT_CANCEL_DATE + "," + UPDATED_CANCEL_DATE);

        // Get all the classLessionPlanList where cancelDate equals to UPDATED_CANCEL_DATE
        defaultClassLessionPlanShouldNotBeFound("cancelDate.in=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllClassLessionPlansByCancelDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        classLessionPlanRepository.saveAndFlush(classLessionPlan);

        // Get all the classLessionPlanList where cancelDate is not null
        defaultClassLessionPlanShouldBeFound("cancelDate.specified=true");

        // Get all the classLessionPlanList where cancelDate is null
        defaultClassLessionPlanShouldNotBeFound("cancelDate.specified=false");
    }

    @Test
    @Transactional
    void getAllClassLessionPlansByCancelDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        classLessionPlanRepository.saveAndFlush(classLessionPlan);

        // Get all the classLessionPlanList where cancelDate is greater than or equal to DEFAULT_CANCEL_DATE
        defaultClassLessionPlanShouldBeFound("cancelDate.greaterThanOrEqual=" + DEFAULT_CANCEL_DATE);

        // Get all the classLessionPlanList where cancelDate is greater than or equal to UPDATED_CANCEL_DATE
        defaultClassLessionPlanShouldNotBeFound("cancelDate.greaterThanOrEqual=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllClassLessionPlansByCancelDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        classLessionPlanRepository.saveAndFlush(classLessionPlan);

        // Get all the classLessionPlanList where cancelDate is less than or equal to DEFAULT_CANCEL_DATE
        defaultClassLessionPlanShouldBeFound("cancelDate.lessThanOrEqual=" + DEFAULT_CANCEL_DATE);

        // Get all the classLessionPlanList where cancelDate is less than or equal to SMALLER_CANCEL_DATE
        defaultClassLessionPlanShouldNotBeFound("cancelDate.lessThanOrEqual=" + SMALLER_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllClassLessionPlansByCancelDateIsLessThanSomething() throws Exception {
        // Initialize the database
        classLessionPlanRepository.saveAndFlush(classLessionPlan);

        // Get all the classLessionPlanList where cancelDate is less than DEFAULT_CANCEL_DATE
        defaultClassLessionPlanShouldNotBeFound("cancelDate.lessThan=" + DEFAULT_CANCEL_DATE);

        // Get all the classLessionPlanList where cancelDate is less than UPDATED_CANCEL_DATE
        defaultClassLessionPlanShouldBeFound("cancelDate.lessThan=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllClassLessionPlansByCancelDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        classLessionPlanRepository.saveAndFlush(classLessionPlan);

        // Get all the classLessionPlanList where cancelDate is greater than DEFAULT_CANCEL_DATE
        defaultClassLessionPlanShouldNotBeFound("cancelDate.greaterThan=" + DEFAULT_CANCEL_DATE);

        // Get all the classLessionPlanList where cancelDate is greater than SMALLER_CANCEL_DATE
        defaultClassLessionPlanShouldBeFound("cancelDate.greaterThan=" + SMALLER_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllClassLessionPlansByClassLessionPlanTrackIsEqualToSomething() throws Exception {
        // Initialize the database
        classLessionPlanRepository.saveAndFlush(classLessionPlan);
        ClassLessionPlanTrack classLessionPlanTrack = ClassLessionPlanTrackResourceIT.createEntity(em);
        em.persist(classLessionPlanTrack);
        em.flush();
        classLessionPlan.addClassLessionPlanTrack(classLessionPlanTrack);
        classLessionPlanRepository.saveAndFlush(classLessionPlan);
        Long classLessionPlanTrackId = classLessionPlanTrack.getId();

        // Get all the classLessionPlanList where classLessionPlanTrack equals to classLessionPlanTrackId
        defaultClassLessionPlanShouldBeFound("classLessionPlanTrackId.equals=" + classLessionPlanTrackId);

        // Get all the classLessionPlanList where classLessionPlanTrack equals to (classLessionPlanTrackId + 1)
        defaultClassLessionPlanShouldNotBeFound("classLessionPlanTrackId.equals=" + (classLessionPlanTrackId + 1));
    }

    @Test
    @Transactional
    void getAllClassLessionPlansByChapterSectionIsEqualToSomething() throws Exception {
        // Initialize the database
        classLessionPlanRepository.saveAndFlush(classLessionPlan);
        ChapterSection chapterSection = ChapterSectionResourceIT.createEntity(em);
        em.persist(chapterSection);
        em.flush();
        classLessionPlan.setChapterSection(chapterSection);
        classLessionPlanRepository.saveAndFlush(classLessionPlan);
        Long chapterSectionId = chapterSection.getId();

        // Get all the classLessionPlanList where chapterSection equals to chapterSectionId
        defaultClassLessionPlanShouldBeFound("chapterSectionId.equals=" + chapterSectionId);

        // Get all the classLessionPlanList where chapterSection equals to (chapterSectionId + 1)
        defaultClassLessionPlanShouldNotBeFound("chapterSectionId.equals=" + (chapterSectionId + 1));
    }

    @Test
    @Transactional
    void getAllClassLessionPlansBySchoolClassIsEqualToSomething() throws Exception {
        // Initialize the database
        classLessionPlanRepository.saveAndFlush(classLessionPlan);
        SchoolClass schoolClass = SchoolClassResourceIT.createEntity(em);
        em.persist(schoolClass);
        em.flush();
        classLessionPlan.setSchoolClass(schoolClass);
        classLessionPlanRepository.saveAndFlush(classLessionPlan);
        Long schoolClassId = schoolClass.getId();

        // Get all the classLessionPlanList where schoolClass equals to schoolClassId
        defaultClassLessionPlanShouldBeFound("schoolClassId.equals=" + schoolClassId);

        // Get all the classLessionPlanList where schoolClass equals to (schoolClassId + 1)
        defaultClassLessionPlanShouldNotBeFound("schoolClassId.equals=" + (schoolClassId + 1));
    }

    @Test
    @Transactional
    void getAllClassLessionPlansByClassSubjectIsEqualToSomething() throws Exception {
        // Initialize the database
        classLessionPlanRepository.saveAndFlush(classLessionPlan);
        ClassSubject classSubject = ClassSubjectResourceIT.createEntity(em);
        em.persist(classSubject);
        em.flush();
        classLessionPlan.setClassSubject(classSubject);
        classLessionPlanRepository.saveAndFlush(classLessionPlan);
        Long classSubjectId = classSubject.getId();

        // Get all the classLessionPlanList where classSubject equals to classSubjectId
        defaultClassLessionPlanShouldBeFound("classSubjectId.equals=" + classSubjectId);

        // Get all the classLessionPlanList where classSubject equals to (classSubjectId + 1)
        defaultClassLessionPlanShouldNotBeFound("classSubjectId.equals=" + (classSubjectId + 1));
    }

    @Test
    @Transactional
    void getAllClassLessionPlansBySubjectChapterIsEqualToSomething() throws Exception {
        // Initialize the database
        classLessionPlanRepository.saveAndFlush(classLessionPlan);
        SubjectChapter subjectChapter = SubjectChapterResourceIT.createEntity(em);
        em.persist(subjectChapter);
        em.flush();
        classLessionPlan.setSubjectChapter(subjectChapter);
        classLessionPlanRepository.saveAndFlush(classLessionPlan);
        Long subjectChapterId = subjectChapter.getId();

        // Get all the classLessionPlanList where subjectChapter equals to subjectChapterId
        defaultClassLessionPlanShouldBeFound("subjectChapterId.equals=" + subjectChapterId);

        // Get all the classLessionPlanList where subjectChapter equals to (subjectChapterId + 1)
        defaultClassLessionPlanShouldNotBeFound("subjectChapterId.equals=" + (subjectChapterId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultClassLessionPlanShouldBeFound(String filter) throws Exception {
        restClassLessionPlanMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(classLessionPlan.getId().intValue())))
            .andExpect(jsonPath("$.[*].schoolDate").value(hasItem(DEFAULT_SCHOOL_DATE.toString())))
            .andExpect(jsonPath("$.[*].classWorkText").value(hasItem(DEFAULT_CLASS_WORK_TEXT)))
            .andExpect(jsonPath("$.[*].homeWorkText").value(hasItem(DEFAULT_HOME_WORK_TEXT)))
            .andExpect(jsonPath("$.[*].workStatus").value(hasItem(DEFAULT_WORK_STATUS.toString())))
            .andExpect(jsonPath("$.[*].lesionPlanFileContentType").value(hasItem(DEFAULT_LESION_PLAN_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].lesionPlanFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_LESION_PLAN_FILE))))
            .andExpect(jsonPath("$.[*].lessionPlanFileLink").value(hasItem(DEFAULT_LESSION_PLAN_FILE_LINK)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].cancelDate").value(hasItem(DEFAULT_CANCEL_DATE.toString())));

        // Check, that the count call also returns 1
        restClassLessionPlanMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultClassLessionPlanShouldNotBeFound(String filter) throws Exception {
        restClassLessionPlanMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restClassLessionPlanMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingClassLessionPlan() throws Exception {
        // Get the classLessionPlan
        restClassLessionPlanMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewClassLessionPlan() throws Exception {
        // Initialize the database
        classLessionPlanRepository.saveAndFlush(classLessionPlan);

        int databaseSizeBeforeUpdate = classLessionPlanRepository.findAll().size();

        // Update the classLessionPlan
        ClassLessionPlan updatedClassLessionPlan = classLessionPlanRepository.findById(classLessionPlan.getId()).get();
        // Disconnect from session so that the updates on updatedClassLessionPlan are not directly saved in db
        em.detach(updatedClassLessionPlan);
        updatedClassLessionPlan
            .schoolDate(UPDATED_SCHOOL_DATE)
            .classWorkText(UPDATED_CLASS_WORK_TEXT)
            .homeWorkText(UPDATED_HOME_WORK_TEXT)
            .workStatus(UPDATED_WORK_STATUS)
            .lesionPlanFile(UPDATED_LESION_PLAN_FILE)
            .lesionPlanFileContentType(UPDATED_LESION_PLAN_FILE_CONTENT_TYPE)
            .lessionPlanFileLink(UPDATED_LESSION_PLAN_FILE_LINK)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);
        ClassLessionPlanDTO classLessionPlanDTO = classLessionPlanMapper.toDto(updatedClassLessionPlan);

        restClassLessionPlanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, classLessionPlanDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(classLessionPlanDTO))
            )
            .andExpect(status().isOk());

        // Validate the ClassLessionPlan in the database
        List<ClassLessionPlan> classLessionPlanList = classLessionPlanRepository.findAll();
        assertThat(classLessionPlanList).hasSize(databaseSizeBeforeUpdate);
        ClassLessionPlan testClassLessionPlan = classLessionPlanList.get(classLessionPlanList.size() - 1);
        assertThat(testClassLessionPlan.getSchoolDate()).isEqualTo(UPDATED_SCHOOL_DATE);
        assertThat(testClassLessionPlan.getClassWorkText()).isEqualTo(UPDATED_CLASS_WORK_TEXT);
        assertThat(testClassLessionPlan.getHomeWorkText()).isEqualTo(UPDATED_HOME_WORK_TEXT);
        assertThat(testClassLessionPlan.getWorkStatus()).isEqualTo(UPDATED_WORK_STATUS);
        assertThat(testClassLessionPlan.getLesionPlanFile()).isEqualTo(UPDATED_LESION_PLAN_FILE);
        assertThat(testClassLessionPlan.getLesionPlanFileContentType()).isEqualTo(UPDATED_LESION_PLAN_FILE_CONTENT_TYPE);
        assertThat(testClassLessionPlan.getLessionPlanFileLink()).isEqualTo(UPDATED_LESSION_PLAN_FILE_LINK);
        assertThat(testClassLessionPlan.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testClassLessionPlan.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testClassLessionPlan.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void putNonExistingClassLessionPlan() throws Exception {
        int databaseSizeBeforeUpdate = classLessionPlanRepository.findAll().size();
        classLessionPlan.setId(count.incrementAndGet());

        // Create the ClassLessionPlan
        ClassLessionPlanDTO classLessionPlanDTO = classLessionPlanMapper.toDto(classLessionPlan);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClassLessionPlanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, classLessionPlanDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(classLessionPlanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClassLessionPlan in the database
        List<ClassLessionPlan> classLessionPlanList = classLessionPlanRepository.findAll();
        assertThat(classLessionPlanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchClassLessionPlan() throws Exception {
        int databaseSizeBeforeUpdate = classLessionPlanRepository.findAll().size();
        classLessionPlan.setId(count.incrementAndGet());

        // Create the ClassLessionPlan
        ClassLessionPlanDTO classLessionPlanDTO = classLessionPlanMapper.toDto(classLessionPlan);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClassLessionPlanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(classLessionPlanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClassLessionPlan in the database
        List<ClassLessionPlan> classLessionPlanList = classLessionPlanRepository.findAll();
        assertThat(classLessionPlanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamClassLessionPlan() throws Exception {
        int databaseSizeBeforeUpdate = classLessionPlanRepository.findAll().size();
        classLessionPlan.setId(count.incrementAndGet());

        // Create the ClassLessionPlan
        ClassLessionPlanDTO classLessionPlanDTO = classLessionPlanMapper.toDto(classLessionPlan);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClassLessionPlanMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(classLessionPlanDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ClassLessionPlan in the database
        List<ClassLessionPlan> classLessionPlanList = classLessionPlanRepository.findAll();
        assertThat(classLessionPlanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateClassLessionPlanWithPatch() throws Exception {
        // Initialize the database
        classLessionPlanRepository.saveAndFlush(classLessionPlan);

        int databaseSizeBeforeUpdate = classLessionPlanRepository.findAll().size();

        // Update the classLessionPlan using partial update
        ClassLessionPlan partialUpdatedClassLessionPlan = new ClassLessionPlan();
        partialUpdatedClassLessionPlan.setId(classLessionPlan.getId());

        partialUpdatedClassLessionPlan
            .schoolDate(UPDATED_SCHOOL_DATE)
            .homeWorkText(UPDATED_HOME_WORK_TEXT)
            .workStatus(UPDATED_WORK_STATUS)
            .lesionPlanFile(UPDATED_LESION_PLAN_FILE)
            .lesionPlanFileContentType(UPDATED_LESION_PLAN_FILE_CONTENT_TYPE)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED);

        restClassLessionPlanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClassLessionPlan.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedClassLessionPlan))
            )
            .andExpect(status().isOk());

        // Validate the ClassLessionPlan in the database
        List<ClassLessionPlan> classLessionPlanList = classLessionPlanRepository.findAll();
        assertThat(classLessionPlanList).hasSize(databaseSizeBeforeUpdate);
        ClassLessionPlan testClassLessionPlan = classLessionPlanList.get(classLessionPlanList.size() - 1);
        assertThat(testClassLessionPlan.getSchoolDate()).isEqualTo(UPDATED_SCHOOL_DATE);
        assertThat(testClassLessionPlan.getClassWorkText()).isEqualTo(DEFAULT_CLASS_WORK_TEXT);
        assertThat(testClassLessionPlan.getHomeWorkText()).isEqualTo(UPDATED_HOME_WORK_TEXT);
        assertThat(testClassLessionPlan.getWorkStatus()).isEqualTo(UPDATED_WORK_STATUS);
        assertThat(testClassLessionPlan.getLesionPlanFile()).isEqualTo(UPDATED_LESION_PLAN_FILE);
        assertThat(testClassLessionPlan.getLesionPlanFileContentType()).isEqualTo(UPDATED_LESION_PLAN_FILE_CONTENT_TYPE);
        assertThat(testClassLessionPlan.getLessionPlanFileLink()).isEqualTo(DEFAULT_LESSION_PLAN_FILE_LINK);
        assertThat(testClassLessionPlan.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testClassLessionPlan.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testClassLessionPlan.getCancelDate()).isEqualTo(DEFAULT_CANCEL_DATE);
    }

    @Test
    @Transactional
    void fullUpdateClassLessionPlanWithPatch() throws Exception {
        // Initialize the database
        classLessionPlanRepository.saveAndFlush(classLessionPlan);

        int databaseSizeBeforeUpdate = classLessionPlanRepository.findAll().size();

        // Update the classLessionPlan using partial update
        ClassLessionPlan partialUpdatedClassLessionPlan = new ClassLessionPlan();
        partialUpdatedClassLessionPlan.setId(classLessionPlan.getId());

        partialUpdatedClassLessionPlan
            .schoolDate(UPDATED_SCHOOL_DATE)
            .classWorkText(UPDATED_CLASS_WORK_TEXT)
            .homeWorkText(UPDATED_HOME_WORK_TEXT)
            .workStatus(UPDATED_WORK_STATUS)
            .lesionPlanFile(UPDATED_LESION_PLAN_FILE)
            .lesionPlanFileContentType(UPDATED_LESION_PLAN_FILE_CONTENT_TYPE)
            .lessionPlanFileLink(UPDATED_LESSION_PLAN_FILE_LINK)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);

        restClassLessionPlanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClassLessionPlan.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedClassLessionPlan))
            )
            .andExpect(status().isOk());

        // Validate the ClassLessionPlan in the database
        List<ClassLessionPlan> classLessionPlanList = classLessionPlanRepository.findAll();
        assertThat(classLessionPlanList).hasSize(databaseSizeBeforeUpdate);
        ClassLessionPlan testClassLessionPlan = classLessionPlanList.get(classLessionPlanList.size() - 1);
        assertThat(testClassLessionPlan.getSchoolDate()).isEqualTo(UPDATED_SCHOOL_DATE);
        assertThat(testClassLessionPlan.getClassWorkText()).isEqualTo(UPDATED_CLASS_WORK_TEXT);
        assertThat(testClassLessionPlan.getHomeWorkText()).isEqualTo(UPDATED_HOME_WORK_TEXT);
        assertThat(testClassLessionPlan.getWorkStatus()).isEqualTo(UPDATED_WORK_STATUS);
        assertThat(testClassLessionPlan.getLesionPlanFile()).isEqualTo(UPDATED_LESION_PLAN_FILE);
        assertThat(testClassLessionPlan.getLesionPlanFileContentType()).isEqualTo(UPDATED_LESION_PLAN_FILE_CONTENT_TYPE);
        assertThat(testClassLessionPlan.getLessionPlanFileLink()).isEqualTo(UPDATED_LESSION_PLAN_FILE_LINK);
        assertThat(testClassLessionPlan.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testClassLessionPlan.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testClassLessionPlan.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingClassLessionPlan() throws Exception {
        int databaseSizeBeforeUpdate = classLessionPlanRepository.findAll().size();
        classLessionPlan.setId(count.incrementAndGet());

        // Create the ClassLessionPlan
        ClassLessionPlanDTO classLessionPlanDTO = classLessionPlanMapper.toDto(classLessionPlan);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClassLessionPlanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, classLessionPlanDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(classLessionPlanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClassLessionPlan in the database
        List<ClassLessionPlan> classLessionPlanList = classLessionPlanRepository.findAll();
        assertThat(classLessionPlanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchClassLessionPlan() throws Exception {
        int databaseSizeBeforeUpdate = classLessionPlanRepository.findAll().size();
        classLessionPlan.setId(count.incrementAndGet());

        // Create the ClassLessionPlan
        ClassLessionPlanDTO classLessionPlanDTO = classLessionPlanMapper.toDto(classLessionPlan);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClassLessionPlanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(classLessionPlanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClassLessionPlan in the database
        List<ClassLessionPlan> classLessionPlanList = classLessionPlanRepository.findAll();
        assertThat(classLessionPlanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamClassLessionPlan() throws Exception {
        int databaseSizeBeforeUpdate = classLessionPlanRepository.findAll().size();
        classLessionPlan.setId(count.incrementAndGet());

        // Create the ClassLessionPlan
        ClassLessionPlanDTO classLessionPlanDTO = classLessionPlanMapper.toDto(classLessionPlan);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClassLessionPlanMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(classLessionPlanDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ClassLessionPlan in the database
        List<ClassLessionPlan> classLessionPlanList = classLessionPlanRepository.findAll();
        assertThat(classLessionPlanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteClassLessionPlan() throws Exception {
        // Initialize the database
        classLessionPlanRepository.saveAndFlush(classLessionPlan);

        int databaseSizeBeforeDelete = classLessionPlanRepository.findAll().size();

        // Delete the classLessionPlan
        restClassLessionPlanMockMvc
            .perform(delete(ENTITY_API_URL_ID, classLessionPlan.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ClassLessionPlan> classLessionPlanList = classLessionPlanRepository.findAll();
        assertThat(classLessionPlanList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
