package com.ssik.manageit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ssik.manageit.IntegrationTest;
import com.ssik.manageit.domain.ClassStudent;
import com.ssik.manageit.domain.SchoolLedgerHead;
import com.ssik.manageit.domain.StudentDiscount;
import com.ssik.manageit.domain.enumeration.FeeYear;
import com.ssik.manageit.repository.StudentDiscountRepository;
import com.ssik.manageit.service.criteria.StudentDiscountCriteria;
import com.ssik.manageit.service.dto.StudentDiscountDTO;
import com.ssik.manageit.service.mapper.StudentDiscountMapper;
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
 * Integration tests for the {@link StudentDiscountResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StudentDiscountResourceIT {

	private static final FeeYear DEFAULT_FEE_YEAR = FeeYear.YEAR_2023;
	private static final FeeYear UPDATED_FEE_YEAR = FeeYear.YEAR_2023;

	private static final Integer DEFAULT_DUE_DATE = 1;
	private static final Integer UPDATED_DUE_DATE = 2;
	private static final Integer SMALLER_DUE_DATE = 1 - 1;

	private static final Double DEFAULT_JAN_FEE_DISC = 1D;
	private static final Double UPDATED_JAN_FEE_DISC = 2D;
	private static final Double SMALLER_JAN_FEE_DISC = 1D - 1D;

	private static final Double DEFAULT_FEB_FEE_DISC = 1D;
	private static final Double UPDATED_FEB_FEE_DISC = 2D;
	private static final Double SMALLER_FEB_FEE_DISC = 1D - 1D;

	private static final Double DEFAULT_MAR_FEE_DISC = 1D;
	private static final Double UPDATED_MAR_FEE_DISC = 2D;
	private static final Double SMALLER_MAR_FEE_DISC = 1D - 1D;

	private static final Double DEFAULT_APR_FEE_DISC = 1D;
	private static final Double UPDATED_APR_FEE_DISC = 2D;
	private static final Double SMALLER_APR_FEE_DISC = 1D - 1D;

	private static final Double DEFAULT_MAY_FEE_DISC = 1D;
	private static final Double UPDATED_MAY_FEE_DISC = 2D;
	private static final Double SMALLER_MAY_FEE_DISC = 1D - 1D;

	private static final Double DEFAULT_JUN_FEE_DISC = 1D;
	private static final Double UPDATED_JUN_FEE_DISC = 2D;
	private static final Double SMALLER_JUN_FEE_DISC = 1D - 1D;

	private static final Double DEFAULT_JUL_FEE_DISC = 1D;
	private static final Double UPDATED_JUL_FEE_DISC = 2D;
	private static final Double SMALLER_JUL_FEE_DISC = 1D - 1D;

	private static final Double DEFAULT_AUG_FEE_DISC = 1D;
	private static final Double UPDATED_AUG_FEE_DISC = 2D;
	private static final Double SMALLER_AUG_FEE_DISC = 1D - 1D;

	private static final Double DEFAULT_SEP_FEE_DISC = 1D;
	private static final Double UPDATED_SEP_FEE_DISC = 2D;
	private static final Double SMALLER_SEP_FEE_DISC = 1D - 1D;

	private static final Double DEFAULT_OCT_FEE_DISC = 1D;
	private static final Double UPDATED_OCT_FEE_DISC = 2D;
	private static final Double SMALLER_OCT_FEE_DISC = 1D - 1D;

	private static final Double DEFAULT_NOV_FEE_DISC = 1D;
	private static final Double UPDATED_NOV_FEE_DISC = 2D;
	private static final Double SMALLER_NOV_FEE_DISC = 1D - 1D;

	private static final Double DEFAULT_DEC_FEE_DISC = 1D;
	private static final Double UPDATED_DEC_FEE_DISC = 2D;
	private static final Double SMALLER_DEC_FEE_DISC = 1D - 1D;

	private static final LocalDate DEFAULT_CREATE_DATE = LocalDate.ofEpochDay(0L);
	private static final LocalDate UPDATED_CREATE_DATE = LocalDate.now(ZoneId.systemDefault());
	private static final LocalDate SMALLER_CREATE_DATE = LocalDate.ofEpochDay(-1L);

	private static final LocalDate DEFAULT_LAST_MODIFIED = LocalDate.ofEpochDay(0L);
	private static final LocalDate UPDATED_LAST_MODIFIED = LocalDate.now(ZoneId.systemDefault());
	private static final LocalDate SMALLER_LAST_MODIFIED = LocalDate.ofEpochDay(-1L);

	private static final LocalDate DEFAULT_CANCEL_DATE = LocalDate.ofEpochDay(0L);
	private static final LocalDate UPDATED_CANCEL_DATE = LocalDate.now(ZoneId.systemDefault());
	private static final LocalDate SMALLER_CANCEL_DATE = LocalDate.ofEpochDay(-1L);

	private static final String ENTITY_API_URL = "/api/student-discounts";
	private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

	private static Random random = new Random();
	private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

	@Autowired
	private StudentDiscountRepository studentDiscountRepository;

	@Autowired
	private StudentDiscountMapper studentDiscountMapper;

	@Autowired
	private EntityManager em;

	@Autowired
	private MockMvc restStudentDiscountMockMvc;

	private StudentDiscount studentDiscount;

	/**
	 * Create an entity for this test.
	 *
	 * This is a static method, as tests for other entities might also need it, if
	 * they test an entity which requires the current entity.
	 */
	public static StudentDiscount createEntity(EntityManager em) {
		StudentDiscount studentDiscount = new StudentDiscount().feeYear(DEFAULT_FEE_YEAR).dueDate(DEFAULT_DUE_DATE)
				.janFeeDisc(DEFAULT_JAN_FEE_DISC).febFeeDisc(DEFAULT_FEB_FEE_DISC).marFeeDisc(DEFAULT_MAR_FEE_DISC)
				.aprFeeDisc(DEFAULT_APR_FEE_DISC).mayFeeDisc(DEFAULT_MAY_FEE_DISC).junFeeDisc(DEFAULT_JUN_FEE_DISC)
				.julFeeDisc(DEFAULT_JUL_FEE_DISC).augFeeDisc(DEFAULT_AUG_FEE_DISC).sepFeeDisc(DEFAULT_SEP_FEE_DISC)
				.octFeeDisc(DEFAULT_OCT_FEE_DISC).novFeeDisc(DEFAULT_NOV_FEE_DISC).decFeeDisc(DEFAULT_DEC_FEE_DISC)
				.createDate(DEFAULT_CREATE_DATE).lastModified(DEFAULT_LAST_MODIFIED).cancelDate(DEFAULT_CANCEL_DATE);
		return studentDiscount;
	}

	/**
	 * Create an updated entity for this test.
	 *
	 * This is a static method, as tests for other entities might also need it, if
	 * they test an entity which requires the current entity.
	 */
	public static StudentDiscount createUpdatedEntity(EntityManager em) {
		StudentDiscount studentDiscount = new StudentDiscount().feeYear(UPDATED_FEE_YEAR).dueDate(UPDATED_DUE_DATE)
				.janFeeDisc(UPDATED_JAN_FEE_DISC).febFeeDisc(UPDATED_FEB_FEE_DISC).marFeeDisc(UPDATED_MAR_FEE_DISC)
				.aprFeeDisc(UPDATED_APR_FEE_DISC).mayFeeDisc(UPDATED_MAY_FEE_DISC).junFeeDisc(UPDATED_JUN_FEE_DISC)
				.julFeeDisc(UPDATED_JUL_FEE_DISC).augFeeDisc(UPDATED_AUG_FEE_DISC).sepFeeDisc(UPDATED_SEP_FEE_DISC)
				.octFeeDisc(UPDATED_OCT_FEE_DISC).novFeeDisc(UPDATED_NOV_FEE_DISC).decFeeDisc(UPDATED_DEC_FEE_DISC)
				.createDate(UPDATED_CREATE_DATE).lastModified(UPDATED_LAST_MODIFIED).cancelDate(UPDATED_CANCEL_DATE);
		return studentDiscount;
	}

	@BeforeEach
	public void initTest() {
		studentDiscount = createEntity(em);
	}

	@Test
	@Transactional
	void createStudentDiscount() throws Exception {
		int databaseSizeBeforeCreate = studentDiscountRepository.findAll().size();
		// Create the StudentDiscount
		StudentDiscountDTO studentDiscountDTO = studentDiscountMapper.toDto(studentDiscount);
		restStudentDiscountMockMvc
				.perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON)
						.content(TestUtil.convertObjectToJsonBytes(studentDiscountDTO)))
				.andExpect(status().isCreated());

		// Validate the StudentDiscount in the database
		List<StudentDiscount> studentDiscountList = studentDiscountRepository.findAll();
		assertThat(studentDiscountList).hasSize(databaseSizeBeforeCreate + 1);
		StudentDiscount testStudentDiscount = studentDiscountList.get(studentDiscountList.size() - 1);
		assertThat(testStudentDiscount.getFeeYear()).isEqualTo(DEFAULT_FEE_YEAR);
		assertThat(testStudentDiscount.getDueDate()).isEqualTo(DEFAULT_DUE_DATE);
		assertThat(testStudentDiscount.getJanFeeDisc()).isEqualTo(DEFAULT_JAN_FEE_DISC);
		assertThat(testStudentDiscount.getFebFeeDisc()).isEqualTo(DEFAULT_FEB_FEE_DISC);
		assertThat(testStudentDiscount.getMarFeeDisc()).isEqualTo(DEFAULT_MAR_FEE_DISC);
		assertThat(testStudentDiscount.getAprFeeDisc()).isEqualTo(DEFAULT_APR_FEE_DISC);
		assertThat(testStudentDiscount.getMayFeeDisc()).isEqualTo(DEFAULT_MAY_FEE_DISC);
		assertThat(testStudentDiscount.getJunFeeDisc()).isEqualTo(DEFAULT_JUN_FEE_DISC);
		assertThat(testStudentDiscount.getJulFeeDisc()).isEqualTo(DEFAULT_JUL_FEE_DISC);
		assertThat(testStudentDiscount.getAugFeeDisc()).isEqualTo(DEFAULT_AUG_FEE_DISC);
		assertThat(testStudentDiscount.getSepFeeDisc()).isEqualTo(DEFAULT_SEP_FEE_DISC);
		assertThat(testStudentDiscount.getOctFeeDisc()).isEqualTo(DEFAULT_OCT_FEE_DISC);
		assertThat(testStudentDiscount.getNovFeeDisc()).isEqualTo(DEFAULT_NOV_FEE_DISC);
		assertThat(testStudentDiscount.getDecFeeDisc()).isEqualTo(DEFAULT_DEC_FEE_DISC);
		assertThat(testStudentDiscount.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
		assertThat(testStudentDiscount.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
		assertThat(testStudentDiscount.getCancelDate()).isEqualTo(DEFAULT_CANCEL_DATE);
	}

	@Test
	@Transactional
	void createStudentDiscountWithExistingId() throws Exception {
		// Create the StudentDiscount with an existing ID
		studentDiscount.setId(1L);
		StudentDiscountDTO studentDiscountDTO = studentDiscountMapper.toDto(studentDiscount);

		int databaseSizeBeforeCreate = studentDiscountRepository.findAll().size();

		// An entity with an existing ID cannot be created, so this API call must fail
		restStudentDiscountMockMvc
				.perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON)
						.content(TestUtil.convertObjectToJsonBytes(studentDiscountDTO)))
				.andExpect(status().isBadRequest());

		// Validate the StudentDiscount in the database
		List<StudentDiscount> studentDiscountList = studentDiscountRepository.findAll();
		assertThat(studentDiscountList).hasSize(databaseSizeBeforeCreate);
	}

	@Test
	@Transactional
	void checkFeeYearIsRequired() throws Exception {
		int databaseSizeBeforeTest = studentDiscountRepository.findAll().size();
		// set the field null
		studentDiscount.setFeeYear(null);

		// Create the StudentDiscount, which fails.
		StudentDiscountDTO studentDiscountDTO = studentDiscountMapper.toDto(studentDiscount);

		restStudentDiscountMockMvc
				.perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON)
						.content(TestUtil.convertObjectToJsonBytes(studentDiscountDTO)))
				.andExpect(status().isBadRequest());

		List<StudentDiscount> studentDiscountList = studentDiscountRepository.findAll();
		assertThat(studentDiscountList).hasSize(databaseSizeBeforeTest);
	}

	@Test
	@Transactional
	void getAllStudentDiscounts() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList
		restStudentDiscountMockMvc.perform(get(ENTITY_API_URL + "?sort=id,desc")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.[*].id").value(hasItem(studentDiscount.getId().intValue())))
				.andExpect(jsonPath("$.[*].feeYear").value(hasItem(DEFAULT_FEE_YEAR.toString())))
				.andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE)))
				.andExpect(jsonPath("$.[*].janFeeDisc").value(hasItem(DEFAULT_JAN_FEE_DISC.doubleValue())))
				.andExpect(jsonPath("$.[*].febFeeDisc").value(hasItem(DEFAULT_FEB_FEE_DISC.doubleValue())))
				.andExpect(jsonPath("$.[*].marFeeDisc").value(hasItem(DEFAULT_MAR_FEE_DISC.doubleValue())))
				.andExpect(jsonPath("$.[*].aprFeeDisc").value(hasItem(DEFAULT_APR_FEE_DISC.doubleValue())))
				.andExpect(jsonPath("$.[*].mayFeeDisc").value(hasItem(DEFAULT_MAY_FEE_DISC.doubleValue())))
				.andExpect(jsonPath("$.[*].junFeeDisc").value(hasItem(DEFAULT_JUN_FEE_DISC.doubleValue())))
				.andExpect(jsonPath("$.[*].julFeeDisc").value(hasItem(DEFAULT_JUL_FEE_DISC.doubleValue())))
				.andExpect(jsonPath("$.[*].augFeeDisc").value(hasItem(DEFAULT_AUG_FEE_DISC.doubleValue())))
				.andExpect(jsonPath("$.[*].sepFeeDisc").value(hasItem(DEFAULT_SEP_FEE_DISC.doubleValue())))
				.andExpect(jsonPath("$.[*].octFeeDisc").value(hasItem(DEFAULT_OCT_FEE_DISC.doubleValue())))
				.andExpect(jsonPath("$.[*].novFeeDisc").value(hasItem(DEFAULT_NOV_FEE_DISC.doubleValue())))
				.andExpect(jsonPath("$.[*].decFeeDisc").value(hasItem(DEFAULT_DEC_FEE_DISC.doubleValue())))
				.andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
				.andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
				.andExpect(jsonPath("$.[*].cancelDate").value(hasItem(DEFAULT_CANCEL_DATE.toString())));
	}

	@Test
	@Transactional
	void getStudentDiscount() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get the studentDiscount
		restStudentDiscountMockMvc.perform(get(ENTITY_API_URL_ID, studentDiscount.getId())).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.id").value(studentDiscount.getId().intValue()))
				.andExpect(jsonPath("$.feeYear").value(DEFAULT_FEE_YEAR.toString()))
				.andExpect(jsonPath("$.dueDate").value(DEFAULT_DUE_DATE))
				.andExpect(jsonPath("$.janFeeDisc").value(DEFAULT_JAN_FEE_DISC.doubleValue()))
				.andExpect(jsonPath("$.febFeeDisc").value(DEFAULT_FEB_FEE_DISC.doubleValue()))
				.andExpect(jsonPath("$.marFeeDisc").value(DEFAULT_MAR_FEE_DISC.doubleValue()))
				.andExpect(jsonPath("$.aprFeeDisc").value(DEFAULT_APR_FEE_DISC.doubleValue()))
				.andExpect(jsonPath("$.mayFeeDisc").value(DEFAULT_MAY_FEE_DISC.doubleValue()))
				.andExpect(jsonPath("$.junFeeDisc").value(DEFAULT_JUN_FEE_DISC.doubleValue()))
				.andExpect(jsonPath("$.julFeeDisc").value(DEFAULT_JUL_FEE_DISC.doubleValue()))
				.andExpect(jsonPath("$.augFeeDisc").value(DEFAULT_AUG_FEE_DISC.doubleValue()))
				.andExpect(jsonPath("$.sepFeeDisc").value(DEFAULT_SEP_FEE_DISC.doubleValue()))
				.andExpect(jsonPath("$.octFeeDisc").value(DEFAULT_OCT_FEE_DISC.doubleValue()))
				.andExpect(jsonPath("$.novFeeDisc").value(DEFAULT_NOV_FEE_DISC.doubleValue()))
				.andExpect(jsonPath("$.decFeeDisc").value(DEFAULT_DEC_FEE_DISC.doubleValue()))
				.andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
				.andExpect(jsonPath("$.lastModified").value(DEFAULT_LAST_MODIFIED.toString()))
				.andExpect(jsonPath("$.cancelDate").value(DEFAULT_CANCEL_DATE.toString()));
	}

	@Test
	@Transactional
	void getStudentDiscountsByIdFiltering() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		Long id = studentDiscount.getId();

		defaultStudentDiscountShouldBeFound("id.equals=" + id);
		defaultStudentDiscountShouldNotBeFound("id.notEquals=" + id);

		defaultStudentDiscountShouldBeFound("id.greaterThanOrEqual=" + id);
		defaultStudentDiscountShouldNotBeFound("id.greaterThan=" + id);

		defaultStudentDiscountShouldBeFound("id.lessThanOrEqual=" + id);
		defaultStudentDiscountShouldNotBeFound("id.lessThan=" + id);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByFeeYearIsEqualToSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where feeYear equals to DEFAULT_FEE_YEAR
		defaultStudentDiscountShouldBeFound("feeYear.equals=" + DEFAULT_FEE_YEAR);

		// Get all the studentDiscountList where feeYear equals to UPDATED_FEE_YEAR
		defaultStudentDiscountShouldNotBeFound("feeYear.equals=" + UPDATED_FEE_YEAR);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByFeeYearIsNotEqualToSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where feeYear not equals to DEFAULT_FEE_YEAR
		defaultStudentDiscountShouldNotBeFound("feeYear.notEquals=" + DEFAULT_FEE_YEAR);

		// Get all the studentDiscountList where feeYear not equals to UPDATED_FEE_YEAR
		defaultStudentDiscountShouldBeFound("feeYear.notEquals=" + UPDATED_FEE_YEAR);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByFeeYearIsInShouldWork() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where feeYear in DEFAULT_FEE_YEAR or
		// UPDATED_FEE_YEAR
		defaultStudentDiscountShouldBeFound("feeYear.in=" + DEFAULT_FEE_YEAR + "," + UPDATED_FEE_YEAR);

		// Get all the studentDiscountList where feeYear equals to UPDATED_FEE_YEAR
		defaultStudentDiscountShouldNotBeFound("feeYear.in=" + UPDATED_FEE_YEAR);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByFeeYearIsNullOrNotNull() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where feeYear is not null
		defaultStudentDiscountShouldBeFound("feeYear.specified=true");

		// Get all the studentDiscountList where feeYear is null
		defaultStudentDiscountShouldNotBeFound("feeYear.specified=false");
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByDueDateIsEqualToSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where dueDate equals to DEFAULT_DUE_DATE
		defaultStudentDiscountShouldBeFound("dueDate.equals=" + DEFAULT_DUE_DATE);

		// Get all the studentDiscountList where dueDate equals to UPDATED_DUE_DATE
		defaultStudentDiscountShouldNotBeFound("dueDate.equals=" + UPDATED_DUE_DATE);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByDueDateIsNotEqualToSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where dueDate not equals to DEFAULT_DUE_DATE
		defaultStudentDiscountShouldNotBeFound("dueDate.notEquals=" + DEFAULT_DUE_DATE);

		// Get all the studentDiscountList where dueDate not equals to UPDATED_DUE_DATE
		defaultStudentDiscountShouldBeFound("dueDate.notEquals=" + UPDATED_DUE_DATE);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByDueDateIsInShouldWork() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where dueDate in DEFAULT_DUE_DATE or
		// UPDATED_DUE_DATE
		defaultStudentDiscountShouldBeFound("dueDate.in=" + DEFAULT_DUE_DATE + "," + UPDATED_DUE_DATE);

		// Get all the studentDiscountList where dueDate equals to UPDATED_DUE_DATE
		defaultStudentDiscountShouldNotBeFound("dueDate.in=" + UPDATED_DUE_DATE);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByDueDateIsNullOrNotNull() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where dueDate is not null
		defaultStudentDiscountShouldBeFound("dueDate.specified=true");

		// Get all the studentDiscountList where dueDate is null
		defaultStudentDiscountShouldNotBeFound("dueDate.specified=false");
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByDueDateIsGreaterThanOrEqualToSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where dueDate is greater than or equal to
		// DEFAULT_DUE_DATE
		defaultStudentDiscountShouldBeFound("dueDate.greaterThanOrEqual=" + DEFAULT_DUE_DATE);

		// Get all the studentDiscountList where dueDate is greater than or equal to
		// UPDATED_DUE_DATE
		defaultStudentDiscountShouldNotBeFound("dueDate.greaterThanOrEqual=" + UPDATED_DUE_DATE);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByDueDateIsLessThanOrEqualToSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where dueDate is less than or equal to
		// DEFAULT_DUE_DATE
		defaultStudentDiscountShouldBeFound("dueDate.lessThanOrEqual=" + DEFAULT_DUE_DATE);

		// Get all the studentDiscountList where dueDate is less than or equal to
		// SMALLER_DUE_DATE
		defaultStudentDiscountShouldNotBeFound("dueDate.lessThanOrEqual=" + SMALLER_DUE_DATE);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByDueDateIsLessThanSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where dueDate is less than DEFAULT_DUE_DATE
		defaultStudentDiscountShouldNotBeFound("dueDate.lessThan=" + DEFAULT_DUE_DATE);

		// Get all the studentDiscountList where dueDate is less than UPDATED_DUE_DATE
		defaultStudentDiscountShouldBeFound("dueDate.lessThan=" + UPDATED_DUE_DATE);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByDueDateIsGreaterThanSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where dueDate is greater than
		// DEFAULT_DUE_DATE
		defaultStudentDiscountShouldNotBeFound("dueDate.greaterThan=" + DEFAULT_DUE_DATE);

		// Get all the studentDiscountList where dueDate is greater than
		// SMALLER_DUE_DATE
		defaultStudentDiscountShouldBeFound("dueDate.greaterThan=" + SMALLER_DUE_DATE);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByJanFeeDiscIsEqualToSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where janFeeDisc equals to
		// DEFAULT_JAN_FEE_DISC
		defaultStudentDiscountShouldBeFound("janFeeDisc.equals=" + DEFAULT_JAN_FEE_DISC);

		// Get all the studentDiscountList where janFeeDisc equals to
		// UPDATED_JAN_FEE_DISC
		defaultStudentDiscountShouldNotBeFound("janFeeDisc.equals=" + UPDATED_JAN_FEE_DISC);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByJanFeeDiscIsNotEqualToSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where janFeeDisc not equals to
		// DEFAULT_JAN_FEE_DISC
		defaultStudentDiscountShouldNotBeFound("janFeeDisc.notEquals=" + DEFAULT_JAN_FEE_DISC);

		// Get all the studentDiscountList where janFeeDisc not equals to
		// UPDATED_JAN_FEE_DISC
		defaultStudentDiscountShouldBeFound("janFeeDisc.notEquals=" + UPDATED_JAN_FEE_DISC);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByJanFeeDiscIsInShouldWork() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where janFeeDisc in DEFAULT_JAN_FEE_DISC or
		// UPDATED_JAN_FEE_DISC
		defaultStudentDiscountShouldBeFound("janFeeDisc.in=" + DEFAULT_JAN_FEE_DISC + "," + UPDATED_JAN_FEE_DISC);

		// Get all the studentDiscountList where janFeeDisc equals to
		// UPDATED_JAN_FEE_DISC
		defaultStudentDiscountShouldNotBeFound("janFeeDisc.in=" + UPDATED_JAN_FEE_DISC);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByJanFeeDiscIsNullOrNotNull() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where janFeeDisc is not null
		defaultStudentDiscountShouldBeFound("janFeeDisc.specified=true");

		// Get all the studentDiscountList where janFeeDisc is null
		defaultStudentDiscountShouldNotBeFound("janFeeDisc.specified=false");
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByJanFeeDiscIsGreaterThanOrEqualToSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where janFeeDisc is greater than or equal to
		// DEFAULT_JAN_FEE_DISC
		defaultStudentDiscountShouldBeFound("janFeeDisc.greaterThanOrEqual=" + DEFAULT_JAN_FEE_DISC);

		// Get all the studentDiscountList where janFeeDisc is greater than or equal to
		// UPDATED_JAN_FEE_DISC
		defaultStudentDiscountShouldNotBeFound("janFeeDisc.greaterThanOrEqual=" + UPDATED_JAN_FEE_DISC);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByJanFeeDiscIsLessThanOrEqualToSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where janFeeDisc is less than or equal to
		// DEFAULT_JAN_FEE_DISC
		defaultStudentDiscountShouldBeFound("janFeeDisc.lessThanOrEqual=" + DEFAULT_JAN_FEE_DISC);

		// Get all the studentDiscountList where janFeeDisc is less than or equal to
		// SMALLER_JAN_FEE_DISC
		defaultStudentDiscountShouldNotBeFound("janFeeDisc.lessThanOrEqual=" + SMALLER_JAN_FEE_DISC);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByJanFeeDiscIsLessThanSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where janFeeDisc is less than
		// DEFAULT_JAN_FEE_DISC
		defaultStudentDiscountShouldNotBeFound("janFeeDisc.lessThan=" + DEFAULT_JAN_FEE_DISC);

		// Get all the studentDiscountList where janFeeDisc is less than
		// UPDATED_JAN_FEE_DISC
		defaultStudentDiscountShouldBeFound("janFeeDisc.lessThan=" + UPDATED_JAN_FEE_DISC);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByJanFeeDiscIsGreaterThanSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where janFeeDisc is greater than
		// DEFAULT_JAN_FEE_DISC
		defaultStudentDiscountShouldNotBeFound("janFeeDisc.greaterThan=" + DEFAULT_JAN_FEE_DISC);

		// Get all the studentDiscountList where janFeeDisc is greater than
		// SMALLER_JAN_FEE_DISC
		defaultStudentDiscountShouldBeFound("janFeeDisc.greaterThan=" + SMALLER_JAN_FEE_DISC);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByFebFeeDiscIsEqualToSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where febFeeDisc equals to
		// DEFAULT_FEB_FEE_DISC
		defaultStudentDiscountShouldBeFound("febFeeDisc.equals=" + DEFAULT_FEB_FEE_DISC);

		// Get all the studentDiscountList where febFeeDisc equals to
		// UPDATED_FEB_FEE_DISC
		defaultStudentDiscountShouldNotBeFound("febFeeDisc.equals=" + UPDATED_FEB_FEE_DISC);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByFebFeeDiscIsNotEqualToSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where febFeeDisc not equals to
		// DEFAULT_FEB_FEE_DISC
		defaultStudentDiscountShouldNotBeFound("febFeeDisc.notEquals=" + DEFAULT_FEB_FEE_DISC);

		// Get all the studentDiscountList where febFeeDisc not equals to
		// UPDATED_FEB_FEE_DISC
		defaultStudentDiscountShouldBeFound("febFeeDisc.notEquals=" + UPDATED_FEB_FEE_DISC);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByFebFeeDiscIsInShouldWork() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where febFeeDisc in DEFAULT_FEB_FEE_DISC or
		// UPDATED_FEB_FEE_DISC
		defaultStudentDiscountShouldBeFound("febFeeDisc.in=" + DEFAULT_FEB_FEE_DISC + "," + UPDATED_FEB_FEE_DISC);

		// Get all the studentDiscountList where febFeeDisc equals to
		// UPDATED_FEB_FEE_DISC
		defaultStudentDiscountShouldNotBeFound("febFeeDisc.in=" + UPDATED_FEB_FEE_DISC);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByFebFeeDiscIsNullOrNotNull() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where febFeeDisc is not null
		defaultStudentDiscountShouldBeFound("febFeeDisc.specified=true");

		// Get all the studentDiscountList where febFeeDisc is null
		defaultStudentDiscountShouldNotBeFound("febFeeDisc.specified=false");
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByFebFeeDiscIsGreaterThanOrEqualToSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where febFeeDisc is greater than or equal to
		// DEFAULT_FEB_FEE_DISC
		defaultStudentDiscountShouldBeFound("febFeeDisc.greaterThanOrEqual=" + DEFAULT_FEB_FEE_DISC);

		// Get all the studentDiscountList where febFeeDisc is greater than or equal to
		// UPDATED_FEB_FEE_DISC
		defaultStudentDiscountShouldNotBeFound("febFeeDisc.greaterThanOrEqual=" + UPDATED_FEB_FEE_DISC);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByFebFeeDiscIsLessThanOrEqualToSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where febFeeDisc is less than or equal to
		// DEFAULT_FEB_FEE_DISC
		defaultStudentDiscountShouldBeFound("febFeeDisc.lessThanOrEqual=" + DEFAULT_FEB_FEE_DISC);

		// Get all the studentDiscountList where febFeeDisc is less than or equal to
		// SMALLER_FEB_FEE_DISC
		defaultStudentDiscountShouldNotBeFound("febFeeDisc.lessThanOrEqual=" + SMALLER_FEB_FEE_DISC);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByFebFeeDiscIsLessThanSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where febFeeDisc is less than
		// DEFAULT_FEB_FEE_DISC
		defaultStudentDiscountShouldNotBeFound("febFeeDisc.lessThan=" + DEFAULT_FEB_FEE_DISC);

		// Get all the studentDiscountList where febFeeDisc is less than
		// UPDATED_FEB_FEE_DISC
		defaultStudentDiscountShouldBeFound("febFeeDisc.lessThan=" + UPDATED_FEB_FEE_DISC);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByFebFeeDiscIsGreaterThanSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where febFeeDisc is greater than
		// DEFAULT_FEB_FEE_DISC
		defaultStudentDiscountShouldNotBeFound("febFeeDisc.greaterThan=" + DEFAULT_FEB_FEE_DISC);

		// Get all the studentDiscountList where febFeeDisc is greater than
		// SMALLER_FEB_FEE_DISC
		defaultStudentDiscountShouldBeFound("febFeeDisc.greaterThan=" + SMALLER_FEB_FEE_DISC);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByMarFeeDiscIsEqualToSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where marFeeDisc equals to
		// DEFAULT_MAR_FEE_DISC
		defaultStudentDiscountShouldBeFound("marFeeDisc.equals=" + DEFAULT_MAR_FEE_DISC);

		// Get all the studentDiscountList where marFeeDisc equals to
		// UPDATED_MAR_FEE_DISC
		defaultStudentDiscountShouldNotBeFound("marFeeDisc.equals=" + UPDATED_MAR_FEE_DISC);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByMarFeeDiscIsNotEqualToSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where marFeeDisc not equals to
		// DEFAULT_MAR_FEE_DISC
		defaultStudentDiscountShouldNotBeFound("marFeeDisc.notEquals=" + DEFAULT_MAR_FEE_DISC);

		// Get all the studentDiscountList where marFeeDisc not equals to
		// UPDATED_MAR_FEE_DISC
		defaultStudentDiscountShouldBeFound("marFeeDisc.notEquals=" + UPDATED_MAR_FEE_DISC);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByMarFeeDiscIsInShouldWork() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where marFeeDisc in DEFAULT_MAR_FEE_DISC or
		// UPDATED_MAR_FEE_DISC
		defaultStudentDiscountShouldBeFound("marFeeDisc.in=" + DEFAULT_MAR_FEE_DISC + "," + UPDATED_MAR_FEE_DISC);

		// Get all the studentDiscountList where marFeeDisc equals to
		// UPDATED_MAR_FEE_DISC
		defaultStudentDiscountShouldNotBeFound("marFeeDisc.in=" + UPDATED_MAR_FEE_DISC);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByMarFeeDiscIsNullOrNotNull() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where marFeeDisc is not null
		defaultStudentDiscountShouldBeFound("marFeeDisc.specified=true");

		// Get all the studentDiscountList where marFeeDisc is null
		defaultStudentDiscountShouldNotBeFound("marFeeDisc.specified=false");
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByMarFeeDiscIsGreaterThanOrEqualToSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where marFeeDisc is greater than or equal to
		// DEFAULT_MAR_FEE_DISC
		defaultStudentDiscountShouldBeFound("marFeeDisc.greaterThanOrEqual=" + DEFAULT_MAR_FEE_DISC);

		// Get all the studentDiscountList where marFeeDisc is greater than or equal to
		// UPDATED_MAR_FEE_DISC
		defaultStudentDiscountShouldNotBeFound("marFeeDisc.greaterThanOrEqual=" + UPDATED_MAR_FEE_DISC);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByMarFeeDiscIsLessThanOrEqualToSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where marFeeDisc is less than or equal to
		// DEFAULT_MAR_FEE_DISC
		defaultStudentDiscountShouldBeFound("marFeeDisc.lessThanOrEqual=" + DEFAULT_MAR_FEE_DISC);

		// Get all the studentDiscountList where marFeeDisc is less than or equal to
		// SMALLER_MAR_FEE_DISC
		defaultStudentDiscountShouldNotBeFound("marFeeDisc.lessThanOrEqual=" + SMALLER_MAR_FEE_DISC);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByMarFeeDiscIsLessThanSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where marFeeDisc is less than
		// DEFAULT_MAR_FEE_DISC
		defaultStudentDiscountShouldNotBeFound("marFeeDisc.lessThan=" + DEFAULT_MAR_FEE_DISC);

		// Get all the studentDiscountList where marFeeDisc is less than
		// UPDATED_MAR_FEE_DISC
		defaultStudentDiscountShouldBeFound("marFeeDisc.lessThan=" + UPDATED_MAR_FEE_DISC);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByMarFeeDiscIsGreaterThanSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where marFeeDisc is greater than
		// DEFAULT_MAR_FEE_DISC
		defaultStudentDiscountShouldNotBeFound("marFeeDisc.greaterThan=" + DEFAULT_MAR_FEE_DISC);

		// Get all the studentDiscountList where marFeeDisc is greater than
		// SMALLER_MAR_FEE_DISC
		defaultStudentDiscountShouldBeFound("marFeeDisc.greaterThan=" + SMALLER_MAR_FEE_DISC);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByAprFeeDiscIsEqualToSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where aprFeeDisc equals to
		// DEFAULT_APR_FEE_DISC
		defaultStudentDiscountShouldBeFound("aprFeeDisc.equals=" + DEFAULT_APR_FEE_DISC);

		// Get all the studentDiscountList where aprFeeDisc equals to
		// UPDATED_APR_FEE_DISC
		defaultStudentDiscountShouldNotBeFound("aprFeeDisc.equals=" + UPDATED_APR_FEE_DISC);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByAprFeeDiscIsNotEqualToSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where aprFeeDisc not equals to
		// DEFAULT_APR_FEE_DISC
		defaultStudentDiscountShouldNotBeFound("aprFeeDisc.notEquals=" + DEFAULT_APR_FEE_DISC);

		// Get all the studentDiscountList where aprFeeDisc not equals to
		// UPDATED_APR_FEE_DISC
		defaultStudentDiscountShouldBeFound("aprFeeDisc.notEquals=" + UPDATED_APR_FEE_DISC);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByAprFeeDiscIsInShouldWork() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where aprFeeDisc in DEFAULT_APR_FEE_DISC or
		// UPDATED_APR_FEE_DISC
		defaultStudentDiscountShouldBeFound("aprFeeDisc.in=" + DEFAULT_APR_FEE_DISC + "," + UPDATED_APR_FEE_DISC);

		// Get all the studentDiscountList where aprFeeDisc equals to
		// UPDATED_APR_FEE_DISC
		defaultStudentDiscountShouldNotBeFound("aprFeeDisc.in=" + UPDATED_APR_FEE_DISC);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByAprFeeDiscIsNullOrNotNull() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where aprFeeDisc is not null
		defaultStudentDiscountShouldBeFound("aprFeeDisc.specified=true");

		// Get all the studentDiscountList where aprFeeDisc is null
		defaultStudentDiscountShouldNotBeFound("aprFeeDisc.specified=false");
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByAprFeeDiscIsGreaterThanOrEqualToSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where aprFeeDisc is greater than or equal to
		// DEFAULT_APR_FEE_DISC
		defaultStudentDiscountShouldBeFound("aprFeeDisc.greaterThanOrEqual=" + DEFAULT_APR_FEE_DISC);

		// Get all the studentDiscountList where aprFeeDisc is greater than or equal to
		// UPDATED_APR_FEE_DISC
		defaultStudentDiscountShouldNotBeFound("aprFeeDisc.greaterThanOrEqual=" + UPDATED_APR_FEE_DISC);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByAprFeeDiscIsLessThanOrEqualToSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where aprFeeDisc is less than or equal to
		// DEFAULT_APR_FEE_DISC
		defaultStudentDiscountShouldBeFound("aprFeeDisc.lessThanOrEqual=" + DEFAULT_APR_FEE_DISC);

		// Get all the studentDiscountList where aprFeeDisc is less than or equal to
		// SMALLER_APR_FEE_DISC
		defaultStudentDiscountShouldNotBeFound("aprFeeDisc.lessThanOrEqual=" + SMALLER_APR_FEE_DISC);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByAprFeeDiscIsLessThanSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where aprFeeDisc is less than
		// DEFAULT_APR_FEE_DISC
		defaultStudentDiscountShouldNotBeFound("aprFeeDisc.lessThan=" + DEFAULT_APR_FEE_DISC);

		// Get all the studentDiscountList where aprFeeDisc is less than
		// UPDATED_APR_FEE_DISC
		defaultStudentDiscountShouldBeFound("aprFeeDisc.lessThan=" + UPDATED_APR_FEE_DISC);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByAprFeeDiscIsGreaterThanSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where aprFeeDisc is greater than
		// DEFAULT_APR_FEE_DISC
		defaultStudentDiscountShouldNotBeFound("aprFeeDisc.greaterThan=" + DEFAULT_APR_FEE_DISC);

		// Get all the studentDiscountList where aprFeeDisc is greater than
		// SMALLER_APR_FEE_DISC
		defaultStudentDiscountShouldBeFound("aprFeeDisc.greaterThan=" + SMALLER_APR_FEE_DISC);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByMayFeeDiscIsEqualToSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where mayFeeDisc equals to
		// DEFAULT_MAY_FEE_DISC
		defaultStudentDiscountShouldBeFound("mayFeeDisc.equals=" + DEFAULT_MAY_FEE_DISC);

		// Get all the studentDiscountList where mayFeeDisc equals to
		// UPDATED_MAY_FEE_DISC
		defaultStudentDiscountShouldNotBeFound("mayFeeDisc.equals=" + UPDATED_MAY_FEE_DISC);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByMayFeeDiscIsNotEqualToSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where mayFeeDisc not equals to
		// DEFAULT_MAY_FEE_DISC
		defaultStudentDiscountShouldNotBeFound("mayFeeDisc.notEquals=" + DEFAULT_MAY_FEE_DISC);

		// Get all the studentDiscountList where mayFeeDisc not equals to
		// UPDATED_MAY_FEE_DISC
		defaultStudentDiscountShouldBeFound("mayFeeDisc.notEquals=" + UPDATED_MAY_FEE_DISC);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByMayFeeDiscIsInShouldWork() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where mayFeeDisc in DEFAULT_MAY_FEE_DISC or
		// UPDATED_MAY_FEE_DISC
		defaultStudentDiscountShouldBeFound("mayFeeDisc.in=" + DEFAULT_MAY_FEE_DISC + "," + UPDATED_MAY_FEE_DISC);

		// Get all the studentDiscountList where mayFeeDisc equals to
		// UPDATED_MAY_FEE_DISC
		defaultStudentDiscountShouldNotBeFound("mayFeeDisc.in=" + UPDATED_MAY_FEE_DISC);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByMayFeeDiscIsNullOrNotNull() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where mayFeeDisc is not null
		defaultStudentDiscountShouldBeFound("mayFeeDisc.specified=true");

		// Get all the studentDiscountList where mayFeeDisc is null
		defaultStudentDiscountShouldNotBeFound("mayFeeDisc.specified=false");
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByMayFeeDiscIsGreaterThanOrEqualToSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where mayFeeDisc is greater than or equal to
		// DEFAULT_MAY_FEE_DISC
		defaultStudentDiscountShouldBeFound("mayFeeDisc.greaterThanOrEqual=" + DEFAULT_MAY_FEE_DISC);

		// Get all the studentDiscountList where mayFeeDisc is greater than or equal to
		// UPDATED_MAY_FEE_DISC
		defaultStudentDiscountShouldNotBeFound("mayFeeDisc.greaterThanOrEqual=" + UPDATED_MAY_FEE_DISC);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByMayFeeDiscIsLessThanOrEqualToSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where mayFeeDisc is less than or equal to
		// DEFAULT_MAY_FEE_DISC
		defaultStudentDiscountShouldBeFound("mayFeeDisc.lessThanOrEqual=" + DEFAULT_MAY_FEE_DISC);

		// Get all the studentDiscountList where mayFeeDisc is less than or equal to
		// SMALLER_MAY_FEE_DISC
		defaultStudentDiscountShouldNotBeFound("mayFeeDisc.lessThanOrEqual=" + SMALLER_MAY_FEE_DISC);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByMayFeeDiscIsLessThanSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where mayFeeDisc is less than
		// DEFAULT_MAY_FEE_DISC
		defaultStudentDiscountShouldNotBeFound("mayFeeDisc.lessThan=" + DEFAULT_MAY_FEE_DISC);

		// Get all the studentDiscountList where mayFeeDisc is less than
		// UPDATED_MAY_FEE_DISC
		defaultStudentDiscountShouldBeFound("mayFeeDisc.lessThan=" + UPDATED_MAY_FEE_DISC);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByMayFeeDiscIsGreaterThanSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where mayFeeDisc is greater than
		// DEFAULT_MAY_FEE_DISC
		defaultStudentDiscountShouldNotBeFound("mayFeeDisc.greaterThan=" + DEFAULT_MAY_FEE_DISC);

		// Get all the studentDiscountList where mayFeeDisc is greater than
		// SMALLER_MAY_FEE_DISC
		defaultStudentDiscountShouldBeFound("mayFeeDisc.greaterThan=" + SMALLER_MAY_FEE_DISC);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByJunFeeDiscIsEqualToSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where junFeeDisc equals to
		// DEFAULT_JUN_FEE_DISC
		defaultStudentDiscountShouldBeFound("junFeeDisc.equals=" + DEFAULT_JUN_FEE_DISC);

		// Get all the studentDiscountList where junFeeDisc equals to
		// UPDATED_JUN_FEE_DISC
		defaultStudentDiscountShouldNotBeFound("junFeeDisc.equals=" + UPDATED_JUN_FEE_DISC);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByJunFeeDiscIsNotEqualToSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where junFeeDisc not equals to
		// DEFAULT_JUN_FEE_DISC
		defaultStudentDiscountShouldNotBeFound("junFeeDisc.notEquals=" + DEFAULT_JUN_FEE_DISC);

		// Get all the studentDiscountList where junFeeDisc not equals to
		// UPDATED_JUN_FEE_DISC
		defaultStudentDiscountShouldBeFound("junFeeDisc.notEquals=" + UPDATED_JUN_FEE_DISC);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByJunFeeDiscIsInShouldWork() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where junFeeDisc in DEFAULT_JUN_FEE_DISC or
		// UPDATED_JUN_FEE_DISC
		defaultStudentDiscountShouldBeFound("junFeeDisc.in=" + DEFAULT_JUN_FEE_DISC + "," + UPDATED_JUN_FEE_DISC);

		// Get all the studentDiscountList where junFeeDisc equals to
		// UPDATED_JUN_FEE_DISC
		defaultStudentDiscountShouldNotBeFound("junFeeDisc.in=" + UPDATED_JUN_FEE_DISC);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByJunFeeDiscIsNullOrNotNull() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where junFeeDisc is not null
		defaultStudentDiscountShouldBeFound("junFeeDisc.specified=true");

		// Get all the studentDiscountList where junFeeDisc is null
		defaultStudentDiscountShouldNotBeFound("junFeeDisc.specified=false");
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByJunFeeDiscIsGreaterThanOrEqualToSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where junFeeDisc is greater than or equal to
		// DEFAULT_JUN_FEE_DISC
		defaultStudentDiscountShouldBeFound("junFeeDisc.greaterThanOrEqual=" + DEFAULT_JUN_FEE_DISC);

		// Get all the studentDiscountList where junFeeDisc is greater than or equal to
		// UPDATED_JUN_FEE_DISC
		defaultStudentDiscountShouldNotBeFound("junFeeDisc.greaterThanOrEqual=" + UPDATED_JUN_FEE_DISC);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByJunFeeDiscIsLessThanOrEqualToSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where junFeeDisc is less than or equal to
		// DEFAULT_JUN_FEE_DISC
		defaultStudentDiscountShouldBeFound("junFeeDisc.lessThanOrEqual=" + DEFAULT_JUN_FEE_DISC);

		// Get all the studentDiscountList where junFeeDisc is less than or equal to
		// SMALLER_JUN_FEE_DISC
		defaultStudentDiscountShouldNotBeFound("junFeeDisc.lessThanOrEqual=" + SMALLER_JUN_FEE_DISC);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByJunFeeDiscIsLessThanSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where junFeeDisc is less than
		// DEFAULT_JUN_FEE_DISC
		defaultStudentDiscountShouldNotBeFound("junFeeDisc.lessThan=" + DEFAULT_JUN_FEE_DISC);

		// Get all the studentDiscountList where junFeeDisc is less than
		// UPDATED_JUN_FEE_DISC
		defaultStudentDiscountShouldBeFound("junFeeDisc.lessThan=" + UPDATED_JUN_FEE_DISC);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByJunFeeDiscIsGreaterThanSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where junFeeDisc is greater than
		// DEFAULT_JUN_FEE_DISC
		defaultStudentDiscountShouldNotBeFound("junFeeDisc.greaterThan=" + DEFAULT_JUN_FEE_DISC);

		// Get all the studentDiscountList where junFeeDisc is greater than
		// SMALLER_JUN_FEE_DISC
		defaultStudentDiscountShouldBeFound("junFeeDisc.greaterThan=" + SMALLER_JUN_FEE_DISC);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByJulFeeDiscIsEqualToSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where julFeeDisc equals to
		// DEFAULT_JUL_FEE_DISC
		defaultStudentDiscountShouldBeFound("julFeeDisc.equals=" + DEFAULT_JUL_FEE_DISC);

		// Get all the studentDiscountList where julFeeDisc equals to
		// UPDATED_JUL_FEE_DISC
		defaultStudentDiscountShouldNotBeFound("julFeeDisc.equals=" + UPDATED_JUL_FEE_DISC);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByJulFeeDiscIsNotEqualToSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where julFeeDisc not equals to
		// DEFAULT_JUL_FEE_DISC
		defaultStudentDiscountShouldNotBeFound("julFeeDisc.notEquals=" + DEFAULT_JUL_FEE_DISC);

		// Get all the studentDiscountList where julFeeDisc not equals to
		// UPDATED_JUL_FEE_DISC
		defaultStudentDiscountShouldBeFound("julFeeDisc.notEquals=" + UPDATED_JUL_FEE_DISC);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByJulFeeDiscIsInShouldWork() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where julFeeDisc in DEFAULT_JUL_FEE_DISC or
		// UPDATED_JUL_FEE_DISC
		defaultStudentDiscountShouldBeFound("julFeeDisc.in=" + DEFAULT_JUL_FEE_DISC + "," + UPDATED_JUL_FEE_DISC);

		// Get all the studentDiscountList where julFeeDisc equals to
		// UPDATED_JUL_FEE_DISC
		defaultStudentDiscountShouldNotBeFound("julFeeDisc.in=" + UPDATED_JUL_FEE_DISC);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByJulFeeDiscIsNullOrNotNull() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where julFeeDisc is not null
		defaultStudentDiscountShouldBeFound("julFeeDisc.specified=true");

		// Get all the studentDiscountList where julFeeDisc is null
		defaultStudentDiscountShouldNotBeFound("julFeeDisc.specified=false");
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByJulFeeDiscIsGreaterThanOrEqualToSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where julFeeDisc is greater than or equal to
		// DEFAULT_JUL_FEE_DISC
		defaultStudentDiscountShouldBeFound("julFeeDisc.greaterThanOrEqual=" + DEFAULT_JUL_FEE_DISC);

		// Get all the studentDiscountList where julFeeDisc is greater than or equal to
		// UPDATED_JUL_FEE_DISC
		defaultStudentDiscountShouldNotBeFound("julFeeDisc.greaterThanOrEqual=" + UPDATED_JUL_FEE_DISC);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByJulFeeDiscIsLessThanOrEqualToSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where julFeeDisc is less than or equal to
		// DEFAULT_JUL_FEE_DISC
		defaultStudentDiscountShouldBeFound("julFeeDisc.lessThanOrEqual=" + DEFAULT_JUL_FEE_DISC);

		// Get all the studentDiscountList where julFeeDisc is less than or equal to
		// SMALLER_JUL_FEE_DISC
		defaultStudentDiscountShouldNotBeFound("julFeeDisc.lessThanOrEqual=" + SMALLER_JUL_FEE_DISC);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByJulFeeDiscIsLessThanSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where julFeeDisc is less than
		// DEFAULT_JUL_FEE_DISC
		defaultStudentDiscountShouldNotBeFound("julFeeDisc.lessThan=" + DEFAULT_JUL_FEE_DISC);

		// Get all the studentDiscountList where julFeeDisc is less than
		// UPDATED_JUL_FEE_DISC
		defaultStudentDiscountShouldBeFound("julFeeDisc.lessThan=" + UPDATED_JUL_FEE_DISC);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByJulFeeDiscIsGreaterThanSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where julFeeDisc is greater than
		// DEFAULT_JUL_FEE_DISC
		defaultStudentDiscountShouldNotBeFound("julFeeDisc.greaterThan=" + DEFAULT_JUL_FEE_DISC);

		// Get all the studentDiscountList where julFeeDisc is greater than
		// SMALLER_JUL_FEE_DISC
		defaultStudentDiscountShouldBeFound("julFeeDisc.greaterThan=" + SMALLER_JUL_FEE_DISC);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByAugFeeDiscIsEqualToSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where augFeeDisc equals to
		// DEFAULT_AUG_FEE_DISC
		defaultStudentDiscountShouldBeFound("augFeeDisc.equals=" + DEFAULT_AUG_FEE_DISC);

		// Get all the studentDiscountList where augFeeDisc equals to
		// UPDATED_AUG_FEE_DISC
		defaultStudentDiscountShouldNotBeFound("augFeeDisc.equals=" + UPDATED_AUG_FEE_DISC);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByAugFeeDiscIsNotEqualToSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where augFeeDisc not equals to
		// DEFAULT_AUG_FEE_DISC
		defaultStudentDiscountShouldNotBeFound("augFeeDisc.notEquals=" + DEFAULT_AUG_FEE_DISC);

		// Get all the studentDiscountList where augFeeDisc not equals to
		// UPDATED_AUG_FEE_DISC
		defaultStudentDiscountShouldBeFound("augFeeDisc.notEquals=" + UPDATED_AUG_FEE_DISC);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByAugFeeDiscIsInShouldWork() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where augFeeDisc in DEFAULT_AUG_FEE_DISC or
		// UPDATED_AUG_FEE_DISC
		defaultStudentDiscountShouldBeFound("augFeeDisc.in=" + DEFAULT_AUG_FEE_DISC + "," + UPDATED_AUG_FEE_DISC);

		// Get all the studentDiscountList where augFeeDisc equals to
		// UPDATED_AUG_FEE_DISC
		defaultStudentDiscountShouldNotBeFound("augFeeDisc.in=" + UPDATED_AUG_FEE_DISC);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByAugFeeDiscIsNullOrNotNull() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where augFeeDisc is not null
		defaultStudentDiscountShouldBeFound("augFeeDisc.specified=true");

		// Get all the studentDiscountList where augFeeDisc is null
		defaultStudentDiscountShouldNotBeFound("augFeeDisc.specified=false");
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByAugFeeDiscIsGreaterThanOrEqualToSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where augFeeDisc is greater than or equal to
		// DEFAULT_AUG_FEE_DISC
		defaultStudentDiscountShouldBeFound("augFeeDisc.greaterThanOrEqual=" + DEFAULT_AUG_FEE_DISC);

		// Get all the studentDiscountList where augFeeDisc is greater than or equal to
		// UPDATED_AUG_FEE_DISC
		defaultStudentDiscountShouldNotBeFound("augFeeDisc.greaterThanOrEqual=" + UPDATED_AUG_FEE_DISC);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByAugFeeDiscIsLessThanOrEqualToSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where augFeeDisc is less than or equal to
		// DEFAULT_AUG_FEE_DISC
		defaultStudentDiscountShouldBeFound("augFeeDisc.lessThanOrEqual=" + DEFAULT_AUG_FEE_DISC);

		// Get all the studentDiscountList where augFeeDisc is less than or equal to
		// SMALLER_AUG_FEE_DISC
		defaultStudentDiscountShouldNotBeFound("augFeeDisc.lessThanOrEqual=" + SMALLER_AUG_FEE_DISC);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByAugFeeDiscIsLessThanSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where augFeeDisc is less than
		// DEFAULT_AUG_FEE_DISC
		defaultStudentDiscountShouldNotBeFound("augFeeDisc.lessThan=" + DEFAULT_AUG_FEE_DISC);

		// Get all the studentDiscountList where augFeeDisc is less than
		// UPDATED_AUG_FEE_DISC
		defaultStudentDiscountShouldBeFound("augFeeDisc.lessThan=" + UPDATED_AUG_FEE_DISC);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByAugFeeDiscIsGreaterThanSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where augFeeDisc is greater than
		// DEFAULT_AUG_FEE_DISC
		defaultStudentDiscountShouldNotBeFound("augFeeDisc.greaterThan=" + DEFAULT_AUG_FEE_DISC);

		// Get all the studentDiscountList where augFeeDisc is greater than
		// SMALLER_AUG_FEE_DISC
		defaultStudentDiscountShouldBeFound("augFeeDisc.greaterThan=" + SMALLER_AUG_FEE_DISC);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsBySepFeeDiscIsEqualToSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where sepFeeDisc equals to
		// DEFAULT_SEP_FEE_DISC
		defaultStudentDiscountShouldBeFound("sepFeeDisc.equals=" + DEFAULT_SEP_FEE_DISC);

		// Get all the studentDiscountList where sepFeeDisc equals to
		// UPDATED_SEP_FEE_DISC
		defaultStudentDiscountShouldNotBeFound("sepFeeDisc.equals=" + UPDATED_SEP_FEE_DISC);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsBySepFeeDiscIsNotEqualToSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where sepFeeDisc not equals to
		// DEFAULT_SEP_FEE_DISC
		defaultStudentDiscountShouldNotBeFound("sepFeeDisc.notEquals=" + DEFAULT_SEP_FEE_DISC);

		// Get all the studentDiscountList where sepFeeDisc not equals to
		// UPDATED_SEP_FEE_DISC
		defaultStudentDiscountShouldBeFound("sepFeeDisc.notEquals=" + UPDATED_SEP_FEE_DISC);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsBySepFeeDiscIsInShouldWork() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where sepFeeDisc in DEFAULT_SEP_FEE_DISC or
		// UPDATED_SEP_FEE_DISC
		defaultStudentDiscountShouldBeFound("sepFeeDisc.in=" + DEFAULT_SEP_FEE_DISC + "," + UPDATED_SEP_FEE_DISC);

		// Get all the studentDiscountList where sepFeeDisc equals to
		// UPDATED_SEP_FEE_DISC
		defaultStudentDiscountShouldNotBeFound("sepFeeDisc.in=" + UPDATED_SEP_FEE_DISC);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsBySepFeeDiscIsNullOrNotNull() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where sepFeeDisc is not null
		defaultStudentDiscountShouldBeFound("sepFeeDisc.specified=true");

		// Get all the studentDiscountList where sepFeeDisc is null
		defaultStudentDiscountShouldNotBeFound("sepFeeDisc.specified=false");
	}

	@Test
	@Transactional
	void getAllStudentDiscountsBySepFeeDiscIsGreaterThanOrEqualToSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where sepFeeDisc is greater than or equal to
		// DEFAULT_SEP_FEE_DISC
		defaultStudentDiscountShouldBeFound("sepFeeDisc.greaterThanOrEqual=" + DEFAULT_SEP_FEE_DISC);

		// Get all the studentDiscountList where sepFeeDisc is greater than or equal to
		// UPDATED_SEP_FEE_DISC
		defaultStudentDiscountShouldNotBeFound("sepFeeDisc.greaterThanOrEqual=" + UPDATED_SEP_FEE_DISC);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsBySepFeeDiscIsLessThanOrEqualToSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where sepFeeDisc is less than or equal to
		// DEFAULT_SEP_FEE_DISC
		defaultStudentDiscountShouldBeFound("sepFeeDisc.lessThanOrEqual=" + DEFAULT_SEP_FEE_DISC);

		// Get all the studentDiscountList where sepFeeDisc is less than or equal to
		// SMALLER_SEP_FEE_DISC
		defaultStudentDiscountShouldNotBeFound("sepFeeDisc.lessThanOrEqual=" + SMALLER_SEP_FEE_DISC);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsBySepFeeDiscIsLessThanSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where sepFeeDisc is less than
		// DEFAULT_SEP_FEE_DISC
		defaultStudentDiscountShouldNotBeFound("sepFeeDisc.lessThan=" + DEFAULT_SEP_FEE_DISC);

		// Get all the studentDiscountList where sepFeeDisc is less than
		// UPDATED_SEP_FEE_DISC
		defaultStudentDiscountShouldBeFound("sepFeeDisc.lessThan=" + UPDATED_SEP_FEE_DISC);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsBySepFeeDiscIsGreaterThanSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where sepFeeDisc is greater than
		// DEFAULT_SEP_FEE_DISC
		defaultStudentDiscountShouldNotBeFound("sepFeeDisc.greaterThan=" + DEFAULT_SEP_FEE_DISC);

		// Get all the studentDiscountList where sepFeeDisc is greater than
		// SMALLER_SEP_FEE_DISC
		defaultStudentDiscountShouldBeFound("sepFeeDisc.greaterThan=" + SMALLER_SEP_FEE_DISC);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByOctFeeDiscIsEqualToSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where octFeeDisc equals to
		// DEFAULT_OCT_FEE_DISC
		defaultStudentDiscountShouldBeFound("octFeeDisc.equals=" + DEFAULT_OCT_FEE_DISC);

		// Get all the studentDiscountList where octFeeDisc equals to
		// UPDATED_OCT_FEE_DISC
		defaultStudentDiscountShouldNotBeFound("octFeeDisc.equals=" + UPDATED_OCT_FEE_DISC);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByOctFeeDiscIsNotEqualToSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where octFeeDisc not equals to
		// DEFAULT_OCT_FEE_DISC
		defaultStudentDiscountShouldNotBeFound("octFeeDisc.notEquals=" + DEFAULT_OCT_FEE_DISC);

		// Get all the studentDiscountList where octFeeDisc not equals to
		// UPDATED_OCT_FEE_DISC
		defaultStudentDiscountShouldBeFound("octFeeDisc.notEquals=" + UPDATED_OCT_FEE_DISC);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByOctFeeDiscIsInShouldWork() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where octFeeDisc in DEFAULT_OCT_FEE_DISC or
		// UPDATED_OCT_FEE_DISC
		defaultStudentDiscountShouldBeFound("octFeeDisc.in=" + DEFAULT_OCT_FEE_DISC + "," + UPDATED_OCT_FEE_DISC);

		// Get all the studentDiscountList where octFeeDisc equals to
		// UPDATED_OCT_FEE_DISC
		defaultStudentDiscountShouldNotBeFound("octFeeDisc.in=" + UPDATED_OCT_FEE_DISC);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByOctFeeDiscIsNullOrNotNull() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where octFeeDisc is not null
		defaultStudentDiscountShouldBeFound("octFeeDisc.specified=true");

		// Get all the studentDiscountList where octFeeDisc is null
		defaultStudentDiscountShouldNotBeFound("octFeeDisc.specified=false");
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByOctFeeDiscIsGreaterThanOrEqualToSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where octFeeDisc is greater than or equal to
		// DEFAULT_OCT_FEE_DISC
		defaultStudentDiscountShouldBeFound("octFeeDisc.greaterThanOrEqual=" + DEFAULT_OCT_FEE_DISC);

		// Get all the studentDiscountList where octFeeDisc is greater than or equal to
		// UPDATED_OCT_FEE_DISC
		defaultStudentDiscountShouldNotBeFound("octFeeDisc.greaterThanOrEqual=" + UPDATED_OCT_FEE_DISC);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByOctFeeDiscIsLessThanOrEqualToSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where octFeeDisc is less than or equal to
		// DEFAULT_OCT_FEE_DISC
		defaultStudentDiscountShouldBeFound("octFeeDisc.lessThanOrEqual=" + DEFAULT_OCT_FEE_DISC);

		// Get all the studentDiscountList where octFeeDisc is less than or equal to
		// SMALLER_OCT_FEE_DISC
		defaultStudentDiscountShouldNotBeFound("octFeeDisc.lessThanOrEqual=" + SMALLER_OCT_FEE_DISC);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByOctFeeDiscIsLessThanSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where octFeeDisc is less than
		// DEFAULT_OCT_FEE_DISC
		defaultStudentDiscountShouldNotBeFound("octFeeDisc.lessThan=" + DEFAULT_OCT_FEE_DISC);

		// Get all the studentDiscountList where octFeeDisc is less than
		// UPDATED_OCT_FEE_DISC
		defaultStudentDiscountShouldBeFound("octFeeDisc.lessThan=" + UPDATED_OCT_FEE_DISC);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByOctFeeDiscIsGreaterThanSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where octFeeDisc is greater than
		// DEFAULT_OCT_FEE_DISC
		defaultStudentDiscountShouldNotBeFound("octFeeDisc.greaterThan=" + DEFAULT_OCT_FEE_DISC);

		// Get all the studentDiscountList where octFeeDisc is greater than
		// SMALLER_OCT_FEE_DISC
		defaultStudentDiscountShouldBeFound("octFeeDisc.greaterThan=" + SMALLER_OCT_FEE_DISC);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByNovFeeDiscIsEqualToSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where novFeeDisc equals to
		// DEFAULT_NOV_FEE_DISC
		defaultStudentDiscountShouldBeFound("novFeeDisc.equals=" + DEFAULT_NOV_FEE_DISC);

		// Get all the studentDiscountList where novFeeDisc equals to
		// UPDATED_NOV_FEE_DISC
		defaultStudentDiscountShouldNotBeFound("novFeeDisc.equals=" + UPDATED_NOV_FEE_DISC);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByNovFeeDiscIsNotEqualToSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where novFeeDisc not equals to
		// DEFAULT_NOV_FEE_DISC
		defaultStudentDiscountShouldNotBeFound("novFeeDisc.notEquals=" + DEFAULT_NOV_FEE_DISC);

		// Get all the studentDiscountList where novFeeDisc not equals to
		// UPDATED_NOV_FEE_DISC
		defaultStudentDiscountShouldBeFound("novFeeDisc.notEquals=" + UPDATED_NOV_FEE_DISC);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByNovFeeDiscIsInShouldWork() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where novFeeDisc in DEFAULT_NOV_FEE_DISC or
		// UPDATED_NOV_FEE_DISC
		defaultStudentDiscountShouldBeFound("novFeeDisc.in=" + DEFAULT_NOV_FEE_DISC + "," + UPDATED_NOV_FEE_DISC);

		// Get all the studentDiscountList where novFeeDisc equals to
		// UPDATED_NOV_FEE_DISC
		defaultStudentDiscountShouldNotBeFound("novFeeDisc.in=" + UPDATED_NOV_FEE_DISC);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByNovFeeDiscIsNullOrNotNull() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where novFeeDisc is not null
		defaultStudentDiscountShouldBeFound("novFeeDisc.specified=true");

		// Get all the studentDiscountList where novFeeDisc is null
		defaultStudentDiscountShouldNotBeFound("novFeeDisc.specified=false");
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByNovFeeDiscIsGreaterThanOrEqualToSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where novFeeDisc is greater than or equal to
		// DEFAULT_NOV_FEE_DISC
		defaultStudentDiscountShouldBeFound("novFeeDisc.greaterThanOrEqual=" + DEFAULT_NOV_FEE_DISC);

		// Get all the studentDiscountList where novFeeDisc is greater than or equal to
		// UPDATED_NOV_FEE_DISC
		defaultStudentDiscountShouldNotBeFound("novFeeDisc.greaterThanOrEqual=" + UPDATED_NOV_FEE_DISC);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByNovFeeDiscIsLessThanOrEqualToSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where novFeeDisc is less than or equal to
		// DEFAULT_NOV_FEE_DISC
		defaultStudentDiscountShouldBeFound("novFeeDisc.lessThanOrEqual=" + DEFAULT_NOV_FEE_DISC);

		// Get all the studentDiscountList where novFeeDisc is less than or equal to
		// SMALLER_NOV_FEE_DISC
		defaultStudentDiscountShouldNotBeFound("novFeeDisc.lessThanOrEqual=" + SMALLER_NOV_FEE_DISC);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByNovFeeDiscIsLessThanSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where novFeeDisc is less than
		// DEFAULT_NOV_FEE_DISC
		defaultStudentDiscountShouldNotBeFound("novFeeDisc.lessThan=" + DEFAULT_NOV_FEE_DISC);

		// Get all the studentDiscountList where novFeeDisc is less than
		// UPDATED_NOV_FEE_DISC
		defaultStudentDiscountShouldBeFound("novFeeDisc.lessThan=" + UPDATED_NOV_FEE_DISC);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByNovFeeDiscIsGreaterThanSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where novFeeDisc is greater than
		// DEFAULT_NOV_FEE_DISC
		defaultStudentDiscountShouldNotBeFound("novFeeDisc.greaterThan=" + DEFAULT_NOV_FEE_DISC);

		// Get all the studentDiscountList where novFeeDisc is greater than
		// SMALLER_NOV_FEE_DISC
		defaultStudentDiscountShouldBeFound("novFeeDisc.greaterThan=" + SMALLER_NOV_FEE_DISC);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByDecFeeDiscIsEqualToSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where decFeeDisc equals to
		// DEFAULT_DEC_FEE_DISC
		defaultStudentDiscountShouldBeFound("decFeeDisc.equals=" + DEFAULT_DEC_FEE_DISC);

		// Get all the studentDiscountList where decFeeDisc equals to
		// UPDATED_DEC_FEE_DISC
		defaultStudentDiscountShouldNotBeFound("decFeeDisc.equals=" + UPDATED_DEC_FEE_DISC);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByDecFeeDiscIsNotEqualToSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where decFeeDisc not equals to
		// DEFAULT_DEC_FEE_DISC
		defaultStudentDiscountShouldNotBeFound("decFeeDisc.notEquals=" + DEFAULT_DEC_FEE_DISC);

		// Get all the studentDiscountList where decFeeDisc not equals to
		// UPDATED_DEC_FEE_DISC
		defaultStudentDiscountShouldBeFound("decFeeDisc.notEquals=" + UPDATED_DEC_FEE_DISC);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByDecFeeDiscIsInShouldWork() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where decFeeDisc in DEFAULT_DEC_FEE_DISC or
		// UPDATED_DEC_FEE_DISC
		defaultStudentDiscountShouldBeFound("decFeeDisc.in=" + DEFAULT_DEC_FEE_DISC + "," + UPDATED_DEC_FEE_DISC);

		// Get all the studentDiscountList where decFeeDisc equals to
		// UPDATED_DEC_FEE_DISC
		defaultStudentDiscountShouldNotBeFound("decFeeDisc.in=" + UPDATED_DEC_FEE_DISC);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByDecFeeDiscIsNullOrNotNull() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where decFeeDisc is not null
		defaultStudentDiscountShouldBeFound("decFeeDisc.specified=true");

		// Get all the studentDiscountList where decFeeDisc is null
		defaultStudentDiscountShouldNotBeFound("decFeeDisc.specified=false");
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByDecFeeDiscIsGreaterThanOrEqualToSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where decFeeDisc is greater than or equal to
		// DEFAULT_DEC_FEE_DISC
		defaultStudentDiscountShouldBeFound("decFeeDisc.greaterThanOrEqual=" + DEFAULT_DEC_FEE_DISC);

		// Get all the studentDiscountList where decFeeDisc is greater than or equal to
		// UPDATED_DEC_FEE_DISC
		defaultStudentDiscountShouldNotBeFound("decFeeDisc.greaterThanOrEqual=" + UPDATED_DEC_FEE_DISC);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByDecFeeDiscIsLessThanOrEqualToSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where decFeeDisc is less than or equal to
		// DEFAULT_DEC_FEE_DISC
		defaultStudentDiscountShouldBeFound("decFeeDisc.lessThanOrEqual=" + DEFAULT_DEC_FEE_DISC);

		// Get all the studentDiscountList where decFeeDisc is less than or equal to
		// SMALLER_DEC_FEE_DISC
		defaultStudentDiscountShouldNotBeFound("decFeeDisc.lessThanOrEqual=" + SMALLER_DEC_FEE_DISC);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByDecFeeDiscIsLessThanSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where decFeeDisc is less than
		// DEFAULT_DEC_FEE_DISC
		defaultStudentDiscountShouldNotBeFound("decFeeDisc.lessThan=" + DEFAULT_DEC_FEE_DISC);

		// Get all the studentDiscountList where decFeeDisc is less than
		// UPDATED_DEC_FEE_DISC
		defaultStudentDiscountShouldBeFound("decFeeDisc.lessThan=" + UPDATED_DEC_FEE_DISC);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByDecFeeDiscIsGreaterThanSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where decFeeDisc is greater than
		// DEFAULT_DEC_FEE_DISC
		defaultStudentDiscountShouldNotBeFound("decFeeDisc.greaterThan=" + DEFAULT_DEC_FEE_DISC);

		// Get all the studentDiscountList where decFeeDisc is greater than
		// SMALLER_DEC_FEE_DISC
		defaultStudentDiscountShouldBeFound("decFeeDisc.greaterThan=" + SMALLER_DEC_FEE_DISC);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByCreateDateIsEqualToSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where createDate equals to
		// DEFAULT_CREATE_DATE
		defaultStudentDiscountShouldBeFound("createDate.equals=" + DEFAULT_CREATE_DATE);

		// Get all the studentDiscountList where createDate equals to
		// UPDATED_CREATE_DATE
		defaultStudentDiscountShouldNotBeFound("createDate.equals=" + UPDATED_CREATE_DATE);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByCreateDateIsNotEqualToSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where createDate not equals to
		// DEFAULT_CREATE_DATE
		defaultStudentDiscountShouldNotBeFound("createDate.notEquals=" + DEFAULT_CREATE_DATE);

		// Get all the studentDiscountList where createDate not equals to
		// UPDATED_CREATE_DATE
		defaultStudentDiscountShouldBeFound("createDate.notEquals=" + UPDATED_CREATE_DATE);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByCreateDateIsInShouldWork() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where createDate in DEFAULT_CREATE_DATE or
		// UPDATED_CREATE_DATE
		defaultStudentDiscountShouldBeFound("createDate.in=" + DEFAULT_CREATE_DATE + "," + UPDATED_CREATE_DATE);

		// Get all the studentDiscountList where createDate equals to
		// UPDATED_CREATE_DATE
		defaultStudentDiscountShouldNotBeFound("createDate.in=" + UPDATED_CREATE_DATE);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByCreateDateIsNullOrNotNull() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where createDate is not null
		defaultStudentDiscountShouldBeFound("createDate.specified=true");

		// Get all the studentDiscountList where createDate is null
		defaultStudentDiscountShouldNotBeFound("createDate.specified=false");
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByCreateDateIsGreaterThanOrEqualToSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where createDate is greater than or equal to
		// DEFAULT_CREATE_DATE
		defaultStudentDiscountShouldBeFound("createDate.greaterThanOrEqual=" + DEFAULT_CREATE_DATE);

		// Get all the studentDiscountList where createDate is greater than or equal to
		// UPDATED_CREATE_DATE
		defaultStudentDiscountShouldNotBeFound("createDate.greaterThanOrEqual=" + UPDATED_CREATE_DATE);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByCreateDateIsLessThanOrEqualToSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where createDate is less than or equal to
		// DEFAULT_CREATE_DATE
		defaultStudentDiscountShouldBeFound("createDate.lessThanOrEqual=" + DEFAULT_CREATE_DATE);

		// Get all the studentDiscountList where createDate is less than or equal to
		// SMALLER_CREATE_DATE
		defaultStudentDiscountShouldNotBeFound("createDate.lessThanOrEqual=" + SMALLER_CREATE_DATE);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByCreateDateIsLessThanSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where createDate is less than
		// DEFAULT_CREATE_DATE
		defaultStudentDiscountShouldNotBeFound("createDate.lessThan=" + DEFAULT_CREATE_DATE);

		// Get all the studentDiscountList where createDate is less than
		// UPDATED_CREATE_DATE
		defaultStudentDiscountShouldBeFound("createDate.lessThan=" + UPDATED_CREATE_DATE);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByCreateDateIsGreaterThanSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where createDate is greater than
		// DEFAULT_CREATE_DATE
		defaultStudentDiscountShouldNotBeFound("createDate.greaterThan=" + DEFAULT_CREATE_DATE);

		// Get all the studentDiscountList where createDate is greater than
		// SMALLER_CREATE_DATE
		defaultStudentDiscountShouldBeFound("createDate.greaterThan=" + SMALLER_CREATE_DATE);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByLastModifiedIsEqualToSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where lastModified equals to
		// DEFAULT_LAST_MODIFIED
		defaultStudentDiscountShouldBeFound("lastModified.equals=" + DEFAULT_LAST_MODIFIED);

		// Get all the studentDiscountList where lastModified equals to
		// UPDATED_LAST_MODIFIED
		defaultStudentDiscountShouldNotBeFound("lastModified.equals=" + UPDATED_LAST_MODIFIED);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByLastModifiedIsNotEqualToSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where lastModified not equals to
		// DEFAULT_LAST_MODIFIED
		defaultStudentDiscountShouldNotBeFound("lastModified.notEquals=" + DEFAULT_LAST_MODIFIED);

		// Get all the studentDiscountList where lastModified not equals to
		// UPDATED_LAST_MODIFIED
		defaultStudentDiscountShouldBeFound("lastModified.notEquals=" + UPDATED_LAST_MODIFIED);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByLastModifiedIsInShouldWork() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where lastModified in DEFAULT_LAST_MODIFIED
		// or UPDATED_LAST_MODIFIED
		defaultStudentDiscountShouldBeFound("lastModified.in=" + DEFAULT_LAST_MODIFIED + "," + UPDATED_LAST_MODIFIED);

		// Get all the studentDiscountList where lastModified equals to
		// UPDATED_LAST_MODIFIED
		defaultStudentDiscountShouldNotBeFound("lastModified.in=" + UPDATED_LAST_MODIFIED);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByLastModifiedIsNullOrNotNull() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where lastModified is not null
		defaultStudentDiscountShouldBeFound("lastModified.specified=true");

		// Get all the studentDiscountList where lastModified is null
		defaultStudentDiscountShouldNotBeFound("lastModified.specified=false");
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByLastModifiedIsGreaterThanOrEqualToSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where lastModified is greater than or equal
		// to DEFAULT_LAST_MODIFIED
		defaultStudentDiscountShouldBeFound("lastModified.greaterThanOrEqual=" + DEFAULT_LAST_MODIFIED);

		// Get all the studentDiscountList where lastModified is greater than or equal
		// to UPDATED_LAST_MODIFIED
		defaultStudentDiscountShouldNotBeFound("lastModified.greaterThanOrEqual=" + UPDATED_LAST_MODIFIED);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByLastModifiedIsLessThanOrEqualToSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where lastModified is less than or equal to
		// DEFAULT_LAST_MODIFIED
		defaultStudentDiscountShouldBeFound("lastModified.lessThanOrEqual=" + DEFAULT_LAST_MODIFIED);

		// Get all the studentDiscountList where lastModified is less than or equal to
		// SMALLER_LAST_MODIFIED
		defaultStudentDiscountShouldNotBeFound("lastModified.lessThanOrEqual=" + SMALLER_LAST_MODIFIED);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByLastModifiedIsLessThanSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where lastModified is less than
		// DEFAULT_LAST_MODIFIED
		defaultStudentDiscountShouldNotBeFound("lastModified.lessThan=" + DEFAULT_LAST_MODIFIED);

		// Get all the studentDiscountList where lastModified is less than
		// UPDATED_LAST_MODIFIED
		defaultStudentDiscountShouldBeFound("lastModified.lessThan=" + UPDATED_LAST_MODIFIED);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByLastModifiedIsGreaterThanSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where lastModified is greater than
		// DEFAULT_LAST_MODIFIED
		defaultStudentDiscountShouldNotBeFound("lastModified.greaterThan=" + DEFAULT_LAST_MODIFIED);

		// Get all the studentDiscountList where lastModified is greater than
		// SMALLER_LAST_MODIFIED
		defaultStudentDiscountShouldBeFound("lastModified.greaterThan=" + SMALLER_LAST_MODIFIED);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByCancelDateIsEqualToSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where cancelDate equals to
		// DEFAULT_CANCEL_DATE
		defaultStudentDiscountShouldBeFound("cancelDate.equals=" + DEFAULT_CANCEL_DATE);

		// Get all the studentDiscountList where cancelDate equals to
		// UPDATED_CANCEL_DATE
		defaultStudentDiscountShouldNotBeFound("cancelDate.equals=" + UPDATED_CANCEL_DATE);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByCancelDateIsNotEqualToSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where cancelDate not equals to
		// DEFAULT_CANCEL_DATE
		defaultStudentDiscountShouldNotBeFound("cancelDate.notEquals=" + DEFAULT_CANCEL_DATE);

		// Get all the studentDiscountList where cancelDate not equals to
		// UPDATED_CANCEL_DATE
		defaultStudentDiscountShouldBeFound("cancelDate.notEquals=" + UPDATED_CANCEL_DATE);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByCancelDateIsInShouldWork() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where cancelDate in DEFAULT_CANCEL_DATE or
		// UPDATED_CANCEL_DATE
		defaultStudentDiscountShouldBeFound("cancelDate.in=" + DEFAULT_CANCEL_DATE + "," + UPDATED_CANCEL_DATE);

		// Get all the studentDiscountList where cancelDate equals to
		// UPDATED_CANCEL_DATE
		defaultStudentDiscountShouldNotBeFound("cancelDate.in=" + UPDATED_CANCEL_DATE);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByCancelDateIsNullOrNotNull() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where cancelDate is not null
		defaultStudentDiscountShouldBeFound("cancelDate.specified=true");

		// Get all the studentDiscountList where cancelDate is null
		defaultStudentDiscountShouldNotBeFound("cancelDate.specified=false");
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByCancelDateIsGreaterThanOrEqualToSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where cancelDate is greater than or equal to
		// DEFAULT_CANCEL_DATE
		defaultStudentDiscountShouldBeFound("cancelDate.greaterThanOrEqual=" + DEFAULT_CANCEL_DATE);

		// Get all the studentDiscountList where cancelDate is greater than or equal to
		// UPDATED_CANCEL_DATE
		defaultStudentDiscountShouldNotBeFound("cancelDate.greaterThanOrEqual=" + UPDATED_CANCEL_DATE);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByCancelDateIsLessThanOrEqualToSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where cancelDate is less than or equal to
		// DEFAULT_CANCEL_DATE
		defaultStudentDiscountShouldBeFound("cancelDate.lessThanOrEqual=" + DEFAULT_CANCEL_DATE);

		// Get all the studentDiscountList where cancelDate is less than or equal to
		// SMALLER_CANCEL_DATE
		defaultStudentDiscountShouldNotBeFound("cancelDate.lessThanOrEqual=" + SMALLER_CANCEL_DATE);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByCancelDateIsLessThanSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where cancelDate is less than
		// DEFAULT_CANCEL_DATE
		defaultStudentDiscountShouldNotBeFound("cancelDate.lessThan=" + DEFAULT_CANCEL_DATE);

		// Get all the studentDiscountList where cancelDate is less than
		// UPDATED_CANCEL_DATE
		defaultStudentDiscountShouldBeFound("cancelDate.lessThan=" + UPDATED_CANCEL_DATE);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByCancelDateIsGreaterThanSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		// Get all the studentDiscountList where cancelDate is greater than
		// DEFAULT_CANCEL_DATE
		defaultStudentDiscountShouldNotBeFound("cancelDate.greaterThan=" + DEFAULT_CANCEL_DATE);

		// Get all the studentDiscountList where cancelDate is greater than
		// SMALLER_CANCEL_DATE
		defaultStudentDiscountShouldBeFound("cancelDate.greaterThan=" + SMALLER_CANCEL_DATE);
	}

	@Test
	@Transactional
	void getAllStudentDiscountsBySchoolLedgerHeadIsEqualToSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);
		SchoolLedgerHead schoolLedgerHead = SchoolLedgerHeadResourceIT.createEntity(em);
		em.persist(schoolLedgerHead);
		em.flush();
		studentDiscount.setSchoolLedgerHead(schoolLedgerHead);
		studentDiscountRepository.saveAndFlush(studentDiscount);
		Long schoolLedgerHeadId = schoolLedgerHead.getId();

		// Get all the studentDiscountList where schoolLedgerHead equals to
		// schoolLedgerHeadId
		defaultStudentDiscountShouldBeFound("schoolLedgerHeadId.equals=" + schoolLedgerHeadId);

		// Get all the studentDiscountList where schoolLedgerHead equals to
		// (schoolLedgerHeadId + 1)
		defaultStudentDiscountShouldNotBeFound("schoolLedgerHeadId.equals=" + (schoolLedgerHeadId + 1));
	}

	@Test
	@Transactional
	void getAllStudentDiscountsByClassStudentIsEqualToSomething() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);
		ClassStudent classStudent = ClassStudentResourceIT.createEntity(em);
		em.persist(classStudent);
		em.flush();
		studentDiscount.setClassStudent(classStudent);
		studentDiscountRepository.saveAndFlush(studentDiscount);
		Long classStudentId = classStudent.getId();

		// Get all the studentDiscountList where classStudent equals to classStudentId
		defaultStudentDiscountShouldBeFound("classStudentId.equals=" + classStudentId);

		// Get all the studentDiscountList where classStudent equals to (classStudentId
		// + 1)
		defaultStudentDiscountShouldNotBeFound("classStudentId.equals=" + (classStudentId + 1));
	}

	/**
	 * Executes the search, and checks that the default entity is returned.
	 */
	private void defaultStudentDiscountShouldBeFound(String filter) throws Exception {
		restStudentDiscountMockMvc.perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter)).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.[*].id").value(hasItem(studentDiscount.getId().intValue())))
				.andExpect(jsonPath("$.[*].feeYear").value(hasItem(DEFAULT_FEE_YEAR.toString())))
				.andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE)))
				.andExpect(jsonPath("$.[*].janFeeDisc").value(hasItem(DEFAULT_JAN_FEE_DISC.doubleValue())))
				.andExpect(jsonPath("$.[*].febFeeDisc").value(hasItem(DEFAULT_FEB_FEE_DISC.doubleValue())))
				.andExpect(jsonPath("$.[*].marFeeDisc").value(hasItem(DEFAULT_MAR_FEE_DISC.doubleValue())))
				.andExpect(jsonPath("$.[*].aprFeeDisc").value(hasItem(DEFAULT_APR_FEE_DISC.doubleValue())))
				.andExpect(jsonPath("$.[*].mayFeeDisc").value(hasItem(DEFAULT_MAY_FEE_DISC.doubleValue())))
				.andExpect(jsonPath("$.[*].junFeeDisc").value(hasItem(DEFAULT_JUN_FEE_DISC.doubleValue())))
				.andExpect(jsonPath("$.[*].julFeeDisc").value(hasItem(DEFAULT_JUL_FEE_DISC.doubleValue())))
				.andExpect(jsonPath("$.[*].augFeeDisc").value(hasItem(DEFAULT_AUG_FEE_DISC.doubleValue())))
				.andExpect(jsonPath("$.[*].sepFeeDisc").value(hasItem(DEFAULT_SEP_FEE_DISC.doubleValue())))
				.andExpect(jsonPath("$.[*].octFeeDisc").value(hasItem(DEFAULT_OCT_FEE_DISC.doubleValue())))
				.andExpect(jsonPath("$.[*].novFeeDisc").value(hasItem(DEFAULT_NOV_FEE_DISC.doubleValue())))
				.andExpect(jsonPath("$.[*].decFeeDisc").value(hasItem(DEFAULT_DEC_FEE_DISC.doubleValue())))
				.andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
				.andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
				.andExpect(jsonPath("$.[*].cancelDate").value(hasItem(DEFAULT_CANCEL_DATE.toString())));

		// Check, that the count call also returns 1
		restStudentDiscountMockMvc.perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(content().string("1"));
	}

	/**
	 * Executes the search, and checks that the default entity is not returned.
	 */
	private void defaultStudentDiscountShouldNotBeFound(String filter) throws Exception {
		restStudentDiscountMockMvc.perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter)).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$").isEmpty());

		// Check, that the count call also returns 0
		restStudentDiscountMockMvc.perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(content().string("0"));
	}

	@Test
	@Transactional
	void getNonExistingStudentDiscount() throws Exception {
		// Get the studentDiscount
		restStudentDiscountMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
	}

	@Test
	@Transactional
	void putNewStudentDiscount() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		int databaseSizeBeforeUpdate = studentDiscountRepository.findAll().size();

		// Update the studentDiscount
		StudentDiscount updatedStudentDiscount = studentDiscountRepository.findById(studentDiscount.getId()).get();
		// Disconnect from session so that the updates on updatedStudentDiscount are not
		// directly saved in db
		em.detach(updatedStudentDiscount);
		updatedStudentDiscount.feeYear(UPDATED_FEE_YEAR).dueDate(UPDATED_DUE_DATE).janFeeDisc(UPDATED_JAN_FEE_DISC)
				.febFeeDisc(UPDATED_FEB_FEE_DISC).marFeeDisc(UPDATED_MAR_FEE_DISC).aprFeeDisc(UPDATED_APR_FEE_DISC)
				.mayFeeDisc(UPDATED_MAY_FEE_DISC).junFeeDisc(UPDATED_JUN_FEE_DISC).julFeeDisc(UPDATED_JUL_FEE_DISC)
				.augFeeDisc(UPDATED_AUG_FEE_DISC).sepFeeDisc(UPDATED_SEP_FEE_DISC).octFeeDisc(UPDATED_OCT_FEE_DISC)
				.novFeeDisc(UPDATED_NOV_FEE_DISC).decFeeDisc(UPDATED_DEC_FEE_DISC).createDate(UPDATED_CREATE_DATE)
				.lastModified(UPDATED_LAST_MODIFIED).cancelDate(UPDATED_CANCEL_DATE);
		StudentDiscountDTO studentDiscountDTO = studentDiscountMapper.toDto(updatedStudentDiscount);

		restStudentDiscountMockMvc.perform(put(ENTITY_API_URL_ID, studentDiscountDTO.getId())
				.contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(studentDiscountDTO)))
				.andExpect(status().isOk());

		// Validate the StudentDiscount in the database
		List<StudentDiscount> studentDiscountList = studentDiscountRepository.findAll();
		assertThat(studentDiscountList).hasSize(databaseSizeBeforeUpdate);
		StudentDiscount testStudentDiscount = studentDiscountList.get(studentDiscountList.size() - 1);
		assertThat(testStudentDiscount.getFeeYear()).isEqualTo(UPDATED_FEE_YEAR);
		assertThat(testStudentDiscount.getDueDate()).isEqualTo(UPDATED_DUE_DATE);
		assertThat(testStudentDiscount.getJanFeeDisc()).isEqualTo(UPDATED_JAN_FEE_DISC);
		assertThat(testStudentDiscount.getFebFeeDisc()).isEqualTo(UPDATED_FEB_FEE_DISC);
		assertThat(testStudentDiscount.getMarFeeDisc()).isEqualTo(UPDATED_MAR_FEE_DISC);
		assertThat(testStudentDiscount.getAprFeeDisc()).isEqualTo(UPDATED_APR_FEE_DISC);
		assertThat(testStudentDiscount.getMayFeeDisc()).isEqualTo(UPDATED_MAY_FEE_DISC);
		assertThat(testStudentDiscount.getJunFeeDisc()).isEqualTo(UPDATED_JUN_FEE_DISC);
		assertThat(testStudentDiscount.getJulFeeDisc()).isEqualTo(UPDATED_JUL_FEE_DISC);
		assertThat(testStudentDiscount.getAugFeeDisc()).isEqualTo(UPDATED_AUG_FEE_DISC);
		assertThat(testStudentDiscount.getSepFeeDisc()).isEqualTo(UPDATED_SEP_FEE_DISC);
		assertThat(testStudentDiscount.getOctFeeDisc()).isEqualTo(UPDATED_OCT_FEE_DISC);
		assertThat(testStudentDiscount.getNovFeeDisc()).isEqualTo(UPDATED_NOV_FEE_DISC);
		assertThat(testStudentDiscount.getDecFeeDisc()).isEqualTo(UPDATED_DEC_FEE_DISC);
		assertThat(testStudentDiscount.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
		assertThat(testStudentDiscount.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
		assertThat(testStudentDiscount.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
	}

	@Test
	@Transactional
	void putNonExistingStudentDiscount() throws Exception {
		int databaseSizeBeforeUpdate = studentDiscountRepository.findAll().size();
		studentDiscount.setId(count.incrementAndGet());

		// Create the StudentDiscount
		StudentDiscountDTO studentDiscountDTO = studentDiscountMapper.toDto(studentDiscount);

		// If the entity doesn't have an ID, it will throw BadRequestAlertException
		restStudentDiscountMockMvc
				.perform(put(ENTITY_API_URL_ID, studentDiscountDTO.getId()).contentType(MediaType.APPLICATION_JSON)
						.content(TestUtil.convertObjectToJsonBytes(studentDiscountDTO)))
				.andExpect(status().isBadRequest());

		// Validate the StudentDiscount in the database
		List<StudentDiscount> studentDiscountList = studentDiscountRepository.findAll();
		assertThat(studentDiscountList).hasSize(databaseSizeBeforeUpdate);
	}

	@Test
	@Transactional
	void putWithIdMismatchStudentDiscount() throws Exception {
		int databaseSizeBeforeUpdate = studentDiscountRepository.findAll().size();
		studentDiscount.setId(count.incrementAndGet());

		// Create the StudentDiscount
		StudentDiscountDTO studentDiscountDTO = studentDiscountMapper.toDto(studentDiscount);

		// If url ID doesn't match entity ID, it will throw BadRequestAlertException
		restStudentDiscountMockMvc
				.perform(put(ENTITY_API_URL_ID, count.incrementAndGet()).contentType(MediaType.APPLICATION_JSON)
						.content(TestUtil.convertObjectToJsonBytes(studentDiscountDTO)))
				.andExpect(status().isBadRequest());

		// Validate the StudentDiscount in the database
		List<StudentDiscount> studentDiscountList = studentDiscountRepository.findAll();
		assertThat(studentDiscountList).hasSize(databaseSizeBeforeUpdate);
	}

	@Test
	@Transactional
	void putWithMissingIdPathParamStudentDiscount() throws Exception {
		int databaseSizeBeforeUpdate = studentDiscountRepository.findAll().size();
		studentDiscount.setId(count.incrementAndGet());

		// Create the StudentDiscount
		StudentDiscountDTO studentDiscountDTO = studentDiscountMapper.toDto(studentDiscount);

		// If url ID doesn't match entity ID, it will throw BadRequestAlertException
		restStudentDiscountMockMvc
				.perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON)
						.content(TestUtil.convertObjectToJsonBytes(studentDiscountDTO)))
				.andExpect(status().isMethodNotAllowed());

		// Validate the StudentDiscount in the database
		List<StudentDiscount> studentDiscountList = studentDiscountRepository.findAll();
		assertThat(studentDiscountList).hasSize(databaseSizeBeforeUpdate);
	}

	@Test
	@Transactional
	void partialUpdateStudentDiscountWithPatch() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		int databaseSizeBeforeUpdate = studentDiscountRepository.findAll().size();

		// Update the studentDiscount using partial update
		StudentDiscount partialUpdatedStudentDiscount = new StudentDiscount();
		partialUpdatedStudentDiscount.setId(studentDiscount.getId());

		partialUpdatedStudentDiscount.feeYear(UPDATED_FEE_YEAR).dueDate(UPDATED_DUE_DATE)
				.marFeeDisc(UPDATED_MAR_FEE_DISC).aprFeeDisc(UPDATED_APR_FEE_DISC).mayFeeDisc(UPDATED_MAY_FEE_DISC)
				.junFeeDisc(UPDATED_JUN_FEE_DISC).julFeeDisc(UPDATED_JUL_FEE_DISC).sepFeeDisc(UPDATED_SEP_FEE_DISC)
				.novFeeDisc(UPDATED_NOV_FEE_DISC).createDate(UPDATED_CREATE_DATE);

		restStudentDiscountMockMvc
				.perform(patch(ENTITY_API_URL_ID, partialUpdatedStudentDiscount.getId())
						.contentType("application/merge-patch+json")
						.content(TestUtil.convertObjectToJsonBytes(partialUpdatedStudentDiscount)))
				.andExpect(status().isOk());

		// Validate the StudentDiscount in the database
		List<StudentDiscount> studentDiscountList = studentDiscountRepository.findAll();
		assertThat(studentDiscountList).hasSize(databaseSizeBeforeUpdate);
		StudentDiscount testStudentDiscount = studentDiscountList.get(studentDiscountList.size() - 1);
		assertThat(testStudentDiscount.getFeeYear()).isEqualTo(UPDATED_FEE_YEAR);
		assertThat(testStudentDiscount.getDueDate()).isEqualTo(UPDATED_DUE_DATE);
		assertThat(testStudentDiscount.getJanFeeDisc()).isEqualTo(DEFAULT_JAN_FEE_DISC);
		assertThat(testStudentDiscount.getFebFeeDisc()).isEqualTo(DEFAULT_FEB_FEE_DISC);
		assertThat(testStudentDiscount.getMarFeeDisc()).isEqualTo(UPDATED_MAR_FEE_DISC);
		assertThat(testStudentDiscount.getAprFeeDisc()).isEqualTo(UPDATED_APR_FEE_DISC);
		assertThat(testStudentDiscount.getMayFeeDisc()).isEqualTo(UPDATED_MAY_FEE_DISC);
		assertThat(testStudentDiscount.getJunFeeDisc()).isEqualTo(UPDATED_JUN_FEE_DISC);
		assertThat(testStudentDiscount.getJulFeeDisc()).isEqualTo(UPDATED_JUL_FEE_DISC);
		assertThat(testStudentDiscount.getAugFeeDisc()).isEqualTo(DEFAULT_AUG_FEE_DISC);
		assertThat(testStudentDiscount.getSepFeeDisc()).isEqualTo(UPDATED_SEP_FEE_DISC);
		assertThat(testStudentDiscount.getOctFeeDisc()).isEqualTo(DEFAULT_OCT_FEE_DISC);
		assertThat(testStudentDiscount.getNovFeeDisc()).isEqualTo(UPDATED_NOV_FEE_DISC);
		assertThat(testStudentDiscount.getDecFeeDisc()).isEqualTo(DEFAULT_DEC_FEE_DISC);
		assertThat(testStudentDiscount.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
		assertThat(testStudentDiscount.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
		assertThat(testStudentDiscount.getCancelDate()).isEqualTo(DEFAULT_CANCEL_DATE);
	}

	@Test
	@Transactional
	void fullUpdateStudentDiscountWithPatch() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		int databaseSizeBeforeUpdate = studentDiscountRepository.findAll().size();

		// Update the studentDiscount using partial update
		StudentDiscount partialUpdatedStudentDiscount = new StudentDiscount();
		partialUpdatedStudentDiscount.setId(studentDiscount.getId());

		partialUpdatedStudentDiscount.feeYear(UPDATED_FEE_YEAR).dueDate(UPDATED_DUE_DATE)
				.janFeeDisc(UPDATED_JAN_FEE_DISC).febFeeDisc(UPDATED_FEB_FEE_DISC).marFeeDisc(UPDATED_MAR_FEE_DISC)
				.aprFeeDisc(UPDATED_APR_FEE_DISC).mayFeeDisc(UPDATED_MAY_FEE_DISC).junFeeDisc(UPDATED_JUN_FEE_DISC)
				.julFeeDisc(UPDATED_JUL_FEE_DISC).augFeeDisc(UPDATED_AUG_FEE_DISC).sepFeeDisc(UPDATED_SEP_FEE_DISC)
				.octFeeDisc(UPDATED_OCT_FEE_DISC).novFeeDisc(UPDATED_NOV_FEE_DISC).decFeeDisc(UPDATED_DEC_FEE_DISC)
				.createDate(UPDATED_CREATE_DATE).lastModified(UPDATED_LAST_MODIFIED).cancelDate(UPDATED_CANCEL_DATE);

		restStudentDiscountMockMvc
				.perform(patch(ENTITY_API_URL_ID, partialUpdatedStudentDiscount.getId())
						.contentType("application/merge-patch+json")
						.content(TestUtil.convertObjectToJsonBytes(partialUpdatedStudentDiscount)))
				.andExpect(status().isOk());

		// Validate the StudentDiscount in the database
		List<StudentDiscount> studentDiscountList = studentDiscountRepository.findAll();
		assertThat(studentDiscountList).hasSize(databaseSizeBeforeUpdate);
		StudentDiscount testStudentDiscount = studentDiscountList.get(studentDiscountList.size() - 1);
		assertThat(testStudentDiscount.getFeeYear()).isEqualTo(UPDATED_FEE_YEAR);
		assertThat(testStudentDiscount.getDueDate()).isEqualTo(UPDATED_DUE_DATE);
		assertThat(testStudentDiscount.getJanFeeDisc()).isEqualTo(UPDATED_JAN_FEE_DISC);
		assertThat(testStudentDiscount.getFebFeeDisc()).isEqualTo(UPDATED_FEB_FEE_DISC);
		assertThat(testStudentDiscount.getMarFeeDisc()).isEqualTo(UPDATED_MAR_FEE_DISC);
		assertThat(testStudentDiscount.getAprFeeDisc()).isEqualTo(UPDATED_APR_FEE_DISC);
		assertThat(testStudentDiscount.getMayFeeDisc()).isEqualTo(UPDATED_MAY_FEE_DISC);
		assertThat(testStudentDiscount.getJunFeeDisc()).isEqualTo(UPDATED_JUN_FEE_DISC);
		assertThat(testStudentDiscount.getJulFeeDisc()).isEqualTo(UPDATED_JUL_FEE_DISC);
		assertThat(testStudentDiscount.getAugFeeDisc()).isEqualTo(UPDATED_AUG_FEE_DISC);
		assertThat(testStudentDiscount.getSepFeeDisc()).isEqualTo(UPDATED_SEP_FEE_DISC);
		assertThat(testStudentDiscount.getOctFeeDisc()).isEqualTo(UPDATED_OCT_FEE_DISC);
		assertThat(testStudentDiscount.getNovFeeDisc()).isEqualTo(UPDATED_NOV_FEE_DISC);
		assertThat(testStudentDiscount.getDecFeeDisc()).isEqualTo(UPDATED_DEC_FEE_DISC);
		assertThat(testStudentDiscount.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
		assertThat(testStudentDiscount.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
		assertThat(testStudentDiscount.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
	}

	@Test
	@Transactional
	void patchNonExistingStudentDiscount() throws Exception {
		int databaseSizeBeforeUpdate = studentDiscountRepository.findAll().size();
		studentDiscount.setId(count.incrementAndGet());

		// Create the StudentDiscount
		StudentDiscountDTO studentDiscountDTO = studentDiscountMapper.toDto(studentDiscount);

		// If the entity doesn't have an ID, it will throw BadRequestAlertException
		restStudentDiscountMockMvc
				.perform(
						patch(ENTITY_API_URL_ID, studentDiscountDTO.getId()).contentType("application/merge-patch+json")
								.content(TestUtil.convertObjectToJsonBytes(studentDiscountDTO)))
				.andExpect(status().isBadRequest());

		// Validate the StudentDiscount in the database
		List<StudentDiscount> studentDiscountList = studentDiscountRepository.findAll();
		assertThat(studentDiscountList).hasSize(databaseSizeBeforeUpdate);
	}

	@Test
	@Transactional
	void patchWithIdMismatchStudentDiscount() throws Exception {
		int databaseSizeBeforeUpdate = studentDiscountRepository.findAll().size();
		studentDiscount.setId(count.incrementAndGet());

		// Create the StudentDiscount
		StudentDiscountDTO studentDiscountDTO = studentDiscountMapper.toDto(studentDiscount);

		// If url ID doesn't match entity ID, it will throw BadRequestAlertException
		restStudentDiscountMockMvc
				.perform(patch(ENTITY_API_URL_ID, count.incrementAndGet()).contentType("application/merge-patch+json")
						.content(TestUtil.convertObjectToJsonBytes(studentDiscountDTO)))
				.andExpect(status().isBadRequest());

		// Validate the StudentDiscount in the database
		List<StudentDiscount> studentDiscountList = studentDiscountRepository.findAll();
		assertThat(studentDiscountList).hasSize(databaseSizeBeforeUpdate);
	}

	@Test
	@Transactional
	void patchWithMissingIdPathParamStudentDiscount() throws Exception {
		int databaseSizeBeforeUpdate = studentDiscountRepository.findAll().size();
		studentDiscount.setId(count.incrementAndGet());

		// Create the StudentDiscount
		StudentDiscountDTO studentDiscountDTO = studentDiscountMapper.toDto(studentDiscount);

		// If url ID doesn't match entity ID, it will throw BadRequestAlertException
		restStudentDiscountMockMvc
				.perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json")
						.content(TestUtil.convertObjectToJsonBytes(studentDiscountDTO)))
				.andExpect(status().isMethodNotAllowed());

		// Validate the StudentDiscount in the database
		List<StudentDiscount> studentDiscountList = studentDiscountRepository.findAll();
		assertThat(studentDiscountList).hasSize(databaseSizeBeforeUpdate);
	}

	@Test
	@Transactional
	void deleteStudentDiscount() throws Exception {
		// Initialize the database
		studentDiscountRepository.saveAndFlush(studentDiscount);

		int databaseSizeBeforeDelete = studentDiscountRepository.findAll().size();

		// Delete the studentDiscount
		restStudentDiscountMockMvc
				.perform(delete(ENTITY_API_URL_ID, studentDiscount.getId()).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());

		// Validate the database contains one less item
		List<StudentDiscount> studentDiscountList = studentDiscountRepository.findAll();
		assertThat(studentDiscountList).hasSize(databaseSizeBeforeDelete - 1);
	}
}
