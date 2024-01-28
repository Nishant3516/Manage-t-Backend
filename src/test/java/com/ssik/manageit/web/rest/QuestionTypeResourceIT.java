package com.ssik.manageit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ssik.manageit.IntegrationTest;
import com.ssik.manageit.domain.Question;
import com.ssik.manageit.domain.QuestionType;
import com.ssik.manageit.domain.Tenant;
import com.ssik.manageit.repository.QuestionTypeRepository;
import com.ssik.manageit.service.criteria.QuestionTypeCriteria;
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
 * Integration tests for the {@link QuestionTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class QuestionTypeResourceIT {

    private static final String DEFAULT_QUESTION_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_QUESTION_TYPE = "BBBBBBBBBB";

    private static final Integer DEFAULT_MARKS = 1;
    private static final Integer UPDATED_MARKS = 2;
    private static final Integer SMALLER_MARKS = 1 - 1;

    private static final LocalDate DEFAULT_CREATE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATE_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CREATE_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_LAST_MODIFIED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_MODIFIED = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_LAST_MODIFIED = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_CANCEL_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CANCEL_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CANCEL_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/question-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private QuestionTypeRepository questionTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQuestionTypeMockMvc;

    private QuestionType questionType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuestionType createEntity(EntityManager em) {
        QuestionType questionType = new QuestionType()
            .questionType(DEFAULT_QUESTION_TYPE)
            .marks(DEFAULT_MARKS)
            .createDate(DEFAULT_CREATE_DATE)
            .lastModified(DEFAULT_LAST_MODIFIED)
            .cancelDate(DEFAULT_CANCEL_DATE);
        return questionType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuestionType createUpdatedEntity(EntityManager em) {
        QuestionType questionType = new QuestionType()
            .questionType(UPDATED_QUESTION_TYPE)
            .marks(UPDATED_MARKS)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);
        return questionType;
    }

    @BeforeEach
    public void initTest() {
        questionType = createEntity(em);
    }

    @Test
    @Transactional
    void createQuestionType() throws Exception {
        int databaseSizeBeforeCreate = questionTypeRepository.findAll().size();
        // Create the QuestionType
        restQuestionTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(questionType)))
            .andExpect(status().isCreated());

        // Validate the QuestionType in the database
        List<QuestionType> questionTypeList = questionTypeRepository.findAll();
        assertThat(questionTypeList).hasSize(databaseSizeBeforeCreate + 1);
        QuestionType testQuestionType = questionTypeList.get(questionTypeList.size() - 1);
        assertThat(testQuestionType.getQuestionType()).isEqualTo(DEFAULT_QUESTION_TYPE);
        assertThat(testQuestionType.getMarks()).isEqualTo(DEFAULT_MARKS);
        assertThat(testQuestionType.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testQuestionType.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
        assertThat(testQuestionType.getCancelDate()).isEqualTo(DEFAULT_CANCEL_DATE);
    }

    @Test
    @Transactional
    void createQuestionTypeWithExistingId() throws Exception {
        // Create the QuestionType with an existing ID
        questionType.setId(1L);

        int databaseSizeBeforeCreate = questionTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuestionTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(questionType)))
            .andExpect(status().isBadRequest());

        // Validate the QuestionType in the database
        List<QuestionType> questionTypeList = questionTypeRepository.findAll();
        assertThat(questionTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllQuestionTypes() throws Exception {
        // Initialize the database
        questionTypeRepository.saveAndFlush(questionType);

        // Get all the questionTypeList
        restQuestionTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(questionType.getId().intValue())))
            .andExpect(jsonPath("$.[*].questionType").value(hasItem(DEFAULT_QUESTION_TYPE)))
            .andExpect(jsonPath("$.[*].marks").value(hasItem(DEFAULT_MARKS)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].cancelDate").value(hasItem(DEFAULT_CANCEL_DATE.toString())));
    }

    @Test
    @Transactional
    void getQuestionType() throws Exception {
        // Initialize the database
        questionTypeRepository.saveAndFlush(questionType);

        // Get the questionType
        restQuestionTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, questionType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(questionType.getId().intValue()))
            .andExpect(jsonPath("$.questionType").value(DEFAULT_QUESTION_TYPE))
            .andExpect(jsonPath("$.marks").value(DEFAULT_MARKS))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.lastModified").value(DEFAULT_LAST_MODIFIED.toString()))
            .andExpect(jsonPath("$.cancelDate").value(DEFAULT_CANCEL_DATE.toString()));
    }

    @Test
    @Transactional
    void getQuestionTypesByIdFiltering() throws Exception {
        // Initialize the database
        questionTypeRepository.saveAndFlush(questionType);

        Long id = questionType.getId();

        defaultQuestionTypeShouldBeFound("id.equals=" + id);
        defaultQuestionTypeShouldNotBeFound("id.notEquals=" + id);

        defaultQuestionTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultQuestionTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultQuestionTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultQuestionTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllQuestionTypesByQuestionTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        questionTypeRepository.saveAndFlush(questionType);

        // Get all the questionTypeList where questionType equals to DEFAULT_QUESTION_TYPE
        defaultQuestionTypeShouldBeFound("questionType.equals=" + DEFAULT_QUESTION_TYPE);

        // Get all the questionTypeList where questionType equals to UPDATED_QUESTION_TYPE
        defaultQuestionTypeShouldNotBeFound("questionType.equals=" + UPDATED_QUESTION_TYPE);
    }

    @Test
    @Transactional
    void getAllQuestionTypesByQuestionTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        questionTypeRepository.saveAndFlush(questionType);

        // Get all the questionTypeList where questionType not equals to DEFAULT_QUESTION_TYPE
        defaultQuestionTypeShouldNotBeFound("questionType.notEquals=" + DEFAULT_QUESTION_TYPE);

        // Get all the questionTypeList where questionType not equals to UPDATED_QUESTION_TYPE
        defaultQuestionTypeShouldBeFound("questionType.notEquals=" + UPDATED_QUESTION_TYPE);
    }

    @Test
    @Transactional
    void getAllQuestionTypesByQuestionTypeIsInShouldWork() throws Exception {
        // Initialize the database
        questionTypeRepository.saveAndFlush(questionType);

        // Get all the questionTypeList where questionType in DEFAULT_QUESTION_TYPE or UPDATED_QUESTION_TYPE
        defaultQuestionTypeShouldBeFound("questionType.in=" + DEFAULT_QUESTION_TYPE + "," + UPDATED_QUESTION_TYPE);

        // Get all the questionTypeList where questionType equals to UPDATED_QUESTION_TYPE
        defaultQuestionTypeShouldNotBeFound("questionType.in=" + UPDATED_QUESTION_TYPE);
    }

    @Test
    @Transactional
    void getAllQuestionTypesByQuestionTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        questionTypeRepository.saveAndFlush(questionType);

        // Get all the questionTypeList where questionType is not null
        defaultQuestionTypeShouldBeFound("questionType.specified=true");

        // Get all the questionTypeList where questionType is null
        defaultQuestionTypeShouldNotBeFound("questionType.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionTypesByQuestionTypeContainsSomething() throws Exception {
        // Initialize the database
        questionTypeRepository.saveAndFlush(questionType);

        // Get all the questionTypeList where questionType contains DEFAULT_QUESTION_TYPE
        defaultQuestionTypeShouldBeFound("questionType.contains=" + DEFAULT_QUESTION_TYPE);

        // Get all the questionTypeList where questionType contains UPDATED_QUESTION_TYPE
        defaultQuestionTypeShouldNotBeFound("questionType.contains=" + UPDATED_QUESTION_TYPE);
    }

    @Test
    @Transactional
    void getAllQuestionTypesByQuestionTypeNotContainsSomething() throws Exception {
        // Initialize the database
        questionTypeRepository.saveAndFlush(questionType);

        // Get all the questionTypeList where questionType does not contain DEFAULT_QUESTION_TYPE
        defaultQuestionTypeShouldNotBeFound("questionType.doesNotContain=" + DEFAULT_QUESTION_TYPE);

        // Get all the questionTypeList where questionType does not contain UPDATED_QUESTION_TYPE
        defaultQuestionTypeShouldBeFound("questionType.doesNotContain=" + UPDATED_QUESTION_TYPE);
    }

    @Test
    @Transactional
    void getAllQuestionTypesByMarksIsEqualToSomething() throws Exception {
        // Initialize the database
        questionTypeRepository.saveAndFlush(questionType);

        // Get all the questionTypeList where marks equals to DEFAULT_MARKS
        defaultQuestionTypeShouldBeFound("marks.equals=" + DEFAULT_MARKS);

        // Get all the questionTypeList where marks equals to UPDATED_MARKS
        defaultQuestionTypeShouldNotBeFound("marks.equals=" + UPDATED_MARKS);
    }

    @Test
    @Transactional
    void getAllQuestionTypesByMarksIsNotEqualToSomething() throws Exception {
        // Initialize the database
        questionTypeRepository.saveAndFlush(questionType);

        // Get all the questionTypeList where marks not equals to DEFAULT_MARKS
        defaultQuestionTypeShouldNotBeFound("marks.notEquals=" + DEFAULT_MARKS);

        // Get all the questionTypeList where marks not equals to UPDATED_MARKS
        defaultQuestionTypeShouldBeFound("marks.notEquals=" + UPDATED_MARKS);
    }

    @Test
    @Transactional
    void getAllQuestionTypesByMarksIsInShouldWork() throws Exception {
        // Initialize the database
        questionTypeRepository.saveAndFlush(questionType);

        // Get all the questionTypeList where marks in DEFAULT_MARKS or UPDATED_MARKS
        defaultQuestionTypeShouldBeFound("marks.in=" + DEFAULT_MARKS + "," + UPDATED_MARKS);

        // Get all the questionTypeList where marks equals to UPDATED_MARKS
        defaultQuestionTypeShouldNotBeFound("marks.in=" + UPDATED_MARKS);
    }

    @Test
    @Transactional
    void getAllQuestionTypesByMarksIsNullOrNotNull() throws Exception {
        // Initialize the database
        questionTypeRepository.saveAndFlush(questionType);

        // Get all the questionTypeList where marks is not null
        defaultQuestionTypeShouldBeFound("marks.specified=true");

        // Get all the questionTypeList where marks is null
        defaultQuestionTypeShouldNotBeFound("marks.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionTypesByMarksIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        questionTypeRepository.saveAndFlush(questionType);

        // Get all the questionTypeList where marks is greater than or equal to DEFAULT_MARKS
        defaultQuestionTypeShouldBeFound("marks.greaterThanOrEqual=" + DEFAULT_MARKS);

        // Get all the questionTypeList where marks is greater than or equal to UPDATED_MARKS
        defaultQuestionTypeShouldNotBeFound("marks.greaterThanOrEqual=" + UPDATED_MARKS);
    }

    @Test
    @Transactional
    void getAllQuestionTypesByMarksIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        questionTypeRepository.saveAndFlush(questionType);

        // Get all the questionTypeList where marks is less than or equal to DEFAULT_MARKS
        defaultQuestionTypeShouldBeFound("marks.lessThanOrEqual=" + DEFAULT_MARKS);

        // Get all the questionTypeList where marks is less than or equal to SMALLER_MARKS
        defaultQuestionTypeShouldNotBeFound("marks.lessThanOrEqual=" + SMALLER_MARKS);
    }

    @Test
    @Transactional
    void getAllQuestionTypesByMarksIsLessThanSomething() throws Exception {
        // Initialize the database
        questionTypeRepository.saveAndFlush(questionType);

        // Get all the questionTypeList where marks is less than DEFAULT_MARKS
        defaultQuestionTypeShouldNotBeFound("marks.lessThan=" + DEFAULT_MARKS);

        // Get all the questionTypeList where marks is less than UPDATED_MARKS
        defaultQuestionTypeShouldBeFound("marks.lessThan=" + UPDATED_MARKS);
    }

    @Test
    @Transactional
    void getAllQuestionTypesByMarksIsGreaterThanSomething() throws Exception {
        // Initialize the database
        questionTypeRepository.saveAndFlush(questionType);

        // Get all the questionTypeList where marks is greater than DEFAULT_MARKS
        defaultQuestionTypeShouldNotBeFound("marks.greaterThan=" + DEFAULT_MARKS);

        // Get all the questionTypeList where marks is greater than SMALLER_MARKS
        defaultQuestionTypeShouldBeFound("marks.greaterThan=" + SMALLER_MARKS);
    }

    @Test
    @Transactional
    void getAllQuestionTypesByCreateDateIsEqualToSomething() throws Exception {
        // Initialize the database
        questionTypeRepository.saveAndFlush(questionType);

        // Get all the questionTypeList where createDate equals to DEFAULT_CREATE_DATE
        defaultQuestionTypeShouldBeFound("createDate.equals=" + DEFAULT_CREATE_DATE);

        // Get all the questionTypeList where createDate equals to UPDATED_CREATE_DATE
        defaultQuestionTypeShouldNotBeFound("createDate.equals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllQuestionTypesByCreateDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        questionTypeRepository.saveAndFlush(questionType);

        // Get all the questionTypeList where createDate not equals to DEFAULT_CREATE_DATE
        defaultQuestionTypeShouldNotBeFound("createDate.notEquals=" + DEFAULT_CREATE_DATE);

        // Get all the questionTypeList where createDate not equals to UPDATED_CREATE_DATE
        defaultQuestionTypeShouldBeFound("createDate.notEquals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllQuestionTypesByCreateDateIsInShouldWork() throws Exception {
        // Initialize the database
        questionTypeRepository.saveAndFlush(questionType);

        // Get all the questionTypeList where createDate in DEFAULT_CREATE_DATE or UPDATED_CREATE_DATE
        defaultQuestionTypeShouldBeFound("createDate.in=" + DEFAULT_CREATE_DATE + "," + UPDATED_CREATE_DATE);

        // Get all the questionTypeList where createDate equals to UPDATED_CREATE_DATE
        defaultQuestionTypeShouldNotBeFound("createDate.in=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllQuestionTypesByCreateDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        questionTypeRepository.saveAndFlush(questionType);

        // Get all the questionTypeList where createDate is not null
        defaultQuestionTypeShouldBeFound("createDate.specified=true");

        // Get all the questionTypeList where createDate is null
        defaultQuestionTypeShouldNotBeFound("createDate.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionTypesByCreateDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        questionTypeRepository.saveAndFlush(questionType);

        // Get all the questionTypeList where createDate is greater than or equal to DEFAULT_CREATE_DATE
        defaultQuestionTypeShouldBeFound("createDate.greaterThanOrEqual=" + DEFAULT_CREATE_DATE);

        // Get all the questionTypeList where createDate is greater than or equal to UPDATED_CREATE_DATE
        defaultQuestionTypeShouldNotBeFound("createDate.greaterThanOrEqual=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllQuestionTypesByCreateDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        questionTypeRepository.saveAndFlush(questionType);

        // Get all the questionTypeList where createDate is less than or equal to DEFAULT_CREATE_DATE
        defaultQuestionTypeShouldBeFound("createDate.lessThanOrEqual=" + DEFAULT_CREATE_DATE);

        // Get all the questionTypeList where createDate is less than or equal to SMALLER_CREATE_DATE
        defaultQuestionTypeShouldNotBeFound("createDate.lessThanOrEqual=" + SMALLER_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllQuestionTypesByCreateDateIsLessThanSomething() throws Exception {
        // Initialize the database
        questionTypeRepository.saveAndFlush(questionType);

        // Get all the questionTypeList where createDate is less than DEFAULT_CREATE_DATE
        defaultQuestionTypeShouldNotBeFound("createDate.lessThan=" + DEFAULT_CREATE_DATE);

        // Get all the questionTypeList where createDate is less than UPDATED_CREATE_DATE
        defaultQuestionTypeShouldBeFound("createDate.lessThan=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllQuestionTypesByCreateDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        questionTypeRepository.saveAndFlush(questionType);

        // Get all the questionTypeList where createDate is greater than DEFAULT_CREATE_DATE
        defaultQuestionTypeShouldNotBeFound("createDate.greaterThan=" + DEFAULT_CREATE_DATE);

        // Get all the questionTypeList where createDate is greater than SMALLER_CREATE_DATE
        defaultQuestionTypeShouldBeFound("createDate.greaterThan=" + SMALLER_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllQuestionTypesByLastModifiedIsEqualToSomething() throws Exception {
        // Initialize the database
        questionTypeRepository.saveAndFlush(questionType);

        // Get all the questionTypeList where lastModified equals to DEFAULT_LAST_MODIFIED
        defaultQuestionTypeShouldBeFound("lastModified.equals=" + DEFAULT_LAST_MODIFIED);

        // Get all the questionTypeList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultQuestionTypeShouldNotBeFound("lastModified.equals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllQuestionTypesByLastModifiedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        questionTypeRepository.saveAndFlush(questionType);

        // Get all the questionTypeList where lastModified not equals to DEFAULT_LAST_MODIFIED
        defaultQuestionTypeShouldNotBeFound("lastModified.notEquals=" + DEFAULT_LAST_MODIFIED);

        // Get all the questionTypeList where lastModified not equals to UPDATED_LAST_MODIFIED
        defaultQuestionTypeShouldBeFound("lastModified.notEquals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllQuestionTypesByLastModifiedIsInShouldWork() throws Exception {
        // Initialize the database
        questionTypeRepository.saveAndFlush(questionType);

        // Get all the questionTypeList where lastModified in DEFAULT_LAST_MODIFIED or UPDATED_LAST_MODIFIED
        defaultQuestionTypeShouldBeFound("lastModified.in=" + DEFAULT_LAST_MODIFIED + "," + UPDATED_LAST_MODIFIED);

        // Get all the questionTypeList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultQuestionTypeShouldNotBeFound("lastModified.in=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllQuestionTypesByLastModifiedIsNullOrNotNull() throws Exception {
        // Initialize the database
        questionTypeRepository.saveAndFlush(questionType);

        // Get all the questionTypeList where lastModified is not null
        defaultQuestionTypeShouldBeFound("lastModified.specified=true");

        // Get all the questionTypeList where lastModified is null
        defaultQuestionTypeShouldNotBeFound("lastModified.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionTypesByLastModifiedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        questionTypeRepository.saveAndFlush(questionType);

        // Get all the questionTypeList where lastModified is greater than or equal to DEFAULT_LAST_MODIFIED
        defaultQuestionTypeShouldBeFound("lastModified.greaterThanOrEqual=" + DEFAULT_LAST_MODIFIED);

        // Get all the questionTypeList where lastModified is greater than or equal to UPDATED_LAST_MODIFIED
        defaultQuestionTypeShouldNotBeFound("lastModified.greaterThanOrEqual=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllQuestionTypesByLastModifiedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        questionTypeRepository.saveAndFlush(questionType);

        // Get all the questionTypeList where lastModified is less than or equal to DEFAULT_LAST_MODIFIED
        defaultQuestionTypeShouldBeFound("lastModified.lessThanOrEqual=" + DEFAULT_LAST_MODIFIED);

        // Get all the questionTypeList where lastModified is less than or equal to SMALLER_LAST_MODIFIED
        defaultQuestionTypeShouldNotBeFound("lastModified.lessThanOrEqual=" + SMALLER_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllQuestionTypesByLastModifiedIsLessThanSomething() throws Exception {
        // Initialize the database
        questionTypeRepository.saveAndFlush(questionType);

        // Get all the questionTypeList where lastModified is less than DEFAULT_LAST_MODIFIED
        defaultQuestionTypeShouldNotBeFound("lastModified.lessThan=" + DEFAULT_LAST_MODIFIED);

        // Get all the questionTypeList where lastModified is less than UPDATED_LAST_MODIFIED
        defaultQuestionTypeShouldBeFound("lastModified.lessThan=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllQuestionTypesByLastModifiedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        questionTypeRepository.saveAndFlush(questionType);

        // Get all the questionTypeList where lastModified is greater than DEFAULT_LAST_MODIFIED
        defaultQuestionTypeShouldNotBeFound("lastModified.greaterThan=" + DEFAULT_LAST_MODIFIED);

        // Get all the questionTypeList where lastModified is greater than SMALLER_LAST_MODIFIED
        defaultQuestionTypeShouldBeFound("lastModified.greaterThan=" + SMALLER_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllQuestionTypesByCancelDateIsEqualToSomething() throws Exception {
        // Initialize the database
        questionTypeRepository.saveAndFlush(questionType);

        // Get all the questionTypeList where cancelDate equals to DEFAULT_CANCEL_DATE
        defaultQuestionTypeShouldBeFound("cancelDate.equals=" + DEFAULT_CANCEL_DATE);

        // Get all the questionTypeList where cancelDate equals to UPDATED_CANCEL_DATE
        defaultQuestionTypeShouldNotBeFound("cancelDate.equals=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllQuestionTypesByCancelDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        questionTypeRepository.saveAndFlush(questionType);

        // Get all the questionTypeList where cancelDate not equals to DEFAULT_CANCEL_DATE
        defaultQuestionTypeShouldNotBeFound("cancelDate.notEquals=" + DEFAULT_CANCEL_DATE);

        // Get all the questionTypeList where cancelDate not equals to UPDATED_CANCEL_DATE
        defaultQuestionTypeShouldBeFound("cancelDate.notEquals=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllQuestionTypesByCancelDateIsInShouldWork() throws Exception {
        // Initialize the database
        questionTypeRepository.saveAndFlush(questionType);

        // Get all the questionTypeList where cancelDate in DEFAULT_CANCEL_DATE or UPDATED_CANCEL_DATE
        defaultQuestionTypeShouldBeFound("cancelDate.in=" + DEFAULT_CANCEL_DATE + "," + UPDATED_CANCEL_DATE);

        // Get all the questionTypeList where cancelDate equals to UPDATED_CANCEL_DATE
        defaultQuestionTypeShouldNotBeFound("cancelDate.in=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllQuestionTypesByCancelDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        questionTypeRepository.saveAndFlush(questionType);

        // Get all the questionTypeList where cancelDate is not null
        defaultQuestionTypeShouldBeFound("cancelDate.specified=true");

        // Get all the questionTypeList where cancelDate is null
        defaultQuestionTypeShouldNotBeFound("cancelDate.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionTypesByCancelDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        questionTypeRepository.saveAndFlush(questionType);

        // Get all the questionTypeList where cancelDate is greater than or equal to DEFAULT_CANCEL_DATE
        defaultQuestionTypeShouldBeFound("cancelDate.greaterThanOrEqual=" + DEFAULT_CANCEL_DATE);

        // Get all the questionTypeList where cancelDate is greater than or equal to UPDATED_CANCEL_DATE
        defaultQuestionTypeShouldNotBeFound("cancelDate.greaterThanOrEqual=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllQuestionTypesByCancelDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        questionTypeRepository.saveAndFlush(questionType);

        // Get all the questionTypeList where cancelDate is less than or equal to DEFAULT_CANCEL_DATE
        defaultQuestionTypeShouldBeFound("cancelDate.lessThanOrEqual=" + DEFAULT_CANCEL_DATE);

        // Get all the questionTypeList where cancelDate is less than or equal to SMALLER_CANCEL_DATE
        defaultQuestionTypeShouldNotBeFound("cancelDate.lessThanOrEqual=" + SMALLER_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllQuestionTypesByCancelDateIsLessThanSomething() throws Exception {
        // Initialize the database
        questionTypeRepository.saveAndFlush(questionType);

        // Get all the questionTypeList where cancelDate is less than DEFAULT_CANCEL_DATE
        defaultQuestionTypeShouldNotBeFound("cancelDate.lessThan=" + DEFAULT_CANCEL_DATE);

        // Get all the questionTypeList where cancelDate is less than UPDATED_CANCEL_DATE
        defaultQuestionTypeShouldBeFound("cancelDate.lessThan=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllQuestionTypesByCancelDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        questionTypeRepository.saveAndFlush(questionType);

        // Get all the questionTypeList where cancelDate is greater than DEFAULT_CANCEL_DATE
        defaultQuestionTypeShouldNotBeFound("cancelDate.greaterThan=" + DEFAULT_CANCEL_DATE);

        // Get all the questionTypeList where cancelDate is greater than SMALLER_CANCEL_DATE
        defaultQuestionTypeShouldBeFound("cancelDate.greaterThan=" + SMALLER_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllQuestionTypesByQuestionIsEqualToSomething() throws Exception {
        // Initialize the database
        questionTypeRepository.saveAndFlush(questionType);
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
        questionType.addQuestion(question);
        questionTypeRepository.saveAndFlush(questionType);
        Long questionId = question.getId();

        // Get all the questionTypeList where question equals to questionId
        defaultQuestionTypeShouldBeFound("questionId.equals=" + questionId);

        // Get all the questionTypeList where question equals to (questionId + 1)
        defaultQuestionTypeShouldNotBeFound("questionId.equals=" + (questionId + 1));
    }

    @Test
    @Transactional
    void getAllQuestionTypesByTenantIsEqualToSomething() throws Exception {
        // Initialize the database
        questionTypeRepository.saveAndFlush(questionType);
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
        questionType.setTenant(tenant);
        questionTypeRepository.saveAndFlush(questionType);
        Long tenantId = tenant.getId();

        // Get all the questionTypeList where tenant equals to tenantId
        defaultQuestionTypeShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the questionTypeList where tenant equals to (tenantId + 1)
        defaultQuestionTypeShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultQuestionTypeShouldBeFound(String filter) throws Exception {
        restQuestionTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(questionType.getId().intValue())))
            .andExpect(jsonPath("$.[*].questionType").value(hasItem(DEFAULT_QUESTION_TYPE)))
            .andExpect(jsonPath("$.[*].marks").value(hasItem(DEFAULT_MARKS)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].cancelDate").value(hasItem(DEFAULT_CANCEL_DATE.toString())));

        // Check, that the count call also returns 1
        restQuestionTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultQuestionTypeShouldNotBeFound(String filter) throws Exception {
        restQuestionTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restQuestionTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingQuestionType() throws Exception {
        // Get the questionType
        restQuestionTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewQuestionType() throws Exception {
        // Initialize the database
        questionTypeRepository.saveAndFlush(questionType);

        int databaseSizeBeforeUpdate = questionTypeRepository.findAll().size();

        // Update the questionType
        QuestionType updatedQuestionType = questionTypeRepository.findById(questionType.getId()).get();
        // Disconnect from session so that the updates on updatedQuestionType are not directly saved in db
        em.detach(updatedQuestionType);
        updatedQuestionType
            .questionType(UPDATED_QUESTION_TYPE)
            .marks(UPDATED_MARKS)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);

        restQuestionTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedQuestionType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedQuestionType))
            )
            .andExpect(status().isOk());

        // Validate the QuestionType in the database
        List<QuestionType> questionTypeList = questionTypeRepository.findAll();
        assertThat(questionTypeList).hasSize(databaseSizeBeforeUpdate);
        QuestionType testQuestionType = questionTypeList.get(questionTypeList.size() - 1);
        assertThat(testQuestionType.getQuestionType()).isEqualTo(UPDATED_QUESTION_TYPE);
        assertThat(testQuestionType.getMarks()).isEqualTo(UPDATED_MARKS);
        assertThat(testQuestionType.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testQuestionType.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testQuestionType.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void putNonExistingQuestionType() throws Exception {
        int databaseSizeBeforeUpdate = questionTypeRepository.findAll().size();
        questionType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestionTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, questionType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(questionType))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuestionType in the database
        List<QuestionType> questionTypeList = questionTypeRepository.findAll();
        assertThat(questionTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchQuestionType() throws Exception {
        int databaseSizeBeforeUpdate = questionTypeRepository.findAll().size();
        questionType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(questionType))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuestionType in the database
        List<QuestionType> questionTypeList = questionTypeRepository.findAll();
        assertThat(questionTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamQuestionType() throws Exception {
        int databaseSizeBeforeUpdate = questionTypeRepository.findAll().size();
        questionType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(questionType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the QuestionType in the database
        List<QuestionType> questionTypeList = questionTypeRepository.findAll();
        assertThat(questionTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateQuestionTypeWithPatch() throws Exception {
        // Initialize the database
        questionTypeRepository.saveAndFlush(questionType);

        int databaseSizeBeforeUpdate = questionTypeRepository.findAll().size();

        // Update the questionType using partial update
        QuestionType partialUpdatedQuestionType = new QuestionType();
        partialUpdatedQuestionType.setId(questionType.getId());

        partialUpdatedQuestionType.marks(UPDATED_MARKS).createDate(UPDATED_CREATE_DATE).cancelDate(UPDATED_CANCEL_DATE);

        restQuestionTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuestionType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQuestionType))
            )
            .andExpect(status().isOk());

        // Validate the QuestionType in the database
        List<QuestionType> questionTypeList = questionTypeRepository.findAll();
        assertThat(questionTypeList).hasSize(databaseSizeBeforeUpdate);
        QuestionType testQuestionType = questionTypeList.get(questionTypeList.size() - 1);
        assertThat(testQuestionType.getQuestionType()).isEqualTo(DEFAULT_QUESTION_TYPE);
        assertThat(testQuestionType.getMarks()).isEqualTo(UPDATED_MARKS);
        assertThat(testQuestionType.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testQuestionType.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
        assertThat(testQuestionType.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void fullUpdateQuestionTypeWithPatch() throws Exception {
        // Initialize the database
        questionTypeRepository.saveAndFlush(questionType);

        int databaseSizeBeforeUpdate = questionTypeRepository.findAll().size();

        // Update the questionType using partial update
        QuestionType partialUpdatedQuestionType = new QuestionType();
        partialUpdatedQuestionType.setId(questionType.getId());

        partialUpdatedQuestionType
            .questionType(UPDATED_QUESTION_TYPE)
            .marks(UPDATED_MARKS)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);

        restQuestionTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuestionType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQuestionType))
            )
            .andExpect(status().isOk());

        // Validate the QuestionType in the database
        List<QuestionType> questionTypeList = questionTypeRepository.findAll();
        assertThat(questionTypeList).hasSize(databaseSizeBeforeUpdate);
        QuestionType testQuestionType = questionTypeList.get(questionTypeList.size() - 1);
        assertThat(testQuestionType.getQuestionType()).isEqualTo(UPDATED_QUESTION_TYPE);
        assertThat(testQuestionType.getMarks()).isEqualTo(UPDATED_MARKS);
        assertThat(testQuestionType.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testQuestionType.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testQuestionType.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingQuestionType() throws Exception {
        int databaseSizeBeforeUpdate = questionTypeRepository.findAll().size();
        questionType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestionTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, questionType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(questionType))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuestionType in the database
        List<QuestionType> questionTypeList = questionTypeRepository.findAll();
        assertThat(questionTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchQuestionType() throws Exception {
        int databaseSizeBeforeUpdate = questionTypeRepository.findAll().size();
        questionType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(questionType))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuestionType in the database
        List<QuestionType> questionTypeList = questionTypeRepository.findAll();
        assertThat(questionTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamQuestionType() throws Exception {
        int databaseSizeBeforeUpdate = questionTypeRepository.findAll().size();
        questionType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionTypeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(questionType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the QuestionType in the database
        List<QuestionType> questionTypeList = questionTypeRepository.findAll();
        assertThat(questionTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteQuestionType() throws Exception {
        // Initialize the database
        questionTypeRepository.saveAndFlush(questionType);

        int databaseSizeBeforeDelete = questionTypeRepository.findAll().size();

        // Delete the questionType
        restQuestionTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, questionType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<QuestionType> questionTypeList = questionTypeRepository.findAll();
        assertThat(questionTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
