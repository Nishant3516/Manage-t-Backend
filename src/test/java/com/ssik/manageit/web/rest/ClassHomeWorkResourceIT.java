package com.ssik.manageit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ssik.manageit.IntegrationTest;
import com.ssik.manageit.domain.ChapterSection;
import com.ssik.manageit.domain.ClassHomeWork;
import com.ssik.manageit.domain.StudentHomeWorkTrack;
import com.ssik.manageit.domain.enumeration.StudentAssignmentType;
import com.ssik.manageit.repository.ClassHomeWorkRepository;
import com.ssik.manageit.service.criteria.ClassHomeWorkCriteria;
import com.ssik.manageit.service.dto.ClassHomeWorkDTO;
import com.ssik.manageit.service.mapper.ClassHomeWorkMapper;
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
 * Integration tests for the {@link ClassHomeWorkResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ClassHomeWorkResourceIT {

    private static final LocalDate DEFAULT_SCHOOL_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_SCHOOL_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_SCHOOL_DATE = LocalDate.ofEpochDay(-1L);

    private static final StudentAssignmentType DEFAULT_STUDENT_ASSIGNMENT_TYPE = StudentAssignmentType.READING_WRITING;
    private static final StudentAssignmentType UPDATED_STUDENT_ASSIGNMENT_TYPE = StudentAssignmentType.PROJECT;

    private static final String DEFAULT_HOME_WORK_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_HOME_WORK_TEXT = "BBBBBBBBBB";

    private static final byte[] DEFAULT_HOME_WORK_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_HOME_WORK_FILE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_HOME_WORK_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_HOME_WORK_FILE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_HOME_WORK_FILE_LINK = "AAAAAAAAAA";
    private static final String UPDATED_HOME_WORK_FILE_LINK = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ASSIGN = false;
    private static final Boolean UPDATED_ASSIGN = true;

    private static final LocalDate DEFAULT_CREATE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATE_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CREATE_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_LAST_MODIFIED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_MODIFIED = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_LAST_MODIFIED = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_CANCEL_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CANCEL_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CANCEL_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/class-home-works";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ClassHomeWorkRepository classHomeWorkRepository;

    @Autowired
    private ClassHomeWorkMapper classHomeWorkMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restClassHomeWorkMockMvc;

    private ClassHomeWork classHomeWork;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClassHomeWork createEntity(EntityManager em) {
        ClassHomeWork classHomeWork = new ClassHomeWork()
            .schoolDate(DEFAULT_SCHOOL_DATE)
            .studentAssignmentType(DEFAULT_STUDENT_ASSIGNMENT_TYPE)
            .homeWorkText(DEFAULT_HOME_WORK_TEXT)
            .homeWorkFile(DEFAULT_HOME_WORK_FILE)
            .homeWorkFileContentType(DEFAULT_HOME_WORK_FILE_CONTENT_TYPE)
            .homeWorkFileLink(DEFAULT_HOME_WORK_FILE_LINK)
            .assign(DEFAULT_ASSIGN)
            .createDate(DEFAULT_CREATE_DATE)
            .lastModified(DEFAULT_LAST_MODIFIED)
            .cancelDate(DEFAULT_CANCEL_DATE);
        return classHomeWork;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClassHomeWork createUpdatedEntity(EntityManager em) {
        ClassHomeWork classHomeWork = new ClassHomeWork()
            .schoolDate(UPDATED_SCHOOL_DATE)
            .studentAssignmentType(UPDATED_STUDENT_ASSIGNMENT_TYPE)
            .homeWorkText(UPDATED_HOME_WORK_TEXT)
            .homeWorkFile(UPDATED_HOME_WORK_FILE)
            .homeWorkFileContentType(UPDATED_HOME_WORK_FILE_CONTENT_TYPE)
            .homeWorkFileLink(UPDATED_HOME_WORK_FILE_LINK)
            .assign(UPDATED_ASSIGN)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);
        return classHomeWork;
    }

    @BeforeEach
    public void initTest() {
        classHomeWork = createEntity(em);
    }

    @Test
    @Transactional
    void createClassHomeWork() throws Exception {
        int databaseSizeBeforeCreate = classHomeWorkRepository.findAll().size();
        // Create the ClassHomeWork
        ClassHomeWorkDTO classHomeWorkDTO = classHomeWorkMapper.toDto(classHomeWork);
        restClassHomeWorkMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(classHomeWorkDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ClassHomeWork in the database
        List<ClassHomeWork> classHomeWorkList = classHomeWorkRepository.findAll();
        assertThat(classHomeWorkList).hasSize(databaseSizeBeforeCreate + 1);
        ClassHomeWork testClassHomeWork = classHomeWorkList.get(classHomeWorkList.size() - 1);
        assertThat(testClassHomeWork.getSchoolDate()).isEqualTo(DEFAULT_SCHOOL_DATE);
        assertThat(testClassHomeWork.getStudentAssignmentType()).isEqualTo(DEFAULT_STUDENT_ASSIGNMENT_TYPE);
        assertThat(testClassHomeWork.getHomeWorkText()).isEqualTo(DEFAULT_HOME_WORK_TEXT);
        assertThat(testClassHomeWork.getHomeWorkFile()).isEqualTo(DEFAULT_HOME_WORK_FILE);
        assertThat(testClassHomeWork.getHomeWorkFileContentType()).isEqualTo(DEFAULT_HOME_WORK_FILE_CONTENT_TYPE);
        assertThat(testClassHomeWork.getHomeWorkFileLink()).isEqualTo(DEFAULT_HOME_WORK_FILE_LINK);
        assertThat(testClassHomeWork.getAssign()).isEqualTo(DEFAULT_ASSIGN);
        assertThat(testClassHomeWork.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testClassHomeWork.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
        assertThat(testClassHomeWork.getCancelDate()).isEqualTo(DEFAULT_CANCEL_DATE);
    }

    @Test
    @Transactional
    void createClassHomeWorkWithExistingId() throws Exception {
        // Create the ClassHomeWork with an existing ID
        classHomeWork.setId(1L);
        ClassHomeWorkDTO classHomeWorkDTO = classHomeWorkMapper.toDto(classHomeWork);

        int databaseSizeBeforeCreate = classHomeWorkRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restClassHomeWorkMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(classHomeWorkDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClassHomeWork in the database
        List<ClassHomeWork> classHomeWorkList = classHomeWorkRepository.findAll();
        assertThat(classHomeWorkList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSchoolDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = classHomeWorkRepository.findAll().size();
        // set the field null
        classHomeWork.setSchoolDate(null);

        // Create the ClassHomeWork, which fails.
        ClassHomeWorkDTO classHomeWorkDTO = classHomeWorkMapper.toDto(classHomeWork);

        restClassHomeWorkMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(classHomeWorkDTO))
            )
            .andExpect(status().isBadRequest());

        List<ClassHomeWork> classHomeWorkList = classHomeWorkRepository.findAll();
        assertThat(classHomeWorkList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStudentAssignmentTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = classHomeWorkRepository.findAll().size();
        // set the field null
        classHomeWork.setStudentAssignmentType(null);

        // Create the ClassHomeWork, which fails.
        ClassHomeWorkDTO classHomeWorkDTO = classHomeWorkMapper.toDto(classHomeWork);

        restClassHomeWorkMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(classHomeWorkDTO))
            )
            .andExpect(status().isBadRequest());

        List<ClassHomeWork> classHomeWorkList = classHomeWorkRepository.findAll();
        assertThat(classHomeWorkList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkHomeWorkTextIsRequired() throws Exception {
        int databaseSizeBeforeTest = classHomeWorkRepository.findAll().size();
        // set the field null
        classHomeWork.setHomeWorkText(null);

        // Create the ClassHomeWork, which fails.
        ClassHomeWorkDTO classHomeWorkDTO = classHomeWorkMapper.toDto(classHomeWork);

        restClassHomeWorkMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(classHomeWorkDTO))
            )
            .andExpect(status().isBadRequest());

        List<ClassHomeWork> classHomeWorkList = classHomeWorkRepository.findAll();
        assertThat(classHomeWorkList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllClassHomeWorks() throws Exception {
        // Initialize the database
        classHomeWorkRepository.saveAndFlush(classHomeWork);

        // Get all the classHomeWorkList
        restClassHomeWorkMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(classHomeWork.getId().intValue())))
            .andExpect(jsonPath("$.[*].schoolDate").value(hasItem(DEFAULT_SCHOOL_DATE.toString())))
            .andExpect(jsonPath("$.[*].studentAssignmentType").value(hasItem(DEFAULT_STUDENT_ASSIGNMENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].homeWorkText").value(hasItem(DEFAULT_HOME_WORK_TEXT)))
            .andExpect(jsonPath("$.[*].homeWorkFileContentType").value(hasItem(DEFAULT_HOME_WORK_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].homeWorkFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_HOME_WORK_FILE))))
            .andExpect(jsonPath("$.[*].homeWorkFileLink").value(hasItem(DEFAULT_HOME_WORK_FILE_LINK)))
            .andExpect(jsonPath("$.[*].assign").value(hasItem(DEFAULT_ASSIGN.booleanValue())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].cancelDate").value(hasItem(DEFAULT_CANCEL_DATE.toString())));
    }

    @Test
    @Transactional
    void getClassHomeWork() throws Exception {
        // Initialize the database
        classHomeWorkRepository.saveAndFlush(classHomeWork);

        // Get the classHomeWork
        restClassHomeWorkMockMvc
            .perform(get(ENTITY_API_URL_ID, classHomeWork.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(classHomeWork.getId().intValue()))
            .andExpect(jsonPath("$.schoolDate").value(DEFAULT_SCHOOL_DATE.toString()))
            .andExpect(jsonPath("$.studentAssignmentType").value(DEFAULT_STUDENT_ASSIGNMENT_TYPE.toString()))
            .andExpect(jsonPath("$.homeWorkText").value(DEFAULT_HOME_WORK_TEXT))
            .andExpect(jsonPath("$.homeWorkFileContentType").value(DEFAULT_HOME_WORK_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.homeWorkFile").value(Base64Utils.encodeToString(DEFAULT_HOME_WORK_FILE)))
            .andExpect(jsonPath("$.homeWorkFileLink").value(DEFAULT_HOME_WORK_FILE_LINK))
            .andExpect(jsonPath("$.assign").value(DEFAULT_ASSIGN.booleanValue()))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.lastModified").value(DEFAULT_LAST_MODIFIED.toString()))
            .andExpect(jsonPath("$.cancelDate").value(DEFAULT_CANCEL_DATE.toString()));
    }

    @Test
    @Transactional
    void getClassHomeWorksByIdFiltering() throws Exception {
        // Initialize the database
        classHomeWorkRepository.saveAndFlush(classHomeWork);

        Long id = classHomeWork.getId();

        defaultClassHomeWorkShouldBeFound("id.equals=" + id);
        defaultClassHomeWorkShouldNotBeFound("id.notEquals=" + id);

        defaultClassHomeWorkShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultClassHomeWorkShouldNotBeFound("id.greaterThan=" + id);

        defaultClassHomeWorkShouldBeFound("id.lessThanOrEqual=" + id);
        defaultClassHomeWorkShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllClassHomeWorksBySchoolDateIsEqualToSomething() throws Exception {
        // Initialize the database
        classHomeWorkRepository.saveAndFlush(classHomeWork);

        // Get all the classHomeWorkList where schoolDate equals to DEFAULT_SCHOOL_DATE
        defaultClassHomeWorkShouldBeFound("schoolDate.equals=" + DEFAULT_SCHOOL_DATE);

        // Get all the classHomeWorkList where schoolDate equals to UPDATED_SCHOOL_DATE
        defaultClassHomeWorkShouldNotBeFound("schoolDate.equals=" + UPDATED_SCHOOL_DATE);
    }

    @Test
    @Transactional
    void getAllClassHomeWorksBySchoolDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classHomeWorkRepository.saveAndFlush(classHomeWork);

        // Get all the classHomeWorkList where schoolDate not equals to DEFAULT_SCHOOL_DATE
        defaultClassHomeWorkShouldNotBeFound("schoolDate.notEquals=" + DEFAULT_SCHOOL_DATE);

        // Get all the classHomeWorkList where schoolDate not equals to UPDATED_SCHOOL_DATE
        defaultClassHomeWorkShouldBeFound("schoolDate.notEquals=" + UPDATED_SCHOOL_DATE);
    }

    @Test
    @Transactional
    void getAllClassHomeWorksBySchoolDateIsInShouldWork() throws Exception {
        // Initialize the database
        classHomeWorkRepository.saveAndFlush(classHomeWork);

        // Get all the classHomeWorkList where schoolDate in DEFAULT_SCHOOL_DATE or UPDATED_SCHOOL_DATE
        defaultClassHomeWorkShouldBeFound("schoolDate.in=" + DEFAULT_SCHOOL_DATE + "," + UPDATED_SCHOOL_DATE);

        // Get all the classHomeWorkList where schoolDate equals to UPDATED_SCHOOL_DATE
        defaultClassHomeWorkShouldNotBeFound("schoolDate.in=" + UPDATED_SCHOOL_DATE);
    }

    @Test
    @Transactional
    void getAllClassHomeWorksBySchoolDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        classHomeWorkRepository.saveAndFlush(classHomeWork);

        // Get all the classHomeWorkList where schoolDate is not null
        defaultClassHomeWorkShouldBeFound("schoolDate.specified=true");

        // Get all the classHomeWorkList where schoolDate is null
        defaultClassHomeWorkShouldNotBeFound("schoolDate.specified=false");
    }

    @Test
    @Transactional
    void getAllClassHomeWorksBySchoolDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        classHomeWorkRepository.saveAndFlush(classHomeWork);

        // Get all the classHomeWorkList where schoolDate is greater than or equal to DEFAULT_SCHOOL_DATE
        defaultClassHomeWorkShouldBeFound("schoolDate.greaterThanOrEqual=" + DEFAULT_SCHOOL_DATE);

        // Get all the classHomeWorkList where schoolDate is greater than or equal to UPDATED_SCHOOL_DATE
        defaultClassHomeWorkShouldNotBeFound("schoolDate.greaterThanOrEqual=" + UPDATED_SCHOOL_DATE);
    }

    @Test
    @Transactional
    void getAllClassHomeWorksBySchoolDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        classHomeWorkRepository.saveAndFlush(classHomeWork);

        // Get all the classHomeWorkList where schoolDate is less than or equal to DEFAULT_SCHOOL_DATE
        defaultClassHomeWorkShouldBeFound("schoolDate.lessThanOrEqual=" + DEFAULT_SCHOOL_DATE);

        // Get all the classHomeWorkList where schoolDate is less than or equal to SMALLER_SCHOOL_DATE
        defaultClassHomeWorkShouldNotBeFound("schoolDate.lessThanOrEqual=" + SMALLER_SCHOOL_DATE);
    }

    @Test
    @Transactional
    void getAllClassHomeWorksBySchoolDateIsLessThanSomething() throws Exception {
        // Initialize the database
        classHomeWorkRepository.saveAndFlush(classHomeWork);

        // Get all the classHomeWorkList where schoolDate is less than DEFAULT_SCHOOL_DATE
        defaultClassHomeWorkShouldNotBeFound("schoolDate.lessThan=" + DEFAULT_SCHOOL_DATE);

        // Get all the classHomeWorkList where schoolDate is less than UPDATED_SCHOOL_DATE
        defaultClassHomeWorkShouldBeFound("schoolDate.lessThan=" + UPDATED_SCHOOL_DATE);
    }

    @Test
    @Transactional
    void getAllClassHomeWorksBySchoolDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        classHomeWorkRepository.saveAndFlush(classHomeWork);

        // Get all the classHomeWorkList where schoolDate is greater than DEFAULT_SCHOOL_DATE
        defaultClassHomeWorkShouldNotBeFound("schoolDate.greaterThan=" + DEFAULT_SCHOOL_DATE);

        // Get all the classHomeWorkList where schoolDate is greater than SMALLER_SCHOOL_DATE
        defaultClassHomeWorkShouldBeFound("schoolDate.greaterThan=" + SMALLER_SCHOOL_DATE);
    }

    @Test
    @Transactional
    void getAllClassHomeWorksByStudentAssignmentTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        classHomeWorkRepository.saveAndFlush(classHomeWork);

        // Get all the classHomeWorkList where studentAssignmentType equals to DEFAULT_STUDENT_ASSIGNMENT_TYPE
        defaultClassHomeWorkShouldBeFound("studentAssignmentType.equals=" + DEFAULT_STUDENT_ASSIGNMENT_TYPE);

        // Get all the classHomeWorkList where studentAssignmentType equals to UPDATED_STUDENT_ASSIGNMENT_TYPE
        defaultClassHomeWorkShouldNotBeFound("studentAssignmentType.equals=" + UPDATED_STUDENT_ASSIGNMENT_TYPE);
    }

    @Test
    @Transactional
    void getAllClassHomeWorksByStudentAssignmentTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classHomeWorkRepository.saveAndFlush(classHomeWork);

        // Get all the classHomeWorkList where studentAssignmentType not equals to DEFAULT_STUDENT_ASSIGNMENT_TYPE
        defaultClassHomeWorkShouldNotBeFound("studentAssignmentType.notEquals=" + DEFAULT_STUDENT_ASSIGNMENT_TYPE);

        // Get all the classHomeWorkList where studentAssignmentType not equals to UPDATED_STUDENT_ASSIGNMENT_TYPE
        defaultClassHomeWorkShouldBeFound("studentAssignmentType.notEquals=" + UPDATED_STUDENT_ASSIGNMENT_TYPE);
    }

    @Test
    @Transactional
    void getAllClassHomeWorksByStudentAssignmentTypeIsInShouldWork() throws Exception {
        // Initialize the database
        classHomeWorkRepository.saveAndFlush(classHomeWork);

        // Get all the classHomeWorkList where studentAssignmentType in DEFAULT_STUDENT_ASSIGNMENT_TYPE or UPDATED_STUDENT_ASSIGNMENT_TYPE
        defaultClassHomeWorkShouldBeFound(
            "studentAssignmentType.in=" + DEFAULT_STUDENT_ASSIGNMENT_TYPE + "," + UPDATED_STUDENT_ASSIGNMENT_TYPE
        );

        // Get all the classHomeWorkList where studentAssignmentType equals to UPDATED_STUDENT_ASSIGNMENT_TYPE
        defaultClassHomeWorkShouldNotBeFound("studentAssignmentType.in=" + UPDATED_STUDENT_ASSIGNMENT_TYPE);
    }

    @Test
    @Transactional
    void getAllClassHomeWorksByStudentAssignmentTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        classHomeWorkRepository.saveAndFlush(classHomeWork);

        // Get all the classHomeWorkList where studentAssignmentType is not null
        defaultClassHomeWorkShouldBeFound("studentAssignmentType.specified=true");

        // Get all the classHomeWorkList where studentAssignmentType is null
        defaultClassHomeWorkShouldNotBeFound("studentAssignmentType.specified=false");
    }

    @Test
    @Transactional
    void getAllClassHomeWorksByHomeWorkTextIsEqualToSomething() throws Exception {
        // Initialize the database
        classHomeWorkRepository.saveAndFlush(classHomeWork);

        // Get all the classHomeWorkList where homeWorkText equals to DEFAULT_HOME_WORK_TEXT
        defaultClassHomeWorkShouldBeFound("homeWorkText.equals=" + DEFAULT_HOME_WORK_TEXT);

        // Get all the classHomeWorkList where homeWorkText equals to UPDATED_HOME_WORK_TEXT
        defaultClassHomeWorkShouldNotBeFound("homeWorkText.equals=" + UPDATED_HOME_WORK_TEXT);
    }

    @Test
    @Transactional
    void getAllClassHomeWorksByHomeWorkTextIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classHomeWorkRepository.saveAndFlush(classHomeWork);

        // Get all the classHomeWorkList where homeWorkText not equals to DEFAULT_HOME_WORK_TEXT
        defaultClassHomeWorkShouldNotBeFound("homeWorkText.notEquals=" + DEFAULT_HOME_WORK_TEXT);

        // Get all the classHomeWorkList where homeWorkText not equals to UPDATED_HOME_WORK_TEXT
        defaultClassHomeWorkShouldBeFound("homeWorkText.notEquals=" + UPDATED_HOME_WORK_TEXT);
    }

    @Test
    @Transactional
    void getAllClassHomeWorksByHomeWorkTextIsInShouldWork() throws Exception {
        // Initialize the database
        classHomeWorkRepository.saveAndFlush(classHomeWork);

        // Get all the classHomeWorkList where homeWorkText in DEFAULT_HOME_WORK_TEXT or UPDATED_HOME_WORK_TEXT
        defaultClassHomeWorkShouldBeFound("homeWorkText.in=" + DEFAULT_HOME_WORK_TEXT + "," + UPDATED_HOME_WORK_TEXT);

        // Get all the classHomeWorkList where homeWorkText equals to UPDATED_HOME_WORK_TEXT
        defaultClassHomeWorkShouldNotBeFound("homeWorkText.in=" + UPDATED_HOME_WORK_TEXT);
    }

    @Test
    @Transactional
    void getAllClassHomeWorksByHomeWorkTextIsNullOrNotNull() throws Exception {
        // Initialize the database
        classHomeWorkRepository.saveAndFlush(classHomeWork);

        // Get all the classHomeWorkList where homeWorkText is not null
        defaultClassHomeWorkShouldBeFound("homeWorkText.specified=true");

        // Get all the classHomeWorkList where homeWorkText is null
        defaultClassHomeWorkShouldNotBeFound("homeWorkText.specified=false");
    }

    @Test
    @Transactional
    void getAllClassHomeWorksByHomeWorkTextContainsSomething() throws Exception {
        // Initialize the database
        classHomeWorkRepository.saveAndFlush(classHomeWork);

        // Get all the classHomeWorkList where homeWorkText contains DEFAULT_HOME_WORK_TEXT
        defaultClassHomeWorkShouldBeFound("homeWorkText.contains=" + DEFAULT_HOME_WORK_TEXT);

        // Get all the classHomeWorkList where homeWorkText contains UPDATED_HOME_WORK_TEXT
        defaultClassHomeWorkShouldNotBeFound("homeWorkText.contains=" + UPDATED_HOME_WORK_TEXT);
    }

    @Test
    @Transactional
    void getAllClassHomeWorksByHomeWorkTextNotContainsSomething() throws Exception {
        // Initialize the database
        classHomeWorkRepository.saveAndFlush(classHomeWork);

        // Get all the classHomeWorkList where homeWorkText does not contain DEFAULT_HOME_WORK_TEXT
        defaultClassHomeWorkShouldNotBeFound("homeWorkText.doesNotContain=" + DEFAULT_HOME_WORK_TEXT);

        // Get all the classHomeWorkList where homeWorkText does not contain UPDATED_HOME_WORK_TEXT
        defaultClassHomeWorkShouldBeFound("homeWorkText.doesNotContain=" + UPDATED_HOME_WORK_TEXT);
    }

    @Test
    @Transactional
    void getAllClassHomeWorksByHomeWorkFileLinkIsEqualToSomething() throws Exception {
        // Initialize the database
        classHomeWorkRepository.saveAndFlush(classHomeWork);

        // Get all the classHomeWorkList where homeWorkFileLink equals to DEFAULT_HOME_WORK_FILE_LINK
        defaultClassHomeWorkShouldBeFound("homeWorkFileLink.equals=" + DEFAULT_HOME_WORK_FILE_LINK);

        // Get all the classHomeWorkList where homeWorkFileLink equals to UPDATED_HOME_WORK_FILE_LINK
        defaultClassHomeWorkShouldNotBeFound("homeWorkFileLink.equals=" + UPDATED_HOME_WORK_FILE_LINK);
    }

    @Test
    @Transactional
    void getAllClassHomeWorksByHomeWorkFileLinkIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classHomeWorkRepository.saveAndFlush(classHomeWork);

        // Get all the classHomeWorkList where homeWorkFileLink not equals to DEFAULT_HOME_WORK_FILE_LINK
        defaultClassHomeWorkShouldNotBeFound("homeWorkFileLink.notEquals=" + DEFAULT_HOME_WORK_FILE_LINK);

        // Get all the classHomeWorkList where homeWorkFileLink not equals to UPDATED_HOME_WORK_FILE_LINK
        defaultClassHomeWorkShouldBeFound("homeWorkFileLink.notEquals=" + UPDATED_HOME_WORK_FILE_LINK);
    }

    @Test
    @Transactional
    void getAllClassHomeWorksByHomeWorkFileLinkIsInShouldWork() throws Exception {
        // Initialize the database
        classHomeWorkRepository.saveAndFlush(classHomeWork);

        // Get all the classHomeWorkList where homeWorkFileLink in DEFAULT_HOME_WORK_FILE_LINK or UPDATED_HOME_WORK_FILE_LINK
        defaultClassHomeWorkShouldBeFound("homeWorkFileLink.in=" + DEFAULT_HOME_WORK_FILE_LINK + "," + UPDATED_HOME_WORK_FILE_LINK);

        // Get all the classHomeWorkList where homeWorkFileLink equals to UPDATED_HOME_WORK_FILE_LINK
        defaultClassHomeWorkShouldNotBeFound("homeWorkFileLink.in=" + UPDATED_HOME_WORK_FILE_LINK);
    }

    @Test
    @Transactional
    void getAllClassHomeWorksByHomeWorkFileLinkIsNullOrNotNull() throws Exception {
        // Initialize the database
        classHomeWorkRepository.saveAndFlush(classHomeWork);

        // Get all the classHomeWorkList where homeWorkFileLink is not null
        defaultClassHomeWorkShouldBeFound("homeWorkFileLink.specified=true");

        // Get all the classHomeWorkList where homeWorkFileLink is null
        defaultClassHomeWorkShouldNotBeFound("homeWorkFileLink.specified=false");
    }

    @Test
    @Transactional
    void getAllClassHomeWorksByHomeWorkFileLinkContainsSomething() throws Exception {
        // Initialize the database
        classHomeWorkRepository.saveAndFlush(classHomeWork);

        // Get all the classHomeWorkList where homeWorkFileLink contains DEFAULT_HOME_WORK_FILE_LINK
        defaultClassHomeWorkShouldBeFound("homeWorkFileLink.contains=" + DEFAULT_HOME_WORK_FILE_LINK);

        // Get all the classHomeWorkList where homeWorkFileLink contains UPDATED_HOME_WORK_FILE_LINK
        defaultClassHomeWorkShouldNotBeFound("homeWorkFileLink.contains=" + UPDATED_HOME_WORK_FILE_LINK);
    }

    @Test
    @Transactional
    void getAllClassHomeWorksByHomeWorkFileLinkNotContainsSomething() throws Exception {
        // Initialize the database
        classHomeWorkRepository.saveAndFlush(classHomeWork);

        // Get all the classHomeWorkList where homeWorkFileLink does not contain DEFAULT_HOME_WORK_FILE_LINK
        defaultClassHomeWorkShouldNotBeFound("homeWorkFileLink.doesNotContain=" + DEFAULT_HOME_WORK_FILE_LINK);

        // Get all the classHomeWorkList where homeWorkFileLink does not contain UPDATED_HOME_WORK_FILE_LINK
        defaultClassHomeWorkShouldBeFound("homeWorkFileLink.doesNotContain=" + UPDATED_HOME_WORK_FILE_LINK);
    }

    @Test
    @Transactional
    void getAllClassHomeWorksByAssignIsEqualToSomething() throws Exception {
        // Initialize the database
        classHomeWorkRepository.saveAndFlush(classHomeWork);

        // Get all the classHomeWorkList where assign equals to DEFAULT_ASSIGN
        defaultClassHomeWorkShouldBeFound("assign.equals=" + DEFAULT_ASSIGN);

        // Get all the classHomeWorkList where assign equals to UPDATED_ASSIGN
        defaultClassHomeWorkShouldNotBeFound("assign.equals=" + UPDATED_ASSIGN);
    }

    @Test
    @Transactional
    void getAllClassHomeWorksByAssignIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classHomeWorkRepository.saveAndFlush(classHomeWork);

        // Get all the classHomeWorkList where assign not equals to DEFAULT_ASSIGN
        defaultClassHomeWorkShouldNotBeFound("assign.notEquals=" + DEFAULT_ASSIGN);

        // Get all the classHomeWorkList where assign not equals to UPDATED_ASSIGN
        defaultClassHomeWorkShouldBeFound("assign.notEquals=" + UPDATED_ASSIGN);
    }

    @Test
    @Transactional
    void getAllClassHomeWorksByAssignIsInShouldWork() throws Exception {
        // Initialize the database
        classHomeWorkRepository.saveAndFlush(classHomeWork);

        // Get all the classHomeWorkList where assign in DEFAULT_ASSIGN or UPDATED_ASSIGN
        defaultClassHomeWorkShouldBeFound("assign.in=" + DEFAULT_ASSIGN + "," + UPDATED_ASSIGN);

        // Get all the classHomeWorkList where assign equals to UPDATED_ASSIGN
        defaultClassHomeWorkShouldNotBeFound("assign.in=" + UPDATED_ASSIGN);
    }

    @Test
    @Transactional
    void getAllClassHomeWorksByAssignIsNullOrNotNull() throws Exception {
        // Initialize the database
        classHomeWorkRepository.saveAndFlush(classHomeWork);

        // Get all the classHomeWorkList where assign is not null
        defaultClassHomeWorkShouldBeFound("assign.specified=true");

        // Get all the classHomeWorkList where assign is null
        defaultClassHomeWorkShouldNotBeFound("assign.specified=false");
    }

    @Test
    @Transactional
    void getAllClassHomeWorksByCreateDateIsEqualToSomething() throws Exception {
        // Initialize the database
        classHomeWorkRepository.saveAndFlush(classHomeWork);

        // Get all the classHomeWorkList where createDate equals to DEFAULT_CREATE_DATE
        defaultClassHomeWorkShouldBeFound("createDate.equals=" + DEFAULT_CREATE_DATE);

        // Get all the classHomeWorkList where createDate equals to UPDATED_CREATE_DATE
        defaultClassHomeWorkShouldNotBeFound("createDate.equals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllClassHomeWorksByCreateDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classHomeWorkRepository.saveAndFlush(classHomeWork);

        // Get all the classHomeWorkList where createDate not equals to DEFAULT_CREATE_DATE
        defaultClassHomeWorkShouldNotBeFound("createDate.notEquals=" + DEFAULT_CREATE_DATE);

        // Get all the classHomeWorkList where createDate not equals to UPDATED_CREATE_DATE
        defaultClassHomeWorkShouldBeFound("createDate.notEquals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllClassHomeWorksByCreateDateIsInShouldWork() throws Exception {
        // Initialize the database
        classHomeWorkRepository.saveAndFlush(classHomeWork);

        // Get all the classHomeWorkList where createDate in DEFAULT_CREATE_DATE or UPDATED_CREATE_DATE
        defaultClassHomeWorkShouldBeFound("createDate.in=" + DEFAULT_CREATE_DATE + "," + UPDATED_CREATE_DATE);

        // Get all the classHomeWorkList where createDate equals to UPDATED_CREATE_DATE
        defaultClassHomeWorkShouldNotBeFound("createDate.in=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllClassHomeWorksByCreateDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        classHomeWorkRepository.saveAndFlush(classHomeWork);

        // Get all the classHomeWorkList where createDate is not null
        defaultClassHomeWorkShouldBeFound("createDate.specified=true");

        // Get all the classHomeWorkList where createDate is null
        defaultClassHomeWorkShouldNotBeFound("createDate.specified=false");
    }

    @Test
    @Transactional
    void getAllClassHomeWorksByCreateDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        classHomeWorkRepository.saveAndFlush(classHomeWork);

        // Get all the classHomeWorkList where createDate is greater than or equal to DEFAULT_CREATE_DATE
        defaultClassHomeWorkShouldBeFound("createDate.greaterThanOrEqual=" + DEFAULT_CREATE_DATE);

        // Get all the classHomeWorkList where createDate is greater than or equal to UPDATED_CREATE_DATE
        defaultClassHomeWorkShouldNotBeFound("createDate.greaterThanOrEqual=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllClassHomeWorksByCreateDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        classHomeWorkRepository.saveAndFlush(classHomeWork);

        // Get all the classHomeWorkList where createDate is less than or equal to DEFAULT_CREATE_DATE
        defaultClassHomeWorkShouldBeFound("createDate.lessThanOrEqual=" + DEFAULT_CREATE_DATE);

        // Get all the classHomeWorkList where createDate is less than or equal to SMALLER_CREATE_DATE
        defaultClassHomeWorkShouldNotBeFound("createDate.lessThanOrEqual=" + SMALLER_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllClassHomeWorksByCreateDateIsLessThanSomething() throws Exception {
        // Initialize the database
        classHomeWorkRepository.saveAndFlush(classHomeWork);

        // Get all the classHomeWorkList where createDate is less than DEFAULT_CREATE_DATE
        defaultClassHomeWorkShouldNotBeFound("createDate.lessThan=" + DEFAULT_CREATE_DATE);

        // Get all the classHomeWorkList where createDate is less than UPDATED_CREATE_DATE
        defaultClassHomeWorkShouldBeFound("createDate.lessThan=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllClassHomeWorksByCreateDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        classHomeWorkRepository.saveAndFlush(classHomeWork);

        // Get all the classHomeWorkList where createDate is greater than DEFAULT_CREATE_DATE
        defaultClassHomeWorkShouldNotBeFound("createDate.greaterThan=" + DEFAULT_CREATE_DATE);

        // Get all the classHomeWorkList where createDate is greater than SMALLER_CREATE_DATE
        defaultClassHomeWorkShouldBeFound("createDate.greaterThan=" + SMALLER_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllClassHomeWorksByLastModifiedIsEqualToSomething() throws Exception {
        // Initialize the database
        classHomeWorkRepository.saveAndFlush(classHomeWork);

        // Get all the classHomeWorkList where lastModified equals to DEFAULT_LAST_MODIFIED
        defaultClassHomeWorkShouldBeFound("lastModified.equals=" + DEFAULT_LAST_MODIFIED);

        // Get all the classHomeWorkList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultClassHomeWorkShouldNotBeFound("lastModified.equals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllClassHomeWorksByLastModifiedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classHomeWorkRepository.saveAndFlush(classHomeWork);

        // Get all the classHomeWorkList where lastModified not equals to DEFAULT_LAST_MODIFIED
        defaultClassHomeWorkShouldNotBeFound("lastModified.notEquals=" + DEFAULT_LAST_MODIFIED);

        // Get all the classHomeWorkList where lastModified not equals to UPDATED_LAST_MODIFIED
        defaultClassHomeWorkShouldBeFound("lastModified.notEquals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllClassHomeWorksByLastModifiedIsInShouldWork() throws Exception {
        // Initialize the database
        classHomeWorkRepository.saveAndFlush(classHomeWork);

        // Get all the classHomeWorkList where lastModified in DEFAULT_LAST_MODIFIED or UPDATED_LAST_MODIFIED
        defaultClassHomeWorkShouldBeFound("lastModified.in=" + DEFAULT_LAST_MODIFIED + "," + UPDATED_LAST_MODIFIED);

        // Get all the classHomeWorkList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultClassHomeWorkShouldNotBeFound("lastModified.in=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllClassHomeWorksByLastModifiedIsNullOrNotNull() throws Exception {
        // Initialize the database
        classHomeWorkRepository.saveAndFlush(classHomeWork);

        // Get all the classHomeWorkList where lastModified is not null
        defaultClassHomeWorkShouldBeFound("lastModified.specified=true");

        // Get all the classHomeWorkList where lastModified is null
        defaultClassHomeWorkShouldNotBeFound("lastModified.specified=false");
    }

    @Test
    @Transactional
    void getAllClassHomeWorksByLastModifiedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        classHomeWorkRepository.saveAndFlush(classHomeWork);

        // Get all the classHomeWorkList where lastModified is greater than or equal to DEFAULT_LAST_MODIFIED
        defaultClassHomeWorkShouldBeFound("lastModified.greaterThanOrEqual=" + DEFAULT_LAST_MODIFIED);

        // Get all the classHomeWorkList where lastModified is greater than or equal to UPDATED_LAST_MODIFIED
        defaultClassHomeWorkShouldNotBeFound("lastModified.greaterThanOrEqual=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllClassHomeWorksByLastModifiedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        classHomeWorkRepository.saveAndFlush(classHomeWork);

        // Get all the classHomeWorkList where lastModified is less than or equal to DEFAULT_LAST_MODIFIED
        defaultClassHomeWorkShouldBeFound("lastModified.lessThanOrEqual=" + DEFAULT_LAST_MODIFIED);

        // Get all the classHomeWorkList where lastModified is less than or equal to SMALLER_LAST_MODIFIED
        defaultClassHomeWorkShouldNotBeFound("lastModified.lessThanOrEqual=" + SMALLER_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllClassHomeWorksByLastModifiedIsLessThanSomething() throws Exception {
        // Initialize the database
        classHomeWorkRepository.saveAndFlush(classHomeWork);

        // Get all the classHomeWorkList where lastModified is less than DEFAULT_LAST_MODIFIED
        defaultClassHomeWorkShouldNotBeFound("lastModified.lessThan=" + DEFAULT_LAST_MODIFIED);

        // Get all the classHomeWorkList where lastModified is less than UPDATED_LAST_MODIFIED
        defaultClassHomeWorkShouldBeFound("lastModified.lessThan=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllClassHomeWorksByLastModifiedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        classHomeWorkRepository.saveAndFlush(classHomeWork);

        // Get all the classHomeWorkList where lastModified is greater than DEFAULT_LAST_MODIFIED
        defaultClassHomeWorkShouldNotBeFound("lastModified.greaterThan=" + DEFAULT_LAST_MODIFIED);

        // Get all the classHomeWorkList where lastModified is greater than SMALLER_LAST_MODIFIED
        defaultClassHomeWorkShouldBeFound("lastModified.greaterThan=" + SMALLER_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllClassHomeWorksByCancelDateIsEqualToSomething() throws Exception {
        // Initialize the database
        classHomeWorkRepository.saveAndFlush(classHomeWork);

        // Get all the classHomeWorkList where cancelDate equals to DEFAULT_CANCEL_DATE
        defaultClassHomeWorkShouldBeFound("cancelDate.equals=" + DEFAULT_CANCEL_DATE);

        // Get all the classHomeWorkList where cancelDate equals to UPDATED_CANCEL_DATE
        defaultClassHomeWorkShouldNotBeFound("cancelDate.equals=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllClassHomeWorksByCancelDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classHomeWorkRepository.saveAndFlush(classHomeWork);

        // Get all the classHomeWorkList where cancelDate not equals to DEFAULT_CANCEL_DATE
        defaultClassHomeWorkShouldNotBeFound("cancelDate.notEquals=" + DEFAULT_CANCEL_DATE);

        // Get all the classHomeWorkList where cancelDate not equals to UPDATED_CANCEL_DATE
        defaultClassHomeWorkShouldBeFound("cancelDate.notEquals=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllClassHomeWorksByCancelDateIsInShouldWork() throws Exception {
        // Initialize the database
        classHomeWorkRepository.saveAndFlush(classHomeWork);

        // Get all the classHomeWorkList where cancelDate in DEFAULT_CANCEL_DATE or UPDATED_CANCEL_DATE
        defaultClassHomeWorkShouldBeFound("cancelDate.in=" + DEFAULT_CANCEL_DATE + "," + UPDATED_CANCEL_DATE);

        // Get all the classHomeWorkList where cancelDate equals to UPDATED_CANCEL_DATE
        defaultClassHomeWorkShouldNotBeFound("cancelDate.in=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllClassHomeWorksByCancelDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        classHomeWorkRepository.saveAndFlush(classHomeWork);

        // Get all the classHomeWorkList where cancelDate is not null
        defaultClassHomeWorkShouldBeFound("cancelDate.specified=true");

        // Get all the classHomeWorkList where cancelDate is null
        defaultClassHomeWorkShouldNotBeFound("cancelDate.specified=false");
    }

    @Test
    @Transactional
    void getAllClassHomeWorksByCancelDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        classHomeWorkRepository.saveAndFlush(classHomeWork);

        // Get all the classHomeWorkList where cancelDate is greater than or equal to DEFAULT_CANCEL_DATE
        defaultClassHomeWorkShouldBeFound("cancelDate.greaterThanOrEqual=" + DEFAULT_CANCEL_DATE);

        // Get all the classHomeWorkList where cancelDate is greater than or equal to UPDATED_CANCEL_DATE
        defaultClassHomeWorkShouldNotBeFound("cancelDate.greaterThanOrEqual=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllClassHomeWorksByCancelDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        classHomeWorkRepository.saveAndFlush(classHomeWork);

        // Get all the classHomeWorkList where cancelDate is less than or equal to DEFAULT_CANCEL_DATE
        defaultClassHomeWorkShouldBeFound("cancelDate.lessThanOrEqual=" + DEFAULT_CANCEL_DATE);

        // Get all the classHomeWorkList where cancelDate is less than or equal to SMALLER_CANCEL_DATE
        defaultClassHomeWorkShouldNotBeFound("cancelDate.lessThanOrEqual=" + SMALLER_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllClassHomeWorksByCancelDateIsLessThanSomething() throws Exception {
        // Initialize the database
        classHomeWorkRepository.saveAndFlush(classHomeWork);

        // Get all the classHomeWorkList where cancelDate is less than DEFAULT_CANCEL_DATE
        defaultClassHomeWorkShouldNotBeFound("cancelDate.lessThan=" + DEFAULT_CANCEL_DATE);

        // Get all the classHomeWorkList where cancelDate is less than UPDATED_CANCEL_DATE
        defaultClassHomeWorkShouldBeFound("cancelDate.lessThan=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllClassHomeWorksByCancelDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        classHomeWorkRepository.saveAndFlush(classHomeWork);

        // Get all the classHomeWorkList where cancelDate is greater than DEFAULT_CANCEL_DATE
        defaultClassHomeWorkShouldNotBeFound("cancelDate.greaterThan=" + DEFAULT_CANCEL_DATE);

        // Get all the classHomeWorkList where cancelDate is greater than SMALLER_CANCEL_DATE
        defaultClassHomeWorkShouldBeFound("cancelDate.greaterThan=" + SMALLER_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllClassHomeWorksByStudentHomeWorkTrackIsEqualToSomething() throws Exception {
        // Initialize the database
        classHomeWorkRepository.saveAndFlush(classHomeWork);
        StudentHomeWorkTrack studentHomeWorkTrack = StudentHomeWorkTrackResourceIT.createEntity(em);
        em.persist(studentHomeWorkTrack);
        em.flush();
        classHomeWork.addStudentHomeWorkTrack(studentHomeWorkTrack);
        classHomeWorkRepository.saveAndFlush(classHomeWork);
        Long studentHomeWorkTrackId = studentHomeWorkTrack.getId();

        // Get all the classHomeWorkList where studentHomeWorkTrack equals to studentHomeWorkTrackId
        defaultClassHomeWorkShouldBeFound("studentHomeWorkTrackId.equals=" + studentHomeWorkTrackId);

        // Get all the classHomeWorkList where studentHomeWorkTrack equals to (studentHomeWorkTrackId + 1)
        defaultClassHomeWorkShouldNotBeFound("studentHomeWorkTrackId.equals=" + (studentHomeWorkTrackId + 1));
    }

    @Test
    @Transactional
    void getAllClassHomeWorksByChapterSectionIsEqualToSomething() throws Exception {
        // Initialize the database
        classHomeWorkRepository.saveAndFlush(classHomeWork);
        ChapterSection chapterSection = ChapterSectionResourceIT.createEntity(em);
        em.persist(chapterSection);
        em.flush();
        classHomeWork.setChapterSection(chapterSection);
        classHomeWorkRepository.saveAndFlush(classHomeWork);
        Long chapterSectionId = chapterSection.getId();

        // Get all the classHomeWorkList where chapterSection equals to chapterSectionId
        defaultClassHomeWorkShouldBeFound("chapterSectionId.equals=" + chapterSectionId);

        // Get all the classHomeWorkList where chapterSection equals to (chapterSectionId + 1)
        defaultClassHomeWorkShouldNotBeFound("chapterSectionId.equals=" + (chapterSectionId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultClassHomeWorkShouldBeFound(String filter) throws Exception {
        restClassHomeWorkMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(classHomeWork.getId().intValue())))
            .andExpect(jsonPath("$.[*].schoolDate").value(hasItem(DEFAULT_SCHOOL_DATE.toString())))
            .andExpect(jsonPath("$.[*].studentAssignmentType").value(hasItem(DEFAULT_STUDENT_ASSIGNMENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].homeWorkText").value(hasItem(DEFAULT_HOME_WORK_TEXT)))
            .andExpect(jsonPath("$.[*].homeWorkFileContentType").value(hasItem(DEFAULT_HOME_WORK_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].homeWorkFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_HOME_WORK_FILE))))
            .andExpect(jsonPath("$.[*].homeWorkFileLink").value(hasItem(DEFAULT_HOME_WORK_FILE_LINK)))
            .andExpect(jsonPath("$.[*].assign").value(hasItem(DEFAULT_ASSIGN.booleanValue())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].cancelDate").value(hasItem(DEFAULT_CANCEL_DATE.toString())));

        // Check, that the count call also returns 1
        restClassHomeWorkMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultClassHomeWorkShouldNotBeFound(String filter) throws Exception {
        restClassHomeWorkMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restClassHomeWorkMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingClassHomeWork() throws Exception {
        // Get the classHomeWork
        restClassHomeWorkMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewClassHomeWork() throws Exception {
        // Initialize the database
        classHomeWorkRepository.saveAndFlush(classHomeWork);

        int databaseSizeBeforeUpdate = classHomeWorkRepository.findAll().size();

        // Update the classHomeWork
        ClassHomeWork updatedClassHomeWork = classHomeWorkRepository.findById(classHomeWork.getId()).get();
        // Disconnect from session so that the updates on updatedClassHomeWork are not directly saved in db
        em.detach(updatedClassHomeWork);
        updatedClassHomeWork
            .schoolDate(UPDATED_SCHOOL_DATE)
            .studentAssignmentType(UPDATED_STUDENT_ASSIGNMENT_TYPE)
            .homeWorkText(UPDATED_HOME_WORK_TEXT)
            .homeWorkFile(UPDATED_HOME_WORK_FILE)
            .homeWorkFileContentType(UPDATED_HOME_WORK_FILE_CONTENT_TYPE)
            .homeWorkFileLink(UPDATED_HOME_WORK_FILE_LINK)
            .assign(UPDATED_ASSIGN)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);
        ClassHomeWorkDTO classHomeWorkDTO = classHomeWorkMapper.toDto(updatedClassHomeWork);

        restClassHomeWorkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, classHomeWorkDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(classHomeWorkDTO))
            )
            .andExpect(status().isOk());

        // Validate the ClassHomeWork in the database
        List<ClassHomeWork> classHomeWorkList = classHomeWorkRepository.findAll();
        assertThat(classHomeWorkList).hasSize(databaseSizeBeforeUpdate);
        ClassHomeWork testClassHomeWork = classHomeWorkList.get(classHomeWorkList.size() - 1);
        assertThat(testClassHomeWork.getSchoolDate()).isEqualTo(UPDATED_SCHOOL_DATE);
        assertThat(testClassHomeWork.getStudentAssignmentType()).isEqualTo(UPDATED_STUDENT_ASSIGNMENT_TYPE);
        assertThat(testClassHomeWork.getHomeWorkText()).isEqualTo(UPDATED_HOME_WORK_TEXT);
        assertThat(testClassHomeWork.getHomeWorkFile()).isEqualTo(UPDATED_HOME_WORK_FILE);
        assertThat(testClassHomeWork.getHomeWorkFileContentType()).isEqualTo(UPDATED_HOME_WORK_FILE_CONTENT_TYPE);
        assertThat(testClassHomeWork.getHomeWorkFileLink()).isEqualTo(UPDATED_HOME_WORK_FILE_LINK);
        assertThat(testClassHomeWork.getAssign()).isEqualTo(UPDATED_ASSIGN);
        assertThat(testClassHomeWork.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testClassHomeWork.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testClassHomeWork.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void putNonExistingClassHomeWork() throws Exception {
        int databaseSizeBeforeUpdate = classHomeWorkRepository.findAll().size();
        classHomeWork.setId(count.incrementAndGet());

        // Create the ClassHomeWork
        ClassHomeWorkDTO classHomeWorkDTO = classHomeWorkMapper.toDto(classHomeWork);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClassHomeWorkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, classHomeWorkDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(classHomeWorkDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClassHomeWork in the database
        List<ClassHomeWork> classHomeWorkList = classHomeWorkRepository.findAll();
        assertThat(classHomeWorkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchClassHomeWork() throws Exception {
        int databaseSizeBeforeUpdate = classHomeWorkRepository.findAll().size();
        classHomeWork.setId(count.incrementAndGet());

        // Create the ClassHomeWork
        ClassHomeWorkDTO classHomeWorkDTO = classHomeWorkMapper.toDto(classHomeWork);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClassHomeWorkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(classHomeWorkDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClassHomeWork in the database
        List<ClassHomeWork> classHomeWorkList = classHomeWorkRepository.findAll();
        assertThat(classHomeWorkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamClassHomeWork() throws Exception {
        int databaseSizeBeforeUpdate = classHomeWorkRepository.findAll().size();
        classHomeWork.setId(count.incrementAndGet());

        // Create the ClassHomeWork
        ClassHomeWorkDTO classHomeWorkDTO = classHomeWorkMapper.toDto(classHomeWork);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClassHomeWorkMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(classHomeWorkDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ClassHomeWork in the database
        List<ClassHomeWork> classHomeWorkList = classHomeWorkRepository.findAll();
        assertThat(classHomeWorkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateClassHomeWorkWithPatch() throws Exception {
        // Initialize the database
        classHomeWorkRepository.saveAndFlush(classHomeWork);

        int databaseSizeBeforeUpdate = classHomeWorkRepository.findAll().size();

        // Update the classHomeWork using partial update
        ClassHomeWork partialUpdatedClassHomeWork = new ClassHomeWork();
        partialUpdatedClassHomeWork.setId(classHomeWork.getId());

        partialUpdatedClassHomeWork
            .schoolDate(UPDATED_SCHOOL_DATE)
            .homeWorkText(UPDATED_HOME_WORK_TEXT)
            .homeWorkFile(UPDATED_HOME_WORK_FILE)
            .homeWorkFileContentType(UPDATED_HOME_WORK_FILE_CONTENT_TYPE)
            .homeWorkFileLink(UPDATED_HOME_WORK_FILE_LINK)
            .assign(UPDATED_ASSIGN)
            .lastModified(UPDATED_LAST_MODIFIED);

        restClassHomeWorkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClassHomeWork.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedClassHomeWork))
            )
            .andExpect(status().isOk());

        // Validate the ClassHomeWork in the database
        List<ClassHomeWork> classHomeWorkList = classHomeWorkRepository.findAll();
        assertThat(classHomeWorkList).hasSize(databaseSizeBeforeUpdate);
        ClassHomeWork testClassHomeWork = classHomeWorkList.get(classHomeWorkList.size() - 1);
        assertThat(testClassHomeWork.getSchoolDate()).isEqualTo(UPDATED_SCHOOL_DATE);
        assertThat(testClassHomeWork.getStudentAssignmentType()).isEqualTo(DEFAULT_STUDENT_ASSIGNMENT_TYPE);
        assertThat(testClassHomeWork.getHomeWorkText()).isEqualTo(UPDATED_HOME_WORK_TEXT);
        assertThat(testClassHomeWork.getHomeWorkFile()).isEqualTo(UPDATED_HOME_WORK_FILE);
        assertThat(testClassHomeWork.getHomeWorkFileContentType()).isEqualTo(UPDATED_HOME_WORK_FILE_CONTENT_TYPE);
        assertThat(testClassHomeWork.getHomeWorkFileLink()).isEqualTo(UPDATED_HOME_WORK_FILE_LINK);
        assertThat(testClassHomeWork.getAssign()).isEqualTo(UPDATED_ASSIGN);
        assertThat(testClassHomeWork.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testClassHomeWork.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testClassHomeWork.getCancelDate()).isEqualTo(DEFAULT_CANCEL_DATE);
    }

    @Test
    @Transactional
    void fullUpdateClassHomeWorkWithPatch() throws Exception {
        // Initialize the database
        classHomeWorkRepository.saveAndFlush(classHomeWork);

        int databaseSizeBeforeUpdate = classHomeWorkRepository.findAll().size();

        // Update the classHomeWork using partial update
        ClassHomeWork partialUpdatedClassHomeWork = new ClassHomeWork();
        partialUpdatedClassHomeWork.setId(classHomeWork.getId());

        partialUpdatedClassHomeWork
            .schoolDate(UPDATED_SCHOOL_DATE)
            .studentAssignmentType(UPDATED_STUDENT_ASSIGNMENT_TYPE)
            .homeWorkText(UPDATED_HOME_WORK_TEXT)
            .homeWorkFile(UPDATED_HOME_WORK_FILE)
            .homeWorkFileContentType(UPDATED_HOME_WORK_FILE_CONTENT_TYPE)
            .homeWorkFileLink(UPDATED_HOME_WORK_FILE_LINK)
            .assign(UPDATED_ASSIGN)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);

        restClassHomeWorkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClassHomeWork.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedClassHomeWork))
            )
            .andExpect(status().isOk());

        // Validate the ClassHomeWork in the database
        List<ClassHomeWork> classHomeWorkList = classHomeWorkRepository.findAll();
        assertThat(classHomeWorkList).hasSize(databaseSizeBeforeUpdate);
        ClassHomeWork testClassHomeWork = classHomeWorkList.get(classHomeWorkList.size() - 1);
        assertThat(testClassHomeWork.getSchoolDate()).isEqualTo(UPDATED_SCHOOL_DATE);
        assertThat(testClassHomeWork.getStudentAssignmentType()).isEqualTo(UPDATED_STUDENT_ASSIGNMENT_TYPE);
        assertThat(testClassHomeWork.getHomeWorkText()).isEqualTo(UPDATED_HOME_WORK_TEXT);
        assertThat(testClassHomeWork.getHomeWorkFile()).isEqualTo(UPDATED_HOME_WORK_FILE);
        assertThat(testClassHomeWork.getHomeWorkFileContentType()).isEqualTo(UPDATED_HOME_WORK_FILE_CONTENT_TYPE);
        assertThat(testClassHomeWork.getHomeWorkFileLink()).isEqualTo(UPDATED_HOME_WORK_FILE_LINK);
        assertThat(testClassHomeWork.getAssign()).isEqualTo(UPDATED_ASSIGN);
        assertThat(testClassHomeWork.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testClassHomeWork.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testClassHomeWork.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingClassHomeWork() throws Exception {
        int databaseSizeBeforeUpdate = classHomeWorkRepository.findAll().size();
        classHomeWork.setId(count.incrementAndGet());

        // Create the ClassHomeWork
        ClassHomeWorkDTO classHomeWorkDTO = classHomeWorkMapper.toDto(classHomeWork);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClassHomeWorkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, classHomeWorkDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(classHomeWorkDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClassHomeWork in the database
        List<ClassHomeWork> classHomeWorkList = classHomeWorkRepository.findAll();
        assertThat(classHomeWorkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchClassHomeWork() throws Exception {
        int databaseSizeBeforeUpdate = classHomeWorkRepository.findAll().size();
        classHomeWork.setId(count.incrementAndGet());

        // Create the ClassHomeWork
        ClassHomeWorkDTO classHomeWorkDTO = classHomeWorkMapper.toDto(classHomeWork);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClassHomeWorkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(classHomeWorkDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClassHomeWork in the database
        List<ClassHomeWork> classHomeWorkList = classHomeWorkRepository.findAll();
        assertThat(classHomeWorkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamClassHomeWork() throws Exception {
        int databaseSizeBeforeUpdate = classHomeWorkRepository.findAll().size();
        classHomeWork.setId(count.incrementAndGet());

        // Create the ClassHomeWork
        ClassHomeWorkDTO classHomeWorkDTO = classHomeWorkMapper.toDto(classHomeWork);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClassHomeWorkMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(classHomeWorkDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ClassHomeWork in the database
        List<ClassHomeWork> classHomeWorkList = classHomeWorkRepository.findAll();
        assertThat(classHomeWorkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteClassHomeWork() throws Exception {
        // Initialize the database
        classHomeWorkRepository.saveAndFlush(classHomeWork);

        int databaseSizeBeforeDelete = classHomeWorkRepository.findAll().size();

        // Delete the classHomeWork
        restClassHomeWorkMockMvc
            .perform(delete(ENTITY_API_URL_ID, classHomeWork.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ClassHomeWork> classHomeWorkList = classHomeWorkRepository.findAll();
        assertThat(classHomeWorkList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
