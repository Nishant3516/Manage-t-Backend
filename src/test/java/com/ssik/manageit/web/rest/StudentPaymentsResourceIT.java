package com.ssik.manageit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ssik.manageit.IntegrationTest;
import com.ssik.manageit.domain.ClassStudent;
import com.ssik.manageit.domain.StudentPayments;
import com.ssik.manageit.domain.enumeration.ModeOfPayment;
import com.ssik.manageit.repository.StudentPaymentsRepository;
import com.ssik.manageit.service.criteria.StudentPaymentsCriteria;
import com.ssik.manageit.service.dto.StudentPaymentsDTO;
import com.ssik.manageit.service.mapper.StudentPaymentsMapper;
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
 * Integration tests for the {@link StudentPaymentsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StudentPaymentsResourceIT {

    private static final Double DEFAULT_AMOUNT_PAID = 1D;
    private static final Double UPDATED_AMOUNT_PAID = 2D;
    private static final Double SMALLER_AMOUNT_PAID = 1D - 1D;

    private static final ModeOfPayment DEFAULT_MODE_OF_PAY = ModeOfPayment.CASH;
    private static final ModeOfPayment UPDATED_MODE_OF_PAY = ModeOfPayment.UPI;

    private static final String DEFAULT_NOTE_NUMBERS = "AAAAAAAAAA";
    private static final String UPDATED_NOTE_NUMBERS = "BBBBBBBBBB";

    private static final String DEFAULT_UPI_ID = "AAAAAAAAAA";
    private static final String UPDATED_UPI_ID = "BBBBBBBBBB";

    private static final String DEFAULT_REMARKS = "AAAAAAAAAA";
    private static final String UPDATED_REMARKS = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_PAYMENT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PAYMENT_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_PAYMENT_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_RECEIPT_ID = "AAAAAAAAAA";
    private static final String UPDATED_RECEIPT_ID = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATE_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CREATE_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_LAST_MODIFIED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_MODIFIED = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_LAST_MODIFIED = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_CANCEL_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CANCEL_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CANCEL_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/student-payments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private StudentPaymentsRepository studentPaymentsRepository;

    @Autowired
    private StudentPaymentsMapper studentPaymentsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStudentPaymentsMockMvc;

    private StudentPayments studentPayments;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StudentPayments createEntity(EntityManager em) {
        StudentPayments studentPayments = new StudentPayments()
            .amountPaid(DEFAULT_AMOUNT_PAID)
            .modeOfPay(DEFAULT_MODE_OF_PAY)
            .noteNumbers(DEFAULT_NOTE_NUMBERS)
            .upiId(DEFAULT_UPI_ID)
            .remarks(DEFAULT_REMARKS)
            .paymentDate(DEFAULT_PAYMENT_DATE)
            .receiptId(DEFAULT_RECEIPT_ID)
            .createDate(DEFAULT_CREATE_DATE)
            .lastModified(DEFAULT_LAST_MODIFIED)
            .cancelDate(DEFAULT_CANCEL_DATE);
        return studentPayments;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StudentPayments createUpdatedEntity(EntityManager em) {
        StudentPayments studentPayments = new StudentPayments()
            .amountPaid(UPDATED_AMOUNT_PAID)
            .modeOfPay(UPDATED_MODE_OF_PAY)
            .noteNumbers(UPDATED_NOTE_NUMBERS)
            .upiId(UPDATED_UPI_ID)
            .remarks(UPDATED_REMARKS)
            .paymentDate(UPDATED_PAYMENT_DATE)
            .receiptId(UPDATED_RECEIPT_ID)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);
        return studentPayments;
    }

    @BeforeEach
    public void initTest() {
        studentPayments = createEntity(em);
    }

    @Test
    @Transactional
    void createStudentPayments() throws Exception {
        int databaseSizeBeforeCreate = studentPaymentsRepository.findAll().size();
        // Create the StudentPayments
        StudentPaymentsDTO studentPaymentsDTO = studentPaymentsMapper.toDto(studentPayments);
        restStudentPaymentsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(studentPaymentsDTO))
            )
            .andExpect(status().isCreated());

        // Validate the StudentPayments in the database
        List<StudentPayments> studentPaymentsList = studentPaymentsRepository.findAll();
        assertThat(studentPaymentsList).hasSize(databaseSizeBeforeCreate + 1);
        StudentPayments testStudentPayments = studentPaymentsList.get(studentPaymentsList.size() - 1);
        assertThat(testStudentPayments.getAmountPaid()).isEqualTo(DEFAULT_AMOUNT_PAID);
        assertThat(testStudentPayments.getModeOfPay()).isEqualTo(DEFAULT_MODE_OF_PAY);
        assertThat(testStudentPayments.getNoteNumbers()).isEqualTo(DEFAULT_NOTE_NUMBERS);
        assertThat(testStudentPayments.getUpiId()).isEqualTo(DEFAULT_UPI_ID);
        assertThat(testStudentPayments.getRemarks()).isEqualTo(DEFAULT_REMARKS);
        assertThat(testStudentPayments.getPaymentDate()).isEqualTo(DEFAULT_PAYMENT_DATE);
        assertThat(testStudentPayments.getReceiptId()).isEqualTo(DEFAULT_RECEIPT_ID);
        assertThat(testStudentPayments.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testStudentPayments.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
        assertThat(testStudentPayments.getCancelDate()).isEqualTo(DEFAULT_CANCEL_DATE);
    }

    @Test
    @Transactional
    void createStudentPaymentsWithExistingId() throws Exception {
        // Create the StudentPayments with an existing ID
        studentPayments.setId(1L);
        StudentPaymentsDTO studentPaymentsDTO = studentPaymentsMapper.toDto(studentPayments);

        int databaseSizeBeforeCreate = studentPaymentsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStudentPaymentsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(studentPaymentsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentPayments in the database
        List<StudentPayments> studentPaymentsList = studentPaymentsRepository.findAll();
        assertThat(studentPaymentsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAmountPaidIsRequired() throws Exception {
        int databaseSizeBeforeTest = studentPaymentsRepository.findAll().size();
        // set the field null
        studentPayments.setAmountPaid(null);

        // Create the StudentPayments, which fails.
        StudentPaymentsDTO studentPaymentsDTO = studentPaymentsMapper.toDto(studentPayments);

        restStudentPaymentsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(studentPaymentsDTO))
            )
            .andExpect(status().isBadRequest());

        List<StudentPayments> studentPaymentsList = studentPaymentsRepository.findAll();
        assertThat(studentPaymentsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllStudentPayments() throws Exception {
        // Initialize the database
        studentPaymentsRepository.saveAndFlush(studentPayments);

        // Get all the studentPaymentsList
        restStudentPaymentsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(studentPayments.getId().intValue())))
            .andExpect(jsonPath("$.[*].amountPaid").value(hasItem(DEFAULT_AMOUNT_PAID.doubleValue())))
            .andExpect(jsonPath("$.[*].modeOfPay").value(hasItem(DEFAULT_MODE_OF_PAY.toString())))
            .andExpect(jsonPath("$.[*].noteNumbers").value(hasItem(DEFAULT_NOTE_NUMBERS)))
            .andExpect(jsonPath("$.[*].upiId").value(hasItem(DEFAULT_UPI_ID)))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS)))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].receiptId").value(hasItem(DEFAULT_RECEIPT_ID)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].cancelDate").value(hasItem(DEFAULT_CANCEL_DATE.toString())));
    }

    @Test
    @Transactional
    void getStudentPayments() throws Exception {
        // Initialize the database
        studentPaymentsRepository.saveAndFlush(studentPayments);

        // Get the studentPayments
        restStudentPaymentsMockMvc
            .perform(get(ENTITY_API_URL_ID, studentPayments.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(studentPayments.getId().intValue()))
            .andExpect(jsonPath("$.amountPaid").value(DEFAULT_AMOUNT_PAID.doubleValue()))
            .andExpect(jsonPath("$.modeOfPay").value(DEFAULT_MODE_OF_PAY.toString()))
            .andExpect(jsonPath("$.noteNumbers").value(DEFAULT_NOTE_NUMBERS))
            .andExpect(jsonPath("$.upiId").value(DEFAULT_UPI_ID))
            .andExpect(jsonPath("$.remarks").value(DEFAULT_REMARKS))
            .andExpect(jsonPath("$.paymentDate").value(DEFAULT_PAYMENT_DATE.toString()))
            .andExpect(jsonPath("$.receiptId").value(DEFAULT_RECEIPT_ID))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.lastModified").value(DEFAULT_LAST_MODIFIED.toString()))
            .andExpect(jsonPath("$.cancelDate").value(DEFAULT_CANCEL_DATE.toString()));
    }

    @Test
    @Transactional
    void getStudentPaymentsByIdFiltering() throws Exception {
        // Initialize the database
        studentPaymentsRepository.saveAndFlush(studentPayments);

        Long id = studentPayments.getId();

        defaultStudentPaymentsShouldBeFound("id.equals=" + id);
        defaultStudentPaymentsShouldNotBeFound("id.notEquals=" + id);

        defaultStudentPaymentsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultStudentPaymentsShouldNotBeFound("id.greaterThan=" + id);

        defaultStudentPaymentsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultStudentPaymentsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllStudentPaymentsByAmountPaidIsEqualToSomething() throws Exception {
        // Initialize the database
        studentPaymentsRepository.saveAndFlush(studentPayments);

        // Get all the studentPaymentsList where amountPaid equals to DEFAULT_AMOUNT_PAID
        defaultStudentPaymentsShouldBeFound("amountPaid.equals=" + DEFAULT_AMOUNT_PAID);

        // Get all the studentPaymentsList where amountPaid equals to UPDATED_AMOUNT_PAID
        defaultStudentPaymentsShouldNotBeFound("amountPaid.equals=" + UPDATED_AMOUNT_PAID);
    }

    @Test
    @Transactional
    void getAllStudentPaymentsByAmountPaidIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentPaymentsRepository.saveAndFlush(studentPayments);

        // Get all the studentPaymentsList where amountPaid not equals to DEFAULT_AMOUNT_PAID
        defaultStudentPaymentsShouldNotBeFound("amountPaid.notEquals=" + DEFAULT_AMOUNT_PAID);

        // Get all the studentPaymentsList where amountPaid not equals to UPDATED_AMOUNT_PAID
        defaultStudentPaymentsShouldBeFound("amountPaid.notEquals=" + UPDATED_AMOUNT_PAID);
    }

    @Test
    @Transactional
    void getAllStudentPaymentsByAmountPaidIsInShouldWork() throws Exception {
        // Initialize the database
        studentPaymentsRepository.saveAndFlush(studentPayments);

        // Get all the studentPaymentsList where amountPaid in DEFAULT_AMOUNT_PAID or UPDATED_AMOUNT_PAID
        defaultStudentPaymentsShouldBeFound("amountPaid.in=" + DEFAULT_AMOUNT_PAID + "," + UPDATED_AMOUNT_PAID);

        // Get all the studentPaymentsList where amountPaid equals to UPDATED_AMOUNT_PAID
        defaultStudentPaymentsShouldNotBeFound("amountPaid.in=" + UPDATED_AMOUNT_PAID);
    }

    @Test
    @Transactional
    void getAllStudentPaymentsByAmountPaidIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentPaymentsRepository.saveAndFlush(studentPayments);

        // Get all the studentPaymentsList where amountPaid is not null
        defaultStudentPaymentsShouldBeFound("amountPaid.specified=true");

        // Get all the studentPaymentsList where amountPaid is null
        defaultStudentPaymentsShouldNotBeFound("amountPaid.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentPaymentsByAmountPaidIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        studentPaymentsRepository.saveAndFlush(studentPayments);

        // Get all the studentPaymentsList where amountPaid is greater than or equal to DEFAULT_AMOUNT_PAID
        defaultStudentPaymentsShouldBeFound("amountPaid.greaterThanOrEqual=" + DEFAULT_AMOUNT_PAID);

        // Get all the studentPaymentsList where amountPaid is greater than or equal to UPDATED_AMOUNT_PAID
        defaultStudentPaymentsShouldNotBeFound("amountPaid.greaterThanOrEqual=" + UPDATED_AMOUNT_PAID);
    }

    @Test
    @Transactional
    void getAllStudentPaymentsByAmountPaidIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        studentPaymentsRepository.saveAndFlush(studentPayments);

        // Get all the studentPaymentsList where amountPaid is less than or equal to DEFAULT_AMOUNT_PAID
        defaultStudentPaymentsShouldBeFound("amountPaid.lessThanOrEqual=" + DEFAULT_AMOUNT_PAID);

        // Get all the studentPaymentsList where amountPaid is less than or equal to SMALLER_AMOUNT_PAID
        defaultStudentPaymentsShouldNotBeFound("amountPaid.lessThanOrEqual=" + SMALLER_AMOUNT_PAID);
    }

    @Test
    @Transactional
    void getAllStudentPaymentsByAmountPaidIsLessThanSomething() throws Exception {
        // Initialize the database
        studentPaymentsRepository.saveAndFlush(studentPayments);

        // Get all the studentPaymentsList where amountPaid is less than DEFAULT_AMOUNT_PAID
        defaultStudentPaymentsShouldNotBeFound("amountPaid.lessThan=" + DEFAULT_AMOUNT_PAID);

        // Get all the studentPaymentsList where amountPaid is less than UPDATED_AMOUNT_PAID
        defaultStudentPaymentsShouldBeFound("amountPaid.lessThan=" + UPDATED_AMOUNT_PAID);
    }

    @Test
    @Transactional
    void getAllStudentPaymentsByAmountPaidIsGreaterThanSomething() throws Exception {
        // Initialize the database
        studentPaymentsRepository.saveAndFlush(studentPayments);

        // Get all the studentPaymentsList where amountPaid is greater than DEFAULT_AMOUNT_PAID
        defaultStudentPaymentsShouldNotBeFound("amountPaid.greaterThan=" + DEFAULT_AMOUNT_PAID);

        // Get all the studentPaymentsList where amountPaid is greater than SMALLER_AMOUNT_PAID
        defaultStudentPaymentsShouldBeFound("amountPaid.greaterThan=" + SMALLER_AMOUNT_PAID);
    }

    @Test
    @Transactional
    void getAllStudentPaymentsByModeOfPayIsEqualToSomething() throws Exception {
        // Initialize the database
        studentPaymentsRepository.saveAndFlush(studentPayments);

        // Get all the studentPaymentsList where modeOfPay equals to DEFAULT_MODE_OF_PAY
        defaultStudentPaymentsShouldBeFound("modeOfPay.equals=" + DEFAULT_MODE_OF_PAY);

        // Get all the studentPaymentsList where modeOfPay equals to UPDATED_MODE_OF_PAY
        defaultStudentPaymentsShouldNotBeFound("modeOfPay.equals=" + UPDATED_MODE_OF_PAY);
    }

    @Test
    @Transactional
    void getAllStudentPaymentsByModeOfPayIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentPaymentsRepository.saveAndFlush(studentPayments);

        // Get all the studentPaymentsList where modeOfPay not equals to DEFAULT_MODE_OF_PAY
        defaultStudentPaymentsShouldNotBeFound("modeOfPay.notEquals=" + DEFAULT_MODE_OF_PAY);

        // Get all the studentPaymentsList where modeOfPay not equals to UPDATED_MODE_OF_PAY
        defaultStudentPaymentsShouldBeFound("modeOfPay.notEquals=" + UPDATED_MODE_OF_PAY);
    }

    @Test
    @Transactional
    void getAllStudentPaymentsByModeOfPayIsInShouldWork() throws Exception {
        // Initialize the database
        studentPaymentsRepository.saveAndFlush(studentPayments);

        // Get all the studentPaymentsList where modeOfPay in DEFAULT_MODE_OF_PAY or UPDATED_MODE_OF_PAY
        defaultStudentPaymentsShouldBeFound("modeOfPay.in=" + DEFAULT_MODE_OF_PAY + "," + UPDATED_MODE_OF_PAY);

        // Get all the studentPaymentsList where modeOfPay equals to UPDATED_MODE_OF_PAY
        defaultStudentPaymentsShouldNotBeFound("modeOfPay.in=" + UPDATED_MODE_OF_PAY);
    }

    @Test
    @Transactional
    void getAllStudentPaymentsByModeOfPayIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentPaymentsRepository.saveAndFlush(studentPayments);

        // Get all the studentPaymentsList where modeOfPay is not null
        defaultStudentPaymentsShouldBeFound("modeOfPay.specified=true");

        // Get all the studentPaymentsList where modeOfPay is null
        defaultStudentPaymentsShouldNotBeFound("modeOfPay.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentPaymentsByNoteNumbersIsEqualToSomething() throws Exception {
        // Initialize the database
        studentPaymentsRepository.saveAndFlush(studentPayments);

        // Get all the studentPaymentsList where noteNumbers equals to DEFAULT_NOTE_NUMBERS
        defaultStudentPaymentsShouldBeFound("noteNumbers.equals=" + DEFAULT_NOTE_NUMBERS);

        // Get all the studentPaymentsList where noteNumbers equals to UPDATED_NOTE_NUMBERS
        defaultStudentPaymentsShouldNotBeFound("noteNumbers.equals=" + UPDATED_NOTE_NUMBERS);
    }

    @Test
    @Transactional
    void getAllStudentPaymentsByNoteNumbersIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentPaymentsRepository.saveAndFlush(studentPayments);

        // Get all the studentPaymentsList where noteNumbers not equals to DEFAULT_NOTE_NUMBERS
        defaultStudentPaymentsShouldNotBeFound("noteNumbers.notEquals=" + DEFAULT_NOTE_NUMBERS);

        // Get all the studentPaymentsList where noteNumbers not equals to UPDATED_NOTE_NUMBERS
        defaultStudentPaymentsShouldBeFound("noteNumbers.notEquals=" + UPDATED_NOTE_NUMBERS);
    }

    @Test
    @Transactional
    void getAllStudentPaymentsByNoteNumbersIsInShouldWork() throws Exception {
        // Initialize the database
        studentPaymentsRepository.saveAndFlush(studentPayments);

        // Get all the studentPaymentsList where noteNumbers in DEFAULT_NOTE_NUMBERS or UPDATED_NOTE_NUMBERS
        defaultStudentPaymentsShouldBeFound("noteNumbers.in=" + DEFAULT_NOTE_NUMBERS + "," + UPDATED_NOTE_NUMBERS);

        // Get all the studentPaymentsList where noteNumbers equals to UPDATED_NOTE_NUMBERS
        defaultStudentPaymentsShouldNotBeFound("noteNumbers.in=" + UPDATED_NOTE_NUMBERS);
    }

    @Test
    @Transactional
    void getAllStudentPaymentsByNoteNumbersIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentPaymentsRepository.saveAndFlush(studentPayments);

        // Get all the studentPaymentsList where noteNumbers is not null
        defaultStudentPaymentsShouldBeFound("noteNumbers.specified=true");

        // Get all the studentPaymentsList where noteNumbers is null
        defaultStudentPaymentsShouldNotBeFound("noteNumbers.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentPaymentsByNoteNumbersContainsSomething() throws Exception {
        // Initialize the database
        studentPaymentsRepository.saveAndFlush(studentPayments);

        // Get all the studentPaymentsList where noteNumbers contains DEFAULT_NOTE_NUMBERS
        defaultStudentPaymentsShouldBeFound("noteNumbers.contains=" + DEFAULT_NOTE_NUMBERS);

        // Get all the studentPaymentsList where noteNumbers contains UPDATED_NOTE_NUMBERS
        defaultStudentPaymentsShouldNotBeFound("noteNumbers.contains=" + UPDATED_NOTE_NUMBERS);
    }

    @Test
    @Transactional
    void getAllStudentPaymentsByNoteNumbersNotContainsSomething() throws Exception {
        // Initialize the database
        studentPaymentsRepository.saveAndFlush(studentPayments);

        // Get all the studentPaymentsList where noteNumbers does not contain DEFAULT_NOTE_NUMBERS
        defaultStudentPaymentsShouldNotBeFound("noteNumbers.doesNotContain=" + DEFAULT_NOTE_NUMBERS);

        // Get all the studentPaymentsList where noteNumbers does not contain UPDATED_NOTE_NUMBERS
        defaultStudentPaymentsShouldBeFound("noteNumbers.doesNotContain=" + UPDATED_NOTE_NUMBERS);
    }

    @Test
    @Transactional
    void getAllStudentPaymentsByUpiIdIsEqualToSomething() throws Exception {
        // Initialize the database
        studentPaymentsRepository.saveAndFlush(studentPayments);

        // Get all the studentPaymentsList where upiId equals to DEFAULT_UPI_ID
        defaultStudentPaymentsShouldBeFound("upiId.equals=" + DEFAULT_UPI_ID);

        // Get all the studentPaymentsList where upiId equals to UPDATED_UPI_ID
        defaultStudentPaymentsShouldNotBeFound("upiId.equals=" + UPDATED_UPI_ID);
    }

    @Test
    @Transactional
    void getAllStudentPaymentsByUpiIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentPaymentsRepository.saveAndFlush(studentPayments);

        // Get all the studentPaymentsList where upiId not equals to DEFAULT_UPI_ID
        defaultStudentPaymentsShouldNotBeFound("upiId.notEquals=" + DEFAULT_UPI_ID);

        // Get all the studentPaymentsList where upiId not equals to UPDATED_UPI_ID
        defaultStudentPaymentsShouldBeFound("upiId.notEquals=" + UPDATED_UPI_ID);
    }

    @Test
    @Transactional
    void getAllStudentPaymentsByUpiIdIsInShouldWork() throws Exception {
        // Initialize the database
        studentPaymentsRepository.saveAndFlush(studentPayments);

        // Get all the studentPaymentsList where upiId in DEFAULT_UPI_ID or UPDATED_UPI_ID
        defaultStudentPaymentsShouldBeFound("upiId.in=" + DEFAULT_UPI_ID + "," + UPDATED_UPI_ID);

        // Get all the studentPaymentsList where upiId equals to UPDATED_UPI_ID
        defaultStudentPaymentsShouldNotBeFound("upiId.in=" + UPDATED_UPI_ID);
    }

    @Test
    @Transactional
    void getAllStudentPaymentsByUpiIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentPaymentsRepository.saveAndFlush(studentPayments);

        // Get all the studentPaymentsList where upiId is not null
        defaultStudentPaymentsShouldBeFound("upiId.specified=true");

        // Get all the studentPaymentsList where upiId is null
        defaultStudentPaymentsShouldNotBeFound("upiId.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentPaymentsByUpiIdContainsSomething() throws Exception {
        // Initialize the database
        studentPaymentsRepository.saveAndFlush(studentPayments);

        // Get all the studentPaymentsList where upiId contains DEFAULT_UPI_ID
        defaultStudentPaymentsShouldBeFound("upiId.contains=" + DEFAULT_UPI_ID);

        // Get all the studentPaymentsList where upiId contains UPDATED_UPI_ID
        defaultStudentPaymentsShouldNotBeFound("upiId.contains=" + UPDATED_UPI_ID);
    }

    @Test
    @Transactional
    void getAllStudentPaymentsByUpiIdNotContainsSomething() throws Exception {
        // Initialize the database
        studentPaymentsRepository.saveAndFlush(studentPayments);

        // Get all the studentPaymentsList where upiId does not contain DEFAULT_UPI_ID
        defaultStudentPaymentsShouldNotBeFound("upiId.doesNotContain=" + DEFAULT_UPI_ID);

        // Get all the studentPaymentsList where upiId does not contain UPDATED_UPI_ID
        defaultStudentPaymentsShouldBeFound("upiId.doesNotContain=" + UPDATED_UPI_ID);
    }

    @Test
    @Transactional
    void getAllStudentPaymentsByRemarksIsEqualToSomething() throws Exception {
        // Initialize the database
        studentPaymentsRepository.saveAndFlush(studentPayments);

        // Get all the studentPaymentsList where remarks equals to DEFAULT_REMARKS
        defaultStudentPaymentsShouldBeFound("remarks.equals=" + DEFAULT_REMARKS);

        // Get all the studentPaymentsList where remarks equals to UPDATED_REMARKS
        defaultStudentPaymentsShouldNotBeFound("remarks.equals=" + UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void getAllStudentPaymentsByRemarksIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentPaymentsRepository.saveAndFlush(studentPayments);

        // Get all the studentPaymentsList where remarks not equals to DEFAULT_REMARKS
        defaultStudentPaymentsShouldNotBeFound("remarks.notEquals=" + DEFAULT_REMARKS);

        // Get all the studentPaymentsList where remarks not equals to UPDATED_REMARKS
        defaultStudentPaymentsShouldBeFound("remarks.notEquals=" + UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void getAllStudentPaymentsByRemarksIsInShouldWork() throws Exception {
        // Initialize the database
        studentPaymentsRepository.saveAndFlush(studentPayments);

        // Get all the studentPaymentsList where remarks in DEFAULT_REMARKS or UPDATED_REMARKS
        defaultStudentPaymentsShouldBeFound("remarks.in=" + DEFAULT_REMARKS + "," + UPDATED_REMARKS);

        // Get all the studentPaymentsList where remarks equals to UPDATED_REMARKS
        defaultStudentPaymentsShouldNotBeFound("remarks.in=" + UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void getAllStudentPaymentsByRemarksIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentPaymentsRepository.saveAndFlush(studentPayments);

        // Get all the studentPaymentsList where remarks is not null
        defaultStudentPaymentsShouldBeFound("remarks.specified=true");

        // Get all the studentPaymentsList where remarks is null
        defaultStudentPaymentsShouldNotBeFound("remarks.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentPaymentsByRemarksContainsSomething() throws Exception {
        // Initialize the database
        studentPaymentsRepository.saveAndFlush(studentPayments);

        // Get all the studentPaymentsList where remarks contains DEFAULT_REMARKS
        defaultStudentPaymentsShouldBeFound("remarks.contains=" + DEFAULT_REMARKS);

        // Get all the studentPaymentsList where remarks contains UPDATED_REMARKS
        defaultStudentPaymentsShouldNotBeFound("remarks.contains=" + UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void getAllStudentPaymentsByRemarksNotContainsSomething() throws Exception {
        // Initialize the database
        studentPaymentsRepository.saveAndFlush(studentPayments);

        // Get all the studentPaymentsList where remarks does not contain DEFAULT_REMARKS
        defaultStudentPaymentsShouldNotBeFound("remarks.doesNotContain=" + DEFAULT_REMARKS);

        // Get all the studentPaymentsList where remarks does not contain UPDATED_REMARKS
        defaultStudentPaymentsShouldBeFound("remarks.doesNotContain=" + UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void getAllStudentPaymentsByPaymentDateIsEqualToSomething() throws Exception {
        // Initialize the database
        studentPaymentsRepository.saveAndFlush(studentPayments);

        // Get all the studentPaymentsList where paymentDate equals to DEFAULT_PAYMENT_DATE
        defaultStudentPaymentsShouldBeFound("paymentDate.equals=" + DEFAULT_PAYMENT_DATE);

        // Get all the studentPaymentsList where paymentDate equals to UPDATED_PAYMENT_DATE
        defaultStudentPaymentsShouldNotBeFound("paymentDate.equals=" + UPDATED_PAYMENT_DATE);
    }

    @Test
    @Transactional
    void getAllStudentPaymentsByPaymentDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentPaymentsRepository.saveAndFlush(studentPayments);

        // Get all the studentPaymentsList where paymentDate not equals to DEFAULT_PAYMENT_DATE
        defaultStudentPaymentsShouldNotBeFound("paymentDate.notEquals=" + DEFAULT_PAYMENT_DATE);

        // Get all the studentPaymentsList where paymentDate not equals to UPDATED_PAYMENT_DATE
        defaultStudentPaymentsShouldBeFound("paymentDate.notEquals=" + UPDATED_PAYMENT_DATE);
    }

    @Test
    @Transactional
    void getAllStudentPaymentsByPaymentDateIsInShouldWork() throws Exception {
        // Initialize the database
        studentPaymentsRepository.saveAndFlush(studentPayments);

        // Get all the studentPaymentsList where paymentDate in DEFAULT_PAYMENT_DATE or UPDATED_PAYMENT_DATE
        defaultStudentPaymentsShouldBeFound("paymentDate.in=" + DEFAULT_PAYMENT_DATE + "," + UPDATED_PAYMENT_DATE);

        // Get all the studentPaymentsList where paymentDate equals to UPDATED_PAYMENT_DATE
        defaultStudentPaymentsShouldNotBeFound("paymentDate.in=" + UPDATED_PAYMENT_DATE);
    }

    @Test
    @Transactional
    void getAllStudentPaymentsByPaymentDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentPaymentsRepository.saveAndFlush(studentPayments);

        // Get all the studentPaymentsList where paymentDate is not null
        defaultStudentPaymentsShouldBeFound("paymentDate.specified=true");

        // Get all the studentPaymentsList where paymentDate is null
        defaultStudentPaymentsShouldNotBeFound("paymentDate.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentPaymentsByPaymentDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        studentPaymentsRepository.saveAndFlush(studentPayments);

        // Get all the studentPaymentsList where paymentDate is greater than or equal to DEFAULT_PAYMENT_DATE
        defaultStudentPaymentsShouldBeFound("paymentDate.greaterThanOrEqual=" + DEFAULT_PAYMENT_DATE);

        // Get all the studentPaymentsList where paymentDate is greater than or equal to UPDATED_PAYMENT_DATE
        defaultStudentPaymentsShouldNotBeFound("paymentDate.greaterThanOrEqual=" + UPDATED_PAYMENT_DATE);
    }

    @Test
    @Transactional
    void getAllStudentPaymentsByPaymentDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        studentPaymentsRepository.saveAndFlush(studentPayments);

        // Get all the studentPaymentsList where paymentDate is less than or equal to DEFAULT_PAYMENT_DATE
        defaultStudentPaymentsShouldBeFound("paymentDate.lessThanOrEqual=" + DEFAULT_PAYMENT_DATE);

        // Get all the studentPaymentsList where paymentDate is less than or equal to SMALLER_PAYMENT_DATE
        defaultStudentPaymentsShouldNotBeFound("paymentDate.lessThanOrEqual=" + SMALLER_PAYMENT_DATE);
    }

    @Test
    @Transactional
    void getAllStudentPaymentsByPaymentDateIsLessThanSomething() throws Exception {
        // Initialize the database
        studentPaymentsRepository.saveAndFlush(studentPayments);

        // Get all the studentPaymentsList where paymentDate is less than DEFAULT_PAYMENT_DATE
        defaultStudentPaymentsShouldNotBeFound("paymentDate.lessThan=" + DEFAULT_PAYMENT_DATE);

        // Get all the studentPaymentsList where paymentDate is less than UPDATED_PAYMENT_DATE
        defaultStudentPaymentsShouldBeFound("paymentDate.lessThan=" + UPDATED_PAYMENT_DATE);
    }

    @Test
    @Transactional
    void getAllStudentPaymentsByPaymentDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        studentPaymentsRepository.saveAndFlush(studentPayments);

        // Get all the studentPaymentsList where paymentDate is greater than DEFAULT_PAYMENT_DATE
        defaultStudentPaymentsShouldNotBeFound("paymentDate.greaterThan=" + DEFAULT_PAYMENT_DATE);

        // Get all the studentPaymentsList where paymentDate is greater than SMALLER_PAYMENT_DATE
        defaultStudentPaymentsShouldBeFound("paymentDate.greaterThan=" + SMALLER_PAYMENT_DATE);
    }

    @Test
    @Transactional
    void getAllStudentPaymentsByReceiptIdIsEqualToSomething() throws Exception {
        // Initialize the database
        studentPaymentsRepository.saveAndFlush(studentPayments);

        // Get all the studentPaymentsList where receiptId equals to DEFAULT_RECEIPT_ID
        defaultStudentPaymentsShouldBeFound("receiptId.equals=" + DEFAULT_RECEIPT_ID);

        // Get all the studentPaymentsList where receiptId equals to UPDATED_RECEIPT_ID
        defaultStudentPaymentsShouldNotBeFound("receiptId.equals=" + UPDATED_RECEIPT_ID);
    }

    @Test
    @Transactional
    void getAllStudentPaymentsByReceiptIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentPaymentsRepository.saveAndFlush(studentPayments);

        // Get all the studentPaymentsList where receiptId not equals to DEFAULT_RECEIPT_ID
        defaultStudentPaymentsShouldNotBeFound("receiptId.notEquals=" + DEFAULT_RECEIPT_ID);

        // Get all the studentPaymentsList where receiptId not equals to UPDATED_RECEIPT_ID
        defaultStudentPaymentsShouldBeFound("receiptId.notEquals=" + UPDATED_RECEIPT_ID);
    }

    @Test
    @Transactional
    void getAllStudentPaymentsByReceiptIdIsInShouldWork() throws Exception {
        // Initialize the database
        studentPaymentsRepository.saveAndFlush(studentPayments);

        // Get all the studentPaymentsList where receiptId in DEFAULT_RECEIPT_ID or UPDATED_RECEIPT_ID
        defaultStudentPaymentsShouldBeFound("receiptId.in=" + DEFAULT_RECEIPT_ID + "," + UPDATED_RECEIPT_ID);

        // Get all the studentPaymentsList where receiptId equals to UPDATED_RECEIPT_ID
        defaultStudentPaymentsShouldNotBeFound("receiptId.in=" + UPDATED_RECEIPT_ID);
    }

    @Test
    @Transactional
    void getAllStudentPaymentsByReceiptIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentPaymentsRepository.saveAndFlush(studentPayments);

        // Get all the studentPaymentsList where receiptId is not null
        defaultStudentPaymentsShouldBeFound("receiptId.specified=true");

        // Get all the studentPaymentsList where receiptId is null
        defaultStudentPaymentsShouldNotBeFound("receiptId.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentPaymentsByReceiptIdContainsSomething() throws Exception {
        // Initialize the database
        studentPaymentsRepository.saveAndFlush(studentPayments);

        // Get all the studentPaymentsList where receiptId contains DEFAULT_RECEIPT_ID
        defaultStudentPaymentsShouldBeFound("receiptId.contains=" + DEFAULT_RECEIPT_ID);

        // Get all the studentPaymentsList where receiptId contains UPDATED_RECEIPT_ID
        defaultStudentPaymentsShouldNotBeFound("receiptId.contains=" + UPDATED_RECEIPT_ID);
    }

    @Test
    @Transactional
    void getAllStudentPaymentsByReceiptIdNotContainsSomething() throws Exception {
        // Initialize the database
        studentPaymentsRepository.saveAndFlush(studentPayments);

        // Get all the studentPaymentsList where receiptId does not contain DEFAULT_RECEIPT_ID
        defaultStudentPaymentsShouldNotBeFound("receiptId.doesNotContain=" + DEFAULT_RECEIPT_ID);

        // Get all the studentPaymentsList where receiptId does not contain UPDATED_RECEIPT_ID
        defaultStudentPaymentsShouldBeFound("receiptId.doesNotContain=" + UPDATED_RECEIPT_ID);
    }

    @Test
    @Transactional
    void getAllStudentPaymentsByCreateDateIsEqualToSomething() throws Exception {
        // Initialize the database
        studentPaymentsRepository.saveAndFlush(studentPayments);

        // Get all the studentPaymentsList where createDate equals to DEFAULT_CREATE_DATE
        defaultStudentPaymentsShouldBeFound("createDate.equals=" + DEFAULT_CREATE_DATE);

        // Get all the studentPaymentsList where createDate equals to UPDATED_CREATE_DATE
        defaultStudentPaymentsShouldNotBeFound("createDate.equals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllStudentPaymentsByCreateDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentPaymentsRepository.saveAndFlush(studentPayments);

        // Get all the studentPaymentsList where createDate not equals to DEFAULT_CREATE_DATE
        defaultStudentPaymentsShouldNotBeFound("createDate.notEquals=" + DEFAULT_CREATE_DATE);

        // Get all the studentPaymentsList where createDate not equals to UPDATED_CREATE_DATE
        defaultStudentPaymentsShouldBeFound("createDate.notEquals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllStudentPaymentsByCreateDateIsInShouldWork() throws Exception {
        // Initialize the database
        studentPaymentsRepository.saveAndFlush(studentPayments);

        // Get all the studentPaymentsList where createDate in DEFAULT_CREATE_DATE or UPDATED_CREATE_DATE
        defaultStudentPaymentsShouldBeFound("createDate.in=" + DEFAULT_CREATE_DATE + "," + UPDATED_CREATE_DATE);

        // Get all the studentPaymentsList where createDate equals to UPDATED_CREATE_DATE
        defaultStudentPaymentsShouldNotBeFound("createDate.in=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllStudentPaymentsByCreateDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentPaymentsRepository.saveAndFlush(studentPayments);

        // Get all the studentPaymentsList where createDate is not null
        defaultStudentPaymentsShouldBeFound("createDate.specified=true");

        // Get all the studentPaymentsList where createDate is null
        defaultStudentPaymentsShouldNotBeFound("createDate.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentPaymentsByCreateDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        studentPaymentsRepository.saveAndFlush(studentPayments);

        // Get all the studentPaymentsList where createDate is greater than or equal to DEFAULT_CREATE_DATE
        defaultStudentPaymentsShouldBeFound("createDate.greaterThanOrEqual=" + DEFAULT_CREATE_DATE);

        // Get all the studentPaymentsList where createDate is greater than or equal to UPDATED_CREATE_DATE
        defaultStudentPaymentsShouldNotBeFound("createDate.greaterThanOrEqual=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllStudentPaymentsByCreateDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        studentPaymentsRepository.saveAndFlush(studentPayments);

        // Get all the studentPaymentsList where createDate is less than or equal to DEFAULT_CREATE_DATE
        defaultStudentPaymentsShouldBeFound("createDate.lessThanOrEqual=" + DEFAULT_CREATE_DATE);

        // Get all the studentPaymentsList where createDate is less than or equal to SMALLER_CREATE_DATE
        defaultStudentPaymentsShouldNotBeFound("createDate.lessThanOrEqual=" + SMALLER_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllStudentPaymentsByCreateDateIsLessThanSomething() throws Exception {
        // Initialize the database
        studentPaymentsRepository.saveAndFlush(studentPayments);

        // Get all the studentPaymentsList where createDate is less than DEFAULT_CREATE_DATE
        defaultStudentPaymentsShouldNotBeFound("createDate.lessThan=" + DEFAULT_CREATE_DATE);

        // Get all the studentPaymentsList where createDate is less than UPDATED_CREATE_DATE
        defaultStudentPaymentsShouldBeFound("createDate.lessThan=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllStudentPaymentsByCreateDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        studentPaymentsRepository.saveAndFlush(studentPayments);

        // Get all the studentPaymentsList where createDate is greater than DEFAULT_CREATE_DATE
        defaultStudentPaymentsShouldNotBeFound("createDate.greaterThan=" + DEFAULT_CREATE_DATE);

        // Get all the studentPaymentsList where createDate is greater than SMALLER_CREATE_DATE
        defaultStudentPaymentsShouldBeFound("createDate.greaterThan=" + SMALLER_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllStudentPaymentsByLastModifiedIsEqualToSomething() throws Exception {
        // Initialize the database
        studentPaymentsRepository.saveAndFlush(studentPayments);

        // Get all the studentPaymentsList where lastModified equals to DEFAULT_LAST_MODIFIED
        defaultStudentPaymentsShouldBeFound("lastModified.equals=" + DEFAULT_LAST_MODIFIED);

        // Get all the studentPaymentsList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultStudentPaymentsShouldNotBeFound("lastModified.equals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllStudentPaymentsByLastModifiedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentPaymentsRepository.saveAndFlush(studentPayments);

        // Get all the studentPaymentsList where lastModified not equals to DEFAULT_LAST_MODIFIED
        defaultStudentPaymentsShouldNotBeFound("lastModified.notEquals=" + DEFAULT_LAST_MODIFIED);

        // Get all the studentPaymentsList where lastModified not equals to UPDATED_LAST_MODIFIED
        defaultStudentPaymentsShouldBeFound("lastModified.notEquals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllStudentPaymentsByLastModifiedIsInShouldWork() throws Exception {
        // Initialize the database
        studentPaymentsRepository.saveAndFlush(studentPayments);

        // Get all the studentPaymentsList where lastModified in DEFAULT_LAST_MODIFIED or UPDATED_LAST_MODIFIED
        defaultStudentPaymentsShouldBeFound("lastModified.in=" + DEFAULT_LAST_MODIFIED + "," + UPDATED_LAST_MODIFIED);

        // Get all the studentPaymentsList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultStudentPaymentsShouldNotBeFound("lastModified.in=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllStudentPaymentsByLastModifiedIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentPaymentsRepository.saveAndFlush(studentPayments);

        // Get all the studentPaymentsList where lastModified is not null
        defaultStudentPaymentsShouldBeFound("lastModified.specified=true");

        // Get all the studentPaymentsList where lastModified is null
        defaultStudentPaymentsShouldNotBeFound("lastModified.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentPaymentsByLastModifiedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        studentPaymentsRepository.saveAndFlush(studentPayments);

        // Get all the studentPaymentsList where lastModified is greater than or equal to DEFAULT_LAST_MODIFIED
        defaultStudentPaymentsShouldBeFound("lastModified.greaterThanOrEqual=" + DEFAULT_LAST_MODIFIED);

        // Get all the studentPaymentsList where lastModified is greater than or equal to UPDATED_LAST_MODIFIED
        defaultStudentPaymentsShouldNotBeFound("lastModified.greaterThanOrEqual=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllStudentPaymentsByLastModifiedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        studentPaymentsRepository.saveAndFlush(studentPayments);

        // Get all the studentPaymentsList where lastModified is less than or equal to DEFAULT_LAST_MODIFIED
        defaultStudentPaymentsShouldBeFound("lastModified.lessThanOrEqual=" + DEFAULT_LAST_MODIFIED);

        // Get all the studentPaymentsList where lastModified is less than or equal to SMALLER_LAST_MODIFIED
        defaultStudentPaymentsShouldNotBeFound("lastModified.lessThanOrEqual=" + SMALLER_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllStudentPaymentsByLastModifiedIsLessThanSomething() throws Exception {
        // Initialize the database
        studentPaymentsRepository.saveAndFlush(studentPayments);

        // Get all the studentPaymentsList where lastModified is less than DEFAULT_LAST_MODIFIED
        defaultStudentPaymentsShouldNotBeFound("lastModified.lessThan=" + DEFAULT_LAST_MODIFIED);

        // Get all the studentPaymentsList where lastModified is less than UPDATED_LAST_MODIFIED
        defaultStudentPaymentsShouldBeFound("lastModified.lessThan=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllStudentPaymentsByLastModifiedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        studentPaymentsRepository.saveAndFlush(studentPayments);

        // Get all the studentPaymentsList where lastModified is greater than DEFAULT_LAST_MODIFIED
        defaultStudentPaymentsShouldNotBeFound("lastModified.greaterThan=" + DEFAULT_LAST_MODIFIED);

        // Get all the studentPaymentsList where lastModified is greater than SMALLER_LAST_MODIFIED
        defaultStudentPaymentsShouldBeFound("lastModified.greaterThan=" + SMALLER_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllStudentPaymentsByCancelDateIsEqualToSomething() throws Exception {
        // Initialize the database
        studentPaymentsRepository.saveAndFlush(studentPayments);

        // Get all the studentPaymentsList where cancelDate equals to DEFAULT_CANCEL_DATE
        defaultStudentPaymentsShouldBeFound("cancelDate.equals=" + DEFAULT_CANCEL_DATE);

        // Get all the studentPaymentsList where cancelDate equals to UPDATED_CANCEL_DATE
        defaultStudentPaymentsShouldNotBeFound("cancelDate.equals=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllStudentPaymentsByCancelDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentPaymentsRepository.saveAndFlush(studentPayments);

        // Get all the studentPaymentsList where cancelDate not equals to DEFAULT_CANCEL_DATE
        defaultStudentPaymentsShouldNotBeFound("cancelDate.notEquals=" + DEFAULT_CANCEL_DATE);

        // Get all the studentPaymentsList where cancelDate not equals to UPDATED_CANCEL_DATE
        defaultStudentPaymentsShouldBeFound("cancelDate.notEquals=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllStudentPaymentsByCancelDateIsInShouldWork() throws Exception {
        // Initialize the database
        studentPaymentsRepository.saveAndFlush(studentPayments);

        // Get all the studentPaymentsList where cancelDate in DEFAULT_CANCEL_DATE or UPDATED_CANCEL_DATE
        defaultStudentPaymentsShouldBeFound("cancelDate.in=" + DEFAULT_CANCEL_DATE + "," + UPDATED_CANCEL_DATE);

        // Get all the studentPaymentsList where cancelDate equals to UPDATED_CANCEL_DATE
        defaultStudentPaymentsShouldNotBeFound("cancelDate.in=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllStudentPaymentsByCancelDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentPaymentsRepository.saveAndFlush(studentPayments);

        // Get all the studentPaymentsList where cancelDate is not null
        defaultStudentPaymentsShouldBeFound("cancelDate.specified=true");

        // Get all the studentPaymentsList where cancelDate is null
        defaultStudentPaymentsShouldNotBeFound("cancelDate.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentPaymentsByCancelDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        studentPaymentsRepository.saveAndFlush(studentPayments);

        // Get all the studentPaymentsList where cancelDate is greater than or equal to DEFAULT_CANCEL_DATE
        defaultStudentPaymentsShouldBeFound("cancelDate.greaterThanOrEqual=" + DEFAULT_CANCEL_DATE);

        // Get all the studentPaymentsList where cancelDate is greater than or equal to UPDATED_CANCEL_DATE
        defaultStudentPaymentsShouldNotBeFound("cancelDate.greaterThanOrEqual=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllStudentPaymentsByCancelDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        studentPaymentsRepository.saveAndFlush(studentPayments);

        // Get all the studentPaymentsList where cancelDate is less than or equal to DEFAULT_CANCEL_DATE
        defaultStudentPaymentsShouldBeFound("cancelDate.lessThanOrEqual=" + DEFAULT_CANCEL_DATE);

        // Get all the studentPaymentsList where cancelDate is less than or equal to SMALLER_CANCEL_DATE
        defaultStudentPaymentsShouldNotBeFound("cancelDate.lessThanOrEqual=" + SMALLER_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllStudentPaymentsByCancelDateIsLessThanSomething() throws Exception {
        // Initialize the database
        studentPaymentsRepository.saveAndFlush(studentPayments);

        // Get all the studentPaymentsList where cancelDate is less than DEFAULT_CANCEL_DATE
        defaultStudentPaymentsShouldNotBeFound("cancelDate.lessThan=" + DEFAULT_CANCEL_DATE);

        // Get all the studentPaymentsList where cancelDate is less than UPDATED_CANCEL_DATE
        defaultStudentPaymentsShouldBeFound("cancelDate.lessThan=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllStudentPaymentsByCancelDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        studentPaymentsRepository.saveAndFlush(studentPayments);

        // Get all the studentPaymentsList where cancelDate is greater than DEFAULT_CANCEL_DATE
        defaultStudentPaymentsShouldNotBeFound("cancelDate.greaterThan=" + DEFAULT_CANCEL_DATE);

        // Get all the studentPaymentsList where cancelDate is greater than SMALLER_CANCEL_DATE
        defaultStudentPaymentsShouldBeFound("cancelDate.greaterThan=" + SMALLER_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllStudentPaymentsByClassStudentIsEqualToSomething() throws Exception {
        // Initialize the database
        studentPaymentsRepository.saveAndFlush(studentPayments);
        ClassStudent classStudent = ClassStudentResourceIT.createEntity(em);
        em.persist(classStudent);
        em.flush();
        studentPayments.setClassStudent(classStudent);
        studentPaymentsRepository.saveAndFlush(studentPayments);
        Long classStudentId = classStudent.getId();

        // Get all the studentPaymentsList where classStudent equals to classStudentId
        defaultStudentPaymentsShouldBeFound("classStudentId.equals=" + classStudentId);

        // Get all the studentPaymentsList where classStudent equals to (classStudentId + 1)
        defaultStudentPaymentsShouldNotBeFound("classStudentId.equals=" + (classStudentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultStudentPaymentsShouldBeFound(String filter) throws Exception {
        restStudentPaymentsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(studentPayments.getId().intValue())))
            .andExpect(jsonPath("$.[*].amountPaid").value(hasItem(DEFAULT_AMOUNT_PAID.doubleValue())))
            .andExpect(jsonPath("$.[*].modeOfPay").value(hasItem(DEFAULT_MODE_OF_PAY.toString())))
            .andExpect(jsonPath("$.[*].noteNumbers").value(hasItem(DEFAULT_NOTE_NUMBERS)))
            .andExpect(jsonPath("$.[*].upiId").value(hasItem(DEFAULT_UPI_ID)))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS)))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].receiptId").value(hasItem(DEFAULT_RECEIPT_ID)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].cancelDate").value(hasItem(DEFAULT_CANCEL_DATE.toString())));

        // Check, that the count call also returns 1
        restStudentPaymentsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultStudentPaymentsShouldNotBeFound(String filter) throws Exception {
        restStudentPaymentsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restStudentPaymentsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingStudentPayments() throws Exception {
        // Get the studentPayments
        restStudentPaymentsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewStudentPayments() throws Exception {
        // Initialize the database
        studentPaymentsRepository.saveAndFlush(studentPayments);

        int databaseSizeBeforeUpdate = studentPaymentsRepository.findAll().size();

        // Update the studentPayments
        StudentPayments updatedStudentPayments = studentPaymentsRepository.findById(studentPayments.getId()).get();
        // Disconnect from session so that the updates on updatedStudentPayments are not directly saved in db
        em.detach(updatedStudentPayments);
        updatedStudentPayments
            .amountPaid(UPDATED_AMOUNT_PAID)
            .modeOfPay(UPDATED_MODE_OF_PAY)
            .noteNumbers(UPDATED_NOTE_NUMBERS)
            .upiId(UPDATED_UPI_ID)
            .remarks(UPDATED_REMARKS)
            .paymentDate(UPDATED_PAYMENT_DATE)
            .receiptId(UPDATED_RECEIPT_ID)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);
        StudentPaymentsDTO studentPaymentsDTO = studentPaymentsMapper.toDto(updatedStudentPayments);

        restStudentPaymentsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, studentPaymentsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(studentPaymentsDTO))
            )
            .andExpect(status().isOk());

        // Validate the StudentPayments in the database
        List<StudentPayments> studentPaymentsList = studentPaymentsRepository.findAll();
        assertThat(studentPaymentsList).hasSize(databaseSizeBeforeUpdate);
        StudentPayments testStudentPayments = studentPaymentsList.get(studentPaymentsList.size() - 1);
        assertThat(testStudentPayments.getAmountPaid()).isEqualTo(UPDATED_AMOUNT_PAID);
        assertThat(testStudentPayments.getModeOfPay()).isEqualTo(UPDATED_MODE_OF_PAY);
        assertThat(testStudentPayments.getNoteNumbers()).isEqualTo(UPDATED_NOTE_NUMBERS);
        assertThat(testStudentPayments.getUpiId()).isEqualTo(UPDATED_UPI_ID);
        assertThat(testStudentPayments.getRemarks()).isEqualTo(UPDATED_REMARKS);
        assertThat(testStudentPayments.getPaymentDate()).isEqualTo(UPDATED_PAYMENT_DATE);
        assertThat(testStudentPayments.getReceiptId()).isEqualTo(UPDATED_RECEIPT_ID);
        assertThat(testStudentPayments.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testStudentPayments.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testStudentPayments.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void putNonExistingStudentPayments() throws Exception {
        int databaseSizeBeforeUpdate = studentPaymentsRepository.findAll().size();
        studentPayments.setId(count.incrementAndGet());

        // Create the StudentPayments
        StudentPaymentsDTO studentPaymentsDTO = studentPaymentsMapper.toDto(studentPayments);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudentPaymentsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, studentPaymentsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(studentPaymentsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentPayments in the database
        List<StudentPayments> studentPaymentsList = studentPaymentsRepository.findAll();
        assertThat(studentPaymentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStudentPayments() throws Exception {
        int databaseSizeBeforeUpdate = studentPaymentsRepository.findAll().size();
        studentPayments.setId(count.incrementAndGet());

        // Create the StudentPayments
        StudentPaymentsDTO studentPaymentsDTO = studentPaymentsMapper.toDto(studentPayments);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentPaymentsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(studentPaymentsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentPayments in the database
        List<StudentPayments> studentPaymentsList = studentPaymentsRepository.findAll();
        assertThat(studentPaymentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStudentPayments() throws Exception {
        int databaseSizeBeforeUpdate = studentPaymentsRepository.findAll().size();
        studentPayments.setId(count.incrementAndGet());

        // Create the StudentPayments
        StudentPaymentsDTO studentPaymentsDTO = studentPaymentsMapper.toDto(studentPayments);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentPaymentsMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(studentPaymentsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StudentPayments in the database
        List<StudentPayments> studentPaymentsList = studentPaymentsRepository.findAll();
        assertThat(studentPaymentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStudentPaymentsWithPatch() throws Exception {
        // Initialize the database
        studentPaymentsRepository.saveAndFlush(studentPayments);

        int databaseSizeBeforeUpdate = studentPaymentsRepository.findAll().size();

        // Update the studentPayments using partial update
        StudentPayments partialUpdatedStudentPayments = new StudentPayments();
        partialUpdatedStudentPayments.setId(studentPayments.getId());

        partialUpdatedStudentPayments
            .amountPaid(UPDATED_AMOUNT_PAID)
            .upiId(UPDATED_UPI_ID)
            .createDate(UPDATED_CREATE_DATE)
            .cancelDate(UPDATED_CANCEL_DATE);

        restStudentPaymentsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStudentPayments.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStudentPayments))
            )
            .andExpect(status().isOk());

        // Validate the StudentPayments in the database
        List<StudentPayments> studentPaymentsList = studentPaymentsRepository.findAll();
        assertThat(studentPaymentsList).hasSize(databaseSizeBeforeUpdate);
        StudentPayments testStudentPayments = studentPaymentsList.get(studentPaymentsList.size() - 1);
        assertThat(testStudentPayments.getAmountPaid()).isEqualTo(UPDATED_AMOUNT_PAID);
        assertThat(testStudentPayments.getModeOfPay()).isEqualTo(DEFAULT_MODE_OF_PAY);
        assertThat(testStudentPayments.getNoteNumbers()).isEqualTo(DEFAULT_NOTE_NUMBERS);
        assertThat(testStudentPayments.getUpiId()).isEqualTo(UPDATED_UPI_ID);
        assertThat(testStudentPayments.getRemarks()).isEqualTo(DEFAULT_REMARKS);
        assertThat(testStudentPayments.getPaymentDate()).isEqualTo(DEFAULT_PAYMENT_DATE);
        assertThat(testStudentPayments.getReceiptId()).isEqualTo(DEFAULT_RECEIPT_ID);
        assertThat(testStudentPayments.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testStudentPayments.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
        assertThat(testStudentPayments.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void fullUpdateStudentPaymentsWithPatch() throws Exception {
        // Initialize the database
        studentPaymentsRepository.saveAndFlush(studentPayments);

        int databaseSizeBeforeUpdate = studentPaymentsRepository.findAll().size();

        // Update the studentPayments using partial update
        StudentPayments partialUpdatedStudentPayments = new StudentPayments();
        partialUpdatedStudentPayments.setId(studentPayments.getId());

        partialUpdatedStudentPayments
            .amountPaid(UPDATED_AMOUNT_PAID)
            .modeOfPay(UPDATED_MODE_OF_PAY)
            .noteNumbers(UPDATED_NOTE_NUMBERS)
            .upiId(UPDATED_UPI_ID)
            .remarks(UPDATED_REMARKS)
            .paymentDate(UPDATED_PAYMENT_DATE)
            .receiptId(UPDATED_RECEIPT_ID)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE);

        restStudentPaymentsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStudentPayments.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStudentPayments))
            )
            .andExpect(status().isOk());

        // Validate the StudentPayments in the database
        List<StudentPayments> studentPaymentsList = studentPaymentsRepository.findAll();
        assertThat(studentPaymentsList).hasSize(databaseSizeBeforeUpdate);
        StudentPayments testStudentPayments = studentPaymentsList.get(studentPaymentsList.size() - 1);
        assertThat(testStudentPayments.getAmountPaid()).isEqualTo(UPDATED_AMOUNT_PAID);
        assertThat(testStudentPayments.getModeOfPay()).isEqualTo(UPDATED_MODE_OF_PAY);
        assertThat(testStudentPayments.getNoteNumbers()).isEqualTo(UPDATED_NOTE_NUMBERS);
        assertThat(testStudentPayments.getUpiId()).isEqualTo(UPDATED_UPI_ID);
        assertThat(testStudentPayments.getRemarks()).isEqualTo(UPDATED_REMARKS);
        assertThat(testStudentPayments.getPaymentDate()).isEqualTo(UPDATED_PAYMENT_DATE);
        assertThat(testStudentPayments.getReceiptId()).isEqualTo(UPDATED_RECEIPT_ID);
        assertThat(testStudentPayments.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testStudentPayments.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testStudentPayments.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingStudentPayments() throws Exception {
        int databaseSizeBeforeUpdate = studentPaymentsRepository.findAll().size();
        studentPayments.setId(count.incrementAndGet());

        // Create the StudentPayments
        StudentPaymentsDTO studentPaymentsDTO = studentPaymentsMapper.toDto(studentPayments);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudentPaymentsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, studentPaymentsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(studentPaymentsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentPayments in the database
        List<StudentPayments> studentPaymentsList = studentPaymentsRepository.findAll();
        assertThat(studentPaymentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStudentPayments() throws Exception {
        int databaseSizeBeforeUpdate = studentPaymentsRepository.findAll().size();
        studentPayments.setId(count.incrementAndGet());

        // Create the StudentPayments
        StudentPaymentsDTO studentPaymentsDTO = studentPaymentsMapper.toDto(studentPayments);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentPaymentsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(studentPaymentsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentPayments in the database
        List<StudentPayments> studentPaymentsList = studentPaymentsRepository.findAll();
        assertThat(studentPaymentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStudentPayments() throws Exception {
        int databaseSizeBeforeUpdate = studentPaymentsRepository.findAll().size();
        studentPayments.setId(count.incrementAndGet());

        // Create the StudentPayments
        StudentPaymentsDTO studentPaymentsDTO = studentPaymentsMapper.toDto(studentPayments);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentPaymentsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(studentPaymentsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StudentPayments in the database
        List<StudentPayments> studentPaymentsList = studentPaymentsRepository.findAll();
        assertThat(studentPaymentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStudentPayments() throws Exception {
        // Initialize the database
        studentPaymentsRepository.saveAndFlush(studentPayments);

        int databaseSizeBeforeDelete = studentPaymentsRepository.findAll().size();

        // Delete the studentPayments
        restStudentPaymentsMockMvc
            .perform(delete(ENTITY_API_URL_ID, studentPayments.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<StudentPayments> studentPaymentsList = studentPaymentsRepository.findAll();
        assertThat(studentPaymentsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
