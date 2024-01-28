package com.ssik.manageit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ssik.manageit.IntegrationTest;
import com.ssik.manageit.domain.ClassStudent;
import com.ssik.manageit.domain.SchoolLedgerHead;
import com.ssik.manageit.domain.StudentAdditionalCharges;
import com.ssik.manageit.domain.enumeration.FeeYear;
import com.ssik.manageit.repository.StudentAdditionalChargesRepository;
import com.ssik.manageit.service.criteria.StudentAdditionalChargesCriteria;
import com.ssik.manageit.service.dto.StudentAdditionalChargesDTO;
import com.ssik.manageit.service.mapper.StudentAdditionalChargesMapper;
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
 * Integration tests for the {@link StudentAdditionalChargesResource} REST
 * controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StudentAdditionalChargesResourceIT {

	private static final FeeYear DEFAULT_FEE_YEAR = FeeYear.YEAR_2023;
	private static final FeeYear UPDATED_FEE_YEAR = FeeYear.YEAR_2023;

	private static final Integer DEFAULT_DUE_DATE = 1;
	private static final Integer UPDATED_DUE_DATE = 2;
	private static final Integer SMALLER_DUE_DATE = 1 - 1;

	private static final Double DEFAULT_JAN_ADD_CHRG = 1D;
	private static final Double UPDATED_JAN_ADD_CHRG = 2D;
	private static final Double SMALLER_JAN_ADD_CHRG = 1D - 1D;

	private static final Double DEFAULT_FEB_ADD_CHRGC = 1D;
	private static final Double UPDATED_FEB_ADD_CHRGC = 2D;
	private static final Double SMALLER_FEB_ADD_CHRGC = 1D - 1D;

	private static final Double DEFAULT_MAR_ADD_CHRG = 1D;
	private static final Double UPDATED_MAR_ADD_CHRG = 2D;
	private static final Double SMALLER_MAR_ADD_CHRG = 1D - 1D;

	private static final Double DEFAULT_APR_ADD_CHRG = 1D;
	private static final Double UPDATED_APR_ADD_CHRG = 2D;
	private static final Double SMALLER_APR_ADD_CHRG = 1D - 1D;

	private static final Double DEFAULT_MAY_ADD_CHRG = 1D;
	private static final Double UPDATED_MAY_ADD_CHRG = 2D;
	private static final Double SMALLER_MAY_ADD_CHRG = 1D - 1D;

	private static final Double DEFAULT_JUN_ADD_CHRG = 1D;
	private static final Double UPDATED_JUN_ADD_CHRG = 2D;
	private static final Double SMALLER_JUN_ADD_CHRG = 1D - 1D;

	private static final Double DEFAULT_JUL_ADD_CHRG = 1D;
	private static final Double UPDATED_JUL_ADD_CHRG = 2D;
	private static final Double SMALLER_JUL_ADD_CHRG = 1D - 1D;

	private static final Double DEFAULT_AUG_ADD_CHRG = 1D;
	private static final Double UPDATED_AUG_ADD_CHRG = 2D;
	private static final Double SMALLER_AUG_ADD_CHRG = 1D - 1D;

	private static final Double DEFAULT_SEP_ADD_CHRG = 1D;
	private static final Double UPDATED_SEP_ADD_CHRG = 2D;
	private static final Double SMALLER_SEP_ADD_CHRG = 1D - 1D;

	private static final Double DEFAULT_OCT_ADD_CHRG = 1D;
	private static final Double UPDATED_OCT_ADD_CHRG = 2D;
	private static final Double SMALLER_OCT_ADD_CHRG = 1D - 1D;

	private static final Double DEFAULT_NOV_ADD_CHRG = 1D;
	private static final Double UPDATED_NOV_ADD_CHRG = 2D;
	private static final Double SMALLER_NOV_ADD_CHRG = 1D - 1D;

	private static final Double DEFAULT_DEC_ADD_CHRG = 1D;
	private static final Double UPDATED_DEC_ADD_CHRG = 2D;
	private static final Double SMALLER_DEC_ADD_CHRG = 1D - 1D;

	private static final LocalDate DEFAULT_CREATE_DATE = LocalDate.ofEpochDay(0L);
	private static final LocalDate UPDATED_CREATE_DATE = LocalDate.now(ZoneId.systemDefault());
	private static final LocalDate SMALLER_CREATE_DATE = LocalDate.ofEpochDay(-1L);

	private static final LocalDate DEFAULT_LAST_MODIFIED = LocalDate.ofEpochDay(0L);
	private static final LocalDate UPDATED_LAST_MODIFIED = LocalDate.now(ZoneId.systemDefault());
	private static final LocalDate SMALLER_LAST_MODIFIED = LocalDate.ofEpochDay(-1L);

	private static final LocalDate DEFAULT_CANCEL_DATE = LocalDate.ofEpochDay(0L);
	private static final LocalDate UPDATED_CANCEL_DATE = LocalDate.now(ZoneId.systemDefault());
	private static final LocalDate SMALLER_CANCEL_DATE = LocalDate.ofEpochDay(-1L);

	private static final String ENTITY_API_URL = "/api/student-additional-charges";
	private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

	private static Random random = new Random();
	private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

	@Autowired
	private StudentAdditionalChargesRepository studentAdditionalChargesRepository;

	@Autowired
	private StudentAdditionalChargesMapper studentAdditionalChargesMapper;

	@Autowired
	private EntityManager em;

	@Autowired
	private MockMvc restStudentAdditionalChargesMockMvc;

	private StudentAdditionalCharges studentAdditionalCharges;

	/**
	 * Create an entity for this test.
	 *
	 * This is a static method, as tests for other entities might also need it, if
	 * they test an entity which requires the current entity.
	 */
	public static StudentAdditionalCharges createEntity(EntityManager em) {
		StudentAdditionalCharges studentAdditionalCharges = new StudentAdditionalCharges().feeYear(DEFAULT_FEE_YEAR)
				.dueDate(DEFAULT_DUE_DATE).janAddChrg(DEFAULT_JAN_ADD_CHRG).febAddChrgc(DEFAULT_FEB_ADD_CHRGC)
				.marAddChrg(DEFAULT_MAR_ADD_CHRG).aprAddChrg(DEFAULT_APR_ADD_CHRG).mayAddChrg(DEFAULT_MAY_ADD_CHRG)
				.junAddChrg(DEFAULT_JUN_ADD_CHRG).julAddChrg(DEFAULT_JUL_ADD_CHRG).augAddChrg(DEFAULT_AUG_ADD_CHRG)
				.sepAddChrg(DEFAULT_SEP_ADD_CHRG).octAddChrg(DEFAULT_OCT_ADD_CHRG).novAddChrg(DEFAULT_NOV_ADD_CHRG)
				.decAddChrg(DEFAULT_DEC_ADD_CHRG).createDate(DEFAULT_CREATE_DATE).lastModified(DEFAULT_LAST_MODIFIED)
				.cancelDate(DEFAULT_CANCEL_DATE);
		return studentAdditionalCharges;
	}

	/**
	 * Create an updated entity for this test.
	 *
	 * This is a static method, as tests for other entities might also need it, if
	 * they test an entity which requires the current entity.
	 */
	public static StudentAdditionalCharges createUpdatedEntity(EntityManager em) {
		StudentAdditionalCharges studentAdditionalCharges = new StudentAdditionalCharges().feeYear(UPDATED_FEE_YEAR)
				.dueDate(UPDATED_DUE_DATE).janAddChrg(UPDATED_JAN_ADD_CHRG).febAddChrgc(UPDATED_FEB_ADD_CHRGC)
				.marAddChrg(UPDATED_MAR_ADD_CHRG).aprAddChrg(UPDATED_APR_ADD_CHRG).mayAddChrg(UPDATED_MAY_ADD_CHRG)
				.junAddChrg(UPDATED_JUN_ADD_CHRG).julAddChrg(UPDATED_JUL_ADD_CHRG).augAddChrg(UPDATED_AUG_ADD_CHRG)
				.sepAddChrg(UPDATED_SEP_ADD_CHRG).octAddChrg(UPDATED_OCT_ADD_CHRG).novAddChrg(UPDATED_NOV_ADD_CHRG)
				.decAddChrg(UPDATED_DEC_ADD_CHRG).createDate(UPDATED_CREATE_DATE).lastModified(UPDATED_LAST_MODIFIED)
				.cancelDate(UPDATED_CANCEL_DATE);
		return studentAdditionalCharges;
	}

	@BeforeEach
	public void initTest() {
		studentAdditionalCharges = createEntity(em);
	}

	@Test
	@Transactional
	void createStudentAdditionalCharges() throws Exception {
		int databaseSizeBeforeCreate = studentAdditionalChargesRepository.findAll().size();
		// Create the StudentAdditionalCharges
		StudentAdditionalChargesDTO studentAdditionalChargesDTO = studentAdditionalChargesMapper
				.toDto(studentAdditionalCharges);
		restStudentAdditionalChargesMockMvc
				.perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON)
						.content(TestUtil.convertObjectToJsonBytes(studentAdditionalChargesDTO)))
				.andExpect(status().isCreated());

		// Validate the StudentAdditionalCharges in the database
		List<StudentAdditionalCharges> studentAdditionalChargesList = studentAdditionalChargesRepository.findAll();
		assertThat(studentAdditionalChargesList).hasSize(databaseSizeBeforeCreate + 1);
		StudentAdditionalCharges testStudentAdditionalCharges = studentAdditionalChargesList
				.get(studentAdditionalChargesList.size() - 1);
		assertThat(testStudentAdditionalCharges.getFeeYear()).isEqualTo(DEFAULT_FEE_YEAR);
		assertThat(testStudentAdditionalCharges.getDueDate()).isEqualTo(DEFAULT_DUE_DATE);
		assertThat(testStudentAdditionalCharges.getJanAddChrg()).isEqualTo(DEFAULT_JAN_ADD_CHRG);
		assertThat(testStudentAdditionalCharges.getFebAddChrgc()).isEqualTo(DEFAULT_FEB_ADD_CHRGC);
		assertThat(testStudentAdditionalCharges.getMarAddChrg()).isEqualTo(DEFAULT_MAR_ADD_CHRG);
		assertThat(testStudentAdditionalCharges.getAprAddChrg()).isEqualTo(DEFAULT_APR_ADD_CHRG);
		assertThat(testStudentAdditionalCharges.getMayAddChrg()).isEqualTo(DEFAULT_MAY_ADD_CHRG);
		assertThat(testStudentAdditionalCharges.getJunAddChrg()).isEqualTo(DEFAULT_JUN_ADD_CHRG);
		assertThat(testStudentAdditionalCharges.getJulAddChrg()).isEqualTo(DEFAULT_JUL_ADD_CHRG);
		assertThat(testStudentAdditionalCharges.getAugAddChrg()).isEqualTo(DEFAULT_AUG_ADD_CHRG);
		assertThat(testStudentAdditionalCharges.getSepAddChrg()).isEqualTo(DEFAULT_SEP_ADD_CHRG);
		assertThat(testStudentAdditionalCharges.getOctAddChrg()).isEqualTo(DEFAULT_OCT_ADD_CHRG);
		assertThat(testStudentAdditionalCharges.getNovAddChrg()).isEqualTo(DEFAULT_NOV_ADD_CHRG);
		assertThat(testStudentAdditionalCharges.getDecAddChrg()).isEqualTo(DEFAULT_DEC_ADD_CHRG);
		assertThat(testStudentAdditionalCharges.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
		assertThat(testStudentAdditionalCharges.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
		assertThat(testStudentAdditionalCharges.getCancelDate()).isEqualTo(DEFAULT_CANCEL_DATE);
	}

	@Test
	@Transactional
	void createStudentAdditionalChargesWithExistingId() throws Exception {
		// Create the StudentAdditionalCharges with an existing ID
		studentAdditionalCharges.setId(1L);
		StudentAdditionalChargesDTO studentAdditionalChargesDTO = studentAdditionalChargesMapper
				.toDto(studentAdditionalCharges);

		int databaseSizeBeforeCreate = studentAdditionalChargesRepository.findAll().size();

		// An entity with an existing ID cannot be created, so this API call must fail
		restStudentAdditionalChargesMockMvc
				.perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON)
						.content(TestUtil.convertObjectToJsonBytes(studentAdditionalChargesDTO)))
				.andExpect(status().isBadRequest());

		// Validate the StudentAdditionalCharges in the database
		List<StudentAdditionalCharges> studentAdditionalChargesList = studentAdditionalChargesRepository.findAll();
		assertThat(studentAdditionalChargesList).hasSize(databaseSizeBeforeCreate);
	}

	@Test
	@Transactional
	void checkFeeYearIsRequired() throws Exception {
		int databaseSizeBeforeTest = studentAdditionalChargesRepository.findAll().size();
		// set the field null
		studentAdditionalCharges.setFeeYear(null);

		// Create the StudentAdditionalCharges, which fails.
		StudentAdditionalChargesDTO studentAdditionalChargesDTO = studentAdditionalChargesMapper
				.toDto(studentAdditionalCharges);

		restStudentAdditionalChargesMockMvc
				.perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON)
						.content(TestUtil.convertObjectToJsonBytes(studentAdditionalChargesDTO)))
				.andExpect(status().isBadRequest());

		List<StudentAdditionalCharges> studentAdditionalChargesList = studentAdditionalChargesRepository.findAll();
		assertThat(studentAdditionalChargesList).hasSize(databaseSizeBeforeTest);
	}

	@Test
	@Transactional
	void checkDueDateIsRequired() throws Exception {
		int databaseSizeBeforeTest = studentAdditionalChargesRepository.findAll().size();
		// set the field null
		studentAdditionalCharges.setDueDate(null);

		// Create the StudentAdditionalCharges, which fails.
		StudentAdditionalChargesDTO studentAdditionalChargesDTO = studentAdditionalChargesMapper
				.toDto(studentAdditionalCharges);

		restStudentAdditionalChargesMockMvc
				.perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON)
						.content(TestUtil.convertObjectToJsonBytes(studentAdditionalChargesDTO)))
				.andExpect(status().isBadRequest());

		List<StudentAdditionalCharges> studentAdditionalChargesList = studentAdditionalChargesRepository.findAll();
		assertThat(studentAdditionalChargesList).hasSize(databaseSizeBeforeTest);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalCharges() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList
		restStudentAdditionalChargesMockMvc.perform(get(ENTITY_API_URL + "?sort=id,desc")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.[*].id").value(hasItem(studentAdditionalCharges.getId().intValue())))
				.andExpect(jsonPath("$.[*].feeYear").value(hasItem(DEFAULT_FEE_YEAR.toString())))
				.andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE)))
				.andExpect(jsonPath("$.[*].janAddChrg").value(hasItem(DEFAULT_JAN_ADD_CHRG.doubleValue())))
				.andExpect(jsonPath("$.[*].febAddChrgc").value(hasItem(DEFAULT_FEB_ADD_CHRGC.doubleValue())))
				.andExpect(jsonPath("$.[*].marAddChrg").value(hasItem(DEFAULT_MAR_ADD_CHRG.doubleValue())))
				.andExpect(jsonPath("$.[*].aprAddChrg").value(hasItem(DEFAULT_APR_ADD_CHRG.doubleValue())))
				.andExpect(jsonPath("$.[*].mayAddChrg").value(hasItem(DEFAULT_MAY_ADD_CHRG.doubleValue())))
				.andExpect(jsonPath("$.[*].junAddChrg").value(hasItem(DEFAULT_JUN_ADD_CHRG.doubleValue())))
				.andExpect(jsonPath("$.[*].julAddChrg").value(hasItem(DEFAULT_JUL_ADD_CHRG.doubleValue())))
				.andExpect(jsonPath("$.[*].augAddChrg").value(hasItem(DEFAULT_AUG_ADD_CHRG.doubleValue())))
				.andExpect(jsonPath("$.[*].sepAddChrg").value(hasItem(DEFAULT_SEP_ADD_CHRG.doubleValue())))
				.andExpect(jsonPath("$.[*].octAddChrg").value(hasItem(DEFAULT_OCT_ADD_CHRG.doubleValue())))
				.andExpect(jsonPath("$.[*].novAddChrg").value(hasItem(DEFAULT_NOV_ADD_CHRG.doubleValue())))
				.andExpect(jsonPath("$.[*].decAddChrg").value(hasItem(DEFAULT_DEC_ADD_CHRG.doubleValue())))
				.andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
				.andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
				.andExpect(jsonPath("$.[*].cancelDate").value(hasItem(DEFAULT_CANCEL_DATE.toString())));
	}

	@Test
	@Transactional
	void getStudentAdditionalCharges() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get the studentAdditionalCharges
		restStudentAdditionalChargesMockMvc.perform(get(ENTITY_API_URL_ID, studentAdditionalCharges.getId()))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.id").value(studentAdditionalCharges.getId().intValue()))
				.andExpect(jsonPath("$.feeYear").value(DEFAULT_FEE_YEAR.toString()))
				.andExpect(jsonPath("$.dueDate").value(DEFAULT_DUE_DATE))
				.andExpect(jsonPath("$.janAddChrg").value(DEFAULT_JAN_ADD_CHRG.doubleValue()))
				.andExpect(jsonPath("$.febAddChrgc").value(DEFAULT_FEB_ADD_CHRGC.doubleValue()))
				.andExpect(jsonPath("$.marAddChrg").value(DEFAULT_MAR_ADD_CHRG.doubleValue()))
				.andExpect(jsonPath("$.aprAddChrg").value(DEFAULT_APR_ADD_CHRG.doubleValue()))
				.andExpect(jsonPath("$.mayAddChrg").value(DEFAULT_MAY_ADD_CHRG.doubleValue()))
				.andExpect(jsonPath("$.junAddChrg").value(DEFAULT_JUN_ADD_CHRG.doubleValue()))
				.andExpect(jsonPath("$.julAddChrg").value(DEFAULT_JUL_ADD_CHRG.doubleValue()))
				.andExpect(jsonPath("$.augAddChrg").value(DEFAULT_AUG_ADD_CHRG.doubleValue()))
				.andExpect(jsonPath("$.sepAddChrg").value(DEFAULT_SEP_ADD_CHRG.doubleValue()))
				.andExpect(jsonPath("$.octAddChrg").value(DEFAULT_OCT_ADD_CHRG.doubleValue()))
				.andExpect(jsonPath("$.novAddChrg").value(DEFAULT_NOV_ADD_CHRG.doubleValue()))
				.andExpect(jsonPath("$.decAddChrg").value(DEFAULT_DEC_ADD_CHRG.doubleValue()))
				.andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
				.andExpect(jsonPath("$.lastModified").value(DEFAULT_LAST_MODIFIED.toString()))
				.andExpect(jsonPath("$.cancelDate").value(DEFAULT_CANCEL_DATE.toString()));
	}

	@Test
	@Transactional
	void getStudentAdditionalChargesByIdFiltering() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		Long id = studentAdditionalCharges.getId();

		defaultStudentAdditionalChargesShouldBeFound("id.equals=" + id);
		defaultStudentAdditionalChargesShouldNotBeFound("id.notEquals=" + id);

		defaultStudentAdditionalChargesShouldBeFound("id.greaterThanOrEqual=" + id);
		defaultStudentAdditionalChargesShouldNotBeFound("id.greaterThan=" + id);

		defaultStudentAdditionalChargesShouldBeFound("id.lessThanOrEqual=" + id);
		defaultStudentAdditionalChargesShouldNotBeFound("id.lessThan=" + id);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByFeeYearIsEqualToSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where feeYear equals to
		// DEFAULT_FEE_YEAR
		defaultStudentAdditionalChargesShouldBeFound("feeYear.equals=" + DEFAULT_FEE_YEAR);

		// Get all the studentAdditionalChargesList where feeYear equals to
		// UPDATED_FEE_YEAR
		defaultStudentAdditionalChargesShouldNotBeFound("feeYear.equals=" + UPDATED_FEE_YEAR);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByFeeYearIsNotEqualToSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where feeYear not equals to
		// DEFAULT_FEE_YEAR
		defaultStudentAdditionalChargesShouldNotBeFound("feeYear.notEquals=" + DEFAULT_FEE_YEAR);

		// Get all the studentAdditionalChargesList where feeYear not equals to
		// UPDATED_FEE_YEAR
		defaultStudentAdditionalChargesShouldBeFound("feeYear.notEquals=" + UPDATED_FEE_YEAR);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByFeeYearIsInShouldWork() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where feeYear in DEFAULT_FEE_YEAR or
		// UPDATED_FEE_YEAR
		defaultStudentAdditionalChargesShouldBeFound("feeYear.in=" + DEFAULT_FEE_YEAR + "," + UPDATED_FEE_YEAR);

		// Get all the studentAdditionalChargesList where feeYear equals to
		// UPDATED_FEE_YEAR
		defaultStudentAdditionalChargesShouldNotBeFound("feeYear.in=" + UPDATED_FEE_YEAR);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByFeeYearIsNullOrNotNull() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where feeYear is not null
		defaultStudentAdditionalChargesShouldBeFound("feeYear.specified=true");

		// Get all the studentAdditionalChargesList where feeYear is null
		defaultStudentAdditionalChargesShouldNotBeFound("feeYear.specified=false");
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByDueDateIsEqualToSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where dueDate equals to
		// DEFAULT_DUE_DATE
		defaultStudentAdditionalChargesShouldBeFound("dueDate.equals=" + DEFAULT_DUE_DATE);

		// Get all the studentAdditionalChargesList where dueDate equals to
		// UPDATED_DUE_DATE
		defaultStudentAdditionalChargesShouldNotBeFound("dueDate.equals=" + UPDATED_DUE_DATE);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByDueDateIsNotEqualToSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where dueDate not equals to
		// DEFAULT_DUE_DATE
		defaultStudentAdditionalChargesShouldNotBeFound("dueDate.notEquals=" + DEFAULT_DUE_DATE);

		// Get all the studentAdditionalChargesList where dueDate not equals to
		// UPDATED_DUE_DATE
		defaultStudentAdditionalChargesShouldBeFound("dueDate.notEquals=" + UPDATED_DUE_DATE);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByDueDateIsInShouldWork() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where dueDate in DEFAULT_DUE_DATE or
		// UPDATED_DUE_DATE
		defaultStudentAdditionalChargesShouldBeFound("dueDate.in=" + DEFAULT_DUE_DATE + "," + UPDATED_DUE_DATE);

		// Get all the studentAdditionalChargesList where dueDate equals to
		// UPDATED_DUE_DATE
		defaultStudentAdditionalChargesShouldNotBeFound("dueDate.in=" + UPDATED_DUE_DATE);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByDueDateIsNullOrNotNull() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where dueDate is not null
		defaultStudentAdditionalChargesShouldBeFound("dueDate.specified=true");

		// Get all the studentAdditionalChargesList where dueDate is null
		defaultStudentAdditionalChargesShouldNotBeFound("dueDate.specified=false");
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByDueDateIsGreaterThanOrEqualToSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where dueDate is greater than or
		// equal to DEFAULT_DUE_DATE
		defaultStudentAdditionalChargesShouldBeFound("dueDate.greaterThanOrEqual=" + DEFAULT_DUE_DATE);

		// Get all the studentAdditionalChargesList where dueDate is greater than or
		// equal to UPDATED_DUE_DATE
		defaultStudentAdditionalChargesShouldNotBeFound("dueDate.greaterThanOrEqual=" + UPDATED_DUE_DATE);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByDueDateIsLessThanOrEqualToSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where dueDate is less than or equal
		// to DEFAULT_DUE_DATE
		defaultStudentAdditionalChargesShouldBeFound("dueDate.lessThanOrEqual=" + DEFAULT_DUE_DATE);

		// Get all the studentAdditionalChargesList where dueDate is less than or equal
		// to SMALLER_DUE_DATE
		defaultStudentAdditionalChargesShouldNotBeFound("dueDate.lessThanOrEqual=" + SMALLER_DUE_DATE);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByDueDateIsLessThanSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where dueDate is less than
		// DEFAULT_DUE_DATE
		defaultStudentAdditionalChargesShouldNotBeFound("dueDate.lessThan=" + DEFAULT_DUE_DATE);

		// Get all the studentAdditionalChargesList where dueDate is less than
		// UPDATED_DUE_DATE
		defaultStudentAdditionalChargesShouldBeFound("dueDate.lessThan=" + UPDATED_DUE_DATE);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByDueDateIsGreaterThanSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where dueDate is greater than
		// DEFAULT_DUE_DATE
		defaultStudentAdditionalChargesShouldNotBeFound("dueDate.greaterThan=" + DEFAULT_DUE_DATE);

		// Get all the studentAdditionalChargesList where dueDate is greater than
		// SMALLER_DUE_DATE
		defaultStudentAdditionalChargesShouldBeFound("dueDate.greaterThan=" + SMALLER_DUE_DATE);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByJanAddChrgIsEqualToSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where janAddChrg equals to
		// DEFAULT_JAN_ADD_CHRG
		defaultStudentAdditionalChargesShouldBeFound("janAddChrg.equals=" + DEFAULT_JAN_ADD_CHRG);

		// Get all the studentAdditionalChargesList where janAddChrg equals to
		// UPDATED_JAN_ADD_CHRG
		defaultStudentAdditionalChargesShouldNotBeFound("janAddChrg.equals=" + UPDATED_JAN_ADD_CHRG);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByJanAddChrgIsNotEqualToSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where janAddChrg not equals to
		// DEFAULT_JAN_ADD_CHRG
		defaultStudentAdditionalChargesShouldNotBeFound("janAddChrg.notEquals=" + DEFAULT_JAN_ADD_CHRG);

		// Get all the studentAdditionalChargesList where janAddChrg not equals to
		// UPDATED_JAN_ADD_CHRG
		defaultStudentAdditionalChargesShouldBeFound("janAddChrg.notEquals=" + UPDATED_JAN_ADD_CHRG);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByJanAddChrgIsInShouldWork() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where janAddChrg in
		// DEFAULT_JAN_ADD_CHRG or UPDATED_JAN_ADD_CHRG
		defaultStudentAdditionalChargesShouldBeFound(
				"janAddChrg.in=" + DEFAULT_JAN_ADD_CHRG + "," + UPDATED_JAN_ADD_CHRG);

		// Get all the studentAdditionalChargesList where janAddChrg equals to
		// UPDATED_JAN_ADD_CHRG
		defaultStudentAdditionalChargesShouldNotBeFound("janAddChrg.in=" + UPDATED_JAN_ADD_CHRG);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByJanAddChrgIsNullOrNotNull() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where janAddChrg is not null
		defaultStudentAdditionalChargesShouldBeFound("janAddChrg.specified=true");

		// Get all the studentAdditionalChargesList where janAddChrg is null
		defaultStudentAdditionalChargesShouldNotBeFound("janAddChrg.specified=false");
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByJanAddChrgIsGreaterThanOrEqualToSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where janAddChrg is greater than or
		// equal to DEFAULT_JAN_ADD_CHRG
		defaultStudentAdditionalChargesShouldBeFound("janAddChrg.greaterThanOrEqual=" + DEFAULT_JAN_ADD_CHRG);

		// Get all the studentAdditionalChargesList where janAddChrg is greater than or
		// equal to UPDATED_JAN_ADD_CHRG
		defaultStudentAdditionalChargesShouldNotBeFound("janAddChrg.greaterThanOrEqual=" + UPDATED_JAN_ADD_CHRG);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByJanAddChrgIsLessThanOrEqualToSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where janAddChrg is less than or
		// equal to DEFAULT_JAN_ADD_CHRG
		defaultStudentAdditionalChargesShouldBeFound("janAddChrg.lessThanOrEqual=" + DEFAULT_JAN_ADD_CHRG);

		// Get all the studentAdditionalChargesList where janAddChrg is less than or
		// equal to SMALLER_JAN_ADD_CHRG
		defaultStudentAdditionalChargesShouldNotBeFound("janAddChrg.lessThanOrEqual=" + SMALLER_JAN_ADD_CHRG);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByJanAddChrgIsLessThanSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where janAddChrg is less than
		// DEFAULT_JAN_ADD_CHRG
		defaultStudentAdditionalChargesShouldNotBeFound("janAddChrg.lessThan=" + DEFAULT_JAN_ADD_CHRG);

		// Get all the studentAdditionalChargesList where janAddChrg is less than
		// UPDATED_JAN_ADD_CHRG
		defaultStudentAdditionalChargesShouldBeFound("janAddChrg.lessThan=" + UPDATED_JAN_ADD_CHRG);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByJanAddChrgIsGreaterThanSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where janAddChrg is greater than
		// DEFAULT_JAN_ADD_CHRG
		defaultStudentAdditionalChargesShouldNotBeFound("janAddChrg.greaterThan=" + DEFAULT_JAN_ADD_CHRG);

		// Get all the studentAdditionalChargesList where janAddChrg is greater than
		// SMALLER_JAN_ADD_CHRG
		defaultStudentAdditionalChargesShouldBeFound("janAddChrg.greaterThan=" + SMALLER_JAN_ADD_CHRG);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByFebAddChrgcIsEqualToSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where febAddChrgc equals to
		// DEFAULT_FEB_ADD_CHRGC
		defaultStudentAdditionalChargesShouldBeFound("febAddChrgc.equals=" + DEFAULT_FEB_ADD_CHRGC);

		// Get all the studentAdditionalChargesList where febAddChrgc equals to
		// UPDATED_FEB_ADD_CHRGC
		defaultStudentAdditionalChargesShouldNotBeFound("febAddChrgc.equals=" + UPDATED_FEB_ADD_CHRGC);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByFebAddChrgcIsNotEqualToSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where febAddChrgc not equals to
		// DEFAULT_FEB_ADD_CHRGC
		defaultStudentAdditionalChargesShouldNotBeFound("febAddChrgc.notEquals=" + DEFAULT_FEB_ADD_CHRGC);

		// Get all the studentAdditionalChargesList where febAddChrgc not equals to
		// UPDATED_FEB_ADD_CHRGC
		defaultStudentAdditionalChargesShouldBeFound("febAddChrgc.notEquals=" + UPDATED_FEB_ADD_CHRGC);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByFebAddChrgcIsInShouldWork() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where febAddChrgc in
		// DEFAULT_FEB_ADD_CHRGC or UPDATED_FEB_ADD_CHRGC
		defaultStudentAdditionalChargesShouldBeFound(
				"febAddChrgc.in=" + DEFAULT_FEB_ADD_CHRGC + "," + UPDATED_FEB_ADD_CHRGC);

		// Get all the studentAdditionalChargesList where febAddChrgc equals to
		// UPDATED_FEB_ADD_CHRGC
		defaultStudentAdditionalChargesShouldNotBeFound("febAddChrgc.in=" + UPDATED_FEB_ADD_CHRGC);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByFebAddChrgcIsNullOrNotNull() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where febAddChrgc is not null
		defaultStudentAdditionalChargesShouldBeFound("febAddChrgc.specified=true");

		// Get all the studentAdditionalChargesList where febAddChrgc is null
		defaultStudentAdditionalChargesShouldNotBeFound("febAddChrgc.specified=false");
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByFebAddChrgcIsGreaterThanOrEqualToSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where febAddChrgc is greater than or
		// equal to DEFAULT_FEB_ADD_CHRGC
		defaultStudentAdditionalChargesShouldBeFound("febAddChrgc.greaterThanOrEqual=" + DEFAULT_FEB_ADD_CHRGC);

		// Get all the studentAdditionalChargesList where febAddChrgc is greater than or
		// equal to UPDATED_FEB_ADD_CHRGC
		defaultStudentAdditionalChargesShouldNotBeFound("febAddChrgc.greaterThanOrEqual=" + UPDATED_FEB_ADD_CHRGC);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByFebAddChrgcIsLessThanOrEqualToSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where febAddChrgc is less than or
		// equal to DEFAULT_FEB_ADD_CHRGC
		defaultStudentAdditionalChargesShouldBeFound("febAddChrgc.lessThanOrEqual=" + DEFAULT_FEB_ADD_CHRGC);

		// Get all the studentAdditionalChargesList where febAddChrgc is less than or
		// equal to SMALLER_FEB_ADD_CHRGC
		defaultStudentAdditionalChargesShouldNotBeFound("febAddChrgc.lessThanOrEqual=" + SMALLER_FEB_ADD_CHRGC);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByFebAddChrgcIsLessThanSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where febAddChrgc is less than
		// DEFAULT_FEB_ADD_CHRGC
		defaultStudentAdditionalChargesShouldNotBeFound("febAddChrgc.lessThan=" + DEFAULT_FEB_ADD_CHRGC);

		// Get all the studentAdditionalChargesList where febAddChrgc is less than
		// UPDATED_FEB_ADD_CHRGC
		defaultStudentAdditionalChargesShouldBeFound("febAddChrgc.lessThan=" + UPDATED_FEB_ADD_CHRGC);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByFebAddChrgcIsGreaterThanSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where febAddChrgc is greater than
		// DEFAULT_FEB_ADD_CHRGC
		defaultStudentAdditionalChargesShouldNotBeFound("febAddChrgc.greaterThan=" + DEFAULT_FEB_ADD_CHRGC);

		// Get all the studentAdditionalChargesList where febAddChrgc is greater than
		// SMALLER_FEB_ADD_CHRGC
		defaultStudentAdditionalChargesShouldBeFound("febAddChrgc.greaterThan=" + SMALLER_FEB_ADD_CHRGC);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByMarAddChrgIsEqualToSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where marAddChrg equals to
		// DEFAULT_MAR_ADD_CHRG
		defaultStudentAdditionalChargesShouldBeFound("marAddChrg.equals=" + DEFAULT_MAR_ADD_CHRG);

		// Get all the studentAdditionalChargesList where marAddChrg equals to
		// UPDATED_MAR_ADD_CHRG
		defaultStudentAdditionalChargesShouldNotBeFound("marAddChrg.equals=" + UPDATED_MAR_ADD_CHRG);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByMarAddChrgIsNotEqualToSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where marAddChrg not equals to
		// DEFAULT_MAR_ADD_CHRG
		defaultStudentAdditionalChargesShouldNotBeFound("marAddChrg.notEquals=" + DEFAULT_MAR_ADD_CHRG);

		// Get all the studentAdditionalChargesList where marAddChrg not equals to
		// UPDATED_MAR_ADD_CHRG
		defaultStudentAdditionalChargesShouldBeFound("marAddChrg.notEquals=" + UPDATED_MAR_ADD_CHRG);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByMarAddChrgIsInShouldWork() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where marAddChrg in
		// DEFAULT_MAR_ADD_CHRG or UPDATED_MAR_ADD_CHRG
		defaultStudentAdditionalChargesShouldBeFound(
				"marAddChrg.in=" + DEFAULT_MAR_ADD_CHRG + "," + UPDATED_MAR_ADD_CHRG);

		// Get all the studentAdditionalChargesList where marAddChrg equals to
		// UPDATED_MAR_ADD_CHRG
		defaultStudentAdditionalChargesShouldNotBeFound("marAddChrg.in=" + UPDATED_MAR_ADD_CHRG);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByMarAddChrgIsNullOrNotNull() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where marAddChrg is not null
		defaultStudentAdditionalChargesShouldBeFound("marAddChrg.specified=true");

		// Get all the studentAdditionalChargesList where marAddChrg is null
		defaultStudentAdditionalChargesShouldNotBeFound("marAddChrg.specified=false");
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByMarAddChrgIsGreaterThanOrEqualToSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where marAddChrg is greater than or
		// equal to DEFAULT_MAR_ADD_CHRG
		defaultStudentAdditionalChargesShouldBeFound("marAddChrg.greaterThanOrEqual=" + DEFAULT_MAR_ADD_CHRG);

		// Get all the studentAdditionalChargesList where marAddChrg is greater than or
		// equal to UPDATED_MAR_ADD_CHRG
		defaultStudentAdditionalChargesShouldNotBeFound("marAddChrg.greaterThanOrEqual=" + UPDATED_MAR_ADD_CHRG);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByMarAddChrgIsLessThanOrEqualToSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where marAddChrg is less than or
		// equal to DEFAULT_MAR_ADD_CHRG
		defaultStudentAdditionalChargesShouldBeFound("marAddChrg.lessThanOrEqual=" + DEFAULT_MAR_ADD_CHRG);

		// Get all the studentAdditionalChargesList where marAddChrg is less than or
		// equal to SMALLER_MAR_ADD_CHRG
		defaultStudentAdditionalChargesShouldNotBeFound("marAddChrg.lessThanOrEqual=" + SMALLER_MAR_ADD_CHRG);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByMarAddChrgIsLessThanSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where marAddChrg is less than
		// DEFAULT_MAR_ADD_CHRG
		defaultStudentAdditionalChargesShouldNotBeFound("marAddChrg.lessThan=" + DEFAULT_MAR_ADD_CHRG);

		// Get all the studentAdditionalChargesList where marAddChrg is less than
		// UPDATED_MAR_ADD_CHRG
		defaultStudentAdditionalChargesShouldBeFound("marAddChrg.lessThan=" + UPDATED_MAR_ADD_CHRG);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByMarAddChrgIsGreaterThanSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where marAddChrg is greater than
		// DEFAULT_MAR_ADD_CHRG
		defaultStudentAdditionalChargesShouldNotBeFound("marAddChrg.greaterThan=" + DEFAULT_MAR_ADD_CHRG);

		// Get all the studentAdditionalChargesList where marAddChrg is greater than
		// SMALLER_MAR_ADD_CHRG
		defaultStudentAdditionalChargesShouldBeFound("marAddChrg.greaterThan=" + SMALLER_MAR_ADD_CHRG);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByAprAddChrgIsEqualToSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where aprAddChrg equals to
		// DEFAULT_APR_ADD_CHRG
		defaultStudentAdditionalChargesShouldBeFound("aprAddChrg.equals=" + DEFAULT_APR_ADD_CHRG);

		// Get all the studentAdditionalChargesList where aprAddChrg equals to
		// UPDATED_APR_ADD_CHRG
		defaultStudentAdditionalChargesShouldNotBeFound("aprAddChrg.equals=" + UPDATED_APR_ADD_CHRG);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByAprAddChrgIsNotEqualToSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where aprAddChrg not equals to
		// DEFAULT_APR_ADD_CHRG
		defaultStudentAdditionalChargesShouldNotBeFound("aprAddChrg.notEquals=" + DEFAULT_APR_ADD_CHRG);

		// Get all the studentAdditionalChargesList where aprAddChrg not equals to
		// UPDATED_APR_ADD_CHRG
		defaultStudentAdditionalChargesShouldBeFound("aprAddChrg.notEquals=" + UPDATED_APR_ADD_CHRG);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByAprAddChrgIsInShouldWork() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where aprAddChrg in
		// DEFAULT_APR_ADD_CHRG or UPDATED_APR_ADD_CHRG
		defaultStudentAdditionalChargesShouldBeFound(
				"aprAddChrg.in=" + DEFAULT_APR_ADD_CHRG + "," + UPDATED_APR_ADD_CHRG);

		// Get all the studentAdditionalChargesList where aprAddChrg equals to
		// UPDATED_APR_ADD_CHRG
		defaultStudentAdditionalChargesShouldNotBeFound("aprAddChrg.in=" + UPDATED_APR_ADD_CHRG);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByAprAddChrgIsNullOrNotNull() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where aprAddChrg is not null
		defaultStudentAdditionalChargesShouldBeFound("aprAddChrg.specified=true");

		// Get all the studentAdditionalChargesList where aprAddChrg is null
		defaultStudentAdditionalChargesShouldNotBeFound("aprAddChrg.specified=false");
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByAprAddChrgIsGreaterThanOrEqualToSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where aprAddChrg is greater than or
		// equal to DEFAULT_APR_ADD_CHRG
		defaultStudentAdditionalChargesShouldBeFound("aprAddChrg.greaterThanOrEqual=" + DEFAULT_APR_ADD_CHRG);

		// Get all the studentAdditionalChargesList where aprAddChrg is greater than or
		// equal to UPDATED_APR_ADD_CHRG
		defaultStudentAdditionalChargesShouldNotBeFound("aprAddChrg.greaterThanOrEqual=" + UPDATED_APR_ADD_CHRG);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByAprAddChrgIsLessThanOrEqualToSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where aprAddChrg is less than or
		// equal to DEFAULT_APR_ADD_CHRG
		defaultStudentAdditionalChargesShouldBeFound("aprAddChrg.lessThanOrEqual=" + DEFAULT_APR_ADD_CHRG);

		// Get all the studentAdditionalChargesList where aprAddChrg is less than or
		// equal to SMALLER_APR_ADD_CHRG
		defaultStudentAdditionalChargesShouldNotBeFound("aprAddChrg.lessThanOrEqual=" + SMALLER_APR_ADD_CHRG);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByAprAddChrgIsLessThanSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where aprAddChrg is less than
		// DEFAULT_APR_ADD_CHRG
		defaultStudentAdditionalChargesShouldNotBeFound("aprAddChrg.lessThan=" + DEFAULT_APR_ADD_CHRG);

		// Get all the studentAdditionalChargesList where aprAddChrg is less than
		// UPDATED_APR_ADD_CHRG
		defaultStudentAdditionalChargesShouldBeFound("aprAddChrg.lessThan=" + UPDATED_APR_ADD_CHRG);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByAprAddChrgIsGreaterThanSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where aprAddChrg is greater than
		// DEFAULT_APR_ADD_CHRG
		defaultStudentAdditionalChargesShouldNotBeFound("aprAddChrg.greaterThan=" + DEFAULT_APR_ADD_CHRG);

		// Get all the studentAdditionalChargesList where aprAddChrg is greater than
		// SMALLER_APR_ADD_CHRG
		defaultStudentAdditionalChargesShouldBeFound("aprAddChrg.greaterThan=" + SMALLER_APR_ADD_CHRG);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByMayAddChrgIsEqualToSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where mayAddChrg equals to
		// DEFAULT_MAY_ADD_CHRG
		defaultStudentAdditionalChargesShouldBeFound("mayAddChrg.equals=" + DEFAULT_MAY_ADD_CHRG);

		// Get all the studentAdditionalChargesList where mayAddChrg equals to
		// UPDATED_MAY_ADD_CHRG
		defaultStudentAdditionalChargesShouldNotBeFound("mayAddChrg.equals=" + UPDATED_MAY_ADD_CHRG);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByMayAddChrgIsNotEqualToSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where mayAddChrg not equals to
		// DEFAULT_MAY_ADD_CHRG
		defaultStudentAdditionalChargesShouldNotBeFound("mayAddChrg.notEquals=" + DEFAULT_MAY_ADD_CHRG);

		// Get all the studentAdditionalChargesList where mayAddChrg not equals to
		// UPDATED_MAY_ADD_CHRG
		defaultStudentAdditionalChargesShouldBeFound("mayAddChrg.notEquals=" + UPDATED_MAY_ADD_CHRG);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByMayAddChrgIsInShouldWork() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where mayAddChrg in
		// DEFAULT_MAY_ADD_CHRG or UPDATED_MAY_ADD_CHRG
		defaultStudentAdditionalChargesShouldBeFound(
				"mayAddChrg.in=" + DEFAULT_MAY_ADD_CHRG + "," + UPDATED_MAY_ADD_CHRG);

		// Get all the studentAdditionalChargesList where mayAddChrg equals to
		// UPDATED_MAY_ADD_CHRG
		defaultStudentAdditionalChargesShouldNotBeFound("mayAddChrg.in=" + UPDATED_MAY_ADD_CHRG);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByMayAddChrgIsNullOrNotNull() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where mayAddChrg is not null
		defaultStudentAdditionalChargesShouldBeFound("mayAddChrg.specified=true");

		// Get all the studentAdditionalChargesList where mayAddChrg is null
		defaultStudentAdditionalChargesShouldNotBeFound("mayAddChrg.specified=false");
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByMayAddChrgIsGreaterThanOrEqualToSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where mayAddChrg is greater than or
		// equal to DEFAULT_MAY_ADD_CHRG
		defaultStudentAdditionalChargesShouldBeFound("mayAddChrg.greaterThanOrEqual=" + DEFAULT_MAY_ADD_CHRG);

		// Get all the studentAdditionalChargesList where mayAddChrg is greater than or
		// equal to UPDATED_MAY_ADD_CHRG
		defaultStudentAdditionalChargesShouldNotBeFound("mayAddChrg.greaterThanOrEqual=" + UPDATED_MAY_ADD_CHRG);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByMayAddChrgIsLessThanOrEqualToSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where mayAddChrg is less than or
		// equal to DEFAULT_MAY_ADD_CHRG
		defaultStudentAdditionalChargesShouldBeFound("mayAddChrg.lessThanOrEqual=" + DEFAULT_MAY_ADD_CHRG);

		// Get all the studentAdditionalChargesList where mayAddChrg is less than or
		// equal to SMALLER_MAY_ADD_CHRG
		defaultStudentAdditionalChargesShouldNotBeFound("mayAddChrg.lessThanOrEqual=" + SMALLER_MAY_ADD_CHRG);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByMayAddChrgIsLessThanSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where mayAddChrg is less than
		// DEFAULT_MAY_ADD_CHRG
		defaultStudentAdditionalChargesShouldNotBeFound("mayAddChrg.lessThan=" + DEFAULT_MAY_ADD_CHRG);

		// Get all the studentAdditionalChargesList where mayAddChrg is less than
		// UPDATED_MAY_ADD_CHRG
		defaultStudentAdditionalChargesShouldBeFound("mayAddChrg.lessThan=" + UPDATED_MAY_ADD_CHRG);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByMayAddChrgIsGreaterThanSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where mayAddChrg is greater than
		// DEFAULT_MAY_ADD_CHRG
		defaultStudentAdditionalChargesShouldNotBeFound("mayAddChrg.greaterThan=" + DEFAULT_MAY_ADD_CHRG);

		// Get all the studentAdditionalChargesList where mayAddChrg is greater than
		// SMALLER_MAY_ADD_CHRG
		defaultStudentAdditionalChargesShouldBeFound("mayAddChrg.greaterThan=" + SMALLER_MAY_ADD_CHRG);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByJunAddChrgIsEqualToSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where junAddChrg equals to
		// DEFAULT_JUN_ADD_CHRG
		defaultStudentAdditionalChargesShouldBeFound("junAddChrg.equals=" + DEFAULT_JUN_ADD_CHRG);

		// Get all the studentAdditionalChargesList where junAddChrg equals to
		// UPDATED_JUN_ADD_CHRG
		defaultStudentAdditionalChargesShouldNotBeFound("junAddChrg.equals=" + UPDATED_JUN_ADD_CHRG);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByJunAddChrgIsNotEqualToSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where junAddChrg not equals to
		// DEFAULT_JUN_ADD_CHRG
		defaultStudentAdditionalChargesShouldNotBeFound("junAddChrg.notEquals=" + DEFAULT_JUN_ADD_CHRG);

		// Get all the studentAdditionalChargesList where junAddChrg not equals to
		// UPDATED_JUN_ADD_CHRG
		defaultStudentAdditionalChargesShouldBeFound("junAddChrg.notEquals=" + UPDATED_JUN_ADD_CHRG);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByJunAddChrgIsInShouldWork() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where junAddChrg in
		// DEFAULT_JUN_ADD_CHRG or UPDATED_JUN_ADD_CHRG
		defaultStudentAdditionalChargesShouldBeFound(
				"junAddChrg.in=" + DEFAULT_JUN_ADD_CHRG + "," + UPDATED_JUN_ADD_CHRG);

		// Get all the studentAdditionalChargesList where junAddChrg equals to
		// UPDATED_JUN_ADD_CHRG
		defaultStudentAdditionalChargesShouldNotBeFound("junAddChrg.in=" + UPDATED_JUN_ADD_CHRG);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByJunAddChrgIsNullOrNotNull() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where junAddChrg is not null
		defaultStudentAdditionalChargesShouldBeFound("junAddChrg.specified=true");

		// Get all the studentAdditionalChargesList where junAddChrg is null
		defaultStudentAdditionalChargesShouldNotBeFound("junAddChrg.specified=false");
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByJunAddChrgIsGreaterThanOrEqualToSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where junAddChrg is greater than or
		// equal to DEFAULT_JUN_ADD_CHRG
		defaultStudentAdditionalChargesShouldBeFound("junAddChrg.greaterThanOrEqual=" + DEFAULT_JUN_ADD_CHRG);

		// Get all the studentAdditionalChargesList where junAddChrg is greater than or
		// equal to UPDATED_JUN_ADD_CHRG
		defaultStudentAdditionalChargesShouldNotBeFound("junAddChrg.greaterThanOrEqual=" + UPDATED_JUN_ADD_CHRG);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByJunAddChrgIsLessThanOrEqualToSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where junAddChrg is less than or
		// equal to DEFAULT_JUN_ADD_CHRG
		defaultStudentAdditionalChargesShouldBeFound("junAddChrg.lessThanOrEqual=" + DEFAULT_JUN_ADD_CHRG);

		// Get all the studentAdditionalChargesList where junAddChrg is less than or
		// equal to SMALLER_JUN_ADD_CHRG
		defaultStudentAdditionalChargesShouldNotBeFound("junAddChrg.lessThanOrEqual=" + SMALLER_JUN_ADD_CHRG);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByJunAddChrgIsLessThanSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where junAddChrg is less than
		// DEFAULT_JUN_ADD_CHRG
		defaultStudentAdditionalChargesShouldNotBeFound("junAddChrg.lessThan=" + DEFAULT_JUN_ADD_CHRG);

		// Get all the studentAdditionalChargesList where junAddChrg is less than
		// UPDATED_JUN_ADD_CHRG
		defaultStudentAdditionalChargesShouldBeFound("junAddChrg.lessThan=" + UPDATED_JUN_ADD_CHRG);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByJunAddChrgIsGreaterThanSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where junAddChrg is greater than
		// DEFAULT_JUN_ADD_CHRG
		defaultStudentAdditionalChargesShouldNotBeFound("junAddChrg.greaterThan=" + DEFAULT_JUN_ADD_CHRG);

		// Get all the studentAdditionalChargesList where junAddChrg is greater than
		// SMALLER_JUN_ADD_CHRG
		defaultStudentAdditionalChargesShouldBeFound("junAddChrg.greaterThan=" + SMALLER_JUN_ADD_CHRG);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByJulAddChrgIsEqualToSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where julAddChrg equals to
		// DEFAULT_JUL_ADD_CHRG
		defaultStudentAdditionalChargesShouldBeFound("julAddChrg.equals=" + DEFAULT_JUL_ADD_CHRG);

		// Get all the studentAdditionalChargesList where julAddChrg equals to
		// UPDATED_JUL_ADD_CHRG
		defaultStudentAdditionalChargesShouldNotBeFound("julAddChrg.equals=" + UPDATED_JUL_ADD_CHRG);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByJulAddChrgIsNotEqualToSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where julAddChrg not equals to
		// DEFAULT_JUL_ADD_CHRG
		defaultStudentAdditionalChargesShouldNotBeFound("julAddChrg.notEquals=" + DEFAULT_JUL_ADD_CHRG);

		// Get all the studentAdditionalChargesList where julAddChrg not equals to
		// UPDATED_JUL_ADD_CHRG
		defaultStudentAdditionalChargesShouldBeFound("julAddChrg.notEquals=" + UPDATED_JUL_ADD_CHRG);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByJulAddChrgIsInShouldWork() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where julAddChrg in
		// DEFAULT_JUL_ADD_CHRG or UPDATED_JUL_ADD_CHRG
		defaultStudentAdditionalChargesShouldBeFound(
				"julAddChrg.in=" + DEFAULT_JUL_ADD_CHRG + "," + UPDATED_JUL_ADD_CHRG);

		// Get all the studentAdditionalChargesList where julAddChrg equals to
		// UPDATED_JUL_ADD_CHRG
		defaultStudentAdditionalChargesShouldNotBeFound("julAddChrg.in=" + UPDATED_JUL_ADD_CHRG);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByJulAddChrgIsNullOrNotNull() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where julAddChrg is not null
		defaultStudentAdditionalChargesShouldBeFound("julAddChrg.specified=true");

		// Get all the studentAdditionalChargesList where julAddChrg is null
		defaultStudentAdditionalChargesShouldNotBeFound("julAddChrg.specified=false");
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByJulAddChrgIsGreaterThanOrEqualToSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where julAddChrg is greater than or
		// equal to DEFAULT_JUL_ADD_CHRG
		defaultStudentAdditionalChargesShouldBeFound("julAddChrg.greaterThanOrEqual=" + DEFAULT_JUL_ADD_CHRG);

		// Get all the studentAdditionalChargesList where julAddChrg is greater than or
		// equal to UPDATED_JUL_ADD_CHRG
		defaultStudentAdditionalChargesShouldNotBeFound("julAddChrg.greaterThanOrEqual=" + UPDATED_JUL_ADD_CHRG);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByJulAddChrgIsLessThanOrEqualToSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where julAddChrg is less than or
		// equal to DEFAULT_JUL_ADD_CHRG
		defaultStudentAdditionalChargesShouldBeFound("julAddChrg.lessThanOrEqual=" + DEFAULT_JUL_ADD_CHRG);

		// Get all the studentAdditionalChargesList where julAddChrg is less than or
		// equal to SMALLER_JUL_ADD_CHRG
		defaultStudentAdditionalChargesShouldNotBeFound("julAddChrg.lessThanOrEqual=" + SMALLER_JUL_ADD_CHRG);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByJulAddChrgIsLessThanSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where julAddChrg is less than
		// DEFAULT_JUL_ADD_CHRG
		defaultStudentAdditionalChargesShouldNotBeFound("julAddChrg.lessThan=" + DEFAULT_JUL_ADD_CHRG);

		// Get all the studentAdditionalChargesList where julAddChrg is less than
		// UPDATED_JUL_ADD_CHRG
		defaultStudentAdditionalChargesShouldBeFound("julAddChrg.lessThan=" + UPDATED_JUL_ADD_CHRG);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByJulAddChrgIsGreaterThanSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where julAddChrg is greater than
		// DEFAULT_JUL_ADD_CHRG
		defaultStudentAdditionalChargesShouldNotBeFound("julAddChrg.greaterThan=" + DEFAULT_JUL_ADD_CHRG);

		// Get all the studentAdditionalChargesList where julAddChrg is greater than
		// SMALLER_JUL_ADD_CHRG
		defaultStudentAdditionalChargesShouldBeFound("julAddChrg.greaterThan=" + SMALLER_JUL_ADD_CHRG);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByAugAddChrgIsEqualToSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where augAddChrg equals to
		// DEFAULT_AUG_ADD_CHRG
		defaultStudentAdditionalChargesShouldBeFound("augAddChrg.equals=" + DEFAULT_AUG_ADD_CHRG);

		// Get all the studentAdditionalChargesList where augAddChrg equals to
		// UPDATED_AUG_ADD_CHRG
		defaultStudentAdditionalChargesShouldNotBeFound("augAddChrg.equals=" + UPDATED_AUG_ADD_CHRG);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByAugAddChrgIsNotEqualToSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where augAddChrg not equals to
		// DEFAULT_AUG_ADD_CHRG
		defaultStudentAdditionalChargesShouldNotBeFound("augAddChrg.notEquals=" + DEFAULT_AUG_ADD_CHRG);

		// Get all the studentAdditionalChargesList where augAddChrg not equals to
		// UPDATED_AUG_ADD_CHRG
		defaultStudentAdditionalChargesShouldBeFound("augAddChrg.notEquals=" + UPDATED_AUG_ADD_CHRG);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByAugAddChrgIsInShouldWork() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where augAddChrg in
		// DEFAULT_AUG_ADD_CHRG or UPDATED_AUG_ADD_CHRG
		defaultStudentAdditionalChargesShouldBeFound(
				"augAddChrg.in=" + DEFAULT_AUG_ADD_CHRG + "," + UPDATED_AUG_ADD_CHRG);

		// Get all the studentAdditionalChargesList where augAddChrg equals to
		// UPDATED_AUG_ADD_CHRG
		defaultStudentAdditionalChargesShouldNotBeFound("augAddChrg.in=" + UPDATED_AUG_ADD_CHRG);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByAugAddChrgIsNullOrNotNull() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where augAddChrg is not null
		defaultStudentAdditionalChargesShouldBeFound("augAddChrg.specified=true");

		// Get all the studentAdditionalChargesList where augAddChrg is null
		defaultStudentAdditionalChargesShouldNotBeFound("augAddChrg.specified=false");
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByAugAddChrgIsGreaterThanOrEqualToSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where augAddChrg is greater than or
		// equal to DEFAULT_AUG_ADD_CHRG
		defaultStudentAdditionalChargesShouldBeFound("augAddChrg.greaterThanOrEqual=" + DEFAULT_AUG_ADD_CHRG);

		// Get all the studentAdditionalChargesList where augAddChrg is greater than or
		// equal to UPDATED_AUG_ADD_CHRG
		defaultStudentAdditionalChargesShouldNotBeFound("augAddChrg.greaterThanOrEqual=" + UPDATED_AUG_ADD_CHRG);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByAugAddChrgIsLessThanOrEqualToSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where augAddChrg is less than or
		// equal to DEFAULT_AUG_ADD_CHRG
		defaultStudentAdditionalChargesShouldBeFound("augAddChrg.lessThanOrEqual=" + DEFAULT_AUG_ADD_CHRG);

		// Get all the studentAdditionalChargesList where augAddChrg is less than or
		// equal to SMALLER_AUG_ADD_CHRG
		defaultStudentAdditionalChargesShouldNotBeFound("augAddChrg.lessThanOrEqual=" + SMALLER_AUG_ADD_CHRG);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByAugAddChrgIsLessThanSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where augAddChrg is less than
		// DEFAULT_AUG_ADD_CHRG
		defaultStudentAdditionalChargesShouldNotBeFound("augAddChrg.lessThan=" + DEFAULT_AUG_ADD_CHRG);

		// Get all the studentAdditionalChargesList where augAddChrg is less than
		// UPDATED_AUG_ADD_CHRG
		defaultStudentAdditionalChargesShouldBeFound("augAddChrg.lessThan=" + UPDATED_AUG_ADD_CHRG);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByAugAddChrgIsGreaterThanSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where augAddChrg is greater than
		// DEFAULT_AUG_ADD_CHRG
		defaultStudentAdditionalChargesShouldNotBeFound("augAddChrg.greaterThan=" + DEFAULT_AUG_ADD_CHRG);

		// Get all the studentAdditionalChargesList where augAddChrg is greater than
		// SMALLER_AUG_ADD_CHRG
		defaultStudentAdditionalChargesShouldBeFound("augAddChrg.greaterThan=" + SMALLER_AUG_ADD_CHRG);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesBySepAddChrgIsEqualToSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where sepAddChrg equals to
		// DEFAULT_SEP_ADD_CHRG
		defaultStudentAdditionalChargesShouldBeFound("sepAddChrg.equals=" + DEFAULT_SEP_ADD_CHRG);

		// Get all the studentAdditionalChargesList where sepAddChrg equals to
		// UPDATED_SEP_ADD_CHRG
		defaultStudentAdditionalChargesShouldNotBeFound("sepAddChrg.equals=" + UPDATED_SEP_ADD_CHRG);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesBySepAddChrgIsNotEqualToSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where sepAddChrg not equals to
		// DEFAULT_SEP_ADD_CHRG
		defaultStudentAdditionalChargesShouldNotBeFound("sepAddChrg.notEquals=" + DEFAULT_SEP_ADD_CHRG);

		// Get all the studentAdditionalChargesList where sepAddChrg not equals to
		// UPDATED_SEP_ADD_CHRG
		defaultStudentAdditionalChargesShouldBeFound("sepAddChrg.notEquals=" + UPDATED_SEP_ADD_CHRG);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesBySepAddChrgIsInShouldWork() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where sepAddChrg in
		// DEFAULT_SEP_ADD_CHRG or UPDATED_SEP_ADD_CHRG
		defaultStudentAdditionalChargesShouldBeFound(
				"sepAddChrg.in=" + DEFAULT_SEP_ADD_CHRG + "," + UPDATED_SEP_ADD_CHRG);

		// Get all the studentAdditionalChargesList where sepAddChrg equals to
		// UPDATED_SEP_ADD_CHRG
		defaultStudentAdditionalChargesShouldNotBeFound("sepAddChrg.in=" + UPDATED_SEP_ADD_CHRG);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesBySepAddChrgIsNullOrNotNull() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where sepAddChrg is not null
		defaultStudentAdditionalChargesShouldBeFound("sepAddChrg.specified=true");

		// Get all the studentAdditionalChargesList where sepAddChrg is null
		defaultStudentAdditionalChargesShouldNotBeFound("sepAddChrg.specified=false");
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesBySepAddChrgIsGreaterThanOrEqualToSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where sepAddChrg is greater than or
		// equal to DEFAULT_SEP_ADD_CHRG
		defaultStudentAdditionalChargesShouldBeFound("sepAddChrg.greaterThanOrEqual=" + DEFAULT_SEP_ADD_CHRG);

		// Get all the studentAdditionalChargesList where sepAddChrg is greater than or
		// equal to UPDATED_SEP_ADD_CHRG
		defaultStudentAdditionalChargesShouldNotBeFound("sepAddChrg.greaterThanOrEqual=" + UPDATED_SEP_ADD_CHRG);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesBySepAddChrgIsLessThanOrEqualToSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where sepAddChrg is less than or
		// equal to DEFAULT_SEP_ADD_CHRG
		defaultStudentAdditionalChargesShouldBeFound("sepAddChrg.lessThanOrEqual=" + DEFAULT_SEP_ADD_CHRG);

		// Get all the studentAdditionalChargesList where sepAddChrg is less than or
		// equal to SMALLER_SEP_ADD_CHRG
		defaultStudentAdditionalChargesShouldNotBeFound("sepAddChrg.lessThanOrEqual=" + SMALLER_SEP_ADD_CHRG);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesBySepAddChrgIsLessThanSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where sepAddChrg is less than
		// DEFAULT_SEP_ADD_CHRG
		defaultStudentAdditionalChargesShouldNotBeFound("sepAddChrg.lessThan=" + DEFAULT_SEP_ADD_CHRG);

		// Get all the studentAdditionalChargesList where sepAddChrg is less than
		// UPDATED_SEP_ADD_CHRG
		defaultStudentAdditionalChargesShouldBeFound("sepAddChrg.lessThan=" + UPDATED_SEP_ADD_CHRG);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesBySepAddChrgIsGreaterThanSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where sepAddChrg is greater than
		// DEFAULT_SEP_ADD_CHRG
		defaultStudentAdditionalChargesShouldNotBeFound("sepAddChrg.greaterThan=" + DEFAULT_SEP_ADD_CHRG);

		// Get all the studentAdditionalChargesList where sepAddChrg is greater than
		// SMALLER_SEP_ADD_CHRG
		defaultStudentAdditionalChargesShouldBeFound("sepAddChrg.greaterThan=" + SMALLER_SEP_ADD_CHRG);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByOctAddChrgIsEqualToSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where octAddChrg equals to
		// DEFAULT_OCT_ADD_CHRG
		defaultStudentAdditionalChargesShouldBeFound("octAddChrg.equals=" + DEFAULT_OCT_ADD_CHRG);

		// Get all the studentAdditionalChargesList where octAddChrg equals to
		// UPDATED_OCT_ADD_CHRG
		defaultStudentAdditionalChargesShouldNotBeFound("octAddChrg.equals=" + UPDATED_OCT_ADD_CHRG);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByOctAddChrgIsNotEqualToSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where octAddChrg not equals to
		// DEFAULT_OCT_ADD_CHRG
		defaultStudentAdditionalChargesShouldNotBeFound("octAddChrg.notEquals=" + DEFAULT_OCT_ADD_CHRG);

		// Get all the studentAdditionalChargesList where octAddChrg not equals to
		// UPDATED_OCT_ADD_CHRG
		defaultStudentAdditionalChargesShouldBeFound("octAddChrg.notEquals=" + UPDATED_OCT_ADD_CHRG);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByOctAddChrgIsInShouldWork() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where octAddChrg in
		// DEFAULT_OCT_ADD_CHRG or UPDATED_OCT_ADD_CHRG
		defaultStudentAdditionalChargesShouldBeFound(
				"octAddChrg.in=" + DEFAULT_OCT_ADD_CHRG + "," + UPDATED_OCT_ADD_CHRG);

		// Get all the studentAdditionalChargesList where octAddChrg equals to
		// UPDATED_OCT_ADD_CHRG
		defaultStudentAdditionalChargesShouldNotBeFound("octAddChrg.in=" + UPDATED_OCT_ADD_CHRG);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByOctAddChrgIsNullOrNotNull() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where octAddChrg is not null
		defaultStudentAdditionalChargesShouldBeFound("octAddChrg.specified=true");

		// Get all the studentAdditionalChargesList where octAddChrg is null
		defaultStudentAdditionalChargesShouldNotBeFound("octAddChrg.specified=false");
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByOctAddChrgIsGreaterThanOrEqualToSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where octAddChrg is greater than or
		// equal to DEFAULT_OCT_ADD_CHRG
		defaultStudentAdditionalChargesShouldBeFound("octAddChrg.greaterThanOrEqual=" + DEFAULT_OCT_ADD_CHRG);

		// Get all the studentAdditionalChargesList where octAddChrg is greater than or
		// equal to UPDATED_OCT_ADD_CHRG
		defaultStudentAdditionalChargesShouldNotBeFound("octAddChrg.greaterThanOrEqual=" + UPDATED_OCT_ADD_CHRG);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByOctAddChrgIsLessThanOrEqualToSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where octAddChrg is less than or
		// equal to DEFAULT_OCT_ADD_CHRG
		defaultStudentAdditionalChargesShouldBeFound("octAddChrg.lessThanOrEqual=" + DEFAULT_OCT_ADD_CHRG);

		// Get all the studentAdditionalChargesList where octAddChrg is less than or
		// equal to SMALLER_OCT_ADD_CHRG
		defaultStudentAdditionalChargesShouldNotBeFound("octAddChrg.lessThanOrEqual=" + SMALLER_OCT_ADD_CHRG);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByOctAddChrgIsLessThanSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where octAddChrg is less than
		// DEFAULT_OCT_ADD_CHRG
		defaultStudentAdditionalChargesShouldNotBeFound("octAddChrg.lessThan=" + DEFAULT_OCT_ADD_CHRG);

		// Get all the studentAdditionalChargesList where octAddChrg is less than
		// UPDATED_OCT_ADD_CHRG
		defaultStudentAdditionalChargesShouldBeFound("octAddChrg.lessThan=" + UPDATED_OCT_ADD_CHRG);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByOctAddChrgIsGreaterThanSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where octAddChrg is greater than
		// DEFAULT_OCT_ADD_CHRG
		defaultStudentAdditionalChargesShouldNotBeFound("octAddChrg.greaterThan=" + DEFAULT_OCT_ADD_CHRG);

		// Get all the studentAdditionalChargesList where octAddChrg is greater than
		// SMALLER_OCT_ADD_CHRG
		defaultStudentAdditionalChargesShouldBeFound("octAddChrg.greaterThan=" + SMALLER_OCT_ADD_CHRG);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByNovAddChrgIsEqualToSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where novAddChrg equals to
		// DEFAULT_NOV_ADD_CHRG
		defaultStudentAdditionalChargesShouldBeFound("novAddChrg.equals=" + DEFAULT_NOV_ADD_CHRG);

		// Get all the studentAdditionalChargesList where novAddChrg equals to
		// UPDATED_NOV_ADD_CHRG
		defaultStudentAdditionalChargesShouldNotBeFound("novAddChrg.equals=" + UPDATED_NOV_ADD_CHRG);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByNovAddChrgIsNotEqualToSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where novAddChrg not equals to
		// DEFAULT_NOV_ADD_CHRG
		defaultStudentAdditionalChargesShouldNotBeFound("novAddChrg.notEquals=" + DEFAULT_NOV_ADD_CHRG);

		// Get all the studentAdditionalChargesList where novAddChrg not equals to
		// UPDATED_NOV_ADD_CHRG
		defaultStudentAdditionalChargesShouldBeFound("novAddChrg.notEquals=" + UPDATED_NOV_ADD_CHRG);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByNovAddChrgIsInShouldWork() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where novAddChrg in
		// DEFAULT_NOV_ADD_CHRG or UPDATED_NOV_ADD_CHRG
		defaultStudentAdditionalChargesShouldBeFound(
				"novAddChrg.in=" + DEFAULT_NOV_ADD_CHRG + "," + UPDATED_NOV_ADD_CHRG);

		// Get all the studentAdditionalChargesList where novAddChrg equals to
		// UPDATED_NOV_ADD_CHRG
		defaultStudentAdditionalChargesShouldNotBeFound("novAddChrg.in=" + UPDATED_NOV_ADD_CHRG);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByNovAddChrgIsNullOrNotNull() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where novAddChrg is not null
		defaultStudentAdditionalChargesShouldBeFound("novAddChrg.specified=true");

		// Get all the studentAdditionalChargesList where novAddChrg is null
		defaultStudentAdditionalChargesShouldNotBeFound("novAddChrg.specified=false");
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByNovAddChrgIsGreaterThanOrEqualToSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where novAddChrg is greater than or
		// equal to DEFAULT_NOV_ADD_CHRG
		defaultStudentAdditionalChargesShouldBeFound("novAddChrg.greaterThanOrEqual=" + DEFAULT_NOV_ADD_CHRG);

		// Get all the studentAdditionalChargesList where novAddChrg is greater than or
		// equal to UPDATED_NOV_ADD_CHRG
		defaultStudentAdditionalChargesShouldNotBeFound("novAddChrg.greaterThanOrEqual=" + UPDATED_NOV_ADD_CHRG);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByNovAddChrgIsLessThanOrEqualToSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where novAddChrg is less than or
		// equal to DEFAULT_NOV_ADD_CHRG
		defaultStudentAdditionalChargesShouldBeFound("novAddChrg.lessThanOrEqual=" + DEFAULT_NOV_ADD_CHRG);

		// Get all the studentAdditionalChargesList where novAddChrg is less than or
		// equal to SMALLER_NOV_ADD_CHRG
		defaultStudentAdditionalChargesShouldNotBeFound("novAddChrg.lessThanOrEqual=" + SMALLER_NOV_ADD_CHRG);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByNovAddChrgIsLessThanSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where novAddChrg is less than
		// DEFAULT_NOV_ADD_CHRG
		defaultStudentAdditionalChargesShouldNotBeFound("novAddChrg.lessThan=" + DEFAULT_NOV_ADD_CHRG);

		// Get all the studentAdditionalChargesList where novAddChrg is less than
		// UPDATED_NOV_ADD_CHRG
		defaultStudentAdditionalChargesShouldBeFound("novAddChrg.lessThan=" + UPDATED_NOV_ADD_CHRG);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByNovAddChrgIsGreaterThanSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where novAddChrg is greater than
		// DEFAULT_NOV_ADD_CHRG
		defaultStudentAdditionalChargesShouldNotBeFound("novAddChrg.greaterThan=" + DEFAULT_NOV_ADD_CHRG);

		// Get all the studentAdditionalChargesList where novAddChrg is greater than
		// SMALLER_NOV_ADD_CHRG
		defaultStudentAdditionalChargesShouldBeFound("novAddChrg.greaterThan=" + SMALLER_NOV_ADD_CHRG);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByDecAddChrgIsEqualToSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where decAddChrg equals to
		// DEFAULT_DEC_ADD_CHRG
		defaultStudentAdditionalChargesShouldBeFound("decAddChrg.equals=" + DEFAULT_DEC_ADD_CHRG);

		// Get all the studentAdditionalChargesList where decAddChrg equals to
		// UPDATED_DEC_ADD_CHRG
		defaultStudentAdditionalChargesShouldNotBeFound("decAddChrg.equals=" + UPDATED_DEC_ADD_CHRG);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByDecAddChrgIsNotEqualToSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where decAddChrg not equals to
		// DEFAULT_DEC_ADD_CHRG
		defaultStudentAdditionalChargesShouldNotBeFound("decAddChrg.notEquals=" + DEFAULT_DEC_ADD_CHRG);

		// Get all the studentAdditionalChargesList where decAddChrg not equals to
		// UPDATED_DEC_ADD_CHRG
		defaultStudentAdditionalChargesShouldBeFound("decAddChrg.notEquals=" + UPDATED_DEC_ADD_CHRG);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByDecAddChrgIsInShouldWork() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where decAddChrg in
		// DEFAULT_DEC_ADD_CHRG or UPDATED_DEC_ADD_CHRG
		defaultStudentAdditionalChargesShouldBeFound(
				"decAddChrg.in=" + DEFAULT_DEC_ADD_CHRG + "," + UPDATED_DEC_ADD_CHRG);

		// Get all the studentAdditionalChargesList where decAddChrg equals to
		// UPDATED_DEC_ADD_CHRG
		defaultStudentAdditionalChargesShouldNotBeFound("decAddChrg.in=" + UPDATED_DEC_ADD_CHRG);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByDecAddChrgIsNullOrNotNull() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where decAddChrg is not null
		defaultStudentAdditionalChargesShouldBeFound("decAddChrg.specified=true");

		// Get all the studentAdditionalChargesList where decAddChrg is null
		defaultStudentAdditionalChargesShouldNotBeFound("decAddChrg.specified=false");
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByDecAddChrgIsGreaterThanOrEqualToSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where decAddChrg is greater than or
		// equal to DEFAULT_DEC_ADD_CHRG
		defaultStudentAdditionalChargesShouldBeFound("decAddChrg.greaterThanOrEqual=" + DEFAULT_DEC_ADD_CHRG);

		// Get all the studentAdditionalChargesList where decAddChrg is greater than or
		// equal to UPDATED_DEC_ADD_CHRG
		defaultStudentAdditionalChargesShouldNotBeFound("decAddChrg.greaterThanOrEqual=" + UPDATED_DEC_ADD_CHRG);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByDecAddChrgIsLessThanOrEqualToSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where decAddChrg is less than or
		// equal to DEFAULT_DEC_ADD_CHRG
		defaultStudentAdditionalChargesShouldBeFound("decAddChrg.lessThanOrEqual=" + DEFAULT_DEC_ADD_CHRG);

		// Get all the studentAdditionalChargesList where decAddChrg is less than or
		// equal to SMALLER_DEC_ADD_CHRG
		defaultStudentAdditionalChargesShouldNotBeFound("decAddChrg.lessThanOrEqual=" + SMALLER_DEC_ADD_CHRG);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByDecAddChrgIsLessThanSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where decAddChrg is less than
		// DEFAULT_DEC_ADD_CHRG
		defaultStudentAdditionalChargesShouldNotBeFound("decAddChrg.lessThan=" + DEFAULT_DEC_ADD_CHRG);

		// Get all the studentAdditionalChargesList where decAddChrg is less than
		// UPDATED_DEC_ADD_CHRG
		defaultStudentAdditionalChargesShouldBeFound("decAddChrg.lessThan=" + UPDATED_DEC_ADD_CHRG);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByDecAddChrgIsGreaterThanSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where decAddChrg is greater than
		// DEFAULT_DEC_ADD_CHRG
		defaultStudentAdditionalChargesShouldNotBeFound("decAddChrg.greaterThan=" + DEFAULT_DEC_ADD_CHRG);

		// Get all the studentAdditionalChargesList where decAddChrg is greater than
		// SMALLER_DEC_ADD_CHRG
		defaultStudentAdditionalChargesShouldBeFound("decAddChrg.greaterThan=" + SMALLER_DEC_ADD_CHRG);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByCreateDateIsEqualToSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where createDate equals to
		// DEFAULT_CREATE_DATE
		defaultStudentAdditionalChargesShouldBeFound("createDate.equals=" + DEFAULT_CREATE_DATE);

		// Get all the studentAdditionalChargesList where createDate equals to
		// UPDATED_CREATE_DATE
		defaultStudentAdditionalChargesShouldNotBeFound("createDate.equals=" + UPDATED_CREATE_DATE);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByCreateDateIsNotEqualToSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where createDate not equals to
		// DEFAULT_CREATE_DATE
		defaultStudentAdditionalChargesShouldNotBeFound("createDate.notEquals=" + DEFAULT_CREATE_DATE);

		// Get all the studentAdditionalChargesList where createDate not equals to
		// UPDATED_CREATE_DATE
		defaultStudentAdditionalChargesShouldBeFound("createDate.notEquals=" + UPDATED_CREATE_DATE);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByCreateDateIsInShouldWork() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where createDate in
		// DEFAULT_CREATE_DATE or UPDATED_CREATE_DATE
		defaultStudentAdditionalChargesShouldBeFound(
				"createDate.in=" + DEFAULT_CREATE_DATE + "," + UPDATED_CREATE_DATE);

		// Get all the studentAdditionalChargesList where createDate equals to
		// UPDATED_CREATE_DATE
		defaultStudentAdditionalChargesShouldNotBeFound("createDate.in=" + UPDATED_CREATE_DATE);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByCreateDateIsNullOrNotNull() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where createDate is not null
		defaultStudentAdditionalChargesShouldBeFound("createDate.specified=true");

		// Get all the studentAdditionalChargesList where createDate is null
		defaultStudentAdditionalChargesShouldNotBeFound("createDate.specified=false");
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByCreateDateIsGreaterThanOrEqualToSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where createDate is greater than or
		// equal to DEFAULT_CREATE_DATE
		defaultStudentAdditionalChargesShouldBeFound("createDate.greaterThanOrEqual=" + DEFAULT_CREATE_DATE);

		// Get all the studentAdditionalChargesList where createDate is greater than or
		// equal to UPDATED_CREATE_DATE
		defaultStudentAdditionalChargesShouldNotBeFound("createDate.greaterThanOrEqual=" + UPDATED_CREATE_DATE);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByCreateDateIsLessThanOrEqualToSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where createDate is less than or
		// equal to DEFAULT_CREATE_DATE
		defaultStudentAdditionalChargesShouldBeFound("createDate.lessThanOrEqual=" + DEFAULT_CREATE_DATE);

		// Get all the studentAdditionalChargesList where createDate is less than or
		// equal to SMALLER_CREATE_DATE
		defaultStudentAdditionalChargesShouldNotBeFound("createDate.lessThanOrEqual=" + SMALLER_CREATE_DATE);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByCreateDateIsLessThanSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where createDate is less than
		// DEFAULT_CREATE_DATE
		defaultStudentAdditionalChargesShouldNotBeFound("createDate.lessThan=" + DEFAULT_CREATE_DATE);

		// Get all the studentAdditionalChargesList where createDate is less than
		// UPDATED_CREATE_DATE
		defaultStudentAdditionalChargesShouldBeFound("createDate.lessThan=" + UPDATED_CREATE_DATE);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByCreateDateIsGreaterThanSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where createDate is greater than
		// DEFAULT_CREATE_DATE
		defaultStudentAdditionalChargesShouldNotBeFound("createDate.greaterThan=" + DEFAULT_CREATE_DATE);

		// Get all the studentAdditionalChargesList where createDate is greater than
		// SMALLER_CREATE_DATE
		defaultStudentAdditionalChargesShouldBeFound("createDate.greaterThan=" + SMALLER_CREATE_DATE);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByLastModifiedIsEqualToSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where lastModified equals to
		// DEFAULT_LAST_MODIFIED
		defaultStudentAdditionalChargesShouldBeFound("lastModified.equals=" + DEFAULT_LAST_MODIFIED);

		// Get all the studentAdditionalChargesList where lastModified equals to
		// UPDATED_LAST_MODIFIED
		defaultStudentAdditionalChargesShouldNotBeFound("lastModified.equals=" + UPDATED_LAST_MODIFIED);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByLastModifiedIsNotEqualToSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where lastModified not equals to
		// DEFAULT_LAST_MODIFIED
		defaultStudentAdditionalChargesShouldNotBeFound("lastModified.notEquals=" + DEFAULT_LAST_MODIFIED);

		// Get all the studentAdditionalChargesList where lastModified not equals to
		// UPDATED_LAST_MODIFIED
		defaultStudentAdditionalChargesShouldBeFound("lastModified.notEquals=" + UPDATED_LAST_MODIFIED);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByLastModifiedIsInShouldWork() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where lastModified in
		// DEFAULT_LAST_MODIFIED or UPDATED_LAST_MODIFIED
		defaultStudentAdditionalChargesShouldBeFound(
				"lastModified.in=" + DEFAULT_LAST_MODIFIED + "," + UPDATED_LAST_MODIFIED);

		// Get all the studentAdditionalChargesList where lastModified equals to
		// UPDATED_LAST_MODIFIED
		defaultStudentAdditionalChargesShouldNotBeFound("lastModified.in=" + UPDATED_LAST_MODIFIED);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByLastModifiedIsNullOrNotNull() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where lastModified is not null
		defaultStudentAdditionalChargesShouldBeFound("lastModified.specified=true");

		// Get all the studentAdditionalChargesList where lastModified is null
		defaultStudentAdditionalChargesShouldNotBeFound("lastModified.specified=false");
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByLastModifiedIsGreaterThanOrEqualToSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where lastModified is greater than
		// or equal to DEFAULT_LAST_MODIFIED
		defaultStudentAdditionalChargesShouldBeFound("lastModified.greaterThanOrEqual=" + DEFAULT_LAST_MODIFIED);

		// Get all the studentAdditionalChargesList where lastModified is greater than
		// or equal to UPDATED_LAST_MODIFIED
		defaultStudentAdditionalChargesShouldNotBeFound("lastModified.greaterThanOrEqual=" + UPDATED_LAST_MODIFIED);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByLastModifiedIsLessThanOrEqualToSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where lastModified is less than or
		// equal to DEFAULT_LAST_MODIFIED
		defaultStudentAdditionalChargesShouldBeFound("lastModified.lessThanOrEqual=" + DEFAULT_LAST_MODIFIED);

		// Get all the studentAdditionalChargesList where lastModified is less than or
		// equal to SMALLER_LAST_MODIFIED
		defaultStudentAdditionalChargesShouldNotBeFound("lastModified.lessThanOrEqual=" + SMALLER_LAST_MODIFIED);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByLastModifiedIsLessThanSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where lastModified is less than
		// DEFAULT_LAST_MODIFIED
		defaultStudentAdditionalChargesShouldNotBeFound("lastModified.lessThan=" + DEFAULT_LAST_MODIFIED);

		// Get all the studentAdditionalChargesList where lastModified is less than
		// UPDATED_LAST_MODIFIED
		defaultStudentAdditionalChargesShouldBeFound("lastModified.lessThan=" + UPDATED_LAST_MODIFIED);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByLastModifiedIsGreaterThanSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where lastModified is greater than
		// DEFAULT_LAST_MODIFIED
		defaultStudentAdditionalChargesShouldNotBeFound("lastModified.greaterThan=" + DEFAULT_LAST_MODIFIED);

		// Get all the studentAdditionalChargesList where lastModified is greater than
		// SMALLER_LAST_MODIFIED
		defaultStudentAdditionalChargesShouldBeFound("lastModified.greaterThan=" + SMALLER_LAST_MODIFIED);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByCancelDateIsEqualToSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where cancelDate equals to
		// DEFAULT_CANCEL_DATE
		defaultStudentAdditionalChargesShouldBeFound("cancelDate.equals=" + DEFAULT_CANCEL_DATE);

		// Get all the studentAdditionalChargesList where cancelDate equals to
		// UPDATED_CANCEL_DATE
		defaultStudentAdditionalChargesShouldNotBeFound("cancelDate.equals=" + UPDATED_CANCEL_DATE);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByCancelDateIsNotEqualToSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where cancelDate not equals to
		// DEFAULT_CANCEL_DATE
		defaultStudentAdditionalChargesShouldNotBeFound("cancelDate.notEquals=" + DEFAULT_CANCEL_DATE);

		// Get all the studentAdditionalChargesList where cancelDate not equals to
		// UPDATED_CANCEL_DATE
		defaultStudentAdditionalChargesShouldBeFound("cancelDate.notEquals=" + UPDATED_CANCEL_DATE);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByCancelDateIsInShouldWork() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where cancelDate in
		// DEFAULT_CANCEL_DATE or UPDATED_CANCEL_DATE
		defaultStudentAdditionalChargesShouldBeFound(
				"cancelDate.in=" + DEFAULT_CANCEL_DATE + "," + UPDATED_CANCEL_DATE);

		// Get all the studentAdditionalChargesList where cancelDate equals to
		// UPDATED_CANCEL_DATE
		defaultStudentAdditionalChargesShouldNotBeFound("cancelDate.in=" + UPDATED_CANCEL_DATE);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByCancelDateIsNullOrNotNull() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where cancelDate is not null
		defaultStudentAdditionalChargesShouldBeFound("cancelDate.specified=true");

		// Get all the studentAdditionalChargesList where cancelDate is null
		defaultStudentAdditionalChargesShouldNotBeFound("cancelDate.specified=false");
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByCancelDateIsGreaterThanOrEqualToSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where cancelDate is greater than or
		// equal to DEFAULT_CANCEL_DATE
		defaultStudentAdditionalChargesShouldBeFound("cancelDate.greaterThanOrEqual=" + DEFAULT_CANCEL_DATE);

		// Get all the studentAdditionalChargesList where cancelDate is greater than or
		// equal to UPDATED_CANCEL_DATE
		defaultStudentAdditionalChargesShouldNotBeFound("cancelDate.greaterThanOrEqual=" + UPDATED_CANCEL_DATE);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByCancelDateIsLessThanOrEqualToSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where cancelDate is less than or
		// equal to DEFAULT_CANCEL_DATE
		defaultStudentAdditionalChargesShouldBeFound("cancelDate.lessThanOrEqual=" + DEFAULT_CANCEL_DATE);

		// Get all the studentAdditionalChargesList where cancelDate is less than or
		// equal to SMALLER_CANCEL_DATE
		defaultStudentAdditionalChargesShouldNotBeFound("cancelDate.lessThanOrEqual=" + SMALLER_CANCEL_DATE);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByCancelDateIsLessThanSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where cancelDate is less than
		// DEFAULT_CANCEL_DATE
		defaultStudentAdditionalChargesShouldNotBeFound("cancelDate.lessThan=" + DEFAULT_CANCEL_DATE);

		// Get all the studentAdditionalChargesList where cancelDate is less than
		// UPDATED_CANCEL_DATE
		defaultStudentAdditionalChargesShouldBeFound("cancelDate.lessThan=" + UPDATED_CANCEL_DATE);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByCancelDateIsGreaterThanSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		// Get all the studentAdditionalChargesList where cancelDate is greater than
		// DEFAULT_CANCEL_DATE
		defaultStudentAdditionalChargesShouldNotBeFound("cancelDate.greaterThan=" + DEFAULT_CANCEL_DATE);

		// Get all the studentAdditionalChargesList where cancelDate is greater than
		// SMALLER_CANCEL_DATE
		defaultStudentAdditionalChargesShouldBeFound("cancelDate.greaterThan=" + SMALLER_CANCEL_DATE);
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesBySchoolLedgerHeadIsEqualToSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);
		SchoolLedgerHead schoolLedgerHead = SchoolLedgerHeadResourceIT.createEntity(em);
		em.persist(schoolLedgerHead);
		em.flush();
		studentAdditionalCharges.setSchoolLedgerHead(schoolLedgerHead);
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);
		Long schoolLedgerHeadId = schoolLedgerHead.getId();

		// Get all the studentAdditionalChargesList where schoolLedgerHead equals to
		// schoolLedgerHeadId
		defaultStudentAdditionalChargesShouldBeFound("schoolLedgerHeadId.equals=" + schoolLedgerHeadId);

		// Get all the studentAdditionalChargesList where schoolLedgerHead equals to
		// (schoolLedgerHeadId + 1)
		defaultStudentAdditionalChargesShouldNotBeFound("schoolLedgerHeadId.equals=" + (schoolLedgerHeadId + 1));
	}

	@Test
	@Transactional
	void getAllStudentAdditionalChargesByClassStudentIsEqualToSomething() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);
		ClassStudent classStudent = ClassStudentResourceIT.createEntity(em);
		em.persist(classStudent);
		em.flush();
		studentAdditionalCharges.setClassStudent(classStudent);
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);
		Long classStudentId = classStudent.getId();

		// Get all the studentAdditionalChargesList where classStudent equals to
		// classStudentId
		defaultStudentAdditionalChargesShouldBeFound("classStudentId.equals=" + classStudentId);

		// Get all the studentAdditionalChargesList where classStudent equals to
		// (classStudentId + 1)
		defaultStudentAdditionalChargesShouldNotBeFound("classStudentId.equals=" + (classStudentId + 1));
	}

	/**
	 * Executes the search, and checks that the default entity is returned.
	 */
	private void defaultStudentAdditionalChargesShouldBeFound(String filter) throws Exception {
		restStudentAdditionalChargesMockMvc.perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.[*].id").value(hasItem(studentAdditionalCharges.getId().intValue())))
				.andExpect(jsonPath("$.[*].feeYear").value(hasItem(DEFAULT_FEE_YEAR.toString())))
				.andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE)))
				.andExpect(jsonPath("$.[*].janAddChrg").value(hasItem(DEFAULT_JAN_ADD_CHRG.doubleValue())))
				.andExpect(jsonPath("$.[*].febAddChrgc").value(hasItem(DEFAULT_FEB_ADD_CHRGC.doubleValue())))
				.andExpect(jsonPath("$.[*].marAddChrg").value(hasItem(DEFAULT_MAR_ADD_CHRG.doubleValue())))
				.andExpect(jsonPath("$.[*].aprAddChrg").value(hasItem(DEFAULT_APR_ADD_CHRG.doubleValue())))
				.andExpect(jsonPath("$.[*].mayAddChrg").value(hasItem(DEFAULT_MAY_ADD_CHRG.doubleValue())))
				.andExpect(jsonPath("$.[*].junAddChrg").value(hasItem(DEFAULT_JUN_ADD_CHRG.doubleValue())))
				.andExpect(jsonPath("$.[*].julAddChrg").value(hasItem(DEFAULT_JUL_ADD_CHRG.doubleValue())))
				.andExpect(jsonPath("$.[*].augAddChrg").value(hasItem(DEFAULT_AUG_ADD_CHRG.doubleValue())))
				.andExpect(jsonPath("$.[*].sepAddChrg").value(hasItem(DEFAULT_SEP_ADD_CHRG.doubleValue())))
				.andExpect(jsonPath("$.[*].octAddChrg").value(hasItem(DEFAULT_OCT_ADD_CHRG.doubleValue())))
				.andExpect(jsonPath("$.[*].novAddChrg").value(hasItem(DEFAULT_NOV_ADD_CHRG.doubleValue())))
				.andExpect(jsonPath("$.[*].decAddChrg").value(hasItem(DEFAULT_DEC_ADD_CHRG.doubleValue())))
				.andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
				.andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
				.andExpect(jsonPath("$.[*].cancelDate").value(hasItem(DEFAULT_CANCEL_DATE.toString())));

		// Check, that the count call also returns 1
		restStudentAdditionalChargesMockMvc.perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(content().string("1"));
	}

	/**
	 * Executes the search, and checks that the default entity is not returned.
	 */
	private void defaultStudentAdditionalChargesShouldNotBeFound(String filter) throws Exception {
		restStudentAdditionalChargesMockMvc.perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$").isArray()).andExpect(jsonPath("$").isEmpty());

		// Check, that the count call also returns 0
		restStudentAdditionalChargesMockMvc.perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(content().string("0"));
	}

	@Test
	@Transactional
	void getNonExistingStudentAdditionalCharges() throws Exception {
		// Get the studentAdditionalCharges
		restStudentAdditionalChargesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE))
				.andExpect(status().isNotFound());
	}

	@Test
	@Transactional
	void putNewStudentAdditionalCharges() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		int databaseSizeBeforeUpdate = studentAdditionalChargesRepository.findAll().size();

		// Update the studentAdditionalCharges
		StudentAdditionalCharges updatedStudentAdditionalCharges = studentAdditionalChargesRepository
				.findById(studentAdditionalCharges.getId()).get();
		// Disconnect from session so that the updates on
		// updatedStudentAdditionalCharges are not directly saved in db
		em.detach(updatedStudentAdditionalCharges);
		updatedStudentAdditionalCharges.feeYear(UPDATED_FEE_YEAR).dueDate(UPDATED_DUE_DATE)
				.janAddChrg(UPDATED_JAN_ADD_CHRG).febAddChrgc(UPDATED_FEB_ADD_CHRGC).marAddChrg(UPDATED_MAR_ADD_CHRG)
				.aprAddChrg(UPDATED_APR_ADD_CHRG).mayAddChrg(UPDATED_MAY_ADD_CHRG).junAddChrg(UPDATED_JUN_ADD_CHRG)
				.julAddChrg(UPDATED_JUL_ADD_CHRG).augAddChrg(UPDATED_AUG_ADD_CHRG).sepAddChrg(UPDATED_SEP_ADD_CHRG)
				.octAddChrg(UPDATED_OCT_ADD_CHRG).novAddChrg(UPDATED_NOV_ADD_CHRG).decAddChrg(UPDATED_DEC_ADD_CHRG)
				.createDate(UPDATED_CREATE_DATE).lastModified(UPDATED_LAST_MODIFIED).cancelDate(UPDATED_CANCEL_DATE);
		StudentAdditionalChargesDTO studentAdditionalChargesDTO = studentAdditionalChargesMapper
				.toDto(updatedStudentAdditionalCharges);

		restStudentAdditionalChargesMockMvc
				.perform(put(ENTITY_API_URL_ID, studentAdditionalChargesDTO.getId())
						.contentType(MediaType.APPLICATION_JSON)
						.content(TestUtil.convertObjectToJsonBytes(studentAdditionalChargesDTO)))
				.andExpect(status().isOk());

		// Validate the StudentAdditionalCharges in the database
		List<StudentAdditionalCharges> studentAdditionalChargesList = studentAdditionalChargesRepository.findAll();
		assertThat(studentAdditionalChargesList).hasSize(databaseSizeBeforeUpdate);
		StudentAdditionalCharges testStudentAdditionalCharges = studentAdditionalChargesList
				.get(studentAdditionalChargesList.size() - 1);
		assertThat(testStudentAdditionalCharges.getFeeYear()).isEqualTo(UPDATED_FEE_YEAR);
		assertThat(testStudentAdditionalCharges.getDueDate()).isEqualTo(UPDATED_DUE_DATE);
		assertThat(testStudentAdditionalCharges.getJanAddChrg()).isEqualTo(UPDATED_JAN_ADD_CHRG);
		assertThat(testStudentAdditionalCharges.getFebAddChrgc()).isEqualTo(UPDATED_FEB_ADD_CHRGC);
		assertThat(testStudentAdditionalCharges.getMarAddChrg()).isEqualTo(UPDATED_MAR_ADD_CHRG);
		assertThat(testStudentAdditionalCharges.getAprAddChrg()).isEqualTo(UPDATED_APR_ADD_CHRG);
		assertThat(testStudentAdditionalCharges.getMayAddChrg()).isEqualTo(UPDATED_MAY_ADD_CHRG);
		assertThat(testStudentAdditionalCharges.getJunAddChrg()).isEqualTo(UPDATED_JUN_ADD_CHRG);
		assertThat(testStudentAdditionalCharges.getJulAddChrg()).isEqualTo(UPDATED_JUL_ADD_CHRG);
		assertThat(testStudentAdditionalCharges.getAugAddChrg()).isEqualTo(UPDATED_AUG_ADD_CHRG);
		assertThat(testStudentAdditionalCharges.getSepAddChrg()).isEqualTo(UPDATED_SEP_ADD_CHRG);
		assertThat(testStudentAdditionalCharges.getOctAddChrg()).isEqualTo(UPDATED_OCT_ADD_CHRG);
		assertThat(testStudentAdditionalCharges.getNovAddChrg()).isEqualTo(UPDATED_NOV_ADD_CHRG);
		assertThat(testStudentAdditionalCharges.getDecAddChrg()).isEqualTo(UPDATED_DEC_ADD_CHRG);
		assertThat(testStudentAdditionalCharges.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
		assertThat(testStudentAdditionalCharges.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
		assertThat(testStudentAdditionalCharges.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
	}

	@Test
	@Transactional
	void putNonExistingStudentAdditionalCharges() throws Exception {
		int databaseSizeBeforeUpdate = studentAdditionalChargesRepository.findAll().size();
		studentAdditionalCharges.setId(count.incrementAndGet());

		// Create the StudentAdditionalCharges
		StudentAdditionalChargesDTO studentAdditionalChargesDTO = studentAdditionalChargesMapper
				.toDto(studentAdditionalCharges);

		// If the entity doesn't have an ID, it will throw BadRequestAlertException
		restStudentAdditionalChargesMockMvc
				.perform(put(ENTITY_API_URL_ID, studentAdditionalChargesDTO.getId())
						.contentType(MediaType.APPLICATION_JSON)
						.content(TestUtil.convertObjectToJsonBytes(studentAdditionalChargesDTO)))
				.andExpect(status().isBadRequest());

		// Validate the StudentAdditionalCharges in the database
		List<StudentAdditionalCharges> studentAdditionalChargesList = studentAdditionalChargesRepository.findAll();
		assertThat(studentAdditionalChargesList).hasSize(databaseSizeBeforeUpdate);
	}

	@Test
	@Transactional
	void putWithIdMismatchStudentAdditionalCharges() throws Exception {
		int databaseSizeBeforeUpdate = studentAdditionalChargesRepository.findAll().size();
		studentAdditionalCharges.setId(count.incrementAndGet());

		// Create the StudentAdditionalCharges
		StudentAdditionalChargesDTO studentAdditionalChargesDTO = studentAdditionalChargesMapper
				.toDto(studentAdditionalCharges);

		// If url ID doesn't match entity ID, it will throw BadRequestAlertException
		restStudentAdditionalChargesMockMvc
				.perform(put(ENTITY_API_URL_ID, count.incrementAndGet()).contentType(MediaType.APPLICATION_JSON)
						.content(TestUtil.convertObjectToJsonBytes(studentAdditionalChargesDTO)))
				.andExpect(status().isBadRequest());

		// Validate the StudentAdditionalCharges in the database
		List<StudentAdditionalCharges> studentAdditionalChargesList = studentAdditionalChargesRepository.findAll();
		assertThat(studentAdditionalChargesList).hasSize(databaseSizeBeforeUpdate);
	}

	@Test
	@Transactional
	void putWithMissingIdPathParamStudentAdditionalCharges() throws Exception {
		int databaseSizeBeforeUpdate = studentAdditionalChargesRepository.findAll().size();
		studentAdditionalCharges.setId(count.incrementAndGet());

		// Create the StudentAdditionalCharges
		StudentAdditionalChargesDTO studentAdditionalChargesDTO = studentAdditionalChargesMapper
				.toDto(studentAdditionalCharges);

		// If url ID doesn't match entity ID, it will throw BadRequestAlertException
		restStudentAdditionalChargesMockMvc
				.perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON)
						.content(TestUtil.convertObjectToJsonBytes(studentAdditionalChargesDTO)))
				.andExpect(status().isMethodNotAllowed());

		// Validate the StudentAdditionalCharges in the database
		List<StudentAdditionalCharges> studentAdditionalChargesList = studentAdditionalChargesRepository.findAll();
		assertThat(studentAdditionalChargesList).hasSize(databaseSizeBeforeUpdate);
	}

	@Test
	@Transactional
	void partialUpdateStudentAdditionalChargesWithPatch() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		int databaseSizeBeforeUpdate = studentAdditionalChargesRepository.findAll().size();

		// Update the studentAdditionalCharges using partial update
		StudentAdditionalCharges partialUpdatedStudentAdditionalCharges = new StudentAdditionalCharges();
		partialUpdatedStudentAdditionalCharges.setId(studentAdditionalCharges.getId());

		partialUpdatedStudentAdditionalCharges.aprAddChrg(UPDATED_APR_ADD_CHRG).mayAddChrg(UPDATED_MAY_ADD_CHRG)
				.sepAddChrg(UPDATED_SEP_ADD_CHRG).decAddChrg(UPDATED_DEC_ADD_CHRG).cancelDate(UPDATED_CANCEL_DATE);

		restStudentAdditionalChargesMockMvc
				.perform(patch(ENTITY_API_URL_ID, partialUpdatedStudentAdditionalCharges.getId())
						.contentType("application/merge-patch+json")
						.content(TestUtil.convertObjectToJsonBytes(partialUpdatedStudentAdditionalCharges)))
				.andExpect(status().isOk());

		// Validate the StudentAdditionalCharges in the database
		List<StudentAdditionalCharges> studentAdditionalChargesList = studentAdditionalChargesRepository.findAll();
		assertThat(studentAdditionalChargesList).hasSize(databaseSizeBeforeUpdate);
		StudentAdditionalCharges testStudentAdditionalCharges = studentAdditionalChargesList
				.get(studentAdditionalChargesList.size() - 1);
		assertThat(testStudentAdditionalCharges.getFeeYear()).isEqualTo(DEFAULT_FEE_YEAR);
		assertThat(testStudentAdditionalCharges.getDueDate()).isEqualTo(DEFAULT_DUE_DATE);
		assertThat(testStudentAdditionalCharges.getJanAddChrg()).isEqualTo(DEFAULT_JAN_ADD_CHRG);
		assertThat(testStudentAdditionalCharges.getFebAddChrgc()).isEqualTo(DEFAULT_FEB_ADD_CHRGC);
		assertThat(testStudentAdditionalCharges.getMarAddChrg()).isEqualTo(DEFAULT_MAR_ADD_CHRG);
		assertThat(testStudentAdditionalCharges.getAprAddChrg()).isEqualTo(UPDATED_APR_ADD_CHRG);
		assertThat(testStudentAdditionalCharges.getMayAddChrg()).isEqualTo(UPDATED_MAY_ADD_CHRG);
		assertThat(testStudentAdditionalCharges.getJunAddChrg()).isEqualTo(DEFAULT_JUN_ADD_CHRG);
		assertThat(testStudentAdditionalCharges.getJulAddChrg()).isEqualTo(DEFAULT_JUL_ADD_CHRG);
		assertThat(testStudentAdditionalCharges.getAugAddChrg()).isEqualTo(DEFAULT_AUG_ADD_CHRG);
		assertThat(testStudentAdditionalCharges.getSepAddChrg()).isEqualTo(UPDATED_SEP_ADD_CHRG);
		assertThat(testStudentAdditionalCharges.getOctAddChrg()).isEqualTo(DEFAULT_OCT_ADD_CHRG);
		assertThat(testStudentAdditionalCharges.getNovAddChrg()).isEqualTo(DEFAULT_NOV_ADD_CHRG);
		assertThat(testStudentAdditionalCharges.getDecAddChrg()).isEqualTo(UPDATED_DEC_ADD_CHRG);
		assertThat(testStudentAdditionalCharges.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
		assertThat(testStudentAdditionalCharges.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
		assertThat(testStudentAdditionalCharges.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
	}

	@Test
	@Transactional
	void fullUpdateStudentAdditionalChargesWithPatch() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		int databaseSizeBeforeUpdate = studentAdditionalChargesRepository.findAll().size();

		// Update the studentAdditionalCharges using partial update
		StudentAdditionalCharges partialUpdatedStudentAdditionalCharges = new StudentAdditionalCharges();
		partialUpdatedStudentAdditionalCharges.setId(studentAdditionalCharges.getId());

		partialUpdatedStudentAdditionalCharges.feeYear(UPDATED_FEE_YEAR).dueDate(UPDATED_DUE_DATE)
				.janAddChrg(UPDATED_JAN_ADD_CHRG).febAddChrgc(UPDATED_FEB_ADD_CHRGC).marAddChrg(UPDATED_MAR_ADD_CHRG)
				.aprAddChrg(UPDATED_APR_ADD_CHRG).mayAddChrg(UPDATED_MAY_ADD_CHRG).junAddChrg(UPDATED_JUN_ADD_CHRG)
				.julAddChrg(UPDATED_JUL_ADD_CHRG).augAddChrg(UPDATED_AUG_ADD_CHRG).sepAddChrg(UPDATED_SEP_ADD_CHRG)
				.octAddChrg(UPDATED_OCT_ADD_CHRG).novAddChrg(UPDATED_NOV_ADD_CHRG).decAddChrg(UPDATED_DEC_ADD_CHRG)
				.createDate(UPDATED_CREATE_DATE).lastModified(UPDATED_LAST_MODIFIED).cancelDate(UPDATED_CANCEL_DATE);

		restStudentAdditionalChargesMockMvc
				.perform(patch(ENTITY_API_URL_ID, partialUpdatedStudentAdditionalCharges.getId())
						.contentType("application/merge-patch+json")
						.content(TestUtil.convertObjectToJsonBytes(partialUpdatedStudentAdditionalCharges)))
				.andExpect(status().isOk());

		// Validate the StudentAdditionalCharges in the database
		List<StudentAdditionalCharges> studentAdditionalChargesList = studentAdditionalChargesRepository.findAll();
		assertThat(studentAdditionalChargesList).hasSize(databaseSizeBeforeUpdate);
		StudentAdditionalCharges testStudentAdditionalCharges = studentAdditionalChargesList
				.get(studentAdditionalChargesList.size() - 1);
		assertThat(testStudentAdditionalCharges.getFeeYear()).isEqualTo(UPDATED_FEE_YEAR);
		assertThat(testStudentAdditionalCharges.getDueDate()).isEqualTo(UPDATED_DUE_DATE);
		assertThat(testStudentAdditionalCharges.getJanAddChrg()).isEqualTo(UPDATED_JAN_ADD_CHRG);
		assertThat(testStudentAdditionalCharges.getFebAddChrgc()).isEqualTo(UPDATED_FEB_ADD_CHRGC);
		assertThat(testStudentAdditionalCharges.getMarAddChrg()).isEqualTo(UPDATED_MAR_ADD_CHRG);
		assertThat(testStudentAdditionalCharges.getAprAddChrg()).isEqualTo(UPDATED_APR_ADD_CHRG);
		assertThat(testStudentAdditionalCharges.getMayAddChrg()).isEqualTo(UPDATED_MAY_ADD_CHRG);
		assertThat(testStudentAdditionalCharges.getJunAddChrg()).isEqualTo(UPDATED_JUN_ADD_CHRG);
		assertThat(testStudentAdditionalCharges.getJulAddChrg()).isEqualTo(UPDATED_JUL_ADD_CHRG);
		assertThat(testStudentAdditionalCharges.getAugAddChrg()).isEqualTo(UPDATED_AUG_ADD_CHRG);
		assertThat(testStudentAdditionalCharges.getSepAddChrg()).isEqualTo(UPDATED_SEP_ADD_CHRG);
		assertThat(testStudentAdditionalCharges.getOctAddChrg()).isEqualTo(UPDATED_OCT_ADD_CHRG);
		assertThat(testStudentAdditionalCharges.getNovAddChrg()).isEqualTo(UPDATED_NOV_ADD_CHRG);
		assertThat(testStudentAdditionalCharges.getDecAddChrg()).isEqualTo(UPDATED_DEC_ADD_CHRG);
		assertThat(testStudentAdditionalCharges.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
		assertThat(testStudentAdditionalCharges.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
		assertThat(testStudentAdditionalCharges.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
	}

	@Test
	@Transactional
	void patchNonExistingStudentAdditionalCharges() throws Exception {
		int databaseSizeBeforeUpdate = studentAdditionalChargesRepository.findAll().size();
		studentAdditionalCharges.setId(count.incrementAndGet());

		// Create the StudentAdditionalCharges
		StudentAdditionalChargesDTO studentAdditionalChargesDTO = studentAdditionalChargesMapper
				.toDto(studentAdditionalCharges);

		// If the entity doesn't have an ID, it will throw BadRequestAlertException
		restStudentAdditionalChargesMockMvc
				.perform(patch(ENTITY_API_URL_ID, studentAdditionalChargesDTO.getId())
						.contentType("application/merge-patch+json")
						.content(TestUtil.convertObjectToJsonBytes(studentAdditionalChargesDTO)))
				.andExpect(status().isBadRequest());

		// Validate the StudentAdditionalCharges in the database
		List<StudentAdditionalCharges> studentAdditionalChargesList = studentAdditionalChargesRepository.findAll();
		assertThat(studentAdditionalChargesList).hasSize(databaseSizeBeforeUpdate);
	}

	@Test
	@Transactional
	void patchWithIdMismatchStudentAdditionalCharges() throws Exception {
		int databaseSizeBeforeUpdate = studentAdditionalChargesRepository.findAll().size();
		studentAdditionalCharges.setId(count.incrementAndGet());

		// Create the StudentAdditionalCharges
		StudentAdditionalChargesDTO studentAdditionalChargesDTO = studentAdditionalChargesMapper
				.toDto(studentAdditionalCharges);

		// If url ID doesn't match entity ID, it will throw BadRequestAlertException
		restStudentAdditionalChargesMockMvc
				.perform(patch(ENTITY_API_URL_ID, count.incrementAndGet()).contentType("application/merge-patch+json")
						.content(TestUtil.convertObjectToJsonBytes(studentAdditionalChargesDTO)))
				.andExpect(status().isBadRequest());

		// Validate the StudentAdditionalCharges in the database
		List<StudentAdditionalCharges> studentAdditionalChargesList = studentAdditionalChargesRepository.findAll();
		assertThat(studentAdditionalChargesList).hasSize(databaseSizeBeforeUpdate);
	}

	@Test
	@Transactional
	void patchWithMissingIdPathParamStudentAdditionalCharges() throws Exception {
		int databaseSizeBeforeUpdate = studentAdditionalChargesRepository.findAll().size();
		studentAdditionalCharges.setId(count.incrementAndGet());

		// Create the StudentAdditionalCharges
		StudentAdditionalChargesDTO studentAdditionalChargesDTO = studentAdditionalChargesMapper
				.toDto(studentAdditionalCharges);

		// If url ID doesn't match entity ID, it will throw BadRequestAlertException
		restStudentAdditionalChargesMockMvc
				.perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json")
						.content(TestUtil.convertObjectToJsonBytes(studentAdditionalChargesDTO)))
				.andExpect(status().isMethodNotAllowed());

		// Validate the StudentAdditionalCharges in the database
		List<StudentAdditionalCharges> studentAdditionalChargesList = studentAdditionalChargesRepository.findAll();
		assertThat(studentAdditionalChargesList).hasSize(databaseSizeBeforeUpdate);
	}

	@Test
	@Transactional
	void deleteStudentAdditionalCharges() throws Exception {
		// Initialize the database
		studentAdditionalChargesRepository.saveAndFlush(studentAdditionalCharges);

		int databaseSizeBeforeDelete = studentAdditionalChargesRepository.findAll().size();

		// Delete the studentAdditionalCharges
		restStudentAdditionalChargesMockMvc
				.perform(delete(ENTITY_API_URL_ID, studentAdditionalCharges.getId()).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());

		// Validate the database contains one less item
		List<StudentAdditionalCharges> studentAdditionalChargesList = studentAdditionalChargesRepository.findAll();
		assertThat(studentAdditionalChargesList).hasSize(databaseSizeBeforeDelete - 1);
	}
}
