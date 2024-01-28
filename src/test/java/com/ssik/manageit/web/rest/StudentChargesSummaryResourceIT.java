package com.ssik.manageit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ssik.manageit.IntegrationTest;
import com.ssik.manageit.domain.ClassStudent;
import com.ssik.manageit.domain.SchoolLedgerHead;
import com.ssik.manageit.domain.StudentChargesSummary;
import com.ssik.manageit.repository.StudentChargesSummaryRepository;
import com.ssik.manageit.service.criteria.StudentChargesSummaryCriteria;
import com.ssik.manageit.service.dto.StudentChargesSummaryDTO;
import com.ssik.manageit.service.mapper.StudentChargesSummaryMapper;
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
 * Integration tests for the {@link StudentChargesSummaryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StudentChargesSummaryResourceIT {

    private static final String DEFAULT_SUMMARY_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_SUMMARY_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_FEE_YEAR = "AAAAAAAAAA";
    private static final String UPDATED_FEE_YEAR = "BBBBBBBBBB";

    private static final Integer DEFAULT_DUE_DATE = 1;
    private static final Integer UPDATED_DUE_DATE = 2;
    private static final Integer SMALLER_DUE_DATE = 1 - 1;

    private static final String DEFAULT_APR_SUMMARY = "AAAAAAAAAA";
    private static final String UPDATED_APR_SUMMARY = "BBBBBBBBBB";

    private static final String DEFAULT_MAY_SUMMARY = "AAAAAAAAAA";
    private static final String UPDATED_MAY_SUMMARY = "BBBBBBBBBB";

    private static final String DEFAULT_JUN_SUMMARY = "AAAAAAAAAA";
    private static final String UPDATED_JUN_SUMMARY = "BBBBBBBBBB";

    private static final String DEFAULT_JUL_SUMMARY = "AAAAAAAAAA";
    private static final String UPDATED_JUL_SUMMARY = "BBBBBBBBBB";

    private static final String DEFAULT_AUG_SUMMARY = "AAAAAAAAAA";
    private static final String UPDATED_AUG_SUMMARY = "BBBBBBBBBB";

    private static final String DEFAULT_SEP_SUMMARY = "AAAAAAAAAA";
    private static final String UPDATED_SEP_SUMMARY = "BBBBBBBBBB";

    private static final String DEFAULT_OCT_SUMMARY = "AAAAAAAAAA";
    private static final String UPDATED_OCT_SUMMARY = "BBBBBBBBBB";

    private static final String DEFAULT_NOV_SUMMARY = "AAAAAAAAAA";
    private static final String UPDATED_NOV_SUMMARY = "BBBBBBBBBB";

    private static final String DEFAULT_DEC_SUMMARY = "AAAAAAAAAA";
    private static final String UPDATED_DEC_SUMMARY = "BBBBBBBBBB";

    private static final String DEFAULT_JAN_SUMMARY = "AAAAAAAAAA";
    private static final String UPDATED_JAN_SUMMARY = "BBBBBBBBBB";

    private static final String DEFAULT_FEB_SUMMARY = "AAAAAAAAAA";
    private static final String UPDATED_FEB_SUMMARY = "BBBBBBBBBB";

    private static final String DEFAULT_MAR_SUMMARY = "AAAAAAAAAA";
    private static final String UPDATED_MAR_SUMMARY = "BBBBBBBBBB";

    private static final String DEFAULT_ADDITIONAL_INFO_1 = "AAAAAAAAAA";
    private static final String UPDATED_ADDITIONAL_INFO_1 = "BBBBBBBBBB";

    private static final String DEFAULT_ADDITIONAL_INFO_2 = "AAAAAAAAAA";
    private static final String UPDATED_ADDITIONAL_INFO_2 = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATE_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CREATE_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_LAST_MODIFIED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_MODIFIED = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_LAST_MODIFIED = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_CANCEL_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CANCEL_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CANCEL_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/student-charges-summaries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private StudentChargesSummaryRepository studentChargesSummaryRepository;

    @Autowired
    private StudentChargesSummaryMapper studentChargesSummaryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStudentChargesSummaryMockMvc;

    private StudentChargesSummary studentChargesSummary;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StudentChargesSummary createEntity(EntityManager em) {
        StudentChargesSummary studentChargesSummary = new StudentChargesSummary()
            .summaryType(DEFAULT_SUMMARY_TYPE)
            .feeYear(DEFAULT_FEE_YEAR)
            .dueDate(DEFAULT_DUE_DATE)
            .aprSummary(DEFAULT_APR_SUMMARY)
            .maySummary(DEFAULT_MAY_SUMMARY)
            .junSummary(DEFAULT_JUN_SUMMARY)
            .julSummary(DEFAULT_JUL_SUMMARY)
            .augSummary(DEFAULT_AUG_SUMMARY)
            .sepSummary(DEFAULT_SEP_SUMMARY)
            .octSummary(DEFAULT_OCT_SUMMARY)
            .novSummary(DEFAULT_NOV_SUMMARY)
            .decSummary(DEFAULT_DEC_SUMMARY)
            .janSummary(DEFAULT_JAN_SUMMARY)
            .febSummary(DEFAULT_FEB_SUMMARY)
            .marSummary(DEFAULT_MAR_SUMMARY)
            .additionalInfo1(DEFAULT_ADDITIONAL_INFO_1)
            .additionalInfo2(DEFAULT_ADDITIONAL_INFO_2)
            .createDate(DEFAULT_CREATE_DATE)
            .lastModified(DEFAULT_LAST_MODIFIED)
            .cancelDate(DEFAULT_CANCEL_DATE);
        return studentChargesSummary;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StudentChargesSummary createUpdatedEntity(EntityManager em) {
        StudentChargesSummary studentChargesSummary = new StudentChargesSummary()
            .summaryType(UPDATED_SUMMARY_TYPE)
            .feeYear(UPDATED_FEE_YEAR)
            .dueDate(UPDATED_DUE_DATE)
            .aprSummary(UPDATED_APR_SUMMARY)
            .maySummary(UPDATED_MAY_SUMMARY)
            .junSummary(UPDATED_JUN_SUMMARY)
            .julSummary(UPDATED_JUL_SUMMARY)
            .augSummary(UPDATED_AUG_SUMMARY)
            .sepSummary(UPDATED_SEP_SUMMARY)
            .octSummary(UPDATED_OCT_SUMMARY)
            .novSummary(UPDATED_NOV_SUMMARY)
            .decSummary(UPDATED_DEC_SUMMARY)
            .janSummary(UPDATED_JAN_SUMMARY)
            .febSummary(UPDATED_FEB_SUMMARY)
            .marSummary(UPDATED_MAR_SUMMARY)
            .additionalInfo1(UPDATED_ADDITIONAL_INFO_1)
            .additionalInfo2(UPDATED_ADDITIONAL_INFO_2)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);
        return studentChargesSummary;
    }

    @BeforeEach
    public void initTest() {
        studentChargesSummary = createEntity(em);
    }

    @Test
    @Transactional
    void createStudentChargesSummary() throws Exception {
        int databaseSizeBeforeCreate = studentChargesSummaryRepository.findAll().size();
        // Create the StudentChargesSummary
        StudentChargesSummaryDTO studentChargesSummaryDTO = studentChargesSummaryMapper.toDto(studentChargesSummary);
        restStudentChargesSummaryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(studentChargesSummaryDTO))
            )
            .andExpect(status().isCreated());

        // Validate the StudentChargesSummary in the database
        List<StudentChargesSummary> studentChargesSummaryList = studentChargesSummaryRepository.findAll();
        assertThat(studentChargesSummaryList).hasSize(databaseSizeBeforeCreate + 1);
        StudentChargesSummary testStudentChargesSummary = studentChargesSummaryList.get(studentChargesSummaryList.size() - 1);
        assertThat(testStudentChargesSummary.getSummaryType()).isEqualTo(DEFAULT_SUMMARY_TYPE);
        assertThat(testStudentChargesSummary.getFeeYear()).isEqualTo(DEFAULT_FEE_YEAR);
        assertThat(testStudentChargesSummary.getDueDate()).isEqualTo(DEFAULT_DUE_DATE);
        assertThat(testStudentChargesSummary.getAprSummary()).isEqualTo(DEFAULT_APR_SUMMARY);
        assertThat(testStudentChargesSummary.getMaySummary()).isEqualTo(DEFAULT_MAY_SUMMARY);
        assertThat(testStudentChargesSummary.getJunSummary()).isEqualTo(DEFAULT_JUN_SUMMARY);
        assertThat(testStudentChargesSummary.getJulSummary()).isEqualTo(DEFAULT_JUL_SUMMARY);
        assertThat(testStudentChargesSummary.getAugSummary()).isEqualTo(DEFAULT_AUG_SUMMARY);
        assertThat(testStudentChargesSummary.getSepSummary()).isEqualTo(DEFAULT_SEP_SUMMARY);
        assertThat(testStudentChargesSummary.getOctSummary()).isEqualTo(DEFAULT_OCT_SUMMARY);
        assertThat(testStudentChargesSummary.getNovSummary()).isEqualTo(DEFAULT_NOV_SUMMARY);
        assertThat(testStudentChargesSummary.getDecSummary()).isEqualTo(DEFAULT_DEC_SUMMARY);
        assertThat(testStudentChargesSummary.getJanSummary()).isEqualTo(DEFAULT_JAN_SUMMARY);
        assertThat(testStudentChargesSummary.getFebSummary()).isEqualTo(DEFAULT_FEB_SUMMARY);
        assertThat(testStudentChargesSummary.getMarSummary()).isEqualTo(DEFAULT_MAR_SUMMARY);
        assertThat(testStudentChargesSummary.getAdditionalInfo1()).isEqualTo(DEFAULT_ADDITIONAL_INFO_1);
        assertThat(testStudentChargesSummary.getAdditionalInfo2()).isEqualTo(DEFAULT_ADDITIONAL_INFO_2);
        assertThat(testStudentChargesSummary.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testStudentChargesSummary.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
        assertThat(testStudentChargesSummary.getCancelDate()).isEqualTo(DEFAULT_CANCEL_DATE);
    }

    @Test
    @Transactional
    void createStudentChargesSummaryWithExistingId() throws Exception {
        // Create the StudentChargesSummary with an existing ID
        studentChargesSummary.setId(1L);
        StudentChargesSummaryDTO studentChargesSummaryDTO = studentChargesSummaryMapper.toDto(studentChargesSummary);

        int databaseSizeBeforeCreate = studentChargesSummaryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStudentChargesSummaryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(studentChargesSummaryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentChargesSummary in the database
        List<StudentChargesSummary> studentChargesSummaryList = studentChargesSummaryRepository.findAll();
        assertThat(studentChargesSummaryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummaries() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList
        restStudentChargesSummaryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(studentChargesSummary.getId().intValue())))
            .andExpect(jsonPath("$.[*].summaryType").value(hasItem(DEFAULT_SUMMARY_TYPE)))
            .andExpect(jsonPath("$.[*].feeYear").value(hasItem(DEFAULT_FEE_YEAR)))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE)))
            .andExpect(jsonPath("$.[*].aprSummary").value(hasItem(DEFAULT_APR_SUMMARY)))
            .andExpect(jsonPath("$.[*].maySummary").value(hasItem(DEFAULT_MAY_SUMMARY)))
            .andExpect(jsonPath("$.[*].junSummary").value(hasItem(DEFAULT_JUN_SUMMARY)))
            .andExpect(jsonPath("$.[*].julSummary").value(hasItem(DEFAULT_JUL_SUMMARY)))
            .andExpect(jsonPath("$.[*].augSummary").value(hasItem(DEFAULT_AUG_SUMMARY)))
            .andExpect(jsonPath("$.[*].sepSummary").value(hasItem(DEFAULT_SEP_SUMMARY)))
            .andExpect(jsonPath("$.[*].octSummary").value(hasItem(DEFAULT_OCT_SUMMARY)))
            .andExpect(jsonPath("$.[*].novSummary").value(hasItem(DEFAULT_NOV_SUMMARY)))
            .andExpect(jsonPath("$.[*].decSummary").value(hasItem(DEFAULT_DEC_SUMMARY)))
            .andExpect(jsonPath("$.[*].janSummary").value(hasItem(DEFAULT_JAN_SUMMARY)))
            .andExpect(jsonPath("$.[*].febSummary").value(hasItem(DEFAULT_FEB_SUMMARY)))
            .andExpect(jsonPath("$.[*].marSummary").value(hasItem(DEFAULT_MAR_SUMMARY)))
            .andExpect(jsonPath("$.[*].additionalInfo1").value(hasItem(DEFAULT_ADDITIONAL_INFO_1)))
            .andExpect(jsonPath("$.[*].additionalInfo2").value(hasItem(DEFAULT_ADDITIONAL_INFO_2)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].cancelDate").value(hasItem(DEFAULT_CANCEL_DATE.toString())));
    }

    @Test
    @Transactional
    void getStudentChargesSummary() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get the studentChargesSummary
        restStudentChargesSummaryMockMvc
            .perform(get(ENTITY_API_URL_ID, studentChargesSummary.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(studentChargesSummary.getId().intValue()))
            .andExpect(jsonPath("$.summaryType").value(DEFAULT_SUMMARY_TYPE))
            .andExpect(jsonPath("$.feeYear").value(DEFAULT_FEE_YEAR))
            .andExpect(jsonPath("$.dueDate").value(DEFAULT_DUE_DATE))
            .andExpect(jsonPath("$.aprSummary").value(DEFAULT_APR_SUMMARY))
            .andExpect(jsonPath("$.maySummary").value(DEFAULT_MAY_SUMMARY))
            .andExpect(jsonPath("$.junSummary").value(DEFAULT_JUN_SUMMARY))
            .andExpect(jsonPath("$.julSummary").value(DEFAULT_JUL_SUMMARY))
            .andExpect(jsonPath("$.augSummary").value(DEFAULT_AUG_SUMMARY))
            .andExpect(jsonPath("$.sepSummary").value(DEFAULT_SEP_SUMMARY))
            .andExpect(jsonPath("$.octSummary").value(DEFAULT_OCT_SUMMARY))
            .andExpect(jsonPath("$.novSummary").value(DEFAULT_NOV_SUMMARY))
            .andExpect(jsonPath("$.decSummary").value(DEFAULT_DEC_SUMMARY))
            .andExpect(jsonPath("$.janSummary").value(DEFAULT_JAN_SUMMARY))
            .andExpect(jsonPath("$.febSummary").value(DEFAULT_FEB_SUMMARY))
            .andExpect(jsonPath("$.marSummary").value(DEFAULT_MAR_SUMMARY))
            .andExpect(jsonPath("$.additionalInfo1").value(DEFAULT_ADDITIONAL_INFO_1))
            .andExpect(jsonPath("$.additionalInfo2").value(DEFAULT_ADDITIONAL_INFO_2))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.lastModified").value(DEFAULT_LAST_MODIFIED.toString()))
            .andExpect(jsonPath("$.cancelDate").value(DEFAULT_CANCEL_DATE.toString()));
    }

    @Test
    @Transactional
    void getStudentChargesSummariesByIdFiltering() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        Long id = studentChargesSummary.getId();

        defaultStudentChargesSummaryShouldBeFound("id.equals=" + id);
        defaultStudentChargesSummaryShouldNotBeFound("id.notEquals=" + id);

        defaultStudentChargesSummaryShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultStudentChargesSummaryShouldNotBeFound("id.greaterThan=" + id);

        defaultStudentChargesSummaryShouldBeFound("id.lessThanOrEqual=" + id);
        defaultStudentChargesSummaryShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesBySummaryTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where summaryType equals to DEFAULT_SUMMARY_TYPE
        defaultStudentChargesSummaryShouldBeFound("summaryType.equals=" + DEFAULT_SUMMARY_TYPE);

        // Get all the studentChargesSummaryList where summaryType equals to UPDATED_SUMMARY_TYPE
        defaultStudentChargesSummaryShouldNotBeFound("summaryType.equals=" + UPDATED_SUMMARY_TYPE);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesBySummaryTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where summaryType not equals to DEFAULT_SUMMARY_TYPE
        defaultStudentChargesSummaryShouldNotBeFound("summaryType.notEquals=" + DEFAULT_SUMMARY_TYPE);

        // Get all the studentChargesSummaryList where summaryType not equals to UPDATED_SUMMARY_TYPE
        defaultStudentChargesSummaryShouldBeFound("summaryType.notEquals=" + UPDATED_SUMMARY_TYPE);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesBySummaryTypeIsInShouldWork() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where summaryType in DEFAULT_SUMMARY_TYPE or UPDATED_SUMMARY_TYPE
        defaultStudentChargesSummaryShouldBeFound("summaryType.in=" + DEFAULT_SUMMARY_TYPE + "," + UPDATED_SUMMARY_TYPE);

        // Get all the studentChargesSummaryList where summaryType equals to UPDATED_SUMMARY_TYPE
        defaultStudentChargesSummaryShouldNotBeFound("summaryType.in=" + UPDATED_SUMMARY_TYPE);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesBySummaryTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where summaryType is not null
        defaultStudentChargesSummaryShouldBeFound("summaryType.specified=true");

        // Get all the studentChargesSummaryList where summaryType is null
        defaultStudentChargesSummaryShouldNotBeFound("summaryType.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesBySummaryTypeContainsSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where summaryType contains DEFAULT_SUMMARY_TYPE
        defaultStudentChargesSummaryShouldBeFound("summaryType.contains=" + DEFAULT_SUMMARY_TYPE);

        // Get all the studentChargesSummaryList where summaryType contains UPDATED_SUMMARY_TYPE
        defaultStudentChargesSummaryShouldNotBeFound("summaryType.contains=" + UPDATED_SUMMARY_TYPE);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesBySummaryTypeNotContainsSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where summaryType does not contain DEFAULT_SUMMARY_TYPE
        defaultStudentChargesSummaryShouldNotBeFound("summaryType.doesNotContain=" + DEFAULT_SUMMARY_TYPE);

        // Get all the studentChargesSummaryList where summaryType does not contain UPDATED_SUMMARY_TYPE
        defaultStudentChargesSummaryShouldBeFound("summaryType.doesNotContain=" + UPDATED_SUMMARY_TYPE);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByFeeYearIsEqualToSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where feeYear equals to DEFAULT_FEE_YEAR
        defaultStudentChargesSummaryShouldBeFound("feeYear.equals=" + DEFAULT_FEE_YEAR);

        // Get all the studentChargesSummaryList where feeYear equals to UPDATED_FEE_YEAR
        defaultStudentChargesSummaryShouldNotBeFound("feeYear.equals=" + UPDATED_FEE_YEAR);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByFeeYearIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where feeYear not equals to DEFAULT_FEE_YEAR
        defaultStudentChargesSummaryShouldNotBeFound("feeYear.notEquals=" + DEFAULT_FEE_YEAR);

        // Get all the studentChargesSummaryList where feeYear not equals to UPDATED_FEE_YEAR
        defaultStudentChargesSummaryShouldBeFound("feeYear.notEquals=" + UPDATED_FEE_YEAR);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByFeeYearIsInShouldWork() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where feeYear in DEFAULT_FEE_YEAR or UPDATED_FEE_YEAR
        defaultStudentChargesSummaryShouldBeFound("feeYear.in=" + DEFAULT_FEE_YEAR + "," + UPDATED_FEE_YEAR);

        // Get all the studentChargesSummaryList where feeYear equals to UPDATED_FEE_YEAR
        defaultStudentChargesSummaryShouldNotBeFound("feeYear.in=" + UPDATED_FEE_YEAR);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByFeeYearIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where feeYear is not null
        defaultStudentChargesSummaryShouldBeFound("feeYear.specified=true");

        // Get all the studentChargesSummaryList where feeYear is null
        defaultStudentChargesSummaryShouldNotBeFound("feeYear.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByFeeYearContainsSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where feeYear contains DEFAULT_FEE_YEAR
        defaultStudentChargesSummaryShouldBeFound("feeYear.contains=" + DEFAULT_FEE_YEAR);

        // Get all the studentChargesSummaryList where feeYear contains UPDATED_FEE_YEAR
        defaultStudentChargesSummaryShouldNotBeFound("feeYear.contains=" + UPDATED_FEE_YEAR);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByFeeYearNotContainsSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where feeYear does not contain DEFAULT_FEE_YEAR
        defaultStudentChargesSummaryShouldNotBeFound("feeYear.doesNotContain=" + DEFAULT_FEE_YEAR);

        // Get all the studentChargesSummaryList where feeYear does not contain UPDATED_FEE_YEAR
        defaultStudentChargesSummaryShouldBeFound("feeYear.doesNotContain=" + UPDATED_FEE_YEAR);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByDueDateIsEqualToSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where dueDate equals to DEFAULT_DUE_DATE
        defaultStudentChargesSummaryShouldBeFound("dueDate.equals=" + DEFAULT_DUE_DATE);

        // Get all the studentChargesSummaryList where dueDate equals to UPDATED_DUE_DATE
        defaultStudentChargesSummaryShouldNotBeFound("dueDate.equals=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByDueDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where dueDate not equals to DEFAULT_DUE_DATE
        defaultStudentChargesSummaryShouldNotBeFound("dueDate.notEquals=" + DEFAULT_DUE_DATE);

        // Get all the studentChargesSummaryList where dueDate not equals to UPDATED_DUE_DATE
        defaultStudentChargesSummaryShouldBeFound("dueDate.notEquals=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByDueDateIsInShouldWork() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where dueDate in DEFAULT_DUE_DATE or UPDATED_DUE_DATE
        defaultStudentChargesSummaryShouldBeFound("dueDate.in=" + DEFAULT_DUE_DATE + "," + UPDATED_DUE_DATE);

        // Get all the studentChargesSummaryList where dueDate equals to UPDATED_DUE_DATE
        defaultStudentChargesSummaryShouldNotBeFound("dueDate.in=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByDueDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where dueDate is not null
        defaultStudentChargesSummaryShouldBeFound("dueDate.specified=true");

        // Get all the studentChargesSummaryList where dueDate is null
        defaultStudentChargesSummaryShouldNotBeFound("dueDate.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByDueDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where dueDate is greater than or equal to DEFAULT_DUE_DATE
        defaultStudentChargesSummaryShouldBeFound("dueDate.greaterThanOrEqual=" + DEFAULT_DUE_DATE);

        // Get all the studentChargesSummaryList where dueDate is greater than or equal to UPDATED_DUE_DATE
        defaultStudentChargesSummaryShouldNotBeFound("dueDate.greaterThanOrEqual=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByDueDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where dueDate is less than or equal to DEFAULT_DUE_DATE
        defaultStudentChargesSummaryShouldBeFound("dueDate.lessThanOrEqual=" + DEFAULT_DUE_DATE);

        // Get all the studentChargesSummaryList where dueDate is less than or equal to SMALLER_DUE_DATE
        defaultStudentChargesSummaryShouldNotBeFound("dueDate.lessThanOrEqual=" + SMALLER_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByDueDateIsLessThanSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where dueDate is less than DEFAULT_DUE_DATE
        defaultStudentChargesSummaryShouldNotBeFound("dueDate.lessThan=" + DEFAULT_DUE_DATE);

        // Get all the studentChargesSummaryList where dueDate is less than UPDATED_DUE_DATE
        defaultStudentChargesSummaryShouldBeFound("dueDate.lessThan=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByDueDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where dueDate is greater than DEFAULT_DUE_DATE
        defaultStudentChargesSummaryShouldNotBeFound("dueDate.greaterThan=" + DEFAULT_DUE_DATE);

        // Get all the studentChargesSummaryList where dueDate is greater than SMALLER_DUE_DATE
        defaultStudentChargesSummaryShouldBeFound("dueDate.greaterThan=" + SMALLER_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByAprSummaryIsEqualToSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where aprSummary equals to DEFAULT_APR_SUMMARY
        defaultStudentChargesSummaryShouldBeFound("aprSummary.equals=" + DEFAULT_APR_SUMMARY);

        // Get all the studentChargesSummaryList where aprSummary equals to UPDATED_APR_SUMMARY
        defaultStudentChargesSummaryShouldNotBeFound("aprSummary.equals=" + UPDATED_APR_SUMMARY);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByAprSummaryIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where aprSummary not equals to DEFAULT_APR_SUMMARY
        defaultStudentChargesSummaryShouldNotBeFound("aprSummary.notEquals=" + DEFAULT_APR_SUMMARY);

        // Get all the studentChargesSummaryList where aprSummary not equals to UPDATED_APR_SUMMARY
        defaultStudentChargesSummaryShouldBeFound("aprSummary.notEquals=" + UPDATED_APR_SUMMARY);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByAprSummaryIsInShouldWork() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where aprSummary in DEFAULT_APR_SUMMARY or UPDATED_APR_SUMMARY
        defaultStudentChargesSummaryShouldBeFound("aprSummary.in=" + DEFAULT_APR_SUMMARY + "," + UPDATED_APR_SUMMARY);

        // Get all the studentChargesSummaryList where aprSummary equals to UPDATED_APR_SUMMARY
        defaultStudentChargesSummaryShouldNotBeFound("aprSummary.in=" + UPDATED_APR_SUMMARY);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByAprSummaryIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where aprSummary is not null
        defaultStudentChargesSummaryShouldBeFound("aprSummary.specified=true");

        // Get all the studentChargesSummaryList where aprSummary is null
        defaultStudentChargesSummaryShouldNotBeFound("aprSummary.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByAprSummaryContainsSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where aprSummary contains DEFAULT_APR_SUMMARY
        defaultStudentChargesSummaryShouldBeFound("aprSummary.contains=" + DEFAULT_APR_SUMMARY);

        // Get all the studentChargesSummaryList where aprSummary contains UPDATED_APR_SUMMARY
        defaultStudentChargesSummaryShouldNotBeFound("aprSummary.contains=" + UPDATED_APR_SUMMARY);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByAprSummaryNotContainsSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where aprSummary does not contain DEFAULT_APR_SUMMARY
        defaultStudentChargesSummaryShouldNotBeFound("aprSummary.doesNotContain=" + DEFAULT_APR_SUMMARY);

        // Get all the studentChargesSummaryList where aprSummary does not contain UPDATED_APR_SUMMARY
        defaultStudentChargesSummaryShouldBeFound("aprSummary.doesNotContain=" + UPDATED_APR_SUMMARY);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByMaySummaryIsEqualToSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where maySummary equals to DEFAULT_MAY_SUMMARY
        defaultStudentChargesSummaryShouldBeFound("maySummary.equals=" + DEFAULT_MAY_SUMMARY);

        // Get all the studentChargesSummaryList where maySummary equals to UPDATED_MAY_SUMMARY
        defaultStudentChargesSummaryShouldNotBeFound("maySummary.equals=" + UPDATED_MAY_SUMMARY);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByMaySummaryIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where maySummary not equals to DEFAULT_MAY_SUMMARY
        defaultStudentChargesSummaryShouldNotBeFound("maySummary.notEquals=" + DEFAULT_MAY_SUMMARY);

        // Get all the studentChargesSummaryList where maySummary not equals to UPDATED_MAY_SUMMARY
        defaultStudentChargesSummaryShouldBeFound("maySummary.notEquals=" + UPDATED_MAY_SUMMARY);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByMaySummaryIsInShouldWork() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where maySummary in DEFAULT_MAY_SUMMARY or UPDATED_MAY_SUMMARY
        defaultStudentChargesSummaryShouldBeFound("maySummary.in=" + DEFAULT_MAY_SUMMARY + "," + UPDATED_MAY_SUMMARY);

        // Get all the studentChargesSummaryList where maySummary equals to UPDATED_MAY_SUMMARY
        defaultStudentChargesSummaryShouldNotBeFound("maySummary.in=" + UPDATED_MAY_SUMMARY);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByMaySummaryIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where maySummary is not null
        defaultStudentChargesSummaryShouldBeFound("maySummary.specified=true");

        // Get all the studentChargesSummaryList where maySummary is null
        defaultStudentChargesSummaryShouldNotBeFound("maySummary.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByMaySummaryContainsSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where maySummary contains DEFAULT_MAY_SUMMARY
        defaultStudentChargesSummaryShouldBeFound("maySummary.contains=" + DEFAULT_MAY_SUMMARY);

        // Get all the studentChargesSummaryList where maySummary contains UPDATED_MAY_SUMMARY
        defaultStudentChargesSummaryShouldNotBeFound("maySummary.contains=" + UPDATED_MAY_SUMMARY);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByMaySummaryNotContainsSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where maySummary does not contain DEFAULT_MAY_SUMMARY
        defaultStudentChargesSummaryShouldNotBeFound("maySummary.doesNotContain=" + DEFAULT_MAY_SUMMARY);

        // Get all the studentChargesSummaryList where maySummary does not contain UPDATED_MAY_SUMMARY
        defaultStudentChargesSummaryShouldBeFound("maySummary.doesNotContain=" + UPDATED_MAY_SUMMARY);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByJunSummaryIsEqualToSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where junSummary equals to DEFAULT_JUN_SUMMARY
        defaultStudentChargesSummaryShouldBeFound("junSummary.equals=" + DEFAULT_JUN_SUMMARY);

        // Get all the studentChargesSummaryList where junSummary equals to UPDATED_JUN_SUMMARY
        defaultStudentChargesSummaryShouldNotBeFound("junSummary.equals=" + UPDATED_JUN_SUMMARY);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByJunSummaryIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where junSummary not equals to DEFAULT_JUN_SUMMARY
        defaultStudentChargesSummaryShouldNotBeFound("junSummary.notEquals=" + DEFAULT_JUN_SUMMARY);

        // Get all the studentChargesSummaryList where junSummary not equals to UPDATED_JUN_SUMMARY
        defaultStudentChargesSummaryShouldBeFound("junSummary.notEquals=" + UPDATED_JUN_SUMMARY);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByJunSummaryIsInShouldWork() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where junSummary in DEFAULT_JUN_SUMMARY or UPDATED_JUN_SUMMARY
        defaultStudentChargesSummaryShouldBeFound("junSummary.in=" + DEFAULT_JUN_SUMMARY + "," + UPDATED_JUN_SUMMARY);

        // Get all the studentChargesSummaryList where junSummary equals to UPDATED_JUN_SUMMARY
        defaultStudentChargesSummaryShouldNotBeFound("junSummary.in=" + UPDATED_JUN_SUMMARY);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByJunSummaryIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where junSummary is not null
        defaultStudentChargesSummaryShouldBeFound("junSummary.specified=true");

        // Get all the studentChargesSummaryList where junSummary is null
        defaultStudentChargesSummaryShouldNotBeFound("junSummary.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByJunSummaryContainsSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where junSummary contains DEFAULT_JUN_SUMMARY
        defaultStudentChargesSummaryShouldBeFound("junSummary.contains=" + DEFAULT_JUN_SUMMARY);

        // Get all the studentChargesSummaryList where junSummary contains UPDATED_JUN_SUMMARY
        defaultStudentChargesSummaryShouldNotBeFound("junSummary.contains=" + UPDATED_JUN_SUMMARY);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByJunSummaryNotContainsSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where junSummary does not contain DEFAULT_JUN_SUMMARY
        defaultStudentChargesSummaryShouldNotBeFound("junSummary.doesNotContain=" + DEFAULT_JUN_SUMMARY);

        // Get all the studentChargesSummaryList where junSummary does not contain UPDATED_JUN_SUMMARY
        defaultStudentChargesSummaryShouldBeFound("junSummary.doesNotContain=" + UPDATED_JUN_SUMMARY);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByJulSummaryIsEqualToSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where julSummary equals to DEFAULT_JUL_SUMMARY
        defaultStudentChargesSummaryShouldBeFound("julSummary.equals=" + DEFAULT_JUL_SUMMARY);

        // Get all the studentChargesSummaryList where julSummary equals to UPDATED_JUL_SUMMARY
        defaultStudentChargesSummaryShouldNotBeFound("julSummary.equals=" + UPDATED_JUL_SUMMARY);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByJulSummaryIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where julSummary not equals to DEFAULT_JUL_SUMMARY
        defaultStudentChargesSummaryShouldNotBeFound("julSummary.notEquals=" + DEFAULT_JUL_SUMMARY);

        // Get all the studentChargesSummaryList where julSummary not equals to UPDATED_JUL_SUMMARY
        defaultStudentChargesSummaryShouldBeFound("julSummary.notEquals=" + UPDATED_JUL_SUMMARY);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByJulSummaryIsInShouldWork() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where julSummary in DEFAULT_JUL_SUMMARY or UPDATED_JUL_SUMMARY
        defaultStudentChargesSummaryShouldBeFound("julSummary.in=" + DEFAULT_JUL_SUMMARY + "," + UPDATED_JUL_SUMMARY);

        // Get all the studentChargesSummaryList where julSummary equals to UPDATED_JUL_SUMMARY
        defaultStudentChargesSummaryShouldNotBeFound("julSummary.in=" + UPDATED_JUL_SUMMARY);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByJulSummaryIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where julSummary is not null
        defaultStudentChargesSummaryShouldBeFound("julSummary.specified=true");

        // Get all the studentChargesSummaryList where julSummary is null
        defaultStudentChargesSummaryShouldNotBeFound("julSummary.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByJulSummaryContainsSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where julSummary contains DEFAULT_JUL_SUMMARY
        defaultStudentChargesSummaryShouldBeFound("julSummary.contains=" + DEFAULT_JUL_SUMMARY);

        // Get all the studentChargesSummaryList where julSummary contains UPDATED_JUL_SUMMARY
        defaultStudentChargesSummaryShouldNotBeFound("julSummary.contains=" + UPDATED_JUL_SUMMARY);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByJulSummaryNotContainsSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where julSummary does not contain DEFAULT_JUL_SUMMARY
        defaultStudentChargesSummaryShouldNotBeFound("julSummary.doesNotContain=" + DEFAULT_JUL_SUMMARY);

        // Get all the studentChargesSummaryList where julSummary does not contain UPDATED_JUL_SUMMARY
        defaultStudentChargesSummaryShouldBeFound("julSummary.doesNotContain=" + UPDATED_JUL_SUMMARY);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByAugSummaryIsEqualToSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where augSummary equals to DEFAULT_AUG_SUMMARY
        defaultStudentChargesSummaryShouldBeFound("augSummary.equals=" + DEFAULT_AUG_SUMMARY);

        // Get all the studentChargesSummaryList where augSummary equals to UPDATED_AUG_SUMMARY
        defaultStudentChargesSummaryShouldNotBeFound("augSummary.equals=" + UPDATED_AUG_SUMMARY);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByAugSummaryIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where augSummary not equals to DEFAULT_AUG_SUMMARY
        defaultStudentChargesSummaryShouldNotBeFound("augSummary.notEquals=" + DEFAULT_AUG_SUMMARY);

        // Get all the studentChargesSummaryList where augSummary not equals to UPDATED_AUG_SUMMARY
        defaultStudentChargesSummaryShouldBeFound("augSummary.notEquals=" + UPDATED_AUG_SUMMARY);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByAugSummaryIsInShouldWork() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where augSummary in DEFAULT_AUG_SUMMARY or UPDATED_AUG_SUMMARY
        defaultStudentChargesSummaryShouldBeFound("augSummary.in=" + DEFAULT_AUG_SUMMARY + "," + UPDATED_AUG_SUMMARY);

        // Get all the studentChargesSummaryList where augSummary equals to UPDATED_AUG_SUMMARY
        defaultStudentChargesSummaryShouldNotBeFound("augSummary.in=" + UPDATED_AUG_SUMMARY);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByAugSummaryIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where augSummary is not null
        defaultStudentChargesSummaryShouldBeFound("augSummary.specified=true");

        // Get all the studentChargesSummaryList where augSummary is null
        defaultStudentChargesSummaryShouldNotBeFound("augSummary.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByAugSummaryContainsSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where augSummary contains DEFAULT_AUG_SUMMARY
        defaultStudentChargesSummaryShouldBeFound("augSummary.contains=" + DEFAULT_AUG_SUMMARY);

        // Get all the studentChargesSummaryList where augSummary contains UPDATED_AUG_SUMMARY
        defaultStudentChargesSummaryShouldNotBeFound("augSummary.contains=" + UPDATED_AUG_SUMMARY);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByAugSummaryNotContainsSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where augSummary does not contain DEFAULT_AUG_SUMMARY
        defaultStudentChargesSummaryShouldNotBeFound("augSummary.doesNotContain=" + DEFAULT_AUG_SUMMARY);

        // Get all the studentChargesSummaryList where augSummary does not contain UPDATED_AUG_SUMMARY
        defaultStudentChargesSummaryShouldBeFound("augSummary.doesNotContain=" + UPDATED_AUG_SUMMARY);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesBySepSummaryIsEqualToSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where sepSummary equals to DEFAULT_SEP_SUMMARY
        defaultStudentChargesSummaryShouldBeFound("sepSummary.equals=" + DEFAULT_SEP_SUMMARY);

        // Get all the studentChargesSummaryList where sepSummary equals to UPDATED_SEP_SUMMARY
        defaultStudentChargesSummaryShouldNotBeFound("sepSummary.equals=" + UPDATED_SEP_SUMMARY);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesBySepSummaryIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where sepSummary not equals to DEFAULT_SEP_SUMMARY
        defaultStudentChargesSummaryShouldNotBeFound("sepSummary.notEquals=" + DEFAULT_SEP_SUMMARY);

        // Get all the studentChargesSummaryList where sepSummary not equals to UPDATED_SEP_SUMMARY
        defaultStudentChargesSummaryShouldBeFound("sepSummary.notEquals=" + UPDATED_SEP_SUMMARY);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesBySepSummaryIsInShouldWork() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where sepSummary in DEFAULT_SEP_SUMMARY or UPDATED_SEP_SUMMARY
        defaultStudentChargesSummaryShouldBeFound("sepSummary.in=" + DEFAULT_SEP_SUMMARY + "," + UPDATED_SEP_SUMMARY);

        // Get all the studentChargesSummaryList where sepSummary equals to UPDATED_SEP_SUMMARY
        defaultStudentChargesSummaryShouldNotBeFound("sepSummary.in=" + UPDATED_SEP_SUMMARY);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesBySepSummaryIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where sepSummary is not null
        defaultStudentChargesSummaryShouldBeFound("sepSummary.specified=true");

        // Get all the studentChargesSummaryList where sepSummary is null
        defaultStudentChargesSummaryShouldNotBeFound("sepSummary.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesBySepSummaryContainsSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where sepSummary contains DEFAULT_SEP_SUMMARY
        defaultStudentChargesSummaryShouldBeFound("sepSummary.contains=" + DEFAULT_SEP_SUMMARY);

        // Get all the studentChargesSummaryList where sepSummary contains UPDATED_SEP_SUMMARY
        defaultStudentChargesSummaryShouldNotBeFound("sepSummary.contains=" + UPDATED_SEP_SUMMARY);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesBySepSummaryNotContainsSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where sepSummary does not contain DEFAULT_SEP_SUMMARY
        defaultStudentChargesSummaryShouldNotBeFound("sepSummary.doesNotContain=" + DEFAULT_SEP_SUMMARY);

        // Get all the studentChargesSummaryList where sepSummary does not contain UPDATED_SEP_SUMMARY
        defaultStudentChargesSummaryShouldBeFound("sepSummary.doesNotContain=" + UPDATED_SEP_SUMMARY);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByOctSummaryIsEqualToSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where octSummary equals to DEFAULT_OCT_SUMMARY
        defaultStudentChargesSummaryShouldBeFound("octSummary.equals=" + DEFAULT_OCT_SUMMARY);

        // Get all the studentChargesSummaryList where octSummary equals to UPDATED_OCT_SUMMARY
        defaultStudentChargesSummaryShouldNotBeFound("octSummary.equals=" + UPDATED_OCT_SUMMARY);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByOctSummaryIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where octSummary not equals to DEFAULT_OCT_SUMMARY
        defaultStudentChargesSummaryShouldNotBeFound("octSummary.notEquals=" + DEFAULT_OCT_SUMMARY);

        // Get all the studentChargesSummaryList where octSummary not equals to UPDATED_OCT_SUMMARY
        defaultStudentChargesSummaryShouldBeFound("octSummary.notEquals=" + UPDATED_OCT_SUMMARY);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByOctSummaryIsInShouldWork() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where octSummary in DEFAULT_OCT_SUMMARY or UPDATED_OCT_SUMMARY
        defaultStudentChargesSummaryShouldBeFound("octSummary.in=" + DEFAULT_OCT_SUMMARY + "," + UPDATED_OCT_SUMMARY);

        // Get all the studentChargesSummaryList where octSummary equals to UPDATED_OCT_SUMMARY
        defaultStudentChargesSummaryShouldNotBeFound("octSummary.in=" + UPDATED_OCT_SUMMARY);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByOctSummaryIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where octSummary is not null
        defaultStudentChargesSummaryShouldBeFound("octSummary.specified=true");

        // Get all the studentChargesSummaryList where octSummary is null
        defaultStudentChargesSummaryShouldNotBeFound("octSummary.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByOctSummaryContainsSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where octSummary contains DEFAULT_OCT_SUMMARY
        defaultStudentChargesSummaryShouldBeFound("octSummary.contains=" + DEFAULT_OCT_SUMMARY);

        // Get all the studentChargesSummaryList where octSummary contains UPDATED_OCT_SUMMARY
        defaultStudentChargesSummaryShouldNotBeFound("octSummary.contains=" + UPDATED_OCT_SUMMARY);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByOctSummaryNotContainsSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where octSummary does not contain DEFAULT_OCT_SUMMARY
        defaultStudentChargesSummaryShouldNotBeFound("octSummary.doesNotContain=" + DEFAULT_OCT_SUMMARY);

        // Get all the studentChargesSummaryList where octSummary does not contain UPDATED_OCT_SUMMARY
        defaultStudentChargesSummaryShouldBeFound("octSummary.doesNotContain=" + UPDATED_OCT_SUMMARY);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByNovSummaryIsEqualToSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where novSummary equals to DEFAULT_NOV_SUMMARY
        defaultStudentChargesSummaryShouldBeFound("novSummary.equals=" + DEFAULT_NOV_SUMMARY);

        // Get all the studentChargesSummaryList where novSummary equals to UPDATED_NOV_SUMMARY
        defaultStudentChargesSummaryShouldNotBeFound("novSummary.equals=" + UPDATED_NOV_SUMMARY);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByNovSummaryIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where novSummary not equals to DEFAULT_NOV_SUMMARY
        defaultStudentChargesSummaryShouldNotBeFound("novSummary.notEquals=" + DEFAULT_NOV_SUMMARY);

        // Get all the studentChargesSummaryList where novSummary not equals to UPDATED_NOV_SUMMARY
        defaultStudentChargesSummaryShouldBeFound("novSummary.notEquals=" + UPDATED_NOV_SUMMARY);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByNovSummaryIsInShouldWork() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where novSummary in DEFAULT_NOV_SUMMARY or UPDATED_NOV_SUMMARY
        defaultStudentChargesSummaryShouldBeFound("novSummary.in=" + DEFAULT_NOV_SUMMARY + "," + UPDATED_NOV_SUMMARY);

        // Get all the studentChargesSummaryList where novSummary equals to UPDATED_NOV_SUMMARY
        defaultStudentChargesSummaryShouldNotBeFound("novSummary.in=" + UPDATED_NOV_SUMMARY);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByNovSummaryIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where novSummary is not null
        defaultStudentChargesSummaryShouldBeFound("novSummary.specified=true");

        // Get all the studentChargesSummaryList where novSummary is null
        defaultStudentChargesSummaryShouldNotBeFound("novSummary.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByNovSummaryContainsSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where novSummary contains DEFAULT_NOV_SUMMARY
        defaultStudentChargesSummaryShouldBeFound("novSummary.contains=" + DEFAULT_NOV_SUMMARY);

        // Get all the studentChargesSummaryList where novSummary contains UPDATED_NOV_SUMMARY
        defaultStudentChargesSummaryShouldNotBeFound("novSummary.contains=" + UPDATED_NOV_SUMMARY);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByNovSummaryNotContainsSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where novSummary does not contain DEFAULT_NOV_SUMMARY
        defaultStudentChargesSummaryShouldNotBeFound("novSummary.doesNotContain=" + DEFAULT_NOV_SUMMARY);

        // Get all the studentChargesSummaryList where novSummary does not contain UPDATED_NOV_SUMMARY
        defaultStudentChargesSummaryShouldBeFound("novSummary.doesNotContain=" + UPDATED_NOV_SUMMARY);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByDecSummaryIsEqualToSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where decSummary equals to DEFAULT_DEC_SUMMARY
        defaultStudentChargesSummaryShouldBeFound("decSummary.equals=" + DEFAULT_DEC_SUMMARY);

        // Get all the studentChargesSummaryList where decSummary equals to UPDATED_DEC_SUMMARY
        defaultStudentChargesSummaryShouldNotBeFound("decSummary.equals=" + UPDATED_DEC_SUMMARY);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByDecSummaryIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where decSummary not equals to DEFAULT_DEC_SUMMARY
        defaultStudentChargesSummaryShouldNotBeFound("decSummary.notEquals=" + DEFAULT_DEC_SUMMARY);

        // Get all the studentChargesSummaryList where decSummary not equals to UPDATED_DEC_SUMMARY
        defaultStudentChargesSummaryShouldBeFound("decSummary.notEquals=" + UPDATED_DEC_SUMMARY);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByDecSummaryIsInShouldWork() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where decSummary in DEFAULT_DEC_SUMMARY or UPDATED_DEC_SUMMARY
        defaultStudentChargesSummaryShouldBeFound("decSummary.in=" + DEFAULT_DEC_SUMMARY + "," + UPDATED_DEC_SUMMARY);

        // Get all the studentChargesSummaryList where decSummary equals to UPDATED_DEC_SUMMARY
        defaultStudentChargesSummaryShouldNotBeFound("decSummary.in=" + UPDATED_DEC_SUMMARY);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByDecSummaryIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where decSummary is not null
        defaultStudentChargesSummaryShouldBeFound("decSummary.specified=true");

        // Get all the studentChargesSummaryList where decSummary is null
        defaultStudentChargesSummaryShouldNotBeFound("decSummary.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByDecSummaryContainsSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where decSummary contains DEFAULT_DEC_SUMMARY
        defaultStudentChargesSummaryShouldBeFound("decSummary.contains=" + DEFAULT_DEC_SUMMARY);

        // Get all the studentChargesSummaryList where decSummary contains UPDATED_DEC_SUMMARY
        defaultStudentChargesSummaryShouldNotBeFound("decSummary.contains=" + UPDATED_DEC_SUMMARY);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByDecSummaryNotContainsSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where decSummary does not contain DEFAULT_DEC_SUMMARY
        defaultStudentChargesSummaryShouldNotBeFound("decSummary.doesNotContain=" + DEFAULT_DEC_SUMMARY);

        // Get all the studentChargesSummaryList where decSummary does not contain UPDATED_DEC_SUMMARY
        defaultStudentChargesSummaryShouldBeFound("decSummary.doesNotContain=" + UPDATED_DEC_SUMMARY);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByJanSummaryIsEqualToSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where janSummary equals to DEFAULT_JAN_SUMMARY
        defaultStudentChargesSummaryShouldBeFound("janSummary.equals=" + DEFAULT_JAN_SUMMARY);

        // Get all the studentChargesSummaryList where janSummary equals to UPDATED_JAN_SUMMARY
        defaultStudentChargesSummaryShouldNotBeFound("janSummary.equals=" + UPDATED_JAN_SUMMARY);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByJanSummaryIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where janSummary not equals to DEFAULT_JAN_SUMMARY
        defaultStudentChargesSummaryShouldNotBeFound("janSummary.notEquals=" + DEFAULT_JAN_SUMMARY);

        // Get all the studentChargesSummaryList where janSummary not equals to UPDATED_JAN_SUMMARY
        defaultStudentChargesSummaryShouldBeFound("janSummary.notEquals=" + UPDATED_JAN_SUMMARY);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByJanSummaryIsInShouldWork() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where janSummary in DEFAULT_JAN_SUMMARY or UPDATED_JAN_SUMMARY
        defaultStudentChargesSummaryShouldBeFound("janSummary.in=" + DEFAULT_JAN_SUMMARY + "," + UPDATED_JAN_SUMMARY);

        // Get all the studentChargesSummaryList where janSummary equals to UPDATED_JAN_SUMMARY
        defaultStudentChargesSummaryShouldNotBeFound("janSummary.in=" + UPDATED_JAN_SUMMARY);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByJanSummaryIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where janSummary is not null
        defaultStudentChargesSummaryShouldBeFound("janSummary.specified=true");

        // Get all the studentChargesSummaryList where janSummary is null
        defaultStudentChargesSummaryShouldNotBeFound("janSummary.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByJanSummaryContainsSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where janSummary contains DEFAULT_JAN_SUMMARY
        defaultStudentChargesSummaryShouldBeFound("janSummary.contains=" + DEFAULT_JAN_SUMMARY);

        // Get all the studentChargesSummaryList where janSummary contains UPDATED_JAN_SUMMARY
        defaultStudentChargesSummaryShouldNotBeFound("janSummary.contains=" + UPDATED_JAN_SUMMARY);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByJanSummaryNotContainsSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where janSummary does not contain DEFAULT_JAN_SUMMARY
        defaultStudentChargesSummaryShouldNotBeFound("janSummary.doesNotContain=" + DEFAULT_JAN_SUMMARY);

        // Get all the studentChargesSummaryList where janSummary does not contain UPDATED_JAN_SUMMARY
        defaultStudentChargesSummaryShouldBeFound("janSummary.doesNotContain=" + UPDATED_JAN_SUMMARY);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByFebSummaryIsEqualToSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where febSummary equals to DEFAULT_FEB_SUMMARY
        defaultStudentChargesSummaryShouldBeFound("febSummary.equals=" + DEFAULT_FEB_SUMMARY);

        // Get all the studentChargesSummaryList where febSummary equals to UPDATED_FEB_SUMMARY
        defaultStudentChargesSummaryShouldNotBeFound("febSummary.equals=" + UPDATED_FEB_SUMMARY);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByFebSummaryIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where febSummary not equals to DEFAULT_FEB_SUMMARY
        defaultStudentChargesSummaryShouldNotBeFound("febSummary.notEquals=" + DEFAULT_FEB_SUMMARY);

        // Get all the studentChargesSummaryList where febSummary not equals to UPDATED_FEB_SUMMARY
        defaultStudentChargesSummaryShouldBeFound("febSummary.notEquals=" + UPDATED_FEB_SUMMARY);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByFebSummaryIsInShouldWork() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where febSummary in DEFAULT_FEB_SUMMARY or UPDATED_FEB_SUMMARY
        defaultStudentChargesSummaryShouldBeFound("febSummary.in=" + DEFAULT_FEB_SUMMARY + "," + UPDATED_FEB_SUMMARY);

        // Get all the studentChargesSummaryList where febSummary equals to UPDATED_FEB_SUMMARY
        defaultStudentChargesSummaryShouldNotBeFound("febSummary.in=" + UPDATED_FEB_SUMMARY);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByFebSummaryIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where febSummary is not null
        defaultStudentChargesSummaryShouldBeFound("febSummary.specified=true");

        // Get all the studentChargesSummaryList where febSummary is null
        defaultStudentChargesSummaryShouldNotBeFound("febSummary.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByFebSummaryContainsSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where febSummary contains DEFAULT_FEB_SUMMARY
        defaultStudentChargesSummaryShouldBeFound("febSummary.contains=" + DEFAULT_FEB_SUMMARY);

        // Get all the studentChargesSummaryList where febSummary contains UPDATED_FEB_SUMMARY
        defaultStudentChargesSummaryShouldNotBeFound("febSummary.contains=" + UPDATED_FEB_SUMMARY);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByFebSummaryNotContainsSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where febSummary does not contain DEFAULT_FEB_SUMMARY
        defaultStudentChargesSummaryShouldNotBeFound("febSummary.doesNotContain=" + DEFAULT_FEB_SUMMARY);

        // Get all the studentChargesSummaryList where febSummary does not contain UPDATED_FEB_SUMMARY
        defaultStudentChargesSummaryShouldBeFound("febSummary.doesNotContain=" + UPDATED_FEB_SUMMARY);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByMarSummaryIsEqualToSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where marSummary equals to DEFAULT_MAR_SUMMARY
        defaultStudentChargesSummaryShouldBeFound("marSummary.equals=" + DEFAULT_MAR_SUMMARY);

        // Get all the studentChargesSummaryList where marSummary equals to UPDATED_MAR_SUMMARY
        defaultStudentChargesSummaryShouldNotBeFound("marSummary.equals=" + UPDATED_MAR_SUMMARY);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByMarSummaryIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where marSummary not equals to DEFAULT_MAR_SUMMARY
        defaultStudentChargesSummaryShouldNotBeFound("marSummary.notEquals=" + DEFAULT_MAR_SUMMARY);

        // Get all the studentChargesSummaryList where marSummary not equals to UPDATED_MAR_SUMMARY
        defaultStudentChargesSummaryShouldBeFound("marSummary.notEquals=" + UPDATED_MAR_SUMMARY);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByMarSummaryIsInShouldWork() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where marSummary in DEFAULT_MAR_SUMMARY or UPDATED_MAR_SUMMARY
        defaultStudentChargesSummaryShouldBeFound("marSummary.in=" + DEFAULT_MAR_SUMMARY + "," + UPDATED_MAR_SUMMARY);

        // Get all the studentChargesSummaryList where marSummary equals to UPDATED_MAR_SUMMARY
        defaultStudentChargesSummaryShouldNotBeFound("marSummary.in=" + UPDATED_MAR_SUMMARY);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByMarSummaryIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where marSummary is not null
        defaultStudentChargesSummaryShouldBeFound("marSummary.specified=true");

        // Get all the studentChargesSummaryList where marSummary is null
        defaultStudentChargesSummaryShouldNotBeFound("marSummary.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByMarSummaryContainsSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where marSummary contains DEFAULT_MAR_SUMMARY
        defaultStudentChargesSummaryShouldBeFound("marSummary.contains=" + DEFAULT_MAR_SUMMARY);

        // Get all the studentChargesSummaryList where marSummary contains UPDATED_MAR_SUMMARY
        defaultStudentChargesSummaryShouldNotBeFound("marSummary.contains=" + UPDATED_MAR_SUMMARY);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByMarSummaryNotContainsSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where marSummary does not contain DEFAULT_MAR_SUMMARY
        defaultStudentChargesSummaryShouldNotBeFound("marSummary.doesNotContain=" + DEFAULT_MAR_SUMMARY);

        // Get all the studentChargesSummaryList where marSummary does not contain UPDATED_MAR_SUMMARY
        defaultStudentChargesSummaryShouldBeFound("marSummary.doesNotContain=" + UPDATED_MAR_SUMMARY);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByAdditionalInfo1IsEqualToSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where additionalInfo1 equals to DEFAULT_ADDITIONAL_INFO_1
        defaultStudentChargesSummaryShouldBeFound("additionalInfo1.equals=" + DEFAULT_ADDITIONAL_INFO_1);

        // Get all the studentChargesSummaryList where additionalInfo1 equals to UPDATED_ADDITIONAL_INFO_1
        defaultStudentChargesSummaryShouldNotBeFound("additionalInfo1.equals=" + UPDATED_ADDITIONAL_INFO_1);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByAdditionalInfo1IsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where additionalInfo1 not equals to DEFAULT_ADDITIONAL_INFO_1
        defaultStudentChargesSummaryShouldNotBeFound("additionalInfo1.notEquals=" + DEFAULT_ADDITIONAL_INFO_1);

        // Get all the studentChargesSummaryList where additionalInfo1 not equals to UPDATED_ADDITIONAL_INFO_1
        defaultStudentChargesSummaryShouldBeFound("additionalInfo1.notEquals=" + UPDATED_ADDITIONAL_INFO_1);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByAdditionalInfo1IsInShouldWork() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where additionalInfo1 in DEFAULT_ADDITIONAL_INFO_1 or UPDATED_ADDITIONAL_INFO_1
        defaultStudentChargesSummaryShouldBeFound("additionalInfo1.in=" + DEFAULT_ADDITIONAL_INFO_1 + "," + UPDATED_ADDITIONAL_INFO_1);

        // Get all the studentChargesSummaryList where additionalInfo1 equals to UPDATED_ADDITIONAL_INFO_1
        defaultStudentChargesSummaryShouldNotBeFound("additionalInfo1.in=" + UPDATED_ADDITIONAL_INFO_1);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByAdditionalInfo1IsNullOrNotNull() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where additionalInfo1 is not null
        defaultStudentChargesSummaryShouldBeFound("additionalInfo1.specified=true");

        // Get all the studentChargesSummaryList where additionalInfo1 is null
        defaultStudentChargesSummaryShouldNotBeFound("additionalInfo1.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByAdditionalInfo1ContainsSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where additionalInfo1 contains DEFAULT_ADDITIONAL_INFO_1
        defaultStudentChargesSummaryShouldBeFound("additionalInfo1.contains=" + DEFAULT_ADDITIONAL_INFO_1);

        // Get all the studentChargesSummaryList where additionalInfo1 contains UPDATED_ADDITIONAL_INFO_1
        defaultStudentChargesSummaryShouldNotBeFound("additionalInfo1.contains=" + UPDATED_ADDITIONAL_INFO_1);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByAdditionalInfo1NotContainsSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where additionalInfo1 does not contain DEFAULT_ADDITIONAL_INFO_1
        defaultStudentChargesSummaryShouldNotBeFound("additionalInfo1.doesNotContain=" + DEFAULT_ADDITIONAL_INFO_1);

        // Get all the studentChargesSummaryList where additionalInfo1 does not contain UPDATED_ADDITIONAL_INFO_1
        defaultStudentChargesSummaryShouldBeFound("additionalInfo1.doesNotContain=" + UPDATED_ADDITIONAL_INFO_1);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByAdditionalInfo2IsEqualToSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where additionalInfo2 equals to DEFAULT_ADDITIONAL_INFO_2
        defaultStudentChargesSummaryShouldBeFound("additionalInfo2.equals=" + DEFAULT_ADDITIONAL_INFO_2);

        // Get all the studentChargesSummaryList where additionalInfo2 equals to UPDATED_ADDITIONAL_INFO_2
        defaultStudentChargesSummaryShouldNotBeFound("additionalInfo2.equals=" + UPDATED_ADDITIONAL_INFO_2);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByAdditionalInfo2IsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where additionalInfo2 not equals to DEFAULT_ADDITIONAL_INFO_2
        defaultStudentChargesSummaryShouldNotBeFound("additionalInfo2.notEquals=" + DEFAULT_ADDITIONAL_INFO_2);

        // Get all the studentChargesSummaryList where additionalInfo2 not equals to UPDATED_ADDITIONAL_INFO_2
        defaultStudentChargesSummaryShouldBeFound("additionalInfo2.notEquals=" + UPDATED_ADDITIONAL_INFO_2);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByAdditionalInfo2IsInShouldWork() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where additionalInfo2 in DEFAULT_ADDITIONAL_INFO_2 or UPDATED_ADDITIONAL_INFO_2
        defaultStudentChargesSummaryShouldBeFound("additionalInfo2.in=" + DEFAULT_ADDITIONAL_INFO_2 + "," + UPDATED_ADDITIONAL_INFO_2);

        // Get all the studentChargesSummaryList where additionalInfo2 equals to UPDATED_ADDITIONAL_INFO_2
        defaultStudentChargesSummaryShouldNotBeFound("additionalInfo2.in=" + UPDATED_ADDITIONAL_INFO_2);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByAdditionalInfo2IsNullOrNotNull() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where additionalInfo2 is not null
        defaultStudentChargesSummaryShouldBeFound("additionalInfo2.specified=true");

        // Get all the studentChargesSummaryList where additionalInfo2 is null
        defaultStudentChargesSummaryShouldNotBeFound("additionalInfo2.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByAdditionalInfo2ContainsSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where additionalInfo2 contains DEFAULT_ADDITIONAL_INFO_2
        defaultStudentChargesSummaryShouldBeFound("additionalInfo2.contains=" + DEFAULT_ADDITIONAL_INFO_2);

        // Get all the studentChargesSummaryList where additionalInfo2 contains UPDATED_ADDITIONAL_INFO_2
        defaultStudentChargesSummaryShouldNotBeFound("additionalInfo2.contains=" + UPDATED_ADDITIONAL_INFO_2);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByAdditionalInfo2NotContainsSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where additionalInfo2 does not contain DEFAULT_ADDITIONAL_INFO_2
        defaultStudentChargesSummaryShouldNotBeFound("additionalInfo2.doesNotContain=" + DEFAULT_ADDITIONAL_INFO_2);

        // Get all the studentChargesSummaryList where additionalInfo2 does not contain UPDATED_ADDITIONAL_INFO_2
        defaultStudentChargesSummaryShouldBeFound("additionalInfo2.doesNotContain=" + UPDATED_ADDITIONAL_INFO_2);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByCreateDateIsEqualToSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where createDate equals to DEFAULT_CREATE_DATE
        defaultStudentChargesSummaryShouldBeFound("createDate.equals=" + DEFAULT_CREATE_DATE);

        // Get all the studentChargesSummaryList where createDate equals to UPDATED_CREATE_DATE
        defaultStudentChargesSummaryShouldNotBeFound("createDate.equals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByCreateDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where createDate not equals to DEFAULT_CREATE_DATE
        defaultStudentChargesSummaryShouldNotBeFound("createDate.notEquals=" + DEFAULT_CREATE_DATE);

        // Get all the studentChargesSummaryList where createDate not equals to UPDATED_CREATE_DATE
        defaultStudentChargesSummaryShouldBeFound("createDate.notEquals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByCreateDateIsInShouldWork() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where createDate in DEFAULT_CREATE_DATE or UPDATED_CREATE_DATE
        defaultStudentChargesSummaryShouldBeFound("createDate.in=" + DEFAULT_CREATE_DATE + "," + UPDATED_CREATE_DATE);

        // Get all the studentChargesSummaryList where createDate equals to UPDATED_CREATE_DATE
        defaultStudentChargesSummaryShouldNotBeFound("createDate.in=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByCreateDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where createDate is not null
        defaultStudentChargesSummaryShouldBeFound("createDate.specified=true");

        // Get all the studentChargesSummaryList where createDate is null
        defaultStudentChargesSummaryShouldNotBeFound("createDate.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByCreateDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where createDate is greater than or equal to DEFAULT_CREATE_DATE
        defaultStudentChargesSummaryShouldBeFound("createDate.greaterThanOrEqual=" + DEFAULT_CREATE_DATE);

        // Get all the studentChargesSummaryList where createDate is greater than or equal to UPDATED_CREATE_DATE
        defaultStudentChargesSummaryShouldNotBeFound("createDate.greaterThanOrEqual=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByCreateDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where createDate is less than or equal to DEFAULT_CREATE_DATE
        defaultStudentChargesSummaryShouldBeFound("createDate.lessThanOrEqual=" + DEFAULT_CREATE_DATE);

        // Get all the studentChargesSummaryList where createDate is less than or equal to SMALLER_CREATE_DATE
        defaultStudentChargesSummaryShouldNotBeFound("createDate.lessThanOrEqual=" + SMALLER_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByCreateDateIsLessThanSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where createDate is less than DEFAULT_CREATE_DATE
        defaultStudentChargesSummaryShouldNotBeFound("createDate.lessThan=" + DEFAULT_CREATE_DATE);

        // Get all the studentChargesSummaryList where createDate is less than UPDATED_CREATE_DATE
        defaultStudentChargesSummaryShouldBeFound("createDate.lessThan=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByCreateDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where createDate is greater than DEFAULT_CREATE_DATE
        defaultStudentChargesSummaryShouldNotBeFound("createDate.greaterThan=" + DEFAULT_CREATE_DATE);

        // Get all the studentChargesSummaryList where createDate is greater than SMALLER_CREATE_DATE
        defaultStudentChargesSummaryShouldBeFound("createDate.greaterThan=" + SMALLER_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByLastModifiedIsEqualToSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where lastModified equals to DEFAULT_LAST_MODIFIED
        defaultStudentChargesSummaryShouldBeFound("lastModified.equals=" + DEFAULT_LAST_MODIFIED);

        // Get all the studentChargesSummaryList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultStudentChargesSummaryShouldNotBeFound("lastModified.equals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByLastModifiedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where lastModified not equals to DEFAULT_LAST_MODIFIED
        defaultStudentChargesSummaryShouldNotBeFound("lastModified.notEquals=" + DEFAULT_LAST_MODIFIED);

        // Get all the studentChargesSummaryList where lastModified not equals to UPDATED_LAST_MODIFIED
        defaultStudentChargesSummaryShouldBeFound("lastModified.notEquals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByLastModifiedIsInShouldWork() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where lastModified in DEFAULT_LAST_MODIFIED or UPDATED_LAST_MODIFIED
        defaultStudentChargesSummaryShouldBeFound("lastModified.in=" + DEFAULT_LAST_MODIFIED + "," + UPDATED_LAST_MODIFIED);

        // Get all the studentChargesSummaryList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultStudentChargesSummaryShouldNotBeFound("lastModified.in=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByLastModifiedIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where lastModified is not null
        defaultStudentChargesSummaryShouldBeFound("lastModified.specified=true");

        // Get all the studentChargesSummaryList where lastModified is null
        defaultStudentChargesSummaryShouldNotBeFound("lastModified.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByLastModifiedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where lastModified is greater than or equal to DEFAULT_LAST_MODIFIED
        defaultStudentChargesSummaryShouldBeFound("lastModified.greaterThanOrEqual=" + DEFAULT_LAST_MODIFIED);

        // Get all the studentChargesSummaryList where lastModified is greater than or equal to UPDATED_LAST_MODIFIED
        defaultStudentChargesSummaryShouldNotBeFound("lastModified.greaterThanOrEqual=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByLastModifiedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where lastModified is less than or equal to DEFAULT_LAST_MODIFIED
        defaultStudentChargesSummaryShouldBeFound("lastModified.lessThanOrEqual=" + DEFAULT_LAST_MODIFIED);

        // Get all the studentChargesSummaryList where lastModified is less than or equal to SMALLER_LAST_MODIFIED
        defaultStudentChargesSummaryShouldNotBeFound("lastModified.lessThanOrEqual=" + SMALLER_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByLastModifiedIsLessThanSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where lastModified is less than DEFAULT_LAST_MODIFIED
        defaultStudentChargesSummaryShouldNotBeFound("lastModified.lessThan=" + DEFAULT_LAST_MODIFIED);

        // Get all the studentChargesSummaryList where lastModified is less than UPDATED_LAST_MODIFIED
        defaultStudentChargesSummaryShouldBeFound("lastModified.lessThan=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByLastModifiedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where lastModified is greater than DEFAULT_LAST_MODIFIED
        defaultStudentChargesSummaryShouldNotBeFound("lastModified.greaterThan=" + DEFAULT_LAST_MODIFIED);

        // Get all the studentChargesSummaryList where lastModified is greater than SMALLER_LAST_MODIFIED
        defaultStudentChargesSummaryShouldBeFound("lastModified.greaterThan=" + SMALLER_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByCancelDateIsEqualToSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where cancelDate equals to DEFAULT_CANCEL_DATE
        defaultStudentChargesSummaryShouldBeFound("cancelDate.equals=" + DEFAULT_CANCEL_DATE);

        // Get all the studentChargesSummaryList where cancelDate equals to UPDATED_CANCEL_DATE
        defaultStudentChargesSummaryShouldNotBeFound("cancelDate.equals=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByCancelDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where cancelDate not equals to DEFAULT_CANCEL_DATE
        defaultStudentChargesSummaryShouldNotBeFound("cancelDate.notEquals=" + DEFAULT_CANCEL_DATE);

        // Get all the studentChargesSummaryList where cancelDate not equals to UPDATED_CANCEL_DATE
        defaultStudentChargesSummaryShouldBeFound("cancelDate.notEquals=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByCancelDateIsInShouldWork() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where cancelDate in DEFAULT_CANCEL_DATE or UPDATED_CANCEL_DATE
        defaultStudentChargesSummaryShouldBeFound("cancelDate.in=" + DEFAULT_CANCEL_DATE + "," + UPDATED_CANCEL_DATE);

        // Get all the studentChargesSummaryList where cancelDate equals to UPDATED_CANCEL_DATE
        defaultStudentChargesSummaryShouldNotBeFound("cancelDate.in=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByCancelDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where cancelDate is not null
        defaultStudentChargesSummaryShouldBeFound("cancelDate.specified=true");

        // Get all the studentChargesSummaryList where cancelDate is null
        defaultStudentChargesSummaryShouldNotBeFound("cancelDate.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByCancelDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where cancelDate is greater than or equal to DEFAULT_CANCEL_DATE
        defaultStudentChargesSummaryShouldBeFound("cancelDate.greaterThanOrEqual=" + DEFAULT_CANCEL_DATE);

        // Get all the studentChargesSummaryList where cancelDate is greater than or equal to UPDATED_CANCEL_DATE
        defaultStudentChargesSummaryShouldNotBeFound("cancelDate.greaterThanOrEqual=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByCancelDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where cancelDate is less than or equal to DEFAULT_CANCEL_DATE
        defaultStudentChargesSummaryShouldBeFound("cancelDate.lessThanOrEqual=" + DEFAULT_CANCEL_DATE);

        // Get all the studentChargesSummaryList where cancelDate is less than or equal to SMALLER_CANCEL_DATE
        defaultStudentChargesSummaryShouldNotBeFound("cancelDate.lessThanOrEqual=" + SMALLER_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByCancelDateIsLessThanSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where cancelDate is less than DEFAULT_CANCEL_DATE
        defaultStudentChargesSummaryShouldNotBeFound("cancelDate.lessThan=" + DEFAULT_CANCEL_DATE);

        // Get all the studentChargesSummaryList where cancelDate is less than UPDATED_CANCEL_DATE
        defaultStudentChargesSummaryShouldBeFound("cancelDate.lessThan=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByCancelDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        // Get all the studentChargesSummaryList where cancelDate is greater than DEFAULT_CANCEL_DATE
        defaultStudentChargesSummaryShouldNotBeFound("cancelDate.greaterThan=" + DEFAULT_CANCEL_DATE);

        // Get all the studentChargesSummaryList where cancelDate is greater than SMALLER_CANCEL_DATE
        defaultStudentChargesSummaryShouldBeFound("cancelDate.greaterThan=" + SMALLER_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesBySchoolLedgerHeadIsEqualToSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);
        SchoolLedgerHead schoolLedgerHead = SchoolLedgerHeadResourceIT.createEntity(em);
        em.persist(schoolLedgerHead);
        em.flush();
        studentChargesSummary.setSchoolLedgerHead(schoolLedgerHead);
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);
        Long schoolLedgerHeadId = schoolLedgerHead.getId();

        // Get all the studentChargesSummaryList where schoolLedgerHead equals to schoolLedgerHeadId
        defaultStudentChargesSummaryShouldBeFound("schoolLedgerHeadId.equals=" + schoolLedgerHeadId);

        // Get all the studentChargesSummaryList where schoolLedgerHead equals to (schoolLedgerHeadId + 1)
        defaultStudentChargesSummaryShouldNotBeFound("schoolLedgerHeadId.equals=" + (schoolLedgerHeadId + 1));
    }

    @Test
    @Transactional
    void getAllStudentChargesSummariesByClassStudentIsEqualToSomething() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);
        ClassStudent classStudent = ClassStudentResourceIT.createEntity(em);
        em.persist(classStudent);
        em.flush();
        studentChargesSummary.setClassStudent(classStudent);
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);
        Long classStudentId = classStudent.getId();

        // Get all the studentChargesSummaryList where classStudent equals to classStudentId
        defaultStudentChargesSummaryShouldBeFound("classStudentId.equals=" + classStudentId);

        // Get all the studentChargesSummaryList where classStudent equals to (classStudentId + 1)
        defaultStudentChargesSummaryShouldNotBeFound("classStudentId.equals=" + (classStudentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultStudentChargesSummaryShouldBeFound(String filter) throws Exception {
        restStudentChargesSummaryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(studentChargesSummary.getId().intValue())))
            .andExpect(jsonPath("$.[*].summaryType").value(hasItem(DEFAULT_SUMMARY_TYPE)))
            .andExpect(jsonPath("$.[*].feeYear").value(hasItem(DEFAULT_FEE_YEAR)))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE)))
            .andExpect(jsonPath("$.[*].aprSummary").value(hasItem(DEFAULT_APR_SUMMARY)))
            .andExpect(jsonPath("$.[*].maySummary").value(hasItem(DEFAULT_MAY_SUMMARY)))
            .andExpect(jsonPath("$.[*].junSummary").value(hasItem(DEFAULT_JUN_SUMMARY)))
            .andExpect(jsonPath("$.[*].julSummary").value(hasItem(DEFAULT_JUL_SUMMARY)))
            .andExpect(jsonPath("$.[*].augSummary").value(hasItem(DEFAULT_AUG_SUMMARY)))
            .andExpect(jsonPath("$.[*].sepSummary").value(hasItem(DEFAULT_SEP_SUMMARY)))
            .andExpect(jsonPath("$.[*].octSummary").value(hasItem(DEFAULT_OCT_SUMMARY)))
            .andExpect(jsonPath("$.[*].novSummary").value(hasItem(DEFAULT_NOV_SUMMARY)))
            .andExpect(jsonPath("$.[*].decSummary").value(hasItem(DEFAULT_DEC_SUMMARY)))
            .andExpect(jsonPath("$.[*].janSummary").value(hasItem(DEFAULT_JAN_SUMMARY)))
            .andExpect(jsonPath("$.[*].febSummary").value(hasItem(DEFAULT_FEB_SUMMARY)))
            .andExpect(jsonPath("$.[*].marSummary").value(hasItem(DEFAULT_MAR_SUMMARY)))
            .andExpect(jsonPath("$.[*].additionalInfo1").value(hasItem(DEFAULT_ADDITIONAL_INFO_1)))
            .andExpect(jsonPath("$.[*].additionalInfo2").value(hasItem(DEFAULT_ADDITIONAL_INFO_2)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].cancelDate").value(hasItem(DEFAULT_CANCEL_DATE.toString())));

        // Check, that the count call also returns 1
        restStudentChargesSummaryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultStudentChargesSummaryShouldNotBeFound(String filter) throws Exception {
        restStudentChargesSummaryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restStudentChargesSummaryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingStudentChargesSummary() throws Exception {
        // Get the studentChargesSummary
        restStudentChargesSummaryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewStudentChargesSummary() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        int databaseSizeBeforeUpdate = studentChargesSummaryRepository.findAll().size();

        // Update the studentChargesSummary
        StudentChargesSummary updatedStudentChargesSummary = studentChargesSummaryRepository.findById(studentChargesSummary.getId()).get();
        // Disconnect from session so that the updates on updatedStudentChargesSummary are not directly saved in db
        em.detach(updatedStudentChargesSummary);
        updatedStudentChargesSummary
            .summaryType(UPDATED_SUMMARY_TYPE)
            .feeYear(UPDATED_FEE_YEAR)
            .dueDate(UPDATED_DUE_DATE)
            .aprSummary(UPDATED_APR_SUMMARY)
            .maySummary(UPDATED_MAY_SUMMARY)
            .junSummary(UPDATED_JUN_SUMMARY)
            .julSummary(UPDATED_JUL_SUMMARY)
            .augSummary(UPDATED_AUG_SUMMARY)
            .sepSummary(UPDATED_SEP_SUMMARY)
            .octSummary(UPDATED_OCT_SUMMARY)
            .novSummary(UPDATED_NOV_SUMMARY)
            .decSummary(UPDATED_DEC_SUMMARY)
            .janSummary(UPDATED_JAN_SUMMARY)
            .febSummary(UPDATED_FEB_SUMMARY)
            .marSummary(UPDATED_MAR_SUMMARY)
            .additionalInfo1(UPDATED_ADDITIONAL_INFO_1)
            .additionalInfo2(UPDATED_ADDITIONAL_INFO_2)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);
        StudentChargesSummaryDTO studentChargesSummaryDTO = studentChargesSummaryMapper.toDto(updatedStudentChargesSummary);

        restStudentChargesSummaryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, studentChargesSummaryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(studentChargesSummaryDTO))
            )
            .andExpect(status().isOk());

        // Validate the StudentChargesSummary in the database
        List<StudentChargesSummary> studentChargesSummaryList = studentChargesSummaryRepository.findAll();
        assertThat(studentChargesSummaryList).hasSize(databaseSizeBeforeUpdate);
        StudentChargesSummary testStudentChargesSummary = studentChargesSummaryList.get(studentChargesSummaryList.size() - 1);
        assertThat(testStudentChargesSummary.getSummaryType()).isEqualTo(UPDATED_SUMMARY_TYPE);
        assertThat(testStudentChargesSummary.getFeeYear()).isEqualTo(UPDATED_FEE_YEAR);
        assertThat(testStudentChargesSummary.getDueDate()).isEqualTo(UPDATED_DUE_DATE);
        assertThat(testStudentChargesSummary.getAprSummary()).isEqualTo(UPDATED_APR_SUMMARY);
        assertThat(testStudentChargesSummary.getMaySummary()).isEqualTo(UPDATED_MAY_SUMMARY);
        assertThat(testStudentChargesSummary.getJunSummary()).isEqualTo(UPDATED_JUN_SUMMARY);
        assertThat(testStudentChargesSummary.getJulSummary()).isEqualTo(UPDATED_JUL_SUMMARY);
        assertThat(testStudentChargesSummary.getAugSummary()).isEqualTo(UPDATED_AUG_SUMMARY);
        assertThat(testStudentChargesSummary.getSepSummary()).isEqualTo(UPDATED_SEP_SUMMARY);
        assertThat(testStudentChargesSummary.getOctSummary()).isEqualTo(UPDATED_OCT_SUMMARY);
        assertThat(testStudentChargesSummary.getNovSummary()).isEqualTo(UPDATED_NOV_SUMMARY);
        assertThat(testStudentChargesSummary.getDecSummary()).isEqualTo(UPDATED_DEC_SUMMARY);
        assertThat(testStudentChargesSummary.getJanSummary()).isEqualTo(UPDATED_JAN_SUMMARY);
        assertThat(testStudentChargesSummary.getFebSummary()).isEqualTo(UPDATED_FEB_SUMMARY);
        assertThat(testStudentChargesSummary.getMarSummary()).isEqualTo(UPDATED_MAR_SUMMARY);
        assertThat(testStudentChargesSummary.getAdditionalInfo1()).isEqualTo(UPDATED_ADDITIONAL_INFO_1);
        assertThat(testStudentChargesSummary.getAdditionalInfo2()).isEqualTo(UPDATED_ADDITIONAL_INFO_2);
        assertThat(testStudentChargesSummary.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testStudentChargesSummary.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testStudentChargesSummary.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void putNonExistingStudentChargesSummary() throws Exception {
        int databaseSizeBeforeUpdate = studentChargesSummaryRepository.findAll().size();
        studentChargesSummary.setId(count.incrementAndGet());

        // Create the StudentChargesSummary
        StudentChargesSummaryDTO studentChargesSummaryDTO = studentChargesSummaryMapper.toDto(studentChargesSummary);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudentChargesSummaryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, studentChargesSummaryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(studentChargesSummaryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentChargesSummary in the database
        List<StudentChargesSummary> studentChargesSummaryList = studentChargesSummaryRepository.findAll();
        assertThat(studentChargesSummaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStudentChargesSummary() throws Exception {
        int databaseSizeBeforeUpdate = studentChargesSummaryRepository.findAll().size();
        studentChargesSummary.setId(count.incrementAndGet());

        // Create the StudentChargesSummary
        StudentChargesSummaryDTO studentChargesSummaryDTO = studentChargesSummaryMapper.toDto(studentChargesSummary);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentChargesSummaryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(studentChargesSummaryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentChargesSummary in the database
        List<StudentChargesSummary> studentChargesSummaryList = studentChargesSummaryRepository.findAll();
        assertThat(studentChargesSummaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStudentChargesSummary() throws Exception {
        int databaseSizeBeforeUpdate = studentChargesSummaryRepository.findAll().size();
        studentChargesSummary.setId(count.incrementAndGet());

        // Create the StudentChargesSummary
        StudentChargesSummaryDTO studentChargesSummaryDTO = studentChargesSummaryMapper.toDto(studentChargesSummary);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentChargesSummaryMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(studentChargesSummaryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StudentChargesSummary in the database
        List<StudentChargesSummary> studentChargesSummaryList = studentChargesSummaryRepository.findAll();
        assertThat(studentChargesSummaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStudentChargesSummaryWithPatch() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        int databaseSizeBeforeUpdate = studentChargesSummaryRepository.findAll().size();

        // Update the studentChargesSummary using partial update
        StudentChargesSummary partialUpdatedStudentChargesSummary = new StudentChargesSummary();
        partialUpdatedStudentChargesSummary.setId(studentChargesSummary.getId());

        partialUpdatedStudentChargesSummary
            .summaryType(UPDATED_SUMMARY_TYPE)
            .feeYear(UPDATED_FEE_YEAR)
            .dueDate(UPDATED_DUE_DATE)
            .julSummary(UPDATED_JUL_SUMMARY)
            .augSummary(UPDATED_AUG_SUMMARY)
            .sepSummary(UPDATED_SEP_SUMMARY)
            .octSummary(UPDATED_OCT_SUMMARY)
            .novSummary(UPDATED_NOV_SUMMARY)
            .decSummary(UPDATED_DEC_SUMMARY)
            .janSummary(UPDATED_JAN_SUMMARY)
            .febSummary(UPDATED_FEB_SUMMARY)
            .marSummary(UPDATED_MAR_SUMMARY)
            .additionalInfo2(UPDATED_ADDITIONAL_INFO_2);

        restStudentChargesSummaryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStudentChargesSummary.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStudentChargesSummary))
            )
            .andExpect(status().isOk());

        // Validate the StudentChargesSummary in the database
        List<StudentChargesSummary> studentChargesSummaryList = studentChargesSummaryRepository.findAll();
        assertThat(studentChargesSummaryList).hasSize(databaseSizeBeforeUpdate);
        StudentChargesSummary testStudentChargesSummary = studentChargesSummaryList.get(studentChargesSummaryList.size() - 1);
        assertThat(testStudentChargesSummary.getSummaryType()).isEqualTo(UPDATED_SUMMARY_TYPE);
        assertThat(testStudentChargesSummary.getFeeYear()).isEqualTo(UPDATED_FEE_YEAR);
        assertThat(testStudentChargesSummary.getDueDate()).isEqualTo(UPDATED_DUE_DATE);
        assertThat(testStudentChargesSummary.getAprSummary()).isEqualTo(DEFAULT_APR_SUMMARY);
        assertThat(testStudentChargesSummary.getMaySummary()).isEqualTo(DEFAULT_MAY_SUMMARY);
        assertThat(testStudentChargesSummary.getJunSummary()).isEqualTo(DEFAULT_JUN_SUMMARY);
        assertThat(testStudentChargesSummary.getJulSummary()).isEqualTo(UPDATED_JUL_SUMMARY);
        assertThat(testStudentChargesSummary.getAugSummary()).isEqualTo(UPDATED_AUG_SUMMARY);
        assertThat(testStudentChargesSummary.getSepSummary()).isEqualTo(UPDATED_SEP_SUMMARY);
        assertThat(testStudentChargesSummary.getOctSummary()).isEqualTo(UPDATED_OCT_SUMMARY);
        assertThat(testStudentChargesSummary.getNovSummary()).isEqualTo(UPDATED_NOV_SUMMARY);
        assertThat(testStudentChargesSummary.getDecSummary()).isEqualTo(UPDATED_DEC_SUMMARY);
        assertThat(testStudentChargesSummary.getJanSummary()).isEqualTo(UPDATED_JAN_SUMMARY);
        assertThat(testStudentChargesSummary.getFebSummary()).isEqualTo(UPDATED_FEB_SUMMARY);
        assertThat(testStudentChargesSummary.getMarSummary()).isEqualTo(UPDATED_MAR_SUMMARY);
        assertThat(testStudentChargesSummary.getAdditionalInfo1()).isEqualTo(DEFAULT_ADDITIONAL_INFO_1);
        assertThat(testStudentChargesSummary.getAdditionalInfo2()).isEqualTo(UPDATED_ADDITIONAL_INFO_2);
        assertThat(testStudentChargesSummary.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testStudentChargesSummary.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
        assertThat(testStudentChargesSummary.getCancelDate()).isEqualTo(DEFAULT_CANCEL_DATE);
    }

    @Test
    @Transactional
    void fullUpdateStudentChargesSummaryWithPatch() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        int databaseSizeBeforeUpdate = studentChargesSummaryRepository.findAll().size();

        // Update the studentChargesSummary using partial update
        StudentChargesSummary partialUpdatedStudentChargesSummary = new StudentChargesSummary();
        partialUpdatedStudentChargesSummary.setId(studentChargesSummary.getId());

        partialUpdatedStudentChargesSummary
            .summaryType(UPDATED_SUMMARY_TYPE)
            .feeYear(UPDATED_FEE_YEAR)
            .dueDate(UPDATED_DUE_DATE)
            .aprSummary(UPDATED_APR_SUMMARY)
            .maySummary(UPDATED_MAY_SUMMARY)
            .junSummary(UPDATED_JUN_SUMMARY)
            .julSummary(UPDATED_JUL_SUMMARY)
            .augSummary(UPDATED_AUG_SUMMARY)
            .sepSummary(UPDATED_SEP_SUMMARY)
            .octSummary(UPDATED_OCT_SUMMARY)
            .novSummary(UPDATED_NOV_SUMMARY)
            .decSummary(UPDATED_DEC_SUMMARY)
            .janSummary(UPDATED_JAN_SUMMARY)
            .febSummary(UPDATED_FEB_SUMMARY)
            .marSummary(UPDATED_MAR_SUMMARY)
            .additionalInfo1(UPDATED_ADDITIONAL_INFO_1)
            .additionalInfo2(UPDATED_ADDITIONAL_INFO_2)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);

        restStudentChargesSummaryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStudentChargesSummary.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStudentChargesSummary))
            )
            .andExpect(status().isOk());

        // Validate the StudentChargesSummary in the database
        List<StudentChargesSummary> studentChargesSummaryList = studentChargesSummaryRepository.findAll();
        assertThat(studentChargesSummaryList).hasSize(databaseSizeBeforeUpdate);
        StudentChargesSummary testStudentChargesSummary = studentChargesSummaryList.get(studentChargesSummaryList.size() - 1);
        assertThat(testStudentChargesSummary.getSummaryType()).isEqualTo(UPDATED_SUMMARY_TYPE);
        assertThat(testStudentChargesSummary.getFeeYear()).isEqualTo(UPDATED_FEE_YEAR);
        assertThat(testStudentChargesSummary.getDueDate()).isEqualTo(UPDATED_DUE_DATE);
        assertThat(testStudentChargesSummary.getAprSummary()).isEqualTo(UPDATED_APR_SUMMARY);
        assertThat(testStudentChargesSummary.getMaySummary()).isEqualTo(UPDATED_MAY_SUMMARY);
        assertThat(testStudentChargesSummary.getJunSummary()).isEqualTo(UPDATED_JUN_SUMMARY);
        assertThat(testStudentChargesSummary.getJulSummary()).isEqualTo(UPDATED_JUL_SUMMARY);
        assertThat(testStudentChargesSummary.getAugSummary()).isEqualTo(UPDATED_AUG_SUMMARY);
        assertThat(testStudentChargesSummary.getSepSummary()).isEqualTo(UPDATED_SEP_SUMMARY);
        assertThat(testStudentChargesSummary.getOctSummary()).isEqualTo(UPDATED_OCT_SUMMARY);
        assertThat(testStudentChargesSummary.getNovSummary()).isEqualTo(UPDATED_NOV_SUMMARY);
        assertThat(testStudentChargesSummary.getDecSummary()).isEqualTo(UPDATED_DEC_SUMMARY);
        assertThat(testStudentChargesSummary.getJanSummary()).isEqualTo(UPDATED_JAN_SUMMARY);
        assertThat(testStudentChargesSummary.getFebSummary()).isEqualTo(UPDATED_FEB_SUMMARY);
        assertThat(testStudentChargesSummary.getMarSummary()).isEqualTo(UPDATED_MAR_SUMMARY);
        assertThat(testStudentChargesSummary.getAdditionalInfo1()).isEqualTo(UPDATED_ADDITIONAL_INFO_1);
        assertThat(testStudentChargesSummary.getAdditionalInfo2()).isEqualTo(UPDATED_ADDITIONAL_INFO_2);
        assertThat(testStudentChargesSummary.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testStudentChargesSummary.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testStudentChargesSummary.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingStudentChargesSummary() throws Exception {
        int databaseSizeBeforeUpdate = studentChargesSummaryRepository.findAll().size();
        studentChargesSummary.setId(count.incrementAndGet());

        // Create the StudentChargesSummary
        StudentChargesSummaryDTO studentChargesSummaryDTO = studentChargesSummaryMapper.toDto(studentChargesSummary);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudentChargesSummaryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, studentChargesSummaryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(studentChargesSummaryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentChargesSummary in the database
        List<StudentChargesSummary> studentChargesSummaryList = studentChargesSummaryRepository.findAll();
        assertThat(studentChargesSummaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStudentChargesSummary() throws Exception {
        int databaseSizeBeforeUpdate = studentChargesSummaryRepository.findAll().size();
        studentChargesSummary.setId(count.incrementAndGet());

        // Create the StudentChargesSummary
        StudentChargesSummaryDTO studentChargesSummaryDTO = studentChargesSummaryMapper.toDto(studentChargesSummary);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentChargesSummaryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(studentChargesSummaryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentChargesSummary in the database
        List<StudentChargesSummary> studentChargesSummaryList = studentChargesSummaryRepository.findAll();
        assertThat(studentChargesSummaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStudentChargesSummary() throws Exception {
        int databaseSizeBeforeUpdate = studentChargesSummaryRepository.findAll().size();
        studentChargesSummary.setId(count.incrementAndGet());

        // Create the StudentChargesSummary
        StudentChargesSummaryDTO studentChargesSummaryDTO = studentChargesSummaryMapper.toDto(studentChargesSummary);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentChargesSummaryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(studentChargesSummaryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StudentChargesSummary in the database
        List<StudentChargesSummary> studentChargesSummaryList = studentChargesSummaryRepository.findAll();
        assertThat(studentChargesSummaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStudentChargesSummary() throws Exception {
        // Initialize the database
        studentChargesSummaryRepository.saveAndFlush(studentChargesSummary);

        int databaseSizeBeforeDelete = studentChargesSummaryRepository.findAll().size();

        // Delete the studentChargesSummary
        restStudentChargesSummaryMockMvc
            .perform(delete(ENTITY_API_URL_ID, studentChargesSummary.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<StudentChargesSummary> studentChargesSummaryList = studentChargesSummaryRepository.findAll();
        assertThat(studentChargesSummaryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
