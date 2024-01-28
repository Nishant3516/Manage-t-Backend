package com.ssik.manageit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ssik.manageit.IntegrationTest;
import com.ssik.manageit.domain.SchoolClass;
import com.ssik.manageit.domain.SchoolPictureGallery;
import com.ssik.manageit.repository.SchoolPictureGalleryRepository;
import com.ssik.manageit.service.SchoolPictureGalleryService;
import com.ssik.manageit.service.criteria.SchoolPictureGalleryCriteria;
import com.ssik.manageit.service.dto.SchoolPictureGalleryDTO;
import com.ssik.manageit.service.mapper.SchoolPictureGalleryMapper;
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
 * Integration tests for the {@link SchoolPictureGalleryResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SchoolPictureGalleryResourceIT {

    private static final String DEFAULT_PICTURE_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_PICTURE_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_PICTURE_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_PICTURE_DESCRIPTION = "BBBBBBBBBB";

    private static final byte[] DEFAULT_PICTURE_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PICTURE_FILE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PICTURE_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PICTURE_FILE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_PICTURE_LINK = "AAAAAAAAAA";
    private static final String UPDATED_PICTURE_LINK = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATE_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CREATE_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_LAST_MODIFIED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_MODIFIED = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_LAST_MODIFIED = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_CANCEL_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CANCEL_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CANCEL_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/school-picture-galleries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SchoolPictureGalleryRepository schoolPictureGalleryRepository;

    @Mock
    private SchoolPictureGalleryRepository schoolPictureGalleryRepositoryMock;

    @Autowired
    private SchoolPictureGalleryMapper schoolPictureGalleryMapper;

    @Mock
    private SchoolPictureGalleryService schoolPictureGalleryServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSchoolPictureGalleryMockMvc;

    private SchoolPictureGallery schoolPictureGallery;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SchoolPictureGallery createEntity(EntityManager em) {
        SchoolPictureGallery schoolPictureGallery = new SchoolPictureGallery()
            .pictureTitle(DEFAULT_PICTURE_TITLE)
            .pictureDescription(DEFAULT_PICTURE_DESCRIPTION)
            .pictureFile(DEFAULT_PICTURE_FILE)
            .pictureFileContentType(DEFAULT_PICTURE_FILE_CONTENT_TYPE)
            .pictureLink(DEFAULT_PICTURE_LINK)
            .createDate(DEFAULT_CREATE_DATE)
            .lastModified(DEFAULT_LAST_MODIFIED)
            .cancelDate(DEFAULT_CANCEL_DATE);
        return schoolPictureGallery;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SchoolPictureGallery createUpdatedEntity(EntityManager em) {
        SchoolPictureGallery schoolPictureGallery = new SchoolPictureGallery()
            .pictureTitle(UPDATED_PICTURE_TITLE)
            .pictureDescription(UPDATED_PICTURE_DESCRIPTION)
            .pictureFile(UPDATED_PICTURE_FILE)
            .pictureFileContentType(UPDATED_PICTURE_FILE_CONTENT_TYPE)
            .pictureLink(UPDATED_PICTURE_LINK)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);
        return schoolPictureGallery;
    }

    @BeforeEach
    public void initTest() {
        schoolPictureGallery = createEntity(em);
    }

    @Test
    @Transactional
    void createSchoolPictureGallery() throws Exception {
        int databaseSizeBeforeCreate = schoolPictureGalleryRepository.findAll().size();
        // Create the SchoolPictureGallery
        SchoolPictureGalleryDTO schoolPictureGalleryDTO = schoolPictureGalleryMapper.toDto(schoolPictureGallery);
        restSchoolPictureGalleryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schoolPictureGalleryDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SchoolPictureGallery in the database
        List<SchoolPictureGallery> schoolPictureGalleryList = schoolPictureGalleryRepository.findAll();
        assertThat(schoolPictureGalleryList).hasSize(databaseSizeBeforeCreate + 1);
        SchoolPictureGallery testSchoolPictureGallery = schoolPictureGalleryList.get(schoolPictureGalleryList.size() - 1);
        assertThat(testSchoolPictureGallery.getPictureTitle()).isEqualTo(DEFAULT_PICTURE_TITLE);
        assertThat(testSchoolPictureGallery.getPictureDescription()).isEqualTo(DEFAULT_PICTURE_DESCRIPTION);
        assertThat(testSchoolPictureGallery.getPictureFile()).isEqualTo(DEFAULT_PICTURE_FILE);
        assertThat(testSchoolPictureGallery.getPictureFileContentType()).isEqualTo(DEFAULT_PICTURE_FILE_CONTENT_TYPE);
        assertThat(testSchoolPictureGallery.getPictureLink()).isEqualTo(DEFAULT_PICTURE_LINK);
        assertThat(testSchoolPictureGallery.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testSchoolPictureGallery.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
        assertThat(testSchoolPictureGallery.getCancelDate()).isEqualTo(DEFAULT_CANCEL_DATE);
    }

    @Test
    @Transactional
    void createSchoolPictureGalleryWithExistingId() throws Exception {
        // Create the SchoolPictureGallery with an existing ID
        schoolPictureGallery.setId(1L);
        SchoolPictureGalleryDTO schoolPictureGalleryDTO = schoolPictureGalleryMapper.toDto(schoolPictureGallery);

        int databaseSizeBeforeCreate = schoolPictureGalleryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSchoolPictureGalleryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schoolPictureGalleryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchoolPictureGallery in the database
        List<SchoolPictureGallery> schoolPictureGalleryList = schoolPictureGalleryRepository.findAll();
        assertThat(schoolPictureGalleryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPictureTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = schoolPictureGalleryRepository.findAll().size();
        // set the field null
        schoolPictureGallery.setPictureTitle(null);

        // Create the SchoolPictureGallery, which fails.
        SchoolPictureGalleryDTO schoolPictureGalleryDTO = schoolPictureGalleryMapper.toDto(schoolPictureGallery);

        restSchoolPictureGalleryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schoolPictureGalleryDTO))
            )
            .andExpect(status().isBadRequest());

        List<SchoolPictureGallery> schoolPictureGalleryList = schoolPictureGalleryRepository.findAll();
        assertThat(schoolPictureGalleryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSchoolPictureGalleries() throws Exception {
        // Initialize the database
        schoolPictureGalleryRepository.saveAndFlush(schoolPictureGallery);

        // Get all the schoolPictureGalleryList
        restSchoolPictureGalleryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(schoolPictureGallery.getId().intValue())))
            .andExpect(jsonPath("$.[*].pictureTitle").value(hasItem(DEFAULT_PICTURE_TITLE)))
            .andExpect(jsonPath("$.[*].pictureDescription").value(hasItem(DEFAULT_PICTURE_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].pictureFileContentType").value(hasItem(DEFAULT_PICTURE_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].pictureFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_PICTURE_FILE))))
            .andExpect(jsonPath("$.[*].pictureLink").value(hasItem(DEFAULT_PICTURE_LINK)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].cancelDate").value(hasItem(DEFAULT_CANCEL_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSchoolPictureGalleriesWithEagerRelationshipsIsEnabled() throws Exception {
        when(schoolPictureGalleryServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSchoolPictureGalleryMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(schoolPictureGalleryServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSchoolPictureGalleriesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(schoolPictureGalleryServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSchoolPictureGalleryMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(schoolPictureGalleryServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getSchoolPictureGallery() throws Exception {
        // Initialize the database
        schoolPictureGalleryRepository.saveAndFlush(schoolPictureGallery);

        // Get the schoolPictureGallery
        restSchoolPictureGalleryMockMvc
            .perform(get(ENTITY_API_URL_ID, schoolPictureGallery.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(schoolPictureGallery.getId().intValue()))
            .andExpect(jsonPath("$.pictureTitle").value(DEFAULT_PICTURE_TITLE))
            .andExpect(jsonPath("$.pictureDescription").value(DEFAULT_PICTURE_DESCRIPTION))
            .andExpect(jsonPath("$.pictureFileContentType").value(DEFAULT_PICTURE_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.pictureFile").value(Base64Utils.encodeToString(DEFAULT_PICTURE_FILE)))
            .andExpect(jsonPath("$.pictureLink").value(DEFAULT_PICTURE_LINK))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.lastModified").value(DEFAULT_LAST_MODIFIED.toString()))
            .andExpect(jsonPath("$.cancelDate").value(DEFAULT_CANCEL_DATE.toString()));
    }

    @Test
    @Transactional
    void getSchoolPictureGalleriesByIdFiltering() throws Exception {
        // Initialize the database
        schoolPictureGalleryRepository.saveAndFlush(schoolPictureGallery);

        Long id = schoolPictureGallery.getId();

        defaultSchoolPictureGalleryShouldBeFound("id.equals=" + id);
        defaultSchoolPictureGalleryShouldNotBeFound("id.notEquals=" + id);

        defaultSchoolPictureGalleryShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSchoolPictureGalleryShouldNotBeFound("id.greaterThan=" + id);

        defaultSchoolPictureGalleryShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSchoolPictureGalleryShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSchoolPictureGalleriesByPictureTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolPictureGalleryRepository.saveAndFlush(schoolPictureGallery);

        // Get all the schoolPictureGalleryList where pictureTitle equals to DEFAULT_PICTURE_TITLE
        defaultSchoolPictureGalleryShouldBeFound("pictureTitle.equals=" + DEFAULT_PICTURE_TITLE);

        // Get all the schoolPictureGalleryList where pictureTitle equals to UPDATED_PICTURE_TITLE
        defaultSchoolPictureGalleryShouldNotBeFound("pictureTitle.equals=" + UPDATED_PICTURE_TITLE);
    }

    @Test
    @Transactional
    void getAllSchoolPictureGalleriesByPictureTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        schoolPictureGalleryRepository.saveAndFlush(schoolPictureGallery);

        // Get all the schoolPictureGalleryList where pictureTitle not equals to DEFAULT_PICTURE_TITLE
        defaultSchoolPictureGalleryShouldNotBeFound("pictureTitle.notEquals=" + DEFAULT_PICTURE_TITLE);

        // Get all the schoolPictureGalleryList where pictureTitle not equals to UPDATED_PICTURE_TITLE
        defaultSchoolPictureGalleryShouldBeFound("pictureTitle.notEquals=" + UPDATED_PICTURE_TITLE);
    }

    @Test
    @Transactional
    void getAllSchoolPictureGalleriesByPictureTitleIsInShouldWork() throws Exception {
        // Initialize the database
        schoolPictureGalleryRepository.saveAndFlush(schoolPictureGallery);

        // Get all the schoolPictureGalleryList where pictureTitle in DEFAULT_PICTURE_TITLE or UPDATED_PICTURE_TITLE
        defaultSchoolPictureGalleryShouldBeFound("pictureTitle.in=" + DEFAULT_PICTURE_TITLE + "," + UPDATED_PICTURE_TITLE);

        // Get all the schoolPictureGalleryList where pictureTitle equals to UPDATED_PICTURE_TITLE
        defaultSchoolPictureGalleryShouldNotBeFound("pictureTitle.in=" + UPDATED_PICTURE_TITLE);
    }

    @Test
    @Transactional
    void getAllSchoolPictureGalleriesByPictureTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolPictureGalleryRepository.saveAndFlush(schoolPictureGallery);

        // Get all the schoolPictureGalleryList where pictureTitle is not null
        defaultSchoolPictureGalleryShouldBeFound("pictureTitle.specified=true");

        // Get all the schoolPictureGalleryList where pictureTitle is null
        defaultSchoolPictureGalleryShouldNotBeFound("pictureTitle.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolPictureGalleriesByPictureTitleContainsSomething() throws Exception {
        // Initialize the database
        schoolPictureGalleryRepository.saveAndFlush(schoolPictureGallery);

        // Get all the schoolPictureGalleryList where pictureTitle contains DEFAULT_PICTURE_TITLE
        defaultSchoolPictureGalleryShouldBeFound("pictureTitle.contains=" + DEFAULT_PICTURE_TITLE);

        // Get all the schoolPictureGalleryList where pictureTitle contains UPDATED_PICTURE_TITLE
        defaultSchoolPictureGalleryShouldNotBeFound("pictureTitle.contains=" + UPDATED_PICTURE_TITLE);
    }

    @Test
    @Transactional
    void getAllSchoolPictureGalleriesByPictureTitleNotContainsSomething() throws Exception {
        // Initialize the database
        schoolPictureGalleryRepository.saveAndFlush(schoolPictureGallery);

        // Get all the schoolPictureGalleryList where pictureTitle does not contain DEFAULT_PICTURE_TITLE
        defaultSchoolPictureGalleryShouldNotBeFound("pictureTitle.doesNotContain=" + DEFAULT_PICTURE_TITLE);

        // Get all the schoolPictureGalleryList where pictureTitle does not contain UPDATED_PICTURE_TITLE
        defaultSchoolPictureGalleryShouldBeFound("pictureTitle.doesNotContain=" + UPDATED_PICTURE_TITLE);
    }

    @Test
    @Transactional
    void getAllSchoolPictureGalleriesByPictureDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolPictureGalleryRepository.saveAndFlush(schoolPictureGallery);

        // Get all the schoolPictureGalleryList where pictureDescription equals to DEFAULT_PICTURE_DESCRIPTION
        defaultSchoolPictureGalleryShouldBeFound("pictureDescription.equals=" + DEFAULT_PICTURE_DESCRIPTION);

        // Get all the schoolPictureGalleryList where pictureDescription equals to UPDATED_PICTURE_DESCRIPTION
        defaultSchoolPictureGalleryShouldNotBeFound("pictureDescription.equals=" + UPDATED_PICTURE_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSchoolPictureGalleriesByPictureDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        schoolPictureGalleryRepository.saveAndFlush(schoolPictureGallery);

        // Get all the schoolPictureGalleryList where pictureDescription not equals to DEFAULT_PICTURE_DESCRIPTION
        defaultSchoolPictureGalleryShouldNotBeFound("pictureDescription.notEquals=" + DEFAULT_PICTURE_DESCRIPTION);

        // Get all the schoolPictureGalleryList where pictureDescription not equals to UPDATED_PICTURE_DESCRIPTION
        defaultSchoolPictureGalleryShouldBeFound("pictureDescription.notEquals=" + UPDATED_PICTURE_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSchoolPictureGalleriesByPictureDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        schoolPictureGalleryRepository.saveAndFlush(schoolPictureGallery);

        // Get all the schoolPictureGalleryList where pictureDescription in DEFAULT_PICTURE_DESCRIPTION or UPDATED_PICTURE_DESCRIPTION
        defaultSchoolPictureGalleryShouldBeFound(
            "pictureDescription.in=" + DEFAULT_PICTURE_DESCRIPTION + "," + UPDATED_PICTURE_DESCRIPTION
        );

        // Get all the schoolPictureGalleryList where pictureDescription equals to UPDATED_PICTURE_DESCRIPTION
        defaultSchoolPictureGalleryShouldNotBeFound("pictureDescription.in=" + UPDATED_PICTURE_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSchoolPictureGalleriesByPictureDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolPictureGalleryRepository.saveAndFlush(schoolPictureGallery);

        // Get all the schoolPictureGalleryList where pictureDescription is not null
        defaultSchoolPictureGalleryShouldBeFound("pictureDescription.specified=true");

        // Get all the schoolPictureGalleryList where pictureDescription is null
        defaultSchoolPictureGalleryShouldNotBeFound("pictureDescription.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolPictureGalleriesByPictureDescriptionContainsSomething() throws Exception {
        // Initialize the database
        schoolPictureGalleryRepository.saveAndFlush(schoolPictureGallery);

        // Get all the schoolPictureGalleryList where pictureDescription contains DEFAULT_PICTURE_DESCRIPTION
        defaultSchoolPictureGalleryShouldBeFound("pictureDescription.contains=" + DEFAULT_PICTURE_DESCRIPTION);

        // Get all the schoolPictureGalleryList where pictureDescription contains UPDATED_PICTURE_DESCRIPTION
        defaultSchoolPictureGalleryShouldNotBeFound("pictureDescription.contains=" + UPDATED_PICTURE_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSchoolPictureGalleriesByPictureDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        schoolPictureGalleryRepository.saveAndFlush(schoolPictureGallery);

        // Get all the schoolPictureGalleryList where pictureDescription does not contain DEFAULT_PICTURE_DESCRIPTION
        defaultSchoolPictureGalleryShouldNotBeFound("pictureDescription.doesNotContain=" + DEFAULT_PICTURE_DESCRIPTION);

        // Get all the schoolPictureGalleryList where pictureDescription does not contain UPDATED_PICTURE_DESCRIPTION
        defaultSchoolPictureGalleryShouldBeFound("pictureDescription.doesNotContain=" + UPDATED_PICTURE_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSchoolPictureGalleriesByPictureLinkIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolPictureGalleryRepository.saveAndFlush(schoolPictureGallery);

        // Get all the schoolPictureGalleryList where pictureLink equals to DEFAULT_PICTURE_LINK
        defaultSchoolPictureGalleryShouldBeFound("pictureLink.equals=" + DEFAULT_PICTURE_LINK);

        // Get all the schoolPictureGalleryList where pictureLink equals to UPDATED_PICTURE_LINK
        defaultSchoolPictureGalleryShouldNotBeFound("pictureLink.equals=" + UPDATED_PICTURE_LINK);
    }

    @Test
    @Transactional
    void getAllSchoolPictureGalleriesByPictureLinkIsNotEqualToSomething() throws Exception {
        // Initialize the database
        schoolPictureGalleryRepository.saveAndFlush(schoolPictureGallery);

        // Get all the schoolPictureGalleryList where pictureLink not equals to DEFAULT_PICTURE_LINK
        defaultSchoolPictureGalleryShouldNotBeFound("pictureLink.notEquals=" + DEFAULT_PICTURE_LINK);

        // Get all the schoolPictureGalleryList where pictureLink not equals to UPDATED_PICTURE_LINK
        defaultSchoolPictureGalleryShouldBeFound("pictureLink.notEquals=" + UPDATED_PICTURE_LINK);
    }

    @Test
    @Transactional
    void getAllSchoolPictureGalleriesByPictureLinkIsInShouldWork() throws Exception {
        // Initialize the database
        schoolPictureGalleryRepository.saveAndFlush(schoolPictureGallery);

        // Get all the schoolPictureGalleryList where pictureLink in DEFAULT_PICTURE_LINK or UPDATED_PICTURE_LINK
        defaultSchoolPictureGalleryShouldBeFound("pictureLink.in=" + DEFAULT_PICTURE_LINK + "," + UPDATED_PICTURE_LINK);

        // Get all the schoolPictureGalleryList where pictureLink equals to UPDATED_PICTURE_LINK
        defaultSchoolPictureGalleryShouldNotBeFound("pictureLink.in=" + UPDATED_PICTURE_LINK);
    }

    @Test
    @Transactional
    void getAllSchoolPictureGalleriesByPictureLinkIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolPictureGalleryRepository.saveAndFlush(schoolPictureGallery);

        // Get all the schoolPictureGalleryList where pictureLink is not null
        defaultSchoolPictureGalleryShouldBeFound("pictureLink.specified=true");

        // Get all the schoolPictureGalleryList where pictureLink is null
        defaultSchoolPictureGalleryShouldNotBeFound("pictureLink.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolPictureGalleriesByPictureLinkContainsSomething() throws Exception {
        // Initialize the database
        schoolPictureGalleryRepository.saveAndFlush(schoolPictureGallery);

        // Get all the schoolPictureGalleryList where pictureLink contains DEFAULT_PICTURE_LINK
        defaultSchoolPictureGalleryShouldBeFound("pictureLink.contains=" + DEFAULT_PICTURE_LINK);

        // Get all the schoolPictureGalleryList where pictureLink contains UPDATED_PICTURE_LINK
        defaultSchoolPictureGalleryShouldNotBeFound("pictureLink.contains=" + UPDATED_PICTURE_LINK);
    }

    @Test
    @Transactional
    void getAllSchoolPictureGalleriesByPictureLinkNotContainsSomething() throws Exception {
        // Initialize the database
        schoolPictureGalleryRepository.saveAndFlush(schoolPictureGallery);

        // Get all the schoolPictureGalleryList where pictureLink does not contain DEFAULT_PICTURE_LINK
        defaultSchoolPictureGalleryShouldNotBeFound("pictureLink.doesNotContain=" + DEFAULT_PICTURE_LINK);

        // Get all the schoolPictureGalleryList where pictureLink does not contain UPDATED_PICTURE_LINK
        defaultSchoolPictureGalleryShouldBeFound("pictureLink.doesNotContain=" + UPDATED_PICTURE_LINK);
    }

    @Test
    @Transactional
    void getAllSchoolPictureGalleriesByCreateDateIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolPictureGalleryRepository.saveAndFlush(schoolPictureGallery);

        // Get all the schoolPictureGalleryList where createDate equals to DEFAULT_CREATE_DATE
        defaultSchoolPictureGalleryShouldBeFound("createDate.equals=" + DEFAULT_CREATE_DATE);

        // Get all the schoolPictureGalleryList where createDate equals to UPDATED_CREATE_DATE
        defaultSchoolPictureGalleryShouldNotBeFound("createDate.equals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolPictureGalleriesByCreateDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        schoolPictureGalleryRepository.saveAndFlush(schoolPictureGallery);

        // Get all the schoolPictureGalleryList where createDate not equals to DEFAULT_CREATE_DATE
        defaultSchoolPictureGalleryShouldNotBeFound("createDate.notEquals=" + DEFAULT_CREATE_DATE);

        // Get all the schoolPictureGalleryList where createDate not equals to UPDATED_CREATE_DATE
        defaultSchoolPictureGalleryShouldBeFound("createDate.notEquals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolPictureGalleriesByCreateDateIsInShouldWork() throws Exception {
        // Initialize the database
        schoolPictureGalleryRepository.saveAndFlush(schoolPictureGallery);

        // Get all the schoolPictureGalleryList where createDate in DEFAULT_CREATE_DATE or UPDATED_CREATE_DATE
        defaultSchoolPictureGalleryShouldBeFound("createDate.in=" + DEFAULT_CREATE_DATE + "," + UPDATED_CREATE_DATE);

        // Get all the schoolPictureGalleryList where createDate equals to UPDATED_CREATE_DATE
        defaultSchoolPictureGalleryShouldNotBeFound("createDate.in=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolPictureGalleriesByCreateDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolPictureGalleryRepository.saveAndFlush(schoolPictureGallery);

        // Get all the schoolPictureGalleryList where createDate is not null
        defaultSchoolPictureGalleryShouldBeFound("createDate.specified=true");

        // Get all the schoolPictureGalleryList where createDate is null
        defaultSchoolPictureGalleryShouldNotBeFound("createDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolPictureGalleriesByCreateDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        schoolPictureGalleryRepository.saveAndFlush(schoolPictureGallery);

        // Get all the schoolPictureGalleryList where createDate is greater than or equal to DEFAULT_CREATE_DATE
        defaultSchoolPictureGalleryShouldBeFound("createDate.greaterThanOrEqual=" + DEFAULT_CREATE_DATE);

        // Get all the schoolPictureGalleryList where createDate is greater than or equal to UPDATED_CREATE_DATE
        defaultSchoolPictureGalleryShouldNotBeFound("createDate.greaterThanOrEqual=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolPictureGalleriesByCreateDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        schoolPictureGalleryRepository.saveAndFlush(schoolPictureGallery);

        // Get all the schoolPictureGalleryList where createDate is less than or equal to DEFAULT_CREATE_DATE
        defaultSchoolPictureGalleryShouldBeFound("createDate.lessThanOrEqual=" + DEFAULT_CREATE_DATE);

        // Get all the schoolPictureGalleryList where createDate is less than or equal to SMALLER_CREATE_DATE
        defaultSchoolPictureGalleryShouldNotBeFound("createDate.lessThanOrEqual=" + SMALLER_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolPictureGalleriesByCreateDateIsLessThanSomething() throws Exception {
        // Initialize the database
        schoolPictureGalleryRepository.saveAndFlush(schoolPictureGallery);

        // Get all the schoolPictureGalleryList where createDate is less than DEFAULT_CREATE_DATE
        defaultSchoolPictureGalleryShouldNotBeFound("createDate.lessThan=" + DEFAULT_CREATE_DATE);

        // Get all the schoolPictureGalleryList where createDate is less than UPDATED_CREATE_DATE
        defaultSchoolPictureGalleryShouldBeFound("createDate.lessThan=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolPictureGalleriesByCreateDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        schoolPictureGalleryRepository.saveAndFlush(schoolPictureGallery);

        // Get all the schoolPictureGalleryList where createDate is greater than DEFAULT_CREATE_DATE
        defaultSchoolPictureGalleryShouldNotBeFound("createDate.greaterThan=" + DEFAULT_CREATE_DATE);

        // Get all the schoolPictureGalleryList where createDate is greater than SMALLER_CREATE_DATE
        defaultSchoolPictureGalleryShouldBeFound("createDate.greaterThan=" + SMALLER_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolPictureGalleriesByLastModifiedIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolPictureGalleryRepository.saveAndFlush(schoolPictureGallery);

        // Get all the schoolPictureGalleryList where lastModified equals to DEFAULT_LAST_MODIFIED
        defaultSchoolPictureGalleryShouldBeFound("lastModified.equals=" + DEFAULT_LAST_MODIFIED);

        // Get all the schoolPictureGalleryList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultSchoolPictureGalleryShouldNotBeFound("lastModified.equals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSchoolPictureGalleriesByLastModifiedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        schoolPictureGalleryRepository.saveAndFlush(schoolPictureGallery);

        // Get all the schoolPictureGalleryList where lastModified not equals to DEFAULT_LAST_MODIFIED
        defaultSchoolPictureGalleryShouldNotBeFound("lastModified.notEquals=" + DEFAULT_LAST_MODIFIED);

        // Get all the schoolPictureGalleryList where lastModified not equals to UPDATED_LAST_MODIFIED
        defaultSchoolPictureGalleryShouldBeFound("lastModified.notEquals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSchoolPictureGalleriesByLastModifiedIsInShouldWork() throws Exception {
        // Initialize the database
        schoolPictureGalleryRepository.saveAndFlush(schoolPictureGallery);

        // Get all the schoolPictureGalleryList where lastModified in DEFAULT_LAST_MODIFIED or UPDATED_LAST_MODIFIED
        defaultSchoolPictureGalleryShouldBeFound("lastModified.in=" + DEFAULT_LAST_MODIFIED + "," + UPDATED_LAST_MODIFIED);

        // Get all the schoolPictureGalleryList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultSchoolPictureGalleryShouldNotBeFound("lastModified.in=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSchoolPictureGalleriesByLastModifiedIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolPictureGalleryRepository.saveAndFlush(schoolPictureGallery);

        // Get all the schoolPictureGalleryList where lastModified is not null
        defaultSchoolPictureGalleryShouldBeFound("lastModified.specified=true");

        // Get all the schoolPictureGalleryList where lastModified is null
        defaultSchoolPictureGalleryShouldNotBeFound("lastModified.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolPictureGalleriesByLastModifiedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        schoolPictureGalleryRepository.saveAndFlush(schoolPictureGallery);

        // Get all the schoolPictureGalleryList where lastModified is greater than or equal to DEFAULT_LAST_MODIFIED
        defaultSchoolPictureGalleryShouldBeFound("lastModified.greaterThanOrEqual=" + DEFAULT_LAST_MODIFIED);

        // Get all the schoolPictureGalleryList where lastModified is greater than or equal to UPDATED_LAST_MODIFIED
        defaultSchoolPictureGalleryShouldNotBeFound("lastModified.greaterThanOrEqual=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSchoolPictureGalleriesByLastModifiedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        schoolPictureGalleryRepository.saveAndFlush(schoolPictureGallery);

        // Get all the schoolPictureGalleryList where lastModified is less than or equal to DEFAULT_LAST_MODIFIED
        defaultSchoolPictureGalleryShouldBeFound("lastModified.lessThanOrEqual=" + DEFAULT_LAST_MODIFIED);

        // Get all the schoolPictureGalleryList where lastModified is less than or equal to SMALLER_LAST_MODIFIED
        defaultSchoolPictureGalleryShouldNotBeFound("lastModified.lessThanOrEqual=" + SMALLER_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSchoolPictureGalleriesByLastModifiedIsLessThanSomething() throws Exception {
        // Initialize the database
        schoolPictureGalleryRepository.saveAndFlush(schoolPictureGallery);

        // Get all the schoolPictureGalleryList where lastModified is less than DEFAULT_LAST_MODIFIED
        defaultSchoolPictureGalleryShouldNotBeFound("lastModified.lessThan=" + DEFAULT_LAST_MODIFIED);

        // Get all the schoolPictureGalleryList where lastModified is less than UPDATED_LAST_MODIFIED
        defaultSchoolPictureGalleryShouldBeFound("lastModified.lessThan=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSchoolPictureGalleriesByLastModifiedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        schoolPictureGalleryRepository.saveAndFlush(schoolPictureGallery);

        // Get all the schoolPictureGalleryList where lastModified is greater than DEFAULT_LAST_MODIFIED
        defaultSchoolPictureGalleryShouldNotBeFound("lastModified.greaterThan=" + DEFAULT_LAST_MODIFIED);

        // Get all the schoolPictureGalleryList where lastModified is greater than SMALLER_LAST_MODIFIED
        defaultSchoolPictureGalleryShouldBeFound("lastModified.greaterThan=" + SMALLER_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSchoolPictureGalleriesByCancelDateIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolPictureGalleryRepository.saveAndFlush(schoolPictureGallery);

        // Get all the schoolPictureGalleryList where cancelDate equals to DEFAULT_CANCEL_DATE
        defaultSchoolPictureGalleryShouldBeFound("cancelDate.equals=" + DEFAULT_CANCEL_DATE);

        // Get all the schoolPictureGalleryList where cancelDate equals to UPDATED_CANCEL_DATE
        defaultSchoolPictureGalleryShouldNotBeFound("cancelDate.equals=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolPictureGalleriesByCancelDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        schoolPictureGalleryRepository.saveAndFlush(schoolPictureGallery);

        // Get all the schoolPictureGalleryList where cancelDate not equals to DEFAULT_CANCEL_DATE
        defaultSchoolPictureGalleryShouldNotBeFound("cancelDate.notEquals=" + DEFAULT_CANCEL_DATE);

        // Get all the schoolPictureGalleryList where cancelDate not equals to UPDATED_CANCEL_DATE
        defaultSchoolPictureGalleryShouldBeFound("cancelDate.notEquals=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolPictureGalleriesByCancelDateIsInShouldWork() throws Exception {
        // Initialize the database
        schoolPictureGalleryRepository.saveAndFlush(schoolPictureGallery);

        // Get all the schoolPictureGalleryList where cancelDate in DEFAULT_CANCEL_DATE or UPDATED_CANCEL_DATE
        defaultSchoolPictureGalleryShouldBeFound("cancelDate.in=" + DEFAULT_CANCEL_DATE + "," + UPDATED_CANCEL_DATE);

        // Get all the schoolPictureGalleryList where cancelDate equals to UPDATED_CANCEL_DATE
        defaultSchoolPictureGalleryShouldNotBeFound("cancelDate.in=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolPictureGalleriesByCancelDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolPictureGalleryRepository.saveAndFlush(schoolPictureGallery);

        // Get all the schoolPictureGalleryList where cancelDate is not null
        defaultSchoolPictureGalleryShouldBeFound("cancelDate.specified=true");

        // Get all the schoolPictureGalleryList where cancelDate is null
        defaultSchoolPictureGalleryShouldNotBeFound("cancelDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolPictureGalleriesByCancelDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        schoolPictureGalleryRepository.saveAndFlush(schoolPictureGallery);

        // Get all the schoolPictureGalleryList where cancelDate is greater than or equal to DEFAULT_CANCEL_DATE
        defaultSchoolPictureGalleryShouldBeFound("cancelDate.greaterThanOrEqual=" + DEFAULT_CANCEL_DATE);

        // Get all the schoolPictureGalleryList where cancelDate is greater than or equal to UPDATED_CANCEL_DATE
        defaultSchoolPictureGalleryShouldNotBeFound("cancelDate.greaterThanOrEqual=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolPictureGalleriesByCancelDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        schoolPictureGalleryRepository.saveAndFlush(schoolPictureGallery);

        // Get all the schoolPictureGalleryList where cancelDate is less than or equal to DEFAULT_CANCEL_DATE
        defaultSchoolPictureGalleryShouldBeFound("cancelDate.lessThanOrEqual=" + DEFAULT_CANCEL_DATE);

        // Get all the schoolPictureGalleryList where cancelDate is less than or equal to SMALLER_CANCEL_DATE
        defaultSchoolPictureGalleryShouldNotBeFound("cancelDate.lessThanOrEqual=" + SMALLER_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolPictureGalleriesByCancelDateIsLessThanSomething() throws Exception {
        // Initialize the database
        schoolPictureGalleryRepository.saveAndFlush(schoolPictureGallery);

        // Get all the schoolPictureGalleryList where cancelDate is less than DEFAULT_CANCEL_DATE
        defaultSchoolPictureGalleryShouldNotBeFound("cancelDate.lessThan=" + DEFAULT_CANCEL_DATE);

        // Get all the schoolPictureGalleryList where cancelDate is less than UPDATED_CANCEL_DATE
        defaultSchoolPictureGalleryShouldBeFound("cancelDate.lessThan=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolPictureGalleriesByCancelDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        schoolPictureGalleryRepository.saveAndFlush(schoolPictureGallery);

        // Get all the schoolPictureGalleryList where cancelDate is greater than DEFAULT_CANCEL_DATE
        defaultSchoolPictureGalleryShouldNotBeFound("cancelDate.greaterThan=" + DEFAULT_CANCEL_DATE);

        // Get all the schoolPictureGalleryList where cancelDate is greater than SMALLER_CANCEL_DATE
        defaultSchoolPictureGalleryShouldBeFound("cancelDate.greaterThan=" + SMALLER_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolPictureGalleriesBySchoolClassIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolPictureGalleryRepository.saveAndFlush(schoolPictureGallery);
        SchoolClass schoolClass = SchoolClassResourceIT.createEntity(em);
        em.persist(schoolClass);
        em.flush();
        schoolPictureGallery.addSchoolClass(schoolClass);
        schoolPictureGalleryRepository.saveAndFlush(schoolPictureGallery);
        Long schoolClassId = schoolClass.getId();

        // Get all the schoolPictureGalleryList where schoolClass equals to schoolClassId
        defaultSchoolPictureGalleryShouldBeFound("schoolClassId.equals=" + schoolClassId);

        // Get all the schoolPictureGalleryList where schoolClass equals to (schoolClassId + 1)
        defaultSchoolPictureGalleryShouldNotBeFound("schoolClassId.equals=" + (schoolClassId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSchoolPictureGalleryShouldBeFound(String filter) throws Exception {
        restSchoolPictureGalleryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(schoolPictureGallery.getId().intValue())))
            .andExpect(jsonPath("$.[*].pictureTitle").value(hasItem(DEFAULT_PICTURE_TITLE)))
            .andExpect(jsonPath("$.[*].pictureDescription").value(hasItem(DEFAULT_PICTURE_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].pictureFileContentType").value(hasItem(DEFAULT_PICTURE_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].pictureFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_PICTURE_FILE))))
            .andExpect(jsonPath("$.[*].pictureLink").value(hasItem(DEFAULT_PICTURE_LINK)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].cancelDate").value(hasItem(DEFAULT_CANCEL_DATE.toString())));

        // Check, that the count call also returns 1
        restSchoolPictureGalleryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSchoolPictureGalleryShouldNotBeFound(String filter) throws Exception {
        restSchoolPictureGalleryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSchoolPictureGalleryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSchoolPictureGallery() throws Exception {
        // Get the schoolPictureGallery
        restSchoolPictureGalleryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSchoolPictureGallery() throws Exception {
        // Initialize the database
        schoolPictureGalleryRepository.saveAndFlush(schoolPictureGallery);

        int databaseSizeBeforeUpdate = schoolPictureGalleryRepository.findAll().size();

        // Update the schoolPictureGallery
        SchoolPictureGallery updatedSchoolPictureGallery = schoolPictureGalleryRepository.findById(schoolPictureGallery.getId()).get();
        // Disconnect from session so that the updates on updatedSchoolPictureGallery are not directly saved in db
        em.detach(updatedSchoolPictureGallery);
        updatedSchoolPictureGallery
            .pictureTitle(UPDATED_PICTURE_TITLE)
            .pictureDescription(UPDATED_PICTURE_DESCRIPTION)
            .pictureFile(UPDATED_PICTURE_FILE)
            .pictureFileContentType(UPDATED_PICTURE_FILE_CONTENT_TYPE)
            .pictureLink(UPDATED_PICTURE_LINK)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);
        SchoolPictureGalleryDTO schoolPictureGalleryDTO = schoolPictureGalleryMapper.toDto(updatedSchoolPictureGallery);

        restSchoolPictureGalleryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, schoolPictureGalleryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schoolPictureGalleryDTO))
            )
            .andExpect(status().isOk());

        // Validate the SchoolPictureGallery in the database
        List<SchoolPictureGallery> schoolPictureGalleryList = schoolPictureGalleryRepository.findAll();
        assertThat(schoolPictureGalleryList).hasSize(databaseSizeBeforeUpdate);
        SchoolPictureGallery testSchoolPictureGallery = schoolPictureGalleryList.get(schoolPictureGalleryList.size() - 1);
        assertThat(testSchoolPictureGallery.getPictureTitle()).isEqualTo(UPDATED_PICTURE_TITLE);
        assertThat(testSchoolPictureGallery.getPictureDescription()).isEqualTo(UPDATED_PICTURE_DESCRIPTION);
        assertThat(testSchoolPictureGallery.getPictureFile()).isEqualTo(UPDATED_PICTURE_FILE);
        assertThat(testSchoolPictureGallery.getPictureFileContentType()).isEqualTo(UPDATED_PICTURE_FILE_CONTENT_TYPE);
        assertThat(testSchoolPictureGallery.getPictureLink()).isEqualTo(UPDATED_PICTURE_LINK);
        assertThat(testSchoolPictureGallery.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testSchoolPictureGallery.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testSchoolPictureGallery.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void putNonExistingSchoolPictureGallery() throws Exception {
        int databaseSizeBeforeUpdate = schoolPictureGalleryRepository.findAll().size();
        schoolPictureGallery.setId(count.incrementAndGet());

        // Create the SchoolPictureGallery
        SchoolPictureGalleryDTO schoolPictureGalleryDTO = schoolPictureGalleryMapper.toDto(schoolPictureGallery);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSchoolPictureGalleryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, schoolPictureGalleryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schoolPictureGalleryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchoolPictureGallery in the database
        List<SchoolPictureGallery> schoolPictureGalleryList = schoolPictureGalleryRepository.findAll();
        assertThat(schoolPictureGalleryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSchoolPictureGallery() throws Exception {
        int databaseSizeBeforeUpdate = schoolPictureGalleryRepository.findAll().size();
        schoolPictureGallery.setId(count.incrementAndGet());

        // Create the SchoolPictureGallery
        SchoolPictureGalleryDTO schoolPictureGalleryDTO = schoolPictureGalleryMapper.toDto(schoolPictureGallery);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchoolPictureGalleryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schoolPictureGalleryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchoolPictureGallery in the database
        List<SchoolPictureGallery> schoolPictureGalleryList = schoolPictureGalleryRepository.findAll();
        assertThat(schoolPictureGalleryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSchoolPictureGallery() throws Exception {
        int databaseSizeBeforeUpdate = schoolPictureGalleryRepository.findAll().size();
        schoolPictureGallery.setId(count.incrementAndGet());

        // Create the SchoolPictureGallery
        SchoolPictureGalleryDTO schoolPictureGalleryDTO = schoolPictureGalleryMapper.toDto(schoolPictureGallery);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchoolPictureGalleryMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schoolPictureGalleryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SchoolPictureGallery in the database
        List<SchoolPictureGallery> schoolPictureGalleryList = schoolPictureGalleryRepository.findAll();
        assertThat(schoolPictureGalleryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSchoolPictureGalleryWithPatch() throws Exception {
        // Initialize the database
        schoolPictureGalleryRepository.saveAndFlush(schoolPictureGallery);

        int databaseSizeBeforeUpdate = schoolPictureGalleryRepository.findAll().size();

        // Update the schoolPictureGallery using partial update
        SchoolPictureGallery partialUpdatedSchoolPictureGallery = new SchoolPictureGallery();
        partialUpdatedSchoolPictureGallery.setId(schoolPictureGallery.getId());

        partialUpdatedSchoolPictureGallery
            .pictureFile(UPDATED_PICTURE_FILE)
            .pictureFileContentType(UPDATED_PICTURE_FILE_CONTENT_TYPE)
            .pictureLink(UPDATED_PICTURE_LINK)
            .cancelDate(UPDATED_CANCEL_DATE);

        restSchoolPictureGalleryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSchoolPictureGallery.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSchoolPictureGallery))
            )
            .andExpect(status().isOk());

        // Validate the SchoolPictureGallery in the database
        List<SchoolPictureGallery> schoolPictureGalleryList = schoolPictureGalleryRepository.findAll();
        assertThat(schoolPictureGalleryList).hasSize(databaseSizeBeforeUpdate);
        SchoolPictureGallery testSchoolPictureGallery = schoolPictureGalleryList.get(schoolPictureGalleryList.size() - 1);
        assertThat(testSchoolPictureGallery.getPictureTitle()).isEqualTo(DEFAULT_PICTURE_TITLE);
        assertThat(testSchoolPictureGallery.getPictureDescription()).isEqualTo(DEFAULT_PICTURE_DESCRIPTION);
        assertThat(testSchoolPictureGallery.getPictureFile()).isEqualTo(UPDATED_PICTURE_FILE);
        assertThat(testSchoolPictureGallery.getPictureFileContentType()).isEqualTo(UPDATED_PICTURE_FILE_CONTENT_TYPE);
        assertThat(testSchoolPictureGallery.getPictureLink()).isEqualTo(UPDATED_PICTURE_LINK);
        assertThat(testSchoolPictureGallery.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testSchoolPictureGallery.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
        assertThat(testSchoolPictureGallery.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void fullUpdateSchoolPictureGalleryWithPatch() throws Exception {
        // Initialize the database
        schoolPictureGalleryRepository.saveAndFlush(schoolPictureGallery);

        int databaseSizeBeforeUpdate = schoolPictureGalleryRepository.findAll().size();

        // Update the schoolPictureGallery using partial update
        SchoolPictureGallery partialUpdatedSchoolPictureGallery = new SchoolPictureGallery();
        partialUpdatedSchoolPictureGallery.setId(schoolPictureGallery.getId());

        partialUpdatedSchoolPictureGallery
            .pictureTitle(UPDATED_PICTURE_TITLE)
            .pictureDescription(UPDATED_PICTURE_DESCRIPTION)
            .pictureFile(UPDATED_PICTURE_FILE)
            .pictureFileContentType(UPDATED_PICTURE_FILE_CONTENT_TYPE)
            .pictureLink(UPDATED_PICTURE_LINK)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);

        restSchoolPictureGalleryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSchoolPictureGallery.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSchoolPictureGallery))
            )
            .andExpect(status().isOk());

        // Validate the SchoolPictureGallery in the database
        List<SchoolPictureGallery> schoolPictureGalleryList = schoolPictureGalleryRepository.findAll();
        assertThat(schoolPictureGalleryList).hasSize(databaseSizeBeforeUpdate);
        SchoolPictureGallery testSchoolPictureGallery = schoolPictureGalleryList.get(schoolPictureGalleryList.size() - 1);
        assertThat(testSchoolPictureGallery.getPictureTitle()).isEqualTo(UPDATED_PICTURE_TITLE);
        assertThat(testSchoolPictureGallery.getPictureDescription()).isEqualTo(UPDATED_PICTURE_DESCRIPTION);
        assertThat(testSchoolPictureGallery.getPictureFile()).isEqualTo(UPDATED_PICTURE_FILE);
        assertThat(testSchoolPictureGallery.getPictureFileContentType()).isEqualTo(UPDATED_PICTURE_FILE_CONTENT_TYPE);
        assertThat(testSchoolPictureGallery.getPictureLink()).isEqualTo(UPDATED_PICTURE_LINK);
        assertThat(testSchoolPictureGallery.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testSchoolPictureGallery.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testSchoolPictureGallery.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingSchoolPictureGallery() throws Exception {
        int databaseSizeBeforeUpdate = schoolPictureGalleryRepository.findAll().size();
        schoolPictureGallery.setId(count.incrementAndGet());

        // Create the SchoolPictureGallery
        SchoolPictureGalleryDTO schoolPictureGalleryDTO = schoolPictureGalleryMapper.toDto(schoolPictureGallery);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSchoolPictureGalleryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, schoolPictureGalleryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(schoolPictureGalleryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchoolPictureGallery in the database
        List<SchoolPictureGallery> schoolPictureGalleryList = schoolPictureGalleryRepository.findAll();
        assertThat(schoolPictureGalleryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSchoolPictureGallery() throws Exception {
        int databaseSizeBeforeUpdate = schoolPictureGalleryRepository.findAll().size();
        schoolPictureGallery.setId(count.incrementAndGet());

        // Create the SchoolPictureGallery
        SchoolPictureGalleryDTO schoolPictureGalleryDTO = schoolPictureGalleryMapper.toDto(schoolPictureGallery);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchoolPictureGalleryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(schoolPictureGalleryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchoolPictureGallery in the database
        List<SchoolPictureGallery> schoolPictureGalleryList = schoolPictureGalleryRepository.findAll();
        assertThat(schoolPictureGalleryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSchoolPictureGallery() throws Exception {
        int databaseSizeBeforeUpdate = schoolPictureGalleryRepository.findAll().size();
        schoolPictureGallery.setId(count.incrementAndGet());

        // Create the SchoolPictureGallery
        SchoolPictureGalleryDTO schoolPictureGalleryDTO = schoolPictureGalleryMapper.toDto(schoolPictureGallery);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchoolPictureGalleryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(schoolPictureGalleryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SchoolPictureGallery in the database
        List<SchoolPictureGallery> schoolPictureGalleryList = schoolPictureGalleryRepository.findAll();
        assertThat(schoolPictureGalleryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSchoolPictureGallery() throws Exception {
        // Initialize the database
        schoolPictureGalleryRepository.saveAndFlush(schoolPictureGallery);

        int databaseSizeBeforeDelete = schoolPictureGalleryRepository.findAll().size();

        // Delete the schoolPictureGallery
        restSchoolPictureGalleryMockMvc
            .perform(delete(ENTITY_API_URL_ID, schoolPictureGallery.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SchoolPictureGallery> schoolPictureGalleryList = schoolPictureGalleryRepository.findAll();
        assertThat(schoolPictureGalleryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
