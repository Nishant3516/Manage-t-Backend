package com.ssik.manageit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ssik.manageit.IntegrationTest;
import com.ssik.manageit.domain.ClassClassWork;
import com.ssik.manageit.domain.ClassStudent;
import com.ssik.manageit.domain.StudentClassWorkTrack;
import com.ssik.manageit.domain.enumeration.WorkStatus;
import com.ssik.manageit.repository.StudentClassWorkTrackRepository;
import com.ssik.manageit.service.criteria.StudentClassWorkTrackCriteria;
import com.ssik.manageit.service.dto.StudentClassWorkTrackDTO;
import com.ssik.manageit.service.mapper.StudentClassWorkTrackMapper;
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
 * Integration tests for the {@link StudentClassWorkTrackResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StudentClassWorkTrackResourceIT {

    private static final WorkStatus DEFAULT_WORK_STATUS = WorkStatus.Done;
    private static final WorkStatus UPDATED_WORK_STATUS = WorkStatus.NotDone;

    private static final String DEFAULT_REMARKS = "AAAAAAAAAA";
    private static final String UPDATED_REMARKS = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATE_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CREATE_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_LAST_MODIFIED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_MODIFIED = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_LAST_MODIFIED = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_CANCEL_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CANCEL_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CANCEL_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/student-class-work-tracks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private StudentClassWorkTrackRepository studentClassWorkTrackRepository;

    @Autowired
    private StudentClassWorkTrackMapper studentClassWorkTrackMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStudentClassWorkTrackMockMvc;

    private StudentClassWorkTrack studentClassWorkTrack;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StudentClassWorkTrack createEntity(EntityManager em) {
        StudentClassWorkTrack studentClassWorkTrack = new StudentClassWorkTrack()
            .workStatus(DEFAULT_WORK_STATUS)
            .remarks(DEFAULT_REMARKS)
            .createDate(DEFAULT_CREATE_DATE)
            .lastModified(DEFAULT_LAST_MODIFIED)
            .cancelDate(DEFAULT_CANCEL_DATE);
        return studentClassWorkTrack;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StudentClassWorkTrack createUpdatedEntity(EntityManager em) {
        StudentClassWorkTrack studentClassWorkTrack = new StudentClassWorkTrack()
            .workStatus(UPDATED_WORK_STATUS)
            .remarks(UPDATED_REMARKS)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);
        return studentClassWorkTrack;
    }

    @BeforeEach
    public void initTest() {
        studentClassWorkTrack = createEntity(em);
    }

    @Test
    @Transactional
    void createStudentClassWorkTrack() throws Exception {
        int databaseSizeBeforeCreate = studentClassWorkTrackRepository.findAll().size();
        // Create the StudentClassWorkTrack
        StudentClassWorkTrackDTO studentClassWorkTrackDTO = studentClassWorkTrackMapper.toDto(studentClassWorkTrack);
        restStudentClassWorkTrackMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(studentClassWorkTrackDTO))
            )
            .andExpect(status().isCreated());

        // Validate the StudentClassWorkTrack in the database
        List<StudentClassWorkTrack> studentClassWorkTrackList = studentClassWorkTrackRepository.findAll();
        assertThat(studentClassWorkTrackList).hasSize(databaseSizeBeforeCreate + 1);
        StudentClassWorkTrack testStudentClassWorkTrack = studentClassWorkTrackList.get(studentClassWorkTrackList.size() - 1);
        assertThat(testStudentClassWorkTrack.getWorkStatus()).isEqualTo(DEFAULT_WORK_STATUS);
        assertThat(testStudentClassWorkTrack.getRemarks()).isEqualTo(DEFAULT_REMARKS);
        assertThat(testStudentClassWorkTrack.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testStudentClassWorkTrack.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
        assertThat(testStudentClassWorkTrack.getCancelDate()).isEqualTo(DEFAULT_CANCEL_DATE);
    }

    @Test
    @Transactional
    void createStudentClassWorkTrackWithExistingId() throws Exception {
        // Create the StudentClassWorkTrack with an existing ID
        studentClassWorkTrack.setId(1L);
        StudentClassWorkTrackDTO studentClassWorkTrackDTO = studentClassWorkTrackMapper.toDto(studentClassWorkTrack);

        int databaseSizeBeforeCreate = studentClassWorkTrackRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStudentClassWorkTrackMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(studentClassWorkTrackDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentClassWorkTrack in the database
        List<StudentClassWorkTrack> studentClassWorkTrackList = studentClassWorkTrackRepository.findAll();
        assertThat(studentClassWorkTrackList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkWorkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = studentClassWorkTrackRepository.findAll().size();
        // set the field null
        studentClassWorkTrack.setWorkStatus(null);

        // Create the StudentClassWorkTrack, which fails.
        StudentClassWorkTrackDTO studentClassWorkTrackDTO = studentClassWorkTrackMapper.toDto(studentClassWorkTrack);

        restStudentClassWorkTrackMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(studentClassWorkTrackDTO))
            )
            .andExpect(status().isBadRequest());

        List<StudentClassWorkTrack> studentClassWorkTrackList = studentClassWorkTrackRepository.findAll();
        assertThat(studentClassWorkTrackList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllStudentClassWorkTracks() throws Exception {
        // Initialize the database
        studentClassWorkTrackRepository.saveAndFlush(studentClassWorkTrack);

        // Get all the studentClassWorkTrackList
        restStudentClassWorkTrackMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(studentClassWorkTrack.getId().intValue())))
            .andExpect(jsonPath("$.[*].workStatus").value(hasItem(DEFAULT_WORK_STATUS.toString())))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].cancelDate").value(hasItem(DEFAULT_CANCEL_DATE.toString())));
    }

    @Test
    @Transactional
    void getStudentClassWorkTrack() throws Exception {
        // Initialize the database
        studentClassWorkTrackRepository.saveAndFlush(studentClassWorkTrack);

        // Get the studentClassWorkTrack
        restStudentClassWorkTrackMockMvc
            .perform(get(ENTITY_API_URL_ID, studentClassWorkTrack.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(studentClassWorkTrack.getId().intValue()))
            .andExpect(jsonPath("$.workStatus").value(DEFAULT_WORK_STATUS.toString()))
            .andExpect(jsonPath("$.remarks").value(DEFAULT_REMARKS))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.lastModified").value(DEFAULT_LAST_MODIFIED.toString()))
            .andExpect(jsonPath("$.cancelDate").value(DEFAULT_CANCEL_DATE.toString()));
    }

    @Test
    @Transactional
    void getStudentClassWorkTracksByIdFiltering() throws Exception {
        // Initialize the database
        studentClassWorkTrackRepository.saveAndFlush(studentClassWorkTrack);

        Long id = studentClassWorkTrack.getId();

        defaultStudentClassWorkTrackShouldBeFound("id.equals=" + id);
        defaultStudentClassWorkTrackShouldNotBeFound("id.notEquals=" + id);

        defaultStudentClassWorkTrackShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultStudentClassWorkTrackShouldNotBeFound("id.greaterThan=" + id);

        defaultStudentClassWorkTrackShouldBeFound("id.lessThanOrEqual=" + id);
        defaultStudentClassWorkTrackShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllStudentClassWorkTracksByWorkStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        studentClassWorkTrackRepository.saveAndFlush(studentClassWorkTrack);

        // Get all the studentClassWorkTrackList where workStatus equals to DEFAULT_WORK_STATUS
        defaultStudentClassWorkTrackShouldBeFound("workStatus.equals=" + DEFAULT_WORK_STATUS);

        // Get all the studentClassWorkTrackList where workStatus equals to UPDATED_WORK_STATUS
        defaultStudentClassWorkTrackShouldNotBeFound("workStatus.equals=" + UPDATED_WORK_STATUS);
    }

    @Test
    @Transactional
    void getAllStudentClassWorkTracksByWorkStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentClassWorkTrackRepository.saveAndFlush(studentClassWorkTrack);

        // Get all the studentClassWorkTrackList where workStatus not equals to DEFAULT_WORK_STATUS
        defaultStudentClassWorkTrackShouldNotBeFound("workStatus.notEquals=" + DEFAULT_WORK_STATUS);

        // Get all the studentClassWorkTrackList where workStatus not equals to UPDATED_WORK_STATUS
        defaultStudentClassWorkTrackShouldBeFound("workStatus.notEquals=" + UPDATED_WORK_STATUS);
    }

    @Test
    @Transactional
    void getAllStudentClassWorkTracksByWorkStatusIsInShouldWork() throws Exception {
        // Initialize the database
        studentClassWorkTrackRepository.saveAndFlush(studentClassWorkTrack);

        // Get all the studentClassWorkTrackList where workStatus in DEFAULT_WORK_STATUS or UPDATED_WORK_STATUS
        defaultStudentClassWorkTrackShouldBeFound("workStatus.in=" + DEFAULT_WORK_STATUS + "," + UPDATED_WORK_STATUS);

        // Get all the studentClassWorkTrackList where workStatus equals to UPDATED_WORK_STATUS
        defaultStudentClassWorkTrackShouldNotBeFound("workStatus.in=" + UPDATED_WORK_STATUS);
    }

    @Test
    @Transactional
    void getAllStudentClassWorkTracksByWorkStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentClassWorkTrackRepository.saveAndFlush(studentClassWorkTrack);

        // Get all the studentClassWorkTrackList where workStatus is not null
        defaultStudentClassWorkTrackShouldBeFound("workStatus.specified=true");

        // Get all the studentClassWorkTrackList where workStatus is null
        defaultStudentClassWorkTrackShouldNotBeFound("workStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentClassWorkTracksByRemarksIsEqualToSomething() throws Exception {
        // Initialize the database
        studentClassWorkTrackRepository.saveAndFlush(studentClassWorkTrack);

        // Get all the studentClassWorkTrackList where remarks equals to DEFAULT_REMARKS
        defaultStudentClassWorkTrackShouldBeFound("remarks.equals=" + DEFAULT_REMARKS);

        // Get all the studentClassWorkTrackList where remarks equals to UPDATED_REMARKS
        defaultStudentClassWorkTrackShouldNotBeFound("remarks.equals=" + UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void getAllStudentClassWorkTracksByRemarksIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentClassWorkTrackRepository.saveAndFlush(studentClassWorkTrack);

        // Get all the studentClassWorkTrackList where remarks not equals to DEFAULT_REMARKS
        defaultStudentClassWorkTrackShouldNotBeFound("remarks.notEquals=" + DEFAULT_REMARKS);

        // Get all the studentClassWorkTrackList where remarks not equals to UPDATED_REMARKS
        defaultStudentClassWorkTrackShouldBeFound("remarks.notEquals=" + UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void getAllStudentClassWorkTracksByRemarksIsInShouldWork() throws Exception {
        // Initialize the database
        studentClassWorkTrackRepository.saveAndFlush(studentClassWorkTrack);

        // Get all the studentClassWorkTrackList where remarks in DEFAULT_REMARKS or UPDATED_REMARKS
        defaultStudentClassWorkTrackShouldBeFound("remarks.in=" + DEFAULT_REMARKS + "," + UPDATED_REMARKS);

        // Get all the studentClassWorkTrackList where remarks equals to UPDATED_REMARKS
        defaultStudentClassWorkTrackShouldNotBeFound("remarks.in=" + UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void getAllStudentClassWorkTracksByRemarksIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentClassWorkTrackRepository.saveAndFlush(studentClassWorkTrack);

        // Get all the studentClassWorkTrackList where remarks is not null
        defaultStudentClassWorkTrackShouldBeFound("remarks.specified=true");

        // Get all the studentClassWorkTrackList where remarks is null
        defaultStudentClassWorkTrackShouldNotBeFound("remarks.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentClassWorkTracksByRemarksContainsSomething() throws Exception {
        // Initialize the database
        studentClassWorkTrackRepository.saveAndFlush(studentClassWorkTrack);

        // Get all the studentClassWorkTrackList where remarks contains DEFAULT_REMARKS
        defaultStudentClassWorkTrackShouldBeFound("remarks.contains=" + DEFAULT_REMARKS);

        // Get all the studentClassWorkTrackList where remarks contains UPDATED_REMARKS
        defaultStudentClassWorkTrackShouldNotBeFound("remarks.contains=" + UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void getAllStudentClassWorkTracksByRemarksNotContainsSomething() throws Exception {
        // Initialize the database
        studentClassWorkTrackRepository.saveAndFlush(studentClassWorkTrack);

        // Get all the studentClassWorkTrackList where remarks does not contain DEFAULT_REMARKS
        defaultStudentClassWorkTrackShouldNotBeFound("remarks.doesNotContain=" + DEFAULT_REMARKS);

        // Get all the studentClassWorkTrackList where remarks does not contain UPDATED_REMARKS
        defaultStudentClassWorkTrackShouldBeFound("remarks.doesNotContain=" + UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void getAllStudentClassWorkTracksByCreateDateIsEqualToSomething() throws Exception {
        // Initialize the database
        studentClassWorkTrackRepository.saveAndFlush(studentClassWorkTrack);

        // Get all the studentClassWorkTrackList where createDate equals to DEFAULT_CREATE_DATE
        defaultStudentClassWorkTrackShouldBeFound("createDate.equals=" + DEFAULT_CREATE_DATE);

        // Get all the studentClassWorkTrackList where createDate equals to UPDATED_CREATE_DATE
        defaultStudentClassWorkTrackShouldNotBeFound("createDate.equals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllStudentClassWorkTracksByCreateDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentClassWorkTrackRepository.saveAndFlush(studentClassWorkTrack);

        // Get all the studentClassWorkTrackList where createDate not equals to DEFAULT_CREATE_DATE
        defaultStudentClassWorkTrackShouldNotBeFound("createDate.notEquals=" + DEFAULT_CREATE_DATE);

        // Get all the studentClassWorkTrackList where createDate not equals to UPDATED_CREATE_DATE
        defaultStudentClassWorkTrackShouldBeFound("createDate.notEquals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllStudentClassWorkTracksByCreateDateIsInShouldWork() throws Exception {
        // Initialize the database
        studentClassWorkTrackRepository.saveAndFlush(studentClassWorkTrack);

        // Get all the studentClassWorkTrackList where createDate in DEFAULT_CREATE_DATE or UPDATED_CREATE_DATE
        defaultStudentClassWorkTrackShouldBeFound("createDate.in=" + DEFAULT_CREATE_DATE + "," + UPDATED_CREATE_DATE);

        // Get all the studentClassWorkTrackList where createDate equals to UPDATED_CREATE_DATE
        defaultStudentClassWorkTrackShouldNotBeFound("createDate.in=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllStudentClassWorkTracksByCreateDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentClassWorkTrackRepository.saveAndFlush(studentClassWorkTrack);

        // Get all the studentClassWorkTrackList where createDate is not null
        defaultStudentClassWorkTrackShouldBeFound("createDate.specified=true");

        // Get all the studentClassWorkTrackList where createDate is null
        defaultStudentClassWorkTrackShouldNotBeFound("createDate.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentClassWorkTracksByCreateDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        studentClassWorkTrackRepository.saveAndFlush(studentClassWorkTrack);

        // Get all the studentClassWorkTrackList where createDate is greater than or equal to DEFAULT_CREATE_DATE
        defaultStudentClassWorkTrackShouldBeFound("createDate.greaterThanOrEqual=" + DEFAULT_CREATE_DATE);

        // Get all the studentClassWorkTrackList where createDate is greater than or equal to UPDATED_CREATE_DATE
        defaultStudentClassWorkTrackShouldNotBeFound("createDate.greaterThanOrEqual=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllStudentClassWorkTracksByCreateDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        studentClassWorkTrackRepository.saveAndFlush(studentClassWorkTrack);

        // Get all the studentClassWorkTrackList where createDate is less than or equal to DEFAULT_CREATE_DATE
        defaultStudentClassWorkTrackShouldBeFound("createDate.lessThanOrEqual=" + DEFAULT_CREATE_DATE);

        // Get all the studentClassWorkTrackList where createDate is less than or equal to SMALLER_CREATE_DATE
        defaultStudentClassWorkTrackShouldNotBeFound("createDate.lessThanOrEqual=" + SMALLER_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllStudentClassWorkTracksByCreateDateIsLessThanSomething() throws Exception {
        // Initialize the database
        studentClassWorkTrackRepository.saveAndFlush(studentClassWorkTrack);

        // Get all the studentClassWorkTrackList where createDate is less than DEFAULT_CREATE_DATE
        defaultStudentClassWorkTrackShouldNotBeFound("createDate.lessThan=" + DEFAULT_CREATE_DATE);

        // Get all the studentClassWorkTrackList where createDate is less than UPDATED_CREATE_DATE
        defaultStudentClassWorkTrackShouldBeFound("createDate.lessThan=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllStudentClassWorkTracksByCreateDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        studentClassWorkTrackRepository.saveAndFlush(studentClassWorkTrack);

        // Get all the studentClassWorkTrackList where createDate is greater than DEFAULT_CREATE_DATE
        defaultStudentClassWorkTrackShouldNotBeFound("createDate.greaterThan=" + DEFAULT_CREATE_DATE);

        // Get all the studentClassWorkTrackList where createDate is greater than SMALLER_CREATE_DATE
        defaultStudentClassWorkTrackShouldBeFound("createDate.greaterThan=" + SMALLER_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllStudentClassWorkTracksByLastModifiedIsEqualToSomething() throws Exception {
        // Initialize the database
        studentClassWorkTrackRepository.saveAndFlush(studentClassWorkTrack);

        // Get all the studentClassWorkTrackList where lastModified equals to DEFAULT_LAST_MODIFIED
        defaultStudentClassWorkTrackShouldBeFound("lastModified.equals=" + DEFAULT_LAST_MODIFIED);

        // Get all the studentClassWorkTrackList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultStudentClassWorkTrackShouldNotBeFound("lastModified.equals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllStudentClassWorkTracksByLastModifiedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentClassWorkTrackRepository.saveAndFlush(studentClassWorkTrack);

        // Get all the studentClassWorkTrackList where lastModified not equals to DEFAULT_LAST_MODIFIED
        defaultStudentClassWorkTrackShouldNotBeFound("lastModified.notEquals=" + DEFAULT_LAST_MODIFIED);

        // Get all the studentClassWorkTrackList where lastModified not equals to UPDATED_LAST_MODIFIED
        defaultStudentClassWorkTrackShouldBeFound("lastModified.notEquals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllStudentClassWorkTracksByLastModifiedIsInShouldWork() throws Exception {
        // Initialize the database
        studentClassWorkTrackRepository.saveAndFlush(studentClassWorkTrack);

        // Get all the studentClassWorkTrackList where lastModified in DEFAULT_LAST_MODIFIED or UPDATED_LAST_MODIFIED
        defaultStudentClassWorkTrackShouldBeFound("lastModified.in=" + DEFAULT_LAST_MODIFIED + "," + UPDATED_LAST_MODIFIED);

        // Get all the studentClassWorkTrackList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultStudentClassWorkTrackShouldNotBeFound("lastModified.in=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllStudentClassWorkTracksByLastModifiedIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentClassWorkTrackRepository.saveAndFlush(studentClassWorkTrack);

        // Get all the studentClassWorkTrackList where lastModified is not null
        defaultStudentClassWorkTrackShouldBeFound("lastModified.specified=true");

        // Get all the studentClassWorkTrackList where lastModified is null
        defaultStudentClassWorkTrackShouldNotBeFound("lastModified.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentClassWorkTracksByLastModifiedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        studentClassWorkTrackRepository.saveAndFlush(studentClassWorkTrack);

        // Get all the studentClassWorkTrackList where lastModified is greater than or equal to DEFAULT_LAST_MODIFIED
        defaultStudentClassWorkTrackShouldBeFound("lastModified.greaterThanOrEqual=" + DEFAULT_LAST_MODIFIED);

        // Get all the studentClassWorkTrackList where lastModified is greater than or equal to UPDATED_LAST_MODIFIED
        defaultStudentClassWorkTrackShouldNotBeFound("lastModified.greaterThanOrEqual=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllStudentClassWorkTracksByLastModifiedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        studentClassWorkTrackRepository.saveAndFlush(studentClassWorkTrack);

        // Get all the studentClassWorkTrackList where lastModified is less than or equal to DEFAULT_LAST_MODIFIED
        defaultStudentClassWorkTrackShouldBeFound("lastModified.lessThanOrEqual=" + DEFAULT_LAST_MODIFIED);

        // Get all the studentClassWorkTrackList where lastModified is less than or equal to SMALLER_LAST_MODIFIED
        defaultStudentClassWorkTrackShouldNotBeFound("lastModified.lessThanOrEqual=" + SMALLER_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllStudentClassWorkTracksByLastModifiedIsLessThanSomething() throws Exception {
        // Initialize the database
        studentClassWorkTrackRepository.saveAndFlush(studentClassWorkTrack);

        // Get all the studentClassWorkTrackList where lastModified is less than DEFAULT_LAST_MODIFIED
        defaultStudentClassWorkTrackShouldNotBeFound("lastModified.lessThan=" + DEFAULT_LAST_MODIFIED);

        // Get all the studentClassWorkTrackList where lastModified is less than UPDATED_LAST_MODIFIED
        defaultStudentClassWorkTrackShouldBeFound("lastModified.lessThan=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllStudentClassWorkTracksByLastModifiedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        studentClassWorkTrackRepository.saveAndFlush(studentClassWorkTrack);

        // Get all the studentClassWorkTrackList where lastModified is greater than DEFAULT_LAST_MODIFIED
        defaultStudentClassWorkTrackShouldNotBeFound("lastModified.greaterThan=" + DEFAULT_LAST_MODIFIED);

        // Get all the studentClassWorkTrackList where lastModified is greater than SMALLER_LAST_MODIFIED
        defaultStudentClassWorkTrackShouldBeFound("lastModified.greaterThan=" + SMALLER_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllStudentClassWorkTracksByCancelDateIsEqualToSomething() throws Exception {
        // Initialize the database
        studentClassWorkTrackRepository.saveAndFlush(studentClassWorkTrack);

        // Get all the studentClassWorkTrackList where cancelDate equals to DEFAULT_CANCEL_DATE
        defaultStudentClassWorkTrackShouldBeFound("cancelDate.equals=" + DEFAULT_CANCEL_DATE);

        // Get all the studentClassWorkTrackList where cancelDate equals to UPDATED_CANCEL_DATE
        defaultStudentClassWorkTrackShouldNotBeFound("cancelDate.equals=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllStudentClassWorkTracksByCancelDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentClassWorkTrackRepository.saveAndFlush(studentClassWorkTrack);

        // Get all the studentClassWorkTrackList where cancelDate not equals to DEFAULT_CANCEL_DATE
        defaultStudentClassWorkTrackShouldNotBeFound("cancelDate.notEquals=" + DEFAULT_CANCEL_DATE);

        // Get all the studentClassWorkTrackList where cancelDate not equals to UPDATED_CANCEL_DATE
        defaultStudentClassWorkTrackShouldBeFound("cancelDate.notEquals=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllStudentClassWorkTracksByCancelDateIsInShouldWork() throws Exception {
        // Initialize the database
        studentClassWorkTrackRepository.saveAndFlush(studentClassWorkTrack);

        // Get all the studentClassWorkTrackList where cancelDate in DEFAULT_CANCEL_DATE or UPDATED_CANCEL_DATE
        defaultStudentClassWorkTrackShouldBeFound("cancelDate.in=" + DEFAULT_CANCEL_DATE + "," + UPDATED_CANCEL_DATE);

        // Get all the studentClassWorkTrackList where cancelDate equals to UPDATED_CANCEL_DATE
        defaultStudentClassWorkTrackShouldNotBeFound("cancelDate.in=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllStudentClassWorkTracksByCancelDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentClassWorkTrackRepository.saveAndFlush(studentClassWorkTrack);

        // Get all the studentClassWorkTrackList where cancelDate is not null
        defaultStudentClassWorkTrackShouldBeFound("cancelDate.specified=true");

        // Get all the studentClassWorkTrackList where cancelDate is null
        defaultStudentClassWorkTrackShouldNotBeFound("cancelDate.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentClassWorkTracksByCancelDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        studentClassWorkTrackRepository.saveAndFlush(studentClassWorkTrack);

        // Get all the studentClassWorkTrackList where cancelDate is greater than or equal to DEFAULT_CANCEL_DATE
        defaultStudentClassWorkTrackShouldBeFound("cancelDate.greaterThanOrEqual=" + DEFAULT_CANCEL_DATE);

        // Get all the studentClassWorkTrackList where cancelDate is greater than or equal to UPDATED_CANCEL_DATE
        defaultStudentClassWorkTrackShouldNotBeFound("cancelDate.greaterThanOrEqual=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllStudentClassWorkTracksByCancelDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        studentClassWorkTrackRepository.saveAndFlush(studentClassWorkTrack);

        // Get all the studentClassWorkTrackList where cancelDate is less than or equal to DEFAULT_CANCEL_DATE
        defaultStudentClassWorkTrackShouldBeFound("cancelDate.lessThanOrEqual=" + DEFAULT_CANCEL_DATE);

        // Get all the studentClassWorkTrackList where cancelDate is less than or equal to SMALLER_CANCEL_DATE
        defaultStudentClassWorkTrackShouldNotBeFound("cancelDate.lessThanOrEqual=" + SMALLER_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllStudentClassWorkTracksByCancelDateIsLessThanSomething() throws Exception {
        // Initialize the database
        studentClassWorkTrackRepository.saveAndFlush(studentClassWorkTrack);

        // Get all the studentClassWorkTrackList where cancelDate is less than DEFAULT_CANCEL_DATE
        defaultStudentClassWorkTrackShouldNotBeFound("cancelDate.lessThan=" + DEFAULT_CANCEL_DATE);

        // Get all the studentClassWorkTrackList where cancelDate is less than UPDATED_CANCEL_DATE
        defaultStudentClassWorkTrackShouldBeFound("cancelDate.lessThan=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllStudentClassWorkTracksByCancelDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        studentClassWorkTrackRepository.saveAndFlush(studentClassWorkTrack);

        // Get all the studentClassWorkTrackList where cancelDate is greater than DEFAULT_CANCEL_DATE
        defaultStudentClassWorkTrackShouldNotBeFound("cancelDate.greaterThan=" + DEFAULT_CANCEL_DATE);

        // Get all the studentClassWorkTrackList where cancelDate is greater than SMALLER_CANCEL_DATE
        defaultStudentClassWorkTrackShouldBeFound("cancelDate.greaterThan=" + SMALLER_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllStudentClassWorkTracksByClassStudentIsEqualToSomething() throws Exception {
        // Initialize the database
        studentClassWorkTrackRepository.saveAndFlush(studentClassWorkTrack);
        ClassStudent classStudent = ClassStudentResourceIT.createEntity(em);
        em.persist(classStudent);
        em.flush();
        studentClassWorkTrack.setClassStudent(classStudent);
        studentClassWorkTrackRepository.saveAndFlush(studentClassWorkTrack);
        Long classStudentId = classStudent.getId();

        // Get all the studentClassWorkTrackList where classStudent equals to classStudentId
        defaultStudentClassWorkTrackShouldBeFound("classStudentId.equals=" + classStudentId);

        // Get all the studentClassWorkTrackList where classStudent equals to (classStudentId + 1)
        defaultStudentClassWorkTrackShouldNotBeFound("classStudentId.equals=" + (classStudentId + 1));
    }

    @Test
    @Transactional
    void getAllStudentClassWorkTracksByClassClassWorkIsEqualToSomething() throws Exception {
        // Initialize the database
        studentClassWorkTrackRepository.saveAndFlush(studentClassWorkTrack);
        ClassClassWork classClassWork = ClassClassWorkResourceIT.createEntity(em);
        em.persist(classClassWork);
        em.flush();
        studentClassWorkTrack.setClassClassWork(classClassWork);
        studentClassWorkTrackRepository.saveAndFlush(studentClassWorkTrack);
        Long classClassWorkId = classClassWork.getId();

        // Get all the studentClassWorkTrackList where classClassWork equals to classClassWorkId
        defaultStudentClassWorkTrackShouldBeFound("classClassWorkId.equals=" + classClassWorkId);

        // Get all the studentClassWorkTrackList where classClassWork equals to (classClassWorkId + 1)
        defaultStudentClassWorkTrackShouldNotBeFound("classClassWorkId.equals=" + (classClassWorkId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultStudentClassWorkTrackShouldBeFound(String filter) throws Exception {
        restStudentClassWorkTrackMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(studentClassWorkTrack.getId().intValue())))
            .andExpect(jsonPath("$.[*].workStatus").value(hasItem(DEFAULT_WORK_STATUS.toString())))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].cancelDate").value(hasItem(DEFAULT_CANCEL_DATE.toString())));

        // Check, that the count call also returns 1
        restStudentClassWorkTrackMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultStudentClassWorkTrackShouldNotBeFound(String filter) throws Exception {
        restStudentClassWorkTrackMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restStudentClassWorkTrackMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingStudentClassWorkTrack() throws Exception {
        // Get the studentClassWorkTrack
        restStudentClassWorkTrackMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewStudentClassWorkTrack() throws Exception {
        // Initialize the database
        studentClassWorkTrackRepository.saveAndFlush(studentClassWorkTrack);

        int databaseSizeBeforeUpdate = studentClassWorkTrackRepository.findAll().size();

        // Update the studentClassWorkTrack
        StudentClassWorkTrack updatedStudentClassWorkTrack = studentClassWorkTrackRepository.findById(studentClassWorkTrack.getId()).get();
        // Disconnect from session so that the updates on updatedStudentClassWorkTrack are not directly saved in db
        em.detach(updatedStudentClassWorkTrack);
        updatedStudentClassWorkTrack
            .workStatus(UPDATED_WORK_STATUS)
            .remarks(UPDATED_REMARKS)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);
        StudentClassWorkTrackDTO studentClassWorkTrackDTO = studentClassWorkTrackMapper.toDto(updatedStudentClassWorkTrack);

        restStudentClassWorkTrackMockMvc
            .perform(
                put(ENTITY_API_URL_ID, studentClassWorkTrackDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(studentClassWorkTrackDTO))
            )
            .andExpect(status().isOk());

        // Validate the StudentClassWorkTrack in the database
        List<StudentClassWorkTrack> studentClassWorkTrackList = studentClassWorkTrackRepository.findAll();
        assertThat(studentClassWorkTrackList).hasSize(databaseSizeBeforeUpdate);
        StudentClassWorkTrack testStudentClassWorkTrack = studentClassWorkTrackList.get(studentClassWorkTrackList.size() - 1);
        assertThat(testStudentClassWorkTrack.getWorkStatus()).isEqualTo(UPDATED_WORK_STATUS);
        assertThat(testStudentClassWorkTrack.getRemarks()).isEqualTo(UPDATED_REMARKS);
        assertThat(testStudentClassWorkTrack.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testStudentClassWorkTrack.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testStudentClassWorkTrack.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void putNonExistingStudentClassWorkTrack() throws Exception {
        int databaseSizeBeforeUpdate = studentClassWorkTrackRepository.findAll().size();
        studentClassWorkTrack.setId(count.incrementAndGet());

        // Create the StudentClassWorkTrack
        StudentClassWorkTrackDTO studentClassWorkTrackDTO = studentClassWorkTrackMapper.toDto(studentClassWorkTrack);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudentClassWorkTrackMockMvc
            .perform(
                put(ENTITY_API_URL_ID, studentClassWorkTrackDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(studentClassWorkTrackDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentClassWorkTrack in the database
        List<StudentClassWorkTrack> studentClassWorkTrackList = studentClassWorkTrackRepository.findAll();
        assertThat(studentClassWorkTrackList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStudentClassWorkTrack() throws Exception {
        int databaseSizeBeforeUpdate = studentClassWorkTrackRepository.findAll().size();
        studentClassWorkTrack.setId(count.incrementAndGet());

        // Create the StudentClassWorkTrack
        StudentClassWorkTrackDTO studentClassWorkTrackDTO = studentClassWorkTrackMapper.toDto(studentClassWorkTrack);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentClassWorkTrackMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(studentClassWorkTrackDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentClassWorkTrack in the database
        List<StudentClassWorkTrack> studentClassWorkTrackList = studentClassWorkTrackRepository.findAll();
        assertThat(studentClassWorkTrackList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStudentClassWorkTrack() throws Exception {
        int databaseSizeBeforeUpdate = studentClassWorkTrackRepository.findAll().size();
        studentClassWorkTrack.setId(count.incrementAndGet());

        // Create the StudentClassWorkTrack
        StudentClassWorkTrackDTO studentClassWorkTrackDTO = studentClassWorkTrackMapper.toDto(studentClassWorkTrack);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentClassWorkTrackMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(studentClassWorkTrackDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StudentClassWorkTrack in the database
        List<StudentClassWorkTrack> studentClassWorkTrackList = studentClassWorkTrackRepository.findAll();
        assertThat(studentClassWorkTrackList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStudentClassWorkTrackWithPatch() throws Exception {
        // Initialize the database
        studentClassWorkTrackRepository.saveAndFlush(studentClassWorkTrack);

        int databaseSizeBeforeUpdate = studentClassWorkTrackRepository.findAll().size();

        // Update the studentClassWorkTrack using partial update
        StudentClassWorkTrack partialUpdatedStudentClassWorkTrack = new StudentClassWorkTrack();
        partialUpdatedStudentClassWorkTrack.setId(studentClassWorkTrack.getId());

        partialUpdatedStudentClassWorkTrack.remarks(UPDATED_REMARKS).lastModified(UPDATED_LAST_MODIFIED).cancelDate(UPDATED_CANCEL_DATE);

        restStudentClassWorkTrackMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStudentClassWorkTrack.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStudentClassWorkTrack))
            )
            .andExpect(status().isOk());

        // Validate the StudentClassWorkTrack in the database
        List<StudentClassWorkTrack> studentClassWorkTrackList = studentClassWorkTrackRepository.findAll();
        assertThat(studentClassWorkTrackList).hasSize(databaseSizeBeforeUpdate);
        StudentClassWorkTrack testStudentClassWorkTrack = studentClassWorkTrackList.get(studentClassWorkTrackList.size() - 1);
        assertThat(testStudentClassWorkTrack.getWorkStatus()).isEqualTo(DEFAULT_WORK_STATUS);
        assertThat(testStudentClassWorkTrack.getRemarks()).isEqualTo(UPDATED_REMARKS);
        assertThat(testStudentClassWorkTrack.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testStudentClassWorkTrack.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testStudentClassWorkTrack.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void fullUpdateStudentClassWorkTrackWithPatch() throws Exception {
        // Initialize the database
        studentClassWorkTrackRepository.saveAndFlush(studentClassWorkTrack);

        int databaseSizeBeforeUpdate = studentClassWorkTrackRepository.findAll().size();

        // Update the studentClassWorkTrack using partial update
        StudentClassWorkTrack partialUpdatedStudentClassWorkTrack = new StudentClassWorkTrack();
        partialUpdatedStudentClassWorkTrack.setId(studentClassWorkTrack.getId());

        partialUpdatedStudentClassWorkTrack
            .workStatus(UPDATED_WORK_STATUS)
            .remarks(UPDATED_REMARKS)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);

        restStudentClassWorkTrackMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStudentClassWorkTrack.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStudentClassWorkTrack))
            )
            .andExpect(status().isOk());

        // Validate the StudentClassWorkTrack in the database
        List<StudentClassWorkTrack> studentClassWorkTrackList = studentClassWorkTrackRepository.findAll();
        assertThat(studentClassWorkTrackList).hasSize(databaseSizeBeforeUpdate);
        StudentClassWorkTrack testStudentClassWorkTrack = studentClassWorkTrackList.get(studentClassWorkTrackList.size() - 1);
        assertThat(testStudentClassWorkTrack.getWorkStatus()).isEqualTo(UPDATED_WORK_STATUS);
        assertThat(testStudentClassWorkTrack.getRemarks()).isEqualTo(UPDATED_REMARKS);
        assertThat(testStudentClassWorkTrack.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testStudentClassWorkTrack.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testStudentClassWorkTrack.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingStudentClassWorkTrack() throws Exception {
        int databaseSizeBeforeUpdate = studentClassWorkTrackRepository.findAll().size();
        studentClassWorkTrack.setId(count.incrementAndGet());

        // Create the StudentClassWorkTrack
        StudentClassWorkTrackDTO studentClassWorkTrackDTO = studentClassWorkTrackMapper.toDto(studentClassWorkTrack);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudentClassWorkTrackMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, studentClassWorkTrackDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(studentClassWorkTrackDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentClassWorkTrack in the database
        List<StudentClassWorkTrack> studentClassWorkTrackList = studentClassWorkTrackRepository.findAll();
        assertThat(studentClassWorkTrackList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStudentClassWorkTrack() throws Exception {
        int databaseSizeBeforeUpdate = studentClassWorkTrackRepository.findAll().size();
        studentClassWorkTrack.setId(count.incrementAndGet());

        // Create the StudentClassWorkTrack
        StudentClassWorkTrackDTO studentClassWorkTrackDTO = studentClassWorkTrackMapper.toDto(studentClassWorkTrack);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentClassWorkTrackMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(studentClassWorkTrackDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentClassWorkTrack in the database
        List<StudentClassWorkTrack> studentClassWorkTrackList = studentClassWorkTrackRepository.findAll();
        assertThat(studentClassWorkTrackList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStudentClassWorkTrack() throws Exception {
        int databaseSizeBeforeUpdate = studentClassWorkTrackRepository.findAll().size();
        studentClassWorkTrack.setId(count.incrementAndGet());

        // Create the StudentClassWorkTrack
        StudentClassWorkTrackDTO studentClassWorkTrackDTO = studentClassWorkTrackMapper.toDto(studentClassWorkTrack);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentClassWorkTrackMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(studentClassWorkTrackDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StudentClassWorkTrack in the database
        List<StudentClassWorkTrack> studentClassWorkTrackList = studentClassWorkTrackRepository.findAll();
        assertThat(studentClassWorkTrackList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStudentClassWorkTrack() throws Exception {
        // Initialize the database
        studentClassWorkTrackRepository.saveAndFlush(studentClassWorkTrack);

        int databaseSizeBeforeDelete = studentClassWorkTrackRepository.findAll().size();

        // Delete the studentClassWorkTrack
        restStudentClassWorkTrackMockMvc
            .perform(delete(ENTITY_API_URL_ID, studentClassWorkTrack.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<StudentClassWorkTrack> studentClassWorkTrackList = studentClassWorkTrackRepository.findAll();
        assertThat(studentClassWorkTrackList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
