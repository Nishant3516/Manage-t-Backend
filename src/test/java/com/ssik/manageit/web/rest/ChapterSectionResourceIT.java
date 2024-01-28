package com.ssik.manageit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ssik.manageit.IntegrationTest;
import com.ssik.manageit.domain.ChapterSection;
import com.ssik.manageit.domain.ClassClassWork;
import com.ssik.manageit.domain.ClassHomeWork;
import com.ssik.manageit.domain.ClassLessionPlan;
import com.ssik.manageit.domain.SubjectChapter;
import com.ssik.manageit.repository.ChapterSectionRepository;
import com.ssik.manageit.service.criteria.ChapterSectionCriteria;
import com.ssik.manageit.service.dto.ChapterSectionDTO;
import com.ssik.manageit.service.mapper.ChapterSectionMapper;
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
 * Integration tests for the {@link ChapterSectionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ChapterSectionResourceIT {

    private static final String DEFAULT_SECTION_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SECTION_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SECTION_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_SECTION_NUMBER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATE_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CREATE_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_LAST_MODIFIED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_MODIFIED = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_LAST_MODIFIED = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_CANCEL_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CANCEL_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CANCEL_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/chapter-sections";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ChapterSectionRepository chapterSectionRepository;

    @Autowired
    private ChapterSectionMapper chapterSectionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restChapterSectionMockMvc;

    private ChapterSection chapterSection;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChapterSection createEntity(EntityManager em) {
        ChapterSection chapterSection = new ChapterSection()
            .sectionName(DEFAULT_SECTION_NAME)
            .sectionNumber(DEFAULT_SECTION_NUMBER)
            .createDate(DEFAULT_CREATE_DATE)
            .lastModified(DEFAULT_LAST_MODIFIED)
            .cancelDate(DEFAULT_CANCEL_DATE);
        return chapterSection;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChapterSection createUpdatedEntity(EntityManager em) {
        ChapterSection chapterSection = new ChapterSection()
            .sectionName(UPDATED_SECTION_NAME)
            .sectionNumber(UPDATED_SECTION_NUMBER)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);
        return chapterSection;
    }

    @BeforeEach
    public void initTest() {
        chapterSection = createEntity(em);
    }

    @Test
    @Transactional
    void createChapterSection() throws Exception {
        int databaseSizeBeforeCreate = chapterSectionRepository.findAll().size();
        // Create the ChapterSection
        ChapterSectionDTO chapterSectionDTO = chapterSectionMapper.toDto(chapterSection);
        restChapterSectionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(chapterSectionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ChapterSection in the database
        List<ChapterSection> chapterSectionList = chapterSectionRepository.findAll();
        assertThat(chapterSectionList).hasSize(databaseSizeBeforeCreate + 1);
        ChapterSection testChapterSection = chapterSectionList.get(chapterSectionList.size() - 1);
        assertThat(testChapterSection.getSectionName()).isEqualTo(DEFAULT_SECTION_NAME);
        assertThat(testChapterSection.getSectionNumber()).isEqualTo(DEFAULT_SECTION_NUMBER);
        assertThat(testChapterSection.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testChapterSection.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
        assertThat(testChapterSection.getCancelDate()).isEqualTo(DEFAULT_CANCEL_DATE);
    }

    @Test
    @Transactional
    void createChapterSectionWithExistingId() throws Exception {
        // Create the ChapterSection with an existing ID
        chapterSection.setId(1L);
        ChapterSectionDTO chapterSectionDTO = chapterSectionMapper.toDto(chapterSection);

        int databaseSizeBeforeCreate = chapterSectionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restChapterSectionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(chapterSectionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChapterSection in the database
        List<ChapterSection> chapterSectionList = chapterSectionRepository.findAll();
        assertThat(chapterSectionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSectionNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = chapterSectionRepository.findAll().size();
        // set the field null
        chapterSection.setSectionName(null);

        // Create the ChapterSection, which fails.
        ChapterSectionDTO chapterSectionDTO = chapterSectionMapper.toDto(chapterSection);

        restChapterSectionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(chapterSectionDTO))
            )
            .andExpect(status().isBadRequest());

        List<ChapterSection> chapterSectionList = chapterSectionRepository.findAll();
        assertThat(chapterSectionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllChapterSections() throws Exception {
        // Initialize the database
        chapterSectionRepository.saveAndFlush(chapterSection);

        // Get all the chapterSectionList
        restChapterSectionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chapterSection.getId().intValue())))
            .andExpect(jsonPath("$.[*].sectionName").value(hasItem(DEFAULT_SECTION_NAME)))
            .andExpect(jsonPath("$.[*].sectionNumber").value(hasItem(DEFAULT_SECTION_NUMBER)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].cancelDate").value(hasItem(DEFAULT_CANCEL_DATE.toString())));
    }

    @Test
    @Transactional
    void getChapterSection() throws Exception {
        // Initialize the database
        chapterSectionRepository.saveAndFlush(chapterSection);

        // Get the chapterSection
        restChapterSectionMockMvc
            .perform(get(ENTITY_API_URL_ID, chapterSection.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(chapterSection.getId().intValue()))
            .andExpect(jsonPath("$.sectionName").value(DEFAULT_SECTION_NAME))
            .andExpect(jsonPath("$.sectionNumber").value(DEFAULT_SECTION_NUMBER))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.lastModified").value(DEFAULT_LAST_MODIFIED.toString()))
            .andExpect(jsonPath("$.cancelDate").value(DEFAULT_CANCEL_DATE.toString()));
    }

    @Test
    @Transactional
    void getChapterSectionsByIdFiltering() throws Exception {
        // Initialize the database
        chapterSectionRepository.saveAndFlush(chapterSection);

        Long id = chapterSection.getId();

        defaultChapterSectionShouldBeFound("id.equals=" + id);
        defaultChapterSectionShouldNotBeFound("id.notEquals=" + id);

        defaultChapterSectionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultChapterSectionShouldNotBeFound("id.greaterThan=" + id);

        defaultChapterSectionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultChapterSectionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllChapterSectionsBySectionNameIsEqualToSomething() throws Exception {
        // Initialize the database
        chapterSectionRepository.saveAndFlush(chapterSection);

        // Get all the chapterSectionList where sectionName equals to DEFAULT_SECTION_NAME
        defaultChapterSectionShouldBeFound("sectionName.equals=" + DEFAULT_SECTION_NAME);

        // Get all the chapterSectionList where sectionName equals to UPDATED_SECTION_NAME
        defaultChapterSectionShouldNotBeFound("sectionName.equals=" + UPDATED_SECTION_NAME);
    }

    @Test
    @Transactional
    void getAllChapterSectionsBySectionNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        chapterSectionRepository.saveAndFlush(chapterSection);

        // Get all the chapterSectionList where sectionName not equals to DEFAULT_SECTION_NAME
        defaultChapterSectionShouldNotBeFound("sectionName.notEquals=" + DEFAULT_SECTION_NAME);

        // Get all the chapterSectionList where sectionName not equals to UPDATED_SECTION_NAME
        defaultChapterSectionShouldBeFound("sectionName.notEquals=" + UPDATED_SECTION_NAME);
    }

    @Test
    @Transactional
    void getAllChapterSectionsBySectionNameIsInShouldWork() throws Exception {
        // Initialize the database
        chapterSectionRepository.saveAndFlush(chapterSection);

        // Get all the chapterSectionList where sectionName in DEFAULT_SECTION_NAME or UPDATED_SECTION_NAME
        defaultChapterSectionShouldBeFound("sectionName.in=" + DEFAULT_SECTION_NAME + "," + UPDATED_SECTION_NAME);

        // Get all the chapterSectionList where sectionName equals to UPDATED_SECTION_NAME
        defaultChapterSectionShouldNotBeFound("sectionName.in=" + UPDATED_SECTION_NAME);
    }

    @Test
    @Transactional
    void getAllChapterSectionsBySectionNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        chapterSectionRepository.saveAndFlush(chapterSection);

        // Get all the chapterSectionList where sectionName is not null
        defaultChapterSectionShouldBeFound("sectionName.specified=true");

        // Get all the chapterSectionList where sectionName is null
        defaultChapterSectionShouldNotBeFound("sectionName.specified=false");
    }

    @Test
    @Transactional
    void getAllChapterSectionsBySectionNameContainsSomething() throws Exception {
        // Initialize the database
        chapterSectionRepository.saveAndFlush(chapterSection);

        // Get all the chapterSectionList where sectionName contains DEFAULT_SECTION_NAME
        defaultChapterSectionShouldBeFound("sectionName.contains=" + DEFAULT_SECTION_NAME);

        // Get all the chapterSectionList where sectionName contains UPDATED_SECTION_NAME
        defaultChapterSectionShouldNotBeFound("sectionName.contains=" + UPDATED_SECTION_NAME);
    }

    @Test
    @Transactional
    void getAllChapterSectionsBySectionNameNotContainsSomething() throws Exception {
        // Initialize the database
        chapterSectionRepository.saveAndFlush(chapterSection);

        // Get all the chapterSectionList where sectionName does not contain DEFAULT_SECTION_NAME
        defaultChapterSectionShouldNotBeFound("sectionName.doesNotContain=" + DEFAULT_SECTION_NAME);

        // Get all the chapterSectionList where sectionName does not contain UPDATED_SECTION_NAME
        defaultChapterSectionShouldBeFound("sectionName.doesNotContain=" + UPDATED_SECTION_NAME);
    }

    @Test
    @Transactional
    void getAllChapterSectionsBySectionNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        chapterSectionRepository.saveAndFlush(chapterSection);

        // Get all the chapterSectionList where sectionNumber equals to DEFAULT_SECTION_NUMBER
        defaultChapterSectionShouldBeFound("sectionNumber.equals=" + DEFAULT_SECTION_NUMBER);

        // Get all the chapterSectionList where sectionNumber equals to UPDATED_SECTION_NUMBER
        defaultChapterSectionShouldNotBeFound("sectionNumber.equals=" + UPDATED_SECTION_NUMBER);
    }

    @Test
    @Transactional
    void getAllChapterSectionsBySectionNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        chapterSectionRepository.saveAndFlush(chapterSection);

        // Get all the chapterSectionList where sectionNumber not equals to DEFAULT_SECTION_NUMBER
        defaultChapterSectionShouldNotBeFound("sectionNumber.notEquals=" + DEFAULT_SECTION_NUMBER);

        // Get all the chapterSectionList where sectionNumber not equals to UPDATED_SECTION_NUMBER
        defaultChapterSectionShouldBeFound("sectionNumber.notEquals=" + UPDATED_SECTION_NUMBER);
    }

    @Test
    @Transactional
    void getAllChapterSectionsBySectionNumberIsInShouldWork() throws Exception {
        // Initialize the database
        chapterSectionRepository.saveAndFlush(chapterSection);

        // Get all the chapterSectionList where sectionNumber in DEFAULT_SECTION_NUMBER or UPDATED_SECTION_NUMBER
        defaultChapterSectionShouldBeFound("sectionNumber.in=" + DEFAULT_SECTION_NUMBER + "," + UPDATED_SECTION_NUMBER);

        // Get all the chapterSectionList where sectionNumber equals to UPDATED_SECTION_NUMBER
        defaultChapterSectionShouldNotBeFound("sectionNumber.in=" + UPDATED_SECTION_NUMBER);
    }

    @Test
    @Transactional
    void getAllChapterSectionsBySectionNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        chapterSectionRepository.saveAndFlush(chapterSection);

        // Get all the chapterSectionList where sectionNumber is not null
        defaultChapterSectionShouldBeFound("sectionNumber.specified=true");

        // Get all the chapterSectionList where sectionNumber is null
        defaultChapterSectionShouldNotBeFound("sectionNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllChapterSectionsBySectionNumberContainsSomething() throws Exception {
        // Initialize the database
        chapterSectionRepository.saveAndFlush(chapterSection);

        // Get all the chapterSectionList where sectionNumber contains DEFAULT_SECTION_NUMBER
        defaultChapterSectionShouldBeFound("sectionNumber.contains=" + DEFAULT_SECTION_NUMBER);

        // Get all the chapterSectionList where sectionNumber contains UPDATED_SECTION_NUMBER
        defaultChapterSectionShouldNotBeFound("sectionNumber.contains=" + UPDATED_SECTION_NUMBER);
    }

    @Test
    @Transactional
    void getAllChapterSectionsBySectionNumberNotContainsSomething() throws Exception {
        // Initialize the database
        chapterSectionRepository.saveAndFlush(chapterSection);

        // Get all the chapterSectionList where sectionNumber does not contain DEFAULT_SECTION_NUMBER
        defaultChapterSectionShouldNotBeFound("sectionNumber.doesNotContain=" + DEFAULT_SECTION_NUMBER);

        // Get all the chapterSectionList where sectionNumber does not contain UPDATED_SECTION_NUMBER
        defaultChapterSectionShouldBeFound("sectionNumber.doesNotContain=" + UPDATED_SECTION_NUMBER);
    }

    @Test
    @Transactional
    void getAllChapterSectionsByCreateDateIsEqualToSomething() throws Exception {
        // Initialize the database
        chapterSectionRepository.saveAndFlush(chapterSection);

        // Get all the chapterSectionList where createDate equals to DEFAULT_CREATE_DATE
        defaultChapterSectionShouldBeFound("createDate.equals=" + DEFAULT_CREATE_DATE);

        // Get all the chapterSectionList where createDate equals to UPDATED_CREATE_DATE
        defaultChapterSectionShouldNotBeFound("createDate.equals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllChapterSectionsByCreateDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        chapterSectionRepository.saveAndFlush(chapterSection);

        // Get all the chapterSectionList where createDate not equals to DEFAULT_CREATE_DATE
        defaultChapterSectionShouldNotBeFound("createDate.notEquals=" + DEFAULT_CREATE_DATE);

        // Get all the chapterSectionList where createDate not equals to UPDATED_CREATE_DATE
        defaultChapterSectionShouldBeFound("createDate.notEquals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllChapterSectionsByCreateDateIsInShouldWork() throws Exception {
        // Initialize the database
        chapterSectionRepository.saveAndFlush(chapterSection);

        // Get all the chapterSectionList where createDate in DEFAULT_CREATE_DATE or UPDATED_CREATE_DATE
        defaultChapterSectionShouldBeFound("createDate.in=" + DEFAULT_CREATE_DATE + "," + UPDATED_CREATE_DATE);

        // Get all the chapterSectionList where createDate equals to UPDATED_CREATE_DATE
        defaultChapterSectionShouldNotBeFound("createDate.in=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllChapterSectionsByCreateDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        chapterSectionRepository.saveAndFlush(chapterSection);

        // Get all the chapterSectionList where createDate is not null
        defaultChapterSectionShouldBeFound("createDate.specified=true");

        // Get all the chapterSectionList where createDate is null
        defaultChapterSectionShouldNotBeFound("createDate.specified=false");
    }

    @Test
    @Transactional
    void getAllChapterSectionsByCreateDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        chapterSectionRepository.saveAndFlush(chapterSection);

        // Get all the chapterSectionList where createDate is greater than or equal to DEFAULT_CREATE_DATE
        defaultChapterSectionShouldBeFound("createDate.greaterThanOrEqual=" + DEFAULT_CREATE_DATE);

        // Get all the chapterSectionList where createDate is greater than or equal to UPDATED_CREATE_DATE
        defaultChapterSectionShouldNotBeFound("createDate.greaterThanOrEqual=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllChapterSectionsByCreateDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        chapterSectionRepository.saveAndFlush(chapterSection);

        // Get all the chapterSectionList where createDate is less than or equal to DEFAULT_CREATE_DATE
        defaultChapterSectionShouldBeFound("createDate.lessThanOrEqual=" + DEFAULT_CREATE_DATE);

        // Get all the chapterSectionList where createDate is less than or equal to SMALLER_CREATE_DATE
        defaultChapterSectionShouldNotBeFound("createDate.lessThanOrEqual=" + SMALLER_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllChapterSectionsByCreateDateIsLessThanSomething() throws Exception {
        // Initialize the database
        chapterSectionRepository.saveAndFlush(chapterSection);

        // Get all the chapterSectionList where createDate is less than DEFAULT_CREATE_DATE
        defaultChapterSectionShouldNotBeFound("createDate.lessThan=" + DEFAULT_CREATE_DATE);

        // Get all the chapterSectionList where createDate is less than UPDATED_CREATE_DATE
        defaultChapterSectionShouldBeFound("createDate.lessThan=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllChapterSectionsByCreateDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        chapterSectionRepository.saveAndFlush(chapterSection);

        // Get all the chapterSectionList where createDate is greater than DEFAULT_CREATE_DATE
        defaultChapterSectionShouldNotBeFound("createDate.greaterThan=" + DEFAULT_CREATE_DATE);

        // Get all the chapterSectionList where createDate is greater than SMALLER_CREATE_DATE
        defaultChapterSectionShouldBeFound("createDate.greaterThan=" + SMALLER_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllChapterSectionsByLastModifiedIsEqualToSomething() throws Exception {
        // Initialize the database
        chapterSectionRepository.saveAndFlush(chapterSection);

        // Get all the chapterSectionList where lastModified equals to DEFAULT_LAST_MODIFIED
        defaultChapterSectionShouldBeFound("lastModified.equals=" + DEFAULT_LAST_MODIFIED);

        // Get all the chapterSectionList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultChapterSectionShouldNotBeFound("lastModified.equals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllChapterSectionsByLastModifiedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        chapterSectionRepository.saveAndFlush(chapterSection);

        // Get all the chapterSectionList where lastModified not equals to DEFAULT_LAST_MODIFIED
        defaultChapterSectionShouldNotBeFound("lastModified.notEquals=" + DEFAULT_LAST_MODIFIED);

        // Get all the chapterSectionList where lastModified not equals to UPDATED_LAST_MODIFIED
        defaultChapterSectionShouldBeFound("lastModified.notEquals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllChapterSectionsByLastModifiedIsInShouldWork() throws Exception {
        // Initialize the database
        chapterSectionRepository.saveAndFlush(chapterSection);

        // Get all the chapterSectionList where lastModified in DEFAULT_LAST_MODIFIED or UPDATED_LAST_MODIFIED
        defaultChapterSectionShouldBeFound("lastModified.in=" + DEFAULT_LAST_MODIFIED + "," + UPDATED_LAST_MODIFIED);

        // Get all the chapterSectionList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultChapterSectionShouldNotBeFound("lastModified.in=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllChapterSectionsByLastModifiedIsNullOrNotNull() throws Exception {
        // Initialize the database
        chapterSectionRepository.saveAndFlush(chapterSection);

        // Get all the chapterSectionList where lastModified is not null
        defaultChapterSectionShouldBeFound("lastModified.specified=true");

        // Get all the chapterSectionList where lastModified is null
        defaultChapterSectionShouldNotBeFound("lastModified.specified=false");
    }

    @Test
    @Transactional
    void getAllChapterSectionsByLastModifiedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        chapterSectionRepository.saveAndFlush(chapterSection);

        // Get all the chapterSectionList where lastModified is greater than or equal to DEFAULT_LAST_MODIFIED
        defaultChapterSectionShouldBeFound("lastModified.greaterThanOrEqual=" + DEFAULT_LAST_MODIFIED);

        // Get all the chapterSectionList where lastModified is greater than or equal to UPDATED_LAST_MODIFIED
        defaultChapterSectionShouldNotBeFound("lastModified.greaterThanOrEqual=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllChapterSectionsByLastModifiedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        chapterSectionRepository.saveAndFlush(chapterSection);

        // Get all the chapterSectionList where lastModified is less than or equal to DEFAULT_LAST_MODIFIED
        defaultChapterSectionShouldBeFound("lastModified.lessThanOrEqual=" + DEFAULT_LAST_MODIFIED);

        // Get all the chapterSectionList where lastModified is less than or equal to SMALLER_LAST_MODIFIED
        defaultChapterSectionShouldNotBeFound("lastModified.lessThanOrEqual=" + SMALLER_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllChapterSectionsByLastModifiedIsLessThanSomething() throws Exception {
        // Initialize the database
        chapterSectionRepository.saveAndFlush(chapterSection);

        // Get all the chapterSectionList where lastModified is less than DEFAULT_LAST_MODIFIED
        defaultChapterSectionShouldNotBeFound("lastModified.lessThan=" + DEFAULT_LAST_MODIFIED);

        // Get all the chapterSectionList where lastModified is less than UPDATED_LAST_MODIFIED
        defaultChapterSectionShouldBeFound("lastModified.lessThan=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllChapterSectionsByLastModifiedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        chapterSectionRepository.saveAndFlush(chapterSection);

        // Get all the chapterSectionList where lastModified is greater than DEFAULT_LAST_MODIFIED
        defaultChapterSectionShouldNotBeFound("lastModified.greaterThan=" + DEFAULT_LAST_MODIFIED);

        // Get all the chapterSectionList where lastModified is greater than SMALLER_LAST_MODIFIED
        defaultChapterSectionShouldBeFound("lastModified.greaterThan=" + SMALLER_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllChapterSectionsByCancelDateIsEqualToSomething() throws Exception {
        // Initialize the database
        chapterSectionRepository.saveAndFlush(chapterSection);

        // Get all the chapterSectionList where cancelDate equals to DEFAULT_CANCEL_DATE
        defaultChapterSectionShouldBeFound("cancelDate.equals=" + DEFAULT_CANCEL_DATE);

        // Get all the chapterSectionList where cancelDate equals to UPDATED_CANCEL_DATE
        defaultChapterSectionShouldNotBeFound("cancelDate.equals=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllChapterSectionsByCancelDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        chapterSectionRepository.saveAndFlush(chapterSection);

        // Get all the chapterSectionList where cancelDate not equals to DEFAULT_CANCEL_DATE
        defaultChapterSectionShouldNotBeFound("cancelDate.notEquals=" + DEFAULT_CANCEL_DATE);

        // Get all the chapterSectionList where cancelDate not equals to UPDATED_CANCEL_DATE
        defaultChapterSectionShouldBeFound("cancelDate.notEquals=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllChapterSectionsByCancelDateIsInShouldWork() throws Exception {
        // Initialize the database
        chapterSectionRepository.saveAndFlush(chapterSection);

        // Get all the chapterSectionList where cancelDate in DEFAULT_CANCEL_DATE or UPDATED_CANCEL_DATE
        defaultChapterSectionShouldBeFound("cancelDate.in=" + DEFAULT_CANCEL_DATE + "," + UPDATED_CANCEL_DATE);

        // Get all the chapterSectionList where cancelDate equals to UPDATED_CANCEL_DATE
        defaultChapterSectionShouldNotBeFound("cancelDate.in=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllChapterSectionsByCancelDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        chapterSectionRepository.saveAndFlush(chapterSection);

        // Get all the chapterSectionList where cancelDate is not null
        defaultChapterSectionShouldBeFound("cancelDate.specified=true");

        // Get all the chapterSectionList where cancelDate is null
        defaultChapterSectionShouldNotBeFound("cancelDate.specified=false");
    }

    @Test
    @Transactional
    void getAllChapterSectionsByCancelDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        chapterSectionRepository.saveAndFlush(chapterSection);

        // Get all the chapterSectionList where cancelDate is greater than or equal to DEFAULT_CANCEL_DATE
        defaultChapterSectionShouldBeFound("cancelDate.greaterThanOrEqual=" + DEFAULT_CANCEL_DATE);

        // Get all the chapterSectionList where cancelDate is greater than or equal to UPDATED_CANCEL_DATE
        defaultChapterSectionShouldNotBeFound("cancelDate.greaterThanOrEqual=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllChapterSectionsByCancelDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        chapterSectionRepository.saveAndFlush(chapterSection);

        // Get all the chapterSectionList where cancelDate is less than or equal to DEFAULT_CANCEL_DATE
        defaultChapterSectionShouldBeFound("cancelDate.lessThanOrEqual=" + DEFAULT_CANCEL_DATE);

        // Get all the chapterSectionList where cancelDate is less than or equal to SMALLER_CANCEL_DATE
        defaultChapterSectionShouldNotBeFound("cancelDate.lessThanOrEqual=" + SMALLER_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllChapterSectionsByCancelDateIsLessThanSomething() throws Exception {
        // Initialize the database
        chapterSectionRepository.saveAndFlush(chapterSection);

        // Get all the chapterSectionList where cancelDate is less than DEFAULT_CANCEL_DATE
        defaultChapterSectionShouldNotBeFound("cancelDate.lessThan=" + DEFAULT_CANCEL_DATE);

        // Get all the chapterSectionList where cancelDate is less than UPDATED_CANCEL_DATE
        defaultChapterSectionShouldBeFound("cancelDate.lessThan=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllChapterSectionsByCancelDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        chapterSectionRepository.saveAndFlush(chapterSection);

        // Get all the chapterSectionList where cancelDate is greater than DEFAULT_CANCEL_DATE
        defaultChapterSectionShouldNotBeFound("cancelDate.greaterThan=" + DEFAULT_CANCEL_DATE);

        // Get all the chapterSectionList where cancelDate is greater than SMALLER_CANCEL_DATE
        defaultChapterSectionShouldBeFound("cancelDate.greaterThan=" + SMALLER_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllChapterSectionsByClassHomeWorkIsEqualToSomething() throws Exception {
        // Initialize the database
        chapterSectionRepository.saveAndFlush(chapterSection);
        ClassHomeWork classHomeWork = ClassHomeWorkResourceIT.createEntity(em);
        em.persist(classHomeWork);
        em.flush();
        chapterSection.addClassHomeWork(classHomeWork);
        chapterSectionRepository.saveAndFlush(chapterSection);
        Long classHomeWorkId = classHomeWork.getId();

        // Get all the chapterSectionList where classHomeWork equals to classHomeWorkId
        defaultChapterSectionShouldBeFound("classHomeWorkId.equals=" + classHomeWorkId);

        // Get all the chapterSectionList where classHomeWork equals to (classHomeWorkId + 1)
        defaultChapterSectionShouldNotBeFound("classHomeWorkId.equals=" + (classHomeWorkId + 1));
    }

    @Test
    @Transactional
    void getAllChapterSectionsByClassClassWorkIsEqualToSomething() throws Exception {
        // Initialize the database
        chapterSectionRepository.saveAndFlush(chapterSection);
        ClassClassWork classClassWork = ClassClassWorkResourceIT.createEntity(em);
        em.persist(classClassWork);
        em.flush();
        chapterSection.addClassClassWork(classClassWork);
        chapterSectionRepository.saveAndFlush(chapterSection);
        Long classClassWorkId = classClassWork.getId();

        // Get all the chapterSectionList where classClassWork equals to classClassWorkId
        defaultChapterSectionShouldBeFound("classClassWorkId.equals=" + classClassWorkId);

        // Get all the chapterSectionList where classClassWork equals to (classClassWorkId + 1)
        defaultChapterSectionShouldNotBeFound("classClassWorkId.equals=" + (classClassWorkId + 1));
    }

    @Test
    @Transactional
    void getAllChapterSectionsByClassLessionPlanIsEqualToSomething() throws Exception {
        // Initialize the database
        chapterSectionRepository.saveAndFlush(chapterSection);
        ClassLessionPlan classLessionPlan = ClassLessionPlanResourceIT.createEntity(em);
        em.persist(classLessionPlan);
        em.flush();
        chapterSection.addClassLessionPlan(classLessionPlan);
        chapterSectionRepository.saveAndFlush(chapterSection);
        Long classLessionPlanId = classLessionPlan.getId();

        // Get all the chapterSectionList where classLessionPlan equals to classLessionPlanId
        defaultChapterSectionShouldBeFound("classLessionPlanId.equals=" + classLessionPlanId);

        // Get all the chapterSectionList where classLessionPlan equals to (classLessionPlanId + 1)
        defaultChapterSectionShouldNotBeFound("classLessionPlanId.equals=" + (classLessionPlanId + 1));
    }

    @Test
    @Transactional
    void getAllChapterSectionsBySubjectChapterIsEqualToSomething() throws Exception {
        // Initialize the database
        chapterSectionRepository.saveAndFlush(chapterSection);
        SubjectChapter subjectChapter = SubjectChapterResourceIT.createEntity(em);
        em.persist(subjectChapter);
        em.flush();
        chapterSection.setSubjectChapter(subjectChapter);
        chapterSectionRepository.saveAndFlush(chapterSection);
        Long subjectChapterId = subjectChapter.getId();

        // Get all the chapterSectionList where subjectChapter equals to subjectChapterId
        defaultChapterSectionShouldBeFound("subjectChapterId.equals=" + subjectChapterId);

        // Get all the chapterSectionList where subjectChapter equals to (subjectChapterId + 1)
        defaultChapterSectionShouldNotBeFound("subjectChapterId.equals=" + (subjectChapterId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultChapterSectionShouldBeFound(String filter) throws Exception {
        restChapterSectionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chapterSection.getId().intValue())))
            .andExpect(jsonPath("$.[*].sectionName").value(hasItem(DEFAULT_SECTION_NAME)))
            .andExpect(jsonPath("$.[*].sectionNumber").value(hasItem(DEFAULT_SECTION_NUMBER)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].cancelDate").value(hasItem(DEFAULT_CANCEL_DATE.toString())));

        // Check, that the count call also returns 1
        restChapterSectionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultChapterSectionShouldNotBeFound(String filter) throws Exception {
        restChapterSectionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restChapterSectionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingChapterSection() throws Exception {
        // Get the chapterSection
        restChapterSectionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewChapterSection() throws Exception {
        // Initialize the database
        chapterSectionRepository.saveAndFlush(chapterSection);

        int databaseSizeBeforeUpdate = chapterSectionRepository.findAll().size();

        // Update the chapterSection
        ChapterSection updatedChapterSection = chapterSectionRepository.findById(chapterSection.getId()).get();
        // Disconnect from session so that the updates on updatedChapterSection are not directly saved in db
        em.detach(updatedChapterSection);
        updatedChapterSection
            .sectionName(UPDATED_SECTION_NAME)
            .sectionNumber(UPDATED_SECTION_NUMBER)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);
        ChapterSectionDTO chapterSectionDTO = chapterSectionMapper.toDto(updatedChapterSection);

        restChapterSectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, chapterSectionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(chapterSectionDTO))
            )
            .andExpect(status().isOk());

        // Validate the ChapterSection in the database
        List<ChapterSection> chapterSectionList = chapterSectionRepository.findAll();
        assertThat(chapterSectionList).hasSize(databaseSizeBeforeUpdate);
        ChapterSection testChapterSection = chapterSectionList.get(chapterSectionList.size() - 1);
        assertThat(testChapterSection.getSectionName()).isEqualTo(UPDATED_SECTION_NAME);
        assertThat(testChapterSection.getSectionNumber()).isEqualTo(UPDATED_SECTION_NUMBER);
        assertThat(testChapterSection.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testChapterSection.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testChapterSection.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void putNonExistingChapterSection() throws Exception {
        int databaseSizeBeforeUpdate = chapterSectionRepository.findAll().size();
        chapterSection.setId(count.incrementAndGet());

        // Create the ChapterSection
        ChapterSectionDTO chapterSectionDTO = chapterSectionMapper.toDto(chapterSection);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChapterSectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, chapterSectionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(chapterSectionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChapterSection in the database
        List<ChapterSection> chapterSectionList = chapterSectionRepository.findAll();
        assertThat(chapterSectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchChapterSection() throws Exception {
        int databaseSizeBeforeUpdate = chapterSectionRepository.findAll().size();
        chapterSection.setId(count.incrementAndGet());

        // Create the ChapterSection
        ChapterSectionDTO chapterSectionDTO = chapterSectionMapper.toDto(chapterSection);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChapterSectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(chapterSectionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChapterSection in the database
        List<ChapterSection> chapterSectionList = chapterSectionRepository.findAll();
        assertThat(chapterSectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamChapterSection() throws Exception {
        int databaseSizeBeforeUpdate = chapterSectionRepository.findAll().size();
        chapterSection.setId(count.incrementAndGet());

        // Create the ChapterSection
        ChapterSectionDTO chapterSectionDTO = chapterSectionMapper.toDto(chapterSection);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChapterSectionMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(chapterSectionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ChapterSection in the database
        List<ChapterSection> chapterSectionList = chapterSectionRepository.findAll();
        assertThat(chapterSectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateChapterSectionWithPatch() throws Exception {
        // Initialize the database
        chapterSectionRepository.saveAndFlush(chapterSection);

        int databaseSizeBeforeUpdate = chapterSectionRepository.findAll().size();

        // Update the chapterSection using partial update
        ChapterSection partialUpdatedChapterSection = new ChapterSection();
        partialUpdatedChapterSection.setId(chapterSection.getId());

        partialUpdatedChapterSection
            .sectionName(UPDATED_SECTION_NAME)
            .sectionNumber(UPDATED_SECTION_NUMBER)
            .createDate(UPDATED_CREATE_DATE);

        restChapterSectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChapterSection.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedChapterSection))
            )
            .andExpect(status().isOk());

        // Validate the ChapterSection in the database
        List<ChapterSection> chapterSectionList = chapterSectionRepository.findAll();
        assertThat(chapterSectionList).hasSize(databaseSizeBeforeUpdate);
        ChapterSection testChapterSection = chapterSectionList.get(chapterSectionList.size() - 1);
        assertThat(testChapterSection.getSectionName()).isEqualTo(UPDATED_SECTION_NAME);
        assertThat(testChapterSection.getSectionNumber()).isEqualTo(UPDATED_SECTION_NUMBER);
        assertThat(testChapterSection.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testChapterSection.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
        assertThat(testChapterSection.getCancelDate()).isEqualTo(DEFAULT_CANCEL_DATE);
    }

    @Test
    @Transactional
    void fullUpdateChapterSectionWithPatch() throws Exception {
        // Initialize the database
        chapterSectionRepository.saveAndFlush(chapterSection);

        int databaseSizeBeforeUpdate = chapterSectionRepository.findAll().size();

        // Update the chapterSection using partial update
        ChapterSection partialUpdatedChapterSection = new ChapterSection();
        partialUpdatedChapterSection.setId(chapterSection.getId());

        partialUpdatedChapterSection
            .sectionName(UPDATED_SECTION_NAME)
            .sectionNumber(UPDATED_SECTION_NUMBER)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);

        restChapterSectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChapterSection.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedChapterSection))
            )
            .andExpect(status().isOk());

        // Validate the ChapterSection in the database
        List<ChapterSection> chapterSectionList = chapterSectionRepository.findAll();
        assertThat(chapterSectionList).hasSize(databaseSizeBeforeUpdate);
        ChapterSection testChapterSection = chapterSectionList.get(chapterSectionList.size() - 1);
        assertThat(testChapterSection.getSectionName()).isEqualTo(UPDATED_SECTION_NAME);
        assertThat(testChapterSection.getSectionNumber()).isEqualTo(UPDATED_SECTION_NUMBER);
        assertThat(testChapterSection.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testChapterSection.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testChapterSection.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingChapterSection() throws Exception {
        int databaseSizeBeforeUpdate = chapterSectionRepository.findAll().size();
        chapterSection.setId(count.incrementAndGet());

        // Create the ChapterSection
        ChapterSectionDTO chapterSectionDTO = chapterSectionMapper.toDto(chapterSection);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChapterSectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, chapterSectionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(chapterSectionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChapterSection in the database
        List<ChapterSection> chapterSectionList = chapterSectionRepository.findAll();
        assertThat(chapterSectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchChapterSection() throws Exception {
        int databaseSizeBeforeUpdate = chapterSectionRepository.findAll().size();
        chapterSection.setId(count.incrementAndGet());

        // Create the ChapterSection
        ChapterSectionDTO chapterSectionDTO = chapterSectionMapper.toDto(chapterSection);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChapterSectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(chapterSectionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChapterSection in the database
        List<ChapterSection> chapterSectionList = chapterSectionRepository.findAll();
        assertThat(chapterSectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamChapterSection() throws Exception {
        int databaseSizeBeforeUpdate = chapterSectionRepository.findAll().size();
        chapterSection.setId(count.incrementAndGet());

        // Create the ChapterSection
        ChapterSectionDTO chapterSectionDTO = chapterSectionMapper.toDto(chapterSection);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChapterSectionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(chapterSectionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ChapterSection in the database
        List<ChapterSection> chapterSectionList = chapterSectionRepository.findAll();
        assertThat(chapterSectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteChapterSection() throws Exception {
        // Initialize the database
        chapterSectionRepository.saveAndFlush(chapterSection);

        int databaseSizeBeforeDelete = chapterSectionRepository.findAll().size();

        // Delete the chapterSection
        restChapterSectionMockMvc
            .perform(delete(ENTITY_API_URL_ID, chapterSection.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ChapterSection> chapterSectionList = chapterSectionRepository.findAll();
        assertThat(chapterSectionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
