package com.ssik.manageit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ssik.manageit.IntegrationTest;
import com.ssik.manageit.domain.ClassSubject;
import com.ssik.manageit.domain.Question;
import com.ssik.manageit.domain.QuestionPaper;
import com.ssik.manageit.domain.SchoolClass;
import com.ssik.manageit.domain.Tag;
import com.ssik.manageit.domain.Tenant;
import com.ssik.manageit.repository.QuestionPaperRepository;
import com.ssik.manageit.service.QuestionPaperService;
import com.ssik.manageit.service.criteria.QuestionPaperCriteria;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link QuestionPaperResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class QuestionPaperResourceIT {

    private static final byte[] DEFAULT_TENAT_LOGO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_TENAT_LOGO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_TENAT_LOGO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_TENAT_LOGO_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_QUESTION_PAPER_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_QUESTION_PAPER_FILE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_QUESTION_PAPER_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_QUESTION_PAPER_FILE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_QUESTION_PAPER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_QUESTION_PAPER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MAIN_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_MAIN_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_SUB_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_SUB_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_LEFT_SUB_HEADING_1 = "AAAAAAAAAA";
    private static final String UPDATED_LEFT_SUB_HEADING_1 = "BBBBBBBBBB";

    private static final String DEFAULT_LEFT_SUB_HEADING_2 = "AAAAAAAAAA";
    private static final String UPDATED_LEFT_SUB_HEADING_2 = "BBBBBBBBBB";

    private static final String DEFAULT_RIGHT_SUB_HEADING_1 = "AAAAAAAAAA";
    private static final String UPDATED_RIGHT_SUB_HEADING_1 = "BBBBBBBBBB";

    private static final String DEFAULT_RIGHT_SUB_HEADING_2 = "AAAAAAAAAA";
    private static final String UPDATED_RIGHT_SUB_HEADING_2 = "BBBBBBBBBB";

    private static final String DEFAULT_INSTRUCTIONS = "AAAAAAAAAA";
    private static final String UPDATED_INSTRUCTIONS = "BBBBBBBBBB";

    private static final String DEFAULT_FOOTER_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_FOOTER_TEXT = "BBBBBBBBBB";

    private static final Integer DEFAULT_TOTAL_MARKS = 1;
    private static final Integer UPDATED_TOTAL_MARKS = 2;
    private static final Integer SMALLER_TOTAL_MARKS = 1 - 1;

    private static final LocalDate DEFAULT_CREATE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATE_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CREATE_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_LAST_MODIFIED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_MODIFIED = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_LAST_MODIFIED = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_CANCEL_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CANCEL_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CANCEL_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/question-papers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private QuestionPaperRepository questionPaperRepository;

    @Mock
    private QuestionPaperRepository questionPaperRepositoryMock;

    @Mock
    private QuestionPaperService questionPaperServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQuestionPaperMockMvc;

    private QuestionPaper questionPaper;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuestionPaper createEntity(EntityManager em) {
        QuestionPaper questionPaper = new QuestionPaper()
            .tenatLogo(DEFAULT_TENAT_LOGO)
            .tenatLogoContentType(DEFAULT_TENAT_LOGO_CONTENT_TYPE)
            .questionPaperFile(DEFAULT_QUESTION_PAPER_FILE)
            .questionPaperFileContentType(DEFAULT_QUESTION_PAPER_FILE_CONTENT_TYPE)
            .questionPaperName(DEFAULT_QUESTION_PAPER_NAME)
            .mainTitle(DEFAULT_MAIN_TITLE)
            .subTitle(DEFAULT_SUB_TITLE)
            .leftSubHeading1(DEFAULT_LEFT_SUB_HEADING_1)
            .leftSubHeading2(DEFAULT_LEFT_SUB_HEADING_2)
            .rightSubHeading1(DEFAULT_RIGHT_SUB_HEADING_1)
            .rightSubHeading2(DEFAULT_RIGHT_SUB_HEADING_2)
            .instructions(DEFAULT_INSTRUCTIONS)
            .footerText(DEFAULT_FOOTER_TEXT)
            .totalMarks(DEFAULT_TOTAL_MARKS)
            .createDate(DEFAULT_CREATE_DATE)
            .lastModified(DEFAULT_LAST_MODIFIED)
            .cancelDate(DEFAULT_CANCEL_DATE);
        return questionPaper;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuestionPaper createUpdatedEntity(EntityManager em) {
        QuestionPaper questionPaper = new QuestionPaper()
            .tenatLogo(UPDATED_TENAT_LOGO)
            .tenatLogoContentType(UPDATED_TENAT_LOGO_CONTENT_TYPE)
            .questionPaperFile(UPDATED_QUESTION_PAPER_FILE)
            .questionPaperFileContentType(UPDATED_QUESTION_PAPER_FILE_CONTENT_TYPE)
            .questionPaperName(UPDATED_QUESTION_PAPER_NAME)
            .mainTitle(UPDATED_MAIN_TITLE)
            .subTitle(UPDATED_SUB_TITLE)
            .leftSubHeading1(UPDATED_LEFT_SUB_HEADING_1)
            .leftSubHeading2(UPDATED_LEFT_SUB_HEADING_2)
            .rightSubHeading1(UPDATED_RIGHT_SUB_HEADING_1)
            .rightSubHeading2(UPDATED_RIGHT_SUB_HEADING_2)
            .instructions(UPDATED_INSTRUCTIONS)
            .footerText(UPDATED_FOOTER_TEXT)
            .totalMarks(UPDATED_TOTAL_MARKS)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);
        return questionPaper;
    }

    @BeforeEach
    public void initTest() {
        questionPaper = createEntity(em);
    }

    @Test
    @Transactional
    void createQuestionPaper() throws Exception {
        int databaseSizeBeforeCreate = questionPaperRepository.findAll().size();
        // Create the QuestionPaper
        restQuestionPaperMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(questionPaper)))
            .andExpect(status().isCreated());

        // Validate the QuestionPaper in the database
        List<QuestionPaper> questionPaperList = questionPaperRepository.findAll();
        assertThat(questionPaperList).hasSize(databaseSizeBeforeCreate + 1);
        QuestionPaper testQuestionPaper = questionPaperList.get(questionPaperList.size() - 1);
        assertThat(testQuestionPaper.getTenatLogo()).isEqualTo(DEFAULT_TENAT_LOGO);
        assertThat(testQuestionPaper.getTenatLogoContentType()).isEqualTo(DEFAULT_TENAT_LOGO_CONTENT_TYPE);
        assertThat(testQuestionPaper.getQuestionPaperFile()).isEqualTo(DEFAULT_QUESTION_PAPER_FILE);
        assertThat(testQuestionPaper.getQuestionPaperFileContentType()).isEqualTo(DEFAULT_QUESTION_PAPER_FILE_CONTENT_TYPE);
        assertThat(testQuestionPaper.getQuestionPaperName()).isEqualTo(DEFAULT_QUESTION_PAPER_NAME);
        assertThat(testQuestionPaper.getMainTitle()).isEqualTo(DEFAULT_MAIN_TITLE);
        assertThat(testQuestionPaper.getSubTitle()).isEqualTo(DEFAULT_SUB_TITLE);
        assertThat(testQuestionPaper.getLeftSubHeading1()).isEqualTo(DEFAULT_LEFT_SUB_HEADING_1);
        assertThat(testQuestionPaper.getLeftSubHeading2()).isEqualTo(DEFAULT_LEFT_SUB_HEADING_2);
        assertThat(testQuestionPaper.getRightSubHeading1()).isEqualTo(DEFAULT_RIGHT_SUB_HEADING_1);
        assertThat(testQuestionPaper.getRightSubHeading2()).isEqualTo(DEFAULT_RIGHT_SUB_HEADING_2);
        assertThat(testQuestionPaper.getInstructions()).isEqualTo(DEFAULT_INSTRUCTIONS);
        assertThat(testQuestionPaper.getFooterText()).isEqualTo(DEFAULT_FOOTER_TEXT);
        assertThat(testQuestionPaper.getTotalMarks()).isEqualTo(DEFAULT_TOTAL_MARKS);
        assertThat(testQuestionPaper.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testQuestionPaper.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
        assertThat(testQuestionPaper.getCancelDate()).isEqualTo(DEFAULT_CANCEL_DATE);
    }

    @Test
    @Transactional
    void createQuestionPaperWithExistingId() throws Exception {
        // Create the QuestionPaper with an existing ID
        questionPaper.setId(1L);

        int databaseSizeBeforeCreate = questionPaperRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuestionPaperMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(questionPaper)))
            .andExpect(status().isBadRequest());

        // Validate the QuestionPaper in the database
        List<QuestionPaper> questionPaperList = questionPaperRepository.findAll();
        assertThat(questionPaperList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllQuestionPapers() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList
        restQuestionPaperMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(questionPaper.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenatLogoContentType").value(hasItem(DEFAULT_TENAT_LOGO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].tenatLogo").value(hasItem(Base64Utils.encodeToString(DEFAULT_TENAT_LOGO))))
            .andExpect(jsonPath("$.[*].questionPaperFileContentType").value(hasItem(DEFAULT_QUESTION_PAPER_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].questionPaperFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_QUESTION_PAPER_FILE))))
            .andExpect(jsonPath("$.[*].questionPaperName").value(hasItem(DEFAULT_QUESTION_PAPER_NAME)))
            .andExpect(jsonPath("$.[*].mainTitle").value(hasItem(DEFAULT_MAIN_TITLE)))
            .andExpect(jsonPath("$.[*].subTitle").value(hasItem(DEFAULT_SUB_TITLE)))
            .andExpect(jsonPath("$.[*].leftSubHeading1").value(hasItem(DEFAULT_LEFT_SUB_HEADING_1)))
            .andExpect(jsonPath("$.[*].leftSubHeading2").value(hasItem(DEFAULT_LEFT_SUB_HEADING_2)))
            .andExpect(jsonPath("$.[*].rightSubHeading1").value(hasItem(DEFAULT_RIGHT_SUB_HEADING_1)))
            .andExpect(jsonPath("$.[*].rightSubHeading2").value(hasItem(DEFAULT_RIGHT_SUB_HEADING_2)))
            .andExpect(jsonPath("$.[*].instructions").value(hasItem(DEFAULT_INSTRUCTIONS)))
            .andExpect(jsonPath("$.[*].footerText").value(hasItem(DEFAULT_FOOTER_TEXT)))
            .andExpect(jsonPath("$.[*].totalMarks").value(hasItem(DEFAULT_TOTAL_MARKS)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].cancelDate").value(hasItem(DEFAULT_CANCEL_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllQuestionPapersWithEagerRelationshipsIsEnabled() throws Exception {
        when(questionPaperServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restQuestionPaperMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(questionPaperServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllQuestionPapersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(questionPaperServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restQuestionPaperMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(questionPaperServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getQuestionPaper() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get the questionPaper
        restQuestionPaperMockMvc
            .perform(get(ENTITY_API_URL_ID, questionPaper.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(questionPaper.getId().intValue()))
            .andExpect(jsonPath("$.tenatLogoContentType").value(DEFAULT_TENAT_LOGO_CONTENT_TYPE))
            .andExpect(jsonPath("$.tenatLogo").value(Base64Utils.encodeToString(DEFAULT_TENAT_LOGO)))
            .andExpect(jsonPath("$.questionPaperFileContentType").value(DEFAULT_QUESTION_PAPER_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.questionPaperFile").value(Base64Utils.encodeToString(DEFAULT_QUESTION_PAPER_FILE)))
            .andExpect(jsonPath("$.questionPaperName").value(DEFAULT_QUESTION_PAPER_NAME))
            .andExpect(jsonPath("$.mainTitle").value(DEFAULT_MAIN_TITLE))
            .andExpect(jsonPath("$.subTitle").value(DEFAULT_SUB_TITLE))
            .andExpect(jsonPath("$.leftSubHeading1").value(DEFAULT_LEFT_SUB_HEADING_1))
            .andExpect(jsonPath("$.leftSubHeading2").value(DEFAULT_LEFT_SUB_HEADING_2))
            .andExpect(jsonPath("$.rightSubHeading1").value(DEFAULT_RIGHT_SUB_HEADING_1))
            .andExpect(jsonPath("$.rightSubHeading2").value(DEFAULT_RIGHT_SUB_HEADING_2))
            .andExpect(jsonPath("$.instructions").value(DEFAULT_INSTRUCTIONS))
            .andExpect(jsonPath("$.footerText").value(DEFAULT_FOOTER_TEXT))
            .andExpect(jsonPath("$.totalMarks").value(DEFAULT_TOTAL_MARKS))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.lastModified").value(DEFAULT_LAST_MODIFIED.toString()))
            .andExpect(jsonPath("$.cancelDate").value(DEFAULT_CANCEL_DATE.toString()));
    }

    @Test
    @Transactional
    void getQuestionPapersByIdFiltering() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        Long id = questionPaper.getId();

        defaultQuestionPaperShouldBeFound("id.equals=" + id);
        defaultQuestionPaperShouldNotBeFound("id.notEquals=" + id);

        defaultQuestionPaperShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultQuestionPaperShouldNotBeFound("id.greaterThan=" + id);

        defaultQuestionPaperShouldBeFound("id.lessThanOrEqual=" + id);
        defaultQuestionPaperShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllQuestionPapersByQuestionPaperNameIsEqualToSomething() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where questionPaperName equals to DEFAULT_QUESTION_PAPER_NAME
        defaultQuestionPaperShouldBeFound("questionPaperName.equals=" + DEFAULT_QUESTION_PAPER_NAME);

        // Get all the questionPaperList where questionPaperName equals to UPDATED_QUESTION_PAPER_NAME
        defaultQuestionPaperShouldNotBeFound("questionPaperName.equals=" + UPDATED_QUESTION_PAPER_NAME);
    }

    @Test
    @Transactional
    void getAllQuestionPapersByQuestionPaperNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where questionPaperName not equals to DEFAULT_QUESTION_PAPER_NAME
        defaultQuestionPaperShouldNotBeFound("questionPaperName.notEquals=" + DEFAULT_QUESTION_PAPER_NAME);

        // Get all the questionPaperList where questionPaperName not equals to UPDATED_QUESTION_PAPER_NAME
        defaultQuestionPaperShouldBeFound("questionPaperName.notEquals=" + UPDATED_QUESTION_PAPER_NAME);
    }

    @Test
    @Transactional
    void getAllQuestionPapersByQuestionPaperNameIsInShouldWork() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where questionPaperName in DEFAULT_QUESTION_PAPER_NAME or UPDATED_QUESTION_PAPER_NAME
        defaultQuestionPaperShouldBeFound("questionPaperName.in=" + DEFAULT_QUESTION_PAPER_NAME + "," + UPDATED_QUESTION_PAPER_NAME);

        // Get all the questionPaperList where questionPaperName equals to UPDATED_QUESTION_PAPER_NAME
        defaultQuestionPaperShouldNotBeFound("questionPaperName.in=" + UPDATED_QUESTION_PAPER_NAME);
    }

    @Test
    @Transactional
    void getAllQuestionPapersByQuestionPaperNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where questionPaperName is not null
        defaultQuestionPaperShouldBeFound("questionPaperName.specified=true");

        // Get all the questionPaperList where questionPaperName is null
        defaultQuestionPaperShouldNotBeFound("questionPaperName.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionPapersByQuestionPaperNameContainsSomething() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where questionPaperName contains DEFAULT_QUESTION_PAPER_NAME
        defaultQuestionPaperShouldBeFound("questionPaperName.contains=" + DEFAULT_QUESTION_PAPER_NAME);

        // Get all the questionPaperList where questionPaperName contains UPDATED_QUESTION_PAPER_NAME
        defaultQuestionPaperShouldNotBeFound("questionPaperName.contains=" + UPDATED_QUESTION_PAPER_NAME);
    }

    @Test
    @Transactional
    void getAllQuestionPapersByQuestionPaperNameNotContainsSomething() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where questionPaperName does not contain DEFAULT_QUESTION_PAPER_NAME
        defaultQuestionPaperShouldNotBeFound("questionPaperName.doesNotContain=" + DEFAULT_QUESTION_PAPER_NAME);

        // Get all the questionPaperList where questionPaperName does not contain UPDATED_QUESTION_PAPER_NAME
        defaultQuestionPaperShouldBeFound("questionPaperName.doesNotContain=" + UPDATED_QUESTION_PAPER_NAME);
    }

    @Test
    @Transactional
    void getAllQuestionPapersByMainTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where mainTitle equals to DEFAULT_MAIN_TITLE
        defaultQuestionPaperShouldBeFound("mainTitle.equals=" + DEFAULT_MAIN_TITLE);

        // Get all the questionPaperList where mainTitle equals to UPDATED_MAIN_TITLE
        defaultQuestionPaperShouldNotBeFound("mainTitle.equals=" + UPDATED_MAIN_TITLE);
    }

    @Test
    @Transactional
    void getAllQuestionPapersByMainTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where mainTitle not equals to DEFAULT_MAIN_TITLE
        defaultQuestionPaperShouldNotBeFound("mainTitle.notEquals=" + DEFAULT_MAIN_TITLE);

        // Get all the questionPaperList where mainTitle not equals to UPDATED_MAIN_TITLE
        defaultQuestionPaperShouldBeFound("mainTitle.notEquals=" + UPDATED_MAIN_TITLE);
    }

    @Test
    @Transactional
    void getAllQuestionPapersByMainTitleIsInShouldWork() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where mainTitle in DEFAULT_MAIN_TITLE or UPDATED_MAIN_TITLE
        defaultQuestionPaperShouldBeFound("mainTitle.in=" + DEFAULT_MAIN_TITLE + "," + UPDATED_MAIN_TITLE);

        // Get all the questionPaperList where mainTitle equals to UPDATED_MAIN_TITLE
        defaultQuestionPaperShouldNotBeFound("mainTitle.in=" + UPDATED_MAIN_TITLE);
    }

    @Test
    @Transactional
    void getAllQuestionPapersByMainTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where mainTitle is not null
        defaultQuestionPaperShouldBeFound("mainTitle.specified=true");

        // Get all the questionPaperList where mainTitle is null
        defaultQuestionPaperShouldNotBeFound("mainTitle.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionPapersByMainTitleContainsSomething() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where mainTitle contains DEFAULT_MAIN_TITLE
        defaultQuestionPaperShouldBeFound("mainTitle.contains=" + DEFAULT_MAIN_TITLE);

        // Get all the questionPaperList where mainTitle contains UPDATED_MAIN_TITLE
        defaultQuestionPaperShouldNotBeFound("mainTitle.contains=" + UPDATED_MAIN_TITLE);
    }

    @Test
    @Transactional
    void getAllQuestionPapersByMainTitleNotContainsSomething() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where mainTitle does not contain DEFAULT_MAIN_TITLE
        defaultQuestionPaperShouldNotBeFound("mainTitle.doesNotContain=" + DEFAULT_MAIN_TITLE);

        // Get all the questionPaperList where mainTitle does not contain UPDATED_MAIN_TITLE
        defaultQuestionPaperShouldBeFound("mainTitle.doesNotContain=" + UPDATED_MAIN_TITLE);
    }

    @Test
    @Transactional
    void getAllQuestionPapersBySubTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where subTitle equals to DEFAULT_SUB_TITLE
        defaultQuestionPaperShouldBeFound("subTitle.equals=" + DEFAULT_SUB_TITLE);

        // Get all the questionPaperList where subTitle equals to UPDATED_SUB_TITLE
        defaultQuestionPaperShouldNotBeFound("subTitle.equals=" + UPDATED_SUB_TITLE);
    }

    @Test
    @Transactional
    void getAllQuestionPapersBySubTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where subTitle not equals to DEFAULT_SUB_TITLE
        defaultQuestionPaperShouldNotBeFound("subTitle.notEquals=" + DEFAULT_SUB_TITLE);

        // Get all the questionPaperList where subTitle not equals to UPDATED_SUB_TITLE
        defaultQuestionPaperShouldBeFound("subTitle.notEquals=" + UPDATED_SUB_TITLE);
    }

    @Test
    @Transactional
    void getAllQuestionPapersBySubTitleIsInShouldWork() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where subTitle in DEFAULT_SUB_TITLE or UPDATED_SUB_TITLE
        defaultQuestionPaperShouldBeFound("subTitle.in=" + DEFAULT_SUB_TITLE + "," + UPDATED_SUB_TITLE);

        // Get all the questionPaperList where subTitle equals to UPDATED_SUB_TITLE
        defaultQuestionPaperShouldNotBeFound("subTitle.in=" + UPDATED_SUB_TITLE);
    }

    @Test
    @Transactional
    void getAllQuestionPapersBySubTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where subTitle is not null
        defaultQuestionPaperShouldBeFound("subTitle.specified=true");

        // Get all the questionPaperList where subTitle is null
        defaultQuestionPaperShouldNotBeFound("subTitle.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionPapersBySubTitleContainsSomething() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where subTitle contains DEFAULT_SUB_TITLE
        defaultQuestionPaperShouldBeFound("subTitle.contains=" + DEFAULT_SUB_TITLE);

        // Get all the questionPaperList where subTitle contains UPDATED_SUB_TITLE
        defaultQuestionPaperShouldNotBeFound("subTitle.contains=" + UPDATED_SUB_TITLE);
    }

    @Test
    @Transactional
    void getAllQuestionPapersBySubTitleNotContainsSomething() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where subTitle does not contain DEFAULT_SUB_TITLE
        defaultQuestionPaperShouldNotBeFound("subTitle.doesNotContain=" + DEFAULT_SUB_TITLE);

        // Get all the questionPaperList where subTitle does not contain UPDATED_SUB_TITLE
        defaultQuestionPaperShouldBeFound("subTitle.doesNotContain=" + UPDATED_SUB_TITLE);
    }

    @Test
    @Transactional
    void getAllQuestionPapersByLeftSubHeading1IsEqualToSomething() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where leftSubHeading1 equals to DEFAULT_LEFT_SUB_HEADING_1
        defaultQuestionPaperShouldBeFound("leftSubHeading1.equals=" + DEFAULT_LEFT_SUB_HEADING_1);

        // Get all the questionPaperList where leftSubHeading1 equals to UPDATED_LEFT_SUB_HEADING_1
        defaultQuestionPaperShouldNotBeFound("leftSubHeading1.equals=" + UPDATED_LEFT_SUB_HEADING_1);
    }

    @Test
    @Transactional
    void getAllQuestionPapersByLeftSubHeading1IsNotEqualToSomething() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where leftSubHeading1 not equals to DEFAULT_LEFT_SUB_HEADING_1
        defaultQuestionPaperShouldNotBeFound("leftSubHeading1.notEquals=" + DEFAULT_LEFT_SUB_HEADING_1);

        // Get all the questionPaperList where leftSubHeading1 not equals to UPDATED_LEFT_SUB_HEADING_1
        defaultQuestionPaperShouldBeFound("leftSubHeading1.notEquals=" + UPDATED_LEFT_SUB_HEADING_1);
    }

    @Test
    @Transactional
    void getAllQuestionPapersByLeftSubHeading1IsInShouldWork() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where leftSubHeading1 in DEFAULT_LEFT_SUB_HEADING_1 or UPDATED_LEFT_SUB_HEADING_1
        defaultQuestionPaperShouldBeFound("leftSubHeading1.in=" + DEFAULT_LEFT_SUB_HEADING_1 + "," + UPDATED_LEFT_SUB_HEADING_1);

        // Get all the questionPaperList where leftSubHeading1 equals to UPDATED_LEFT_SUB_HEADING_1
        defaultQuestionPaperShouldNotBeFound("leftSubHeading1.in=" + UPDATED_LEFT_SUB_HEADING_1);
    }

    @Test
    @Transactional
    void getAllQuestionPapersByLeftSubHeading1IsNullOrNotNull() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where leftSubHeading1 is not null
        defaultQuestionPaperShouldBeFound("leftSubHeading1.specified=true");

        // Get all the questionPaperList where leftSubHeading1 is null
        defaultQuestionPaperShouldNotBeFound("leftSubHeading1.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionPapersByLeftSubHeading1ContainsSomething() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where leftSubHeading1 contains DEFAULT_LEFT_SUB_HEADING_1
        defaultQuestionPaperShouldBeFound("leftSubHeading1.contains=" + DEFAULT_LEFT_SUB_HEADING_1);

        // Get all the questionPaperList where leftSubHeading1 contains UPDATED_LEFT_SUB_HEADING_1
        defaultQuestionPaperShouldNotBeFound("leftSubHeading1.contains=" + UPDATED_LEFT_SUB_HEADING_1);
    }

    @Test
    @Transactional
    void getAllQuestionPapersByLeftSubHeading1NotContainsSomething() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where leftSubHeading1 does not contain DEFAULT_LEFT_SUB_HEADING_1
        defaultQuestionPaperShouldNotBeFound("leftSubHeading1.doesNotContain=" + DEFAULT_LEFT_SUB_HEADING_1);

        // Get all the questionPaperList where leftSubHeading1 does not contain UPDATED_LEFT_SUB_HEADING_1
        defaultQuestionPaperShouldBeFound("leftSubHeading1.doesNotContain=" + UPDATED_LEFT_SUB_HEADING_1);
    }

    @Test
    @Transactional
    void getAllQuestionPapersByLeftSubHeading2IsEqualToSomething() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where leftSubHeading2 equals to DEFAULT_LEFT_SUB_HEADING_2
        defaultQuestionPaperShouldBeFound("leftSubHeading2.equals=" + DEFAULT_LEFT_SUB_HEADING_2);

        // Get all the questionPaperList where leftSubHeading2 equals to UPDATED_LEFT_SUB_HEADING_2
        defaultQuestionPaperShouldNotBeFound("leftSubHeading2.equals=" + UPDATED_LEFT_SUB_HEADING_2);
    }

    @Test
    @Transactional
    void getAllQuestionPapersByLeftSubHeading2IsNotEqualToSomething() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where leftSubHeading2 not equals to DEFAULT_LEFT_SUB_HEADING_2
        defaultQuestionPaperShouldNotBeFound("leftSubHeading2.notEquals=" + DEFAULT_LEFT_SUB_HEADING_2);

        // Get all the questionPaperList where leftSubHeading2 not equals to UPDATED_LEFT_SUB_HEADING_2
        defaultQuestionPaperShouldBeFound("leftSubHeading2.notEquals=" + UPDATED_LEFT_SUB_HEADING_2);
    }

    @Test
    @Transactional
    void getAllQuestionPapersByLeftSubHeading2IsInShouldWork() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where leftSubHeading2 in DEFAULT_LEFT_SUB_HEADING_2 or UPDATED_LEFT_SUB_HEADING_2
        defaultQuestionPaperShouldBeFound("leftSubHeading2.in=" + DEFAULT_LEFT_SUB_HEADING_2 + "," + UPDATED_LEFT_SUB_HEADING_2);

        // Get all the questionPaperList where leftSubHeading2 equals to UPDATED_LEFT_SUB_HEADING_2
        defaultQuestionPaperShouldNotBeFound("leftSubHeading2.in=" + UPDATED_LEFT_SUB_HEADING_2);
    }

    @Test
    @Transactional
    void getAllQuestionPapersByLeftSubHeading2IsNullOrNotNull() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where leftSubHeading2 is not null
        defaultQuestionPaperShouldBeFound("leftSubHeading2.specified=true");

        // Get all the questionPaperList where leftSubHeading2 is null
        defaultQuestionPaperShouldNotBeFound("leftSubHeading2.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionPapersByLeftSubHeading2ContainsSomething() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where leftSubHeading2 contains DEFAULT_LEFT_SUB_HEADING_2
        defaultQuestionPaperShouldBeFound("leftSubHeading2.contains=" + DEFAULT_LEFT_SUB_HEADING_2);

        // Get all the questionPaperList where leftSubHeading2 contains UPDATED_LEFT_SUB_HEADING_2
        defaultQuestionPaperShouldNotBeFound("leftSubHeading2.contains=" + UPDATED_LEFT_SUB_HEADING_2);
    }

    @Test
    @Transactional
    void getAllQuestionPapersByLeftSubHeading2NotContainsSomething() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where leftSubHeading2 does not contain DEFAULT_LEFT_SUB_HEADING_2
        defaultQuestionPaperShouldNotBeFound("leftSubHeading2.doesNotContain=" + DEFAULT_LEFT_SUB_HEADING_2);

        // Get all the questionPaperList where leftSubHeading2 does not contain UPDATED_LEFT_SUB_HEADING_2
        defaultQuestionPaperShouldBeFound("leftSubHeading2.doesNotContain=" + UPDATED_LEFT_SUB_HEADING_2);
    }

    @Test
    @Transactional
    void getAllQuestionPapersByRightSubHeading1IsEqualToSomething() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where rightSubHeading1 equals to DEFAULT_RIGHT_SUB_HEADING_1
        defaultQuestionPaperShouldBeFound("rightSubHeading1.equals=" + DEFAULT_RIGHT_SUB_HEADING_1);

        // Get all the questionPaperList where rightSubHeading1 equals to UPDATED_RIGHT_SUB_HEADING_1
        defaultQuestionPaperShouldNotBeFound("rightSubHeading1.equals=" + UPDATED_RIGHT_SUB_HEADING_1);
    }

    @Test
    @Transactional
    void getAllQuestionPapersByRightSubHeading1IsNotEqualToSomething() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where rightSubHeading1 not equals to DEFAULT_RIGHT_SUB_HEADING_1
        defaultQuestionPaperShouldNotBeFound("rightSubHeading1.notEquals=" + DEFAULT_RIGHT_SUB_HEADING_1);

        // Get all the questionPaperList where rightSubHeading1 not equals to UPDATED_RIGHT_SUB_HEADING_1
        defaultQuestionPaperShouldBeFound("rightSubHeading1.notEquals=" + UPDATED_RIGHT_SUB_HEADING_1);
    }

    @Test
    @Transactional
    void getAllQuestionPapersByRightSubHeading1IsInShouldWork() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where rightSubHeading1 in DEFAULT_RIGHT_SUB_HEADING_1 or UPDATED_RIGHT_SUB_HEADING_1
        defaultQuestionPaperShouldBeFound("rightSubHeading1.in=" + DEFAULT_RIGHT_SUB_HEADING_1 + "," + UPDATED_RIGHT_SUB_HEADING_1);

        // Get all the questionPaperList where rightSubHeading1 equals to UPDATED_RIGHT_SUB_HEADING_1
        defaultQuestionPaperShouldNotBeFound("rightSubHeading1.in=" + UPDATED_RIGHT_SUB_HEADING_1);
    }

    @Test
    @Transactional
    void getAllQuestionPapersByRightSubHeading1IsNullOrNotNull() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where rightSubHeading1 is not null
        defaultQuestionPaperShouldBeFound("rightSubHeading1.specified=true");

        // Get all the questionPaperList where rightSubHeading1 is null
        defaultQuestionPaperShouldNotBeFound("rightSubHeading1.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionPapersByRightSubHeading1ContainsSomething() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where rightSubHeading1 contains DEFAULT_RIGHT_SUB_HEADING_1
        defaultQuestionPaperShouldBeFound("rightSubHeading1.contains=" + DEFAULT_RIGHT_SUB_HEADING_1);

        // Get all the questionPaperList where rightSubHeading1 contains UPDATED_RIGHT_SUB_HEADING_1
        defaultQuestionPaperShouldNotBeFound("rightSubHeading1.contains=" + UPDATED_RIGHT_SUB_HEADING_1);
    }

    @Test
    @Transactional
    void getAllQuestionPapersByRightSubHeading1NotContainsSomething() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where rightSubHeading1 does not contain DEFAULT_RIGHT_SUB_HEADING_1
        defaultQuestionPaperShouldNotBeFound("rightSubHeading1.doesNotContain=" + DEFAULT_RIGHT_SUB_HEADING_1);

        // Get all the questionPaperList where rightSubHeading1 does not contain UPDATED_RIGHT_SUB_HEADING_1
        defaultQuestionPaperShouldBeFound("rightSubHeading1.doesNotContain=" + UPDATED_RIGHT_SUB_HEADING_1);
    }

    @Test
    @Transactional
    void getAllQuestionPapersByRightSubHeading2IsEqualToSomething() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where rightSubHeading2 equals to DEFAULT_RIGHT_SUB_HEADING_2
        defaultQuestionPaperShouldBeFound("rightSubHeading2.equals=" + DEFAULT_RIGHT_SUB_HEADING_2);

        // Get all the questionPaperList where rightSubHeading2 equals to UPDATED_RIGHT_SUB_HEADING_2
        defaultQuestionPaperShouldNotBeFound("rightSubHeading2.equals=" + UPDATED_RIGHT_SUB_HEADING_2);
    }

    @Test
    @Transactional
    void getAllQuestionPapersByRightSubHeading2IsNotEqualToSomething() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where rightSubHeading2 not equals to DEFAULT_RIGHT_SUB_HEADING_2
        defaultQuestionPaperShouldNotBeFound("rightSubHeading2.notEquals=" + DEFAULT_RIGHT_SUB_HEADING_2);

        // Get all the questionPaperList where rightSubHeading2 not equals to UPDATED_RIGHT_SUB_HEADING_2
        defaultQuestionPaperShouldBeFound("rightSubHeading2.notEquals=" + UPDATED_RIGHT_SUB_HEADING_2);
    }

    @Test
    @Transactional
    void getAllQuestionPapersByRightSubHeading2IsInShouldWork() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where rightSubHeading2 in DEFAULT_RIGHT_SUB_HEADING_2 or UPDATED_RIGHT_SUB_HEADING_2
        defaultQuestionPaperShouldBeFound("rightSubHeading2.in=" + DEFAULT_RIGHT_SUB_HEADING_2 + "," + UPDATED_RIGHT_SUB_HEADING_2);

        // Get all the questionPaperList where rightSubHeading2 equals to UPDATED_RIGHT_SUB_HEADING_2
        defaultQuestionPaperShouldNotBeFound("rightSubHeading2.in=" + UPDATED_RIGHT_SUB_HEADING_2);
    }

    @Test
    @Transactional
    void getAllQuestionPapersByRightSubHeading2IsNullOrNotNull() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where rightSubHeading2 is not null
        defaultQuestionPaperShouldBeFound("rightSubHeading2.specified=true");

        // Get all the questionPaperList where rightSubHeading2 is null
        defaultQuestionPaperShouldNotBeFound("rightSubHeading2.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionPapersByRightSubHeading2ContainsSomething() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where rightSubHeading2 contains DEFAULT_RIGHT_SUB_HEADING_2
        defaultQuestionPaperShouldBeFound("rightSubHeading2.contains=" + DEFAULT_RIGHT_SUB_HEADING_2);

        // Get all the questionPaperList where rightSubHeading2 contains UPDATED_RIGHT_SUB_HEADING_2
        defaultQuestionPaperShouldNotBeFound("rightSubHeading2.contains=" + UPDATED_RIGHT_SUB_HEADING_2);
    }

    @Test
    @Transactional
    void getAllQuestionPapersByRightSubHeading2NotContainsSomething() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where rightSubHeading2 does not contain DEFAULT_RIGHT_SUB_HEADING_2
        defaultQuestionPaperShouldNotBeFound("rightSubHeading2.doesNotContain=" + DEFAULT_RIGHT_SUB_HEADING_2);

        // Get all the questionPaperList where rightSubHeading2 does not contain UPDATED_RIGHT_SUB_HEADING_2
        defaultQuestionPaperShouldBeFound("rightSubHeading2.doesNotContain=" + UPDATED_RIGHT_SUB_HEADING_2);
    }

    @Test
    @Transactional
    void getAllQuestionPapersByInstructionsIsEqualToSomething() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where instructions equals to DEFAULT_INSTRUCTIONS
        defaultQuestionPaperShouldBeFound("instructions.equals=" + DEFAULT_INSTRUCTIONS);

        // Get all the questionPaperList where instructions equals to UPDATED_INSTRUCTIONS
        defaultQuestionPaperShouldNotBeFound("instructions.equals=" + UPDATED_INSTRUCTIONS);
    }

    @Test
    @Transactional
    void getAllQuestionPapersByInstructionsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where instructions not equals to DEFAULT_INSTRUCTIONS
        defaultQuestionPaperShouldNotBeFound("instructions.notEquals=" + DEFAULT_INSTRUCTIONS);

        // Get all the questionPaperList where instructions not equals to UPDATED_INSTRUCTIONS
        defaultQuestionPaperShouldBeFound("instructions.notEquals=" + UPDATED_INSTRUCTIONS);
    }

    @Test
    @Transactional
    void getAllQuestionPapersByInstructionsIsInShouldWork() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where instructions in DEFAULT_INSTRUCTIONS or UPDATED_INSTRUCTIONS
        defaultQuestionPaperShouldBeFound("instructions.in=" + DEFAULT_INSTRUCTIONS + "," + UPDATED_INSTRUCTIONS);

        // Get all the questionPaperList where instructions equals to UPDATED_INSTRUCTIONS
        defaultQuestionPaperShouldNotBeFound("instructions.in=" + UPDATED_INSTRUCTIONS);
    }

    @Test
    @Transactional
    void getAllQuestionPapersByInstructionsIsNullOrNotNull() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where instructions is not null
        defaultQuestionPaperShouldBeFound("instructions.specified=true");

        // Get all the questionPaperList where instructions is null
        defaultQuestionPaperShouldNotBeFound("instructions.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionPapersByInstructionsContainsSomething() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where instructions contains DEFAULT_INSTRUCTIONS
        defaultQuestionPaperShouldBeFound("instructions.contains=" + DEFAULT_INSTRUCTIONS);

        // Get all the questionPaperList where instructions contains UPDATED_INSTRUCTIONS
        defaultQuestionPaperShouldNotBeFound("instructions.contains=" + UPDATED_INSTRUCTIONS);
    }

    @Test
    @Transactional
    void getAllQuestionPapersByInstructionsNotContainsSomething() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where instructions does not contain DEFAULT_INSTRUCTIONS
        defaultQuestionPaperShouldNotBeFound("instructions.doesNotContain=" + DEFAULT_INSTRUCTIONS);

        // Get all the questionPaperList where instructions does not contain UPDATED_INSTRUCTIONS
        defaultQuestionPaperShouldBeFound("instructions.doesNotContain=" + UPDATED_INSTRUCTIONS);
    }

    @Test
    @Transactional
    void getAllQuestionPapersByFooterTextIsEqualToSomething() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where footerText equals to DEFAULT_FOOTER_TEXT
        defaultQuestionPaperShouldBeFound("footerText.equals=" + DEFAULT_FOOTER_TEXT);

        // Get all the questionPaperList where footerText equals to UPDATED_FOOTER_TEXT
        defaultQuestionPaperShouldNotBeFound("footerText.equals=" + UPDATED_FOOTER_TEXT);
    }

    @Test
    @Transactional
    void getAllQuestionPapersByFooterTextIsNotEqualToSomething() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where footerText not equals to DEFAULT_FOOTER_TEXT
        defaultQuestionPaperShouldNotBeFound("footerText.notEquals=" + DEFAULT_FOOTER_TEXT);

        // Get all the questionPaperList where footerText not equals to UPDATED_FOOTER_TEXT
        defaultQuestionPaperShouldBeFound("footerText.notEquals=" + UPDATED_FOOTER_TEXT);
    }

    @Test
    @Transactional
    void getAllQuestionPapersByFooterTextIsInShouldWork() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where footerText in DEFAULT_FOOTER_TEXT or UPDATED_FOOTER_TEXT
        defaultQuestionPaperShouldBeFound("footerText.in=" + DEFAULT_FOOTER_TEXT + "," + UPDATED_FOOTER_TEXT);

        // Get all the questionPaperList where footerText equals to UPDATED_FOOTER_TEXT
        defaultQuestionPaperShouldNotBeFound("footerText.in=" + UPDATED_FOOTER_TEXT);
    }

    @Test
    @Transactional
    void getAllQuestionPapersByFooterTextIsNullOrNotNull() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where footerText is not null
        defaultQuestionPaperShouldBeFound("footerText.specified=true");

        // Get all the questionPaperList where footerText is null
        defaultQuestionPaperShouldNotBeFound("footerText.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionPapersByFooterTextContainsSomething() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where footerText contains DEFAULT_FOOTER_TEXT
        defaultQuestionPaperShouldBeFound("footerText.contains=" + DEFAULT_FOOTER_TEXT);

        // Get all the questionPaperList where footerText contains UPDATED_FOOTER_TEXT
        defaultQuestionPaperShouldNotBeFound("footerText.contains=" + UPDATED_FOOTER_TEXT);
    }

    @Test
    @Transactional
    void getAllQuestionPapersByFooterTextNotContainsSomething() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where footerText does not contain DEFAULT_FOOTER_TEXT
        defaultQuestionPaperShouldNotBeFound("footerText.doesNotContain=" + DEFAULT_FOOTER_TEXT);

        // Get all the questionPaperList where footerText does not contain UPDATED_FOOTER_TEXT
        defaultQuestionPaperShouldBeFound("footerText.doesNotContain=" + UPDATED_FOOTER_TEXT);
    }

    @Test
    @Transactional
    void getAllQuestionPapersByTotalMarksIsEqualToSomething() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where totalMarks equals to DEFAULT_TOTAL_MARKS
        defaultQuestionPaperShouldBeFound("totalMarks.equals=" + DEFAULT_TOTAL_MARKS);

        // Get all the questionPaperList where totalMarks equals to UPDATED_TOTAL_MARKS
        defaultQuestionPaperShouldNotBeFound("totalMarks.equals=" + UPDATED_TOTAL_MARKS);
    }

    @Test
    @Transactional
    void getAllQuestionPapersByTotalMarksIsNotEqualToSomething() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where totalMarks not equals to DEFAULT_TOTAL_MARKS
        defaultQuestionPaperShouldNotBeFound("totalMarks.notEquals=" + DEFAULT_TOTAL_MARKS);

        // Get all the questionPaperList where totalMarks not equals to UPDATED_TOTAL_MARKS
        defaultQuestionPaperShouldBeFound("totalMarks.notEquals=" + UPDATED_TOTAL_MARKS);
    }

    @Test
    @Transactional
    void getAllQuestionPapersByTotalMarksIsInShouldWork() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where totalMarks in DEFAULT_TOTAL_MARKS or UPDATED_TOTAL_MARKS
        defaultQuestionPaperShouldBeFound("totalMarks.in=" + DEFAULT_TOTAL_MARKS + "," + UPDATED_TOTAL_MARKS);

        // Get all the questionPaperList where totalMarks equals to UPDATED_TOTAL_MARKS
        defaultQuestionPaperShouldNotBeFound("totalMarks.in=" + UPDATED_TOTAL_MARKS);
    }

    @Test
    @Transactional
    void getAllQuestionPapersByTotalMarksIsNullOrNotNull() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where totalMarks is not null
        defaultQuestionPaperShouldBeFound("totalMarks.specified=true");

        // Get all the questionPaperList where totalMarks is null
        defaultQuestionPaperShouldNotBeFound("totalMarks.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionPapersByTotalMarksIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where totalMarks is greater than or equal to DEFAULT_TOTAL_MARKS
        defaultQuestionPaperShouldBeFound("totalMarks.greaterThanOrEqual=" + DEFAULT_TOTAL_MARKS);

        // Get all the questionPaperList where totalMarks is greater than or equal to UPDATED_TOTAL_MARKS
        defaultQuestionPaperShouldNotBeFound("totalMarks.greaterThanOrEqual=" + UPDATED_TOTAL_MARKS);
    }

    @Test
    @Transactional
    void getAllQuestionPapersByTotalMarksIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where totalMarks is less than or equal to DEFAULT_TOTAL_MARKS
        defaultQuestionPaperShouldBeFound("totalMarks.lessThanOrEqual=" + DEFAULT_TOTAL_MARKS);

        // Get all the questionPaperList where totalMarks is less than or equal to SMALLER_TOTAL_MARKS
        defaultQuestionPaperShouldNotBeFound("totalMarks.lessThanOrEqual=" + SMALLER_TOTAL_MARKS);
    }

    @Test
    @Transactional
    void getAllQuestionPapersByTotalMarksIsLessThanSomething() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where totalMarks is less than DEFAULT_TOTAL_MARKS
        defaultQuestionPaperShouldNotBeFound("totalMarks.lessThan=" + DEFAULT_TOTAL_MARKS);

        // Get all the questionPaperList where totalMarks is less than UPDATED_TOTAL_MARKS
        defaultQuestionPaperShouldBeFound("totalMarks.lessThan=" + UPDATED_TOTAL_MARKS);
    }

    @Test
    @Transactional
    void getAllQuestionPapersByTotalMarksIsGreaterThanSomething() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where totalMarks is greater than DEFAULT_TOTAL_MARKS
        defaultQuestionPaperShouldNotBeFound("totalMarks.greaterThan=" + DEFAULT_TOTAL_MARKS);

        // Get all the questionPaperList where totalMarks is greater than SMALLER_TOTAL_MARKS
        defaultQuestionPaperShouldBeFound("totalMarks.greaterThan=" + SMALLER_TOTAL_MARKS);
    }

    @Test
    @Transactional
    void getAllQuestionPapersByCreateDateIsEqualToSomething() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where createDate equals to DEFAULT_CREATE_DATE
        defaultQuestionPaperShouldBeFound("createDate.equals=" + DEFAULT_CREATE_DATE);

        // Get all the questionPaperList where createDate equals to UPDATED_CREATE_DATE
        defaultQuestionPaperShouldNotBeFound("createDate.equals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllQuestionPapersByCreateDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where createDate not equals to DEFAULT_CREATE_DATE
        defaultQuestionPaperShouldNotBeFound("createDate.notEquals=" + DEFAULT_CREATE_DATE);

        // Get all the questionPaperList where createDate not equals to UPDATED_CREATE_DATE
        defaultQuestionPaperShouldBeFound("createDate.notEquals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllQuestionPapersByCreateDateIsInShouldWork() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where createDate in DEFAULT_CREATE_DATE or UPDATED_CREATE_DATE
        defaultQuestionPaperShouldBeFound("createDate.in=" + DEFAULT_CREATE_DATE + "," + UPDATED_CREATE_DATE);

        // Get all the questionPaperList where createDate equals to UPDATED_CREATE_DATE
        defaultQuestionPaperShouldNotBeFound("createDate.in=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllQuestionPapersByCreateDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where createDate is not null
        defaultQuestionPaperShouldBeFound("createDate.specified=true");

        // Get all the questionPaperList where createDate is null
        defaultQuestionPaperShouldNotBeFound("createDate.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionPapersByCreateDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where createDate is greater than or equal to DEFAULT_CREATE_DATE
        defaultQuestionPaperShouldBeFound("createDate.greaterThanOrEqual=" + DEFAULT_CREATE_DATE);

        // Get all the questionPaperList where createDate is greater than or equal to UPDATED_CREATE_DATE
        defaultQuestionPaperShouldNotBeFound("createDate.greaterThanOrEqual=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllQuestionPapersByCreateDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where createDate is less than or equal to DEFAULT_CREATE_DATE
        defaultQuestionPaperShouldBeFound("createDate.lessThanOrEqual=" + DEFAULT_CREATE_DATE);

        // Get all the questionPaperList where createDate is less than or equal to SMALLER_CREATE_DATE
        defaultQuestionPaperShouldNotBeFound("createDate.lessThanOrEqual=" + SMALLER_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllQuestionPapersByCreateDateIsLessThanSomething() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where createDate is less than DEFAULT_CREATE_DATE
        defaultQuestionPaperShouldNotBeFound("createDate.lessThan=" + DEFAULT_CREATE_DATE);

        // Get all the questionPaperList where createDate is less than UPDATED_CREATE_DATE
        defaultQuestionPaperShouldBeFound("createDate.lessThan=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllQuestionPapersByCreateDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where createDate is greater than DEFAULT_CREATE_DATE
        defaultQuestionPaperShouldNotBeFound("createDate.greaterThan=" + DEFAULT_CREATE_DATE);

        // Get all the questionPaperList where createDate is greater than SMALLER_CREATE_DATE
        defaultQuestionPaperShouldBeFound("createDate.greaterThan=" + SMALLER_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllQuestionPapersByLastModifiedIsEqualToSomething() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where lastModified equals to DEFAULT_LAST_MODIFIED
        defaultQuestionPaperShouldBeFound("lastModified.equals=" + DEFAULT_LAST_MODIFIED);

        // Get all the questionPaperList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultQuestionPaperShouldNotBeFound("lastModified.equals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllQuestionPapersByLastModifiedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where lastModified not equals to DEFAULT_LAST_MODIFIED
        defaultQuestionPaperShouldNotBeFound("lastModified.notEquals=" + DEFAULT_LAST_MODIFIED);

        // Get all the questionPaperList where lastModified not equals to UPDATED_LAST_MODIFIED
        defaultQuestionPaperShouldBeFound("lastModified.notEquals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllQuestionPapersByLastModifiedIsInShouldWork() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where lastModified in DEFAULT_LAST_MODIFIED or UPDATED_LAST_MODIFIED
        defaultQuestionPaperShouldBeFound("lastModified.in=" + DEFAULT_LAST_MODIFIED + "," + UPDATED_LAST_MODIFIED);

        // Get all the questionPaperList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultQuestionPaperShouldNotBeFound("lastModified.in=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllQuestionPapersByLastModifiedIsNullOrNotNull() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where lastModified is not null
        defaultQuestionPaperShouldBeFound("lastModified.specified=true");

        // Get all the questionPaperList where lastModified is null
        defaultQuestionPaperShouldNotBeFound("lastModified.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionPapersByLastModifiedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where lastModified is greater than or equal to DEFAULT_LAST_MODIFIED
        defaultQuestionPaperShouldBeFound("lastModified.greaterThanOrEqual=" + DEFAULT_LAST_MODIFIED);

        // Get all the questionPaperList where lastModified is greater than or equal to UPDATED_LAST_MODIFIED
        defaultQuestionPaperShouldNotBeFound("lastModified.greaterThanOrEqual=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllQuestionPapersByLastModifiedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where lastModified is less than or equal to DEFAULT_LAST_MODIFIED
        defaultQuestionPaperShouldBeFound("lastModified.lessThanOrEqual=" + DEFAULT_LAST_MODIFIED);

        // Get all the questionPaperList where lastModified is less than or equal to SMALLER_LAST_MODIFIED
        defaultQuestionPaperShouldNotBeFound("lastModified.lessThanOrEqual=" + SMALLER_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllQuestionPapersByLastModifiedIsLessThanSomething() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where lastModified is less than DEFAULT_LAST_MODIFIED
        defaultQuestionPaperShouldNotBeFound("lastModified.lessThan=" + DEFAULT_LAST_MODIFIED);

        // Get all the questionPaperList where lastModified is less than UPDATED_LAST_MODIFIED
        defaultQuestionPaperShouldBeFound("lastModified.lessThan=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllQuestionPapersByLastModifiedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where lastModified is greater than DEFAULT_LAST_MODIFIED
        defaultQuestionPaperShouldNotBeFound("lastModified.greaterThan=" + DEFAULT_LAST_MODIFIED);

        // Get all the questionPaperList where lastModified is greater than SMALLER_LAST_MODIFIED
        defaultQuestionPaperShouldBeFound("lastModified.greaterThan=" + SMALLER_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllQuestionPapersByCancelDateIsEqualToSomething() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where cancelDate equals to DEFAULT_CANCEL_DATE
        defaultQuestionPaperShouldBeFound("cancelDate.equals=" + DEFAULT_CANCEL_DATE);

        // Get all the questionPaperList where cancelDate equals to UPDATED_CANCEL_DATE
        defaultQuestionPaperShouldNotBeFound("cancelDate.equals=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllQuestionPapersByCancelDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where cancelDate not equals to DEFAULT_CANCEL_DATE
        defaultQuestionPaperShouldNotBeFound("cancelDate.notEquals=" + DEFAULT_CANCEL_DATE);

        // Get all the questionPaperList where cancelDate not equals to UPDATED_CANCEL_DATE
        defaultQuestionPaperShouldBeFound("cancelDate.notEquals=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllQuestionPapersByCancelDateIsInShouldWork() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where cancelDate in DEFAULT_CANCEL_DATE or UPDATED_CANCEL_DATE
        defaultQuestionPaperShouldBeFound("cancelDate.in=" + DEFAULT_CANCEL_DATE + "," + UPDATED_CANCEL_DATE);

        // Get all the questionPaperList where cancelDate equals to UPDATED_CANCEL_DATE
        defaultQuestionPaperShouldNotBeFound("cancelDate.in=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllQuestionPapersByCancelDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where cancelDate is not null
        defaultQuestionPaperShouldBeFound("cancelDate.specified=true");

        // Get all the questionPaperList where cancelDate is null
        defaultQuestionPaperShouldNotBeFound("cancelDate.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionPapersByCancelDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where cancelDate is greater than or equal to DEFAULT_CANCEL_DATE
        defaultQuestionPaperShouldBeFound("cancelDate.greaterThanOrEqual=" + DEFAULT_CANCEL_DATE);

        // Get all the questionPaperList where cancelDate is greater than or equal to UPDATED_CANCEL_DATE
        defaultQuestionPaperShouldNotBeFound("cancelDate.greaterThanOrEqual=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllQuestionPapersByCancelDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where cancelDate is less than or equal to DEFAULT_CANCEL_DATE
        defaultQuestionPaperShouldBeFound("cancelDate.lessThanOrEqual=" + DEFAULT_CANCEL_DATE);

        // Get all the questionPaperList where cancelDate is less than or equal to SMALLER_CANCEL_DATE
        defaultQuestionPaperShouldNotBeFound("cancelDate.lessThanOrEqual=" + SMALLER_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllQuestionPapersByCancelDateIsLessThanSomething() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where cancelDate is less than DEFAULT_CANCEL_DATE
        defaultQuestionPaperShouldNotBeFound("cancelDate.lessThan=" + DEFAULT_CANCEL_DATE);

        // Get all the questionPaperList where cancelDate is less than UPDATED_CANCEL_DATE
        defaultQuestionPaperShouldBeFound("cancelDate.lessThan=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllQuestionPapersByCancelDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        // Get all the questionPaperList where cancelDate is greater than DEFAULT_CANCEL_DATE
        defaultQuestionPaperShouldNotBeFound("cancelDate.greaterThan=" + DEFAULT_CANCEL_DATE);

        // Get all the questionPaperList where cancelDate is greater than SMALLER_CANCEL_DATE
        defaultQuestionPaperShouldBeFound("cancelDate.greaterThan=" + SMALLER_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllQuestionPapersByQuestionIsEqualToSomething() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);
        Question question;
        if (TestUtil.findAll(em, Question.class).isEmpty()) {
            question = QuestionResourceIT.createEntity(em);
            em.persist(question);
            em.flush();
        } else {
            question = TestUtil.findAll(em, Question.class).get(0);
        }
        em.persist(question);
        em.flush();
        questionPaper.addQuestion(question);
        questionPaperRepository.saveAndFlush(questionPaper);
        Long questionId = question.getId();

        // Get all the questionPaperList where question equals to questionId
        defaultQuestionPaperShouldBeFound("questionId.equals=" + questionId);

        // Get all the questionPaperList where question equals to (questionId + 1)
        defaultQuestionPaperShouldNotBeFound("questionId.equals=" + (questionId + 1));
    }

    @Test
    @Transactional
    void getAllQuestionPapersByTagIsEqualToSomething() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);
        Tag tag;
        if (TestUtil.findAll(em, Tag.class).isEmpty()) {
            tag = TagResourceIT.createEntity(em);
            em.persist(tag);
            em.flush();
        } else {
            tag = TestUtil.findAll(em, Tag.class).get(0);
        }
        em.persist(tag);
        em.flush();
        questionPaper.addTag(tag);
        questionPaperRepository.saveAndFlush(questionPaper);
        Long tagId = tag.getId();

        // Get all the questionPaperList where tag equals to tagId
        defaultQuestionPaperShouldBeFound("tagId.equals=" + tagId);

        // Get all the questionPaperList where tag equals to (tagId + 1)
        defaultQuestionPaperShouldNotBeFound("tagId.equals=" + (tagId + 1));
    }

    @Test
    @Transactional
    void getAllQuestionPapersByTenantIsEqualToSomething() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);
        Tenant tenant;
        if (TestUtil.findAll(em, Tenant.class).isEmpty()) {
            tenant = TenantResourceIT.createEntity(em);
            em.persist(tenant);
            em.flush();
        } else {
            tenant = TestUtil.findAll(em, Tenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        questionPaper.setTenant(tenant);
        questionPaperRepository.saveAndFlush(questionPaper);
        Long tenantId = tenant.getId();

        // Get all the questionPaperList where tenant equals to tenantId
        defaultQuestionPaperShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the questionPaperList where tenant equals to (tenantId + 1)
        defaultQuestionPaperShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    @Test
    @Transactional
    void getAllQuestionPapersBySchoolClassIsEqualToSomething() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);
        SchoolClass schoolClass;
        if (TestUtil.findAll(em, SchoolClass.class).isEmpty()) {
            schoolClass = SchoolClassResourceIT.createEntity(em);
            em.persist(schoolClass);
            em.flush();
        } else {
            schoolClass = TestUtil.findAll(em, SchoolClass.class).get(0);
        }
        em.persist(schoolClass);
        em.flush();
        questionPaper.setSchoolClass(schoolClass);
        questionPaperRepository.saveAndFlush(questionPaper);
        Long schoolClassId = schoolClass.getId();

        // Get all the questionPaperList where schoolClass equals to schoolClassId
        defaultQuestionPaperShouldBeFound("schoolClassId.equals=" + schoolClassId);

        // Get all the questionPaperList where schoolClass equals to (schoolClassId + 1)
        defaultQuestionPaperShouldNotBeFound("schoolClassId.equals=" + (schoolClassId + 1));
    }

    @Test
    @Transactional
    void getAllQuestionPapersByClassSubjectIsEqualToSomething() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);
        ClassSubject classSubject;
        if (TestUtil.findAll(em, ClassSubject.class).isEmpty()) {
            classSubject = ClassSubjectResourceIT.createEntity(em);
            em.persist(classSubject);
            em.flush();
        } else {
            classSubject = TestUtil.findAll(em, ClassSubject.class).get(0);
        }
        em.persist(classSubject);
        em.flush();
        questionPaper.setClassSubject(classSubject);
        questionPaperRepository.saveAndFlush(questionPaper);
        Long classSubjectId = classSubject.getId();

        // Get all the questionPaperList where classSubject equals to classSubjectId
        defaultQuestionPaperShouldBeFound("classSubjectId.equals=" + classSubjectId);

        // Get all the questionPaperList where classSubject equals to (classSubjectId + 1)
        defaultQuestionPaperShouldNotBeFound("classSubjectId.equals=" + (classSubjectId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultQuestionPaperShouldBeFound(String filter) throws Exception {
        restQuestionPaperMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(questionPaper.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenatLogoContentType").value(hasItem(DEFAULT_TENAT_LOGO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].tenatLogo").value(hasItem(Base64Utils.encodeToString(DEFAULT_TENAT_LOGO))))
            .andExpect(jsonPath("$.[*].questionPaperFileContentType").value(hasItem(DEFAULT_QUESTION_PAPER_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].questionPaperFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_QUESTION_PAPER_FILE))))
            .andExpect(jsonPath("$.[*].questionPaperName").value(hasItem(DEFAULT_QUESTION_PAPER_NAME)))
            .andExpect(jsonPath("$.[*].mainTitle").value(hasItem(DEFAULT_MAIN_TITLE)))
            .andExpect(jsonPath("$.[*].subTitle").value(hasItem(DEFAULT_SUB_TITLE)))
            .andExpect(jsonPath("$.[*].leftSubHeading1").value(hasItem(DEFAULT_LEFT_SUB_HEADING_1)))
            .andExpect(jsonPath("$.[*].leftSubHeading2").value(hasItem(DEFAULT_LEFT_SUB_HEADING_2)))
            .andExpect(jsonPath("$.[*].rightSubHeading1").value(hasItem(DEFAULT_RIGHT_SUB_HEADING_1)))
            .andExpect(jsonPath("$.[*].rightSubHeading2").value(hasItem(DEFAULT_RIGHT_SUB_HEADING_2)))
            .andExpect(jsonPath("$.[*].instructions").value(hasItem(DEFAULT_INSTRUCTIONS)))
            .andExpect(jsonPath("$.[*].footerText").value(hasItem(DEFAULT_FOOTER_TEXT)))
            .andExpect(jsonPath("$.[*].totalMarks").value(hasItem(DEFAULT_TOTAL_MARKS)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].cancelDate").value(hasItem(DEFAULT_CANCEL_DATE.toString())));

        // Check, that the count call also returns 1
        restQuestionPaperMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultQuestionPaperShouldNotBeFound(String filter) throws Exception {
        restQuestionPaperMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restQuestionPaperMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingQuestionPaper() throws Exception {
        // Get the questionPaper
        restQuestionPaperMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewQuestionPaper() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        int databaseSizeBeforeUpdate = questionPaperRepository.findAll().size();

        // Update the questionPaper
        QuestionPaper updatedQuestionPaper = questionPaperRepository.findById(questionPaper.getId()).get();
        // Disconnect from session so that the updates on updatedQuestionPaper are not directly saved in db
        em.detach(updatedQuestionPaper);
        updatedQuestionPaper
            .tenatLogo(UPDATED_TENAT_LOGO)
            .tenatLogoContentType(UPDATED_TENAT_LOGO_CONTENT_TYPE)
            .questionPaperFile(UPDATED_QUESTION_PAPER_FILE)
            .questionPaperFileContentType(UPDATED_QUESTION_PAPER_FILE_CONTENT_TYPE)
            .questionPaperName(UPDATED_QUESTION_PAPER_NAME)
            .mainTitle(UPDATED_MAIN_TITLE)
            .subTitle(UPDATED_SUB_TITLE)
            .leftSubHeading1(UPDATED_LEFT_SUB_HEADING_1)
            .leftSubHeading2(UPDATED_LEFT_SUB_HEADING_2)
            .rightSubHeading1(UPDATED_RIGHT_SUB_HEADING_1)
            .rightSubHeading2(UPDATED_RIGHT_SUB_HEADING_2)
            .instructions(UPDATED_INSTRUCTIONS)
            .footerText(UPDATED_FOOTER_TEXT)
            .totalMarks(UPDATED_TOTAL_MARKS)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);

        restQuestionPaperMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedQuestionPaper.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedQuestionPaper))
            )
            .andExpect(status().isOk());

        // Validate the QuestionPaper in the database
        List<QuestionPaper> questionPaperList = questionPaperRepository.findAll();
        assertThat(questionPaperList).hasSize(databaseSizeBeforeUpdate);
        QuestionPaper testQuestionPaper = questionPaperList.get(questionPaperList.size() - 1);
        assertThat(testQuestionPaper.getTenatLogo()).isEqualTo(UPDATED_TENAT_LOGO);
        assertThat(testQuestionPaper.getTenatLogoContentType()).isEqualTo(UPDATED_TENAT_LOGO_CONTENT_TYPE);
        assertThat(testQuestionPaper.getQuestionPaperFile()).isEqualTo(UPDATED_QUESTION_PAPER_FILE);
        assertThat(testQuestionPaper.getQuestionPaperFileContentType()).isEqualTo(UPDATED_QUESTION_PAPER_FILE_CONTENT_TYPE);
        assertThat(testQuestionPaper.getQuestionPaperName()).isEqualTo(UPDATED_QUESTION_PAPER_NAME);
        assertThat(testQuestionPaper.getMainTitle()).isEqualTo(UPDATED_MAIN_TITLE);
        assertThat(testQuestionPaper.getSubTitle()).isEqualTo(UPDATED_SUB_TITLE);
        assertThat(testQuestionPaper.getLeftSubHeading1()).isEqualTo(UPDATED_LEFT_SUB_HEADING_1);
        assertThat(testQuestionPaper.getLeftSubHeading2()).isEqualTo(UPDATED_LEFT_SUB_HEADING_2);
        assertThat(testQuestionPaper.getRightSubHeading1()).isEqualTo(UPDATED_RIGHT_SUB_HEADING_1);
        assertThat(testQuestionPaper.getRightSubHeading2()).isEqualTo(UPDATED_RIGHT_SUB_HEADING_2);
        assertThat(testQuestionPaper.getInstructions()).isEqualTo(UPDATED_INSTRUCTIONS);
        assertThat(testQuestionPaper.getFooterText()).isEqualTo(UPDATED_FOOTER_TEXT);
        assertThat(testQuestionPaper.getTotalMarks()).isEqualTo(UPDATED_TOTAL_MARKS);
        assertThat(testQuestionPaper.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testQuestionPaper.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testQuestionPaper.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void putNonExistingQuestionPaper() throws Exception {
        int databaseSizeBeforeUpdate = questionPaperRepository.findAll().size();
        questionPaper.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestionPaperMockMvc
            .perform(
                put(ENTITY_API_URL_ID, questionPaper.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(questionPaper))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuestionPaper in the database
        List<QuestionPaper> questionPaperList = questionPaperRepository.findAll();
        assertThat(questionPaperList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchQuestionPaper() throws Exception {
        int databaseSizeBeforeUpdate = questionPaperRepository.findAll().size();
        questionPaper.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionPaperMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(questionPaper))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuestionPaper in the database
        List<QuestionPaper> questionPaperList = questionPaperRepository.findAll();
        assertThat(questionPaperList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamQuestionPaper() throws Exception {
        int databaseSizeBeforeUpdate = questionPaperRepository.findAll().size();
        questionPaper.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionPaperMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(questionPaper)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the QuestionPaper in the database
        List<QuestionPaper> questionPaperList = questionPaperRepository.findAll();
        assertThat(questionPaperList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateQuestionPaperWithPatch() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        int databaseSizeBeforeUpdate = questionPaperRepository.findAll().size();

        // Update the questionPaper using partial update
        QuestionPaper partialUpdatedQuestionPaper = new QuestionPaper();
        partialUpdatedQuestionPaper.setId(questionPaper.getId());

        partialUpdatedQuestionPaper
            .mainTitle(UPDATED_MAIN_TITLE)
            .leftSubHeading2(UPDATED_LEFT_SUB_HEADING_2)
            .instructions(UPDATED_INSTRUCTIONS)
            .totalMarks(UPDATED_TOTAL_MARKS)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);

        restQuestionPaperMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuestionPaper.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQuestionPaper))
            )
            .andExpect(status().isOk());

        // Validate the QuestionPaper in the database
        List<QuestionPaper> questionPaperList = questionPaperRepository.findAll();
        assertThat(questionPaperList).hasSize(databaseSizeBeforeUpdate);
        QuestionPaper testQuestionPaper = questionPaperList.get(questionPaperList.size() - 1);
        assertThat(testQuestionPaper.getTenatLogo()).isEqualTo(DEFAULT_TENAT_LOGO);
        assertThat(testQuestionPaper.getTenatLogoContentType()).isEqualTo(DEFAULT_TENAT_LOGO_CONTENT_TYPE);
        assertThat(testQuestionPaper.getQuestionPaperFile()).isEqualTo(DEFAULT_QUESTION_PAPER_FILE);
        assertThat(testQuestionPaper.getQuestionPaperFileContentType()).isEqualTo(DEFAULT_QUESTION_PAPER_FILE_CONTENT_TYPE);
        assertThat(testQuestionPaper.getQuestionPaperName()).isEqualTo(DEFAULT_QUESTION_PAPER_NAME);
        assertThat(testQuestionPaper.getMainTitle()).isEqualTo(UPDATED_MAIN_TITLE);
        assertThat(testQuestionPaper.getSubTitle()).isEqualTo(DEFAULT_SUB_TITLE);
        assertThat(testQuestionPaper.getLeftSubHeading1()).isEqualTo(DEFAULT_LEFT_SUB_HEADING_1);
        assertThat(testQuestionPaper.getLeftSubHeading2()).isEqualTo(UPDATED_LEFT_SUB_HEADING_2);
        assertThat(testQuestionPaper.getRightSubHeading1()).isEqualTo(DEFAULT_RIGHT_SUB_HEADING_1);
        assertThat(testQuestionPaper.getRightSubHeading2()).isEqualTo(DEFAULT_RIGHT_SUB_HEADING_2);
        assertThat(testQuestionPaper.getInstructions()).isEqualTo(UPDATED_INSTRUCTIONS);
        assertThat(testQuestionPaper.getFooterText()).isEqualTo(DEFAULT_FOOTER_TEXT);
        assertThat(testQuestionPaper.getTotalMarks()).isEqualTo(UPDATED_TOTAL_MARKS);
        assertThat(testQuestionPaper.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testQuestionPaper.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testQuestionPaper.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void fullUpdateQuestionPaperWithPatch() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        int databaseSizeBeforeUpdate = questionPaperRepository.findAll().size();

        // Update the questionPaper using partial update
        QuestionPaper partialUpdatedQuestionPaper = new QuestionPaper();
        partialUpdatedQuestionPaper.setId(questionPaper.getId());

        partialUpdatedQuestionPaper
            .tenatLogo(UPDATED_TENAT_LOGO)
            .tenatLogoContentType(UPDATED_TENAT_LOGO_CONTENT_TYPE)
            .questionPaperFile(UPDATED_QUESTION_PAPER_FILE)
            .questionPaperFileContentType(UPDATED_QUESTION_PAPER_FILE_CONTENT_TYPE)
            .questionPaperName(UPDATED_QUESTION_PAPER_NAME)
            .mainTitle(UPDATED_MAIN_TITLE)
            .subTitle(UPDATED_SUB_TITLE)
            .leftSubHeading1(UPDATED_LEFT_SUB_HEADING_1)
            .leftSubHeading2(UPDATED_LEFT_SUB_HEADING_2)
            .rightSubHeading1(UPDATED_RIGHT_SUB_HEADING_1)
            .rightSubHeading2(UPDATED_RIGHT_SUB_HEADING_2)
            .instructions(UPDATED_INSTRUCTIONS)
            .footerText(UPDATED_FOOTER_TEXT)
            .totalMarks(UPDATED_TOTAL_MARKS)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);

        restQuestionPaperMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuestionPaper.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQuestionPaper))
            )
            .andExpect(status().isOk());

        // Validate the QuestionPaper in the database
        List<QuestionPaper> questionPaperList = questionPaperRepository.findAll();
        assertThat(questionPaperList).hasSize(databaseSizeBeforeUpdate);
        QuestionPaper testQuestionPaper = questionPaperList.get(questionPaperList.size() - 1);
        assertThat(testQuestionPaper.getTenatLogo()).isEqualTo(UPDATED_TENAT_LOGO);
        assertThat(testQuestionPaper.getTenatLogoContentType()).isEqualTo(UPDATED_TENAT_LOGO_CONTENT_TYPE);
        assertThat(testQuestionPaper.getQuestionPaperFile()).isEqualTo(UPDATED_QUESTION_PAPER_FILE);
        assertThat(testQuestionPaper.getQuestionPaperFileContentType()).isEqualTo(UPDATED_QUESTION_PAPER_FILE_CONTENT_TYPE);
        assertThat(testQuestionPaper.getQuestionPaperName()).isEqualTo(UPDATED_QUESTION_PAPER_NAME);
        assertThat(testQuestionPaper.getMainTitle()).isEqualTo(UPDATED_MAIN_TITLE);
        assertThat(testQuestionPaper.getSubTitle()).isEqualTo(UPDATED_SUB_TITLE);
        assertThat(testQuestionPaper.getLeftSubHeading1()).isEqualTo(UPDATED_LEFT_SUB_HEADING_1);
        assertThat(testQuestionPaper.getLeftSubHeading2()).isEqualTo(UPDATED_LEFT_SUB_HEADING_2);
        assertThat(testQuestionPaper.getRightSubHeading1()).isEqualTo(UPDATED_RIGHT_SUB_HEADING_1);
        assertThat(testQuestionPaper.getRightSubHeading2()).isEqualTo(UPDATED_RIGHT_SUB_HEADING_2);
        assertThat(testQuestionPaper.getInstructions()).isEqualTo(UPDATED_INSTRUCTIONS);
        assertThat(testQuestionPaper.getFooterText()).isEqualTo(UPDATED_FOOTER_TEXT);
        assertThat(testQuestionPaper.getTotalMarks()).isEqualTo(UPDATED_TOTAL_MARKS);
        assertThat(testQuestionPaper.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testQuestionPaper.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testQuestionPaper.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingQuestionPaper() throws Exception {
        int databaseSizeBeforeUpdate = questionPaperRepository.findAll().size();
        questionPaper.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestionPaperMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, questionPaper.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(questionPaper))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuestionPaper in the database
        List<QuestionPaper> questionPaperList = questionPaperRepository.findAll();
        assertThat(questionPaperList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchQuestionPaper() throws Exception {
        int databaseSizeBeforeUpdate = questionPaperRepository.findAll().size();
        questionPaper.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionPaperMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(questionPaper))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuestionPaper in the database
        List<QuestionPaper> questionPaperList = questionPaperRepository.findAll();
        assertThat(questionPaperList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamQuestionPaper() throws Exception {
        int databaseSizeBeforeUpdate = questionPaperRepository.findAll().size();
        questionPaper.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionPaperMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(questionPaper))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the QuestionPaper in the database
        List<QuestionPaper> questionPaperList = questionPaperRepository.findAll();
        assertThat(questionPaperList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteQuestionPaper() throws Exception {
        // Initialize the database
        questionPaperRepository.saveAndFlush(questionPaper);

        int databaseSizeBeforeDelete = questionPaperRepository.findAll().size();

        // Delete the questionPaper
        restQuestionPaperMockMvc
            .perform(delete(ENTITY_API_URL_ID, questionPaper.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<QuestionPaper> questionPaperList = questionPaperRepository.findAll();
        assertThat(questionPaperList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
