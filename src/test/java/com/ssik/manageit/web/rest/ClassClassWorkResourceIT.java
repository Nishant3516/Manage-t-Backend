package com.ssik.manageit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ssik.manageit.IntegrationTest;
import com.ssik.manageit.domain.ChapterSection;
import com.ssik.manageit.domain.ClassClassWork;
import com.ssik.manageit.domain.StudentClassWorkTrack;
import com.ssik.manageit.domain.enumeration.StudentAssignmentType;
import com.ssik.manageit.repository.ClassClassWorkRepository;
import com.ssik.manageit.service.criteria.ClassClassWorkCriteria;
import com.ssik.manageit.service.dto.ClassClassWorkDTO;
import com.ssik.manageit.service.mapper.ClassClassWorkMapper;
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
 * Integration tests for the {@link ClassClassWorkResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ClassClassWorkResourceIT {

    private static final LocalDate DEFAULT_SCHOOL_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_SCHOOL_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_SCHOOL_DATE = LocalDate.ofEpochDay(-1L);

    private static final StudentAssignmentType DEFAULT_STUDENT_ASSIGNMENT_TYPE = StudentAssignmentType.READING_WRITING;
    private static final StudentAssignmentType UPDATED_STUDENT_ASSIGNMENT_TYPE = StudentAssignmentType.PROJECT;

    private static final String DEFAULT_CLASS_WORK_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_CLASS_WORK_TEXT = "BBBBBBBBBB";

    private static final byte[] DEFAULT_CLASS_WORK_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_CLASS_WORK_FILE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_CLASS_WORK_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_CLASS_WORK_FILE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_CLASS_WORK_FILE_LINK = "AAAAAAAAAA";
    private static final String UPDATED_CLASS_WORK_FILE_LINK = "BBBBBBBBBB";

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

    private static final String ENTITY_API_URL = "/api/class-class-works";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ClassClassWorkRepository classClassWorkRepository;

    @Autowired
    private ClassClassWorkMapper classClassWorkMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restClassClassWorkMockMvc;

    private ClassClassWork classClassWork;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClassClassWork createEntity(EntityManager em) {
        ClassClassWork classClassWork = new ClassClassWork()
            .schoolDate(DEFAULT_SCHOOL_DATE)
            .studentAssignmentType(DEFAULT_STUDENT_ASSIGNMENT_TYPE)
            .classWorkText(DEFAULT_CLASS_WORK_TEXT)
            .classWorkFile(DEFAULT_CLASS_WORK_FILE)
            .classWorkFileContentType(DEFAULT_CLASS_WORK_FILE_CONTENT_TYPE)
            .classWorkFileLink(DEFAULT_CLASS_WORK_FILE_LINK)
            .assign(DEFAULT_ASSIGN)
            .createDate(DEFAULT_CREATE_DATE)
            .lastModified(DEFAULT_LAST_MODIFIED)
            .cancelDate(DEFAULT_CANCEL_DATE);
        return classClassWork;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClassClassWork createUpdatedEntity(EntityManager em) {
        ClassClassWork classClassWork = new ClassClassWork()
            .schoolDate(UPDATED_SCHOOL_DATE)
            .studentAssignmentType(UPDATED_STUDENT_ASSIGNMENT_TYPE)
            .classWorkText(UPDATED_CLASS_WORK_TEXT)
            .classWorkFile(UPDATED_CLASS_WORK_FILE)
            .classWorkFileContentType(UPDATED_CLASS_WORK_FILE_CONTENT_TYPE)
            .classWorkFileLink(UPDATED_CLASS_WORK_FILE_LINK)
            .assign(UPDATED_ASSIGN)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);
        return classClassWork;
    }

    @BeforeEach
    public void initTest() {
        classClassWork = createEntity(em);
    }

    @Test
    @Transactional
    void createClassClassWork() throws Exception {
        int databaseSizeBeforeCreate = classClassWorkRepository.findAll().size();
        // Create the ClassClassWork
        ClassClassWorkDTO classClassWorkDTO = classClassWorkMapper.toDto(classClassWork);
        restClassClassWorkMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(classClassWorkDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ClassClassWork in the database
        List<ClassClassWork> classClassWorkList = classClassWorkRepository.findAll();
        assertThat(classClassWorkList).hasSize(databaseSizeBeforeCreate + 1);
        ClassClassWork testClassClassWork = classClassWorkList.get(classClassWorkList.size() - 1);
        assertThat(testClassClassWork.getSchoolDate()).isEqualTo(DEFAULT_SCHOOL_DATE);
        assertThat(testClassClassWork.getStudentAssignmentType()).isEqualTo(DEFAULT_STUDENT_ASSIGNMENT_TYPE);
        assertThat(testClassClassWork.getClassWorkText()).isEqualTo(DEFAULT_CLASS_WORK_TEXT);
        assertThat(testClassClassWork.getClassWorkFile()).isEqualTo(DEFAULT_CLASS_WORK_FILE);
        assertThat(testClassClassWork.getClassWorkFileContentType()).isEqualTo(DEFAULT_CLASS_WORK_FILE_CONTENT_TYPE);
        assertThat(testClassClassWork.getClassWorkFileLink()).isEqualTo(DEFAULT_CLASS_WORK_FILE_LINK);
        assertThat(testClassClassWork.getAssign()).isEqualTo(DEFAULT_ASSIGN);
        assertThat(testClassClassWork.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testClassClassWork.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
        assertThat(testClassClassWork.getCancelDate()).isEqualTo(DEFAULT_CANCEL_DATE);
    }

    @Test
    @Transactional
    void createClassClassWorkWithExistingId() throws Exception {
        // Create the ClassClassWork with an existing ID
        classClassWork.setId(1L);
        ClassClassWorkDTO classClassWorkDTO = classClassWorkMapper.toDto(classClassWork);

        int databaseSizeBeforeCreate = classClassWorkRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restClassClassWorkMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(classClassWorkDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClassClassWork in the database
        List<ClassClassWork> classClassWorkList = classClassWorkRepository.findAll();
        assertThat(classClassWorkList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSchoolDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = classClassWorkRepository.findAll().size();
        // set the field null
        classClassWork.setSchoolDate(null);

        // Create the ClassClassWork, which fails.
        ClassClassWorkDTO classClassWorkDTO = classClassWorkMapper.toDto(classClassWork);

        restClassClassWorkMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(classClassWorkDTO))
            )
            .andExpect(status().isBadRequest());

        List<ClassClassWork> classClassWorkList = classClassWorkRepository.findAll();
        assertThat(classClassWorkList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStudentAssignmentTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = classClassWorkRepository.findAll().size();
        // set the field null
        classClassWork.setStudentAssignmentType(null);

        // Create the ClassClassWork, which fails.
        ClassClassWorkDTO classClassWorkDTO = classClassWorkMapper.toDto(classClassWork);

        restClassClassWorkMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(classClassWorkDTO))
            )
            .andExpect(status().isBadRequest());

        List<ClassClassWork> classClassWorkList = classClassWorkRepository.findAll();
        assertThat(classClassWorkList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkClassWorkTextIsRequired() throws Exception {
        int databaseSizeBeforeTest = classClassWorkRepository.findAll().size();
        // set the field null
        classClassWork.setClassWorkText(null);

        // Create the ClassClassWork, which fails.
        ClassClassWorkDTO classClassWorkDTO = classClassWorkMapper.toDto(classClassWork);

        restClassClassWorkMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(classClassWorkDTO))
            )
            .andExpect(status().isBadRequest());

        List<ClassClassWork> classClassWorkList = classClassWorkRepository.findAll();
        assertThat(classClassWorkList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllClassClassWorks() throws Exception {
        // Initialize the database
        classClassWorkRepository.saveAndFlush(classClassWork);

        // Get all the classClassWorkList
        restClassClassWorkMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(classClassWork.getId().intValue())))
            .andExpect(jsonPath("$.[*].schoolDate").value(hasItem(DEFAULT_SCHOOL_DATE.toString())))
            .andExpect(jsonPath("$.[*].studentAssignmentType").value(hasItem(DEFAULT_STUDENT_ASSIGNMENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].classWorkText").value(hasItem(DEFAULT_CLASS_WORK_TEXT)))
            .andExpect(jsonPath("$.[*].classWorkFileContentType").value(hasItem(DEFAULT_CLASS_WORK_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].classWorkFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_CLASS_WORK_FILE))))
            .andExpect(jsonPath("$.[*].classWorkFileLink").value(hasItem(DEFAULT_CLASS_WORK_FILE_LINK)))
            .andExpect(jsonPath("$.[*].assign").value(hasItem(DEFAULT_ASSIGN.booleanValue())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].cancelDate").value(hasItem(DEFAULT_CANCEL_DATE.toString())));
    }

    @Test
    @Transactional
    void getClassClassWork() throws Exception {
        // Initialize the database
        classClassWorkRepository.saveAndFlush(classClassWork);

        // Get the classClassWork
        restClassClassWorkMockMvc
            .perform(get(ENTITY_API_URL_ID, classClassWork.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(classClassWork.getId().intValue()))
            .andExpect(jsonPath("$.schoolDate").value(DEFAULT_SCHOOL_DATE.toString()))
            .andExpect(jsonPath("$.studentAssignmentType").value(DEFAULT_STUDENT_ASSIGNMENT_TYPE.toString()))
            .andExpect(jsonPath("$.classWorkText").value(DEFAULT_CLASS_WORK_TEXT))
            .andExpect(jsonPath("$.classWorkFileContentType").value(DEFAULT_CLASS_WORK_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.classWorkFile").value(Base64Utils.encodeToString(DEFAULT_CLASS_WORK_FILE)))
            .andExpect(jsonPath("$.classWorkFileLink").value(DEFAULT_CLASS_WORK_FILE_LINK))
            .andExpect(jsonPath("$.assign").value(DEFAULT_ASSIGN.booleanValue()))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.lastModified").value(DEFAULT_LAST_MODIFIED.toString()))
            .andExpect(jsonPath("$.cancelDate").value(DEFAULT_CANCEL_DATE.toString()));
    }

    @Test
    @Transactional
    void getClassClassWorksByIdFiltering() throws Exception {
        // Initialize the database
        classClassWorkRepository.saveAndFlush(classClassWork);

        Long id = classClassWork.getId();

        defaultClassClassWorkShouldBeFound("id.equals=" + id);
        defaultClassClassWorkShouldNotBeFound("id.notEquals=" + id);

        defaultClassClassWorkShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultClassClassWorkShouldNotBeFound("id.greaterThan=" + id);

        defaultClassClassWorkShouldBeFound("id.lessThanOrEqual=" + id);
        defaultClassClassWorkShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllClassClassWorksBySchoolDateIsEqualToSomething() throws Exception {
        // Initialize the database
        classClassWorkRepository.saveAndFlush(classClassWork);

        // Get all the classClassWorkList where schoolDate equals to DEFAULT_SCHOOL_DATE
        defaultClassClassWorkShouldBeFound("schoolDate.equals=" + DEFAULT_SCHOOL_DATE);

        // Get all the classClassWorkList where schoolDate equals to UPDATED_SCHOOL_DATE
        defaultClassClassWorkShouldNotBeFound("schoolDate.equals=" + UPDATED_SCHOOL_DATE);
    }

    @Test
    @Transactional
    void getAllClassClassWorksBySchoolDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classClassWorkRepository.saveAndFlush(classClassWork);

        // Get all the classClassWorkList where schoolDate not equals to DEFAULT_SCHOOL_DATE
        defaultClassClassWorkShouldNotBeFound("schoolDate.notEquals=" + DEFAULT_SCHOOL_DATE);

        // Get all the classClassWorkList where schoolDate not equals to UPDATED_SCHOOL_DATE
        defaultClassClassWorkShouldBeFound("schoolDate.notEquals=" + UPDATED_SCHOOL_DATE);
    }

    @Test
    @Transactional
    void getAllClassClassWorksBySchoolDateIsInShouldWork() throws Exception {
        // Initialize the database
        classClassWorkRepository.saveAndFlush(classClassWork);

        // Get all the classClassWorkList where schoolDate in DEFAULT_SCHOOL_DATE or UPDATED_SCHOOL_DATE
        defaultClassClassWorkShouldBeFound("schoolDate.in=" + DEFAULT_SCHOOL_DATE + "," + UPDATED_SCHOOL_DATE);

        // Get all the classClassWorkList where schoolDate equals to UPDATED_SCHOOL_DATE
        defaultClassClassWorkShouldNotBeFound("schoolDate.in=" + UPDATED_SCHOOL_DATE);
    }

    @Test
    @Transactional
    void getAllClassClassWorksBySchoolDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        classClassWorkRepository.saveAndFlush(classClassWork);

        // Get all the classClassWorkList where schoolDate is not null
        defaultClassClassWorkShouldBeFound("schoolDate.specified=true");

        // Get all the classClassWorkList where schoolDate is null
        defaultClassClassWorkShouldNotBeFound("schoolDate.specified=false");
    }

    @Test
    @Transactional
    void getAllClassClassWorksBySchoolDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        classClassWorkRepository.saveAndFlush(classClassWork);

        // Get all the classClassWorkList where schoolDate is greater than or equal to DEFAULT_SCHOOL_DATE
        defaultClassClassWorkShouldBeFound("schoolDate.greaterThanOrEqual=" + DEFAULT_SCHOOL_DATE);

        // Get all the classClassWorkList where schoolDate is greater than or equal to UPDATED_SCHOOL_DATE
        defaultClassClassWorkShouldNotBeFound("schoolDate.greaterThanOrEqual=" + UPDATED_SCHOOL_DATE);
    }

    @Test
    @Transactional
    void getAllClassClassWorksBySchoolDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        classClassWorkRepository.saveAndFlush(classClassWork);

        // Get all the classClassWorkList where schoolDate is less than or equal to DEFAULT_SCHOOL_DATE
        defaultClassClassWorkShouldBeFound("schoolDate.lessThanOrEqual=" + DEFAULT_SCHOOL_DATE);

        // Get all the classClassWorkList where schoolDate is less than or equal to SMALLER_SCHOOL_DATE
        defaultClassClassWorkShouldNotBeFound("schoolDate.lessThanOrEqual=" + SMALLER_SCHOOL_DATE);
    }

    @Test
    @Transactional
    void getAllClassClassWorksBySchoolDateIsLessThanSomething() throws Exception {
        // Initialize the database
        classClassWorkRepository.saveAndFlush(classClassWork);

        // Get all the classClassWorkList where schoolDate is less than DEFAULT_SCHOOL_DATE
        defaultClassClassWorkShouldNotBeFound("schoolDate.lessThan=" + DEFAULT_SCHOOL_DATE);

        // Get all the classClassWorkList where schoolDate is less than UPDATED_SCHOOL_DATE
        defaultClassClassWorkShouldBeFound("schoolDate.lessThan=" + UPDATED_SCHOOL_DATE);
    }

    @Test
    @Transactional
    void getAllClassClassWorksBySchoolDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        classClassWorkRepository.saveAndFlush(classClassWork);

        // Get all the classClassWorkList where schoolDate is greater than DEFAULT_SCHOOL_DATE
        defaultClassClassWorkShouldNotBeFound("schoolDate.greaterThan=" + DEFAULT_SCHOOL_DATE);

        // Get all the classClassWorkList where schoolDate is greater than SMALLER_SCHOOL_DATE
        defaultClassClassWorkShouldBeFound("schoolDate.greaterThan=" + SMALLER_SCHOOL_DATE);
    }

    @Test
    @Transactional
    void getAllClassClassWorksByStudentAssignmentTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        classClassWorkRepository.saveAndFlush(classClassWork);

        // Get all the classClassWorkList where studentAssignmentType equals to DEFAULT_STUDENT_ASSIGNMENT_TYPE
        defaultClassClassWorkShouldBeFound("studentAssignmentType.equals=" + DEFAULT_STUDENT_ASSIGNMENT_TYPE);

        // Get all the classClassWorkList where studentAssignmentType equals to UPDATED_STUDENT_ASSIGNMENT_TYPE
        defaultClassClassWorkShouldNotBeFound("studentAssignmentType.equals=" + UPDATED_STUDENT_ASSIGNMENT_TYPE);
    }

    @Test
    @Transactional
    void getAllClassClassWorksByStudentAssignmentTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classClassWorkRepository.saveAndFlush(classClassWork);

        // Get all the classClassWorkList where studentAssignmentType not equals to DEFAULT_STUDENT_ASSIGNMENT_TYPE
        defaultClassClassWorkShouldNotBeFound("studentAssignmentType.notEquals=" + DEFAULT_STUDENT_ASSIGNMENT_TYPE);

        // Get all the classClassWorkList where studentAssignmentType not equals to UPDATED_STUDENT_ASSIGNMENT_TYPE
        defaultClassClassWorkShouldBeFound("studentAssignmentType.notEquals=" + UPDATED_STUDENT_ASSIGNMENT_TYPE);
    }

    @Test
    @Transactional
    void getAllClassClassWorksByStudentAssignmentTypeIsInShouldWork() throws Exception {
        // Initialize the database
        classClassWorkRepository.saveAndFlush(classClassWork);

        // Get all the classClassWorkList where studentAssignmentType in DEFAULT_STUDENT_ASSIGNMENT_TYPE or UPDATED_STUDENT_ASSIGNMENT_TYPE
        defaultClassClassWorkShouldBeFound(
            "studentAssignmentType.in=" + DEFAULT_STUDENT_ASSIGNMENT_TYPE + "," + UPDATED_STUDENT_ASSIGNMENT_TYPE
        );

        // Get all the classClassWorkList where studentAssignmentType equals to UPDATED_STUDENT_ASSIGNMENT_TYPE
        defaultClassClassWorkShouldNotBeFound("studentAssignmentType.in=" + UPDATED_STUDENT_ASSIGNMENT_TYPE);
    }

    @Test
    @Transactional
    void getAllClassClassWorksByStudentAssignmentTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        classClassWorkRepository.saveAndFlush(classClassWork);

        // Get all the classClassWorkList where studentAssignmentType is not null
        defaultClassClassWorkShouldBeFound("studentAssignmentType.specified=true");

        // Get all the classClassWorkList where studentAssignmentType is null
        defaultClassClassWorkShouldNotBeFound("studentAssignmentType.specified=false");
    }

    @Test
    @Transactional
    void getAllClassClassWorksByClassWorkTextIsEqualToSomething() throws Exception {
        // Initialize the database
        classClassWorkRepository.saveAndFlush(classClassWork);

        // Get all the classClassWorkList where classWorkText equals to DEFAULT_CLASS_WORK_TEXT
        defaultClassClassWorkShouldBeFound("classWorkText.equals=" + DEFAULT_CLASS_WORK_TEXT);

        // Get all the classClassWorkList where classWorkText equals to UPDATED_CLASS_WORK_TEXT
        defaultClassClassWorkShouldNotBeFound("classWorkText.equals=" + UPDATED_CLASS_WORK_TEXT);
    }

    @Test
    @Transactional
    void getAllClassClassWorksByClassWorkTextIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classClassWorkRepository.saveAndFlush(classClassWork);

        // Get all the classClassWorkList where classWorkText not equals to DEFAULT_CLASS_WORK_TEXT
        defaultClassClassWorkShouldNotBeFound("classWorkText.notEquals=" + DEFAULT_CLASS_WORK_TEXT);

        // Get all the classClassWorkList where classWorkText not equals to UPDATED_CLASS_WORK_TEXT
        defaultClassClassWorkShouldBeFound("classWorkText.notEquals=" + UPDATED_CLASS_WORK_TEXT);
    }

    @Test
    @Transactional
    void getAllClassClassWorksByClassWorkTextIsInShouldWork() throws Exception {
        // Initialize the database
        classClassWorkRepository.saveAndFlush(classClassWork);

        // Get all the classClassWorkList where classWorkText in DEFAULT_CLASS_WORK_TEXT or UPDATED_CLASS_WORK_TEXT
        defaultClassClassWorkShouldBeFound("classWorkText.in=" + DEFAULT_CLASS_WORK_TEXT + "," + UPDATED_CLASS_WORK_TEXT);

        // Get all the classClassWorkList where classWorkText equals to UPDATED_CLASS_WORK_TEXT
        defaultClassClassWorkShouldNotBeFound("classWorkText.in=" + UPDATED_CLASS_WORK_TEXT);
    }

    @Test
    @Transactional
    void getAllClassClassWorksByClassWorkTextIsNullOrNotNull() throws Exception {
        // Initialize the database
        classClassWorkRepository.saveAndFlush(classClassWork);

        // Get all the classClassWorkList where classWorkText is not null
        defaultClassClassWorkShouldBeFound("classWorkText.specified=true");

        // Get all the classClassWorkList where classWorkText is null
        defaultClassClassWorkShouldNotBeFound("classWorkText.specified=false");
    }

    @Test
    @Transactional
    void getAllClassClassWorksByClassWorkTextContainsSomething() throws Exception {
        // Initialize the database
        classClassWorkRepository.saveAndFlush(classClassWork);

        // Get all the classClassWorkList where classWorkText contains DEFAULT_CLASS_WORK_TEXT
        defaultClassClassWorkShouldBeFound("classWorkText.contains=" + DEFAULT_CLASS_WORK_TEXT);

        // Get all the classClassWorkList where classWorkText contains UPDATED_CLASS_WORK_TEXT
        defaultClassClassWorkShouldNotBeFound("classWorkText.contains=" + UPDATED_CLASS_WORK_TEXT);
    }

    @Test
    @Transactional
    void getAllClassClassWorksByClassWorkTextNotContainsSomething() throws Exception {
        // Initialize the database
        classClassWorkRepository.saveAndFlush(classClassWork);

        // Get all the classClassWorkList where classWorkText does not contain DEFAULT_CLASS_WORK_TEXT
        defaultClassClassWorkShouldNotBeFound("classWorkText.doesNotContain=" + DEFAULT_CLASS_WORK_TEXT);

        // Get all the classClassWorkList where classWorkText does not contain UPDATED_CLASS_WORK_TEXT
        defaultClassClassWorkShouldBeFound("classWorkText.doesNotContain=" + UPDATED_CLASS_WORK_TEXT);
    }

    @Test
    @Transactional
    void getAllClassClassWorksByClassWorkFileLinkIsEqualToSomething() throws Exception {
        // Initialize the database
        classClassWorkRepository.saveAndFlush(classClassWork);

        // Get all the classClassWorkList where classWorkFileLink equals to DEFAULT_CLASS_WORK_FILE_LINK
        defaultClassClassWorkShouldBeFound("classWorkFileLink.equals=" + DEFAULT_CLASS_WORK_FILE_LINK);

        // Get all the classClassWorkList where classWorkFileLink equals to UPDATED_CLASS_WORK_FILE_LINK
        defaultClassClassWorkShouldNotBeFound("classWorkFileLink.equals=" + UPDATED_CLASS_WORK_FILE_LINK);
    }

    @Test
    @Transactional
    void getAllClassClassWorksByClassWorkFileLinkIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classClassWorkRepository.saveAndFlush(classClassWork);

        // Get all the classClassWorkList where classWorkFileLink not equals to DEFAULT_CLASS_WORK_FILE_LINK
        defaultClassClassWorkShouldNotBeFound("classWorkFileLink.notEquals=" + DEFAULT_CLASS_WORK_FILE_LINK);

        // Get all the classClassWorkList where classWorkFileLink not equals to UPDATED_CLASS_WORK_FILE_LINK
        defaultClassClassWorkShouldBeFound("classWorkFileLink.notEquals=" + UPDATED_CLASS_WORK_FILE_LINK);
    }

    @Test
    @Transactional
    void getAllClassClassWorksByClassWorkFileLinkIsInShouldWork() throws Exception {
        // Initialize the database
        classClassWorkRepository.saveAndFlush(classClassWork);

        // Get all the classClassWorkList where classWorkFileLink in DEFAULT_CLASS_WORK_FILE_LINK or UPDATED_CLASS_WORK_FILE_LINK
        defaultClassClassWorkShouldBeFound("classWorkFileLink.in=" + DEFAULT_CLASS_WORK_FILE_LINK + "," + UPDATED_CLASS_WORK_FILE_LINK);

        // Get all the classClassWorkList where classWorkFileLink equals to UPDATED_CLASS_WORK_FILE_LINK
        defaultClassClassWorkShouldNotBeFound("classWorkFileLink.in=" + UPDATED_CLASS_WORK_FILE_LINK);
    }

    @Test
    @Transactional
    void getAllClassClassWorksByClassWorkFileLinkIsNullOrNotNull() throws Exception {
        // Initialize the database
        classClassWorkRepository.saveAndFlush(classClassWork);

        // Get all the classClassWorkList where classWorkFileLink is not null
        defaultClassClassWorkShouldBeFound("classWorkFileLink.specified=true");

        // Get all the classClassWorkList where classWorkFileLink is null
        defaultClassClassWorkShouldNotBeFound("classWorkFileLink.specified=false");
    }

    @Test
    @Transactional
    void getAllClassClassWorksByClassWorkFileLinkContainsSomething() throws Exception {
        // Initialize the database
        classClassWorkRepository.saveAndFlush(classClassWork);

        // Get all the classClassWorkList where classWorkFileLink contains DEFAULT_CLASS_WORK_FILE_LINK
        defaultClassClassWorkShouldBeFound("classWorkFileLink.contains=" + DEFAULT_CLASS_WORK_FILE_LINK);

        // Get all the classClassWorkList where classWorkFileLink contains UPDATED_CLASS_WORK_FILE_LINK
        defaultClassClassWorkShouldNotBeFound("classWorkFileLink.contains=" + UPDATED_CLASS_WORK_FILE_LINK);
    }

    @Test
    @Transactional
    void getAllClassClassWorksByClassWorkFileLinkNotContainsSomething() throws Exception {
        // Initialize the database
        classClassWorkRepository.saveAndFlush(classClassWork);

        // Get all the classClassWorkList where classWorkFileLink does not contain DEFAULT_CLASS_WORK_FILE_LINK
        defaultClassClassWorkShouldNotBeFound("classWorkFileLink.doesNotContain=" + DEFAULT_CLASS_WORK_FILE_LINK);

        // Get all the classClassWorkList where classWorkFileLink does not contain UPDATED_CLASS_WORK_FILE_LINK
        defaultClassClassWorkShouldBeFound("classWorkFileLink.doesNotContain=" + UPDATED_CLASS_WORK_FILE_LINK);
    }

    @Test
    @Transactional
    void getAllClassClassWorksByAssignIsEqualToSomething() throws Exception {
        // Initialize the database
        classClassWorkRepository.saveAndFlush(classClassWork);

        // Get all the classClassWorkList where assign equals to DEFAULT_ASSIGN
        defaultClassClassWorkShouldBeFound("assign.equals=" + DEFAULT_ASSIGN);

        // Get all the classClassWorkList where assign equals to UPDATED_ASSIGN
        defaultClassClassWorkShouldNotBeFound("assign.equals=" + UPDATED_ASSIGN);
    }

    @Test
    @Transactional
    void getAllClassClassWorksByAssignIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classClassWorkRepository.saveAndFlush(classClassWork);

        // Get all the classClassWorkList where assign not equals to DEFAULT_ASSIGN
        defaultClassClassWorkShouldNotBeFound("assign.notEquals=" + DEFAULT_ASSIGN);

        // Get all the classClassWorkList where assign not equals to UPDATED_ASSIGN
        defaultClassClassWorkShouldBeFound("assign.notEquals=" + UPDATED_ASSIGN);
    }

    @Test
    @Transactional
    void getAllClassClassWorksByAssignIsInShouldWork() throws Exception {
        // Initialize the database
        classClassWorkRepository.saveAndFlush(classClassWork);

        // Get all the classClassWorkList where assign in DEFAULT_ASSIGN or UPDATED_ASSIGN
        defaultClassClassWorkShouldBeFound("assign.in=" + DEFAULT_ASSIGN + "," + UPDATED_ASSIGN);

        // Get all the classClassWorkList where assign equals to UPDATED_ASSIGN
        defaultClassClassWorkShouldNotBeFound("assign.in=" + UPDATED_ASSIGN);
    }

    @Test
    @Transactional
    void getAllClassClassWorksByAssignIsNullOrNotNull() throws Exception {
        // Initialize the database
        classClassWorkRepository.saveAndFlush(classClassWork);

        // Get all the classClassWorkList where assign is not null
        defaultClassClassWorkShouldBeFound("assign.specified=true");

        // Get all the classClassWorkList where assign is null
        defaultClassClassWorkShouldNotBeFound("assign.specified=false");
    }

    @Test
    @Transactional
    void getAllClassClassWorksByCreateDateIsEqualToSomething() throws Exception {
        // Initialize the database
        classClassWorkRepository.saveAndFlush(classClassWork);

        // Get all the classClassWorkList where createDate equals to DEFAULT_CREATE_DATE
        defaultClassClassWorkShouldBeFound("createDate.equals=" + DEFAULT_CREATE_DATE);

        // Get all the classClassWorkList where createDate equals to UPDATED_CREATE_DATE
        defaultClassClassWorkShouldNotBeFound("createDate.equals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllClassClassWorksByCreateDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classClassWorkRepository.saveAndFlush(classClassWork);

        // Get all the classClassWorkList where createDate not equals to DEFAULT_CREATE_DATE
        defaultClassClassWorkShouldNotBeFound("createDate.notEquals=" + DEFAULT_CREATE_DATE);

        // Get all the classClassWorkList where createDate not equals to UPDATED_CREATE_DATE
        defaultClassClassWorkShouldBeFound("createDate.notEquals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllClassClassWorksByCreateDateIsInShouldWork() throws Exception {
        // Initialize the database
        classClassWorkRepository.saveAndFlush(classClassWork);

        // Get all the classClassWorkList where createDate in DEFAULT_CREATE_DATE or UPDATED_CREATE_DATE
        defaultClassClassWorkShouldBeFound("createDate.in=" + DEFAULT_CREATE_DATE + "," + UPDATED_CREATE_DATE);

        // Get all the classClassWorkList where createDate equals to UPDATED_CREATE_DATE
        defaultClassClassWorkShouldNotBeFound("createDate.in=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllClassClassWorksByCreateDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        classClassWorkRepository.saveAndFlush(classClassWork);

        // Get all the classClassWorkList where createDate is not null
        defaultClassClassWorkShouldBeFound("createDate.specified=true");

        // Get all the classClassWorkList where createDate is null
        defaultClassClassWorkShouldNotBeFound("createDate.specified=false");
    }

    @Test
    @Transactional
    void getAllClassClassWorksByCreateDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        classClassWorkRepository.saveAndFlush(classClassWork);

        // Get all the classClassWorkList where createDate is greater than or equal to DEFAULT_CREATE_DATE
        defaultClassClassWorkShouldBeFound("createDate.greaterThanOrEqual=" + DEFAULT_CREATE_DATE);

        // Get all the classClassWorkList where createDate is greater than or equal to UPDATED_CREATE_DATE
        defaultClassClassWorkShouldNotBeFound("createDate.greaterThanOrEqual=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllClassClassWorksByCreateDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        classClassWorkRepository.saveAndFlush(classClassWork);

        // Get all the classClassWorkList where createDate is less than or equal to DEFAULT_CREATE_DATE
        defaultClassClassWorkShouldBeFound("createDate.lessThanOrEqual=" + DEFAULT_CREATE_DATE);

        // Get all the classClassWorkList where createDate is less than or equal to SMALLER_CREATE_DATE
        defaultClassClassWorkShouldNotBeFound("createDate.lessThanOrEqual=" + SMALLER_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllClassClassWorksByCreateDateIsLessThanSomething() throws Exception {
        // Initialize the database
        classClassWorkRepository.saveAndFlush(classClassWork);

        // Get all the classClassWorkList where createDate is less than DEFAULT_CREATE_DATE
        defaultClassClassWorkShouldNotBeFound("createDate.lessThan=" + DEFAULT_CREATE_DATE);

        // Get all the classClassWorkList where createDate is less than UPDATED_CREATE_DATE
        defaultClassClassWorkShouldBeFound("createDate.lessThan=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllClassClassWorksByCreateDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        classClassWorkRepository.saveAndFlush(classClassWork);

        // Get all the classClassWorkList where createDate is greater than DEFAULT_CREATE_DATE
        defaultClassClassWorkShouldNotBeFound("createDate.greaterThan=" + DEFAULT_CREATE_DATE);

        // Get all the classClassWorkList where createDate is greater than SMALLER_CREATE_DATE
        defaultClassClassWorkShouldBeFound("createDate.greaterThan=" + SMALLER_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllClassClassWorksByLastModifiedIsEqualToSomething() throws Exception {
        // Initialize the database
        classClassWorkRepository.saveAndFlush(classClassWork);

        // Get all the classClassWorkList where lastModified equals to DEFAULT_LAST_MODIFIED
        defaultClassClassWorkShouldBeFound("lastModified.equals=" + DEFAULT_LAST_MODIFIED);

        // Get all the classClassWorkList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultClassClassWorkShouldNotBeFound("lastModified.equals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllClassClassWorksByLastModifiedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classClassWorkRepository.saveAndFlush(classClassWork);

        // Get all the classClassWorkList where lastModified not equals to DEFAULT_LAST_MODIFIED
        defaultClassClassWorkShouldNotBeFound("lastModified.notEquals=" + DEFAULT_LAST_MODIFIED);

        // Get all the classClassWorkList where lastModified not equals to UPDATED_LAST_MODIFIED
        defaultClassClassWorkShouldBeFound("lastModified.notEquals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllClassClassWorksByLastModifiedIsInShouldWork() throws Exception {
        // Initialize the database
        classClassWorkRepository.saveAndFlush(classClassWork);

        // Get all the classClassWorkList where lastModified in DEFAULT_LAST_MODIFIED or UPDATED_LAST_MODIFIED
        defaultClassClassWorkShouldBeFound("lastModified.in=" + DEFAULT_LAST_MODIFIED + "," + UPDATED_LAST_MODIFIED);

        // Get all the classClassWorkList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultClassClassWorkShouldNotBeFound("lastModified.in=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllClassClassWorksByLastModifiedIsNullOrNotNull() throws Exception {
        // Initialize the database
        classClassWorkRepository.saveAndFlush(classClassWork);

        // Get all the classClassWorkList where lastModified is not null
        defaultClassClassWorkShouldBeFound("lastModified.specified=true");

        // Get all the classClassWorkList where lastModified is null
        defaultClassClassWorkShouldNotBeFound("lastModified.specified=false");
    }

    @Test
    @Transactional
    void getAllClassClassWorksByLastModifiedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        classClassWorkRepository.saveAndFlush(classClassWork);

        // Get all the classClassWorkList where lastModified is greater than or equal to DEFAULT_LAST_MODIFIED
        defaultClassClassWorkShouldBeFound("lastModified.greaterThanOrEqual=" + DEFAULT_LAST_MODIFIED);

        // Get all the classClassWorkList where lastModified is greater than or equal to UPDATED_LAST_MODIFIED
        defaultClassClassWorkShouldNotBeFound("lastModified.greaterThanOrEqual=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllClassClassWorksByLastModifiedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        classClassWorkRepository.saveAndFlush(classClassWork);

        // Get all the classClassWorkList where lastModified is less than or equal to DEFAULT_LAST_MODIFIED
        defaultClassClassWorkShouldBeFound("lastModified.lessThanOrEqual=" + DEFAULT_LAST_MODIFIED);

        // Get all the classClassWorkList where lastModified is less than or equal to SMALLER_LAST_MODIFIED
        defaultClassClassWorkShouldNotBeFound("lastModified.lessThanOrEqual=" + SMALLER_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllClassClassWorksByLastModifiedIsLessThanSomething() throws Exception {
        // Initialize the database
        classClassWorkRepository.saveAndFlush(classClassWork);

        // Get all the classClassWorkList where lastModified is less than DEFAULT_LAST_MODIFIED
        defaultClassClassWorkShouldNotBeFound("lastModified.lessThan=" + DEFAULT_LAST_MODIFIED);

        // Get all the classClassWorkList where lastModified is less than UPDATED_LAST_MODIFIED
        defaultClassClassWorkShouldBeFound("lastModified.lessThan=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllClassClassWorksByLastModifiedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        classClassWorkRepository.saveAndFlush(classClassWork);

        // Get all the classClassWorkList where lastModified is greater than DEFAULT_LAST_MODIFIED
        defaultClassClassWorkShouldNotBeFound("lastModified.greaterThan=" + DEFAULT_LAST_MODIFIED);

        // Get all the classClassWorkList where lastModified is greater than SMALLER_LAST_MODIFIED
        defaultClassClassWorkShouldBeFound("lastModified.greaterThan=" + SMALLER_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllClassClassWorksByCancelDateIsEqualToSomething() throws Exception {
        // Initialize the database
        classClassWorkRepository.saveAndFlush(classClassWork);

        // Get all the classClassWorkList where cancelDate equals to DEFAULT_CANCEL_DATE
        defaultClassClassWorkShouldBeFound("cancelDate.equals=" + DEFAULT_CANCEL_DATE);

        // Get all the classClassWorkList where cancelDate equals to UPDATED_CANCEL_DATE
        defaultClassClassWorkShouldNotBeFound("cancelDate.equals=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllClassClassWorksByCancelDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classClassWorkRepository.saveAndFlush(classClassWork);

        // Get all the classClassWorkList where cancelDate not equals to DEFAULT_CANCEL_DATE
        defaultClassClassWorkShouldNotBeFound("cancelDate.notEquals=" + DEFAULT_CANCEL_DATE);

        // Get all the classClassWorkList where cancelDate not equals to UPDATED_CANCEL_DATE
        defaultClassClassWorkShouldBeFound("cancelDate.notEquals=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllClassClassWorksByCancelDateIsInShouldWork() throws Exception {
        // Initialize the database
        classClassWorkRepository.saveAndFlush(classClassWork);

        // Get all the classClassWorkList where cancelDate in DEFAULT_CANCEL_DATE or UPDATED_CANCEL_DATE
        defaultClassClassWorkShouldBeFound("cancelDate.in=" + DEFAULT_CANCEL_DATE + "," + UPDATED_CANCEL_DATE);

        // Get all the classClassWorkList where cancelDate equals to UPDATED_CANCEL_DATE
        defaultClassClassWorkShouldNotBeFound("cancelDate.in=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllClassClassWorksByCancelDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        classClassWorkRepository.saveAndFlush(classClassWork);

        // Get all the classClassWorkList where cancelDate is not null
        defaultClassClassWorkShouldBeFound("cancelDate.specified=true");

        // Get all the classClassWorkList where cancelDate is null
        defaultClassClassWorkShouldNotBeFound("cancelDate.specified=false");
    }

    @Test
    @Transactional
    void getAllClassClassWorksByCancelDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        classClassWorkRepository.saveAndFlush(classClassWork);

        // Get all the classClassWorkList where cancelDate is greater than or equal to DEFAULT_CANCEL_DATE
        defaultClassClassWorkShouldBeFound("cancelDate.greaterThanOrEqual=" + DEFAULT_CANCEL_DATE);

        // Get all the classClassWorkList where cancelDate is greater than or equal to UPDATED_CANCEL_DATE
        defaultClassClassWorkShouldNotBeFound("cancelDate.greaterThanOrEqual=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllClassClassWorksByCancelDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        classClassWorkRepository.saveAndFlush(classClassWork);

        // Get all the classClassWorkList where cancelDate is less than or equal to DEFAULT_CANCEL_DATE
        defaultClassClassWorkShouldBeFound("cancelDate.lessThanOrEqual=" + DEFAULT_CANCEL_DATE);

        // Get all the classClassWorkList where cancelDate is less than or equal to SMALLER_CANCEL_DATE
        defaultClassClassWorkShouldNotBeFound("cancelDate.lessThanOrEqual=" + SMALLER_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllClassClassWorksByCancelDateIsLessThanSomething() throws Exception {
        // Initialize the database
        classClassWorkRepository.saveAndFlush(classClassWork);

        // Get all the classClassWorkList where cancelDate is less than DEFAULT_CANCEL_DATE
        defaultClassClassWorkShouldNotBeFound("cancelDate.lessThan=" + DEFAULT_CANCEL_DATE);

        // Get all the classClassWorkList where cancelDate is less than UPDATED_CANCEL_DATE
        defaultClassClassWorkShouldBeFound("cancelDate.lessThan=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllClassClassWorksByCancelDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        classClassWorkRepository.saveAndFlush(classClassWork);

        // Get all the classClassWorkList where cancelDate is greater than DEFAULT_CANCEL_DATE
        defaultClassClassWorkShouldNotBeFound("cancelDate.greaterThan=" + DEFAULT_CANCEL_DATE);

        // Get all the classClassWorkList where cancelDate is greater than SMALLER_CANCEL_DATE
        defaultClassClassWorkShouldBeFound("cancelDate.greaterThan=" + SMALLER_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllClassClassWorksByStudentClassWorkTrackIsEqualToSomething() throws Exception {
        // Initialize the database
        classClassWorkRepository.saveAndFlush(classClassWork);
        StudentClassWorkTrack studentClassWorkTrack = StudentClassWorkTrackResourceIT.createEntity(em);
        em.persist(studentClassWorkTrack);
        em.flush();
        classClassWork.addStudentClassWorkTrack(studentClassWorkTrack);
        classClassWorkRepository.saveAndFlush(classClassWork);
        Long studentClassWorkTrackId = studentClassWorkTrack.getId();

        // Get all the classClassWorkList where studentClassWorkTrack equals to studentClassWorkTrackId
        defaultClassClassWorkShouldBeFound("studentClassWorkTrackId.equals=" + studentClassWorkTrackId);

        // Get all the classClassWorkList where studentClassWorkTrack equals to (studentClassWorkTrackId + 1)
        defaultClassClassWorkShouldNotBeFound("studentClassWorkTrackId.equals=" + (studentClassWorkTrackId + 1));
    }

    @Test
    @Transactional
    void getAllClassClassWorksByChapterSectionIsEqualToSomething() throws Exception {
        // Initialize the database
        classClassWorkRepository.saveAndFlush(classClassWork);
        ChapterSection chapterSection = ChapterSectionResourceIT.createEntity(em);
        em.persist(chapterSection);
        em.flush();
        classClassWork.setChapterSection(chapterSection);
        classClassWorkRepository.saveAndFlush(classClassWork);
        Long chapterSectionId = chapterSection.getId();

        // Get all the classClassWorkList where chapterSection equals to chapterSectionId
        defaultClassClassWorkShouldBeFound("chapterSectionId.equals=" + chapterSectionId);

        // Get all the classClassWorkList where chapterSection equals to (chapterSectionId + 1)
        defaultClassClassWorkShouldNotBeFound("chapterSectionId.equals=" + (chapterSectionId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultClassClassWorkShouldBeFound(String filter) throws Exception {
        restClassClassWorkMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(classClassWork.getId().intValue())))
            .andExpect(jsonPath("$.[*].schoolDate").value(hasItem(DEFAULT_SCHOOL_DATE.toString())))
            .andExpect(jsonPath("$.[*].studentAssignmentType").value(hasItem(DEFAULT_STUDENT_ASSIGNMENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].classWorkText").value(hasItem(DEFAULT_CLASS_WORK_TEXT)))
            .andExpect(jsonPath("$.[*].classWorkFileContentType").value(hasItem(DEFAULT_CLASS_WORK_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].classWorkFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_CLASS_WORK_FILE))))
            .andExpect(jsonPath("$.[*].classWorkFileLink").value(hasItem(DEFAULT_CLASS_WORK_FILE_LINK)))
            .andExpect(jsonPath("$.[*].assign").value(hasItem(DEFAULT_ASSIGN.booleanValue())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].cancelDate").value(hasItem(DEFAULT_CANCEL_DATE.toString())));

        // Check, that the count call also returns 1
        restClassClassWorkMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultClassClassWorkShouldNotBeFound(String filter) throws Exception {
        restClassClassWorkMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restClassClassWorkMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingClassClassWork() throws Exception {
        // Get the classClassWork
        restClassClassWorkMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewClassClassWork() throws Exception {
        // Initialize the database
        classClassWorkRepository.saveAndFlush(classClassWork);

        int databaseSizeBeforeUpdate = classClassWorkRepository.findAll().size();

        // Update the classClassWork
        ClassClassWork updatedClassClassWork = classClassWorkRepository.findById(classClassWork.getId()).get();
        // Disconnect from session so that the updates on updatedClassClassWork are not directly saved in db
        em.detach(updatedClassClassWork);
        updatedClassClassWork
            .schoolDate(UPDATED_SCHOOL_DATE)
            .studentAssignmentType(UPDATED_STUDENT_ASSIGNMENT_TYPE)
            .classWorkText(UPDATED_CLASS_WORK_TEXT)
            .classWorkFile(UPDATED_CLASS_WORK_FILE)
            .classWorkFileContentType(UPDATED_CLASS_WORK_FILE_CONTENT_TYPE)
            .classWorkFileLink(UPDATED_CLASS_WORK_FILE_LINK)
            .assign(UPDATED_ASSIGN)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);
        ClassClassWorkDTO classClassWorkDTO = classClassWorkMapper.toDto(updatedClassClassWork);

        restClassClassWorkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, classClassWorkDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(classClassWorkDTO))
            )
            .andExpect(status().isOk());

        // Validate the ClassClassWork in the database
        List<ClassClassWork> classClassWorkList = classClassWorkRepository.findAll();
        assertThat(classClassWorkList).hasSize(databaseSizeBeforeUpdate);
        ClassClassWork testClassClassWork = classClassWorkList.get(classClassWorkList.size() - 1);
        assertThat(testClassClassWork.getSchoolDate()).isEqualTo(UPDATED_SCHOOL_DATE);
        assertThat(testClassClassWork.getStudentAssignmentType()).isEqualTo(UPDATED_STUDENT_ASSIGNMENT_TYPE);
        assertThat(testClassClassWork.getClassWorkText()).isEqualTo(UPDATED_CLASS_WORK_TEXT);
        assertThat(testClassClassWork.getClassWorkFile()).isEqualTo(UPDATED_CLASS_WORK_FILE);
        assertThat(testClassClassWork.getClassWorkFileContentType()).isEqualTo(UPDATED_CLASS_WORK_FILE_CONTENT_TYPE);
        assertThat(testClassClassWork.getClassWorkFileLink()).isEqualTo(UPDATED_CLASS_WORK_FILE_LINK);
        assertThat(testClassClassWork.getAssign()).isEqualTo(UPDATED_ASSIGN);
        assertThat(testClassClassWork.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testClassClassWork.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testClassClassWork.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void putNonExistingClassClassWork() throws Exception {
        int databaseSizeBeforeUpdate = classClassWorkRepository.findAll().size();
        classClassWork.setId(count.incrementAndGet());

        // Create the ClassClassWork
        ClassClassWorkDTO classClassWorkDTO = classClassWorkMapper.toDto(classClassWork);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClassClassWorkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, classClassWorkDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(classClassWorkDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClassClassWork in the database
        List<ClassClassWork> classClassWorkList = classClassWorkRepository.findAll();
        assertThat(classClassWorkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchClassClassWork() throws Exception {
        int databaseSizeBeforeUpdate = classClassWorkRepository.findAll().size();
        classClassWork.setId(count.incrementAndGet());

        // Create the ClassClassWork
        ClassClassWorkDTO classClassWorkDTO = classClassWorkMapper.toDto(classClassWork);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClassClassWorkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(classClassWorkDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClassClassWork in the database
        List<ClassClassWork> classClassWorkList = classClassWorkRepository.findAll();
        assertThat(classClassWorkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamClassClassWork() throws Exception {
        int databaseSizeBeforeUpdate = classClassWorkRepository.findAll().size();
        classClassWork.setId(count.incrementAndGet());

        // Create the ClassClassWork
        ClassClassWorkDTO classClassWorkDTO = classClassWorkMapper.toDto(classClassWork);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClassClassWorkMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(classClassWorkDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ClassClassWork in the database
        List<ClassClassWork> classClassWorkList = classClassWorkRepository.findAll();
        assertThat(classClassWorkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateClassClassWorkWithPatch() throws Exception {
        // Initialize the database
        classClassWorkRepository.saveAndFlush(classClassWork);

        int databaseSizeBeforeUpdate = classClassWorkRepository.findAll().size();

        // Update the classClassWork using partial update
        ClassClassWork partialUpdatedClassClassWork = new ClassClassWork();
        partialUpdatedClassClassWork.setId(classClassWork.getId());

        partialUpdatedClassClassWork
            .studentAssignmentType(UPDATED_STUDENT_ASSIGNMENT_TYPE)
            .classWorkText(UPDATED_CLASS_WORK_TEXT)
            .classWorkFile(UPDATED_CLASS_WORK_FILE)
            .classWorkFileContentType(UPDATED_CLASS_WORK_FILE_CONTENT_TYPE)
            .lastModified(UPDATED_LAST_MODIFIED);

        restClassClassWorkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClassClassWork.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedClassClassWork))
            )
            .andExpect(status().isOk());

        // Validate the ClassClassWork in the database
        List<ClassClassWork> classClassWorkList = classClassWorkRepository.findAll();
        assertThat(classClassWorkList).hasSize(databaseSizeBeforeUpdate);
        ClassClassWork testClassClassWork = classClassWorkList.get(classClassWorkList.size() - 1);
        assertThat(testClassClassWork.getSchoolDate()).isEqualTo(DEFAULT_SCHOOL_DATE);
        assertThat(testClassClassWork.getStudentAssignmentType()).isEqualTo(UPDATED_STUDENT_ASSIGNMENT_TYPE);
        assertThat(testClassClassWork.getClassWorkText()).isEqualTo(UPDATED_CLASS_WORK_TEXT);
        assertThat(testClassClassWork.getClassWorkFile()).isEqualTo(UPDATED_CLASS_WORK_FILE);
        assertThat(testClassClassWork.getClassWorkFileContentType()).isEqualTo(UPDATED_CLASS_WORK_FILE_CONTENT_TYPE);
        assertThat(testClassClassWork.getClassWorkFileLink()).isEqualTo(DEFAULT_CLASS_WORK_FILE_LINK);
        assertThat(testClassClassWork.getAssign()).isEqualTo(DEFAULT_ASSIGN);
        assertThat(testClassClassWork.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testClassClassWork.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testClassClassWork.getCancelDate()).isEqualTo(DEFAULT_CANCEL_DATE);
    }

    @Test
    @Transactional
    void fullUpdateClassClassWorkWithPatch() throws Exception {
        // Initialize the database
        classClassWorkRepository.saveAndFlush(classClassWork);

        int databaseSizeBeforeUpdate = classClassWorkRepository.findAll().size();

        // Update the classClassWork using partial update
        ClassClassWork partialUpdatedClassClassWork = new ClassClassWork();
        partialUpdatedClassClassWork.setId(classClassWork.getId());

        partialUpdatedClassClassWork
            .schoolDate(UPDATED_SCHOOL_DATE)
            .studentAssignmentType(UPDATED_STUDENT_ASSIGNMENT_TYPE)
            .classWorkText(UPDATED_CLASS_WORK_TEXT)
            .classWorkFile(UPDATED_CLASS_WORK_FILE)
            .classWorkFileContentType(UPDATED_CLASS_WORK_FILE_CONTENT_TYPE)
            .classWorkFileLink(UPDATED_CLASS_WORK_FILE_LINK)
            .assign(UPDATED_ASSIGN)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);

        restClassClassWorkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClassClassWork.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedClassClassWork))
            )
            .andExpect(status().isOk());

        // Validate the ClassClassWork in the database
        List<ClassClassWork> classClassWorkList = classClassWorkRepository.findAll();
        assertThat(classClassWorkList).hasSize(databaseSizeBeforeUpdate);
        ClassClassWork testClassClassWork = classClassWorkList.get(classClassWorkList.size() - 1);
        assertThat(testClassClassWork.getSchoolDate()).isEqualTo(UPDATED_SCHOOL_DATE);
        assertThat(testClassClassWork.getStudentAssignmentType()).isEqualTo(UPDATED_STUDENT_ASSIGNMENT_TYPE);
        assertThat(testClassClassWork.getClassWorkText()).isEqualTo(UPDATED_CLASS_WORK_TEXT);
        assertThat(testClassClassWork.getClassWorkFile()).isEqualTo(UPDATED_CLASS_WORK_FILE);
        assertThat(testClassClassWork.getClassWorkFileContentType()).isEqualTo(UPDATED_CLASS_WORK_FILE_CONTENT_TYPE);
        assertThat(testClassClassWork.getClassWorkFileLink()).isEqualTo(UPDATED_CLASS_WORK_FILE_LINK);
        assertThat(testClassClassWork.getAssign()).isEqualTo(UPDATED_ASSIGN);
        assertThat(testClassClassWork.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testClassClassWork.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testClassClassWork.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingClassClassWork() throws Exception {
        int databaseSizeBeforeUpdate = classClassWorkRepository.findAll().size();
        classClassWork.setId(count.incrementAndGet());

        // Create the ClassClassWork
        ClassClassWorkDTO classClassWorkDTO = classClassWorkMapper.toDto(classClassWork);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClassClassWorkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, classClassWorkDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(classClassWorkDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClassClassWork in the database
        List<ClassClassWork> classClassWorkList = classClassWorkRepository.findAll();
        assertThat(classClassWorkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchClassClassWork() throws Exception {
        int databaseSizeBeforeUpdate = classClassWorkRepository.findAll().size();
        classClassWork.setId(count.incrementAndGet());

        // Create the ClassClassWork
        ClassClassWorkDTO classClassWorkDTO = classClassWorkMapper.toDto(classClassWork);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClassClassWorkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(classClassWorkDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClassClassWork in the database
        List<ClassClassWork> classClassWorkList = classClassWorkRepository.findAll();
        assertThat(classClassWorkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamClassClassWork() throws Exception {
        int databaseSizeBeforeUpdate = classClassWorkRepository.findAll().size();
        classClassWork.setId(count.incrementAndGet());

        // Create the ClassClassWork
        ClassClassWorkDTO classClassWorkDTO = classClassWorkMapper.toDto(classClassWork);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClassClassWorkMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(classClassWorkDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ClassClassWork in the database
        List<ClassClassWork> classClassWorkList = classClassWorkRepository.findAll();
        assertThat(classClassWorkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteClassClassWork() throws Exception {
        // Initialize the database
        classClassWorkRepository.saveAndFlush(classClassWork);

        int databaseSizeBeforeDelete = classClassWorkRepository.findAll().size();

        // Delete the classClassWork
        restClassClassWorkMockMvc
            .perform(delete(ENTITY_API_URL_ID, classClassWork.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ClassClassWork> classClassWorkList = classClassWorkRepository.findAll();
        assertThat(classClassWorkList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
