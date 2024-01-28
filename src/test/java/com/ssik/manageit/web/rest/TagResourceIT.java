package com.ssik.manageit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ssik.manageit.IntegrationTest;
import com.ssik.manageit.domain.Question;
import com.ssik.manageit.domain.QuestionPaper;
import com.ssik.manageit.domain.Tag;
import com.ssik.manageit.domain.Tenant;
import com.ssik.manageit.domain.enumeration.TagLevel;
import com.ssik.manageit.repository.TagRepository;
import com.ssik.manageit.service.criteria.TagCriteria;
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
 * Integration tests for the {@link TagResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TagResourceIT {

    private static final String DEFAULT_TAG_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_TAG_TEXT = "BBBBBBBBBB";

    private static final TagLevel DEFAULT_TAG_LEVEL = TagLevel.FIRST;
    private static final TagLevel UPDATED_TAG_LEVEL = TagLevel.SECOND;

    private static final LocalDate DEFAULT_CREATE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATE_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CREATE_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_LAST_MODIFIED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_MODIFIED = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_LAST_MODIFIED = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_CANCEL_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CANCEL_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CANCEL_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/tags";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTagMockMvc;

    private Tag tag;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tag createEntity(EntityManager em) {
        Tag tag = new Tag()
            .tagText(DEFAULT_TAG_TEXT)
            .tagLevel(DEFAULT_TAG_LEVEL)
            .createDate(DEFAULT_CREATE_DATE)
            .lastModified(DEFAULT_LAST_MODIFIED)
            .cancelDate(DEFAULT_CANCEL_DATE);
        return tag;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tag createUpdatedEntity(EntityManager em) {
        Tag tag = new Tag()
            .tagText(UPDATED_TAG_TEXT)
            .tagLevel(UPDATED_TAG_LEVEL)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);
        return tag;
    }

    @BeforeEach
    public void initTest() {
        tag = createEntity(em);
    }

    @Test
    @Transactional
    void createTag() throws Exception {
        int databaseSizeBeforeCreate = tagRepository.findAll().size();
        // Create the Tag
        restTagMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tag)))
            .andExpect(status().isCreated());

        // Validate the Tag in the database
        List<Tag> tagList = tagRepository.findAll();
        assertThat(tagList).hasSize(databaseSizeBeforeCreate + 1);
        Tag testTag = tagList.get(tagList.size() - 1);
        assertThat(testTag.getTagText()).isEqualTo(DEFAULT_TAG_TEXT);
        assertThat(testTag.getTagLevel()).isEqualTo(DEFAULT_TAG_LEVEL);
        assertThat(testTag.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testTag.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
        assertThat(testTag.getCancelDate()).isEqualTo(DEFAULT_CANCEL_DATE);
    }

    @Test
    @Transactional
    void createTagWithExistingId() throws Exception {
        // Create the Tag with an existing ID
        tag.setId(1L);

        int databaseSizeBeforeCreate = tagRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTagMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tag)))
            .andExpect(status().isBadRequest());

        // Validate the Tag in the database
        List<Tag> tagList = tagRepository.findAll();
        assertThat(tagList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTags() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList
        restTagMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tag.getId().intValue())))
            .andExpect(jsonPath("$.[*].tagText").value(hasItem(DEFAULT_TAG_TEXT)))
            .andExpect(jsonPath("$.[*].tagLevel").value(hasItem(DEFAULT_TAG_LEVEL.toString())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].cancelDate").value(hasItem(DEFAULT_CANCEL_DATE.toString())));
    }

    @Test
    @Transactional
    void getTag() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get the tag
        restTagMockMvc
            .perform(get(ENTITY_API_URL_ID, tag.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tag.getId().intValue()))
            .andExpect(jsonPath("$.tagText").value(DEFAULT_TAG_TEXT))
            .andExpect(jsonPath("$.tagLevel").value(DEFAULT_TAG_LEVEL.toString()))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.lastModified").value(DEFAULT_LAST_MODIFIED.toString()))
            .andExpect(jsonPath("$.cancelDate").value(DEFAULT_CANCEL_DATE.toString()));
    }

    @Test
    @Transactional
    void getTagsByIdFiltering() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        Long id = tag.getId();

        defaultTagShouldBeFound("id.equals=" + id);
        defaultTagShouldNotBeFound("id.notEquals=" + id);

        defaultTagShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTagShouldNotBeFound("id.greaterThan=" + id);

        defaultTagShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTagShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTagsByTagTextIsEqualToSomething() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where tagText equals to DEFAULT_TAG_TEXT
        defaultTagShouldBeFound("tagText.equals=" + DEFAULT_TAG_TEXT);

        // Get all the tagList where tagText equals to UPDATED_TAG_TEXT
        defaultTagShouldNotBeFound("tagText.equals=" + UPDATED_TAG_TEXT);
    }

    @Test
    @Transactional
    void getAllTagsByTagTextIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where tagText not equals to DEFAULT_TAG_TEXT
        defaultTagShouldNotBeFound("tagText.notEquals=" + DEFAULT_TAG_TEXT);

        // Get all the tagList where tagText not equals to UPDATED_TAG_TEXT
        defaultTagShouldBeFound("tagText.notEquals=" + UPDATED_TAG_TEXT);
    }

    @Test
    @Transactional
    void getAllTagsByTagTextIsInShouldWork() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where tagText in DEFAULT_TAG_TEXT or UPDATED_TAG_TEXT
        defaultTagShouldBeFound("tagText.in=" + DEFAULT_TAG_TEXT + "," + UPDATED_TAG_TEXT);

        // Get all the tagList where tagText equals to UPDATED_TAG_TEXT
        defaultTagShouldNotBeFound("tagText.in=" + UPDATED_TAG_TEXT);
    }

    @Test
    @Transactional
    void getAllTagsByTagTextIsNullOrNotNull() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where tagText is not null
        defaultTagShouldBeFound("tagText.specified=true");

        // Get all the tagList where tagText is null
        defaultTagShouldNotBeFound("tagText.specified=false");
    }

    @Test
    @Transactional
    void getAllTagsByTagTextContainsSomething() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where tagText contains DEFAULT_TAG_TEXT
        defaultTagShouldBeFound("tagText.contains=" + DEFAULT_TAG_TEXT);

        // Get all the tagList where tagText contains UPDATED_TAG_TEXT
        defaultTagShouldNotBeFound("tagText.contains=" + UPDATED_TAG_TEXT);
    }

    @Test
    @Transactional
    void getAllTagsByTagTextNotContainsSomething() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where tagText does not contain DEFAULT_TAG_TEXT
        defaultTagShouldNotBeFound("tagText.doesNotContain=" + DEFAULT_TAG_TEXT);

        // Get all the tagList where tagText does not contain UPDATED_TAG_TEXT
        defaultTagShouldBeFound("tagText.doesNotContain=" + UPDATED_TAG_TEXT);
    }

    @Test
    @Transactional
    void getAllTagsByTagLevelIsEqualToSomething() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where tagLevel equals to DEFAULT_TAG_LEVEL
        defaultTagShouldBeFound("tagLevel.equals=" + DEFAULT_TAG_LEVEL);

        // Get all the tagList where tagLevel equals to UPDATED_TAG_LEVEL
        defaultTagShouldNotBeFound("tagLevel.equals=" + UPDATED_TAG_LEVEL);
    }

    @Test
    @Transactional
    void getAllTagsByTagLevelIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where tagLevel not equals to DEFAULT_TAG_LEVEL
        defaultTagShouldNotBeFound("tagLevel.notEquals=" + DEFAULT_TAG_LEVEL);

        // Get all the tagList where tagLevel not equals to UPDATED_TAG_LEVEL
        defaultTagShouldBeFound("tagLevel.notEquals=" + UPDATED_TAG_LEVEL);
    }

    @Test
    @Transactional
    void getAllTagsByTagLevelIsInShouldWork() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where tagLevel in DEFAULT_TAG_LEVEL or UPDATED_TAG_LEVEL
        defaultTagShouldBeFound("tagLevel.in=" + DEFAULT_TAG_LEVEL + "," + UPDATED_TAG_LEVEL);

        // Get all the tagList where tagLevel equals to UPDATED_TAG_LEVEL
        defaultTagShouldNotBeFound("tagLevel.in=" + UPDATED_TAG_LEVEL);
    }

    @Test
    @Transactional
    void getAllTagsByTagLevelIsNullOrNotNull() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where tagLevel is not null
        defaultTagShouldBeFound("tagLevel.specified=true");

        // Get all the tagList where tagLevel is null
        defaultTagShouldNotBeFound("tagLevel.specified=false");
    }

    @Test
    @Transactional
    void getAllTagsByCreateDateIsEqualToSomething() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where createDate equals to DEFAULT_CREATE_DATE
        defaultTagShouldBeFound("createDate.equals=" + DEFAULT_CREATE_DATE);

        // Get all the tagList where createDate equals to UPDATED_CREATE_DATE
        defaultTagShouldNotBeFound("createDate.equals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllTagsByCreateDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where createDate not equals to DEFAULT_CREATE_DATE
        defaultTagShouldNotBeFound("createDate.notEquals=" + DEFAULT_CREATE_DATE);

        // Get all the tagList where createDate not equals to UPDATED_CREATE_DATE
        defaultTagShouldBeFound("createDate.notEquals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllTagsByCreateDateIsInShouldWork() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where createDate in DEFAULT_CREATE_DATE or UPDATED_CREATE_DATE
        defaultTagShouldBeFound("createDate.in=" + DEFAULT_CREATE_DATE + "," + UPDATED_CREATE_DATE);

        // Get all the tagList where createDate equals to UPDATED_CREATE_DATE
        defaultTagShouldNotBeFound("createDate.in=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllTagsByCreateDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where createDate is not null
        defaultTagShouldBeFound("createDate.specified=true");

        // Get all the tagList where createDate is null
        defaultTagShouldNotBeFound("createDate.specified=false");
    }

    @Test
    @Transactional
    void getAllTagsByCreateDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where createDate is greater than or equal to DEFAULT_CREATE_DATE
        defaultTagShouldBeFound("createDate.greaterThanOrEqual=" + DEFAULT_CREATE_DATE);

        // Get all the tagList where createDate is greater than or equal to UPDATED_CREATE_DATE
        defaultTagShouldNotBeFound("createDate.greaterThanOrEqual=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllTagsByCreateDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where createDate is less than or equal to DEFAULT_CREATE_DATE
        defaultTagShouldBeFound("createDate.lessThanOrEqual=" + DEFAULT_CREATE_DATE);

        // Get all the tagList where createDate is less than or equal to SMALLER_CREATE_DATE
        defaultTagShouldNotBeFound("createDate.lessThanOrEqual=" + SMALLER_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllTagsByCreateDateIsLessThanSomething() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where createDate is less than DEFAULT_CREATE_DATE
        defaultTagShouldNotBeFound("createDate.lessThan=" + DEFAULT_CREATE_DATE);

        // Get all the tagList where createDate is less than UPDATED_CREATE_DATE
        defaultTagShouldBeFound("createDate.lessThan=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllTagsByCreateDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where createDate is greater than DEFAULT_CREATE_DATE
        defaultTagShouldNotBeFound("createDate.greaterThan=" + DEFAULT_CREATE_DATE);

        // Get all the tagList where createDate is greater than SMALLER_CREATE_DATE
        defaultTagShouldBeFound("createDate.greaterThan=" + SMALLER_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllTagsByLastModifiedIsEqualToSomething() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where lastModified equals to DEFAULT_LAST_MODIFIED
        defaultTagShouldBeFound("lastModified.equals=" + DEFAULT_LAST_MODIFIED);

        // Get all the tagList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultTagShouldNotBeFound("lastModified.equals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllTagsByLastModifiedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where lastModified not equals to DEFAULT_LAST_MODIFIED
        defaultTagShouldNotBeFound("lastModified.notEquals=" + DEFAULT_LAST_MODIFIED);

        // Get all the tagList where lastModified not equals to UPDATED_LAST_MODIFIED
        defaultTagShouldBeFound("lastModified.notEquals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllTagsByLastModifiedIsInShouldWork() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where lastModified in DEFAULT_LAST_MODIFIED or UPDATED_LAST_MODIFIED
        defaultTagShouldBeFound("lastModified.in=" + DEFAULT_LAST_MODIFIED + "," + UPDATED_LAST_MODIFIED);

        // Get all the tagList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultTagShouldNotBeFound("lastModified.in=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllTagsByLastModifiedIsNullOrNotNull() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where lastModified is not null
        defaultTagShouldBeFound("lastModified.specified=true");

        // Get all the tagList where lastModified is null
        defaultTagShouldNotBeFound("lastModified.specified=false");
    }

    @Test
    @Transactional
    void getAllTagsByLastModifiedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where lastModified is greater than or equal to DEFAULT_LAST_MODIFIED
        defaultTagShouldBeFound("lastModified.greaterThanOrEqual=" + DEFAULT_LAST_MODIFIED);

        // Get all the tagList where lastModified is greater than or equal to UPDATED_LAST_MODIFIED
        defaultTagShouldNotBeFound("lastModified.greaterThanOrEqual=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllTagsByLastModifiedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where lastModified is less than or equal to DEFAULT_LAST_MODIFIED
        defaultTagShouldBeFound("lastModified.lessThanOrEqual=" + DEFAULT_LAST_MODIFIED);

        // Get all the tagList where lastModified is less than or equal to SMALLER_LAST_MODIFIED
        defaultTagShouldNotBeFound("lastModified.lessThanOrEqual=" + SMALLER_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllTagsByLastModifiedIsLessThanSomething() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where lastModified is less than DEFAULT_LAST_MODIFIED
        defaultTagShouldNotBeFound("lastModified.lessThan=" + DEFAULT_LAST_MODIFIED);

        // Get all the tagList where lastModified is less than UPDATED_LAST_MODIFIED
        defaultTagShouldBeFound("lastModified.lessThan=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllTagsByLastModifiedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where lastModified is greater than DEFAULT_LAST_MODIFIED
        defaultTagShouldNotBeFound("lastModified.greaterThan=" + DEFAULT_LAST_MODIFIED);

        // Get all the tagList where lastModified is greater than SMALLER_LAST_MODIFIED
        defaultTagShouldBeFound("lastModified.greaterThan=" + SMALLER_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllTagsByCancelDateIsEqualToSomething() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where cancelDate equals to DEFAULT_CANCEL_DATE
        defaultTagShouldBeFound("cancelDate.equals=" + DEFAULT_CANCEL_DATE);

        // Get all the tagList where cancelDate equals to UPDATED_CANCEL_DATE
        defaultTagShouldNotBeFound("cancelDate.equals=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllTagsByCancelDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where cancelDate not equals to DEFAULT_CANCEL_DATE
        defaultTagShouldNotBeFound("cancelDate.notEquals=" + DEFAULT_CANCEL_DATE);

        // Get all the tagList where cancelDate not equals to UPDATED_CANCEL_DATE
        defaultTagShouldBeFound("cancelDate.notEquals=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllTagsByCancelDateIsInShouldWork() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where cancelDate in DEFAULT_CANCEL_DATE or UPDATED_CANCEL_DATE
        defaultTagShouldBeFound("cancelDate.in=" + DEFAULT_CANCEL_DATE + "," + UPDATED_CANCEL_DATE);

        // Get all the tagList where cancelDate equals to UPDATED_CANCEL_DATE
        defaultTagShouldNotBeFound("cancelDate.in=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllTagsByCancelDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where cancelDate is not null
        defaultTagShouldBeFound("cancelDate.specified=true");

        // Get all the tagList where cancelDate is null
        defaultTagShouldNotBeFound("cancelDate.specified=false");
    }

    @Test
    @Transactional
    void getAllTagsByCancelDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where cancelDate is greater than or equal to DEFAULT_CANCEL_DATE
        defaultTagShouldBeFound("cancelDate.greaterThanOrEqual=" + DEFAULT_CANCEL_DATE);

        // Get all the tagList where cancelDate is greater than or equal to UPDATED_CANCEL_DATE
        defaultTagShouldNotBeFound("cancelDate.greaterThanOrEqual=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllTagsByCancelDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where cancelDate is less than or equal to DEFAULT_CANCEL_DATE
        defaultTagShouldBeFound("cancelDate.lessThanOrEqual=" + DEFAULT_CANCEL_DATE);

        // Get all the tagList where cancelDate is less than or equal to SMALLER_CANCEL_DATE
        defaultTagShouldNotBeFound("cancelDate.lessThanOrEqual=" + SMALLER_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllTagsByCancelDateIsLessThanSomething() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where cancelDate is less than DEFAULT_CANCEL_DATE
        defaultTagShouldNotBeFound("cancelDate.lessThan=" + DEFAULT_CANCEL_DATE);

        // Get all the tagList where cancelDate is less than UPDATED_CANCEL_DATE
        defaultTagShouldBeFound("cancelDate.lessThan=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllTagsByCancelDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where cancelDate is greater than DEFAULT_CANCEL_DATE
        defaultTagShouldNotBeFound("cancelDate.greaterThan=" + DEFAULT_CANCEL_DATE);

        // Get all the tagList where cancelDate is greater than SMALLER_CANCEL_DATE
        defaultTagShouldBeFound("cancelDate.greaterThan=" + SMALLER_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllTagsByTenantIsEqualToSomething() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);
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
        tag.setTenant(tenant);
        tagRepository.saveAndFlush(tag);
        Long tenantId = tenant.getId();

        // Get all the tagList where tenant equals to tenantId
        defaultTagShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the tagList where tenant equals to (tenantId + 1)
        defaultTagShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    @Test
    @Transactional
    void getAllTagsByQuestionPaperIsEqualToSomething() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);
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
        tag.addQuestionPaper(questionPaper);
        tagRepository.saveAndFlush(tag);
        Long questionPaperId = questionPaper.getId();

        // Get all the tagList where questionPaper equals to questionPaperId
        defaultTagShouldBeFound("questionPaperId.equals=" + questionPaperId);

        // Get all the tagList where questionPaper equals to (questionPaperId + 1)
        defaultTagShouldNotBeFound("questionPaperId.equals=" + (questionPaperId + 1));
    }

    @Test
    @Transactional
    void getAllTagsByQuestionIsEqualToSomething() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);
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
        tag.addQuestion(question);
        tagRepository.saveAndFlush(tag);
        Long questionId = question.getId();

        // Get all the tagList where question equals to questionId
        defaultTagShouldBeFound("questionId.equals=" + questionId);

        // Get all the tagList where question equals to (questionId + 1)
        defaultTagShouldNotBeFound("questionId.equals=" + (questionId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTagShouldBeFound(String filter) throws Exception {
        restTagMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tag.getId().intValue())))
            .andExpect(jsonPath("$.[*].tagText").value(hasItem(DEFAULT_TAG_TEXT)))
            .andExpect(jsonPath("$.[*].tagLevel").value(hasItem(DEFAULT_TAG_LEVEL.toString())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].cancelDate").value(hasItem(DEFAULT_CANCEL_DATE.toString())));

        // Check, that the count call also returns 1
        restTagMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTagShouldNotBeFound(String filter) throws Exception {
        restTagMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTagMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTag() throws Exception {
        // Get the tag
        restTagMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTag() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        int databaseSizeBeforeUpdate = tagRepository.findAll().size();

        // Update the tag
        Tag updatedTag = tagRepository.findById(tag.getId()).get();
        // Disconnect from session so that the updates on updatedTag are not directly saved in db
        em.detach(updatedTag);
        updatedTag
            .tagText(UPDATED_TAG_TEXT)
            .tagLevel(UPDATED_TAG_LEVEL)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);

        restTagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTag.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTag))
            )
            .andExpect(status().isOk());

        // Validate the Tag in the database
        List<Tag> tagList = tagRepository.findAll();
        assertThat(tagList).hasSize(databaseSizeBeforeUpdate);
        Tag testTag = tagList.get(tagList.size() - 1);
        assertThat(testTag.getTagText()).isEqualTo(UPDATED_TAG_TEXT);
        assertThat(testTag.getTagLevel()).isEqualTo(UPDATED_TAG_LEVEL);
        assertThat(testTag.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testTag.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testTag.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void putNonExistingTag() throws Exception {
        int databaseSizeBeforeUpdate = tagRepository.findAll().size();
        tag.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tag.getId()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tag))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tag in the database
        List<Tag> tagList = tagRepository.findAll();
        assertThat(tagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTag() throws Exception {
        int databaseSizeBeforeUpdate = tagRepository.findAll().size();
        tag.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tag))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tag in the database
        List<Tag> tagList = tagRepository.findAll();
        assertThat(tagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTag() throws Exception {
        int databaseSizeBeforeUpdate = tagRepository.findAll().size();
        tag.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTagMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tag)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tag in the database
        List<Tag> tagList = tagRepository.findAll();
        assertThat(tagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTagWithPatch() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        int databaseSizeBeforeUpdate = tagRepository.findAll().size();

        // Update the tag using partial update
        Tag partialUpdatedTag = new Tag();
        partialUpdatedTag.setId(tag.getId());

        partialUpdatedTag
            .tagText(UPDATED_TAG_TEXT)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);

        restTagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTag.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTag))
            )
            .andExpect(status().isOk());

        // Validate the Tag in the database
        List<Tag> tagList = tagRepository.findAll();
        assertThat(tagList).hasSize(databaseSizeBeforeUpdate);
        Tag testTag = tagList.get(tagList.size() - 1);
        assertThat(testTag.getTagText()).isEqualTo(UPDATED_TAG_TEXT);
        assertThat(testTag.getTagLevel()).isEqualTo(DEFAULT_TAG_LEVEL);
        assertThat(testTag.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testTag.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testTag.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void fullUpdateTagWithPatch() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        int databaseSizeBeforeUpdate = tagRepository.findAll().size();

        // Update the tag using partial update
        Tag partialUpdatedTag = new Tag();
        partialUpdatedTag.setId(tag.getId());

        partialUpdatedTag
            .tagText(UPDATED_TAG_TEXT)
            .tagLevel(UPDATED_TAG_LEVEL)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);

        restTagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTag.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTag))
            )
            .andExpect(status().isOk());

        // Validate the Tag in the database
        List<Tag> tagList = tagRepository.findAll();
        assertThat(tagList).hasSize(databaseSizeBeforeUpdate);
        Tag testTag = tagList.get(tagList.size() - 1);
        assertThat(testTag.getTagText()).isEqualTo(UPDATED_TAG_TEXT);
        assertThat(testTag.getTagLevel()).isEqualTo(UPDATED_TAG_LEVEL);
        assertThat(testTag.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testTag.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testTag.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingTag() throws Exception {
        int databaseSizeBeforeUpdate = tagRepository.findAll().size();
        tag.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tag.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tag))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tag in the database
        List<Tag> tagList = tagRepository.findAll();
        assertThat(tagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTag() throws Exception {
        int databaseSizeBeforeUpdate = tagRepository.findAll().size();
        tag.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tag))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tag in the database
        List<Tag> tagList = tagRepository.findAll();
        assertThat(tagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTag() throws Exception {
        int databaseSizeBeforeUpdate = tagRepository.findAll().size();
        tag.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTagMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(tag)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tag in the database
        List<Tag> tagList = tagRepository.findAll();
        assertThat(tagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTag() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        int databaseSizeBeforeDelete = tagRepository.findAll().size();

        // Delete the tag
        restTagMockMvc.perform(delete(ENTITY_API_URL_ID, tag.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Tag> tagList = tagRepository.findAll();
        assertThat(tagList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
