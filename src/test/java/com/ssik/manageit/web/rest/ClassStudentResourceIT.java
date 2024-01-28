package com.ssik.manageit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ssik.manageit.IntegrationTest;
import com.ssik.manageit.domain.ClassStudent;
import com.ssik.manageit.domain.SchoolClass;
import com.ssik.manageit.domain.StudentAdditionalCharges;
import com.ssik.manageit.domain.StudentAttendence;
import com.ssik.manageit.domain.StudentChargesSummary;
import com.ssik.manageit.domain.StudentClassWorkTrack;
import com.ssik.manageit.domain.StudentDiscount;
import com.ssik.manageit.domain.StudentHomeWorkTrack;
import com.ssik.manageit.domain.StudentPayments;
import com.ssik.manageit.domain.enumeration.BloodGroup;
import com.ssik.manageit.domain.enumeration.Gender;
import com.ssik.manageit.repository.ClassStudentRepository;
import com.ssik.manageit.service.criteria.ClassStudentCriteria;
import com.ssik.manageit.service.dto.ClassStudentDTO;
import com.ssik.manageit.service.mapper.ClassStudentMapper;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link ClassStudentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ClassStudentResourceIT {

    private static final byte[] DEFAULT_STUDENT_PHOTO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_STUDENT_PHOTO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_STUDENT_PHOTO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_STUDENT_PHOTO_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_STUDENT_PHOTO_LINK = "AAAAAAAAAA";
    private static final String UPDATED_STUDENT_PHOTO_LINK = "BBBBBBBBBB";

    private static final String DEFAULT_STUDENT_ID = "AAAAAAAAAA";
    private static final String UPDATED_STUDENT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final Gender DEFAULT_GENDER = Gender.FEMALE;
    private static final Gender UPDATED_GENDER = Gender.MALE;

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ROLL_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_ROLL_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final BloodGroup DEFAULT_BLOOD_GROUP = BloodGroup.A_Pos;
    private static final BloodGroup UPDATED_BLOOD_GROUP = BloodGroup.A_Neg;

    private static final Instant DEFAULT_DATE_OF_BIRTH = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_OF_BIRTH = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_START_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_ADDRESS_LINE_1 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_LINE_1 = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_LINE_2 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_LINE_2 = "BBBBBBBBBB";

    private static final String DEFAULT_NICK_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NICK_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FATHER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FATHER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MOTHER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MOTHER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_ADMISSION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ADMISSION_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_ADMISSION_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_REG_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_REG_NUMBER = "BBBBBBBBBB";

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

    private static final String ENTITY_API_URL = "/api/class-students";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ClassStudentRepository classStudentRepository;

    @Autowired
    private ClassStudentMapper classStudentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restClassStudentMockMvc;

    private ClassStudent classStudent;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClassStudent createEntity(EntityManager em) {
        ClassStudent classStudent = new ClassStudent()
            .studentPhoto(DEFAULT_STUDENT_PHOTO)
            .studentPhotoContentType(DEFAULT_STUDENT_PHOTO_CONTENT_TYPE)
            .studentPhotoLink(DEFAULT_STUDENT_PHOTO_LINK)
            .studentId(DEFAULT_STUDENT_ID)
            .firstName(DEFAULT_FIRST_NAME)
            .gender(DEFAULT_GENDER)
            .lastName(DEFAULT_LAST_NAME)
            .rollNumber(DEFAULT_ROLL_NUMBER)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .bloodGroup(DEFAULT_BLOOD_GROUP)
            .dateOfBirth(DEFAULT_DATE_OF_BIRTH)
            .startDate(DEFAULT_START_DATE)
            .addressLine1(DEFAULT_ADDRESS_LINE_1)
            .addressLine2(DEFAULT_ADDRESS_LINE_2)
            .nickName(DEFAULT_NICK_NAME)
            .fatherName(DEFAULT_FATHER_NAME)
            .motherName(DEFAULT_MOTHER_NAME)
            .email(DEFAULT_EMAIL)
            .admissionDate(DEFAULT_ADMISSION_DATE)
            .regNumber(DEFAULT_REG_NUMBER)
            .endDate(DEFAULT_END_DATE)
            .createDate(DEFAULT_CREATE_DATE)
            .lastModified(DEFAULT_LAST_MODIFIED)
            .cancelDate(DEFAULT_CANCEL_DATE);
        return classStudent;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClassStudent createUpdatedEntity(EntityManager em) {
        ClassStudent classStudent = new ClassStudent()
            .studentPhoto(UPDATED_STUDENT_PHOTO)
            .studentPhotoContentType(UPDATED_STUDENT_PHOTO_CONTENT_TYPE)
            .studentPhotoLink(UPDATED_STUDENT_PHOTO_LINK)
            .studentId(UPDATED_STUDENT_ID)
            .firstName(UPDATED_FIRST_NAME)
            .gender(UPDATED_GENDER)
            .lastName(UPDATED_LAST_NAME)
            .rollNumber(UPDATED_ROLL_NUMBER)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .bloodGroup(UPDATED_BLOOD_GROUP)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .startDate(UPDATED_START_DATE)
            .addressLine1(UPDATED_ADDRESS_LINE_1)
            .addressLine2(UPDATED_ADDRESS_LINE_2)
            .nickName(UPDATED_NICK_NAME)
            .fatherName(UPDATED_FATHER_NAME)
            .motherName(UPDATED_MOTHER_NAME)
            .email(UPDATED_EMAIL)
            .admissionDate(UPDATED_ADMISSION_DATE)
            .regNumber(UPDATED_REG_NUMBER)
            .endDate(UPDATED_END_DATE)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);
        return classStudent;
    }

    @BeforeEach
    public void initTest() {
        classStudent = createEntity(em);
    }

    @Test
    @Transactional
    void createClassStudent() throws Exception {
        int databaseSizeBeforeCreate = classStudentRepository.findAll().size();
        // Create the ClassStudent
        ClassStudentDTO classStudentDTO = classStudentMapper.toDto(classStudent);
        restClassStudentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(classStudentDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ClassStudent in the database
        List<ClassStudent> classStudentList = classStudentRepository.findAll();
        assertThat(classStudentList).hasSize(databaseSizeBeforeCreate + 1);
        ClassStudent testClassStudent = classStudentList.get(classStudentList.size() - 1);
        assertThat(testClassStudent.getStudentPhoto()).isEqualTo(DEFAULT_STUDENT_PHOTO);
        assertThat(testClassStudent.getStudentPhotoContentType()).isEqualTo(DEFAULT_STUDENT_PHOTO_CONTENT_TYPE);
        assertThat(testClassStudent.getStudentPhotoLink()).isEqualTo(DEFAULT_STUDENT_PHOTO_LINK);
        assertThat(testClassStudent.getStudentId()).isEqualTo(DEFAULT_STUDENT_ID);
        assertThat(testClassStudent.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testClassStudent.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testClassStudent.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testClassStudent.getRollNumber()).isEqualTo(DEFAULT_ROLL_NUMBER);
        assertThat(testClassStudent.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testClassStudent.getBloodGroup()).isEqualTo(DEFAULT_BLOOD_GROUP);
        assertThat(testClassStudent.getDateOfBirth()).isEqualTo(DEFAULT_DATE_OF_BIRTH);
        assertThat(testClassStudent.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testClassStudent.getAddressLine1()).isEqualTo(DEFAULT_ADDRESS_LINE_1);
        assertThat(testClassStudent.getAddressLine2()).isEqualTo(DEFAULT_ADDRESS_LINE_2);
        assertThat(testClassStudent.getNickName()).isEqualTo(DEFAULT_NICK_NAME);
        assertThat(testClassStudent.getFatherName()).isEqualTo(DEFAULT_FATHER_NAME);
        assertThat(testClassStudent.getMotherName()).isEqualTo(DEFAULT_MOTHER_NAME);
        assertThat(testClassStudent.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testClassStudent.getAdmissionDate()).isEqualTo(DEFAULT_ADMISSION_DATE);
        assertThat(testClassStudent.getRegNumber()).isEqualTo(DEFAULT_REG_NUMBER);
        assertThat(testClassStudent.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testClassStudent.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testClassStudent.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
        assertThat(testClassStudent.getCancelDate()).isEqualTo(DEFAULT_CANCEL_DATE);
    }

    @Test
    @Transactional
    void createClassStudentWithExistingId() throws Exception {
        // Create the ClassStudent with an existing ID
        classStudent.setId(1L);
        ClassStudentDTO classStudentDTO = classStudentMapper.toDto(classStudent);

        int databaseSizeBeforeCreate = classStudentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restClassStudentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(classStudentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClassStudent in the database
        List<ClassStudent> classStudentList = classStudentRepository.findAll();
        assertThat(classStudentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = classStudentRepository.findAll().size();
        // set the field null
        classStudent.setFirstName(null);

        // Create the ClassStudent, which fails.
        ClassStudentDTO classStudentDTO = classStudentMapper.toDto(classStudent);

        restClassStudentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(classStudentDTO))
            )
            .andExpect(status().isBadRequest());

        List<ClassStudent> classStudentList = classStudentRepository.findAll();
        assertThat(classStudentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllClassStudents() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList
        restClassStudentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(classStudent.getId().intValue())))
            .andExpect(jsonPath("$.[*].studentPhotoContentType").value(hasItem(DEFAULT_STUDENT_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].studentPhoto").value(hasItem(Base64Utils.encodeToString(DEFAULT_STUDENT_PHOTO))))
            .andExpect(jsonPath("$.[*].studentPhotoLink").value(hasItem(DEFAULT_STUDENT_PHOTO_LINK)))
            .andExpect(jsonPath("$.[*].studentId").value(hasItem(DEFAULT_STUDENT_ID)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].rollNumber").value(hasItem(DEFAULT_ROLL_NUMBER)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].bloodGroup").value(hasItem(DEFAULT_BLOOD_GROUP.toString())))
            .andExpect(jsonPath("$.[*].dateOfBirth").value(hasItem(DEFAULT_DATE_OF_BIRTH.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].addressLine1").value(hasItem(DEFAULT_ADDRESS_LINE_1)))
            .andExpect(jsonPath("$.[*].addressLine2").value(hasItem(DEFAULT_ADDRESS_LINE_2)))
            .andExpect(jsonPath("$.[*].nickName").value(hasItem(DEFAULT_NICK_NAME)))
            .andExpect(jsonPath("$.[*].fatherName").value(hasItem(DEFAULT_FATHER_NAME)))
            .andExpect(jsonPath("$.[*].motherName").value(hasItem(DEFAULT_MOTHER_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].admissionDate").value(hasItem(DEFAULT_ADMISSION_DATE.toString())))
            .andExpect(jsonPath("$.[*].regNumber").value(hasItem(DEFAULT_REG_NUMBER)))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].cancelDate").value(hasItem(DEFAULT_CANCEL_DATE.toString())));
    }

    @Test
    @Transactional
    void getClassStudent() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get the classStudent
        restClassStudentMockMvc
            .perform(get(ENTITY_API_URL_ID, classStudent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(classStudent.getId().intValue()))
            .andExpect(jsonPath("$.studentPhotoContentType").value(DEFAULT_STUDENT_PHOTO_CONTENT_TYPE))
            .andExpect(jsonPath("$.studentPhoto").value(Base64Utils.encodeToString(DEFAULT_STUDENT_PHOTO)))
            .andExpect(jsonPath("$.studentPhotoLink").value(DEFAULT_STUDENT_PHOTO_LINK))
            .andExpect(jsonPath("$.studentId").value(DEFAULT_STUDENT_ID))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.rollNumber").value(DEFAULT_ROLL_NUMBER))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.bloodGroup").value(DEFAULT_BLOOD_GROUP.toString()))
            .andExpect(jsonPath("$.dateOfBirth").value(DEFAULT_DATE_OF_BIRTH.toString()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.addressLine1").value(DEFAULT_ADDRESS_LINE_1))
            .andExpect(jsonPath("$.addressLine2").value(DEFAULT_ADDRESS_LINE_2))
            .andExpect(jsonPath("$.nickName").value(DEFAULT_NICK_NAME))
            .andExpect(jsonPath("$.fatherName").value(DEFAULT_FATHER_NAME))
            .andExpect(jsonPath("$.motherName").value(DEFAULT_MOTHER_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.admissionDate").value(DEFAULT_ADMISSION_DATE.toString()))
            .andExpect(jsonPath("$.regNumber").value(DEFAULT_REG_NUMBER))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.lastModified").value(DEFAULT_LAST_MODIFIED.toString()))
            .andExpect(jsonPath("$.cancelDate").value(DEFAULT_CANCEL_DATE.toString()));
    }

    @Test
    @Transactional
    void getClassStudentsByIdFiltering() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        Long id = classStudent.getId();

        defaultClassStudentShouldBeFound("id.equals=" + id);
        defaultClassStudentShouldNotBeFound("id.notEquals=" + id);

        defaultClassStudentShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultClassStudentShouldNotBeFound("id.greaterThan=" + id);

        defaultClassStudentShouldBeFound("id.lessThanOrEqual=" + id);
        defaultClassStudentShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllClassStudentsByStudentPhotoLinkIsEqualToSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where studentPhotoLink equals to DEFAULT_STUDENT_PHOTO_LINK
        defaultClassStudentShouldBeFound("studentPhotoLink.equals=" + DEFAULT_STUDENT_PHOTO_LINK);

        // Get all the classStudentList where studentPhotoLink equals to UPDATED_STUDENT_PHOTO_LINK
        defaultClassStudentShouldNotBeFound("studentPhotoLink.equals=" + UPDATED_STUDENT_PHOTO_LINK);
    }

    @Test
    @Transactional
    void getAllClassStudentsByStudentPhotoLinkIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where studentPhotoLink not equals to DEFAULT_STUDENT_PHOTO_LINK
        defaultClassStudentShouldNotBeFound("studentPhotoLink.notEquals=" + DEFAULT_STUDENT_PHOTO_LINK);

        // Get all the classStudentList where studentPhotoLink not equals to UPDATED_STUDENT_PHOTO_LINK
        defaultClassStudentShouldBeFound("studentPhotoLink.notEquals=" + UPDATED_STUDENT_PHOTO_LINK);
    }

    @Test
    @Transactional
    void getAllClassStudentsByStudentPhotoLinkIsInShouldWork() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where studentPhotoLink in DEFAULT_STUDENT_PHOTO_LINK or UPDATED_STUDENT_PHOTO_LINK
        defaultClassStudentShouldBeFound("studentPhotoLink.in=" + DEFAULT_STUDENT_PHOTO_LINK + "," + UPDATED_STUDENT_PHOTO_LINK);

        // Get all the classStudentList where studentPhotoLink equals to UPDATED_STUDENT_PHOTO_LINK
        defaultClassStudentShouldNotBeFound("studentPhotoLink.in=" + UPDATED_STUDENT_PHOTO_LINK);
    }

    @Test
    @Transactional
    void getAllClassStudentsByStudentPhotoLinkIsNullOrNotNull() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where studentPhotoLink is not null
        defaultClassStudentShouldBeFound("studentPhotoLink.specified=true");

        // Get all the classStudentList where studentPhotoLink is null
        defaultClassStudentShouldNotBeFound("studentPhotoLink.specified=false");
    }

    @Test
    @Transactional
    void getAllClassStudentsByStudentPhotoLinkContainsSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where studentPhotoLink contains DEFAULT_STUDENT_PHOTO_LINK
        defaultClassStudentShouldBeFound("studentPhotoLink.contains=" + DEFAULT_STUDENT_PHOTO_LINK);

        // Get all the classStudentList where studentPhotoLink contains UPDATED_STUDENT_PHOTO_LINK
        defaultClassStudentShouldNotBeFound("studentPhotoLink.contains=" + UPDATED_STUDENT_PHOTO_LINK);
    }

    @Test
    @Transactional
    void getAllClassStudentsByStudentPhotoLinkNotContainsSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where studentPhotoLink does not contain DEFAULT_STUDENT_PHOTO_LINK
        defaultClassStudentShouldNotBeFound("studentPhotoLink.doesNotContain=" + DEFAULT_STUDENT_PHOTO_LINK);

        // Get all the classStudentList where studentPhotoLink does not contain UPDATED_STUDENT_PHOTO_LINK
        defaultClassStudentShouldBeFound("studentPhotoLink.doesNotContain=" + UPDATED_STUDENT_PHOTO_LINK);
    }

    @Test
    @Transactional
    void getAllClassStudentsByStudentIdIsEqualToSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where studentId equals to DEFAULT_STUDENT_ID
        defaultClassStudentShouldBeFound("studentId.equals=" + DEFAULT_STUDENT_ID);

        // Get all the classStudentList where studentId equals to UPDATED_STUDENT_ID
        defaultClassStudentShouldNotBeFound("studentId.equals=" + UPDATED_STUDENT_ID);
    }

    @Test
    @Transactional
    void getAllClassStudentsByStudentIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where studentId not equals to DEFAULT_STUDENT_ID
        defaultClassStudentShouldNotBeFound("studentId.notEquals=" + DEFAULT_STUDENT_ID);

        // Get all the classStudentList where studentId not equals to UPDATED_STUDENT_ID
        defaultClassStudentShouldBeFound("studentId.notEquals=" + UPDATED_STUDENT_ID);
    }

    @Test
    @Transactional
    void getAllClassStudentsByStudentIdIsInShouldWork() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where studentId in DEFAULT_STUDENT_ID or UPDATED_STUDENT_ID
        defaultClassStudentShouldBeFound("studentId.in=" + DEFAULT_STUDENT_ID + "," + UPDATED_STUDENT_ID);

        // Get all the classStudentList where studentId equals to UPDATED_STUDENT_ID
        defaultClassStudentShouldNotBeFound("studentId.in=" + UPDATED_STUDENT_ID);
    }

    @Test
    @Transactional
    void getAllClassStudentsByStudentIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where studentId is not null
        defaultClassStudentShouldBeFound("studentId.specified=true");

        // Get all the classStudentList where studentId is null
        defaultClassStudentShouldNotBeFound("studentId.specified=false");
    }

    @Test
    @Transactional
    void getAllClassStudentsByStudentIdContainsSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where studentId contains DEFAULT_STUDENT_ID
        defaultClassStudentShouldBeFound("studentId.contains=" + DEFAULT_STUDENT_ID);

        // Get all the classStudentList where studentId contains UPDATED_STUDENT_ID
        defaultClassStudentShouldNotBeFound("studentId.contains=" + UPDATED_STUDENT_ID);
    }

    @Test
    @Transactional
    void getAllClassStudentsByStudentIdNotContainsSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where studentId does not contain DEFAULT_STUDENT_ID
        defaultClassStudentShouldNotBeFound("studentId.doesNotContain=" + DEFAULT_STUDENT_ID);

        // Get all the classStudentList where studentId does not contain UPDATED_STUDENT_ID
        defaultClassStudentShouldBeFound("studentId.doesNotContain=" + UPDATED_STUDENT_ID);
    }

    @Test
    @Transactional
    void getAllClassStudentsByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where firstName equals to DEFAULT_FIRST_NAME
        defaultClassStudentShouldBeFound("firstName.equals=" + DEFAULT_FIRST_NAME);

        // Get all the classStudentList where firstName equals to UPDATED_FIRST_NAME
        defaultClassStudentShouldNotBeFound("firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllClassStudentsByFirstNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where firstName not equals to DEFAULT_FIRST_NAME
        defaultClassStudentShouldNotBeFound("firstName.notEquals=" + DEFAULT_FIRST_NAME);

        // Get all the classStudentList where firstName not equals to UPDATED_FIRST_NAME
        defaultClassStudentShouldBeFound("firstName.notEquals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllClassStudentsByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where firstName in DEFAULT_FIRST_NAME or UPDATED_FIRST_NAME
        defaultClassStudentShouldBeFound("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME);

        // Get all the classStudentList where firstName equals to UPDATED_FIRST_NAME
        defaultClassStudentShouldNotBeFound("firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllClassStudentsByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where firstName is not null
        defaultClassStudentShouldBeFound("firstName.specified=true");

        // Get all the classStudentList where firstName is null
        defaultClassStudentShouldNotBeFound("firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllClassStudentsByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where firstName contains DEFAULT_FIRST_NAME
        defaultClassStudentShouldBeFound("firstName.contains=" + DEFAULT_FIRST_NAME);

        // Get all the classStudentList where firstName contains UPDATED_FIRST_NAME
        defaultClassStudentShouldNotBeFound("firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllClassStudentsByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where firstName does not contain DEFAULT_FIRST_NAME
        defaultClassStudentShouldNotBeFound("firstName.doesNotContain=" + DEFAULT_FIRST_NAME);

        // Get all the classStudentList where firstName does not contain UPDATED_FIRST_NAME
        defaultClassStudentShouldBeFound("firstName.doesNotContain=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllClassStudentsByGenderIsEqualToSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where gender equals to DEFAULT_GENDER
        defaultClassStudentShouldBeFound("gender.equals=" + DEFAULT_GENDER);

        // Get all the classStudentList where gender equals to UPDATED_GENDER
        defaultClassStudentShouldNotBeFound("gender.equals=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllClassStudentsByGenderIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where gender not equals to DEFAULT_GENDER
        defaultClassStudentShouldNotBeFound("gender.notEquals=" + DEFAULT_GENDER);

        // Get all the classStudentList where gender not equals to UPDATED_GENDER
        defaultClassStudentShouldBeFound("gender.notEquals=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllClassStudentsByGenderIsInShouldWork() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where gender in DEFAULT_GENDER or UPDATED_GENDER
        defaultClassStudentShouldBeFound("gender.in=" + DEFAULT_GENDER + "," + UPDATED_GENDER);

        // Get all the classStudentList where gender equals to UPDATED_GENDER
        defaultClassStudentShouldNotBeFound("gender.in=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllClassStudentsByGenderIsNullOrNotNull() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where gender is not null
        defaultClassStudentShouldBeFound("gender.specified=true");

        // Get all the classStudentList where gender is null
        defaultClassStudentShouldNotBeFound("gender.specified=false");
    }

    @Test
    @Transactional
    void getAllClassStudentsByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where lastName equals to DEFAULT_LAST_NAME
        defaultClassStudentShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the classStudentList where lastName equals to UPDATED_LAST_NAME
        defaultClassStudentShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllClassStudentsByLastNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where lastName not equals to DEFAULT_LAST_NAME
        defaultClassStudentShouldNotBeFound("lastName.notEquals=" + DEFAULT_LAST_NAME);

        // Get all the classStudentList where lastName not equals to UPDATED_LAST_NAME
        defaultClassStudentShouldBeFound("lastName.notEquals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllClassStudentsByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultClassStudentShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the classStudentList where lastName equals to UPDATED_LAST_NAME
        defaultClassStudentShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllClassStudentsByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where lastName is not null
        defaultClassStudentShouldBeFound("lastName.specified=true");

        // Get all the classStudentList where lastName is null
        defaultClassStudentShouldNotBeFound("lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllClassStudentsByLastNameContainsSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where lastName contains DEFAULT_LAST_NAME
        defaultClassStudentShouldBeFound("lastName.contains=" + DEFAULT_LAST_NAME);

        // Get all the classStudentList where lastName contains UPDATED_LAST_NAME
        defaultClassStudentShouldNotBeFound("lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllClassStudentsByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where lastName does not contain DEFAULT_LAST_NAME
        defaultClassStudentShouldNotBeFound("lastName.doesNotContain=" + DEFAULT_LAST_NAME);

        // Get all the classStudentList where lastName does not contain UPDATED_LAST_NAME
        defaultClassStudentShouldBeFound("lastName.doesNotContain=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllClassStudentsByRollNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where rollNumber equals to DEFAULT_ROLL_NUMBER
        defaultClassStudentShouldBeFound("rollNumber.equals=" + DEFAULT_ROLL_NUMBER);

        // Get all the classStudentList where rollNumber equals to UPDATED_ROLL_NUMBER
        defaultClassStudentShouldNotBeFound("rollNumber.equals=" + UPDATED_ROLL_NUMBER);
    }

    @Test
    @Transactional
    void getAllClassStudentsByRollNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where rollNumber not equals to DEFAULT_ROLL_NUMBER
        defaultClassStudentShouldNotBeFound("rollNumber.notEquals=" + DEFAULT_ROLL_NUMBER);

        // Get all the classStudentList where rollNumber not equals to UPDATED_ROLL_NUMBER
        defaultClassStudentShouldBeFound("rollNumber.notEquals=" + UPDATED_ROLL_NUMBER);
    }

    @Test
    @Transactional
    void getAllClassStudentsByRollNumberIsInShouldWork() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where rollNumber in DEFAULT_ROLL_NUMBER or UPDATED_ROLL_NUMBER
        defaultClassStudentShouldBeFound("rollNumber.in=" + DEFAULT_ROLL_NUMBER + "," + UPDATED_ROLL_NUMBER);

        // Get all the classStudentList where rollNumber equals to UPDATED_ROLL_NUMBER
        defaultClassStudentShouldNotBeFound("rollNumber.in=" + UPDATED_ROLL_NUMBER);
    }

    @Test
    @Transactional
    void getAllClassStudentsByRollNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where rollNumber is not null
        defaultClassStudentShouldBeFound("rollNumber.specified=true");

        // Get all the classStudentList where rollNumber is null
        defaultClassStudentShouldNotBeFound("rollNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllClassStudentsByRollNumberContainsSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where rollNumber contains DEFAULT_ROLL_NUMBER
        defaultClassStudentShouldBeFound("rollNumber.contains=" + DEFAULT_ROLL_NUMBER);

        // Get all the classStudentList where rollNumber contains UPDATED_ROLL_NUMBER
        defaultClassStudentShouldNotBeFound("rollNumber.contains=" + UPDATED_ROLL_NUMBER);
    }

    @Test
    @Transactional
    void getAllClassStudentsByRollNumberNotContainsSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where rollNumber does not contain DEFAULT_ROLL_NUMBER
        defaultClassStudentShouldNotBeFound("rollNumber.doesNotContain=" + DEFAULT_ROLL_NUMBER);

        // Get all the classStudentList where rollNumber does not contain UPDATED_ROLL_NUMBER
        defaultClassStudentShouldBeFound("rollNumber.doesNotContain=" + UPDATED_ROLL_NUMBER);
    }

    @Test
    @Transactional
    void getAllClassStudentsByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where phoneNumber equals to DEFAULT_PHONE_NUMBER
        defaultClassStudentShouldBeFound("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER);

        // Get all the classStudentList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultClassStudentShouldNotBeFound("phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllClassStudentsByPhoneNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where phoneNumber not equals to DEFAULT_PHONE_NUMBER
        defaultClassStudentShouldNotBeFound("phoneNumber.notEquals=" + DEFAULT_PHONE_NUMBER);

        // Get all the classStudentList where phoneNumber not equals to UPDATED_PHONE_NUMBER
        defaultClassStudentShouldBeFound("phoneNumber.notEquals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllClassStudentsByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where phoneNumber in DEFAULT_PHONE_NUMBER or UPDATED_PHONE_NUMBER
        defaultClassStudentShouldBeFound("phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER);

        // Get all the classStudentList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultClassStudentShouldNotBeFound("phoneNumber.in=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllClassStudentsByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where phoneNumber is not null
        defaultClassStudentShouldBeFound("phoneNumber.specified=true");

        // Get all the classStudentList where phoneNumber is null
        defaultClassStudentShouldNotBeFound("phoneNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllClassStudentsByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where phoneNumber contains DEFAULT_PHONE_NUMBER
        defaultClassStudentShouldBeFound("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER);

        // Get all the classStudentList where phoneNumber contains UPDATED_PHONE_NUMBER
        defaultClassStudentShouldNotBeFound("phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllClassStudentsByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where phoneNumber does not contain DEFAULT_PHONE_NUMBER
        defaultClassStudentShouldNotBeFound("phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER);

        // Get all the classStudentList where phoneNumber does not contain UPDATED_PHONE_NUMBER
        defaultClassStudentShouldBeFound("phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllClassStudentsByBloodGroupIsEqualToSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where bloodGroup equals to DEFAULT_BLOOD_GROUP
        defaultClassStudentShouldBeFound("bloodGroup.equals=" + DEFAULT_BLOOD_GROUP);

        // Get all the classStudentList where bloodGroup equals to UPDATED_BLOOD_GROUP
        defaultClassStudentShouldNotBeFound("bloodGroup.equals=" + UPDATED_BLOOD_GROUP);
    }

    @Test
    @Transactional
    void getAllClassStudentsByBloodGroupIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where bloodGroup not equals to DEFAULT_BLOOD_GROUP
        defaultClassStudentShouldNotBeFound("bloodGroup.notEquals=" + DEFAULT_BLOOD_GROUP);

        // Get all the classStudentList where bloodGroup not equals to UPDATED_BLOOD_GROUP
        defaultClassStudentShouldBeFound("bloodGroup.notEquals=" + UPDATED_BLOOD_GROUP);
    }

    @Test
    @Transactional
    void getAllClassStudentsByBloodGroupIsInShouldWork() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where bloodGroup in DEFAULT_BLOOD_GROUP or UPDATED_BLOOD_GROUP
        defaultClassStudentShouldBeFound("bloodGroup.in=" + DEFAULT_BLOOD_GROUP + "," + UPDATED_BLOOD_GROUP);

        // Get all the classStudentList where bloodGroup equals to UPDATED_BLOOD_GROUP
        defaultClassStudentShouldNotBeFound("bloodGroup.in=" + UPDATED_BLOOD_GROUP);
    }

    @Test
    @Transactional
    void getAllClassStudentsByBloodGroupIsNullOrNotNull() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where bloodGroup is not null
        defaultClassStudentShouldBeFound("bloodGroup.specified=true");

        // Get all the classStudentList where bloodGroup is null
        defaultClassStudentShouldNotBeFound("bloodGroup.specified=false");
    }

    @Test
    @Transactional
    void getAllClassStudentsByDateOfBirthIsEqualToSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where dateOfBirth equals to DEFAULT_DATE_OF_BIRTH
        defaultClassStudentShouldBeFound("dateOfBirth.equals=" + DEFAULT_DATE_OF_BIRTH);

        // Get all the classStudentList where dateOfBirth equals to UPDATED_DATE_OF_BIRTH
        defaultClassStudentShouldNotBeFound("dateOfBirth.equals=" + UPDATED_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    void getAllClassStudentsByDateOfBirthIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where dateOfBirth not equals to DEFAULT_DATE_OF_BIRTH
        defaultClassStudentShouldNotBeFound("dateOfBirth.notEquals=" + DEFAULT_DATE_OF_BIRTH);

        // Get all the classStudentList where dateOfBirth not equals to UPDATED_DATE_OF_BIRTH
        defaultClassStudentShouldBeFound("dateOfBirth.notEquals=" + UPDATED_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    void getAllClassStudentsByDateOfBirthIsInShouldWork() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where dateOfBirth in DEFAULT_DATE_OF_BIRTH or UPDATED_DATE_OF_BIRTH
        defaultClassStudentShouldBeFound("dateOfBirth.in=" + DEFAULT_DATE_OF_BIRTH + "," + UPDATED_DATE_OF_BIRTH);

        // Get all the classStudentList where dateOfBirth equals to UPDATED_DATE_OF_BIRTH
        defaultClassStudentShouldNotBeFound("dateOfBirth.in=" + UPDATED_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    void getAllClassStudentsByDateOfBirthIsNullOrNotNull() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where dateOfBirth is not null
        defaultClassStudentShouldBeFound("dateOfBirth.specified=true");

        // Get all the classStudentList where dateOfBirth is null
        defaultClassStudentShouldNotBeFound("dateOfBirth.specified=false");
    }

    @Test
    @Transactional
    void getAllClassStudentsByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where startDate equals to DEFAULT_START_DATE
        defaultClassStudentShouldBeFound("startDate.equals=" + DEFAULT_START_DATE);

        // Get all the classStudentList where startDate equals to UPDATED_START_DATE
        defaultClassStudentShouldNotBeFound("startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllClassStudentsByStartDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where startDate not equals to DEFAULT_START_DATE
        defaultClassStudentShouldNotBeFound("startDate.notEquals=" + DEFAULT_START_DATE);

        // Get all the classStudentList where startDate not equals to UPDATED_START_DATE
        defaultClassStudentShouldBeFound("startDate.notEquals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllClassStudentsByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where startDate in DEFAULT_START_DATE or UPDATED_START_DATE
        defaultClassStudentShouldBeFound("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE);

        // Get all the classStudentList where startDate equals to UPDATED_START_DATE
        defaultClassStudentShouldNotBeFound("startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllClassStudentsByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where startDate is not null
        defaultClassStudentShouldBeFound("startDate.specified=true");

        // Get all the classStudentList where startDate is null
        defaultClassStudentShouldNotBeFound("startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllClassStudentsByStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where startDate is greater than or equal to DEFAULT_START_DATE
        defaultClassStudentShouldBeFound("startDate.greaterThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the classStudentList where startDate is greater than or equal to UPDATED_START_DATE
        defaultClassStudentShouldNotBeFound("startDate.greaterThanOrEqual=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllClassStudentsByStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where startDate is less than or equal to DEFAULT_START_DATE
        defaultClassStudentShouldBeFound("startDate.lessThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the classStudentList where startDate is less than or equal to SMALLER_START_DATE
        defaultClassStudentShouldNotBeFound("startDate.lessThanOrEqual=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    void getAllClassStudentsByStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where startDate is less than DEFAULT_START_DATE
        defaultClassStudentShouldNotBeFound("startDate.lessThan=" + DEFAULT_START_DATE);

        // Get all the classStudentList where startDate is less than UPDATED_START_DATE
        defaultClassStudentShouldBeFound("startDate.lessThan=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllClassStudentsByStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where startDate is greater than DEFAULT_START_DATE
        defaultClassStudentShouldNotBeFound("startDate.greaterThan=" + DEFAULT_START_DATE);

        // Get all the classStudentList where startDate is greater than SMALLER_START_DATE
        defaultClassStudentShouldBeFound("startDate.greaterThan=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    void getAllClassStudentsByAddressLine1IsEqualToSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where addressLine1 equals to DEFAULT_ADDRESS_LINE_1
        defaultClassStudentShouldBeFound("addressLine1.equals=" + DEFAULT_ADDRESS_LINE_1);

        // Get all the classStudentList where addressLine1 equals to UPDATED_ADDRESS_LINE_1
        defaultClassStudentShouldNotBeFound("addressLine1.equals=" + UPDATED_ADDRESS_LINE_1);
    }

    @Test
    @Transactional
    void getAllClassStudentsByAddressLine1IsNotEqualToSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where addressLine1 not equals to DEFAULT_ADDRESS_LINE_1
        defaultClassStudentShouldNotBeFound("addressLine1.notEquals=" + DEFAULT_ADDRESS_LINE_1);

        // Get all the classStudentList where addressLine1 not equals to UPDATED_ADDRESS_LINE_1
        defaultClassStudentShouldBeFound("addressLine1.notEquals=" + UPDATED_ADDRESS_LINE_1);
    }

    @Test
    @Transactional
    void getAllClassStudentsByAddressLine1IsInShouldWork() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where addressLine1 in DEFAULT_ADDRESS_LINE_1 or UPDATED_ADDRESS_LINE_1
        defaultClassStudentShouldBeFound("addressLine1.in=" + DEFAULT_ADDRESS_LINE_1 + "," + UPDATED_ADDRESS_LINE_1);

        // Get all the classStudentList where addressLine1 equals to UPDATED_ADDRESS_LINE_1
        defaultClassStudentShouldNotBeFound("addressLine1.in=" + UPDATED_ADDRESS_LINE_1);
    }

    @Test
    @Transactional
    void getAllClassStudentsByAddressLine1IsNullOrNotNull() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where addressLine1 is not null
        defaultClassStudentShouldBeFound("addressLine1.specified=true");

        // Get all the classStudentList where addressLine1 is null
        defaultClassStudentShouldNotBeFound("addressLine1.specified=false");
    }

    @Test
    @Transactional
    void getAllClassStudentsByAddressLine1ContainsSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where addressLine1 contains DEFAULT_ADDRESS_LINE_1
        defaultClassStudentShouldBeFound("addressLine1.contains=" + DEFAULT_ADDRESS_LINE_1);

        // Get all the classStudentList where addressLine1 contains UPDATED_ADDRESS_LINE_1
        defaultClassStudentShouldNotBeFound("addressLine1.contains=" + UPDATED_ADDRESS_LINE_1);
    }

    @Test
    @Transactional
    void getAllClassStudentsByAddressLine1NotContainsSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where addressLine1 does not contain DEFAULT_ADDRESS_LINE_1
        defaultClassStudentShouldNotBeFound("addressLine1.doesNotContain=" + DEFAULT_ADDRESS_LINE_1);

        // Get all the classStudentList where addressLine1 does not contain UPDATED_ADDRESS_LINE_1
        defaultClassStudentShouldBeFound("addressLine1.doesNotContain=" + UPDATED_ADDRESS_LINE_1);
    }

    @Test
    @Transactional
    void getAllClassStudentsByAddressLine2IsEqualToSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where addressLine2 equals to DEFAULT_ADDRESS_LINE_2
        defaultClassStudentShouldBeFound("addressLine2.equals=" + DEFAULT_ADDRESS_LINE_2);

        // Get all the classStudentList where addressLine2 equals to UPDATED_ADDRESS_LINE_2
        defaultClassStudentShouldNotBeFound("addressLine2.equals=" + UPDATED_ADDRESS_LINE_2);
    }

    @Test
    @Transactional
    void getAllClassStudentsByAddressLine2IsNotEqualToSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where addressLine2 not equals to DEFAULT_ADDRESS_LINE_2
        defaultClassStudentShouldNotBeFound("addressLine2.notEquals=" + DEFAULT_ADDRESS_LINE_2);

        // Get all the classStudentList where addressLine2 not equals to UPDATED_ADDRESS_LINE_2
        defaultClassStudentShouldBeFound("addressLine2.notEquals=" + UPDATED_ADDRESS_LINE_2);
    }

    @Test
    @Transactional
    void getAllClassStudentsByAddressLine2IsInShouldWork() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where addressLine2 in DEFAULT_ADDRESS_LINE_2 or UPDATED_ADDRESS_LINE_2
        defaultClassStudentShouldBeFound("addressLine2.in=" + DEFAULT_ADDRESS_LINE_2 + "," + UPDATED_ADDRESS_LINE_2);

        // Get all the classStudentList where addressLine2 equals to UPDATED_ADDRESS_LINE_2
        defaultClassStudentShouldNotBeFound("addressLine2.in=" + UPDATED_ADDRESS_LINE_2);
    }

    @Test
    @Transactional
    void getAllClassStudentsByAddressLine2IsNullOrNotNull() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where addressLine2 is not null
        defaultClassStudentShouldBeFound("addressLine2.specified=true");

        // Get all the classStudentList where addressLine2 is null
        defaultClassStudentShouldNotBeFound("addressLine2.specified=false");
    }

    @Test
    @Transactional
    void getAllClassStudentsByAddressLine2ContainsSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where addressLine2 contains DEFAULT_ADDRESS_LINE_2
        defaultClassStudentShouldBeFound("addressLine2.contains=" + DEFAULT_ADDRESS_LINE_2);

        // Get all the classStudentList where addressLine2 contains UPDATED_ADDRESS_LINE_2
        defaultClassStudentShouldNotBeFound("addressLine2.contains=" + UPDATED_ADDRESS_LINE_2);
    }

    @Test
    @Transactional
    void getAllClassStudentsByAddressLine2NotContainsSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where addressLine2 does not contain DEFAULT_ADDRESS_LINE_2
        defaultClassStudentShouldNotBeFound("addressLine2.doesNotContain=" + DEFAULT_ADDRESS_LINE_2);

        // Get all the classStudentList where addressLine2 does not contain UPDATED_ADDRESS_LINE_2
        defaultClassStudentShouldBeFound("addressLine2.doesNotContain=" + UPDATED_ADDRESS_LINE_2);
    }

    @Test
    @Transactional
    void getAllClassStudentsByNickNameIsEqualToSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where nickName equals to DEFAULT_NICK_NAME
        defaultClassStudentShouldBeFound("nickName.equals=" + DEFAULT_NICK_NAME);

        // Get all the classStudentList where nickName equals to UPDATED_NICK_NAME
        defaultClassStudentShouldNotBeFound("nickName.equals=" + UPDATED_NICK_NAME);
    }

    @Test
    @Transactional
    void getAllClassStudentsByNickNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where nickName not equals to DEFAULT_NICK_NAME
        defaultClassStudentShouldNotBeFound("nickName.notEquals=" + DEFAULT_NICK_NAME);

        // Get all the classStudentList where nickName not equals to UPDATED_NICK_NAME
        defaultClassStudentShouldBeFound("nickName.notEquals=" + UPDATED_NICK_NAME);
    }

    @Test
    @Transactional
    void getAllClassStudentsByNickNameIsInShouldWork() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where nickName in DEFAULT_NICK_NAME or UPDATED_NICK_NAME
        defaultClassStudentShouldBeFound("nickName.in=" + DEFAULT_NICK_NAME + "," + UPDATED_NICK_NAME);

        // Get all the classStudentList where nickName equals to UPDATED_NICK_NAME
        defaultClassStudentShouldNotBeFound("nickName.in=" + UPDATED_NICK_NAME);
    }

    @Test
    @Transactional
    void getAllClassStudentsByNickNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where nickName is not null
        defaultClassStudentShouldBeFound("nickName.specified=true");

        // Get all the classStudentList where nickName is null
        defaultClassStudentShouldNotBeFound("nickName.specified=false");
    }

    @Test
    @Transactional
    void getAllClassStudentsByNickNameContainsSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where nickName contains DEFAULT_NICK_NAME
        defaultClassStudentShouldBeFound("nickName.contains=" + DEFAULT_NICK_NAME);

        // Get all the classStudentList where nickName contains UPDATED_NICK_NAME
        defaultClassStudentShouldNotBeFound("nickName.contains=" + UPDATED_NICK_NAME);
    }

    @Test
    @Transactional
    void getAllClassStudentsByNickNameNotContainsSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where nickName does not contain DEFAULT_NICK_NAME
        defaultClassStudentShouldNotBeFound("nickName.doesNotContain=" + DEFAULT_NICK_NAME);

        // Get all the classStudentList where nickName does not contain UPDATED_NICK_NAME
        defaultClassStudentShouldBeFound("nickName.doesNotContain=" + UPDATED_NICK_NAME);
    }

    @Test
    @Transactional
    void getAllClassStudentsByFatherNameIsEqualToSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where fatherName equals to DEFAULT_FATHER_NAME
        defaultClassStudentShouldBeFound("fatherName.equals=" + DEFAULT_FATHER_NAME);

        // Get all the classStudentList where fatherName equals to UPDATED_FATHER_NAME
        defaultClassStudentShouldNotBeFound("fatherName.equals=" + UPDATED_FATHER_NAME);
    }

    @Test
    @Transactional
    void getAllClassStudentsByFatherNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where fatherName not equals to DEFAULT_FATHER_NAME
        defaultClassStudentShouldNotBeFound("fatherName.notEquals=" + DEFAULT_FATHER_NAME);

        // Get all the classStudentList where fatherName not equals to UPDATED_FATHER_NAME
        defaultClassStudentShouldBeFound("fatherName.notEquals=" + UPDATED_FATHER_NAME);
    }

    @Test
    @Transactional
    void getAllClassStudentsByFatherNameIsInShouldWork() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where fatherName in DEFAULT_FATHER_NAME or UPDATED_FATHER_NAME
        defaultClassStudentShouldBeFound("fatherName.in=" + DEFAULT_FATHER_NAME + "," + UPDATED_FATHER_NAME);

        // Get all the classStudentList where fatherName equals to UPDATED_FATHER_NAME
        defaultClassStudentShouldNotBeFound("fatherName.in=" + UPDATED_FATHER_NAME);
    }

    @Test
    @Transactional
    void getAllClassStudentsByFatherNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where fatherName is not null
        defaultClassStudentShouldBeFound("fatherName.specified=true");

        // Get all the classStudentList where fatherName is null
        defaultClassStudentShouldNotBeFound("fatherName.specified=false");
    }

    @Test
    @Transactional
    void getAllClassStudentsByFatherNameContainsSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where fatherName contains DEFAULT_FATHER_NAME
        defaultClassStudentShouldBeFound("fatherName.contains=" + DEFAULT_FATHER_NAME);

        // Get all the classStudentList where fatherName contains UPDATED_FATHER_NAME
        defaultClassStudentShouldNotBeFound("fatherName.contains=" + UPDATED_FATHER_NAME);
    }

    @Test
    @Transactional
    void getAllClassStudentsByFatherNameNotContainsSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where fatherName does not contain DEFAULT_FATHER_NAME
        defaultClassStudentShouldNotBeFound("fatherName.doesNotContain=" + DEFAULT_FATHER_NAME);

        // Get all the classStudentList where fatherName does not contain UPDATED_FATHER_NAME
        defaultClassStudentShouldBeFound("fatherName.doesNotContain=" + UPDATED_FATHER_NAME);
    }

    @Test
    @Transactional
    void getAllClassStudentsByMotherNameIsEqualToSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where motherName equals to DEFAULT_MOTHER_NAME
        defaultClassStudentShouldBeFound("motherName.equals=" + DEFAULT_MOTHER_NAME);

        // Get all the classStudentList where motherName equals to UPDATED_MOTHER_NAME
        defaultClassStudentShouldNotBeFound("motherName.equals=" + UPDATED_MOTHER_NAME);
    }

    @Test
    @Transactional
    void getAllClassStudentsByMotherNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where motherName not equals to DEFAULT_MOTHER_NAME
        defaultClassStudentShouldNotBeFound("motherName.notEquals=" + DEFAULT_MOTHER_NAME);

        // Get all the classStudentList where motherName not equals to UPDATED_MOTHER_NAME
        defaultClassStudentShouldBeFound("motherName.notEquals=" + UPDATED_MOTHER_NAME);
    }

    @Test
    @Transactional
    void getAllClassStudentsByMotherNameIsInShouldWork() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where motherName in DEFAULT_MOTHER_NAME or UPDATED_MOTHER_NAME
        defaultClassStudentShouldBeFound("motherName.in=" + DEFAULT_MOTHER_NAME + "," + UPDATED_MOTHER_NAME);

        // Get all the classStudentList where motherName equals to UPDATED_MOTHER_NAME
        defaultClassStudentShouldNotBeFound("motherName.in=" + UPDATED_MOTHER_NAME);
    }

    @Test
    @Transactional
    void getAllClassStudentsByMotherNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where motherName is not null
        defaultClassStudentShouldBeFound("motherName.specified=true");

        // Get all the classStudentList where motherName is null
        defaultClassStudentShouldNotBeFound("motherName.specified=false");
    }

    @Test
    @Transactional
    void getAllClassStudentsByMotherNameContainsSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where motherName contains DEFAULT_MOTHER_NAME
        defaultClassStudentShouldBeFound("motherName.contains=" + DEFAULT_MOTHER_NAME);

        // Get all the classStudentList where motherName contains UPDATED_MOTHER_NAME
        defaultClassStudentShouldNotBeFound("motherName.contains=" + UPDATED_MOTHER_NAME);
    }

    @Test
    @Transactional
    void getAllClassStudentsByMotherNameNotContainsSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where motherName does not contain DEFAULT_MOTHER_NAME
        defaultClassStudentShouldNotBeFound("motherName.doesNotContain=" + DEFAULT_MOTHER_NAME);

        // Get all the classStudentList where motherName does not contain UPDATED_MOTHER_NAME
        defaultClassStudentShouldBeFound("motherName.doesNotContain=" + UPDATED_MOTHER_NAME);
    }

    @Test
    @Transactional
    void getAllClassStudentsByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where email equals to DEFAULT_EMAIL
        defaultClassStudentShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the classStudentList where email equals to UPDATED_EMAIL
        defaultClassStudentShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllClassStudentsByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where email not equals to DEFAULT_EMAIL
        defaultClassStudentShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the classStudentList where email not equals to UPDATED_EMAIL
        defaultClassStudentShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllClassStudentsByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultClassStudentShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the classStudentList where email equals to UPDATED_EMAIL
        defaultClassStudentShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllClassStudentsByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where email is not null
        defaultClassStudentShouldBeFound("email.specified=true");

        // Get all the classStudentList where email is null
        defaultClassStudentShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllClassStudentsByEmailContainsSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where email contains DEFAULT_EMAIL
        defaultClassStudentShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the classStudentList where email contains UPDATED_EMAIL
        defaultClassStudentShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllClassStudentsByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where email does not contain DEFAULT_EMAIL
        defaultClassStudentShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the classStudentList where email does not contain UPDATED_EMAIL
        defaultClassStudentShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllClassStudentsByAdmissionDateIsEqualToSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where admissionDate equals to DEFAULT_ADMISSION_DATE
        defaultClassStudentShouldBeFound("admissionDate.equals=" + DEFAULT_ADMISSION_DATE);

        // Get all the classStudentList where admissionDate equals to UPDATED_ADMISSION_DATE
        defaultClassStudentShouldNotBeFound("admissionDate.equals=" + UPDATED_ADMISSION_DATE);
    }

    @Test
    @Transactional
    void getAllClassStudentsByAdmissionDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where admissionDate not equals to DEFAULT_ADMISSION_DATE
        defaultClassStudentShouldNotBeFound("admissionDate.notEquals=" + DEFAULT_ADMISSION_DATE);

        // Get all the classStudentList where admissionDate not equals to UPDATED_ADMISSION_DATE
        defaultClassStudentShouldBeFound("admissionDate.notEquals=" + UPDATED_ADMISSION_DATE);
    }

    @Test
    @Transactional
    void getAllClassStudentsByAdmissionDateIsInShouldWork() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where admissionDate in DEFAULT_ADMISSION_DATE or UPDATED_ADMISSION_DATE
        defaultClassStudentShouldBeFound("admissionDate.in=" + DEFAULT_ADMISSION_DATE + "," + UPDATED_ADMISSION_DATE);

        // Get all the classStudentList where admissionDate equals to UPDATED_ADMISSION_DATE
        defaultClassStudentShouldNotBeFound("admissionDate.in=" + UPDATED_ADMISSION_DATE);
    }

    @Test
    @Transactional
    void getAllClassStudentsByAdmissionDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where admissionDate is not null
        defaultClassStudentShouldBeFound("admissionDate.specified=true");

        // Get all the classStudentList where admissionDate is null
        defaultClassStudentShouldNotBeFound("admissionDate.specified=false");
    }

    @Test
    @Transactional
    void getAllClassStudentsByAdmissionDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where admissionDate is greater than or equal to DEFAULT_ADMISSION_DATE
        defaultClassStudentShouldBeFound("admissionDate.greaterThanOrEqual=" + DEFAULT_ADMISSION_DATE);

        // Get all the classStudentList where admissionDate is greater than or equal to UPDATED_ADMISSION_DATE
        defaultClassStudentShouldNotBeFound("admissionDate.greaterThanOrEqual=" + UPDATED_ADMISSION_DATE);
    }

    @Test
    @Transactional
    void getAllClassStudentsByAdmissionDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where admissionDate is less than or equal to DEFAULT_ADMISSION_DATE
        defaultClassStudentShouldBeFound("admissionDate.lessThanOrEqual=" + DEFAULT_ADMISSION_DATE);

        // Get all the classStudentList where admissionDate is less than or equal to SMALLER_ADMISSION_DATE
        defaultClassStudentShouldNotBeFound("admissionDate.lessThanOrEqual=" + SMALLER_ADMISSION_DATE);
    }

    @Test
    @Transactional
    void getAllClassStudentsByAdmissionDateIsLessThanSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where admissionDate is less than DEFAULT_ADMISSION_DATE
        defaultClassStudentShouldNotBeFound("admissionDate.lessThan=" + DEFAULT_ADMISSION_DATE);

        // Get all the classStudentList where admissionDate is less than UPDATED_ADMISSION_DATE
        defaultClassStudentShouldBeFound("admissionDate.lessThan=" + UPDATED_ADMISSION_DATE);
    }

    @Test
    @Transactional
    void getAllClassStudentsByAdmissionDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where admissionDate is greater than DEFAULT_ADMISSION_DATE
        defaultClassStudentShouldNotBeFound("admissionDate.greaterThan=" + DEFAULT_ADMISSION_DATE);

        // Get all the classStudentList where admissionDate is greater than SMALLER_ADMISSION_DATE
        defaultClassStudentShouldBeFound("admissionDate.greaterThan=" + SMALLER_ADMISSION_DATE);
    }

    @Test
    @Transactional
    void getAllClassStudentsByRegNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where regNumber equals to DEFAULT_REG_NUMBER
        defaultClassStudentShouldBeFound("regNumber.equals=" + DEFAULT_REG_NUMBER);

        // Get all the classStudentList where regNumber equals to UPDATED_REG_NUMBER
        defaultClassStudentShouldNotBeFound("regNumber.equals=" + UPDATED_REG_NUMBER);
    }

    @Test
    @Transactional
    void getAllClassStudentsByRegNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where regNumber not equals to DEFAULT_REG_NUMBER
        defaultClassStudentShouldNotBeFound("regNumber.notEquals=" + DEFAULT_REG_NUMBER);

        // Get all the classStudentList where regNumber not equals to UPDATED_REG_NUMBER
        defaultClassStudentShouldBeFound("regNumber.notEquals=" + UPDATED_REG_NUMBER);
    }

    @Test
    @Transactional
    void getAllClassStudentsByRegNumberIsInShouldWork() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where regNumber in DEFAULT_REG_NUMBER or UPDATED_REG_NUMBER
        defaultClassStudentShouldBeFound("regNumber.in=" + DEFAULT_REG_NUMBER + "," + UPDATED_REG_NUMBER);

        // Get all the classStudentList where regNumber equals to UPDATED_REG_NUMBER
        defaultClassStudentShouldNotBeFound("regNumber.in=" + UPDATED_REG_NUMBER);
    }

    @Test
    @Transactional
    void getAllClassStudentsByRegNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where regNumber is not null
        defaultClassStudentShouldBeFound("regNumber.specified=true");

        // Get all the classStudentList where regNumber is null
        defaultClassStudentShouldNotBeFound("regNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllClassStudentsByRegNumberContainsSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where regNumber contains DEFAULT_REG_NUMBER
        defaultClassStudentShouldBeFound("regNumber.contains=" + DEFAULT_REG_NUMBER);

        // Get all the classStudentList where regNumber contains UPDATED_REG_NUMBER
        defaultClassStudentShouldNotBeFound("regNumber.contains=" + UPDATED_REG_NUMBER);
    }

    @Test
    @Transactional
    void getAllClassStudentsByRegNumberNotContainsSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where regNumber does not contain DEFAULT_REG_NUMBER
        defaultClassStudentShouldNotBeFound("regNumber.doesNotContain=" + DEFAULT_REG_NUMBER);

        // Get all the classStudentList where regNumber does not contain UPDATED_REG_NUMBER
        defaultClassStudentShouldBeFound("regNumber.doesNotContain=" + UPDATED_REG_NUMBER);
    }

    @Test
    @Transactional
    void getAllClassStudentsByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where endDate equals to DEFAULT_END_DATE
        defaultClassStudentShouldBeFound("endDate.equals=" + DEFAULT_END_DATE);

        // Get all the classStudentList where endDate equals to UPDATED_END_DATE
        defaultClassStudentShouldNotBeFound("endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllClassStudentsByEndDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where endDate not equals to DEFAULT_END_DATE
        defaultClassStudentShouldNotBeFound("endDate.notEquals=" + DEFAULT_END_DATE);

        // Get all the classStudentList where endDate not equals to UPDATED_END_DATE
        defaultClassStudentShouldBeFound("endDate.notEquals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllClassStudentsByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where endDate in DEFAULT_END_DATE or UPDATED_END_DATE
        defaultClassStudentShouldBeFound("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE);

        // Get all the classStudentList where endDate equals to UPDATED_END_DATE
        defaultClassStudentShouldNotBeFound("endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllClassStudentsByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where endDate is not null
        defaultClassStudentShouldBeFound("endDate.specified=true");

        // Get all the classStudentList where endDate is null
        defaultClassStudentShouldNotBeFound("endDate.specified=false");
    }

    @Test
    @Transactional
    void getAllClassStudentsByEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where endDate is greater than or equal to DEFAULT_END_DATE
        defaultClassStudentShouldBeFound("endDate.greaterThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the classStudentList where endDate is greater than or equal to UPDATED_END_DATE
        defaultClassStudentShouldNotBeFound("endDate.greaterThanOrEqual=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllClassStudentsByEndDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where endDate is less than or equal to DEFAULT_END_DATE
        defaultClassStudentShouldBeFound("endDate.lessThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the classStudentList where endDate is less than or equal to SMALLER_END_DATE
        defaultClassStudentShouldNotBeFound("endDate.lessThanOrEqual=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    void getAllClassStudentsByEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where endDate is less than DEFAULT_END_DATE
        defaultClassStudentShouldNotBeFound("endDate.lessThan=" + DEFAULT_END_DATE);

        // Get all the classStudentList where endDate is less than UPDATED_END_DATE
        defaultClassStudentShouldBeFound("endDate.lessThan=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllClassStudentsByEndDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where endDate is greater than DEFAULT_END_DATE
        defaultClassStudentShouldNotBeFound("endDate.greaterThan=" + DEFAULT_END_DATE);

        // Get all the classStudentList where endDate is greater than SMALLER_END_DATE
        defaultClassStudentShouldBeFound("endDate.greaterThan=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    void getAllClassStudentsByCreateDateIsEqualToSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where createDate equals to DEFAULT_CREATE_DATE
        defaultClassStudentShouldBeFound("createDate.equals=" + DEFAULT_CREATE_DATE);

        // Get all the classStudentList where createDate equals to UPDATED_CREATE_DATE
        defaultClassStudentShouldNotBeFound("createDate.equals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllClassStudentsByCreateDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where createDate not equals to DEFAULT_CREATE_DATE
        defaultClassStudentShouldNotBeFound("createDate.notEquals=" + DEFAULT_CREATE_DATE);

        // Get all the classStudentList where createDate not equals to UPDATED_CREATE_DATE
        defaultClassStudentShouldBeFound("createDate.notEquals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllClassStudentsByCreateDateIsInShouldWork() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where createDate in DEFAULT_CREATE_DATE or UPDATED_CREATE_DATE
        defaultClassStudentShouldBeFound("createDate.in=" + DEFAULT_CREATE_DATE + "," + UPDATED_CREATE_DATE);

        // Get all the classStudentList where createDate equals to UPDATED_CREATE_DATE
        defaultClassStudentShouldNotBeFound("createDate.in=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllClassStudentsByCreateDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where createDate is not null
        defaultClassStudentShouldBeFound("createDate.specified=true");

        // Get all the classStudentList where createDate is null
        defaultClassStudentShouldNotBeFound("createDate.specified=false");
    }

    @Test
    @Transactional
    void getAllClassStudentsByCreateDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where createDate is greater than or equal to DEFAULT_CREATE_DATE
        defaultClassStudentShouldBeFound("createDate.greaterThanOrEqual=" + DEFAULT_CREATE_DATE);

        // Get all the classStudentList where createDate is greater than or equal to UPDATED_CREATE_DATE
        defaultClassStudentShouldNotBeFound("createDate.greaterThanOrEqual=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllClassStudentsByCreateDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where createDate is less than or equal to DEFAULT_CREATE_DATE
        defaultClassStudentShouldBeFound("createDate.lessThanOrEqual=" + DEFAULT_CREATE_DATE);

        // Get all the classStudentList where createDate is less than or equal to SMALLER_CREATE_DATE
        defaultClassStudentShouldNotBeFound("createDate.lessThanOrEqual=" + SMALLER_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllClassStudentsByCreateDateIsLessThanSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where createDate is less than DEFAULT_CREATE_DATE
        defaultClassStudentShouldNotBeFound("createDate.lessThan=" + DEFAULT_CREATE_DATE);

        // Get all the classStudentList where createDate is less than UPDATED_CREATE_DATE
        defaultClassStudentShouldBeFound("createDate.lessThan=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllClassStudentsByCreateDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where createDate is greater than DEFAULT_CREATE_DATE
        defaultClassStudentShouldNotBeFound("createDate.greaterThan=" + DEFAULT_CREATE_DATE);

        // Get all the classStudentList where createDate is greater than SMALLER_CREATE_DATE
        defaultClassStudentShouldBeFound("createDate.greaterThan=" + SMALLER_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllClassStudentsByLastModifiedIsEqualToSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where lastModified equals to DEFAULT_LAST_MODIFIED
        defaultClassStudentShouldBeFound("lastModified.equals=" + DEFAULT_LAST_MODIFIED);

        // Get all the classStudentList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultClassStudentShouldNotBeFound("lastModified.equals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllClassStudentsByLastModifiedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where lastModified not equals to DEFAULT_LAST_MODIFIED
        defaultClassStudentShouldNotBeFound("lastModified.notEquals=" + DEFAULT_LAST_MODIFIED);

        // Get all the classStudentList where lastModified not equals to UPDATED_LAST_MODIFIED
        defaultClassStudentShouldBeFound("lastModified.notEquals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllClassStudentsByLastModifiedIsInShouldWork() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where lastModified in DEFAULT_LAST_MODIFIED or UPDATED_LAST_MODIFIED
        defaultClassStudentShouldBeFound("lastModified.in=" + DEFAULT_LAST_MODIFIED + "," + UPDATED_LAST_MODIFIED);

        // Get all the classStudentList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultClassStudentShouldNotBeFound("lastModified.in=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllClassStudentsByLastModifiedIsNullOrNotNull() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where lastModified is not null
        defaultClassStudentShouldBeFound("lastModified.specified=true");

        // Get all the classStudentList where lastModified is null
        defaultClassStudentShouldNotBeFound("lastModified.specified=false");
    }

    @Test
    @Transactional
    void getAllClassStudentsByLastModifiedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where lastModified is greater than or equal to DEFAULT_LAST_MODIFIED
        defaultClassStudentShouldBeFound("lastModified.greaterThanOrEqual=" + DEFAULT_LAST_MODIFIED);

        // Get all the classStudentList where lastModified is greater than or equal to UPDATED_LAST_MODIFIED
        defaultClassStudentShouldNotBeFound("lastModified.greaterThanOrEqual=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllClassStudentsByLastModifiedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where lastModified is less than or equal to DEFAULT_LAST_MODIFIED
        defaultClassStudentShouldBeFound("lastModified.lessThanOrEqual=" + DEFAULT_LAST_MODIFIED);

        // Get all the classStudentList where lastModified is less than or equal to SMALLER_LAST_MODIFIED
        defaultClassStudentShouldNotBeFound("lastModified.lessThanOrEqual=" + SMALLER_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllClassStudentsByLastModifiedIsLessThanSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where lastModified is less than DEFAULT_LAST_MODIFIED
        defaultClassStudentShouldNotBeFound("lastModified.lessThan=" + DEFAULT_LAST_MODIFIED);

        // Get all the classStudentList where lastModified is less than UPDATED_LAST_MODIFIED
        defaultClassStudentShouldBeFound("lastModified.lessThan=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllClassStudentsByLastModifiedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where lastModified is greater than DEFAULT_LAST_MODIFIED
        defaultClassStudentShouldNotBeFound("lastModified.greaterThan=" + DEFAULT_LAST_MODIFIED);

        // Get all the classStudentList where lastModified is greater than SMALLER_LAST_MODIFIED
        defaultClassStudentShouldBeFound("lastModified.greaterThan=" + SMALLER_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllClassStudentsByCancelDateIsEqualToSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where cancelDate equals to DEFAULT_CANCEL_DATE
        defaultClassStudentShouldBeFound("cancelDate.equals=" + DEFAULT_CANCEL_DATE);

        // Get all the classStudentList where cancelDate equals to UPDATED_CANCEL_DATE
        defaultClassStudentShouldNotBeFound("cancelDate.equals=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllClassStudentsByCancelDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where cancelDate not equals to DEFAULT_CANCEL_DATE
        defaultClassStudentShouldNotBeFound("cancelDate.notEquals=" + DEFAULT_CANCEL_DATE);

        // Get all the classStudentList where cancelDate not equals to UPDATED_CANCEL_DATE
        defaultClassStudentShouldBeFound("cancelDate.notEquals=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllClassStudentsByCancelDateIsInShouldWork() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where cancelDate in DEFAULT_CANCEL_DATE or UPDATED_CANCEL_DATE
        defaultClassStudentShouldBeFound("cancelDate.in=" + DEFAULT_CANCEL_DATE + "," + UPDATED_CANCEL_DATE);

        // Get all the classStudentList where cancelDate equals to UPDATED_CANCEL_DATE
        defaultClassStudentShouldNotBeFound("cancelDate.in=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllClassStudentsByCancelDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where cancelDate is not null
        defaultClassStudentShouldBeFound("cancelDate.specified=true");

        // Get all the classStudentList where cancelDate is null
        defaultClassStudentShouldNotBeFound("cancelDate.specified=false");
    }

    @Test
    @Transactional
    void getAllClassStudentsByCancelDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where cancelDate is greater than or equal to DEFAULT_CANCEL_DATE
        defaultClassStudentShouldBeFound("cancelDate.greaterThanOrEqual=" + DEFAULT_CANCEL_DATE);

        // Get all the classStudentList where cancelDate is greater than or equal to UPDATED_CANCEL_DATE
        defaultClassStudentShouldNotBeFound("cancelDate.greaterThanOrEqual=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllClassStudentsByCancelDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where cancelDate is less than or equal to DEFAULT_CANCEL_DATE
        defaultClassStudentShouldBeFound("cancelDate.lessThanOrEqual=" + DEFAULT_CANCEL_DATE);

        // Get all the classStudentList where cancelDate is less than or equal to SMALLER_CANCEL_DATE
        defaultClassStudentShouldNotBeFound("cancelDate.lessThanOrEqual=" + SMALLER_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllClassStudentsByCancelDateIsLessThanSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where cancelDate is less than DEFAULT_CANCEL_DATE
        defaultClassStudentShouldNotBeFound("cancelDate.lessThan=" + DEFAULT_CANCEL_DATE);

        // Get all the classStudentList where cancelDate is less than UPDATED_CANCEL_DATE
        defaultClassStudentShouldBeFound("cancelDate.lessThan=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllClassStudentsByCancelDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        // Get all the classStudentList where cancelDate is greater than DEFAULT_CANCEL_DATE
        defaultClassStudentShouldNotBeFound("cancelDate.greaterThan=" + DEFAULT_CANCEL_DATE);

        // Get all the classStudentList where cancelDate is greater than SMALLER_CANCEL_DATE
        defaultClassStudentShouldBeFound("cancelDate.greaterThan=" + SMALLER_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllClassStudentsByStudentDiscountIsEqualToSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);
        StudentDiscount studentDiscount = StudentDiscountResourceIT.createEntity(em);
        em.persist(studentDiscount);
        em.flush();
        classStudent.addStudentDiscount(studentDiscount);
        classStudentRepository.saveAndFlush(classStudent);
        Long studentDiscountId = studentDiscount.getId();

        // Get all the classStudentList where studentDiscount equals to studentDiscountId
        defaultClassStudentShouldBeFound("studentDiscountId.equals=" + studentDiscountId);

        // Get all the classStudentList where studentDiscount equals to (studentDiscountId + 1)
        defaultClassStudentShouldNotBeFound("studentDiscountId.equals=" + (studentDiscountId + 1));
    }

    @Test
    @Transactional
    void getAllClassStudentsByStudentAdditionalChargesIsEqualToSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);
        StudentAdditionalCharges studentAdditionalCharges = StudentAdditionalChargesResourceIT.createEntity(em);
        em.persist(studentAdditionalCharges);
        em.flush();
        classStudent.addStudentAdditionalCharges(studentAdditionalCharges);
        classStudentRepository.saveAndFlush(classStudent);
        Long studentAdditionalChargesId = studentAdditionalCharges.getId();

        // Get all the classStudentList where studentAdditionalCharges equals to studentAdditionalChargesId
        defaultClassStudentShouldBeFound("studentAdditionalChargesId.equals=" + studentAdditionalChargesId);

        // Get all the classStudentList where studentAdditionalCharges equals to (studentAdditionalChargesId + 1)
        defaultClassStudentShouldNotBeFound("studentAdditionalChargesId.equals=" + (studentAdditionalChargesId + 1));
    }

    @Test
    @Transactional
    void getAllClassStudentsByStudentChargesSummaryIsEqualToSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);
        StudentChargesSummary studentChargesSummary = StudentChargesSummaryResourceIT.createEntity(em);
        em.persist(studentChargesSummary);
        em.flush();
        classStudent.addStudentChargesSummary(studentChargesSummary);
        classStudentRepository.saveAndFlush(classStudent);
        Long studentChargesSummaryId = studentChargesSummary.getId();

        // Get all the classStudentList where studentChargesSummary equals to studentChargesSummaryId
        defaultClassStudentShouldBeFound("studentChargesSummaryId.equals=" + studentChargesSummaryId);

        // Get all the classStudentList where studentChargesSummary equals to (studentChargesSummaryId + 1)
        defaultClassStudentShouldNotBeFound("studentChargesSummaryId.equals=" + (studentChargesSummaryId + 1));
    }

    @Test
    @Transactional
    void getAllClassStudentsByStudentPaymentsIsEqualToSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);
        StudentPayments studentPayments = StudentPaymentsResourceIT.createEntity(em);
        em.persist(studentPayments);
        em.flush();
        classStudent.addStudentPayments(studentPayments);
        classStudentRepository.saveAndFlush(classStudent);
        Long studentPaymentsId = studentPayments.getId();

        // Get all the classStudentList where studentPayments equals to studentPaymentsId
        defaultClassStudentShouldBeFound("studentPaymentsId.equals=" + studentPaymentsId);

        // Get all the classStudentList where studentPayments equals to (studentPaymentsId + 1)
        defaultClassStudentShouldNotBeFound("studentPaymentsId.equals=" + (studentPaymentsId + 1));
    }

    @Test
    @Transactional
    void getAllClassStudentsByStudentAttendenceIsEqualToSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);
        StudentAttendence studentAttendence = StudentAttendenceResourceIT.createEntity(em);
        em.persist(studentAttendence);
        em.flush();
        classStudent.addStudentAttendence(studentAttendence);
        classStudentRepository.saveAndFlush(classStudent);
        Long studentAttendenceId = studentAttendence.getId();

        // Get all the classStudentList where studentAttendence equals to studentAttendenceId
        defaultClassStudentShouldBeFound("studentAttendenceId.equals=" + studentAttendenceId);

        // Get all the classStudentList where studentAttendence equals to (studentAttendenceId + 1)
        defaultClassStudentShouldNotBeFound("studentAttendenceId.equals=" + (studentAttendenceId + 1));
    }

    @Test
    @Transactional
    void getAllClassStudentsByStudentHomeWorkTrackIsEqualToSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);
        StudentHomeWorkTrack studentHomeWorkTrack = StudentHomeWorkTrackResourceIT.createEntity(em);
        em.persist(studentHomeWorkTrack);
        em.flush();
        classStudent.addStudentHomeWorkTrack(studentHomeWorkTrack);
        classStudentRepository.saveAndFlush(classStudent);
        Long studentHomeWorkTrackId = studentHomeWorkTrack.getId();

        // Get all the classStudentList where studentHomeWorkTrack equals to studentHomeWorkTrackId
        defaultClassStudentShouldBeFound("studentHomeWorkTrackId.equals=" + studentHomeWorkTrackId);

        // Get all the classStudentList where studentHomeWorkTrack equals to (studentHomeWorkTrackId + 1)
        defaultClassStudentShouldNotBeFound("studentHomeWorkTrackId.equals=" + (studentHomeWorkTrackId + 1));
    }

    @Test
    @Transactional
    void getAllClassStudentsByStudentClassWorkTrackIsEqualToSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);
        StudentClassWorkTrack studentClassWorkTrack = StudentClassWorkTrackResourceIT.createEntity(em);
        em.persist(studentClassWorkTrack);
        em.flush();
        classStudent.addStudentClassWorkTrack(studentClassWorkTrack);
        classStudentRepository.saveAndFlush(classStudent);
        Long studentClassWorkTrackId = studentClassWorkTrack.getId();

        // Get all the classStudentList where studentClassWorkTrack equals to studentClassWorkTrackId
        defaultClassStudentShouldBeFound("studentClassWorkTrackId.equals=" + studentClassWorkTrackId);

        // Get all the classStudentList where studentClassWorkTrack equals to (studentClassWorkTrackId + 1)
        defaultClassStudentShouldNotBeFound("studentClassWorkTrackId.equals=" + (studentClassWorkTrackId + 1));
    }

    @Test
    @Transactional
    void getAllClassStudentsBySchoolClassIsEqualToSomething() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);
        SchoolClass schoolClass = SchoolClassResourceIT.createEntity(em);
        em.persist(schoolClass);
        em.flush();
        classStudent.setSchoolClass(schoolClass);
        classStudentRepository.saveAndFlush(classStudent);
        Long schoolClassId = schoolClass.getId();

        // Get all the classStudentList where schoolClass equals to schoolClassId
        defaultClassStudentShouldBeFound("schoolClassId.equals=" + schoolClassId);

        // Get all the classStudentList where schoolClass equals to (schoolClassId + 1)
        defaultClassStudentShouldNotBeFound("schoolClassId.equals=" + (schoolClassId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultClassStudentShouldBeFound(String filter) throws Exception {
        restClassStudentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(classStudent.getId().intValue())))
            .andExpect(jsonPath("$.[*].studentPhotoContentType").value(hasItem(DEFAULT_STUDENT_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].studentPhoto").value(hasItem(Base64Utils.encodeToString(DEFAULT_STUDENT_PHOTO))))
            .andExpect(jsonPath("$.[*].studentPhotoLink").value(hasItem(DEFAULT_STUDENT_PHOTO_LINK)))
            .andExpect(jsonPath("$.[*].studentId").value(hasItem(DEFAULT_STUDENT_ID)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].rollNumber").value(hasItem(DEFAULT_ROLL_NUMBER)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].bloodGroup").value(hasItem(DEFAULT_BLOOD_GROUP.toString())))
            .andExpect(jsonPath("$.[*].dateOfBirth").value(hasItem(DEFAULT_DATE_OF_BIRTH.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].addressLine1").value(hasItem(DEFAULT_ADDRESS_LINE_1)))
            .andExpect(jsonPath("$.[*].addressLine2").value(hasItem(DEFAULT_ADDRESS_LINE_2)))
            .andExpect(jsonPath("$.[*].nickName").value(hasItem(DEFAULT_NICK_NAME)))
            .andExpect(jsonPath("$.[*].fatherName").value(hasItem(DEFAULT_FATHER_NAME)))
            .andExpect(jsonPath("$.[*].motherName").value(hasItem(DEFAULT_MOTHER_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].admissionDate").value(hasItem(DEFAULT_ADMISSION_DATE.toString())))
            .andExpect(jsonPath("$.[*].regNumber").value(hasItem(DEFAULT_REG_NUMBER)))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].cancelDate").value(hasItem(DEFAULT_CANCEL_DATE.toString())));

        // Check, that the count call also returns 1
        restClassStudentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultClassStudentShouldNotBeFound(String filter) throws Exception {
        restClassStudentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restClassStudentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingClassStudent() throws Exception {
        // Get the classStudent
        restClassStudentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewClassStudent() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        int databaseSizeBeforeUpdate = classStudentRepository.findAll().size();

        // Update the classStudent
        ClassStudent updatedClassStudent = classStudentRepository.findById(classStudent.getId()).get();
        // Disconnect from session so that the updates on updatedClassStudent are not directly saved in db
        em.detach(updatedClassStudent);
        updatedClassStudent
            .studentPhoto(UPDATED_STUDENT_PHOTO)
            .studentPhotoContentType(UPDATED_STUDENT_PHOTO_CONTENT_TYPE)
            .studentPhotoLink(UPDATED_STUDENT_PHOTO_LINK)
            .studentId(UPDATED_STUDENT_ID)
            .firstName(UPDATED_FIRST_NAME)
            .gender(UPDATED_GENDER)
            .lastName(UPDATED_LAST_NAME)
            .rollNumber(UPDATED_ROLL_NUMBER)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .bloodGroup(UPDATED_BLOOD_GROUP)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .startDate(UPDATED_START_DATE)
            .addressLine1(UPDATED_ADDRESS_LINE_1)
            .addressLine2(UPDATED_ADDRESS_LINE_2)
            .nickName(UPDATED_NICK_NAME)
            .fatherName(UPDATED_FATHER_NAME)
            .motherName(UPDATED_MOTHER_NAME)
            .email(UPDATED_EMAIL)
            .admissionDate(UPDATED_ADMISSION_DATE)
            .regNumber(UPDATED_REG_NUMBER)
            .endDate(UPDATED_END_DATE)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);
        ClassStudentDTO classStudentDTO = classStudentMapper.toDto(updatedClassStudent);

        restClassStudentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, classStudentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(classStudentDTO))
            )
            .andExpect(status().isOk());

        // Validate the ClassStudent in the database
        List<ClassStudent> classStudentList = classStudentRepository.findAll();
        assertThat(classStudentList).hasSize(databaseSizeBeforeUpdate);
        ClassStudent testClassStudent = classStudentList.get(classStudentList.size() - 1);
        assertThat(testClassStudent.getStudentPhoto()).isEqualTo(UPDATED_STUDENT_PHOTO);
        assertThat(testClassStudent.getStudentPhotoContentType()).isEqualTo(UPDATED_STUDENT_PHOTO_CONTENT_TYPE);
        assertThat(testClassStudent.getStudentPhotoLink()).isEqualTo(UPDATED_STUDENT_PHOTO_LINK);
        assertThat(testClassStudent.getStudentId()).isEqualTo(UPDATED_STUDENT_ID);
        assertThat(testClassStudent.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testClassStudent.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testClassStudent.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testClassStudent.getRollNumber()).isEqualTo(UPDATED_ROLL_NUMBER);
        assertThat(testClassStudent.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testClassStudent.getBloodGroup()).isEqualTo(UPDATED_BLOOD_GROUP);
        assertThat(testClassStudent.getDateOfBirth()).isEqualTo(UPDATED_DATE_OF_BIRTH);
        assertThat(testClassStudent.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testClassStudent.getAddressLine1()).isEqualTo(UPDATED_ADDRESS_LINE_1);
        assertThat(testClassStudent.getAddressLine2()).isEqualTo(UPDATED_ADDRESS_LINE_2);
        assertThat(testClassStudent.getNickName()).isEqualTo(UPDATED_NICK_NAME);
        assertThat(testClassStudent.getFatherName()).isEqualTo(UPDATED_FATHER_NAME);
        assertThat(testClassStudent.getMotherName()).isEqualTo(UPDATED_MOTHER_NAME);
        assertThat(testClassStudent.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testClassStudent.getAdmissionDate()).isEqualTo(UPDATED_ADMISSION_DATE);
        assertThat(testClassStudent.getRegNumber()).isEqualTo(UPDATED_REG_NUMBER);
        assertThat(testClassStudent.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testClassStudent.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testClassStudent.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testClassStudent.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void putNonExistingClassStudent() throws Exception {
        int databaseSizeBeforeUpdate = classStudentRepository.findAll().size();
        classStudent.setId(count.incrementAndGet());

        // Create the ClassStudent
        ClassStudentDTO classStudentDTO = classStudentMapper.toDto(classStudent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClassStudentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, classStudentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(classStudentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClassStudent in the database
        List<ClassStudent> classStudentList = classStudentRepository.findAll();
        assertThat(classStudentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchClassStudent() throws Exception {
        int databaseSizeBeforeUpdate = classStudentRepository.findAll().size();
        classStudent.setId(count.incrementAndGet());

        // Create the ClassStudent
        ClassStudentDTO classStudentDTO = classStudentMapper.toDto(classStudent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClassStudentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(classStudentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClassStudent in the database
        List<ClassStudent> classStudentList = classStudentRepository.findAll();
        assertThat(classStudentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamClassStudent() throws Exception {
        int databaseSizeBeforeUpdate = classStudentRepository.findAll().size();
        classStudent.setId(count.incrementAndGet());

        // Create the ClassStudent
        ClassStudentDTO classStudentDTO = classStudentMapper.toDto(classStudent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClassStudentMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(classStudentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ClassStudent in the database
        List<ClassStudent> classStudentList = classStudentRepository.findAll();
        assertThat(classStudentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateClassStudentWithPatch() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        int databaseSizeBeforeUpdate = classStudentRepository.findAll().size();

        // Update the classStudent using partial update
        ClassStudent partialUpdatedClassStudent = new ClassStudent();
        partialUpdatedClassStudent.setId(classStudent.getId());

        partialUpdatedClassStudent
            .studentPhoto(UPDATED_STUDENT_PHOTO)
            .studentPhotoContentType(UPDATED_STUDENT_PHOTO_CONTENT_TYPE)
            .firstName(UPDATED_FIRST_NAME)
            .gender(UPDATED_GENDER)
            .bloodGroup(UPDATED_BLOOD_GROUP)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .email(UPDATED_EMAIL)
            .admissionDate(UPDATED_ADMISSION_DATE)
            .regNumber(UPDATED_REG_NUMBER)
            .endDate(UPDATED_END_DATE);

        restClassStudentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClassStudent.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedClassStudent))
            )
            .andExpect(status().isOk());

        // Validate the ClassStudent in the database
        List<ClassStudent> classStudentList = classStudentRepository.findAll();
        assertThat(classStudentList).hasSize(databaseSizeBeforeUpdate);
        ClassStudent testClassStudent = classStudentList.get(classStudentList.size() - 1);
        assertThat(testClassStudent.getStudentPhoto()).isEqualTo(UPDATED_STUDENT_PHOTO);
        assertThat(testClassStudent.getStudentPhotoContentType()).isEqualTo(UPDATED_STUDENT_PHOTO_CONTENT_TYPE);
        assertThat(testClassStudent.getStudentPhotoLink()).isEqualTo(DEFAULT_STUDENT_PHOTO_LINK);
        assertThat(testClassStudent.getStudentId()).isEqualTo(DEFAULT_STUDENT_ID);
        assertThat(testClassStudent.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testClassStudent.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testClassStudent.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testClassStudent.getRollNumber()).isEqualTo(DEFAULT_ROLL_NUMBER);
        assertThat(testClassStudent.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testClassStudent.getBloodGroup()).isEqualTo(UPDATED_BLOOD_GROUP);
        assertThat(testClassStudent.getDateOfBirth()).isEqualTo(UPDATED_DATE_OF_BIRTH);
        assertThat(testClassStudent.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testClassStudent.getAddressLine1()).isEqualTo(DEFAULT_ADDRESS_LINE_1);
        assertThat(testClassStudent.getAddressLine2()).isEqualTo(DEFAULT_ADDRESS_LINE_2);
        assertThat(testClassStudent.getNickName()).isEqualTo(DEFAULT_NICK_NAME);
        assertThat(testClassStudent.getFatherName()).isEqualTo(DEFAULT_FATHER_NAME);
        assertThat(testClassStudent.getMotherName()).isEqualTo(DEFAULT_MOTHER_NAME);
        assertThat(testClassStudent.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testClassStudent.getAdmissionDate()).isEqualTo(UPDATED_ADMISSION_DATE);
        assertThat(testClassStudent.getRegNumber()).isEqualTo(UPDATED_REG_NUMBER);
        assertThat(testClassStudent.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testClassStudent.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testClassStudent.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
        assertThat(testClassStudent.getCancelDate()).isEqualTo(DEFAULT_CANCEL_DATE);
    }

    @Test
    @Transactional
    void fullUpdateClassStudentWithPatch() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        int databaseSizeBeforeUpdate = classStudentRepository.findAll().size();

        // Update the classStudent using partial update
        ClassStudent partialUpdatedClassStudent = new ClassStudent();
        partialUpdatedClassStudent.setId(classStudent.getId());

        partialUpdatedClassStudent
            .studentPhoto(UPDATED_STUDENT_PHOTO)
            .studentPhotoContentType(UPDATED_STUDENT_PHOTO_CONTENT_TYPE)
            .studentPhotoLink(UPDATED_STUDENT_PHOTO_LINK)
            .studentId(UPDATED_STUDENT_ID)
            .firstName(UPDATED_FIRST_NAME)
            .gender(UPDATED_GENDER)
            .lastName(UPDATED_LAST_NAME)
            .rollNumber(UPDATED_ROLL_NUMBER)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .bloodGroup(UPDATED_BLOOD_GROUP)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .startDate(UPDATED_START_DATE)
            .addressLine1(UPDATED_ADDRESS_LINE_1)
            .addressLine2(UPDATED_ADDRESS_LINE_2)
            .nickName(UPDATED_NICK_NAME)
            .fatherName(UPDATED_FATHER_NAME)
            .motherName(UPDATED_MOTHER_NAME)
            .email(UPDATED_EMAIL)
            .admissionDate(UPDATED_ADMISSION_DATE)
            .regNumber(UPDATED_REG_NUMBER)
            .endDate(UPDATED_END_DATE)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);

        restClassStudentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClassStudent.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedClassStudent))
            )
            .andExpect(status().isOk());

        // Validate the ClassStudent in the database
        List<ClassStudent> classStudentList = classStudentRepository.findAll();
        assertThat(classStudentList).hasSize(databaseSizeBeforeUpdate);
        ClassStudent testClassStudent = classStudentList.get(classStudentList.size() - 1);
        assertThat(testClassStudent.getStudentPhoto()).isEqualTo(UPDATED_STUDENT_PHOTO);
        assertThat(testClassStudent.getStudentPhotoContentType()).isEqualTo(UPDATED_STUDENT_PHOTO_CONTENT_TYPE);
        assertThat(testClassStudent.getStudentPhotoLink()).isEqualTo(UPDATED_STUDENT_PHOTO_LINK);
        assertThat(testClassStudent.getStudentId()).isEqualTo(UPDATED_STUDENT_ID);
        assertThat(testClassStudent.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testClassStudent.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testClassStudent.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testClassStudent.getRollNumber()).isEqualTo(UPDATED_ROLL_NUMBER);
        assertThat(testClassStudent.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testClassStudent.getBloodGroup()).isEqualTo(UPDATED_BLOOD_GROUP);
        assertThat(testClassStudent.getDateOfBirth()).isEqualTo(UPDATED_DATE_OF_BIRTH);
        assertThat(testClassStudent.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testClassStudent.getAddressLine1()).isEqualTo(UPDATED_ADDRESS_LINE_1);
        assertThat(testClassStudent.getAddressLine2()).isEqualTo(UPDATED_ADDRESS_LINE_2);
        assertThat(testClassStudent.getNickName()).isEqualTo(UPDATED_NICK_NAME);
        assertThat(testClassStudent.getFatherName()).isEqualTo(UPDATED_FATHER_NAME);
        assertThat(testClassStudent.getMotherName()).isEqualTo(UPDATED_MOTHER_NAME);
        assertThat(testClassStudent.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testClassStudent.getAdmissionDate()).isEqualTo(UPDATED_ADMISSION_DATE);
        assertThat(testClassStudent.getRegNumber()).isEqualTo(UPDATED_REG_NUMBER);
        assertThat(testClassStudent.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testClassStudent.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testClassStudent.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testClassStudent.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingClassStudent() throws Exception {
        int databaseSizeBeforeUpdate = classStudentRepository.findAll().size();
        classStudent.setId(count.incrementAndGet());

        // Create the ClassStudent
        ClassStudentDTO classStudentDTO = classStudentMapper.toDto(classStudent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClassStudentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, classStudentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(classStudentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClassStudent in the database
        List<ClassStudent> classStudentList = classStudentRepository.findAll();
        assertThat(classStudentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchClassStudent() throws Exception {
        int databaseSizeBeforeUpdate = classStudentRepository.findAll().size();
        classStudent.setId(count.incrementAndGet());

        // Create the ClassStudent
        ClassStudentDTO classStudentDTO = classStudentMapper.toDto(classStudent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClassStudentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(classStudentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClassStudent in the database
        List<ClassStudent> classStudentList = classStudentRepository.findAll();
        assertThat(classStudentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamClassStudent() throws Exception {
        int databaseSizeBeforeUpdate = classStudentRepository.findAll().size();
        classStudent.setId(count.incrementAndGet());

        // Create the ClassStudent
        ClassStudentDTO classStudentDTO = classStudentMapper.toDto(classStudent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClassStudentMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(classStudentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ClassStudent in the database
        List<ClassStudent> classStudentList = classStudentRepository.findAll();
        assertThat(classStudentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteClassStudent() throws Exception {
        // Initialize the database
        classStudentRepository.saveAndFlush(classStudent);

        int databaseSizeBeforeDelete = classStudentRepository.findAll().size();

        // Delete the classStudent
        restClassStudentMockMvc
            .perform(delete(ENTITY_API_URL_ID, classStudent.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ClassStudent> classStudentList = classStudentRepository.findAll();
        assertThat(classStudentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
