package com.ssik.manageit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ssik.manageit.IntegrationTest;
import com.ssik.manageit.domain.SchoolClass;
import com.ssik.manageit.domain.SchoolEvent;
import com.ssik.manageit.repository.SchoolEventRepository;
import com.ssik.manageit.service.SchoolEventService;
import com.ssik.manageit.service.criteria.SchoolEventCriteria;
import com.ssik.manageit.service.dto.SchoolEventDTO;
import com.ssik.manageit.service.mapper.SchoolEventMapper;
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
 * Integration tests for the {@link SchoolEventResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SchoolEventResourceIT {

    private static final String DEFAULT_EVENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_EVENT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EVENT_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_EVENT_DETAILS = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_START_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_END_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_CREATE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATE_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CREATE_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_LAST_MODIFIED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_MODIFIED = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_LAST_MODIFIED = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_CANCEL_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CANCEL_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CANCEL_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/school-events";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SchoolEventRepository schoolEventRepository;

    @Mock
    private SchoolEventRepository schoolEventRepositoryMock;

    @Autowired
    private SchoolEventMapper schoolEventMapper;

    @Mock
    private SchoolEventService schoolEventServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSchoolEventMockMvc;

    private SchoolEvent schoolEvent;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SchoolEvent createEntity(EntityManager em) {
        SchoolEvent schoolEvent = new SchoolEvent()
            .eventName(DEFAULT_EVENT_NAME)
            .eventDetails(DEFAULT_EVENT_DETAILS)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .createDate(DEFAULT_CREATE_DATE)
            .lastModified(DEFAULT_LAST_MODIFIED)
            .cancelDate(DEFAULT_CANCEL_DATE);
        return schoolEvent;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SchoolEvent createUpdatedEntity(EntityManager em) {
        SchoolEvent schoolEvent = new SchoolEvent()
            .eventName(UPDATED_EVENT_NAME)
            .eventDetails(UPDATED_EVENT_DETAILS)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);
        return schoolEvent;
    }

    @BeforeEach
    public void initTest() {
        schoolEvent = createEntity(em);
    }

    @Test
    @Transactional
    void createSchoolEvent() throws Exception {
        int databaseSizeBeforeCreate = schoolEventRepository.findAll().size();
        // Create the SchoolEvent
        SchoolEventDTO schoolEventDTO = schoolEventMapper.toDto(schoolEvent);
        restSchoolEventMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(schoolEventDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SchoolEvent in the database
        List<SchoolEvent> schoolEventList = schoolEventRepository.findAll();
        assertThat(schoolEventList).hasSize(databaseSizeBeforeCreate + 1);
        SchoolEvent testSchoolEvent = schoolEventList.get(schoolEventList.size() - 1);
        assertThat(testSchoolEvent.getEventName()).isEqualTo(DEFAULT_EVENT_NAME);
        assertThat(testSchoolEvent.getEventDetails()).isEqualTo(DEFAULT_EVENT_DETAILS);
        assertThat(testSchoolEvent.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testSchoolEvent.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testSchoolEvent.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testSchoolEvent.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
        assertThat(testSchoolEvent.getCancelDate()).isEqualTo(DEFAULT_CANCEL_DATE);
    }

    @Test
    @Transactional
    void createSchoolEventWithExistingId() throws Exception {
        // Create the SchoolEvent with an existing ID
        schoolEvent.setId(1L);
        SchoolEventDTO schoolEventDTO = schoolEventMapper.toDto(schoolEvent);

        int databaseSizeBeforeCreate = schoolEventRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSchoolEventMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(schoolEventDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchoolEvent in the database
        List<SchoolEvent> schoolEventList = schoolEventRepository.findAll();
        assertThat(schoolEventList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEventNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = schoolEventRepository.findAll().size();
        // set the field null
        schoolEvent.setEventName(null);

        // Create the SchoolEvent, which fails.
        SchoolEventDTO schoolEventDTO = schoolEventMapper.toDto(schoolEvent);

        restSchoolEventMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(schoolEventDTO))
            )
            .andExpect(status().isBadRequest());

        List<SchoolEvent> schoolEventList = schoolEventRepository.findAll();
        assertThat(schoolEventList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = schoolEventRepository.findAll().size();
        // set the field null
        schoolEvent.setStartDate(null);

        // Create the SchoolEvent, which fails.
        SchoolEventDTO schoolEventDTO = schoolEventMapper.toDto(schoolEvent);

        restSchoolEventMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(schoolEventDTO))
            )
            .andExpect(status().isBadRequest());

        List<SchoolEvent> schoolEventList = schoolEventRepository.findAll();
        assertThat(schoolEventList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = schoolEventRepository.findAll().size();
        // set the field null
        schoolEvent.setEndDate(null);

        // Create the SchoolEvent, which fails.
        SchoolEventDTO schoolEventDTO = schoolEventMapper.toDto(schoolEvent);

        restSchoolEventMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(schoolEventDTO))
            )
            .andExpect(status().isBadRequest());

        List<SchoolEvent> schoolEventList = schoolEventRepository.findAll();
        assertThat(schoolEventList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSchoolEvents() throws Exception {
        // Initialize the database
        schoolEventRepository.saveAndFlush(schoolEvent);

        // Get all the schoolEventList
        restSchoolEventMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(schoolEvent.getId().intValue())))
            .andExpect(jsonPath("$.[*].eventName").value(hasItem(DEFAULT_EVENT_NAME)))
            .andExpect(jsonPath("$.[*].eventDetails").value(hasItem(DEFAULT_EVENT_DETAILS)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].cancelDate").value(hasItem(DEFAULT_CANCEL_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSchoolEventsWithEagerRelationshipsIsEnabled() throws Exception {
        when(schoolEventServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSchoolEventMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(schoolEventServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSchoolEventsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(schoolEventServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSchoolEventMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(schoolEventServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getSchoolEvent() throws Exception {
        // Initialize the database
        schoolEventRepository.saveAndFlush(schoolEvent);

        // Get the schoolEvent
        restSchoolEventMockMvc
            .perform(get(ENTITY_API_URL_ID, schoolEvent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(schoolEvent.getId().intValue()))
            .andExpect(jsonPath("$.eventName").value(DEFAULT_EVENT_NAME))
            .andExpect(jsonPath("$.eventDetails").value(DEFAULT_EVENT_DETAILS))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.lastModified").value(DEFAULT_LAST_MODIFIED.toString()))
            .andExpect(jsonPath("$.cancelDate").value(DEFAULT_CANCEL_DATE.toString()));
    }

    @Test
    @Transactional
    void getSchoolEventsByIdFiltering() throws Exception {
        // Initialize the database
        schoolEventRepository.saveAndFlush(schoolEvent);

        Long id = schoolEvent.getId();

        defaultSchoolEventShouldBeFound("id.equals=" + id);
        defaultSchoolEventShouldNotBeFound("id.notEquals=" + id);

        defaultSchoolEventShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSchoolEventShouldNotBeFound("id.greaterThan=" + id);

        defaultSchoolEventShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSchoolEventShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSchoolEventsByEventNameIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolEventRepository.saveAndFlush(schoolEvent);

        // Get all the schoolEventList where eventName equals to DEFAULT_EVENT_NAME
        defaultSchoolEventShouldBeFound("eventName.equals=" + DEFAULT_EVENT_NAME);

        // Get all the schoolEventList where eventName equals to UPDATED_EVENT_NAME
        defaultSchoolEventShouldNotBeFound("eventName.equals=" + UPDATED_EVENT_NAME);
    }

    @Test
    @Transactional
    void getAllSchoolEventsByEventNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        schoolEventRepository.saveAndFlush(schoolEvent);

        // Get all the schoolEventList where eventName not equals to DEFAULT_EVENT_NAME
        defaultSchoolEventShouldNotBeFound("eventName.notEquals=" + DEFAULT_EVENT_NAME);

        // Get all the schoolEventList where eventName not equals to UPDATED_EVENT_NAME
        defaultSchoolEventShouldBeFound("eventName.notEquals=" + UPDATED_EVENT_NAME);
    }

    @Test
    @Transactional
    void getAllSchoolEventsByEventNameIsInShouldWork() throws Exception {
        // Initialize the database
        schoolEventRepository.saveAndFlush(schoolEvent);

        // Get all the schoolEventList where eventName in DEFAULT_EVENT_NAME or UPDATED_EVENT_NAME
        defaultSchoolEventShouldBeFound("eventName.in=" + DEFAULT_EVENT_NAME + "," + UPDATED_EVENT_NAME);

        // Get all the schoolEventList where eventName equals to UPDATED_EVENT_NAME
        defaultSchoolEventShouldNotBeFound("eventName.in=" + UPDATED_EVENT_NAME);
    }

    @Test
    @Transactional
    void getAllSchoolEventsByEventNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolEventRepository.saveAndFlush(schoolEvent);

        // Get all the schoolEventList where eventName is not null
        defaultSchoolEventShouldBeFound("eventName.specified=true");

        // Get all the schoolEventList where eventName is null
        defaultSchoolEventShouldNotBeFound("eventName.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolEventsByEventNameContainsSomething() throws Exception {
        // Initialize the database
        schoolEventRepository.saveAndFlush(schoolEvent);

        // Get all the schoolEventList where eventName contains DEFAULT_EVENT_NAME
        defaultSchoolEventShouldBeFound("eventName.contains=" + DEFAULT_EVENT_NAME);

        // Get all the schoolEventList where eventName contains UPDATED_EVENT_NAME
        defaultSchoolEventShouldNotBeFound("eventName.contains=" + UPDATED_EVENT_NAME);
    }

    @Test
    @Transactional
    void getAllSchoolEventsByEventNameNotContainsSomething() throws Exception {
        // Initialize the database
        schoolEventRepository.saveAndFlush(schoolEvent);

        // Get all the schoolEventList where eventName does not contain DEFAULT_EVENT_NAME
        defaultSchoolEventShouldNotBeFound("eventName.doesNotContain=" + DEFAULT_EVENT_NAME);

        // Get all the schoolEventList where eventName does not contain UPDATED_EVENT_NAME
        defaultSchoolEventShouldBeFound("eventName.doesNotContain=" + UPDATED_EVENT_NAME);
    }

    @Test
    @Transactional
    void getAllSchoolEventsByEventDetailsIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolEventRepository.saveAndFlush(schoolEvent);

        // Get all the schoolEventList where eventDetails equals to DEFAULT_EVENT_DETAILS
        defaultSchoolEventShouldBeFound("eventDetails.equals=" + DEFAULT_EVENT_DETAILS);

        // Get all the schoolEventList where eventDetails equals to UPDATED_EVENT_DETAILS
        defaultSchoolEventShouldNotBeFound("eventDetails.equals=" + UPDATED_EVENT_DETAILS);
    }

    @Test
    @Transactional
    void getAllSchoolEventsByEventDetailsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        schoolEventRepository.saveAndFlush(schoolEvent);

        // Get all the schoolEventList where eventDetails not equals to DEFAULT_EVENT_DETAILS
        defaultSchoolEventShouldNotBeFound("eventDetails.notEquals=" + DEFAULT_EVENT_DETAILS);

        // Get all the schoolEventList where eventDetails not equals to UPDATED_EVENT_DETAILS
        defaultSchoolEventShouldBeFound("eventDetails.notEquals=" + UPDATED_EVENT_DETAILS);
    }

    @Test
    @Transactional
    void getAllSchoolEventsByEventDetailsIsInShouldWork() throws Exception {
        // Initialize the database
        schoolEventRepository.saveAndFlush(schoolEvent);

        // Get all the schoolEventList where eventDetails in DEFAULT_EVENT_DETAILS or UPDATED_EVENT_DETAILS
        defaultSchoolEventShouldBeFound("eventDetails.in=" + DEFAULT_EVENT_DETAILS + "," + UPDATED_EVENT_DETAILS);

        // Get all the schoolEventList where eventDetails equals to UPDATED_EVENT_DETAILS
        defaultSchoolEventShouldNotBeFound("eventDetails.in=" + UPDATED_EVENT_DETAILS);
    }

    @Test
    @Transactional
    void getAllSchoolEventsByEventDetailsIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolEventRepository.saveAndFlush(schoolEvent);

        // Get all the schoolEventList where eventDetails is not null
        defaultSchoolEventShouldBeFound("eventDetails.specified=true");

        // Get all the schoolEventList where eventDetails is null
        defaultSchoolEventShouldNotBeFound("eventDetails.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolEventsByEventDetailsContainsSomething() throws Exception {
        // Initialize the database
        schoolEventRepository.saveAndFlush(schoolEvent);

        // Get all the schoolEventList where eventDetails contains DEFAULT_EVENT_DETAILS
        defaultSchoolEventShouldBeFound("eventDetails.contains=" + DEFAULT_EVENT_DETAILS);

        // Get all the schoolEventList where eventDetails contains UPDATED_EVENT_DETAILS
        defaultSchoolEventShouldNotBeFound("eventDetails.contains=" + UPDATED_EVENT_DETAILS);
    }

    @Test
    @Transactional
    void getAllSchoolEventsByEventDetailsNotContainsSomething() throws Exception {
        // Initialize the database
        schoolEventRepository.saveAndFlush(schoolEvent);

        // Get all the schoolEventList where eventDetails does not contain DEFAULT_EVENT_DETAILS
        defaultSchoolEventShouldNotBeFound("eventDetails.doesNotContain=" + DEFAULT_EVENT_DETAILS);

        // Get all the schoolEventList where eventDetails does not contain UPDATED_EVENT_DETAILS
        defaultSchoolEventShouldBeFound("eventDetails.doesNotContain=" + UPDATED_EVENT_DETAILS);
    }

    @Test
    @Transactional
    void getAllSchoolEventsByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolEventRepository.saveAndFlush(schoolEvent);

        // Get all the schoolEventList where startDate equals to DEFAULT_START_DATE
        defaultSchoolEventShouldBeFound("startDate.equals=" + DEFAULT_START_DATE);

        // Get all the schoolEventList where startDate equals to UPDATED_START_DATE
        defaultSchoolEventShouldNotBeFound("startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolEventsByStartDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        schoolEventRepository.saveAndFlush(schoolEvent);

        // Get all the schoolEventList where startDate not equals to DEFAULT_START_DATE
        defaultSchoolEventShouldNotBeFound("startDate.notEquals=" + DEFAULT_START_DATE);

        // Get all the schoolEventList where startDate not equals to UPDATED_START_DATE
        defaultSchoolEventShouldBeFound("startDate.notEquals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolEventsByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        schoolEventRepository.saveAndFlush(schoolEvent);

        // Get all the schoolEventList where startDate in DEFAULT_START_DATE or UPDATED_START_DATE
        defaultSchoolEventShouldBeFound("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE);

        // Get all the schoolEventList where startDate equals to UPDATED_START_DATE
        defaultSchoolEventShouldNotBeFound("startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolEventsByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolEventRepository.saveAndFlush(schoolEvent);

        // Get all the schoolEventList where startDate is not null
        defaultSchoolEventShouldBeFound("startDate.specified=true");

        // Get all the schoolEventList where startDate is null
        defaultSchoolEventShouldNotBeFound("startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolEventsByStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        schoolEventRepository.saveAndFlush(schoolEvent);

        // Get all the schoolEventList where startDate is greater than or equal to DEFAULT_START_DATE
        defaultSchoolEventShouldBeFound("startDate.greaterThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the schoolEventList where startDate is greater than or equal to UPDATED_START_DATE
        defaultSchoolEventShouldNotBeFound("startDate.greaterThanOrEqual=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolEventsByStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        schoolEventRepository.saveAndFlush(schoolEvent);

        // Get all the schoolEventList where startDate is less than or equal to DEFAULT_START_DATE
        defaultSchoolEventShouldBeFound("startDate.lessThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the schoolEventList where startDate is less than or equal to SMALLER_START_DATE
        defaultSchoolEventShouldNotBeFound("startDate.lessThanOrEqual=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolEventsByStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        schoolEventRepository.saveAndFlush(schoolEvent);

        // Get all the schoolEventList where startDate is less than DEFAULT_START_DATE
        defaultSchoolEventShouldNotBeFound("startDate.lessThan=" + DEFAULT_START_DATE);

        // Get all the schoolEventList where startDate is less than UPDATED_START_DATE
        defaultSchoolEventShouldBeFound("startDate.lessThan=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolEventsByStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        schoolEventRepository.saveAndFlush(schoolEvent);

        // Get all the schoolEventList where startDate is greater than DEFAULT_START_DATE
        defaultSchoolEventShouldNotBeFound("startDate.greaterThan=" + DEFAULT_START_DATE);

        // Get all the schoolEventList where startDate is greater than SMALLER_START_DATE
        defaultSchoolEventShouldBeFound("startDate.greaterThan=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolEventsByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolEventRepository.saveAndFlush(schoolEvent);

        // Get all the schoolEventList where endDate equals to DEFAULT_END_DATE
        defaultSchoolEventShouldBeFound("endDate.equals=" + DEFAULT_END_DATE);

        // Get all the schoolEventList where endDate equals to UPDATED_END_DATE
        defaultSchoolEventShouldNotBeFound("endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolEventsByEndDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        schoolEventRepository.saveAndFlush(schoolEvent);

        // Get all the schoolEventList where endDate not equals to DEFAULT_END_DATE
        defaultSchoolEventShouldNotBeFound("endDate.notEquals=" + DEFAULT_END_DATE);

        // Get all the schoolEventList where endDate not equals to UPDATED_END_DATE
        defaultSchoolEventShouldBeFound("endDate.notEquals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolEventsByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        schoolEventRepository.saveAndFlush(schoolEvent);

        // Get all the schoolEventList where endDate in DEFAULT_END_DATE or UPDATED_END_DATE
        defaultSchoolEventShouldBeFound("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE);

        // Get all the schoolEventList where endDate equals to UPDATED_END_DATE
        defaultSchoolEventShouldNotBeFound("endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolEventsByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolEventRepository.saveAndFlush(schoolEvent);

        // Get all the schoolEventList where endDate is not null
        defaultSchoolEventShouldBeFound("endDate.specified=true");

        // Get all the schoolEventList where endDate is null
        defaultSchoolEventShouldNotBeFound("endDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolEventsByEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        schoolEventRepository.saveAndFlush(schoolEvent);

        // Get all the schoolEventList where endDate is greater than or equal to DEFAULT_END_DATE
        defaultSchoolEventShouldBeFound("endDate.greaterThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the schoolEventList where endDate is greater than or equal to UPDATED_END_DATE
        defaultSchoolEventShouldNotBeFound("endDate.greaterThanOrEqual=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolEventsByEndDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        schoolEventRepository.saveAndFlush(schoolEvent);

        // Get all the schoolEventList where endDate is less than or equal to DEFAULT_END_DATE
        defaultSchoolEventShouldBeFound("endDate.lessThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the schoolEventList where endDate is less than or equal to SMALLER_END_DATE
        defaultSchoolEventShouldNotBeFound("endDate.lessThanOrEqual=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolEventsByEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        schoolEventRepository.saveAndFlush(schoolEvent);

        // Get all the schoolEventList where endDate is less than DEFAULT_END_DATE
        defaultSchoolEventShouldNotBeFound("endDate.lessThan=" + DEFAULT_END_DATE);

        // Get all the schoolEventList where endDate is less than UPDATED_END_DATE
        defaultSchoolEventShouldBeFound("endDate.lessThan=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolEventsByEndDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        schoolEventRepository.saveAndFlush(schoolEvent);

        // Get all the schoolEventList where endDate is greater than DEFAULT_END_DATE
        defaultSchoolEventShouldNotBeFound("endDate.greaterThan=" + DEFAULT_END_DATE);

        // Get all the schoolEventList where endDate is greater than SMALLER_END_DATE
        defaultSchoolEventShouldBeFound("endDate.greaterThan=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolEventsByCreateDateIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolEventRepository.saveAndFlush(schoolEvent);

        // Get all the schoolEventList where createDate equals to DEFAULT_CREATE_DATE
        defaultSchoolEventShouldBeFound("createDate.equals=" + DEFAULT_CREATE_DATE);

        // Get all the schoolEventList where createDate equals to UPDATED_CREATE_DATE
        defaultSchoolEventShouldNotBeFound("createDate.equals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolEventsByCreateDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        schoolEventRepository.saveAndFlush(schoolEvent);

        // Get all the schoolEventList where createDate not equals to DEFAULT_CREATE_DATE
        defaultSchoolEventShouldNotBeFound("createDate.notEquals=" + DEFAULT_CREATE_DATE);

        // Get all the schoolEventList where createDate not equals to UPDATED_CREATE_DATE
        defaultSchoolEventShouldBeFound("createDate.notEquals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolEventsByCreateDateIsInShouldWork() throws Exception {
        // Initialize the database
        schoolEventRepository.saveAndFlush(schoolEvent);

        // Get all the schoolEventList where createDate in DEFAULT_CREATE_DATE or UPDATED_CREATE_DATE
        defaultSchoolEventShouldBeFound("createDate.in=" + DEFAULT_CREATE_DATE + "," + UPDATED_CREATE_DATE);

        // Get all the schoolEventList where createDate equals to UPDATED_CREATE_DATE
        defaultSchoolEventShouldNotBeFound("createDate.in=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolEventsByCreateDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolEventRepository.saveAndFlush(schoolEvent);

        // Get all the schoolEventList where createDate is not null
        defaultSchoolEventShouldBeFound("createDate.specified=true");

        // Get all the schoolEventList where createDate is null
        defaultSchoolEventShouldNotBeFound("createDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolEventsByCreateDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        schoolEventRepository.saveAndFlush(schoolEvent);

        // Get all the schoolEventList where createDate is greater than or equal to DEFAULT_CREATE_DATE
        defaultSchoolEventShouldBeFound("createDate.greaterThanOrEqual=" + DEFAULT_CREATE_DATE);

        // Get all the schoolEventList where createDate is greater than or equal to UPDATED_CREATE_DATE
        defaultSchoolEventShouldNotBeFound("createDate.greaterThanOrEqual=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolEventsByCreateDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        schoolEventRepository.saveAndFlush(schoolEvent);

        // Get all the schoolEventList where createDate is less than or equal to DEFAULT_CREATE_DATE
        defaultSchoolEventShouldBeFound("createDate.lessThanOrEqual=" + DEFAULT_CREATE_DATE);

        // Get all the schoolEventList where createDate is less than or equal to SMALLER_CREATE_DATE
        defaultSchoolEventShouldNotBeFound("createDate.lessThanOrEqual=" + SMALLER_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolEventsByCreateDateIsLessThanSomething() throws Exception {
        // Initialize the database
        schoolEventRepository.saveAndFlush(schoolEvent);

        // Get all the schoolEventList where createDate is less than DEFAULT_CREATE_DATE
        defaultSchoolEventShouldNotBeFound("createDate.lessThan=" + DEFAULT_CREATE_DATE);

        // Get all the schoolEventList where createDate is less than UPDATED_CREATE_DATE
        defaultSchoolEventShouldBeFound("createDate.lessThan=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolEventsByCreateDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        schoolEventRepository.saveAndFlush(schoolEvent);

        // Get all the schoolEventList where createDate is greater than DEFAULT_CREATE_DATE
        defaultSchoolEventShouldNotBeFound("createDate.greaterThan=" + DEFAULT_CREATE_DATE);

        // Get all the schoolEventList where createDate is greater than SMALLER_CREATE_DATE
        defaultSchoolEventShouldBeFound("createDate.greaterThan=" + SMALLER_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolEventsByLastModifiedIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolEventRepository.saveAndFlush(schoolEvent);

        // Get all the schoolEventList where lastModified equals to DEFAULT_LAST_MODIFIED
        defaultSchoolEventShouldBeFound("lastModified.equals=" + DEFAULT_LAST_MODIFIED);

        // Get all the schoolEventList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultSchoolEventShouldNotBeFound("lastModified.equals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSchoolEventsByLastModifiedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        schoolEventRepository.saveAndFlush(schoolEvent);

        // Get all the schoolEventList where lastModified not equals to DEFAULT_LAST_MODIFIED
        defaultSchoolEventShouldNotBeFound("lastModified.notEquals=" + DEFAULT_LAST_MODIFIED);

        // Get all the schoolEventList where lastModified not equals to UPDATED_LAST_MODIFIED
        defaultSchoolEventShouldBeFound("lastModified.notEquals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSchoolEventsByLastModifiedIsInShouldWork() throws Exception {
        // Initialize the database
        schoolEventRepository.saveAndFlush(schoolEvent);

        // Get all the schoolEventList where lastModified in DEFAULT_LAST_MODIFIED or UPDATED_LAST_MODIFIED
        defaultSchoolEventShouldBeFound("lastModified.in=" + DEFAULT_LAST_MODIFIED + "," + UPDATED_LAST_MODIFIED);

        // Get all the schoolEventList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultSchoolEventShouldNotBeFound("lastModified.in=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSchoolEventsByLastModifiedIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolEventRepository.saveAndFlush(schoolEvent);

        // Get all the schoolEventList where lastModified is not null
        defaultSchoolEventShouldBeFound("lastModified.specified=true");

        // Get all the schoolEventList where lastModified is null
        defaultSchoolEventShouldNotBeFound("lastModified.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolEventsByLastModifiedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        schoolEventRepository.saveAndFlush(schoolEvent);

        // Get all the schoolEventList where lastModified is greater than or equal to DEFAULT_LAST_MODIFIED
        defaultSchoolEventShouldBeFound("lastModified.greaterThanOrEqual=" + DEFAULT_LAST_MODIFIED);

        // Get all the schoolEventList where lastModified is greater than or equal to UPDATED_LAST_MODIFIED
        defaultSchoolEventShouldNotBeFound("lastModified.greaterThanOrEqual=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSchoolEventsByLastModifiedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        schoolEventRepository.saveAndFlush(schoolEvent);

        // Get all the schoolEventList where lastModified is less than or equal to DEFAULT_LAST_MODIFIED
        defaultSchoolEventShouldBeFound("lastModified.lessThanOrEqual=" + DEFAULT_LAST_MODIFIED);

        // Get all the schoolEventList where lastModified is less than or equal to SMALLER_LAST_MODIFIED
        defaultSchoolEventShouldNotBeFound("lastModified.lessThanOrEqual=" + SMALLER_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSchoolEventsByLastModifiedIsLessThanSomething() throws Exception {
        // Initialize the database
        schoolEventRepository.saveAndFlush(schoolEvent);

        // Get all the schoolEventList where lastModified is less than DEFAULT_LAST_MODIFIED
        defaultSchoolEventShouldNotBeFound("lastModified.lessThan=" + DEFAULT_LAST_MODIFIED);

        // Get all the schoolEventList where lastModified is less than UPDATED_LAST_MODIFIED
        defaultSchoolEventShouldBeFound("lastModified.lessThan=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSchoolEventsByLastModifiedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        schoolEventRepository.saveAndFlush(schoolEvent);

        // Get all the schoolEventList where lastModified is greater than DEFAULT_LAST_MODIFIED
        defaultSchoolEventShouldNotBeFound("lastModified.greaterThan=" + DEFAULT_LAST_MODIFIED);

        // Get all the schoolEventList where lastModified is greater than SMALLER_LAST_MODIFIED
        defaultSchoolEventShouldBeFound("lastModified.greaterThan=" + SMALLER_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSchoolEventsByCancelDateIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolEventRepository.saveAndFlush(schoolEvent);

        // Get all the schoolEventList where cancelDate equals to DEFAULT_CANCEL_DATE
        defaultSchoolEventShouldBeFound("cancelDate.equals=" + DEFAULT_CANCEL_DATE);

        // Get all the schoolEventList where cancelDate equals to UPDATED_CANCEL_DATE
        defaultSchoolEventShouldNotBeFound("cancelDate.equals=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolEventsByCancelDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        schoolEventRepository.saveAndFlush(schoolEvent);

        // Get all the schoolEventList where cancelDate not equals to DEFAULT_CANCEL_DATE
        defaultSchoolEventShouldNotBeFound("cancelDate.notEquals=" + DEFAULT_CANCEL_DATE);

        // Get all the schoolEventList where cancelDate not equals to UPDATED_CANCEL_DATE
        defaultSchoolEventShouldBeFound("cancelDate.notEquals=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolEventsByCancelDateIsInShouldWork() throws Exception {
        // Initialize the database
        schoolEventRepository.saveAndFlush(schoolEvent);

        // Get all the schoolEventList where cancelDate in DEFAULT_CANCEL_DATE or UPDATED_CANCEL_DATE
        defaultSchoolEventShouldBeFound("cancelDate.in=" + DEFAULT_CANCEL_DATE + "," + UPDATED_CANCEL_DATE);

        // Get all the schoolEventList where cancelDate equals to UPDATED_CANCEL_DATE
        defaultSchoolEventShouldNotBeFound("cancelDate.in=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolEventsByCancelDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolEventRepository.saveAndFlush(schoolEvent);

        // Get all the schoolEventList where cancelDate is not null
        defaultSchoolEventShouldBeFound("cancelDate.specified=true");

        // Get all the schoolEventList where cancelDate is null
        defaultSchoolEventShouldNotBeFound("cancelDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolEventsByCancelDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        schoolEventRepository.saveAndFlush(schoolEvent);

        // Get all the schoolEventList where cancelDate is greater than or equal to DEFAULT_CANCEL_DATE
        defaultSchoolEventShouldBeFound("cancelDate.greaterThanOrEqual=" + DEFAULT_CANCEL_DATE);

        // Get all the schoolEventList where cancelDate is greater than or equal to UPDATED_CANCEL_DATE
        defaultSchoolEventShouldNotBeFound("cancelDate.greaterThanOrEqual=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolEventsByCancelDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        schoolEventRepository.saveAndFlush(schoolEvent);

        // Get all the schoolEventList where cancelDate is less than or equal to DEFAULT_CANCEL_DATE
        defaultSchoolEventShouldBeFound("cancelDate.lessThanOrEqual=" + DEFAULT_CANCEL_DATE);

        // Get all the schoolEventList where cancelDate is less than or equal to SMALLER_CANCEL_DATE
        defaultSchoolEventShouldNotBeFound("cancelDate.lessThanOrEqual=" + SMALLER_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolEventsByCancelDateIsLessThanSomething() throws Exception {
        // Initialize the database
        schoolEventRepository.saveAndFlush(schoolEvent);

        // Get all the schoolEventList where cancelDate is less than DEFAULT_CANCEL_DATE
        defaultSchoolEventShouldNotBeFound("cancelDate.lessThan=" + DEFAULT_CANCEL_DATE);

        // Get all the schoolEventList where cancelDate is less than UPDATED_CANCEL_DATE
        defaultSchoolEventShouldBeFound("cancelDate.lessThan=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolEventsByCancelDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        schoolEventRepository.saveAndFlush(schoolEvent);

        // Get all the schoolEventList where cancelDate is greater than DEFAULT_CANCEL_DATE
        defaultSchoolEventShouldNotBeFound("cancelDate.greaterThan=" + DEFAULT_CANCEL_DATE);

        // Get all the schoolEventList where cancelDate is greater than SMALLER_CANCEL_DATE
        defaultSchoolEventShouldBeFound("cancelDate.greaterThan=" + SMALLER_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolEventsBySchoolClassIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolEventRepository.saveAndFlush(schoolEvent);
        SchoolClass schoolClass = SchoolClassResourceIT.createEntity(em);
        em.persist(schoolClass);
        em.flush();
        schoolEvent.addSchoolClass(schoolClass);
        schoolEventRepository.saveAndFlush(schoolEvent);
        Long schoolClassId = schoolClass.getId();

        // Get all the schoolEventList where schoolClass equals to schoolClassId
        defaultSchoolEventShouldBeFound("schoolClassId.equals=" + schoolClassId);

        // Get all the schoolEventList where schoolClass equals to (schoolClassId + 1)
        defaultSchoolEventShouldNotBeFound("schoolClassId.equals=" + (schoolClassId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSchoolEventShouldBeFound(String filter) throws Exception {
        restSchoolEventMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(schoolEvent.getId().intValue())))
            .andExpect(jsonPath("$.[*].eventName").value(hasItem(DEFAULT_EVENT_NAME)))
            .andExpect(jsonPath("$.[*].eventDetails").value(hasItem(DEFAULT_EVENT_DETAILS)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].cancelDate").value(hasItem(DEFAULT_CANCEL_DATE.toString())));

        // Check, that the count call also returns 1
        restSchoolEventMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSchoolEventShouldNotBeFound(String filter) throws Exception {
        restSchoolEventMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSchoolEventMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSchoolEvent() throws Exception {
        // Get the schoolEvent
        restSchoolEventMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSchoolEvent() throws Exception {
        // Initialize the database
        schoolEventRepository.saveAndFlush(schoolEvent);

        int databaseSizeBeforeUpdate = schoolEventRepository.findAll().size();

        // Update the schoolEvent
        SchoolEvent updatedSchoolEvent = schoolEventRepository.findById(schoolEvent.getId()).get();
        // Disconnect from session so that the updates on updatedSchoolEvent are not directly saved in db
        em.detach(updatedSchoolEvent);
        updatedSchoolEvent
            .eventName(UPDATED_EVENT_NAME)
            .eventDetails(UPDATED_EVENT_DETAILS)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);
        SchoolEventDTO schoolEventDTO = schoolEventMapper.toDto(updatedSchoolEvent);

        restSchoolEventMockMvc
            .perform(
                put(ENTITY_API_URL_ID, schoolEventDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schoolEventDTO))
            )
            .andExpect(status().isOk());

        // Validate the SchoolEvent in the database
        List<SchoolEvent> schoolEventList = schoolEventRepository.findAll();
        assertThat(schoolEventList).hasSize(databaseSizeBeforeUpdate);
        SchoolEvent testSchoolEvent = schoolEventList.get(schoolEventList.size() - 1);
        assertThat(testSchoolEvent.getEventName()).isEqualTo(UPDATED_EVENT_NAME);
        assertThat(testSchoolEvent.getEventDetails()).isEqualTo(UPDATED_EVENT_DETAILS);
        assertThat(testSchoolEvent.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testSchoolEvent.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testSchoolEvent.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testSchoolEvent.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testSchoolEvent.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void putNonExistingSchoolEvent() throws Exception {
        int databaseSizeBeforeUpdate = schoolEventRepository.findAll().size();
        schoolEvent.setId(count.incrementAndGet());

        // Create the SchoolEvent
        SchoolEventDTO schoolEventDTO = schoolEventMapper.toDto(schoolEvent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSchoolEventMockMvc
            .perform(
                put(ENTITY_API_URL_ID, schoolEventDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schoolEventDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchoolEvent in the database
        List<SchoolEvent> schoolEventList = schoolEventRepository.findAll();
        assertThat(schoolEventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSchoolEvent() throws Exception {
        int databaseSizeBeforeUpdate = schoolEventRepository.findAll().size();
        schoolEvent.setId(count.incrementAndGet());

        // Create the SchoolEvent
        SchoolEventDTO schoolEventDTO = schoolEventMapper.toDto(schoolEvent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchoolEventMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schoolEventDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchoolEvent in the database
        List<SchoolEvent> schoolEventList = schoolEventRepository.findAll();
        assertThat(schoolEventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSchoolEvent() throws Exception {
        int databaseSizeBeforeUpdate = schoolEventRepository.findAll().size();
        schoolEvent.setId(count.incrementAndGet());

        // Create the SchoolEvent
        SchoolEventDTO schoolEventDTO = schoolEventMapper.toDto(schoolEvent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchoolEventMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(schoolEventDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SchoolEvent in the database
        List<SchoolEvent> schoolEventList = schoolEventRepository.findAll();
        assertThat(schoolEventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSchoolEventWithPatch() throws Exception {
        // Initialize the database
        schoolEventRepository.saveAndFlush(schoolEvent);

        int databaseSizeBeforeUpdate = schoolEventRepository.findAll().size();

        // Update the schoolEvent using partial update
        SchoolEvent partialUpdatedSchoolEvent = new SchoolEvent();
        partialUpdatedSchoolEvent.setId(schoolEvent.getId());

        partialUpdatedSchoolEvent.eventDetails(UPDATED_EVENT_DETAILS).createDate(UPDATED_CREATE_DATE);

        restSchoolEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSchoolEvent.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSchoolEvent))
            )
            .andExpect(status().isOk());

        // Validate the SchoolEvent in the database
        List<SchoolEvent> schoolEventList = schoolEventRepository.findAll();
        assertThat(schoolEventList).hasSize(databaseSizeBeforeUpdate);
        SchoolEvent testSchoolEvent = schoolEventList.get(schoolEventList.size() - 1);
        assertThat(testSchoolEvent.getEventName()).isEqualTo(DEFAULT_EVENT_NAME);
        assertThat(testSchoolEvent.getEventDetails()).isEqualTo(UPDATED_EVENT_DETAILS);
        assertThat(testSchoolEvent.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testSchoolEvent.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testSchoolEvent.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testSchoolEvent.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
        assertThat(testSchoolEvent.getCancelDate()).isEqualTo(DEFAULT_CANCEL_DATE);
    }

    @Test
    @Transactional
    void fullUpdateSchoolEventWithPatch() throws Exception {
        // Initialize the database
        schoolEventRepository.saveAndFlush(schoolEvent);

        int databaseSizeBeforeUpdate = schoolEventRepository.findAll().size();

        // Update the schoolEvent using partial update
        SchoolEvent partialUpdatedSchoolEvent = new SchoolEvent();
        partialUpdatedSchoolEvent.setId(schoolEvent.getId());

        partialUpdatedSchoolEvent
            .eventName(UPDATED_EVENT_NAME)
            .eventDetails(UPDATED_EVENT_DETAILS)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);

        restSchoolEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSchoolEvent.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSchoolEvent))
            )
            .andExpect(status().isOk());

        // Validate the SchoolEvent in the database
        List<SchoolEvent> schoolEventList = schoolEventRepository.findAll();
        assertThat(schoolEventList).hasSize(databaseSizeBeforeUpdate);
        SchoolEvent testSchoolEvent = schoolEventList.get(schoolEventList.size() - 1);
        assertThat(testSchoolEvent.getEventName()).isEqualTo(UPDATED_EVENT_NAME);
        assertThat(testSchoolEvent.getEventDetails()).isEqualTo(UPDATED_EVENT_DETAILS);
        assertThat(testSchoolEvent.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testSchoolEvent.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testSchoolEvent.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testSchoolEvent.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testSchoolEvent.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingSchoolEvent() throws Exception {
        int databaseSizeBeforeUpdate = schoolEventRepository.findAll().size();
        schoolEvent.setId(count.incrementAndGet());

        // Create the SchoolEvent
        SchoolEventDTO schoolEventDTO = schoolEventMapper.toDto(schoolEvent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSchoolEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, schoolEventDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(schoolEventDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchoolEvent in the database
        List<SchoolEvent> schoolEventList = schoolEventRepository.findAll();
        assertThat(schoolEventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSchoolEvent() throws Exception {
        int databaseSizeBeforeUpdate = schoolEventRepository.findAll().size();
        schoolEvent.setId(count.incrementAndGet());

        // Create the SchoolEvent
        SchoolEventDTO schoolEventDTO = schoolEventMapper.toDto(schoolEvent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchoolEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(schoolEventDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchoolEvent in the database
        List<SchoolEvent> schoolEventList = schoolEventRepository.findAll();
        assertThat(schoolEventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSchoolEvent() throws Exception {
        int databaseSizeBeforeUpdate = schoolEventRepository.findAll().size();
        schoolEvent.setId(count.incrementAndGet());

        // Create the SchoolEvent
        SchoolEventDTO schoolEventDTO = schoolEventMapper.toDto(schoolEvent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchoolEventMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(schoolEventDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SchoolEvent in the database
        List<SchoolEvent> schoolEventList = schoolEventRepository.findAll();
        assertThat(schoolEventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSchoolEvent() throws Exception {
        // Initialize the database
        schoolEventRepository.saveAndFlush(schoolEvent);

        int databaseSizeBeforeDelete = schoolEventRepository.findAll().size();

        // Delete the schoolEvent
        restSchoolEventMockMvc
            .perform(delete(ENTITY_API_URL_ID, schoolEvent.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SchoolEvent> schoolEventList = schoolEventRepository.findAll();
        assertThat(schoolEventList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
