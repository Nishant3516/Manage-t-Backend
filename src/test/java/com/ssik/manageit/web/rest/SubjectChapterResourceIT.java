package com.ssik.manageit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ssik.manageit.IntegrationTest;
import com.ssik.manageit.domain.ChapterSection;
import com.ssik.manageit.domain.ClassLessionPlan;
import com.ssik.manageit.domain.ClassSubject;
import com.ssik.manageit.domain.SubjectChapter;
import com.ssik.manageit.repository.SubjectChapterRepository;
import com.ssik.manageit.service.criteria.SubjectChapterCriteria;
import com.ssik.manageit.service.dto.SubjectChapterDTO;
import com.ssik.manageit.service.mapper.SubjectChapterMapper;
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
 * Integration tests for the {@link SubjectChapterResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SubjectChapterResourceIT {

    private static final String DEFAULT_CHAPTER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CHAPTER_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_CHAPTER_NUMBER = 1;
    private static final Integer UPDATED_CHAPTER_NUMBER = 2;
    private static final Integer SMALLER_CHAPTER_NUMBER = 1 - 1;

    private static final LocalDate DEFAULT_CREATE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATE_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CREATE_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_LAST_MODIFIED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_MODIFIED = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_LAST_MODIFIED = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_CANCEL_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CANCEL_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CANCEL_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/subject-chapters";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SubjectChapterRepository subjectChapterRepository;

    @Autowired
    private SubjectChapterMapper subjectChapterMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSubjectChapterMockMvc;

    private SubjectChapter subjectChapter;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubjectChapter createEntity(EntityManager em) {
        SubjectChapter subjectChapter = new SubjectChapter()
            .chapterName(DEFAULT_CHAPTER_NAME)
            .chapterNumber(DEFAULT_CHAPTER_NUMBER)
            .createDate(DEFAULT_CREATE_DATE)
            .lastModified(DEFAULT_LAST_MODIFIED)
            .cancelDate(DEFAULT_CANCEL_DATE);
        return subjectChapter;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubjectChapter createUpdatedEntity(EntityManager em) {
        SubjectChapter subjectChapter = new SubjectChapter()
            .chapterName(UPDATED_CHAPTER_NAME)
            .chapterNumber(UPDATED_CHAPTER_NUMBER)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);
        return subjectChapter;
    }

    @BeforeEach
    public void initTest() {
        subjectChapter = createEntity(em);
    }

    @Test
    @Transactional
    void createSubjectChapter() throws Exception {
        int databaseSizeBeforeCreate = subjectChapterRepository.findAll().size();
        // Create the SubjectChapter
        SubjectChapterDTO subjectChapterDTO = subjectChapterMapper.toDto(subjectChapter);
        restSubjectChapterMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subjectChapterDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SubjectChapter in the database
        List<SubjectChapter> subjectChapterList = subjectChapterRepository.findAll();
        assertThat(subjectChapterList).hasSize(databaseSizeBeforeCreate + 1);
        SubjectChapter testSubjectChapter = subjectChapterList.get(subjectChapterList.size() - 1);
        assertThat(testSubjectChapter.getChapterName()).isEqualTo(DEFAULT_CHAPTER_NAME);
        assertThat(testSubjectChapter.getChapterNumber()).isEqualTo(DEFAULT_CHAPTER_NUMBER);
        assertThat(testSubjectChapter.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testSubjectChapter.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
        assertThat(testSubjectChapter.getCancelDate()).isEqualTo(DEFAULT_CANCEL_DATE);
    }

    @Test
    @Transactional
    void createSubjectChapterWithExistingId() throws Exception {
        // Create the SubjectChapter with an existing ID
        subjectChapter.setId(1L);
        SubjectChapterDTO subjectChapterDTO = subjectChapterMapper.toDto(subjectChapter);

        int databaseSizeBeforeCreate = subjectChapterRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubjectChapterMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subjectChapterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubjectChapter in the database
        List<SubjectChapter> subjectChapterList = subjectChapterRepository.findAll();
        assertThat(subjectChapterList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkChapterNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = subjectChapterRepository.findAll().size();
        // set the field null
        subjectChapter.setChapterName(null);

        // Create the SubjectChapter, which fails.
        SubjectChapterDTO subjectChapterDTO = subjectChapterMapper.toDto(subjectChapter);

        restSubjectChapterMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subjectChapterDTO))
            )
            .andExpect(status().isBadRequest());

        List<SubjectChapter> subjectChapterList = subjectChapterRepository.findAll();
        assertThat(subjectChapterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSubjectChapters() throws Exception {
        // Initialize the database
        subjectChapterRepository.saveAndFlush(subjectChapter);

        // Get all the subjectChapterList
        restSubjectChapterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subjectChapter.getId().intValue())))
            .andExpect(jsonPath("$.[*].chapterName").value(hasItem(DEFAULT_CHAPTER_NAME)))
            .andExpect(jsonPath("$.[*].chapterNumber").value(hasItem(DEFAULT_CHAPTER_NUMBER)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].cancelDate").value(hasItem(DEFAULT_CANCEL_DATE.toString())));
    }

    @Test
    @Transactional
    void getSubjectChapter() throws Exception {
        // Initialize the database
        subjectChapterRepository.saveAndFlush(subjectChapter);

        // Get the subjectChapter
        restSubjectChapterMockMvc
            .perform(get(ENTITY_API_URL_ID, subjectChapter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(subjectChapter.getId().intValue()))
            .andExpect(jsonPath("$.chapterName").value(DEFAULT_CHAPTER_NAME))
            .andExpect(jsonPath("$.chapterNumber").value(DEFAULT_CHAPTER_NUMBER))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.lastModified").value(DEFAULT_LAST_MODIFIED.toString()))
            .andExpect(jsonPath("$.cancelDate").value(DEFAULT_CANCEL_DATE.toString()));
    }

    @Test
    @Transactional
    void getSubjectChaptersByIdFiltering() throws Exception {
        // Initialize the database
        subjectChapterRepository.saveAndFlush(subjectChapter);

        Long id = subjectChapter.getId();

        defaultSubjectChapterShouldBeFound("id.equals=" + id);
        defaultSubjectChapterShouldNotBeFound("id.notEquals=" + id);

        defaultSubjectChapterShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSubjectChapterShouldNotBeFound("id.greaterThan=" + id);

        defaultSubjectChapterShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSubjectChapterShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSubjectChaptersByChapterNameIsEqualToSomething() throws Exception {
        // Initialize the database
        subjectChapterRepository.saveAndFlush(subjectChapter);

        // Get all the subjectChapterList where chapterName equals to DEFAULT_CHAPTER_NAME
        defaultSubjectChapterShouldBeFound("chapterName.equals=" + DEFAULT_CHAPTER_NAME);

        // Get all the subjectChapterList where chapterName equals to UPDATED_CHAPTER_NAME
        defaultSubjectChapterShouldNotBeFound("chapterName.equals=" + UPDATED_CHAPTER_NAME);
    }

    @Test
    @Transactional
    void getAllSubjectChaptersByChapterNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        subjectChapterRepository.saveAndFlush(subjectChapter);

        // Get all the subjectChapterList where chapterName not equals to DEFAULT_CHAPTER_NAME
        defaultSubjectChapterShouldNotBeFound("chapterName.notEquals=" + DEFAULT_CHAPTER_NAME);

        // Get all the subjectChapterList where chapterName not equals to UPDATED_CHAPTER_NAME
        defaultSubjectChapterShouldBeFound("chapterName.notEquals=" + UPDATED_CHAPTER_NAME);
    }

    @Test
    @Transactional
    void getAllSubjectChaptersByChapterNameIsInShouldWork() throws Exception {
        // Initialize the database
        subjectChapterRepository.saveAndFlush(subjectChapter);

        // Get all the subjectChapterList where chapterName in DEFAULT_CHAPTER_NAME or UPDATED_CHAPTER_NAME
        defaultSubjectChapterShouldBeFound("chapterName.in=" + DEFAULT_CHAPTER_NAME + "," + UPDATED_CHAPTER_NAME);

        // Get all the subjectChapterList where chapterName equals to UPDATED_CHAPTER_NAME
        defaultSubjectChapterShouldNotBeFound("chapterName.in=" + UPDATED_CHAPTER_NAME);
    }

    @Test
    @Transactional
    void getAllSubjectChaptersByChapterNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        subjectChapterRepository.saveAndFlush(subjectChapter);

        // Get all the subjectChapterList where chapterName is not null
        defaultSubjectChapterShouldBeFound("chapterName.specified=true");

        // Get all the subjectChapterList where chapterName is null
        defaultSubjectChapterShouldNotBeFound("chapterName.specified=false");
    }

    @Test
    @Transactional
    void getAllSubjectChaptersByChapterNameContainsSomething() throws Exception {
        // Initialize the database
        subjectChapterRepository.saveAndFlush(subjectChapter);

        // Get all the subjectChapterList where chapterName contains DEFAULT_CHAPTER_NAME
        defaultSubjectChapterShouldBeFound("chapterName.contains=" + DEFAULT_CHAPTER_NAME);

        // Get all the subjectChapterList where chapterName contains UPDATED_CHAPTER_NAME
        defaultSubjectChapterShouldNotBeFound("chapterName.contains=" + UPDATED_CHAPTER_NAME);
    }

    @Test
    @Transactional
    void getAllSubjectChaptersByChapterNameNotContainsSomething() throws Exception {
        // Initialize the database
        subjectChapterRepository.saveAndFlush(subjectChapter);

        // Get all the subjectChapterList where chapterName does not contain DEFAULT_CHAPTER_NAME
        defaultSubjectChapterShouldNotBeFound("chapterName.doesNotContain=" + DEFAULT_CHAPTER_NAME);

        // Get all the subjectChapterList where chapterName does not contain UPDATED_CHAPTER_NAME
        defaultSubjectChapterShouldBeFound("chapterName.doesNotContain=" + UPDATED_CHAPTER_NAME);
    }

    @Test
    @Transactional
    void getAllSubjectChaptersByChapterNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        subjectChapterRepository.saveAndFlush(subjectChapter);

        // Get all the subjectChapterList where chapterNumber equals to DEFAULT_CHAPTER_NUMBER
        defaultSubjectChapterShouldBeFound("chapterNumber.equals=" + DEFAULT_CHAPTER_NUMBER);

        // Get all the subjectChapterList where chapterNumber equals to UPDATED_CHAPTER_NUMBER
        defaultSubjectChapterShouldNotBeFound("chapterNumber.equals=" + UPDATED_CHAPTER_NUMBER);
    }

    @Test
    @Transactional
    void getAllSubjectChaptersByChapterNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        subjectChapterRepository.saveAndFlush(subjectChapter);

        // Get all the subjectChapterList where chapterNumber not equals to DEFAULT_CHAPTER_NUMBER
        defaultSubjectChapterShouldNotBeFound("chapterNumber.notEquals=" + DEFAULT_CHAPTER_NUMBER);

        // Get all the subjectChapterList where chapterNumber not equals to UPDATED_CHAPTER_NUMBER
        defaultSubjectChapterShouldBeFound("chapterNumber.notEquals=" + UPDATED_CHAPTER_NUMBER);
    }

    @Test
    @Transactional
    void getAllSubjectChaptersByChapterNumberIsInShouldWork() throws Exception {
        // Initialize the database
        subjectChapterRepository.saveAndFlush(subjectChapter);

        // Get all the subjectChapterList where chapterNumber in DEFAULT_CHAPTER_NUMBER or UPDATED_CHAPTER_NUMBER
        defaultSubjectChapterShouldBeFound("chapterNumber.in=" + DEFAULT_CHAPTER_NUMBER + "," + UPDATED_CHAPTER_NUMBER);

        // Get all the subjectChapterList where chapterNumber equals to UPDATED_CHAPTER_NUMBER
        defaultSubjectChapterShouldNotBeFound("chapterNumber.in=" + UPDATED_CHAPTER_NUMBER);
    }

    @Test
    @Transactional
    void getAllSubjectChaptersByChapterNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        subjectChapterRepository.saveAndFlush(subjectChapter);

        // Get all the subjectChapterList where chapterNumber is not null
        defaultSubjectChapterShouldBeFound("chapterNumber.specified=true");

        // Get all the subjectChapterList where chapterNumber is null
        defaultSubjectChapterShouldNotBeFound("chapterNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllSubjectChaptersByChapterNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        subjectChapterRepository.saveAndFlush(subjectChapter);

        // Get all the subjectChapterList where chapterNumber is greater than or equal to DEFAULT_CHAPTER_NUMBER
        defaultSubjectChapterShouldBeFound("chapterNumber.greaterThanOrEqual=" + DEFAULT_CHAPTER_NUMBER);

        // Get all the subjectChapterList where chapterNumber is greater than or equal to UPDATED_CHAPTER_NUMBER
        defaultSubjectChapterShouldNotBeFound("chapterNumber.greaterThanOrEqual=" + UPDATED_CHAPTER_NUMBER);
    }

    @Test
    @Transactional
    void getAllSubjectChaptersByChapterNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        subjectChapterRepository.saveAndFlush(subjectChapter);

        // Get all the subjectChapterList where chapterNumber is less than or equal to DEFAULT_CHAPTER_NUMBER
        defaultSubjectChapterShouldBeFound("chapterNumber.lessThanOrEqual=" + DEFAULT_CHAPTER_NUMBER);

        // Get all the subjectChapterList where chapterNumber is less than or equal to SMALLER_CHAPTER_NUMBER
        defaultSubjectChapterShouldNotBeFound("chapterNumber.lessThanOrEqual=" + SMALLER_CHAPTER_NUMBER);
    }

    @Test
    @Transactional
    void getAllSubjectChaptersByChapterNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        subjectChapterRepository.saveAndFlush(subjectChapter);

        // Get all the subjectChapterList where chapterNumber is less than DEFAULT_CHAPTER_NUMBER
        defaultSubjectChapterShouldNotBeFound("chapterNumber.lessThan=" + DEFAULT_CHAPTER_NUMBER);

        // Get all the subjectChapterList where chapterNumber is less than UPDATED_CHAPTER_NUMBER
        defaultSubjectChapterShouldBeFound("chapterNumber.lessThan=" + UPDATED_CHAPTER_NUMBER);
    }

    @Test
    @Transactional
    void getAllSubjectChaptersByChapterNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        subjectChapterRepository.saveAndFlush(subjectChapter);

        // Get all the subjectChapterList where chapterNumber is greater than DEFAULT_CHAPTER_NUMBER
        defaultSubjectChapterShouldNotBeFound("chapterNumber.greaterThan=" + DEFAULT_CHAPTER_NUMBER);

        // Get all the subjectChapterList where chapterNumber is greater than SMALLER_CHAPTER_NUMBER
        defaultSubjectChapterShouldBeFound("chapterNumber.greaterThan=" + SMALLER_CHAPTER_NUMBER);
    }

    @Test
    @Transactional
    void getAllSubjectChaptersByCreateDateIsEqualToSomething() throws Exception {
        // Initialize the database
        subjectChapterRepository.saveAndFlush(subjectChapter);

        // Get all the subjectChapterList where createDate equals to DEFAULT_CREATE_DATE
        defaultSubjectChapterShouldBeFound("createDate.equals=" + DEFAULT_CREATE_DATE);

        // Get all the subjectChapterList where createDate equals to UPDATED_CREATE_DATE
        defaultSubjectChapterShouldNotBeFound("createDate.equals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSubjectChaptersByCreateDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        subjectChapterRepository.saveAndFlush(subjectChapter);

        // Get all the subjectChapterList where createDate not equals to DEFAULT_CREATE_DATE
        defaultSubjectChapterShouldNotBeFound("createDate.notEquals=" + DEFAULT_CREATE_DATE);

        // Get all the subjectChapterList where createDate not equals to UPDATED_CREATE_DATE
        defaultSubjectChapterShouldBeFound("createDate.notEquals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSubjectChaptersByCreateDateIsInShouldWork() throws Exception {
        // Initialize the database
        subjectChapterRepository.saveAndFlush(subjectChapter);

        // Get all the subjectChapterList where createDate in DEFAULT_CREATE_DATE or UPDATED_CREATE_DATE
        defaultSubjectChapterShouldBeFound("createDate.in=" + DEFAULT_CREATE_DATE + "," + UPDATED_CREATE_DATE);

        // Get all the subjectChapterList where createDate equals to UPDATED_CREATE_DATE
        defaultSubjectChapterShouldNotBeFound("createDate.in=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSubjectChaptersByCreateDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        subjectChapterRepository.saveAndFlush(subjectChapter);

        // Get all the subjectChapterList where createDate is not null
        defaultSubjectChapterShouldBeFound("createDate.specified=true");

        // Get all the subjectChapterList where createDate is null
        defaultSubjectChapterShouldNotBeFound("createDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSubjectChaptersByCreateDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        subjectChapterRepository.saveAndFlush(subjectChapter);

        // Get all the subjectChapterList where createDate is greater than or equal to DEFAULT_CREATE_DATE
        defaultSubjectChapterShouldBeFound("createDate.greaterThanOrEqual=" + DEFAULT_CREATE_DATE);

        // Get all the subjectChapterList where createDate is greater than or equal to UPDATED_CREATE_DATE
        defaultSubjectChapterShouldNotBeFound("createDate.greaterThanOrEqual=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSubjectChaptersByCreateDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        subjectChapterRepository.saveAndFlush(subjectChapter);

        // Get all the subjectChapterList where createDate is less than or equal to DEFAULT_CREATE_DATE
        defaultSubjectChapterShouldBeFound("createDate.lessThanOrEqual=" + DEFAULT_CREATE_DATE);

        // Get all the subjectChapterList where createDate is less than or equal to SMALLER_CREATE_DATE
        defaultSubjectChapterShouldNotBeFound("createDate.lessThanOrEqual=" + SMALLER_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSubjectChaptersByCreateDateIsLessThanSomething() throws Exception {
        // Initialize the database
        subjectChapterRepository.saveAndFlush(subjectChapter);

        // Get all the subjectChapterList where createDate is less than DEFAULT_CREATE_DATE
        defaultSubjectChapterShouldNotBeFound("createDate.lessThan=" + DEFAULT_CREATE_DATE);

        // Get all the subjectChapterList where createDate is less than UPDATED_CREATE_DATE
        defaultSubjectChapterShouldBeFound("createDate.lessThan=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSubjectChaptersByCreateDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        subjectChapterRepository.saveAndFlush(subjectChapter);

        // Get all the subjectChapterList where createDate is greater than DEFAULT_CREATE_DATE
        defaultSubjectChapterShouldNotBeFound("createDate.greaterThan=" + DEFAULT_CREATE_DATE);

        // Get all the subjectChapterList where createDate is greater than SMALLER_CREATE_DATE
        defaultSubjectChapterShouldBeFound("createDate.greaterThan=" + SMALLER_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSubjectChaptersByLastModifiedIsEqualToSomething() throws Exception {
        // Initialize the database
        subjectChapterRepository.saveAndFlush(subjectChapter);

        // Get all the subjectChapterList where lastModified equals to DEFAULT_LAST_MODIFIED
        defaultSubjectChapterShouldBeFound("lastModified.equals=" + DEFAULT_LAST_MODIFIED);

        // Get all the subjectChapterList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultSubjectChapterShouldNotBeFound("lastModified.equals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSubjectChaptersByLastModifiedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        subjectChapterRepository.saveAndFlush(subjectChapter);

        // Get all the subjectChapterList where lastModified not equals to DEFAULT_LAST_MODIFIED
        defaultSubjectChapterShouldNotBeFound("lastModified.notEquals=" + DEFAULT_LAST_MODIFIED);

        // Get all the subjectChapterList where lastModified not equals to UPDATED_LAST_MODIFIED
        defaultSubjectChapterShouldBeFound("lastModified.notEquals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSubjectChaptersByLastModifiedIsInShouldWork() throws Exception {
        // Initialize the database
        subjectChapterRepository.saveAndFlush(subjectChapter);

        // Get all the subjectChapterList where lastModified in DEFAULT_LAST_MODIFIED or UPDATED_LAST_MODIFIED
        defaultSubjectChapterShouldBeFound("lastModified.in=" + DEFAULT_LAST_MODIFIED + "," + UPDATED_LAST_MODIFIED);

        // Get all the subjectChapterList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultSubjectChapterShouldNotBeFound("lastModified.in=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSubjectChaptersByLastModifiedIsNullOrNotNull() throws Exception {
        // Initialize the database
        subjectChapterRepository.saveAndFlush(subjectChapter);

        // Get all the subjectChapterList where lastModified is not null
        defaultSubjectChapterShouldBeFound("lastModified.specified=true");

        // Get all the subjectChapterList where lastModified is null
        defaultSubjectChapterShouldNotBeFound("lastModified.specified=false");
    }

    @Test
    @Transactional
    void getAllSubjectChaptersByLastModifiedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        subjectChapterRepository.saveAndFlush(subjectChapter);

        // Get all the subjectChapterList where lastModified is greater than or equal to DEFAULT_LAST_MODIFIED
        defaultSubjectChapterShouldBeFound("lastModified.greaterThanOrEqual=" + DEFAULT_LAST_MODIFIED);

        // Get all the subjectChapterList where lastModified is greater than or equal to UPDATED_LAST_MODIFIED
        defaultSubjectChapterShouldNotBeFound("lastModified.greaterThanOrEqual=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSubjectChaptersByLastModifiedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        subjectChapterRepository.saveAndFlush(subjectChapter);

        // Get all the subjectChapterList where lastModified is less than or equal to DEFAULT_LAST_MODIFIED
        defaultSubjectChapterShouldBeFound("lastModified.lessThanOrEqual=" + DEFAULT_LAST_MODIFIED);

        // Get all the subjectChapterList where lastModified is less than or equal to SMALLER_LAST_MODIFIED
        defaultSubjectChapterShouldNotBeFound("lastModified.lessThanOrEqual=" + SMALLER_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSubjectChaptersByLastModifiedIsLessThanSomething() throws Exception {
        // Initialize the database
        subjectChapterRepository.saveAndFlush(subjectChapter);

        // Get all the subjectChapterList where lastModified is less than DEFAULT_LAST_MODIFIED
        defaultSubjectChapterShouldNotBeFound("lastModified.lessThan=" + DEFAULT_LAST_MODIFIED);

        // Get all the subjectChapterList where lastModified is less than UPDATED_LAST_MODIFIED
        defaultSubjectChapterShouldBeFound("lastModified.lessThan=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSubjectChaptersByLastModifiedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        subjectChapterRepository.saveAndFlush(subjectChapter);

        // Get all the subjectChapterList where lastModified is greater than DEFAULT_LAST_MODIFIED
        defaultSubjectChapterShouldNotBeFound("lastModified.greaterThan=" + DEFAULT_LAST_MODIFIED);

        // Get all the subjectChapterList where lastModified is greater than SMALLER_LAST_MODIFIED
        defaultSubjectChapterShouldBeFound("lastModified.greaterThan=" + SMALLER_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSubjectChaptersByCancelDateIsEqualToSomething() throws Exception {
        // Initialize the database
        subjectChapterRepository.saveAndFlush(subjectChapter);

        // Get all the subjectChapterList where cancelDate equals to DEFAULT_CANCEL_DATE
        defaultSubjectChapterShouldBeFound("cancelDate.equals=" + DEFAULT_CANCEL_DATE);

        // Get all the subjectChapterList where cancelDate equals to UPDATED_CANCEL_DATE
        defaultSubjectChapterShouldNotBeFound("cancelDate.equals=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSubjectChaptersByCancelDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        subjectChapterRepository.saveAndFlush(subjectChapter);

        // Get all the subjectChapterList where cancelDate not equals to DEFAULT_CANCEL_DATE
        defaultSubjectChapterShouldNotBeFound("cancelDate.notEquals=" + DEFAULT_CANCEL_DATE);

        // Get all the subjectChapterList where cancelDate not equals to UPDATED_CANCEL_DATE
        defaultSubjectChapterShouldBeFound("cancelDate.notEquals=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSubjectChaptersByCancelDateIsInShouldWork() throws Exception {
        // Initialize the database
        subjectChapterRepository.saveAndFlush(subjectChapter);

        // Get all the subjectChapterList where cancelDate in DEFAULT_CANCEL_DATE or UPDATED_CANCEL_DATE
        defaultSubjectChapterShouldBeFound("cancelDate.in=" + DEFAULT_CANCEL_DATE + "," + UPDATED_CANCEL_DATE);

        // Get all the subjectChapterList where cancelDate equals to UPDATED_CANCEL_DATE
        defaultSubjectChapterShouldNotBeFound("cancelDate.in=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSubjectChaptersByCancelDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        subjectChapterRepository.saveAndFlush(subjectChapter);

        // Get all the subjectChapterList where cancelDate is not null
        defaultSubjectChapterShouldBeFound("cancelDate.specified=true");

        // Get all the subjectChapterList where cancelDate is null
        defaultSubjectChapterShouldNotBeFound("cancelDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSubjectChaptersByCancelDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        subjectChapterRepository.saveAndFlush(subjectChapter);

        // Get all the subjectChapterList where cancelDate is greater than or equal to DEFAULT_CANCEL_DATE
        defaultSubjectChapterShouldBeFound("cancelDate.greaterThanOrEqual=" + DEFAULT_CANCEL_DATE);

        // Get all the subjectChapterList where cancelDate is greater than or equal to UPDATED_CANCEL_DATE
        defaultSubjectChapterShouldNotBeFound("cancelDate.greaterThanOrEqual=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSubjectChaptersByCancelDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        subjectChapterRepository.saveAndFlush(subjectChapter);

        // Get all the subjectChapterList where cancelDate is less than or equal to DEFAULT_CANCEL_DATE
        defaultSubjectChapterShouldBeFound("cancelDate.lessThanOrEqual=" + DEFAULT_CANCEL_DATE);

        // Get all the subjectChapterList where cancelDate is less than or equal to SMALLER_CANCEL_DATE
        defaultSubjectChapterShouldNotBeFound("cancelDate.lessThanOrEqual=" + SMALLER_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSubjectChaptersByCancelDateIsLessThanSomething() throws Exception {
        // Initialize the database
        subjectChapterRepository.saveAndFlush(subjectChapter);

        // Get all the subjectChapterList where cancelDate is less than DEFAULT_CANCEL_DATE
        defaultSubjectChapterShouldNotBeFound("cancelDate.lessThan=" + DEFAULT_CANCEL_DATE);

        // Get all the subjectChapterList where cancelDate is less than UPDATED_CANCEL_DATE
        defaultSubjectChapterShouldBeFound("cancelDate.lessThan=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSubjectChaptersByCancelDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        subjectChapterRepository.saveAndFlush(subjectChapter);

        // Get all the subjectChapterList where cancelDate is greater than DEFAULT_CANCEL_DATE
        defaultSubjectChapterShouldNotBeFound("cancelDate.greaterThan=" + DEFAULT_CANCEL_DATE);

        // Get all the subjectChapterList where cancelDate is greater than SMALLER_CANCEL_DATE
        defaultSubjectChapterShouldBeFound("cancelDate.greaterThan=" + SMALLER_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSubjectChaptersByChapterSectionIsEqualToSomething() throws Exception {
        // Initialize the database
        subjectChapterRepository.saveAndFlush(subjectChapter);
        ChapterSection chapterSection = ChapterSectionResourceIT.createEntity(em);
        em.persist(chapterSection);
        em.flush();
        subjectChapter.addChapterSection(chapterSection);
        subjectChapterRepository.saveAndFlush(subjectChapter);
        Long chapterSectionId = chapterSection.getId();

        // Get all the subjectChapterList where chapterSection equals to chapterSectionId
        defaultSubjectChapterShouldBeFound("chapterSectionId.equals=" + chapterSectionId);

        // Get all the subjectChapterList where chapterSection equals to (chapterSectionId + 1)
        defaultSubjectChapterShouldNotBeFound("chapterSectionId.equals=" + (chapterSectionId + 1));
    }

    @Test
    @Transactional
    void getAllSubjectChaptersByClassLessionPlanIsEqualToSomething() throws Exception {
        // Initialize the database
        subjectChapterRepository.saveAndFlush(subjectChapter);
        ClassLessionPlan classLessionPlan = ClassLessionPlanResourceIT.createEntity(em);
        em.persist(classLessionPlan);
        em.flush();
        subjectChapter.addClassLessionPlan(classLessionPlan);
        subjectChapterRepository.saveAndFlush(subjectChapter);
        Long classLessionPlanId = classLessionPlan.getId();

        // Get all the subjectChapterList where classLessionPlan equals to classLessionPlanId
        defaultSubjectChapterShouldBeFound("classLessionPlanId.equals=" + classLessionPlanId);

        // Get all the subjectChapterList where classLessionPlan equals to (classLessionPlanId + 1)
        defaultSubjectChapterShouldNotBeFound("classLessionPlanId.equals=" + (classLessionPlanId + 1));
    }

    @Test
    @Transactional
    void getAllSubjectChaptersByClassSubjectIsEqualToSomething() throws Exception {
        // Initialize the database
        subjectChapterRepository.saveAndFlush(subjectChapter);
        ClassSubject classSubject = ClassSubjectResourceIT.createEntity(em);
        em.persist(classSubject);
        em.flush();
        subjectChapter.setClassSubject(classSubject);
        subjectChapterRepository.saveAndFlush(subjectChapter);
        Long classSubjectId = classSubject.getId();

        // Get all the subjectChapterList where classSubject equals to classSubjectId
        defaultSubjectChapterShouldBeFound("classSubjectId.equals=" + classSubjectId);

        // Get all the subjectChapterList where classSubject equals to (classSubjectId + 1)
        defaultSubjectChapterShouldNotBeFound("classSubjectId.equals=" + (classSubjectId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSubjectChapterShouldBeFound(String filter) throws Exception {
        restSubjectChapterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subjectChapter.getId().intValue())))
            .andExpect(jsonPath("$.[*].chapterName").value(hasItem(DEFAULT_CHAPTER_NAME)))
            .andExpect(jsonPath("$.[*].chapterNumber").value(hasItem(DEFAULT_CHAPTER_NUMBER)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].cancelDate").value(hasItem(DEFAULT_CANCEL_DATE.toString())));

        // Check, that the count call also returns 1
        restSubjectChapterMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSubjectChapterShouldNotBeFound(String filter) throws Exception {
        restSubjectChapterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSubjectChapterMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSubjectChapter() throws Exception {
        // Get the subjectChapter
        restSubjectChapterMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSubjectChapter() throws Exception {
        // Initialize the database
        subjectChapterRepository.saveAndFlush(subjectChapter);

        int databaseSizeBeforeUpdate = subjectChapterRepository.findAll().size();

        // Update the subjectChapter
        SubjectChapter updatedSubjectChapter = subjectChapterRepository.findById(subjectChapter.getId()).get();
        // Disconnect from session so that the updates on updatedSubjectChapter are not directly saved in db
        em.detach(updatedSubjectChapter);
        updatedSubjectChapter
            .chapterName(UPDATED_CHAPTER_NAME)
            .chapterNumber(UPDATED_CHAPTER_NUMBER)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);
        SubjectChapterDTO subjectChapterDTO = subjectChapterMapper.toDto(updatedSubjectChapter);

        restSubjectChapterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, subjectChapterDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subjectChapterDTO))
            )
            .andExpect(status().isOk());

        // Validate the SubjectChapter in the database
        List<SubjectChapter> subjectChapterList = subjectChapterRepository.findAll();
        assertThat(subjectChapterList).hasSize(databaseSizeBeforeUpdate);
        SubjectChapter testSubjectChapter = subjectChapterList.get(subjectChapterList.size() - 1);
        assertThat(testSubjectChapter.getChapterName()).isEqualTo(UPDATED_CHAPTER_NAME);
        assertThat(testSubjectChapter.getChapterNumber()).isEqualTo(UPDATED_CHAPTER_NUMBER);
        assertThat(testSubjectChapter.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testSubjectChapter.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testSubjectChapter.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void putNonExistingSubjectChapter() throws Exception {
        int databaseSizeBeforeUpdate = subjectChapterRepository.findAll().size();
        subjectChapter.setId(count.incrementAndGet());

        // Create the SubjectChapter
        SubjectChapterDTO subjectChapterDTO = subjectChapterMapper.toDto(subjectChapter);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubjectChapterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, subjectChapterDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subjectChapterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubjectChapter in the database
        List<SubjectChapter> subjectChapterList = subjectChapterRepository.findAll();
        assertThat(subjectChapterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSubjectChapter() throws Exception {
        int databaseSizeBeforeUpdate = subjectChapterRepository.findAll().size();
        subjectChapter.setId(count.incrementAndGet());

        // Create the SubjectChapter
        SubjectChapterDTO subjectChapterDTO = subjectChapterMapper.toDto(subjectChapter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubjectChapterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subjectChapterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubjectChapter in the database
        List<SubjectChapter> subjectChapterList = subjectChapterRepository.findAll();
        assertThat(subjectChapterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSubjectChapter() throws Exception {
        int databaseSizeBeforeUpdate = subjectChapterRepository.findAll().size();
        subjectChapter.setId(count.incrementAndGet());

        // Create the SubjectChapter
        SubjectChapterDTO subjectChapterDTO = subjectChapterMapper.toDto(subjectChapter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubjectChapterMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subjectChapterDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SubjectChapter in the database
        List<SubjectChapter> subjectChapterList = subjectChapterRepository.findAll();
        assertThat(subjectChapterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSubjectChapterWithPatch() throws Exception {
        // Initialize the database
        subjectChapterRepository.saveAndFlush(subjectChapter);

        int databaseSizeBeforeUpdate = subjectChapterRepository.findAll().size();

        // Update the subjectChapter using partial update
        SubjectChapter partialUpdatedSubjectChapter = new SubjectChapter();
        partialUpdatedSubjectChapter.setId(subjectChapter.getId());

        partialUpdatedSubjectChapter
            .chapterNumber(UPDATED_CHAPTER_NUMBER)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);

        restSubjectChapterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubjectChapter.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSubjectChapter))
            )
            .andExpect(status().isOk());

        // Validate the SubjectChapter in the database
        List<SubjectChapter> subjectChapterList = subjectChapterRepository.findAll();
        assertThat(subjectChapterList).hasSize(databaseSizeBeforeUpdate);
        SubjectChapter testSubjectChapter = subjectChapterList.get(subjectChapterList.size() - 1);
        assertThat(testSubjectChapter.getChapterName()).isEqualTo(DEFAULT_CHAPTER_NAME);
        assertThat(testSubjectChapter.getChapterNumber()).isEqualTo(UPDATED_CHAPTER_NUMBER);
        assertThat(testSubjectChapter.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testSubjectChapter.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testSubjectChapter.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void fullUpdateSubjectChapterWithPatch() throws Exception {
        // Initialize the database
        subjectChapterRepository.saveAndFlush(subjectChapter);

        int databaseSizeBeforeUpdate = subjectChapterRepository.findAll().size();

        // Update the subjectChapter using partial update
        SubjectChapter partialUpdatedSubjectChapter = new SubjectChapter();
        partialUpdatedSubjectChapter.setId(subjectChapter.getId());

        partialUpdatedSubjectChapter
            .chapterName(UPDATED_CHAPTER_NAME)
            .chapterNumber(UPDATED_CHAPTER_NUMBER)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);

        restSubjectChapterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubjectChapter.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSubjectChapter))
            )
            .andExpect(status().isOk());

        // Validate the SubjectChapter in the database
        List<SubjectChapter> subjectChapterList = subjectChapterRepository.findAll();
        assertThat(subjectChapterList).hasSize(databaseSizeBeforeUpdate);
        SubjectChapter testSubjectChapter = subjectChapterList.get(subjectChapterList.size() - 1);
        assertThat(testSubjectChapter.getChapterName()).isEqualTo(UPDATED_CHAPTER_NAME);
        assertThat(testSubjectChapter.getChapterNumber()).isEqualTo(UPDATED_CHAPTER_NUMBER);
        assertThat(testSubjectChapter.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testSubjectChapter.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testSubjectChapter.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingSubjectChapter() throws Exception {
        int databaseSizeBeforeUpdate = subjectChapterRepository.findAll().size();
        subjectChapter.setId(count.incrementAndGet());

        // Create the SubjectChapter
        SubjectChapterDTO subjectChapterDTO = subjectChapterMapper.toDto(subjectChapter);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubjectChapterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, subjectChapterDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(subjectChapterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubjectChapter in the database
        List<SubjectChapter> subjectChapterList = subjectChapterRepository.findAll();
        assertThat(subjectChapterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSubjectChapter() throws Exception {
        int databaseSizeBeforeUpdate = subjectChapterRepository.findAll().size();
        subjectChapter.setId(count.incrementAndGet());

        // Create the SubjectChapter
        SubjectChapterDTO subjectChapterDTO = subjectChapterMapper.toDto(subjectChapter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubjectChapterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(subjectChapterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubjectChapter in the database
        List<SubjectChapter> subjectChapterList = subjectChapterRepository.findAll();
        assertThat(subjectChapterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSubjectChapter() throws Exception {
        int databaseSizeBeforeUpdate = subjectChapterRepository.findAll().size();
        subjectChapter.setId(count.incrementAndGet());

        // Create the SubjectChapter
        SubjectChapterDTO subjectChapterDTO = subjectChapterMapper.toDto(subjectChapter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubjectChapterMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(subjectChapterDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SubjectChapter in the database
        List<SubjectChapter> subjectChapterList = subjectChapterRepository.findAll();
        assertThat(subjectChapterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSubjectChapter() throws Exception {
        // Initialize the database
        subjectChapterRepository.saveAndFlush(subjectChapter);

        int databaseSizeBeforeDelete = subjectChapterRepository.findAll().size();

        // Delete the subjectChapter
        restSubjectChapterMockMvc
            .perform(delete(ENTITY_API_URL_ID, subjectChapter.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SubjectChapter> subjectChapterList = subjectChapterRepository.findAll();
        assertThat(subjectChapterList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
