package com.ssik.manageit.service;

import com.ssik.manageit.domain.IncomeExpenses;
import com.ssik.manageit.repository.IncomeExpensesRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link IncomeExpenses}.
 */
@Service
@Transactional
public class IncomeExpensesService {

    private final Logger log = LoggerFactory.getLogger(IncomeExpensesService.class);

    private final IncomeExpensesRepository incomeExpensesRepository;

    public IncomeExpensesService(IncomeExpensesRepository incomeExpensesRepository) {
        this.incomeExpensesRepository = incomeExpensesRepository;
    }

    /**
     * Save a incomeExpenses.
     *
     * @param incomeExpenses the entity to save.
     * @return the persisted entity.
     */
    public IncomeExpenses save(IncomeExpenses incomeExpenses) {
        log.debug("Request to save IncomeExpenses : {}", incomeExpenses);
        return incomeExpensesRepository.save(incomeExpenses);
    }

    /**
     * Partially update a incomeExpenses.
     *
     * @param incomeExpenses the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<IncomeExpenses> partialUpdate(IncomeExpenses incomeExpenses) {
        log.debug("Request to partially update IncomeExpenses : {}", incomeExpenses);

        return incomeExpensesRepository
            .findById(incomeExpenses.getId())
            .map(existingIncomeExpenses -> {
                if (incomeExpenses.getAmountPaid() != null) {
                    existingIncomeExpenses.setAmountPaid(incomeExpenses.getAmountPaid());
                }
                if (incomeExpenses.getModeOfPay() != null) {
                    existingIncomeExpenses.setModeOfPay(incomeExpenses.getModeOfPay());
                }
                if (incomeExpenses.getNoteNumbers() != null) {
                    existingIncomeExpenses.setNoteNumbers(incomeExpenses.getNoteNumbers());
                }
                if (incomeExpenses.getUpiId() != null) {
                    existingIncomeExpenses.setUpiId(incomeExpenses.getUpiId());
                }
                if (incomeExpenses.getRemarks() != null) {
                    existingIncomeExpenses.setRemarks(incomeExpenses.getRemarks());
                }
                if (incomeExpenses.getPaymentDate() != null) {
                    existingIncomeExpenses.setPaymentDate(incomeExpenses.getPaymentDate());
                }
                if (incomeExpenses.getReceiptId() != null) {
                    existingIncomeExpenses.setReceiptId(incomeExpenses.getReceiptId());
                }
                if (incomeExpenses.getCreateDate() != null) {
                    existingIncomeExpenses.setCreateDate(incomeExpenses.getCreateDate());
                }
                if (incomeExpenses.getLastModified() != null) {
                    existingIncomeExpenses.setLastModified(incomeExpenses.getLastModified());
                }
                if (incomeExpenses.getCancelDate() != null) {
                    existingIncomeExpenses.setCancelDate(incomeExpenses.getCancelDate());
                }
                if (incomeExpenses.getTransactionType() != null) {
                    existingIncomeExpenses.setTransactionType(incomeExpenses.getTransactionType());
                }

                return existingIncomeExpenses;
            })
            .map(incomeExpensesRepository::save);
    }

    /**
     * Get all the incomeExpenses.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<IncomeExpenses> findAll() {
        log.debug("Request to get all IncomeExpenses");
        return incomeExpensesRepository.findAll();
    }

    /**
     * Get one incomeExpenses by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<IncomeExpenses> findOne(Long id) {
        log.debug("Request to get IncomeExpenses : {}", id);
        return incomeExpensesRepository.findById(id);
    }

    /**
     * Delete the incomeExpenses by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete IncomeExpenses : {}", id);
        incomeExpensesRepository.deleteById(id);
    }
}
