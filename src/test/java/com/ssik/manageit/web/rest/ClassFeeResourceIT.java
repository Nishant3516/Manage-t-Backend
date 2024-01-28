package com.ssik.manageit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.ssik.manageit.IntegrationTest;
import com.ssik.manageit.domain.ClassFee;
import com.ssik.manageit.domain.SchoolClass;
import com.ssik.manageit.domain.SchoolLedgerHead;
import com.ssik.manageit.domain.enumeration.FeeYear;
import com.ssik.manageit.repository.ClassFeeRepository;
import com.ssik.manageit.service.ClassFeeService;
import com.ssik.manageit.service.dto.ClassFeeDTO;
import com.ssik.manageit.service.mapper.ClassFeeMapper;

/**
 * Integration tests for the {@link ClassFeeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ClassFeeResourceIT {

	private static final FeeYear DEFAULT_FEE_YEAR = FeeYear.YEAR_2023;
	private static final FeeYear UPDATED_FEE_YEAR = FeeYear.YEAR_2023;

	private static final Integer DEFAULT_DUE_DATE = 1;
	private static final Integer UPDATED_DUE_DATE = 2;
	private static final Integer SMALLER_DUE_DATE = 1 - 1;

	private static final Double DEFAULT_JAN_FEE = 1D;
	private static final Double UPDATED_JAN_FEE = 2D;
	private static final Double SMALLER_JAN_FEE = 1D - 1D;

	private static final Double DEFAULT_FEB_FEE = 1D;
	private static final Double UPDATED_FEB_FEE = 2D;
	private static final Double SMALLER_FEB_FEE = 1D - 1D;

	private static final Double DEFAULT_MAR_FEE = 1D;
	private static final Double UPDATED_MAR_FEE = 2D;
	private static final Double SMALLER_MAR_FEE = 1D - 1D;

	private static final Double DEFAULT_APR_FEE = 1D;
	private static final Double UPDATED_APR_FEE = 2D;
	private static final Double SMALLER_APR_FEE = 1D - 1D;

	private static final Double DEFAULT_MAY_FEE = 1D;
	private static final Double UPDATED_MAY_FEE = 2D;
	private static final Double SMALLER_MAY_FEE = 1D - 1D;

	private static final Double DEFAULT_JUN_FEE = 1D;
	private static final Double UPDATED_JUN_FEE = 2D;
	private static final Double SMALLER_JUN_FEE = 1D - 1D;

	private static final Double DEFAULT_JUL_FEE = 1D;
	private static final Double UPDATED_JUL_FEE = 2D;
	private static final Double SMALLER_JUL_FEE = 1D - 1D;

	private static final Double DEFAULT_AUG_FEE = 1D;
	private static final Double UPDATED_AUG_FEE = 2D;
	private static final Double SMALLER_AUG_FEE = 1D - 1D;

	private static final Double DEFAULT_SEP_FEE = 1D;
	private static final Double UPDATED_SEP_FEE = 2D;
	private static final Double SMALLER_SEP_FEE = 1D - 1D;

	private static final Double DEFAULT_OCT_FEE = 1D;
	private static final Double UPDATED_OCT_FEE = 2D;
	private static final Double SMALLER_OCT_FEE = 1D - 1D;

	private static final Double DEFAULT_NOV_FEE = 1D;
	private static final Double UPDATED_NOV_FEE = 2D;
	private static final Double SMALLER_NOV_FEE = 1D - 1D;

	private static final Double DEFAULT_DEC_FEE = 1D;
	private static final Double UPDATED_DEC_FEE = 2D;
	private static final Double SMALLER_DEC_FEE = 1D - 1D;

	private static final LocalDate DEFAULT_PAY_BY_DATE = LocalDate.ofEpochDay(0L);
	private static final LocalDate UPDATED_PAY_BY_DATE = LocalDate.now(ZoneId.systemDefault());
	private static final LocalDate SMALLER_PAY_BY_DATE = LocalDate.ofEpochDay(-1L);

	private static final LocalDate DEFAULT_CREATE_DATE = LocalDate.ofEpochDay(0L);
	private static final LocalDate UPDATED_CREATE_DATE = LocalDate.now(ZoneId.systemDefault());
	private static final LocalDate SMALLER_CREATE_DATE = LocalDate.ofEpochDay(-1L);

	private static final LocalDate DEFAULT_LAST_MODIFIED = LocalDate.ofEpochDay(0L);
	private static final LocalDate UPDATED_LAST_MODIFIED = LocalDate.now(ZoneId.systemDefault());
	private static final LocalDate SMALLER_LAST_MODIFIED = LocalDate.ofEpochDay(-1L);

	private static final LocalDate DEFAULT_CANCEL_DATE = LocalDate.ofEpochDay(0L);
	private static final LocalDate UPDATED_CANCEL_DATE = LocalDate.now(ZoneId.systemDefault());
	private static final LocalDate SMALLER_CANCEL_DATE = LocalDate.ofEpochDay(-1L);

	private static final String ENTITY_API_URL = "/api/class-fees";
	private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

	private static Random random = new Random();
	private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

	@Autowired
	private ClassFeeRepository classFeeRepository;

	@Mock
	private ClassFeeRepository classFeeRepositoryMock;

	@Autowired
	private ClassFeeMapper classFeeMapper;

	@Mock
	private ClassFeeService classFeeServiceMock;

	@Autowired
	private EntityManager em;

	@Autowired
	private MockMvc restClassFeeMockMvc;

	private ClassFee classFee;

	/**
	 * Create an entity for this test.
	 *
	 * This is a static method, as tests for other entities might also need it, if
	 * they test an entity which requires the current entity.
	 */
	public static ClassFee createEntity(EntityManager em) {
		ClassFee classFee = new ClassFee().feeYear(DEFAULT_FEE_YEAR).dueDate(DEFAULT_DUE_DATE).janFee(DEFAULT_JAN_FEE)
				.febFee(DEFAULT_FEB_FEE).marFee(DEFAULT_MAR_FEE).aprFee(DEFAULT_APR_FEE).mayFee(DEFAULT_MAY_FEE)
				.junFee(DEFAULT_JUN_FEE).julFee(DEFAULT_JUL_FEE).augFee(DEFAULT_AUG_FEE).sepFee(DEFAULT_SEP_FEE)
				.octFee(DEFAULT_OCT_FEE).novFee(DEFAULT_NOV_FEE).decFee(DEFAULT_DEC_FEE).payByDate(DEFAULT_PAY_BY_DATE)
				.createDate(DEFAULT_CREATE_DATE).lastModified(DEFAULT_LAST_MODIFIED).cancelDate(DEFAULT_CANCEL_DATE);
		return classFee;
	}

	/**
	 * Create an updated entity for this test.
	 *
	 * This is a static method, as tests for other entities might also need it, if
	 * they test an entity which requires the current entity.
	 */
	public static ClassFee createUpdatedEntity(EntityManager em) {
		ClassFee classFee = new ClassFee().feeYear(UPDATED_FEE_YEAR).dueDate(UPDATED_DUE_DATE).janFee(UPDATED_JAN_FEE)
				.febFee(UPDATED_FEB_FEE).marFee(UPDATED_MAR_FEE).aprFee(UPDATED_APR_FEE).mayFee(UPDATED_MAY_FEE)
				.junFee(UPDATED_JUN_FEE).julFee(UPDATED_JUL_FEE).augFee(UPDATED_AUG_FEE).sepFee(UPDATED_SEP_FEE)
				.octFee(UPDATED_OCT_FEE).novFee(UPDATED_NOV_FEE).decFee(UPDATED_DEC_FEE).payByDate(UPDATED_PAY_BY_DATE)
				.createDate(UPDATED_CREATE_DATE).lastModified(UPDATED_LAST_MODIFIED).cancelDate(UPDATED_CANCEL_DATE);
		return classFee;
	}

	@BeforeEach
	public void initTest() {
		classFee = createEntity(em);
	}

	@Test
	@Transactional
	void createClassFee() throws Exception {
		int databaseSizeBeforeCreate = classFeeRepository.findAll().size();
		// Create the ClassFee
		ClassFeeDTO classFeeDTO = classFeeMapper.toDto(classFee);
		restClassFeeMockMvc.perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(classFeeDTO))).andExpect(status().isCreated());

		// Validate the ClassFee in the database
		List<ClassFee> classFeeList = classFeeRepository.findAll();
		assertThat(classFeeList).hasSize(databaseSizeBeforeCreate + 1);
		ClassFee testClassFee = classFeeList.get(classFeeList.size() - 1);
		assertThat(testClassFee.getFeeYear()).isEqualTo(DEFAULT_FEE_YEAR);
		assertThat(testClassFee.getDueDate()).isEqualTo(DEFAULT_DUE_DATE);
		assertThat(testClassFee.getJanFee()).isEqualTo(DEFAULT_JAN_FEE);
		assertThat(testClassFee.getFebFee()).isEqualTo(DEFAULT_FEB_FEE);
		assertThat(testClassFee.getMarFee()).isEqualTo(DEFAULT_MAR_FEE);
		assertThat(testClassFee.getAprFee()).isEqualTo(DEFAULT_APR_FEE);
		assertThat(testClassFee.getMayFee()).isEqualTo(DEFAULT_MAY_FEE);
		assertThat(testClassFee.getJunFee()).isEqualTo(DEFAULT_JUN_FEE);
		assertThat(testClassFee.getJulFee()).isEqualTo(DEFAULT_JUL_FEE);
		assertThat(testClassFee.getAugFee()).isEqualTo(DEFAULT_AUG_FEE);
		assertThat(testClassFee.getSepFee()).isEqualTo(DEFAULT_SEP_FEE);
		assertThat(testClassFee.getOctFee()).isEqualTo(DEFAULT_OCT_FEE);
		assertThat(testClassFee.getNovFee()).isEqualTo(DEFAULT_NOV_FEE);
		assertThat(testClassFee.getDecFee()).isEqualTo(DEFAULT_DEC_FEE);
		assertThat(testClassFee.getPayByDate()).isEqualTo(DEFAULT_PAY_BY_DATE);
		assertThat(testClassFee.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
		assertThat(testClassFee.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
		assertThat(testClassFee.getCancelDate()).isEqualTo(DEFAULT_CANCEL_DATE);
	}

	@Test
	@Transactional
	void createClassFeeWithExistingId() throws Exception {
		// Create the ClassFee with an existing ID
		classFee.setId(1L);
		ClassFeeDTO classFeeDTO = classFeeMapper.toDto(classFee);

		int databaseSizeBeforeCreate = classFeeRepository.findAll().size();

		// An entity with an existing ID cannot be created, so this API call must fail
		restClassFeeMockMvc.perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(classFeeDTO))).andExpect(status().isBadRequest());

		// Validate the ClassFee in the database
		List<ClassFee> classFeeList = classFeeRepository.findAll();
		assertThat(classFeeList).hasSize(databaseSizeBeforeCreate);
	}

	@Test
	@Transactional
	void checkFeeYearIsRequired() throws Exception {
		int databaseSizeBeforeTest = classFeeRepository.findAll().size();
		// set the field null
		classFee.setFeeYear(null);

		// Create the ClassFee, which fails.
		ClassFeeDTO classFeeDTO = classFeeMapper.toDto(classFee);

		restClassFeeMockMvc.perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(classFeeDTO))).andExpect(status().isBadRequest());

		List<ClassFee> classFeeList = classFeeRepository.findAll();
		assertThat(classFeeList).hasSize(databaseSizeBeforeTest);
	}

	@Test
	@Transactional
	void checkDueDateIsRequired() throws Exception {
		int databaseSizeBeforeTest = classFeeRepository.findAll().size();
		// set the field null
		classFee.setDueDate(null);

		// Create the ClassFee, which fails.
		ClassFeeDTO classFeeDTO = classFeeMapper.toDto(classFee);

		restClassFeeMockMvc.perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(classFeeDTO))).andExpect(status().isBadRequest());

		List<ClassFee> classFeeList = classFeeRepository.findAll();
		assertThat(classFeeList).hasSize(databaseSizeBeforeTest);
	}

	@Test
	@Transactional
	void getAllClassFees() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList
		restClassFeeMockMvc.perform(get(ENTITY_API_URL + "?sort=id,desc")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.[*].id").value(hasItem(classFee.getId().intValue())))
				.andExpect(jsonPath("$.[*].feeYear").value(hasItem(DEFAULT_FEE_YEAR.toString())))
				.andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE)))
				.andExpect(jsonPath("$.[*].janFee").value(hasItem(DEFAULT_JAN_FEE.doubleValue())))
				.andExpect(jsonPath("$.[*].febFee").value(hasItem(DEFAULT_FEB_FEE.doubleValue())))
				.andExpect(jsonPath("$.[*].marFee").value(hasItem(DEFAULT_MAR_FEE.doubleValue())))
				.andExpect(jsonPath("$.[*].aprFee").value(hasItem(DEFAULT_APR_FEE.doubleValue())))
				.andExpect(jsonPath("$.[*].mayFee").value(hasItem(DEFAULT_MAY_FEE.doubleValue())))
				.andExpect(jsonPath("$.[*].junFee").value(hasItem(DEFAULT_JUN_FEE.doubleValue())))
				.andExpect(jsonPath("$.[*].julFee").value(hasItem(DEFAULT_JUL_FEE.doubleValue())))
				.andExpect(jsonPath("$.[*].augFee").value(hasItem(DEFAULT_AUG_FEE.doubleValue())))
				.andExpect(jsonPath("$.[*].sepFee").value(hasItem(DEFAULT_SEP_FEE.doubleValue())))
				.andExpect(jsonPath("$.[*].octFee").value(hasItem(DEFAULT_OCT_FEE.doubleValue())))
				.andExpect(jsonPath("$.[*].novFee").value(hasItem(DEFAULT_NOV_FEE.doubleValue())))
				.andExpect(jsonPath("$.[*].decFee").value(hasItem(DEFAULT_DEC_FEE.doubleValue())))
				.andExpect(jsonPath("$.[*].payByDate").value(hasItem(DEFAULT_PAY_BY_DATE.toString())))
				.andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
				.andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
				.andExpect(jsonPath("$.[*].cancelDate").value(hasItem(DEFAULT_CANCEL_DATE.toString())));
	}

	@SuppressWarnings({ "unchecked" })
	void getAllClassFeesWithEagerRelationshipsIsEnabled() throws Exception {
		when(classFeeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

		restClassFeeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

		verify(classFeeServiceMock, times(1)).findAllWithEagerRelationships(any());
	}

	@SuppressWarnings({ "unchecked" })
	void getAllClassFeesWithEagerRelationshipsIsNotEnabled() throws Exception {
		when(classFeeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

		restClassFeeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

		verify(classFeeServiceMock, times(1)).findAllWithEagerRelationships(any());
	}

	@Test
	@Transactional
	void getClassFee() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get the classFee
		restClassFeeMockMvc.perform(get(ENTITY_API_URL_ID, classFee.getId())).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.id").value(classFee.getId().intValue()))
				.andExpect(jsonPath("$.feeYear").value(DEFAULT_FEE_YEAR.toString()))
				.andExpect(jsonPath("$.dueDate").value(DEFAULT_DUE_DATE))
				.andExpect(jsonPath("$.janFee").value(DEFAULT_JAN_FEE.doubleValue()))
				.andExpect(jsonPath("$.febFee").value(DEFAULT_FEB_FEE.doubleValue()))
				.andExpect(jsonPath("$.marFee").value(DEFAULT_MAR_FEE.doubleValue()))
				.andExpect(jsonPath("$.aprFee").value(DEFAULT_APR_FEE.doubleValue()))
				.andExpect(jsonPath("$.mayFee").value(DEFAULT_MAY_FEE.doubleValue()))
				.andExpect(jsonPath("$.junFee").value(DEFAULT_JUN_FEE.doubleValue()))
				.andExpect(jsonPath("$.julFee").value(DEFAULT_JUL_FEE.doubleValue()))
				.andExpect(jsonPath("$.augFee").value(DEFAULT_AUG_FEE.doubleValue()))
				.andExpect(jsonPath("$.sepFee").value(DEFAULT_SEP_FEE.doubleValue()))
				.andExpect(jsonPath("$.octFee").value(DEFAULT_OCT_FEE.doubleValue()))
				.andExpect(jsonPath("$.novFee").value(DEFAULT_NOV_FEE.doubleValue()))
				.andExpect(jsonPath("$.decFee").value(DEFAULT_DEC_FEE.doubleValue()))
				.andExpect(jsonPath("$.payByDate").value(DEFAULT_PAY_BY_DATE.toString()))
				.andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
				.andExpect(jsonPath("$.lastModified").value(DEFAULT_LAST_MODIFIED.toString()))
				.andExpect(jsonPath("$.cancelDate").value(DEFAULT_CANCEL_DATE.toString()));
	}

	@Test
	@Transactional
	void getClassFeesByIdFiltering() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		Long id = classFee.getId();

		defaultClassFeeShouldBeFound("id.equals=" + id);
		defaultClassFeeShouldNotBeFound("id.notEquals=" + id);

		defaultClassFeeShouldBeFound("id.greaterThanOrEqual=" + id);
		defaultClassFeeShouldNotBeFound("id.greaterThan=" + id);

		defaultClassFeeShouldBeFound("id.lessThanOrEqual=" + id);
		defaultClassFeeShouldNotBeFound("id.lessThan=" + id);
	}

	@Test
	@Transactional
	void getAllClassFeesByFeeYearIsEqualToSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where feeYear equals to DEFAULT_FEE_YEAR
		defaultClassFeeShouldBeFound("feeYear.equals=" + DEFAULT_FEE_YEAR);

		// Get all the classFeeList where feeYear equals to UPDATED_FEE_YEAR
		defaultClassFeeShouldNotBeFound("feeYear.equals=" + UPDATED_FEE_YEAR);
	}

	@Test
	@Transactional
	void getAllClassFeesByFeeYearIsNotEqualToSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where feeYear not equals to DEFAULT_FEE_YEAR
		defaultClassFeeShouldNotBeFound("feeYear.notEquals=" + DEFAULT_FEE_YEAR);

		// Get all the classFeeList where feeYear not equals to UPDATED_FEE_YEAR
		defaultClassFeeShouldBeFound("feeYear.notEquals=" + UPDATED_FEE_YEAR);
	}

	@Test
	@Transactional
	void getAllClassFeesByFeeYearIsInShouldWork() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where feeYear in DEFAULT_FEE_YEAR or
		// UPDATED_FEE_YEAR
		defaultClassFeeShouldBeFound("feeYear.in=" + DEFAULT_FEE_YEAR + "," + UPDATED_FEE_YEAR);

		// Get all the classFeeList where feeYear equals to UPDATED_FEE_YEAR
		defaultClassFeeShouldNotBeFound("feeYear.in=" + UPDATED_FEE_YEAR);
	}

	@Test
	@Transactional
	void getAllClassFeesByFeeYearIsNullOrNotNull() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where feeYear is not null
		defaultClassFeeShouldBeFound("feeYear.specified=true");

		// Get all the classFeeList where feeYear is null
		defaultClassFeeShouldNotBeFound("feeYear.specified=false");
	}

	@Test
	@Transactional
	void getAllClassFeesByDueDateIsEqualToSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where dueDate equals to DEFAULT_DUE_DATE
		defaultClassFeeShouldBeFound("dueDate.equals=" + DEFAULT_DUE_DATE);

		// Get all the classFeeList where dueDate equals to UPDATED_DUE_DATE
		defaultClassFeeShouldNotBeFound("dueDate.equals=" + UPDATED_DUE_DATE);
	}

	@Test
	@Transactional
	void getAllClassFeesByDueDateIsNotEqualToSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where dueDate not equals to DEFAULT_DUE_DATE
		defaultClassFeeShouldNotBeFound("dueDate.notEquals=" + DEFAULT_DUE_DATE);

		// Get all the classFeeList where dueDate not equals to UPDATED_DUE_DATE
		defaultClassFeeShouldBeFound("dueDate.notEquals=" + UPDATED_DUE_DATE);
	}

	@Test
	@Transactional
	void getAllClassFeesByDueDateIsInShouldWork() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where dueDate in DEFAULT_DUE_DATE or
		// UPDATED_DUE_DATE
		defaultClassFeeShouldBeFound("dueDate.in=" + DEFAULT_DUE_DATE + "," + UPDATED_DUE_DATE);

		// Get all the classFeeList where dueDate equals to UPDATED_DUE_DATE
		defaultClassFeeShouldNotBeFound("dueDate.in=" + UPDATED_DUE_DATE);
	}

	@Test
	@Transactional
	void getAllClassFeesByDueDateIsNullOrNotNull() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where dueDate is not null
		defaultClassFeeShouldBeFound("dueDate.specified=true");

		// Get all the classFeeList where dueDate is null
		defaultClassFeeShouldNotBeFound("dueDate.specified=false");
	}

	@Test
	@Transactional
	void getAllClassFeesByDueDateIsGreaterThanOrEqualToSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where dueDate is greater than or equal to
		// DEFAULT_DUE_DATE
		defaultClassFeeShouldBeFound("dueDate.greaterThanOrEqual=" + DEFAULT_DUE_DATE);

		// Get all the classFeeList where dueDate is greater than or equal to
		// UPDATED_DUE_DATE
		defaultClassFeeShouldNotBeFound("dueDate.greaterThanOrEqual=" + UPDATED_DUE_DATE);
	}

	@Test
	@Transactional
	void getAllClassFeesByDueDateIsLessThanOrEqualToSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where dueDate is less than or equal to
		// DEFAULT_DUE_DATE
		defaultClassFeeShouldBeFound("dueDate.lessThanOrEqual=" + DEFAULT_DUE_DATE);

		// Get all the classFeeList where dueDate is less than or equal to
		// SMALLER_DUE_DATE
		defaultClassFeeShouldNotBeFound("dueDate.lessThanOrEqual=" + SMALLER_DUE_DATE);
	}

	@Test
	@Transactional
	void getAllClassFeesByDueDateIsLessThanSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where dueDate is less than DEFAULT_DUE_DATE
		defaultClassFeeShouldNotBeFound("dueDate.lessThan=" + DEFAULT_DUE_DATE);

		// Get all the classFeeList where dueDate is less than UPDATED_DUE_DATE
		defaultClassFeeShouldBeFound("dueDate.lessThan=" + UPDATED_DUE_DATE);
	}

	@Test
	@Transactional
	void getAllClassFeesByDueDateIsGreaterThanSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where dueDate is greater than DEFAULT_DUE_DATE
		defaultClassFeeShouldNotBeFound("dueDate.greaterThan=" + DEFAULT_DUE_DATE);

		// Get all the classFeeList where dueDate is greater than SMALLER_DUE_DATE
		defaultClassFeeShouldBeFound("dueDate.greaterThan=" + SMALLER_DUE_DATE);
	}

	@Test
	@Transactional
	void getAllClassFeesByJanFeeIsEqualToSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where janFee equals to DEFAULT_JAN_FEE
		defaultClassFeeShouldBeFound("janFee.equals=" + DEFAULT_JAN_FEE);

		// Get all the classFeeList where janFee equals to UPDATED_JAN_FEE
		defaultClassFeeShouldNotBeFound("janFee.equals=" + UPDATED_JAN_FEE);
	}

	@Test
	@Transactional
	void getAllClassFeesByJanFeeIsNotEqualToSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where janFee not equals to DEFAULT_JAN_FEE
		defaultClassFeeShouldNotBeFound("janFee.notEquals=" + DEFAULT_JAN_FEE);

		// Get all the classFeeList where janFee not equals to UPDATED_JAN_FEE
		defaultClassFeeShouldBeFound("janFee.notEquals=" + UPDATED_JAN_FEE);
	}

	@Test
	@Transactional
	void getAllClassFeesByJanFeeIsInShouldWork() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where janFee in DEFAULT_JAN_FEE or UPDATED_JAN_FEE
		defaultClassFeeShouldBeFound("janFee.in=" + DEFAULT_JAN_FEE + "," + UPDATED_JAN_FEE);

		// Get all the classFeeList where janFee equals to UPDATED_JAN_FEE
		defaultClassFeeShouldNotBeFound("janFee.in=" + UPDATED_JAN_FEE);
	}

	@Test
	@Transactional
	void getAllClassFeesByJanFeeIsNullOrNotNull() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where janFee is not null
		defaultClassFeeShouldBeFound("janFee.specified=true");

		// Get all the classFeeList where janFee is null
		defaultClassFeeShouldNotBeFound("janFee.specified=false");
	}

	@Test
	@Transactional
	void getAllClassFeesByJanFeeIsGreaterThanOrEqualToSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where janFee is greater than or equal to
		// DEFAULT_JAN_FEE
		defaultClassFeeShouldBeFound("janFee.greaterThanOrEqual=" + DEFAULT_JAN_FEE);

		// Get all the classFeeList where janFee is greater than or equal to
		// UPDATED_JAN_FEE
		defaultClassFeeShouldNotBeFound("janFee.greaterThanOrEqual=" + UPDATED_JAN_FEE);
	}

	@Test
	@Transactional
	void getAllClassFeesByJanFeeIsLessThanOrEqualToSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where janFee is less than or equal to
		// DEFAULT_JAN_FEE
		defaultClassFeeShouldBeFound("janFee.lessThanOrEqual=" + DEFAULT_JAN_FEE);

		// Get all the classFeeList where janFee is less than or equal to
		// SMALLER_JAN_FEE
		defaultClassFeeShouldNotBeFound("janFee.lessThanOrEqual=" + SMALLER_JAN_FEE);
	}

	@Test
	@Transactional
	void getAllClassFeesByJanFeeIsLessThanSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where janFee is less than DEFAULT_JAN_FEE
		defaultClassFeeShouldNotBeFound("janFee.lessThan=" + DEFAULT_JAN_FEE);

		// Get all the classFeeList where janFee is less than UPDATED_JAN_FEE
		defaultClassFeeShouldBeFound("janFee.lessThan=" + UPDATED_JAN_FEE);
	}

	@Test
	@Transactional
	void getAllClassFeesByJanFeeIsGreaterThanSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where janFee is greater than DEFAULT_JAN_FEE
		defaultClassFeeShouldNotBeFound("janFee.greaterThan=" + DEFAULT_JAN_FEE);

		// Get all the classFeeList where janFee is greater than SMALLER_JAN_FEE
		defaultClassFeeShouldBeFound("janFee.greaterThan=" + SMALLER_JAN_FEE);
	}

	@Test
	@Transactional
	void getAllClassFeesByFebFeeIsEqualToSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where febFee equals to DEFAULT_FEB_FEE
		defaultClassFeeShouldBeFound("febFee.equals=" + DEFAULT_FEB_FEE);

		// Get all the classFeeList where febFee equals to UPDATED_FEB_FEE
		defaultClassFeeShouldNotBeFound("febFee.equals=" + UPDATED_FEB_FEE);
	}

	@Test
	@Transactional
	void getAllClassFeesByFebFeeIsNotEqualToSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where febFee not equals to DEFAULT_FEB_FEE
		defaultClassFeeShouldNotBeFound("febFee.notEquals=" + DEFAULT_FEB_FEE);

		// Get all the classFeeList where febFee not equals to UPDATED_FEB_FEE
		defaultClassFeeShouldBeFound("febFee.notEquals=" + UPDATED_FEB_FEE);
	}

	@Test
	@Transactional
	void getAllClassFeesByFebFeeIsInShouldWork() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where febFee in DEFAULT_FEB_FEE or UPDATED_FEB_FEE
		defaultClassFeeShouldBeFound("febFee.in=" + DEFAULT_FEB_FEE + "," + UPDATED_FEB_FEE);

		// Get all the classFeeList where febFee equals to UPDATED_FEB_FEE
		defaultClassFeeShouldNotBeFound("febFee.in=" + UPDATED_FEB_FEE);
	}

	@Test
	@Transactional
	void getAllClassFeesByFebFeeIsNullOrNotNull() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where febFee is not null
		defaultClassFeeShouldBeFound("febFee.specified=true");

		// Get all the classFeeList where febFee is null
		defaultClassFeeShouldNotBeFound("febFee.specified=false");
	}

	@Test
	@Transactional
	void getAllClassFeesByFebFeeIsGreaterThanOrEqualToSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where febFee is greater than or equal to
		// DEFAULT_FEB_FEE
		defaultClassFeeShouldBeFound("febFee.greaterThanOrEqual=" + DEFAULT_FEB_FEE);

		// Get all the classFeeList where febFee is greater than or equal to
		// UPDATED_FEB_FEE
		defaultClassFeeShouldNotBeFound("febFee.greaterThanOrEqual=" + UPDATED_FEB_FEE);
	}

	@Test
	@Transactional
	void getAllClassFeesByFebFeeIsLessThanOrEqualToSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where febFee is less than or equal to
		// DEFAULT_FEB_FEE
		defaultClassFeeShouldBeFound("febFee.lessThanOrEqual=" + DEFAULT_FEB_FEE);

		// Get all the classFeeList where febFee is less than or equal to
		// SMALLER_FEB_FEE
		defaultClassFeeShouldNotBeFound("febFee.lessThanOrEqual=" + SMALLER_FEB_FEE);
	}

	@Test
	@Transactional
	void getAllClassFeesByFebFeeIsLessThanSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where febFee is less than DEFAULT_FEB_FEE
		defaultClassFeeShouldNotBeFound("febFee.lessThan=" + DEFAULT_FEB_FEE);

		// Get all the classFeeList where febFee is less than UPDATED_FEB_FEE
		defaultClassFeeShouldBeFound("febFee.lessThan=" + UPDATED_FEB_FEE);
	}

	@Test
	@Transactional
	void getAllClassFeesByFebFeeIsGreaterThanSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where febFee is greater than DEFAULT_FEB_FEE
		defaultClassFeeShouldNotBeFound("febFee.greaterThan=" + DEFAULT_FEB_FEE);

		// Get all the classFeeList where febFee is greater than SMALLER_FEB_FEE
		defaultClassFeeShouldBeFound("febFee.greaterThan=" + SMALLER_FEB_FEE);
	}

	@Test
	@Transactional
	void getAllClassFeesByMarFeeIsEqualToSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where marFee equals to DEFAULT_MAR_FEE
		defaultClassFeeShouldBeFound("marFee.equals=" + DEFAULT_MAR_FEE);

		// Get all the classFeeList where marFee equals to UPDATED_MAR_FEE
		defaultClassFeeShouldNotBeFound("marFee.equals=" + UPDATED_MAR_FEE);
	}

	@Test
	@Transactional
	void getAllClassFeesByMarFeeIsNotEqualToSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where marFee not equals to DEFAULT_MAR_FEE
		defaultClassFeeShouldNotBeFound("marFee.notEquals=" + DEFAULT_MAR_FEE);

		// Get all the classFeeList where marFee not equals to UPDATED_MAR_FEE
		defaultClassFeeShouldBeFound("marFee.notEquals=" + UPDATED_MAR_FEE);
	}

	@Test
	@Transactional
	void getAllClassFeesByMarFeeIsInShouldWork() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where marFee in DEFAULT_MAR_FEE or UPDATED_MAR_FEE
		defaultClassFeeShouldBeFound("marFee.in=" + DEFAULT_MAR_FEE + "," + UPDATED_MAR_FEE);

		// Get all the classFeeList where marFee equals to UPDATED_MAR_FEE
		defaultClassFeeShouldNotBeFound("marFee.in=" + UPDATED_MAR_FEE);
	}

	@Test
	@Transactional
	void getAllClassFeesByMarFeeIsNullOrNotNull() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where marFee is not null
		defaultClassFeeShouldBeFound("marFee.specified=true");

		// Get all the classFeeList where marFee is null
		defaultClassFeeShouldNotBeFound("marFee.specified=false");
	}

	@Test
	@Transactional
	void getAllClassFeesByMarFeeIsGreaterThanOrEqualToSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where marFee is greater than or equal to
		// DEFAULT_MAR_FEE
		defaultClassFeeShouldBeFound("marFee.greaterThanOrEqual=" + DEFAULT_MAR_FEE);

		// Get all the classFeeList where marFee is greater than or equal to
		// UPDATED_MAR_FEE
		defaultClassFeeShouldNotBeFound("marFee.greaterThanOrEqual=" + UPDATED_MAR_FEE);
	}

	@Test
	@Transactional
	void getAllClassFeesByMarFeeIsLessThanOrEqualToSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where marFee is less than or equal to
		// DEFAULT_MAR_FEE
		defaultClassFeeShouldBeFound("marFee.lessThanOrEqual=" + DEFAULT_MAR_FEE);

		// Get all the classFeeList where marFee is less than or equal to
		// SMALLER_MAR_FEE
		defaultClassFeeShouldNotBeFound("marFee.lessThanOrEqual=" + SMALLER_MAR_FEE);
	}

	@Test
	@Transactional
	void getAllClassFeesByMarFeeIsLessThanSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where marFee is less than DEFAULT_MAR_FEE
		defaultClassFeeShouldNotBeFound("marFee.lessThan=" + DEFAULT_MAR_FEE);

		// Get all the classFeeList where marFee is less than UPDATED_MAR_FEE
		defaultClassFeeShouldBeFound("marFee.lessThan=" + UPDATED_MAR_FEE);
	}

	@Test
	@Transactional
	void getAllClassFeesByMarFeeIsGreaterThanSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where marFee is greater than DEFAULT_MAR_FEE
		defaultClassFeeShouldNotBeFound("marFee.greaterThan=" + DEFAULT_MAR_FEE);

		// Get all the classFeeList where marFee is greater than SMALLER_MAR_FEE
		defaultClassFeeShouldBeFound("marFee.greaterThan=" + SMALLER_MAR_FEE);
	}

	@Test
	@Transactional
	void getAllClassFeesByAprFeeIsEqualToSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where aprFee equals to DEFAULT_APR_FEE
		defaultClassFeeShouldBeFound("aprFee.equals=" + DEFAULT_APR_FEE);

		// Get all the classFeeList where aprFee equals to UPDATED_APR_FEE
		defaultClassFeeShouldNotBeFound("aprFee.equals=" + UPDATED_APR_FEE);
	}

	@Test
	@Transactional
	void getAllClassFeesByAprFeeIsNotEqualToSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where aprFee not equals to DEFAULT_APR_FEE
		defaultClassFeeShouldNotBeFound("aprFee.notEquals=" + DEFAULT_APR_FEE);

		// Get all the classFeeList where aprFee not equals to UPDATED_APR_FEE
		defaultClassFeeShouldBeFound("aprFee.notEquals=" + UPDATED_APR_FEE);
	}

	@Test
	@Transactional
	void getAllClassFeesByAprFeeIsInShouldWork() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where aprFee in DEFAULT_APR_FEE or UPDATED_APR_FEE
		defaultClassFeeShouldBeFound("aprFee.in=" + DEFAULT_APR_FEE + "," + UPDATED_APR_FEE);

		// Get all the classFeeList where aprFee equals to UPDATED_APR_FEE
		defaultClassFeeShouldNotBeFound("aprFee.in=" + UPDATED_APR_FEE);
	}

	@Test
	@Transactional
	void getAllClassFeesByAprFeeIsNullOrNotNull() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where aprFee is not null
		defaultClassFeeShouldBeFound("aprFee.specified=true");

		// Get all the classFeeList where aprFee is null
		defaultClassFeeShouldNotBeFound("aprFee.specified=false");
	}

	@Test
	@Transactional
	void getAllClassFeesByAprFeeIsGreaterThanOrEqualToSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where aprFee is greater than or equal to
		// DEFAULT_APR_FEE
		defaultClassFeeShouldBeFound("aprFee.greaterThanOrEqual=" + DEFAULT_APR_FEE);

		// Get all the classFeeList where aprFee is greater than or equal to
		// UPDATED_APR_FEE
		defaultClassFeeShouldNotBeFound("aprFee.greaterThanOrEqual=" + UPDATED_APR_FEE);
	}

	@Test
	@Transactional
	void getAllClassFeesByAprFeeIsLessThanOrEqualToSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where aprFee is less than or equal to
		// DEFAULT_APR_FEE
		defaultClassFeeShouldBeFound("aprFee.lessThanOrEqual=" + DEFAULT_APR_FEE);

		// Get all the classFeeList where aprFee is less than or equal to
		// SMALLER_APR_FEE
		defaultClassFeeShouldNotBeFound("aprFee.lessThanOrEqual=" + SMALLER_APR_FEE);
	}

	@Test
	@Transactional
	void getAllClassFeesByAprFeeIsLessThanSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where aprFee is less than DEFAULT_APR_FEE
		defaultClassFeeShouldNotBeFound("aprFee.lessThan=" + DEFAULT_APR_FEE);

		// Get all the classFeeList where aprFee is less than UPDATED_APR_FEE
		defaultClassFeeShouldBeFound("aprFee.lessThan=" + UPDATED_APR_FEE);
	}

	@Test
	@Transactional
	void getAllClassFeesByAprFeeIsGreaterThanSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where aprFee is greater than DEFAULT_APR_FEE
		defaultClassFeeShouldNotBeFound("aprFee.greaterThan=" + DEFAULT_APR_FEE);

		// Get all the classFeeList where aprFee is greater than SMALLER_APR_FEE
		defaultClassFeeShouldBeFound("aprFee.greaterThan=" + SMALLER_APR_FEE);
	}

	@Test
	@Transactional
	void getAllClassFeesByMayFeeIsEqualToSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where mayFee equals to DEFAULT_MAY_FEE
		defaultClassFeeShouldBeFound("mayFee.equals=" + DEFAULT_MAY_FEE);

		// Get all the classFeeList where mayFee equals to UPDATED_MAY_FEE
		defaultClassFeeShouldNotBeFound("mayFee.equals=" + UPDATED_MAY_FEE);
	}

	@Test
	@Transactional
	void getAllClassFeesByMayFeeIsNotEqualToSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where mayFee not equals to DEFAULT_MAY_FEE
		defaultClassFeeShouldNotBeFound("mayFee.notEquals=" + DEFAULT_MAY_FEE);

		// Get all the classFeeList where mayFee not equals to UPDATED_MAY_FEE
		defaultClassFeeShouldBeFound("mayFee.notEquals=" + UPDATED_MAY_FEE);
	}

	@Test
	@Transactional
	void getAllClassFeesByMayFeeIsInShouldWork() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where mayFee in DEFAULT_MAY_FEE or UPDATED_MAY_FEE
		defaultClassFeeShouldBeFound("mayFee.in=" + DEFAULT_MAY_FEE + "," + UPDATED_MAY_FEE);

		// Get all the classFeeList where mayFee equals to UPDATED_MAY_FEE
		defaultClassFeeShouldNotBeFound("mayFee.in=" + UPDATED_MAY_FEE);
	}

	@Test
	@Transactional
	void getAllClassFeesByMayFeeIsNullOrNotNull() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where mayFee is not null
		defaultClassFeeShouldBeFound("mayFee.specified=true");

		// Get all the classFeeList where mayFee is null
		defaultClassFeeShouldNotBeFound("mayFee.specified=false");
	}

	@Test
	@Transactional
	void getAllClassFeesByMayFeeIsGreaterThanOrEqualToSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where mayFee is greater than or equal to
		// DEFAULT_MAY_FEE
		defaultClassFeeShouldBeFound("mayFee.greaterThanOrEqual=" + DEFAULT_MAY_FEE);

		// Get all the classFeeList where mayFee is greater than or equal to
		// UPDATED_MAY_FEE
		defaultClassFeeShouldNotBeFound("mayFee.greaterThanOrEqual=" + UPDATED_MAY_FEE);
	}

	@Test
	@Transactional
	void getAllClassFeesByMayFeeIsLessThanOrEqualToSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where mayFee is less than or equal to
		// DEFAULT_MAY_FEE
		defaultClassFeeShouldBeFound("mayFee.lessThanOrEqual=" + DEFAULT_MAY_FEE);

		// Get all the classFeeList where mayFee is less than or equal to
		// SMALLER_MAY_FEE
		defaultClassFeeShouldNotBeFound("mayFee.lessThanOrEqual=" + SMALLER_MAY_FEE);
	}

	@Test
	@Transactional
	void getAllClassFeesByMayFeeIsLessThanSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where mayFee is less than DEFAULT_MAY_FEE
		defaultClassFeeShouldNotBeFound("mayFee.lessThan=" + DEFAULT_MAY_FEE);

		// Get all the classFeeList where mayFee is less than UPDATED_MAY_FEE
		defaultClassFeeShouldBeFound("mayFee.lessThan=" + UPDATED_MAY_FEE);
	}

	@Test
	@Transactional
	void getAllClassFeesByMayFeeIsGreaterThanSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where mayFee is greater than DEFAULT_MAY_FEE
		defaultClassFeeShouldNotBeFound("mayFee.greaterThan=" + DEFAULT_MAY_FEE);

		// Get all the classFeeList where mayFee is greater than SMALLER_MAY_FEE
		defaultClassFeeShouldBeFound("mayFee.greaterThan=" + SMALLER_MAY_FEE);
	}

	@Test
	@Transactional
	void getAllClassFeesByJunFeeIsEqualToSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where junFee equals to DEFAULT_JUN_FEE
		defaultClassFeeShouldBeFound("junFee.equals=" + DEFAULT_JUN_FEE);

		// Get all the classFeeList where junFee equals to UPDATED_JUN_FEE
		defaultClassFeeShouldNotBeFound("junFee.equals=" + UPDATED_JUN_FEE);
	}

	@Test
	@Transactional
	void getAllClassFeesByJunFeeIsNotEqualToSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where junFee not equals to DEFAULT_JUN_FEE
		defaultClassFeeShouldNotBeFound("junFee.notEquals=" + DEFAULT_JUN_FEE);

		// Get all the classFeeList where junFee not equals to UPDATED_JUN_FEE
		defaultClassFeeShouldBeFound("junFee.notEquals=" + UPDATED_JUN_FEE);
	}

	@Test
	@Transactional
	void getAllClassFeesByJunFeeIsInShouldWork() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where junFee in DEFAULT_JUN_FEE or UPDATED_JUN_FEE
		defaultClassFeeShouldBeFound("junFee.in=" + DEFAULT_JUN_FEE + "," + UPDATED_JUN_FEE);

		// Get all the classFeeList where junFee equals to UPDATED_JUN_FEE
		defaultClassFeeShouldNotBeFound("junFee.in=" + UPDATED_JUN_FEE);
	}

	@Test
	@Transactional
	void getAllClassFeesByJunFeeIsNullOrNotNull() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where junFee is not null
		defaultClassFeeShouldBeFound("junFee.specified=true");

		// Get all the classFeeList where junFee is null
		defaultClassFeeShouldNotBeFound("junFee.specified=false");
	}

	@Test
	@Transactional
	void getAllClassFeesByJunFeeIsGreaterThanOrEqualToSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where junFee is greater than or equal to
		// DEFAULT_JUN_FEE
		defaultClassFeeShouldBeFound("junFee.greaterThanOrEqual=" + DEFAULT_JUN_FEE);

		// Get all the classFeeList where junFee is greater than or equal to
		// UPDATED_JUN_FEE
		defaultClassFeeShouldNotBeFound("junFee.greaterThanOrEqual=" + UPDATED_JUN_FEE);
	}

	@Test
	@Transactional
	void getAllClassFeesByJunFeeIsLessThanOrEqualToSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where junFee is less than or equal to
		// DEFAULT_JUN_FEE
		defaultClassFeeShouldBeFound("junFee.lessThanOrEqual=" + DEFAULT_JUN_FEE);

		// Get all the classFeeList where junFee is less than or equal to
		// SMALLER_JUN_FEE
		defaultClassFeeShouldNotBeFound("junFee.lessThanOrEqual=" + SMALLER_JUN_FEE);
	}

	@Test
	@Transactional
	void getAllClassFeesByJunFeeIsLessThanSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where junFee is less than DEFAULT_JUN_FEE
		defaultClassFeeShouldNotBeFound("junFee.lessThan=" + DEFAULT_JUN_FEE);

		// Get all the classFeeList where junFee is less than UPDATED_JUN_FEE
		defaultClassFeeShouldBeFound("junFee.lessThan=" + UPDATED_JUN_FEE);
	}

	@Test
	@Transactional
	void getAllClassFeesByJunFeeIsGreaterThanSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where junFee is greater than DEFAULT_JUN_FEE
		defaultClassFeeShouldNotBeFound("junFee.greaterThan=" + DEFAULT_JUN_FEE);

		// Get all the classFeeList where junFee is greater than SMALLER_JUN_FEE
		defaultClassFeeShouldBeFound("junFee.greaterThan=" + SMALLER_JUN_FEE);
	}

	@Test
	@Transactional
	void getAllClassFeesByJulFeeIsEqualToSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where julFee equals to DEFAULT_JUL_FEE
		defaultClassFeeShouldBeFound("julFee.equals=" + DEFAULT_JUL_FEE);

		// Get all the classFeeList where julFee equals to UPDATED_JUL_FEE
		defaultClassFeeShouldNotBeFound("julFee.equals=" + UPDATED_JUL_FEE);
	}

	@Test
	@Transactional
	void getAllClassFeesByJulFeeIsNotEqualToSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where julFee not equals to DEFAULT_JUL_FEE
		defaultClassFeeShouldNotBeFound("julFee.notEquals=" + DEFAULT_JUL_FEE);

		// Get all the classFeeList where julFee not equals to UPDATED_JUL_FEE
		defaultClassFeeShouldBeFound("julFee.notEquals=" + UPDATED_JUL_FEE);
	}

	@Test
	@Transactional
	void getAllClassFeesByJulFeeIsInShouldWork() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where julFee in DEFAULT_JUL_FEE or UPDATED_JUL_FEE
		defaultClassFeeShouldBeFound("julFee.in=" + DEFAULT_JUL_FEE + "," + UPDATED_JUL_FEE);

		// Get all the classFeeList where julFee equals to UPDATED_JUL_FEE
		defaultClassFeeShouldNotBeFound("julFee.in=" + UPDATED_JUL_FEE);
	}

	@Test
	@Transactional
	void getAllClassFeesByJulFeeIsNullOrNotNull() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where julFee is not null
		defaultClassFeeShouldBeFound("julFee.specified=true");

		// Get all the classFeeList where julFee is null
		defaultClassFeeShouldNotBeFound("julFee.specified=false");
	}

	@Test
	@Transactional
	void getAllClassFeesByJulFeeIsGreaterThanOrEqualToSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where julFee is greater than or equal to
		// DEFAULT_JUL_FEE
		defaultClassFeeShouldBeFound("julFee.greaterThanOrEqual=" + DEFAULT_JUL_FEE);

		// Get all the classFeeList where julFee is greater than or equal to
		// UPDATED_JUL_FEE
		defaultClassFeeShouldNotBeFound("julFee.greaterThanOrEqual=" + UPDATED_JUL_FEE);
	}

	@Test
	@Transactional
	void getAllClassFeesByJulFeeIsLessThanOrEqualToSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where julFee is less than or equal to
		// DEFAULT_JUL_FEE
		defaultClassFeeShouldBeFound("julFee.lessThanOrEqual=" + DEFAULT_JUL_FEE);

		// Get all the classFeeList where julFee is less than or equal to
		// SMALLER_JUL_FEE
		defaultClassFeeShouldNotBeFound("julFee.lessThanOrEqual=" + SMALLER_JUL_FEE);
	}

	@Test
	@Transactional
	void getAllClassFeesByJulFeeIsLessThanSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where julFee is less than DEFAULT_JUL_FEE
		defaultClassFeeShouldNotBeFound("julFee.lessThan=" + DEFAULT_JUL_FEE);

		// Get all the classFeeList where julFee is less than UPDATED_JUL_FEE
		defaultClassFeeShouldBeFound("julFee.lessThan=" + UPDATED_JUL_FEE);
	}

	@Test
	@Transactional
	void getAllClassFeesByJulFeeIsGreaterThanSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where julFee is greater than DEFAULT_JUL_FEE
		defaultClassFeeShouldNotBeFound("julFee.greaterThan=" + DEFAULT_JUL_FEE);

		// Get all the classFeeList where julFee is greater than SMALLER_JUL_FEE
		defaultClassFeeShouldBeFound("julFee.greaterThan=" + SMALLER_JUL_FEE);
	}

	@Test
	@Transactional
	void getAllClassFeesByAugFeeIsEqualToSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where augFee equals to DEFAULT_AUG_FEE
		defaultClassFeeShouldBeFound("augFee.equals=" + DEFAULT_AUG_FEE);

		// Get all the classFeeList where augFee equals to UPDATED_AUG_FEE
		defaultClassFeeShouldNotBeFound("augFee.equals=" + UPDATED_AUG_FEE);
	}

	@Test
	@Transactional
	void getAllClassFeesByAugFeeIsNotEqualToSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where augFee not equals to DEFAULT_AUG_FEE
		defaultClassFeeShouldNotBeFound("augFee.notEquals=" + DEFAULT_AUG_FEE);

		// Get all the classFeeList where augFee not equals to UPDATED_AUG_FEE
		defaultClassFeeShouldBeFound("augFee.notEquals=" + UPDATED_AUG_FEE);
	}

	@Test
	@Transactional
	void getAllClassFeesByAugFeeIsInShouldWork() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where augFee in DEFAULT_AUG_FEE or UPDATED_AUG_FEE
		defaultClassFeeShouldBeFound("augFee.in=" + DEFAULT_AUG_FEE + "," + UPDATED_AUG_FEE);

		// Get all the classFeeList where augFee equals to UPDATED_AUG_FEE
		defaultClassFeeShouldNotBeFound("augFee.in=" + UPDATED_AUG_FEE);
	}

	@Test
	@Transactional
	void getAllClassFeesByAugFeeIsNullOrNotNull() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where augFee is not null
		defaultClassFeeShouldBeFound("augFee.specified=true");

		// Get all the classFeeList where augFee is null
		defaultClassFeeShouldNotBeFound("augFee.specified=false");
	}

	@Test
	@Transactional
	void getAllClassFeesByAugFeeIsGreaterThanOrEqualToSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where augFee is greater than or equal to
		// DEFAULT_AUG_FEE
		defaultClassFeeShouldBeFound("augFee.greaterThanOrEqual=" + DEFAULT_AUG_FEE);

		// Get all the classFeeList where augFee is greater than or equal to
		// UPDATED_AUG_FEE
		defaultClassFeeShouldNotBeFound("augFee.greaterThanOrEqual=" + UPDATED_AUG_FEE);
	}

	@Test
	@Transactional
	void getAllClassFeesByAugFeeIsLessThanOrEqualToSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where augFee is less than or equal to
		// DEFAULT_AUG_FEE
		defaultClassFeeShouldBeFound("augFee.lessThanOrEqual=" + DEFAULT_AUG_FEE);

		// Get all the classFeeList where augFee is less than or equal to
		// SMALLER_AUG_FEE
		defaultClassFeeShouldNotBeFound("augFee.lessThanOrEqual=" + SMALLER_AUG_FEE);
	}

	@Test
	@Transactional
	void getAllClassFeesByAugFeeIsLessThanSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where augFee is less than DEFAULT_AUG_FEE
		defaultClassFeeShouldNotBeFound("augFee.lessThan=" + DEFAULT_AUG_FEE);

		// Get all the classFeeList where augFee is less than UPDATED_AUG_FEE
		defaultClassFeeShouldBeFound("augFee.lessThan=" + UPDATED_AUG_FEE);
	}

	@Test
	@Transactional
	void getAllClassFeesByAugFeeIsGreaterThanSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where augFee is greater than DEFAULT_AUG_FEE
		defaultClassFeeShouldNotBeFound("augFee.greaterThan=" + DEFAULT_AUG_FEE);

		// Get all the classFeeList where augFee is greater than SMALLER_AUG_FEE
		defaultClassFeeShouldBeFound("augFee.greaterThan=" + SMALLER_AUG_FEE);
	}

	@Test
	@Transactional
	void getAllClassFeesBySepFeeIsEqualToSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where sepFee equals to DEFAULT_SEP_FEE
		defaultClassFeeShouldBeFound("sepFee.equals=" + DEFAULT_SEP_FEE);

		// Get all the classFeeList where sepFee equals to UPDATED_SEP_FEE
		defaultClassFeeShouldNotBeFound("sepFee.equals=" + UPDATED_SEP_FEE);
	}

	@Test
	@Transactional
	void getAllClassFeesBySepFeeIsNotEqualToSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where sepFee not equals to DEFAULT_SEP_FEE
		defaultClassFeeShouldNotBeFound("sepFee.notEquals=" + DEFAULT_SEP_FEE);

		// Get all the classFeeList where sepFee not equals to UPDATED_SEP_FEE
		defaultClassFeeShouldBeFound("sepFee.notEquals=" + UPDATED_SEP_FEE);
	}

	@Test
	@Transactional
	void getAllClassFeesBySepFeeIsInShouldWork() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where sepFee in DEFAULT_SEP_FEE or UPDATED_SEP_FEE
		defaultClassFeeShouldBeFound("sepFee.in=" + DEFAULT_SEP_FEE + "," + UPDATED_SEP_FEE);

		// Get all the classFeeList where sepFee equals to UPDATED_SEP_FEE
		defaultClassFeeShouldNotBeFound("sepFee.in=" + UPDATED_SEP_FEE);
	}

	@Test
	@Transactional
	void getAllClassFeesBySepFeeIsNullOrNotNull() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where sepFee is not null
		defaultClassFeeShouldBeFound("sepFee.specified=true");

		// Get all the classFeeList where sepFee is null
		defaultClassFeeShouldNotBeFound("sepFee.specified=false");
	}

	@Test
	@Transactional
	void getAllClassFeesBySepFeeIsGreaterThanOrEqualToSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where sepFee is greater than or equal to
		// DEFAULT_SEP_FEE
		defaultClassFeeShouldBeFound("sepFee.greaterThanOrEqual=" + DEFAULT_SEP_FEE);

		// Get all the classFeeList where sepFee is greater than or equal to
		// UPDATED_SEP_FEE
		defaultClassFeeShouldNotBeFound("sepFee.greaterThanOrEqual=" + UPDATED_SEP_FEE);
	}

	@Test
	@Transactional
	void getAllClassFeesBySepFeeIsLessThanOrEqualToSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where sepFee is less than or equal to
		// DEFAULT_SEP_FEE
		defaultClassFeeShouldBeFound("sepFee.lessThanOrEqual=" + DEFAULT_SEP_FEE);

		// Get all the classFeeList where sepFee is less than or equal to
		// SMALLER_SEP_FEE
		defaultClassFeeShouldNotBeFound("sepFee.lessThanOrEqual=" + SMALLER_SEP_FEE);
	}

	@Test
	@Transactional
	void getAllClassFeesBySepFeeIsLessThanSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where sepFee is less than DEFAULT_SEP_FEE
		defaultClassFeeShouldNotBeFound("sepFee.lessThan=" + DEFAULT_SEP_FEE);

		// Get all the classFeeList where sepFee is less than UPDATED_SEP_FEE
		defaultClassFeeShouldBeFound("sepFee.lessThan=" + UPDATED_SEP_FEE);
	}

	@Test
	@Transactional
	void getAllClassFeesBySepFeeIsGreaterThanSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where sepFee is greater than DEFAULT_SEP_FEE
		defaultClassFeeShouldNotBeFound("sepFee.greaterThan=" + DEFAULT_SEP_FEE);

		// Get all the classFeeList where sepFee is greater than SMALLER_SEP_FEE
		defaultClassFeeShouldBeFound("sepFee.greaterThan=" + SMALLER_SEP_FEE);
	}

	@Test
	@Transactional
	void getAllClassFeesByOctFeeIsEqualToSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where octFee equals to DEFAULT_OCT_FEE
		defaultClassFeeShouldBeFound("octFee.equals=" + DEFAULT_OCT_FEE);

		// Get all the classFeeList where octFee equals to UPDATED_OCT_FEE
		defaultClassFeeShouldNotBeFound("octFee.equals=" + UPDATED_OCT_FEE);
	}

	@Test
	@Transactional
	void getAllClassFeesByOctFeeIsNotEqualToSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where octFee not equals to DEFAULT_OCT_FEE
		defaultClassFeeShouldNotBeFound("octFee.notEquals=" + DEFAULT_OCT_FEE);

		// Get all the classFeeList where octFee not equals to UPDATED_OCT_FEE
		defaultClassFeeShouldBeFound("octFee.notEquals=" + UPDATED_OCT_FEE);
	}

	@Test
	@Transactional
	void getAllClassFeesByOctFeeIsInShouldWork() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where octFee in DEFAULT_OCT_FEE or UPDATED_OCT_FEE
		defaultClassFeeShouldBeFound("octFee.in=" + DEFAULT_OCT_FEE + "," + UPDATED_OCT_FEE);

		// Get all the classFeeList where octFee equals to UPDATED_OCT_FEE
		defaultClassFeeShouldNotBeFound("octFee.in=" + UPDATED_OCT_FEE);
	}

	@Test
	@Transactional
	void getAllClassFeesByOctFeeIsNullOrNotNull() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where octFee is not null
		defaultClassFeeShouldBeFound("octFee.specified=true");

		// Get all the classFeeList where octFee is null
		defaultClassFeeShouldNotBeFound("octFee.specified=false");
	}

	@Test
	@Transactional
	void getAllClassFeesByOctFeeIsGreaterThanOrEqualToSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where octFee is greater than or equal to
		// DEFAULT_OCT_FEE
		defaultClassFeeShouldBeFound("octFee.greaterThanOrEqual=" + DEFAULT_OCT_FEE);

		// Get all the classFeeList where octFee is greater than or equal to
		// UPDATED_OCT_FEE
		defaultClassFeeShouldNotBeFound("octFee.greaterThanOrEqual=" + UPDATED_OCT_FEE);
	}

	@Test
	@Transactional
	void getAllClassFeesByOctFeeIsLessThanOrEqualToSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where octFee is less than or equal to
		// DEFAULT_OCT_FEE
		defaultClassFeeShouldBeFound("octFee.lessThanOrEqual=" + DEFAULT_OCT_FEE);

		// Get all the classFeeList where octFee is less than or equal to
		// SMALLER_OCT_FEE
		defaultClassFeeShouldNotBeFound("octFee.lessThanOrEqual=" + SMALLER_OCT_FEE);
	}

	@Test
	@Transactional
	void getAllClassFeesByOctFeeIsLessThanSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where octFee is less than DEFAULT_OCT_FEE
		defaultClassFeeShouldNotBeFound("octFee.lessThan=" + DEFAULT_OCT_FEE);

		// Get all the classFeeList where octFee is less than UPDATED_OCT_FEE
		defaultClassFeeShouldBeFound("octFee.lessThan=" + UPDATED_OCT_FEE);
	}

	@Test
	@Transactional
	void getAllClassFeesByOctFeeIsGreaterThanSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where octFee is greater than DEFAULT_OCT_FEE
		defaultClassFeeShouldNotBeFound("octFee.greaterThan=" + DEFAULT_OCT_FEE);

		// Get all the classFeeList where octFee is greater than SMALLER_OCT_FEE
		defaultClassFeeShouldBeFound("octFee.greaterThan=" + SMALLER_OCT_FEE);
	}

	@Test
	@Transactional
	void getAllClassFeesByNovFeeIsEqualToSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where novFee equals to DEFAULT_NOV_FEE
		defaultClassFeeShouldBeFound("novFee.equals=" + DEFAULT_NOV_FEE);

		// Get all the classFeeList where novFee equals to UPDATED_NOV_FEE
		defaultClassFeeShouldNotBeFound("novFee.equals=" + UPDATED_NOV_FEE);
	}

	@Test
	@Transactional
	void getAllClassFeesByNovFeeIsNotEqualToSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where novFee not equals to DEFAULT_NOV_FEE
		defaultClassFeeShouldNotBeFound("novFee.notEquals=" + DEFAULT_NOV_FEE);

		// Get all the classFeeList where novFee not equals to UPDATED_NOV_FEE
		defaultClassFeeShouldBeFound("novFee.notEquals=" + UPDATED_NOV_FEE);
	}

	@Test
	@Transactional
	void getAllClassFeesByNovFeeIsInShouldWork() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where novFee in DEFAULT_NOV_FEE or UPDATED_NOV_FEE
		defaultClassFeeShouldBeFound("novFee.in=" + DEFAULT_NOV_FEE + "," + UPDATED_NOV_FEE);

		// Get all the classFeeList where novFee equals to UPDATED_NOV_FEE
		defaultClassFeeShouldNotBeFound("novFee.in=" + UPDATED_NOV_FEE);
	}

	@Test
	@Transactional
	void getAllClassFeesByNovFeeIsNullOrNotNull() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where novFee is not null
		defaultClassFeeShouldBeFound("novFee.specified=true");

		// Get all the classFeeList where novFee is null
		defaultClassFeeShouldNotBeFound("novFee.specified=false");
	}

	@Test
	@Transactional
	void getAllClassFeesByNovFeeIsGreaterThanOrEqualToSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where novFee is greater than or equal to
		// DEFAULT_NOV_FEE
		defaultClassFeeShouldBeFound("novFee.greaterThanOrEqual=" + DEFAULT_NOV_FEE);

		// Get all the classFeeList where novFee is greater than or equal to
		// UPDATED_NOV_FEE
		defaultClassFeeShouldNotBeFound("novFee.greaterThanOrEqual=" + UPDATED_NOV_FEE);
	}

	@Test
	@Transactional
	void getAllClassFeesByNovFeeIsLessThanOrEqualToSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where novFee is less than or equal to
		// DEFAULT_NOV_FEE
		defaultClassFeeShouldBeFound("novFee.lessThanOrEqual=" + DEFAULT_NOV_FEE);

		// Get all the classFeeList where novFee is less than or equal to
		// SMALLER_NOV_FEE
		defaultClassFeeShouldNotBeFound("novFee.lessThanOrEqual=" + SMALLER_NOV_FEE);
	}

	@Test
	@Transactional
	void getAllClassFeesByNovFeeIsLessThanSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where novFee is less than DEFAULT_NOV_FEE
		defaultClassFeeShouldNotBeFound("novFee.lessThan=" + DEFAULT_NOV_FEE);

		// Get all the classFeeList where novFee is less than UPDATED_NOV_FEE
		defaultClassFeeShouldBeFound("novFee.lessThan=" + UPDATED_NOV_FEE);
	}

	@Test
	@Transactional
	void getAllClassFeesByNovFeeIsGreaterThanSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where novFee is greater than DEFAULT_NOV_FEE
		defaultClassFeeShouldNotBeFound("novFee.greaterThan=" + DEFAULT_NOV_FEE);

		// Get all the classFeeList where novFee is greater than SMALLER_NOV_FEE
		defaultClassFeeShouldBeFound("novFee.greaterThan=" + SMALLER_NOV_FEE);
	}

	@Test
	@Transactional
	void getAllClassFeesByDecFeeIsEqualToSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where decFee equals to DEFAULT_DEC_FEE
		defaultClassFeeShouldBeFound("decFee.equals=" + DEFAULT_DEC_FEE);

		// Get all the classFeeList where decFee equals to UPDATED_DEC_FEE
		defaultClassFeeShouldNotBeFound("decFee.equals=" + UPDATED_DEC_FEE);
	}

	@Test
	@Transactional
	void getAllClassFeesByDecFeeIsNotEqualToSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where decFee not equals to DEFAULT_DEC_FEE
		defaultClassFeeShouldNotBeFound("decFee.notEquals=" + DEFAULT_DEC_FEE);

		// Get all the classFeeList where decFee not equals to UPDATED_DEC_FEE
		defaultClassFeeShouldBeFound("decFee.notEquals=" + UPDATED_DEC_FEE);
	}

	@Test
	@Transactional
	void getAllClassFeesByDecFeeIsInShouldWork() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where decFee in DEFAULT_DEC_FEE or UPDATED_DEC_FEE
		defaultClassFeeShouldBeFound("decFee.in=" + DEFAULT_DEC_FEE + "," + UPDATED_DEC_FEE);

		// Get all the classFeeList where decFee equals to UPDATED_DEC_FEE
		defaultClassFeeShouldNotBeFound("decFee.in=" + UPDATED_DEC_FEE);
	}

	@Test
	@Transactional
	void getAllClassFeesByDecFeeIsNullOrNotNull() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where decFee is not null
		defaultClassFeeShouldBeFound("decFee.specified=true");

		// Get all the classFeeList where decFee is null
		defaultClassFeeShouldNotBeFound("decFee.specified=false");
	}

	@Test
	@Transactional
	void getAllClassFeesByDecFeeIsGreaterThanOrEqualToSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where decFee is greater than or equal to
		// DEFAULT_DEC_FEE
		defaultClassFeeShouldBeFound("decFee.greaterThanOrEqual=" + DEFAULT_DEC_FEE);

		// Get all the classFeeList where decFee is greater than or equal to
		// UPDATED_DEC_FEE
		defaultClassFeeShouldNotBeFound("decFee.greaterThanOrEqual=" + UPDATED_DEC_FEE);
	}

	@Test
	@Transactional
	void getAllClassFeesByDecFeeIsLessThanOrEqualToSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where decFee is less than or equal to
		// DEFAULT_DEC_FEE
		defaultClassFeeShouldBeFound("decFee.lessThanOrEqual=" + DEFAULT_DEC_FEE);

		// Get all the classFeeList where decFee is less than or equal to
		// SMALLER_DEC_FEE
		defaultClassFeeShouldNotBeFound("decFee.lessThanOrEqual=" + SMALLER_DEC_FEE);
	}

	@Test
	@Transactional
	void getAllClassFeesByDecFeeIsLessThanSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where decFee is less than DEFAULT_DEC_FEE
		defaultClassFeeShouldNotBeFound("decFee.lessThan=" + DEFAULT_DEC_FEE);

		// Get all the classFeeList where decFee is less than UPDATED_DEC_FEE
		defaultClassFeeShouldBeFound("decFee.lessThan=" + UPDATED_DEC_FEE);
	}

	@Test
	@Transactional
	void getAllClassFeesByDecFeeIsGreaterThanSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where decFee is greater than DEFAULT_DEC_FEE
		defaultClassFeeShouldNotBeFound("decFee.greaterThan=" + DEFAULT_DEC_FEE);

		// Get all the classFeeList where decFee is greater than SMALLER_DEC_FEE
		defaultClassFeeShouldBeFound("decFee.greaterThan=" + SMALLER_DEC_FEE);
	}

	@Test
	@Transactional
	void getAllClassFeesByPayByDateIsEqualToSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where payByDate equals to DEFAULT_PAY_BY_DATE
		defaultClassFeeShouldBeFound("payByDate.equals=" + DEFAULT_PAY_BY_DATE);

		// Get all the classFeeList where payByDate equals to UPDATED_PAY_BY_DATE
		defaultClassFeeShouldNotBeFound("payByDate.equals=" + UPDATED_PAY_BY_DATE);
	}

	@Test
	@Transactional
	void getAllClassFeesByPayByDateIsNotEqualToSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where payByDate not equals to DEFAULT_PAY_BY_DATE
		defaultClassFeeShouldNotBeFound("payByDate.notEquals=" + DEFAULT_PAY_BY_DATE);

		// Get all the classFeeList where payByDate not equals to UPDATED_PAY_BY_DATE
		defaultClassFeeShouldBeFound("payByDate.notEquals=" + UPDATED_PAY_BY_DATE);
	}

	@Test
	@Transactional
	void getAllClassFeesByPayByDateIsInShouldWork() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where payByDate in DEFAULT_PAY_BY_DATE or
		// UPDATED_PAY_BY_DATE
		defaultClassFeeShouldBeFound("payByDate.in=" + DEFAULT_PAY_BY_DATE + "," + UPDATED_PAY_BY_DATE);

		// Get all the classFeeList where payByDate equals to UPDATED_PAY_BY_DATE
		defaultClassFeeShouldNotBeFound("payByDate.in=" + UPDATED_PAY_BY_DATE);
	}

	@Test
	@Transactional
	void getAllClassFeesByPayByDateIsNullOrNotNull() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where payByDate is not null
		defaultClassFeeShouldBeFound("payByDate.specified=true");

		// Get all the classFeeList where payByDate is null
		defaultClassFeeShouldNotBeFound("payByDate.specified=false");
	}

	@Test
	@Transactional
	void getAllClassFeesByPayByDateIsGreaterThanOrEqualToSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where payByDate is greater than or equal to
		// DEFAULT_PAY_BY_DATE
		defaultClassFeeShouldBeFound("payByDate.greaterThanOrEqual=" + DEFAULT_PAY_BY_DATE);

		// Get all the classFeeList where payByDate is greater than or equal to
		// UPDATED_PAY_BY_DATE
		defaultClassFeeShouldNotBeFound("payByDate.greaterThanOrEqual=" + UPDATED_PAY_BY_DATE);
	}

	@Test
	@Transactional
	void getAllClassFeesByPayByDateIsLessThanOrEqualToSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where payByDate is less than or equal to
		// DEFAULT_PAY_BY_DATE
		defaultClassFeeShouldBeFound("payByDate.lessThanOrEqual=" + DEFAULT_PAY_BY_DATE);

		// Get all the classFeeList where payByDate is less than or equal to
		// SMALLER_PAY_BY_DATE
		defaultClassFeeShouldNotBeFound("payByDate.lessThanOrEqual=" + SMALLER_PAY_BY_DATE);
	}

	@Test
	@Transactional
	void getAllClassFeesByPayByDateIsLessThanSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where payByDate is less than DEFAULT_PAY_BY_DATE
		defaultClassFeeShouldNotBeFound("payByDate.lessThan=" + DEFAULT_PAY_BY_DATE);

		// Get all the classFeeList where payByDate is less than UPDATED_PAY_BY_DATE
		defaultClassFeeShouldBeFound("payByDate.lessThan=" + UPDATED_PAY_BY_DATE);
	}

	@Test
	@Transactional
	void getAllClassFeesByPayByDateIsGreaterThanSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where payByDate is greater than DEFAULT_PAY_BY_DATE
		defaultClassFeeShouldNotBeFound("payByDate.greaterThan=" + DEFAULT_PAY_BY_DATE);

		// Get all the classFeeList where payByDate is greater than SMALLER_PAY_BY_DATE
		defaultClassFeeShouldBeFound("payByDate.greaterThan=" + SMALLER_PAY_BY_DATE);
	}

	@Test
	@Transactional
	void getAllClassFeesByCreateDateIsEqualToSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where createDate equals to DEFAULT_CREATE_DATE
		defaultClassFeeShouldBeFound("createDate.equals=" + DEFAULT_CREATE_DATE);

		// Get all the classFeeList where createDate equals to UPDATED_CREATE_DATE
		defaultClassFeeShouldNotBeFound("createDate.equals=" + UPDATED_CREATE_DATE);
	}

	@Test
	@Transactional
	void getAllClassFeesByCreateDateIsNotEqualToSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where createDate not equals to DEFAULT_CREATE_DATE
		defaultClassFeeShouldNotBeFound("createDate.notEquals=" + DEFAULT_CREATE_DATE);

		// Get all the classFeeList where createDate not equals to UPDATED_CREATE_DATE
		defaultClassFeeShouldBeFound("createDate.notEquals=" + UPDATED_CREATE_DATE);
	}

	@Test
	@Transactional
	void getAllClassFeesByCreateDateIsInShouldWork() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where createDate in DEFAULT_CREATE_DATE or
		// UPDATED_CREATE_DATE
		defaultClassFeeShouldBeFound("createDate.in=" + DEFAULT_CREATE_DATE + "," + UPDATED_CREATE_DATE);

		// Get all the classFeeList where createDate equals to UPDATED_CREATE_DATE
		defaultClassFeeShouldNotBeFound("createDate.in=" + UPDATED_CREATE_DATE);
	}

	@Test
	@Transactional
	void getAllClassFeesByCreateDateIsNullOrNotNull() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where createDate is not null
		defaultClassFeeShouldBeFound("createDate.specified=true");

		// Get all the classFeeList where createDate is null
		defaultClassFeeShouldNotBeFound("createDate.specified=false");
	}

	@Test
	@Transactional
	void getAllClassFeesByCreateDateIsGreaterThanOrEqualToSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where createDate is greater than or equal to
		// DEFAULT_CREATE_DATE
		defaultClassFeeShouldBeFound("createDate.greaterThanOrEqual=" + DEFAULT_CREATE_DATE);

		// Get all the classFeeList where createDate is greater than or equal to
		// UPDATED_CREATE_DATE
		defaultClassFeeShouldNotBeFound("createDate.greaterThanOrEqual=" + UPDATED_CREATE_DATE);
	}

	@Test
	@Transactional
	void getAllClassFeesByCreateDateIsLessThanOrEqualToSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where createDate is less than or equal to
		// DEFAULT_CREATE_DATE
		defaultClassFeeShouldBeFound("createDate.lessThanOrEqual=" + DEFAULT_CREATE_DATE);

		// Get all the classFeeList where createDate is less than or equal to
		// SMALLER_CREATE_DATE
		defaultClassFeeShouldNotBeFound("createDate.lessThanOrEqual=" + SMALLER_CREATE_DATE);
	}

	@Test
	@Transactional
	void getAllClassFeesByCreateDateIsLessThanSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where createDate is less than DEFAULT_CREATE_DATE
		defaultClassFeeShouldNotBeFound("createDate.lessThan=" + DEFAULT_CREATE_DATE);

		// Get all the classFeeList where createDate is less than UPDATED_CREATE_DATE
		defaultClassFeeShouldBeFound("createDate.lessThan=" + UPDATED_CREATE_DATE);
	}

	@Test
	@Transactional
	void getAllClassFeesByCreateDateIsGreaterThanSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where createDate is greater than DEFAULT_CREATE_DATE
		defaultClassFeeShouldNotBeFound("createDate.greaterThan=" + DEFAULT_CREATE_DATE);

		// Get all the classFeeList where createDate is greater than SMALLER_CREATE_DATE
		defaultClassFeeShouldBeFound("createDate.greaterThan=" + SMALLER_CREATE_DATE);
	}

	@Test
	@Transactional
	void getAllClassFeesByLastModifiedIsEqualToSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where lastModified equals to DEFAULT_LAST_MODIFIED
		defaultClassFeeShouldBeFound("lastModified.equals=" + DEFAULT_LAST_MODIFIED);

		// Get all the classFeeList where lastModified equals to UPDATED_LAST_MODIFIED
		defaultClassFeeShouldNotBeFound("lastModified.equals=" + UPDATED_LAST_MODIFIED);
	}

	@Test
	@Transactional
	void getAllClassFeesByLastModifiedIsNotEqualToSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where lastModified not equals to
		// DEFAULT_LAST_MODIFIED
		defaultClassFeeShouldNotBeFound("lastModified.notEquals=" + DEFAULT_LAST_MODIFIED);

		// Get all the classFeeList where lastModified not equals to
		// UPDATED_LAST_MODIFIED
		defaultClassFeeShouldBeFound("lastModified.notEquals=" + UPDATED_LAST_MODIFIED);
	}

	@Test
	@Transactional
	void getAllClassFeesByLastModifiedIsInShouldWork() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where lastModified in DEFAULT_LAST_MODIFIED or
		// UPDATED_LAST_MODIFIED
		defaultClassFeeShouldBeFound("lastModified.in=" + DEFAULT_LAST_MODIFIED + "," + UPDATED_LAST_MODIFIED);

		// Get all the classFeeList where lastModified equals to UPDATED_LAST_MODIFIED
		defaultClassFeeShouldNotBeFound("lastModified.in=" + UPDATED_LAST_MODIFIED);
	}

	@Test
	@Transactional
	void getAllClassFeesByLastModifiedIsNullOrNotNull() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where lastModified is not null
		defaultClassFeeShouldBeFound("lastModified.specified=true");

		// Get all the classFeeList where lastModified is null
		defaultClassFeeShouldNotBeFound("lastModified.specified=false");
	}

	@Test
	@Transactional
	void getAllClassFeesByLastModifiedIsGreaterThanOrEqualToSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where lastModified is greater than or equal to
		// DEFAULT_LAST_MODIFIED
		defaultClassFeeShouldBeFound("lastModified.greaterThanOrEqual=" + DEFAULT_LAST_MODIFIED);

		// Get all the classFeeList where lastModified is greater than or equal to
		// UPDATED_LAST_MODIFIED
		defaultClassFeeShouldNotBeFound("lastModified.greaterThanOrEqual=" + UPDATED_LAST_MODIFIED);
	}

	@Test
	@Transactional
	void getAllClassFeesByLastModifiedIsLessThanOrEqualToSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where lastModified is less than or equal to
		// DEFAULT_LAST_MODIFIED
		defaultClassFeeShouldBeFound("lastModified.lessThanOrEqual=" + DEFAULT_LAST_MODIFIED);

		// Get all the classFeeList where lastModified is less than or equal to
		// SMALLER_LAST_MODIFIED
		defaultClassFeeShouldNotBeFound("lastModified.lessThanOrEqual=" + SMALLER_LAST_MODIFIED);
	}

	@Test
	@Transactional
	void getAllClassFeesByLastModifiedIsLessThanSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where lastModified is less than
		// DEFAULT_LAST_MODIFIED
		defaultClassFeeShouldNotBeFound("lastModified.lessThan=" + DEFAULT_LAST_MODIFIED);

		// Get all the classFeeList where lastModified is less than
		// UPDATED_LAST_MODIFIED
		defaultClassFeeShouldBeFound("lastModified.lessThan=" + UPDATED_LAST_MODIFIED);
	}

	@Test
	@Transactional
	void getAllClassFeesByLastModifiedIsGreaterThanSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where lastModified is greater than
		// DEFAULT_LAST_MODIFIED
		defaultClassFeeShouldNotBeFound("lastModified.greaterThan=" + DEFAULT_LAST_MODIFIED);

		// Get all the classFeeList where lastModified is greater than
		// SMALLER_LAST_MODIFIED
		defaultClassFeeShouldBeFound("lastModified.greaterThan=" + SMALLER_LAST_MODIFIED);
	}

	@Test
	@Transactional
	void getAllClassFeesByCancelDateIsEqualToSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where cancelDate equals to DEFAULT_CANCEL_DATE
		defaultClassFeeShouldBeFound("cancelDate.equals=" + DEFAULT_CANCEL_DATE);

		// Get all the classFeeList where cancelDate equals to UPDATED_CANCEL_DATE
		defaultClassFeeShouldNotBeFound("cancelDate.equals=" + UPDATED_CANCEL_DATE);
	}

	@Test
	@Transactional
	void getAllClassFeesByCancelDateIsNotEqualToSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where cancelDate not equals to DEFAULT_CANCEL_DATE
		defaultClassFeeShouldNotBeFound("cancelDate.notEquals=" + DEFAULT_CANCEL_DATE);

		// Get all the classFeeList where cancelDate not equals to UPDATED_CANCEL_DATE
		defaultClassFeeShouldBeFound("cancelDate.notEquals=" + UPDATED_CANCEL_DATE);
	}

	@Test
	@Transactional
	void getAllClassFeesByCancelDateIsInShouldWork() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where cancelDate in DEFAULT_CANCEL_DATE or
		// UPDATED_CANCEL_DATE
		defaultClassFeeShouldBeFound("cancelDate.in=" + DEFAULT_CANCEL_DATE + "," + UPDATED_CANCEL_DATE);

		// Get all the classFeeList where cancelDate equals to UPDATED_CANCEL_DATE
		defaultClassFeeShouldNotBeFound("cancelDate.in=" + UPDATED_CANCEL_DATE);
	}

	@Test
	@Transactional
	void getAllClassFeesByCancelDateIsNullOrNotNull() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where cancelDate is not null
		defaultClassFeeShouldBeFound("cancelDate.specified=true");

		// Get all the classFeeList where cancelDate is null
		defaultClassFeeShouldNotBeFound("cancelDate.specified=false");
	}

	@Test
	@Transactional
	void getAllClassFeesByCancelDateIsGreaterThanOrEqualToSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where cancelDate is greater than or equal to
		// DEFAULT_CANCEL_DATE
		defaultClassFeeShouldBeFound("cancelDate.greaterThanOrEqual=" + DEFAULT_CANCEL_DATE);

		// Get all the classFeeList where cancelDate is greater than or equal to
		// UPDATED_CANCEL_DATE
		defaultClassFeeShouldNotBeFound("cancelDate.greaterThanOrEqual=" + UPDATED_CANCEL_DATE);
	}

	@Test
	@Transactional
	void getAllClassFeesByCancelDateIsLessThanOrEqualToSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where cancelDate is less than or equal to
		// DEFAULT_CANCEL_DATE
		defaultClassFeeShouldBeFound("cancelDate.lessThanOrEqual=" + DEFAULT_CANCEL_DATE);

		// Get all the classFeeList where cancelDate is less than or equal to
		// SMALLER_CANCEL_DATE
		defaultClassFeeShouldNotBeFound("cancelDate.lessThanOrEqual=" + SMALLER_CANCEL_DATE);
	}

	@Test
	@Transactional
	void getAllClassFeesByCancelDateIsLessThanSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where cancelDate is less than DEFAULT_CANCEL_DATE
		defaultClassFeeShouldNotBeFound("cancelDate.lessThan=" + DEFAULT_CANCEL_DATE);

		// Get all the classFeeList where cancelDate is less than UPDATED_CANCEL_DATE
		defaultClassFeeShouldBeFound("cancelDate.lessThan=" + UPDATED_CANCEL_DATE);
	}

	@Test
	@Transactional
	void getAllClassFeesByCancelDateIsGreaterThanSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		// Get all the classFeeList where cancelDate is greater than DEFAULT_CANCEL_DATE
		defaultClassFeeShouldNotBeFound("cancelDate.greaterThan=" + DEFAULT_CANCEL_DATE);

		// Get all the classFeeList where cancelDate is greater than SMALLER_CANCEL_DATE
		defaultClassFeeShouldBeFound("cancelDate.greaterThan=" + SMALLER_CANCEL_DATE);
	}

	@Test
	@Transactional
	void getAllClassFeesBySchoolClassIsEqualToSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);
		SchoolClass schoolClass = SchoolClassResourceIT.createEntity(em);
		em.persist(schoolClass);
		em.flush();
		classFee.addSchoolClass(schoolClass);
		classFeeRepository.saveAndFlush(classFee);
		Long schoolClassId = schoolClass.getId();

		// Get all the classFeeList where schoolClass equals to schoolClassId
		defaultClassFeeShouldBeFound("schoolClassId.equals=" + schoolClassId);

		// Get all the classFeeList where schoolClass equals to (schoolClassId + 1)
		defaultClassFeeShouldNotBeFound("schoolClassId.equals=" + (schoolClassId + 1));
	}

	@Test
	@Transactional
	void getAllClassFeesBySchoolLedgerHeadIsEqualToSomething() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);
		SchoolLedgerHead schoolLedgerHead = SchoolLedgerHeadResourceIT.createEntity(em);
		em.persist(schoolLedgerHead);
		em.flush();
		classFee.setSchoolLedgerHead(schoolLedgerHead);
		classFeeRepository.saveAndFlush(classFee);
		Long schoolLedgerHeadId = schoolLedgerHead.getId();

		// Get all the classFeeList where schoolLedgerHead equals to schoolLedgerHeadId
		defaultClassFeeShouldBeFound("schoolLedgerHeadId.equals=" + schoolLedgerHeadId);

		// Get all the classFeeList where schoolLedgerHead equals to (schoolLedgerHeadId
		// + 1)
		defaultClassFeeShouldNotBeFound("schoolLedgerHeadId.equals=" + (schoolLedgerHeadId + 1));
	}

	/**
	 * Executes the search, and checks that the default entity is returned.
	 */
	private void defaultClassFeeShouldBeFound(String filter) throws Exception {
		restClassFeeMockMvc.perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter)).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.[*].id").value(hasItem(classFee.getId().intValue())))
				.andExpect(jsonPath("$.[*].feeYear").value(hasItem(DEFAULT_FEE_YEAR.toString())))
				.andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE)))
				.andExpect(jsonPath("$.[*].janFee").value(hasItem(DEFAULT_JAN_FEE.doubleValue())))
				.andExpect(jsonPath("$.[*].febFee").value(hasItem(DEFAULT_FEB_FEE.doubleValue())))
				.andExpect(jsonPath("$.[*].marFee").value(hasItem(DEFAULT_MAR_FEE.doubleValue())))
				.andExpect(jsonPath("$.[*].aprFee").value(hasItem(DEFAULT_APR_FEE.doubleValue())))
				.andExpect(jsonPath("$.[*].mayFee").value(hasItem(DEFAULT_MAY_FEE.doubleValue())))
				.andExpect(jsonPath("$.[*].junFee").value(hasItem(DEFAULT_JUN_FEE.doubleValue())))
				.andExpect(jsonPath("$.[*].julFee").value(hasItem(DEFAULT_JUL_FEE.doubleValue())))
				.andExpect(jsonPath("$.[*].augFee").value(hasItem(DEFAULT_AUG_FEE.doubleValue())))
				.andExpect(jsonPath("$.[*].sepFee").value(hasItem(DEFAULT_SEP_FEE.doubleValue())))
				.andExpect(jsonPath("$.[*].octFee").value(hasItem(DEFAULT_OCT_FEE.doubleValue())))
				.andExpect(jsonPath("$.[*].novFee").value(hasItem(DEFAULT_NOV_FEE.doubleValue())))
				.andExpect(jsonPath("$.[*].decFee").value(hasItem(DEFAULT_DEC_FEE.doubleValue())))
				.andExpect(jsonPath("$.[*].payByDate").value(hasItem(DEFAULT_PAY_BY_DATE.toString())))
				.andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
				.andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
				.andExpect(jsonPath("$.[*].cancelDate").value(hasItem(DEFAULT_CANCEL_DATE.toString())));

		// Check, that the count call also returns 1
		restClassFeeMockMvc.perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter)).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(content().string("1"));
	}

	/**
	 * Executes the search, and checks that the default entity is not returned.
	 */
	private void defaultClassFeeShouldNotBeFound(String filter) throws Exception {
		restClassFeeMockMvc.perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter)).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$").isEmpty());

		// Check, that the count call also returns 0
		restClassFeeMockMvc.perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter)).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(content().string("0"));
	}

	@Test
	@Transactional
	void getNonExistingClassFee() throws Exception {
		// Get the classFee
		restClassFeeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
	}

	@Test
	@Transactional
	void putNewClassFee() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		int databaseSizeBeforeUpdate = classFeeRepository.findAll().size();

		// Update the classFee
		ClassFee updatedClassFee = classFeeRepository.findById(classFee.getId()).get();
		// Disconnect from session so that the updates on updatedClassFee are not
		// directly saved in db
		em.detach(updatedClassFee);
		updatedClassFee.feeYear(UPDATED_FEE_YEAR).dueDate(UPDATED_DUE_DATE).janFee(UPDATED_JAN_FEE)
				.febFee(UPDATED_FEB_FEE).marFee(UPDATED_MAR_FEE).aprFee(UPDATED_APR_FEE).mayFee(UPDATED_MAY_FEE)
				.junFee(UPDATED_JUN_FEE).julFee(UPDATED_JUL_FEE).augFee(UPDATED_AUG_FEE).sepFee(UPDATED_SEP_FEE)
				.octFee(UPDATED_OCT_FEE).novFee(UPDATED_NOV_FEE).decFee(UPDATED_DEC_FEE).payByDate(UPDATED_PAY_BY_DATE)
				.createDate(UPDATED_CREATE_DATE).lastModified(UPDATED_LAST_MODIFIED).cancelDate(UPDATED_CANCEL_DATE);
		ClassFeeDTO classFeeDTO = classFeeMapper.toDto(updatedClassFee);

		restClassFeeMockMvc.perform(put(ENTITY_API_URL_ID, classFeeDTO.getId()).contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(classFeeDTO))).andExpect(status().isOk());

		// Validate the ClassFee in the database
		List<ClassFee> classFeeList = classFeeRepository.findAll();
		assertThat(classFeeList).hasSize(databaseSizeBeforeUpdate);
		ClassFee testClassFee = classFeeList.get(classFeeList.size() - 1);
		assertThat(testClassFee.getFeeYear()).isEqualTo(UPDATED_FEE_YEAR);
		assertThat(testClassFee.getDueDate()).isEqualTo(UPDATED_DUE_DATE);
		assertThat(testClassFee.getJanFee()).isEqualTo(UPDATED_JAN_FEE);
		assertThat(testClassFee.getFebFee()).isEqualTo(UPDATED_FEB_FEE);
		assertThat(testClassFee.getMarFee()).isEqualTo(UPDATED_MAR_FEE);
		assertThat(testClassFee.getAprFee()).isEqualTo(UPDATED_APR_FEE);
		assertThat(testClassFee.getMayFee()).isEqualTo(UPDATED_MAY_FEE);
		assertThat(testClassFee.getJunFee()).isEqualTo(UPDATED_JUN_FEE);
		assertThat(testClassFee.getJulFee()).isEqualTo(UPDATED_JUL_FEE);
		assertThat(testClassFee.getAugFee()).isEqualTo(UPDATED_AUG_FEE);
		assertThat(testClassFee.getSepFee()).isEqualTo(UPDATED_SEP_FEE);
		assertThat(testClassFee.getOctFee()).isEqualTo(UPDATED_OCT_FEE);
		assertThat(testClassFee.getNovFee()).isEqualTo(UPDATED_NOV_FEE);
		assertThat(testClassFee.getDecFee()).isEqualTo(UPDATED_DEC_FEE);
		assertThat(testClassFee.getPayByDate()).isEqualTo(UPDATED_PAY_BY_DATE);
		assertThat(testClassFee.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
		assertThat(testClassFee.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
		assertThat(testClassFee.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
	}

	@Test
	@Transactional
	void putNonExistingClassFee() throws Exception {
		int databaseSizeBeforeUpdate = classFeeRepository.findAll().size();
		classFee.setId(count.incrementAndGet());

		// Create the ClassFee
		ClassFeeDTO classFeeDTO = classFeeMapper.toDto(classFee);

		// If the entity doesn't have an ID, it will throw BadRequestAlertException
		restClassFeeMockMvc.perform(put(ENTITY_API_URL_ID, classFeeDTO.getId()).contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(classFeeDTO))).andExpect(status().isBadRequest());

		// Validate the ClassFee in the database
		List<ClassFee> classFeeList = classFeeRepository.findAll();
		assertThat(classFeeList).hasSize(databaseSizeBeforeUpdate);
	}

	@Test
	@Transactional
	void putWithIdMismatchClassFee() throws Exception {
		int databaseSizeBeforeUpdate = classFeeRepository.findAll().size();
		classFee.setId(count.incrementAndGet());

		// Create the ClassFee
		ClassFeeDTO classFeeDTO = classFeeMapper.toDto(classFee);

		// If url ID doesn't match entity ID, it will throw BadRequestAlertException
		restClassFeeMockMvc.perform(put(ENTITY_API_URL_ID, count.incrementAndGet())
				.contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(classFeeDTO)))
				.andExpect(status().isBadRequest());

		// Validate the ClassFee in the database
		List<ClassFee> classFeeList = classFeeRepository.findAll();
		assertThat(classFeeList).hasSize(databaseSizeBeforeUpdate);
	}

	@Test
	@Transactional
	void putWithMissingIdPathParamClassFee() throws Exception {
		int databaseSizeBeforeUpdate = classFeeRepository.findAll().size();
		classFee.setId(count.incrementAndGet());

		// Create the ClassFee
		ClassFeeDTO classFeeDTO = classFeeMapper.toDto(classFee);

		// If url ID doesn't match entity ID, it will throw BadRequestAlertException
		restClassFeeMockMvc
				.perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON)
						.content(TestUtil.convertObjectToJsonBytes(classFeeDTO)))
				.andExpect(status().isMethodNotAllowed());

		// Validate the ClassFee in the database
		List<ClassFee> classFeeList = classFeeRepository.findAll();
		assertThat(classFeeList).hasSize(databaseSizeBeforeUpdate);
	}

	@Test
	@Transactional
	void partialUpdateClassFeeWithPatch() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		int databaseSizeBeforeUpdate = classFeeRepository.findAll().size();

		// Update the classFee using partial update
		ClassFee partialUpdatedClassFee = new ClassFee();
		partialUpdatedClassFee.setId(classFee.getId());

		partialUpdatedClassFee.dueDate(UPDATED_DUE_DATE).marFee(UPDATED_MAR_FEE).aprFee(UPDATED_APR_FEE)
				.junFee(UPDATED_JUN_FEE).julFee(UPDATED_JUL_FEE).augFee(UPDATED_AUG_FEE).sepFee(UPDATED_SEP_FEE)
				.novFee(UPDATED_NOV_FEE).payByDate(UPDATED_PAY_BY_DATE).lastModified(UPDATED_LAST_MODIFIED);

		restClassFeeMockMvc.perform(
				patch(ENTITY_API_URL_ID, partialUpdatedClassFee.getId()).contentType("application/merge-patch+json")
						.content(TestUtil.convertObjectToJsonBytes(partialUpdatedClassFee)))
				.andExpect(status().isOk());

		// Validate the ClassFee in the database
		List<ClassFee> classFeeList = classFeeRepository.findAll();
		assertThat(classFeeList).hasSize(databaseSizeBeforeUpdate);
		ClassFee testClassFee = classFeeList.get(classFeeList.size() - 1);
		assertThat(testClassFee.getFeeYear()).isEqualTo(DEFAULT_FEE_YEAR);
		assertThat(testClassFee.getDueDate()).isEqualTo(UPDATED_DUE_DATE);
		assertThat(testClassFee.getJanFee()).isEqualTo(DEFAULT_JAN_FEE);
		assertThat(testClassFee.getFebFee()).isEqualTo(DEFAULT_FEB_FEE);
		assertThat(testClassFee.getMarFee()).isEqualTo(UPDATED_MAR_FEE);
		assertThat(testClassFee.getAprFee()).isEqualTo(UPDATED_APR_FEE);
		assertThat(testClassFee.getMayFee()).isEqualTo(DEFAULT_MAY_FEE);
		assertThat(testClassFee.getJunFee()).isEqualTo(UPDATED_JUN_FEE);
		assertThat(testClassFee.getJulFee()).isEqualTo(UPDATED_JUL_FEE);
		assertThat(testClassFee.getAugFee()).isEqualTo(UPDATED_AUG_FEE);
		assertThat(testClassFee.getSepFee()).isEqualTo(UPDATED_SEP_FEE);
		assertThat(testClassFee.getOctFee()).isEqualTo(DEFAULT_OCT_FEE);
		assertThat(testClassFee.getNovFee()).isEqualTo(UPDATED_NOV_FEE);
		assertThat(testClassFee.getDecFee()).isEqualTo(DEFAULT_DEC_FEE);
		assertThat(testClassFee.getPayByDate()).isEqualTo(UPDATED_PAY_BY_DATE);
		assertThat(testClassFee.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
		assertThat(testClassFee.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
		assertThat(testClassFee.getCancelDate()).isEqualTo(DEFAULT_CANCEL_DATE);
	}

	@Test
	@Transactional
	void fullUpdateClassFeeWithPatch() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		int databaseSizeBeforeUpdate = classFeeRepository.findAll().size();

		// Update the classFee using partial update
		ClassFee partialUpdatedClassFee = new ClassFee();
		partialUpdatedClassFee.setId(classFee.getId());

		partialUpdatedClassFee.feeYear(UPDATED_FEE_YEAR).dueDate(UPDATED_DUE_DATE).janFee(UPDATED_JAN_FEE)
				.febFee(UPDATED_FEB_FEE).marFee(UPDATED_MAR_FEE).aprFee(UPDATED_APR_FEE).mayFee(UPDATED_MAY_FEE)
				.junFee(UPDATED_JUN_FEE).julFee(UPDATED_JUL_FEE).augFee(UPDATED_AUG_FEE).sepFee(UPDATED_SEP_FEE)
				.octFee(UPDATED_OCT_FEE).novFee(UPDATED_NOV_FEE).decFee(UPDATED_DEC_FEE).payByDate(UPDATED_PAY_BY_DATE)
				.createDate(UPDATED_CREATE_DATE).lastModified(UPDATED_LAST_MODIFIED).cancelDate(UPDATED_CANCEL_DATE);

		restClassFeeMockMvc.perform(
				patch(ENTITY_API_URL_ID, partialUpdatedClassFee.getId()).contentType("application/merge-patch+json")
						.content(TestUtil.convertObjectToJsonBytes(partialUpdatedClassFee)))
				.andExpect(status().isOk());

		// Validate the ClassFee in the database
		List<ClassFee> classFeeList = classFeeRepository.findAll();
		assertThat(classFeeList).hasSize(databaseSizeBeforeUpdate);
		ClassFee testClassFee = classFeeList.get(classFeeList.size() - 1);
		assertThat(testClassFee.getFeeYear()).isEqualTo(UPDATED_FEE_YEAR);
		assertThat(testClassFee.getDueDate()).isEqualTo(UPDATED_DUE_DATE);
		assertThat(testClassFee.getJanFee()).isEqualTo(UPDATED_JAN_FEE);
		assertThat(testClassFee.getFebFee()).isEqualTo(UPDATED_FEB_FEE);
		assertThat(testClassFee.getMarFee()).isEqualTo(UPDATED_MAR_FEE);
		assertThat(testClassFee.getAprFee()).isEqualTo(UPDATED_APR_FEE);
		assertThat(testClassFee.getMayFee()).isEqualTo(UPDATED_MAY_FEE);
		assertThat(testClassFee.getJunFee()).isEqualTo(UPDATED_JUN_FEE);
		assertThat(testClassFee.getJulFee()).isEqualTo(UPDATED_JUL_FEE);
		assertThat(testClassFee.getAugFee()).isEqualTo(UPDATED_AUG_FEE);
		assertThat(testClassFee.getSepFee()).isEqualTo(UPDATED_SEP_FEE);
		assertThat(testClassFee.getOctFee()).isEqualTo(UPDATED_OCT_FEE);
		assertThat(testClassFee.getNovFee()).isEqualTo(UPDATED_NOV_FEE);
		assertThat(testClassFee.getDecFee()).isEqualTo(UPDATED_DEC_FEE);
		assertThat(testClassFee.getPayByDate()).isEqualTo(UPDATED_PAY_BY_DATE);
		assertThat(testClassFee.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
		assertThat(testClassFee.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
		assertThat(testClassFee.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
	}

	@Test
	@Transactional
	void patchNonExistingClassFee() throws Exception {
		int databaseSizeBeforeUpdate = classFeeRepository.findAll().size();
		classFee.setId(count.incrementAndGet());

		// Create the ClassFee
		ClassFeeDTO classFeeDTO = classFeeMapper.toDto(classFee);

		// If the entity doesn't have an ID, it will throw BadRequestAlertException
		restClassFeeMockMvc.perform(patch(ENTITY_API_URL_ID, classFeeDTO.getId())
				.contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(classFeeDTO)))
				.andExpect(status().isBadRequest());

		// Validate the ClassFee in the database
		List<ClassFee> classFeeList = classFeeRepository.findAll();
		assertThat(classFeeList).hasSize(databaseSizeBeforeUpdate);
	}

	@Test
	@Transactional
	void patchWithIdMismatchClassFee() throws Exception {
		int databaseSizeBeforeUpdate = classFeeRepository.findAll().size();
		classFee.setId(count.incrementAndGet());

		// Create the ClassFee
		ClassFeeDTO classFeeDTO = classFeeMapper.toDto(classFee);

		// If url ID doesn't match entity ID, it will throw BadRequestAlertException
		restClassFeeMockMvc.perform(patch(ENTITY_API_URL_ID, count.incrementAndGet())
				.contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(classFeeDTO)))
				.andExpect(status().isBadRequest());

		// Validate the ClassFee in the database
		List<ClassFee> classFeeList = classFeeRepository.findAll();
		assertThat(classFeeList).hasSize(databaseSizeBeforeUpdate);
	}

	@Test
	@Transactional
	void patchWithMissingIdPathParamClassFee() throws Exception {
		int databaseSizeBeforeUpdate = classFeeRepository.findAll().size();
		classFee.setId(count.incrementAndGet());

		// Create the ClassFee
		ClassFeeDTO classFeeDTO = classFeeMapper.toDto(classFee);

		// If url ID doesn't match entity ID, it will throw BadRequestAlertException
		restClassFeeMockMvc
				.perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json")
						.content(TestUtil.convertObjectToJsonBytes(classFeeDTO)))
				.andExpect(status().isMethodNotAllowed());

		// Validate the ClassFee in the database
		List<ClassFee> classFeeList = classFeeRepository.findAll();
		assertThat(classFeeList).hasSize(databaseSizeBeforeUpdate);
	}

	@Test
	@Transactional
	void deleteClassFee() throws Exception {
		// Initialize the database
		classFeeRepository.saveAndFlush(classFee);

		int databaseSizeBeforeDelete = classFeeRepository.findAll().size();

		// Delete the classFee
		restClassFeeMockMvc.perform(delete(ENTITY_API_URL_ID, classFee.getId()).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());

		// Validate the database contains one less item
		List<ClassFee> classFeeList = classFeeRepository.findAll();
		assertThat(classFeeList).hasSize(databaseSizeBeforeDelete - 1);
	}
}
