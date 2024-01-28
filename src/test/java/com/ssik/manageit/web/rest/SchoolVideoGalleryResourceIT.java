package com.ssik.manageit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ssik.manageit.IntegrationTest;
import com.ssik.manageit.domain.SchoolClass;
import com.ssik.manageit.domain.SchoolVideoGallery;
import com.ssik.manageit.repository.SchoolVideoGalleryRepository;
import com.ssik.manageit.service.SchoolVideoGalleryService;
import com.ssik.manageit.service.criteria.SchoolVideoGalleryCriteria;
import com.ssik.manageit.service.dto.SchoolVideoGalleryDTO;
import com.ssik.manageit.service.mapper.SchoolVideoGalleryMapper;
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
 * Integration tests for the {@link SchoolVideoGalleryResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SchoolVideoGalleryResourceIT {

    private static final String DEFAULT_VIDEO_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_VIDEO_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_VIDEO_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_VIDEO_DESCRIPTION = "BBBBBBBBBB";

    private static final byte[] DEFAULT_VIDEO_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_VIDEO_FILE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_VIDEO_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_VIDEO_FILE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_VIDEO_LINK = "AAAAAAAAAA";
    private static final String UPDATED_VIDEO_LINK = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATE_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CREATE_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_LAST_MODIFIED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_MODIFIED = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_LAST_MODIFIED = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_CANCEL_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CANCEL_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CANCEL_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/school-video-galleries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SchoolVideoGalleryRepository schoolVideoGalleryRepository;

    @Mock
    private SchoolVideoGalleryRepository schoolVideoGalleryRepositoryMock;

    @Autowired
    private SchoolVideoGalleryMapper schoolVideoGalleryMapper;

    @Mock
    private SchoolVideoGalleryService schoolVideoGalleryServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSchoolVideoGalleryMockMvc;

    private SchoolVideoGallery schoolVideoGallery;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SchoolVideoGallery createEntity(EntityManager em) {
        SchoolVideoGallery schoolVideoGallery = new SchoolVideoGallery()
            .videoTitle(DEFAULT_VIDEO_TITLE)
            .videoDescription(DEFAULT_VIDEO_DESCRIPTION)
            .videoFile(DEFAULT_VIDEO_FILE)
            .videoFileContentType(DEFAULT_VIDEO_FILE_CONTENT_TYPE)
            .videoLink(DEFAULT_VIDEO_LINK)
            .createDate(DEFAULT_CREATE_DATE)
            .lastModified(DEFAULT_LAST_MODIFIED)
            .cancelDate(DEFAULT_CANCEL_DATE);
        return schoolVideoGallery;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SchoolVideoGallery createUpdatedEntity(EntityManager em) {
        SchoolVideoGallery schoolVideoGallery = new SchoolVideoGallery()
            .videoTitle(UPDATED_VIDEO_TITLE)
            .videoDescription(UPDATED_VIDEO_DESCRIPTION)
            .videoFile(UPDATED_VIDEO_FILE)
            .videoFileContentType(UPDATED_VIDEO_FILE_CONTENT_TYPE)
            .videoLink(UPDATED_VIDEO_LINK)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);
        return schoolVideoGallery;
    }

    @BeforeEach
    public void initTest() {
        schoolVideoGallery = createEntity(em);
    }

    @Test
    @Transactional
    void createSchoolVideoGallery() throws Exception {
        int databaseSizeBeforeCreate = schoolVideoGalleryRepository.findAll().size();
        // Create the SchoolVideoGallery
        SchoolVideoGalleryDTO schoolVideoGalleryDTO = schoolVideoGalleryMapper.toDto(schoolVideoGallery);
        restSchoolVideoGalleryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schoolVideoGalleryDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SchoolVideoGallery in the database
        List<SchoolVideoGallery> schoolVideoGalleryList = schoolVideoGalleryRepository.findAll();
        assertThat(schoolVideoGalleryList).hasSize(databaseSizeBeforeCreate + 1);
        SchoolVideoGallery testSchoolVideoGallery = schoolVideoGalleryList.get(schoolVideoGalleryList.size() - 1);
        assertThat(testSchoolVideoGallery.getVideoTitle()).isEqualTo(DEFAULT_VIDEO_TITLE);
        assertThat(testSchoolVideoGallery.getVideoDescription()).isEqualTo(DEFAULT_VIDEO_DESCRIPTION);
        assertThat(testSchoolVideoGallery.getVideoFile()).isEqualTo(DEFAULT_VIDEO_FILE);
        assertThat(testSchoolVideoGallery.getVideoFileContentType()).isEqualTo(DEFAULT_VIDEO_FILE_CONTENT_TYPE);
        assertThat(testSchoolVideoGallery.getVideoLink()).isEqualTo(DEFAULT_VIDEO_LINK);
        assertThat(testSchoolVideoGallery.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testSchoolVideoGallery.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
        assertThat(testSchoolVideoGallery.getCancelDate()).isEqualTo(DEFAULT_CANCEL_DATE);
    }

    @Test
    @Transactional
    void createSchoolVideoGalleryWithExistingId() throws Exception {
        // Create the SchoolVideoGallery with an existing ID
        schoolVideoGallery.setId(1L);
        SchoolVideoGalleryDTO schoolVideoGalleryDTO = schoolVideoGalleryMapper.toDto(schoolVideoGallery);

        int databaseSizeBeforeCreate = schoolVideoGalleryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSchoolVideoGalleryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schoolVideoGalleryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchoolVideoGallery in the database
        List<SchoolVideoGallery> schoolVideoGalleryList = schoolVideoGalleryRepository.findAll();
        assertThat(schoolVideoGalleryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkVideoTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = schoolVideoGalleryRepository.findAll().size();
        // set the field null
        schoolVideoGallery.setVideoTitle(null);

        // Create the SchoolVideoGallery, which fails.
        SchoolVideoGalleryDTO schoolVideoGalleryDTO = schoolVideoGalleryMapper.toDto(schoolVideoGallery);

        restSchoolVideoGalleryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schoolVideoGalleryDTO))
            )
            .andExpect(status().isBadRequest());

        List<SchoolVideoGallery> schoolVideoGalleryList = schoolVideoGalleryRepository.findAll();
        assertThat(schoolVideoGalleryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSchoolVideoGalleries() throws Exception {
        // Initialize the database
        schoolVideoGalleryRepository.saveAndFlush(schoolVideoGallery);

        // Get all the schoolVideoGalleryList
        restSchoolVideoGalleryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(schoolVideoGallery.getId().intValue())))
            .andExpect(jsonPath("$.[*].videoTitle").value(hasItem(DEFAULT_VIDEO_TITLE)))
            .andExpect(jsonPath("$.[*].videoDescription").value(hasItem(DEFAULT_VIDEO_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].videoFileContentType").value(hasItem(DEFAULT_VIDEO_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].videoFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_VIDEO_FILE))))
            .andExpect(jsonPath("$.[*].videoLink").value(hasItem(DEFAULT_VIDEO_LINK)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].cancelDate").value(hasItem(DEFAULT_CANCEL_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSchoolVideoGalleriesWithEagerRelationshipsIsEnabled() throws Exception {
        when(schoolVideoGalleryServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSchoolVideoGalleryMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(schoolVideoGalleryServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSchoolVideoGalleriesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(schoolVideoGalleryServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSchoolVideoGalleryMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(schoolVideoGalleryServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getSchoolVideoGallery() throws Exception {
        // Initialize the database
        schoolVideoGalleryRepository.saveAndFlush(schoolVideoGallery);

        // Get the schoolVideoGallery
        restSchoolVideoGalleryMockMvc
            .perform(get(ENTITY_API_URL_ID, schoolVideoGallery.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(schoolVideoGallery.getId().intValue()))
            .andExpect(jsonPath("$.videoTitle").value(DEFAULT_VIDEO_TITLE))
            .andExpect(jsonPath("$.videoDescription").value(DEFAULT_VIDEO_DESCRIPTION))
            .andExpect(jsonPath("$.videoFileContentType").value(DEFAULT_VIDEO_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.videoFile").value(Base64Utils.encodeToString(DEFAULT_VIDEO_FILE)))
            .andExpect(jsonPath("$.videoLink").value(DEFAULT_VIDEO_LINK))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.lastModified").value(DEFAULT_LAST_MODIFIED.toString()))
            .andExpect(jsonPath("$.cancelDate").value(DEFAULT_CANCEL_DATE.toString()));
    }

    @Test
    @Transactional
    void getSchoolVideoGalleriesByIdFiltering() throws Exception {
        // Initialize the database
        schoolVideoGalleryRepository.saveAndFlush(schoolVideoGallery);

        Long id = schoolVideoGallery.getId();

        defaultSchoolVideoGalleryShouldBeFound("id.equals=" + id);
        defaultSchoolVideoGalleryShouldNotBeFound("id.notEquals=" + id);

        defaultSchoolVideoGalleryShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSchoolVideoGalleryShouldNotBeFound("id.greaterThan=" + id);

        defaultSchoolVideoGalleryShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSchoolVideoGalleryShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSchoolVideoGalleriesByVideoTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolVideoGalleryRepository.saveAndFlush(schoolVideoGallery);

        // Get all the schoolVideoGalleryList where videoTitle equals to DEFAULT_VIDEO_TITLE
        defaultSchoolVideoGalleryShouldBeFound("videoTitle.equals=" + DEFAULT_VIDEO_TITLE);

        // Get all the schoolVideoGalleryList where videoTitle equals to UPDATED_VIDEO_TITLE
        defaultSchoolVideoGalleryShouldNotBeFound("videoTitle.equals=" + UPDATED_VIDEO_TITLE);
    }

    @Test
    @Transactional
    void getAllSchoolVideoGalleriesByVideoTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        schoolVideoGalleryRepository.saveAndFlush(schoolVideoGallery);

        // Get all the schoolVideoGalleryList where videoTitle not equals to DEFAULT_VIDEO_TITLE
        defaultSchoolVideoGalleryShouldNotBeFound("videoTitle.notEquals=" + DEFAULT_VIDEO_TITLE);

        // Get all the schoolVideoGalleryList where videoTitle not equals to UPDATED_VIDEO_TITLE
        defaultSchoolVideoGalleryShouldBeFound("videoTitle.notEquals=" + UPDATED_VIDEO_TITLE);
    }

    @Test
    @Transactional
    void getAllSchoolVideoGalleriesByVideoTitleIsInShouldWork() throws Exception {
        // Initialize the database
        schoolVideoGalleryRepository.saveAndFlush(schoolVideoGallery);

        // Get all the schoolVideoGalleryList where videoTitle in DEFAULT_VIDEO_TITLE or UPDATED_VIDEO_TITLE
        defaultSchoolVideoGalleryShouldBeFound("videoTitle.in=" + DEFAULT_VIDEO_TITLE + "," + UPDATED_VIDEO_TITLE);

        // Get all the schoolVideoGalleryList where videoTitle equals to UPDATED_VIDEO_TITLE
        defaultSchoolVideoGalleryShouldNotBeFound("videoTitle.in=" + UPDATED_VIDEO_TITLE);
    }

    @Test
    @Transactional
    void getAllSchoolVideoGalleriesByVideoTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolVideoGalleryRepository.saveAndFlush(schoolVideoGallery);

        // Get all the schoolVideoGalleryList where videoTitle is not null
        defaultSchoolVideoGalleryShouldBeFound("videoTitle.specified=true");

        // Get all the schoolVideoGalleryList where videoTitle is null
        defaultSchoolVideoGalleryShouldNotBeFound("videoTitle.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolVideoGalleriesByVideoTitleContainsSomething() throws Exception {
        // Initialize the database
        schoolVideoGalleryRepository.saveAndFlush(schoolVideoGallery);

        // Get all the schoolVideoGalleryList where videoTitle contains DEFAULT_VIDEO_TITLE
        defaultSchoolVideoGalleryShouldBeFound("videoTitle.contains=" + DEFAULT_VIDEO_TITLE);

        // Get all the schoolVideoGalleryList where videoTitle contains UPDATED_VIDEO_TITLE
        defaultSchoolVideoGalleryShouldNotBeFound("videoTitle.contains=" + UPDATED_VIDEO_TITLE);
    }

    @Test
    @Transactional
    void getAllSchoolVideoGalleriesByVideoTitleNotContainsSomething() throws Exception {
        // Initialize the database
        schoolVideoGalleryRepository.saveAndFlush(schoolVideoGallery);

        // Get all the schoolVideoGalleryList where videoTitle does not contain DEFAULT_VIDEO_TITLE
        defaultSchoolVideoGalleryShouldNotBeFound("videoTitle.doesNotContain=" + DEFAULT_VIDEO_TITLE);

        // Get all the schoolVideoGalleryList where videoTitle does not contain UPDATED_VIDEO_TITLE
        defaultSchoolVideoGalleryShouldBeFound("videoTitle.doesNotContain=" + UPDATED_VIDEO_TITLE);
    }

    @Test
    @Transactional
    void getAllSchoolVideoGalleriesByVideoDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolVideoGalleryRepository.saveAndFlush(schoolVideoGallery);

        // Get all the schoolVideoGalleryList where videoDescription equals to DEFAULT_VIDEO_DESCRIPTION
        defaultSchoolVideoGalleryShouldBeFound("videoDescription.equals=" + DEFAULT_VIDEO_DESCRIPTION);

        // Get all the schoolVideoGalleryList where videoDescription equals to UPDATED_VIDEO_DESCRIPTION
        defaultSchoolVideoGalleryShouldNotBeFound("videoDescription.equals=" + UPDATED_VIDEO_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSchoolVideoGalleriesByVideoDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        schoolVideoGalleryRepository.saveAndFlush(schoolVideoGallery);

        // Get all the schoolVideoGalleryList where videoDescription not equals to DEFAULT_VIDEO_DESCRIPTION
        defaultSchoolVideoGalleryShouldNotBeFound("videoDescription.notEquals=" + DEFAULT_VIDEO_DESCRIPTION);

        // Get all the schoolVideoGalleryList where videoDescription not equals to UPDATED_VIDEO_DESCRIPTION
        defaultSchoolVideoGalleryShouldBeFound("videoDescription.notEquals=" + UPDATED_VIDEO_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSchoolVideoGalleriesByVideoDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        schoolVideoGalleryRepository.saveAndFlush(schoolVideoGallery);

        // Get all the schoolVideoGalleryList where videoDescription in DEFAULT_VIDEO_DESCRIPTION or UPDATED_VIDEO_DESCRIPTION
        defaultSchoolVideoGalleryShouldBeFound("videoDescription.in=" + DEFAULT_VIDEO_DESCRIPTION + "," + UPDATED_VIDEO_DESCRIPTION);

        // Get all the schoolVideoGalleryList where videoDescription equals to UPDATED_VIDEO_DESCRIPTION
        defaultSchoolVideoGalleryShouldNotBeFound("videoDescription.in=" + UPDATED_VIDEO_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSchoolVideoGalleriesByVideoDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolVideoGalleryRepository.saveAndFlush(schoolVideoGallery);

        // Get all the schoolVideoGalleryList where videoDescription is not null
        defaultSchoolVideoGalleryShouldBeFound("videoDescription.specified=true");

        // Get all the schoolVideoGalleryList where videoDescription is null
        defaultSchoolVideoGalleryShouldNotBeFound("videoDescription.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolVideoGalleriesByVideoDescriptionContainsSomething() throws Exception {
        // Initialize the database
        schoolVideoGalleryRepository.saveAndFlush(schoolVideoGallery);

        // Get all the schoolVideoGalleryList where videoDescription contains DEFAULT_VIDEO_DESCRIPTION
        defaultSchoolVideoGalleryShouldBeFound("videoDescription.contains=" + DEFAULT_VIDEO_DESCRIPTION);

        // Get all the schoolVideoGalleryList where videoDescription contains UPDATED_VIDEO_DESCRIPTION
        defaultSchoolVideoGalleryShouldNotBeFound("videoDescription.contains=" + UPDATED_VIDEO_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSchoolVideoGalleriesByVideoDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        schoolVideoGalleryRepository.saveAndFlush(schoolVideoGallery);

        // Get all the schoolVideoGalleryList where videoDescription does not contain DEFAULT_VIDEO_DESCRIPTION
        defaultSchoolVideoGalleryShouldNotBeFound("videoDescription.doesNotContain=" + DEFAULT_VIDEO_DESCRIPTION);

        // Get all the schoolVideoGalleryList where videoDescription does not contain UPDATED_VIDEO_DESCRIPTION
        defaultSchoolVideoGalleryShouldBeFound("videoDescription.doesNotContain=" + UPDATED_VIDEO_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSchoolVideoGalleriesByVideoLinkIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolVideoGalleryRepository.saveAndFlush(schoolVideoGallery);

        // Get all the schoolVideoGalleryList where videoLink equals to DEFAULT_VIDEO_LINK
        defaultSchoolVideoGalleryShouldBeFound("videoLink.equals=" + DEFAULT_VIDEO_LINK);

        // Get all the schoolVideoGalleryList where videoLink equals to UPDATED_VIDEO_LINK
        defaultSchoolVideoGalleryShouldNotBeFound("videoLink.equals=" + UPDATED_VIDEO_LINK);
    }

    @Test
    @Transactional
    void getAllSchoolVideoGalleriesByVideoLinkIsNotEqualToSomething() throws Exception {
        // Initialize the database
        schoolVideoGalleryRepository.saveAndFlush(schoolVideoGallery);

        // Get all the schoolVideoGalleryList where videoLink not equals to DEFAULT_VIDEO_LINK
        defaultSchoolVideoGalleryShouldNotBeFound("videoLink.notEquals=" + DEFAULT_VIDEO_LINK);

        // Get all the schoolVideoGalleryList where videoLink not equals to UPDATED_VIDEO_LINK
        defaultSchoolVideoGalleryShouldBeFound("videoLink.notEquals=" + UPDATED_VIDEO_LINK);
    }

    @Test
    @Transactional
    void getAllSchoolVideoGalleriesByVideoLinkIsInShouldWork() throws Exception {
        // Initialize the database
        schoolVideoGalleryRepository.saveAndFlush(schoolVideoGallery);

        // Get all the schoolVideoGalleryList where videoLink in DEFAULT_VIDEO_LINK or UPDATED_VIDEO_LINK
        defaultSchoolVideoGalleryShouldBeFound("videoLink.in=" + DEFAULT_VIDEO_LINK + "," + UPDATED_VIDEO_LINK);

        // Get all the schoolVideoGalleryList where videoLink equals to UPDATED_VIDEO_LINK
        defaultSchoolVideoGalleryShouldNotBeFound("videoLink.in=" + UPDATED_VIDEO_LINK);
    }

    @Test
    @Transactional
    void getAllSchoolVideoGalleriesByVideoLinkIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolVideoGalleryRepository.saveAndFlush(schoolVideoGallery);

        // Get all the schoolVideoGalleryList where videoLink is not null
        defaultSchoolVideoGalleryShouldBeFound("videoLink.specified=true");

        // Get all the schoolVideoGalleryList where videoLink is null
        defaultSchoolVideoGalleryShouldNotBeFound("videoLink.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolVideoGalleriesByVideoLinkContainsSomething() throws Exception {
        // Initialize the database
        schoolVideoGalleryRepository.saveAndFlush(schoolVideoGallery);

        // Get all the schoolVideoGalleryList where videoLink contains DEFAULT_VIDEO_LINK
        defaultSchoolVideoGalleryShouldBeFound("videoLink.contains=" + DEFAULT_VIDEO_LINK);

        // Get all the schoolVideoGalleryList where videoLink contains UPDATED_VIDEO_LINK
        defaultSchoolVideoGalleryShouldNotBeFound("videoLink.contains=" + UPDATED_VIDEO_LINK);
    }

    @Test
    @Transactional
    void getAllSchoolVideoGalleriesByVideoLinkNotContainsSomething() throws Exception {
        // Initialize the database
        schoolVideoGalleryRepository.saveAndFlush(schoolVideoGallery);

        // Get all the schoolVideoGalleryList where videoLink does not contain DEFAULT_VIDEO_LINK
        defaultSchoolVideoGalleryShouldNotBeFound("videoLink.doesNotContain=" + DEFAULT_VIDEO_LINK);

        // Get all the schoolVideoGalleryList where videoLink does not contain UPDATED_VIDEO_LINK
        defaultSchoolVideoGalleryShouldBeFound("videoLink.doesNotContain=" + UPDATED_VIDEO_LINK);
    }

    @Test
    @Transactional
    void getAllSchoolVideoGalleriesByCreateDateIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolVideoGalleryRepository.saveAndFlush(schoolVideoGallery);

        // Get all the schoolVideoGalleryList where createDate equals to DEFAULT_CREATE_DATE
        defaultSchoolVideoGalleryShouldBeFound("createDate.equals=" + DEFAULT_CREATE_DATE);

        // Get all the schoolVideoGalleryList where createDate equals to UPDATED_CREATE_DATE
        defaultSchoolVideoGalleryShouldNotBeFound("createDate.equals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolVideoGalleriesByCreateDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        schoolVideoGalleryRepository.saveAndFlush(schoolVideoGallery);

        // Get all the schoolVideoGalleryList where createDate not equals to DEFAULT_CREATE_DATE
        defaultSchoolVideoGalleryShouldNotBeFound("createDate.notEquals=" + DEFAULT_CREATE_DATE);

        // Get all the schoolVideoGalleryList where createDate not equals to UPDATED_CREATE_DATE
        defaultSchoolVideoGalleryShouldBeFound("createDate.notEquals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolVideoGalleriesByCreateDateIsInShouldWork() throws Exception {
        // Initialize the database
        schoolVideoGalleryRepository.saveAndFlush(schoolVideoGallery);

        // Get all the schoolVideoGalleryList where createDate in DEFAULT_CREATE_DATE or UPDATED_CREATE_DATE
        defaultSchoolVideoGalleryShouldBeFound("createDate.in=" + DEFAULT_CREATE_DATE + "," + UPDATED_CREATE_DATE);

        // Get all the schoolVideoGalleryList where createDate equals to UPDATED_CREATE_DATE
        defaultSchoolVideoGalleryShouldNotBeFound("createDate.in=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolVideoGalleriesByCreateDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolVideoGalleryRepository.saveAndFlush(schoolVideoGallery);

        // Get all the schoolVideoGalleryList where createDate is not null
        defaultSchoolVideoGalleryShouldBeFound("createDate.specified=true");

        // Get all the schoolVideoGalleryList where createDate is null
        defaultSchoolVideoGalleryShouldNotBeFound("createDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolVideoGalleriesByCreateDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        schoolVideoGalleryRepository.saveAndFlush(schoolVideoGallery);

        // Get all the schoolVideoGalleryList where createDate is greater than or equal to DEFAULT_CREATE_DATE
        defaultSchoolVideoGalleryShouldBeFound("createDate.greaterThanOrEqual=" + DEFAULT_CREATE_DATE);

        // Get all the schoolVideoGalleryList where createDate is greater than or equal to UPDATED_CREATE_DATE
        defaultSchoolVideoGalleryShouldNotBeFound("createDate.greaterThanOrEqual=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolVideoGalleriesByCreateDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        schoolVideoGalleryRepository.saveAndFlush(schoolVideoGallery);

        // Get all the schoolVideoGalleryList where createDate is less than or equal to DEFAULT_CREATE_DATE
        defaultSchoolVideoGalleryShouldBeFound("createDate.lessThanOrEqual=" + DEFAULT_CREATE_DATE);

        // Get all the schoolVideoGalleryList where createDate is less than or equal to SMALLER_CREATE_DATE
        defaultSchoolVideoGalleryShouldNotBeFound("createDate.lessThanOrEqual=" + SMALLER_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolVideoGalleriesByCreateDateIsLessThanSomething() throws Exception {
        // Initialize the database
        schoolVideoGalleryRepository.saveAndFlush(schoolVideoGallery);

        // Get all the schoolVideoGalleryList where createDate is less than DEFAULT_CREATE_DATE
        defaultSchoolVideoGalleryShouldNotBeFound("createDate.lessThan=" + DEFAULT_CREATE_DATE);

        // Get all the schoolVideoGalleryList where createDate is less than UPDATED_CREATE_DATE
        defaultSchoolVideoGalleryShouldBeFound("createDate.lessThan=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolVideoGalleriesByCreateDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        schoolVideoGalleryRepository.saveAndFlush(schoolVideoGallery);

        // Get all the schoolVideoGalleryList where createDate is greater than DEFAULT_CREATE_DATE
        defaultSchoolVideoGalleryShouldNotBeFound("createDate.greaterThan=" + DEFAULT_CREATE_DATE);

        // Get all the schoolVideoGalleryList where createDate is greater than SMALLER_CREATE_DATE
        defaultSchoolVideoGalleryShouldBeFound("createDate.greaterThan=" + SMALLER_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolVideoGalleriesByLastModifiedIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolVideoGalleryRepository.saveAndFlush(schoolVideoGallery);

        // Get all the schoolVideoGalleryList where lastModified equals to DEFAULT_LAST_MODIFIED
        defaultSchoolVideoGalleryShouldBeFound("lastModified.equals=" + DEFAULT_LAST_MODIFIED);

        // Get all the schoolVideoGalleryList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultSchoolVideoGalleryShouldNotBeFound("lastModified.equals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSchoolVideoGalleriesByLastModifiedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        schoolVideoGalleryRepository.saveAndFlush(schoolVideoGallery);

        // Get all the schoolVideoGalleryList where lastModified not equals to DEFAULT_LAST_MODIFIED
        defaultSchoolVideoGalleryShouldNotBeFound("lastModified.notEquals=" + DEFAULT_LAST_MODIFIED);

        // Get all the schoolVideoGalleryList where lastModified not equals to UPDATED_LAST_MODIFIED
        defaultSchoolVideoGalleryShouldBeFound("lastModified.notEquals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSchoolVideoGalleriesByLastModifiedIsInShouldWork() throws Exception {
        // Initialize the database
        schoolVideoGalleryRepository.saveAndFlush(schoolVideoGallery);

        // Get all the schoolVideoGalleryList where lastModified in DEFAULT_LAST_MODIFIED or UPDATED_LAST_MODIFIED
        defaultSchoolVideoGalleryShouldBeFound("lastModified.in=" + DEFAULT_LAST_MODIFIED + "," + UPDATED_LAST_MODIFIED);

        // Get all the schoolVideoGalleryList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultSchoolVideoGalleryShouldNotBeFound("lastModified.in=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSchoolVideoGalleriesByLastModifiedIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolVideoGalleryRepository.saveAndFlush(schoolVideoGallery);

        // Get all the schoolVideoGalleryList where lastModified is not null
        defaultSchoolVideoGalleryShouldBeFound("lastModified.specified=true");

        // Get all the schoolVideoGalleryList where lastModified is null
        defaultSchoolVideoGalleryShouldNotBeFound("lastModified.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolVideoGalleriesByLastModifiedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        schoolVideoGalleryRepository.saveAndFlush(schoolVideoGallery);

        // Get all the schoolVideoGalleryList where lastModified is greater than or equal to DEFAULT_LAST_MODIFIED
        defaultSchoolVideoGalleryShouldBeFound("lastModified.greaterThanOrEqual=" + DEFAULT_LAST_MODIFIED);

        // Get all the schoolVideoGalleryList where lastModified is greater than or equal to UPDATED_LAST_MODIFIED
        defaultSchoolVideoGalleryShouldNotBeFound("lastModified.greaterThanOrEqual=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSchoolVideoGalleriesByLastModifiedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        schoolVideoGalleryRepository.saveAndFlush(schoolVideoGallery);

        // Get all the schoolVideoGalleryList where lastModified is less than or equal to DEFAULT_LAST_MODIFIED
        defaultSchoolVideoGalleryShouldBeFound("lastModified.lessThanOrEqual=" + DEFAULT_LAST_MODIFIED);

        // Get all the schoolVideoGalleryList where lastModified is less than or equal to SMALLER_LAST_MODIFIED
        defaultSchoolVideoGalleryShouldNotBeFound("lastModified.lessThanOrEqual=" + SMALLER_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSchoolVideoGalleriesByLastModifiedIsLessThanSomething() throws Exception {
        // Initialize the database
        schoolVideoGalleryRepository.saveAndFlush(schoolVideoGallery);

        // Get all the schoolVideoGalleryList where lastModified is less than DEFAULT_LAST_MODIFIED
        defaultSchoolVideoGalleryShouldNotBeFound("lastModified.lessThan=" + DEFAULT_LAST_MODIFIED);

        // Get all the schoolVideoGalleryList where lastModified is less than UPDATED_LAST_MODIFIED
        defaultSchoolVideoGalleryShouldBeFound("lastModified.lessThan=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSchoolVideoGalleriesByLastModifiedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        schoolVideoGalleryRepository.saveAndFlush(schoolVideoGallery);

        // Get all the schoolVideoGalleryList where lastModified is greater than DEFAULT_LAST_MODIFIED
        defaultSchoolVideoGalleryShouldNotBeFound("lastModified.greaterThan=" + DEFAULT_LAST_MODIFIED);

        // Get all the schoolVideoGalleryList where lastModified is greater than SMALLER_LAST_MODIFIED
        defaultSchoolVideoGalleryShouldBeFound("lastModified.greaterThan=" + SMALLER_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSchoolVideoGalleriesByCancelDateIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolVideoGalleryRepository.saveAndFlush(schoolVideoGallery);

        // Get all the schoolVideoGalleryList where cancelDate equals to DEFAULT_CANCEL_DATE
        defaultSchoolVideoGalleryShouldBeFound("cancelDate.equals=" + DEFAULT_CANCEL_DATE);

        // Get all the schoolVideoGalleryList where cancelDate equals to UPDATED_CANCEL_DATE
        defaultSchoolVideoGalleryShouldNotBeFound("cancelDate.equals=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolVideoGalleriesByCancelDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        schoolVideoGalleryRepository.saveAndFlush(schoolVideoGallery);

        // Get all the schoolVideoGalleryList where cancelDate not equals to DEFAULT_CANCEL_DATE
        defaultSchoolVideoGalleryShouldNotBeFound("cancelDate.notEquals=" + DEFAULT_CANCEL_DATE);

        // Get all the schoolVideoGalleryList where cancelDate not equals to UPDATED_CANCEL_DATE
        defaultSchoolVideoGalleryShouldBeFound("cancelDate.notEquals=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolVideoGalleriesByCancelDateIsInShouldWork() throws Exception {
        // Initialize the database
        schoolVideoGalleryRepository.saveAndFlush(schoolVideoGallery);

        // Get all the schoolVideoGalleryList where cancelDate in DEFAULT_CANCEL_DATE or UPDATED_CANCEL_DATE
        defaultSchoolVideoGalleryShouldBeFound("cancelDate.in=" + DEFAULT_CANCEL_DATE + "," + UPDATED_CANCEL_DATE);

        // Get all the schoolVideoGalleryList where cancelDate equals to UPDATED_CANCEL_DATE
        defaultSchoolVideoGalleryShouldNotBeFound("cancelDate.in=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolVideoGalleriesByCancelDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolVideoGalleryRepository.saveAndFlush(schoolVideoGallery);

        // Get all the schoolVideoGalleryList where cancelDate is not null
        defaultSchoolVideoGalleryShouldBeFound("cancelDate.specified=true");

        // Get all the schoolVideoGalleryList where cancelDate is null
        defaultSchoolVideoGalleryShouldNotBeFound("cancelDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolVideoGalleriesByCancelDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        schoolVideoGalleryRepository.saveAndFlush(schoolVideoGallery);

        // Get all the schoolVideoGalleryList where cancelDate is greater than or equal to DEFAULT_CANCEL_DATE
        defaultSchoolVideoGalleryShouldBeFound("cancelDate.greaterThanOrEqual=" + DEFAULT_CANCEL_DATE);

        // Get all the schoolVideoGalleryList where cancelDate is greater than or equal to UPDATED_CANCEL_DATE
        defaultSchoolVideoGalleryShouldNotBeFound("cancelDate.greaterThanOrEqual=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolVideoGalleriesByCancelDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        schoolVideoGalleryRepository.saveAndFlush(schoolVideoGallery);

        // Get all the schoolVideoGalleryList where cancelDate is less than or equal to DEFAULT_CANCEL_DATE
        defaultSchoolVideoGalleryShouldBeFound("cancelDate.lessThanOrEqual=" + DEFAULT_CANCEL_DATE);

        // Get all the schoolVideoGalleryList where cancelDate is less than or equal to SMALLER_CANCEL_DATE
        defaultSchoolVideoGalleryShouldNotBeFound("cancelDate.lessThanOrEqual=" + SMALLER_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolVideoGalleriesByCancelDateIsLessThanSomething() throws Exception {
        // Initialize the database
        schoolVideoGalleryRepository.saveAndFlush(schoolVideoGallery);

        // Get all the schoolVideoGalleryList where cancelDate is less than DEFAULT_CANCEL_DATE
        defaultSchoolVideoGalleryShouldNotBeFound("cancelDate.lessThan=" + DEFAULT_CANCEL_DATE);

        // Get all the schoolVideoGalleryList where cancelDate is less than UPDATED_CANCEL_DATE
        defaultSchoolVideoGalleryShouldBeFound("cancelDate.lessThan=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolVideoGalleriesByCancelDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        schoolVideoGalleryRepository.saveAndFlush(schoolVideoGallery);

        // Get all the schoolVideoGalleryList where cancelDate is greater than DEFAULT_CANCEL_DATE
        defaultSchoolVideoGalleryShouldNotBeFound("cancelDate.greaterThan=" + DEFAULT_CANCEL_DATE);

        // Get all the schoolVideoGalleryList where cancelDate is greater than SMALLER_CANCEL_DATE
        defaultSchoolVideoGalleryShouldBeFound("cancelDate.greaterThan=" + SMALLER_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolVideoGalleriesBySchoolClassIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolVideoGalleryRepository.saveAndFlush(schoolVideoGallery);
        SchoolClass schoolClass = SchoolClassResourceIT.createEntity(em);
        em.persist(schoolClass);
        em.flush();
        schoolVideoGallery.addSchoolClass(schoolClass);
        schoolVideoGalleryRepository.saveAndFlush(schoolVideoGallery);
        Long schoolClassId = schoolClass.getId();

        // Get all the schoolVideoGalleryList where schoolClass equals to schoolClassId
        defaultSchoolVideoGalleryShouldBeFound("schoolClassId.equals=" + schoolClassId);

        // Get all the schoolVideoGalleryList where schoolClass equals to (schoolClassId + 1)
        defaultSchoolVideoGalleryShouldNotBeFound("schoolClassId.equals=" + (schoolClassId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSchoolVideoGalleryShouldBeFound(String filter) throws Exception {
        restSchoolVideoGalleryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(schoolVideoGallery.getId().intValue())))
            .andExpect(jsonPath("$.[*].videoTitle").value(hasItem(DEFAULT_VIDEO_TITLE)))
            .andExpect(jsonPath("$.[*].videoDescription").value(hasItem(DEFAULT_VIDEO_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].videoFileContentType").value(hasItem(DEFAULT_VIDEO_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].videoFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_VIDEO_FILE))))
            .andExpect(jsonPath("$.[*].videoLink").value(hasItem(DEFAULT_VIDEO_LINK)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].cancelDate").value(hasItem(DEFAULT_CANCEL_DATE.toString())));

        // Check, that the count call also returns 1
        restSchoolVideoGalleryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSchoolVideoGalleryShouldNotBeFound(String filter) throws Exception {
        restSchoolVideoGalleryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSchoolVideoGalleryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSchoolVideoGallery() throws Exception {
        // Get the schoolVideoGallery
        restSchoolVideoGalleryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSchoolVideoGallery() throws Exception {
        // Initialize the database
        schoolVideoGalleryRepository.saveAndFlush(schoolVideoGallery);

        int databaseSizeBeforeUpdate = schoolVideoGalleryRepository.findAll().size();

        // Update the schoolVideoGallery
        SchoolVideoGallery updatedSchoolVideoGallery = schoolVideoGalleryRepository.findById(schoolVideoGallery.getId()).get();
        // Disconnect from session so that the updates on updatedSchoolVideoGallery are not directly saved in db
        em.detach(updatedSchoolVideoGallery);
        updatedSchoolVideoGallery
            .videoTitle(UPDATED_VIDEO_TITLE)
            .videoDescription(UPDATED_VIDEO_DESCRIPTION)
            .videoFile(UPDATED_VIDEO_FILE)
            .videoFileContentType(UPDATED_VIDEO_FILE_CONTENT_TYPE)
            .videoLink(UPDATED_VIDEO_LINK)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);
        SchoolVideoGalleryDTO schoolVideoGalleryDTO = schoolVideoGalleryMapper.toDto(updatedSchoolVideoGallery);

        restSchoolVideoGalleryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, schoolVideoGalleryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schoolVideoGalleryDTO))
            )
            .andExpect(status().isOk());

        // Validate the SchoolVideoGallery in the database
        List<SchoolVideoGallery> schoolVideoGalleryList = schoolVideoGalleryRepository.findAll();
        assertThat(schoolVideoGalleryList).hasSize(databaseSizeBeforeUpdate);
        SchoolVideoGallery testSchoolVideoGallery = schoolVideoGalleryList.get(schoolVideoGalleryList.size() - 1);
        assertThat(testSchoolVideoGallery.getVideoTitle()).isEqualTo(UPDATED_VIDEO_TITLE);
        assertThat(testSchoolVideoGallery.getVideoDescription()).isEqualTo(UPDATED_VIDEO_DESCRIPTION);
        assertThat(testSchoolVideoGallery.getVideoFile()).isEqualTo(UPDATED_VIDEO_FILE);
        assertThat(testSchoolVideoGallery.getVideoFileContentType()).isEqualTo(UPDATED_VIDEO_FILE_CONTENT_TYPE);
        assertThat(testSchoolVideoGallery.getVideoLink()).isEqualTo(UPDATED_VIDEO_LINK);
        assertThat(testSchoolVideoGallery.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testSchoolVideoGallery.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testSchoolVideoGallery.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void putNonExistingSchoolVideoGallery() throws Exception {
        int databaseSizeBeforeUpdate = schoolVideoGalleryRepository.findAll().size();
        schoolVideoGallery.setId(count.incrementAndGet());

        // Create the SchoolVideoGallery
        SchoolVideoGalleryDTO schoolVideoGalleryDTO = schoolVideoGalleryMapper.toDto(schoolVideoGallery);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSchoolVideoGalleryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, schoolVideoGalleryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schoolVideoGalleryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchoolVideoGallery in the database
        List<SchoolVideoGallery> schoolVideoGalleryList = schoolVideoGalleryRepository.findAll();
        assertThat(schoolVideoGalleryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSchoolVideoGallery() throws Exception {
        int databaseSizeBeforeUpdate = schoolVideoGalleryRepository.findAll().size();
        schoolVideoGallery.setId(count.incrementAndGet());

        // Create the SchoolVideoGallery
        SchoolVideoGalleryDTO schoolVideoGalleryDTO = schoolVideoGalleryMapper.toDto(schoolVideoGallery);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchoolVideoGalleryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schoolVideoGalleryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchoolVideoGallery in the database
        List<SchoolVideoGallery> schoolVideoGalleryList = schoolVideoGalleryRepository.findAll();
        assertThat(schoolVideoGalleryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSchoolVideoGallery() throws Exception {
        int databaseSizeBeforeUpdate = schoolVideoGalleryRepository.findAll().size();
        schoolVideoGallery.setId(count.incrementAndGet());

        // Create the SchoolVideoGallery
        SchoolVideoGalleryDTO schoolVideoGalleryDTO = schoolVideoGalleryMapper.toDto(schoolVideoGallery);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchoolVideoGalleryMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schoolVideoGalleryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SchoolVideoGallery in the database
        List<SchoolVideoGallery> schoolVideoGalleryList = schoolVideoGalleryRepository.findAll();
        assertThat(schoolVideoGalleryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSchoolVideoGalleryWithPatch() throws Exception {
        // Initialize the database
        schoolVideoGalleryRepository.saveAndFlush(schoolVideoGallery);

        int databaseSizeBeforeUpdate = schoolVideoGalleryRepository.findAll().size();

        // Update the schoolVideoGallery using partial update
        SchoolVideoGallery partialUpdatedSchoolVideoGallery = new SchoolVideoGallery();
        partialUpdatedSchoolVideoGallery.setId(schoolVideoGallery.getId());

        partialUpdatedSchoolVideoGallery
            .videoTitle(UPDATED_VIDEO_TITLE)
            .videoDescription(UPDATED_VIDEO_DESCRIPTION)
            .videoLink(UPDATED_VIDEO_LINK)
            .createDate(UPDATED_CREATE_DATE)
            .cancelDate(UPDATED_CANCEL_DATE);

        restSchoolVideoGalleryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSchoolVideoGallery.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSchoolVideoGallery))
            )
            .andExpect(status().isOk());

        // Validate the SchoolVideoGallery in the database
        List<SchoolVideoGallery> schoolVideoGalleryList = schoolVideoGalleryRepository.findAll();
        assertThat(schoolVideoGalleryList).hasSize(databaseSizeBeforeUpdate);
        SchoolVideoGallery testSchoolVideoGallery = schoolVideoGalleryList.get(schoolVideoGalleryList.size() - 1);
        assertThat(testSchoolVideoGallery.getVideoTitle()).isEqualTo(UPDATED_VIDEO_TITLE);
        assertThat(testSchoolVideoGallery.getVideoDescription()).isEqualTo(UPDATED_VIDEO_DESCRIPTION);
        assertThat(testSchoolVideoGallery.getVideoFile()).isEqualTo(DEFAULT_VIDEO_FILE);
        assertThat(testSchoolVideoGallery.getVideoFileContentType()).isEqualTo(DEFAULT_VIDEO_FILE_CONTENT_TYPE);
        assertThat(testSchoolVideoGallery.getVideoLink()).isEqualTo(UPDATED_VIDEO_LINK);
        assertThat(testSchoolVideoGallery.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testSchoolVideoGallery.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
        assertThat(testSchoolVideoGallery.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void fullUpdateSchoolVideoGalleryWithPatch() throws Exception {
        // Initialize the database
        schoolVideoGalleryRepository.saveAndFlush(schoolVideoGallery);

        int databaseSizeBeforeUpdate = schoolVideoGalleryRepository.findAll().size();

        // Update the schoolVideoGallery using partial update
        SchoolVideoGallery partialUpdatedSchoolVideoGallery = new SchoolVideoGallery();
        partialUpdatedSchoolVideoGallery.setId(schoolVideoGallery.getId());

        partialUpdatedSchoolVideoGallery
            .videoTitle(UPDATED_VIDEO_TITLE)
            .videoDescription(UPDATED_VIDEO_DESCRIPTION)
            .videoFile(UPDATED_VIDEO_FILE)
            .videoFileContentType(UPDATED_VIDEO_FILE_CONTENT_TYPE)
            .videoLink(UPDATED_VIDEO_LINK)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);

        restSchoolVideoGalleryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSchoolVideoGallery.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSchoolVideoGallery))
            )
            .andExpect(status().isOk());

        // Validate the SchoolVideoGallery in the database
        List<SchoolVideoGallery> schoolVideoGalleryList = schoolVideoGalleryRepository.findAll();
        assertThat(schoolVideoGalleryList).hasSize(databaseSizeBeforeUpdate);
        SchoolVideoGallery testSchoolVideoGallery = schoolVideoGalleryList.get(schoolVideoGalleryList.size() - 1);
        assertThat(testSchoolVideoGallery.getVideoTitle()).isEqualTo(UPDATED_VIDEO_TITLE);
        assertThat(testSchoolVideoGallery.getVideoDescription()).isEqualTo(UPDATED_VIDEO_DESCRIPTION);
        assertThat(testSchoolVideoGallery.getVideoFile()).isEqualTo(UPDATED_VIDEO_FILE);
        assertThat(testSchoolVideoGallery.getVideoFileContentType()).isEqualTo(UPDATED_VIDEO_FILE_CONTENT_TYPE);
        assertThat(testSchoolVideoGallery.getVideoLink()).isEqualTo(UPDATED_VIDEO_LINK);
        assertThat(testSchoolVideoGallery.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testSchoolVideoGallery.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testSchoolVideoGallery.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingSchoolVideoGallery() throws Exception {
        int databaseSizeBeforeUpdate = schoolVideoGalleryRepository.findAll().size();
        schoolVideoGallery.setId(count.incrementAndGet());

        // Create the SchoolVideoGallery
        SchoolVideoGalleryDTO schoolVideoGalleryDTO = schoolVideoGalleryMapper.toDto(schoolVideoGallery);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSchoolVideoGalleryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, schoolVideoGalleryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(schoolVideoGalleryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchoolVideoGallery in the database
        List<SchoolVideoGallery> schoolVideoGalleryList = schoolVideoGalleryRepository.findAll();
        assertThat(schoolVideoGalleryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSchoolVideoGallery() throws Exception {
        int databaseSizeBeforeUpdate = schoolVideoGalleryRepository.findAll().size();
        schoolVideoGallery.setId(count.incrementAndGet());

        // Create the SchoolVideoGallery
        SchoolVideoGalleryDTO schoolVideoGalleryDTO = schoolVideoGalleryMapper.toDto(schoolVideoGallery);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchoolVideoGalleryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(schoolVideoGalleryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchoolVideoGallery in the database
        List<SchoolVideoGallery> schoolVideoGalleryList = schoolVideoGalleryRepository.findAll();
        assertThat(schoolVideoGalleryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSchoolVideoGallery() throws Exception {
        int databaseSizeBeforeUpdate = schoolVideoGalleryRepository.findAll().size();
        schoolVideoGallery.setId(count.incrementAndGet());

        // Create the SchoolVideoGallery
        SchoolVideoGalleryDTO schoolVideoGalleryDTO = schoolVideoGalleryMapper.toDto(schoolVideoGallery);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchoolVideoGalleryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(schoolVideoGalleryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SchoolVideoGallery in the database
        List<SchoolVideoGallery> schoolVideoGalleryList = schoolVideoGalleryRepository.findAll();
        assertThat(schoolVideoGalleryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSchoolVideoGallery() throws Exception {
        // Initialize the database
        schoolVideoGalleryRepository.saveAndFlush(schoolVideoGallery);

        int databaseSizeBeforeDelete = schoolVideoGalleryRepository.findAll().size();

        // Delete the schoolVideoGallery
        restSchoolVideoGalleryMockMvc
            .perform(delete(ENTITY_API_URL_ID, schoolVideoGallery.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SchoolVideoGallery> schoolVideoGalleryList = schoolVideoGalleryRepository.findAll();
        assertThat(schoolVideoGalleryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
