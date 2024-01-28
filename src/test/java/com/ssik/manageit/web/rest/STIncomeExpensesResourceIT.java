package com.ssik.manageit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ssik.manageit.IntegrationTest;
import com.ssik.manageit.domain.ClassStudent;
import com.ssik.manageit.domain.STIncomeExpenses;
import com.ssik.manageit.domain.STRoute;
import com.ssik.manageit.domain.Vendors;
import com.ssik.manageit.domain.enumeration.ModeOfPayment;
import com.ssik.manageit.domain.enumeration.TransactionType;
import com.ssik.manageit.repository.STIncomeExpensesRepository;
import com.ssik.manageit.service.criteria.STIncomeExpensesCriteria;
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
 * Integration tests for the {@link STIncomeExpensesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class STIncomeExpensesResourceIT {

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

    private static final TransactionType DEFAULT_TRANSACTION_TYPE = TransactionType.EXPENSE;
    private static final TransactionType UPDATED_TRANSACTION_TYPE = TransactionType.INCOME;

    private static final String ENTITY_API_URL = "/api/st-income-expenses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private STIncomeExpensesRepository sTIncomeExpensesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSTIncomeExpensesMockMvc;

    private STIncomeExpenses sTIncomeExpenses;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static STIncomeExpenses createEntity(EntityManager em) {
        STIncomeExpenses sTIncomeExpenses = new STIncomeExpenses()
            .amountPaid(DEFAULT_AMOUNT_PAID)
            .modeOfPay(DEFAULT_MODE_OF_PAY)
            .noteNumbers(DEFAULT_NOTE_NUMBERS)
            .upiId(DEFAULT_UPI_ID)
            .remarks(DEFAULT_REMARKS)
            .paymentDate(DEFAULT_PAYMENT_DATE)
            .receiptId(DEFAULT_RECEIPT_ID)
            .createDate(DEFAULT_CREATE_DATE)
            .lastModified(DEFAULT_LAST_MODIFIED)
            .cancelDate(DEFAULT_CANCEL_DATE)
            .transactionType(DEFAULT_TRANSACTION_TYPE);
        return sTIncomeExpenses;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static STIncomeExpenses createUpdatedEntity(EntityManager em) {
        STIncomeExpenses sTIncomeExpenses = new STIncomeExpenses()
            .amountPaid(UPDATED_AMOUNT_PAID)
            .modeOfPay(UPDATED_MODE_OF_PAY)
            .noteNumbers(UPDATED_NOTE_NUMBERS)
            .upiId(UPDATED_UPI_ID)
            .remarks(UPDATED_REMARKS)
            .paymentDate(UPDATED_PAYMENT_DATE)
            .receiptId(UPDATED_RECEIPT_ID)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE)
            .transactionType(UPDATED_TRANSACTION_TYPE);
        return sTIncomeExpenses;
    }

    @BeforeEach
    public void initTest() {
        sTIncomeExpenses = createEntity(em);
    }

    @Test
    @Transactional
    void createSTIncomeExpenses() throws Exception {
        int databaseSizeBeforeCreate = sTIncomeExpensesRepository.findAll().size();
        // Create the STIncomeExpenses
        restSTIncomeExpensesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sTIncomeExpenses))
            )
            .andExpect(status().isCreated());

        // Validate the STIncomeExpenses in the database
        List<STIncomeExpenses> sTIncomeExpensesList = sTIncomeExpensesRepository.findAll();
        assertThat(sTIncomeExpensesList).hasSize(databaseSizeBeforeCreate + 1);
        STIncomeExpenses testSTIncomeExpenses = sTIncomeExpensesList.get(sTIncomeExpensesList.size() - 1);
        assertThat(testSTIncomeExpenses.getAmountPaid()).isEqualTo(DEFAULT_AMOUNT_PAID);
        assertThat(testSTIncomeExpenses.getModeOfPay()).isEqualTo(DEFAULT_MODE_OF_PAY);
        assertThat(testSTIncomeExpenses.getNoteNumbers()).isEqualTo(DEFAULT_NOTE_NUMBERS);
        assertThat(testSTIncomeExpenses.getUpiId()).isEqualTo(DEFAULT_UPI_ID);
        assertThat(testSTIncomeExpenses.getRemarks()).isEqualTo(DEFAULT_REMARKS);
        assertThat(testSTIncomeExpenses.getPaymentDate()).isEqualTo(DEFAULT_PAYMENT_DATE);
        assertThat(testSTIncomeExpenses.getReceiptId()).isEqualTo(DEFAULT_RECEIPT_ID);
        assertThat(testSTIncomeExpenses.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testSTIncomeExpenses.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
        assertThat(testSTIncomeExpenses.getCancelDate()).isEqualTo(DEFAULT_CANCEL_DATE);
        assertThat(testSTIncomeExpenses.getTransactionType()).isEqualTo(DEFAULT_TRANSACTION_TYPE);
    }

    @Test
    @Transactional
    void createSTIncomeExpensesWithExistingId() throws Exception {
        // Create the STIncomeExpenses with an existing ID
        sTIncomeExpenses.setId(1L);

        int databaseSizeBeforeCreate = sTIncomeExpensesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSTIncomeExpensesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sTIncomeExpenses))
            )
            .andExpect(status().isBadRequest());

        // Validate the STIncomeExpenses in the database
        List<STIncomeExpenses> sTIncomeExpensesList = sTIncomeExpensesRepository.findAll();
        assertThat(sTIncomeExpensesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAmountPaidIsRequired() throws Exception {
        int databaseSizeBeforeTest = sTIncomeExpensesRepository.findAll().size();
        // set the field null
        sTIncomeExpenses.setAmountPaid(null);

        // Create the STIncomeExpenses, which fails.

        restSTIncomeExpensesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sTIncomeExpenses))
            )
            .andExpect(status().isBadRequest());

        List<STIncomeExpenses> sTIncomeExpensesList = sTIncomeExpensesRepository.findAll();
        assertThat(sTIncomeExpensesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSTIncomeExpenses() throws Exception {
        // Initialize the database
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);

        // Get all the sTIncomeExpensesList
        restSTIncomeExpensesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sTIncomeExpenses.getId().intValue())))
            .andExpect(jsonPath("$.[*].amountPaid").value(hasItem(DEFAULT_AMOUNT_PAID.doubleValue())))
            .andExpect(jsonPath("$.[*].modeOfPay").value(hasItem(DEFAULT_MODE_OF_PAY.toString())))
            .andExpect(jsonPath("$.[*].noteNumbers").value(hasItem(DEFAULT_NOTE_NUMBERS)))
            .andExpect(jsonPath("$.[*].upiId").value(hasItem(DEFAULT_UPI_ID)))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS)))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].receiptId").value(hasItem(DEFAULT_RECEIPT_ID)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].cancelDate").value(hasItem(DEFAULT_CANCEL_DATE.toString())))
            .andExpect(jsonPath("$.[*].transactionType").value(hasItem(DEFAULT_TRANSACTION_TYPE.toString())));
    }

    @Test
    @Transactional
    void getSTIncomeExpenses() throws Exception {
        // Initialize the database
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);

        // Get the sTIncomeExpenses
        restSTIncomeExpensesMockMvc
            .perform(get(ENTITY_API_URL_ID, sTIncomeExpenses.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sTIncomeExpenses.getId().intValue()))
            .andExpect(jsonPath("$.amountPaid").value(DEFAULT_AMOUNT_PAID.doubleValue()))
            .andExpect(jsonPath("$.modeOfPay").value(DEFAULT_MODE_OF_PAY.toString()))
            .andExpect(jsonPath("$.noteNumbers").value(DEFAULT_NOTE_NUMBERS))
            .andExpect(jsonPath("$.upiId").value(DEFAULT_UPI_ID))
            .andExpect(jsonPath("$.remarks").value(DEFAULT_REMARKS))
            .andExpect(jsonPath("$.paymentDate").value(DEFAULT_PAYMENT_DATE.toString()))
            .andExpect(jsonPath("$.receiptId").value(DEFAULT_RECEIPT_ID))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.lastModified").value(DEFAULT_LAST_MODIFIED.toString()))
            .andExpect(jsonPath("$.cancelDate").value(DEFAULT_CANCEL_DATE.toString()))
            .andExpect(jsonPath("$.transactionType").value(DEFAULT_TRANSACTION_TYPE.toString()));
    }

    @Test
    @Transactional
    void getSTIncomeExpensesByIdFiltering() throws Exception {
        // Initialize the database
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);

        Long id = sTIncomeExpenses.getId();

        defaultSTIncomeExpensesShouldBeFound("id.equals=" + id);
        defaultSTIncomeExpensesShouldNotBeFound("id.notEquals=" + id);

        defaultSTIncomeExpensesShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSTIncomeExpensesShouldNotBeFound("id.greaterThan=" + id);

        defaultSTIncomeExpensesShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSTIncomeExpensesShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSTIncomeExpensesByAmountPaidIsEqualToSomething() throws Exception {
        // Initialize the database
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);

        // Get all the sTIncomeExpensesList where amountPaid equals to DEFAULT_AMOUNT_PAID
        defaultSTIncomeExpensesShouldBeFound("amountPaid.equals=" + DEFAULT_AMOUNT_PAID);

        // Get all the sTIncomeExpensesList where amountPaid equals to UPDATED_AMOUNT_PAID
        defaultSTIncomeExpensesShouldNotBeFound("amountPaid.equals=" + UPDATED_AMOUNT_PAID);
    }

    @Test
    @Transactional
    void getAllSTIncomeExpensesByAmountPaidIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);

        // Get all the sTIncomeExpensesList where amountPaid not equals to DEFAULT_AMOUNT_PAID
        defaultSTIncomeExpensesShouldNotBeFound("amountPaid.notEquals=" + DEFAULT_AMOUNT_PAID);

        // Get all the sTIncomeExpensesList where amountPaid not equals to UPDATED_AMOUNT_PAID
        defaultSTIncomeExpensesShouldBeFound("amountPaid.notEquals=" + UPDATED_AMOUNT_PAID);
    }

    @Test
    @Transactional
    void getAllSTIncomeExpensesByAmountPaidIsInShouldWork() throws Exception {
        // Initialize the database
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);

        // Get all the sTIncomeExpensesList where amountPaid in DEFAULT_AMOUNT_PAID or UPDATED_AMOUNT_PAID
        defaultSTIncomeExpensesShouldBeFound("amountPaid.in=" + DEFAULT_AMOUNT_PAID + "," + UPDATED_AMOUNT_PAID);

        // Get all the sTIncomeExpensesList where amountPaid equals to UPDATED_AMOUNT_PAID
        defaultSTIncomeExpensesShouldNotBeFound("amountPaid.in=" + UPDATED_AMOUNT_PAID);
    }

    @Test
    @Transactional
    void getAllSTIncomeExpensesByAmountPaidIsNullOrNotNull() throws Exception {
        // Initialize the database
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);

        // Get all the sTIncomeExpensesList where amountPaid is not null
        defaultSTIncomeExpensesShouldBeFound("amountPaid.specified=true");

        // Get all the sTIncomeExpensesList where amountPaid is null
        defaultSTIncomeExpensesShouldNotBeFound("amountPaid.specified=false");
    }

    @Test
    @Transactional
    void getAllSTIncomeExpensesByAmountPaidIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);

        // Get all the sTIncomeExpensesList where amountPaid is greater than or equal to DEFAULT_AMOUNT_PAID
        defaultSTIncomeExpensesShouldBeFound("amountPaid.greaterThanOrEqual=" + DEFAULT_AMOUNT_PAID);

        // Get all the sTIncomeExpensesList where amountPaid is greater than or equal to UPDATED_AMOUNT_PAID
        defaultSTIncomeExpensesShouldNotBeFound("amountPaid.greaterThanOrEqual=" + UPDATED_AMOUNT_PAID);
    }

    @Test
    @Transactional
    void getAllSTIncomeExpensesByAmountPaidIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);

        // Get all the sTIncomeExpensesList where amountPaid is less than or equal to DEFAULT_AMOUNT_PAID
        defaultSTIncomeExpensesShouldBeFound("amountPaid.lessThanOrEqual=" + DEFAULT_AMOUNT_PAID);

        // Get all the sTIncomeExpensesList where amountPaid is less than or equal to SMALLER_AMOUNT_PAID
        defaultSTIncomeExpensesShouldNotBeFound("amountPaid.lessThanOrEqual=" + SMALLER_AMOUNT_PAID);
    }

    @Test
    @Transactional
    void getAllSTIncomeExpensesByAmountPaidIsLessThanSomething() throws Exception {
        // Initialize the database
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);

        // Get all the sTIncomeExpensesList where amountPaid is less than DEFAULT_AMOUNT_PAID
        defaultSTIncomeExpensesShouldNotBeFound("amountPaid.lessThan=" + DEFAULT_AMOUNT_PAID);

        // Get all the sTIncomeExpensesList where amountPaid is less than UPDATED_AMOUNT_PAID
        defaultSTIncomeExpensesShouldBeFound("amountPaid.lessThan=" + UPDATED_AMOUNT_PAID);
    }

    @Test
    @Transactional
    void getAllSTIncomeExpensesByAmountPaidIsGreaterThanSomething() throws Exception {
        // Initialize the database
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);

        // Get all the sTIncomeExpensesList where amountPaid is greater than DEFAULT_AMOUNT_PAID
        defaultSTIncomeExpensesShouldNotBeFound("amountPaid.greaterThan=" + DEFAULT_AMOUNT_PAID);

        // Get all the sTIncomeExpensesList where amountPaid is greater than SMALLER_AMOUNT_PAID
        defaultSTIncomeExpensesShouldBeFound("amountPaid.greaterThan=" + SMALLER_AMOUNT_PAID);
    }

    @Test
    @Transactional
    void getAllSTIncomeExpensesByModeOfPayIsEqualToSomething() throws Exception {
        // Initialize the database
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);

        // Get all the sTIncomeExpensesList where modeOfPay equals to DEFAULT_MODE_OF_PAY
        defaultSTIncomeExpensesShouldBeFound("modeOfPay.equals=" + DEFAULT_MODE_OF_PAY);

        // Get all the sTIncomeExpensesList where modeOfPay equals to UPDATED_MODE_OF_PAY
        defaultSTIncomeExpensesShouldNotBeFound("modeOfPay.equals=" + UPDATED_MODE_OF_PAY);
    }

    @Test
    @Transactional
    void getAllSTIncomeExpensesByModeOfPayIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);

        // Get all the sTIncomeExpensesList where modeOfPay not equals to DEFAULT_MODE_OF_PAY
        defaultSTIncomeExpensesShouldNotBeFound("modeOfPay.notEquals=" + DEFAULT_MODE_OF_PAY);

        // Get all the sTIncomeExpensesList where modeOfPay not equals to UPDATED_MODE_OF_PAY
        defaultSTIncomeExpensesShouldBeFound("modeOfPay.notEquals=" + UPDATED_MODE_OF_PAY);
    }

    @Test
    @Transactional
    void getAllSTIncomeExpensesByModeOfPayIsInShouldWork() throws Exception {
        // Initialize the database
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);

        // Get all the sTIncomeExpensesList where modeOfPay in DEFAULT_MODE_OF_PAY or UPDATED_MODE_OF_PAY
        defaultSTIncomeExpensesShouldBeFound("modeOfPay.in=" + DEFAULT_MODE_OF_PAY + "," + UPDATED_MODE_OF_PAY);

        // Get all the sTIncomeExpensesList where modeOfPay equals to UPDATED_MODE_OF_PAY
        defaultSTIncomeExpensesShouldNotBeFound("modeOfPay.in=" + UPDATED_MODE_OF_PAY);
    }

    @Test
    @Transactional
    void getAllSTIncomeExpensesByModeOfPayIsNullOrNotNull() throws Exception {
        // Initialize the database
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);

        // Get all the sTIncomeExpensesList where modeOfPay is not null
        defaultSTIncomeExpensesShouldBeFound("modeOfPay.specified=true");

        // Get all the sTIncomeExpensesList where modeOfPay is null
        defaultSTIncomeExpensesShouldNotBeFound("modeOfPay.specified=false");
    }

    @Test
    @Transactional
    void getAllSTIncomeExpensesByNoteNumbersIsEqualToSomething() throws Exception {
        // Initialize the database
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);

        // Get all the sTIncomeExpensesList where noteNumbers equals to DEFAULT_NOTE_NUMBERS
        defaultSTIncomeExpensesShouldBeFound("noteNumbers.equals=" + DEFAULT_NOTE_NUMBERS);

        // Get all the sTIncomeExpensesList where noteNumbers equals to UPDATED_NOTE_NUMBERS
        defaultSTIncomeExpensesShouldNotBeFound("noteNumbers.equals=" + UPDATED_NOTE_NUMBERS);
    }

    @Test
    @Transactional
    void getAllSTIncomeExpensesByNoteNumbersIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);

        // Get all the sTIncomeExpensesList where noteNumbers not equals to DEFAULT_NOTE_NUMBERS
        defaultSTIncomeExpensesShouldNotBeFound("noteNumbers.notEquals=" + DEFAULT_NOTE_NUMBERS);

        // Get all the sTIncomeExpensesList where noteNumbers not equals to UPDATED_NOTE_NUMBERS
        defaultSTIncomeExpensesShouldBeFound("noteNumbers.notEquals=" + UPDATED_NOTE_NUMBERS);
    }

    @Test
    @Transactional
    void getAllSTIncomeExpensesByNoteNumbersIsInShouldWork() throws Exception {
        // Initialize the database
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);

        // Get all the sTIncomeExpensesList where noteNumbers in DEFAULT_NOTE_NUMBERS or UPDATED_NOTE_NUMBERS
        defaultSTIncomeExpensesShouldBeFound("noteNumbers.in=" + DEFAULT_NOTE_NUMBERS + "," + UPDATED_NOTE_NUMBERS);

        // Get all the sTIncomeExpensesList where noteNumbers equals to UPDATED_NOTE_NUMBERS
        defaultSTIncomeExpensesShouldNotBeFound("noteNumbers.in=" + UPDATED_NOTE_NUMBERS);
    }

    @Test
    @Transactional
    void getAllSTIncomeExpensesByNoteNumbersIsNullOrNotNull() throws Exception {
        // Initialize the database
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);

        // Get all the sTIncomeExpensesList where noteNumbers is not null
        defaultSTIncomeExpensesShouldBeFound("noteNumbers.specified=true");

        // Get all the sTIncomeExpensesList where noteNumbers is null
        defaultSTIncomeExpensesShouldNotBeFound("noteNumbers.specified=false");
    }

    @Test
    @Transactional
    void getAllSTIncomeExpensesByNoteNumbersContainsSomething() throws Exception {
        // Initialize the database
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);

        // Get all the sTIncomeExpensesList where noteNumbers contains DEFAULT_NOTE_NUMBERS
        defaultSTIncomeExpensesShouldBeFound("noteNumbers.contains=" + DEFAULT_NOTE_NUMBERS);

        // Get all the sTIncomeExpensesList where noteNumbers contains UPDATED_NOTE_NUMBERS
        defaultSTIncomeExpensesShouldNotBeFound("noteNumbers.contains=" + UPDATED_NOTE_NUMBERS);
    }

    @Test
    @Transactional
    void getAllSTIncomeExpensesByNoteNumbersNotContainsSomething() throws Exception {
        // Initialize the database
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);

        // Get all the sTIncomeExpensesList where noteNumbers does not contain DEFAULT_NOTE_NUMBERS
        defaultSTIncomeExpensesShouldNotBeFound("noteNumbers.doesNotContain=" + DEFAULT_NOTE_NUMBERS);

        // Get all the sTIncomeExpensesList where noteNumbers does not contain UPDATED_NOTE_NUMBERS
        defaultSTIncomeExpensesShouldBeFound("noteNumbers.doesNotContain=" + UPDATED_NOTE_NUMBERS);
    }

    @Test
    @Transactional
    void getAllSTIncomeExpensesByUpiIdIsEqualToSomething() throws Exception {
        // Initialize the database
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);

        // Get all the sTIncomeExpensesList where upiId equals to DEFAULT_UPI_ID
        defaultSTIncomeExpensesShouldBeFound("upiId.equals=" + DEFAULT_UPI_ID);

        // Get all the sTIncomeExpensesList where upiId equals to UPDATED_UPI_ID
        defaultSTIncomeExpensesShouldNotBeFound("upiId.equals=" + UPDATED_UPI_ID);
    }

    @Test
    @Transactional
    void getAllSTIncomeExpensesByUpiIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);

        // Get all the sTIncomeExpensesList where upiId not equals to DEFAULT_UPI_ID
        defaultSTIncomeExpensesShouldNotBeFound("upiId.notEquals=" + DEFAULT_UPI_ID);

        // Get all the sTIncomeExpensesList where upiId not equals to UPDATED_UPI_ID
        defaultSTIncomeExpensesShouldBeFound("upiId.notEquals=" + UPDATED_UPI_ID);
    }

    @Test
    @Transactional
    void getAllSTIncomeExpensesByUpiIdIsInShouldWork() throws Exception {
        // Initialize the database
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);

        // Get all the sTIncomeExpensesList where upiId in DEFAULT_UPI_ID or UPDATED_UPI_ID
        defaultSTIncomeExpensesShouldBeFound("upiId.in=" + DEFAULT_UPI_ID + "," + UPDATED_UPI_ID);

        // Get all the sTIncomeExpensesList where upiId equals to UPDATED_UPI_ID
        defaultSTIncomeExpensesShouldNotBeFound("upiId.in=" + UPDATED_UPI_ID);
    }

    @Test
    @Transactional
    void getAllSTIncomeExpensesByUpiIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);

        // Get all the sTIncomeExpensesList where upiId is not null
        defaultSTIncomeExpensesShouldBeFound("upiId.specified=true");

        // Get all the sTIncomeExpensesList where upiId is null
        defaultSTIncomeExpensesShouldNotBeFound("upiId.specified=false");
    }

    @Test
    @Transactional
    void getAllSTIncomeExpensesByUpiIdContainsSomething() throws Exception {
        // Initialize the database
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);

        // Get all the sTIncomeExpensesList where upiId contains DEFAULT_UPI_ID
        defaultSTIncomeExpensesShouldBeFound("upiId.contains=" + DEFAULT_UPI_ID);

        // Get all the sTIncomeExpensesList where upiId contains UPDATED_UPI_ID
        defaultSTIncomeExpensesShouldNotBeFound("upiId.contains=" + UPDATED_UPI_ID);
    }

    @Test
    @Transactional
    void getAllSTIncomeExpensesByUpiIdNotContainsSomething() throws Exception {
        // Initialize the database
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);

        // Get all the sTIncomeExpensesList where upiId does not contain DEFAULT_UPI_ID
        defaultSTIncomeExpensesShouldNotBeFound("upiId.doesNotContain=" + DEFAULT_UPI_ID);

        // Get all the sTIncomeExpensesList where upiId does not contain UPDATED_UPI_ID
        defaultSTIncomeExpensesShouldBeFound("upiId.doesNotContain=" + UPDATED_UPI_ID);
    }

    @Test
    @Transactional
    void getAllSTIncomeExpensesByRemarksIsEqualToSomething() throws Exception {
        // Initialize the database
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);

        // Get all the sTIncomeExpensesList where remarks equals to DEFAULT_REMARKS
        defaultSTIncomeExpensesShouldBeFound("remarks.equals=" + DEFAULT_REMARKS);

        // Get all the sTIncomeExpensesList where remarks equals to UPDATED_REMARKS
        defaultSTIncomeExpensesShouldNotBeFound("remarks.equals=" + UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void getAllSTIncomeExpensesByRemarksIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);

        // Get all the sTIncomeExpensesList where remarks not equals to DEFAULT_REMARKS
        defaultSTIncomeExpensesShouldNotBeFound("remarks.notEquals=" + DEFAULT_REMARKS);

        // Get all the sTIncomeExpensesList where remarks not equals to UPDATED_REMARKS
        defaultSTIncomeExpensesShouldBeFound("remarks.notEquals=" + UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void getAllSTIncomeExpensesByRemarksIsInShouldWork() throws Exception {
        // Initialize the database
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);

        // Get all the sTIncomeExpensesList where remarks in DEFAULT_REMARKS or UPDATED_REMARKS
        defaultSTIncomeExpensesShouldBeFound("remarks.in=" + DEFAULT_REMARKS + "," + UPDATED_REMARKS);

        // Get all the sTIncomeExpensesList where remarks equals to UPDATED_REMARKS
        defaultSTIncomeExpensesShouldNotBeFound("remarks.in=" + UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void getAllSTIncomeExpensesByRemarksIsNullOrNotNull() throws Exception {
        // Initialize the database
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);

        // Get all the sTIncomeExpensesList where remarks is not null
        defaultSTIncomeExpensesShouldBeFound("remarks.specified=true");

        // Get all the sTIncomeExpensesList where remarks is null
        defaultSTIncomeExpensesShouldNotBeFound("remarks.specified=false");
    }

    @Test
    @Transactional
    void getAllSTIncomeExpensesByRemarksContainsSomething() throws Exception {
        // Initialize the database
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);

        // Get all the sTIncomeExpensesList where remarks contains DEFAULT_REMARKS
        defaultSTIncomeExpensesShouldBeFound("remarks.contains=" + DEFAULT_REMARKS);

        // Get all the sTIncomeExpensesList where remarks contains UPDATED_REMARKS
        defaultSTIncomeExpensesShouldNotBeFound("remarks.contains=" + UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void getAllSTIncomeExpensesByRemarksNotContainsSomething() throws Exception {
        // Initialize the database
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);

        // Get all the sTIncomeExpensesList where remarks does not contain DEFAULT_REMARKS
        defaultSTIncomeExpensesShouldNotBeFound("remarks.doesNotContain=" + DEFAULT_REMARKS);

        // Get all the sTIncomeExpensesList where remarks does not contain UPDATED_REMARKS
        defaultSTIncomeExpensesShouldBeFound("remarks.doesNotContain=" + UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void getAllSTIncomeExpensesByPaymentDateIsEqualToSomething() throws Exception {
        // Initialize the database
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);

        // Get all the sTIncomeExpensesList where paymentDate equals to DEFAULT_PAYMENT_DATE
        defaultSTIncomeExpensesShouldBeFound("paymentDate.equals=" + DEFAULT_PAYMENT_DATE);

        // Get all the sTIncomeExpensesList where paymentDate equals to UPDATED_PAYMENT_DATE
        defaultSTIncomeExpensesShouldNotBeFound("paymentDate.equals=" + UPDATED_PAYMENT_DATE);
    }

    @Test
    @Transactional
    void getAllSTIncomeExpensesByPaymentDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);

        // Get all the sTIncomeExpensesList where paymentDate not equals to DEFAULT_PAYMENT_DATE
        defaultSTIncomeExpensesShouldNotBeFound("paymentDate.notEquals=" + DEFAULT_PAYMENT_DATE);

        // Get all the sTIncomeExpensesList where paymentDate not equals to UPDATED_PAYMENT_DATE
        defaultSTIncomeExpensesShouldBeFound("paymentDate.notEquals=" + UPDATED_PAYMENT_DATE);
    }

    @Test
    @Transactional
    void getAllSTIncomeExpensesByPaymentDateIsInShouldWork() throws Exception {
        // Initialize the database
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);

        // Get all the sTIncomeExpensesList where paymentDate in DEFAULT_PAYMENT_DATE or UPDATED_PAYMENT_DATE
        defaultSTIncomeExpensesShouldBeFound("paymentDate.in=" + DEFAULT_PAYMENT_DATE + "," + UPDATED_PAYMENT_DATE);

        // Get all the sTIncomeExpensesList where paymentDate equals to UPDATED_PAYMENT_DATE
        defaultSTIncomeExpensesShouldNotBeFound("paymentDate.in=" + UPDATED_PAYMENT_DATE);
    }

    @Test
    @Transactional
    void getAllSTIncomeExpensesByPaymentDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);

        // Get all the sTIncomeExpensesList where paymentDate is not null
        defaultSTIncomeExpensesShouldBeFound("paymentDate.specified=true");

        // Get all the sTIncomeExpensesList where paymentDate is null
        defaultSTIncomeExpensesShouldNotBeFound("paymentDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSTIncomeExpensesByPaymentDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);

        // Get all the sTIncomeExpensesList where paymentDate is greater than or equal to DEFAULT_PAYMENT_DATE
        defaultSTIncomeExpensesShouldBeFound("paymentDate.greaterThanOrEqual=" + DEFAULT_PAYMENT_DATE);

        // Get all the sTIncomeExpensesList where paymentDate is greater than or equal to UPDATED_PAYMENT_DATE
        defaultSTIncomeExpensesShouldNotBeFound("paymentDate.greaterThanOrEqual=" + UPDATED_PAYMENT_DATE);
    }

    @Test
    @Transactional
    void getAllSTIncomeExpensesByPaymentDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);

        // Get all the sTIncomeExpensesList where paymentDate is less than or equal to DEFAULT_PAYMENT_DATE
        defaultSTIncomeExpensesShouldBeFound("paymentDate.lessThanOrEqual=" + DEFAULT_PAYMENT_DATE);

        // Get all the sTIncomeExpensesList where paymentDate is less than or equal to SMALLER_PAYMENT_DATE
        defaultSTIncomeExpensesShouldNotBeFound("paymentDate.lessThanOrEqual=" + SMALLER_PAYMENT_DATE);
    }

    @Test
    @Transactional
    void getAllSTIncomeExpensesByPaymentDateIsLessThanSomething() throws Exception {
        // Initialize the database
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);

        // Get all the sTIncomeExpensesList where paymentDate is less than DEFAULT_PAYMENT_DATE
        defaultSTIncomeExpensesShouldNotBeFound("paymentDate.lessThan=" + DEFAULT_PAYMENT_DATE);

        // Get all the sTIncomeExpensesList where paymentDate is less than UPDATED_PAYMENT_DATE
        defaultSTIncomeExpensesShouldBeFound("paymentDate.lessThan=" + UPDATED_PAYMENT_DATE);
    }

    @Test
    @Transactional
    void getAllSTIncomeExpensesByPaymentDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);

        // Get all the sTIncomeExpensesList where paymentDate is greater than DEFAULT_PAYMENT_DATE
        defaultSTIncomeExpensesShouldNotBeFound("paymentDate.greaterThan=" + DEFAULT_PAYMENT_DATE);

        // Get all the sTIncomeExpensesList where paymentDate is greater than SMALLER_PAYMENT_DATE
        defaultSTIncomeExpensesShouldBeFound("paymentDate.greaterThan=" + SMALLER_PAYMENT_DATE);
    }

    @Test
    @Transactional
    void getAllSTIncomeExpensesByReceiptIdIsEqualToSomething() throws Exception {
        // Initialize the database
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);

        // Get all the sTIncomeExpensesList where receiptId equals to DEFAULT_RECEIPT_ID
        defaultSTIncomeExpensesShouldBeFound("receiptId.equals=" + DEFAULT_RECEIPT_ID);

        // Get all the sTIncomeExpensesList where receiptId equals to UPDATED_RECEIPT_ID
        defaultSTIncomeExpensesShouldNotBeFound("receiptId.equals=" + UPDATED_RECEIPT_ID);
    }

    @Test
    @Transactional
    void getAllSTIncomeExpensesByReceiptIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);

        // Get all the sTIncomeExpensesList where receiptId not equals to DEFAULT_RECEIPT_ID
        defaultSTIncomeExpensesShouldNotBeFound("receiptId.notEquals=" + DEFAULT_RECEIPT_ID);

        // Get all the sTIncomeExpensesList where receiptId not equals to UPDATED_RECEIPT_ID
        defaultSTIncomeExpensesShouldBeFound("receiptId.notEquals=" + UPDATED_RECEIPT_ID);
    }

    @Test
    @Transactional
    void getAllSTIncomeExpensesByReceiptIdIsInShouldWork() throws Exception {
        // Initialize the database
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);

        // Get all the sTIncomeExpensesList where receiptId in DEFAULT_RECEIPT_ID or UPDATED_RECEIPT_ID
        defaultSTIncomeExpensesShouldBeFound("receiptId.in=" + DEFAULT_RECEIPT_ID + "," + UPDATED_RECEIPT_ID);

        // Get all the sTIncomeExpensesList where receiptId equals to UPDATED_RECEIPT_ID
        defaultSTIncomeExpensesShouldNotBeFound("receiptId.in=" + UPDATED_RECEIPT_ID);
    }

    @Test
    @Transactional
    void getAllSTIncomeExpensesByReceiptIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);

        // Get all the sTIncomeExpensesList where receiptId is not null
        defaultSTIncomeExpensesShouldBeFound("receiptId.specified=true");

        // Get all the sTIncomeExpensesList where receiptId is null
        defaultSTIncomeExpensesShouldNotBeFound("receiptId.specified=false");
    }

    @Test
    @Transactional
    void getAllSTIncomeExpensesByReceiptIdContainsSomething() throws Exception {
        // Initialize the database
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);

        // Get all the sTIncomeExpensesList where receiptId contains DEFAULT_RECEIPT_ID
        defaultSTIncomeExpensesShouldBeFound("receiptId.contains=" + DEFAULT_RECEIPT_ID);

        // Get all the sTIncomeExpensesList where receiptId contains UPDATED_RECEIPT_ID
        defaultSTIncomeExpensesShouldNotBeFound("receiptId.contains=" + UPDATED_RECEIPT_ID);
    }

    @Test
    @Transactional
    void getAllSTIncomeExpensesByReceiptIdNotContainsSomething() throws Exception {
        // Initialize the database
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);

        // Get all the sTIncomeExpensesList where receiptId does not contain DEFAULT_RECEIPT_ID
        defaultSTIncomeExpensesShouldNotBeFound("receiptId.doesNotContain=" + DEFAULT_RECEIPT_ID);

        // Get all the sTIncomeExpensesList where receiptId does not contain UPDATED_RECEIPT_ID
        defaultSTIncomeExpensesShouldBeFound("receiptId.doesNotContain=" + UPDATED_RECEIPT_ID);
    }

    @Test
    @Transactional
    void getAllSTIncomeExpensesByCreateDateIsEqualToSomething() throws Exception {
        // Initialize the database
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);

        // Get all the sTIncomeExpensesList where createDate equals to DEFAULT_CREATE_DATE
        defaultSTIncomeExpensesShouldBeFound("createDate.equals=" + DEFAULT_CREATE_DATE);

        // Get all the sTIncomeExpensesList where createDate equals to UPDATED_CREATE_DATE
        defaultSTIncomeExpensesShouldNotBeFound("createDate.equals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSTIncomeExpensesByCreateDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);

        // Get all the sTIncomeExpensesList where createDate not equals to DEFAULT_CREATE_DATE
        defaultSTIncomeExpensesShouldNotBeFound("createDate.notEquals=" + DEFAULT_CREATE_DATE);

        // Get all the sTIncomeExpensesList where createDate not equals to UPDATED_CREATE_DATE
        defaultSTIncomeExpensesShouldBeFound("createDate.notEquals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSTIncomeExpensesByCreateDateIsInShouldWork() throws Exception {
        // Initialize the database
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);

        // Get all the sTIncomeExpensesList where createDate in DEFAULT_CREATE_DATE or UPDATED_CREATE_DATE
        defaultSTIncomeExpensesShouldBeFound("createDate.in=" + DEFAULT_CREATE_DATE + "," + UPDATED_CREATE_DATE);

        // Get all the sTIncomeExpensesList where createDate equals to UPDATED_CREATE_DATE
        defaultSTIncomeExpensesShouldNotBeFound("createDate.in=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSTIncomeExpensesByCreateDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);

        // Get all the sTIncomeExpensesList where createDate is not null
        defaultSTIncomeExpensesShouldBeFound("createDate.specified=true");

        // Get all the sTIncomeExpensesList where createDate is null
        defaultSTIncomeExpensesShouldNotBeFound("createDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSTIncomeExpensesByCreateDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);

        // Get all the sTIncomeExpensesList where createDate is greater than or equal to DEFAULT_CREATE_DATE
        defaultSTIncomeExpensesShouldBeFound("createDate.greaterThanOrEqual=" + DEFAULT_CREATE_DATE);

        // Get all the sTIncomeExpensesList where createDate is greater than or equal to UPDATED_CREATE_DATE
        defaultSTIncomeExpensesShouldNotBeFound("createDate.greaterThanOrEqual=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSTIncomeExpensesByCreateDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);

        // Get all the sTIncomeExpensesList where createDate is less than or equal to DEFAULT_CREATE_DATE
        defaultSTIncomeExpensesShouldBeFound("createDate.lessThanOrEqual=" + DEFAULT_CREATE_DATE);

        // Get all the sTIncomeExpensesList where createDate is less than or equal to SMALLER_CREATE_DATE
        defaultSTIncomeExpensesShouldNotBeFound("createDate.lessThanOrEqual=" + SMALLER_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSTIncomeExpensesByCreateDateIsLessThanSomething() throws Exception {
        // Initialize the database
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);

        // Get all the sTIncomeExpensesList where createDate is less than DEFAULT_CREATE_DATE
        defaultSTIncomeExpensesShouldNotBeFound("createDate.lessThan=" + DEFAULT_CREATE_DATE);

        // Get all the sTIncomeExpensesList where createDate is less than UPDATED_CREATE_DATE
        defaultSTIncomeExpensesShouldBeFound("createDate.lessThan=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSTIncomeExpensesByCreateDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);

        // Get all the sTIncomeExpensesList where createDate is greater than DEFAULT_CREATE_DATE
        defaultSTIncomeExpensesShouldNotBeFound("createDate.greaterThan=" + DEFAULT_CREATE_DATE);

        // Get all the sTIncomeExpensesList where createDate is greater than SMALLER_CREATE_DATE
        defaultSTIncomeExpensesShouldBeFound("createDate.greaterThan=" + SMALLER_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllSTIncomeExpensesByLastModifiedIsEqualToSomething() throws Exception {
        // Initialize the database
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);

        // Get all the sTIncomeExpensesList where lastModified equals to DEFAULT_LAST_MODIFIED
        defaultSTIncomeExpensesShouldBeFound("lastModified.equals=" + DEFAULT_LAST_MODIFIED);

        // Get all the sTIncomeExpensesList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultSTIncomeExpensesShouldNotBeFound("lastModified.equals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSTIncomeExpensesByLastModifiedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);

        // Get all the sTIncomeExpensesList where lastModified not equals to DEFAULT_LAST_MODIFIED
        defaultSTIncomeExpensesShouldNotBeFound("lastModified.notEquals=" + DEFAULT_LAST_MODIFIED);

        // Get all the sTIncomeExpensesList where lastModified not equals to UPDATED_LAST_MODIFIED
        defaultSTIncomeExpensesShouldBeFound("lastModified.notEquals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSTIncomeExpensesByLastModifiedIsInShouldWork() throws Exception {
        // Initialize the database
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);

        // Get all the sTIncomeExpensesList where lastModified in DEFAULT_LAST_MODIFIED or UPDATED_LAST_MODIFIED
        defaultSTIncomeExpensesShouldBeFound("lastModified.in=" + DEFAULT_LAST_MODIFIED + "," + UPDATED_LAST_MODIFIED);

        // Get all the sTIncomeExpensesList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultSTIncomeExpensesShouldNotBeFound("lastModified.in=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSTIncomeExpensesByLastModifiedIsNullOrNotNull() throws Exception {
        // Initialize the database
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);

        // Get all the sTIncomeExpensesList where lastModified is not null
        defaultSTIncomeExpensesShouldBeFound("lastModified.specified=true");

        // Get all the sTIncomeExpensesList where lastModified is null
        defaultSTIncomeExpensesShouldNotBeFound("lastModified.specified=false");
    }

    @Test
    @Transactional
    void getAllSTIncomeExpensesByLastModifiedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);

        // Get all the sTIncomeExpensesList where lastModified is greater than or equal to DEFAULT_LAST_MODIFIED
        defaultSTIncomeExpensesShouldBeFound("lastModified.greaterThanOrEqual=" + DEFAULT_LAST_MODIFIED);

        // Get all the sTIncomeExpensesList where lastModified is greater than or equal to UPDATED_LAST_MODIFIED
        defaultSTIncomeExpensesShouldNotBeFound("lastModified.greaterThanOrEqual=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSTIncomeExpensesByLastModifiedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);

        // Get all the sTIncomeExpensesList where lastModified is less than or equal to DEFAULT_LAST_MODIFIED
        defaultSTIncomeExpensesShouldBeFound("lastModified.lessThanOrEqual=" + DEFAULT_LAST_MODIFIED);

        // Get all the sTIncomeExpensesList where lastModified is less than or equal to SMALLER_LAST_MODIFIED
        defaultSTIncomeExpensesShouldNotBeFound("lastModified.lessThanOrEqual=" + SMALLER_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSTIncomeExpensesByLastModifiedIsLessThanSomething() throws Exception {
        // Initialize the database
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);

        // Get all the sTIncomeExpensesList where lastModified is less than DEFAULT_LAST_MODIFIED
        defaultSTIncomeExpensesShouldNotBeFound("lastModified.lessThan=" + DEFAULT_LAST_MODIFIED);

        // Get all the sTIncomeExpensesList where lastModified is less than UPDATED_LAST_MODIFIED
        defaultSTIncomeExpensesShouldBeFound("lastModified.lessThan=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSTIncomeExpensesByLastModifiedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);

        // Get all the sTIncomeExpensesList where lastModified is greater than DEFAULT_LAST_MODIFIED
        defaultSTIncomeExpensesShouldNotBeFound("lastModified.greaterThan=" + DEFAULT_LAST_MODIFIED);

        // Get all the sTIncomeExpensesList where lastModified is greater than SMALLER_LAST_MODIFIED
        defaultSTIncomeExpensesShouldBeFound("lastModified.greaterThan=" + SMALLER_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllSTIncomeExpensesByCancelDateIsEqualToSomething() throws Exception {
        // Initialize the database
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);

        // Get all the sTIncomeExpensesList where cancelDate equals to DEFAULT_CANCEL_DATE
        defaultSTIncomeExpensesShouldBeFound("cancelDate.equals=" + DEFAULT_CANCEL_DATE);

        // Get all the sTIncomeExpensesList where cancelDate equals to UPDATED_CANCEL_DATE
        defaultSTIncomeExpensesShouldNotBeFound("cancelDate.equals=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSTIncomeExpensesByCancelDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);

        // Get all the sTIncomeExpensesList where cancelDate not equals to DEFAULT_CANCEL_DATE
        defaultSTIncomeExpensesShouldNotBeFound("cancelDate.notEquals=" + DEFAULT_CANCEL_DATE);

        // Get all the sTIncomeExpensesList where cancelDate not equals to UPDATED_CANCEL_DATE
        defaultSTIncomeExpensesShouldBeFound("cancelDate.notEquals=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSTIncomeExpensesByCancelDateIsInShouldWork() throws Exception {
        // Initialize the database
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);

        // Get all the sTIncomeExpensesList where cancelDate in DEFAULT_CANCEL_DATE or UPDATED_CANCEL_DATE
        defaultSTIncomeExpensesShouldBeFound("cancelDate.in=" + DEFAULT_CANCEL_DATE + "," + UPDATED_CANCEL_DATE);

        // Get all the sTIncomeExpensesList where cancelDate equals to UPDATED_CANCEL_DATE
        defaultSTIncomeExpensesShouldNotBeFound("cancelDate.in=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSTIncomeExpensesByCancelDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);

        // Get all the sTIncomeExpensesList where cancelDate is not null
        defaultSTIncomeExpensesShouldBeFound("cancelDate.specified=true");

        // Get all the sTIncomeExpensesList where cancelDate is null
        defaultSTIncomeExpensesShouldNotBeFound("cancelDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSTIncomeExpensesByCancelDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);

        // Get all the sTIncomeExpensesList where cancelDate is greater than or equal to DEFAULT_CANCEL_DATE
        defaultSTIncomeExpensesShouldBeFound("cancelDate.greaterThanOrEqual=" + DEFAULT_CANCEL_DATE);

        // Get all the sTIncomeExpensesList where cancelDate is greater than or equal to UPDATED_CANCEL_DATE
        defaultSTIncomeExpensesShouldNotBeFound("cancelDate.greaterThanOrEqual=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSTIncomeExpensesByCancelDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);

        // Get all the sTIncomeExpensesList where cancelDate is less than or equal to DEFAULT_CANCEL_DATE
        defaultSTIncomeExpensesShouldBeFound("cancelDate.lessThanOrEqual=" + DEFAULT_CANCEL_DATE);

        // Get all the sTIncomeExpensesList where cancelDate is less than or equal to SMALLER_CANCEL_DATE
        defaultSTIncomeExpensesShouldNotBeFound("cancelDate.lessThanOrEqual=" + SMALLER_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSTIncomeExpensesByCancelDateIsLessThanSomething() throws Exception {
        // Initialize the database
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);

        // Get all the sTIncomeExpensesList where cancelDate is less than DEFAULT_CANCEL_DATE
        defaultSTIncomeExpensesShouldNotBeFound("cancelDate.lessThan=" + DEFAULT_CANCEL_DATE);

        // Get all the sTIncomeExpensesList where cancelDate is less than UPDATED_CANCEL_DATE
        defaultSTIncomeExpensesShouldBeFound("cancelDate.lessThan=" + UPDATED_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSTIncomeExpensesByCancelDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);

        // Get all the sTIncomeExpensesList where cancelDate is greater than DEFAULT_CANCEL_DATE
        defaultSTIncomeExpensesShouldNotBeFound("cancelDate.greaterThan=" + DEFAULT_CANCEL_DATE);

        // Get all the sTIncomeExpensesList where cancelDate is greater than SMALLER_CANCEL_DATE
        defaultSTIncomeExpensesShouldBeFound("cancelDate.greaterThan=" + SMALLER_CANCEL_DATE);
    }

    @Test
    @Transactional
    void getAllSTIncomeExpensesByTransactionTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);

        // Get all the sTIncomeExpensesList where transactionType equals to DEFAULT_TRANSACTION_TYPE
        defaultSTIncomeExpensesShouldBeFound("transactionType.equals=" + DEFAULT_TRANSACTION_TYPE);

        // Get all the sTIncomeExpensesList where transactionType equals to UPDATED_TRANSACTION_TYPE
        defaultSTIncomeExpensesShouldNotBeFound("transactionType.equals=" + UPDATED_TRANSACTION_TYPE);
    }

    @Test
    @Transactional
    void getAllSTIncomeExpensesByTransactionTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);

        // Get all the sTIncomeExpensesList where transactionType not equals to DEFAULT_TRANSACTION_TYPE
        defaultSTIncomeExpensesShouldNotBeFound("transactionType.notEquals=" + DEFAULT_TRANSACTION_TYPE);

        // Get all the sTIncomeExpensesList where transactionType not equals to UPDATED_TRANSACTION_TYPE
        defaultSTIncomeExpensesShouldBeFound("transactionType.notEquals=" + UPDATED_TRANSACTION_TYPE);
    }

    @Test
    @Transactional
    void getAllSTIncomeExpensesByTransactionTypeIsInShouldWork() throws Exception {
        // Initialize the database
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);

        // Get all the sTIncomeExpensesList where transactionType in DEFAULT_TRANSACTION_TYPE or UPDATED_TRANSACTION_TYPE
        defaultSTIncomeExpensesShouldBeFound("transactionType.in=" + DEFAULT_TRANSACTION_TYPE + "," + UPDATED_TRANSACTION_TYPE);

        // Get all the sTIncomeExpensesList where transactionType equals to UPDATED_TRANSACTION_TYPE
        defaultSTIncomeExpensesShouldNotBeFound("transactionType.in=" + UPDATED_TRANSACTION_TYPE);
    }

    @Test
    @Transactional
    void getAllSTIncomeExpensesByTransactionTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);

        // Get all the sTIncomeExpensesList where transactionType is not null
        defaultSTIncomeExpensesShouldBeFound("transactionType.specified=true");

        // Get all the sTIncomeExpensesList where transactionType is null
        defaultSTIncomeExpensesShouldNotBeFound("transactionType.specified=false");
    }

    @Test
    @Transactional
    void getAllSTIncomeExpensesByClassStudentIsEqualToSomething() throws Exception {
        // Initialize the database
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);
        ClassStudent classStudent;
        if (TestUtil.findAll(em, ClassStudent.class).isEmpty()) {
            classStudent = ClassStudentResourceIT.createEntity(em);
            em.persist(classStudent);
            em.flush();
        } else {
            classStudent = TestUtil.findAll(em, ClassStudent.class).get(0);
        }
        em.persist(classStudent);
        em.flush();
        sTIncomeExpenses.setClassStudent(classStudent);
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);
        Long classStudentId = classStudent.getId();

        // Get all the sTIncomeExpensesList where classStudent equals to classStudentId
        defaultSTIncomeExpensesShouldBeFound("classStudentId.equals=" + classStudentId);

        // Get all the sTIncomeExpensesList where classStudent equals to (classStudentId + 1)
        defaultSTIncomeExpensesShouldNotBeFound("classStudentId.equals=" + (classStudentId + 1));
    }

    @Test
    @Transactional
    void getAllSTIncomeExpensesByStRouteIsEqualToSomething() throws Exception {
        // Initialize the database
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);
        STRoute stRoute;
        if (TestUtil.findAll(em, STRoute.class).isEmpty()) {
            stRoute = STRouteResourceIT.createEntity(em);
            em.persist(stRoute);
            em.flush();
        } else {
            stRoute = TestUtil.findAll(em, STRoute.class).get(0);
        }
        em.persist(stRoute);
        em.flush();
        sTIncomeExpenses.setStRoute(stRoute);
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);
        Long stRouteId = stRoute.getId();

        // Get all the sTIncomeExpensesList where stRoute equals to stRouteId
        defaultSTIncomeExpensesShouldBeFound("stRouteId.equals=" + stRouteId);

        // Get all the sTIncomeExpensesList where stRoute equals to (stRouteId + 1)
        defaultSTIncomeExpensesShouldNotBeFound("stRouteId.equals=" + (stRouteId + 1));
    }

    @Test
    @Transactional
    void getAllSTIncomeExpensesByOperatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);
        Vendors operatedBy;
        if (TestUtil.findAll(em, Vendors.class).isEmpty()) {
            operatedBy = VendorsResourceIT.createEntity(em);
            em.persist(operatedBy);
            em.flush();
        } else {
            operatedBy = TestUtil.findAll(em, Vendors.class).get(0);
        }
        em.persist(operatedBy);
        em.flush();
        sTIncomeExpenses.setOperatedBy(operatedBy);
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);
        Long operatedById = operatedBy.getId();

        // Get all the sTIncomeExpensesList where operatedBy equals to operatedById
        defaultSTIncomeExpensesShouldBeFound("operatedById.equals=" + operatedById);

        // Get all the sTIncomeExpensesList where operatedBy equals to (operatedById + 1)
        defaultSTIncomeExpensesShouldNotBeFound("operatedById.equals=" + (operatedById + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSTIncomeExpensesShouldBeFound(String filter) throws Exception {
        restSTIncomeExpensesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sTIncomeExpenses.getId().intValue())))
            .andExpect(jsonPath("$.[*].amountPaid").value(hasItem(DEFAULT_AMOUNT_PAID.doubleValue())))
            .andExpect(jsonPath("$.[*].modeOfPay").value(hasItem(DEFAULT_MODE_OF_PAY.toString())))
            .andExpect(jsonPath("$.[*].noteNumbers").value(hasItem(DEFAULT_NOTE_NUMBERS)))
            .andExpect(jsonPath("$.[*].upiId").value(hasItem(DEFAULT_UPI_ID)))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS)))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].receiptId").value(hasItem(DEFAULT_RECEIPT_ID)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].cancelDate").value(hasItem(DEFAULT_CANCEL_DATE.toString())))
            .andExpect(jsonPath("$.[*].transactionType").value(hasItem(DEFAULT_TRANSACTION_TYPE.toString())));

        // Check, that the count call also returns 1
        restSTIncomeExpensesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSTIncomeExpensesShouldNotBeFound(String filter) throws Exception {
        restSTIncomeExpensesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSTIncomeExpensesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSTIncomeExpenses() throws Exception {
        // Get the sTIncomeExpenses
        restSTIncomeExpensesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSTIncomeExpenses() throws Exception {
        // Initialize the database
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);

        int databaseSizeBeforeUpdate = sTIncomeExpensesRepository.findAll().size();

        // Update the sTIncomeExpenses
        STIncomeExpenses updatedSTIncomeExpenses = sTIncomeExpensesRepository.findById(sTIncomeExpenses.getId()).get();
        // Disconnect from session so that the updates on updatedSTIncomeExpenses are not directly saved in db
        em.detach(updatedSTIncomeExpenses);
        updatedSTIncomeExpenses
            .amountPaid(UPDATED_AMOUNT_PAID)
            .modeOfPay(UPDATED_MODE_OF_PAY)
            .noteNumbers(UPDATED_NOTE_NUMBERS)
            .upiId(UPDATED_UPI_ID)
            .remarks(UPDATED_REMARKS)
            .paymentDate(UPDATED_PAYMENT_DATE)
            .receiptId(UPDATED_RECEIPT_ID)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE)
            .transactionType(UPDATED_TRANSACTION_TYPE);

        restSTIncomeExpensesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSTIncomeExpenses.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSTIncomeExpenses))
            )
            .andExpect(status().isOk());

        // Validate the STIncomeExpenses in the database
        List<STIncomeExpenses> sTIncomeExpensesList = sTIncomeExpensesRepository.findAll();
        assertThat(sTIncomeExpensesList).hasSize(databaseSizeBeforeUpdate);
        STIncomeExpenses testSTIncomeExpenses = sTIncomeExpensesList.get(sTIncomeExpensesList.size() - 1);
        assertThat(testSTIncomeExpenses.getAmountPaid()).isEqualTo(UPDATED_AMOUNT_PAID);
        assertThat(testSTIncomeExpenses.getModeOfPay()).isEqualTo(UPDATED_MODE_OF_PAY);
        assertThat(testSTIncomeExpenses.getNoteNumbers()).isEqualTo(UPDATED_NOTE_NUMBERS);
        assertThat(testSTIncomeExpenses.getUpiId()).isEqualTo(UPDATED_UPI_ID);
        assertThat(testSTIncomeExpenses.getRemarks()).isEqualTo(UPDATED_REMARKS);
        assertThat(testSTIncomeExpenses.getPaymentDate()).isEqualTo(UPDATED_PAYMENT_DATE);
        assertThat(testSTIncomeExpenses.getReceiptId()).isEqualTo(UPDATED_RECEIPT_ID);
        assertThat(testSTIncomeExpenses.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testSTIncomeExpenses.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testSTIncomeExpenses.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
        assertThat(testSTIncomeExpenses.getTransactionType()).isEqualTo(UPDATED_TRANSACTION_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingSTIncomeExpenses() throws Exception {
        int databaseSizeBeforeUpdate = sTIncomeExpensesRepository.findAll().size();
        sTIncomeExpenses.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSTIncomeExpensesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sTIncomeExpenses.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sTIncomeExpenses))
            )
            .andExpect(status().isBadRequest());

        // Validate the STIncomeExpenses in the database
        List<STIncomeExpenses> sTIncomeExpensesList = sTIncomeExpensesRepository.findAll();
        assertThat(sTIncomeExpensesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSTIncomeExpenses() throws Exception {
        int databaseSizeBeforeUpdate = sTIncomeExpensesRepository.findAll().size();
        sTIncomeExpenses.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSTIncomeExpensesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sTIncomeExpenses))
            )
            .andExpect(status().isBadRequest());

        // Validate the STIncomeExpenses in the database
        List<STIncomeExpenses> sTIncomeExpensesList = sTIncomeExpensesRepository.findAll();
        assertThat(sTIncomeExpensesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSTIncomeExpenses() throws Exception {
        int databaseSizeBeforeUpdate = sTIncomeExpensesRepository.findAll().size();
        sTIncomeExpenses.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSTIncomeExpensesMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sTIncomeExpenses))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the STIncomeExpenses in the database
        List<STIncomeExpenses> sTIncomeExpensesList = sTIncomeExpensesRepository.findAll();
        assertThat(sTIncomeExpensesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSTIncomeExpensesWithPatch() throws Exception {
        // Initialize the database
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);

        int databaseSizeBeforeUpdate = sTIncomeExpensesRepository.findAll().size();

        // Update the sTIncomeExpenses using partial update
        STIncomeExpenses partialUpdatedSTIncomeExpenses = new STIncomeExpenses();
        partialUpdatedSTIncomeExpenses.setId(sTIncomeExpenses.getId());

        partialUpdatedSTIncomeExpenses
            .amountPaid(UPDATED_AMOUNT_PAID)
            .modeOfPay(UPDATED_MODE_OF_PAY)
            .noteNumbers(UPDATED_NOTE_NUMBERS)
            .remarks(UPDATED_REMARKS)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .transactionType(UPDATED_TRANSACTION_TYPE);

        restSTIncomeExpensesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSTIncomeExpenses.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSTIncomeExpenses))
            )
            .andExpect(status().isOk());

        // Validate the STIncomeExpenses in the database
        List<STIncomeExpenses> sTIncomeExpensesList = sTIncomeExpensesRepository.findAll();
        assertThat(sTIncomeExpensesList).hasSize(databaseSizeBeforeUpdate);
        STIncomeExpenses testSTIncomeExpenses = sTIncomeExpensesList.get(sTIncomeExpensesList.size() - 1);
        assertThat(testSTIncomeExpenses.getAmountPaid()).isEqualTo(UPDATED_AMOUNT_PAID);
        assertThat(testSTIncomeExpenses.getModeOfPay()).isEqualTo(UPDATED_MODE_OF_PAY);
        assertThat(testSTIncomeExpenses.getNoteNumbers()).isEqualTo(UPDATED_NOTE_NUMBERS);
        assertThat(testSTIncomeExpenses.getUpiId()).isEqualTo(DEFAULT_UPI_ID);
        assertThat(testSTIncomeExpenses.getRemarks()).isEqualTo(UPDATED_REMARKS);
        assertThat(testSTIncomeExpenses.getPaymentDate()).isEqualTo(DEFAULT_PAYMENT_DATE);
        assertThat(testSTIncomeExpenses.getReceiptId()).isEqualTo(DEFAULT_RECEIPT_ID);
        assertThat(testSTIncomeExpenses.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testSTIncomeExpenses.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testSTIncomeExpenses.getCancelDate()).isEqualTo(DEFAULT_CANCEL_DATE);
        assertThat(testSTIncomeExpenses.getTransactionType()).isEqualTo(UPDATED_TRANSACTION_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateSTIncomeExpensesWithPatch() throws Exception {
        // Initialize the database
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);

        int databaseSizeBeforeUpdate = sTIncomeExpensesRepository.findAll().size();

        // Update the sTIncomeExpenses using partial update
        STIncomeExpenses partialUpdatedSTIncomeExpenses = new STIncomeExpenses();
        partialUpdatedSTIncomeExpenses.setId(sTIncomeExpenses.getId());

        partialUpdatedSTIncomeExpenses
            .amountPaid(UPDATED_AMOUNT_PAID)
            .modeOfPay(UPDATED_MODE_OF_PAY)
            .noteNumbers(UPDATED_NOTE_NUMBERS)
            .upiId(UPDATED_UPI_ID)
            .remarks(UPDATED_REMARKS)
            .paymentDate(UPDATED_PAYMENT_DATE)
            .receiptId(UPDATED_RECEIPT_ID)
            .createDate(UPDATED_CREATE_DATE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .cancelDate(UPDATED_CANCEL_DATE)
            .transactionType(UPDATED_TRANSACTION_TYPE);

        restSTIncomeExpensesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSTIncomeExpenses.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSTIncomeExpenses))
            )
            .andExpect(status().isOk());

        // Validate the STIncomeExpenses in the database
        List<STIncomeExpenses> sTIncomeExpensesList = sTIncomeExpensesRepository.findAll();
        assertThat(sTIncomeExpensesList).hasSize(databaseSizeBeforeUpdate);
        STIncomeExpenses testSTIncomeExpenses = sTIncomeExpensesList.get(sTIncomeExpensesList.size() - 1);
        assertThat(testSTIncomeExpenses.getAmountPaid()).isEqualTo(UPDATED_AMOUNT_PAID);
        assertThat(testSTIncomeExpenses.getModeOfPay()).isEqualTo(UPDATED_MODE_OF_PAY);
        assertThat(testSTIncomeExpenses.getNoteNumbers()).isEqualTo(UPDATED_NOTE_NUMBERS);
        assertThat(testSTIncomeExpenses.getUpiId()).isEqualTo(UPDATED_UPI_ID);
        assertThat(testSTIncomeExpenses.getRemarks()).isEqualTo(UPDATED_REMARKS);
        assertThat(testSTIncomeExpenses.getPaymentDate()).isEqualTo(UPDATED_PAYMENT_DATE);
        assertThat(testSTIncomeExpenses.getReceiptId()).isEqualTo(UPDATED_RECEIPT_ID);
        assertThat(testSTIncomeExpenses.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testSTIncomeExpenses.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testSTIncomeExpenses.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
        assertThat(testSTIncomeExpenses.getTransactionType()).isEqualTo(UPDATED_TRANSACTION_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingSTIncomeExpenses() throws Exception {
        int databaseSizeBeforeUpdate = sTIncomeExpensesRepository.findAll().size();
        sTIncomeExpenses.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSTIncomeExpensesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sTIncomeExpenses.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sTIncomeExpenses))
            )
            .andExpect(status().isBadRequest());

        // Validate the STIncomeExpenses in the database
        List<STIncomeExpenses> sTIncomeExpensesList = sTIncomeExpensesRepository.findAll();
        assertThat(sTIncomeExpensesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSTIncomeExpenses() throws Exception {
        int databaseSizeBeforeUpdate = sTIncomeExpensesRepository.findAll().size();
        sTIncomeExpenses.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSTIncomeExpensesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sTIncomeExpenses))
            )
            .andExpect(status().isBadRequest());

        // Validate the STIncomeExpenses in the database
        List<STIncomeExpenses> sTIncomeExpensesList = sTIncomeExpensesRepository.findAll();
        assertThat(sTIncomeExpensesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSTIncomeExpenses() throws Exception {
        int databaseSizeBeforeUpdate = sTIncomeExpensesRepository.findAll().size();
        sTIncomeExpenses.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSTIncomeExpensesMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sTIncomeExpenses))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the STIncomeExpenses in the database
        List<STIncomeExpenses> sTIncomeExpensesList = sTIncomeExpensesRepository.findAll();
        assertThat(sTIncomeExpensesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSTIncomeExpenses() throws Exception {
        // Initialize the database
        sTIncomeExpensesRepository.saveAndFlush(sTIncomeExpenses);

        int databaseSizeBeforeDelete = sTIncomeExpensesRepository.findAll().size();

        // Delete the sTIncomeExpenses
        restSTIncomeExpensesMockMvc
            .perform(delete(ENTITY_API_URL_ID, sTIncomeExpenses.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<STIncomeExpenses> sTIncomeExpensesList = sTIncomeExpensesRepository.findAll();
        assertThat(sTIncomeExpensesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
