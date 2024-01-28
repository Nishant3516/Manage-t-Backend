package com.ssik.manageit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ssik.manageit.IntegrationTest;
import com.ssik.manageit.domain.IdStore;
import com.ssik.manageit.domain.School;
import com.ssik.manageit.domain.enumeration.IdType;
import com.ssik.manageit.repository.IdStoreRepository;
import com.ssik.manageit.service.criteria.IdStoreCriteria;
import com.ssik.manageit.service.dto.IdStoreDTO;
import com.ssik.manageit.service.mapper.IdStoreMapper;
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
 * Integration tests for the {@link IdStoreResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class IdStoreResourceIT {

    private static final IdType DEFAULT_ENTRYTYPE = IdType.STUDENT;
    private static final IdType UPDATED_ENTRYTYPE = IdType.RECEIPT;

    private static final Long DEFAULT_LAST_GENERATED_ID = 1L;
    private static final Long UPDATED_LAST_GENERATED_ID = 2L;
    private static final Long SMALLER_LAST_GENERATED_ID = 1L - 1L;

    private static final Long DEFAULT_START_ID = 1L;
    private static final Long UPDATED_START_ID = 2L;
    private static final Long SMALLER_START_ID = 1L - 1L;

    private static final LocalDate DEFAULT_CREATE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATE_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CREATE_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_LAST_MODIFIED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_MODIFIED = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_LAST_MODIFIED = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_CANCEL_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CANCEL_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CANCEL_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/id-stores";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private IdStoreRepository idStoreRepository;

    @Autowired
    private IdStoreMapper idStoreMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIdStoreMockMvc;

    private IdStore idStore;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IdStore createEntity(EntityManager em) {
        IdStore idStore = new IdStore()
            .entrytype(DEFAULT_ENTRYTYPE)
            .lastGeneratedId(DEFAULT_LAST_GENERATED_ID)
            .startId(DEFAULT_START_ID)
            .createDate(DEFAULT_CREATE_DATE)
            .lastModified(DEFAULT_LAST_MODIFIED)
            .cancelDate(DEFAULT_CANCEL_DATE);
        return idStore;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IdStore createUpdatedEntity(EntityManager em) {
        IdStore idStore = new IdStore()
            .entrytype(UPDATED_ENTRYTYPE)
            .lastGeneratedId(UPDATED_LAST_GENERATED_ID)
            .startId(UPDATED_START_ID)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);
        return idStore;
    }

    @BeforeEach
    public void initTest() {
        idStore = createEntity(em);
    }

    @Test
    @Transactional
    void createIdStore() throws Exception {
        int databaseSizeBeforeCreate = idStoreRepository.findAll().size();
        // Create the IdStore
        IdStoreDTO idStoreDTO = idStoreMapper.toDto(idStore);
        restIdStoreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(idStoreDTO)))
            .andExpect(status().isCreated());

        // Validate the IdStore in the database
        List<IdStore> idStoreList = idStoreRepository.findAll();
        assertThat(idStoreList).hasSize(databaseSizeBeforeCreate + 1);
        IdStore testIdStore = idStoreList.get(idStoreList.size() - 1);
        assertThat(testIdStore.getEntrytype()).isEqualTo(DEFAULT_ENTRYTYPE);
        assertThat(testIdStore.getLastGeneratedId()).isEqualTo(DEFAULT_LAST_GENERATED_ID);
        assertThat(testIdStore.getStartId()).isEqualTo(DEFAULT_START_ID);
        assertThat(testIdStore.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testIdStore.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
        assertThat(testIdStore.getCancelDate()).isEqualTo(DEFAULT_CANCEL_DATE);
    }

    @Test
    @Transactional
    void createIdStoreWithExistingId() throws Exception {
        // Create the IdStore with an existing ID
        idStore.setId(1L);
        IdStoreDTO idStoreDTO = idStoreMapper.toDto(idStore);

        int databaseSizeBeforeCreate = idStoreRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restIdStoreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(idStoreDTO)))
            .andExpect(status().isBadRequest());

        // Validate the IdStore in the database
        List<IdStore> idStoreList = idStoreRepository.findAll();
        assertThat(idStoreList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEntrytypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = idStoreRepository.findAll().size();
        // set the field null
        idStore.setEntrytype(null);

        // Create the IdStore, which fails.
        IdStoreDTO idStoreDTO = idStoreMapper.toDto(idStore);

        restIdStoreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(idStoreDTO)))
            .andExpect(status().isBadRequest());

        List<IdStore> idStoreList = idStoreRepository.findAll();
        assertThat(idStoreList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastGeneratedIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = idStoreRepository.findAll().size();
        // set the field null
        idStore.setLastGeneratedId(null);

        // Create the IdStore, which fails.
        IdStoreDTO idStoreDTO = idStoreMapper.toDto(idStore);

        restIdStoreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(idStoreDTO)))
            .andExpect(status().isBadRequest());

        List<IdStore> idStoreList = idStoreRepository.findAll();
        assertThat(idStoreList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllIdStores() throws Exception {
        // Initialize the database
        idStoreRepository.saveAndFlush(idStore);

        // Get all the idStoreList
        restIdStoreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(idStore.getId().intValue())))
            .andExpect(jsonPath("$.[*].entrytype").value(hasItem(DEFAULT_ENTRYTYPE.toString())))
            .andExpect(jsonPath("$.[*].lastGeneratedId").value(hasItem(DEFAULT_LAST_GENERATED_ID.intValue())))
            .andExpect(jsonPath("$.[*].startId").value(hasItem(DEFAULT_START_ID.intValue())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].cancelDate").value(hasItem(DEFAULT_CANCEL_DATE.toString())));
    }

    @Test
    @Transactional
    void getIdStore() throws Exception {
        // Initialize the database
        idStoreRepository.saveAndFlush(idStore);

        // Get the idStore
        restIdStoreMockMvc
            .perform(get(ENTITY_API_URL_ID, idStore.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(idStore.getId().intValue()))
            .andExpect(jsonPath("$.entrytype").value(DEFAULT_ENTRYTYPE.toString()))
            .andExpect(jsonPath("$.lastGeneratedId").value(DEFAULT_LAST_GENERATED_ID.intValue()))
            .andExpect(jsonPath("$.startId").value(DEFAULT_START_ID.intValue()))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.lastModified").value(DEFAULT_LAST_MODIFIED.toString()))
            .andExpect(jsonPath("$.cancelDate").value(DEFAULT_CANCEL_DATE.toString()));
    }

    @Test
    @Transactional
    void getIdStoresByIdFiltering() throws Exception {
        // Initialize the database
        idStoreRepository.saveAndFlush(idStore);

        Long id = idStore.getId();

        defaultIdStoreShouldBeFound("id.equals=" + id);
        defaultIdStoreShouldNotBeFound("id.notEquals=" + id);

        defaultIdStoreShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultIdStoreShouldNotBeFound("id.greaterThan=" + id);

        defaultIdStoreShouldBeFound("id.lessThanOrEqual=" + id);
        defaultIdStoreShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllIdStoresByEntrytypeIsEqualToSomething() throws Exception {
        // Initialize the database
        idStoreRepository.saveAndFlush(idStore);

        // Get all the idStoreList where entrytype equals to DEFAULT_ENTRYTYPE
        defaultIdStoreShouldBeFound("entrytype.equals=" + DEFAULT_ENTRYTYPE);

        // Get all the idStoreList where entrytype equals to UPDATED_ENTRYTYPE
        defaultIdStoreShouldNotBeFound("entrytype.equals=" + UPDATED_ENTRYTYPE);
    }

    @Test
    @Transactional
    void getAllIdStoresByEntrytypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        idStoreRepository.saveAndFlush(idStore);

        // Get all the idStoreList where entrytype not equals to DEFAULT_ENTRYTYPE
        defaultIdStoreShouldNotBeFound("entrytype.notEquals=" + DEFAULT_ENTRYTYPE);

        // Get all the idStoreList where entrytype not equals to UPDATED_ENTRYTYPE
        defaultIdStoreShouldBeFound("entrytype.notEquals=" + UPDATED_ENTRYTYPE);
    }

    @Test
    @Transactional
    void getAllIdStoresByEntrytypeIsInShouldWork() throws Exception {
        // Initialize the database
        idStoreRepository.saveAndFlush(idStore);

        // Get all the idStoreList where entrytype in DEFAULT_ENTRYTYPE or UPDATED_ENTRYTYPE
        defaultIdStoreShouldBeFound("entrytype.in=" + DEFAULT_ENTRYTYPE + "," + UPDATED_ENTRYTYPE);

        // Get all the idStoreList where entrytype equals to UPDATED_ENTRYTYPE
        defaultIdStoreShouldNotBeFound("entrytype.in=" + UPDATED_ENTRYTYPE);
    }

    @Test
    @Transactional
    void getAllIdStoresByEntrytypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        idStoreRepository.saveAndFlush(idStore);

        // Get all the idStoreList where entrytype is not null
        defaultIdStoreShouldBeFound("entrytype.specified=true");

        // Get all the idStoreList where entrytype is null
        defaultIdStoreShouldNotBeFound("entrytype.specified=false");
    }

    @Test
    @Transactional
    void getAllIdStoresByLastGeneratedIdIsEqualToSomething() throws Exception {
        // Initialize the database
        idStoreRepository.saveAndFlush(idStore);

        // Get all the idStoreList where lastGeneratedId equals to DEFAULT_LAST_GENERATED_ID
        defaultIdStoreShouldBeFound("lastGeneratedId.equals=" + DEFAULT_LAST_GENERATED_ID);

        // Get all the idStoreList where lastGeneratedId equals to UPDATED_LAST_GENERATED_ID
        defaultIdStoreShouldNotBeFound("lastGeneratedId.equals=" + UPDATED_LAST_GENERATED_ID);
    }

    @Test
    @Transactional
    void getAllIdStoresByLastGeneratedIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        idStoreRepository.saveAndFlush(idStore);

        // Get all the idStoreList where lastGeneratedId not equals to DEFAULT_LAST_GENERATED_ID
        defaultIdStoreShouldNotBeFound("lastGeneratedId.notEquals=" + DEFAULT_LAST_GENERATED_ID);

        // Get all the idStoreList where lastGeneratedId not equals to UPDATED_LAST_GENERATED_ID
        defaultIdStoreShouldBeFound("lastGeneratedId.notEquals=" + UPDATED_LAST_GENERATED_ID);
    }

    @Test
    @Transactional
    void getAllIdStoresByLastGeneratedIdIsInShouldWork() throws Exception {
        // Initialize the database
        idStoreRepository.saveAndFlush(idStore);

        // Get all the idStoreList where lastGeneratedId in DEFAULT_LAST_GENERATED_ID or UPDATED_LAST_GENERATED_ID
        defaultIdStoreShouldBeFound("lastGeneratedId.in=" + DEFAULT_LAST_GENERATED_ID + "," + UPDATED_LAST_GENERATED_ID);

        // Get all the idStoreList where lastGeneratedId equals to UPDATED_LAST_GENERATED_ID
        defaultIdStoreShouldNotBeFound("lastGeneratedId.in=" + UPDATED_LAST_GENERATED_ID);
    }

    @Test
    @Transactional
    void getAllIdStoresByLastGeneratedIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        idStoreRepository.saveAndFlush(idStore);

        // Get all the idStoreList where lastGeneratedId is not null
        defaultIdStoreShouldBeFound("lastGeneratedId.specified=true");

        // Get all the idStoreList where lastGeneratedId is null
        defaultIdStoreShouldNotBeFound("lastGeneratedId.specified=false");
    }

    @Test
    @Transactional
    void getAllIdStoresByLastGeneratedIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        idStoreRepository.saveAndFlush(idStore);

        // Get all the idStoreList where lastGeneratedId is greater than or equal to DEFAULT_LAST_GENERATED_ID
        defaultIdStoreShouldBeFound("lastGeneratedId.greaterThanOrEqual=" + DEFAULT_LAST_GENERATED_ID);

        // Get all the idStoreList where lastGeneratedId is greater than or equal to UPDATED_LAST_GENERATED_ID
        defaultIdStoreShouldNotBeFound("lastGeneratedId.greaterThanOrEqual=" + UPDATED_LAST_GENERATED_ID);
    }

    @Test
    @Transactional
    void getAllIdStoresByLastGeneratedIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        idStoreRepository.saveAndFlush(idStore);

        // Get all the idStoreList where lastGeneratedId is less than or equal to DEFAULT_LAST_GENERATED_ID
        defaultIdStoreShouldBeFound("lastGeneratedId.lessThanOrEqual=" + DEFAULT_LAST_GENERATED_ID);

        // Get all the idStoreList where lastGeneratedId is less than or equal to SMALLER_LAST_GENERATED_ID
        defaultIdStoreShouldNotBeFound("lastGeneratedId.lessThanOrEqual=" + SMALLER_LAST_GENERATED_ID);
    }

    @Test
    @Transactional
    void getAllIdStoresByLastGeneratedIdIsLessThanSomething() throws Exception {
        // Initialize the database
        idStoreRepository.saveAndFlush(idStore);

        // Get all the idStoreList where lastGeneratedId is less than DEFAULT_LAST_GENERATED_ID
        defaultIdStoreShouldNotBeFound("lastGeneratedId.lessThan=" + DEFAULT_LAST_GENERATED_ID);

        // Get all the idStoreList where lastGeneratedId is less than UPDATED_LAST_GENERATED_ID
        defaultIdStoreShouldBeFound("lastGeneratedId.lessThan=" + UPDATED_LAST_GENERATED_ID);
    }

    @Test
    @Transactional
    void getAllIdStoresByLastGeneratedIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        idStoreRepository.saveAndFlush(idStore);

        // Get all the idStoreList where lastGeneratedId is greater than DEFAULT_LAST_GENERATED_ID
        defaultIdStoreShouldNotBeFound("lastGeneratedId.greaterThan=" + DEFAULT_LAST_GENERATED_ID);

        // Get all the idStoreList where lastGeneratedId is greater than SMALLER_LAST_GENERATED_ID
        defaultIdStoreShouldBeFound("lastGeneratedId.greaterThan=" + SMALLER_LAST_GENERATED_ID);
    }

    @Test
    @Transactional
    void getAllIdStoresByStartIdIsEqualToSomething() throws Exception {
        // Initialize the database
        idStoreRepository.saveAndFlush(idStore);

        // Get all the idStoreList where startId equals to DEFAULT_START_ID
        defaultIdStoreShouldBeFound("startId.equals=" + DEFAULT_START_ID);

        // Get all the idStoreList where startId equals to UPDATED_START_ID
        defaultIdStoreShouldNotBeFound("startId.equals=" + UPDATED_START_ID);
    }

    @Test
    @Transactional
    void getAllIdStoresByStartIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        idStoreRepository.saveAndFlush(idStore);

        // Get all the idStoreList where startId not equals to DEFAULT_START_ID
        defaultIdStoreShouldNotBeFound("startId.notEquals=" + DEFAULT_START_ID);

        // Get all the idStoreList where startId not equals to UPDATED_START_ID
        defaultIdStoreShouldBeFound("startId.notEquals=" + UPDATED_START_ID);
    }

    @Test
    @Transactional
    void getAllIdStoresByStartIdIsInShouldWork() throws Exception {
        // Initialize the database
        idStoreRepository.saveAndFlush(idStore);

        // Get all the idStoreList where startId in DEFAULT_START_ID or UPDATED_START_ID
        defaultIdStoreShouldBeFound("startId.in=" + DEFAULT_START_ID + "," + UPDATED_START_ID);

        // Get all the idStoreList where startId equals to UPDATED_START_ID
        defaultIdStoreShouldNotBeFound("startId.in=" + UPDATED_START_ID);
    }

    @Test
    @Transactional
    void getAllIdStoresByStartIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        idStoreRepository.saveAndFlush(idStore);

        // Get all the idStoreList where startId is not null
        defaultIdStoreShouldBeFound("startId.specified=true");

        // Get all the idStoreList where startId is null
        defaultIdStoreShouldNotBeFound("startId.specified=false");
    }

    @Test
    @Transactional
    void getAllIdStoresByStartIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        idStoreRepository.saveAndFlush(idStore);

        // Get all the idStoreList where startId is greater than or equal to DEFAULT_START_ID
        defaultIdStoreShouldBeFound("startId.greaterThanOrEqual=" + DEFAULT_START_ID);

        // Get all the idStoreList where startId is greater than or equal to UPDATED_START_ID
        defaultIdStoreShouldNotBeFound("startId.greaterThanOrEqual=" + UPDATED_START_ID);
    }

    @Test
    @Transactional
    void getAllIdStoresByStartIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        idStoreRepository.saveAndFlush(idStore);

        // Get all the idStoreList where startId is less than or equal to DEFAULT_START_ID
        defaultIdStoreShouldBeFound("startId.lessThanOrEqual=" + DEFAULT_START_ID);

        // Get all the idStoreList where startId is less than or equal to SMALLER_START_ID
        defaultIdStoreShouldNotBeFound("startId.lessThanOrEqual=" + SMALLER_START_ID);
    }

    @Test
    @Transactional
    void getAllIdStoresByStartIdIsLessThanSomething() throws Exception {
        // Initialize the database
        idStoreRepository.saveAndFlush(idStore);

        // Get all the idStoreList where startId is less than DEFAULT_START_ID
        defaultIdStoreShouldNotBeFound("startId.lessThan=" + DEFAULT_START_ID);

        // Get all the idStoreList where startId is less than UPDATED_START_ID
        defaultIdStoreShouldBeFound("startId.lessThan=" + UPDATED_START_ID);
    }

    @Test
    @Transactional
    void getAllIdStoresByStartIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        idStoreRepository.saveAndFlush(idStore);

        // Get all the idStoreList where startId is greater than DEFAULT_START_ID
        defaultIdStoreShouldNotBeFound("startId.greaterThan=" + DEFAULT_START_ID);

        // Get all the idStoreList where startId is greater than SMALLER_START_ID
        defaultIdStoreShouldBeFound("startId.greaterThan=" + SMALLER_START_ID);
    }

    @Test
    @Transactional
    void getAllIdStoresByCreateDateIsEqualToSomething() throws Exception {
        // Initialize the database
        idStoreRepository.saveAndFlush(idStore);

        // Get all the idStoreList where createDate equals to DEFAULT_CREATE_DATE
        defaultIdStoreShouldBeFound("createDate.equals=" + DEFAULT_CREATE_DATE);

        // Get all the idStoreList where createDate equals to UPDATED_CREATE_DATE
        defaultIdStoreShouldNotBeFound("createDate.equals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllIdStoresByCreateDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        idStoreRepository.saveAndFlush(idStore);

        // Get all the idStoreList where createDate not equals to DEFAULT_CREATE_DATE
        defaultIdStoreShouldNotBeFound("createDate.notEquals=" + DEFAULT_CREATE_DATE);

        // Get all the idStoreList where createDate not equals to UPDATED_CREATE_DATE
        defaultIdStoreShouldBeFound("createDate.notEquals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllIdStoresByCreateDateIsInShouldWork() throws Exception {
        // Initialize the database
        idStoreRepository.saveAndFlush(idStore);

        // Get all the idStoreList where createDate in DEFAULT_CREATE_DATE or UPDATED_CREATE_DATE
        defaultIdStoreShouldBeFound("createDate.in=" + DEFAULT_CREATE_DATE + "," + UPDATED_CREATE_DATE);

        // Get all the idStoreList where createDate equals to UPDATED_CREATE_DATE
        defaultIdStoreShouldNotBeFound("createDate.in=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllIdStoresByCreateDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        idStoreRepository.saveAndFlush(idStore);

        // Get all the idStoreList where createDate is not null
        defaultIdStoreShouldBeFound("createDate.specified=true");

        // Get all the idStoreList where createDate is null
        defaultIdStoreShouldNotBeFound("createDate.specified=false");
    }

    @Test
    @Transactional
    void getAllIdStoresByCreateDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        idStoreRepository.saveAndFlush(idStore);

        // Get all the idStoreList where createDate is greater than or equal to DEFAULT_CREATE_DATE
        defaultIdStoreShouldBeFound("createDate.greaterThanOrEqual=" + DEFAULT_CREATE_DATE);

        // Get all the idStoreList where createDate is greater than or equal to UPDATED_CREATE_DATE
        defaultIdStoreShouldNotBeFound("createDate.greaterThanOrEqual=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllIdStoresByCreateDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        idStoreRepository.saveAndFlush(idStore);

        // Get all the idStoreList where createDate is less than or equal to DEFAULT_CREATE_DATE
        defaultIdStoreShouldBeFound("createDate.lessThanOrEqual=" + DEFAULT_CREATE_DATE);

        // Get all the idStoreList where createDate is less than or equal to SMALLER_CREATE_DATE
        defaultIdStoreShouldNotBeFound("createDate.lessThanOrEqual=" + SMALLER_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllIdStoresByCreateDateIsLessThanSomething() throws Exception {
        // Initialize the database
        idStoreRepository.saveAndFlush(idStore);

        // Get all the idStoreList where createDate is less than DEFAULT_CREATE_DATE
        defaultIdStoreShouldNotBeFound("createDate.lessThan=" + DEFAULT_CREATE_DATE);

        // Get all the idStoreList where createDate is less than UPDATED_CREATE_DATE
        defaultIdStoreShouldBeFound("createDate.lessThan=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllIdStoresByCreateDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        idStoreRepository.saveAndFlush(idStore);

        // Get all the idStoreList where createDate is greater than DEFAULT_CREATE_DATE
        defaultIdStoreShouldNotBeFound("createDate.greaterThan=" + DEFAULT_CREATE_DATE);

        // Get all the idStoreList where createDate is greater than SMALLER_CREATE_DATE
        defaultIdStoreShouldBeFound("createDate.greaterThan=" + SMALLER_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllIdStoresByLastModifiedIsEqualToSomething() throws Exception {
        // Initialize the database
        idStoreRepository.saveAndFlush(idStore);

        // Get all the idStoreList where lastModified equals to DEFAULT_LAST_MODIFIED
        defaultIdStoreShouldBeFound("lastModified.equals=" + DEFAULT_LAST_MODIFIED);

        // Get all the idStoreList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultIdStoreShouldNotBeFound("lastModified.equals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllIdStoresByLastModifiedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        idStoreRepository.saveAndFlush(idStore);

        // Get all the idStoreList where lastModified not equals to DEFAULT_LAST_MODIFIED
        defaultIdStoreShouldNotBeFound("lastModified.notEquals=" + DEFAULT_LAST_MODIFIED);

        // Get all the idStoreList where lastModified not equals to UPDATED_LAST_MODIFIED
        defaultIdStoreShouldBeFound("lastModified.notEquals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllIdStoresByLastModifiedIsInShouldWork() throws Exception {
        // Initialize the database
        idStoreRepository.saveAndFlush(idStore);

        // Get all the idStoreList where lastModified in DEFAULT_LAST_MODIFIED or UPDATED_LAST_MODIFIED
        defaultIdStoreShouldBeFound("lastModified.in=" + DEFAULT_LAST_MODIFIED + "," + UPDATED_LAST_MODIFIED);

        // Get all the idStoreList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultIdStoreShouldNotBeFound("lastModified.in=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllIdStoresByLastModifiedIsNullOrNotNull() throws Exception {
        // Initialize the database
        idStoreRepository.saveAndFlush(idStore);

        // Get all the idStoreList where lastModified is not null
        defaultIdStoreShouldBeFound("lastModified.specified=true");

        // Get all the idStoreList where lastModified is null
        defaultIdStoreShouldNotBeFound("lastModified.specified=false");
    }

    @Test
    @Transactional
    void getAllIdStoresByLastModifiedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        idStoreRepository.saveAndFlush(idStore);

        // Get all the idStoreList where lastModified is greater than or equal to DEFAULT_LAST_MODIFIED
        defaultIdStoreShouldBeFound("lastModified.greaterThanOrEqual=" + DEFAULT_LAST_MODIFIED);

        // Get all the idStoreList where lastModified is greater than or equal to UPDATED_LAST_MODIFIED
        defaultIdStoreShouldNotBeFound("lastModified.greaterThanOrEqual=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllIdStoresByLastModifiedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        idStoreRepository.saveAndFlush(idStore);

        // Get all the idStoreList where lastModified is less than or equal to DEFAULT_LAST_MODIFIED
        defaultIdStoreShouldBeFound("lastModified.lessThanOrEqual=" + DEFAULT_LAST_MODIFIED);

        // Get all the idStoreList where lastModified is less than or equal to SMALLER_LAST_MODIFIED
        defaultIdStoreShouldNotBeFound("lastModified.lessThanOrEqual=" + SMALLER_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllIdStoresByLastModifiedIsLessThanSomething() throws Exception {
        // Initialize the database
        idStoreRepository.saveAndFlush(idStore);

        // Get all the idStoreList where lastModified is less than DEFAULT_LAST_MODIFIED
        defaultIdStoreShouldNotBeFound("lastModified.lessThan=" + DEFAULT_LAST_MODIFIED);

        // Get all the idStoreList where lastModified is less than UPDATED_LAST_MODIFIED
        defaultIdStoreShouldBeFound("lastModified.lessThan=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllIdStoresByLastModifiedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        idStoreRepository.saveAndFlush(idStore);

        // Get all the idStoreList where lastModified is greater than DEFAULT_LAST_MODIFIED
        defaultIdStoreShouldNotBeFound("lastModified.greaterThan=" + DEFAULT_LAST_MODIFIED);

        // Get all the idStoreList where lastModified is greater than SMALLER_LAST_MODIFIED
        defaultIdStoreShouldBeFound("lastModified.greaterThan=" + SMALLER_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllIdStoresByCancelDateIsEqualToSomething() throws Exception {
        // Initialize the database
        idStoreRepository.saveAndFlush(idStore);

        // Get all the idStoreList where cancelDate equals to DEFAULT_CANCEL_DATE
        defaultIdStoreShouldBeFound("cancelDate.equals=" + DEFAULT_CANCEL_DATE);

        // Get all the idStoreList where cancelDate equals to UPDATED_CANCEL_DATE
        defaultIdStoreShouldNotBeFound("cancelDate.equals=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllIdStoresByCancelDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        idStoreRepository.saveAndFlush(idStore);

        // Get all the idStoreList where cancelDate not equals to DEFAULT_CANCEL_DATE
        defaultIdStoreShouldNotBeFound("cancelDate.notEquals=" + DEFAULT_CANCEL_DATE);

        // Get all the idStoreList where cancelDate not equals to UPDATED_CANCEL_DATE
        defaultIdStoreShouldBeFound("cancelDate.notEquals=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllIdStoresByCancelDateIsInShouldWork() throws Exception {
        // Initialize the database
        idStoreRepository.saveAndFlush(idStore);

        // Get all the idStoreList where cancelDate in DEFAULT_CANCEL_DATE or UPDATED_CANCEL_DATE
        defaultIdStoreShouldBeFound("cancelDate.in=" + DEFAULT_CANCEL_DATE + "," + UPDATED_CANCEL_DATE);

        // Get all the idStoreList where cancelDate equals to UPDATED_CANCEL_DATE
        defaultIdStoreShouldNotBeFound("cancelDate.in=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllIdStoresByCancelDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        idStoreRepository.saveAndFlush(idStore);

        // Get all the idStoreList where cancelDate is not null
        defaultIdStoreShouldBeFound("cancelDate.specified=true");

        // Get all the idStoreList where cancelDate is null
        defaultIdStoreShouldNotBeFound("cancelDate.specified=false");
    }

    @Test
    @Transactional
    void getAllIdStoresByCancelDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        idStoreRepository.saveAndFlush(idStore);

        // Get all the idStoreList where cancelDate is greater than or equal to DEFAULT_CANCEL_DATE
        defaultIdStoreShouldBeFound("cancelDate.greaterThanOrEqual=" + DEFAULT_CANCEL_DATE);

        // Get all the idStoreList where cancelDate is greater than or equal to UPDATED_CANCEL_DATE
        defaultIdStoreShouldNotBeFound("cancelDate.greaterThanOrEqual=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllIdStoresByCancelDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        idStoreRepository.saveAndFlush(idStore);

        // Get all the idStoreList where cancelDate is less than or equal to DEFAULT_CANCEL_DATE
        defaultIdStoreShouldBeFound("cancelDate.lessThanOrEqual=" + DEFAULT_CANCEL_DATE);

        // Get all the idStoreList where cancelDate is less than or equal to SMALLER_CANCEL_DATE
        defaultIdStoreShouldNotBeFound("cancelDate.lessThanOrEqual=" + SMALLER_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllIdStoresByCancelDateIsLessThanSomething() throws Exception {
        // Initialize the database
        idStoreRepository.saveAndFlush(idStore);

        // Get all the idStoreList where cancelDate is less than DEFAULT_CANCEL_DATE
        defaultIdStoreShouldNotBeFound("cancelDate.lessThan=" + DEFAULT_CANCEL_DATE);

        // Get all the idStoreList where cancelDate is less than UPDATED_CANCEL_DATE
        defaultIdStoreShouldBeFound("cancelDate.lessThan=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllIdStoresByCancelDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        idStoreRepository.saveAndFlush(idStore);

        // Get all the idStoreList where cancelDate is greater than DEFAULT_CANCEL_DATE
        defaultIdStoreShouldNotBeFound("cancelDate.greaterThan=" + DEFAULT_CANCEL_DATE);

        // Get all the idStoreList where cancelDate is greater than SMALLER_CANCEL_DATE
        defaultIdStoreShouldBeFound("cancelDate.greaterThan=" + SMALLER_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllIdStoresBySchoolIsEqualToSomething() throws Exception {
        // Initialize the database
        idStoreRepository.saveAndFlush(idStore);
        School school = SchoolResourceIT.createEntity(em);
        em.persist(school);
        em.flush();
        idStore.setSchool(school);
        idStoreRepository.saveAndFlush(idStore);
        Long schoolId = school.getId();

        // Get all the idStoreList where school equals to schoolId
        defaultIdStoreShouldBeFound("schoolId.equals=" + schoolId);

        // Get all the idStoreList where school equals to (schoolId + 1)
        defaultIdStoreShouldNotBeFound("schoolId.equals=" + (schoolId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultIdStoreShouldBeFound(String filter) throws Exception {
        restIdStoreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(idStore.getId().intValue())))
            .andExpect(jsonPath("$.[*].entrytype").value(hasItem(DEFAULT_ENTRYTYPE.toString())))
            .andExpect(jsonPath("$.[*].lastGeneratedId").value(hasItem(DEFAULT_LAST_GENERATED_ID.intValue())))
            .andExpect(jsonPath("$.[*].startId").value(hasItem(DEFAULT_START_ID.intValue())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].cancelDate").value(hasItem(DEFAULT_CANCEL_DATE.toString())));

        // Check, that the count call also returns 1
        restIdStoreMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultIdStoreShouldNotBeFound(String filter) throws Exception {
        restIdStoreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restIdStoreMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingIdStore() throws Exception {
        // Get the idStore
        restIdStoreMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewIdStore() throws Exception {
        // Initialize the database
        idStoreRepository.saveAndFlush(idStore);

        int databaseSizeBeforeUpdate = idStoreRepository.findAll().size();

        // Update the idStore
        IdStore updatedIdStore = idStoreRepository.findById(idStore.getId()).get();
        // Disconnect from session so that the updates on updatedIdStore are not directly saved in db
        em.detach(updatedIdStore);
        updatedIdStore
            .entrytype(UPDATED_ENTRYTYPE)
            .lastGeneratedId(UPDATED_LAST_GENERATED_ID)
            .startId(UPDATED_START_ID)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);
        IdStoreDTO idStoreDTO = idStoreMapper.toDto(updatedIdStore);

        restIdStoreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, idStoreDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(idStoreDTO))
            )
            .andExpect(status().isOk());

        // Validate the IdStore in the database
        List<IdStore> idStoreList = idStoreRepository.findAll();
        assertThat(idStoreList).hasSize(databaseSizeBeforeUpdate);
        IdStore testIdStore = idStoreList.get(idStoreList.size() - 1);
        assertThat(testIdStore.getEntrytype()).isEqualTo(UPDATED_ENTRYTYPE);
        assertThat(testIdStore.getLastGeneratedId()).isEqualTo(UPDATED_LAST_GENERATED_ID);
        assertThat(testIdStore.getStartId()).isEqualTo(UPDATED_START_ID);
        assertThat(testIdStore.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testIdStore.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testIdStore.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void putNonExistingIdStore() throws Exception {
        int databaseSizeBeforeUpdate = idStoreRepository.findAll().size();
        idStore.setId(count.incrementAndGet());

        // Create the IdStore
        IdStoreDTO idStoreDTO = idStoreMapper.toDto(idStore);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIdStoreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, idStoreDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(idStoreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IdStore in the database
        List<IdStore> idStoreList = idStoreRepository.findAll();
        assertThat(idStoreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIdStore() throws Exception {
        int databaseSizeBeforeUpdate = idStoreRepository.findAll().size();
        idStore.setId(count.incrementAndGet());

        // Create the IdStore
        IdStoreDTO idStoreDTO = idStoreMapper.toDto(idStore);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIdStoreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(idStoreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IdStore in the database
        List<IdStore> idStoreList = idStoreRepository.findAll();
        assertThat(idStoreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIdStore() throws Exception {
        int databaseSizeBeforeUpdate = idStoreRepository.findAll().size();
        idStore.setId(count.incrementAndGet());

        // Create the IdStore
        IdStoreDTO idStoreDTO = idStoreMapper.toDto(idStore);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIdStoreMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(idStoreDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the IdStore in the database
        List<IdStore> idStoreList = idStoreRepository.findAll();
        assertThat(idStoreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateIdStoreWithPatch() throws Exception {
        // Initialize the database
        idStoreRepository.saveAndFlush(idStore);

        int databaseSizeBeforeUpdate = idStoreRepository.findAll().size();

        // Update the idStore using partial update
        IdStore partialUpdatedIdStore = new IdStore();
        partialUpdatedIdStore.setId(idStore.getId());

        partialUpdatedIdStore.createDate(UPDATED_CREATE_DATE).cancelDate(UPDATED_CANCEL_DATE);

        restIdStoreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIdStore.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIdStore))
            )
            .andExpect(status().isOk());

        // Validate the IdStore in the database
        List<IdStore> idStoreList = idStoreRepository.findAll();
        assertThat(idStoreList).hasSize(databaseSizeBeforeUpdate);
        IdStore testIdStore = idStoreList.get(idStoreList.size() - 1);
        assertThat(testIdStore.getEntrytype()).isEqualTo(DEFAULT_ENTRYTYPE);
        assertThat(testIdStore.getLastGeneratedId()).isEqualTo(DEFAULT_LAST_GENERATED_ID);
        assertThat(testIdStore.getStartId()).isEqualTo(DEFAULT_START_ID);
        assertThat(testIdStore.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testIdStore.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
        assertThat(testIdStore.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void fullUpdateIdStoreWithPatch() throws Exception {
        // Initialize the database
        idStoreRepository.saveAndFlush(idStore);

        int databaseSizeBeforeUpdate = idStoreRepository.findAll().size();

        // Update the idStore using partial update
        IdStore partialUpdatedIdStore = new IdStore();
        partialUpdatedIdStore.setId(idStore.getId());

        partialUpdatedIdStore
            .entrytype(UPDATED_ENTRYTYPE)
            .lastGeneratedId(UPDATED_LAST_GENERATED_ID)
            .startId(UPDATED_START_ID)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);

        restIdStoreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIdStore.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIdStore))
            )
            .andExpect(status().isOk());

        // Validate the IdStore in the database
        List<IdStore> idStoreList = idStoreRepository.findAll();
        assertThat(idStoreList).hasSize(databaseSizeBeforeUpdate);
        IdStore testIdStore = idStoreList.get(idStoreList.size() - 1);
        assertThat(testIdStore.getEntrytype()).isEqualTo(UPDATED_ENTRYTYPE);
        assertThat(testIdStore.getLastGeneratedId()).isEqualTo(UPDATED_LAST_GENERATED_ID);
        assertThat(testIdStore.getStartId()).isEqualTo(UPDATED_START_ID);
        assertThat(testIdStore.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testIdStore.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testIdStore.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingIdStore() throws Exception {
        int databaseSizeBeforeUpdate = idStoreRepository.findAll().size();
        idStore.setId(count.incrementAndGet());

        // Create the IdStore
        IdStoreDTO idStoreDTO = idStoreMapper.toDto(idStore);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIdStoreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, idStoreDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(idStoreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IdStore in the database
        List<IdStore> idStoreList = idStoreRepository.findAll();
        assertThat(idStoreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIdStore() throws Exception {
        int databaseSizeBeforeUpdate = idStoreRepository.findAll().size();
        idStore.setId(count.incrementAndGet());

        // Create the IdStore
        IdStoreDTO idStoreDTO = idStoreMapper.toDto(idStore);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIdStoreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(idStoreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IdStore in the database
        List<IdStore> idStoreList = idStoreRepository.findAll();
        assertThat(idStoreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIdStore() throws Exception {
        int databaseSizeBeforeUpdate = idStoreRepository.findAll().size();
        idStore.setId(count.incrementAndGet());

        // Create the IdStore
        IdStoreDTO idStoreDTO = idStoreMapper.toDto(idStore);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIdStoreMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(idStoreDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the IdStore in the database
        List<IdStore> idStoreList = idStoreRepository.findAll();
        assertThat(idStoreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIdStore() throws Exception {
        // Initialize the database
        idStoreRepository.saveAndFlush(idStore);

        int databaseSizeBeforeDelete = idStoreRepository.findAll().size();

        // Delete the idStore
        restIdStoreMockMvc
            .perform(delete(ENTITY_API_URL_ID, idStore.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<IdStore> idStoreList = idStoreRepository.findAll();
        assertThat(idStoreList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
