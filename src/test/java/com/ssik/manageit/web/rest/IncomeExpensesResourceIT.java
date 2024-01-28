package com.ssik.manageit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ssik.manageit.IntegrationTest;
import com.ssik.manageit.domain.IncomeExpenses;
import com.ssik.manageit.domain.enumeration.ModeOfPayment;
import com.ssik.manageit.domain.enumeration.TransactionType;
import com.ssik.manageit.repository.IncomeExpensesRepository;
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
 * Integration tests for the {@link IncomeExpensesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class IncomeExpensesResourceIT {

    private static final Double DEFAULT_AMOUNT_PAID = 1D;
    private static final Double UPDATED_AMOUNT_PAID = 2D;

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

    private static final String DEFAULT_RECEIPT_ID = "AAAAAAAAAA";
    private static final String UPDATED_RECEIPT_ID = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_LAST_MODIFIED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_MODIFIED = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_CANCEL_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CANCEL_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final TransactionType DEFAULT_TRANSACTION_TYPE = TransactionType.EXPENSE;
    private static final TransactionType UPDATED_TRANSACTION_TYPE = TransactionType.INCOME;

    private static final String ENTITY_API_URL = "/api/income-expenses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private IncomeExpensesRepository incomeExpensesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIncomeExpensesMockMvc;

    private IncomeExpenses incomeExpenses;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IncomeExpenses createEntity(EntityManager em) {
        IncomeExpenses incomeExpenses = new IncomeExpenses()
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
        return incomeExpenses;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IncomeExpenses createUpdatedEntity(EntityManager em) {
        IncomeExpenses incomeExpenses = new IncomeExpenses()
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
        return incomeExpenses;
    }

    @BeforeEach
    public void initTest() {
        incomeExpenses = createEntity(em);
    }

    @Test
    @Transactional
    void createIncomeExpenses() throws Exception {
        int databaseSizeBeforeCreate = incomeExpensesRepository.findAll().size();
        // Create the IncomeExpenses
        restIncomeExpensesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(incomeExpenses))
            )
            .andExpect(status().isCreated());

        // Validate the IncomeExpenses in the database
        List<IncomeExpenses> incomeExpensesList = incomeExpensesRepository.findAll();
        assertThat(incomeExpensesList).hasSize(databaseSizeBeforeCreate + 1);
        IncomeExpenses testIncomeExpenses = incomeExpensesList.get(incomeExpensesList.size() - 1);
        assertThat(testIncomeExpenses.getAmountPaid()).isEqualTo(DEFAULT_AMOUNT_PAID);
        assertThat(testIncomeExpenses.getModeOfPay()).isEqualTo(DEFAULT_MODE_OF_PAY);
        assertThat(testIncomeExpenses.getNoteNumbers()).isEqualTo(DEFAULT_NOTE_NUMBERS);
        assertThat(testIncomeExpenses.getUpiId()).isEqualTo(DEFAULT_UPI_ID);
        assertThat(testIncomeExpenses.getRemarks()).isEqualTo(DEFAULT_REMARKS);
        assertThat(testIncomeExpenses.getPaymentDate()).isEqualTo(DEFAULT_PAYMENT_DATE);
        assertThat(testIncomeExpenses.getReceiptId()).isEqualTo(DEFAULT_RECEIPT_ID);
        assertThat(testIncomeExpenses.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testIncomeExpenses.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
        assertThat(testIncomeExpenses.getCancelDate()).isEqualTo(DEFAULT_CANCEL_DATE);
        assertThat(testIncomeExpenses.getTransactionType()).isEqualTo(DEFAULT_TRANSACTION_TYPE);
    }

    @Test
    @Transactional
    void createIncomeExpensesWithExistingId() throws Exception {
        // Create the IncomeExpenses with an existing ID
        incomeExpenses.setId(1L);

        int databaseSizeBeforeCreate = incomeExpensesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restIncomeExpensesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(incomeExpenses))
            )
            .andExpect(status().isBadRequest());

        // Validate the IncomeExpenses in the database
        List<IncomeExpenses> incomeExpensesList = incomeExpensesRepository.findAll();
        assertThat(incomeExpensesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAmountPaidIsRequired() throws Exception {
        int databaseSizeBeforeTest = incomeExpensesRepository.findAll().size();
        // set the field null
        incomeExpenses.setAmountPaid(null);

        // Create the IncomeExpenses, which fails.

        restIncomeExpensesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(incomeExpenses))
            )
            .andExpect(status().isBadRequest());

        List<IncomeExpenses> incomeExpensesList = incomeExpensesRepository.findAll();
        assertThat(incomeExpensesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllIncomeExpenses() throws Exception {
        // Initialize the database
        incomeExpensesRepository.saveAndFlush(incomeExpenses);

        // Get all the incomeExpensesList
        restIncomeExpensesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(incomeExpenses.getId().intValue())))
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
    void getIncomeExpenses() throws Exception {
        // Initialize the database
        incomeExpensesRepository.saveAndFlush(incomeExpenses);

        // Get the incomeExpenses
        restIncomeExpensesMockMvc
            .perform(get(ENTITY_API_URL_ID, incomeExpenses.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(incomeExpenses.getId().intValue()))
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
    void getNonExistingIncomeExpenses() throws Exception {
        // Get the incomeExpenses
        restIncomeExpensesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewIncomeExpenses() throws Exception {
        // Initialize the database
        incomeExpensesRepository.saveAndFlush(incomeExpenses);

        int databaseSizeBeforeUpdate = incomeExpensesRepository.findAll().size();

        // Update the incomeExpenses
        IncomeExpenses updatedIncomeExpenses = incomeExpensesRepository.findById(incomeExpenses.getId()).get();
        // Disconnect from session so that the updates on updatedIncomeExpenses are not directly saved in db
        em.detach(updatedIncomeExpenses);
        updatedIncomeExpenses
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

        restIncomeExpensesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedIncomeExpenses.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedIncomeExpenses))
            )
            .andExpect(status().isOk());

        // Validate the IncomeExpenses in the database
        List<IncomeExpenses> incomeExpensesList = incomeExpensesRepository.findAll();
        assertThat(incomeExpensesList).hasSize(databaseSizeBeforeUpdate);
        IncomeExpenses testIncomeExpenses = incomeExpensesList.get(incomeExpensesList.size() - 1);
        assertThat(testIncomeExpenses.getAmountPaid()).isEqualTo(UPDATED_AMOUNT_PAID);
        assertThat(testIncomeExpenses.getModeOfPay()).isEqualTo(UPDATED_MODE_OF_PAY);
        assertThat(testIncomeExpenses.getNoteNumbers()).isEqualTo(UPDATED_NOTE_NUMBERS);
        assertThat(testIncomeExpenses.getUpiId()).isEqualTo(UPDATED_UPI_ID);
        assertThat(testIncomeExpenses.getRemarks()).isEqualTo(UPDATED_REMARKS);
        assertThat(testIncomeExpenses.getPaymentDate()).isEqualTo(UPDATED_PAYMENT_DATE);
        assertThat(testIncomeExpenses.getReceiptId()).isEqualTo(UPDATED_RECEIPT_ID);
        assertThat(testIncomeExpenses.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testIncomeExpenses.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testIncomeExpenses.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
        assertThat(testIncomeExpenses.getTransactionType()).isEqualTo(UPDATED_TRANSACTION_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingIncomeExpenses() throws Exception {
        int databaseSizeBeforeUpdate = incomeExpensesRepository.findAll().size();
        incomeExpenses.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIncomeExpensesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, incomeExpenses.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(incomeExpenses))
            )
            .andExpect(status().isBadRequest());

        // Validate the IncomeExpenses in the database
        List<IncomeExpenses> incomeExpensesList = incomeExpensesRepository.findAll();
        assertThat(incomeExpensesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIncomeExpenses() throws Exception {
        int databaseSizeBeforeUpdate = incomeExpensesRepository.findAll().size();
        incomeExpenses.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIncomeExpensesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(incomeExpenses))
            )
            .andExpect(status().isBadRequest());

        // Validate the IncomeExpenses in the database
        List<IncomeExpenses> incomeExpensesList = incomeExpensesRepository.findAll();
        assertThat(incomeExpensesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIncomeExpenses() throws Exception {
        int databaseSizeBeforeUpdate = incomeExpensesRepository.findAll().size();
        incomeExpenses.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIncomeExpensesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(incomeExpenses)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the IncomeExpenses in the database
        List<IncomeExpenses> incomeExpensesList = incomeExpensesRepository.findAll();
        assertThat(incomeExpensesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateIncomeExpensesWithPatch() throws Exception {
        // Initialize the database
        incomeExpensesRepository.saveAndFlush(incomeExpenses);

        int databaseSizeBeforeUpdate = incomeExpensesRepository.findAll().size();

        // Update the incomeExpenses using partial update
        IncomeExpenses partialUpdatedIncomeExpenses = new IncomeExpenses();
        partialUpdatedIncomeExpenses.setId(incomeExpenses.getId());

        partialUpdatedIncomeExpenses.noteNumbers(UPDATED_NOTE_NUMBERS).upiId(UPDATED_UPI_ID).lastModified(UPDATED_LAST_MODIFIED);

        restIncomeExpensesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIncomeExpenses.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIncomeExpenses))
            )
            .andExpect(status().isOk());

        // Validate the IncomeExpenses in the database
        List<IncomeExpenses> incomeExpensesList = incomeExpensesRepository.findAll();
        assertThat(incomeExpensesList).hasSize(databaseSizeBeforeUpdate);
        IncomeExpenses testIncomeExpenses = incomeExpensesList.get(incomeExpensesList.size() - 1);
        assertThat(testIncomeExpenses.getAmountPaid()).isEqualTo(DEFAULT_AMOUNT_PAID);
        assertThat(testIncomeExpenses.getModeOfPay()).isEqualTo(DEFAULT_MODE_OF_PAY);
        assertThat(testIncomeExpenses.getNoteNumbers()).isEqualTo(UPDATED_NOTE_NUMBERS);
        assertThat(testIncomeExpenses.getUpiId()).isEqualTo(UPDATED_UPI_ID);
        assertThat(testIncomeExpenses.getRemarks()).isEqualTo(DEFAULT_REMARKS);
        assertThat(testIncomeExpenses.getPaymentDate()).isEqualTo(DEFAULT_PAYMENT_DATE);
        assertThat(testIncomeExpenses.getReceiptId()).isEqualTo(DEFAULT_RECEIPT_ID);
        assertThat(testIncomeExpenses.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testIncomeExpenses.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testIncomeExpenses.getCancelDate()).isEqualTo(DEFAULT_CANCEL_DATE);
        assertThat(testIncomeExpenses.getTransactionType()).isEqualTo(DEFAULT_TRANSACTION_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateIncomeExpensesWithPatch() throws Exception {
        // Initialize the database
        incomeExpensesRepository.saveAndFlush(incomeExpenses);

        int databaseSizeBeforeUpdate = incomeExpensesRepository.findAll().size();

        // Update the incomeExpenses using partial update
        IncomeExpenses partialUpdatedIncomeExpenses = new IncomeExpenses();
        partialUpdatedIncomeExpenses.setId(incomeExpenses.getId());

        partialUpdatedIncomeExpenses
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

        restIncomeExpensesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIncomeExpenses.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIncomeExpenses))
            )
            .andExpect(status().isOk());

        // Validate the IncomeExpenses in the database
        List<IncomeExpenses> incomeExpensesList = incomeExpensesRepository.findAll();
        assertThat(incomeExpensesList).hasSize(databaseSizeBeforeUpdate);
        IncomeExpenses testIncomeExpenses = incomeExpensesList.get(incomeExpensesList.size() - 1);
        assertThat(testIncomeExpenses.getAmountPaid()).isEqualTo(UPDATED_AMOUNT_PAID);
        assertThat(testIncomeExpenses.getModeOfPay()).isEqualTo(UPDATED_MODE_OF_PAY);
        assertThat(testIncomeExpenses.getNoteNumbers()).isEqualTo(UPDATED_NOTE_NUMBERS);
        assertThat(testIncomeExpenses.getUpiId()).isEqualTo(UPDATED_UPI_ID);
        assertThat(testIncomeExpenses.getRemarks()).isEqualTo(UPDATED_REMARKS);
        assertThat(testIncomeExpenses.getPaymentDate()).isEqualTo(UPDATED_PAYMENT_DATE);
        assertThat(testIncomeExpenses.getReceiptId()).isEqualTo(UPDATED_RECEIPT_ID);
        assertThat(testIncomeExpenses.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testIncomeExpenses.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testIncomeExpenses.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
        assertThat(testIncomeExpenses.getTransactionType()).isEqualTo(UPDATED_TRANSACTION_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingIncomeExpenses() throws Exception {
        int databaseSizeBeforeUpdate = incomeExpensesRepository.findAll().size();
        incomeExpenses.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIncomeExpensesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, incomeExpenses.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(incomeExpenses))
            )
            .andExpect(status().isBadRequest());

        // Validate the IncomeExpenses in the database
        List<IncomeExpenses> incomeExpensesList = incomeExpensesRepository.findAll();
        assertThat(incomeExpensesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIncomeExpenses() throws Exception {
        int databaseSizeBeforeUpdate = incomeExpensesRepository.findAll().size();
        incomeExpenses.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIncomeExpensesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(incomeExpenses))
            )
            .andExpect(status().isBadRequest());

        // Validate the IncomeExpenses in the database
        List<IncomeExpenses> incomeExpensesList = incomeExpensesRepository.findAll();
        assertThat(incomeExpensesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIncomeExpenses() throws Exception {
        int databaseSizeBeforeUpdate = incomeExpensesRepository.findAll().size();
        incomeExpenses.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIncomeExpensesMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(incomeExpenses))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the IncomeExpenses in the database
        List<IncomeExpenses> incomeExpensesList = incomeExpensesRepository.findAll();
        assertThat(incomeExpensesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIncomeExpenses() throws Exception {
        // Initialize the database
        incomeExpensesRepository.saveAndFlush(incomeExpenses);

        int databaseSizeBeforeDelete = incomeExpensesRepository.findAll().size();

        // Delete the incomeExpenses
        restIncomeExpensesMockMvc
            .perform(delete(ENTITY_API_URL_ID, incomeExpenses.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<IncomeExpenses> incomeExpensesList = incomeExpensesRepository.findAll();
        assertThat(incomeExpensesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
