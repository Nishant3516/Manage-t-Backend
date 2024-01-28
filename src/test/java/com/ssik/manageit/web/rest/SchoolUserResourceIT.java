package com.ssik.manageit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ssik.manageit.IntegrationTest;
import com.ssik.manageit.domain.AuditLog;
import com.ssik.manageit.domain.ClassSubject;
import com.ssik.manageit.domain.SchoolClass;
import com.ssik.manageit.domain.SchoolUser;
import com.ssik.manageit.domain.enumeration.UserType;
import com.ssik.manageit.repository.SchoolUserRepository;
import com.ssik.manageit.service.SchoolUserService;
import com.ssik.manageit.service.criteria.SchoolUserCriteria;
import com.ssik.manageit.service.dto.SchoolUserDTO;
import com.ssik.manageit.service.mapper.SchoolUserMapper;
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
 * Integration tests for the {@link SchoolUserResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SchoolUserResourceIT {

    private static final String DEFAULT_LOGIN_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LOGIN_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final String DEFAULT_USER_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_USER_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_EXTRA_INFO = "AAAAAAAAAA";
    private static final String UPDATED_EXTRA_INFO = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVATED = false;
    private static final Boolean UPDATED_ACTIVATED = true;

    private static final UserType DEFAULT_USER_TYPE = UserType.STUDENT;
    private static final UserType UPDATED_USER_TYPE = UserType.TEACHER;

    private static final LocalDate DEFAULT_CREATE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATE_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CREATE_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_LAST_MODIFIED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_MODIFIED = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_LAST_MODIFIED = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_CANCEL_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CANCEL_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CANCEL_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/school-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SchoolUserRepository schoolUserRepository;

    @Mock
    private SchoolUserRepository schoolUserRepositoryMock;

    @Autowired
    private SchoolUserMapper schoolUserMapper;

    @Mock
    private SchoolUserService schoolUserServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSchoolUserMockMvc;

    private SchoolUser schoolUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SchoolUser createEntity(EntityManager em) {
        SchoolUser schoolUser = new SchoolUser()
            .loginName(DEFAULT_LOGIN_NAME)
            .password(DEFAULT_PASSWORD)
            .userEmail(DEFAULT_USER_EMAIL)
            .extraInfo(DEFAULT_EXTRA_INFO)
            .activated(DEFAULT_ACTIVATED)
            .userType(DEFAULT_USER_TYPE)
            .createDate(DEFAULT_CREATE_DATE)
            .lastModified(DEFAULT_LAST_MODIFIED)
            .cancelDate(DEFAULT_CANCEL_DATE);
        return schoolUser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SchoolUser createUpdatedEntity(EntityManager em) {
        SchoolUser schoolUser = new SchoolUser()
            .loginName(UPDATED_LOGIN_NAME)
            .password(UPDATED_PASSWORD)
            .userEmail(UPDATED_USER_EMAIL)
            .extraInfo(UPDATED_EXTRA_INFO)
            .activated(UPDATED_ACTIVATED)
            .userType(UPDATED_USER_TYPE)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);
        return schoolUser;
    }

    @BeforeEach
    public void initTest() {
        schoolUser = createEntity(em);
    }

    @Test
    @Transactional
    void createSchoolUser() throws Exception {
        int databaseSizeBeforeCreate = schoolUserRepository.findAll().size();
        // Create the SchoolUser
        SchoolUserDTO schoolUserDTO = schoolUserMapper.toDto(schoolUser);
        restSchoolUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(schoolUserDTO)))
            .andExpect(status().isCreated());

        // Validate the SchoolUser in the database
        List<SchoolUser> schoolUserList = schoolUserRepository.findAll();
        assertThat(schoolUserList).hasSize(databaseSizeBeforeCreate + 1);
        SchoolUser testSchoolUser = schoolUserList.get(schoolUserList.size() - 1);
        assertThat(testSchoolUser.getLoginName()).isEqualTo(DEFAULT_LOGIN_NAME);
        assertThat(testSchoolUser.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testSchoolUser.getUserEmail()).isEqualTo(DEFAULT_USER_EMAIL);
        assertThat(testSchoolUser.getExtraInfo()).isEqualTo(DEFAULT_EXTRA_INFO);
        assertThat(testSchoolUser.getActivated()).isEqualTo(DEFAULT_ACTIVATED);
        assertThat(testSchoolUser.getUserType()).isEqualTo(DEFAULT_USER_TYPE);
        assertThat(testSchoolUser.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testSchoolUser.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
        assertThat(testSchoolUser.getCancelDate()).isEqualTo(DEFAULT_CANCEL_DATE);
    }

    @Test
    @Transactional
    void createSchoolUserWithExistingId() throws Exception {
        // Create the SchoolUser with an existing ID
        schoolUser.setId(1L);
        SchoolUserDTO schoolUserDTO = schoolUserMapper.toDto(schoolUser);

        int databaseSizeBeforeCreate = schoolUserRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSchoolUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(schoolUserDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SchoolUser in the database
        List<SchoolUser> schoolUserList = schoolUserRepository.findAll();
        assertThat(schoolUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLoginNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = schoolUserRepository.findAll().size();
        // set the field null
        schoolUser.setLoginName(null);

        // Create the SchoolUser, which fails.
        SchoolUserDTO schoolUserDTO = schoolUserMapper.toDto(schoolUser);

        restSchoolUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(schoolUserDTO)))
            .andExpect(status().isBadRequest());

        List<SchoolUser> schoolUserList = schoolUserRepository.findAll();
        assertThat(schoolUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPasswordIsRequired() throws Exception {
        int databaseSizeBeforeTest = schoolUserRepository.findAll().size();
        // set the field null
        schoolUser.setPassword(null);

        // Create the SchoolUser, which fails.
        SchoolUserDTO schoolUserDTO = schoolUserMapper.toDto(schoolUser);

        restSchoolUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(schoolUserDTO)))
            .andExpect(status().isBadRequest());

        List<SchoolUser> schoolUserList = schoolUserRepository.findAll();
        assertThat(schoolUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUserEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = schoolUserRepository.findAll().size();
        // set the field null
        schoolUser.setUserEmail(null);

        // Create the SchoolUser, which fails.
        SchoolUserDTO schoolUserDTO = schoolUserMapper.toDto(schoolUser);

        restSchoolUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(schoolUserDTO)))
            .andExpect(status().isBadRequest());

        List<SchoolUser> schoolUserList = schoolUserRepository.findAll();
        assertThat(schoolUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSchoolUsers() throws Exception {
        // Initialize the database
        schoolUserRepository.saveAndFlush(schoolUser);

        // Get all the schoolUserList
        restSchoolUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(schoolUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].loginName").value(hasItem(DEFAULT_LOGIN_NAME)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)))
            .andExpect(jsonPath("$.[*].userEmail").value(hasItem(DEFAULT_USER_EMAIL)))
            .andExpect(jsonPath("$.[*].extraInfo").value(hasItem(DEFAULT_EXTRA_INFO)))
            .andExpect(jsonPath("$.[*].activated").value(hasItem(DEFAULT_ACTIVATED.booleanValue())))
            .andExpect(jsonPath("$.[*].userType").value(hasItem(DEFAULT_USER_TYPE.toString())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].cancelDate").value(hasItem(DEFAULT_CANCEL_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSchoolUsersWithEagerRelationshipsIsEnabled() throws Exception {
        when(schoolUserServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSchoolUserMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(schoolUserServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSchoolUsersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(schoolUserServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSchoolUserMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(schoolUserServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getSchoolUser() throws Exception {
        // Initialize the database
        schoolUserRepository.saveAndFlush(schoolUser);

        // Get the schoolUser
        restSchoolUserMockMvc
            .perform(get(ENTITY_API_URL_ID, schoolUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(schoolUser.getId().intValue()))
            .andExpect(jsonPath("$.loginName").value(DEFAULT_LOGIN_NAME))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD))
            .andExpect(jsonPath("$.userEmail").value(DEFAULT_USER_EMAIL))
            .andExpect(jsonPath("$.extraInfo").value(DEFAULT_EXTRA_INFO))
            .andExpect(jsonPath("$.activated").value(DEFAULT_ACTIVATED.booleanValue()))
            .andExpect(jsonPath("$.userType").value(DEFAULT_USER_TYPE.toString()))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.lastModified").value(DEFAULT_LAST_MODIFIED.toString()))
            .andExpect(jsonPath("$.cancelDate").value(DEFAULT_CANCEL_DATE.toString()));
    }

    @Test
    @Transactional
    void getSchoolUsersByIdFiltering() throws Exception {
        // Initialize the database
        schoolUserRepository.saveAndFlush(schoolUser);

        Long id = schoolUser.getId();

        defaultSchoolUserShouldBeFound("id.equals=" + id);
        defaultSchoolUserShouldNotBeFound("id.notEquals=" + id);

        defaultSchoolUserShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSchoolUserShouldNotBeFound("id.greaterThan=" + id);

        defaultSchoolUserShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSchoolUserShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSchoolUsersByLoginNameIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolUserRepository.saveAndFlush(schoolUser);

        // Get all the schoolUserList where loginName equals to DEFAULT_LOGIN_NAME
        defaultSchoolUserShouldBeFound("loginName.equals=" + DEFAULT_LOGIN_NAME);

        // Get all the schoolUserList where loginName equals to UPDATED_LOGIN_NAME
        defaultSchoolUserShouldNotBeFound("loginName.equals=" + UPDATED_LOGIN_NAME);
    }

    @Test
    @Transactional
    void getAllSchoolUsersByLoginNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        schoolUserRepository.saveAndFlush(schoolUser);

        // Get all the schoolUserList where loginName not equals to DEFAULT_LOGIN_NAME
        defaultSchoolUserShouldNotBeFound("loginName.notEquals=" + DEFAULT_LOGIN_NAME);

        // Get all the schoolUserList where loginName not equals to UPDATED_LOGIN_NAME
        defaultSchoolUserShouldBeFound("loginName.notEquals=" + UPDATED_LOGIN_NAME);
    }

    @Test
    @Transactional
    void getAllSchoolUsersByLoginNameIsInShouldWork() throws Exception {
        // Initialize the database
        schoolUserRepository.saveAndFlush(schoolUser);

        // Get all the schoolUserList where loginName in DEFAULT_LOGIN_NAME or UPDATED_LOGIN_NAME
        defaultSchoolUserShouldBeFound("loginName.in=" + DEFAULT_LOGIN_NAME + "," + UPDATED_LOGIN_NAME);

        // Get all the schoolUserList where loginName equals to UPDATED_LOGIN_NAME
        defaultSchoolUserShouldNotBeFound("loginName.in=" + UPDATED_LOGIN_NAME);
    }

    @Test
    @Transactional
    void getAllSchoolUsersByLoginNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolUserRepository.saveAndFlush(schoolUser);

        // Get all the schoolUserList where loginName is not null
        defaultSchoolUserShouldBeFound("loginName.specified=true");

        // Get all the schoolUserList where loginName is null
        defaultSchoolUserShouldNotBeFound("loginName.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolUsersByLoginNameContainsSomething() throws Exception {
        // Initialize the database
        schoolUserRepository.saveAndFlush(schoolUser);

        // Get all the schoolUserList where loginName contains DEFAULT_LOGIN_NAME
        defaultSchoolUserShouldBeFound("loginName.contains=" + DEFAULT_LOGIN_NAME);

        // Get all the schoolUserList where loginName contains UPDATED_LOGIN_NAME
        defaultSchoolUserShouldNotBeFound("loginName.contains=" + UPDATED_LOGIN_NAME);
    }

    @Test
    @Transactional
    void getAllSchoolUsersByLoginNameNotContainsSomething() throws Exception {
        // Initialize the database
        schoolUserRepository.saveAndFlush(schoolUser);

        // Get all the schoolUserList where loginName does not contain DEFAULT_LOGIN_NAME
        defaultSchoolUserShouldNotBeFound("loginName.doesNotContain=" + DEFAULT_LOGIN_NAME);

        // Get all the schoolUserList where loginName does not contain UPDATED_LOGIN_NAME
        defaultSchoolUserShouldBeFound("loginName.doesNotContain=" + UPDATED_LOGIN_NAME);
    }

    @Test
    @Transactional
    void getAllSchoolUsersByPasswordIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolUserRepository.saveAndFlush(schoolUser);

        // Get all the schoolUserList where password equals to DEFAULT_PASSWORD
        defaultSchoolUserShouldBeFound("password.equals=" + DEFAULT_PASSWORD);

        // Get all the schoolUserList where password equals to UPDATED_PASSWORD
        defaultSchoolUserShouldNotBeFound("password.equals=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void getAllSchoolUsersByPasswordIsNotEqualToSomething() throws Exception {
        // Initialize the database
        schoolUserRepository.saveAndFlush(schoolUser);

        // Get all the schoolUserList where password not equals to DEFAULT_PASSWORD
        defaultSchoolUserShouldNotBeFound("password.notEquals=" + DEFAULT_PASSWORD);

        // Get all the schoolUserList where password not equals to UPDATED_PASSWORD
        defaultSchoolUserShouldBeFound("password.notEquals=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void getAllSchoolUsersByPasswordIsInShouldWork() throws Exception {
        // Initialize the database
        schoolUserRepository.saveAndFlush(schoolUser);

        // Get all the schoolUserList where password in DEFAULT_PASSWORD or UPDATED_PASSWORD
        defaultSchoolUserShouldBeFound("password.in=" + DEFAULT_PASSWORD + "," + UPDATED_PASSWORD);

        // Get all the schoolUserList where password equals to UPDATED_PASSWORD
        defaultSchoolUserShouldNotBeFound("password.in=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void getAllSchoolUsersByPasswordIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolUserRepository.saveAndFlush(schoolUser);

        // Get all the schoolUserList where password is not null
        defaultSchoolUserShouldBeFound("password.specified=true");

        // Get all the schoolUserList where password is null
        defaultSchoolUserShouldNotBeFound("password.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolUsersByPasswordContainsSomething() throws Exception {
        // Initialize the database
        schoolUserRepository.saveAndFlush(schoolUser);

        // Get all the schoolUserList where password contains DEFAULT_PASSWORD
        defaultSchoolUserShouldBeFound("password.contains=" + DEFAULT_PASSWORD);

        // Get all the schoolUserList where password contains UPDATED_PASSWORD
        defaultSchoolUserShouldNotBeFound("password.contains=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void getAllSchoolUsersByPasswordNotContainsSomething() throws Exception {
        // Initialize the database
        schoolUserRepository.saveAndFlush(schoolUser);

        // Get all the schoolUserList where password does not contain DEFAULT_PASSWORD
        defaultSchoolUserShouldNotBeFound("password.doesNotContain=" + DEFAULT_PASSWORD);

        // Get all the schoolUserList where password does not contain UPDATED_PASSWORD
        defaultSchoolUserShouldBeFound("password.doesNotContain=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void getAllSchoolUsersByUserEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolUserRepository.saveAndFlush(schoolUser);

        // Get all the schoolUserList where userEmail equals to DEFAULT_USER_EMAIL
        defaultSchoolUserShouldBeFound("userEmail.equals=" + DEFAULT_USER_EMAIL);

        // Get all the schoolUserList where userEmail equals to UPDATED_USER_EMAIL
        defaultSchoolUserShouldNotBeFound("userEmail.equals=" + UPDATED_USER_EMAIL);
    }

    @Test
    @Transactional
    void getAllSchoolUsersByUserEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        schoolUserRepository.saveAndFlush(schoolUser);

        // Get all the schoolUserList where userEmail not equals to DEFAULT_USER_EMAIL
        defaultSchoolUserShouldNotBeFound("userEmail.notEquals=" + DEFAULT_USER_EMAIL);

        // Get all the schoolUserList where userEmail not equals to UPDATED_USER_EMAIL
        defaultSchoolUserShouldBeFound("userEmail.notEquals=" + UPDATED_USER_EMAIL);
    }

    @Test
    @Transactional
    void getAllSchoolUsersByUserEmailIsInShouldWork() throws Exception {
        // Initialize the database
        schoolUserRepository.saveAndFlush(schoolUser);

        // Get all the schoolUserList where userEmail in DEFAULT_USER_EMAIL or UPDATED_USER_EMAIL
        defaultSchoolUserShouldBeFound("userEmail.in=" + DEFAULT_USER_EMAIL + "," + UPDATED_USER_EMAIL);

        // Get all the schoolUserList where userEmail equals to UPDATED_USER_EMAIL
        defaultSchoolUserShouldNotBeFound("userEmail.in=" + UPDATED_USER_EMAIL);
    }

    @Test
    @Transactional
    void getAllSchoolUsersByUserEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolUserRepository.saveAndFlush(schoolUser);

        // Get all the schoolUserList where userEmail is not null
        defaultSchoolUserShouldBeFound("userEmail.specified=true");

        // Get all the schoolUserList where userEmail is null
        defaultSchoolUserShouldNotBeFound("userEmail.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolUsersByUserEmailContainsSomething() throws Exception {
        // Initialize the database
        schoolUserRepository.saveAndFlush(schoolUser);

        // Get all the schoolUserList where userEmail contains DEFAULT_USER_EMAIL
        defaultSchoolUserShouldBeFound("userEmail.contains=" + DEFAULT_USER_EMAIL);

        // Get all the schoolUserList where userEmail contains UPDATED_USER_EMAIL
        defaultSchoolUserShouldNotBeFound("userEmail.contains=" + UPDATED_USER_EMAIL);
    }

    @Test
    @Transactional
    void getAllSchoolUsersByUserEmailNotContainsSomething() throws Exception {
        // Initialize the database
        schoolUserRepository.saveAndFlush(schoolUser);

        // Get all the schoolUserList where userEmail does not contain DEFAULT_USER_EMAIL
        defaultSchoolUserShouldNotBeFound("userEmail.doesNotContain=" + DEFAULT_USER_EMAIL);

        // Get all the schoolUserList where userEmail does not contain UPDATED_USER_EMAIL
        defaultSchoolUserShouldBeFound("userEmail.doesNotContain=" + UPDATED_USER_EMAIL);
    }

    @Test
    @Transactional
    void getAllSchoolUsersByExtraInfoIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolUserRepository.saveAndFlush(schoolUser);

        // Get all the schoolUserList where extraInfo equals to DEFAULT_EXTRA_INFO
        defaultSchoolUserShouldBeFound("extraInfo.equals=" + DEFAULT_EXTRA_INFO);

        // Get all the schoolUserList where extraInfo equals to UPDATED_EXTRA_INFO
        defaultSchoolUserShouldNotBeFound("extraInfo.equals=" + UPDATED_EXTRA_INFO);
    }

    @Test
    @Transactional
    void getAllSchoolUsersByExtraInfoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        schoolUserRepository.saveAndFlush(schoolUser);

        // Get all the schoolUserList where extraInfo not equals to DEFAULT_EXTRA_INFO
        defaultSchoolUserShouldNotBeFound("extraInfo.notEquals=" + DEFAULT_EXTRA_INFO);

        // Get all the schoolUserList where extraInfo not equals to UPDATED_EXTRA_INFO
        defaultSchoolUserShouldBeFound("extraInfo.notEquals=" + UPDATED_EXTRA_INFO);
    }

    @Test
    @Transactional
    void getAllSchoolUsersByExtraInfoIsInShouldWork() throws Exception {
        // Initialize the database
        schoolUserRepository.saveAndFlush(schoolUser);

        // Get all the schoolUserList where extraInfo in DEFAULT_EXTRA_INFO or UPDATED_EXTRA_INFO
        defaultSchoolUserShouldBeFound("extraInfo.in=" + DEFAULT_EXTRA_INFO + "," + UPDATED_EXTRA_INFO);

        // Get all the schoolUserList where extraInfo equals to UPDATED_EXTRA_INFO
        defaultSchoolUserShouldNotBeFound("extraInfo.in=" + UPDATED_EXTRA_INFO);
    }

    @Test
    @Transactional
    void getAllSchoolUsersByExtraInfoIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolUserRepository.saveAndFlush(schoolUser);

        // Get all the schoolUserList where extraInfo is not null
        defaultSchoolUserShouldBeFound("extraInfo.specified=true");

        // Get all the schoolUserList where extraInfo is null
        defaultSchoolUserShouldNotBeFound("extraInfo.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolUsersByExtraInfoContainsSomething() throws Exception {
        // Initialize the database
        schoolUserRepository.saveAndFlush(schoolUser);

        // Get all the schoolUserList where extraInfo contains DEFAULT_EXTRA_INFO
        defaultSchoolUserShouldBeFound("extraInfo.contains=" + DEFAULT_EXTRA_INFO);

        // Get all the schoolUserList where extraInfo contains UPDATED_EXTRA_INFO
        defaultSchoolUserShouldNotBeFound("extraInfo.contains=" + UPDATED_EXTRA_INFO);
    }

    @Test
    @Transactional
    void getAllSchoolUsersByExtraInfoNotContainsSomething() throws Exception {
        // Initialize the database
        schoolUserRepository.saveAndFlush(schoolUser);

        // Get all the schoolUserList where extraInfo does not contain DEFAULT_EXTRA_INFO
        defaultSchoolUserShouldNotBeFound("extraInfo.doesNotContain=" + DEFAULT_EXTRA_INFO);

        // Get all the schoolUserList where extraInfo does not contain UPDATED_EXTRA_INFO
        defaultSchoolUserShouldBeFound("extraInfo.doesNotContain=" + UPDATED_EXTRA_INFO);
    }

    @Test
    @Transactional
    void getAllSchoolUsersByActivatedIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolUserRepository.saveAndFlush(schoolUser);

        // Get all the schoolUserList where activated equals to DEFAULT_ACTIVATED
        defaultSchoolUserShouldBeFound("activated.equals=" + DEFAULT_ACTIVATED);

        // Get all the schoolUserList where activated equals to UPDATED_ACTIVATED
        defaultSchoolUserShouldNotBeFound("activated.equals=" + UPDATED_ACTIVATED);
    }

    @Test
    @Transactional
    void getAllSchoolUsersByActivatedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        schoolUserRepository.saveAndFlush(schoolUser);

        // Get all the schoolUserList where activated not equals to DEFAULT_ACTIVATED
        defaultSchoolUserShouldNotBeFound("activated.notEquals=" + DEFAULT_ACTIVATED);

        // Get all the schoolUserList where activated not equals to UPDATED_ACTIVATED
        defaultSchoolUserShouldBeFound("activated.notEquals=" + UPDATED_ACTIVATED);
    }

    @Test
    @Transactional
    void getAllSchoolUsersByActivatedIsInShouldWork() throws Exception {
        // Initialize the database
        schoolUserRepository.saveAndFlush(schoolUser);

        // Get all the schoolUserList where activated in DEFAULT_ACTIVATED or UPDATED_ACTIVATED
        defaultSchoolUserShouldBeFound("activated.in=" + DEFAULT_ACTIVATED + "," + UPDATED_ACTIVATED);

        // Get all the schoolUserList where activated equals to UPDATED_ACTIVATED
        defaultSchoolUserShouldNotBeFound("activated.in=" + UPDATED_ACTIVATED);
    }

    @Test
    @Transactional
    void getAllSchoolUsersByActivatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolUserRepository.saveAndFlush(schoolUser);

        // Get all the schoolUserList where activated is not null
        defaultSchoolUserShouldBeFound("activated.specified=true");

        // Get all the schoolUserList where activated is null
        defaultSchoolUserShouldNotBeFound("activated.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolUsersByUserTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolUserRepository.saveAndFlush(schoolUser);

        // Get all the schoolUserList where userType equals to DEFAULT_USER_TYPE
        defaultSchoolUserShouldBeFound("userType.equals=" + DEFAULT_USER_TYPE);

        // Get all the schoolUserList where userType equals to UPDATED_USER_TYPE
        defaultSchoolUserShouldNotBeFound("userType.equals=" + UPDATED_USER_TYPE);
    }

    @Test
    @Transactional
    void getAllSchoolUsersByUserTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        schoolUserRepository.saveAndFlush(schoolUser);

        // Get all the schoolUserList where userType not equals to DEFAULT_USER_TYPE
        defaultSchoolUserShouldNotBeFound("userType.notEquals=" + DEFAULT_USER_TYPE);

        // Get all the schoolUserList where userType not equals to UPDATED_USER_TYPE
        defaultSchoolUserShouldBeFound("userType.notEquals=" + UPDATED_USER_TYPE);
    }

    @Test
    @Transactional
    void getAllSchoolUsersByUserTypeIsInShouldWork() throws Exception {
        // Initialize the database
        schoolUserRepository.saveAndFlush(schoolUser);

        // Get all the schoolUserList where userType in DEFAULT_USER_TYPE or UPDATED_USER_TYPE
        defaultSchoolUserShouldBeFound("userType.in=" + DEFAULT_USER_TYPE + "," + UPDATED_USER_TYPE);

        // Get all the schoolUserList where userType equals to UPDATED_USER_TYPE
        defaultSchoolUserShouldNotBeFound("userType.in=" + UPDATED_USER_TYPE);
    }

    @Test
    @Transactional
    void getAllSchoolUsersByUserTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolUserRepository.saveAndFlush(schoolUser);

        // Get all the schoolUserList where userType is not null
        defaultSchoolUserShouldBeFound("userType.specified=true");

        // Get all the schoolUserList where userType is null
        defaultSchoolUserShouldNotBeFound("userType.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolUsersByCreateDateIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolUserRepository.saveAndFlush(schoolUser);

        // Get all the schoolUserList where createDate equals to DEFAULT_CREATE_DATE
        defaultSchoolUserShouldBeFound("createDate.equals=" + DEFAULT_CREATE_DATE);

        // Get all the schoolUserList where createDate equals to UPDATED_CREATE_DATE
        defaultSchoolUserShouldNotBeFound("createDate.equals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolUsersByCreateDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        schoolUserRepository.saveAndFlush(schoolUser);

        // Get all the schoolUserList where createDate not equals to DEFAULT_CREATE_DATE
        defaultSchoolUserShouldNotBeFound("createDate.notEquals=" + DEFAULT_CREATE_DATE);

        // Get all the schoolUserList where createDate not equals to UPDATED_CREATE_DATE
        defaultSchoolUserShouldBeFound("createDate.notEquals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolUsersByCreateDateIsInShouldWork() throws Exception {
        // Initialize the database
        schoolUserRepository.saveAndFlush(schoolUser);

        // Get all the schoolUserList where createDate in DEFAULT_CREATE_DATE or UPDATED_CREATE_DATE
        defaultSchoolUserShouldBeFound("createDate.in=" + DEFAULT_CREATE_DATE + "," + UPDATED_CREATE_DATE);

        // Get all the schoolUserList where createDate equals to UPDATED_CREATE_DATE
        defaultSchoolUserShouldNotBeFound("createDate.in=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolUsersByCreateDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolUserRepository.saveAndFlush(schoolUser);

        // Get all the schoolUserList where createDate is not null
        defaultSchoolUserShouldBeFound("createDate.specified=true");

        // Get all the schoolUserList where createDate is null
        defaultSchoolUserShouldNotBeFound("createDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolUsersByCreateDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        schoolUserRepository.saveAndFlush(schoolUser);

        // Get all the schoolUserList where createDate is greater than or equal to DEFAULT_CREATE_DATE
        defaultSchoolUserShouldBeFound("createDate.greaterThanOrEqual=" + DEFAULT_CREATE_DATE);

        // Get all the schoolUserList where createDate is greater than or equal to UPDATED_CREATE_DATE
        defaultSchoolUserShouldNotBeFound("createDate.greaterThanOrEqual=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolUsersByCreateDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        schoolUserRepository.saveAndFlush(schoolUser);

        // Get all the schoolUserList where createDate is less than or equal to DEFAULT_CREATE_DATE
        defaultSchoolUserShouldBeFound("createDate.lessThanOrEqual=" + DEFAULT_CREATE_DATE);

        // Get all the schoolUserList where createDate is less than or equal to SMALLER_CREATE_DATE
        defaultSchoolUserShouldNotBeFound("createDate.lessThanOrEqual=" + SMALLER_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolUsersByCreateDateIsLessThanSomething() throws Exception {
        // Initialize the database
        schoolUserRepository.saveAndFlush(schoolUser);

        // Get all the schoolUserList where createDate is less than DEFAULT_CREATE_DATE
        defaultSchoolUserShouldNotBeFound("createDate.lessThan=" + DEFAULT_CREATE_DATE);

        // Get all the schoolUserList where createDate is less than UPDATED_CREATE_DATE
        defaultSchoolUserShouldBeFound("createDate.lessThan=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolUsersByCreateDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        schoolUserRepository.saveAndFlush(schoolUser);

        // Get all the schoolUserList where createDate is greater than DEFAULT_CREATE_DATE
        defaultSchoolUserShouldNotBeFound("createDate.greaterThan=" + DEFAULT_CREATE_DATE);

        // Get all the schoolUserList where createDate is greater than SMALLER_CREATE_DATE
        defaultSchoolUserShouldBeFound("createDate.greaterThan=" + SMALLER_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolUsersByLastModifiedIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolUserRepository.saveAndFlush(schoolUser);

        // Get all the schoolUserList where lastModified equals to DEFAULT_LAST_MODIFIED
        defaultSchoolUserShouldBeFound("lastModified.equals=" + DEFAULT_LAST_MODIFIED);

        // Get all the schoolUserList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultSchoolUserShouldNotBeFound("lastModified.equals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSchoolUsersByLastModifiedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        schoolUserRepository.saveAndFlush(schoolUser);

        // Get all the schoolUserList where lastModified not equals to DEFAULT_LAST_MODIFIED
        defaultSchoolUserShouldNotBeFound("lastModified.notEquals=" + DEFAULT_LAST_MODIFIED);

        // Get all the schoolUserList where lastModified not equals to UPDATED_LAST_MODIFIED
        defaultSchoolUserShouldBeFound("lastModified.notEquals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSchoolUsersByLastModifiedIsInShouldWork() throws Exception {
        // Initialize the database
        schoolUserRepository.saveAndFlush(schoolUser);

        // Get all the schoolUserList where lastModified in DEFAULT_LAST_MODIFIED or UPDATED_LAST_MODIFIED
        defaultSchoolUserShouldBeFound("lastModified.in=" + DEFAULT_LAST_MODIFIED + "," + UPDATED_LAST_MODIFIED);

        // Get all the schoolUserList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultSchoolUserShouldNotBeFound("lastModified.in=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSchoolUsersByLastModifiedIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolUserRepository.saveAndFlush(schoolUser);

        // Get all the schoolUserList where lastModified is not null
        defaultSchoolUserShouldBeFound("lastModified.specified=true");

        // Get all the schoolUserList where lastModified is null
        defaultSchoolUserShouldNotBeFound("lastModified.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolUsersByLastModifiedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        schoolUserRepository.saveAndFlush(schoolUser);

        // Get all the schoolUserList where lastModified is greater than or equal to DEFAULT_LAST_MODIFIED
        defaultSchoolUserShouldBeFound("lastModified.greaterThanOrEqual=" + DEFAULT_LAST_MODIFIED);

        // Get all the schoolUserList where lastModified is greater than or equal to UPDATED_LAST_MODIFIED
        defaultSchoolUserShouldNotBeFound("lastModified.greaterThanOrEqual=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSchoolUsersByLastModifiedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        schoolUserRepository.saveAndFlush(schoolUser);

        // Get all the schoolUserList where lastModified is less than or equal to DEFAULT_LAST_MODIFIED
        defaultSchoolUserShouldBeFound("lastModified.lessThanOrEqual=" + DEFAULT_LAST_MODIFIED);

        // Get all the schoolUserList where lastModified is less than or equal to SMALLER_LAST_MODIFIED
        defaultSchoolUserShouldNotBeFound("lastModified.lessThanOrEqual=" + SMALLER_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSchoolUsersByLastModifiedIsLessThanSomething() throws Exception {
        // Initialize the database
        schoolUserRepository.saveAndFlush(schoolUser);

        // Get all the schoolUserList where lastModified is less than DEFAULT_LAST_MODIFIED
        defaultSchoolUserShouldNotBeFound("lastModified.lessThan=" + DEFAULT_LAST_MODIFIED);

        // Get all the schoolUserList where lastModified is less than UPDATED_LAST_MODIFIED
        defaultSchoolUserShouldBeFound("lastModified.lessThan=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSchoolUsersByLastModifiedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        schoolUserRepository.saveAndFlush(schoolUser);

        // Get all the schoolUserList where lastModified is greater than DEFAULT_LAST_MODIFIED
        defaultSchoolUserShouldNotBeFound("lastModified.greaterThan=" + DEFAULT_LAST_MODIFIED);

        // Get all the schoolUserList where lastModified is greater than SMALLER_LAST_MODIFIED
        defaultSchoolUserShouldBeFound("lastModified.greaterThan=" + SMALLER_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSchoolUsersByCancelDateIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolUserRepository.saveAndFlush(schoolUser);

        // Get all the schoolUserList where cancelDate equals to DEFAULT_CANCEL_DATE
        defaultSchoolUserShouldBeFound("cancelDate.equals=" + DEFAULT_CANCEL_DATE);

        // Get all the schoolUserList where cancelDate equals to UPDATED_CANCEL_DATE
        defaultSchoolUserShouldNotBeFound("cancelDate.equals=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolUsersByCancelDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        schoolUserRepository.saveAndFlush(schoolUser);

        // Get all the schoolUserList where cancelDate not equals to DEFAULT_CANCEL_DATE
        defaultSchoolUserShouldNotBeFound("cancelDate.notEquals=" + DEFAULT_CANCEL_DATE);

        // Get all the schoolUserList where cancelDate not equals to UPDATED_CANCEL_DATE
        defaultSchoolUserShouldBeFound("cancelDate.notEquals=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolUsersByCancelDateIsInShouldWork() throws Exception {
        // Initialize the database
        schoolUserRepository.saveAndFlush(schoolUser);

        // Get all the schoolUserList where cancelDate in DEFAULT_CANCEL_DATE or UPDATED_CANCEL_DATE
        defaultSchoolUserShouldBeFound("cancelDate.in=" + DEFAULT_CANCEL_DATE + "," + UPDATED_CANCEL_DATE);

        // Get all the schoolUserList where cancelDate equals to UPDATED_CANCEL_DATE
        defaultSchoolUserShouldNotBeFound("cancelDate.in=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolUsersByCancelDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolUserRepository.saveAndFlush(schoolUser);

        // Get all the schoolUserList where cancelDate is not null
        defaultSchoolUserShouldBeFound("cancelDate.specified=true");

        // Get all the schoolUserList where cancelDate is null
        defaultSchoolUserShouldNotBeFound("cancelDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolUsersByCancelDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        schoolUserRepository.saveAndFlush(schoolUser);

        // Get all the schoolUserList where cancelDate is greater than or equal to DEFAULT_CANCEL_DATE
        defaultSchoolUserShouldBeFound("cancelDate.greaterThanOrEqual=" + DEFAULT_CANCEL_DATE);

        // Get all the schoolUserList where cancelDate is greater than or equal to UPDATED_CANCEL_DATE
        defaultSchoolUserShouldNotBeFound("cancelDate.greaterThanOrEqual=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolUsersByCancelDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        schoolUserRepository.saveAndFlush(schoolUser);

        // Get all the schoolUserList where cancelDate is less than or equal to DEFAULT_CANCEL_DATE
        defaultSchoolUserShouldBeFound("cancelDate.lessThanOrEqual=" + DEFAULT_CANCEL_DATE);

        // Get all the schoolUserList where cancelDate is less than or equal to SMALLER_CANCEL_DATE
        defaultSchoolUserShouldNotBeFound("cancelDate.lessThanOrEqual=" + SMALLER_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolUsersByCancelDateIsLessThanSomething() throws Exception {
        // Initialize the database
        schoolUserRepository.saveAndFlush(schoolUser);

        // Get all the schoolUserList where cancelDate is less than DEFAULT_CANCEL_DATE
        defaultSchoolUserShouldNotBeFound("cancelDate.lessThan=" + DEFAULT_CANCEL_DATE);

        // Get all the schoolUserList where cancelDate is less than UPDATED_CANCEL_DATE
        defaultSchoolUserShouldBeFound("cancelDate.lessThan=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolUsersByCancelDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        schoolUserRepository.saveAndFlush(schoolUser);

        // Get all the schoolUserList where cancelDate is greater than DEFAULT_CANCEL_DATE
        defaultSchoolUserShouldNotBeFound("cancelDate.greaterThan=" + DEFAULT_CANCEL_DATE);

        // Get all the schoolUserList where cancelDate is greater than SMALLER_CANCEL_DATE
        defaultSchoolUserShouldBeFound("cancelDate.greaterThan=" + SMALLER_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSchoolUsersByAuditLogIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolUserRepository.saveAndFlush(schoolUser);
        AuditLog auditLog = AuditLogResourceIT.createEntity(em);
        em.persist(auditLog);
        em.flush();
        schoolUser.addAuditLog(auditLog);
        schoolUserRepository.saveAndFlush(schoolUser);
        Long auditLogId = auditLog.getId();

        // Get all the schoolUserList where auditLog equals to auditLogId
        defaultSchoolUserShouldBeFound("auditLogId.equals=" + auditLogId);

        // Get all the schoolUserList where auditLog equals to (auditLogId + 1)
        defaultSchoolUserShouldNotBeFound("auditLogId.equals=" + (auditLogId + 1));
    }

    @Test
    @Transactional
    void getAllSchoolUsersBySchoolClassIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolUserRepository.saveAndFlush(schoolUser);
        SchoolClass schoolClass = SchoolClassResourceIT.createEntity(em);
        em.persist(schoolClass);
        em.flush();
        schoolUser.addSchoolClass(schoolClass);
        schoolUserRepository.saveAndFlush(schoolUser);
        Long schoolClassId = schoolClass.getId();

        // Get all the schoolUserList where schoolClass equals to schoolClassId
        defaultSchoolUserShouldBeFound("schoolClassId.equals=" + schoolClassId);

        // Get all the schoolUserList where schoolClass equals to (schoolClassId + 1)
        defaultSchoolUserShouldNotBeFound("schoolClassId.equals=" + (schoolClassId + 1));
    }

    @Test
    @Transactional
    void getAllSchoolUsersByClassSubjectIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolUserRepository.saveAndFlush(schoolUser);
        ClassSubject classSubject = ClassSubjectResourceIT.createEntity(em);
        em.persist(classSubject);
        em.flush();
        schoolUser.addClassSubject(classSubject);
        schoolUserRepository.saveAndFlush(schoolUser);
        Long classSubjectId = classSubject.getId();

        // Get all the schoolUserList where classSubject equals to classSubjectId
        defaultSchoolUserShouldBeFound("classSubjectId.equals=" + classSubjectId);

        // Get all the schoolUserList where classSubject equals to (classSubjectId + 1)
        defaultSchoolUserShouldNotBeFound("classSubjectId.equals=" + (classSubjectId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSchoolUserShouldBeFound(String filter) throws Exception {
        restSchoolUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(schoolUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].loginName").value(hasItem(DEFAULT_LOGIN_NAME)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)))
            .andExpect(jsonPath("$.[*].userEmail").value(hasItem(DEFAULT_USER_EMAIL)))
            .andExpect(jsonPath("$.[*].extraInfo").value(hasItem(DEFAULT_EXTRA_INFO)))
            .andExpect(jsonPath("$.[*].activated").value(hasItem(DEFAULT_ACTIVATED.booleanValue())))
            .andExpect(jsonPath("$.[*].userType").value(hasItem(DEFAULT_USER_TYPE.toString())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].cancelDate").value(hasItem(DEFAULT_CANCEL_DATE.toString())));

        // Check, that the count call also returns 1
        restSchoolUserMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSchoolUserShouldNotBeFound(String filter) throws Exception {
        restSchoolUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSchoolUserMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSchoolUser() throws Exception {
        // Get the schoolUser
        restSchoolUserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSchoolUser() throws Exception {
        // Initialize the database
        schoolUserRepository.saveAndFlush(schoolUser);

        int databaseSizeBeforeUpdate = schoolUserRepository.findAll().size();

        // Update the schoolUser
        SchoolUser updatedSchoolUser = schoolUserRepository.findById(schoolUser.getId()).get();
        // Disconnect from session so that the updates on updatedSchoolUser are not directly saved in db
        em.detach(updatedSchoolUser);
        updatedSchoolUser
            .loginName(UPDATED_LOGIN_NAME)
            .password(UPDATED_PASSWORD)
            .userEmail(UPDATED_USER_EMAIL)
            .extraInfo(UPDATED_EXTRA_INFO)
            .activated(UPDATED_ACTIVATED)
            .userType(UPDATED_USER_TYPE)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);
        SchoolUserDTO schoolUserDTO = schoolUserMapper.toDto(updatedSchoolUser);

        restSchoolUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, schoolUserDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schoolUserDTO))
            )
            .andExpect(status().isOk());

        // Validate the SchoolUser in the database
        List<SchoolUser> schoolUserList = schoolUserRepository.findAll();
        assertThat(schoolUserList).hasSize(databaseSizeBeforeUpdate);
        SchoolUser testSchoolUser = schoolUserList.get(schoolUserList.size() - 1);
        assertThat(testSchoolUser.getLoginName()).isEqualTo(UPDATED_LOGIN_NAME);
        assertThat(testSchoolUser.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testSchoolUser.getUserEmail()).isEqualTo(UPDATED_USER_EMAIL);
        assertThat(testSchoolUser.getExtraInfo()).isEqualTo(UPDATED_EXTRA_INFO);
        assertThat(testSchoolUser.getActivated()).isEqualTo(UPDATED_ACTIVATED);
        assertThat(testSchoolUser.getUserType()).isEqualTo(UPDATED_USER_TYPE);
        assertThat(testSchoolUser.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testSchoolUser.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testSchoolUser.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void putNonExistingSchoolUser() throws Exception {
        int databaseSizeBeforeUpdate = schoolUserRepository.findAll().size();
        schoolUser.setId(count.incrementAndGet());

        // Create the SchoolUser
        SchoolUserDTO schoolUserDTO = schoolUserMapper.toDto(schoolUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSchoolUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, schoolUserDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schoolUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchoolUser in the database
        List<SchoolUser> schoolUserList = schoolUserRepository.findAll();
        assertThat(schoolUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSchoolUser() throws Exception {
        int databaseSizeBeforeUpdate = schoolUserRepository.findAll().size();
        schoolUser.setId(count.incrementAndGet());

        // Create the SchoolUser
        SchoolUserDTO schoolUserDTO = schoolUserMapper.toDto(schoolUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchoolUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schoolUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchoolUser in the database
        List<SchoolUser> schoolUserList = schoolUserRepository.findAll();
        assertThat(schoolUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSchoolUser() throws Exception {
        int databaseSizeBeforeUpdate = schoolUserRepository.findAll().size();
        schoolUser.setId(count.incrementAndGet());

        // Create the SchoolUser
        SchoolUserDTO schoolUserDTO = schoolUserMapper.toDto(schoolUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchoolUserMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(schoolUserDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SchoolUser in the database
        List<SchoolUser> schoolUserList = schoolUserRepository.findAll();
        assertThat(schoolUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSchoolUserWithPatch() throws Exception {
        // Initialize the database
        schoolUserRepository.saveAndFlush(schoolUser);

        int databaseSizeBeforeUpdate = schoolUserRepository.findAll().size();

        // Update the schoolUser using partial update
        SchoolUser partialUpdatedSchoolUser = new SchoolUser();
        partialUpdatedSchoolUser.setId(schoolUser.getId());

        partialUpdatedSchoolUser
            .loginName(UPDATED_LOGIN_NAME)
            .userEmail(UPDATED_USER_EMAIL)
            .activated(UPDATED_ACTIVATED)
            .userType(UPDATED_USER_TYPE);

        restSchoolUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSchoolUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSchoolUser))
            )
            .andExpect(status().isOk());

        // Validate the SchoolUser in the database
        List<SchoolUser> schoolUserList = schoolUserRepository.findAll();
        assertThat(schoolUserList).hasSize(databaseSizeBeforeUpdate);
        SchoolUser testSchoolUser = schoolUserList.get(schoolUserList.size() - 1);
        assertThat(testSchoolUser.getLoginName()).isEqualTo(UPDATED_LOGIN_NAME);
        assertThat(testSchoolUser.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testSchoolUser.getUserEmail()).isEqualTo(UPDATED_USER_EMAIL);
        assertThat(testSchoolUser.getExtraInfo()).isEqualTo(DEFAULT_EXTRA_INFO);
        assertThat(testSchoolUser.getActivated()).isEqualTo(UPDATED_ACTIVATED);
        assertThat(testSchoolUser.getUserType()).isEqualTo(UPDATED_USER_TYPE);
        assertThat(testSchoolUser.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testSchoolUser.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
        assertThat(testSchoolUser.getCancelDate()).isEqualTo(DEFAULT_CANCEL_DATE);
    }

    @Test
    @Transactional
    void fullUpdateSchoolUserWithPatch() throws Exception {
        // Initialize the database
        schoolUserRepository.saveAndFlush(schoolUser);

        int databaseSizeBeforeUpdate = schoolUserRepository.findAll().size();

        // Update the schoolUser using partial update
        SchoolUser partialUpdatedSchoolUser = new SchoolUser();
        partialUpdatedSchoolUser.setId(schoolUser.getId());

        partialUpdatedSchoolUser
            .loginName(UPDATED_LOGIN_NAME)
            .password(UPDATED_PASSWORD)
            .userEmail(UPDATED_USER_EMAIL)
            .extraInfo(UPDATED_EXTRA_INFO)
            .activated(UPDATED_ACTIVATED)
            .userType(UPDATED_USER_TYPE)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);

        restSchoolUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSchoolUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSchoolUser))
            )
            .andExpect(status().isOk());

        // Validate the SchoolUser in the database
        List<SchoolUser> schoolUserList = schoolUserRepository.findAll();
        assertThat(schoolUserList).hasSize(databaseSizeBeforeUpdate);
        SchoolUser testSchoolUser = schoolUserList.get(schoolUserList.size() - 1);
        assertThat(testSchoolUser.getLoginName()).isEqualTo(UPDATED_LOGIN_NAME);
        assertThat(testSchoolUser.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testSchoolUser.getUserEmail()).isEqualTo(UPDATED_USER_EMAIL);
        assertThat(testSchoolUser.getExtraInfo()).isEqualTo(UPDATED_EXTRA_INFO);
        assertThat(testSchoolUser.getActivated()).isEqualTo(UPDATED_ACTIVATED);
        assertThat(testSchoolUser.getUserType()).isEqualTo(UPDATED_USER_TYPE);
        assertThat(testSchoolUser.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testSchoolUser.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testSchoolUser.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingSchoolUser() throws Exception {
        int databaseSizeBeforeUpdate = schoolUserRepository.findAll().size();
        schoolUser.setId(count.incrementAndGet());

        // Create the SchoolUser
        SchoolUserDTO schoolUserDTO = schoolUserMapper.toDto(schoolUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSchoolUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, schoolUserDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(schoolUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchoolUser in the database
        List<SchoolUser> schoolUserList = schoolUserRepository.findAll();
        assertThat(schoolUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSchoolUser() throws Exception {
        int databaseSizeBeforeUpdate = schoolUserRepository.findAll().size();
        schoolUser.setId(count.incrementAndGet());

        // Create the SchoolUser
        SchoolUserDTO schoolUserDTO = schoolUserMapper.toDto(schoolUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchoolUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(schoolUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchoolUser in the database
        List<SchoolUser> schoolUserList = schoolUserRepository.findAll();
        assertThat(schoolUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSchoolUser() throws Exception {
        int databaseSizeBeforeUpdate = schoolUserRepository.findAll().size();
        schoolUser.setId(count.incrementAndGet());

        // Create the SchoolUser
        SchoolUserDTO schoolUserDTO = schoolUserMapper.toDto(schoolUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchoolUserMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(schoolUserDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SchoolUser in the database
        List<SchoolUser> schoolUserList = schoolUserRepository.findAll();
        assertThat(schoolUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSchoolUser() throws Exception {
        // Initialize the database
        schoolUserRepository.saveAndFlush(schoolUser);

        int databaseSizeBeforeDelete = schoolUserRepository.findAll().size();

        // Delete the schoolUser
        restSchoolUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, schoolUser.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SchoolUser> schoolUserList = schoolUserRepository.findAll();
        assertThat(schoolUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
