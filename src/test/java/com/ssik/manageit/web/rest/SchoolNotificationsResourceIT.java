package com.ssik.manageit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ssik.manageit.IntegrationTest;
import com.ssik.manageit.domain.SchoolClass;
import com.ssik.manageit.domain.SchoolNotifications;
import com.ssik.manageit.repository.SchoolNotificationsRepository;
import com.ssik.manageit.service.SchoolNotificationsService;
import com.ssik.manageit.service.criteria.SchoolNotificationsCriteria;
import com.ssik.manageit.service.dto.SchoolNotificationsDTO;
import com.ssik.manageit.service.mapper.SchoolNotificationsMapper;
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
 * Integration tests for the {@link SchoolNotificationsResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SchoolNotificationsResourceIT {

    private static final String DEFAULT_NOTIFICATION_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_NOTIFICATION_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_NOTIFICATION_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_NOTIFICATION_DETAILS = "BBBBBBBBBB";

    private static final byte[] DEFAULT_NOTIFICATION_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_NOTIFICATION_FILE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_NOTIFICATION_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_NOTIFICATION_FILE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_NOTIFICATION_FILE_LINK = "AAAAAAAAAA";
    private static final String UPDATED_NOTIFICATION_FILE_LINK = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_SHOW_TILL_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_SHOW_TILL_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_SHOW_TILL_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_CREATE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATE_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CREATE_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_LAST_MODIFIED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_MODIFIED = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_LAST_MODIFIED = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_CANCEL_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CANCEL_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CANCEL_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/school-notifications";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SchoolNotificationsRepository schoolNotificationsRepository;

    @Mock
    private SchoolNotificationsRepository schoolNotificationsRepositoryMock;

    @Autowired
    private SchoolNotificationsMapper schoolNotificationsMapper;

    @Mock
    private SchoolNotificationsService schoolNotificationsServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSchoolNotificationsMockMvc;

    private SchoolNotifications schoolNotifications;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SchoolNotifications createEntity(EntityManager em) {
        SchoolNotifications schoolNotifications = new SchoolNotifications()
            .notificationTitle(DEFAULT_NOTIFICATION_TITLE)
            .notificationDetails(DEFAULT_NOTIFICATION_DETAILS)
            .notificationFile(DEFAULT_NOTIFICATION_FILE)
            .notificationFileContentType(DEFAULT_NOTIFICATION_FILE_CONTENT_TYPE)
            .notificationFileLink(DEFAULT_NOTIFICATION_FILE_LINK)
            .showTillDate(DEFAULT_SHOW_TILL_DATE)
            .createDate(DEFAULT_CREATE_DATE)
            .lastModified(DEFAULT_LAST_MODIFIED)
            .cancelDate(DEFAULT_CANCEL_DATE);
        return schoolNotifications;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SchoolNotifications createUpdatedEntity(EntityManager em) {
        SchoolNotifications schoolNotifications = new SchoolNotifications()
            .notificationTitle(UPDATED_NOTIFICATION_TITLE)
            .notificationDetails(UPDATED_NOTIFICATION_DETAILS)
            .notificationFile(UPDATED_NOTIFICATION_FILE)
            .notificationFileContentType(UPDATED_NOTIFICATION_FILE_CONTENT_TYPE)
            .notificationFileLink(UPDATED_NOTIFICATION_FILE_LINK)
            .showTillDate(UPDATED_SHOW_TILL_DATE)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);
        return schoolNotifications;
    }

    @BeforeEach
    public void initTest() {
        schoolNotifications = createEntity(em);
    }

    @Test
    @Transactional
    void createSchoolNotifications() throws Exception {
        int databaseSizeBeforeCreate = schoolNotificationsRepository.findAll().size();
        // Create the SchoolNotifications
        SchoolNotificationsDTO schoolNotificationsDTO = schoolNotificationsMapper.toDto(schoolNotifications);
        restSchoolNotificationsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schoolNotificationsDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SchoolNotifications in the database
        List<SchoolNotifications> schoolNotificationsList = schoolNotificationsRepository.findAll();
        assertThat(schoolNotificationsList).hasSize(databaseSizeBeforeCreate + 1);
        SchoolNotifications testSchoolNotifications = schoolNotificationsList.get(schoolNotificationsList.size() - 1);
        assertThat(testSchoolNotifications.getNotificationTitle()).isEqualTo(DEFAULT_NOTIFICATION_TITLE);
        assertThat(testSchoolNotifications.getNotificationDetails()).isEqualTo(DEFAULT_NOTIFICATION_DETAILS);
        assertThat(testSchoolNotifications.getNotificationFile()).isEqualTo(DEFAULT_NOTIFICATION_FILE);
        assertThat(testSchoolNotifications.getNotificationFileContentType()).isEqualTo(DEFAULT_NOTIFICATION_FILE_CONTENT_TYPE);
        assertThat(testSchoolNotifications.getNotificationFileLink()).isEqualTo(DEFAULT_NOTIFICATION_FILE_LINK);
        assertThat(testSchoolNotifications.getShowTillDate()).isEqualTo(DEFAULT_SHOW_TILL_DATE);
        assertThat(testSchoolNotifications.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testSchoolNotifications.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
        assertThat(testSchoolNotifications.getCancelDate()).isEqualTo(DEFAULT_CANCEL_DATE);
    }

    @Test
    @Transactional
    void createSchoolNotificationsWithExistingId() throws Exception {
        // Create the SchoolNotifications with an existing ID
        schoolNotifications.setId(1L);
        SchoolNotificationsDTO schoolNotificationsDTO = schoolNotificationsMapper.toDto(schoolNotifications);

        int databaseSizeBeforeCreate = schoolNotificationsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSchoolNotificationsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schoolNotificationsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchoolNotifications in the database
        List<SchoolNotifications> schoolNotificationsList = schoolNotificationsRepository.findAll();
        assertThat(schoolNotificationsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNotificationTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = schoolNotificationsRepository.findAll().size();
        // set the field null
        schoolNotifications.setNotificationTitle(null);

        // Create the SchoolNotifications, which fails.
        SchoolNotificationsDTO schoolNotificationsDTO = schoolNotificationsMapper.toDto(schoolNotifications);

        restSchoolNotificationsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schoolNotificationsDTO))
            )
            .andExpect(status().isBadRequest());

        List<SchoolNotifications> schoolNotificationsList = schoolNotificationsRepository.findAll();
        assertThat(schoolNotificationsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNotificationDetailsIsRequired() throws Exception {
        int databaseSizeBeforeTest = schoolNotificationsRepository.findAll().size();
        // set the field null
        schoolNotifications.setNotificationDetails(null);

        // Create the SchoolNotifications, which fails.
        SchoolNotificationsDTO schoolNotificationsDTO = schoolNotificationsMapper.toDto(schoolNotifications);

        restSchoolNotificationsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schoolNotificationsDTO))
            )
            .andExpect(status().isBadRequest());

        List<SchoolNotifications> schoolNotificationsList = schoolNotificationsRepository.findAll();
        assertThat(schoolNotificationsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSchoolNotifications() throws Exception {
        // Initialize the database
        schoolNotificationsRepository.saveAndFlush(schoolNotifications);

        // Get all the schoolNotificationsList
        restSchoolNotificationsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(schoolNotifications.getId().intValue())))
            .andExpect(jsonPath("$.[*].notificationTitle").value(hasItem(DEFAULT_NOTIFICATION_TITLE)))
            .andExpect(jsonPath("$.[*].notificationDetails").value(hasItem(DEFAULT_NOTIFICATION_DETAILS)))
            .andExpect(jsonPath("$.[*].notificationFileContentType").value(hasItem(DEFAULT_NOTIFICATION_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].notificationFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_NOTIFICATION_FILE))))
            .andExpect(jsonPath("$.[*].notificationFileLink").value(hasItem(DEFAULT_NOTIFICATION_FILE_LINK)))
            .andExpect(jsonPath("$.[*].showTillDate").value(hasItem(DEFAULT_SHOW_TILL_DATE.toString())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].cancelDate").value(hasItem(DEFAULT_CANCEL_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSchoolNotificationsWithEagerRelationshipsIsEnabled() throws Exception {
        when(schoolNotificationsServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSchoolNotificationsMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(schoolNotificationsServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSchoolNotificationsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(schoolNotificationsServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSchoolNotificationsMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(schoolNotificationsServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getSchoolNotifications() throws Exception {
        // Initialize the database
        schoolNotificationsRepository.saveAndFlush(schoolNotifications);

        // Get the schoolNotifications
        restSchoolNotificationsMockMvc
            .perform(get(ENTITY_API_URL_ID, schoolNotifications.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(schoolNotifications.getId().intValue()))
            .andExpect(jsonPath("$.notificationTitle").value(DEFAULT_NOTIFICATION_TITLE))
            .andExpect(jsonPath("$.notificationDetails").value(DEFAULT_NOTIFICATION_DETAILS))
            .andExpect(jsonPath("$.notificationFileContentType").value(DEFAULT_NOTIFICATION_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.notificationFile").value(Base64Utils.encodeToString(DEFAULT_NOTIFICATION_FILE)))
            .andExpect(jsonPath("$.notificationFileLink").value(DEFAULT_NOTIFICATION_FILE_LINK))
            .andExpect(jsonPath("$.showTillDate").value(DEFAULT_SHOW_TILL_DATE.toString()))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.lastModified").value(DEFAULT_LAST_MODIFIED.toString()))
            .andExpect(jsonPath("$.cancelDate").value(DEFAULT_CANCEL_DATE.toString()));
    }

    @Test
    @Transactional
    void getSchoolNotificationsByIdFiltering() throws Exception {
        // Initialize the database
        schoolNotificationsRepository.saveAndFlush(schoolNotifications);

        Long id = schoolNotifications.getId();

        defaultSchoolNotificationsShouldBeFound("id.equals=" + id);
        defaultSchoolNotificationsShouldNotBeFound("id.notEquals=" + id);

        defaultSchoolNotificationsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSchoolNotificationsShouldNotBeFound("id.greaterThan=" + id);

        defaultSchoolNotificationsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSchoolNotificationsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSchoolNotificationsByNotificationTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolNotificationsRepository.saveAndFlush(schoolNotifications);

        // Get all the schoolNotificationsList where notificationTitle equals to DEFAULT_NOTIFICATION_TITLE
        defaultSchoolNotificationsShouldBeFound("notificationTitle.equals=" + DEFAULT_NOTIFICATION_TITLE);

        // Get all the schoolNotificationsList where notificationTitle equals to UPDATED_NOTIFICATION_TITLE
        defaultSchoolNotificationsShouldNotBeFound("notificationTitle.equals=" + UPDATED_NOTIFICATION_TITLE);
    }

    @Test
    @Transactional
    void getAllSchoolNotificationsByNotificationTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        schoolNotificationsRepository.saveAndFlush(schoolNotifications);

        // Get all the schoolNotificationsList where notificationTitle not equals to DEFAULT_NOTIFICATION_TITLE
        defaultSchoolNotificationsShouldNotBeFound("notificationTitle.notEquals=" + DEFAULT_NOTIFICATION_TITLE);

        // Get all the schoolNotificationsList where notificationTitle not equals to UPDATED_NOTIFICATION_TITLE
        defaultSchoolNotificationsShouldBeFound("notificationTitle.notEquals=" + UPDATED_NOTIFICATION_TITLE);
    }

    @Test
    @Transactional
    void getAllSchoolNotificationsByNotificationTitleIsInShouldWork() throws Exception {
        // Initialize the database
        schoolNotificationsRepository.saveAndFlush(schoolNotifications);

        // Get all the schoolNotificationsList where notificationTitle in DEFAULT_NOTIFICATION_TITLE or UPDATED_NOTIFICATION_TITLE
        defaultSchoolNotificationsShouldBeFound("notificationTitle.in=" + DEFAULT_NOTIFICATION_TITLE + "," + UPDATED_NOTIFICATION_TITLE);

        // Get all the schoolNotificationsList where notificationTitle equals to UPDATED_NOTIFICATION_TITLE
        defaultSchoolNotificationsShouldNotBeFound("notificationTitle.in=" + UPDATED_NOTIFICATION_TITLE);
    }

    @Test
    @Transactional
    void getAllSchoolNotificationsByNotificationTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolNotificationsRepository.saveAndFlush(schoolNotifications);

        // Get all the schoolNotificationsList where notificationTitle is not null
        defaultSchoolNotificationsShouldBeFound("notificationTitle.specified=true");

        // Get all the schoolNotificationsList where notificationTitle is null
        defaultSchoolNotificationsShouldNotBeFound("notificationTitle.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolNotificationsByNotificationTitleContainsSomething() throws Exception {
        // Initialize the database
        schoolNotificationsRepository.saveAndFlush(schoolNotifications);

        // Get all the schoolNotificationsList where notificationTitle contains DEFAULT_NOTIFICATION_TITLE
        defaultSchoolNotificationsShouldBeFound("notificationTitle.contains=" + DEFAULT_NOTIFICATION_TITLE);

        // Get all the schoolNotificationsList where notificationTitle contains UPDATED_NOTIFICATION_TITLE
        defaultSchoolNotificationsShouldNotBeFound("notificationTitle.contains=" + UPDATED_NOTIFICATION_TITLE);
    }

    @Test
    @Transactional
    void getAllSchoolNotificationsByNotificationTitleNotContainsSomething() throws Exception {
        // Initialize the database
        schoolNotificationsRepository.saveAndFlush(schoolNotifications);

        // Get all the schoolNotificationsList where notificationTitle does not contain DEFAULT_NOTIFICATION_TITLE
        defaultSchoolNotificationsShouldNotBeFound("notificationTitle.doesNotContain=" + DEFAULT_NOTIFICATION_TITLE);

        // Get all the schoolNotificationsList where notificationTitle does not contain UPDATED_NOTIFICATION_TITLE
        defaultSchoolNotificationsShouldBeFound("notificationTitle.doesNotContain=" + UPDATED_NOTIFICATION_TITLE);
    }

    @Test
    @Transactional
    void getAllSchoolNotificationsByNotificationDetailsIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolNotificationsRepository.saveAndFlush(schoolNotifications);

        // Get all the schoolNotificationsList where notificationDetails equals to DEFAULT_NOTIFICATION_DETAILS
        defaultSchoolNotificationsShouldBeFound("notificationDetails.equals=" + DEFAULT_NOTIFICATION_DETAILS);

        // Get all the schoolNotificationsList where notificationDetails equals to UPDATED_NOTIFICATION_DETAILS
        defaultSchoolNotificationsShouldNotBeFound("notificationDetails.equals=" + UPDATED_NOTIFICATION_DETAILS);
    }

    @Test
    @Transactional
    void getAllSchoolNotificationsByNotificationDetailsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        schoolNotificationsRepository.saveAndFlush(schoolNotifications);

        // Get all the schoolNotificationsList where notificationDetails not equals to DEFAULT_NOTIFICATION_DETAILS
        defaultSchoolNotificationsShouldNotBeFound("notificationDetails.notEquals=" + DEFAULT_NOTIFICATION_DETAILS);

        // Get all the schoolNotificationsList where notificationDetails not equals to UPDATED_NOTIFICATION_DETAILS
        defaultSchoolNotificationsShouldBeFound("notificationDetails.notEquals=" + UPDATED_NOTIFICATION_DETAILS);
    }

    @Test
    @Transactional
    void getAllSchoolNotificationsByNotificationDetailsIsInShouldWork() throws Exception {
        // Initialize the database
        schoolNotificationsRepository.saveAndFlush(schoolNotifications);

        // Get all the schoolNotificationsList where notificationDetails in DEFAULT_NOTIFICATION_DETAILS or UPDATED_NOTIFICATION_DETAILS
        defaultSchoolNotificationsShouldBeFound(
            "notificationDetails.in=" + DEFAULT_NOTIFICATION_DETAILS + "," + UPDATED_NOTIFICATION_DETAILS
        );

        // Get all the schoolNotificationsList where notificationDetails equals to UPDATED_NOTIFICATION_DETAILS
        defaultSchoolNotificationsShouldNotBeFound("notificationDetails.in=" + UPDATED_NOTIFICATION_DETAILS);
    }

    @Test
    @Transactional
    void getAllSchoolNotificationsByNotificationDetailsIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolNotificationsRepository.saveAndFlush(schoolNotifications);

        // Get all the schoolNotificationsList where notificationDetails is not null
        defaultSchoolNotificationsShouldBeFound("notificationDetails.specified=true");

        // Get all the schoolNotificationsList where notificationDetails is null
        defaultSchoolNotificationsShouldNotBeFound("notificationDetails.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolNotificationsByNotificationDetailsContainsSomething() throws Exception {
        // Initialize the database
        schoolNotificationsRepository.saveAndFlush(schoolNotifications);

        // Get all the schoolNotificationsList where notificationDetails contains DEFAULT_NOTIFICATION_DETAILS
        defaultSchoolNotificationsShouldBeFound("notificationDetails.contains=" + DEFAULT_NOTIFICATION_DETAILS);

        // Get all the schoolNotificationsList where notificationDetails contains UPDATED_NOTIFICATION_DETAILS
        defaultSchoolNotificationsShouldNotBeFound("notificationDetails.contains=" + UPDATED_NOTIFICATION_DETAILS);
    }

    @Test
    @Transactional
    void getAllSchoolNotificationsByNotificationDetailsNotContainsSomething() throws Exception {
        // Initialize the database
        schoolNotificationsRepository.saveAndFlush(schoolNotifications);

        // Get all the schoolNotificationsList where notificationDetails does not contain DEFAULT_NOTIFICATION_DETAILS
        defaultSchoolNotificationsShouldNotBeFound("notificationDetails.doesNotContain=" + DEFAULT_NOTIFICATION_DETAILS);

        // Get all the schoolNotificationsList where notificationDetails does not contain UPDATED_NOTIFICATION_DETAILS
        defaultSchoolNotificationsShouldBeFound("notificationDetails.doesNotContain=" + UPDATED_NOTIFICATION_DETAILS);
    }

    @Test
    @Transactional
    void getAllSchoolNotificationsByNotificationFileLinkIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolNotificationsRepository.saveAndFlush(schoolNotifications);

        // Get all the schoolNotificationsList where notificationFileLink equals to DEFAULT_NOTIFICATION_FILE_LINK
        defaultSchoolNotificationsShouldBeFound("notificationFileLink.equals=" + DEFAULT_NOTIFICATION_FILE_LINK);

        // Get all the schoolNotificationsList where notificationFileLink equals to UPDATED_NOTIFICATION_FILE_LINK
        defaultSchoolNotificationsShouldNotBeFound("notificationFileLink.equals=" + UPDATED_NOTIFICATION_FILE_LINK);
    }

    @Test
    @Transactional
    void getAllSchoolNotificationsByNotificationFileLinkIsNotEqualToSomething() throws Exception {
        // Initialize the database
        schoolNotificationsRepository.saveAndFlush(schoolNotifications);

        // Get all the schoolNotificationsList where notificationFileLink not equals to DEFAULT_NOTIFICATION_FILE_LINK
        defaultSchoolNotificationsShouldNotBeFound("notificationFileLink.notEquals=" + DEFAULT_NOTIFICATION_FILE_LINK);

        // Get all the schoolNotificationsList where notificationFileLink not equals to UPDATED_NOTIFICATION_FILE_LINK
        defaultSchoolNotificationsShouldBeFound("notificationFileLink.notEquals=" + UPDATED_NOTIFICATION_FILE_LINK);
    }

    @Test
    @Transactional
    void getAllSchoolNotificationsByNotificationFileLinkIsInShouldWork() throws Exception {
        // Initialize the database
        schoolNotificationsRepository.saveAndFlush(schoolNotifications);

        // Get all the schoolNotificationsList where notificationFileLink in DEFAULT_NOTIFICATION_FILE_LINK or UPDATED_NOTIFICATION_FILE_LINK
        defaultSchoolNotificationsShouldBeFound(
            "notificationFileLink.in=" + DEFAULT_NOTIFICATION_FILE_LINK + "," + UPDATED_NOTIFICATION_FILE_LINK
        );

        // Get all the schoolNotificationsList where notificationFileLink equals to UPDATED_NOTIFICATION_FILE_LINK
        defaultSchoolNotificationsShouldNotBeFound("notificationFileLink.in=" + UPDATED_NOTIFICATION_FILE_LINK);
    }

    @Test
    @Transactional
    void getAllSchoolNotificationsByNotificationFileLinkIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolNotificationsRepository.saveAndFlush(schoolNotifications);

        // Get all the schoolNotificationsList where notificationFileLink is not null
        defaultSchoolNotificationsShouldBeFound("notificationFileLink.specified=true");

        // Get all the schoolNotificationsList where notificationFileLink is null
        defaultSchoolNotificationsShouldNotBeFound("notificationFileLink.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolNotificationsByNotificationFileLinkContainsSomething() throws Exception {
        // Initialize the database
        schoolNotificationsRepository.saveAndFlush(schoolNotifications);

        // Get all the schoolNotificationsList where notificationFileLink contains DEFAULT_NOTIFICATION_FILE_LINK
        defaultSchoolNotificationsShouldBeFound("notificationFileLink.contains=" + DEFAULT_NOTIFICATION_FILE_LINK);

        // Get all the schoolNotificationsList where notificationFileLink contains UPDATED_NOTIFICATION_FILE_LINK
        defaultSchoolNotificationsShouldNotBeFound("notificationFileLink.contains=" + UPDATED_NOTIFICATION_FILE_LINK);
    }

    @Test
    @Transactional
    void getAllSchoolNotificationsByNotificationFileLinkNotContainsSomething() throws Exception {
        // Initialize the database
        schoolNotificationsRepository.saveAndFlush(schoolNotifications);

        // Get all the schoolNotificationsList where notificationFileLink does not contain DEFAULT_NOTIFICATION_FILE_LINK
        defaultSchoolNotificationsShouldNotBeFound("notificationFileLink.doesNotContain=" + DEFAULT_NOTIFICATION_FILE_LINK);

        // Get all the schoolNotificationsList where notificationFileLink does not contain UPDATED_NOTIFICATION_FILE_LINK
        defaultSchoolNotificationsShouldBeFound("notificationFileLink.doesNotContain=" + UPDATED_NOTIFICATION_FILE_LINK);
    }

    @Test
    @Transactional
    void getAllSchoolNotificationsByShowTillDateIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolNotificationsRepository.saveAndFlush(schoolNotifications);

        // Get all the schoolNotificationsList where showTillDate equals to DEFAULT_SHOW_TILL_DATE
        defaultSchoolNotificationsShouldBeFound("showTillDate.equals=" + DEFAULT_SHOW_TILL_DATE);

        // Get all the schoolNotificationsList where showTillDate equals to UPDATED_SHOW_TILL_DATE
        defaultSchoolNotificationsShouldNotBeFound("showTillDate.equals=" + UPDATED_SHOW_TILL_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolNotificationsByShowTillDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        schoolNotificationsRepository.saveAndFlush(schoolNotifications);

        // Get all the schoolNotificationsList where showTillDate not equals to DEFAULT_SHOW_TILL_DATE
        defaultSchoolNotificationsShouldNotBeFound("showTillDate.notEquals=" + DEFAULT_SHOW_TILL_DATE);

        // Get all the schoolNotificationsList where showTillDate not equals to UPDATED_SHOW_TILL_DATE
        defaultSchoolNotificationsShouldBeFound("showTillDate.notEquals=" + UPDATED_SHOW_TILL_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolNotificationsByShowTillDateIsInShouldWork() throws Exception {
        // Initialize the database
        schoolNotificationsRepository.saveAndFlush(schoolNotifications);

        // Get all the schoolNotificationsList where showTillDate in DEFAULT_SHOW_TILL_DATE or UPDATED_SHOW_TILL_DATE
        defaultSchoolNotificationsShouldBeFound("showTillDate.in=" + DEFAULT_SHOW_TILL_DATE + "," + UPDATED_SHOW_TILL_DATE);

        // Get all the schoolNotificationsList where showTillDate equals to UPDATED_SHOW_TILL_DATE
        defaultSchoolNotificationsShouldNotBeFound("showTillDate.in=" + UPDATED_SHOW_TILL_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolNotificationsByShowTillDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolNotificationsRepository.saveAndFlush(schoolNotifications);

        // Get all the schoolNotificationsList where showTillDate is not null
        defaultSchoolNotificationsShouldBeFound("showTillDate.specified=true");

        // Get all the schoolNotificationsList where showTillDate is null
        defaultSchoolNotificationsShouldNotBeFound("showTillDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolNotificationsByShowTillDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        schoolNotificationsRepository.saveAndFlush(schoolNotifications);

        // Get all the schoolNotificationsList where showTillDate is greater than or equal to DEFAULT_SHOW_TILL_DATE
        defaultSchoolNotificationsShouldBeFound("showTillDate.greaterThanOrEqual=" + DEFAULT_SHOW_TILL_DATE);

        // Get all the schoolNotificationsList where showTillDate is greater than or equal to UPDATED_SHOW_TILL_DATE
        defaultSchoolNotificationsShouldNotBeFound("showTillDate.greaterThanOrEqual=" + UPDATED_SHOW_TILL_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolNotificationsByShowTillDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        schoolNotificationsRepository.saveAndFlush(schoolNotifications);

        // Get all the schoolNotificationsList where showTillDate is less than or equal to DEFAULT_SHOW_TILL_DATE
        defaultSchoolNotificationsShouldBeFound("showTillDate.lessThanOrEqual=" + DEFAULT_SHOW_TILL_DATE);

        // Get all the schoolNotificationsList where showTillDate is less than or equal to SMALLER_SHOW_TILL_DATE
        defaultSchoolNotificationsShouldNotBeFound("showTillDate.lessThanOrEqual=" + SMALLER_SHOW_TILL_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolNotificationsByShowTillDateIsLessThanSomething() throws Exception {
        // Initialize the database
        schoolNotificationsRepository.saveAndFlush(schoolNotifications);

        // Get all the schoolNotificationsList where showTillDate is less than DEFAULT_SHOW_TILL_DATE
        defaultSchoolNotificationsShouldNotBeFound("showTillDate.lessThan=" + DEFAULT_SHOW_TILL_DATE);

        // Get all the schoolNotificationsList where showTillDate is less than UPDATED_SHOW_TILL_DATE
        defaultSchoolNotificationsShouldBeFound("showTillDate.lessThan=" + UPDATED_SHOW_TILL_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolNotificationsByShowTillDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        schoolNotificationsRepository.saveAndFlush(schoolNotifications);

        // Get all the schoolNotificationsList where showTillDate is greater than DEFAULT_SHOW_TILL_DATE
        defaultSchoolNotificationsShouldNotBeFound("showTillDate.greaterThan=" + DEFAULT_SHOW_TILL_DATE);

        // Get all the schoolNotificationsList where showTillDate is greater than SMALLER_SHOW_TILL_DATE
        defaultSchoolNotificationsShouldBeFound("showTillDate.greaterThan=" + SMALLER_SHOW_TILL_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolNotificationsByCreateDateIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolNotificationsRepository.saveAndFlush(schoolNotifications);

        // Get all the schoolNotificationsList where createDate equals to DEFAULT_CREATE_DATE
        defaultSchoolNotificationsShouldBeFound("createDate.equals=" + DEFAULT_CREATE_DATE);

        // Get all the schoolNotificationsList where createDate equals to UPDATED_CREATE_DATE
        defaultSchoolNotificationsShouldNotBeFound("createDate.equals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolNotificationsByCreateDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        schoolNotificationsRepository.saveAndFlush(schoolNotifications);

        // Get all the schoolNotificationsList where createDate not equals to DEFAULT_CREATE_DATE
        defaultSchoolNotificationsShouldNotBeFound("createDate.notEquals=" + DEFAULT_CREATE_DATE);

        // Get all the schoolNotificationsList where createDate not equals to UPDATED_CREATE_DATE
        defaultSchoolNotificationsShouldBeFound("createDate.notEquals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolNotificationsByCreateDateIsInShouldWork() throws Exception {
        // Initialize the database
        schoolNotificationsRepository.saveAndFlush(schoolNotifications);

        // Get all the schoolNotificationsList where createDate in DEFAULT_CREATE_DATE or UPDATED_CREATE_DATE
        defaultSchoolNotificationsShouldBeFound("createDate.in=" + DEFAULT_CREATE_DATE + "," + UPDATED_CREATE_DATE);

        // Get all the schoolNotificationsList where createDate equals to UPDATED_CREATE_DATE
        defaultSchoolNotificationsShouldNotBeFound("createDate.in=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolNotificationsByCreateDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolNotificationsRepository.saveAndFlush(schoolNotifications);

        // Get all the schoolNotificationsList where createDate is not null
        defaultSchoolNotificationsShouldBeFound("createDate.specified=true");

        // Get all the schoolNotificationsList where createDate is null
        defaultSchoolNotificationsShouldNotBeFound("createDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolNotificationsByCreateDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        schoolNotificationsRepository.saveAndFlush(schoolNotifications);

        // Get all the schoolNotificationsList where createDate is greater than or equal to DEFAULT_CREATE_DATE
        defaultSchoolNotificationsShouldBeFound("createDate.greaterThanOrEqual=" + DEFAULT_CREATE_DATE);

        // Get all the schoolNotificationsList where createDate is greater than or equal to UPDATED_CREATE_DATE
        defaultSchoolNotificationsShouldNotBeFound("createDate.greaterThanOrEqual=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolNotificationsByCreateDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        schoolNotificationsRepository.saveAndFlush(schoolNotifications);

        // Get all the schoolNotificationsList where createDate is less than or equal to DEFAULT_CREATE_DATE
        defaultSchoolNotificationsShouldBeFound("createDate.lessThanOrEqual=" + DEFAULT_CREATE_DATE);

        // Get all the schoolNotificationsList where createDate is less than or equal to SMALLER_CREATE_DATE
        defaultSchoolNotificationsShouldNotBeFound("createDate.lessThanOrEqual=" + SMALLER_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolNotificationsByCreateDateIsLessThanSomething() throws Exception {
        // Initialize the database
        schoolNotificationsRepository.saveAndFlush(schoolNotifications);

        // Get all the schoolNotificationsList where createDate is less than DEFAULT_CREATE_DATE
        defaultSchoolNotificationsShouldNotBeFound("createDate.lessThan=" + DEFAULT_CREATE_DATE);

        // Get all the schoolNotificationsList where createDate is less than UPDATED_CREATE_DATE
        defaultSchoolNotificationsShouldBeFound("createDate.lessThan=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolNotificationsByCreateDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        schoolNotificationsRepository.saveAndFlush(schoolNotifications);

        // Get all the schoolNotificationsList where createDate is greater than DEFAULT_CREATE_DATE
        defaultSchoolNotificationsShouldNotBeFound("createDate.greaterThan=" + DEFAULT_CREATE_DATE);

        // Get all the schoolNotificationsList where createDate is greater than SMALLER_CREATE_DATE
        defaultSchoolNotificationsShouldBeFound("createDate.greaterThan=" + SMALLER_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolNotificationsByLastModifiedIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolNotificationsRepository.saveAndFlush(schoolNotifications);

        // Get all the schoolNotificationsList where lastModified equals to DEFAULT_LAST_MODIFIED
        defaultSchoolNotificationsShouldBeFound("lastModified.equals=" + DEFAULT_LAST_MODIFIED);

        // Get all the schoolNotificationsList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultSchoolNotificationsShouldNotBeFound("lastModified.equals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSchoolNotificationsByLastModifiedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        schoolNotificationsRepository.saveAndFlush(schoolNotifications);

        // Get all the schoolNotificationsList where lastModified not equals to DEFAULT_LAST_MODIFIED
        defaultSchoolNotificationsShouldNotBeFound("lastModified.notEquals=" + DEFAULT_LAST_MODIFIED);

        // Get all the schoolNotificationsList where lastModified not equals to UPDATED_LAST_MODIFIED
        defaultSchoolNotificationsShouldBeFound("lastModified.notEquals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSchoolNotificationsByLastModifiedIsInShouldWork() throws Exception {
        // Initialize the database
        schoolNotificationsRepository.saveAndFlush(schoolNotifications);

        // Get all the schoolNotificationsList where lastModified in DEFAULT_LAST_MODIFIED or UPDATED_LAST_MODIFIED
        defaultSchoolNotificationsShouldBeFound("lastModified.in=" + DEFAULT_LAST_MODIFIED + "," + UPDATED_LAST_MODIFIED);

        // Get all the schoolNotificationsList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultSchoolNotificationsShouldNotBeFound("lastModified.in=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSchoolNotificationsByLastModifiedIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolNotificationsRepository.saveAndFlush(schoolNotifications);

        // Get all the schoolNotificationsList where lastModified is not null
        defaultSchoolNotificationsShouldBeFound("lastModified.specified=true");

        // Get all the schoolNotificationsList where lastModified is null
        defaultSchoolNotificationsShouldNotBeFound("lastModified.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolNotificationsByLastModifiedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        schoolNotificationsRepository.saveAndFlush(schoolNotifications);

        // Get all the schoolNotificationsList where lastModified is greater than or equal to DEFAULT_LAST_MODIFIED
        defaultSchoolNotificationsShouldBeFound("lastModified.greaterThanOrEqual=" + DEFAULT_LAST_MODIFIED);

        // Get all the schoolNotificationsList where lastModified is greater than or equal to UPDATED_LAST_MODIFIED
        defaultSchoolNotificationsShouldNotBeFound("lastModified.greaterThanOrEqual=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSchoolNotificationsByLastModifiedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        schoolNotificationsRepository.saveAndFlush(schoolNotifications);

        // Get all the schoolNotificationsList where lastModified is less than or equal to DEFAULT_LAST_MODIFIED
        defaultSchoolNotificationsShouldBeFound("lastModified.lessThanOrEqual=" + DEFAULT_LAST_MODIFIED);

        // Get all the schoolNotificationsList where lastModified is less than or equal to SMALLER_LAST_MODIFIED
        defaultSchoolNotificationsShouldNotBeFound("lastModified.lessThanOrEqual=" + SMALLER_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSchoolNotificationsByLastModifiedIsLessThanSomething() throws Exception {
        // Initialize the database
        schoolNotificationsRepository.saveAndFlush(schoolNotifications);

        // Get all the schoolNotificationsList where lastModified is less than DEFAULT_LAST_MODIFIED
        defaultSchoolNotificationsShouldNotBeFound("lastModified.lessThan=" + DEFAULT_LAST_MODIFIED);

        // Get all the schoolNotificationsList where lastModified is less than UPDATED_LAST_MODIFIED
        defaultSchoolNotificationsShouldBeFound("lastModified.lessThan=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSchoolNotificationsByLastModifiedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        schoolNotificationsRepository.saveAndFlush(schoolNotifications);

        // Get all the schoolNotificationsList where lastModified is greater than DEFAULT_LAST_MODIFIED
        defaultSchoolNotificationsShouldNotBeFound("lastModified.greaterThan=" + DEFAULT_LAST_MODIFIED);

        // Get all the schoolNotificationsList where lastModified is greater than SMALLER_LAST_MODIFIED
        defaultSchoolNotificationsShouldBeFound("lastModified.greaterThan=" + SMALLER_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSchoolNotificationsByCancelDateIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolNotificationsRepository.saveAndFlush(schoolNotifications);

        // Get all the schoolNotificationsList where cancelDate equals to DEFAULT_CANCEL_DATE
        defaultSchoolNotificationsShouldBeFound("cancelDate.equals=" + DEFAULT_CANCEL_DATE);

        // Get all the schoolNotificationsList where cancelDate equals to UPDATED_CANCEL_DATE
        defaultSchoolNotificationsShouldNotBeFound("cancelDate.equals=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolNotificationsByCancelDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        schoolNotificationsRepository.saveAndFlush(schoolNotifications);

        // Get all the schoolNotificationsList where cancelDate not equals to DEFAULT_CANCEL_DATE
        defaultSchoolNotificationsShouldNotBeFound("cancelDate.notEquals=" + DEFAULT_CANCEL_DATE);

        // Get all the schoolNotificationsList where cancelDate not equals to UPDATED_CANCEL_DATE
        defaultSchoolNotificationsShouldBeFound("cancelDate.notEquals=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolNotificationsByCancelDateIsInShouldWork() throws Exception {
        // Initialize the database
        schoolNotificationsRepository.saveAndFlush(schoolNotifications);

        // Get all the schoolNotificationsList where cancelDate in DEFAULT_CANCEL_DATE or UPDATED_CANCEL_DATE
        defaultSchoolNotificationsShouldBeFound("cancelDate.in=" + DEFAULT_CANCEL_DATE + "," + UPDATED_CANCEL_DATE);

        // Get all the schoolNotificationsList where cancelDate equals to UPDATED_CANCEL_DATE
        defaultSchoolNotificationsShouldNotBeFound("cancelDate.in=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolNotificationsByCancelDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolNotificationsRepository.saveAndFlush(schoolNotifications);

        // Get all the schoolNotificationsList where cancelDate is not null
        defaultSchoolNotificationsShouldBeFound("cancelDate.specified=true");

        // Get all the schoolNotificationsList where cancelDate is null
        defaultSchoolNotificationsShouldNotBeFound("cancelDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolNotificationsByCancelDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        schoolNotificationsRepository.saveAndFlush(schoolNotifications);

        // Get all the schoolNotificationsList where cancelDate is greater than or equal to DEFAULT_CANCEL_DATE
        defaultSchoolNotificationsShouldBeFound("cancelDate.greaterThanOrEqual=" + DEFAULT_CANCEL_DATE);

        // Get all the schoolNotificationsList where cancelDate is greater than or equal to UPDATED_CANCEL_DATE
        defaultSchoolNotificationsShouldNotBeFound("cancelDate.greaterThanOrEqual=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolNotificationsByCancelDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        schoolNotificationsRepository.saveAndFlush(schoolNotifications);

        // Get all the schoolNotificationsList where cancelDate is less than or equal to DEFAULT_CANCEL_DATE
        defaultSchoolNotificationsShouldBeFound("cancelDate.lessThanOrEqual=" + DEFAULT_CANCEL_DATE);

        // Get all the schoolNotificationsList where cancelDate is less than or equal to SMALLER_CANCEL_DATE
        defaultSchoolNotificationsShouldNotBeFound("cancelDate.lessThanOrEqual=" + SMALLER_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolNotificationsByCancelDateIsLessThanSomething() throws Exception {
        // Initialize the database
        schoolNotificationsRepository.saveAndFlush(schoolNotifications);

        // Get all the schoolNotificationsList where cancelDate is less than DEFAULT_CANCEL_DATE
        defaultSchoolNotificationsShouldNotBeFound("cancelDate.lessThan=" + DEFAULT_CANCEL_DATE);

        // Get all the schoolNotificationsList where cancelDate is less than UPDATED_CANCEL_DATE
        defaultSchoolNotificationsShouldBeFound("cancelDate.lessThan=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolNotificationsByCancelDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        schoolNotificationsRepository.saveAndFlush(schoolNotifications);

        // Get all the schoolNotificationsList where cancelDate is greater than DEFAULT_CANCEL_DATE
        defaultSchoolNotificationsShouldNotBeFound("cancelDate.greaterThan=" + DEFAULT_CANCEL_DATE);

        // Get all the schoolNotificationsList where cancelDate is greater than SMALLER_CANCEL_DATE
        defaultSchoolNotificationsShouldBeFound("cancelDate.greaterThan=" + SMALLER_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolNotificationsBySchoolClassIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolNotificationsRepository.saveAndFlush(schoolNotifications);
        SchoolClass schoolClass = SchoolClassResourceIT.createEntity(em);
        em.persist(schoolClass);
        em.flush();
        schoolNotifications.addSchoolClass(schoolClass);
        schoolNotificationsRepository.saveAndFlush(schoolNotifications);
        Long schoolClassId = schoolClass.getId();

        // Get all the schoolNotificationsList where schoolClass equals to schoolClassId
        defaultSchoolNotificationsShouldBeFound("schoolClassId.equals=" + schoolClassId);

        // Get all the schoolNotificationsList where schoolClass equals to (schoolClassId + 1)
        defaultSchoolNotificationsShouldNotBeFound("schoolClassId.equals=" + (schoolClassId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSchoolNotificationsShouldBeFound(String filter) throws Exception {
        restSchoolNotificationsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(schoolNotifications.getId().intValue())))
            .andExpect(jsonPath("$.[*].notificationTitle").value(hasItem(DEFAULT_NOTIFICATION_TITLE)))
            .andExpect(jsonPath("$.[*].notificationDetails").value(hasItem(DEFAULT_NOTIFICATION_DETAILS)))
            .andExpect(jsonPath("$.[*].notificationFileContentType").value(hasItem(DEFAULT_NOTIFICATION_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].notificationFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_NOTIFICATION_FILE))))
            .andExpect(jsonPath("$.[*].notificationFileLink").value(hasItem(DEFAULT_NOTIFICATION_FILE_LINK)))
            .andExpect(jsonPath("$.[*].showTillDate").value(hasItem(DEFAULT_SHOW_TILL_DATE.toString())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].cancelDate").value(hasItem(DEFAULT_CANCEL_DATE.toString())));

        // Check, that the count call also returns 1
        restSchoolNotificationsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSchoolNotificationsShouldNotBeFound(String filter) throws Exception {
        restSchoolNotificationsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSchoolNotificationsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSchoolNotifications() throws Exception {
        // Get the schoolNotifications
        restSchoolNotificationsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSchoolNotifications() throws Exception {
        // Initialize the database
        schoolNotificationsRepository.saveAndFlush(schoolNotifications);

        int databaseSizeBeforeUpdate = schoolNotificationsRepository.findAll().size();

        // Update the schoolNotifications
        SchoolNotifications updatedSchoolNotifications = schoolNotificationsRepository.findById(schoolNotifications.getId()).get();
        // Disconnect from session so that the updates on updatedSchoolNotifications are not directly saved in db
        em.detach(updatedSchoolNotifications);
        updatedSchoolNotifications
            .notificationTitle(UPDATED_NOTIFICATION_TITLE)
            .notificationDetails(UPDATED_NOTIFICATION_DETAILS)
            .notificationFile(UPDATED_NOTIFICATION_FILE)
            .notificationFileContentType(UPDATED_NOTIFICATION_FILE_CONTENT_TYPE)
            .notificationFileLink(UPDATED_NOTIFICATION_FILE_LINK)
            .showTillDate(UPDATED_SHOW_TILL_DATE)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);
        SchoolNotificationsDTO schoolNotificationsDTO = schoolNotificationsMapper.toDto(updatedSchoolNotifications);

        restSchoolNotificationsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, schoolNotificationsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schoolNotificationsDTO))
            )
            .andExpect(status().isOk());

        // Validate the SchoolNotifications in the database
        List<SchoolNotifications> schoolNotificationsList = schoolNotificationsRepository.findAll();
        assertThat(schoolNotificationsList).hasSize(databaseSizeBeforeUpdate);
        SchoolNotifications testSchoolNotifications = schoolNotificationsList.get(schoolNotificationsList.size() - 1);
        assertThat(testSchoolNotifications.getNotificationTitle()).isEqualTo(UPDATED_NOTIFICATION_TITLE);
        assertThat(testSchoolNotifications.getNotificationDetails()).isEqualTo(UPDATED_NOTIFICATION_DETAILS);
        assertThat(testSchoolNotifications.getNotificationFile()).isEqualTo(UPDATED_NOTIFICATION_FILE);
        assertThat(testSchoolNotifications.getNotificationFileContentType()).isEqualTo(UPDATED_NOTIFICATION_FILE_CONTENT_TYPE);
        assertThat(testSchoolNotifications.getNotificationFileLink()).isEqualTo(UPDATED_NOTIFICATION_FILE_LINK);
        assertThat(testSchoolNotifications.getShowTillDate()).isEqualTo(UPDATED_SHOW_TILL_DATE);
        assertThat(testSchoolNotifications.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testSchoolNotifications.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testSchoolNotifications.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void putNonExistingSchoolNotifications() throws Exception {
        int databaseSizeBeforeUpdate = schoolNotificationsRepository.findAll().size();
        schoolNotifications.setId(count.incrementAndGet());

        // Create the SchoolNotifications
        SchoolNotificationsDTO schoolNotificationsDTO = schoolNotificationsMapper.toDto(schoolNotifications);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSchoolNotificationsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, schoolNotificationsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schoolNotificationsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchoolNotifications in the database
        List<SchoolNotifications> schoolNotificationsList = schoolNotificationsRepository.findAll();
        assertThat(schoolNotificationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSchoolNotifications() throws Exception {
        int databaseSizeBeforeUpdate = schoolNotificationsRepository.findAll().size();
        schoolNotifications.setId(count.incrementAndGet());

        // Create the SchoolNotifications
        SchoolNotificationsDTO schoolNotificationsDTO = schoolNotificationsMapper.toDto(schoolNotifications);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchoolNotificationsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schoolNotificationsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchoolNotifications in the database
        List<SchoolNotifications> schoolNotificationsList = schoolNotificationsRepository.findAll();
        assertThat(schoolNotificationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSchoolNotifications() throws Exception {
        int databaseSizeBeforeUpdate = schoolNotificationsRepository.findAll().size();
        schoolNotifications.setId(count.incrementAndGet());

        // Create the SchoolNotifications
        SchoolNotificationsDTO schoolNotificationsDTO = schoolNotificationsMapper.toDto(schoolNotifications);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchoolNotificationsMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schoolNotificationsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SchoolNotifications in the database
        List<SchoolNotifications> schoolNotificationsList = schoolNotificationsRepository.findAll();
        assertThat(schoolNotificationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSchoolNotificationsWithPatch() throws Exception {
        // Initialize the database
        schoolNotificationsRepository.saveAndFlush(schoolNotifications);

        int databaseSizeBeforeUpdate = schoolNotificationsRepository.findAll().size();

        // Update the schoolNotifications using partial update
        SchoolNotifications partialUpdatedSchoolNotifications = new SchoolNotifications();
        partialUpdatedSchoolNotifications.setId(schoolNotifications.getId());

        partialUpdatedSchoolNotifications
            .notificationDetails(UPDATED_NOTIFICATION_DETAILS)
            .notificationFileLink(UPDATED_NOTIFICATION_FILE_LINK)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED);

        restSchoolNotificationsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSchoolNotifications.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSchoolNotifications))
            )
            .andExpect(status().isOk());

        // Validate the SchoolNotifications in the database
        List<SchoolNotifications> schoolNotificationsList = schoolNotificationsRepository.findAll();
        assertThat(schoolNotificationsList).hasSize(databaseSizeBeforeUpdate);
        SchoolNotifications testSchoolNotifications = schoolNotificationsList.get(schoolNotificationsList.size() - 1);
        assertThat(testSchoolNotifications.getNotificationTitle()).isEqualTo(DEFAULT_NOTIFICATION_TITLE);
        assertThat(testSchoolNotifications.getNotificationDetails()).isEqualTo(UPDATED_NOTIFICATION_DETAILS);
        assertThat(testSchoolNotifications.getNotificationFile()).isEqualTo(DEFAULT_NOTIFICATION_FILE);
        assertThat(testSchoolNotifications.getNotificationFileContentType()).isEqualTo(DEFAULT_NOTIFICATION_FILE_CONTENT_TYPE);
        assertThat(testSchoolNotifications.getNotificationFileLink()).isEqualTo(UPDATED_NOTIFICATION_FILE_LINK);
        assertThat(testSchoolNotifications.getShowTillDate()).isEqualTo(DEFAULT_SHOW_TILL_DATE);
        assertThat(testSchoolNotifications.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testSchoolNotifications.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testSchoolNotifications.getCancelDate()).isEqualTo(DEFAULT_CANCEL_DATE);
    }

    @Test
    @Transactional
    void fullUpdateSchoolNotificationsWithPatch() throws Exception {
        // Initialize the database
        schoolNotificationsRepository.saveAndFlush(schoolNotifications);

        int databaseSizeBeforeUpdate = schoolNotificationsRepository.findAll().size();

        // Update the schoolNotifications using partial update
        SchoolNotifications partialUpdatedSchoolNotifications = new SchoolNotifications();
        partialUpdatedSchoolNotifications.setId(schoolNotifications.getId());

        partialUpdatedSchoolNotifications
            .notificationTitle(UPDATED_NOTIFICATION_TITLE)
            .notificationDetails(UPDATED_NOTIFICATION_DETAILS)
            .notificationFile(UPDATED_NOTIFICATION_FILE)
            .notificationFileContentType(UPDATED_NOTIFICATION_FILE_CONTENT_TYPE)
            .notificationFileLink(UPDATED_NOTIFICATION_FILE_LINK)
            .showTillDate(UPDATED_SHOW_TILL_DATE)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);

        restSchoolNotificationsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSchoolNotifications.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSchoolNotifications))
            )
            .andExpect(status().isOk());

        // Validate the SchoolNotifications in the database
        List<SchoolNotifications> schoolNotificationsList = schoolNotificationsRepository.findAll();
        assertThat(schoolNotificationsList).hasSize(databaseSizeBeforeUpdate);
        SchoolNotifications testSchoolNotifications = schoolNotificationsList.get(schoolNotificationsList.size() - 1);
        assertThat(testSchoolNotifications.getNotificationTitle()).isEqualTo(UPDATED_NOTIFICATION_TITLE);
        assertThat(testSchoolNotifications.getNotificationDetails()).isEqualTo(UPDATED_NOTIFICATION_DETAILS);
        assertThat(testSchoolNotifications.getNotificationFile()).isEqualTo(UPDATED_NOTIFICATION_FILE);
        assertThat(testSchoolNotifications.getNotificationFileContentType()).isEqualTo(UPDATED_NOTIFICATION_FILE_CONTENT_TYPE);
        assertThat(testSchoolNotifications.getNotificationFileLink()).isEqualTo(UPDATED_NOTIFICATION_FILE_LINK);
        assertThat(testSchoolNotifications.getShowTillDate()).isEqualTo(UPDATED_SHOW_TILL_DATE);
        assertThat(testSchoolNotifications.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testSchoolNotifications.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testSchoolNotifications.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingSchoolNotifications() throws Exception {
        int databaseSizeBeforeUpdate = schoolNotificationsRepository.findAll().size();
        schoolNotifications.setId(count.incrementAndGet());

        // Create the SchoolNotifications
        SchoolNotificationsDTO schoolNotificationsDTO = schoolNotificationsMapper.toDto(schoolNotifications);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSchoolNotificationsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, schoolNotificationsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(schoolNotificationsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchoolNotifications in the database
        List<SchoolNotifications> schoolNotificationsList = schoolNotificationsRepository.findAll();
        assertThat(schoolNotificationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSchoolNotifications() throws Exception {
        int databaseSizeBeforeUpdate = schoolNotificationsRepository.findAll().size();
        schoolNotifications.setId(count.incrementAndGet());

        // Create the SchoolNotifications
        SchoolNotificationsDTO schoolNotificationsDTO = schoolNotificationsMapper.toDto(schoolNotifications);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchoolNotificationsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(schoolNotificationsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchoolNotifications in the database
        List<SchoolNotifications> schoolNotificationsList = schoolNotificationsRepository.findAll();
        assertThat(schoolNotificationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSchoolNotifications() throws Exception {
        int databaseSizeBeforeUpdate = schoolNotificationsRepository.findAll().size();
        schoolNotifications.setId(count.incrementAndGet());

        // Create the SchoolNotifications
        SchoolNotificationsDTO schoolNotificationsDTO = schoolNotificationsMapper.toDto(schoolNotifications);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchoolNotificationsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(schoolNotificationsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SchoolNotifications in the database
        List<SchoolNotifications> schoolNotificationsList = schoolNotificationsRepository.findAll();
        assertThat(schoolNotificationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSchoolNotifications() throws Exception {
        // Initialize the database
        schoolNotificationsRepository.saveAndFlush(schoolNotifications);

        int databaseSizeBeforeDelete = schoolNotificationsRepository.findAll().size();

        // Delete the schoolNotifications
        restSchoolNotificationsMockMvc
            .perform(delete(ENTITY_API_URL_ID, schoolNotifications.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SchoolNotifications> schoolNotificationsList = schoolNotificationsRepository.findAll();
        assertThat(schoolNotificationsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
