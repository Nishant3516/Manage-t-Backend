package com.ssik.manageit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ssik.manageit.IntegrationTest;
import com.ssik.manageit.domain.AuditLog;
import com.ssik.manageit.domain.School;
import com.ssik.manageit.domain.SchoolUser;
import com.ssik.manageit.repository.AuditLogRepository;
import com.ssik.manageit.service.criteria.AuditLogCriteria;
import com.ssik.manageit.service.dto.AuditLogDTO;
import com.ssik.manageit.service.mapper.AuditLogMapper;
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
 * Integration tests for the {@link AuditLogResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AuditLogResourceIT {

    private static final String DEFAULT_USER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_USER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_USER_DEVICE_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_USER_DEVICE_DETAILS = "BBBBBBBBBB";

    private static final String DEFAULT_ACTION = "AAAAAAAAAA";
    private static final String UPDATED_ACTION = "BBBBBBBBBB";

    private static final String DEFAULT_DATA_1 = "AAAAAAAAAA";
    private static final String UPDATED_DATA_1 = "BBBBBBBBBB";

    private static final String DEFAULT_DATA_2 = "AAAAAAAAAA";
    private static final String UPDATED_DATA_2 = "BBBBBBBBBB";

    private static final String DEFAULT_DATA_3 = "AAAAAAAAAA";
    private static final String UPDATED_DATA_3 = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATE_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CREATE_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_LAST_MODIFIED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_MODIFIED = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_LAST_MODIFIED = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_CANCEL_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CANCEL_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CANCEL_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/audit-logs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Autowired
    private AuditLogMapper auditLogMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAuditLogMockMvc;

    private AuditLog auditLog;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AuditLog createEntity(EntityManager em) {
        AuditLog auditLog = new AuditLog()
            .userName(DEFAULT_USER_NAME)
            .userDeviceDetails(DEFAULT_USER_DEVICE_DETAILS)
            .action(DEFAULT_ACTION)
            .data1(DEFAULT_DATA_1)
            .data2(DEFAULT_DATA_2)
            .data3(DEFAULT_DATA_3)
            .createDate(DEFAULT_CREATE_DATE)
            .lastModified(DEFAULT_LAST_MODIFIED)
            .cancelDate(DEFAULT_CANCEL_DATE);
        return auditLog;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AuditLog createUpdatedEntity(EntityManager em) {
        AuditLog auditLog = new AuditLog()
            .userName(UPDATED_USER_NAME)
            .userDeviceDetails(UPDATED_USER_DEVICE_DETAILS)
            .action(UPDATED_ACTION)
            .data1(UPDATED_DATA_1)
            .data2(UPDATED_DATA_2)
            .data3(UPDATED_DATA_3)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);
        return auditLog;
    }

    @BeforeEach
    public void initTest() {
        auditLog = createEntity(em);
    }

    @Test
    @Transactional
    void createAuditLog() throws Exception {
        int databaseSizeBeforeCreate = auditLogRepository.findAll().size();
        // Create the AuditLog
        AuditLogDTO auditLogDTO = auditLogMapper.toDto(auditLog);
        restAuditLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(auditLogDTO)))
            .andExpect(status().isCreated());

        // Validate the AuditLog in the database
        List<AuditLog> auditLogList = auditLogRepository.findAll();
        assertThat(auditLogList).hasSize(databaseSizeBeforeCreate + 1);
        AuditLog testAuditLog = auditLogList.get(auditLogList.size() - 1);
        assertThat(testAuditLog.getUserName()).isEqualTo(DEFAULT_USER_NAME);
        assertThat(testAuditLog.getUserDeviceDetails()).isEqualTo(DEFAULT_USER_DEVICE_DETAILS);
        assertThat(testAuditLog.getAction()).isEqualTo(DEFAULT_ACTION);
        assertThat(testAuditLog.getData1()).isEqualTo(DEFAULT_DATA_1);
        assertThat(testAuditLog.getData2()).isEqualTo(DEFAULT_DATA_2);
        assertThat(testAuditLog.getData3()).isEqualTo(DEFAULT_DATA_3);
        assertThat(testAuditLog.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testAuditLog.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
        assertThat(testAuditLog.getCancelDate()).isEqualTo(DEFAULT_CANCEL_DATE);
    }

    @Test
    @Transactional
    void createAuditLogWithExistingId() throws Exception {
        // Create the AuditLog with an existing ID
        auditLog.setId(1L);
        AuditLogDTO auditLogDTO = auditLogMapper.toDto(auditLog);

        int databaseSizeBeforeCreate = auditLogRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAuditLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(auditLogDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AuditLog in the database
        List<AuditLog> auditLogList = auditLogRepository.findAll();
        assertThat(auditLogList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUserNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = auditLogRepository.findAll().size();
        // set the field null
        auditLog.setUserName(null);

        // Create the AuditLog, which fails.
        AuditLogDTO auditLogDTO = auditLogMapper.toDto(auditLog);

        restAuditLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(auditLogDTO)))
            .andExpect(status().isBadRequest());

        List<AuditLog> auditLogList = auditLogRepository.findAll();
        assertThat(auditLogList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUserDeviceDetailsIsRequired() throws Exception {
        int databaseSizeBeforeTest = auditLogRepository.findAll().size();
        // set the field null
        auditLog.setUserDeviceDetails(null);

        // Create the AuditLog, which fails.
        AuditLogDTO auditLogDTO = auditLogMapper.toDto(auditLog);

        restAuditLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(auditLogDTO)))
            .andExpect(status().isBadRequest());

        List<AuditLog> auditLogList = auditLogRepository.findAll();
        assertThat(auditLogList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAuditLogs() throws Exception {
        // Initialize the database
        auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList
        restAuditLogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(auditLog.getId().intValue())))
            .andExpect(jsonPath("$.[*].userName").value(hasItem(DEFAULT_USER_NAME)))
            .andExpect(jsonPath("$.[*].userDeviceDetails").value(hasItem(DEFAULT_USER_DEVICE_DETAILS)))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION)))
            .andExpect(jsonPath("$.[*].data1").value(hasItem(DEFAULT_DATA_1)))
            .andExpect(jsonPath("$.[*].data2").value(hasItem(DEFAULT_DATA_2)))
            .andExpect(jsonPath("$.[*].data3").value(hasItem(DEFAULT_DATA_3)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].cancelDate").value(hasItem(DEFAULT_CANCEL_DATE.toString())));
    }

    @Test
    @Transactional
    void getAuditLog() throws Exception {
        // Initialize the database
        auditLogRepository.saveAndFlush(auditLog);

        // Get the auditLog
        restAuditLogMockMvc
            .perform(get(ENTITY_API_URL_ID, auditLog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(auditLog.getId().intValue()))
            .andExpect(jsonPath("$.userName").value(DEFAULT_USER_NAME))
            .andExpect(jsonPath("$.userDeviceDetails").value(DEFAULT_USER_DEVICE_DETAILS))
            .andExpect(jsonPath("$.action").value(DEFAULT_ACTION))
            .andExpect(jsonPath("$.data1").value(DEFAULT_DATA_1))
            .andExpect(jsonPath("$.data2").value(DEFAULT_DATA_2))
            .andExpect(jsonPath("$.data3").value(DEFAULT_DATA_3))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.lastModified").value(DEFAULT_LAST_MODIFIED.toString()))
            .andExpect(jsonPath("$.cancelDate").value(DEFAULT_CANCEL_DATE.toString()));
    }

    @Test
    @Transactional
    void getAuditLogsByIdFiltering() throws Exception {
        // Initialize the database
        auditLogRepository.saveAndFlush(auditLog);

        Long id = auditLog.getId();

        defaultAuditLogShouldBeFound("id.equals=" + id);
        defaultAuditLogShouldNotBeFound("id.notEquals=" + id);

        defaultAuditLogShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAuditLogShouldNotBeFound("id.greaterThan=" + id);

        defaultAuditLogShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAuditLogShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAuditLogsByUserNameIsEqualToSomething() throws Exception {
        // Initialize the database
        auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where userName equals to DEFAULT_USER_NAME
        defaultAuditLogShouldBeFound("userName.equals=" + DEFAULT_USER_NAME);

        // Get all the auditLogList where userName equals to UPDATED_USER_NAME
        defaultAuditLogShouldNotBeFound("userName.equals=" + UPDATED_USER_NAME);
    }

    @Test
    @Transactional
    void getAllAuditLogsByUserNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where userName not equals to DEFAULT_USER_NAME
        defaultAuditLogShouldNotBeFound("userName.notEquals=" + DEFAULT_USER_NAME);

        // Get all the auditLogList where userName not equals to UPDATED_USER_NAME
        defaultAuditLogShouldBeFound("userName.notEquals=" + UPDATED_USER_NAME);
    }

    @Test
    @Transactional
    void getAllAuditLogsByUserNameIsInShouldWork() throws Exception {
        // Initialize the database
        auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where userName in DEFAULT_USER_NAME or UPDATED_USER_NAME
        defaultAuditLogShouldBeFound("userName.in=" + DEFAULT_USER_NAME + "," + UPDATED_USER_NAME);

        // Get all the auditLogList where userName equals to UPDATED_USER_NAME
        defaultAuditLogShouldNotBeFound("userName.in=" + UPDATED_USER_NAME);
    }

    @Test
    @Transactional
    void getAllAuditLogsByUserNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where userName is not null
        defaultAuditLogShouldBeFound("userName.specified=true");

        // Get all the auditLogList where userName is null
        defaultAuditLogShouldNotBeFound("userName.specified=false");
    }

    @Test
    @Transactional
    void getAllAuditLogsByUserNameContainsSomething() throws Exception {
        // Initialize the database
        auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where userName contains DEFAULT_USER_NAME
        defaultAuditLogShouldBeFound("userName.contains=" + DEFAULT_USER_NAME);

        // Get all the auditLogList where userName contains UPDATED_USER_NAME
        defaultAuditLogShouldNotBeFound("userName.contains=" + UPDATED_USER_NAME);
    }

    @Test
    @Transactional
    void getAllAuditLogsByUserNameNotContainsSomething() throws Exception {
        // Initialize the database
        auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where userName does not contain DEFAULT_USER_NAME
        defaultAuditLogShouldNotBeFound("userName.doesNotContain=" + DEFAULT_USER_NAME);

        // Get all the auditLogList where userName does not contain UPDATED_USER_NAME
        defaultAuditLogShouldBeFound("userName.doesNotContain=" + UPDATED_USER_NAME);
    }

    @Test
    @Transactional
    void getAllAuditLogsByUserDeviceDetailsIsEqualToSomething() throws Exception {
        // Initialize the database
        auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where userDeviceDetails equals to DEFAULT_USER_DEVICE_DETAILS
        defaultAuditLogShouldBeFound("userDeviceDetails.equals=" + DEFAULT_USER_DEVICE_DETAILS);

        // Get all the auditLogList where userDeviceDetails equals to UPDATED_USER_DEVICE_DETAILS
        defaultAuditLogShouldNotBeFound("userDeviceDetails.equals=" + UPDATED_USER_DEVICE_DETAILS);
    }

    @Test
    @Transactional
    void getAllAuditLogsByUserDeviceDetailsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where userDeviceDetails not equals to DEFAULT_USER_DEVICE_DETAILS
        defaultAuditLogShouldNotBeFound("userDeviceDetails.notEquals=" + DEFAULT_USER_DEVICE_DETAILS);

        // Get all the auditLogList where userDeviceDetails not equals to UPDATED_USER_DEVICE_DETAILS
        defaultAuditLogShouldBeFound("userDeviceDetails.notEquals=" + UPDATED_USER_DEVICE_DETAILS);
    }

    @Test
    @Transactional
    void getAllAuditLogsByUserDeviceDetailsIsInShouldWork() throws Exception {
        // Initialize the database
        auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where userDeviceDetails in DEFAULT_USER_DEVICE_DETAILS or UPDATED_USER_DEVICE_DETAILS
        defaultAuditLogShouldBeFound("userDeviceDetails.in=" + DEFAULT_USER_DEVICE_DETAILS + "," + UPDATED_USER_DEVICE_DETAILS);

        // Get all the auditLogList where userDeviceDetails equals to UPDATED_USER_DEVICE_DETAILS
        defaultAuditLogShouldNotBeFound("userDeviceDetails.in=" + UPDATED_USER_DEVICE_DETAILS);
    }

    @Test
    @Transactional
    void getAllAuditLogsByUserDeviceDetailsIsNullOrNotNull() throws Exception {
        // Initialize the database
        auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where userDeviceDetails is not null
        defaultAuditLogShouldBeFound("userDeviceDetails.specified=true");

        // Get all the auditLogList where userDeviceDetails is null
        defaultAuditLogShouldNotBeFound("userDeviceDetails.specified=false");
    }

    @Test
    @Transactional
    void getAllAuditLogsByUserDeviceDetailsContainsSomething() throws Exception {
        // Initialize the database
        auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where userDeviceDetails contains DEFAULT_USER_DEVICE_DETAILS
        defaultAuditLogShouldBeFound("userDeviceDetails.contains=" + DEFAULT_USER_DEVICE_DETAILS);

        // Get all the auditLogList where userDeviceDetails contains UPDATED_USER_DEVICE_DETAILS
        defaultAuditLogShouldNotBeFound("userDeviceDetails.contains=" + UPDATED_USER_DEVICE_DETAILS);
    }

    @Test
    @Transactional
    void getAllAuditLogsByUserDeviceDetailsNotContainsSomething() throws Exception {
        // Initialize the database
        auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where userDeviceDetails does not contain DEFAULT_USER_DEVICE_DETAILS
        defaultAuditLogShouldNotBeFound("userDeviceDetails.doesNotContain=" + DEFAULT_USER_DEVICE_DETAILS);

        // Get all the auditLogList where userDeviceDetails does not contain UPDATED_USER_DEVICE_DETAILS
        defaultAuditLogShouldBeFound("userDeviceDetails.doesNotContain=" + UPDATED_USER_DEVICE_DETAILS);
    }

    @Test
    @Transactional
    void getAllAuditLogsByActionIsEqualToSomething() throws Exception {
        // Initialize the database
        auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where action equals to DEFAULT_ACTION
        defaultAuditLogShouldBeFound("action.equals=" + DEFAULT_ACTION);

        // Get all the auditLogList where action equals to UPDATED_ACTION
        defaultAuditLogShouldNotBeFound("action.equals=" + UPDATED_ACTION);
    }

    @Test
    @Transactional
    void getAllAuditLogsByActionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where action not equals to DEFAULT_ACTION
        defaultAuditLogShouldNotBeFound("action.notEquals=" + DEFAULT_ACTION);

        // Get all the auditLogList where action not equals to UPDATED_ACTION
        defaultAuditLogShouldBeFound("action.notEquals=" + UPDATED_ACTION);
    }

    @Test
    @Transactional
    void getAllAuditLogsByActionIsInShouldWork() throws Exception {
        // Initialize the database
        auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where action in DEFAULT_ACTION or UPDATED_ACTION
        defaultAuditLogShouldBeFound("action.in=" + DEFAULT_ACTION + "," + UPDATED_ACTION);

        // Get all the auditLogList where action equals to UPDATED_ACTION
        defaultAuditLogShouldNotBeFound("action.in=" + UPDATED_ACTION);
    }

    @Test
    @Transactional
    void getAllAuditLogsByActionIsNullOrNotNull() throws Exception {
        // Initialize the database
        auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where action is not null
        defaultAuditLogShouldBeFound("action.specified=true");

        // Get all the auditLogList where action is null
        defaultAuditLogShouldNotBeFound("action.specified=false");
    }

    @Test
    @Transactional
    void getAllAuditLogsByActionContainsSomething() throws Exception {
        // Initialize the database
        auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where action contains DEFAULT_ACTION
        defaultAuditLogShouldBeFound("action.contains=" + DEFAULT_ACTION);

        // Get all the auditLogList where action contains UPDATED_ACTION
        defaultAuditLogShouldNotBeFound("action.contains=" + UPDATED_ACTION);
    }

    @Test
    @Transactional
    void getAllAuditLogsByActionNotContainsSomething() throws Exception {
        // Initialize the database
        auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where action does not contain DEFAULT_ACTION
        defaultAuditLogShouldNotBeFound("action.doesNotContain=" + DEFAULT_ACTION);

        // Get all the auditLogList where action does not contain UPDATED_ACTION
        defaultAuditLogShouldBeFound("action.doesNotContain=" + UPDATED_ACTION);
    }

    @Test
    @Transactional
    void getAllAuditLogsByData1IsEqualToSomething() throws Exception {
        // Initialize the database
        auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where data1 equals to DEFAULT_DATA_1
        defaultAuditLogShouldBeFound("data1.equals=" + DEFAULT_DATA_1);

        // Get all the auditLogList where data1 equals to UPDATED_DATA_1
        defaultAuditLogShouldNotBeFound("data1.equals=" + UPDATED_DATA_1);
    }

    @Test
    @Transactional
    void getAllAuditLogsByData1IsNotEqualToSomething() throws Exception {
        // Initialize the database
        auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where data1 not equals to DEFAULT_DATA_1
        defaultAuditLogShouldNotBeFound("data1.notEquals=" + DEFAULT_DATA_1);

        // Get all the auditLogList where data1 not equals to UPDATED_DATA_1
        defaultAuditLogShouldBeFound("data1.notEquals=" + UPDATED_DATA_1);
    }

    @Test
    @Transactional
    void getAllAuditLogsByData1IsInShouldWork() throws Exception {
        // Initialize the database
        auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where data1 in DEFAULT_DATA_1 or UPDATED_DATA_1
        defaultAuditLogShouldBeFound("data1.in=" + DEFAULT_DATA_1 + "," + UPDATED_DATA_1);

        // Get all the auditLogList where data1 equals to UPDATED_DATA_1
        defaultAuditLogShouldNotBeFound("data1.in=" + UPDATED_DATA_1);
    }

    @Test
    @Transactional
    void getAllAuditLogsByData1IsNullOrNotNull() throws Exception {
        // Initialize the database
        auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where data1 is not null
        defaultAuditLogShouldBeFound("data1.specified=true");

        // Get all the auditLogList where data1 is null
        defaultAuditLogShouldNotBeFound("data1.specified=false");
    }

    @Test
    @Transactional
    void getAllAuditLogsByData1ContainsSomething() throws Exception {
        // Initialize the database
        auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where data1 contains DEFAULT_DATA_1
        defaultAuditLogShouldBeFound("data1.contains=" + DEFAULT_DATA_1);

        // Get all the auditLogList where data1 contains UPDATED_DATA_1
        defaultAuditLogShouldNotBeFound("data1.contains=" + UPDATED_DATA_1);
    }

    @Test
    @Transactional
    void getAllAuditLogsByData1NotContainsSomething() throws Exception {
        // Initialize the database
        auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where data1 does not contain DEFAULT_DATA_1
        defaultAuditLogShouldNotBeFound("data1.doesNotContain=" + DEFAULT_DATA_1);

        // Get all the auditLogList where data1 does not contain UPDATED_DATA_1
        defaultAuditLogShouldBeFound("data1.doesNotContain=" + UPDATED_DATA_1);
    }

    @Test
    @Transactional
    void getAllAuditLogsByData2IsEqualToSomething() throws Exception {
        // Initialize the database
        auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where data2 equals to DEFAULT_DATA_2
        defaultAuditLogShouldBeFound("data2.equals=" + DEFAULT_DATA_2);

        // Get all the auditLogList where data2 equals to UPDATED_DATA_2
        defaultAuditLogShouldNotBeFound("data2.equals=" + UPDATED_DATA_2);
    }

    @Test
    @Transactional
    void getAllAuditLogsByData2IsNotEqualToSomething() throws Exception {
        // Initialize the database
        auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where data2 not equals to DEFAULT_DATA_2
        defaultAuditLogShouldNotBeFound("data2.notEquals=" + DEFAULT_DATA_2);

        // Get all the auditLogList where data2 not equals to UPDATED_DATA_2
        defaultAuditLogShouldBeFound("data2.notEquals=" + UPDATED_DATA_2);
    }

    @Test
    @Transactional
    void getAllAuditLogsByData2IsInShouldWork() throws Exception {
        // Initialize the database
        auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where data2 in DEFAULT_DATA_2 or UPDATED_DATA_2
        defaultAuditLogShouldBeFound("data2.in=" + DEFAULT_DATA_2 + "," + UPDATED_DATA_2);

        // Get all the auditLogList where data2 equals to UPDATED_DATA_2
        defaultAuditLogShouldNotBeFound("data2.in=" + UPDATED_DATA_2);
    }

    @Test
    @Transactional
    void getAllAuditLogsByData2IsNullOrNotNull() throws Exception {
        // Initialize the database
        auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where data2 is not null
        defaultAuditLogShouldBeFound("data2.specified=true");

        // Get all the auditLogList where data2 is null
        defaultAuditLogShouldNotBeFound("data2.specified=false");
    }

    @Test
    @Transactional
    void getAllAuditLogsByData2ContainsSomething() throws Exception {
        // Initialize the database
        auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where data2 contains DEFAULT_DATA_2
        defaultAuditLogShouldBeFound("data2.contains=" + DEFAULT_DATA_2);

        // Get all the auditLogList where data2 contains UPDATED_DATA_2
        defaultAuditLogShouldNotBeFound("data2.contains=" + UPDATED_DATA_2);
    }

    @Test
    @Transactional
    void getAllAuditLogsByData2NotContainsSomething() throws Exception {
        // Initialize the database
        auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where data2 does not contain DEFAULT_DATA_2
        defaultAuditLogShouldNotBeFound("data2.doesNotContain=" + DEFAULT_DATA_2);

        // Get all the auditLogList where data2 does not contain UPDATED_DATA_2
        defaultAuditLogShouldBeFound("data2.doesNotContain=" + UPDATED_DATA_2);
    }

    @Test
    @Transactional
    void getAllAuditLogsByData3IsEqualToSomething() throws Exception {
        // Initialize the database
        auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where data3 equals to DEFAULT_DATA_3
        defaultAuditLogShouldBeFound("data3.equals=" + DEFAULT_DATA_3);

        // Get all the auditLogList where data3 equals to UPDATED_DATA_3
        defaultAuditLogShouldNotBeFound("data3.equals=" + UPDATED_DATA_3);
    }

    @Test
    @Transactional
    void getAllAuditLogsByData3IsNotEqualToSomething() throws Exception {
        // Initialize the database
        auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where data3 not equals to DEFAULT_DATA_3
        defaultAuditLogShouldNotBeFound("data3.notEquals=" + DEFAULT_DATA_3);

        // Get all the auditLogList where data3 not equals to UPDATED_DATA_3
        defaultAuditLogShouldBeFound("data3.notEquals=" + UPDATED_DATA_3);
    }

    @Test
    @Transactional
    void getAllAuditLogsByData3IsInShouldWork() throws Exception {
        // Initialize the database
        auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where data3 in DEFAULT_DATA_3 or UPDATED_DATA_3
        defaultAuditLogShouldBeFound("data3.in=" + DEFAULT_DATA_3 + "," + UPDATED_DATA_3);

        // Get all the auditLogList where data3 equals to UPDATED_DATA_3
        defaultAuditLogShouldNotBeFound("data3.in=" + UPDATED_DATA_3);
    }

    @Test
    @Transactional
    void getAllAuditLogsByData3IsNullOrNotNull() throws Exception {
        // Initialize the database
        auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where data3 is not null
        defaultAuditLogShouldBeFound("data3.specified=true");

        // Get all the auditLogList where data3 is null
        defaultAuditLogShouldNotBeFound("data3.specified=false");
    }

    @Test
    @Transactional
    void getAllAuditLogsByData3ContainsSomething() throws Exception {
        // Initialize the database
        auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where data3 contains DEFAULT_DATA_3
        defaultAuditLogShouldBeFound("data3.contains=" + DEFAULT_DATA_3);

        // Get all the auditLogList where data3 contains UPDATED_DATA_3
        defaultAuditLogShouldNotBeFound("data3.contains=" + UPDATED_DATA_3);
    }

    @Test
    @Transactional
    void getAllAuditLogsByData3NotContainsSomething() throws Exception {
        // Initialize the database
        auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where data3 does not contain DEFAULT_DATA_3
        defaultAuditLogShouldNotBeFound("data3.doesNotContain=" + DEFAULT_DATA_3);

        // Get all the auditLogList where data3 does not contain UPDATED_DATA_3
        defaultAuditLogShouldBeFound("data3.doesNotContain=" + UPDATED_DATA_3);
    }

    @Test
    @Transactional
    void getAllAuditLogsByCreateDateIsEqualToSomething() throws Exception {
        // Initialize the database
        auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where createDate equals to DEFAULT_CREATE_DATE
        defaultAuditLogShouldBeFound("createDate.equals=" + DEFAULT_CREATE_DATE);

        // Get all the auditLogList where createDate equals to UPDATED_CREATE_DATE
        defaultAuditLogShouldNotBeFound("createDate.equals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllAuditLogsByCreateDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where createDate not equals to DEFAULT_CREATE_DATE
        defaultAuditLogShouldNotBeFound("createDate.notEquals=" + DEFAULT_CREATE_DATE);

        // Get all the auditLogList where createDate not equals to UPDATED_CREATE_DATE
        defaultAuditLogShouldBeFound("createDate.notEquals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllAuditLogsByCreateDateIsInShouldWork() throws Exception {
        // Initialize the database
        auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where createDate in DEFAULT_CREATE_DATE or UPDATED_CREATE_DATE
        defaultAuditLogShouldBeFound("createDate.in=" + DEFAULT_CREATE_DATE + "," + UPDATED_CREATE_DATE);

        // Get all the auditLogList where createDate equals to UPDATED_CREATE_DATE
        defaultAuditLogShouldNotBeFound("createDate.in=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllAuditLogsByCreateDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where createDate is not null
        defaultAuditLogShouldBeFound("createDate.specified=true");

        // Get all the auditLogList where createDate is null
        defaultAuditLogShouldNotBeFound("createDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAuditLogsByCreateDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where createDate is greater than or equal to DEFAULT_CREATE_DATE
        defaultAuditLogShouldBeFound("createDate.greaterThanOrEqual=" + DEFAULT_CREATE_DATE);

        // Get all the auditLogList where createDate is greater than or equal to UPDATED_CREATE_DATE
        defaultAuditLogShouldNotBeFound("createDate.greaterThanOrEqual=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllAuditLogsByCreateDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where createDate is less than or equal to DEFAULT_CREATE_DATE
        defaultAuditLogShouldBeFound("createDate.lessThanOrEqual=" + DEFAULT_CREATE_DATE);

        // Get all the auditLogList where createDate is less than or equal to SMALLER_CREATE_DATE
        defaultAuditLogShouldNotBeFound("createDate.lessThanOrEqual=" + SMALLER_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllAuditLogsByCreateDateIsLessThanSomething() throws Exception {
        // Initialize the database
        auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where createDate is less than DEFAULT_CREATE_DATE
        defaultAuditLogShouldNotBeFound("createDate.lessThan=" + DEFAULT_CREATE_DATE);

        // Get all the auditLogList where createDate is less than UPDATED_CREATE_DATE
        defaultAuditLogShouldBeFound("createDate.lessThan=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllAuditLogsByCreateDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where createDate is greater than DEFAULT_CREATE_DATE
        defaultAuditLogShouldNotBeFound("createDate.greaterThan=" + DEFAULT_CREATE_DATE);

        // Get all the auditLogList where createDate is greater than SMALLER_CREATE_DATE
        defaultAuditLogShouldBeFound("createDate.greaterThan=" + SMALLER_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllAuditLogsByLastModifiedIsEqualToSomething() throws Exception {
        // Initialize the database
        auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where lastModified equals to DEFAULT_LAST_MODIFIED
        defaultAuditLogShouldBeFound("lastModified.equals=" + DEFAULT_LAST_MODIFIED);

        // Get all the auditLogList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultAuditLogShouldNotBeFound("lastModified.equals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllAuditLogsByLastModifiedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where lastModified not equals to DEFAULT_LAST_MODIFIED
        defaultAuditLogShouldNotBeFound("lastModified.notEquals=" + DEFAULT_LAST_MODIFIED);

        // Get all the auditLogList where lastModified not equals to UPDATED_LAST_MODIFIED
        defaultAuditLogShouldBeFound("lastModified.notEquals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllAuditLogsByLastModifiedIsInShouldWork() throws Exception {
        // Initialize the database
        auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where lastModified in DEFAULT_LAST_MODIFIED or UPDATED_LAST_MODIFIED
        defaultAuditLogShouldBeFound("lastModified.in=" + DEFAULT_LAST_MODIFIED + "," + UPDATED_LAST_MODIFIED);

        // Get all the auditLogList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultAuditLogShouldNotBeFound("lastModified.in=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllAuditLogsByLastModifiedIsNullOrNotNull() throws Exception {
        // Initialize the database
        auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where lastModified is not null
        defaultAuditLogShouldBeFound("lastModified.specified=true");

        // Get all the auditLogList where lastModified is null
        defaultAuditLogShouldNotBeFound("lastModified.specified=false");
    }

    @Test
    @Transactional
    void getAllAuditLogsByLastModifiedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where lastModified is greater than or equal to DEFAULT_LAST_MODIFIED
        defaultAuditLogShouldBeFound("lastModified.greaterThanOrEqual=" + DEFAULT_LAST_MODIFIED);

        // Get all the auditLogList where lastModified is greater than or equal to UPDATED_LAST_MODIFIED
        defaultAuditLogShouldNotBeFound("lastModified.greaterThanOrEqual=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllAuditLogsByLastModifiedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where lastModified is less than or equal to DEFAULT_LAST_MODIFIED
        defaultAuditLogShouldBeFound("lastModified.lessThanOrEqual=" + DEFAULT_LAST_MODIFIED);

        // Get all the auditLogList where lastModified is less than or equal to SMALLER_LAST_MODIFIED
        defaultAuditLogShouldNotBeFound("lastModified.lessThanOrEqual=" + SMALLER_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllAuditLogsByLastModifiedIsLessThanSomething() throws Exception {
        // Initialize the database
        auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where lastModified is less than DEFAULT_LAST_MODIFIED
        defaultAuditLogShouldNotBeFound("lastModified.lessThan=" + DEFAULT_LAST_MODIFIED);

        // Get all the auditLogList where lastModified is less than UPDATED_LAST_MODIFIED
        defaultAuditLogShouldBeFound("lastModified.lessThan=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllAuditLogsByLastModifiedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where lastModified is greater than DEFAULT_LAST_MODIFIED
        defaultAuditLogShouldNotBeFound("lastModified.greaterThan=" + DEFAULT_LAST_MODIFIED);

        // Get all the auditLogList where lastModified is greater than SMALLER_LAST_MODIFIED
        defaultAuditLogShouldBeFound("lastModified.greaterThan=" + SMALLER_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllAuditLogsByCancelDateIsEqualToSomething() throws Exception {
        // Initialize the database
        auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where cancelDate equals to DEFAULT_CANCEL_DATE
        defaultAuditLogShouldBeFound("cancelDate.equals=" + DEFAULT_CANCEL_DATE);

        // Get all the auditLogList where cancelDate equals to UPDATED_CANCEL_DATE
        defaultAuditLogShouldNotBeFound("cancelDate.equals=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllAuditLogsByCancelDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where cancelDate not equals to DEFAULT_CANCEL_DATE
        defaultAuditLogShouldNotBeFound("cancelDate.notEquals=" + DEFAULT_CANCEL_DATE);

        // Get all the auditLogList where cancelDate not equals to UPDATED_CANCEL_DATE
        defaultAuditLogShouldBeFound("cancelDate.notEquals=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllAuditLogsByCancelDateIsInShouldWork() throws Exception {
        // Initialize the database
        auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where cancelDate in DEFAULT_CANCEL_DATE or UPDATED_CANCEL_DATE
        defaultAuditLogShouldBeFound("cancelDate.in=" + DEFAULT_CANCEL_DATE + "," + UPDATED_CANCEL_DATE);

        // Get all the auditLogList where cancelDate equals to UPDATED_CANCEL_DATE
        defaultAuditLogShouldNotBeFound("cancelDate.in=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllAuditLogsByCancelDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where cancelDate is not null
        defaultAuditLogShouldBeFound("cancelDate.specified=true");

        // Get all the auditLogList where cancelDate is null
        defaultAuditLogShouldNotBeFound("cancelDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAuditLogsByCancelDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where cancelDate is greater than or equal to DEFAULT_CANCEL_DATE
        defaultAuditLogShouldBeFound("cancelDate.greaterThanOrEqual=" + DEFAULT_CANCEL_DATE);

        // Get all the auditLogList where cancelDate is greater than or equal to UPDATED_CANCEL_DATE
        defaultAuditLogShouldNotBeFound("cancelDate.greaterThanOrEqual=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllAuditLogsByCancelDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where cancelDate is less than or equal to DEFAULT_CANCEL_DATE
        defaultAuditLogShouldBeFound("cancelDate.lessThanOrEqual=" + DEFAULT_CANCEL_DATE);

        // Get all the auditLogList where cancelDate is less than or equal to SMALLER_CANCEL_DATE
        defaultAuditLogShouldNotBeFound("cancelDate.lessThanOrEqual=" + SMALLER_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllAuditLogsByCancelDateIsLessThanSomething() throws Exception {
        // Initialize the database
        auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where cancelDate is less than DEFAULT_CANCEL_DATE
        defaultAuditLogShouldNotBeFound("cancelDate.lessThan=" + DEFAULT_CANCEL_DATE);

        // Get all the auditLogList where cancelDate is less than UPDATED_CANCEL_DATE
        defaultAuditLogShouldBeFound("cancelDate.lessThan=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllAuditLogsByCancelDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where cancelDate is greater than DEFAULT_CANCEL_DATE
        defaultAuditLogShouldNotBeFound("cancelDate.greaterThan=" + DEFAULT_CANCEL_DATE);

        // Get all the auditLogList where cancelDate is greater than SMALLER_CANCEL_DATE
        defaultAuditLogShouldBeFound("cancelDate.greaterThan=" + SMALLER_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllAuditLogsBySchoolIsEqualToSomething() throws Exception {
        // Initialize the database
        auditLogRepository.saveAndFlush(auditLog);
        School school;
        if (TestUtil.findAll(em, School.class).isEmpty()) {
            school = SchoolResourceIT.createEntity(em);
            em.persist(school);
            em.flush();
        } else {
            school = TestUtil.findAll(em, School.class).get(0);
        }
        em.persist(school);
        em.flush();
        auditLog.setSchool(school);
        auditLogRepository.saveAndFlush(auditLog);
        Long schoolId = school.getId();

        // Get all the auditLogList where school equals to schoolId
        defaultAuditLogShouldBeFound("schoolId.equals=" + schoolId);

        // Get all the auditLogList where school equals to (schoolId + 1)
        defaultAuditLogShouldNotBeFound("schoolId.equals=" + (schoolId + 1));
    }

    @Test
    @Transactional
    void getAllAuditLogsBySchoolUserIsEqualToSomething() throws Exception {
        // Initialize the database
        auditLogRepository.saveAndFlush(auditLog);
        SchoolUser schoolUser;
        if (TestUtil.findAll(em, SchoolUser.class).isEmpty()) {
            schoolUser = SchoolUserResourceIT.createEntity(em);
            em.persist(schoolUser);
            em.flush();
        } else {
            schoolUser = TestUtil.findAll(em, SchoolUser.class).get(0);
        }
        em.persist(schoolUser);
        em.flush();
        auditLog.setSchoolUser(schoolUser);
        auditLogRepository.saveAndFlush(auditLog);
        Long schoolUserId = schoolUser.getId();

        // Get all the auditLogList where schoolUser equals to schoolUserId
        defaultAuditLogShouldBeFound("schoolUserId.equals=" + schoolUserId);

        // Get all the auditLogList where schoolUser equals to (schoolUserId + 1)
        defaultAuditLogShouldNotBeFound("schoolUserId.equals=" + (schoolUserId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAuditLogShouldBeFound(String filter) throws Exception {
        restAuditLogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(auditLog.getId().intValue())))
            .andExpect(jsonPath("$.[*].userName").value(hasItem(DEFAULT_USER_NAME)))
            .andExpect(jsonPath("$.[*].userDeviceDetails").value(hasItem(DEFAULT_USER_DEVICE_DETAILS)))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION)))
            .andExpect(jsonPath("$.[*].data1").value(hasItem(DEFAULT_DATA_1)))
            .andExpect(jsonPath("$.[*].data2").value(hasItem(DEFAULT_DATA_2)))
            .andExpect(jsonPath("$.[*].data3").value(hasItem(DEFAULT_DATA_3)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].cancelDate").value(hasItem(DEFAULT_CANCEL_DATE.toString())));

        // Check, that the count call also returns 1
        restAuditLogMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAuditLogShouldNotBeFound(String filter) throws Exception {
        restAuditLogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAuditLogMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAuditLog() throws Exception {
        // Get the auditLog
        restAuditLogMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAuditLog() throws Exception {
        // Initialize the database
        auditLogRepository.saveAndFlush(auditLog);

        int databaseSizeBeforeUpdate = auditLogRepository.findAll().size();

        // Update the auditLog
        AuditLog updatedAuditLog = auditLogRepository.findById(auditLog.getId()).get();
        // Disconnect from session so that the updates on updatedAuditLog are not directly saved in db
        em.detach(updatedAuditLog);
        updatedAuditLog
            .userName(UPDATED_USER_NAME)
            .userDeviceDetails(UPDATED_USER_DEVICE_DETAILS)
            .action(UPDATED_ACTION)
            .data1(UPDATED_DATA_1)
            .data2(UPDATED_DATA_2)
            .data3(UPDATED_DATA_3)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);
        AuditLogDTO auditLogDTO = auditLogMapper.toDto(updatedAuditLog);

        restAuditLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, auditLogDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(auditLogDTO))
            )
            .andExpect(status().isOk());

        // Validate the AuditLog in the database
        List<AuditLog> auditLogList = auditLogRepository.findAll();
        assertThat(auditLogList).hasSize(databaseSizeBeforeUpdate);
        AuditLog testAuditLog = auditLogList.get(auditLogList.size() - 1);
        assertThat(testAuditLog.getUserName()).isEqualTo(UPDATED_USER_NAME);
        assertThat(testAuditLog.getUserDeviceDetails()).isEqualTo(UPDATED_USER_DEVICE_DETAILS);
        assertThat(testAuditLog.getAction()).isEqualTo(UPDATED_ACTION);
        assertThat(testAuditLog.getData1()).isEqualTo(UPDATED_DATA_1);
        assertThat(testAuditLog.getData2()).isEqualTo(UPDATED_DATA_2);
        assertThat(testAuditLog.getData3()).isEqualTo(UPDATED_DATA_3);
        assertThat(testAuditLog.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testAuditLog.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testAuditLog.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void putNonExistingAuditLog() throws Exception {
        int databaseSizeBeforeUpdate = auditLogRepository.findAll().size();
        auditLog.setId(count.incrementAndGet());

        // Create the AuditLog
        AuditLogDTO auditLogDTO = auditLogMapper.toDto(auditLog);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAuditLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, auditLogDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(auditLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AuditLog in the database
        List<AuditLog> auditLogList = auditLogRepository.findAll();
        assertThat(auditLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAuditLog() throws Exception {
        int databaseSizeBeforeUpdate = auditLogRepository.findAll().size();
        auditLog.setId(count.incrementAndGet());

        // Create the AuditLog
        AuditLogDTO auditLogDTO = auditLogMapper.toDto(auditLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAuditLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(auditLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AuditLog in the database
        List<AuditLog> auditLogList = auditLogRepository.findAll();
        assertThat(auditLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAuditLog() throws Exception {
        int databaseSizeBeforeUpdate = auditLogRepository.findAll().size();
        auditLog.setId(count.incrementAndGet());

        // Create the AuditLog
        AuditLogDTO auditLogDTO = auditLogMapper.toDto(auditLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAuditLogMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(auditLogDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AuditLog in the database
        List<AuditLog> auditLogList = auditLogRepository.findAll();
        assertThat(auditLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAuditLogWithPatch() throws Exception {
        // Initialize the database
        auditLogRepository.saveAndFlush(auditLog);

        int databaseSizeBeforeUpdate = auditLogRepository.findAll().size();

        // Update the auditLog using partial update
        AuditLog partialUpdatedAuditLog = new AuditLog();
        partialUpdatedAuditLog.setId(auditLog.getId());

        partialUpdatedAuditLog
            .userName(UPDATED_USER_NAME)
            .userDeviceDetails(UPDATED_USER_DEVICE_DETAILS)
            .data1(UPDATED_DATA_1)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);

        restAuditLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAuditLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAuditLog))
            )
            .andExpect(status().isOk());

        // Validate the AuditLog in the database
        List<AuditLog> auditLogList = auditLogRepository.findAll();
        assertThat(auditLogList).hasSize(databaseSizeBeforeUpdate);
        AuditLog testAuditLog = auditLogList.get(auditLogList.size() - 1);
        assertThat(testAuditLog.getUserName()).isEqualTo(UPDATED_USER_NAME);
        assertThat(testAuditLog.getUserDeviceDetails()).isEqualTo(UPDATED_USER_DEVICE_DETAILS);
        assertThat(testAuditLog.getAction()).isEqualTo(DEFAULT_ACTION);
        assertThat(testAuditLog.getData1()).isEqualTo(UPDATED_DATA_1);
        assertThat(testAuditLog.getData2()).isEqualTo(DEFAULT_DATA_2);
        assertThat(testAuditLog.getData3()).isEqualTo(DEFAULT_DATA_3);
        assertThat(testAuditLog.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testAuditLog.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testAuditLog.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void fullUpdateAuditLogWithPatch() throws Exception {
        // Initialize the database
        auditLogRepository.saveAndFlush(auditLog);

        int databaseSizeBeforeUpdate = auditLogRepository.findAll().size();

        // Update the auditLog using partial update
        AuditLog partialUpdatedAuditLog = new AuditLog();
        partialUpdatedAuditLog.setId(auditLog.getId());

        partialUpdatedAuditLog
            .userName(UPDATED_USER_NAME)
            .userDeviceDetails(UPDATED_USER_DEVICE_DETAILS)
            .action(UPDATED_ACTION)
            .data1(UPDATED_DATA_1)
            .data2(UPDATED_DATA_2)
            .data3(UPDATED_DATA_3)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);

        restAuditLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAuditLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAuditLog))
            )
            .andExpect(status().isOk());

        // Validate the AuditLog in the database
        List<AuditLog> auditLogList = auditLogRepository.findAll();
        assertThat(auditLogList).hasSize(databaseSizeBeforeUpdate);
        AuditLog testAuditLog = auditLogList.get(auditLogList.size() - 1);
        assertThat(testAuditLog.getUserName()).isEqualTo(UPDATED_USER_NAME);
        assertThat(testAuditLog.getUserDeviceDetails()).isEqualTo(UPDATED_USER_DEVICE_DETAILS);
        assertThat(testAuditLog.getAction()).isEqualTo(UPDATED_ACTION);
        assertThat(testAuditLog.getData1()).isEqualTo(UPDATED_DATA_1);
        assertThat(testAuditLog.getData2()).isEqualTo(UPDATED_DATA_2);
        assertThat(testAuditLog.getData3()).isEqualTo(UPDATED_DATA_3);
        assertThat(testAuditLog.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testAuditLog.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testAuditLog.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingAuditLog() throws Exception {
        int databaseSizeBeforeUpdate = auditLogRepository.findAll().size();
        auditLog.setId(count.incrementAndGet());

        // Create the AuditLog
        AuditLogDTO auditLogDTO = auditLogMapper.toDto(auditLog);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAuditLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, auditLogDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(auditLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AuditLog in the database
        List<AuditLog> auditLogList = auditLogRepository.findAll();
        assertThat(auditLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAuditLog() throws Exception {
        int databaseSizeBeforeUpdate = auditLogRepository.findAll().size();
        auditLog.setId(count.incrementAndGet());

        // Create the AuditLog
        AuditLogDTO auditLogDTO = auditLogMapper.toDto(auditLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAuditLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(auditLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AuditLog in the database
        List<AuditLog> auditLogList = auditLogRepository.findAll();
        assertThat(auditLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAuditLog() throws Exception {
        int databaseSizeBeforeUpdate = auditLogRepository.findAll().size();
        auditLog.setId(count.incrementAndGet());

        // Create the AuditLog
        AuditLogDTO auditLogDTO = auditLogMapper.toDto(auditLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAuditLogMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(auditLogDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AuditLog in the database
        List<AuditLog> auditLogList = auditLogRepository.findAll();
        assertThat(auditLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAuditLog() throws Exception {
        // Initialize the database
        auditLogRepository.saveAndFlush(auditLog);

        int databaseSizeBeforeDelete = auditLogRepository.findAll().size();

        // Delete the auditLog
        restAuditLogMockMvc
            .perform(delete(ENTITY_API_URL_ID, auditLog.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AuditLog> auditLogList = auditLogRepository.findAll();
        assertThat(auditLogList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
