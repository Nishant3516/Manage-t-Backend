package com.ssik.manageit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ssik.manageit.IntegrationTest;
import com.ssik.manageit.domain.ClassLessionPlan;
import com.ssik.manageit.domain.ClassSubject;
import com.ssik.manageit.domain.SchoolClass;
import com.ssik.manageit.domain.SchoolUser;
import com.ssik.manageit.domain.SubjectChapter;
import com.ssik.manageit.repository.ClassSubjectRepository;
import com.ssik.manageit.service.ClassSubjectService;
import com.ssik.manageit.service.criteria.ClassSubjectCriteria;
import com.ssik.manageit.service.dto.ClassSubjectDTO;
import com.ssik.manageit.service.mapper.ClassSubjectMapper;
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
 * Integration tests for the {@link ClassSubjectResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ClassSubjectResourceIT {

    private static final String DEFAULT_SUBJECT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SUBJECT_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATE_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CREATE_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_LAST_MODIFIED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_MODIFIED = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_LAST_MODIFIED = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_CANCEL_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CANCEL_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CANCEL_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/class-subjects";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ClassSubjectRepository classSubjectRepository;

    @Mock
    private ClassSubjectRepository classSubjectRepositoryMock;

    @Autowired
    private ClassSubjectMapper classSubjectMapper;

    @Mock
    private ClassSubjectService classSubjectServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restClassSubjectMockMvc;

    private ClassSubject classSubject;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClassSubject createEntity(EntityManager em) {
        ClassSubject classSubject = new ClassSubject()
            .subjectName(DEFAULT_SUBJECT_NAME)
            .createDate(DEFAULT_CREATE_DATE)
            .lastModified(DEFAULT_LAST_MODIFIED)
            .cancelDate(DEFAULT_CANCEL_DATE);
        return classSubject;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClassSubject createUpdatedEntity(EntityManager em) {
        ClassSubject classSubject = new ClassSubject()
            .subjectName(UPDATED_SUBJECT_NAME)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);
        return classSubject;
    }

    @BeforeEach
    public void initTest() {
        classSubject = createEntity(em);
    }

    @Test
    @Transactional
    void createClassSubject() throws Exception {
        int databaseSizeBeforeCreate = classSubjectRepository.findAll().size();
        // Create the ClassSubject
        ClassSubjectDTO classSubjectDTO = classSubjectMapper.toDto(classSubject);
        restClassSubjectMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(classSubjectDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ClassSubject in the database
        List<ClassSubject> classSubjectList = classSubjectRepository.findAll();
        assertThat(classSubjectList).hasSize(databaseSizeBeforeCreate + 1);
        ClassSubject testClassSubject = classSubjectList.get(classSubjectList.size() - 1);
        assertThat(testClassSubject.getSubjectName()).isEqualTo(DEFAULT_SUBJECT_NAME);
        assertThat(testClassSubject.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testClassSubject.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
        assertThat(testClassSubject.getCancelDate()).isEqualTo(DEFAULT_CANCEL_DATE);
    }

    @Test
    @Transactional
    void createClassSubjectWithExistingId() throws Exception {
        // Create the ClassSubject with an existing ID
        classSubject.setId(1L);
        ClassSubjectDTO classSubjectDTO = classSubjectMapper.toDto(classSubject);

        int databaseSizeBeforeCreate = classSubjectRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restClassSubjectMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(classSubjectDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClassSubject in the database
        List<ClassSubject> classSubjectList = classSubjectRepository.findAll();
        assertThat(classSubjectList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSubjectNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = classSubjectRepository.findAll().size();
        // set the field null
        classSubject.setSubjectName(null);

        // Create the ClassSubject, which fails.
        ClassSubjectDTO classSubjectDTO = classSubjectMapper.toDto(classSubject);

        restClassSubjectMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(classSubjectDTO))
            )
            .andExpect(status().isBadRequest());

        List<ClassSubject> classSubjectList = classSubjectRepository.findAll();
        assertThat(classSubjectList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllClassSubjects() throws Exception {
        // Initialize the database
        classSubjectRepository.saveAndFlush(classSubject);

        // Get all the classSubjectList
        restClassSubjectMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(classSubject.getId().intValue())))
            .andExpect(jsonPath("$.[*].subjectName").value(hasItem(DEFAULT_SUBJECT_NAME)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].cancelDate").value(hasItem(DEFAULT_CANCEL_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllClassSubjectsWithEagerRelationshipsIsEnabled() throws Exception {
        when(classSubjectServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restClassSubjectMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(classSubjectServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllClassSubjectsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(classSubjectServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restClassSubjectMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(classSubjectServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getClassSubject() throws Exception {
        // Initialize the database
        classSubjectRepository.saveAndFlush(classSubject);

        // Get the classSubject
        restClassSubjectMockMvc
            .perform(get(ENTITY_API_URL_ID, classSubject.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(classSubject.getId().intValue()))
            .andExpect(jsonPath("$.subjectName").value(DEFAULT_SUBJECT_NAME))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.lastModified").value(DEFAULT_LAST_MODIFIED.toString()))
            .andExpect(jsonPath("$.cancelDate").value(DEFAULT_CANCEL_DATE.toString()));
    }

    @Test
    @Transactional
    void getClassSubjectsByIdFiltering() throws Exception {
        // Initialize the database
        classSubjectRepository.saveAndFlush(classSubject);

        Long id = classSubject.getId();

        defaultClassSubjectShouldBeFound("id.equals=" + id);
        defaultClassSubjectShouldNotBeFound("id.notEquals=" + id);

        defaultClassSubjectShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultClassSubjectShouldNotBeFound("id.greaterThan=" + id);

        defaultClassSubjectShouldBeFound("id.lessThanOrEqual=" + id);
        defaultClassSubjectShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllClassSubjectsBySubjectNameIsEqualToSomething() throws Exception {
        // Initialize the database
        classSubjectRepository.saveAndFlush(classSubject);

        // Get all the classSubjectList where subjectName equals to DEFAULT_SUBJECT_NAME
        defaultClassSubjectShouldBeFound("subjectName.equals=" + DEFAULT_SUBJECT_NAME);

        // Get all the classSubjectList where subjectName equals to UPDATED_SUBJECT_NAME
        defaultClassSubjectShouldNotBeFound("subjectName.equals=" + UPDATED_SUBJECT_NAME);
    }

    @Test
    @Transactional
    void getAllClassSubjectsBySubjectNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classSubjectRepository.saveAndFlush(classSubject);

        // Get all the classSubjectList where subjectName not equals to DEFAULT_SUBJECT_NAME
        defaultClassSubjectShouldNotBeFound("subjectName.notEquals=" + DEFAULT_SUBJECT_NAME);

        // Get all the classSubjectList where subjectName not equals to UPDATED_SUBJECT_NAME
        defaultClassSubjectShouldBeFound("subjectName.notEquals=" + UPDATED_SUBJECT_NAME);
    }

    @Test
    @Transactional
    void getAllClassSubjectsBySubjectNameIsInShouldWork() throws Exception {
        // Initialize the database
        classSubjectRepository.saveAndFlush(classSubject);

        // Get all the classSubjectList where subjectName in DEFAULT_SUBJECT_NAME or UPDATED_SUBJECT_NAME
        defaultClassSubjectShouldBeFound("subjectName.in=" + DEFAULT_SUBJECT_NAME + "," + UPDATED_SUBJECT_NAME);

        // Get all the classSubjectList where subjectName equals to UPDATED_SUBJECT_NAME
        defaultClassSubjectShouldNotBeFound("subjectName.in=" + UPDATED_SUBJECT_NAME);
    }

    @Test
    @Transactional
    void getAllClassSubjectsBySubjectNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        classSubjectRepository.saveAndFlush(classSubject);

        // Get all the classSubjectList where subjectName is not null
        defaultClassSubjectShouldBeFound("subjectName.specified=true");

        // Get all the classSubjectList where subjectName is null
        defaultClassSubjectShouldNotBeFound("subjectName.specified=false");
    }

    @Test
    @Transactional
    void getAllClassSubjectsBySubjectNameContainsSomething() throws Exception {
        // Initialize the database
        classSubjectRepository.saveAndFlush(classSubject);

        // Get all the classSubjectList where subjectName contains DEFAULT_SUBJECT_NAME
        defaultClassSubjectShouldBeFound("subjectName.contains=" + DEFAULT_SUBJECT_NAME);

        // Get all the classSubjectList where subjectName contains UPDATED_SUBJECT_NAME
        defaultClassSubjectShouldNotBeFound("subjectName.contains=" + UPDATED_SUBJECT_NAME);
    }

    @Test
    @Transactional
    void getAllClassSubjectsBySubjectNameNotContainsSomething() throws Exception {
        // Initialize the database
        classSubjectRepository.saveAndFlush(classSubject);

        // Get all the classSubjectList where subjectName does not contain DEFAULT_SUBJECT_NAME
        defaultClassSubjectShouldNotBeFound("subjectName.doesNotContain=" + DEFAULT_SUBJECT_NAME);

        // Get all the classSubjectList where subjectName does not contain UPDATED_SUBJECT_NAME
        defaultClassSubjectShouldBeFound("subjectName.doesNotContain=" + UPDATED_SUBJECT_NAME);
    }

    @Test
    @Transactional
    void getAllClassSubjectsByCreateDateIsEqualToSomething() throws Exception {
        // Initialize the database
        classSubjectRepository.saveAndFlush(classSubject);

        // Get all the classSubjectList where createDate equals to DEFAULT_CREATE_DATE
        defaultClassSubjectShouldBeFound("createDate.equals=" + DEFAULT_CREATE_DATE);

        // Get all the classSubjectList where createDate equals to UPDATED_CREATE_DATE
        defaultClassSubjectShouldNotBeFound("createDate.equals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllClassSubjectsByCreateDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classSubjectRepository.saveAndFlush(classSubject);

        // Get all the classSubjectList where createDate not equals to DEFAULT_CREATE_DATE
        defaultClassSubjectShouldNotBeFound("createDate.notEquals=" + DEFAULT_CREATE_DATE);

        // Get all the classSubjectList where createDate not equals to UPDATED_CREATE_DATE
        defaultClassSubjectShouldBeFound("createDate.notEquals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllClassSubjectsByCreateDateIsInShouldWork() throws Exception {
        // Initialize the database
        classSubjectRepository.saveAndFlush(classSubject);

        // Get all the classSubjectList where createDate in DEFAULT_CREATE_DATE or UPDATED_CREATE_DATE
        defaultClassSubjectShouldBeFound("createDate.in=" + DEFAULT_CREATE_DATE + "," + UPDATED_CREATE_DATE);

        // Get all the classSubjectList where createDate equals to UPDATED_CREATE_DATE
        defaultClassSubjectShouldNotBeFound("createDate.in=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllClassSubjectsByCreateDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        classSubjectRepository.saveAndFlush(classSubject);

        // Get all the classSubjectList where createDate is not null
        defaultClassSubjectShouldBeFound("createDate.specified=true");

        // Get all the classSubjectList where createDate is null
        defaultClassSubjectShouldNotBeFound("createDate.specified=false");
    }

    @Test
    @Transactional
    void getAllClassSubjectsByCreateDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        classSubjectRepository.saveAndFlush(classSubject);

        // Get all the classSubjectList where createDate is greater than or equal to DEFAULT_CREATE_DATE
        defaultClassSubjectShouldBeFound("createDate.greaterThanOrEqual=" + DEFAULT_CREATE_DATE);

        // Get all the classSubjectList where createDate is greater than or equal to UPDATED_CREATE_DATE
        defaultClassSubjectShouldNotBeFound("createDate.greaterThanOrEqual=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllClassSubjectsByCreateDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        classSubjectRepository.saveAndFlush(classSubject);

        // Get all the classSubjectList where createDate is less than or equal to DEFAULT_CREATE_DATE
        defaultClassSubjectShouldBeFound("createDate.lessThanOrEqual=" + DEFAULT_CREATE_DATE);

        // Get all the classSubjectList where createDate is less than or equal to SMALLER_CREATE_DATE
        defaultClassSubjectShouldNotBeFound("createDate.lessThanOrEqual=" + SMALLER_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllClassSubjectsByCreateDateIsLessThanSomething() throws Exception {
        // Initialize the database
        classSubjectRepository.saveAndFlush(classSubject);

        // Get all the classSubjectList where createDate is less than DEFAULT_CREATE_DATE
        defaultClassSubjectShouldNotBeFound("createDate.lessThan=" + DEFAULT_CREATE_DATE);

        // Get all the classSubjectList where createDate is less than UPDATED_CREATE_DATE
        defaultClassSubjectShouldBeFound("createDate.lessThan=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllClassSubjectsByCreateDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        classSubjectRepository.saveAndFlush(classSubject);

        // Get all the classSubjectList where createDate is greater than DEFAULT_CREATE_DATE
        defaultClassSubjectShouldNotBeFound("createDate.greaterThan=" + DEFAULT_CREATE_DATE);

        // Get all the classSubjectList where createDate is greater than SMALLER_CREATE_DATE
        defaultClassSubjectShouldBeFound("createDate.greaterThan=" + SMALLER_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllClassSubjectsByLastModifiedIsEqualToSomething() throws Exception {
        // Initialize the database
        classSubjectRepository.saveAndFlush(classSubject);

        // Get all the classSubjectList where lastModified equals to DEFAULT_LAST_MODIFIED
        defaultClassSubjectShouldBeFound("lastModified.equals=" + DEFAULT_LAST_MODIFIED);

        // Get all the classSubjectList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultClassSubjectShouldNotBeFound("lastModified.equals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllClassSubjectsByLastModifiedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classSubjectRepository.saveAndFlush(classSubject);

        // Get all the classSubjectList where lastModified not equals to DEFAULT_LAST_MODIFIED
        defaultClassSubjectShouldNotBeFound("lastModified.notEquals=" + DEFAULT_LAST_MODIFIED);

        // Get all the classSubjectList where lastModified not equals to UPDATED_LAST_MODIFIED
        defaultClassSubjectShouldBeFound("lastModified.notEquals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllClassSubjectsByLastModifiedIsInShouldWork() throws Exception {
        // Initialize the database
        classSubjectRepository.saveAndFlush(classSubject);

        // Get all the classSubjectList where lastModified in DEFAULT_LAST_MODIFIED or UPDATED_LAST_MODIFIED
        defaultClassSubjectShouldBeFound("lastModified.in=" + DEFAULT_LAST_MODIFIED + "," + UPDATED_LAST_MODIFIED);

        // Get all the classSubjectList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultClassSubjectShouldNotBeFound("lastModified.in=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllClassSubjectsByLastModifiedIsNullOrNotNull() throws Exception {
        // Initialize the database
        classSubjectRepository.saveAndFlush(classSubject);

        // Get all the classSubjectList where lastModified is not null
        defaultClassSubjectShouldBeFound("lastModified.specified=true");

        // Get all the classSubjectList where lastModified is null
        defaultClassSubjectShouldNotBeFound("lastModified.specified=false");
    }

    @Test
    @Transactional
    void getAllClassSubjectsByLastModifiedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        classSubjectRepository.saveAndFlush(classSubject);

        // Get all the classSubjectList where lastModified is greater than or equal to DEFAULT_LAST_MODIFIED
        defaultClassSubjectShouldBeFound("lastModified.greaterThanOrEqual=" + DEFAULT_LAST_MODIFIED);

        // Get all the classSubjectList where lastModified is greater than or equal to UPDATED_LAST_MODIFIED
        defaultClassSubjectShouldNotBeFound("lastModified.greaterThanOrEqual=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllClassSubjectsByLastModifiedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        classSubjectRepository.saveAndFlush(classSubject);

        // Get all the classSubjectList where lastModified is less than or equal to DEFAULT_LAST_MODIFIED
        defaultClassSubjectShouldBeFound("lastModified.lessThanOrEqual=" + DEFAULT_LAST_MODIFIED);

        // Get all the classSubjectList where lastModified is less than or equal to SMALLER_LAST_MODIFIED
        defaultClassSubjectShouldNotBeFound("lastModified.lessThanOrEqual=" + SMALLER_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllClassSubjectsByLastModifiedIsLessThanSomething() throws Exception {
        // Initialize the database
        classSubjectRepository.saveAndFlush(classSubject);

        // Get all the classSubjectList where lastModified is less than DEFAULT_LAST_MODIFIED
        defaultClassSubjectShouldNotBeFound("lastModified.lessThan=" + DEFAULT_LAST_MODIFIED);

        // Get all the classSubjectList where lastModified is less than UPDATED_LAST_MODIFIED
        defaultClassSubjectShouldBeFound("lastModified.lessThan=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllClassSubjectsByLastModifiedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        classSubjectRepository.saveAndFlush(classSubject);

        // Get all the classSubjectList where lastModified is greater than DEFAULT_LAST_MODIFIED
        defaultClassSubjectShouldNotBeFound("lastModified.greaterThan=" + DEFAULT_LAST_MODIFIED);

        // Get all the classSubjectList where lastModified is greater than SMALLER_LAST_MODIFIED
        defaultClassSubjectShouldBeFound("lastModified.greaterThan=" + SMALLER_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllClassSubjectsByCancelDateIsEqualToSomething() throws Exception {
        // Initialize the database
        classSubjectRepository.saveAndFlush(classSubject);

        // Get all the classSubjectList where cancelDate equals to DEFAULT_CANCEL_DATE
        defaultClassSubjectShouldBeFound("cancelDate.equals=" + DEFAULT_CANCEL_DATE);

        // Get all the classSubjectList where cancelDate equals to UPDATED_CANCEL_DATE
        defaultClassSubjectShouldNotBeFound("cancelDate.equals=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllClassSubjectsByCancelDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classSubjectRepository.saveAndFlush(classSubject);

        // Get all the classSubjectList where cancelDate not equals to DEFAULT_CANCEL_DATE
        defaultClassSubjectShouldNotBeFound("cancelDate.notEquals=" + DEFAULT_CANCEL_DATE);

        // Get all the classSubjectList where cancelDate not equals to UPDATED_CANCEL_DATE
        defaultClassSubjectShouldBeFound("cancelDate.notEquals=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllClassSubjectsByCancelDateIsInShouldWork() throws Exception {
        // Initialize the database
        classSubjectRepository.saveAndFlush(classSubject);

        // Get all the classSubjectList where cancelDate in DEFAULT_CANCEL_DATE or UPDATED_CANCEL_DATE
        defaultClassSubjectShouldBeFound("cancelDate.in=" + DEFAULT_CANCEL_DATE + "," + UPDATED_CANCEL_DATE);

        // Get all the classSubjectList where cancelDate equals to UPDATED_CANCEL_DATE
        defaultClassSubjectShouldNotBeFound("cancelDate.in=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllClassSubjectsByCancelDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        classSubjectRepository.saveAndFlush(classSubject);

        // Get all the classSubjectList where cancelDate is not null
        defaultClassSubjectShouldBeFound("cancelDate.specified=true");

        // Get all the classSubjectList where cancelDate is null
        defaultClassSubjectShouldNotBeFound("cancelDate.specified=false");
    }

    @Test
    @Transactional
    void getAllClassSubjectsByCancelDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        classSubjectRepository.saveAndFlush(classSubject);

        // Get all the classSubjectList where cancelDate is greater than or equal to DEFAULT_CANCEL_DATE
        defaultClassSubjectShouldBeFound("cancelDate.greaterThanOrEqual=" + DEFAULT_CANCEL_DATE);

        // Get all the classSubjectList where cancelDate is greater than or equal to UPDATED_CANCEL_DATE
        defaultClassSubjectShouldNotBeFound("cancelDate.greaterThanOrEqual=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllClassSubjectsByCancelDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        classSubjectRepository.saveAndFlush(classSubject);

        // Get all the classSubjectList where cancelDate is less than or equal to DEFAULT_CANCEL_DATE
        defaultClassSubjectShouldBeFound("cancelDate.lessThanOrEqual=" + DEFAULT_CANCEL_DATE);

        // Get all the classSubjectList where cancelDate is less than or equal to SMALLER_CANCEL_DATE
        defaultClassSubjectShouldNotBeFound("cancelDate.lessThanOrEqual=" + SMALLER_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllClassSubjectsByCancelDateIsLessThanSomething() throws Exception {
        // Initialize the database
        classSubjectRepository.saveAndFlush(classSubject);

        // Get all the classSubjectList where cancelDate is less than DEFAULT_CANCEL_DATE
        defaultClassSubjectShouldNotBeFound("cancelDate.lessThan=" + DEFAULT_CANCEL_DATE);

        // Get all the classSubjectList where cancelDate is less than UPDATED_CANCEL_DATE
        defaultClassSubjectShouldBeFound("cancelDate.lessThan=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllClassSubjectsByCancelDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        classSubjectRepository.saveAndFlush(classSubject);

        // Get all the classSubjectList where cancelDate is greater than DEFAULT_CANCEL_DATE
        defaultClassSubjectShouldNotBeFound("cancelDate.greaterThan=" + DEFAULT_CANCEL_DATE);

        // Get all the classSubjectList where cancelDate is greater than SMALLER_CANCEL_DATE
        defaultClassSubjectShouldBeFound("cancelDate.greaterThan=" + SMALLER_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllClassSubjectsBySubjectChapterIsEqualToSomething() throws Exception {
        // Initialize the database
        classSubjectRepository.saveAndFlush(classSubject);
        SubjectChapter subjectChapter = SubjectChapterResourceIT.createEntity(em);
        em.persist(subjectChapter);
        em.flush();
        classSubject.addSubjectChapter(subjectChapter);
        classSubjectRepository.saveAndFlush(classSubject);
        Long subjectChapterId = subjectChapter.getId();

        // Get all the classSubjectList where subjectChapter equals to subjectChapterId
        defaultClassSubjectShouldBeFound("subjectChapterId.equals=" + subjectChapterId);

        // Get all the classSubjectList where subjectChapter equals to (subjectChapterId + 1)
        defaultClassSubjectShouldNotBeFound("subjectChapterId.equals=" + (subjectChapterId + 1));
    }

    @Test
    @Transactional
    void getAllClassSubjectsByClassLessionPlanIsEqualToSomething() throws Exception {
        // Initialize the database
        classSubjectRepository.saveAndFlush(classSubject);
        ClassLessionPlan classLessionPlan = ClassLessionPlanResourceIT.createEntity(em);
        em.persist(classLessionPlan);
        em.flush();
        classSubject.addClassLessionPlan(classLessionPlan);
        classSubjectRepository.saveAndFlush(classSubject);
        Long classLessionPlanId = classLessionPlan.getId();

        // Get all the classSubjectList where classLessionPlan equals to classLessionPlanId
        defaultClassSubjectShouldBeFound("classLessionPlanId.equals=" + classLessionPlanId);

        // Get all the classSubjectList where classLessionPlan equals to (classLessionPlanId + 1)
        defaultClassSubjectShouldNotBeFound("classLessionPlanId.equals=" + (classLessionPlanId + 1));
    }

    @Test
    @Transactional
    void getAllClassSubjectsBySchoolClassIsEqualToSomething() throws Exception {
        // Initialize the database
        classSubjectRepository.saveAndFlush(classSubject);
        SchoolClass schoolClass = SchoolClassResourceIT.createEntity(em);
        em.persist(schoolClass);
        em.flush();
        classSubject.addSchoolClass(schoolClass);
        classSubjectRepository.saveAndFlush(classSubject);
        Long schoolClassId = schoolClass.getId();

        // Get all the classSubjectList where schoolClass equals to schoolClassId
        defaultClassSubjectShouldBeFound("schoolClassId.equals=" + schoolClassId);

        // Get all the classSubjectList where schoolClass equals to (schoolClassId + 1)
        defaultClassSubjectShouldNotBeFound("schoolClassId.equals=" + (schoolClassId + 1));
    }

    @Test
    @Transactional
    void getAllClassSubjectsBySchoolUserIsEqualToSomething() throws Exception {
        // Initialize the database
        classSubjectRepository.saveAndFlush(classSubject);
        SchoolUser schoolUser = SchoolUserResourceIT.createEntity(em);
        em.persist(schoolUser);
        em.flush();
        classSubject.addSchoolUser(schoolUser);
        classSubjectRepository.saveAndFlush(classSubject);
        Long schoolUserId = schoolUser.getId();

        // Get all the classSubjectList where schoolUser equals to schoolUserId
        defaultClassSubjectShouldBeFound("schoolUserId.equals=" + schoolUserId);

        // Get all the classSubjectList where schoolUser equals to (schoolUserId + 1)
        defaultClassSubjectShouldNotBeFound("schoolUserId.equals=" + (schoolUserId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultClassSubjectShouldBeFound(String filter) throws Exception {
        restClassSubjectMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(classSubject.getId().intValue())))
            .andExpect(jsonPath("$.[*].subjectName").value(hasItem(DEFAULT_SUBJECT_NAME)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].cancelDate").value(hasItem(DEFAULT_CANCEL_DATE.toString())));

        // Check, that the count call also returns 1
        restClassSubjectMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultClassSubjectShouldNotBeFound(String filter) throws Exception {
        restClassSubjectMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restClassSubjectMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingClassSubject() throws Exception {
        // Get the classSubject
        restClassSubjectMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewClassSubject() throws Exception {
        // Initialize the database
        classSubjectRepository.saveAndFlush(classSubject);

        int databaseSizeBeforeUpdate = classSubjectRepository.findAll().size();

        // Update the classSubject
        ClassSubject updatedClassSubject = classSubjectRepository.findById(classSubject.getId()).get();
        // Disconnect from session so that the updates on updatedClassSubject are not directly saved in db
        em.detach(updatedClassSubject);
        updatedClassSubject
            .subjectName(UPDATED_SUBJECT_NAME)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);
        ClassSubjectDTO classSubjectDTO = classSubjectMapper.toDto(updatedClassSubject);

        restClassSubjectMockMvc
            .perform(
                put(ENTITY_API_URL_ID, classSubjectDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(classSubjectDTO))
            )
            .andExpect(status().isOk());

        // Validate the ClassSubject in the database
        List<ClassSubject> classSubjectList = classSubjectRepository.findAll();
        assertThat(classSubjectList).hasSize(databaseSizeBeforeUpdate);
        ClassSubject testClassSubject = classSubjectList.get(classSubjectList.size() - 1);
        assertThat(testClassSubject.getSubjectName()).isEqualTo(UPDATED_SUBJECT_NAME);
        assertThat(testClassSubject.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testClassSubject.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testClassSubject.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void putNonExistingClassSubject() throws Exception {
        int databaseSizeBeforeUpdate = classSubjectRepository.findAll().size();
        classSubject.setId(count.incrementAndGet());

        // Create the ClassSubject
        ClassSubjectDTO classSubjectDTO = classSubjectMapper.toDto(classSubject);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClassSubjectMockMvc
            .perform(
                put(ENTITY_API_URL_ID, classSubjectDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(classSubjectDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClassSubject in the database
        List<ClassSubject> classSubjectList = classSubjectRepository.findAll();
        assertThat(classSubjectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchClassSubject() throws Exception {
        int databaseSizeBeforeUpdate = classSubjectRepository.findAll().size();
        classSubject.setId(count.incrementAndGet());

        // Create the ClassSubject
        ClassSubjectDTO classSubjectDTO = classSubjectMapper.toDto(classSubject);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClassSubjectMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(classSubjectDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClassSubject in the database
        List<ClassSubject> classSubjectList = classSubjectRepository.findAll();
        assertThat(classSubjectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamClassSubject() throws Exception {
        int databaseSizeBeforeUpdate = classSubjectRepository.findAll().size();
        classSubject.setId(count.incrementAndGet());

        // Create the ClassSubject
        ClassSubjectDTO classSubjectDTO = classSubjectMapper.toDto(classSubject);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClassSubjectMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(classSubjectDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ClassSubject in the database
        List<ClassSubject> classSubjectList = classSubjectRepository.findAll();
        assertThat(classSubjectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateClassSubjectWithPatch() throws Exception {
        // Initialize the database
        classSubjectRepository.saveAndFlush(classSubject);

        int databaseSizeBeforeUpdate = classSubjectRepository.findAll().size();

        // Update the classSubject using partial update
        ClassSubject partialUpdatedClassSubject = new ClassSubject();
        partialUpdatedClassSubject.setId(classSubject.getId());

        partialUpdatedClassSubject.lastModified(UPDATED_LAST_MODIFIED).cancelDate(UPDATED_CANCEL_DATE);

        restClassSubjectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClassSubject.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedClassSubject))
            )
            .andExpect(status().isOk());

        // Validate the ClassSubject in the database
        List<ClassSubject> classSubjectList = classSubjectRepository.findAll();
        assertThat(classSubjectList).hasSize(databaseSizeBeforeUpdate);
        ClassSubject testClassSubject = classSubjectList.get(classSubjectList.size() - 1);
        assertThat(testClassSubject.getSubjectName()).isEqualTo(DEFAULT_SUBJECT_NAME);
        assertThat(testClassSubject.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testClassSubject.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testClassSubject.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void fullUpdateClassSubjectWithPatch() throws Exception {
        // Initialize the database
        classSubjectRepository.saveAndFlush(classSubject);

        int databaseSizeBeforeUpdate = classSubjectRepository.findAll().size();

        // Update the classSubject using partial update
        ClassSubject partialUpdatedClassSubject = new ClassSubject();
        partialUpdatedClassSubject.setId(classSubject.getId());

        partialUpdatedClassSubject
            .subjectName(UPDATED_SUBJECT_NAME)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);

        restClassSubjectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClassSubject.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedClassSubject))
            )
            .andExpect(status().isOk());

        // Validate the ClassSubject in the database
        List<ClassSubject> classSubjectList = classSubjectRepository.findAll();
        assertThat(classSubjectList).hasSize(databaseSizeBeforeUpdate);
        ClassSubject testClassSubject = classSubjectList.get(classSubjectList.size() - 1);
        assertThat(testClassSubject.getSubjectName()).isEqualTo(UPDATED_SUBJECT_NAME);
        assertThat(testClassSubject.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testClassSubject.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testClassSubject.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingClassSubject() throws Exception {
        int databaseSizeBeforeUpdate = classSubjectRepository.findAll().size();
        classSubject.setId(count.incrementAndGet());

        // Create the ClassSubject
        ClassSubjectDTO classSubjectDTO = classSubjectMapper.toDto(classSubject);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClassSubjectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, classSubjectDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(classSubjectDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClassSubject in the database
        List<ClassSubject> classSubjectList = classSubjectRepository.findAll();
        assertThat(classSubjectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchClassSubject() throws Exception {
        int databaseSizeBeforeUpdate = classSubjectRepository.findAll().size();
        classSubject.setId(count.incrementAndGet());

        // Create the ClassSubject
        ClassSubjectDTO classSubjectDTO = classSubjectMapper.toDto(classSubject);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClassSubjectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(classSubjectDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClassSubject in the database
        List<ClassSubject> classSubjectList = classSubjectRepository.findAll();
        assertThat(classSubjectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamClassSubject() throws Exception {
        int databaseSizeBeforeUpdate = classSubjectRepository.findAll().size();
        classSubject.setId(count.incrementAndGet());

        // Create the ClassSubject
        ClassSubjectDTO classSubjectDTO = classSubjectMapper.toDto(classSubject);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClassSubjectMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(classSubjectDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ClassSubject in the database
        List<ClassSubject> classSubjectList = classSubjectRepository.findAll();
        assertThat(classSubjectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteClassSubject() throws Exception {
        // Initialize the database
        classSubjectRepository.saveAndFlush(classSubject);

        int databaseSizeBeforeDelete = classSubjectRepository.findAll().size();

        // Delete the classSubject
        restClassSubjectMockMvc
            .perform(delete(ENTITY_API_URL_ID, classSubject.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ClassSubject> classSubjectList = classSubjectRepository.findAll();
        assertThat(classSubjectList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
