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
import com.ssik.manageit.domain.QuestionType;
import com.ssik.manageit.domain.SchoolClass;
import com.ssik.manageit.domain.SubjectChapter;
import com.ssik.manageit.domain.Tag;
import com.ssik.manageit.domain.Tenant;
import com.ssik.manageit.domain.enumeration.Difficulty;
import com.ssik.manageit.domain.enumeration.Status;
import com.ssik.manageit.repository.QuestionRepository;
import com.ssik.manageit.service.QuestionService;
import com.ssik.manageit.service.criteria.QuestionCriteria;
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
 * Integration tests for the {@link QuestionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class QuestionResourceIT {

    private static final byte[] DEFAULT_QUESTION_IMPORT_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_QUESTION_IMPORT_FILE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_QUESTION_IMPORT_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_QUESTION_IMPORT_FILE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_QUESTION_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_QUESTION_TEXT = "BBBBBBBBBB";

    private static final byte[] DEFAULT_QUESTION_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_QUESTION_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_QUESTION_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_QUESTION_IMAGE_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_ANSWER_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_ANSWER_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_ANSWER_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_ANSWER_IMAGE_CONTENT_TYPE = "image/png";

    private static final Boolean DEFAULT_IMAGE_SIDE_BY_SIDE = false;
    private static final Boolean UPDATED_IMAGE_SIDE_BY_SIDE = true;

    private static final String DEFAULT_OPTION_1 = "AAAAAAAAAA";
    private static final String UPDATED_OPTION_1 = "BBBBBBBBBB";

    private static final String DEFAULT_OPTION_2 = "AAAAAAAAAA";
    private static final String UPDATED_OPTION_2 = "BBBBBBBBBB";

    private static final String DEFAULT_OPTION_3 = "AAAAAAAAAA";
    private static final String UPDATED_OPTION_3 = "BBBBBBBBBB";

    private static final String DEFAULT_OPTION_4 = "AAAAAAAAAA";
    private static final String UPDATED_OPTION_4 = "BBBBBBBBBB";

    private static final String DEFAULT_OPTION_5 = "AAAAAAAAAA";
    private static final String UPDATED_OPTION_5 = "BBBBBBBBBB";

    private static final Status DEFAULT_STATUS = Status.DRAFT;
    private static final Status UPDATED_STATUS = Status.INPROGRESS;

    private static final Integer DEFAULT_WEIGHTAGE = 1;
    private static final Integer UPDATED_WEIGHTAGE = 2;
    private static final Integer SMALLER_WEIGHTAGE = 1 - 1;

    private static final Difficulty DEFAULT_DIFFICULTY_LEVEL = Difficulty.EASY;
    private static final Difficulty UPDATED_DIFFICULTY_LEVEL = Difficulty.MEDIUM;

    private static final LocalDate DEFAULT_CREATE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATE_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CREATE_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_LAST_MODIFIED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_MODIFIED = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_LAST_MODIFIED = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_CANCEL_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CANCEL_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CANCEL_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/questions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private QuestionRepository questionRepository;

    @Mock
    private QuestionRepository questionRepositoryMock;

    @Mock
    private QuestionService questionServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQuestionMockMvc;

    private Question question;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Question createEntity(EntityManager em) {
        Question question = new Question()
            .questionImportFile(DEFAULT_QUESTION_IMPORT_FILE)
            .questionImportFileContentType(DEFAULT_QUESTION_IMPORT_FILE_CONTENT_TYPE)
            .questionText(DEFAULT_QUESTION_TEXT)
            .questionImage(DEFAULT_QUESTION_IMAGE)
            .questionImageContentType(DEFAULT_QUESTION_IMAGE_CONTENT_TYPE)
            .answerImage(DEFAULT_ANSWER_IMAGE)
            .answerImageContentType(DEFAULT_ANSWER_IMAGE_CONTENT_TYPE)
            .imageSideBySide(DEFAULT_IMAGE_SIDE_BY_SIDE)
            .option1(DEFAULT_OPTION_1)
            .option2(DEFAULT_OPTION_2)
            .option3(DEFAULT_OPTION_3)
            .option4(DEFAULT_OPTION_4)
            .option5(DEFAULT_OPTION_5)
            .status(DEFAULT_STATUS)
            .weightage(DEFAULT_WEIGHTAGE)
            .difficultyLevel(DEFAULT_DIFFICULTY_LEVEL)
            .createDate(DEFAULT_CREATE_DATE)
            .lastModified(DEFAULT_LAST_MODIFIED)
            .cancelDate(DEFAULT_CANCEL_DATE);
        return question;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Question createUpdatedEntity(EntityManager em) {
        Question question = new Question()
            .questionImportFile(UPDATED_QUESTION_IMPORT_FILE)
            .questionImportFileContentType(UPDATED_QUESTION_IMPORT_FILE_CONTENT_TYPE)
            .questionText(UPDATED_QUESTION_TEXT)
            .questionImage(UPDATED_QUESTION_IMAGE)
            .questionImageContentType(UPDATED_QUESTION_IMAGE_CONTENT_TYPE)
            .answerImage(UPDATED_ANSWER_IMAGE)
            .answerImageContentType(UPDATED_ANSWER_IMAGE_CONTENT_TYPE)
            .imageSideBySide(UPDATED_IMAGE_SIDE_BY_SIDE)
            .option1(UPDATED_OPTION_1)
            .option2(UPDATED_OPTION_2)
            .option3(UPDATED_OPTION_3)
            .option4(UPDATED_OPTION_4)
            .option5(UPDATED_OPTION_5)
            .status(UPDATED_STATUS)
            .weightage(UPDATED_WEIGHTAGE)
            .difficultyLevel(UPDATED_DIFFICULTY_LEVEL)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);
        return question;
    }

    @BeforeEach
    public void initTest() {
        question = createEntity(em);
    }

    @Test
    @Transactional
    void createQuestion() throws Exception {
        int databaseSizeBeforeCreate = questionRepository.findAll().size();
        // Create the Question
        restQuestionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(question)))
            .andExpect(status().isCreated());

        // Validate the Question in the database
        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeCreate + 1);
        Question testQuestion = questionList.get(questionList.size() - 1);
        assertThat(testQuestion.getQuestionImportFile()).isEqualTo(DEFAULT_QUESTION_IMPORT_FILE);
        assertThat(testQuestion.getQuestionImportFileContentType()).isEqualTo(DEFAULT_QUESTION_IMPORT_FILE_CONTENT_TYPE);
        assertThat(testQuestion.getQuestionText()).isEqualTo(DEFAULT_QUESTION_TEXT);
        assertThat(testQuestion.getQuestionImage()).isEqualTo(DEFAULT_QUESTION_IMAGE);
        assertThat(testQuestion.getQuestionImageContentType()).isEqualTo(DEFAULT_QUESTION_IMAGE_CONTENT_TYPE);
        assertThat(testQuestion.getAnswerImage()).isEqualTo(DEFAULT_ANSWER_IMAGE);
        assertThat(testQuestion.getAnswerImageContentType()).isEqualTo(DEFAULT_ANSWER_IMAGE_CONTENT_TYPE);
        assertThat(testQuestion.getImageSideBySide()).isEqualTo(DEFAULT_IMAGE_SIDE_BY_SIDE);
        assertThat(testQuestion.getOption1()).isEqualTo(DEFAULT_OPTION_1);
        assertThat(testQuestion.getOption2()).isEqualTo(DEFAULT_OPTION_2);
        assertThat(testQuestion.getOption3()).isEqualTo(DEFAULT_OPTION_3);
        assertThat(testQuestion.getOption4()).isEqualTo(DEFAULT_OPTION_4);
        assertThat(testQuestion.getOption5()).isEqualTo(DEFAULT_OPTION_5);
        assertThat(testQuestion.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testQuestion.getWeightage()).isEqualTo(DEFAULT_WEIGHTAGE);
        assertThat(testQuestion.getDifficultyLevel()).isEqualTo(DEFAULT_DIFFICULTY_LEVEL);
        assertThat(testQuestion.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testQuestion.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
        assertThat(testQuestion.getCancelDate()).isEqualTo(DEFAULT_CANCEL_DATE);
    }

    @Test
    @Transactional
    void createQuestionWithExistingId() throws Exception {
        // Create the Question with an existing ID
        question.setId(1L);

        int databaseSizeBeforeCreate = questionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuestionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(question)))
            .andExpect(status().isBadRequest());

        // Validate the Question in the database
        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllQuestions() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList
        restQuestionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(question.getId().intValue())))
            .andExpect(jsonPath("$.[*].questionImportFileContentType").value(hasItem(DEFAULT_QUESTION_IMPORT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].questionImportFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_QUESTION_IMPORT_FILE))))
            .andExpect(jsonPath("$.[*].questionText").value(hasItem(DEFAULT_QUESTION_TEXT.toString())))
            .andExpect(jsonPath("$.[*].questionImageContentType").value(hasItem(DEFAULT_QUESTION_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].questionImage").value(hasItem(Base64Utils.encodeToString(DEFAULT_QUESTION_IMAGE))))
            .andExpect(jsonPath("$.[*].answerImageContentType").value(hasItem(DEFAULT_ANSWER_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].answerImage").value(hasItem(Base64Utils.encodeToString(DEFAULT_ANSWER_IMAGE))))
            .andExpect(jsonPath("$.[*].imageSideBySide").value(hasItem(DEFAULT_IMAGE_SIDE_BY_SIDE.booleanValue())))
            .andExpect(jsonPath("$.[*].option1").value(hasItem(DEFAULT_OPTION_1)))
            .andExpect(jsonPath("$.[*].option2").value(hasItem(DEFAULT_OPTION_2)))
            .andExpect(jsonPath("$.[*].option3").value(hasItem(DEFAULT_OPTION_3)))
            .andExpect(jsonPath("$.[*].option4").value(hasItem(DEFAULT_OPTION_4)))
            .andExpect(jsonPath("$.[*].option5").value(hasItem(DEFAULT_OPTION_5)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].weightage").value(hasItem(DEFAULT_WEIGHTAGE)))
            .andExpect(jsonPath("$.[*].difficultyLevel").value(hasItem(DEFAULT_DIFFICULTY_LEVEL.toString())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].cancelDate").value(hasItem(DEFAULT_CANCEL_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllQuestionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(questionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restQuestionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(questionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllQuestionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(questionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restQuestionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(questionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getQuestion() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get the question
        restQuestionMockMvc
            .perform(get(ENTITY_API_URL_ID, question.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(question.getId().intValue()))
            .andExpect(jsonPath("$.questionImportFileContentType").value(DEFAULT_QUESTION_IMPORT_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.questionImportFile").value(Base64Utils.encodeToString(DEFAULT_QUESTION_IMPORT_FILE)))
            .andExpect(jsonPath("$.questionText").value(DEFAULT_QUESTION_TEXT.toString()))
            .andExpect(jsonPath("$.questionImageContentType").value(DEFAULT_QUESTION_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.questionImage").value(Base64Utils.encodeToString(DEFAULT_QUESTION_IMAGE)))
            .andExpect(jsonPath("$.answerImageContentType").value(DEFAULT_ANSWER_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.answerImage").value(Base64Utils.encodeToString(DEFAULT_ANSWER_IMAGE)))
            .andExpect(jsonPath("$.imageSideBySide").value(DEFAULT_IMAGE_SIDE_BY_SIDE.booleanValue()))
            .andExpect(jsonPath("$.option1").value(DEFAULT_OPTION_1))
            .andExpect(jsonPath("$.option2").value(DEFAULT_OPTION_2))
            .andExpect(jsonPath("$.option3").value(DEFAULT_OPTION_3))
            .andExpect(jsonPath("$.option4").value(DEFAULT_OPTION_4))
            .andExpect(jsonPath("$.option5").value(DEFAULT_OPTION_5))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.weightage").value(DEFAULT_WEIGHTAGE))
            .andExpect(jsonPath("$.difficultyLevel").value(DEFAULT_DIFFICULTY_LEVEL.toString()))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.lastModified").value(DEFAULT_LAST_MODIFIED.toString()))
            .andExpect(jsonPath("$.cancelDate").value(DEFAULT_CANCEL_DATE.toString()));
    }

    @Test
    @Transactional
    void getQuestionsByIdFiltering() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        Long id = question.getId();

        defaultQuestionShouldBeFound("id.equals=" + id);
        defaultQuestionShouldNotBeFound("id.notEquals=" + id);

        defaultQuestionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultQuestionShouldNotBeFound("id.greaterThan=" + id);

        defaultQuestionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultQuestionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllQuestionsByImageSideBySideIsEqualToSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where imageSideBySide equals to DEFAULT_IMAGE_SIDE_BY_SIDE
        defaultQuestionShouldBeFound("imageSideBySide.equals=" + DEFAULT_IMAGE_SIDE_BY_SIDE);

        // Get all the questionList where imageSideBySide equals to UPDATED_IMAGE_SIDE_BY_SIDE
        defaultQuestionShouldNotBeFound("imageSideBySide.equals=" + UPDATED_IMAGE_SIDE_BY_SIDE);
    }

    @Test
    @Transactional
    void getAllQuestionsByImageSideBySideIsNotEqualToSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where imageSideBySide not equals to DEFAULT_IMAGE_SIDE_BY_SIDE
        defaultQuestionShouldNotBeFound("imageSideBySide.notEquals=" + DEFAULT_IMAGE_SIDE_BY_SIDE);

        // Get all the questionList where imageSideBySide not equals to UPDATED_IMAGE_SIDE_BY_SIDE
        defaultQuestionShouldBeFound("imageSideBySide.notEquals=" + UPDATED_IMAGE_SIDE_BY_SIDE);
    }

    @Test
    @Transactional
    void getAllQuestionsByImageSideBySideIsInShouldWork() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where imageSideBySide in DEFAULT_IMAGE_SIDE_BY_SIDE or UPDATED_IMAGE_SIDE_BY_SIDE
        defaultQuestionShouldBeFound("imageSideBySide.in=" + DEFAULT_IMAGE_SIDE_BY_SIDE + "," + UPDATED_IMAGE_SIDE_BY_SIDE);

        // Get all the questionList where imageSideBySide equals to UPDATED_IMAGE_SIDE_BY_SIDE
        defaultQuestionShouldNotBeFound("imageSideBySide.in=" + UPDATED_IMAGE_SIDE_BY_SIDE);
    }

    @Test
    @Transactional
    void getAllQuestionsByImageSideBySideIsNullOrNotNull() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where imageSideBySide is not null
        defaultQuestionShouldBeFound("imageSideBySide.specified=true");

        // Get all the questionList where imageSideBySide is null
        defaultQuestionShouldNotBeFound("imageSideBySide.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionsByOption1IsEqualToSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where option1 equals to DEFAULT_OPTION_1
        defaultQuestionShouldBeFound("option1.equals=" + DEFAULT_OPTION_1);

        // Get all the questionList where option1 equals to UPDATED_OPTION_1
        defaultQuestionShouldNotBeFound("option1.equals=" + UPDATED_OPTION_1);
    }

    @Test
    @Transactional
    void getAllQuestionsByOption1IsNotEqualToSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where option1 not equals to DEFAULT_OPTION_1
        defaultQuestionShouldNotBeFound("option1.notEquals=" + DEFAULT_OPTION_1);

        // Get all the questionList where option1 not equals to UPDATED_OPTION_1
        defaultQuestionShouldBeFound("option1.notEquals=" + UPDATED_OPTION_1);
    }

    @Test
    @Transactional
    void getAllQuestionsByOption1IsInShouldWork() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where option1 in DEFAULT_OPTION_1 or UPDATED_OPTION_1
        defaultQuestionShouldBeFound("option1.in=" + DEFAULT_OPTION_1 + "," + UPDATED_OPTION_1);

        // Get all the questionList where option1 equals to UPDATED_OPTION_1
        defaultQuestionShouldNotBeFound("option1.in=" + UPDATED_OPTION_1);
    }

    @Test
    @Transactional
    void getAllQuestionsByOption1IsNullOrNotNull() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where option1 is not null
        defaultQuestionShouldBeFound("option1.specified=true");

        // Get all the questionList where option1 is null
        defaultQuestionShouldNotBeFound("option1.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionsByOption1ContainsSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where option1 contains DEFAULT_OPTION_1
        defaultQuestionShouldBeFound("option1.contains=" + DEFAULT_OPTION_1);

        // Get all the questionList where option1 contains UPDATED_OPTION_1
        defaultQuestionShouldNotBeFound("option1.contains=" + UPDATED_OPTION_1);
    }

    @Test
    @Transactional
    void getAllQuestionsByOption1NotContainsSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where option1 does not contain DEFAULT_OPTION_1
        defaultQuestionShouldNotBeFound("option1.doesNotContain=" + DEFAULT_OPTION_1);

        // Get all the questionList where option1 does not contain UPDATED_OPTION_1
        defaultQuestionShouldBeFound("option1.doesNotContain=" + UPDATED_OPTION_1);
    }

    @Test
    @Transactional
    void getAllQuestionsByOption2IsEqualToSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where option2 equals to DEFAULT_OPTION_2
        defaultQuestionShouldBeFound("option2.equals=" + DEFAULT_OPTION_2);

        // Get all the questionList where option2 equals to UPDATED_OPTION_2
        defaultQuestionShouldNotBeFound("option2.equals=" + UPDATED_OPTION_2);
    }

    @Test
    @Transactional
    void getAllQuestionsByOption2IsNotEqualToSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where option2 not equals to DEFAULT_OPTION_2
        defaultQuestionShouldNotBeFound("option2.notEquals=" + DEFAULT_OPTION_2);

        // Get all the questionList where option2 not equals to UPDATED_OPTION_2
        defaultQuestionShouldBeFound("option2.notEquals=" + UPDATED_OPTION_2);
    }

    @Test
    @Transactional
    void getAllQuestionsByOption2IsInShouldWork() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where option2 in DEFAULT_OPTION_2 or UPDATED_OPTION_2
        defaultQuestionShouldBeFound("option2.in=" + DEFAULT_OPTION_2 + "," + UPDATED_OPTION_2);

        // Get all the questionList where option2 equals to UPDATED_OPTION_2
        defaultQuestionShouldNotBeFound("option2.in=" + UPDATED_OPTION_2);
    }

    @Test
    @Transactional
    void getAllQuestionsByOption2IsNullOrNotNull() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where option2 is not null
        defaultQuestionShouldBeFound("option2.specified=true");

        // Get all the questionList where option2 is null
        defaultQuestionShouldNotBeFound("option2.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionsByOption2ContainsSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where option2 contains DEFAULT_OPTION_2
        defaultQuestionShouldBeFound("option2.contains=" + DEFAULT_OPTION_2);

        // Get all the questionList where option2 contains UPDATED_OPTION_2
        defaultQuestionShouldNotBeFound("option2.contains=" + UPDATED_OPTION_2);
    }

    @Test
    @Transactional
    void getAllQuestionsByOption2NotContainsSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where option2 does not contain DEFAULT_OPTION_2
        defaultQuestionShouldNotBeFound("option2.doesNotContain=" + DEFAULT_OPTION_2);

        // Get all the questionList where option2 does not contain UPDATED_OPTION_2
        defaultQuestionShouldBeFound("option2.doesNotContain=" + UPDATED_OPTION_2);
    }

    @Test
    @Transactional
    void getAllQuestionsByOption3IsEqualToSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where option3 equals to DEFAULT_OPTION_3
        defaultQuestionShouldBeFound("option3.equals=" + DEFAULT_OPTION_3);

        // Get all the questionList where option3 equals to UPDATED_OPTION_3
        defaultQuestionShouldNotBeFound("option3.equals=" + UPDATED_OPTION_3);
    }

    @Test
    @Transactional
    void getAllQuestionsByOption3IsNotEqualToSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where option3 not equals to DEFAULT_OPTION_3
        defaultQuestionShouldNotBeFound("option3.notEquals=" + DEFAULT_OPTION_3);

        // Get all the questionList where option3 not equals to UPDATED_OPTION_3
        defaultQuestionShouldBeFound("option3.notEquals=" + UPDATED_OPTION_3);
    }

    @Test
    @Transactional
    void getAllQuestionsByOption3IsInShouldWork() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where option3 in DEFAULT_OPTION_3 or UPDATED_OPTION_3
        defaultQuestionShouldBeFound("option3.in=" + DEFAULT_OPTION_3 + "," + UPDATED_OPTION_3);

        // Get all the questionList where option3 equals to UPDATED_OPTION_3
        defaultQuestionShouldNotBeFound("option3.in=" + UPDATED_OPTION_3);
    }

    @Test
    @Transactional
    void getAllQuestionsByOption3IsNullOrNotNull() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where option3 is not null
        defaultQuestionShouldBeFound("option3.specified=true");

        // Get all the questionList where option3 is null
        defaultQuestionShouldNotBeFound("option3.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionsByOption3ContainsSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where option3 contains DEFAULT_OPTION_3
        defaultQuestionShouldBeFound("option3.contains=" + DEFAULT_OPTION_3);

        // Get all the questionList where option3 contains UPDATED_OPTION_3
        defaultQuestionShouldNotBeFound("option3.contains=" + UPDATED_OPTION_3);
    }

    @Test
    @Transactional
    void getAllQuestionsByOption3NotContainsSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where option3 does not contain DEFAULT_OPTION_3
        defaultQuestionShouldNotBeFound("option3.doesNotContain=" + DEFAULT_OPTION_3);

        // Get all the questionList where option3 does not contain UPDATED_OPTION_3
        defaultQuestionShouldBeFound("option3.doesNotContain=" + UPDATED_OPTION_3);
    }

    @Test
    @Transactional
    void getAllQuestionsByOption4IsEqualToSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where option4 equals to DEFAULT_OPTION_4
        defaultQuestionShouldBeFound("option4.equals=" + DEFAULT_OPTION_4);

        // Get all the questionList where option4 equals to UPDATED_OPTION_4
        defaultQuestionShouldNotBeFound("option4.equals=" + UPDATED_OPTION_4);
    }

    @Test
    @Transactional
    void getAllQuestionsByOption4IsNotEqualToSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where option4 not equals to DEFAULT_OPTION_4
        defaultQuestionShouldNotBeFound("option4.notEquals=" + DEFAULT_OPTION_4);

        // Get all the questionList where option4 not equals to UPDATED_OPTION_4
        defaultQuestionShouldBeFound("option4.notEquals=" + UPDATED_OPTION_4);
    }

    @Test
    @Transactional
    void getAllQuestionsByOption4IsInShouldWork() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where option4 in DEFAULT_OPTION_4 or UPDATED_OPTION_4
        defaultQuestionShouldBeFound("option4.in=" + DEFAULT_OPTION_4 + "," + UPDATED_OPTION_4);

        // Get all the questionList where option4 equals to UPDATED_OPTION_4
        defaultQuestionShouldNotBeFound("option4.in=" + UPDATED_OPTION_4);
    }

    @Test
    @Transactional
    void getAllQuestionsByOption4IsNullOrNotNull() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where option4 is not null
        defaultQuestionShouldBeFound("option4.specified=true");

        // Get all the questionList where option4 is null
        defaultQuestionShouldNotBeFound("option4.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionsByOption4ContainsSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where option4 contains DEFAULT_OPTION_4
        defaultQuestionShouldBeFound("option4.contains=" + DEFAULT_OPTION_4);

        // Get all the questionList where option4 contains UPDATED_OPTION_4
        defaultQuestionShouldNotBeFound("option4.contains=" + UPDATED_OPTION_4);
    }

    @Test
    @Transactional
    void getAllQuestionsByOption4NotContainsSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where option4 does not contain DEFAULT_OPTION_4
        defaultQuestionShouldNotBeFound("option4.doesNotContain=" + DEFAULT_OPTION_4);

        // Get all the questionList where option4 does not contain UPDATED_OPTION_4
        defaultQuestionShouldBeFound("option4.doesNotContain=" + UPDATED_OPTION_4);
    }

    @Test
    @Transactional
    void getAllQuestionsByOption5IsEqualToSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where option5 equals to DEFAULT_OPTION_5
        defaultQuestionShouldBeFound("option5.equals=" + DEFAULT_OPTION_5);

        // Get all the questionList where option5 equals to UPDATED_OPTION_5
        defaultQuestionShouldNotBeFound("option5.equals=" + UPDATED_OPTION_5);
    }

    @Test
    @Transactional
    void getAllQuestionsByOption5IsNotEqualToSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where option5 not equals to DEFAULT_OPTION_5
        defaultQuestionShouldNotBeFound("option5.notEquals=" + DEFAULT_OPTION_5);

        // Get all the questionList where option5 not equals to UPDATED_OPTION_5
        defaultQuestionShouldBeFound("option5.notEquals=" + UPDATED_OPTION_5);
    }

    @Test
    @Transactional
    void getAllQuestionsByOption5IsInShouldWork() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where option5 in DEFAULT_OPTION_5 or UPDATED_OPTION_5
        defaultQuestionShouldBeFound("option5.in=" + DEFAULT_OPTION_5 + "," + UPDATED_OPTION_5);

        // Get all the questionList where option5 equals to UPDATED_OPTION_5
        defaultQuestionShouldNotBeFound("option5.in=" + UPDATED_OPTION_5);
    }

    @Test
    @Transactional
    void getAllQuestionsByOption5IsNullOrNotNull() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where option5 is not null
        defaultQuestionShouldBeFound("option5.specified=true");

        // Get all the questionList where option5 is null
        defaultQuestionShouldNotBeFound("option5.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionsByOption5ContainsSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where option5 contains DEFAULT_OPTION_5
        defaultQuestionShouldBeFound("option5.contains=" + DEFAULT_OPTION_5);

        // Get all the questionList where option5 contains UPDATED_OPTION_5
        defaultQuestionShouldNotBeFound("option5.contains=" + UPDATED_OPTION_5);
    }

    @Test
    @Transactional
    void getAllQuestionsByOption5NotContainsSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where option5 does not contain DEFAULT_OPTION_5
        defaultQuestionShouldNotBeFound("option5.doesNotContain=" + DEFAULT_OPTION_5);

        // Get all the questionList where option5 does not contain UPDATED_OPTION_5
        defaultQuestionShouldBeFound("option5.doesNotContain=" + UPDATED_OPTION_5);
    }

    @Test
    @Transactional
    void getAllQuestionsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where status equals to DEFAULT_STATUS
        defaultQuestionShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the questionList where status equals to UPDATED_STATUS
        defaultQuestionShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllQuestionsByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where status not equals to DEFAULT_STATUS
        defaultQuestionShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the questionList where status not equals to UPDATED_STATUS
        defaultQuestionShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllQuestionsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultQuestionShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the questionList where status equals to UPDATED_STATUS
        defaultQuestionShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllQuestionsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where status is not null
        defaultQuestionShouldBeFound("status.specified=true");

        // Get all the questionList where status is null
        defaultQuestionShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionsByWeightageIsEqualToSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where weightage equals to DEFAULT_WEIGHTAGE
        defaultQuestionShouldBeFound("weightage.equals=" + DEFAULT_WEIGHTAGE);

        // Get all the questionList where weightage equals to UPDATED_WEIGHTAGE
        defaultQuestionShouldNotBeFound("weightage.equals=" + UPDATED_WEIGHTAGE);
    }

    @Test
    @Transactional
    void getAllQuestionsByWeightageIsNotEqualToSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where weightage not equals to DEFAULT_WEIGHTAGE
        defaultQuestionShouldNotBeFound("weightage.notEquals=" + DEFAULT_WEIGHTAGE);

        // Get all the questionList where weightage not equals to UPDATED_WEIGHTAGE
        defaultQuestionShouldBeFound("weightage.notEquals=" + UPDATED_WEIGHTAGE);
    }

    @Test
    @Transactional
    void getAllQuestionsByWeightageIsInShouldWork() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where weightage in DEFAULT_WEIGHTAGE or UPDATED_WEIGHTAGE
        defaultQuestionShouldBeFound("weightage.in=" + DEFAULT_WEIGHTAGE + "," + UPDATED_WEIGHTAGE);

        // Get all the questionList where weightage equals to UPDATED_WEIGHTAGE
        defaultQuestionShouldNotBeFound("weightage.in=" + UPDATED_WEIGHTAGE);
    }

    @Test
    @Transactional
    void getAllQuestionsByWeightageIsNullOrNotNull() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where weightage is not null
        defaultQuestionShouldBeFound("weightage.specified=true");

        // Get all the questionList where weightage is null
        defaultQuestionShouldNotBeFound("weightage.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionsByWeightageIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where weightage is greater than or equal to DEFAULT_WEIGHTAGE
        defaultQuestionShouldBeFound("weightage.greaterThanOrEqual=" + DEFAULT_WEIGHTAGE);

        // Get all the questionList where weightage is greater than or equal to UPDATED_WEIGHTAGE
        defaultQuestionShouldNotBeFound("weightage.greaterThanOrEqual=" + UPDATED_WEIGHTAGE);
    }

    @Test
    @Transactional
    void getAllQuestionsByWeightageIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where weightage is less than or equal to DEFAULT_WEIGHTAGE
        defaultQuestionShouldBeFound("weightage.lessThanOrEqual=" + DEFAULT_WEIGHTAGE);

        // Get all the questionList where weightage is less than or equal to SMALLER_WEIGHTAGE
        defaultQuestionShouldNotBeFound("weightage.lessThanOrEqual=" + SMALLER_WEIGHTAGE);
    }

    @Test
    @Transactional
    void getAllQuestionsByWeightageIsLessThanSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where weightage is less than DEFAULT_WEIGHTAGE
        defaultQuestionShouldNotBeFound("weightage.lessThan=" + DEFAULT_WEIGHTAGE);

        // Get all the questionList where weightage is less than UPDATED_WEIGHTAGE
        defaultQuestionShouldBeFound("weightage.lessThan=" + UPDATED_WEIGHTAGE);
    }

    @Test
    @Transactional
    void getAllQuestionsByWeightageIsGreaterThanSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where weightage is greater than DEFAULT_WEIGHTAGE
        defaultQuestionShouldNotBeFound("weightage.greaterThan=" + DEFAULT_WEIGHTAGE);

        // Get all the questionList where weightage is greater than SMALLER_WEIGHTAGE
        defaultQuestionShouldBeFound("weightage.greaterThan=" + SMALLER_WEIGHTAGE);
    }

    @Test
    @Transactional
    void getAllQuestionsByDifficultyLevelIsEqualToSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where difficultyLevel equals to DEFAULT_DIFFICULTY_LEVEL
        defaultQuestionShouldBeFound("difficultyLevel.equals=" + DEFAULT_DIFFICULTY_LEVEL);

        // Get all the questionList where difficultyLevel equals to UPDATED_DIFFICULTY_LEVEL
        defaultQuestionShouldNotBeFound("difficultyLevel.equals=" + UPDATED_DIFFICULTY_LEVEL);
    }

    @Test
    @Transactional
    void getAllQuestionsByDifficultyLevelIsNotEqualToSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where difficultyLevel not equals to DEFAULT_DIFFICULTY_LEVEL
        defaultQuestionShouldNotBeFound("difficultyLevel.notEquals=" + DEFAULT_DIFFICULTY_LEVEL);

        // Get all the questionList where difficultyLevel not equals to UPDATED_DIFFICULTY_LEVEL
        defaultQuestionShouldBeFound("difficultyLevel.notEquals=" + UPDATED_DIFFICULTY_LEVEL);
    }

    @Test
    @Transactional
    void getAllQuestionsByDifficultyLevelIsInShouldWork() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where difficultyLevel in DEFAULT_DIFFICULTY_LEVEL or UPDATED_DIFFICULTY_LEVEL
        defaultQuestionShouldBeFound("difficultyLevel.in=" + DEFAULT_DIFFICULTY_LEVEL + "," + UPDATED_DIFFICULTY_LEVEL);

        // Get all the questionList where difficultyLevel equals to UPDATED_DIFFICULTY_LEVEL
        defaultQuestionShouldNotBeFound("difficultyLevel.in=" + UPDATED_DIFFICULTY_LEVEL);
    }

    @Test
    @Transactional
    void getAllQuestionsByDifficultyLevelIsNullOrNotNull() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where difficultyLevel is not null
        defaultQuestionShouldBeFound("difficultyLevel.specified=true");

        // Get all the questionList where difficultyLevel is null
        defaultQuestionShouldNotBeFound("difficultyLevel.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionsByCreateDateIsEqualToSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where createDate equals to DEFAULT_CREATE_DATE
        defaultQuestionShouldBeFound("createDate.equals=" + DEFAULT_CREATE_DATE);

        // Get all the questionList where createDate equals to UPDATED_CREATE_DATE
        defaultQuestionShouldNotBeFound("createDate.equals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllQuestionsByCreateDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where createDate not equals to DEFAULT_CREATE_DATE
        defaultQuestionShouldNotBeFound("createDate.notEquals=" + DEFAULT_CREATE_DATE);

        // Get all the questionList where createDate not equals to UPDATED_CREATE_DATE
        defaultQuestionShouldBeFound("createDate.notEquals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllQuestionsByCreateDateIsInShouldWork() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where createDate in DEFAULT_CREATE_DATE or UPDATED_CREATE_DATE
        defaultQuestionShouldBeFound("createDate.in=" + DEFAULT_CREATE_DATE + "," + UPDATED_CREATE_DATE);

        // Get all the questionList where createDate equals to UPDATED_CREATE_DATE
        defaultQuestionShouldNotBeFound("createDate.in=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllQuestionsByCreateDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where createDate is not null
        defaultQuestionShouldBeFound("createDate.specified=true");

        // Get all the questionList where createDate is null
        defaultQuestionShouldNotBeFound("createDate.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionsByCreateDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where createDate is greater than or equal to DEFAULT_CREATE_DATE
        defaultQuestionShouldBeFound("createDate.greaterThanOrEqual=" + DEFAULT_CREATE_DATE);

        // Get all the questionList where createDate is greater than or equal to UPDATED_CREATE_DATE
        defaultQuestionShouldNotBeFound("createDate.greaterThanOrEqual=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllQuestionsByCreateDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where createDate is less than or equal to DEFAULT_CREATE_DATE
        defaultQuestionShouldBeFound("createDate.lessThanOrEqual=" + DEFAULT_CREATE_DATE);

        // Get all the questionList where createDate is less than or equal to SMALLER_CREATE_DATE
        defaultQuestionShouldNotBeFound("createDate.lessThanOrEqual=" + SMALLER_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllQuestionsByCreateDateIsLessThanSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where createDate is less than DEFAULT_CREATE_DATE
        defaultQuestionShouldNotBeFound("createDate.lessThan=" + DEFAULT_CREATE_DATE);

        // Get all the questionList where createDate is less than UPDATED_CREATE_DATE
        defaultQuestionShouldBeFound("createDate.lessThan=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllQuestionsByCreateDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where createDate is greater than DEFAULT_CREATE_DATE
        defaultQuestionShouldNotBeFound("createDate.greaterThan=" + DEFAULT_CREATE_DATE);

        // Get all the questionList where createDate is greater than SMALLER_CREATE_DATE
        defaultQuestionShouldBeFound("createDate.greaterThan=" + SMALLER_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllQuestionsByLastModifiedIsEqualToSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where lastModified equals to DEFAULT_LAST_MODIFIED
        defaultQuestionShouldBeFound("lastModified.equals=" + DEFAULT_LAST_MODIFIED);

        // Get all the questionList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultQuestionShouldNotBeFound("lastModified.equals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllQuestionsByLastModifiedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where lastModified not equals to DEFAULT_LAST_MODIFIED
        defaultQuestionShouldNotBeFound("lastModified.notEquals=" + DEFAULT_LAST_MODIFIED);

        // Get all the questionList where lastModified not equals to UPDATED_LAST_MODIFIED
        defaultQuestionShouldBeFound("lastModified.notEquals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllQuestionsByLastModifiedIsInShouldWork() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where lastModified in DEFAULT_LAST_MODIFIED or UPDATED_LAST_MODIFIED
        defaultQuestionShouldBeFound("lastModified.in=" + DEFAULT_LAST_MODIFIED + "," + UPDATED_LAST_MODIFIED);

        // Get all the questionList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultQuestionShouldNotBeFound("lastModified.in=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllQuestionsByLastModifiedIsNullOrNotNull() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where lastModified is not null
        defaultQuestionShouldBeFound("lastModified.specified=true");

        // Get all the questionList where lastModified is null
        defaultQuestionShouldNotBeFound("lastModified.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionsByLastModifiedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where lastModified is greater than or equal to DEFAULT_LAST_MODIFIED
        defaultQuestionShouldBeFound("lastModified.greaterThanOrEqual=" + DEFAULT_LAST_MODIFIED);

        // Get all the questionList where lastModified is greater than or equal to UPDATED_LAST_MODIFIED
        defaultQuestionShouldNotBeFound("lastModified.greaterThanOrEqual=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllQuestionsByLastModifiedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where lastModified is less than or equal to DEFAULT_LAST_MODIFIED
        defaultQuestionShouldBeFound("lastModified.lessThanOrEqual=" + DEFAULT_LAST_MODIFIED);

        // Get all the questionList where lastModified is less than or equal to SMALLER_LAST_MODIFIED
        defaultQuestionShouldNotBeFound("lastModified.lessThanOrEqual=" + SMALLER_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllQuestionsByLastModifiedIsLessThanSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where lastModified is less than DEFAULT_LAST_MODIFIED
        defaultQuestionShouldNotBeFound("lastModified.lessThan=" + DEFAULT_LAST_MODIFIED);

        // Get all the questionList where lastModified is less than UPDATED_LAST_MODIFIED
        defaultQuestionShouldBeFound("lastModified.lessThan=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllQuestionsByLastModifiedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where lastModified is greater than DEFAULT_LAST_MODIFIED
        defaultQuestionShouldNotBeFound("lastModified.greaterThan=" + DEFAULT_LAST_MODIFIED);

        // Get all the questionList where lastModified is greater than SMALLER_LAST_MODIFIED
        defaultQuestionShouldBeFound("lastModified.greaterThan=" + SMALLER_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllQuestionsByCancelDateIsEqualToSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where cancelDate equals to DEFAULT_CANCEL_DATE
        defaultQuestionShouldBeFound("cancelDate.equals=" + DEFAULT_CANCEL_DATE);

        // Get all the questionList where cancelDate equals to UPDATED_CANCEL_DATE
        defaultQuestionShouldNotBeFound("cancelDate.equals=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllQuestionsByCancelDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where cancelDate not equals to DEFAULT_CANCEL_DATE
        defaultQuestionShouldNotBeFound("cancelDate.notEquals=" + DEFAULT_CANCEL_DATE);

        // Get all the questionList where cancelDate not equals to UPDATED_CANCEL_DATE
        defaultQuestionShouldBeFound("cancelDate.notEquals=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllQuestionsByCancelDateIsInShouldWork() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where cancelDate in DEFAULT_CANCEL_DATE or UPDATED_CANCEL_DATE
        defaultQuestionShouldBeFound("cancelDate.in=" + DEFAULT_CANCEL_DATE + "," + UPDATED_CANCEL_DATE);

        // Get all the questionList where cancelDate equals to UPDATED_CANCEL_DATE
        defaultQuestionShouldNotBeFound("cancelDate.in=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllQuestionsByCancelDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where cancelDate is not null
        defaultQuestionShouldBeFound("cancelDate.specified=true");

        // Get all the questionList where cancelDate is null
        defaultQuestionShouldNotBeFound("cancelDate.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionsByCancelDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where cancelDate is greater than or equal to DEFAULT_CANCEL_DATE
        defaultQuestionShouldBeFound("cancelDate.greaterThanOrEqual=" + DEFAULT_CANCEL_DATE);

        // Get all the questionList where cancelDate is greater than or equal to UPDATED_CANCEL_DATE
        defaultQuestionShouldNotBeFound("cancelDate.greaterThanOrEqual=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllQuestionsByCancelDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where cancelDate is less than or equal to DEFAULT_CANCEL_DATE
        defaultQuestionShouldBeFound("cancelDate.lessThanOrEqual=" + DEFAULT_CANCEL_DATE);

        // Get all the questionList where cancelDate is less than or equal to SMALLER_CANCEL_DATE
        defaultQuestionShouldNotBeFound("cancelDate.lessThanOrEqual=" + SMALLER_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllQuestionsByCancelDateIsLessThanSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where cancelDate is less than DEFAULT_CANCEL_DATE
        defaultQuestionShouldNotBeFound("cancelDate.lessThan=" + DEFAULT_CANCEL_DATE);

        // Get all the questionList where cancelDate is less than UPDATED_CANCEL_DATE
        defaultQuestionShouldBeFound("cancelDate.lessThan=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllQuestionsByCancelDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where cancelDate is greater than DEFAULT_CANCEL_DATE
        defaultQuestionShouldNotBeFound("cancelDate.greaterThan=" + DEFAULT_CANCEL_DATE);

        // Get all the questionList where cancelDate is greater than SMALLER_CANCEL_DATE
        defaultQuestionShouldBeFound("cancelDate.greaterThan=" + SMALLER_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllQuestionsByTagIsEqualToSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);
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
        question.addTag(tag);
        questionRepository.saveAndFlush(question);
        Long tagId = tag.getId();

        // Get all the questionList where tag equals to tagId
        defaultQuestionShouldBeFound("tagId.equals=" + tagId);

        // Get all the questionList where tag equals to (tagId + 1)
        defaultQuestionShouldNotBeFound("tagId.equals=" + (tagId + 1));
    }

    @Test
    @Transactional
    void getAllQuestionsByQuestionTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);
        QuestionType questionType;
        if (TestUtil.findAll(em, QuestionType.class).isEmpty()) {
            questionType = QuestionTypeResourceIT.createEntity(em);
            em.persist(questionType);
            em.flush();
        } else {
            questionType = TestUtil.findAll(em, QuestionType.class).get(0);
        }
        em.persist(questionType);
        em.flush();
        question.setQuestionType(questionType);
        questionRepository.saveAndFlush(question);
        Long questionTypeId = questionType.getId();

        // Get all the questionList where questionType equals to questionTypeId
        defaultQuestionShouldBeFound("questionTypeId.equals=" + questionTypeId);

        // Get all the questionList where questionType equals to (questionTypeId + 1)
        defaultQuestionShouldNotBeFound("questionTypeId.equals=" + (questionTypeId + 1));
    }

    @Test
    @Transactional
    void getAllQuestionsByTenantIsEqualToSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);
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
        question.setTenant(tenant);
        questionRepository.saveAndFlush(question);
        Long tenantId = tenant.getId();

        // Get all the questionList where tenant equals to tenantId
        defaultQuestionShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the questionList where tenant equals to (tenantId + 1)
        defaultQuestionShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    @Test
    @Transactional
    void getAllQuestionsBySchoolClassIsEqualToSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);
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
        question.setSchoolClass(schoolClass);
        questionRepository.saveAndFlush(question);
        Long schoolClassId = schoolClass.getId();

        // Get all the questionList where schoolClass equals to schoolClassId
        defaultQuestionShouldBeFound("schoolClassId.equals=" + schoolClassId);

        // Get all the questionList where schoolClass equals to (schoolClassId + 1)
        defaultQuestionShouldNotBeFound("schoolClassId.equals=" + (schoolClassId + 1));
    }

    @Test
    @Transactional
    void getAllQuestionsByClassSubjectIsEqualToSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);
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
        question.setClassSubject(classSubject);
        questionRepository.saveAndFlush(question);
        Long classSubjectId = classSubject.getId();

        // Get all the questionList where classSubject equals to classSubjectId
        defaultQuestionShouldBeFound("classSubjectId.equals=" + classSubjectId);

        // Get all the questionList where classSubject equals to (classSubjectId + 1)
        defaultQuestionShouldNotBeFound("classSubjectId.equals=" + (classSubjectId + 1));
    }

    @Test
    @Transactional
    void getAllQuestionsBySubjectChapterIsEqualToSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);
        SubjectChapter subjectChapter;
        if (TestUtil.findAll(em, SubjectChapter.class).isEmpty()) {
            subjectChapter = SubjectChapterResourceIT.createEntity(em);
            em.persist(subjectChapter);
            em.flush();
        } else {
            subjectChapter = TestUtil.findAll(em, SubjectChapter.class).get(0);
        }
        em.persist(subjectChapter);
        em.flush();
        question.setSubjectChapter(subjectChapter);
        questionRepository.saveAndFlush(question);
        Long subjectChapterId = subjectChapter.getId();

        // Get all the questionList where subjectChapter equals to subjectChapterId
        defaultQuestionShouldBeFound("subjectChapterId.equals=" + subjectChapterId);

        // Get all the questionList where subjectChapter equals to (subjectChapterId + 1)
        defaultQuestionShouldNotBeFound("subjectChapterId.equals=" + (subjectChapterId + 1));
    }

    @Test
    @Transactional
    void getAllQuestionsByQuestionPaperIsEqualToSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);
        QuestionPaper questionPaper;
        if (TestUtil.findAll(em, QuestionPaper.class).isEmpty()) {
            questionPaper = QuestionPaperResourceIT.createEntity(em);
            em.persist(questionPaper);
            em.flush();
        } else {
            questionPaper = TestUtil.findAll(em, QuestionPaper.class).get(0);
        }
        em.persist(questionPaper);
        em.flush();
        question.addQuestionPaper(questionPaper);
        questionRepository.saveAndFlush(question);
        Long questionPaperId = questionPaper.getId();

        // Get all the questionList where questionPaper equals to questionPaperId
        defaultQuestionShouldBeFound("questionPaperId.equals=" + questionPaperId);

        // Get all the questionList where questionPaper equals to (questionPaperId + 1)
        defaultQuestionShouldNotBeFound("questionPaperId.equals=" + (questionPaperId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultQuestionShouldBeFound(String filter) throws Exception {
        restQuestionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(question.getId().intValue())))
            .andExpect(jsonPath("$.[*].questionImportFileContentType").value(hasItem(DEFAULT_QUESTION_IMPORT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].questionImportFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_QUESTION_IMPORT_FILE))))
            .andExpect(jsonPath("$.[*].questionText").value(hasItem(DEFAULT_QUESTION_TEXT.toString())))
            .andExpect(jsonPath("$.[*].questionImageContentType").value(hasItem(DEFAULT_QUESTION_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].questionImage").value(hasItem(Base64Utils.encodeToString(DEFAULT_QUESTION_IMAGE))))
            .andExpect(jsonPath("$.[*].answerImageContentType").value(hasItem(DEFAULT_ANSWER_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].answerImage").value(hasItem(Base64Utils.encodeToString(DEFAULT_ANSWER_IMAGE))))
            .andExpect(jsonPath("$.[*].imageSideBySide").value(hasItem(DEFAULT_IMAGE_SIDE_BY_SIDE.booleanValue())))
            .andExpect(jsonPath("$.[*].option1").value(hasItem(DEFAULT_OPTION_1)))
            .andExpect(jsonPath("$.[*].option2").value(hasItem(DEFAULT_OPTION_2)))
            .andExpect(jsonPath("$.[*].option3").value(hasItem(DEFAULT_OPTION_3)))
            .andExpect(jsonPath("$.[*].option4").value(hasItem(DEFAULT_OPTION_4)))
            .andExpect(jsonPath("$.[*].option5").value(hasItem(DEFAULT_OPTION_5)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].weightage").value(hasItem(DEFAULT_WEIGHTAGE)))
            .andExpect(jsonPath("$.[*].difficultyLevel").value(hasItem(DEFAULT_DIFFICULTY_LEVEL.toString())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].cancelDate").value(hasItem(DEFAULT_CANCEL_DATE.toString())));

        // Check, that the count call also returns 1
        restQuestionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultQuestionShouldNotBeFound(String filter) throws Exception {
        restQuestionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restQuestionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingQuestion() throws Exception {
        // Get the question
        restQuestionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewQuestion() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        int databaseSizeBeforeUpdate = questionRepository.findAll().size();

        // Update the question
        Question updatedQuestion = questionRepository.findById(question.getId()).get();
        // Disconnect from session so that the updates on updatedQuestion are not directly saved in db
        em.detach(updatedQuestion);
        updatedQuestion
            .questionImportFile(UPDATED_QUESTION_IMPORT_FILE)
            .questionImportFileContentType(UPDATED_QUESTION_IMPORT_FILE_CONTENT_TYPE)
            .questionText(UPDATED_QUESTION_TEXT)
            .questionImage(UPDATED_QUESTION_IMAGE)
            .questionImageContentType(UPDATED_QUESTION_IMAGE_CONTENT_TYPE)
            .answerImage(UPDATED_ANSWER_IMAGE)
            .answerImageContentType(UPDATED_ANSWER_IMAGE_CONTENT_TYPE)
            .imageSideBySide(UPDATED_IMAGE_SIDE_BY_SIDE)
            .option1(UPDATED_OPTION_1)
            .option2(UPDATED_OPTION_2)
            .option3(UPDATED_OPTION_3)
            .option4(UPDATED_OPTION_4)
            .option5(UPDATED_OPTION_5)
            .status(UPDATED_STATUS)
            .weightage(UPDATED_WEIGHTAGE)
            .difficultyLevel(UPDATED_DIFFICULTY_LEVEL)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);

        restQuestionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedQuestion.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedQuestion))
            )
            .andExpect(status().isOk());

        // Validate the Question in the database
        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeUpdate);
        Question testQuestion = questionList.get(questionList.size() - 1);
        assertThat(testQuestion.getQuestionImportFile()).isEqualTo(UPDATED_QUESTION_IMPORT_FILE);
        assertThat(testQuestion.getQuestionImportFileContentType()).isEqualTo(UPDATED_QUESTION_IMPORT_FILE_CONTENT_TYPE);
        assertThat(testQuestion.getQuestionText()).isEqualTo(UPDATED_QUESTION_TEXT);
        assertThat(testQuestion.getQuestionImage()).isEqualTo(UPDATED_QUESTION_IMAGE);
        assertThat(testQuestion.getQuestionImageContentType()).isEqualTo(UPDATED_QUESTION_IMAGE_CONTENT_TYPE);
        assertThat(testQuestion.getAnswerImage()).isEqualTo(UPDATED_ANSWER_IMAGE);
        assertThat(testQuestion.getAnswerImageContentType()).isEqualTo(UPDATED_ANSWER_IMAGE_CONTENT_TYPE);
        assertThat(testQuestion.getImageSideBySide()).isEqualTo(UPDATED_IMAGE_SIDE_BY_SIDE);
        assertThat(testQuestion.getOption1()).isEqualTo(UPDATED_OPTION_1);
        assertThat(testQuestion.getOption2()).isEqualTo(UPDATED_OPTION_2);
        assertThat(testQuestion.getOption3()).isEqualTo(UPDATED_OPTION_3);
        assertThat(testQuestion.getOption4()).isEqualTo(UPDATED_OPTION_4);
        assertThat(testQuestion.getOption5()).isEqualTo(UPDATED_OPTION_5);
        assertThat(testQuestion.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testQuestion.getWeightage()).isEqualTo(UPDATED_WEIGHTAGE);
        assertThat(testQuestion.getDifficultyLevel()).isEqualTo(UPDATED_DIFFICULTY_LEVEL);
        assertThat(testQuestion.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testQuestion.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testQuestion.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void putNonExistingQuestion() throws Exception {
        int databaseSizeBeforeUpdate = questionRepository.findAll().size();
        question.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, question.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(question))
            )
            .andExpect(status().isBadRequest());

        // Validate the Question in the database
        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchQuestion() throws Exception {
        int databaseSizeBeforeUpdate = questionRepository.findAll().size();
        question.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(question))
            )
            .andExpect(status().isBadRequest());

        // Validate the Question in the database
        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamQuestion() throws Exception {
        int databaseSizeBeforeUpdate = questionRepository.findAll().size();
        question.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(question)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Question in the database
        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateQuestionWithPatch() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        int databaseSizeBeforeUpdate = questionRepository.findAll().size();

        // Update the question using partial update
        Question partialUpdatedQuestion = new Question();
        partialUpdatedQuestion.setId(question.getId());

        partialUpdatedQuestion
            .questionImportFile(UPDATED_QUESTION_IMPORT_FILE)
            .questionImportFileContentType(UPDATED_QUESTION_IMPORT_FILE_CONTENT_TYPE)
            .questionText(UPDATED_QUESTION_TEXT)
            .questionImage(UPDATED_QUESTION_IMAGE)
            .questionImageContentType(UPDATED_QUESTION_IMAGE_CONTENT_TYPE)
            .option1(UPDATED_OPTION_1)
            .option2(UPDATED_OPTION_2)
            .option3(UPDATED_OPTION_3)
            .option5(UPDATED_OPTION_5)
            .createDate(UPDATED_CREATE_DATE);

        restQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuestion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQuestion))
            )
            .andExpect(status().isOk());

        // Validate the Question in the database
        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeUpdate);
        Question testQuestion = questionList.get(questionList.size() - 1);
        assertThat(testQuestion.getQuestionImportFile()).isEqualTo(UPDATED_QUESTION_IMPORT_FILE);
        assertThat(testQuestion.getQuestionImportFileContentType()).isEqualTo(UPDATED_QUESTION_IMPORT_FILE_CONTENT_TYPE);
        assertThat(testQuestion.getQuestionText()).isEqualTo(UPDATED_QUESTION_TEXT);
        assertThat(testQuestion.getQuestionImage()).isEqualTo(UPDATED_QUESTION_IMAGE);
        assertThat(testQuestion.getQuestionImageContentType()).isEqualTo(UPDATED_QUESTION_IMAGE_CONTENT_TYPE);
        assertThat(testQuestion.getAnswerImage()).isEqualTo(DEFAULT_ANSWER_IMAGE);
        assertThat(testQuestion.getAnswerImageContentType()).isEqualTo(DEFAULT_ANSWER_IMAGE_CONTENT_TYPE);
        assertThat(testQuestion.getImageSideBySide()).isEqualTo(DEFAULT_IMAGE_SIDE_BY_SIDE);
        assertThat(testQuestion.getOption1()).isEqualTo(UPDATED_OPTION_1);
        assertThat(testQuestion.getOption2()).isEqualTo(UPDATED_OPTION_2);
        assertThat(testQuestion.getOption3()).isEqualTo(UPDATED_OPTION_3);
        assertThat(testQuestion.getOption4()).isEqualTo(DEFAULT_OPTION_4);
        assertThat(testQuestion.getOption5()).isEqualTo(UPDATED_OPTION_5);
        assertThat(testQuestion.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testQuestion.getWeightage()).isEqualTo(DEFAULT_WEIGHTAGE);
        assertThat(testQuestion.getDifficultyLevel()).isEqualTo(DEFAULT_DIFFICULTY_LEVEL);
        assertThat(testQuestion.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testQuestion.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
        assertThat(testQuestion.getCancelDate()).isEqualTo(DEFAULT_CANCEL_DATE);
    }

    @Test
    @Transactional
    void fullUpdateQuestionWithPatch() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        int databaseSizeBeforeUpdate = questionRepository.findAll().size();

        // Update the question using partial update
        Question partialUpdatedQuestion = new Question();
        partialUpdatedQuestion.setId(question.getId());

        partialUpdatedQuestion
            .questionImportFile(UPDATED_QUESTION_IMPORT_FILE)
            .questionImportFileContentType(UPDATED_QUESTION_IMPORT_FILE_CONTENT_TYPE)
            .questionText(UPDATED_QUESTION_TEXT)
            .questionImage(UPDATED_QUESTION_IMAGE)
            .questionImageContentType(UPDATED_QUESTION_IMAGE_CONTENT_TYPE)
            .answerImage(UPDATED_ANSWER_IMAGE)
            .answerImageContentType(UPDATED_ANSWER_IMAGE_CONTENT_TYPE)
            .imageSideBySide(UPDATED_IMAGE_SIDE_BY_SIDE)
            .option1(UPDATED_OPTION_1)
            .option2(UPDATED_OPTION_2)
            .option3(UPDATED_OPTION_3)
            .option4(UPDATED_OPTION_4)
            .option5(UPDATED_OPTION_5)
            .status(UPDATED_STATUS)
            .weightage(UPDATED_WEIGHTAGE)
            .difficultyLevel(UPDATED_DIFFICULTY_LEVEL)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);

        restQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuestion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQuestion))
            )
            .andExpect(status().isOk());

        // Validate the Question in the database
        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeUpdate);
        Question testQuestion = questionList.get(questionList.size() - 1);
        assertThat(testQuestion.getQuestionImportFile()).isEqualTo(UPDATED_QUESTION_IMPORT_FILE);
        assertThat(testQuestion.getQuestionImportFileContentType()).isEqualTo(UPDATED_QUESTION_IMPORT_FILE_CONTENT_TYPE);
        assertThat(testQuestion.getQuestionText()).isEqualTo(UPDATED_QUESTION_TEXT);
        assertThat(testQuestion.getQuestionImage()).isEqualTo(UPDATED_QUESTION_IMAGE);
        assertThat(testQuestion.getQuestionImageContentType()).isEqualTo(UPDATED_QUESTION_IMAGE_CONTENT_TYPE);
        assertThat(testQuestion.getAnswerImage()).isEqualTo(UPDATED_ANSWER_IMAGE);
        assertThat(testQuestion.getAnswerImageContentType()).isEqualTo(UPDATED_ANSWER_IMAGE_CONTENT_TYPE);
        assertThat(testQuestion.getImageSideBySide()).isEqualTo(UPDATED_IMAGE_SIDE_BY_SIDE);
        assertThat(testQuestion.getOption1()).isEqualTo(UPDATED_OPTION_1);
        assertThat(testQuestion.getOption2()).isEqualTo(UPDATED_OPTION_2);
        assertThat(testQuestion.getOption3()).isEqualTo(UPDATED_OPTION_3);
        assertThat(testQuestion.getOption4()).isEqualTo(UPDATED_OPTION_4);
        assertThat(testQuestion.getOption5()).isEqualTo(UPDATED_OPTION_5);
        assertThat(testQuestion.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testQuestion.getWeightage()).isEqualTo(UPDATED_WEIGHTAGE);
        assertThat(testQuestion.getDifficultyLevel()).isEqualTo(UPDATED_DIFFICULTY_LEVEL);
        assertThat(testQuestion.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testQuestion.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testQuestion.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingQuestion() throws Exception {
        int databaseSizeBeforeUpdate = questionRepository.findAll().size();
        question.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, question.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(question))
            )
            .andExpect(status().isBadRequest());

        // Validate the Question in the database
        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchQuestion() throws Exception {
        int databaseSizeBeforeUpdate = questionRepository.findAll().size();
        question.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(question))
            )
            .andExpect(status().isBadRequest());

        // Validate the Question in the database
        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamQuestion() throws Exception {
        int databaseSizeBeforeUpdate = questionRepository.findAll().size();
        question.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(question)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Question in the database
        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteQuestion() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        int databaseSizeBeforeDelete = questionRepository.findAll().size();

        // Delete the question
        restQuestionMockMvc
            .perform(delete(ENTITY_API_URL_ID, question.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
