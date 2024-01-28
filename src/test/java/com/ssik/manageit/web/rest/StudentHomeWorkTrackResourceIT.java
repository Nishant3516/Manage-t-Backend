package com.ssik.manageit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ssik.manageit.IntegrationTest;
import com.ssik.manageit.domain.ClassHomeWork;
import com.ssik.manageit.domain.ClassStudent;
import com.ssik.manageit.domain.StudentHomeWorkTrack;
import com.ssik.manageit.domain.enumeration.WorkStatus;
import com.ssik.manageit.repository.StudentHomeWorkTrackRepository;
import com.ssik.manageit.service.criteria.StudentHomeWorkTrackCriteria;
import com.ssik.manageit.service.dto.StudentHomeWorkTrackDTO;
import com.ssik.manageit.service.mapper.StudentHomeWorkTrackMapper;
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
 * Integration tests for the {@link StudentHomeWorkTrackResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StudentHomeWorkTrackResourceIT {

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

    private static final String ENTITY_API_URL = "/api/student-home-work-tracks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private StudentHomeWorkTrackRepository studentHomeWorkTrackRepository;

    @Autowired
    private StudentHomeWorkTrackMapper studentHomeWorkTrackMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStudentHomeWorkTrackMockMvc;

    private StudentHomeWorkTrack studentHomeWorkTrack;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StudentHomeWorkTrack createEntity(EntityManager em) {
        StudentHomeWorkTrack studentHomeWorkTrack = new StudentHomeWorkTrack()
            .workStatus(DEFAULT_WORK_STATUS)
            .remarks(DEFAULT_REMARKS)
            .createDate(DEFAULT_CREATE_DATE)
            .lastModified(DEFAULT_LAST_MODIFIED)
            .cancelDate(DEFAULT_CANCEL_DATE);
        return studentHomeWorkTrack;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StudentHomeWorkTrack createUpdatedEntity(EntityManager em) {
        StudentHomeWorkTrack studentHomeWorkTrack = new StudentHomeWorkTrack()
            .workStatus(UPDATED_WORK_STATUS)
            .remarks(UPDATED_REMARKS)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);
        return studentHomeWorkTrack;
    }

    @BeforeEach
    public void initTest() {
        studentHomeWorkTrack = createEntity(em);
    }

    @Test
    @Transactional
    void createStudentHomeWorkTrack() throws Exception {
        int databaseSizeBeforeCreate = studentHomeWorkTrackRepository.findAll().size();
        // Create the StudentHomeWorkTrack
        StudentHomeWorkTrackDTO studentHomeWorkTrackDTO = studentHomeWorkTrackMapper.toDto(studentHomeWorkTrack);
        restStudentHomeWorkTrackMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(studentHomeWorkTrackDTO))
            )
            .andExpect(status().isCreated());

        // Validate the StudentHomeWorkTrack in the database
        List<StudentHomeWorkTrack> studentHomeWorkTrackList = studentHomeWorkTrackRepository.findAll();
        assertThat(studentHomeWorkTrackList).hasSize(databaseSizeBeforeCreate + 1);
        StudentHomeWorkTrack testStudentHomeWorkTrack = studentHomeWorkTrackList.get(studentHomeWorkTrackList.size() - 1);
        assertThat(testStudentHomeWorkTrack.getWorkStatus()).isEqualTo(DEFAULT_WORK_STATUS);
        assertThat(testStudentHomeWorkTrack.getRemarks()).isEqualTo(DEFAULT_REMARKS);
        assertThat(testStudentHomeWorkTrack.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testStudentHomeWorkTrack.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
        assertThat(testStudentHomeWorkTrack.getCancelDate()).isEqualTo(DEFAULT_CANCEL_DATE);
    }

    @Test
    @Transactional
    void createStudentHomeWorkTrackWithExistingId() throws Exception {
        // Create the StudentHomeWorkTrack with an existing ID
        studentHomeWorkTrack.setId(1L);
        StudentHomeWorkTrackDTO studentHomeWorkTrackDTO = studentHomeWorkTrackMapper.toDto(studentHomeWorkTrack);

        int databaseSizeBeforeCreate = studentHomeWorkTrackRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStudentHomeWorkTrackMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(studentHomeWorkTrackDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentHomeWorkTrack in the database
        List<StudentHomeWorkTrack> studentHomeWorkTrackList = studentHomeWorkTrackRepository.findAll();
        assertThat(studentHomeWorkTrackList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkWorkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = studentHomeWorkTrackRepository.findAll().size();
        // set the field null
        studentHomeWorkTrack.setWorkStatus(null);

        // Create the StudentHomeWorkTrack, which fails.
        StudentHomeWorkTrackDTO studentHomeWorkTrackDTO = studentHomeWorkTrackMapper.toDto(studentHomeWorkTrack);

        restStudentHomeWorkTrackMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(studentHomeWorkTrackDTO))
            )
            .andExpect(status().isBadRequest());

        List<StudentHomeWorkTrack> studentHomeWorkTrackList = studentHomeWorkTrackRepository.findAll();
        assertThat(studentHomeWorkTrackList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllStudentHomeWorkTracks() throws Exception {
        // Initialize the database
        studentHomeWorkTrackRepository.saveAndFlush(studentHomeWorkTrack);

        // Get all the studentHomeWorkTrackList
        restStudentHomeWorkTrackMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(studentHomeWorkTrack.getId().intValue())))
            .andExpect(jsonPath("$.[*].workStatus").value(hasItem(DEFAULT_WORK_STATUS.toString())))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].cancelDate").value(hasItem(DEFAULT_CANCEL_DATE.toString())));
    }

    @Test
    @Transactional
    void getStudentHomeWorkTrack() throws Exception {
        // Initialize the database
        studentHomeWorkTrackRepository.saveAndFlush(studentHomeWorkTrack);

        // Get the studentHomeWorkTrack
        restStudentHomeWorkTrackMockMvc
            .perform(get(ENTITY_API_URL_ID, studentHomeWorkTrack.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(studentHomeWorkTrack.getId().intValue()))
            .andExpect(jsonPath("$.workStatus").value(DEFAULT_WORK_STATUS.toString()))
            .andExpect(jsonPath("$.remarks").value(DEFAULT_REMARKS))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.lastModified").value(DEFAULT_LAST_MODIFIED.toString()))
            .andExpect(jsonPath("$.cancelDate").value(DEFAULT_CANCEL_DATE.toString()));
    }

    @Test
    @Transactional
    void getStudentHomeWorkTracksByIdFiltering() throws Exception {
        // Initialize the database
        studentHomeWorkTrackRepository.saveAndFlush(studentHomeWorkTrack);

        Long id = studentHomeWorkTrack.getId();

        defaultStudentHomeWorkTrackShouldBeFound("id.equals=" + id);
        defaultStudentHomeWorkTrackShouldNotBeFound("id.notEquals=" + id);

        defaultStudentHomeWorkTrackShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultStudentHomeWorkTrackShouldNotBeFound("id.greaterThan=" + id);

        defaultStudentHomeWorkTrackShouldBeFound("id.lessThanOrEqual=" + id);
        defaultStudentHomeWorkTrackShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllStudentHomeWorkTracksByWorkStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        studentHomeWorkTrackRepository.saveAndFlush(studentHomeWorkTrack);

        // Get all the studentHomeWorkTrackList where workStatus equals to DEFAULT_WORK_STATUS
        defaultStudentHomeWorkTrackShouldBeFound("workStatus.equals=" + DEFAULT_WORK_STATUS);

        // Get all the studentHomeWorkTrackList where workStatus equals to UPDATED_WORK_STATUS
        defaultStudentHomeWorkTrackShouldNotBeFound("workStatus.equals=" + UPDATED_WORK_STATUS);
    }

    @Test
    @Transactional
    void getAllStudentHomeWorkTracksByWorkStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentHomeWorkTrackRepository.saveAndFlush(studentHomeWorkTrack);

        // Get all the studentHomeWorkTrackList where workStatus not equals to DEFAULT_WORK_STATUS
        defaultStudentHomeWorkTrackShouldNotBeFound("workStatus.notEquals=" + DEFAULT_WORK_STATUS);

        // Get all the studentHomeWorkTrackList where workStatus not equals to UPDATED_WORK_STATUS
        defaultStudentHomeWorkTrackShouldBeFound("workStatus.notEquals=" + UPDATED_WORK_STATUS);
    }

    @Test
    @Transactional
    void getAllStudentHomeWorkTracksByWorkStatusIsInShouldWork() throws Exception {
        // Initialize the database
        studentHomeWorkTrackRepository.saveAndFlush(studentHomeWorkTrack);

        // Get all the studentHomeWorkTrackList where workStatus in DEFAULT_WORK_STATUS or UPDATED_WORK_STATUS
        defaultStudentHomeWorkTrackShouldBeFound("workStatus.in=" + DEFAULT_WORK_STATUS + "," + UPDATED_WORK_STATUS);

        // Get all the studentHomeWorkTrackList where workStatus equals to UPDATED_WORK_STATUS
        defaultStudentHomeWorkTrackShouldNotBeFound("workStatus.in=" + UPDATED_WORK_STATUS);
    }

    @Test
    @Transactional
    void getAllStudentHomeWorkTracksByWorkStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentHomeWorkTrackRepository.saveAndFlush(studentHomeWorkTrack);

        // Get all the studentHomeWorkTrackList where workStatus is not null
        defaultStudentHomeWorkTrackShouldBeFound("workStatus.specified=true");

        // Get all the studentHomeWorkTrackList where workStatus is null
        defaultStudentHomeWorkTrackShouldNotBeFound("workStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentHomeWorkTracksByRemarksIsEqualToSomething() throws Exception {
        // Initialize the database
        studentHomeWorkTrackRepository.saveAndFlush(studentHomeWorkTrack);

        // Get all the studentHomeWorkTrackList where remarks equals to DEFAULT_REMARKS
        defaultStudentHomeWorkTrackShouldBeFound("remarks.equals=" + DEFAULT_REMARKS);

        // Get all the studentHomeWorkTrackList where remarks equals to UPDATED_REMARKS
        defaultStudentHomeWorkTrackShouldNotBeFound("remarks.equals=" + UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void getAllStudentHomeWorkTracksByRemarksIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentHomeWorkTrackRepository.saveAndFlush(studentHomeWorkTrack);

        // Get all the studentHomeWorkTrackList where remarks not equals to DEFAULT_REMARKS
        defaultStudentHomeWorkTrackShouldNotBeFound("remarks.notEquals=" + DEFAULT_REMARKS);

        // Get all the studentHomeWorkTrackList where remarks not equals to UPDATED_REMARKS
        defaultStudentHomeWorkTrackShouldBeFound("remarks.notEquals=" + UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void getAllStudentHomeWorkTracksByRemarksIsInShouldWork() throws Exception {
        // Initialize the database
        studentHomeWorkTrackRepository.saveAndFlush(studentHomeWorkTrack);

        // Get all the studentHomeWorkTrackList where remarks in DEFAULT_REMARKS or UPDATED_REMARKS
        defaultStudentHomeWorkTrackShouldBeFound("remarks.in=" + DEFAULT_REMARKS + "," + UPDATED_REMARKS);

        // Get all the studentHomeWorkTrackList where remarks equals to UPDATED_REMARKS
        defaultStudentHomeWorkTrackShouldNotBeFound("remarks.in=" + UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void getAllStudentHomeWorkTracksByRemarksIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentHomeWorkTrackRepository.saveAndFlush(studentHomeWorkTrack);

        // Get all the studentHomeWorkTrackList where remarks is not null
        defaultStudentHomeWorkTrackShouldBeFound("remarks.specified=true");

        // Get all the studentHomeWorkTrackList where remarks is null
        defaultStudentHomeWorkTrackShouldNotBeFound("remarks.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentHomeWorkTracksByRemarksContainsSomething() throws Exception {
        // Initialize the database
        studentHomeWorkTrackRepository.saveAndFlush(studentHomeWorkTrack);

        // Get all the studentHomeWorkTrackList where remarks contains DEFAULT_REMARKS
        defaultStudentHomeWorkTrackShouldBeFound("remarks.contains=" + DEFAULT_REMARKS);

        // Get all the studentHomeWorkTrackList where remarks contains UPDATED_REMARKS
        defaultStudentHomeWorkTrackShouldNotBeFound("remarks.contains=" + UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void getAllStudentHomeWorkTracksByRemarksNotContainsSomething() throws Exception {
        // Initialize the database
        studentHomeWorkTrackRepository.saveAndFlush(studentHomeWorkTrack);

        // Get all the studentHomeWorkTrackList where remarks does not contain DEFAULT_REMARKS
        defaultStudentHomeWorkTrackShouldNotBeFound("remarks.doesNotContain=" + DEFAULT_REMARKS);

        // Get all the studentHomeWorkTrackList where remarks does not contain UPDATED_REMARKS
        defaultStudentHomeWorkTrackShouldBeFound("remarks.doesNotContain=" + UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void getAllStudentHomeWorkTracksByCreateDateIsEqualToSomething() throws Exception {
        // Initialize the database
        studentHomeWorkTrackRepository.saveAndFlush(studentHomeWorkTrack);

        // Get all the studentHomeWorkTrackList where createDate equals to DEFAULT_CREATE_DATE
        defaultStudentHomeWorkTrackShouldBeFound("createDate.equals=" + DEFAULT_CREATE_DATE);

        // Get all the studentHomeWorkTrackList where createDate equals to UPDATED_CREATE_DATE
        defaultStudentHomeWorkTrackShouldNotBeFound("createDate.equals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllStudentHomeWorkTracksByCreateDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentHomeWorkTrackRepository.saveAndFlush(studentHomeWorkTrack);

        // Get all the studentHomeWorkTrackList where createDate not equals to DEFAULT_CREATE_DATE
        defaultStudentHomeWorkTrackShouldNotBeFound("createDate.notEquals=" + DEFAULT_CREATE_DATE);

        // Get all the studentHomeWorkTrackList where createDate not equals to UPDATED_CREATE_DATE
        defaultStudentHomeWorkTrackShouldBeFound("createDate.notEquals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllStudentHomeWorkTracksByCreateDateIsInShouldWork() throws Exception {
        // Initialize the database
        studentHomeWorkTrackRepository.saveAndFlush(studentHomeWorkTrack);

        // Get all the studentHomeWorkTrackList where createDate in DEFAULT_CREATE_DATE or UPDATED_CREATE_DATE
        defaultStudentHomeWorkTrackShouldBeFound("createDate.in=" + DEFAULT_CREATE_DATE + "," + UPDATED_CREATE_DATE);

        // Get all the studentHomeWorkTrackList where createDate equals to UPDATED_CREATE_DATE
        defaultStudentHomeWorkTrackShouldNotBeFound("createDate.in=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllStudentHomeWorkTracksByCreateDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentHomeWorkTrackRepository.saveAndFlush(studentHomeWorkTrack);

        // Get all the studentHomeWorkTrackList where createDate is not null
        defaultStudentHomeWorkTrackShouldBeFound("createDate.specified=true");

        // Get all the studentHomeWorkTrackList where createDate is null
        defaultStudentHomeWorkTrackShouldNotBeFound("createDate.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentHomeWorkTracksByCreateDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        studentHomeWorkTrackRepository.saveAndFlush(studentHomeWorkTrack);

        // Get all the studentHomeWorkTrackList where createDate is greater than or equal to DEFAULT_CREATE_DATE
        defaultStudentHomeWorkTrackShouldBeFound("createDate.greaterThanOrEqual=" + DEFAULT_CREATE_DATE);

        // Get all the studentHomeWorkTrackList where createDate is greater than or equal to UPDATED_CREATE_DATE
        defaultStudentHomeWorkTrackShouldNotBeFound("createDate.greaterThanOrEqual=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllStudentHomeWorkTracksByCreateDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        studentHomeWorkTrackRepository.saveAndFlush(studentHomeWorkTrack);

        // Get all the studentHomeWorkTrackList where createDate is less than or equal to DEFAULT_CREATE_DATE
        defaultStudentHomeWorkTrackShouldBeFound("createDate.lessThanOrEqual=" + DEFAULT_CREATE_DATE);

        // Get all the studentHomeWorkTrackList where createDate is less than or equal to SMALLER_CREATE_DATE
        defaultStudentHomeWorkTrackShouldNotBeFound("createDate.lessThanOrEqual=" + SMALLER_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllStudentHomeWorkTracksByCreateDateIsLessThanSomething() throws Exception {
        // Initialize the database
        studentHomeWorkTrackRepository.saveAndFlush(studentHomeWorkTrack);

        // Get all the studentHomeWorkTrackList where createDate is less than DEFAULT_CREATE_DATE
        defaultStudentHomeWorkTrackShouldNotBeFound("createDate.lessThan=" + DEFAULT_CREATE_DATE);

        // Get all the studentHomeWorkTrackList where createDate is less than UPDATED_CREATE_DATE
        defaultStudentHomeWorkTrackShouldBeFound("createDate.lessThan=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllStudentHomeWorkTracksByCreateDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        studentHomeWorkTrackRepository.saveAndFlush(studentHomeWorkTrack);

        // Get all the studentHomeWorkTrackList where createDate is greater than DEFAULT_CREATE_DATE
        defaultStudentHomeWorkTrackShouldNotBeFound("createDate.greaterThan=" + DEFAULT_CREATE_DATE);

        // Get all the studentHomeWorkTrackList where createDate is greater than SMALLER_CREATE_DATE
        defaultStudentHomeWorkTrackShouldBeFound("createDate.greaterThan=" + SMALLER_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllStudentHomeWorkTracksByLastModifiedIsEqualToSomething() throws Exception {
        // Initialize the database
        studentHomeWorkTrackRepository.saveAndFlush(studentHomeWorkTrack);

        // Get all the studentHomeWorkTrackList where lastModified equals to DEFAULT_LAST_MODIFIED
        defaultStudentHomeWorkTrackShouldBeFound("lastModified.equals=" + DEFAULT_LAST_MODIFIED);

        // Get all the studentHomeWorkTrackList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultStudentHomeWorkTrackShouldNotBeFound("lastModified.equals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllStudentHomeWorkTracksByLastModifiedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentHomeWorkTrackRepository.saveAndFlush(studentHomeWorkTrack);

        // Get all the studentHomeWorkTrackList where lastModified not equals to DEFAULT_LAST_MODIFIED
        defaultStudentHomeWorkTrackShouldNotBeFound("lastModified.notEquals=" + DEFAULT_LAST_MODIFIED);

        // Get all the studentHomeWorkTrackList where lastModified not equals to UPDATED_LAST_MODIFIED
        defaultStudentHomeWorkTrackShouldBeFound("lastModified.notEquals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllStudentHomeWorkTracksByLastModifiedIsInShouldWork() throws Exception {
        // Initialize the database
        studentHomeWorkTrackRepository.saveAndFlush(studentHomeWorkTrack);

        // Get all the studentHomeWorkTrackList where lastModified in DEFAULT_LAST_MODIFIED or UPDATED_LAST_MODIFIED
        defaultStudentHomeWorkTrackShouldBeFound("lastModified.in=" + DEFAULT_LAST_MODIFIED + "," + UPDATED_LAST_MODIFIED);

        // Get all the studentHomeWorkTrackList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultStudentHomeWorkTrackShouldNotBeFound("lastModified.in=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllStudentHomeWorkTracksByLastModifiedIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentHomeWorkTrackRepository.saveAndFlush(studentHomeWorkTrack);

        // Get all the studentHomeWorkTrackList where lastModified is not null
        defaultStudentHomeWorkTrackShouldBeFound("lastModified.specified=true");

        // Get all the studentHomeWorkTrackList where lastModified is null
        defaultStudentHomeWorkTrackShouldNotBeFound("lastModified.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentHomeWorkTracksByLastModifiedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        studentHomeWorkTrackRepository.saveAndFlush(studentHomeWorkTrack);

        // Get all the studentHomeWorkTrackList where lastModified is greater than or equal to DEFAULT_LAST_MODIFIED
        defaultStudentHomeWorkTrackShouldBeFound("lastModified.greaterThanOrEqual=" + DEFAULT_LAST_MODIFIED);

        // Get all the studentHomeWorkTrackList where lastModified is greater than or equal to UPDATED_LAST_MODIFIED
        defaultStudentHomeWorkTrackShouldNotBeFound("lastModified.greaterThanOrEqual=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllStudentHomeWorkTracksByLastModifiedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        studentHomeWorkTrackRepository.saveAndFlush(studentHomeWorkTrack);

        // Get all the studentHomeWorkTrackList where lastModified is less than or equal to DEFAULT_LAST_MODIFIED
        defaultStudentHomeWorkTrackShouldBeFound("lastModified.lessThanOrEqual=" + DEFAULT_LAST_MODIFIED);

        // Get all the studentHomeWorkTrackList where lastModified is less than or equal to SMALLER_LAST_MODIFIED
        defaultStudentHomeWorkTrackShouldNotBeFound("lastModified.lessThanOrEqual=" + SMALLER_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllStudentHomeWorkTracksByLastModifiedIsLessThanSomething() throws Exception {
        // Initialize the database
        studentHomeWorkTrackRepository.saveAndFlush(studentHomeWorkTrack);

        // Get all the studentHomeWorkTrackList where lastModified is less than DEFAULT_LAST_MODIFIED
        defaultStudentHomeWorkTrackShouldNotBeFound("lastModified.lessThan=" + DEFAULT_LAST_MODIFIED);

        // Get all the studentHomeWorkTrackList where lastModified is less than UPDATED_LAST_MODIFIED
        defaultStudentHomeWorkTrackShouldBeFound("lastModified.lessThan=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllStudentHomeWorkTracksByLastModifiedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        studentHomeWorkTrackRepository.saveAndFlush(studentHomeWorkTrack);

        // Get all the studentHomeWorkTrackList where lastModified is greater than DEFAULT_LAST_MODIFIED
        defaultStudentHomeWorkTrackShouldNotBeFound("lastModified.greaterThan=" + DEFAULT_LAST_MODIFIED);

        // Get all the studentHomeWorkTrackList where lastModified is greater than SMALLER_LAST_MODIFIED
        defaultStudentHomeWorkTrackShouldBeFound("lastModified.greaterThan=" + SMALLER_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllStudentHomeWorkTracksByCancelDateIsEqualToSomething() throws Exception {
        // Initialize the database
        studentHomeWorkTrackRepository.saveAndFlush(studentHomeWorkTrack);

        // Get all the studentHomeWorkTrackList where cancelDate equals to DEFAULT_CANCEL_DATE
        defaultStudentHomeWorkTrackShouldBeFound("cancelDate.equals=" + DEFAULT_CANCEL_DATE);

        // Get all the studentHomeWorkTrackList where cancelDate equals to UPDATED_CANCEL_DATE
        defaultStudentHomeWorkTrackShouldNotBeFound("cancelDate.equals=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllStudentHomeWorkTracksByCancelDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentHomeWorkTrackRepository.saveAndFlush(studentHomeWorkTrack);

        // Get all the studentHomeWorkTrackList where cancelDate not equals to DEFAULT_CANCEL_DATE
        defaultStudentHomeWorkTrackShouldNotBeFound("cancelDate.notEquals=" + DEFAULT_CANCEL_DATE);

        // Get all the studentHomeWorkTrackList where cancelDate not equals to UPDATED_CANCEL_DATE
        defaultStudentHomeWorkTrackShouldBeFound("cancelDate.notEquals=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllStudentHomeWorkTracksByCancelDateIsInShouldWork() throws Exception {
        // Initialize the database
        studentHomeWorkTrackRepository.saveAndFlush(studentHomeWorkTrack);

        // Get all the studentHomeWorkTrackList where cancelDate in DEFAULT_CANCEL_DATE or UPDATED_CANCEL_DATE
        defaultStudentHomeWorkTrackShouldBeFound("cancelDate.in=" + DEFAULT_CANCEL_DATE + "," + UPDATED_CANCEL_DATE);

        // Get all the studentHomeWorkTrackList where cancelDate equals to UPDATED_CANCEL_DATE
        defaultStudentHomeWorkTrackShouldNotBeFound("cancelDate.in=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllStudentHomeWorkTracksByCancelDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentHomeWorkTrackRepository.saveAndFlush(studentHomeWorkTrack);

        // Get all the studentHomeWorkTrackList where cancelDate is not null
        defaultStudentHomeWorkTrackShouldBeFound("cancelDate.specified=true");

        // Get all the studentHomeWorkTrackList where cancelDate is null
        defaultStudentHomeWorkTrackShouldNotBeFound("cancelDate.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentHomeWorkTracksByCancelDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        studentHomeWorkTrackRepository.saveAndFlush(studentHomeWorkTrack);

        // Get all the studentHomeWorkTrackList where cancelDate is greater than or equal to DEFAULT_CANCEL_DATE
        defaultStudentHomeWorkTrackShouldBeFound("cancelDate.greaterThanOrEqual=" + DEFAULT_CANCEL_DATE);

        // Get all the studentHomeWorkTrackList where cancelDate is greater than or equal to UPDATED_CANCEL_DATE
        defaultStudentHomeWorkTrackShouldNotBeFound("cancelDate.greaterThanOrEqual=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllStudentHomeWorkTracksByCancelDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        studentHomeWorkTrackRepository.saveAndFlush(studentHomeWorkTrack);

        // Get all the studentHomeWorkTrackList where cancelDate is less than or equal to DEFAULT_CANCEL_DATE
        defaultStudentHomeWorkTrackShouldBeFound("cancelDate.lessThanOrEqual=" + DEFAULT_CANCEL_DATE);

        // Get all the studentHomeWorkTrackList where cancelDate is less than or equal to SMALLER_CANCEL_DATE
        defaultStudentHomeWorkTrackShouldNotBeFound("cancelDate.lessThanOrEqual=" + SMALLER_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllStudentHomeWorkTracksByCancelDateIsLessThanSomething() throws Exception {
        // Initialize the database
        studentHomeWorkTrackRepository.saveAndFlush(studentHomeWorkTrack);

        // Get all the studentHomeWorkTrackList where cancelDate is less than DEFAULT_CANCEL_DATE
        defaultStudentHomeWorkTrackShouldNotBeFound("cancelDate.lessThan=" + DEFAULT_CANCEL_DATE);

        // Get all the studentHomeWorkTrackList where cancelDate is less than UPDATED_CANCEL_DATE
        defaultStudentHomeWorkTrackShouldBeFound("cancelDate.lessThan=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllStudentHomeWorkTracksByCancelDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        studentHomeWorkTrackRepository.saveAndFlush(studentHomeWorkTrack);

        // Get all the studentHomeWorkTrackList where cancelDate is greater than DEFAULT_CANCEL_DATE
        defaultStudentHomeWorkTrackShouldNotBeFound("cancelDate.greaterThan=" + DEFAULT_CANCEL_DATE);

        // Get all the studentHomeWorkTrackList where cancelDate is greater than SMALLER_CANCEL_DATE
        defaultStudentHomeWorkTrackShouldBeFound("cancelDate.greaterThan=" + SMALLER_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllStudentHomeWorkTracksByClassStudentIsEqualToSomething() throws Exception {
        // Initialize the database
        studentHomeWorkTrackRepository.saveAndFlush(studentHomeWorkTrack);
        ClassStudent classStudent = ClassStudentResourceIT.createEntity(em);
        em.persist(classStudent);
        em.flush();
        studentHomeWorkTrack.setClassStudent(classStudent);
        studentHomeWorkTrackRepository.saveAndFlush(studentHomeWorkTrack);
        Long classStudentId = classStudent.getId();

        // Get all the studentHomeWorkTrackList where classStudent equals to classStudentId
        defaultStudentHomeWorkTrackShouldBeFound("classStudentId.equals=" + classStudentId);

        // Get all the studentHomeWorkTrackList where classStudent equals to (classStudentId + 1)
        defaultStudentHomeWorkTrackShouldNotBeFound("classStudentId.equals=" + (classStudentId + 1));
    }

    @Test
    @Transactional
    void getAllStudentHomeWorkTracksByClassHomeWorkIsEqualToSomething() throws Exception {
        // Initialize the database
        studentHomeWorkTrackRepository.saveAndFlush(studentHomeWorkTrack);
        ClassHomeWork classHomeWork = ClassHomeWorkResourceIT.createEntity(em);
        em.persist(classHomeWork);
        em.flush();
        studentHomeWorkTrack.setClassHomeWork(classHomeWork);
        studentHomeWorkTrackRepository.saveAndFlush(studentHomeWorkTrack);
        Long classHomeWorkId = classHomeWork.getId();

        // Get all the studentHomeWorkTrackList where classHomeWork equals to classHomeWorkId
        defaultStudentHomeWorkTrackShouldBeFound("classHomeWorkId.equals=" + classHomeWorkId);

        // Get all the studentHomeWorkTrackList where classHomeWork equals to (classHomeWorkId + 1)
        defaultStudentHomeWorkTrackShouldNotBeFound("classHomeWorkId.equals=" + (classHomeWorkId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultStudentHomeWorkTrackShouldBeFound(String filter) throws Exception {
        restStudentHomeWorkTrackMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(studentHomeWorkTrack.getId().intValue())))
            .andExpect(jsonPath("$.[*].workStatus").value(hasItem(DEFAULT_WORK_STATUS.toString())))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].cancelDate").value(hasItem(DEFAULT_CANCEL_DATE.toString())));

        // Check, that the count call also returns 1
        restStudentHomeWorkTrackMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultStudentHomeWorkTrackShouldNotBeFound(String filter) throws Exception {
        restStudentHomeWorkTrackMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restStudentHomeWorkTrackMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingStudentHomeWorkTrack() throws Exception {
        // Get the studentHomeWorkTrack
        restStudentHomeWorkTrackMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewStudentHomeWorkTrack() throws Exception {
        // Initialize the database
        studentHomeWorkTrackRepository.saveAndFlush(studentHomeWorkTrack);

        int databaseSizeBeforeUpdate = studentHomeWorkTrackRepository.findAll().size();

        // Update the studentHomeWorkTrack
        StudentHomeWorkTrack updatedStudentHomeWorkTrack = studentHomeWorkTrackRepository.findById(studentHomeWorkTrack.getId()).get();
        // Disconnect from session so that the updates on updatedStudentHomeWorkTrack are not directly saved in db
        em.detach(updatedStudentHomeWorkTrack);
        updatedStudentHomeWorkTrack
            .workStatus(UPDATED_WORK_STATUS)
            .remarks(UPDATED_REMARKS)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);
        StudentHomeWorkTrackDTO studentHomeWorkTrackDTO = studentHomeWorkTrackMapper.toDto(updatedStudentHomeWorkTrack);

        restStudentHomeWorkTrackMockMvc
            .perform(
                put(ENTITY_API_URL_ID, studentHomeWorkTrackDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(studentHomeWorkTrackDTO))
            )
            .andExpect(status().isOk());

        // Validate the StudentHomeWorkTrack in the database
        List<StudentHomeWorkTrack> studentHomeWorkTrackList = studentHomeWorkTrackRepository.findAll();
        assertThat(studentHomeWorkTrackList).hasSize(databaseSizeBeforeUpdate);
        StudentHomeWorkTrack testStudentHomeWorkTrack = studentHomeWorkTrackList.get(studentHomeWorkTrackList.size() - 1);
        assertThat(testStudentHomeWorkTrack.getWorkStatus()).isEqualTo(UPDATED_WORK_STATUS);
        assertThat(testStudentHomeWorkTrack.getRemarks()).isEqualTo(UPDATED_REMARKS);
        assertThat(testStudentHomeWorkTrack.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testStudentHomeWorkTrack.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testStudentHomeWorkTrack.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void putNonExistingStudentHomeWorkTrack() throws Exception {
        int databaseSizeBeforeUpdate = studentHomeWorkTrackRepository.findAll().size();
        studentHomeWorkTrack.setId(count.incrementAndGet());

        // Create the StudentHomeWorkTrack
        StudentHomeWorkTrackDTO studentHomeWorkTrackDTO = studentHomeWorkTrackMapper.toDto(studentHomeWorkTrack);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudentHomeWorkTrackMockMvc
            .perform(
                put(ENTITY_API_URL_ID, studentHomeWorkTrackDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(studentHomeWorkTrackDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentHomeWorkTrack in the database
        List<StudentHomeWorkTrack> studentHomeWorkTrackList = studentHomeWorkTrackRepository.findAll();
        assertThat(studentHomeWorkTrackList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStudentHomeWorkTrack() throws Exception {
        int databaseSizeBeforeUpdate = studentHomeWorkTrackRepository.findAll().size();
        studentHomeWorkTrack.setId(count.incrementAndGet());

        // Create the StudentHomeWorkTrack
        StudentHomeWorkTrackDTO studentHomeWorkTrackDTO = studentHomeWorkTrackMapper.toDto(studentHomeWorkTrack);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentHomeWorkTrackMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(studentHomeWorkTrackDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentHomeWorkTrack in the database
        List<StudentHomeWorkTrack> studentHomeWorkTrackList = studentHomeWorkTrackRepository.findAll();
        assertThat(studentHomeWorkTrackList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStudentHomeWorkTrack() throws Exception {
        int databaseSizeBeforeUpdate = studentHomeWorkTrackRepository.findAll().size();
        studentHomeWorkTrack.setId(count.incrementAndGet());

        // Create the StudentHomeWorkTrack
        StudentHomeWorkTrackDTO studentHomeWorkTrackDTO = studentHomeWorkTrackMapper.toDto(studentHomeWorkTrack);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentHomeWorkTrackMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(studentHomeWorkTrackDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StudentHomeWorkTrack in the database
        List<StudentHomeWorkTrack> studentHomeWorkTrackList = studentHomeWorkTrackRepository.findAll();
        assertThat(studentHomeWorkTrackList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStudentHomeWorkTrackWithPatch() throws Exception {
        // Initialize the database
        studentHomeWorkTrackRepository.saveAndFlush(studentHomeWorkTrack);

        int databaseSizeBeforeUpdate = studentHomeWorkTrackRepository.findAll().size();

        // Update the studentHomeWorkTrack using partial update
        StudentHomeWorkTrack partialUpdatedStudentHomeWorkTrack = new StudentHomeWorkTrack();
        partialUpdatedStudentHomeWorkTrack.setId(studentHomeWorkTrack.getId());

        partialUpdatedStudentHomeWorkTrack
            .workStatus(UPDATED_WORK_STATUS)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);

        restStudentHomeWorkTrackMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStudentHomeWorkTrack.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStudentHomeWorkTrack))
            )
            .andExpect(status().isOk());

        // Validate the StudentHomeWorkTrack in the database
        List<StudentHomeWorkTrack> studentHomeWorkTrackList = studentHomeWorkTrackRepository.findAll();
        assertThat(studentHomeWorkTrackList).hasSize(databaseSizeBeforeUpdate);
        StudentHomeWorkTrack testStudentHomeWorkTrack = studentHomeWorkTrackList.get(studentHomeWorkTrackList.size() - 1);
        assertThat(testStudentHomeWorkTrack.getWorkStatus()).isEqualTo(UPDATED_WORK_STATUS);
        assertThat(testStudentHomeWorkTrack.getRemarks()).isEqualTo(DEFAULT_REMARKS);
        assertThat(testStudentHomeWorkTrack.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testStudentHomeWorkTrack.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testStudentHomeWorkTrack.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void fullUpdateStudentHomeWorkTrackWithPatch() throws Exception {
        // Initialize the database
        studentHomeWorkTrackRepository.saveAndFlush(studentHomeWorkTrack);

        int databaseSizeBeforeUpdate = studentHomeWorkTrackRepository.findAll().size();

        // Update the studentHomeWorkTrack using partial update
        StudentHomeWorkTrack partialUpdatedStudentHomeWorkTrack = new StudentHomeWorkTrack();
        partialUpdatedStudentHomeWorkTrack.setId(studentHomeWorkTrack.getId());

        partialUpdatedStudentHomeWorkTrack
            .workStatus(UPDATED_WORK_STATUS)
            .remarks(UPDATED_REMARKS)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);

        restStudentHomeWorkTrackMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStudentHomeWorkTrack.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStudentHomeWorkTrack))
            )
            .andExpect(status().isOk());

        // Validate the StudentHomeWorkTrack in the database
        List<StudentHomeWorkTrack> studentHomeWorkTrackList = studentHomeWorkTrackRepository.findAll();
        assertThat(studentHomeWorkTrackList).hasSize(databaseSizeBeforeUpdate);
        StudentHomeWorkTrack testStudentHomeWorkTrack = studentHomeWorkTrackList.get(studentHomeWorkTrackList.size() - 1);
        assertThat(testStudentHomeWorkTrack.getWorkStatus()).isEqualTo(UPDATED_WORK_STATUS);
        assertThat(testStudentHomeWorkTrack.getRemarks()).isEqualTo(UPDATED_REMARKS);
        assertThat(testStudentHomeWorkTrack.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testStudentHomeWorkTrack.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testStudentHomeWorkTrack.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingStudentHomeWorkTrack() throws Exception {
        int databaseSizeBeforeUpdate = studentHomeWorkTrackRepository.findAll().size();
        studentHomeWorkTrack.setId(count.incrementAndGet());

        // Create the StudentHomeWorkTrack
        StudentHomeWorkTrackDTO studentHomeWorkTrackDTO = studentHomeWorkTrackMapper.toDto(studentHomeWorkTrack);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudentHomeWorkTrackMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, studentHomeWorkTrackDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(studentHomeWorkTrackDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentHomeWorkTrack in the database
        List<StudentHomeWorkTrack> studentHomeWorkTrackList = studentHomeWorkTrackRepository.findAll();
        assertThat(studentHomeWorkTrackList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStudentHomeWorkTrack() throws Exception {
        int databaseSizeBeforeUpdate = studentHomeWorkTrackRepository.findAll().size();
        studentHomeWorkTrack.setId(count.incrementAndGet());

        // Create the StudentHomeWorkTrack
        StudentHomeWorkTrackDTO studentHomeWorkTrackDTO = studentHomeWorkTrackMapper.toDto(studentHomeWorkTrack);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentHomeWorkTrackMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(studentHomeWorkTrackDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentHomeWorkTrack in the database
        List<StudentHomeWorkTrack> studentHomeWorkTrackList = studentHomeWorkTrackRepository.findAll();
        assertThat(studentHomeWorkTrackList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStudentHomeWorkTrack() throws Exception {
        int databaseSizeBeforeUpdate = studentHomeWorkTrackRepository.findAll().size();
        studentHomeWorkTrack.setId(count.incrementAndGet());

        // Create the StudentHomeWorkTrack
        StudentHomeWorkTrackDTO studentHomeWorkTrackDTO = studentHomeWorkTrackMapper.toDto(studentHomeWorkTrack);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentHomeWorkTrackMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(studentHomeWorkTrackDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StudentHomeWorkTrack in the database
        List<StudentHomeWorkTrack> studentHomeWorkTrackList = studentHomeWorkTrackRepository.findAll();
        assertThat(studentHomeWorkTrackList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStudentHomeWorkTrack() throws Exception {
        // Initialize the database
        studentHomeWorkTrackRepository.saveAndFlush(studentHomeWorkTrack);

        int databaseSizeBeforeDelete = studentHomeWorkTrackRepository.findAll().size();

        // Delete the studentHomeWorkTrack
        restStudentHomeWorkTrackMockMvc
            .perform(delete(ENTITY_API_URL_ID, studentHomeWorkTrack.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<StudentHomeWorkTrack> studentHomeWorkTrackList = studentHomeWorkTrackRepository.findAll();
        assertThat(studentHomeWorkTrackList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
