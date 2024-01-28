package com.ssik.manageit.service;

import com.ssik.manageit.domain.STIncomeExpenses;
import com.ssik.manageit.repository.STIncomeExpensesRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link STIncomeExpenses}.
 */
@Service
@Transactional
public class STIncomeExpensesService {

    private final Logger log = LoggerFactory.getLogger(STIncomeExpensesService.class);

    private final STIncomeExpensesRepository sTIncomeExpensesRepository;

    public STIncomeExpensesService(STIncomeExpensesRepository sTIncomeExpensesRepository) {
        this.sTIncomeExpensesRepository = sTIncomeExpensesRepository;
    }

    /**
     * Save a sTIncomeExpenses.
     *
     * @param sTIncomeExpenses the entity to save.
     * @return the persisted entity.
     */
    public STIncomeExpenses save(STIncomeExpenses sTIncomeExpenses) {
        log.debug("Request to save STIncomeExpenses : {}", sTIncomeExpenses);
        return sTIncomeExpensesRepository.save(sTIncomeExpenses);
    }

    /**
     * Partially update a sTIncomeExpenses.
     *
     * @param sTIncomeExpenses the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<STIncomeExpenses> partialUpdate(STIncomeExpenses sTIncomeExpenses) {
        log.debug("Request to partially update STIncomeExpenses : {}", sTIncomeExpenses);

        return sTIncomeExpensesRepository
            .findById(sTIncomeExpenses.getId())
            .map(existingSTIncomeExpenses -> {
                if (sTIncomeExpenses.getAmountPaid() != null) {
                    existingSTIncomeExpenses.setAmountPaid(sTIncomeExpenses.getAmountPaid());
                }
                if (sTIncomeExpenses.getModeOfPay() != null) {
                    existingSTIncomeExpenses.setModeOfPay(sTIncomeExpenses.getModeOfPay());
                }
                if (sTIncomeExpenses.getNoteNumbers() != null) {
                    existingSTIncomeExpenses.setNoteNumbers(sTIncomeExpenses.getNoteNumbers());
                }
                if (sTIncomeExpenses.getUpiId() != null) {
                    existingSTIncomeExpenses.setUpiId(sTIncomeExpenses.getUpiId());
                }
                if (sTIncomeExpenses.getRemarks() != null) {
                    existingSTIncomeExpenses.setRemarks(sTIncomeExpenses.getRemarks());
                }
                if (sTIncomeExpenses.getPaymentDate() != null) {
                    existingSTIncomeExpenses.setPaymentDate(sTIncomeExpenses.getPaymentDate());
                }
                if (sTIncomeExpenses.getReceiptId() != null) {
                    existingSTIncomeExpenses.setReceiptId(sTIncomeExpenses.getReceiptId());
                }
                if (sTIncomeExpenses.getCreateDate() != null) {
                    existingSTIncomeExpenses.setCreateDate(sTIncomeExpenses.getCreateDate());
                }
                if (sTIncomeExpenses.getLastModified() != null) {
                    existingSTIncomeExpenses.setLastModified(sTIncomeExpenses.getLastModified());
                }
                if (sTIncomeExpenses.getCancelDate() != null) {
                    existingSTIncomeExpenses.setCancelDate(sTIncomeExpenses.getCancelDate());
                }
                if (sTIncomeExpenses.getTransactionType() != null) {
                    existingSTIncomeExpenses.setTransactionType(sTIncomeExpenses.getTransactionType());
                }

                return existingSTIncomeExpenses;
            })
            .map(sTIncomeExpensesRepository::save);
    }

    /**
     * Get all the sTIncomeExpenses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<STIncomeExpenses> findAll(Pageable pageable) {
        log.debug("Request to get all STIncomeExpenses");
        return sTIncomeExpensesRepository.findAll(pageable);
    }

    /**
     * Get one sTIncomeExpenses by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<STIncomeExpenses> findOne(Long id) {
        log.debug("Request to get STIncomeExpenses : {}", id);
        return sTIncomeExpensesRepository.findById(id);
    }

    /**
     * Delete the sTIncomeExpenses by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete STIncomeExpenses : {}", id);
        sTIncomeExpensesRepository.deleteById(id);
    }
}
