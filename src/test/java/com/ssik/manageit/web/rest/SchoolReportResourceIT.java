package com.ssik.manageit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ssik.manageit.IntegrationTest;
import com.ssik.manageit.domain.SchoolClass;
import com.ssik.manageit.domain.SchoolReport;
import com.ssik.manageit.domain.enumeration.ReportType;
import com.ssik.manageit.repository.SchoolReportRepository;
import com.ssik.manageit.service.SchoolReportService;
import com.ssik.manageit.service.criteria.SchoolReportCriteria;
import com.ssik.manageit.service.dto.SchoolReportDTO;
import com.ssik.manageit.service.mapper.SchoolReportMapper;
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
 * Integration tests for the {@link SchoolReportResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SchoolReportResourceIT {

    private static final ReportType DEFAULT_REPORT_TYPE = ReportType.FEE_COLLECTION;
    private static final ReportType UPDATED_REPORT_TYPE = ReportType.FEE_DUE;

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_START_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_END_DATE = LocalDate.ofEpochDay(-1L);

    private static final byte[] DEFAULT_REPORT_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_REPORT_FILE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_REPORT_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_REPORT_FILE_CONTENT_TYPE = "image/png";

    private static final LocalDate DEFAULT_CREATE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATE_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CREATE_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_LAST_MODIFIED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_MODIFIED = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_LAST_MODIFIED = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_CANCEL_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CANCEL_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CANCEL_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/school-reports";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SchoolReportRepository schoolReportRepository;

    @Mock
    private SchoolReportRepository schoolReportRepositoryMock;

    @Autowired
    private SchoolReportMapper schoolReportMapper;

    @Mock
    private SchoolReportService schoolReportServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSchoolReportMockMvc;

    private SchoolReport schoolReport;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SchoolReport createEntity(EntityManager em) {
        SchoolReport schoolReport = new SchoolReport()
            .reportType(DEFAULT_REPORT_TYPE)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .reportFile(DEFAULT_REPORT_FILE)
            .reportFileContentType(DEFAULT_REPORT_FILE_CONTENT_TYPE)
            .createDate(DEFAULT_CREATE_DATE)
            .lastModified(DEFAULT_LAST_MODIFIED)
            .cancelDate(DEFAULT_CANCEL_DATE);
        return schoolReport;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SchoolReport createUpdatedEntity(EntityManager em) {
        SchoolReport schoolReport = new SchoolReport()
            .reportType(UPDATED_REPORT_TYPE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .reportFile(UPDATED_REPORT_FILE)
            .reportFileContentType(UPDATED_REPORT_FILE_CONTENT_TYPE)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);
        return schoolReport;
    }

    @BeforeEach
    public void initTest() {
        schoolReport = createEntity(em);
    }

    @Test
    @Transactional
    void createSchoolReport() throws Exception {
        int databaseSizeBeforeCreate = schoolReportRepository.findAll().size();
        // Create the SchoolReport
        SchoolReportDTO schoolReportDTO = schoolReportMapper.toDto(schoolReport);
        restSchoolReportMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(schoolReportDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SchoolReport in the database
        List<SchoolReport> schoolReportList = schoolReportRepository.findAll();
        assertThat(schoolReportList).hasSize(databaseSizeBeforeCreate + 1);
        SchoolReport testSchoolReport = schoolReportList.get(schoolReportList.size() - 1);
        assertThat(testSchoolReport.getReportType()).isEqualTo(DEFAULT_REPORT_TYPE);
        assertThat(testSchoolReport.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testSchoolReport.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testSchoolReport.getReportFile()).isEqualTo(DEFAULT_REPORT_FILE);
        assertThat(testSchoolReport.getReportFileContentType()).isEqualTo(DEFAULT_REPORT_FILE_CONTENT_TYPE);
        assertThat(testSchoolReport.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testSchoolReport.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
        assertThat(testSchoolReport.getCancelDate()).isEqualTo(DEFAULT_CANCEL_DATE);
    }

    @Test
    @Transactional
    void createSchoolReportWithExistingId() throws Exception {
        // Create the SchoolReport with an existing ID
        schoolReport.setId(1L);
        SchoolReportDTO schoolReportDTO = schoolReportMapper.toDto(schoolReport);

        int databaseSizeBeforeCreate = schoolReportRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSchoolReportMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(schoolReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchoolReport in the database
        List<SchoolReport> schoolReportList = schoolReportRepository.findAll();
        assertThat(schoolReportList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkReportTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = schoolReportRepository.findAll().size();
        // set the field null
        schoolReport.setReportType(null);

        // Create the SchoolReport, which fails.
        SchoolReportDTO schoolReportDTO = schoolReportMapper.toDto(schoolReport);

        restSchoolReportMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(schoolReportDTO))
            )
            .andExpect(status().isBadRequest());

        List<SchoolReport> schoolReportList = schoolReportRepository.findAll();
        assertThat(schoolReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = schoolReportRepository.findAll().size();
        // set the field null
        schoolReport.setStartDate(null);

        // Create the SchoolReport, which fails.
        SchoolReportDTO schoolReportDTO = schoolReportMapper.toDto(schoolReport);

        restSchoolReportMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(schoolReportDTO))
            )
            .andExpect(status().isBadRequest());

        List<SchoolReport> schoolReportList = schoolReportRepository.findAll();
        assertThat(schoolReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = schoolReportRepository.findAll().size();
        // set the field null
        schoolReport.setEndDate(null);

        // Create the SchoolReport, which fails.
        SchoolReportDTO schoolReportDTO = schoolReportMapper.toDto(schoolReport);

        restSchoolReportMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(schoolReportDTO))
            )
            .andExpect(status().isBadRequest());

        List<SchoolReport> schoolReportList = schoolReportRepository.findAll();
        assertThat(schoolReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSchoolReports() throws Exception {
        // Initialize the database
        schoolReportRepository.saveAndFlush(schoolReport);

        // Get all the schoolReportList
        restSchoolReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(schoolReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportType").value(hasItem(DEFAULT_REPORT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].reportFileContentType").value(hasItem(DEFAULT_REPORT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].reportFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_REPORT_FILE))))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].cancelDate").value(hasItem(DEFAULT_CANCEL_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSchoolReportsWithEagerRelationshipsIsEnabled() throws Exception {
        when(schoolReportServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSchoolReportMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(schoolReportServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSchoolReportsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(schoolReportServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSchoolReportMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(schoolReportServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getSchoolReport() throws Exception {
        // Initialize the database
        schoolReportRepository.saveAndFlush(schoolReport);

        // Get the schoolReport
        restSchoolReportMockMvc
            .perform(get(ENTITY_API_URL_ID, schoolReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(schoolReport.getId().intValue()))
            .andExpect(jsonPath("$.reportType").value(DEFAULT_REPORT_TYPE.toString()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.reportFileContentType").value(DEFAULT_REPORT_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.reportFile").value(Base64Utils.encodeToString(DEFAULT_REPORT_FILE)))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.lastModified").value(DEFAULT_LAST_MODIFIED.toString()))
            .andExpect(jsonPath("$.cancelDate").value(DEFAULT_CANCEL_DATE.toString()));
    }

    @Test
    @Transactional
    void getSchoolReportsByIdFiltering() throws Exception {
        // Initialize the database
        schoolReportRepository.saveAndFlush(schoolReport);

        Long id = schoolReport.getId();

        defaultSchoolReportShouldBeFound("id.equals=" + id);
        defaultSchoolReportShouldNotBeFound("id.notEquals=" + id);

        defaultSchoolReportShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSchoolReportShouldNotBeFound("id.greaterThan=" + id);

        defaultSchoolReportShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSchoolReportShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSchoolReportsByReportTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolReportRepository.saveAndFlush(schoolReport);

        // Get all the schoolReportList where reportType equals to DEFAULT_REPORT_TYPE
        defaultSchoolReportShouldBeFound("reportType.equals=" + DEFAULT_REPORT_TYPE);

        // Get all the schoolReportList where reportType equals to UPDATED_REPORT_TYPE
        defaultSchoolReportShouldNotBeFound("reportType.equals=" + UPDATED_REPORT_TYPE);
    }

    @Test
    @Transactional
    void getAllSchoolReportsByReportTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        schoolReportRepository.saveAndFlush(schoolReport);

        // Get all the schoolReportList where reportType not equals to DEFAULT_REPORT_TYPE
        defaultSchoolReportShouldNotBeFound("reportType.notEquals=" + DEFAULT_REPORT_TYPE);

        // Get all the schoolReportList where reportType not equals to UPDATED_REPORT_TYPE
        defaultSchoolReportShouldBeFound("reportType.notEquals=" + UPDATED_REPORT_TYPE);
    }

    @Test
    @Transactional
    void getAllSchoolReportsByReportTypeIsInShouldWork() throws Exception {
        // Initialize the database
        schoolReportRepository.saveAndFlush(schoolReport);

        // Get all the schoolReportList where reportType in DEFAULT_REPORT_TYPE or UPDATED_REPORT_TYPE
        defaultSchoolReportShouldBeFound("reportType.in=" + DEFAULT_REPORT_TYPE + "," + UPDATED_REPORT_TYPE);

        // Get all the schoolReportList where reportType equals to UPDATED_REPORT_TYPE
        defaultSchoolReportShouldNotBeFound("reportType.in=" + UPDATED_REPORT_TYPE);
    }

    @Test
    @Transactional
    void getAllSchoolReportsByReportTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolReportRepository.saveAndFlush(schoolReport);

        // Get all the schoolReportList where reportType is not null
        defaultSchoolReportShouldBeFound("reportType.specified=true");

        // Get all the schoolReportList where reportType is null
        defaultSchoolReportShouldNotBeFound("reportType.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolReportsByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolReportRepository.saveAndFlush(schoolReport);

        // Get all the schoolReportList where startDate equals to DEFAULT_START_DATE
        defaultSchoolReportShouldBeFound("startDate.equals=" + DEFAULT_START_DATE);

        // Get all the schoolReportList where startDate equals to UPDATED_START_DATE
        defaultSchoolReportShouldNotBeFound("startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolReportsByStartDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        schoolReportRepository.saveAndFlush(schoolReport);

        // Get all the schoolReportList where startDate not equals to DEFAULT_START_DATE
        defaultSchoolReportShouldNotBeFound("startDate.notEquals=" + DEFAULT_START_DATE);

        // Get all the schoolReportList where startDate not equals to UPDATED_START_DATE
        defaultSchoolReportShouldBeFound("startDate.notEquals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolReportsByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        schoolReportRepository.saveAndFlush(schoolReport);

        // Get all the schoolReportList where startDate in DEFAULT_START_DATE or UPDATED_START_DATE
        defaultSchoolReportShouldBeFound("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE);

        // Get all the schoolReportList where startDate equals to UPDATED_START_DATE
        defaultSchoolReportShouldNotBeFound("startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolReportsByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolReportRepository.saveAndFlush(schoolReport);

        // Get all the schoolReportList where startDate is not null
        defaultSchoolReportShouldBeFound("startDate.specified=true");

        // Get all the schoolReportList where startDate is null
        defaultSchoolReportShouldNotBeFound("startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolReportsByStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        schoolReportRepository.saveAndFlush(schoolReport);

        // Get all the schoolReportList where startDate is greater than or equal to DEFAULT_START_DATE
        defaultSchoolReportShouldBeFound("startDate.greaterThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the schoolReportList where startDate is greater than or equal to UPDATED_START_DATE
        defaultSchoolReportShouldNotBeFound("startDate.greaterThanOrEqual=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolReportsByStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        schoolReportRepository.saveAndFlush(schoolReport);

        // Get all the schoolReportList where startDate is less than or equal to DEFAULT_START_DATE
        defaultSchoolReportShouldBeFound("startDate.lessThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the schoolReportList where startDate is less than or equal to SMALLER_START_DATE
        defaultSchoolReportShouldNotBeFound("startDate.lessThanOrEqual=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolReportsByStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        schoolReportRepository.saveAndFlush(schoolReport);

        // Get all the schoolReportList where startDate is less than DEFAULT_START_DATE
        defaultSchoolReportShouldNotBeFound("startDate.lessThan=" + DEFAULT_START_DATE);

        // Get all the schoolReportList where startDate is less than UPDATED_START_DATE
        defaultSchoolReportShouldBeFound("startDate.lessThan=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolReportsByStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        schoolReportRepository.saveAndFlush(schoolReport);

        // Get all the schoolReportList where startDate is greater than DEFAULT_START_DATE
        defaultSchoolReportShouldNotBeFound("startDate.greaterThan=" + DEFAULT_START_DATE);

        // Get all the schoolReportList where startDate is greater than SMALLER_START_DATE
        defaultSchoolReportShouldBeFound("startDate.greaterThan=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolReportsByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolReportRepository.saveAndFlush(schoolReport);

        // Get all the schoolReportList where endDate equals to DEFAULT_END_DATE
        defaultSchoolReportShouldBeFound("endDate.equals=" + DEFAULT_END_DATE);

        // Get all the schoolReportList where endDate equals to UPDATED_END_DATE
        defaultSchoolReportShouldNotBeFound("endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolReportsByEndDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        schoolReportRepository.saveAndFlush(schoolReport);

        // Get all the schoolReportList where endDate not equals to DEFAULT_END_DATE
        defaultSchoolReportShouldNotBeFound("endDate.notEquals=" + DEFAULT_END_DATE);

        // Get all the schoolReportList where endDate not equals to UPDATED_END_DATE
        defaultSchoolReportShouldBeFound("endDate.notEquals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolReportsByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        schoolReportRepository.saveAndFlush(schoolReport);

        // Get all the schoolReportList where endDate in DEFAULT_END_DATE or UPDATED_END_DATE
        defaultSchoolReportShouldBeFound("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE);

        // Get all the schoolReportList where endDate equals to UPDATED_END_DATE
        defaultSchoolReportShouldNotBeFound("endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolReportsByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolReportRepository.saveAndFlush(schoolReport);

        // Get all the schoolReportList where endDate is not null
        defaultSchoolReportShouldBeFound("endDate.specified=true");

        // Get all the schoolReportList where endDate is null
        defaultSchoolReportShouldNotBeFound("endDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolReportsByEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        schoolReportRepository.saveAndFlush(schoolReport);

        // Get all the schoolReportList where endDate is greater than or equal to DEFAULT_END_DATE
        defaultSchoolReportShouldBeFound("endDate.greaterThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the schoolReportList where endDate is greater than or equal to UPDATED_END_DATE
        defaultSchoolReportShouldNotBeFound("endDate.greaterThanOrEqual=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolReportsByEndDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        schoolReportRepository.saveAndFlush(schoolReport);

        // Get all the schoolReportList where endDate is less than or equal to DEFAULT_END_DATE
        defaultSchoolReportShouldBeFound("endDate.lessThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the schoolReportList where endDate is less than or equal to SMALLER_END_DATE
        defaultSchoolReportShouldNotBeFound("endDate.lessThanOrEqual=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolReportsByEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        schoolReportRepository.saveAndFlush(schoolReport);

        // Get all the schoolReportList where endDate is less than DEFAULT_END_DATE
        defaultSchoolReportShouldNotBeFound("endDate.lessThan=" + DEFAULT_END_DATE);

        // Get all the schoolReportList where endDate is less than UPDATED_END_DATE
        defaultSchoolReportShouldBeFound("endDate.lessThan=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolReportsByEndDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        schoolReportRepository.saveAndFlush(schoolReport);

        // Get all the schoolReportList where endDate is greater than DEFAULT_END_DATE
        defaultSchoolReportShouldNotBeFound("endDate.greaterThan=" + DEFAULT_END_DATE);

        // Get all the schoolReportList where endDate is greater than SMALLER_END_DATE
        defaultSchoolReportShouldBeFound("endDate.greaterThan=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolReportsByCreateDateIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolReportRepository.saveAndFlush(schoolReport);

        // Get all the schoolReportList where createDate equals to DEFAULT_CREATE_DATE
        defaultSchoolReportShouldBeFound("createDate.equals=" + DEFAULT_CREATE_DATE);

        // Get all the schoolReportList where createDate equals to UPDATED_CREATE_DATE
        defaultSchoolReportShouldNotBeFound("createDate.equals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolReportsByCreateDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        schoolReportRepository.saveAndFlush(schoolReport);

        // Get all the schoolReportList where createDate not equals to DEFAULT_CREATE_DATE
        defaultSchoolReportShouldNotBeFound("createDate.notEquals=" + DEFAULT_CREATE_DATE);

        // Get all the schoolReportList where createDate not equals to UPDATED_CREATE_DATE
        defaultSchoolReportShouldBeFound("createDate.notEquals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolReportsByCreateDateIsInShouldWork() throws Exception {
        // Initialize the database
        schoolReportRepository.saveAndFlush(schoolReport);

        // Get all the schoolReportList where createDate in DEFAULT_CREATE_DATE or UPDATED_CREATE_DATE
        defaultSchoolReportShouldBeFound("createDate.in=" + DEFAULT_CREATE_DATE + "," + UPDATED_CREATE_DATE);

        // Get all the schoolReportList where createDate equals to UPDATED_CREATE_DATE
        defaultSchoolReportShouldNotBeFound("createDate.in=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolReportsByCreateDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolReportRepository.saveAndFlush(schoolReport);

        // Get all the schoolReportList where createDate is not null
        defaultSchoolReportShouldBeFound("createDate.specified=true");

        // Get all the schoolReportList where createDate is null
        defaultSchoolReportShouldNotBeFound("createDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolReportsByCreateDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        schoolReportRepository.saveAndFlush(schoolReport);

        // Get all the schoolReportList where createDate is greater than or equal to DEFAULT_CREATE_DATE
        defaultSchoolReportShouldBeFound("createDate.greaterThanOrEqual=" + DEFAULT_CREATE_DATE);

        // Get all the schoolReportList where createDate is greater than or equal to UPDATED_CREATE_DATE
        defaultSchoolReportShouldNotBeFound("createDate.greaterThanOrEqual=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolReportsByCreateDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        schoolReportRepository.saveAndFlush(schoolReport);

        // Get all the schoolReportList where createDate is less than or equal to DEFAULT_CREATE_DATE
        defaultSchoolReportShouldBeFound("createDate.lessThanOrEqual=" + DEFAULT_CREATE_DATE);

        // Get all the schoolReportList where createDate is less than or equal to SMALLER_CREATE_DATE
        defaultSchoolReportShouldNotBeFound("createDate.lessThanOrEqual=" + SMALLER_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolReportsByCreateDateIsLessThanSomething() throws Exception {
        // Initialize the database
        schoolReportRepository.saveAndFlush(schoolReport);

        // Get all the schoolReportList where createDate is less than DEFAULT_CREATE_DATE
        defaultSchoolReportShouldNotBeFound("createDate.lessThan=" + DEFAULT_CREATE_DATE);

        // Get all the schoolReportList where createDate is less than UPDATED_CREATE_DATE
        defaultSchoolReportShouldBeFound("createDate.lessThan=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolReportsByCreateDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        schoolReportRepository.saveAndFlush(schoolReport);

        // Get all the schoolReportList where createDate is greater than DEFAULT_CREATE_DATE
        defaultSchoolReportShouldNotBeFound("createDate.greaterThan=" + DEFAULT_CREATE_DATE);

        // Get all the schoolReportList where createDate is greater than SMALLER_CREATE_DATE
        defaultSchoolReportShouldBeFound("createDate.greaterThan=" + SMALLER_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolReportsByLastModifiedIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolReportRepository.saveAndFlush(schoolReport);

        // Get all the schoolReportList where lastModified equals to DEFAULT_LAST_MODIFIED
        defaultSchoolReportShouldBeFound("lastModified.equals=" + DEFAULT_LAST_MODIFIED);

        // Get all the schoolReportList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultSchoolReportShouldNotBeFound("lastModified.equals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSchoolReportsByLastModifiedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        schoolReportRepository.saveAndFlush(schoolReport);

        // Get all the schoolReportList where lastModified not equals to DEFAULT_LAST_MODIFIED
        defaultSchoolReportShouldNotBeFound("lastModified.notEquals=" + DEFAULT_LAST_MODIFIED);

        // Get all the schoolReportList where lastModified not equals to UPDATED_LAST_MODIFIED
        defaultSchoolReportShouldBeFound("lastModified.notEquals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSchoolReportsByLastModifiedIsInShouldWork() throws Exception {
        // Initialize the database
        schoolReportRepository.saveAndFlush(schoolReport);

        // Get all the schoolReportList where lastModified in DEFAULT_LAST_MODIFIED or UPDATED_LAST_MODIFIED
        defaultSchoolReportShouldBeFound("lastModified.in=" + DEFAULT_LAST_MODIFIED + "," + UPDATED_LAST_MODIFIED);

        // Get all the schoolReportList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultSchoolReportShouldNotBeFound("lastModified.in=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSchoolReportsByLastModifiedIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolReportRepository.saveAndFlush(schoolReport);

        // Get all the schoolReportList where lastModified is not null
        defaultSchoolReportShouldBeFound("lastModified.specified=true");

        // Get all the schoolReportList where lastModified is null
        defaultSchoolReportShouldNotBeFound("lastModified.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolReportsByLastModifiedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        schoolReportRepository.saveAndFlush(schoolReport);

        // Get all the schoolReportList where lastModified is greater than or equal to DEFAULT_LAST_MODIFIED
        defaultSchoolReportShouldBeFound("lastModified.greaterThanOrEqual=" + DEFAULT_LAST_MODIFIED);

        // Get all the schoolReportList where lastModified is greater than or equal to UPDATED_LAST_MODIFIED
        defaultSchoolReportShouldNotBeFound("lastModified.greaterThanOrEqual=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSchoolReportsByLastModifiedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        schoolReportRepository.saveAndFlush(schoolReport);

        // Get all the schoolReportList where lastModified is less than or equal to DEFAULT_LAST_MODIFIED
        defaultSchoolReportShouldBeFound("lastModified.lessThanOrEqual=" + DEFAULT_LAST_MODIFIED);

        // Get all the schoolReportList where lastModified is less than or equal to SMALLER_LAST_MODIFIED
        defaultSchoolReportShouldNotBeFound("lastModified.lessThanOrEqual=" + SMALLER_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSchoolReportsByLastModifiedIsLessThanSomething() throws Exception {
        // Initialize the database
        schoolReportRepository.saveAndFlush(schoolReport);

        // Get all the schoolReportList where lastModified is less than DEFAULT_LAST_MODIFIED
        defaultSchoolReportShouldNotBeFound("lastModified.lessThan=" + DEFAULT_LAST_MODIFIED);

        // Get all the schoolReportList where lastModified is less than UPDATED_LAST_MODIFIED
        defaultSchoolReportShouldBeFound("lastModified.lessThan=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSchoolReportsByLastModifiedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        schoolReportRepository.saveAndFlush(schoolReport);

        // Get all the schoolReportList where lastModified is greater than DEFAULT_LAST_MODIFIED
        defaultSchoolReportShouldNotBeFound("lastModified.greaterThan=" + DEFAULT_LAST_MODIFIED);

        // Get all the schoolReportList where lastModified is greater than SMALLER_LAST_MODIFIED
        defaultSchoolReportShouldBeFound("lastModified.greaterThan=" + SMALLER_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSchoolReportsByCancelDateIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolReportRepository.saveAndFlush(schoolReport);

        // Get all the schoolReportList where cancelDate equals to DEFAULT_CANCEL_DATE
        defaultSchoolReportShouldBeFound("cancelDate.equals=" + DEFAULT_CANCEL_DATE);

        // Get all the schoolReportList where cancelDate equals to UPDATED_CANCEL_DATE
        defaultSchoolReportShouldNotBeFound("cancelDate.equals=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolReportsByCancelDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        schoolReportRepository.saveAndFlush(schoolReport);

        // Get all the schoolReportList where cancelDate not equals to DEFAULT_CANCEL_DATE
        defaultSchoolReportShouldNotBeFound("cancelDate.notEquals=" + DEFAULT_CANCEL_DATE);

        // Get all the schoolReportList where cancelDate not equals to UPDATED_CANCEL_DATE
        defaultSchoolReportShouldBeFound("cancelDate.notEquals=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolReportsByCancelDateIsInShouldWork() throws Exception {
        // Initialize the database
        schoolReportRepository.saveAndFlush(schoolReport);

        // Get all the schoolReportList where cancelDate in DEFAULT_CANCEL_DATE or UPDATED_CANCEL_DATE
        defaultSchoolReportShouldBeFound("cancelDate.in=" + DEFAULT_CANCEL_DATE + "," + UPDATED_CANCEL_DATE);

        // Get all the schoolReportList where cancelDate equals to UPDATED_CANCEL_DATE
        defaultSchoolReportShouldNotBeFound("cancelDate.in=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolReportsByCancelDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolReportRepository.saveAndFlush(schoolReport);

        // Get all the schoolReportList where cancelDate is not null
        defaultSchoolReportShouldBeFound("cancelDate.specified=true");

        // Get all the schoolReportList where cancelDate is null
        defaultSchoolReportShouldNotBeFound("cancelDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolReportsByCancelDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        schoolReportRepository.saveAndFlush(schoolReport);

        // Get all the schoolReportList where cancelDate is greater than or equal to DEFAULT_CANCEL_DATE
        defaultSchoolReportShouldBeFound("cancelDate.greaterThanOrEqual=" + DEFAULT_CANCEL_DATE);

        // Get all the schoolReportList where cancelDate is greater than or equal to UPDATED_CANCEL_DATE
        defaultSchoolReportShouldNotBeFound("cancelDate.greaterThanOrEqual=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolReportsByCancelDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        schoolReportRepository.saveAndFlush(schoolReport);

        // Get all the schoolReportList where cancelDate is less than or equal to DEFAULT_CANCEL_DATE
        defaultSchoolReportShouldBeFound("cancelDate.lessThanOrEqual=" + DEFAULT_CANCEL_DATE);

        // Get all the schoolReportList where cancelDate is less than or equal to SMALLER_CANCEL_DATE
        defaultSchoolReportShouldNotBeFound("cancelDate.lessThanOrEqual=" + SMALLER_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolReportsByCancelDateIsLessThanSomething() throws Exception {
        // Initialize the database
        schoolReportRepository.saveAndFlush(schoolReport);

        // Get all the schoolReportList where cancelDate is less than DEFAULT_CANCEL_DATE
        defaultSchoolReportShouldNotBeFound("cancelDate.lessThan=" + DEFAULT_CANCEL_DATE);

        // Get all the schoolReportList where cancelDate is less than UPDATED_CANCEL_DATE
        defaultSchoolReportShouldBeFound("cancelDate.lessThan=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolReportsByCancelDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        schoolReportRepository.saveAndFlush(schoolReport);

        // Get all the schoolReportList where cancelDate is greater than DEFAULT_CANCEL_DATE
        defaultSchoolReportShouldNotBeFound("cancelDate.greaterThan=" + DEFAULT_CANCEL_DATE);

        // Get all the schoolReportList where cancelDate is greater than SMALLER_CANCEL_DATE
        defaultSchoolReportShouldBeFound("cancelDate.greaterThan=" + SMALLER_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolReportsBySchoolClassIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolReportRepository.saveAndFlush(schoolReport);
        SchoolClass schoolClass = SchoolClassResourceIT.createEntity(em);
        em.persist(schoolClass);
        em.flush();
        schoolReport.addSchoolClass(schoolClass);
        schoolReportRepository.saveAndFlush(schoolReport);
        Long schoolClassId = schoolClass.getId();

        // Get all the schoolReportList where schoolClass equals to schoolClassId
        defaultSchoolReportShouldBeFound("schoolClassId.equals=" + schoolClassId);

        // Get all the schoolReportList where schoolClass equals to (schoolClassId + 1)
        defaultSchoolReportShouldNotBeFound("schoolClassId.equals=" + (schoolClassId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSchoolReportShouldBeFound(String filter) throws Exception {
        restSchoolReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(schoolReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportType").value(hasItem(DEFAULT_REPORT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].reportFileContentType").value(hasItem(DEFAULT_REPORT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].reportFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_REPORT_FILE))))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].cancelDate").value(hasItem(DEFAULT_CANCEL_DATE.toString())));

        // Check, that the count call also returns 1
        restSchoolReportMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSchoolReportShouldNotBeFound(String filter) throws Exception {
        restSchoolReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSchoolReportMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSchoolReport() throws Exception {
        // Get the schoolReport
        restSchoolReportMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSchoolReport() throws Exception {
        // Initialize the database
        schoolReportRepository.saveAndFlush(schoolReport);

        int databaseSizeBeforeUpdate = schoolReportRepository.findAll().size();

        // Update the schoolReport
        SchoolReport updatedSchoolReport = schoolReportRepository.findById(schoolReport.getId()).get();
        // Disconnect from session so that the updates on updatedSchoolReport are not directly saved in db
        em.detach(updatedSchoolReport);
        updatedSchoolReport
            .reportType(UPDATED_REPORT_TYPE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .reportFile(UPDATED_REPORT_FILE)
            .reportFileContentType(UPDATED_REPORT_FILE_CONTENT_TYPE)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);
        SchoolReportDTO schoolReportDTO = schoolReportMapper.toDto(updatedSchoolReport);

        restSchoolReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, schoolReportDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schoolReportDTO))
            )
            .andExpect(status().isOk());

        // Validate the SchoolReport in the database
        List<SchoolReport> schoolReportList = schoolReportRepository.findAll();
        assertThat(schoolReportList).hasSize(databaseSizeBeforeUpdate);
        SchoolReport testSchoolReport = schoolReportList.get(schoolReportList.size() - 1);
        assertThat(testSchoolReport.getReportType()).isEqualTo(UPDATED_REPORT_TYPE);
        assertThat(testSchoolReport.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testSchoolReport.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testSchoolReport.getReportFile()).isEqualTo(UPDATED_REPORT_FILE);
        assertThat(testSchoolReport.getReportFileContentType()).isEqualTo(UPDATED_REPORT_FILE_CONTENT_TYPE);
        assertThat(testSchoolReport.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testSchoolReport.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testSchoolReport.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void putNonExistingSchoolReport() throws Exception {
        int databaseSizeBeforeUpdate = schoolReportRepository.findAll().size();
        schoolReport.setId(count.incrementAndGet());

        // Create the SchoolReport
        SchoolReportDTO schoolReportDTO = schoolReportMapper.toDto(schoolReport);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSchoolReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, schoolReportDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schoolReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchoolReport in the database
        List<SchoolReport> schoolReportList = schoolReportRepository.findAll();
        assertThat(schoolReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSchoolReport() throws Exception {
        int databaseSizeBeforeUpdate = schoolReportRepository.findAll().size();
        schoolReport.setId(count.incrementAndGet());

        // Create the SchoolReport
        SchoolReportDTO schoolReportDTO = schoolReportMapper.toDto(schoolReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchoolReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schoolReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchoolReport in the database
        List<SchoolReport> schoolReportList = schoolReportRepository.findAll();
        assertThat(schoolReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSchoolReport() throws Exception {
        int databaseSizeBeforeUpdate = schoolReportRepository.findAll().size();
        schoolReport.setId(count.incrementAndGet());

        // Create the SchoolReport
        SchoolReportDTO schoolReportDTO = schoolReportMapper.toDto(schoolReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchoolReportMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(schoolReportDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SchoolReport in the database
        List<SchoolReport> schoolReportList = schoolReportRepository.findAll();
        assertThat(schoolReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSchoolReportWithPatch() throws Exception {
        // Initialize the database
        schoolReportRepository.saveAndFlush(schoolReport);

        int databaseSizeBeforeUpdate = schoolReportRepository.findAll().size();

        // Update the schoolReport using partial update
        SchoolReport partialUpdatedSchoolReport = new SchoolReport();
        partialUpdatedSchoolReport.setId(schoolReport.getId());

        partialUpdatedSchoolReport
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);

        restSchoolReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSchoolReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSchoolReport))
            )
            .andExpect(status().isOk());

        // Validate the SchoolReport in the database
        List<SchoolReport> schoolReportList = schoolReportRepository.findAll();
        assertThat(schoolReportList).hasSize(databaseSizeBeforeUpdate);
        SchoolReport testSchoolReport = schoolReportList.get(schoolReportList.size() - 1);
        assertThat(testSchoolReport.getReportType()).isEqualTo(DEFAULT_REPORT_TYPE);
        assertThat(testSchoolReport.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testSchoolReport.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testSchoolReport.getReportFile()).isEqualTo(DEFAULT_REPORT_FILE);
        assertThat(testSchoolReport.getReportFileContentType()).isEqualTo(DEFAULT_REPORT_FILE_CONTENT_TYPE);
        assertThat(testSchoolReport.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testSchoolReport.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testSchoolReport.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void fullUpdateSchoolReportWithPatch() throws Exception {
        // Initialize the database
        schoolReportRepository.saveAndFlush(schoolReport);

        int databaseSizeBeforeUpdate = schoolReportRepository.findAll().size();

        // Update the schoolReport using partial update
        SchoolReport partialUpdatedSchoolReport = new SchoolReport();
        partialUpdatedSchoolReport.setId(schoolReport.getId());

        partialUpdatedSchoolReport
            .reportType(UPDATED_REPORT_TYPE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .reportFile(UPDATED_REPORT_FILE)
            .reportFileContentType(UPDATED_REPORT_FILE_CONTENT_TYPE)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);

        restSchoolReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSchoolReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSchoolReport))
            )
            .andExpect(status().isOk());

        // Validate the SchoolReport in the database
        List<SchoolReport> schoolReportList = schoolReportRepository.findAll();
        assertThat(schoolReportList).hasSize(databaseSizeBeforeUpdate);
        SchoolReport testSchoolReport = schoolReportList.get(schoolReportList.size() - 1);
        assertThat(testSchoolReport.getReportType()).isEqualTo(UPDATED_REPORT_TYPE);
        assertThat(testSchoolReport.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testSchoolReport.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testSchoolReport.getReportFile()).isEqualTo(UPDATED_REPORT_FILE);
        assertThat(testSchoolReport.getReportFileContentType()).isEqualTo(UPDATED_REPORT_FILE_CONTENT_TYPE);
        assertThat(testSchoolReport.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testSchoolReport.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testSchoolReport.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingSchoolReport() throws Exception {
        int databaseSizeBeforeUpdate = schoolReportRepository.findAll().size();
        schoolReport.setId(count.incrementAndGet());

        // Create the SchoolReport
        SchoolReportDTO schoolReportDTO = schoolReportMapper.toDto(schoolReport);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSchoolReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, schoolReportDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(schoolReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchoolReport in the database
        List<SchoolReport> schoolReportList = schoolReportRepository.findAll();
        assertThat(schoolReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSchoolReport() throws Exception {
        int databaseSizeBeforeUpdate = schoolReportRepository.findAll().size();
        schoolReport.setId(count.incrementAndGet());

        // Create the SchoolReport
        SchoolReportDTO schoolReportDTO = schoolReportMapper.toDto(schoolReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchoolReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(schoolReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchoolReport in the database
        List<SchoolReport> schoolReportList = schoolReportRepository.findAll();
        assertThat(schoolReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSchoolReport() throws Exception {
        int databaseSizeBeforeUpdate = schoolReportRepository.findAll().size();
        schoolReport.setId(count.incrementAndGet());

        // Create the SchoolReport
        SchoolReportDTO schoolReportDTO = schoolReportMapper.toDto(schoolReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchoolReportMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(schoolReportDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SchoolReport in the database
        List<SchoolReport> schoolReportList = schoolReportRepository.findAll();
        assertThat(schoolReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSchoolReport() throws Exception {
        // Initialize the database
        schoolReportRepository.saveAndFlush(schoolReport);

        int databaseSizeBeforeDelete = schoolReportRepository.findAll().size();

        // Delete the schoolReport
        restSchoolReportMockMvc
            .perform(delete(ENTITY_API_URL_ID, schoolReport.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SchoolReport> schoolReportList = schoolReportRepository.findAll();
        assertThat(schoolReportList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
